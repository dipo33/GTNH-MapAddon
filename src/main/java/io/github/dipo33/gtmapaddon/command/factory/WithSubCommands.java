package io.github.dipo33.gtmapaddon.command.factory;

public interface WithSubCommands {
    
    WithSubCommands addSubCommand(AbstractSubCommand subCommand);
    
    AbstractSubCommand build();
}
