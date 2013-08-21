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
    public static HashMap<String, String> choices = new HashMap();

    public static void loadConfig()
    {
        fileName = mc.func_110432_I().func_111285_a() + ".properties";
        directory = mc.mcDataDir + "/aether/donators/";
        config = new File(directory, fileName);
        config.mkdir();

        if (config.exists())
        {
            try
            {
                FileInputStream e = new FileInputStream(directory + fileName);
                props.load(e);

                if (props.size() <= 0)
                {
                    resetChoices();
                    return;
                }

                for (int count = 0; String.valueOf(props.getProperty("choice" + count)) != null; ++count)
                {
                    String name = String.valueOf(props.getProperty("choice" + count));
                    choices.put(name, "choice" + count);
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
            FileInputStream e = new FileInputStream(directory + fileName);
            props.load(e);
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

    public static void removeProperty(String name)
    {
        try
        {
            props.remove(name);
            props.store(new FileOutputStream(directory + fileName), (String)null);
            FileInputStream e = new FileInputStream(fileName);
            props.load(e);
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

    public static void setProperty(String name, String value)
    {
        try
        {
            props.setProperty(name, value);
            props.store(new FileOutputStream(directory + fileName), (String)null);
            FileInputStream e = new FileInputStream(fileName);
            props.load(e);
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
