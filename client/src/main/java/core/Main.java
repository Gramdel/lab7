package core;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Main {
    private static String[] args;
    private static Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        if (args.length<2) {
            System.out.println("Программа не запущена, так как не переданы IP и порт сервера!\n(Они должны быть переданы через аргументы командной строки. Формат IP: xxx.xxx.xxx.xxx; формат порта: число от 0 до 65535.)");
        } else {
            new Thread(new Client()).start();
            Main.args = args;
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
            System.out.println("Вас приветствует программа-клиент для управления коллекцией продуктов. Для получения списка команд введите help. \n" + "Введите команду:");
            Interpreter.fromStream(System.in);
        }
    }

    public static Logger getLogger() {
        return logger;
    }

    public static String[] getArgs() {
        return args;
    }
}