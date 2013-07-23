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
import net.aetherteam.mainmenu_api.MainMenuAPI;
import net.aetherteam.mainmenu_api.MenuBaseAether;
import net.aetherteam.mainmenu_api.MenuBaseLeftMinecraft;
import net.aetherteam.mainmenu_api.MenuBaseMinecraft;
import net.aetherteam.mainmenu_api.MenuCommonProxy;
import net.aetherteam.mainmenu_api.MenuSoundLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.src.ModLoader;
import net.minecraftforge.common.MinecraftForge;

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
        this.installSound("streaming/Aether Menu.ogg");
        this.installSound("streaming/Aether Menu Two.wav");
        MinecraftForge.EVENT_BUS.register(new MenuSoundLoader());
    }

    private void installSound(String var1)
    {
        File var2 = new File(ModLoader.getMinecraftInstance().mcDataDir, "resources/" + var1);

        if (!var2.exists())
        {
            try
            {
                String var3 = soundZipPath + var1;
                InputStream var4 = MainMenuAPI.class.getResourceAsStream(var3);

                if (var4 == null)
                {
                    throw new IOException();
                }

                if (!var2.getParentFile().exists())
                {
                    var2.getParentFile().mkdirs();
                }

                BufferedInputStream var5 = new BufferedInputStream(var4);
                BufferedOutputStream var6 = new BufferedOutputStream(new FileOutputStream(var2));
                byte[] var7 = new byte[1024];
                boolean var8 = false;
                int var10;

                while (-1 != (var10 = var5.read(var7)))
                {
                    var6.write(var7, 0, var10);
                }

                var5.close();
                var6.close();
            }
            catch (IOException var9)
            {
                ;
            }
        }

        if (var2.canRead() && var2.isFile())
        {
            ModLoader.getMinecraftInstance().installResource(var1, var2);
        }
        else
        {
            System.err.println("Could not load file: " + var2);
        }
    }
}
