package net.aetherteam.mainmenu_api.client;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import java.io.PrintStream;
import java.util.EnumSet;
import net.aetherteam.mainmenu_api.MainMenuAPI;
import net.aetherteam.mainmenu_api.MenuBaseConfig;
import net.aetherteam.mainmenu_api.MenuBaseLoaderWithSlider;
import net.aetherteam.mainmenu_api.MenuBaseMinecraft;
import net.aetherteam.mainmenu_api.MenuCommonProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundPool;
import net.minecraft.client.audio.SoundPoolEntry;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.CallableMPL2;
import paulscode.sound.SoundSystem;

public class MenuClientTickHandler
    implements ITickHandler
{
    public SoundPoolEntry soundManager = MainMenuAPI.proxy.getClient().sndManager;

    public String getLabel()
    {
        return null;
    }

    public void tickEnd(EnumSet type, Object[] tickData)
    {
        if (type.equals(EnumSet.of(TickType.CLIENT)))
        {
            GuiScreen guiscreen = Minecraft.getMinecraft().currentScreen;
            MenuBaseMinecraft minecraftMenu = new MenuBaseMinecraft();

            if (((guiscreen instanceof SoundPool)) && (guiscreen.getClass() != minecraftMenu.getClass()))
            {
                Minecraft.getMinecraft().displayGuiScreen(new MenuBaseLoaderWithSlider());
            }
        }
    }

    public EnumSet ticks()
    {
        return EnumSet.of(TickType.CLIENT);
    }

    public void tickStart(EnumSet type, Object[] tickData)
    {
        if (Minecraft.getMinecraft().isIntegratedServerRunning())
        {
            MenuBaseConfig.ticks += 1;

            if (Minecraft.getMinecraft().thePlayer != null)
            {
                MenuBaseConfig.playerPosX = Minecraft.getMinecraft().thePlayer.u;
                MenuBaseConfig.playerPosY = Minecraft.getMinecraft().thePlayer.v;
                MenuBaseConfig.playerPosZ = Minecraft.getMinecraft().thePlayer.w;
            }

            if ((MenuBaseConfig.endMusic) && (SoundPoolEntry.soundName != null) && (SoundPoolEntry.soundName.playing("streaming")) && (MenuBaseConfig.ticks < 10))
            {
                MenuBaseConfig.endMusic = false;
                System.out.println("Stopping rogue music.");
                SoundPoolEntry.soundName.stop("streaming");
            }
        }
    }
}

