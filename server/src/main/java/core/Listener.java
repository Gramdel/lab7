package core;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;

import static core.Main.getLogger;

public class Listener {
    public static void listen(int port){
        ExecutorService es = Executors.newFixedThreadPool(10);
        //while(true) {
            try {
                es.execute(new Reader(new DatagramSocket(port)));
            } catch (SocketException e) {
                System.out.println("Не удалось запустить сервер на порте " + port + "!");
                getLogger().log(Level.WARNING,"Не удалось запустить сервер на порте " + port + "!");
            }
        //}
    }
}
