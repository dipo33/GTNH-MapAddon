package io.github.dipo33.gtmapaddon.command;

import com.sinthoras.visualprospecting.Utils;
import io.github.dipo33.gtmapaddon.GTMapAddonMod;
import io.github.dipo33.gtmapaddon.GeneralUtils;
import io.github.dipo33.gtmapaddon.command.factory.argument.ArgumentList;
import io.github.dipo33.gtmapaddon.command.factory.exception.CommandException;
import io.github.dipo33.gtmapaddon.command.factory.exception.CommandProcessException;
import io.github.dipo33.gtmapaddon.compat.MoneyModWrapper;
import io.github.dipo33.gtmapaddon.data.entity.OwnedChunk;
import io.github.dipo33.gtmapaddon.network.BoughtChunkMessage;
import io.github.dipo33.gtmapaddon.storage.DataCache;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChunkCoordinates;
import net.minecraftforge.common.DimensionManager;
import sk.dipo.money.item.MoneyItems;

public class ChunkBuyCommand {
    private static int getPrice(int dimensionId, int chunkX, int chunkZ, OwnedChunk.Status status) throws CommandException {
        final String dimensionName = DimensionManager.getWorld(dimensionId).provider.getDimensionName();
        final int price = DataCache.PRIZE_LIST.getPrice(dimensionId, status);
        if (price < 0) {
            throw new CommandProcessException("chunkPriceNotSet", status.getName(), chunkX, chunkZ, dimensionName);
        }

        return price;
    }

    private static ItemStack getCreditCard(ICommandSender sender) throws CommandException {
        final ItemStack creditCard = CommandBase.getCommandSenderAsPlayer(sender).getHeldItem();
        if (creditCard == null || creditCard.getItem() != MoneyItems.creditCard) {
            throw new CommandProcessException("useCreditCardNotHeld");
        }

        return creditCard;
    }

    private static int checkUpgradePrice(ICommandSender sender, OwnedChunk ownedChunk, OwnedChunk previouslyOwnedChunk, int price) throws CommandException {
        final String dimensionName = DimensionManager.getWorld(ownedChunk.getDimensionId()).provider.getDimensionName();

        if (previouslyOwnedChunk != null) {
            if (!previouslyOwnedChunk.getOwner().equalsIgnoreCase(sender.getCommandSenderName())) {
                throw new CommandProcessException("chunkAlreadyOwnedBy", ownedChunk.getStatus().getName(),
                        ownedChunk.getChunkX(), ownedChunk.getChunkZ(), dimensionName, previouslyOwnedChunk.getOwner()
                );
            }

            int paidPrice = DataCache.PRIZE_LIST.getPrice(ownedChunk.getDimensionId(), previouslyOwnedChunk.getStatus());
            price -= paidPrice;
            if (price <= 0) {
                throw new CommandProcessException("chunkAlreadyOwned", ownedChunk.getStatus().getName(),
                        ownedChunk.getChunkX(), ownedChunk.getChunkZ(), dimensionName
                );
            }
        }

        return price;
    }

    public static void buyCommand(ArgumentList arguments, ICommandSender sender) throws CommandException {
        final OwnedChunk.Status status = arguments.getEnum(0);
        final String pinCode = arguments.getString(1);

        final ChunkCoordinates coordinates = sender.getPlayerCoordinates();
        final int chunkX = Utils.coordBlockToChunk(coordinates.posX);
        final int chunkZ = Utils.coordBlockToChunk(coordinates.posZ);
        final int dimensionId = sender.getEntityWorld().provider.dimensionId;
        final String owner = sender.getCommandSenderName();

        final ItemStack creditCard = getCreditCard(sender);
        final int price = getPrice(dimensionId, chunkX, chunkZ, status);

        final OwnedChunk ownedChunk = new OwnedChunk(chunkX, chunkZ, dimensionId, owner, status);
        final OwnedChunk previouslyOwnedChunk = DataCache.OWNED_CHUNKS_STORAGE.get(dimensionId, chunkX, chunkZ);
        final int newPrice = checkUpgradePrice(sender, ownedChunk, previouslyOwnedChunk, price);

        final String priceString = MoneyModWrapper.priceToString(newPrice);
        MoneyModWrapper.chargeMoney(creditCard, pinCode, newPrice);
        DataCache.OWNED_CHUNKS_STORAGE.put(dimensionId, chunkX, chunkZ, ownedChunk);
        GTMapAddonMod.NETWORK_CHANNEL.sendToAll(new BoughtChunkMessage(ownedChunk));
        DataCache.OWNED_CHUNKS_SERIALIZER.save();

        final String dimensionName = DimensionManager.getWorld(dimensionId).provider.getDimensionName();
        if (price != newPrice) {
            GeneralUtils.sendFormattedTranslation(sender, "dipogtmapaddon.command.chunkUpgraded",
                    previouslyOwnedChunk.getStatus().getName(), status.getName(), chunkX, chunkZ, dimensionName, priceString
            );
        } else {
            GeneralUtils.sendFormattedTranslation(sender, "dipogtmapaddon.command.chunkBought",
                    status.getName(), chunkX, chunkZ, dimensionName, priceString
            );
        }
    }
}
