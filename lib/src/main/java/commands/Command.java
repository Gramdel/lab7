package commands;

import collection.Organization;
import collection.Product;
import core.DBUnit;

import java.io.Serializable;
import java.util.*;

public abstract class Command implements Serializable {
    public abstract boolean prepare(String arg, boolean isInteractive, HashMap<String, Command> commands);

    public String execute(LinkedHashSet<Product> collection, ArrayList<Organization> organizations, Date date, Stack<String> history, DBUnit dbUnit) {
        return "Команда успешно выполнена!";
    }

    public abstract String description();

    public abstract String syntax();
}
