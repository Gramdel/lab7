package commands;

import collection.Organization;
import collection.Product;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import core.Creator;
import core.DBUnit;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Update extends Command {
    private Product product;
    private Long id;

    @Override
    public boolean prepare(String arg, boolean isInteractive, HashMap<String, Command> commands) {
        Product product = null;
        try {
            if (isInteractive) {
                if (!arg.matches("\\s*\\d+\\s*")) {
                    throw new IllegalArgumentException("У команды update быть 1 аргумент - положительное целое число!");
                } else {
                    Matcher m = Pattern.compile("\\d+").matcher(arg);
                    if (m.find()) {
                        try {
                            id = Long.parseLong(m.group());
                        } catch (NumberFormatException e) {
                            throw new IllegalArgumentException("У команды update быть 1 аргумент - положительное целое число!");
                        }
                    }
                }
            } else {
                if (!arg.matches("\\s*\\d+\\s+\\{.*}\\s*")) {
                    throw new IllegalArgumentException("У команды update должно быть 2 аргумента: положительное целое число и JSON-строка!");
                } else {
                    Matcher m = Pattern.compile("\\{.*}").matcher(arg);
                    if (m.find()) {
                        product = new Gson().fromJson(m.group(), Product.class);
                    }
                    m = Pattern.compile("\\d+").matcher(arg);
                    if (m.find()) {
                        try {
                            id = Long.parseLong(m.group());
                        } catch (NumberFormatException e) {
                            throw new IllegalArgumentException("У команды update должно быть 2 аргумента: положительное целое число и JSON-строка!");
                        }
                    }
                }
            }
            product = Creator.createProduct(product, isInteractive);
            if (product == null) {
                System.out.println("Команда update не выполнена!");
                return false;
            }
        } catch (JsonSyntaxException | NumberFormatException e) {
            System.out.println("Ошибка в синтаксисе JSON-строки!");
            return false;
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return false;
        }
        this.product = product;
        return true;
    }

    @Override
    public synchronized String execute(LinkedHashSet<Product> collection, ArrayList<Organization> organizations, Date date, Stack<String> history, DBUnit dbUnit) {
        if (collection.stream().noneMatch(x -> x.getId().equals(id))) {
            return "Нечего обновлять: элемента с id " + id + " нет в коллекции!";
        } else {
            product.setId(id);
            if (dbUnit.updateProductInDB(product)) {
                Product product = collection.stream().filter(x -> x.getId().equals(id)).findAny().get();
                if (!product.getManufacturer().equals(this.product.getManufacturer())) {
                    if (collection.stream().filter(x -> x.getManufacturer().equals(product.getManufacturer())).count() == 1) {
                        organizations.remove(product.getManufacturer());
                    }
                    Optional<Organization> optional = organizations.stream().filter(x -> x.equals(this.product.getManufacturer())).findAny();
                    if (optional.isPresent()) {
                        this.product.setManufacturer(optional.get());
                    } else {
                        this.product.getManufacturer().createId(organizations);
                        organizations.add(product.getManufacturer());
                    }
                }
                collection.remove(product);
                collection.add(this.product);
                return "Элемент c id " + id + " успешно обновлён!";
            } else {
                return "При обновлении элемента с id " + id + " возникла ошибка SQL!";
            }
        }
    }

    @Override
    public String description() {
        return "Обновляет значение элемента коллекции, id которого равен заданному." + syntax();
    }

    @Override
    public String syntax() {
        return " Синтаксис: update id, где id - целое положительное число. \n\t\t(В скриптах - update id {element}, где {element} - JSON-строка)";
    }
}
