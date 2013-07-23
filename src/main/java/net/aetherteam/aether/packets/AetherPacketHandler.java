package net.aetherteam.aether.packets;

import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import net.aetherteam.aether.data.PlayerClientInfo;
import net.aetherteam.aether.donator.Donator;
import net.aetherteam.aether.donator.DonatorChoice;
import net.aetherteam.aether.donator.EnumChoiceType;
import net.aetherteam.aether.dungeons.Dungeon;
import net.aetherteam.aether.dungeons.keys.EnumKeyType;
import net.aetherteam.aether.entities.mounts.MountInput;
import net.aetherteam.aether.notifications.Notification;
import net.aetherteam.aether.party.Party;
import net.aetherteam.aether.party.PartyType;
import net.aetherteam.aether.party.members.MemberType;
import net.aetherteam.aether.party.members.PartyMember;
import net.aetherteam.aether.tile_entities.TileEntityBronzeDoorController;
import net.aetherteam.aether.tile_entities.TileEntityEntranceController;
import net.aetherteam.aether.worldgen.StructureBoundingBoxSerial;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.util.MathHelper;

public class AetherPacketHandler implements IPacketHandler
{
    public void onPacketData(INetworkManager var1, Packet250CustomPayload var2, Player var3)
    {
        DataInputStream var4 = new DataInputStream(new ByteArrayInputStream(var2.data));

        try
        {
            byte var5 = var4.readByte();
            ArrayList var6 = RegisteredPackets.getPackets();
            Iterator var7 = var6.iterator();

            while (var7.hasNext())
            {
                AetherPacket var8 = (AetherPacket)var7.next();

                if (var8.packetID == var5)
                {
                    var8.onPacketReceived(var2, var3);
                    return;
                }
            }
        }
        catch (IOException var9)
        {
            var9.printStackTrace();
        }
    }

    public static Packet sendRidingPacket(Entity var0)
    {
        ByteArrayOutputStream var1 = new ByteArrayOutputStream(8);
        DataOutputStream var2 = new DataOutputStream(var1);

        try
        {
            var2.writeByte(RegisteredPackets.riding.packetID);
            var2.writeInt(var0 == null ? -1 : var0.entityId);
        }
        catch (Exception var4)
        {
            var4.printStackTrace();
        }

        Packet250CustomPayload var3 = new Packet250CustomPayload();
        var3.channel = "Aether";
        var3.data = var1.toByteArray();
        var3.length = var1.size();
        return var3;
    }

    public static Packet sendDonatorChange(String var0, Donator var1)
    {
        ByteArrayOutputStream var2 = new ByteArrayOutputStream();
        DataOutputStream var3 = new DataOutputStream(var2);

        try
        {
            var3.writeByte(RegisteredPackets.donatorChange.packetID);
            var3.writeUTF(var0.toLowerCase());
            var3.writeUTF(var1.getRSA());
        }
        catch (IOException var5)
        {
            var5.printStackTrace();
        }

        Packet250CustomPayload var4 = new Packet250CustomPayload();
        var4.channel = "Aether";
        var4.data = var2.toByteArray();
        var4.length = var2.size();
        var4.isChunkDataPacket = true;
        return var4;
    }

    public static Packet sendDonatorChoice(String var0, DonatorChoice var1, boolean var2, byte var3)
    {
        ByteArrayOutputStream var4 = new ByteArrayOutputStream();
        DataOutputStream var5 = new DataOutputStream(var4);

        try
        {
            var5.writeByte(RegisteredPackets.donatorChoice.packetID);
            var5.writeUTF(var0);
            var5.writeUTF(var1.name);
            var5.writeBoolean(var2);
            var5.writeByte(var3);
        }
        catch (IOException var7)
        {
            var7.printStackTrace();
        }

        Packet250CustomPayload var6 = new Packet250CustomPayload();
        var6.channel = "Aether";
        var6.data = var4.toByteArray();
        var6.length = var4.size();
        var6.isChunkDataPacket = true;
        return var6;
    }

