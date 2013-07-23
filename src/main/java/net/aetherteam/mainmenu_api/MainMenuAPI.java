package net.aetherteam.mainmenu_api;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import java.io.PrintStream;

@Mod(modid = "MainMenuAPI", name = "Main Menu API", version = "1.0.0")
@NetworkMod(clientSideRequired = true, serverSideRequired = true)
public class MainMenuAPI
{
    @Mod.Instance("MainMenuAPI")
    private static MainMenuAPI instance;

    @SidedProxy(clientSide = "net.aetherteam.mainmenu_api.client.MenuClientProxy", serverSide = "net.aetherteam.mainmenu_api.MenuCommonProxy")
    public static MenuCommonProxy proxy;

    public static MainMenuAPI getInstance()
    {
        return instance;
    }

    @Mod.Init
    public void load(FMLInitializationEvent event)
    {
        proxy.registerTickHandler();
    }

    @Mod.PostInit
    public void postInit(FMLPostInitializationEvent event)
    {
    }

    @Mod.PreInit
    public void preInit(FMLPreInitializationEvent event)
    {
        proxy.registerSounds();
    }

    public static void registerMenu(String menuName, Class menu)
    {
        if (menuName == null)
        {
            throw new NullPointerException("A Menu Base string is null!");
        }

        if (menu == null)
        {
            throw new NullPointerException("The Menu Base '" + menuName + "' has a null MenuBase class!");
        }

        if (MenuBaseSorter.isMenuRegistered(menuName))
        {
            System.out.println("Menu Base '" + menu + "' with name '" + menuName + "' is already registered!");
        }

        System.out.println("Menu Base '" + menu + "' with name '" + menuName + "' has been registered.");
        MenuBaseSorter.addMenuToSorter(menuName, menu);
    }
}

