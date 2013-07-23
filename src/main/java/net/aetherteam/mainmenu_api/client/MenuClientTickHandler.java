package net.aetherteam.mainmenu_api.client;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import java.util.EnumSet;
import net.aetherteam.mainmenu_api.MainMenuAPI;
import net.aetherteam.mainmenu_api.MenuBaseConfig;
import net.aetherteam.mainmenu_api.MenuBaseLoaderWithSlider;
import net.aetherteam.mainmenu_api.MenuBaseMinecraft;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundManager;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;

public class MenuClientTickHandler implements ITickHandler
{
    public SoundManager soundManager;

    public MenuClientTickHandler()
    {
        this.soundManager = MainMenuAPI.proxy.getClient().sndManager;
    }

    public String getLabel()
    {
        return null;
    }

    public void tickEnd(EnumSet var1, Object ... var2)
    {
        if (var1.equals(EnumSet.of(TickType.CLIENT)))
        {
            GuiScreen var3 = Minecraft.getMinecraft().currentScreen;
            MenuBaseMinecraft var4 = new MenuBaseMinecraft();

            if (var3 instanceof GuiMainMenu && var3.getClass() != var4.getClass())
            {
                Minecraft.getMinecraft().displayGuiScreen(new MenuBaseLoaderWithSlider());
            }
        }
    }

    public EnumSet ticks()
    {
        return EnumSet.of(TickType.CLIENT);
    }

    public void tickStart(EnumSet var1, Object ... var2)
    {
        if (Minecraft.getMinecraft().isIntegratedServerRunning())
        {
            ++MenuBaseConfig.ticks;

            if (Minecraft.getMinecraft().thePlayer != null)
            {
                MenuBaseConfig.playerPosX = Minecraft.getMinecraft().thePlayer.posX;
                MenuBaseConfig.playerPosY = Minecraft.getMinecraft().thePlayer.posY;
                MenuBaseConfig.playerPosZ = Minecraft.getMinecraft().thePlayer.posZ;
            }

            if (MenuBaseConfig.endMusic)
            {
                SoundManager var10000 = this.soundManager;

                if (SoundManager.sndSystem != null)
                {
                    var10000 = this.soundManager;

                    if (SoundManager.sndSystem.playing("streaming") && MenuBaseConfig.ticks < 10)
                    {
                        MenuBaseConfig.endMusic = false;
                        System.out.println("Stopping rogue music.");
                        var10000 = this.soundManager;
                        SoundManager.sndSystem.stop("streaming");
                    }
                }
            }
        }
    }
}
