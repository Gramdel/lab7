package core;

import commands.Command;

import java.io.*;
import java.net.*;
import java.util.logging.Level;

import static core.Main.*;

public class Server extends Thread {
    private DatagramSocket socket;

    public Server() {
        if (getArgs().length < 2) {
            System.out.println("Сервер не запущен, так как не указан порт!\n(число от 0 до 65535 должно быть передано вторым аргументом командной строки)");
            getLogger().log(Level.WARNING,"Сервер не запущен, так как не указан порт!");
        } else {
            System.out.println("Пытаемся запустить сервер на порте "+getArgs()[1]+"...");
            try {
                socket = new DatagramSocket((int) Long.parseLong(getArgs()[1]));
                System.out.println("Сервер успешно запущен!");
                getLogger().log(Level.INFO,"Сервер успешно запущен!");
            } catch (SocketException e) {
                System.out.println("Не удалось запустить сервер на этом порте!");
                getLogger().log(Level.WARNING,"Не удалось запустить сервер на порте "+getArgs()[1]+"!");
            } catch (NumberFormatException e) {
                System.out.println("Сервер не запущен, так как указан неправильный формат порта!\n(число от 0 до 65535 должно быть передано вторым аргументом командной строки)");
                getLogger().log(Level.WARNING,"Не удалось запустить сервер на порте "+getArgs()[1]+"!");
            }
        }
    }

    public void run() {
        while(true) {
            try {
                byte[] b = new byte[10000];
                DatagramPacket packet = new DatagramPacket(b, b.length);

                socket.receive(packet);
                getLogger().log(Level.INFO,"Получен пакет от клиента "+packet.getAddress());

                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(b);
                ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
                Command command = (Command) objectInputStream.readObject();
                getLogger().log(Level.INFO,"Успешная десериализация команды "+command.getClass().getSimpleName()+"!");

                try {
                    String response = command.execute(getCollection(),getOrganizations(),getDate(),getHistory());
                    b = response.getBytes();
                    packet = new DatagramPacket(b, b.length, packet.getAddress(), packet.getPort());
                    socket.send(packet);
                    getLogger().log(Level.INFO,"Ответ успешно отправлен!");
                } catch (IOException e) {
                    getLogger().log(Level.WARNING,"Ошибка отправки ответа!");
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                getLogger().log(Level.WARNING,"Ошибка десериализации!");
            }
        }
    }

    public DatagramSocket getSocket() {
        return socket;
    }
}