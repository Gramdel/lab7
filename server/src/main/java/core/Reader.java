package core;

import commands.Command;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.logging.Level;

import static core.Main.getLogger;

public class Reader extends Thread{
    DatagramSocket socket;

    public Reader(DatagramSocket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            byte[] b = new byte[10000];
            DatagramPacket packet = new DatagramPacket(b, b.length);

            socket.receive(packet);
            getLogger().log(Level.INFO, "Получен пакет от клиента " + packet.getAddress());

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(b);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            Command command = (Command) objectInputStream.readObject();
            getLogger().log(Level.INFO, "Успешная десериализация команды " + command.getClass().getSimpleName() + "!");

            new Writer(socket, packet, command).start();
        } catch (IOException | ClassNotFoundException e) {
            getLogger().log(Level.WARNING,"Ошибка десериализации!");
        }
    }
}
