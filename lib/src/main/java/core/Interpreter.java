package core;

import commands.*;

import java.io.InputStream;
import java.util.*;

public abstract class Interpreter {
    protected final HashMap<String, Command> commands = new HashMap<>();
    protected final Stack<String> history = new Stack<>();

    {
        commands.put("add", new Add());
        commands.put("clear", new Clear());
        commands.put("exit", new Exit());
        commands.put("history", new History());
        commands.put("help", new Help());
        commands.put("execute_script", new ExecuteScript());
        commands.put("show", new Show());
        commands.put("remove_by_id", new RemoveById());
        commands.put("update", new Update());
        commands.put("info", new Info());
        commands.put("remove_any_by_unit_of_measure", new RemoveByUOM());
        commands.put("filter_by_manufacturer", new FilterByManufacturer());
        commands.put("add_if_max", new AddIfMax());
        commands.put("print_field_descending_price", new PrintPrice());
        commands.put("remove_greater", new RemoveGreater());
    }

    public abstract void fromStream(InputStream stream, boolean isInteractive);

    protected void addToHistory(String com) {
        history.add(com);
        if (history.size() > 7) history.removeElementAt(0);
    }

    public Stack<String> getHistory() {
        return history;
    }

    public HashMap<String, Command> getCommands() {
        return commands;
    }
}