    public static Packet sendDonatorTypeRemoval(String var0, EnumChoiceType var1, byte var2)
    {
        ByteArrayOutputStream var3 = new ByteArrayOutputStream();
        DataOutputStream var4 = new DataOutputStream(var3);

        try
        {
            var4.writeByte(RegisteredPackets.donatorTypeRemoval.packetID);
            var4.writeUTF(var0);
            var4.writeUTF(var1.name);
            var4.writeByte(var2);
        }
        catch (IOException var6)
        {
            var6.printStackTrace();
        }

        Packet250CustomPayload var5 = new Packet250CustomPayload();
        var5.channel = "Aether";
        var5.data = var3.toByteArray();
        var5.length = var3.size();
        var5.isChunkDataPacket = true;
        return var5;
    }

    public static Packet sendAccessoryChange(NBTTagList var0, boolean var1, boolean var2, Set var3, byte var4)
    {
        ByteArrayOutputStream var5 = new ByteArrayOutputStream();
        DataOutputStream var6 = new DataOutputStream(var5);

        try
        {
            var6.writeByte(RegisteredPackets.accessoryChange.packetID);
            NBTTagList.writeNamedTag(var0, var6);
            var6.writeBoolean(var1);
            var6.writeBoolean(var2);
            var6.writeShort(var3.size());
            var6.writeByte(var4);
            Iterator var7 = var3.iterator();

            while (var7.hasNext())
            {
                String var8 = (String)var7.next();
                var6.writeUTF(var8);
            }
        }
        catch (IOException var9)
        {
            var9.printStackTrace();
        }

        Packet250CustomPayload var10 = new Packet250CustomPayload();
        var10.channel = "Aether";
        var10.data = var5.toByteArray();
        var10.length = var5.size();
        var10.isChunkDataPacket = true;
        return var10;
    }

    public static Packet sendHeartChange(boolean var0, boolean var1, int var2, Set var3)
    {
        ByteArrayOutputStream var4 = new ByteArrayOutputStream();
        DataOutputStream var5 = new DataOutputStream(var4);

        try
        {
            var5.writeByte(RegisteredPackets.heartChange.packetID);
            var5.writeBoolean(var0);
            var5.writeBoolean(var1);
            var5.writeShort(var3.size());
            var5.writeInt(var2);
            Iterator var6 = var3.iterator();

            while (var6.hasNext())
            {
                String var7 = (String)var6.next();
                var5.writeUTF(var7);
            }
        }
        catch (IOException var8)
        {
            var8.printStackTrace();
        }

        Packet250CustomPayload var9 = new Packet250CustomPayload();
        var9.channel = "Aether";
        var9.data = var4.toByteArray();
        var9.length = var4.size();
        var9.isChunkDataPacket = true;
        return var9;
    }

    public static Packet sendCooldown(boolean var0, boolean var1, int var2, int var3, String var4, Set var5)
    {
        ByteArrayOutputStream var6 = new ByteArrayOutputStream();
        DataOutputStream var7 = new DataOutputStream(var6);

        try
        {
            var7.writeByte(RegisteredPackets.cooldown.packetID);
            var7.writeBoolean(var0);
            var7.writeBoolean(var1);
            var7.writeShort(var5.size());
            var7.writeInt(var2);
            var7.writeInt(var3);
            var7.writeUTF(var4);
            Iterator var8 = var5.iterator();

            while (var8.hasNext())
            {
                String var9 = (String)var8.next();
                var7.writeUTF(var9);
            }
        }
        catch (IOException var10)
        {
            var10.printStackTrace();
        }

        Packet250CustomPayload var11 = new Packet250CustomPayload();
        var11.channel = "Aether";
        var11.data = var6.toByteArray();
        var11.length = var6.size();
        var11.isChunkDataPacket = true;
        return var11;
    }

