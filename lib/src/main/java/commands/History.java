package commands;

import collection.Organization;
import collection.Product;

import java.util.*;

public class History extends Command {
    @Override
    public boolean prepare(String arg, boolean isInteractive, HashMap<String, Command> commands) {
        if (!arg.matches("\\s*")) {
            System.out.println("У команды history не может быть аргументов!");
            return false;
        }
        return true;
    }

    @Override
    public synchronized String execute(LinkedHashSet<Product> collection, ArrayList<Organization> organizations, Date date, Stack<String> history) {
        if (history.size() > 0) {
            StringBuilder s = new StringBuilder();
            history.forEach(x -> s.append("\n\t").append(x));
            return "Комманда history выполнена, последние 7 комманд:" + s.toString();
        } else {
            return "Список выполненных комманд пуст!";
        }
    }

    @Override
    public String description() {
        return "Выводит последние 7 комманд." + syntax();
    }

    @Override
    public String syntax() {
        return " Синтаксис: history";
    }
}
