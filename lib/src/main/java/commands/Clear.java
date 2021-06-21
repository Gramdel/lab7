package commands;

import collection.Organization;
import collection.Product;
import core.DBUnit;

import java.util.*;

public class Clear extends Command {
    @Override
    public boolean prepare(String arg, boolean isInteractive, HashMap<String, Command> commands) {
        if (!arg.matches("\\s*")) {
            System.out.println("У команды clear не может быть аргументов!");
            return false;
        }
        return true;
    }

    @Override
    public synchronized String execute(LinkedHashSet<Product> collection, ArrayList<Organization> organizations, Date date, Stack<String> history, DBUnit dbUnit) {
        if (collection.size() > 0) {
            collection.clear();
            return "Коллекция очищена.";
        }
        return "Коллекция пуста, нечего чистить!";
    }

    @Override
    public String description() {
        return "Очищает коллекцию." + syntax();
    }

    @Override
    public String syntax() {
        return " Синтаксис: clear";
    }
}
