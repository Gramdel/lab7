package commands;

import collection.Organization;
import collection.Product;
import collection.UnitOfMeasure;
import core.DBUnit;

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
    public synchronized String execute(LinkedHashSet<Product> collection, ArrayList<Organization> organizations, Date date, Stack<String> history, DBUnit dbUnit) {
        try {
            Product product = collection.stream().filter(x -> x.getUnitOfMeasure().equals(unitOfMeasure)).findAny().get();
            collection.remove(product);
            if (dbUnit.removeProductFromDB(product)) {
                if (collection.stream().filter(x -> x.getManufacturer().equals(product.getManufacturer())).count() == 1) {
                    organizations.remove(product.getManufacturer());
                }
                return "Один из элементов с unitOfMeasure " + unitOfMeasure + " успешно удалён!";
            } else {
                collection.add(product);
                return "При удалении элемента с unitOfMeasure " + unitOfMeasure + " произошла ошибка SQL!";
            }
        } catch (NoSuchElementException e) {
            return "Удаление невозможно, так как в коллекции нет элементов с unitOfMeasure " + unitOfMeasure + "!";
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
