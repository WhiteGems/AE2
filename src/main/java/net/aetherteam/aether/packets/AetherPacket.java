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

    public AetherPacket(int var1)
    {
        this.packetID = Byte.valueOf((byte)var1).byteValue();
        RegisteredPackets.registerPacket(this);
    }

    public abstract void onPacketReceived(Packet250CustomPayload var1, Player var2);

    public void sendPacketToAllExcept(Packet var1, Player var2)
    {
        Side var3 = FMLCommonHandler.instance().getEffectiveSide();

        if (var3.isServer())
        {
            MinecraftServer var4 = FMLCommonHandler.instance().getMinecraftServerInstance();

            if (var4 != null)
            {
                ServerConfigurationManager var5 = var4.getConfigurationManager();
                Iterator var6 = var5.playerEntityList.iterator();

                while (var6.hasNext())
                {
                    Object var7 = var6.next();

                    if (var7 instanceof EntityPlayer && (Player)var7 != var2)
                    {
                        PacketDispatcher.sendPacketToPlayer(var1, (Player)var7);
                    }
                }
            }
        }
    }
}
