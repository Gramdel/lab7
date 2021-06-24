package core;

import commands.Command;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.logging.Level;

import static core.Main.getLogger;

public class Client {
    private static String hostname;
    private static int port;

    public static void setProperties (String hostname, int port) {
        Client.hostname = hostname;
        Client.port = port;
    }
    public static void send(Command command) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(command);
            objectOutputStream.flush();
            byte[] b = byteArrayOutputStream.toByteArray();

            try {
                SocketAddress address = new InetSocketAddress(hostname, port);
                DatagramSocket socket = new DatagramSocket();
                socket.setSoTimeout(5000);
                DatagramPacket packet = new DatagramPacket(b, b.length, address);
                socket.send(packet);
                getLogger().log(Level.INFO, "Команда " + command.getClass().getSimpleName() + " успешно отправлена на сервер " + hostname + ":" + port + "!");

                try {
                    b = new byte[10000];
                    packet = new DatagramPacket(b, b.length);
                    socket.receive(packet);
                    getLogger().log(Level.INFO, "Ответ от сервера успешно получен!");
                    System.out.println(new String(b).trim());
                } catch (SocketTimeoutException e) {
                    System.out.println("Время ожидания ответа от сервера истекло!");
                    getLogger().log(Level.WARNING, "Время ожидания ответа от сервера истекло!");
                }
            } catch (SocketException e) {
                System.out.println("Ошибка отправки пакета!");
                getLogger().log(Level.WARNING, "Ошибка отправки пакета!");
            } catch (IllegalArgumentException e) {
                System.out.println("Не удаётся подключиться к серверу " + hostname + ":" + port + "! Проверьте данные для подключения.");
                getLogger().log(Level.WARNING, "Не удаётся подключиться к серверу " + hostname + ":" + port + "!");
            }
        } catch (IOException e) {
            getLogger().log(Level.WARNING, "Ошибка сериализации!");
        }
    }
}