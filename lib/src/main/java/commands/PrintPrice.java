package commands;

import collection.Organization;
import collection.Product;
import core.DBUnit;

import java.util.*;

public class PrintPrice extends Command {
    @Override
    public boolean prepare(String arg, boolean isInteractive, HashMap<String, Command> commands) {
        if (!arg.matches("\\s*")) {
            System.out.println("У команды print_field_descending_price не может быть аргументов!");
            return false;
        }
        return true;
    }

    @Override
    public synchronized String execute(LinkedHashSet<Product> collection, ArrayList<Organization> organizations, Date date, Stack<String> history, DBUnit dbUnit) {
        if (collection.size() > 0) {
            StringBuilder s = new StringBuilder();
            collection.stream().sorted(Product.byPriceComparator.reversed()).forEach(product -> s.append("\n\t").append(product.getPrice()));
            return "Цены продуктов в коллекции в порядке убывания:" + s;
        } else {
            return "Невозможно вывести цены продуктов, потому что коллекция пуста!";
        }
    }

    @Override
    public String description() {
        return "Выводит цены продуктов в коллекции в порядке убывания." + syntax();
    }

    @Override
    public String syntax() {
        return " Синтаксис: print_field_descending_price";
    }
}
