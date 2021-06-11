package commands;

import collection.Organization;
import collection.Product;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RemoveById extends Command {
    private Long id;

    @Override
    public boolean prepare(String arg, boolean isInteractive, HashMap<String, Command> commands) {
        try {
            if (!arg.matches("\\s*[^\\s]+\\s*")) {
                throw new NumberFormatException();
            } else {
                Matcher m = Pattern.compile("[^\\s]+").matcher(arg);
                if (m.find()) {
                    id = Long.parseLong(m.group());
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("У команды remove_by_id должен быть 1 аргумент - положительное целое число!");
            return false;
        }
        return true;
    }

    @Override
    public synchronized String execute(LinkedHashSet<Product> collection, ArrayList<Organization> organizations, Date date, Stack<String> history) {
        try {
            Product product = collection.stream().filter(x -> x.getId().equals(id)).findFirst().get();
            if (collection.stream().filter(x -> x.getManufacturer().equals(product.getManufacturer())).count() == 1) {
                organizations.remove(product.getManufacturer());
            }
            collection.remove(product);
            return "Элемент с id " + id + " успешно удалён!";
        } catch (NoSuchElementException e) {
            return "Удаление невозможно, так как в коллекции нет элемента с id " + id + ".";
        }
    }

    @Override
    public String description() {
        return "Удаляет элемент из коллекции по его id." + syntax();
    }

    @Override
    public String syntax() {
        return " Синтаксис: remove_by_id id, где id - целое положительное число.";
    }
}
