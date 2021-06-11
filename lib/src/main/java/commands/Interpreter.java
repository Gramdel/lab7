package commands;

import collection.Organization;
import collection.Product;

import java.io.InputStream;
import java.io.Serializable;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Interpreter implements Serializable {
    private static final HashMap<String, Command> commands = new HashMap<>();
    private static LinkedHashSet<Product> collection = new LinkedHashSet<>();
    private static ArrayList<Organization> organizations = new ArrayList<>();
    private static Date date;
    private static Stack<String> history = new Stack<>();
    private static boolean mode = false;
    private static Command command;

    static {
        commands.put("add", new Add());
        commands.put("clear", new Clear());
        commands.put("exit", new Exit());
        commands.put("history", new History());
        commands.put("help", new Help());
        commands.put("execute_script", new ExecuteScript());
        commands.put("show", new Show());
        commands.put("remove_by_id", new RemoveById());
        commands.put("update", new Update());
        commands.put("info", new Info());
        commands.put("remove_any_by_unit_of_measure", new RemoveByUOM());
        commands.put("filter_by_manufacturer", new FilterByManufacturer());
        commands.put("add_if_max", new AddIfMax());
        commands.put("print_field_descending_price", new PrintPrice());
        commands.put("remove_greater", new RemoveGreater());
    }

    public static void setProperties(LinkedHashSet<Product> collection, ArrayList<Organization> organizations, Date date, Stack<String> history){
        Interpreter.collection = collection;
        Interpreter.organizations = organizations;
        Interpreter.date = date;
        Interpreter.history = history;
    }

    public static void switchMode() {
        mode = !mode;
    }

    public static void addCommand(String name, Command command){
        commands.put(name, command);
    }

    public static void fromStream(InputStream stream) {
        Scanner in = new Scanner(stream);
        while (in.hasNext()) {
            String s = in.nextLine();
            if (!s.matches("\\s*")) {
                String com = "";
                String arg = "";
                Matcher m = Pattern.compile("[^\\s]+").matcher(s);
                if (m.find()) {
                    com = m.group();
                    s = m.replaceFirst("");
                }
                m = Pattern.compile("[\\s]+").matcher(s);
                if (m.find()) {
                    arg = m.replaceFirst("");
                }
                addToHistory(com, history);
                Command command = commands.get(com);
                if (command != null) {
                    if (command.prepare(arg, stream.equals(System.in), commands)) {
                        if (mode) {
                            System.out.println(command.execute(collection, organizations, date, history));
                        } else {
                            Interpreter.command = command;
                        }
                    }
                } else {
                    System.out.println("Такой команды не существует! Список команд: help");
                }
            }
        }
    }

    public static Command getCommand() {
        return command;
    }

    public static void setCommand(Command command) {
        Interpreter.command = command;
    }

    public static void addToHistory(String com, Stack<String> history) {
        history.add(com);
        if (history.size() > 7) history.removeElementAt(0);
    }
}