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
    public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player)
    {
        DataInputStream dat = new DataInputStream(new ByteArrayInputStream(packet.data));

        try
        {
            byte e = dat.readByte();
            ArrayList packetList = RegisteredPackets.getPackets();
            Iterator i$ = packetList.iterator();

            while (i$.hasNext())
            {
                AetherPacket aetherPacket = (AetherPacket)i$.next();

                if (aetherPacket.packetID == e)
                {
                    aetherPacket.onPacketReceived(packet, player);
                    return;
                }
            }
        }
        catch (IOException var9)
        {
            var9.printStackTrace();
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
        catch (Exception var4)
        {
            var4.printStackTrace();
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
        catch (IOException var5)
        {
            var5.printStackTrace();
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
        catch (IOException var7)
        {
            var7.printStackTrace();
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
        catch (IOException var6)
        {
            var6.printStackTrace();
        }

        Packet250CustomPayload pkt = new Packet250CustomPayload();
        pkt.channel = "Aether";
        pkt.data = bos.toByteArray();
        pkt.length = bos.size();
        pkt.isChunkDataPacket = true;
        return pkt;
    }

    public static Packet sendAccessoryChange(NBTTagList nbttaglist, boolean clearFirst, boolean adding, Set<String> inventories, byte proxy)
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
            Iterator pkt = inventories.iterator();

            while (pkt.hasNext())
            {
                String username = (String)pkt.next();
                dos.writeUTF(username);
            }
        }
        catch (IOException var9)
        {
            var9.printStackTrace();
        }

        Packet250CustomPayload pkt1 = new Packet250CustomPayload();
        pkt1.channel = "Aether";
        pkt1.data = bos.toByteArray();
        pkt1.length = bos.size();
        pkt1.isChunkDataPacket = true;
        return pkt1;
    }

    public static Packet sendHeartChange(boolean clearFirst, boolean adding, int maxHealth, Set<String> extraHearts)
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
            Iterator pkt = extraHearts.iterator();

            while (pkt.hasNext())
            {
                String username = (String)pkt.next();
                dos.writeUTF(username);
            }
        }
        catch (IOException var8)
        {
            var8.printStackTrace();
        }

        Packet250CustomPayload pkt1 = new Packet250CustomPayload();
        pkt1.channel = "Aether";
        pkt1.data = bos.toByteArray();
        pkt1.length = bos.size();
        pkt1.isChunkDataPacket = true;
        return pkt1;
    }

    public static Packet sendCooldown(boolean clearFirst, boolean adding, int cooldown, int cooldownMax, String stackName, Set<String> playerCooldowns)
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
            Iterator pkt = playerCooldowns.iterator();

            while (pkt.hasNext())
            {
                String username = (String)pkt.next();
                dos.writeUTF(username);
            }
        }
        catch (IOException var10)
        {
            var10.printStackTrace();
        }

        Packet250CustomPayload pkt1 = new Packet250CustomPayload();
        pkt1.channel = "Aether";
        pkt1.data = bos.toByteArray();
        pkt1.length = bos.size();
        pkt1.isChunkDataPacket = true;
        return pkt1;
    }

    public static Packet sendCoinChange(boolean clearFirst, boolean adding, int coinAmount, Set<String> playerCoins)
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
            Iterator pkt = playerCoins.iterator();

            while (pkt.hasNext())
            {
                String username = (String)pkt.next();
                dos.writeUTF(username);
            }
        }
        catch (IOException var8)
        {
            var8.printStackTrace();
        }

        Packet250CustomPayload pkt1 = new Packet250CustomPayload();
        pkt1.channel = "Aether";
        pkt1.data = bos.toByteArray();
        pkt1.length = bos.size();
        pkt1.isChunkDataPacket = true;
        return pkt1;
    }

    public static Packet sendPartyChange(boolean adding, String partyName, String potentialLeader)
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        try
        {
            dos.writeByte(RegisteredPackets.partyChange.packetID);
            dos.writeBoolean(adding);
            dos.writeUTF(partyName);
            dos.writeUTF(potentialLeader);
        }
        catch (IOException var6)
        {
            var6.printStackTrace();
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
        catch (IOException var5)
        {
            var5.printStackTrace();
        }

        Packet250CustomPayload pkt = new Packet250CustomPayload();
        pkt.channel = "Aether";
        pkt.data = bos.toByteArray();
        pkt.length = bos.size();
        pkt.isChunkDataPacket = true;
        return pkt;
    }

    public static Packet sendPartyMemberChange(boolean adding, String partyName, String username)
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        try
        {
            dos.writeByte(RegisteredPackets.memberChange.packetID);
            dos.writeBoolean(adding);
            dos.writeUTF(partyName);
            dos.writeUTF(username);
        }
        catch (IOException var6)
        {
            var6.printStackTrace();
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
        catch (IOException var5)
        {
            var5.printStackTrace();
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
        catch (IOException var5)
        {
            var5.printStackTrace();
        }

        Packet250CustomPayload pkt = new Packet250CustomPayload();
        pkt.channel = "Aether";
        pkt.data = bos.toByteArray();
        pkt.length = bos.size();
        pkt.isChunkDataPacket = true;
        return pkt;
    }

    public static Packet sendPlayerInput(String username, ArrayList<MountInput> directions, boolean isJumping)
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        try
        {
            dos.writeByte(RegisteredPackets.playerInput.packetID);
            dos.writeUTF(username);
            dos.writeInt(directions.size());
            Iterator pkt = directions.iterator();

            while (pkt.hasNext())
            {
                MountInput direction = (MountInput)pkt.next();
                dos.writeUTF(direction.name());
            }

            dos.writeBoolean(isJumping);
        }
        catch (IOException var7)
        {
            var7.printStackTrace();
        }

        Packet250CustomPayload pkt1 = new Packet250CustomPayload();
        pkt1.channel = "Aether";
        pkt1.data = bos.toByteArray();
        pkt1.length = bos.size();
        pkt1.isChunkDataPacket = true;
        return pkt1;
    }

    public static Packet sendAllParties(ArrayList<Party> parties)
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        try
        {
            dos.writeByte(RegisteredPackets.allParties.packetID);
            dos.writeInt(parties.size());
            Iterator pkt = parties.iterator();

            while (pkt.hasNext())
            {
                Party party = (Party)pkt.next();
                dos.writeUTF(party.getName());
                dos.writeUTF(party.getLeader().username);
                dos.writeUTF(party.getType().name());
                dos.writeInt(party.getMemberSizeLimit());
                dos.writeInt(party.getMembers().size());
                Iterator i$ = party.getMembers().iterator();

                while (i$.hasNext())
                {
                    PartyMember member = (PartyMember)i$.next();
                    dos.writeUTF(member.username);
                    dos.writeUTF(member.getType().name());
                }
            }
        }
        catch (IOException var7)
        {
            var7.printStackTrace();
        }

        Packet250CustomPayload pkt1 = new Packet250CustomPayload();
        pkt1.channel = "Aether";
        pkt1.data = bos.toByteArray();
        pkt1.length = bos.size();
        pkt1.isChunkDataPacket = true;
        return pkt1;
    }

    public static Packet sendRequestPlayer(boolean adding, String partyName, String leaderName, String requestedPlayer)
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        try
        {
            dos.writeByte(RegisteredPackets.requestPlayer.packetID);
            dos.writeBoolean(adding);
            dos.writeUTF(partyName);
            dos.writeUTF(leaderName);
            dos.writeUTF(requestedPlayer);
        }
        catch (IOException var7)
        {
            var7.printStackTrace();
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
        catch (IOException var7)
        {
            var7.printStackTrace();
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
        catch (IOException var5)
        {
            var5.printStackTrace();
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
        catch (IOException var9)
        {
            var9.printStackTrace();
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
                Iterator pkt = dungeon.boundingBoxes.iterator();

                while (pkt.hasNext())
                {
                    StructureBoundingBoxSerial box = (StructureBoundingBoxSerial)pkt.next();
                    dos.writeInt(box.minX);
                    dos.writeInt(box.minY);
                    dos.writeInt(box.minZ);
                    dos.writeInt(box.maxX);
                    dos.writeInt(box.maxY);
                    dos.writeInt(box.maxZ);
                }
            }
        }
        catch (IOException var6)
        {
            var6.printStackTrace();
        }

        Packet250CustomPayload pkt1 = new Packet250CustomPayload();
        pkt1.channel = "Aether";
        pkt1.data = bos.toByteArray();
        pkt1.length = bos.size();
        pkt1.isChunkDataPacket = true;
        return pkt1;
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
        catch (IOException var5)
        {
            var5.printStackTrace();
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
        catch (IOException var6)
        {
            var6.printStackTrace();
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
        catch (IOException var6)
        {
            var6.printStackTrace();
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
            dos.writeInt(MathHelper.floor_double((double)bronzeDoor.xCoord));
            dos.writeInt(MathHelper.floor_double((double)bronzeDoor.yCoord));
            dos.writeInt(MathHelper.floor_double((double)bronzeDoor.zCoord));
        }
        catch (IOException var7)
        {
            var7.printStackTrace();
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
        catch (IOException var6)
        {
            var6.printStackTrace();
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
        catch (IOException var5)
        {
            var5.printStackTrace();
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
        catch (IOException var5)
        {
            var5.printStackTrace();
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
        catch (IOException var4)
        {
            var4.printStackTrace();
        }

        Packet250CustomPayload pkt = new Packet250CustomPayload();
        pkt.channel = "Aether";
        pkt.data = bos.toByteArray();
        pkt.length = bos.size();
        pkt.isChunkDataPacket = true;
        return pkt;
    }

    public static Packet sendParachuteCheck(boolean clearFirst, boolean adding, boolean isParachuting, int parachuteType, Set<String> playerParachuting)
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
            Iterator pkt = playerParachuting.iterator();

            while (pkt.hasNext())
            {
                String username = (String)pkt.next();
                dos.writeUTF(username);
            }
        }
        catch (IOException var9)
        {
            var9.printStackTrace();
        }

        Packet250CustomPayload pkt1 = new Packet250CustomPayload();
        pkt1.channel = "Aether";
        pkt1.data = bos.toByteArray();
        pkt1.length = bos.size();
        pkt1.isChunkDataPacket = true;
        return pkt1;
    }
}
