package io.github.dipo33.gtmapaddon.command.factory;

public interface WithSubCommands {

    WithSubCommands addSubCommand(SubCommand subCommand);

    SubCommand build();
}
