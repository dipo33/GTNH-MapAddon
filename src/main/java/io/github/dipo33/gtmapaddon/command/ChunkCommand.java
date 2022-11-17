package io.github.dipo33.gtmapaddon.command;

import com.sinthoras.visualprospecting.Utils;
import io.github.dipo33.gtmapaddon.GTMapAddonMod;
import io.github.dipo33.gtmapaddon.GeneralUtils;
import io.github.dipo33.gtmapaddon.command.factory.CommandFactory;
import io.github.dipo33.gtmapaddon.command.factory.argument.ArgumentList;
import io.github.dipo33.gtmapaddon.command.factory.exception.CommandException;
import io.github.dipo33.gtmapaddon.command.factory.exception.CommandProcessException;
import io.github.dipo33.gtmapaddon.command.factory.subcommand.SubCommandFactory;
import io.github.dipo33.gtmapaddon.compat.MoneyModWrapper;
import io.github.dipo33.gtmapaddon.data.entity.MinedChunk;
import io.github.dipo33.gtmapaddon.data.entity.OwnedChunk;
import io.github.dipo33.gtmapaddon.network.AddMinedChunkMessage;
import io.github.dipo33.gtmapaddon.network.BoughtChunkMessage;
import io.github.dipo33.gtmapaddon.storage.DataCache;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChunkCoordinates;
import net.minecraftforge.common.DimensionManager;
import sk.dipo.money.item.MoneyItems;

public class ChunkCommand {

    public static final ICommand command = CommandFactory.createCommand()
                .onlyPlayerUse()
                .addSubCommand(SubCommandFactory
                            .createSubCommand("chunk")
                            .addSubCommand(SubCommandFactory
                                        .createSubCommand("buy")
                                        .addEnumArgument("type", OwnedChunk.Status.class).build()
                                        .addStringArgument("pinCode").setExpectedLength(4).build()
                                        .build(ChunkCommand::buyCommand)
                            ).addSubCommand(SubCommandFactory
                                        .createSubCommand("mark-as-mined")
                                        .addOfflinePlayerArgument("mined-by")
                                                    .setDefaultFactory(ICommandSender::getCommandSenderName)
                                                    .build()
                                        .build(ChunkCommand::markAsMined)
                            ).addSubCommand(SubCommandFactory
                                        .createSubCommand("price")
                                        .addSubCommand(SubCommandFactory
                                                    .createSubCommand("get")
                                                    .addIntArgument("dimensionId")
                                                                .setDefaultFactory(sender -> sender.getEntityWorld().provider.dimensionId)
                                                                .build()
                                                    .build(ChunkCommand::getPrices)
                                        ).addSubCommand(SubCommandFactory
                                                    .createSubCommand("set")
                                                    .addDipoPriceArg("price").build()
                                                    .addEnumArgument("type", OwnedChunk.Status.class).build()
                                                    .addIntArgument("dimensionId")
                                                                .setDefaultFactory(sender -> sender.getEntityWorld().provider.dimensionId)
                                                                .build()
                                                    .build(ChunkCommand::setPrice)
                                        ).build()
                            ).build()
                );

    private static void buyCommand(ArgumentList arguments, ICommandSender sender) throws CommandException {
        final OwnedChunk.Status status = arguments.getEnum(0);
        final String pinCode = arguments.getString(1);

        final ChunkCoordinates coordinates = sender.getPlayerCoordinates();
        final int chunkX = Utils.coordBlockToChunk(coordinates.posX);
        final int chunkZ = Utils.coordBlockToChunk(coordinates.posZ);
        final int dimensionId = sender.getEntityWorld().provider.dimensionId;
        final String owner = sender.getCommandSenderName();
        final int price = DataCache.PRIZE_LIST.getPrice(dimensionId, status);

        final String priceString = MoneyModWrapper.priceToString(price);
        final EntityPlayerMP player = CommandBase.getCommandSenderAsPlayer(sender);
        final ItemStack creditCard = player.getHeldItem();

        if (creditCard == null || creditCard.getItem() != MoneyItems.creditCard) {
            throw new CommandProcessException("useCreditCardNotHeld");
        }

        MoneyModWrapper.chargeMoney(player.getHeldItem(), pinCode, price);

        final OwnedChunk ownedChunk = new OwnedChunk(chunkX, chunkZ, dimensionId, owner, status);
        DataCache.OWNED_CHUNKS_STORAGE.put(dimensionId, chunkX, chunkZ, ownedChunk);
        GTMapAddonMod.NETWORK_CHANNEL.sendToAll(new BoughtChunkMessage(ownedChunk));
        DataCache.OWNED_CHUNKS_SERIALIZER.save();

        GeneralUtils.sendFormattedText(sender, "dipogtmapaddon.command.chunkBought",
                status.name().toLowerCase(), chunkX, chunkZ, priceString
        );
    }

    private static void markAsMined(ArgumentList arguments, ICommandSender sender) {
        final ChunkCoordinates coordinates = sender.getPlayerCoordinates();
        final int chunkX = Utils.coordBlockToChunk(coordinates.posX);
        final int chunkZ = Utils.coordBlockToChunk(coordinates.posZ);
        final int dimensionId = sender.getEntityWorld().provider.dimensionId;
        final String minedBy = arguments.getOfflinePlayer(0);

        final MinedChunk minedChunk = new MinedChunk(chunkX, chunkZ, dimensionId, minedBy);
        DataCache.MINED_CHUNKS_STORAGE.put(dimensionId, chunkX, chunkZ, minedChunk);
        GTMapAddonMod.NETWORK_CHANNEL.sendToAll(new AddMinedChunkMessage(minedChunk));
        DataCache.MINED_CHUNKS_SERIALIZER.save();

        GeneralUtils.sendFormattedText(sender, "dipogtmapaddon.command.chunkMarked", chunkX, chunkZ, minedBy);
    }

    public static void setPrice(ArgumentList argumentList, ICommandSender sender) {
        final int price = argumentList.getInt(0);
        final OwnedChunk.Status status = argumentList.getEnum(1);
        final int dimensionId = argumentList.getInt(2);
        final String dimensionName = DimensionManager.getWorld(dimensionId).provider.getDimensionName();

        DataCache.PRIZE_LIST.setPrice(dimensionId, status, price);

        GeneralUtils.sendFormattedText(sender, "dipogtmapaddon.command.priceSet",
                status.name().toLowerCase(), dimensionName, MoneyModWrapper.priceToString(price)
        );
    }

    public static void getPrices(ArgumentList argumentList, ICommandSender sender) {
        final int dimensionId = argumentList.getInt(0);
        final String dimensionName = DimensionManager.getWorld(dimensionId).provider.getDimensionName();
        GeneralUtils.sendFormattedText(sender, "dipogtmapaddon.command.pricesGet", dimensionName);

        for (OwnedChunk.Status status : OwnedChunk.Status.values()) {
            final int price = DataCache.PRIZE_LIST.getPrice(dimensionId, status);
            final String priceString = price < 0 ? "???" : MoneyModWrapper.priceToString(price);
            GeneralUtils.sendFormattedText(sender, "dipogtmapaddon.command.pricesGetType",
                    priceString, status.name().toLowerCase()
            );
        }
    }
}
