package net.comcraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketPlayerData extends Packet {

    private static final int ACTION_JOIN = 1;
    private static final int ACTION_QUIT = 2;
    private static final int ACTION_MOVE = 3;
    private EntityPlayer player = null;

    private int action;
    private int pId;
    private Object data;

    public PacketPlayerData() {
    }

    public PacketPlayerData(EntityPlayer player) {
        this.player = player;
    }

    public PacketPlayerData(int id, Vec3D position) {
        this.data = position;
        pId = id;
    }

    public void writeData(DataOutputStream dos) throws IOException {
        if (player != null) {
            dos.write(ACTION_JOIN);
            player.writeToDataOutputStream(dos);
        } else if (data != null) {
            dos.write(ACTION_MOVE);
            dos.writeInt(pId);
            dos.writeFloat(((Vec3D) data).x);
            dos.writeFloat(((Vec3D) data).y);
            dos.writeFloat(((Vec3D) data).z);
        }
    }

    public void readData(DataInputStream dis) throws IOException {
        action = dis.read() & 0xFF;
        pId = dis.readInt();
        switch (action) {
        case ACTION_JOIN:
            data = new Vec3D(dis.readFloat(), dis.readFloat(), dis.readFloat());
            break;
        case ACTION_MOVE:
            data = new Vec3D(dis.readFloat(), dis.readFloat(), dis.readFloat());
            break;
        case ACTION_QUIT:
            break;
        default:
            break;
        }
    }

    public void process(ServerGame handler) {
        if (action == ACTION_MOVE) {
            handler.handlePlayerMove(pId, (Vec3D) data);
        } else if (action == ACTION_JOIN) {
            handler.handleNewPlayer(pId, (Vec3D) data);
        } else if (action == ACTION_QUIT) {
            handler.handlePlayerQuit(pId);
        }
    }
}