    public static Packet sendCoinChange(boolean var0, boolean var1, int var2, Set var3)
    {
        ByteArrayOutputStream var4 = new ByteArrayOutputStream();
        DataOutputStream var5 = new DataOutputStream(var4);

        try
        {
            var5.writeByte(RegisteredPackets.coinChange.packetID);
            var5.writeBoolean(var0);
            var5.writeBoolean(var1);
            var5.writeShort(var3.size());
            var5.writeInt(var2);
            Iterator var6 = var3.iterator();

            while (var6.hasNext())
            {
                String var7 = (String)var6.next();
                var5.writeUTF(var7);
            }
        }
        catch (IOException var8)
        {
            var8.printStackTrace();
        }

        Packet250CustomPayload var9 = new Packet250CustomPayload();
        var9.channel = "Aether";
        var9.data = var4.toByteArray();
        var9.length = var4.size();
        var9.isChunkDataPacket = true;
        return var9;
    }

    public static Packet sendPartyChange(boolean var0, String var1, String var2, String var3)
    {
        ByteArrayOutputStream var4 = new ByteArrayOutputStream();
        DataOutputStream var5 = new DataOutputStream(var4);

        try
        {
            var5.writeByte(RegisteredPackets.partyChange.packetID);
            var5.writeBoolean(var0);
            var5.writeUTF(var1);
            var5.writeUTF(var2);
            var5.writeUTF(var3);
        }
        catch (IOException var7)
        {
            var7.printStackTrace();
        }

        Packet250CustomPayload var6 = new Packet250CustomPayload();
        var6.channel = "Aether";
        var6.data = var4.toByteArray();
        var6.length = var4.size();
        var6.isChunkDataPacket = true;
        return var6;
    }

    public static Packet sendPartyNameChange(String var0, String var1)
    {
        ByteArrayOutputStream var2 = new ByteArrayOutputStream();
        DataOutputStream var3 = new DataOutputStream(var2);

        try
        {
            var3.writeByte(RegisteredPackets.partyNameChange.packetID);
            var3.writeUTF(var0);
            var3.writeUTF(var1);
        }
        catch (IOException var5)
        {
            var5.printStackTrace();
        }

        Packet250CustomPayload var4 = new Packet250CustomPayload();
        var4.channel = "Aether";
        var4.data = var2.toByteArray();
        var4.length = var2.size();
        var4.isChunkDataPacket = true;
        return var4;
    }

    public static Packet sendPartyMemberChange(boolean var0, String var1, String var2, String var3)
    {
        ByteArrayOutputStream var4 = new ByteArrayOutputStream();
        DataOutputStream var5 = new DataOutputStream(var4);

        try
        {
            var5.writeByte(RegisteredPackets.memberChange.packetID);
            var5.writeBoolean(var0);
            var5.writeUTF(var1);
            var5.writeUTF(var2);
            var5.writeUTF(var3);
        }
        catch (IOException var7)
        {
            var7.printStackTrace();
        }

        Packet250CustomPayload var6 = new Packet250CustomPayload();
        var6.channel = "Aether";
        var6.data = var4.toByteArray();
        var6.length = var4.size();
        var6.isChunkDataPacket = true;
        return var6;
    }

    public static Packet sendPartyTypeChange(String var0, PartyType var1)
    {
        ByteArrayOutputStream var2 = new ByteArrayOutputStream();
        DataOutputStream var3 = new DataOutputStream(var2);

        try
        {
            var3.writeByte(RegisteredPackets.partyTypeChange.packetID);
            var3.writeUTF(var0);
            var3.writeUTF(var1.name());
        }
        catch (IOException var5)
        {
            var5.printStackTrace();
        }

        Packet250CustomPayload var4 = new Packet250CustomPayload();
        var4.channel = "Aether";
        var4.data = var2.toByteArray();
        var4.length = var2.size();
        var4.isChunkDataPacket = true;
        return var4;
    }

