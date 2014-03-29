package net.comcraft.src;

import java.util.Hashtable;
import java.util.Vector;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;

import com.google.minijoe.sys.JsFunction;
import com.java4ever.apime.io.GZIP;

import net.comcraft.client.Comcraft;

public class ModLoader {
    public static final int API_VERSION = 6;
    public static final int MIN_API_VERSION = 6;
    private static final int PACKAGE = 0x10;
    private static final int MOD_DESCRIPTOR = 0x20;
    private static final int RESOURCE = 0x30;
    public static final String version = "0.6";
    private Comcraft cc;
    private Vector Mods;
    private boolean hasInitialized = false;
    private Hashtable resourcedata;
    private Hashtable packages;

    public ModLoader(Comcraft cc) {
        this.cc = cc;
        Mods = new Vector();
        System.out.println("Comcraft ModLoader " + version + " Initialized");
    }

    public void initMods() {
        resourcedata = new Hashtable();
        if (!cc.settings.getComcraftFileSystem().isAvailable()) {
            System.out.println("files not initialized");
            return;
        }
        System.out.println("scanning mods folder");
        Vector elements;
        try {
            FileConnection fileConnection = (FileConnection) Connector.open(cc.settings.getComcraftFileSystem().getPathToFolder("mods/"), Connector.READ);
            if (!fileConnection.exists()) {
                fileConnection.mkdir();
            }
            hasInitialized = true;
            elements = FileSystemHelper.getElementsList(fileConnection);
            fileConnection.close();
        } catch (IOException ioEx) {
            ioEx.printStackTrace();
            return;
        }
        packages = new Hashtable(elements.size() / 2);
        ModAPI.getInstance(cc); // Prepare the API
        for (int i = 0; i < elements.size(); ++i) {
            String elementName = (String) elements.elementAt(i);
            if (elementName.endsWith("/") || !elementName.endsWith(".mod")) {
                continue;
            }
            String modFileName = elementName.substring(elementName.lastIndexOf(0x2F) + 1, elementName.length());
            Mod mod = new Mod(this, modFileName);
            byte[] RawData = null;
            try {
                FileConnection GZModFile = open(elementName);
                byte[] GZData = new byte[(int) GZModFile.fileSize()];
                if (GZData.length < 20) {
                    throw new IOException("Not enough bytes in mod file");
                }
                GZModFile.openInputStream().read(GZData, 0, GZData.length);
                GZModFile.close();
                RawData = GZIP.inflate(GZData);
            } catch (IOException modEx) {
                modEx.printStackTrace();
                mod.info = modEx.getMessage();
                mod.fatalError = true;
            }
            if (RawData != null) {
                try {
                    ReadModFile(new DataInputStream(new ByteArrayInputStream(RawData)), mod);
                } catch (IOException e) {
                    mod.info = e.getMessage();
                    mod.fatalError = true;
                }
            }
            Mods.addElement(mod);
        }
        for (int i = 0; i < Mods.size(); i++) {
            ((Mod) Mods.elementAt(i)).initMod();
        }
    }

    private void ReadModFile(DataInputStream dis, Mod mod) throws IOException {
        byte[] b = new byte[4];
        dis.read(b);
        if (!new String(b).equals("CCML")) {
            throw new IOException("Malformed Mod File");
        }
        int version = dis.read();
        if (version > API_VERSION) {
            throw new IOException("Mod built for a later version of comcraft");
        }
        if (version < MIN_API_VERSION) {
            throw new IOException("Mod built for an earlier version of comcraft");
        }
        mod.info = "No Mod Info";
        int flags = dis.read();
        readLoop: while (dis.available() > 0) {
            int opt = dis.read();
            switch (opt) {
            case MOD_DESCRIPTOR:
                mod.name = dis.readUTF();
                mod.description = dis.readUTF();
                mod.mainClass = dis.readUTF();
                String ldesc = dis.readUTF();
                if (ldesc.length() > 0) {
                    mod.info = ldesc;
                }
                if (isDisabled(mod.mainClass)) {
                    // Don't waste time if the mod is disabled
                    break readLoop;
                }
                break;
            case RESOURCE:
                int l = dis.read();
                for (int i = 0; i < l; i++) {
                    String resname = dis.readUTF();
                    String content = dis.readUTF();
                    if (resourcedata.containsKey(resname)) {
                        // Do something here maybe
                    } else {
                        resourcedata.put(resname, content);
                    }
                }
                break;
            case PACKAGE:
                l = dis.read();
                for (int i = 0; i < l; i++) {
                    String packageName = dis.readUTF();
                    int flen = dis.read();
                    Hashtable files = new Hashtable(flen / 2);
                    for (int x = 0; x < flen; x++) {
                        String filename = dis.readUTF();
                        if (version >= 4 && (flags & 1) == 1) {
                            dis.readUTF(); // Skip past source code
                        }
                        int length = dis.readInt();
                        byte[] data = new byte[length];
                        dis.read(data);
                        files.put(filename, data);
                    }
                    packages.put(packageName, files);
                }
                break;
            case -1:
                break;
            default:
                System.out.println("<Unknown Data " + opt + ">");
                break;
            }
        }
        dis.close();
    }

    public boolean executeModInNs(String package_, String fname) throws Exception {
        Hashtable pkg;
        if (packages.containsKey(package_) && (pkg = (Hashtable) packages.get(package_)).containsKey(fname)) {
            JsFunction.exec(new DataInputStream(new ByteArrayInputStream((byte[]) pkg.get(fname))), ModAPI.getInstance());
            return true;
        }
        return false;
    }

    public InputStream getResourceAsStream(String filename) {
        // Emulates standard Class.getResourceAsStream
        String content = (String) resourcedata.get(filename);
        if (content == null) {
            return Class.class.getResourceAsStream(filename);
        }
        return new ByteArrayInputStream(content.getBytes());
    }

    public Vector ListMods() {
        return Mods;
    }

    public boolean isInitialized() {
        return hasInitialized;
    }

    private FileConnection open(String filename) throws IOException {
        return (FileConnection) Connector.open(filename, Connector.READ);
    }

    public boolean isDisabled(String modId) {
        if (modId == null) {
            return false;
        }
        for (int i = 0; i < cc.settings.disabledMods.length; i++) {
            String m;
            if ((m = cc.settings.disabledMods[i]) != null && m.equals(modId)) {
                return true;
            }
        }
        return false;
    }

    public void enable(String name) {
        if (!isDisabled(name)) {
            return;
        }
        String[] newarr = new String[cc.settings.disabledMods.length - 1];
        for (int i = 0, ni = 0; i < cc.settings.disabledMods.length; i++) {
            String m;
            if ((m = cc.settings.disabledMods[i]) != null && !m.equals(name)) {
                newarr[ni++] = cc.settings.disabledMods[i];
            }
        }
        cc.settings.disabledMods = newarr;
    }

    public void disable(String modId) {
        if (isDisabled(modId)) {
            return;
        }
        String[] newarr = new String[cc.settings.disabledMods.length + 1];
        System.arraycopy(cc.settings.disabledMods, 0, newarr, 0, cc.settings.disabledMods.length);
        newarr[newarr.length - 1] = modId;
        cc.settings.disabledMods = newarr;
    }
}