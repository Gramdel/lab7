package commands;

import collection.Organization;
import collection.Product;
import core.DBUnit;
import core.Interpreter;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class ExecuteScript extends Command {
    private final Stack<String> scripts = new Stack<>();
    private String arg;

    @Override
    public boolean prepare(String arg, boolean isInteractive, HashMap<String, Command> commands) {
        if (!Files.exists(Paths.get(arg))) {
            System.out.println("Скрипта с именем " + arg + " не существует!");
        } else if (Files.isDirectory(Paths.get(arg))) {
            System.out.println("Скрипт с именем " + arg + " не выполнен, так как в качестве исполняемого файла была передана директория.");
        } else if (!Files.isRegularFile(Paths.get(arg))) {
            System.out.println("Скрипт с именем " + arg + " не выполнен, так как в качестве исполняемого файла был передан специальный файл.");
        } else if (!Files.isReadable(Paths.get(arg))) {
            System.out.println("Скрипт с именем " + arg + " не выполнен, так как у исполняемого файла нет прав на чтение.");
        } else if (scripts.contains(arg)) {
            System.out.println("Скрипт с именем " + arg + " не выполнен, так как он вызывает сам себя!");
        } else {
            try {
                System.out.println("Скрипт из файла " + arg + " начинает выполняться...");
                scripts.push(arg);
                Interpreter.fromStream(new BufferedInputStream(new FileInputStream(arg)));
                this.arg = arg;
                scripts.remove(arg);
                return true;
            } catch (FileNotFoundException e) {
                System.out.println("Скрипта с именем " + arg + " не существует!");
            }
        }
        return false;
    }

    @Override
    public synchronized String execute(LinkedHashSet<Product> collection, ArrayList<Organization> organizations, Date date, Stack<String> history, DBUnit dbUnit) {
        return "Скрипт из файла " + arg + " выполнен!";
    }

    @Override
    public String description() {
        return "Выполняет скрипт." + syntax();
    }

    @Override
    public String syntax() {
        return " Синтаксис: execute_script file_name, где file_name - полное (с расширением) имя файла.";
    }
}
