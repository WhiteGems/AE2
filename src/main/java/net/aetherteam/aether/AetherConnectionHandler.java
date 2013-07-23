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

public class AetherConnectionHandler implements IConnectionHandler
{
    public void playerLoggedIn(Player var1, NetHandler var2, INetworkManager var3) {}

    public String connectionReceived(NetLoginHandler var1, INetworkManager var2)
    {
        return null;
    }

    public void connectionOpened(NetHandler var1, String var2, int var3, INetworkManager var4) {}

    public void connectionClosed(INetworkManager var1) {}

    public void clientLoggedIn(NetHandler var1, INetworkManager var2, Packet1Login var3)
    {
        NotificationHandler.instance().clearNotifications();
        DungeonHandler.instance().loadInstances(new ArrayList());
    }

    public void connectionOpened(NetHandler var1, MinecraftServer var2, INetworkManager var3) {}
}
