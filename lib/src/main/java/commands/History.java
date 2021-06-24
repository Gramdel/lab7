package commands;

import collection.Organization;
import collection.Product;
import core.DBUnit;
import core.Interpreter;

import java.util.*;

public class History extends Command {
    private Stack<String> history;

    @Override
    public boolean prepare(String arg, boolean isInteractive, Interpreter interpreter) {
        if (!arg.matches("\\s*")) {
            System.out.println("У команды history не может быть аргументов!");
            return false;
        }
        history = interpreter.getHistory();
        return true;
    }

    @Override
    public synchronized String execute(LinkedHashSet<Product> collection, ArrayList<Organization> organizations, Date date, DBUnit dbUnit) {
        if (history.size() > 0) {
            StringBuilder s = new StringBuilder();
            history.forEach(x -> s.append("\n\t").append(x));
            return "Комманда history выполнена, последние 7 комманд:" + s;
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
