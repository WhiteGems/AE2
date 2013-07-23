package net.aetherteam.playercore_api;

import com.google.common.eventbus.EventBus;
import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.ModMetadata;

import java.util.ArrayList;
import java.util.Arrays;

public class PlayerCoreAPI extends DummyModContainer
{
    public static ArrayList playerCoreClientList = new ArrayList();
    public static ArrayList playerCoreServerList = new ArrayList();
    public static ArrayList playerCoreRenderList = new ArrayList();

    public PlayerCoreAPI()
    {
        super(new ModMetadata());
        ModMetadata var1 = this.getMetadata();
        var1.modId = "playercoreapi";
        var1.name = "PlayerCoreAPI";
        var1.version = "0.1";
        var1.credits = "Jaryt23";
        var1.authorList = Arrays.asList(new String[] {"AetherTeam; cafaxo"});
        var1.description = "";
        var1.url = "";
        var1.updateUrl = "";
        var1.screenshots = new String[0];
        var1.logoFile = "";
    }

    public static void register(PlayerCoreType var0, Class var1)
    {
        switch (var0)
        {
            case CLIENT:
                playerCoreClientList.add(var1);
                break;

            case RENDER:
                playerCoreRenderList.add(var1);
                break;

            case SERVER:
                playerCoreServerList.add(var1);
        }
    }

    public boolean registerBus(EventBus var1, LoadController var2)
    {
        return true;
    }


    public enum PlayerCoreType
    {
        CLIENT,
        SERVER,
        RENDER;
    }

}
