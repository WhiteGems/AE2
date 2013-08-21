package net.aetherteam.mainmenu_api.client;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import net.aetherteam.mainmenu_api.MainMenuAPI;
import net.aetherteam.mainmenu_api.MenuBaseAether;
import net.aetherteam.mainmenu_api.MenuBaseLeftMinecraft;
import net.aetherteam.mainmenu_api.MenuBaseMinecraft;
import net.aetherteam.mainmenu_api.MenuCommonProxy;
import net.minecraft.client.Minecraft;

public class MenuClientProxy extends MenuCommonProxy
{
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
}
