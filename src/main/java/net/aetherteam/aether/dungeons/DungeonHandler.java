package net.aetherteam.aether.dungeons;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import net.aetherteam.aether.data.SerialDataHandler;
import net.aetherteam.aether.dungeons.keys.DungeonKey;
import net.aetherteam.aether.notifications.Notification;
import net.aetherteam.aether.notifications.NotificationHandler;
import net.aetherteam.aether.notifications.NotificationType;
import net.aetherteam.aether.notifications.client.ClientNotificationHandler;
import net.aetherteam.aether.packets.AetherPacketHandler;
import net.aetherteam.aether.party.Party;
import net.aetherteam.aether.party.PartyController;
import net.aetherteam.aether.party.members.PartyMember;
import net.aetherteam.aether.tile_entities.TileEntityEntranceController;
import net.aetherteam.aether.worldgen.StructureBoundingBoxSerial;

public class DungeonHandler
{
    private HashMap instances = new HashMap();

    private static DungeonHandler clientHandler = new DungeonHandler();
    private static DungeonHandler serverHandler = new DungeonHandler();

    public static DungeonHandler instance()
    {
        Side side = FMLCommonHandler.instance().getEffectiveSide();

        if (side.isClient())
        {
            return clientHandler;
        }

        return serverHandler;
    }

    public void addInstance(Dungeon newDungeon)
    {
        Side side = FMLCommonHandler.instance().getEffectiveSide();

        if (side.isServer())
        {
            PacketDispatcher.sendPacketToAllPlayers(AetherPacketHandler.sendDungeonChange(true, newDungeon));
        }

        System.out.println("Adding a dungeon! X: (" + newDungeon.centerX + ") Z: (" + newDungeon.centerZ + ") Object: (" + newDungeon + ")");
        System.out.println("Current Dungeons: " + this.instances);
        this.instances.put(Integer.valueOf(this.instances.size()), newDungeon);
        instance().saveDungeons();
    }

    public void removeInstance(Dungeon existingDungeon)
    {
        this.instances.remove(existingDungeon);
        Side side = FMLCommonHandler.instance().getEffectiveSide();

        if (side.isServer())
        {
            PacketDispatcher.sendPacketToAllPlayers(AetherPacketHandler.sendDungeonChange(false, existingDungeon));
        }

        instance().saveDungeons();
    }

    public int getDungeonID(Dungeon dungeon)
    {
        int id = 0;

        for (Dungeon instance : this.instances.values())
        {
            if (instance == dungeon)
            {
                return id;
            }

            id++;
        }

        return -1;
    }

    public Dungeon getDungeon(int id)
    {
        return (Dungeon)this.instances.get(Integer.valueOf(id));
    }

    public Dungeon getInstanceAt(int x, int y, int z)
    {
        for (Iterator i$ = this.instances.values().iterator(); i$.hasNext();)
        {
            instance = (Dungeon)i$.next();

            if (instance.boundingBox.intersectsWith(x, z, x, z))
            {
                for (StructureBoundingBoxSerial box : instance.boundingBoxes)
                {
                    if (box.isVecInside(x, y, z))
                    {
                        return instance;
                    }
                }
            }
        }

        Dungeon instance;
        return null;
    }

    public void queueParty(Dungeon dungeon, Party party, int controlX, int controlY, int controlZ, boolean sendPackets)
    {
        Side side = FMLCommonHandler.instance().getEffectiveSide();

        if ((party != null) && (dungeon != null) && (!dungeon.hasQueuedParty()))
        {
            dungeon.queueParty(party, controlX, controlY, controlZ);
            queueMember(dungeon, party.getLeader(), true);

            if ((sendPackets) && (side.isClient()))
            {
                for (PartyMember member : party.getMembers())
                {
                    if (!party.isLeader(member))
                    {
                        NotificationHandler.instance().sendNotification(new Notification(NotificationType.DUNGEON, party.getLeader().username, member.username));
                    }
                }

                PacketDispatcher.sendPacketToServer(AetherPacketHandler.sendDungeonQueueChange(true, dungeon, controlX, controlY, controlZ, party));
            }
        }
    }

    public void queueMember(Dungeon dungeon, PartyMember queuedMember, boolean sendPackets)
    {
        Party party = PartyController.instance().getParty(queuedMember);
        Side side = FMLCommonHandler.instance().getEffectiveSide();

        if ((party != null) && (dungeon != null) && (dungeon.isQueuedParty(party)))
        {
            dungeon.queueMember(party, queuedMember);

            if (((!side.isClient()) || (dungeon.getAmountQueued() != party.getMembers().size())) || (
                        (sendPackets) && (side.isClient())))
            {
                PacketDispatcher.sendPacketToServer(AetherPacketHandler.sendDungeonMemberQueue(dungeon, queuedMember));
            }
        }
    }

