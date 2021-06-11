package commands;

import collection.Organization;
import collection.Product;

import java.util.*;

public class Help extends Command {
    private HashMap<String, Command> commands;

    @Override
    public boolean prepare(String arg, boolean isInteractive, HashMap<String, Command> commands) {
        if (!arg.matches("\\s*")) {
            System.out.println("У команды help не может быть аргументов!");
            return false;
        }
        this.commands = commands;
        return true;
    }

    @Override
    public synchronized String execute(LinkedHashSet<Product> collection, ArrayList<Organization> organizations, Date date, Stack<String> history) {
        StringBuilder s = new StringBuilder();
        commands.forEach((commandName, command) -> s.append("\n\t").append(commandName).append(" - ").append(command.description()));
        return "Список допустимых команд:" + s.toString();
    }

    @Override
    public String description() {
        return "Выводит справку по доступным коммандам." + syntax();
    }

    @Override
    public String syntax() {
        return " Синтаксис: help";
    }
}
