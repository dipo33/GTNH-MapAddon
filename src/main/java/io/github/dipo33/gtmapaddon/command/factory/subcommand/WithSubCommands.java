package io.github.dipo33.gtmapaddon.command.factory.subcommand;

public interface WithSubCommands {

    WithSubCommands addSubCommand(SubCommand subCommand);

    SubCommand build();
}
