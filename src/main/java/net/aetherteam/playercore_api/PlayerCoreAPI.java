package net.aetherteam.playercore_api;

import com.google.common.eventbus.EventBus;
import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.ModMetadata;
import java.util.ArrayList;
import java.util.Arrays;

public class PlayerCoreAPI extends DummyModContainer
{
    public static ArrayList < Class<? >> playerCoreClientList = new ArrayList();
    public static ArrayList < Class<? >> playerCoreServerList = new ArrayList();
    public static ArrayList < Class<? >> playerCoreRenderList = new ArrayList();

    public PlayerCoreAPI()
    {
        super(new ModMetadata());
        ModMetadata meta = this.getMetadata();
        meta.modId = "playercoreapi";
        meta.name = "PlayerCoreAPI";
        meta.version = "0.1";
        meta.credits = "Jaryt23";
        meta.authorList = Arrays.asList(new String[] {"AetherTeam; cafaxo"});
        meta.description = "";
        meta.url = "";
        meta.updateUrl = "";
        meta.screenshots = new String[0];
        meta.logoFile = "";
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

    public boolean registerBus(EventBus bus, LoadController controller)
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