    public void checkForQueue(Dungeon dungeon)
    {
        Side side = FMLCommonHandler.instance().getEffectiveSide();

        if ((dungeon != null) && (!dungeon.hasStarted()))
        {
            dungeon.checkForQueue();
        }
    }

    public void disbandQueue(Dungeon dungeon, Party party, int tileX, int tileY, int tileZ, PartyMember disbandMember, boolean sendPackets)
    {
        Side side = FMLCommonHandler.instance().getEffectiveSide();

        if ((party != null) && (dungeon != null) && (dungeon.isQueuedParty(party)))
        {
            dungeon.disbandQueue(party);

            if (side.isClient())
            {
                ClientNotificationHandler.createGeneric("Dungeon Queue Failed!", "By " + disbandMember.username, "");

                if (sendPackets)
                {
                    PacketDispatcher.sendPacketToServer(AetherPacketHandler.sendDungeonQueueChange(false, dungeon, tileX, tileY, tileZ, party));
                }
            }
        }
    }

    public void disbandMember(Dungeon dungeon, PartyMember member, boolean sendPackets)
    {
        Side side = FMLCommonHandler.instance().getEffectiveSide();
        System.out.println(side + ": is the side we're on.");

        if ((member != null) && (dungeon != null) && (dungeon.isQueuedParty(PartyController.instance().getParty(member))))
        {
            System.out.println(side + ": is where we kill shit.");
            dungeon.disbandMember(member);

            if ((side.isClient()) && (PartyController.instance().getParty(PartyController.instance().getMember(ClientNotificationHandler.clientUsername())).getName().equalsIgnoreCase(PartyController.instance().getParty(member).getName())))
            {
                ClientNotificationHandler.createGeneric("Member Left Dungeon!", member.username, "");
            }

            if ((sendPackets) && (side.isClient()))
            {
                PacketDispatcher.sendPacketToServer(AetherPacketHandler.sendDungeonDisbandMember(dungeon, member));
            }
        }
    }

    public void finishDungeon(Dungeon dungeon, Party party, TileEntityEntranceController controller, boolean sendPackets)
    {
        Side side = FMLCommonHandler.instance().getEffectiveSide();

        if ((party != null) && (dungeon != null) && (dungeon.hasQueuedParty()) && (controller != null))
        {
            dungeon.finishDungeon(party);

            if ((sendPackets) && (side.isClient()))
            {
                PacketDispatcher.sendPacketToAllPlayers(AetherPacketHandler.sendDungeonFinish(dungeon, controller, party));
            }
        }
    }

    public void startTimer(Dungeon dungeon, Party party, int timerLength)
    {
        if ((party != null) && (dungeon != null) && (dungeon.hasQueuedParty()))
        {
            dungeon.startTimer(timerLength);
        }
    }

    public void addKey(Dungeon dungeon, Party party, DungeonKey key)
    {
        if ((party != null) && (dungeon != null) && (dungeon.hasQueuedParty()))
        {
            dungeon.addKey(key);
        }
    }

    public void removeKey(Dungeon dungeon, Party party, DungeonKey key)
    {
        if ((party != null) && (dungeon != null) && (dungeon.hasQueuedParty()))
        {
            dungeon.removeKey(key);
        }
    }

    public Dungeon getDungeon(Party party)
    {
        if (party != null)
        {
            for (Dungeon dungeon : this.instances.values())
            {
                if ((dungeon.hasQueuedParty()) && (dungeon.getQueuedParty().getName().equalsIgnoreCase(party.getName())))
                {
                    return dungeon;
                }
            }
        }

        return null;
    }

    public boolean isInDungeon(Party party)
    {
        if (party != null)
        {
            for (Dungeon dungeon : this.instances.values())
            {
                if (dungeon.isQueuedParty(party))
                {
                    return true;
                }
            }
        }

        return false;
    }

    public ArrayList getInstances()
    {
        ArrayList dungeons = new ArrayList();
        Iterator it = this.instances.values().iterator();

        while (it.hasNext())
        {
            Object nextObject = it.next();

            if ((nextObject instanceof Dungeon))
            {
                Dungeon dungeon = (Dungeon)nextObject;
                dungeons.add(dungeon);
            }
        }

        return dungeons;
    }

    public void loadInstances(ArrayList loadedInstances)
    {
        for (Dungeon dungeon : loadedInstances)
        {
            addInstance(dungeon);
        }
    }

    public void saveDungeons()
    {
        Side side = FMLCommonHandler.instance().getEffectiveSide();

        if (side.isServer())
        {
            SerialDataHandler dungeonDataHandler = new SerialDataHandler("dungeons.dat");
            ArrayList dungeonObjects = getInstances();
            dungeonDataHandler.serializeObjects(dungeonObjects);
        }
    }
}

