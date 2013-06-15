package net.aetherteam.aether.packets;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;
import java.util.Iterator;
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
        this.packetID = (byte) packetID;
        RegisteredPackets.registerPacket(this);
    }

    public abstract void onPacketReceived(Packet250CustomPayload var1, Player var2);

    public void sendPacketToAllExcept(Packet packet, Player player)
    {
        Side side = FMLCommonHandler.instance().getEffectiveSide();

        if (side.isServer())
        {
            MinecraftServer var4 = FMLCommonHandler.instance().getMinecraftServerInstance();

            if (var4 != null)
            {
                ServerConfigurationManager var5 = var4.getConfigurationManager();

                for (Object var7 : var5.playerEntityList)
                {
                    if (var7 instanceof EntityPlayer && var7 != player)
                    {
                        PacketDispatcher.sendPacketToPlayer(packet, (Player) var7);
                    }
                }
            }
        }
    }
}
