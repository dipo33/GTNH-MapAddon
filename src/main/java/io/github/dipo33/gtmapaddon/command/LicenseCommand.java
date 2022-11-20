package io.github.dipo33.gtmapaddon.command;

import io.github.dipo33.gtmapaddon.GeneralUtils;
import io.github.dipo33.gtmapaddon.command.factory.CommandFactory;
import io.github.dipo33.gtmapaddon.command.factory.argument.ArgumentList;
import io.github.dipo33.gtmapaddon.command.factory.exception.CommandException;
import io.github.dipo33.gtmapaddon.command.factory.exception.CommandProcessException;
import io.github.dipo33.gtmapaddon.command.factory.subcommand.SubCommandFactory;
import io.github.dipo33.gtmapaddon.compat.MoneyModWrapper;
import io.github.dipo33.gtmapaddon.data.entity.License;
import io.github.dipo33.gtmapaddon.data.entity.License.Category;
import io.github.dipo33.gtmapaddon.storage.DataCache;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import sk.dipo.money.item.MoneyItems;

import java.util.Collection;
import java.util.UUID;

public class LicenseCommand {

    public static final ICommand COMMAND = CommandFactory.createCommand()
            .onlyPlayerUse()
            .addSubCommand(SubCommandFactory
                    .createSubCommand("license")
                    .addSubCommand(SubCommandFactory
                            .createSubCommand("create")
                            .addEnumArgument("category", Category.class).build()
                            .addStringArgument("name").build()
                            .addDipoPriceArg("price").build()
                            .build(LicenseCommand::commandCreateLicense)
                    ).addSubCommand(SubCommandFactory
                            .createSubCommand("remove")
                            .addEnumArgument("category", Category.class).build()
                            .addStringArgument("name").build()
                            .build(LicenseCommand::commandRemoveLicense)
                    ).addSubCommand(SubCommandFactory
                            .createSubCommand("purchase")
                            .addEnumArgument("category", Category.class).build()
                            .addStringArgument("name").build()
                            .addStringArgument("pinCode").setExpectedLength(4).build()
                            .build(LicenseCommand::commandPurchaseLicense)
                    ).addSubCommand(SubCommandFactory
                            .createSubCommand("revoke")
                            .addEnumArgument("category", Category.class).build()
                            .addStringArgument("name").build()
                            .addPlayerArgument("player").setDefaultFactory(CommandBase::getCommandSenderAsPlayer).build()
                            .build(LicenseCommand::commandRevokeLicense)
                    ).addSubCommand(SubCommandFactory
                            .createSubCommand("list")
                            .addSubCommand(SubCommandFactory
                                    .createSubCommand("all")
                                    .addEnumArgument("category", Category.class).build()
                                    .build(LicenseCommand::commandListAllLicenses)
                            ).addSubCommand(SubCommandFactory
                                    .createSubCommand("owned")
                                    .addEnumArgument("category", Category.class).build()
                                    .build(LicenseCommand::commandListOwnedLicenses)
                            ).addSubCommand(SubCommandFactory
                                    .createSubCommand("available")
                                    .addEnumArgument("category", Category.class).build()
                                    .build(LicenseCommand::commandListAvailableLicenses)
                            ).build()
                    ).build()
            );

    private static void commandCreateLicense(ArgumentList arguments, ICommandSender sender) throws CommandException {
        Category category = arguments.getEnum(0);
        String name = arguments.getString(1);
        int price = arguments.getInt(2);
        String priceString = MoneyModWrapper.priceToString(price);

        License license = new License(name, category, price);
        if (DataCache.LICENSE_LIST.addLicense(license) != null) {
            throw new CommandProcessException("licenseAlreadyExists", category.getName(), license.getDisplayName());
        }

        GeneralUtils.sendCommandMessage(sender, "licenseCreated", category.getName(), license.getDisplayName(), priceString);
    }

    private static void commandRemoveLicense(ArgumentList arguments, ICommandSender sender) throws CommandException {
        Category category = arguments.getEnum(0);
        String name = arguments.getString(1);

        License license = DataCache.LICENSE_LIST.getLicense(category, name);
        if (license == null) {
            throw new CommandProcessException("licenseDoesNotExist", category.getName(), License.toDisplayName(name));
        }
        
        if (DataCache.LICENSE_LIST.hasLicense(license)) {
            throw new CommandProcessException("licenseStillInUse", category.getName(), license.getDisplayName());
        }
        
        DataCache.LICENSE_LIST.removeLicense(category, name);
        GeneralUtils.sendCommandMessage(sender, "licenseRemoved", category.getName(), license.getDisplayName());
    }

