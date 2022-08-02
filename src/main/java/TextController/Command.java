package TextController;

import java.util.Arrays;

public class Command {
    private final String[] args;
    private final CommandType commandType;

    Command(String line){
        if (!line.startsWith("/")) throw new CommandException();
        String[] split = line.split("\\s");
        commandType = CommandType.toCommandType(split[0].substring(1));
        args = Arrays.copyOfRange(split, 1, split.length);
    }

    public CommandType getCommandType() {return commandType;}
    public String[] getArgs() {return args;}
}
