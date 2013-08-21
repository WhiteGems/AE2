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
    public PacketAccessoryChange(int packetID)
    {
        super(packetID);
    }

    public void onPacketReceived(Packet250CustomPayload packet, Player player)
    {
        DataInputStream dat = new DataInputStream(new ByteArrayInputStream(packet.data));

        try
        {
            byte ex = dat.readByte();
            NBTTagList nbttaglist = (NBTTagList)NBTTagList.readNamedTag(dat);
            boolean clearFirst = dat.readBoolean();
            boolean adding = dat.readBoolean();
            short length = dat.readShort();
            byte proxy = dat.readByte();

            if (proxy == 1)
            {
                HashMap set = Aether.proxy.getClientInventories();

                if (clearFirst)
                {
                    set.clear();
                }

                InventoryAether i = new InventoryAether((EntityPlayer)player);
                i.readFromNBT(nbttaglist);

                for (int username = 0; username < length; ++username)
                {
                    String username1 = dat.readUTF();

                    if (adding)
                    {
                        set.put(username1, i);
                    }
                    else
                    {
                        set.remove(username1);
                    }
                }
            }
            else
            {
                HashSet var16 = new HashSet();

                for (int var15 = 0; var15 < length; ++var15)
                {
                    String var17 = dat.readUTF();
                    var16.add(var17);
                }

                PacketDispatcher.sendPacketToAllPlayers(AetherPacketHandler.sendAccessoryChange(nbttaglist, clearFirst, adding, var16, (byte)1));
            }
        }
        catch (Exception var14)
        {
            var14.printStackTrace();
        }
    }
}
