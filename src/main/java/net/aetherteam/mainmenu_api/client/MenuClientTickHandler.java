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
import net.minecraft.client.audio.SoundManager;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.Entity;
import paulscode.sound.SoundSystem;

public class MenuClientTickHandler implements ITickHandler
{
    public SoundManager soundManager = MainMenuAPI.proxy.getClient().sndManager;

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

            if (((guiscreen instanceof GuiMainMenu)) && (guiscreen.getClass() != minecraftMenu.getClass()))
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
                MenuBaseConfig.playerPosX = Minecraft.getMinecraft().thePlayer.posX;
                MenuBaseConfig.playerPosY = Minecraft.getMinecraft().thePlayer.posY;
                MenuBaseConfig.playerPosZ = Minecraft.getMinecraft().thePlayer.posZ;
            }

            if ((MenuBaseConfig.endMusic) && (SoundManager.sndSystem != null) && (SoundManager.sndSystem.playing("streaming")) && (MenuBaseConfig.ticks < 10))
            {
                MenuBaseConfig.endMusic = false;
                System.out.println("Stopping rogue music.");
                SoundManager.sndSystem.stop("streaming");
            }
        }
    }
}

/* Location:           D:\Dev\Mc\forge_orl\mcp\jars\bin\aether.jar
 * Qualified Name:     net.aetherteam.mainmenu_api.client.MenuClientTickHandler
 * JD-Core Version:    0.6.2
 */