    public static Packet sendNotificationChange(Notification var0, boolean var1)
    {
        ByteArrayOutputStream var2 = new ByteArrayOutputStream();
        DataOutputStream var3 = new DataOutputStream(var2);

        try
        {
            var3.writeByte(RegisteredPackets.notification.packetID);
            var3.writeBoolean(var1);
            var3.writeUTF(var0.getType().name());
            var3.writeUTF(var0.getHeaderText());
            var3.writeUTF(var0.getSenderName());
            var3.writeUTF(var0.getReceiverName());
        }
        catch (IOException var5)
        {
            var5.printStackTrace();
        }

        Packet250CustomPayload var4 = new Packet250CustomPayload();
        var4.channel = "Aether";
        var4.data = var2.toByteArray();
        var4.length = var2.size();
        var4.isChunkDataPacket = true;
        return var4;
    }

    public static Packet sendPlayerInput(String var0, ArrayList var1, boolean var2)
    {
        ByteArrayOutputStream var3 = new ByteArrayOutputStream();
        DataOutputStream var4 = new DataOutputStream(var3);

        try
        {
            var4.writeByte(RegisteredPackets.playerInput.packetID);
            var4.writeUTF(var0);
            var4.writeInt(var1.size());
            Iterator var5 = var1.iterator();

            while (var5.hasNext())
            {
                MountInput var6 = (MountInput)var5.next();
                var4.writeUTF(var6.name());
            }

            var4.writeBoolean(var2);
        }
        catch (IOException var7)
        {
            var7.printStackTrace();
        }

        Packet250CustomPayload var8 = new Packet250CustomPayload();
        var8.channel = "Aether";
        var8.data = var3.toByteArray();
        var8.length = var3.size();
        var8.isChunkDataPacket = true;
        return var8;
    }

    public static Packet sendAllParties(ArrayList var0)
    {
        ByteArrayOutputStream var1 = new ByteArrayOutputStream();
        DataOutputStream var2 = new DataOutputStream(var1);

        try
        {
            var2.writeByte(RegisteredPackets.allParties.packetID);
            var2.writeInt(var0.size());
            Iterator var3 = var0.iterator();

            while (var3.hasNext())
            {
                Party var4 = (Party)var3.next();
                var2.writeUTF(var4.getName());
                var2.writeUTF(var4.getLeader().username);
                var2.writeUTF(var4.getType().name());
                var2.writeInt(var4.getMemberSizeLimit());
                var2.writeInt(var4.getMembers().size());
                Iterator var5 = var4.getMembers().iterator();

                while (var5.hasNext())
                {
                    PartyMember var6 = (PartyMember)var5.next();
                    var2.writeUTF(var6.username);
                    var2.writeUTF(var6.getType().name());
                }
            }
        }
        catch (IOException var7)
        {
            var7.printStackTrace();
        }

        Packet250CustomPayload var8 = new Packet250CustomPayload();
        var8.channel = "Aether";
        var8.data = var1.toByteArray();
        var8.length = var1.size();
        var8.isChunkDataPacket = true;
        return var8;
    }

    public static Packet sendRequestPlayer(boolean var0, String var1, String var2, String var3, String var4)
    {
        ByteArrayOutputStream var5 = new ByteArrayOutputStream();
        DataOutputStream var6 = new DataOutputStream(var5);

        try
        {
            var6.writeByte(RegisteredPackets.requestPlayer.packetID);
            var6.writeBoolean(var0);
            var6.writeUTF(var1);
            var6.writeUTF(var2);
            var6.writeUTF(var3);
            var6.writeUTF(var4);
        }
        catch (IOException var8)
        {
            var8.printStackTrace();
        }

        Packet250CustomPayload var7 = new Packet250CustomPayload();
        var7.channel = "Aether";
        var7.data = var5.toByteArray();
        var7.length = var5.size();
        var7.isChunkDataPacket = true;
        return var7;
    }

