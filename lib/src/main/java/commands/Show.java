package commands;

import collection.Organization;
import collection.Product;

import java.util.*;

public class Show extends Command {
    @Override
    public boolean prepare(String arg, boolean isInteractive, HashMap<String, Command> commands) {
        if (!arg.matches("\\s*")) {
            System.out.println("У команды show не может быть аргументов!");
            return false;
        }
        return true;
    }

    @Override
    public synchronized String execute(LinkedHashSet<Product> collection, ArrayList<Organization> organizations, Date date, Stack<String> history) {
        if (collection.size() > 0) {
            StringBuilder msg = new StringBuilder();
            collection.stream().sorted(Product.byIdComparator).forEach(p -> msg.append("\n").append(p));
            return "Элементы коллекции:" + msg.toString();
        } else {
            return "Коллекция пуста!";
        }
    }

    @Override
    public String description() {
        return "Выводит коллекцию." + syntax();
    }

    @Override
    public String syntax() {
        return " Синтаксис: show";
    }
}
