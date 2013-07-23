package net.aetherteam.aether.packets;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.HashMap;
import java.util.HashSet;
import net.aetherteam.aether.Aether;
import net.aetherteam.aether.containers.InventoryAether;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.packet.Packet250CustomPayload;

public class PacketAccessoryChange extends AetherPacket
{
    public PacketAccessoryChange(int var1)
    {
        super(var1);
    }

    public void onPacketReceived(Packet250CustomPayload var1, Player var2)
    {
        DataInputStream var3 = new DataInputStream(new ByteArrayInputStream(var1.data));

        try
        {
            byte var4 = var3.readByte();
            NBTTagList var5 = (NBTTagList)NBTTagList.readNamedTag(var3);
            boolean var6 = var3.readBoolean();
            boolean var7 = var3.readBoolean();
            short var8 = var3.readShort();
            byte var9 = var3.readByte();

            if (var9 == 1)
            {
                HashMap var10 = Aether.proxy.getClientInventories();

                if (var6)
                {
                    var10.clear();
                }

                InventoryAether var11 = new InventoryAether((EntityPlayer)var2);
                var11.readFromNBT(var5);

                for (int var12 = 0; var12 < var8; ++var12)
                {
                    String var13 = var3.readUTF();

                    if (var7)
                    {
                        var10.put(var13, var11);
                    }
                    else
                    {
                        var10.remove(var13);
                    }
                }
            }
            else
            {
                HashSet var16 = new HashSet();

                for (int var15 = 0; var15 < var8; ++var15)
                {
                    String var17 = var3.readUTF();
                    var16.add(var17);
                }

                PacketDispatcher.sendPacketToAllPlayers(AetherPacketHandler.sendAccessoryChange(var5, var6, var7, var16, (byte)1));
            }
        }
        catch (Exception var14)
        {
            var14.printStackTrace();
        }
    }
}
