package core;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;

import static core.Main.getLogger;

public class Listener {
    private static final ExecutorService es = Executors.newFixedThreadPool(10);
    private static DatagramSocket socket;

    public static boolean bind(int port) {
        try {
            socket = new DatagramSocket(port);
            return true;
        } catch (SocketException e) {
            System.out.println("Не удалось запустить сервер на порте " + port + "!");
            getLogger().log(Level.WARNING,"Не удалось запустить сервер на порте " + port + "!");
            return false;
        }
    }

    public static void listenPort() {
        es.execute(new Reader(socket));
    }
}