    private static void commandPurchaseLicense(ArgumentList arguments, ICommandSender sender) throws CommandException {
        UUID playerUUID = CommandBase.getCommandSenderAsPlayer(sender).getUniqueID();
        Category category = arguments.getEnum(0);
        String name = arguments.getString(1);
        String pinCode = arguments.getString(2);

        License license = DataCache.LICENSE_LIST.getLicense(category, name);
        if (license == null) {
            throw new CommandProcessException("licenseDoesNotExist", category.getName(), License.toDisplayName(name));
        }

        if (DataCache.LICENSE_LIST.hasLicense(license, playerUUID)) {
            throw new CommandProcessException("licenseAlreadyPurchased", category.getName(), license.getDisplayName());
        }

        ItemStack creditCard = CommandBase.getCommandSenderAsPlayer(sender).getHeldItem();
        if (creditCard == null || creditCard.getItem() != MoneyItems.creditCard) {
            throw new CommandProcessException("useCreditCardNotHeld");
        }
        
        int price = license.getPrice();
        String priceString = MoneyModWrapper.priceToString(price);
        MoneyModWrapper.chargeMoney(creditCard, pinCode, license.getPrice());
        DataCache.LICENSE_LIST.assignLicense(license, playerUUID);
        GeneralUtils.sendCommandMessage(sender, "licensePurchased", category.getName(), license.getDisplayName(), priceString);
    }
    
    private static void commandRevokeLicense(ArgumentList arguments, ICommandSender sender) throws CommandException {
        Category category = arguments.getEnum(0);
        String name = arguments.getString(1);
        EntityPlayerMP player = arguments.getPlayer(2);

        License license = DataCache.LICENSE_LIST.getLicense(category, name);
        if (license == null) {
            throw new CommandProcessException("licenseDoesNotExist", category.getName(), License.toDisplayName(name));
        }

        if (!DataCache.LICENSE_LIST.hasLicense(license, player.getUniqueID())) {
            throw new CommandProcessException("licenseNotPurchased", category.getName(), license.getDisplayName());
        }

        DataCache.LICENSE_LIST.revokeLicense(license, player.getUniqueID());
        GeneralUtils.sendCommandMessage(sender, "licenseRevoked", category.getName(), license.getDisplayName(), player.getDisplayName());
    }
    
    private static void commandListAllLicenses(ArgumentList arguments, ICommandSender sender) {
        Category category = arguments.getEnum(0);
        
        GeneralUtils.sendCommandMessage(sender, "licenseListAll", category.getName());
        for (License license : DataCache.LICENSE_LIST.getAllLicenses(category)) {
            String priceString = MoneyModWrapper.priceToString(license.getPrice());
            GeneralUtils.sendCommandMessage(sender, "licenseListEntry", license.getDisplayName(), priceString);
        }
    }
    
    private static void commandListOwnedLicenses(ArgumentList arguments, ICommandSender sender) {
        Category category = arguments.getEnum(0);
        UUID playerUUID = CommandBase.getCommandSenderAsPlayer(sender).getUniqueID();
        
        GeneralUtils.sendCommandMessage(sender, "licenseListOwned", category.getName());
        for (License license : DataCache.LICENSE_LIST.getAllPlayerLicenses(category, playerUUID)) {
            String priceString = MoneyModWrapper.priceToString(license.getPrice());
            GeneralUtils.sendCommandMessage(sender, "licenseListEntry", license.getDisplayName(), priceString);
        }
    }
    
    private static void commandListAvailableLicenses(ArgumentList arguments, ICommandSender sender) {
        Category category = arguments.getEnum(0);
        UUID playerUUID = CommandBase.getCommandSenderAsPlayer(sender).getUniqueID();
        Collection<License> playerLicenses = DataCache.LICENSE_LIST.getAllPlayerLicenses(category, playerUUID);
        
        GeneralUtils.sendCommandMessage(sender, "licenseListAvailable", category.getName());
        for (License license : DataCache.LICENSE_LIST.getAllLicenses(category)) {
            if (!playerLicenses.contains(license)) {
                String priceString = MoneyModWrapper.priceToString(license.getPrice());
                GeneralUtils.sendCommandMessage(sender, "licenseListEntry", license.getDisplayName(), priceString);
            }
        }
    }
}
