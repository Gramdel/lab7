package core;

import collection.Organization;
import collection.Product;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import commands.Creator;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;

import static core.Main.*;

public class CSVUnit {
    private static Product product;
    public static void read(){
        if (getArgs().length > 0) {
            System.out.println("Пытаемся загрузить коллекцию из файла \""+getArgs()[0]+"\"...");
            if (!Files.exists(Paths.get(getArgs()[0]))) {
                System.out.println("Коллекция не загружена, так как файл коллекции не найден!");
            } else if (Files.isDirectory(Paths.get(getArgs()[0]))) {
                System.out.println("Коллекция не загружена, так как в качестве файла коллекци была передана директория!");
            } else if (!Files.isRegularFile(Paths.get(getArgs()[0]))) {
                System.out.println("Коллекция не загружена, так как в качестве файла коллекции передан специальный файл!");
            } else if (!Files.isReadable(Paths.get(getArgs()[0]))) {
                System.out.println("Коллекция не заполнена данными, так как у файла коллекции нет прав на чтение.");
            } else {
                try (BufferedReader in = new BufferedReader(new FileReader(getArgs()[0]))) {
                    String s;
                    int count = 1;
                    while ((s = in.readLine()) != null) {
                        if (s.matches("[^,]*(,[^,]*){11}")) {
                            String[] values = s.split("[,]", -1);
                            for (int i=0;i<values.length;i++) {
                                if (values[i].isEmpty()) {
                                    values[i] = null;
                                }
                            }
                            StringBuilder builder = new StringBuilder();
                            builder.append("{\"name\":\"").append(values[1]);
                            builder.append("\",\"coordinates\":{\"x\":").append(values[2]).append(",\"y\":").append(values[3]);
                            builder.append("},\"price\":").append(values[4]).append(",\"partNumber\":\"").append(values[5]);
                            builder.append("\",\"manufactureCost\":").append(values[6]).append(",\"unitOfMeasure\":\"").append(values[7]);
                            builder.append("\",\"manufacturer\":{\"name\":\"").append(values[8]).append("\",\"annualTurnover\":").append(values[9]);
                            builder.append(",\"employeesCount\":").append(values[10]).append(",\"type\":\"").append(values[11]).append("\"}}");
                            try {
                                product = new Gson().fromJson(builder.toString(), Product.class);
                                product = Creator.createProduct(product, false);
                                if (Long.parseLong(values[0]) <= 0) {
                                    System.out.println("Неправильный ввод id! Требуемый формат: положительное целое число.");
                                } else if (product != null) {
                                    product.setId(Long.parseLong(values[0]));
                                }
                                if (product == null || product.getId() == null) {
                                    System.out.println("Элемент со строки #"+count+" не добавлен из-за перечисленных выше ошибок формата данных!");
                                } else if (!getCollection().isEmpty() && getCollection().stream().anyMatch(x -> x.getId().equals(product.getId()))) {
                                    System.out.println("Элемент со строки #"+count+" не добавлен, т.к. в коллекции уже есть элемент с id "+product.getId()+"!");
                                } else {
                                    if (getOrganizations().contains(product.getManufacturer())) {
                                        for (Organization o : getOrganizations()) {
                                            if (o.equals(product.getManufacturer())) {
                                                product.setManufacturer(o);
                                                break;
                                            }
                                        }
                                    } else {
                                        product.getManufacturer().createId();
                                        getOrganizations().add(product.getManufacturer());
                                    }
                                    getCollection().add(product);
                                }
                            } catch (JsonSyntaxException | NumberFormatException e) {
                                System.out.println("Элемент со строки #"+count+" не добавлен из-за ошибки в структуре CSV!");
                            }
                        } else {
                            System.out.println("Элемент со строки #"+count+" не добавлен из-за ошибки в структуре CSV!");
                        }
                        count++;
                    }
                    if (getCollection().size() == 0) {
                        System.out.println("Коллекция не загружена, так как файл коллекции пуст или не содержит ни одной корректной строки!");
                    } else {
                        System.out.println("Коллекция успешно загружена!");
                    }
                } catch (IOException e) {
                    System.out.println("Коллекция не загружена, так как файл коллекции не найден.");
                }
            }
        } else {
            System.out.println("Коллекция не заполнена данными, так как файл коллекции не указан.");
        }
    }

    public static String write() {
        if (getArgs().length > 0) {
            if (!Files.exists(Paths.get(getArgs()[0]))) {
                return "Коллекция не сохранена, так как файл коллекции не найден.";
            } else if (Files.isDirectory(Paths.get(getArgs()[0]))) {
                return "Коллекция не сохранена, так как в качестве файла коллекци была передана директория.";
            } else if (!Files.isRegularFile(Paths.get(getArgs()[0]))) {
                return "Коллекция не не сохранена, так как в качестве файла коллекции передан специальный файл.";
            } else if (Files.isWritable(Paths.get(getArgs()[0]))) {
                try (BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(getArgs()[0]))) {
                    ArrayList<Product> sortedCollection = new ArrayList<>(getCollection());
                    Collections.sort(sortedCollection);
                    for (Product product : sortedCollection) {
                        out.write(product.toStringForCSV().getBytes());
                    }
                    return "Коллекция сохранена в файл "+getArgs()[0]+"!";
                } catch (IOException e) {
                    return "Коллекция не сохранена, так как файл для сохранения коллекции не найден.";
                }
            } else {
                return "Коллекция не сохранена, так как у файла коллекции нет прав на запись.";
            }
        } else {
            return "Коллекция не сохранена, так как файл коллекции не указан.";
        }
    }
}
