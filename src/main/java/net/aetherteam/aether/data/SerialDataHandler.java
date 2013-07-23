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
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.ISaveHandler;

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
        this.path = (serverIsUp() ? MinecraftServer.getServer().worldServers[0].L().getMapFileFromName(MinecraftServer.getServer().getFolderName()).getAbsolutePath().replace(MinecraftServer.getServer().getFolderName() + ".dat", "") : null);
        this.fileName = fileName;
    }

    public boolean serverIsUp()
    {
        return (MinecraftServer.getServer() != null) && (MinecraftServer.getServer().worldServers != null) && (MinecraftServer.getServer().worldServers[0] != null);
    }

    public void serializeObjects(ArrayList objects)
    {
        try
        {
            ObjectOutputStream objectOut = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(this.path + this.fileName)));

            for (Iterator i$ = objects.iterator(); i$.hasNext();)
            {
                Object object = i$.next();
                objectOut.writeObject(object);
            }

            objectOut.close();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public ArrayList deserializeObjects()
    {
        try
        {
            File file = new File(this.path + this.fileName);

            if (file.exists())
            {
                FileInputStream inputStream = new FileInputStream(this.path + this.fileName);
                ArrayList objects = new ArrayList();
                ObjectInputStream objectIn = new ObjectInputStream(new BufferedInputStream(inputStream));
                Object obj = null;
                boolean catchBool = true;

                try
                {
                    while (true)
                        if (catchBool)
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
                            catch (EOFException exc)
                            {
                            }
                }
                catch (ClassNotFoundException e)
                {
                    e.printStackTrace();
                }

                objectIn.close();
                return objects;
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return null;
    }
}

