package net.comcraft.src;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketWorldInfo extends Packet {

    private DataInputStream dis;
    private Object[][] existingPlayers;

    public PacketWorldInfo() {
    }

    public void writeData(DataOutputStream dos) throws IOException {
    }

    /*
     * A bad way to pull all data from response and store in a temporary stream for processing later.
     */
    public void readData(DataInputStream dis) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        float ver = dis.readFloat();
        dos.writeFloat(ver);
        byte[] data;
        data = new byte[36];
        dis.read(data);
        dos.write(data);
        int n = dis.readInt();
        dos.writeInt(n);
        data = new byte[n * 4];
        dis.read(data);
        dos.write(data);
        if (ver > 3) {
            dos.writeBoolean(dis.readBoolean());
        }
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        this.dis = new DataInputStream(bais);

        int players = dis.read() & 0xFF;
        existingPlayers = new Object[players][2];
        for (int i = 0; i < players; i++) {
            int pId = dis.readInt();
            Vec3D v = new Vec3D(dis.readFloat(), dis.readFloat(), dis.readFloat());
            existingPlayers[i] = new Object[] { new Integer(pId), v };
        }
    }

    public void process(ServerGame handler) {
        handler.handleWorldInfo(dis);
        for (int i = 0; i < existingPlayers.length; i++) {
            handler.handleNewPlayer(((Integer) existingPlayers[i][0]).intValue(), (Vec3D) existingPlayers[i][1]);
        }
    }

}
