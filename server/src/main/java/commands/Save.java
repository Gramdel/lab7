package commands;

import collection.Organization;
import collection.Product;
import core.CSVUnit;

import java.util.*;

public class Save extends Command {
    @Override
    public synchronized String execute(LinkedHashSet<Product> collection, ArrayList<Organization> organizations, Date date, Stack<String> history) {
        return CSVUnit.write();
    }

    @Override
    public boolean prepare(String arg, boolean isInteractive, HashMap<String, Command> commands) {
        if (!arg.matches("\\s*")) {
            System.out.println("У команды save не может быть аргументов!");
            return false;
        }
        return true;
    }

    @Override
    public String description() {
        return "Сохраняет коллекцию." + syntax();
    }

    @Override
    public String syntax() {
        return " Синтаксис: save";
    }
}

