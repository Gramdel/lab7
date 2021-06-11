package commands;

import collection.Organization;
import collection.Product;
import collection.UnitOfMeasure;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RemoveByUOM extends Command {
    private UnitOfMeasure unitOfMeasure;

    @Override
    public boolean prepare(String arg, boolean isInteractive, HashMap<String, Command> commands) {
        try {
            if (!arg.matches("\\s*[^\\s]+\\s*")) {
                throw new IllegalArgumentException();
            } else {
                Matcher m = Pattern.compile("[^\\s]+").matcher(arg);
                if (m.find()) {
                    unitOfMeasure = UnitOfMeasure.fromString(m.group());
                }
            }
        } catch (IllegalArgumentException e) {
            System.out.println("У команды remove_any_by_unit_of_measure должен быть 1 аргумент из следующего списка: " + UnitOfMeasure.valueList() + ".");
            return false;
        }
        return true;
    }

    @Override
    public synchronized String execute(LinkedHashSet<Product> collection, ArrayList<Organization> organizations, Date date, Stack<String> history) {
        try {
            Product product = collection.stream().filter(x -> x.getUnitOfMeasure().equals(unitOfMeasure)).findAny().get();
            collection.remove(product);
            return "Один из элементов с unitOfMeasure " + unitOfMeasure + " успешно удалён!";
        } catch (NoSuchElementException e) {
            return "Удаление невозможно, так как в коллекции нет элемента с unitOfMeasure " + unitOfMeasure + ".";
        }
    }

    @Override
    public String description() {
        return "Удаляет из коллекции один из элементов с unitOfMeasure эквивалентным заданному." + syntax();
    }

    @Override
    public String syntax() {
        return " Синтаксис: remove_any_by_unit_of_measure unitOfMeasure, где unitOfMeasure: " + UnitOfMeasure.valueList() + ".";
    }
}
