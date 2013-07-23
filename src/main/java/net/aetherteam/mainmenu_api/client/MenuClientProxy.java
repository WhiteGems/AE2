package net.aetherteam.mainmenu_api.client;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import net.aetherteam.mainmenu_api.MainMenuAPI;
import net.aetherteam.mainmenu_api.MenuBaseAether;
import net.aetherteam.mainmenu_api.MenuBaseLeftMinecraft;
import net.aetherteam.mainmenu_api.MenuBaseMinecraft;
import net.aetherteam.mainmenu_api.MenuCommonProxy;
import net.aetherteam.mainmenu_api.MenuSoundLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.src.ModLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.EventBus;

public class MenuClientProxy extends MenuCommonProxy
{
    private static String soundZipPath = "/resources/";

    public void registerTickHandler()
    {
        MainMenuAPI.registerMenu("Minecraft", MenuBaseMinecraft.class);
        MainMenuAPI.registerMenu("Left Minecraft", MenuBaseLeftMinecraft.class);
        MainMenuAPI.registerMenu("Aether I", MenuBaseAether.class);
        TickRegistry.registerTickHandler(new MenuClientTickHandler(), Side.CLIENT);
    }

    public Minecraft getClient()
    {
        return FMLClientHandler.instance().getClient();
    }

    public void registerSounds()
    {
        installSound("streaming/Aether Menu.ogg");
        installSound("streaming/Aether Menu Two.wav");
        MinecraftForge.EVENT_BUS.register(new MenuSoundLoader());
    }

    private void installSound(String filename)
    {
        File soundFile = new File(ModLoader.getMinecraftInstance().mcDataDir, "resources/" + filename);

        if (!soundFile.exists())
        {
            try
            {
                String srcPath = soundZipPath + filename;
                InputStream inStream = MainMenuAPI.class.getResourceAsStream(srcPath);

                if (inStream == null)
                {
                    throw new IOException();
                }

                if (!soundFile.getParentFile().exists())
                {
                    soundFile.getParentFile().mkdirs();
                }

                BufferedInputStream fileIn = new BufferedInputStream(inStream);
                BufferedOutputStream fileOut = new BufferedOutputStream(new FileOutputStream(soundFile));
                byte[] buffer = new byte[1024];
                int n = 0;

                while (-1 != (n = fileIn.read(buffer)))
                {
                    fileOut.write(buffer, 0, n);
                }

                fileIn.close();
                fileOut.close();
            }
            catch (IOException ex)
            {
            }
        }

        if ((soundFile.canRead()) && (soundFile.isFile()))
        {
            ModLoader.getMinecraftInstance().installResource(filename, soundFile);
        }
        else
        {
            System.err.println("Could not load file: " + soundFile);
        }
    }
}