    public static Packet sendPlayerClientInfo(boolean var0, boolean var1, String var2, PlayerClientInfo var3)
    {
        ByteArrayOutputStream var4 = new ByteArrayOutputStream();
        DataOutputStream var5 = new DataOutputStream(var4);

        try
        {
            var5.writeByte(RegisteredPackets.playerClientInfo.packetID);
            var5.writeBoolean(var0);
            var5.writeBoolean(var1);
            var5.writeUTF(var2);
            var5.writeShort(var3.getHalfHearts());
            var5.writeShort(var3.getMaxHealth());
            var5.writeShort(var3.getHunger());
            var5.writeShort(var3.getArmourValue());
            var5.writeInt(var3.getAetherCoins());
        }
        catch (IOException var7)
        {
            var7.printStackTrace();
        }

        Packet250CustomPayload var6 = new Packet250CustomPayload();
        var6.channel = "Aether";
        var6.data = var4.toByteArray();
        var6.length = var4.size();
        var6.isChunkDataPacket = true;
        return var6;
    }

    public static Packet sendMemberTypeChange(String var0, MemberType var1)
    {
        ByteArrayOutputStream var2 = new ByteArrayOutputStream();
        DataOutputStream var3 = new DataOutputStream(var2);

        try
        {
            var3.writeByte(RegisteredPackets.memberTypeChange.packetID);
            var3.writeUTF(var0);
            var3.writeUTF(var1.name());
        }
        catch (IOException var5)
        {
            var5.printStackTrace();
        }

        Packet250CustomPayload var4 = new Packet250CustomPayload();
        var4.channel = "Aether";
        var4.data = var2.toByteArray();
        var4.length = var2.size();
        var4.isChunkDataPacket = true;
        return var4;
    }

    public static Packet sendDungeonQueueChange(boolean var0, Dungeon var1, int var2, int var3, int var4, Party var5)
    {
        ByteArrayOutputStream var6 = new ByteArrayOutputStream();
        DataOutputStream var7 = new DataOutputStream(var6);

        try
        {
            var7.writeByte(RegisteredPackets.dungeonQueueChange.packetID);
            var7.writeBoolean(var0);
            var7.writeInt(var1.getID());
            var7.writeUTF(var5.getName());
            var7.writeInt(var2);
            var7.writeInt(var3);
            var7.writeInt(var4);
        }
        catch (IOException var9)
        {
            var9.printStackTrace();
        }

        Packet250CustomPayload var8 = new Packet250CustomPayload();
        var8.channel = "Aether";
        var8.data = var6.toByteArray();
        var8.length = var6.size();
        var8.isChunkDataPacket = true;
        return var8;
    }

    public static Packet sendDungeonChange(boolean var0, Dungeon var1)
    {
        ByteArrayOutputStream var2 = new ByteArrayOutputStream();
        DataOutputStream var3 = new DataOutputStream(var2);

        try
        {
            var3.writeByte(RegisteredPackets.dungeonChange.packetID);
            var3.writeBoolean(var0);
            var3.writeInt(var1.getID());

            if (var0)
            {
                var3.writeUTF(var1.getType().name());
                var3.writeInt(var1.centerX);
                var3.writeInt(var1.centerZ);
                var3.writeInt(var1.boundingBoxes.size());
                var3.writeInt(var1.boundingBox.minX);
                var3.writeInt(var1.boundingBox.minY);
                var3.writeInt(var1.boundingBox.minZ);
                var3.writeInt(var1.boundingBox.maxX);
                var3.writeInt(var1.boundingBox.maxY);
                var3.writeInt(var1.boundingBox.maxZ);
                Iterator var4 = var1.boundingBoxes.iterator();

                while (var4.hasNext())
                {
                    StructureBoundingBoxSerial var5 = (StructureBoundingBoxSerial)var4.next();
                    var3.writeInt(var5.minX);
                    var3.writeInt(var5.minY);
                    var3.writeInt(var5.minZ);
                    var3.writeInt(var5.maxX);
                    var3.writeInt(var5.maxY);
                    var3.writeInt(var5.maxZ);
                }
            }
        }
        catch (IOException var6)
        {
            var6.printStackTrace();
        }

        Packet250CustomPayload var7 = new Packet250CustomPayload();
        var7.channel = "Aether";
        var7.data = var2.toByteArray();
        var7.length = var2.size();
        var7.isChunkDataPacket = true;
        return var7;
    }

