package net.aetherteam.aether;

import cpw.mods.fml.common.network.IConnectionHandler;
import cpw.mods.fml.common.network.Player;
import java.util.ArrayList;
import net.aetherteam.aether.dungeons.DungeonHandler;
import net.aetherteam.aether.notifications.NotificationHandler;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.NetLoginHandler;
import net.minecraft.network.packet.NetHandler;
import net.minecraft.network.packet.Packet1Login;
import net.minecraft.server.MinecraftServer;

public class AetherConnectionHandler
    implements IConnectionHandler
{
    public void playerLoggedIn(Player player, NetHandler netHandler, INetworkManager manager)
    {
    }

    public String connectionReceived(NetLoginHandler netHandler, INetworkManager manager)
    {
        return null;
    }

    public void connectionOpened(NetHandler netClientHandler, String server, int port, INetworkManager manager)
    {
    }

    public void connectionClosed(INetworkManager manager)
    {
    }

    public void clientLoggedIn(NetHandler clientHandler, INetworkManager manager, Packet1Login login)
    {
        NotificationHandler.instance().clearNotifications();
        DungeonHandler.instance().loadInstances(new ArrayList());
    }

    public void connectionOpened(NetHandler netClientHandler, MinecraftServer server, INetworkManager manager)
    {
    }
}

