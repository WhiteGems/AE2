package net.aetherteam.aether.donator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;
import net.minecraft.client.Minecraft;

public class SyncedDonatorChoices
{
    private static Minecraft mc = Minecraft.getMinecraft();
    private static final Properties props = new Properties();
    private static String fileName;
    private static String directory;
    private static File config;
    public static HashMap choices = new HashMap();

    public static void loadConfig()
    {
        fileName = mc.session.username + ".properties";
        StringBuilder var10000 = new StringBuilder();
        Minecraft var10001 = mc;
        directory = var10000.append(Minecraft.getMinecraftDir()).append("/aether/donators/").toString();
        config = new File(directory, fileName);
        config.mkdir();

        if (config.exists())
        {
            try
            {
                FileInputStream var0 = new FileInputStream(directory + fileName);
                props.load(var0);

                if (props.size() <= 0)
                {
                    resetChoices();
                    return;
                }

                for (int var1 = 0; String.valueOf(props.getProperty("choice" + var1)) != null; ++var1)
                {
                    String var2 = String.valueOf(props.getProperty("choice" + var1));
                    choices.put(var2, "choice" + var1);
                }
            }
            catch (FileNotFoundException var3)
            {
                var3.printStackTrace();
            }
            catch (IOException var4)
            {
                var4.printStackTrace();
            }
        }
        else
        {
            resetChoices();
        }
    }

    public static void resetChoices()
    {
        try
        {
            props.clear();
            props.store(new FileOutputStream(directory + fileName), (String)null);
            FileInputStream var0 = new FileInputStream(directory + fileName);
            props.load(var0);
        }
        catch (FileNotFoundException var1)
        {
            var1.printStackTrace();
        }
        catch (IOException var2)
        {
            var2.printStackTrace();
        }
    }

    public static void removeProperty(String var0)
    {
        try
        {
            props.remove(var0);
            props.store(new FileOutputStream(directory + fileName), (String)null);
            FileInputStream var1 = new FileInputStream(fileName);
            props.load(var1);
        }
        catch (FileNotFoundException var2)
        {
            var2.printStackTrace();
        }
        catch (IOException var3)
        {
            var3.printStackTrace();
        }
    }

    public static void setProperty(String var0, String var1)
    {
        try
        {
            props.setProperty(var0, var1);
            props.store(new FileOutputStream(directory + fileName), (String)null);
            FileInputStream var2 = new FileInputStream(fileName);
            props.load(var2);
        }
        catch (FileNotFoundException var3)
        {
            var3.printStackTrace();
        }
        catch (IOException var4)
        {
            var4.printStackTrace();
        }
    }
}
