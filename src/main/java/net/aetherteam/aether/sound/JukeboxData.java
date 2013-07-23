package net.aetherteam.aether.sound;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import net.minecraft.client.Minecraft;

public class JukeboxData
{
    private static Minecraft mc = Minecraft.getMinecraft();

    private static final Properties menuProps = new Properties();
    public static String selectedMenuName;
    public static boolean loopMusic;
    public static boolean muteMusic;
    public static int lastMusicIndex;
    public static int musicIndex;
    public static boolean musicSet;
    public static boolean hasPlayedMusic;
    public static boolean hasStartedMusic;
    public static boolean endMusic = true;
    public static JukeboxPlayer jukebox = new JukeboxPlayer();
    public static double playerPosX;
    public static double playerPosY;
    public static double playerPosZ;
    public static int ticks;
    private static File config = new File(Minecraft.getMinecraftDir(), "MenuAPI.properties");

    public static void loadConfig()
    {
        if (config.exists())
            try
            {
                Minecraft.getMinecraft();
                FileInputStream in = new FileInputStream(Minecraft.getMinecraftDir() + "/MenuAPI.properties");
                menuProps.load(in);

                if (menuProps.size() <= 0)
                {
                    resetConfig();
                    return;
                }

                selectedMenuName = String.valueOf(menuProps.getProperty("selectedMenu"));
                loopMusic = menuProps.getProperty("loopMusic").equals("true");
                muteMusic = menuProps.getProperty("muteMusic").equals("true");
                lastMusicIndex = Integer.valueOf(menuProps.getProperty("lastMusicIndex")).intValue();
                musicIndex = Integer.valueOf(menuProps.getProperty("musicIndex")).intValue();
                musicSet = menuProps.getProperty("musicSet").equals("true");
                hasPlayedMusic = menuProps.getProperty("hasPlayedMusic").equals("true");
                hasStartedMusic = menuProps.getProperty("hasStartedMusic").equals("true");
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
            resetConfig();
        }
    }

    public static void resetConfig()
    {
        try
        {
            menuProps.setProperty("selectedMenu", "");
            menuProps.setProperty("loopMusic", "true");
            menuProps.setProperty("muteMusic", "false");
            menuProps.setProperty("lastMusicIndex", String.valueOf(jukebox.getIndexFromName("Strad")));
            menuProps.setProperty("musicIndex", String.valueOf(jukebox.getIndexFromName("Strad")));
            menuProps.setProperty("musicSet", "false");
            menuProps.setProperty("hasPlayedMusic", "false");
            menuProps.setProperty("hasStartedMusic", "false");
            Minecraft.getMinecraft();
            menuProps.store(new FileOutputStream(Minecraft.getMinecraftDir() + "/MenuAPI.properties"), null);
            Minecraft.getMinecraft();
            FileInputStream in = new FileInputStream(Minecraft.getMinecraftDir() + "/MenuAPI.properties");
            menuProps.load(in);
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

    public static void wipeConfig()
    {
        if (config.exists())
            try
            {
                menuProps.setProperty("selectedMenu", "");
                Minecraft.getMinecraft();
                menuProps.store(new FileOutputStream(Minecraft.getMinecraftDir() + "/MenuAPI.properties"), null);
                FileInputStream in = new FileInputStream("MenuAPI.properties");
                menuProps.load(in);
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
            menuProps.setProperty(name, value);
            Minecraft.getMinecraft();
            menuProps.store(new FileOutputStream(Minecraft.getMinecraftDir() + "/MenuAPI.properties"), null);
            FileInputStream in = new FileInputStream("MenuAPI.properties");
            menuProps.load(in);
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

    static
    {
        Minecraft.getMinecraft();
    }
}

