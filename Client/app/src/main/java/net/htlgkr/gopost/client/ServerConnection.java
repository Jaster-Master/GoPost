package net.htlgkr.gopost.client;

import net.htlgkr.gopost.packet.Packet;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ServerConnection implements Runnable{

    private Client client;
    private ObjectOutputStream writer;
    private ObjectInputStream reader;

    public boolean sendPacket(Packet newPacket){
        return false;
    }

    @Override
    public void run() {

    }
}
