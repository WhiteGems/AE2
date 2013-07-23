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

    public SerialDataHandler(String var1, String var2)
    {
        this.path = var1;
        this.fileName = var2;
    }

    public SerialDataHandler(String var1)
    {
        this.path = this.serverIsUp() ? MinecraftServer.getServer().worldServers[0].getSaveHandler().getMapFileFromName(MinecraftServer.getServer().getFolderName()).getAbsolutePath().replace(MinecraftServer.getServer().getFolderName() + ".dat", "") : null;
        this.fileName = var1;
    }

    public boolean serverIsUp()
    {
        return MinecraftServer.getServer() != null && MinecraftServer.getServer().worldServers != null && MinecraftServer.getServer().worldServers[0] != null;
    }

    public void serializeObjects(ArrayList var1)
    {
        try
        {
            ObjectOutputStream var2 = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(this.path + this.fileName)));
            Iterator var3 = var1.iterator();

            while (var3.hasNext())
            {
                Object var4 = var3.next();
                var2.writeObject(var4);
            }

            var2.close();
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

    public ArrayList deserializeObjects()
    {
        try
        {
            File var1 = new File(this.path + this.fileName);

            if (var1.exists())
            {
                FileInputStream var2 = new FileInputStream(this.path + this.fileName);
                ArrayList var4 = new ArrayList();
                ObjectInputStream var3 = new ObjectInputStream(new BufferedInputStream(var2));
                Object var5 = null;
                boolean var6 = true;

                try
                {
                    while (var6)
                    {
                        try
                        {
                            if ((var5 = var3.readObject()) != null)
                            {
                                var4.add(var5);
                            }
                            else
                            {
                                var6 = false;
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

                var3.close();
                return var4;
            }
        }
        catch (IOException var10)
        {
            var10.printStackTrace();
        }

        return null;
    }
}
