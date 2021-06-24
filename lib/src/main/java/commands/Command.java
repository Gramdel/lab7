package commands;

import collection.Organization;
import collection.Product;
import core.DBUnit;
import core.Interpreter;

import java.io.Serializable;
import java.util.*;

public abstract class Command implements Serializable {
    public abstract boolean prepare(String arg, boolean isInteractive, Interpreter interpreter);

    public String execute(LinkedHashSet<Product> collection, ArrayList<Organization> organizations, Date date, DBUnit dbUnit) {
        return "Команда успешно выполнена!";
    }

    public abstract String description();

    public abstract String syntax();
}
