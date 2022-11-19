package io.github.dipo33.gtmapaddon.command;

import io.github.dipo33.gtmapaddon.GeneralUtils;
import io.github.dipo33.gtmapaddon.command.factory.CommandFactory;
import io.github.dipo33.gtmapaddon.command.factory.argument.ArgumentList;
import io.github.dipo33.gtmapaddon.command.factory.exception.CommandException;
import io.github.dipo33.gtmapaddon.command.factory.exception.CommandInvalidUsageException;
import io.github.dipo33.gtmapaddon.command.factory.subcommand.SubCommandFactory;
import io.github.dipo33.gtmapaddon.data.entity.License;
import io.github.dipo33.gtmapaddon.data.entity.License.Category;
import io.github.dipo33.gtmapaddon.storage.DataCache;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;

public class LicenseCommand {

    public static final ICommand COMMAND = CommandFactory.createCommand()
            .onlyPlayerUse()
            .addSubCommand(SubCommandFactory
                    .createSubCommand("license")
                    .addSubCommand(SubCommandFactory
                            .createSubCommand("create")
                            .addEnumArgument("category", Category.class).build()
                            .addStringArgument("name").build()
                            .addIntArgument("price").build()
                            .build(LicenseCommand::commandCreateLicense)
                    ).addSubCommand(SubCommandFactory
                            .createSubCommand("remove")
                            .addEnumArgument("category", Category.class).build()
                            .addStringArgument("name").build()
                            .build(null)
                    ).addSubCommand(SubCommandFactory
                            .createSubCommand("purchase")
                            .addEnumArgument("category", Category.class).build()
                            .addStringArgument("name").build()
                            .addStringArgument("pinCode").setExpectedLength(4).build()
                            .build(null)
                    ).addSubCommand(SubCommandFactory
                            .createSubCommand("revoke")
                            .addEnumArgument("category", Category.class).build()
                            .addStringArgument("name").build()
                            .addPlayerArgument("player").setDefaultFactory(CommandBase::getCommandSenderAsPlayer).build()
                            .build(null)
                    ).build()
            );

    private static void commandCreateLicense(ArgumentList arguments, ICommandSender sender) throws CommandException {
        Category category = arguments.getEnum(0);
        String name = arguments.getString(1);
        int price = arguments.getInt(2);

        License license = new License(name, category, price);
        if (!DataCache.LICENSE_LIST.addLicense(license)) {
            throw new CommandInvalidUsageException("licenseAlreadyExists", category.getName(), name);
        }

        GeneralUtils.sendFormattedTranslation(sender, "dipogtmapaddon.command.licenseCreated",
                category.getName(), name, price
        );
    }
}
