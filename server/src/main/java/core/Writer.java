package core;

import commands.Command;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.logging.Level;

import static core.Main.*;

public class Writer extends Thread {
    DatagramSocket socket;
    DatagramPacket packet;
    Command command;

    public Writer(DatagramSocket socket, DatagramPacket packet, Command command) {
        this.socket = socket;
        this.packet = packet;
        this.command = command;
    }

    @Override
    public void run() {
        try {
            String response = command.execute(getCollection(), getOrganizations(), getDate(), getHistory(), getDBUnit());
            byte[] b = response.getBytes();
            packet = new DatagramPacket(b, b.length, packet.getAddress(), packet.getPort());
            socket.send(packet);
            getLogger().log(Level.INFO, "Ответ успешно отправлен!");
        } catch (IOException e) {
            getLogger().log(Level.WARNING, "Ошибка отправки ответа!");
        }
    }
}