    public static Packet sendDungeonMemberQueue(Dungeon var0, PartyMember var1)
    {
        ByteArrayOutputStream var2 = new ByteArrayOutputStream();
        DataOutputStream var3 = new DataOutputStream(var2);

        try
        {
            var3.writeByte(RegisteredPackets.dungeonMemberQueue.packetID);
            var3.writeInt(var0.getID());
            var3.writeUTF(var1.username);
        }
        catch (IOException var5)
        {
            var5.printStackTrace();
        }

        Packet250CustomPayload var4 = new Packet250CustomPayload();
        var4.channel = "Aether";
        var4.data = var2.toByteArray();
        var4.length = var2.size();
        var4.isChunkDataPacket = true;
        return var4;
    }

    public static Packet sendDungeonFinish(Dungeon var0, TileEntityEntranceController var1, Party var2)
    {
        ByteArrayOutputStream var3 = new ByteArrayOutputStream();
        DataOutputStream var4 = new DataOutputStream(var3);

        try
        {
            var4.writeByte(RegisteredPackets.dungeonFinish.packetID);
            var4.writeInt(var0.getID());
            var4.writeUTF(var2.getName());
            var4.writeInt(var1.xCoord);
            var4.writeInt(var1.yCoord);
            var4.writeInt(var1.zCoord);
        }
        catch (IOException var6)
        {
            var6.printStackTrace();
        }

        Packet250CustomPayload var5 = new Packet250CustomPayload();
        var5.channel = "Aether";
        var5.data = var3.toByteArray();
        var5.length = var3.size();
        var5.isChunkDataPacket = true;
        return var5;
    }

    public static Packet sendDungeonTimerStart(Dungeon var0, Party var1, int var2)
    {
        ByteArrayOutputStream var3 = new ByteArrayOutputStream();
        DataOutputStream var4 = new DataOutputStream(var3);

        try
        {
            var4.writeByte(RegisteredPackets.dungeonTimerStart.packetID);
            var4.writeInt(var0.getID());
            var4.writeUTF(var1.getName());
            var4.writeInt(var2);
        }
        catch (IOException var6)
        {
            var6.printStackTrace();
        }

        Packet250CustomPayload var5 = new Packet250CustomPayload();
        var5.channel = "Aether";
        var5.data = var3.toByteArray();
        var5.length = var3.size();
        var5.isChunkDataPacket = true;
        return var5;
    }

    public static Packet removeDungeonKey(Dungeon var0, Party var1, EnumKeyType var2, TileEntityBronzeDoorController var3)
    {
        ByteArrayOutputStream var4 = new ByteArrayOutputStream();
        DataOutputStream var5 = new DataOutputStream(var4);

        try
        {
            var5.writeByte(RegisteredPackets.dungeonKey.packetID);
            var5.writeBoolean(false);
            var5.writeInt(var0.getID());
            var5.writeUTF(var1.getName());
            var5.writeUTF(var2.name());
            var5.writeInt(MathHelper.floor_double((double)var3.xCoord));
            var5.writeInt(MathHelper.floor_double((double)var3.yCoord));
            var5.writeInt(MathHelper.floor_double((double)var3.zCoord));
        }
        catch (IOException var7)
        {
            var7.printStackTrace();
        }

        Packet250CustomPayload var6 = new Packet250CustomPayload();
        var6.channel = "Aether";
        var6.data = var4.toByteArray();
        var6.length = var4.size();
        var6.isChunkDataPacket = true;
        return var6;
    }

