package net.htlgkr.gopost.client;

import android.util.Log;

import net.htlgkr.gopost.packet.Packet;
import net.htlgkr.gopost.util.ObservableValue;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ServerConnection {

    private ObjectOutputStream writer;
    private ObjectInputStream reader;

    public ServerConnection() {
        try {
            this.writer = new ObjectOutputStream(Client.getClientSocket().getOutputStream());
            this.reader = new ObjectInputStream(Client.getClientSocket().getInputStream());
            Log.e("AMONG", "READER");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean sendPacket(ObservableValue<Packet> packetValue) {
        try {
            writer.writeObject(packetValue.getValue());
            writer.flush();
            new Thread(() -> receivePacket(packetValue)).start();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private void receivePacket(ObservableValue<Packet> packetValue) {
        try {
            Log.e("AMONG", "RECEIVE");
            Object readObject = reader.readObject();
            if (readObject instanceof Packet && packetValue != null) {
                packetValue.setValue((Packet) readObject);
            }
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }
}
