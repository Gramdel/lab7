package commands;

import collection.Organization;
import collection.Product;

import java.text.SimpleDateFormat;
import java.util.*;

public class Info extends Command {
    @Override
    public boolean prepare(String arg, boolean isInteractive, HashMap<String, Command> commands) {
        if (!arg.matches("\\s*")) {
            System.out.println("У команды info не может быть аргументов!");
            return false;
        }
        return true;
    }

    @Override
    public synchronized String execute(LinkedHashSet<Product> collection, ArrayList<Organization> organizations, Date date, Stack<String> history) {
        return "Тип коллекции:\n" + collection.getClass() + "\n" + "Дата инициализации коллекции:\n" + new SimpleDateFormat("dd.MM.yyyy HH:mm").format(date) + "\n" + "Количество элементов коллекции:\n" + collection.size();
    }

    @Override
    public String description() {
        return "Выводит информацию о коллекции." + syntax();
    }

    @Override
    public String syntax() {
        return " Синтаксис: info";
    }
}
