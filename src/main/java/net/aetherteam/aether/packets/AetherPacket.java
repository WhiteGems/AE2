package net.aetherteam.aether.packets;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;

import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;

public abstract class AetherPacket
{
    public byte packetID;

    public AetherPacket(int packetID)
    {
        this.packetID = Byte.valueOf((byte) packetID).byteValue();

        RegisteredPackets.registerPacket(this);
    }

    public abstract void onPacketReceived(Packet250CustomPayload paramPacket250CustomPayload, Player paramPlayer);

    public void sendPacketToAllExcept(Packet packet, Player player)
    {
        Side side = FMLCommonHandler.instance().getEffectiveSide();
        Iterator i$;
        if (side.isServer())
        {
            MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();

            if (server != null)
            {
                ServerConfigurationManager configManager = server.getConfigurationManager();

                for (i$ = configManager.playerEntityList.iterator(); i$.hasNext(); )
                {
                    Object playerObj = i$.next();

                    if (((playerObj instanceof EntityPlayer)) && ((Player) playerObj != player))
                    {
                        PacketDispatcher.sendPacketToPlayer(packet, (Player) playerObj);
                    }
                }
            }
        }
    }
}

/* Location:           D:\Dev\Mc\forge_orl\mcp\jars\bin\aether.jar
 * Qualified Name:     net.aetherteam.aether.packets.AetherPacket
 * JD-Core Version:    0.6.2
 */