    public static Packet sendDungeonKey(Dungeon var0, Party var1, EnumKeyType var2)
    {
        ByteArrayOutputStream var3 = new ByteArrayOutputStream();
        DataOutputStream var4 = new DataOutputStream(var3);

        try
        {
            var4.writeByte(RegisteredPackets.dungeonKey.packetID);
            var4.writeBoolean(true);
            var4.writeInt(var0.getID());
            var4.writeUTF(var1.getName());
            var4.writeUTF(var2.name());
        }
        catch (IOException var6)
        {
            var6.printStackTrace();
        }

        Packet250CustomPayload var5 = new Packet250CustomPayload();
        var5.channel = "Aether";
        var5.data = var3.toByteArray();
        var5.length = var3.size();
        var5.isChunkDataPacket = true;
        return var5;
    }

    public static Packet sendDungeonRespawn(Dungeon var0, Party var1)
    {
        ByteArrayOutputStream var2 = new ByteArrayOutputStream();
        DataOutputStream var3 = new DataOutputStream(var2);

        try
        {
            var3.writeByte(RegisteredPackets.dungeonRespawn.packetID);
            var3.writeInt(var0.getID());
            var3.writeUTF(var1.getName());
        }
        catch (IOException var5)
        {
            var5.printStackTrace();
        }

        Packet250CustomPayload var4 = new Packet250CustomPayload();
        var4.channel = "Aether";
        var4.data = var2.toByteArray();
        var4.length = var2.size();
        var4.isChunkDataPacket = true;
        return var4;
    }

    public static Packet sendDungeonDisbandMember(Dungeon var0, PartyMember var1)
    {
        ByteArrayOutputStream var2 = new ByteArrayOutputStream();
        DataOutputStream var3 = new DataOutputStream(var2);

        try
        {
            var3.writeByte(RegisteredPackets.dungeonDisbandMember.packetID);
            var3.writeInt(var0.getID());
            var3.writeUTF(var1.username);
        }
        catch (IOException var5)
        {
            var5.printStackTrace();
        }

        Packet250CustomPayload var4 = new Packet250CustomPayload();
        var4.channel = "Aether";
        var4.data = var2.toByteArray();
        var4.length = var2.size();
        var4.isChunkDataPacket = true;
        return var4;
    }

    public static Packet sendDungeonQueueCheck(Dungeon var0)
    {
        ByteArrayOutputStream var1 = new ByteArrayOutputStream();
        DataOutputStream var2 = new DataOutputStream(var1);

        try
        {
            var2.writeByte(RegisteredPackets.dungeonQueueCheck.packetID);
            var2.writeInt(var0.getID());
        }
        catch (IOException var4)
        {
            var4.printStackTrace();
        }

        Packet250CustomPayload var3 = new Packet250CustomPayload();
        var3.channel = "Aether";
        var3.data = var1.toByteArray();
        var3.length = var1.size();
        var3.isChunkDataPacket = true;
        return var3;
    }

    public static Packet sendParachuteCheck(boolean var0, boolean var1, boolean var2, int var3, Set var4)
    {
        ByteArrayOutputStream var5 = new ByteArrayOutputStream();
        DataOutputStream var6 = new DataOutputStream(var5);

        try
        {
            var6.writeByte(RegisteredPackets.parachute.packetID);
            var6.writeBoolean(var0);
            var6.writeBoolean(var1);
            var6.writeShort(var4.size());
            var6.writeBoolean(var2);
            var6.writeInt(var3);
            Iterator var7 = var4.iterator();

            while (var7.hasNext())
            {
                String var8 = (String)var7.next();
                var6.writeUTF(var8);
            }
        }
        catch (IOException var9)
        {
            var9.printStackTrace();
        }

        Packet250CustomPayload var10 = new Packet250CustomPayload();
        var10.channel = "Aether";
        var10.data = var5.toByteArray();
        var10.length = var5.size();
        var10.isChunkDataPacket = true;
        return var10;
    }
}
