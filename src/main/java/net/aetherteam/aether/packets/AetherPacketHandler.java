package net.aetherteam.aether.packets;

import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import net.aetherteam.aether.data.PlayerClientInfo;
import net.aetherteam.aether.donator.Donator;
import net.aetherteam.aether.donator.DonatorChoice;
import net.aetherteam.aether.donator.EnumChoiceType;
import net.aetherteam.aether.dungeons.Dungeon;
import net.aetherteam.aether.dungeons.DungeonType;
import net.aetherteam.aether.dungeons.keys.EnumKeyType;
import net.aetherteam.aether.entities.mounts.MountInput;
import net.aetherteam.aether.notifications.Notification;
import net.aetherteam.aether.notifications.NotificationType;
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
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;

public class AetherPacketHandler
    implements IPacketHandler
{
    public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player)
    {
        DataInputStream dat = new DataInputStream(new ByteArrayInputStream(packet.data));

        try
        {
            packetType = dat.readByte();
            ArrayList packetList = RegisteredPackets.getPackets();

            for (AetherPacket aetherPacket : packetList)
            {
                if (aetherPacket.packetID == packetType)
                {
                    aetherPacket.onPacketReceived(packet, player);
                    return;
                }
            }
        }
        catch (IOException e)
        {
            byte packetType;
            e.printStackTrace();
        }
    }

    public static Packet sendRidingPacket(Entity animal)
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(8);
        DataOutputStream outputStream = new DataOutputStream(bos);

        try
        {
            outputStream.writeByte(RegisteredPackets.riding.packetID);
            outputStream.writeInt(animal == null ? -1 : animal.entityId);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        Packet250CustomPayload packet = new Packet250CustomPayload();
        packet.channel = "Aether";
        packet.data = bos.toByteArray();
        packet.length = bos.size();
        return packet;
    }

    public static Packet sendDonatorChange(String name, Donator donator)
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        try
        {
            dos.writeByte(RegisteredPackets.donatorChange.packetID);
            dos.writeUTF(name.toLowerCase());
            dos.writeUTF(donator.getRSA());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        Packet250CustomPayload pkt = new Packet250CustomPayload();
        pkt.channel = "Aether";
        pkt.data = bos.toByteArray();
        pkt.length = bos.size();
        pkt.isChunkDataPacket = true;
        return pkt;
    }

    public static Packet sendDonatorChoice(String donator, DonatorChoice choice, boolean adding, byte proxy)
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        try
        {
            dos.writeByte(RegisteredPackets.donatorChoice.packetID);
            dos.writeUTF(donator);
            dos.writeUTF(choice.name);
            dos.writeBoolean(adding);
            dos.writeByte(proxy);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        Packet250CustomPayload pkt = new Packet250CustomPayload();
        pkt.channel = "Aether";
        pkt.data = bos.toByteArray();
        pkt.length = bos.size();
        pkt.isChunkDataPacket = true;
        return pkt;
    }

    public static Packet sendDonatorTypeRemoval(String donator, EnumChoiceType type, byte proxy)
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        try
        {
            dos.writeByte(RegisteredPackets.donatorTypeRemoval.packetID);
            dos.writeUTF(donator);
            dos.writeUTF(type.name);
            dos.writeByte(proxy);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        Packet250CustomPayload pkt = new Packet250CustomPayload();
        pkt.channel = "Aether";
        pkt.data = bos.toByteArray();
        pkt.length = bos.size();
        pkt.isChunkDataPacket = true;
        return pkt;
    }

    public static Packet sendAccessoryChange(NBTTagList nbttaglist, boolean clearFirst, boolean adding, Set inventories, byte proxy)
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        try
        {
            dos.writeByte(RegisteredPackets.accessoryChange.packetID);
            NBTTagList.writeNamedTag(nbttaglist, dos);
            dos.writeBoolean(clearFirst);
            dos.writeBoolean(adding);
            dos.writeShort(inventories.size());
            dos.writeByte(proxy);

            for (String username : inventories)
            {
                dos.writeUTF(username);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        Packet250CustomPayload pkt = new Packet250CustomPayload();
        pkt.channel = "Aether";
        pkt.data = bos.toByteArray();
        pkt.length = bos.size();
        pkt.isChunkDataPacket = true;
        return pkt;
    }

    public static Packet sendHeartChange(boolean clearFirst, boolean adding, int maxHealth, Set extraHearts)
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        try
        {
            dos.writeByte(RegisteredPackets.heartChange.packetID);
            dos.writeBoolean(clearFirst);
            dos.writeBoolean(adding);
            dos.writeShort(extraHearts.size());
            dos.writeInt(maxHealth);

            for (String username : extraHearts)
            {
                dos.writeUTF(username);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        Packet250CustomPayload pkt = new Packet250CustomPayload();
        pkt.channel = "Aether";
        pkt.data = bos.toByteArray();
        pkt.length = bos.size();
        pkt.isChunkDataPacket = true;
        return pkt;
    }

    public static Packet sendCooldown(boolean clearFirst, boolean adding, int cooldown, int cooldownMax, String stackName, Set playerCooldowns)
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        try
        {
            dos.writeByte(RegisteredPackets.cooldown.packetID);
            dos.writeBoolean(clearFirst);
            dos.writeBoolean(adding);
            dos.writeShort(playerCooldowns.size());
            dos.writeInt(cooldown);
            dos.writeInt(cooldownMax);
            dos.writeUTF(stackName);

            for (String username : playerCooldowns)
            {
                dos.writeUTF(username);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        Packet250CustomPayload pkt = new Packet250CustomPayload();
        pkt.channel = "Aether";
        pkt.data = bos.toByteArray();
        pkt.length = bos.size();
        pkt.isChunkDataPacket = true;
        return pkt;
    }

    public static Packet sendCoinChange(boolean clearFirst, boolean adding, int coinAmount, Set playerCoins)
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        try
        {
            dos.writeByte(RegisteredPackets.coinChange.packetID);
            dos.writeBoolean(clearFirst);
            dos.writeBoolean(adding);
            dos.writeShort(playerCoins.size());
            dos.writeInt(coinAmount);

            for (String username : playerCoins)
            {
                dos.writeUTF(username);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        Packet250CustomPayload pkt = new Packet250CustomPayload();
        pkt.channel = "Aether";
        pkt.data = bos.toByteArray();
        pkt.length = bos.size();
        pkt.isChunkDataPacket = true;
        return pkt;
    }

    public static Packet sendPartyChange(boolean adding, String partyName, String potentialLeader, String skinUrl)
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        try
        {
            dos.writeByte(RegisteredPackets.partyChange.packetID);
            dos.writeBoolean(adding);
            dos.writeUTF(partyName);
            dos.writeUTF(potentialLeader);
            dos.writeUTF(skinUrl);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        Packet250CustomPayload pkt = new Packet250CustomPayload();
        pkt.channel = "Aether";
        pkt.data = bos.toByteArray();
        pkt.length = bos.size();
        pkt.isChunkDataPacket = true;
        return pkt;
    }

    public static Packet sendPartyNameChange(String partyName, String newPartyName)
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        try
        {
            dos.writeByte(RegisteredPackets.partyNameChange.packetID);
            dos.writeUTF(partyName);
            dos.writeUTF(newPartyName);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        Packet250CustomPayload pkt = new Packet250CustomPayload();
        pkt.channel = "Aether";
        pkt.data = bos.toByteArray();
        pkt.length = bos.size();
        pkt.isChunkDataPacket = true;
        return pkt;
    }

    public static Packet sendPartyMemberChange(boolean adding, String partyName, String username, String skinURL)
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        try
        {
            dos.writeByte(RegisteredPackets.memberChange.packetID);
            dos.writeBoolean(adding);
            dos.writeUTF(partyName);
            dos.writeUTF(username);
            dos.writeUTF(skinURL);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        Packet250CustomPayload pkt = new Packet250CustomPayload();
        pkt.channel = "Aether";
        pkt.data = bos.toByteArray();
        pkt.length = bos.size();
        pkt.isChunkDataPacket = true;
        return pkt;
    }

    public static Packet sendPartyTypeChange(String partyName, PartyType type)
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        try
        {
            dos.writeByte(RegisteredPackets.partyTypeChange.packetID);
            dos.writeUTF(partyName);
            dos.writeUTF(type.name());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        Packet250CustomPayload pkt = new Packet250CustomPayload();
        pkt.channel = "Aether";
        pkt.data = bos.toByteArray();
        pkt.length = bos.size();
        pkt.isChunkDataPacket = true;
        return pkt;
    }

    public static Packet sendNotificationChange(Notification notification, boolean adding)
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        try
        {
            dos.writeByte(RegisteredPackets.notification.packetID);
            dos.writeBoolean(adding);
            dos.writeUTF(notification.getType().name());
            dos.writeUTF(notification.getHeaderText());
            dos.writeUTF(notification.getSenderName());
            dos.writeUTF(notification.getReceiverName());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        Packet250CustomPayload pkt = new Packet250CustomPayload();
        pkt.channel = "Aether";
        pkt.data = bos.toByteArray();
        pkt.length = bos.size();
        pkt.isChunkDataPacket = true;
        return pkt;
    }

    public static Packet sendPlayerInput(String username, ArrayList directions, boolean isJumping)
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        try
        {
            dos.writeByte(RegisteredPackets.playerInput.packetID);
            dos.writeUTF(username);
            dos.writeInt(directions.size());

            for (MountInput direction : directions)
            {
                dos.writeUTF(direction.name());
            }

            dos.writeBoolean(isJumping);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        Packet250CustomPayload pkt = new Packet250CustomPayload();
        pkt.channel = "Aether";
        pkt.data = bos.toByteArray();
        pkt.length = bos.size();
        pkt.isChunkDataPacket = true;
        return pkt;
    }

    public static Packet sendAllParties(ArrayList parties)
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        try
        {
            dos.writeByte(RegisteredPackets.allParties.packetID);
            dos.writeInt(parties.size());

            for (Party party : parties)
            {
                dos.writeUTF(party.getName());
                dos.writeUTF(party.getLeader().username);
                dos.writeUTF(party.getType().name());
                dos.writeInt(party.getMemberSizeLimit());
                dos.writeInt(party.getMembers().size());

                for (PartyMember member : party.getMembers())
                {
                    dos.writeUTF(member.username);
                    dos.writeUTF(member.getType().name());
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        Packet250CustomPayload pkt = new Packet250CustomPayload();
        pkt.channel = "Aether";
        pkt.data = bos.toByteArray();
        pkt.length = bos.size();
        pkt.isChunkDataPacket = true;
        return pkt;
    }

    public static Packet sendRequestPlayer(boolean adding, String partyName, String leaderName, String leaderSkinUrl, String requestedPlayer)
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        try
        {
            dos.writeByte(RegisteredPackets.requestPlayer.packetID);
            dos.writeBoolean(adding);
            dos.writeUTF(partyName);
            dos.writeUTF(leaderName);
            dos.writeUTF(leaderSkinUrl);
            dos.writeUTF(requestedPlayer);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        Packet250CustomPayload pkt = new Packet250CustomPayload();
        pkt.channel = "Aether";
        pkt.data = bos.toByteArray();
        pkt.length = bos.size();
        pkt.isChunkDataPacket = true;
        return pkt;
    }

    public static Packet sendPlayerClientInfo(boolean clearFirst, boolean adding, String username, PlayerClientInfo playerClientInfo)
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        try
        {
            dos.writeByte(RegisteredPackets.playerClientInfo.packetID);
            dos.writeBoolean(clearFirst);
            dos.writeBoolean(adding);
            dos.writeUTF(username);
            dos.writeShort(playerClientInfo.getHalfHearts());
            dos.writeShort(playerClientInfo.getMaxHealth());
            dos.writeShort(playerClientInfo.getHunger());
            dos.writeShort(playerClientInfo.getArmourValue());
            dos.writeInt(playerClientInfo.getAetherCoins());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        Packet250CustomPayload pkt = new Packet250CustomPayload();
        pkt.channel = "Aether";
        pkt.data = bos.toByteArray();
        pkt.length = bos.size();
        pkt.isChunkDataPacket = true;
        return pkt;
    }

    public static Packet sendMemberTypeChange(String username, MemberType type)
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        try
        {
            dos.writeByte(RegisteredPackets.memberTypeChange.packetID);
            dos.writeUTF(username);
            dos.writeUTF(type.name());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        Packet250CustomPayload pkt = new Packet250CustomPayload();
        pkt.channel = "Aether";
        pkt.data = bos.toByteArray();
        pkt.length = bos.size();
        pkt.isChunkDataPacket = true;
        return pkt;
    }

    public static Packet sendDungeonQueueChange(boolean adding, Dungeon dungeon, int tileX, int tileY, int tileZ, Party party)
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        try
        {
            dos.writeByte(RegisteredPackets.dungeonQueueChange.packetID);
            dos.writeBoolean(adding);
            dos.writeInt(dungeon.getID());
            dos.writeUTF(party.getName());
            dos.writeInt(tileX);
            dos.writeInt(tileY);
            dos.writeInt(tileZ);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        Packet250CustomPayload pkt = new Packet250CustomPayload();
        pkt.channel = "Aether";
        pkt.data = bos.toByteArray();
        pkt.length = bos.size();
        pkt.isChunkDataPacket = true;
        return pkt;
    }

    public static Packet sendDungeonChange(boolean adding, Dungeon dungeon)
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        try
        {
            dos.writeByte(RegisteredPackets.dungeonChange.packetID);
            dos.writeBoolean(adding);
            dos.writeInt(dungeon.getID());

            if (adding)
            {
                dos.writeUTF(dungeon.getType().name());
                dos.writeInt(dungeon.centerX);
                dos.writeInt(dungeon.centerZ);
                dos.writeInt(dungeon.boundingBoxes.size());
                dos.writeInt(dungeon.boundingBox.minX);
                dos.writeInt(dungeon.boundingBox.minY);
                dos.writeInt(dungeon.boundingBox.minZ);
                dos.writeInt(dungeon.boundingBox.maxX);
                dos.writeInt(dungeon.boundingBox.maxY);
                dos.writeInt(dungeon.boundingBox.maxZ);

                for (StructureBoundingBoxSerial box : dungeon.boundingBoxes)
                {
                    dos.writeInt(box.minX);
                    dos.writeInt(box.minY);
                    dos.writeInt(box.minZ);
                    dos.writeInt(box.maxX);
                    dos.writeInt(box.maxY);
                    dos.writeInt(box.maxZ);
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        Packet250CustomPayload pkt = new Packet250CustomPayload();
        pkt.channel = "Aether";
        pkt.data = bos.toByteArray();
        pkt.length = bos.size();
        pkt.isChunkDataPacket = true;
        return pkt;
    }

    public static Packet sendDungeonMemberQueue(Dungeon dungeon, PartyMember queuedMember)
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        try
        {
            dos.writeByte(RegisteredPackets.dungeonMemberQueue.packetID);
            dos.writeInt(dungeon.getID());
            dos.writeUTF(queuedMember.username);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        Packet250CustomPayload pkt = new Packet250CustomPayload();
        pkt.channel = "Aether";
        pkt.data = bos.toByteArray();
        pkt.length = bos.size();
        pkt.isChunkDataPacket = true;
        return pkt;
    }

    public static Packet sendDungeonFinish(Dungeon dungeon, TileEntityEntranceController controller, Party party)
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        try
        {
            dos.writeByte(RegisteredPackets.dungeonFinish.packetID);
            dos.writeInt(dungeon.getID());
            dos.writeUTF(party.getName());
            dos.writeInt(controller.xCoord);
            dos.writeInt(controller.yCoord);
            dos.writeInt(controller.zCoord);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        Packet250CustomPayload pkt = new Packet250CustomPayload();
        pkt.channel = "Aether";
        pkt.data = bos.toByteArray();
        pkt.length = bos.size();
        pkt.isChunkDataPacket = true;
        return pkt;
    }

    public static Packet sendDungeonTimerStart(Dungeon dungeon, Party party, int timerLength)
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        try
        {
            dos.writeByte(RegisteredPackets.dungeonTimerStart.packetID);
            dos.writeInt(dungeon.getID());
            dos.writeUTF(party.getName());
            dos.writeInt(timerLength);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        Packet250CustomPayload pkt = new Packet250CustomPayload();
        pkt.channel = "Aether";
        pkt.data = bos.toByteArray();
        pkt.length = bos.size();
        pkt.isChunkDataPacket = true;
        return pkt;
    }

    public static Packet removeDungeonKey(Dungeon dungeon, Party party, EnumKeyType keyType, TileEntityBronzeDoorController bronzeDoor)
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        try
        {
            dos.writeByte(RegisteredPackets.dungeonKey.packetID);
            dos.writeBoolean(false);
            dos.writeInt(dungeon.getID());
            dos.writeUTF(party.getName());
            dos.writeUTF(keyType.name());
            dos.writeInt(MathHelper.floor_double(bronzeDoor.xCoord));
            dos.writeInt(MathHelper.floor_double(bronzeDoor.yCoord));
            dos.writeInt(MathHelper.floor_double(bronzeDoor.zCoord));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        Packet250CustomPayload pkt = new Packet250CustomPayload();
        pkt.channel = "Aether";
        pkt.data = bos.toByteArray();
        pkt.length = bos.size();
        pkt.isChunkDataPacket = true;
        return pkt;
    }

    public static Packet sendDungeonKey(Dungeon dungeon, Party party, EnumKeyType keyType)
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        try
        {
            dos.writeByte(RegisteredPackets.dungeonKey.packetID);
            dos.writeBoolean(true);
            dos.writeInt(dungeon.getID());
            dos.writeUTF(party.getName());
            dos.writeUTF(keyType.name());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        Packet250CustomPayload pkt = new Packet250CustomPayload();
        pkt.channel = "Aether";
        pkt.data = bos.toByteArray();
        pkt.length = bos.size();
        pkt.isChunkDataPacket = true;
        return pkt;
    }

    public static Packet sendDungeonRespawn(Dungeon dungeon, Party party)
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        try
        {
            dos.writeByte(RegisteredPackets.dungeonRespawn.packetID);
            dos.writeInt(dungeon.getID());
            dos.writeUTF(party.getName());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        Packet250CustomPayload pkt = new Packet250CustomPayload();
        pkt.channel = "Aether";
        pkt.data = bos.toByteArray();
        pkt.length = bos.size();
        pkt.isChunkDataPacket = true;
        return pkt;
    }

    public static Packet sendDungeonDisbandMember(Dungeon dungeon, PartyMember member)
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        try
        {
            dos.writeByte(RegisteredPackets.dungeonDisbandMember.packetID);
            dos.writeInt(dungeon.getID());
            dos.writeUTF(member.username);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        Packet250CustomPayload pkt = new Packet250CustomPayload();
        pkt.channel = "Aether";
        pkt.data = bos.toByteArray();
        pkt.length = bos.size();
        pkt.isChunkDataPacket = true;
        return pkt;
    }

    public static Packet sendDungeonQueueCheck(Dungeon dungeon)
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        try
        {
            dos.writeByte(RegisteredPackets.dungeonQueueCheck.packetID);
            dos.writeInt(dungeon.getID());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        Packet250CustomPayload pkt = new Packet250CustomPayload();
        pkt.channel = "Aether";
        pkt.data = bos.toByteArray();
        pkt.length = bos.size();
        pkt.isChunkDataPacket = true;
        return pkt;
    }

    public static Packet sendParachuteCheck(boolean clearFirst, boolean adding, boolean isParachuting, int parachuteType, Set playerParachuting)
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        try
        {
            dos.writeByte(RegisteredPackets.parachute.packetID);
            dos.writeBoolean(clearFirst);
            dos.writeBoolean(adding);
            dos.writeShort(playerParachuting.size());
            dos.writeBoolean(isParachuting);
            dos.writeInt(parachuteType);

            for (String username : playerParachuting)
            {
                dos.writeUTF(username);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        Packet250CustomPayload pkt = new Packet250CustomPayload();
        pkt.channel = "Aether";
        pkt.data = bos.toByteArray();
        pkt.length = bos.size();
        pkt.isChunkDataPacket = true;
        return pkt;
    }
}

