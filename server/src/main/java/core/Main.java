package core;

import collection.Organization;
import collection.Product;
import commands.Interpreter;
import commands.Save;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Main {
    private static final LinkedHashSet<Product> collection = new LinkedHashSet<>();
    private static final ArrayList<Organization> organizations = new ArrayList<>();
    private static Date date;
    private static String[] args;
    private static Logger logger = Logger.getLogger(Main.class.getName());
    private static final Stack<String> history = new Stack<>();

    public static void main(String[] args) {
        try {
            String loggerCfg = "handlers = java.util.logging.FileHandler\n" +
                    "java.util.logging.FileHandler.level     = ALL\n" +
                    "java.util.logging.FileHandler.formatter = java.util.logging.SimpleFormatter\n" +
                    "java.util.logging.FileHandler.append    = true\n" +
                    "java.util.logging.FileHandler.pattern   = log.txt";
            LogManager.getLogManager().readConfiguration(new ByteArrayInputStream(loggerCfg.getBytes()));
            logger = Logger.getLogger(Main.class.getName());
            System.out.println("Логгер успешно инициализирован!");
        } catch (IOException e) {
            System.out.println("Не удалось инициализировать логгер!");
        }

        date = new Date();
        Main.args = args;
        CSVUnit.read();
        Server server = new Server();
        System.out.println("Вас приветствует программа-сервер для управления коллекцией продуктов. Для получения списка команд введите help. \n" + "Введите команду:");
        if (server.getSocket() != null) {
            server.setDaemon(true);
            server.start();
        }
        Interpreter.addCommand("save", new Save());
        Interpreter.switchMode();
        Interpreter.setProperties(collection, organizations, date, history);
        Interpreter.fromStream(System.in);
    }

    public static LinkedHashSet<Product> getCollection() {
        return collection;
    }

    public static ArrayList<Organization> getOrganizations() {
        return organizations;
    }

    public static String[] getArgs() {
        return args;
    }

    public static Logger getLogger() {
        return logger;
    }

    public static Date getDate() {
        return date;
    }

    public static Stack<String> getHistory() {
        return history;
    }
}