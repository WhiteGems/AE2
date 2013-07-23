package net.aetherteam.aether.packets;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import net.aetherteam.aether.Aether;
import net.aetherteam.aether.CommonProxy;
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
            byte packetType = dat.readByte();
            NBTTagList nbttaglist = (NBTTagList)NBTTagList.readNamedTag(dat);
            boolean clearFirst = dat.readBoolean();
            boolean adding = dat.readBoolean();
            short length = dat.readShort();
            byte proxy = dat.readByte();

            if (proxy == 1)
            {
                HashMap inventories = Aether.proxy.getClientInventories();

                if (clearFirst)
                {
                    inventories.clear();
                }

                InventoryAether inv = new InventoryAether((EntityPlayer)player);
                inv.readFromNBT(nbttaglist);

                for (int i = 0; i < length; i++)
                {
                    String username = dat.readUTF();

                    if (adding)
                    {
                        inventories.put(username, inv);
                    }
                    else
                    {
                        inventories.remove(username);
                    }
                }
            }
            else
            {
                Set set = new HashSet();

                for (int i = 0; i < length; i++)
                {
                    String username = dat.readUTF();
                    set.add(username);
                }

                PacketDispatcher.sendPacketToAllPlayers(AetherPacketHandler.sendAccessoryChange(nbttaglist, clearFirst, adding, set, (byte)1));
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}

