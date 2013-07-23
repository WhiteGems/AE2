package net.aetherteam.aether.donator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

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
        directory = Minecraft.getMinecraftDir() + "/aether/donators/";
        config = new File(directory, fileName);
        config.mkdir();

        if (config.exists())
            try
            {
                FileInputStream in = new FileInputStream(directory + fileName);
                props.load(in);

                if (props.size() <= 0)
                {
                    resetChoices();
                    return;
                }

                int count = 0;

                while (String.valueOf(props.getProperty("choice" + count)) != null)
                {
                    String name = String.valueOf(props.getProperty("choice" + count));
                    choices.put(name, "choice" + count);
                    count++;
                }
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
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
            props.store(new FileOutputStream(directory + fileName), null);
            FileInputStream in = new FileInputStream(directory + fileName);
            props.load(in);
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

    public static void removeProperty(String name)
    {
        try
        {
            props.remove(name);
            props.store(new FileOutputStream(directory + fileName), null);
            FileInputStream in = new FileInputStream(fileName);
            props.load(in);
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

    public static void setProperty(String name, String value)
    {
        try
        {
            props.setProperty(name, value);
            props.store(new FileOutputStream(directory + fileName), null);
            FileInputStream in = new FileInputStream(fileName);
            props.load(in);
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
}

