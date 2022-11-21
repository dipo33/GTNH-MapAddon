package io.github.dipo33.gtmapaddon.command.factory.exception;

import java.util.LinkedList;
import java.util.List;

public abstract class CommandException extends Exception {

    private final List<String> processedArgs;

    public CommandException(String message) {
        super(message);
        this.processedArgs = new LinkedList<>();
    }

    public void insertArg(String arg) {
        processedArgs.add(0, arg);
    }

    public String[] getProcessedArgs() {
        return processedArgs.subList(1, processedArgs.size()).toArray(new String[0]);
    }
}
