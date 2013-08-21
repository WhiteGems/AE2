package net.aetherteam.aether.data;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.server.MinecraftServer;

public class SerialDataHandler
{
    private String path;
    private String fileName;

    public SerialDataHandler(String path, String fileName)
    {
        this.path = path;
        this.fileName = fileName;
    }

    public SerialDataHandler(String fileName)
    {
        this.path = this.serverIsUp() ? MinecraftServer.getServer().worldServers[0].getSaveHandler().getMapFileFromName(MinecraftServer.getServer().getFolderName()).getAbsolutePath().replace(MinecraftServer.getServer().getFolderName() + ".dat", "") : null;
        this.fileName = fileName;
    }

    public boolean serverIsUp()
    {
        return MinecraftServer.getServer() != null && MinecraftServer.getServer().worldServers != null && MinecraftServer.getServer().worldServers[0] != null;
    }

    public void serializeObjects(ArrayList<Object> objects)
    {
        try
        {
            ObjectOutputStream e = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(this.path + this.fileName)));
            Iterator i$ = objects.iterator();

            while (i$.hasNext())
            {
                Object object = i$.next();
                e.writeObject(object);
            }

            e.close();
        }
        catch (FileNotFoundException var5)
        {
            var5.printStackTrace();
        }
        catch (IOException var6)
        {
            var6.printStackTrace();
        }
    }

    public ArrayList<Object> deserializeObjects()
    {
        try
        {
            File e = new File(this.path + this.fileName);

            if (e.exists())
            {
                FileInputStream inputStream = new FileInputStream(this.path + this.fileName);
                ArrayList objects = new ArrayList();
                ObjectInputStream objectIn = new ObjectInputStream(new BufferedInputStream(inputStream));
                Object obj = null;
                boolean catchBool = true;

                try
                {
                    while (catchBool)
                    {
                        try
                        {
                            if ((obj = objectIn.readObject()) != null)
                            {
                                objects.add(obj);
                            }
                            else
                            {
                                catchBool = false;
                            }
                        }
                        catch (EOFException var8)
                        {
                            break;
                        }
                    }
                }
                catch (ClassNotFoundException var9)
                {
                    var9.printStackTrace();
                }

                objectIn.close();
                return objects;
            }
        }
        catch (IOException var10)
        {
            var10.printStackTrace();
        }

        return null;
    }
}
