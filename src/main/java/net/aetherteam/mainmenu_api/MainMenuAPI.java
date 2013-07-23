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

@Mod(
    modid = "MainMenuAPI",
    name = "Main Menu API",
    version = "1.0.0"
)
@NetworkMod(
    clientSideRequired = true,
    serverSideRequired = true
)
public class MainMenuAPI
{
    @Mod.Instance("MainMenuAPI")
    private static MainMenuAPI instance;
    @SidedProxy(
        clientSide = "net.aetherteam.mainmenu_api.client.MenuClientProxy",
        serverSide = "net.aetherteam.mainmenu_api.MenuCommonProxy"
    )
    public static MenuCommonProxy proxy;

    public static MainMenuAPI getInstance()
    {
        return instance;
    }

    @Mod.Init
    public void load(FMLInitializationEvent var1)
    {
        proxy.registerTickHandler();
    }

    @Mod.PostInit
    public void postInit(FMLPostInitializationEvent var1) {}

    @Mod.PreInit
    public void preInit(FMLPreInitializationEvent var1)
    {
        proxy.registerSounds();
    }

    public static void registerMenu(String var0, Class var1)
    {
        if (var0 == null)
        {
            throw new NullPointerException("A Menu Base string is null!");
        }
        else if (var1 == null)
        {
            throw new NullPointerException("The Menu Base \'" + var0 + "\' has a null MenuBase class!");
        }
        else
        {
            if (MenuBaseSorter.isMenuRegistered(var0))
            {
                System.out.println("Menu Base \'" + var1 + "\' with name \'" + var0 + "\' is already registered!");
            }

            System.out.println("Menu Base \'" + var1 + "\' with name \'" + var0 + "\' has been registered.");
            MenuBaseSorter.addMenuToSorter(var0, var1);
        }
    }
}
