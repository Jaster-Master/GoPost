package net.htlgkr.gopost.client;

import android.util.Log;

import net.htlgkr.gopost.packet.Packet;
import net.htlgkr.gopost.util.ObservableValue;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URI;
import java.nio.ByteBuffer;

import javax.net.SocketFactory;

public class ServerConnection extends WebSocketClient {

    public static final String LOG_TAG = ServerConnection.class.getSimpleName();
    private ObservableValue<Packet> currentPacketValue;

    public ServerConnection(String address, int port, SocketFactory socketFactory) {
        super(URI.create("ws://" + address + ":" + port));
        setSocketFactory(socketFactory);
    }

    public boolean sendPacket(ObservableValue<Packet> packetValue) {
        try {
            final ByteArrayOutputStream baos = new ByteArrayOutputStream(6400);
            final ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(packetValue.getValue());
            oos.flush();
            final byte[] data = baos.toByteArray();

            currentPacketValue = packetValue;
            send(data);
            Log.i(LOG_TAG, "Data sent: " + packetValue.getValue());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void onOpen(ServerHandshake handshakeData) {
        Log.i(LOG_TAG, "Successfully connected");
    }

    @Override
    public void onMessage(String message) {
        Log.i(LOG_TAG, "Message received: " + message);
    }

    @Override
    public void onMessage(ByteBuffer bytes) {
        super.onMessage(bytes);
        Object readObject;
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes.array());
            ObjectInputStream reader = new ObjectInputStream(byteArrayInputStream);
            readObject = reader.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        Log.i(LOG_TAG, "Data received: " + readObject);
        if (!(readObject instanceof Packet)) return;
        currentPacketValue.setValue((Packet) readObject);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        Log.i(LOG_TAG, "WebSocket closed");
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
    }
}
