package net.aetherteam.aether.dungeons;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;

import java.util.ArrayList;
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
        Side var0 = FMLCommonHandler.instance().getEffectiveSide();
        return var0.isClient() ? clientHandler : serverHandler;
    }

    public void addInstance(Dungeon var1)
    {
        Side var2 = FMLCommonHandler.instance().getEffectiveSide();

        if (var2.isServer())
        {
            PacketDispatcher.sendPacketToAllPlayers(AetherPacketHandler.sendDungeonChange(true, var1));
        }

        System.out.println("Adding a dungeon! X: (" + var1.centerX + ") Z: (" + var1.centerZ + ") Object: (" + var1 + ")");
        System.out.println("Current Dungeons: " + this.instances);
        this.instances.put(Integer.valueOf(this.instances.size()), var1);
        instance().saveDungeons();
    }

    public void removeInstance(Dungeon var1)
    {
        this.instances.remove(var1);
        Side var2 = FMLCommonHandler.instance().getEffectiveSide();

        if (var2.isServer())
        {
            PacketDispatcher.sendPacketToAllPlayers(AetherPacketHandler.sendDungeonChange(false, var1));
        }

        instance().saveDungeons();
    }

    public int getDungeonID(Dungeon var1)
    {
        int var2 = 0;

        for (Iterator var3 = this.instances.values().iterator(); var3.hasNext(); ++var2)
        {
            Dungeon var4 = (Dungeon) var3.next();

            if (var4 == var1)
            {
                return var2;
            }
        }

        return -1;
    }

    public Dungeon getDungeon(int var1)
    {
        return (Dungeon) this.instances.get(Integer.valueOf(var1));
    }

    public Dungeon getInstanceAt(int var1, int var2, int var3)
    {
        Iterator var4 = this.instances.values().iterator();

        while (var4.hasNext())
        {
            Dungeon var5 = (Dungeon) var4.next();

            if (var5.boundingBox.intersectsWith(var1, var3, var1, var3))
            {
                Iterator var6 = var5.boundingBoxes.iterator();

                while (var6.hasNext())
                {
                    StructureBoundingBoxSerial var7 = (StructureBoundingBoxSerial) var6.next();

                    if (var7.isVecInside(var1, var2, var3))
                    {
                        return var5;
                    }
                }
            }
        }

        return null;
    }

    public void queueParty(Dungeon var1, Party var2, int var3, int var4, int var5, boolean var6)
    {
        Side var7 = FMLCommonHandler.instance().getEffectiveSide();

        if (var2 != null && var1 != null && !var1.hasQueuedParty())
        {
            var1.queueParty(var2, var3, var4, var5);
            this.queueMember(var1, var2.getLeader(), true);

            if (var6 && var7.isClient())
            {
                Iterator var8 = var2.getMembers().iterator();

                while (var8.hasNext())
                {
                    PartyMember var9 = (PartyMember) var8.next();

                    if (!var2.isLeader(var9))
                    {
                        NotificationHandler.instance().sendNotification(new Notification(NotificationType.DUNGEON, var2.getLeader().username, var9.username));
                    }
                }

                PacketDispatcher.sendPacketToServer(AetherPacketHandler.sendDungeonQueueChange(true, var1, var3, var4, var5, var2));
            }
        }
    }

    public void queueMember(Dungeon var1, PartyMember var2, boolean var3)
    {
        Party var4 = PartyController.instance().getParty(var2);
        Side var5 = FMLCommonHandler.instance().getEffectiveSide();

        if (var4 != null && var1 != null && var1.isQueuedParty(var4))
        {
            var1.queueMember(var4, var2);

            if (var5.isClient() && var1.getAmountQueued() == var4.getMembers().size())
            {
                ;
            }

            if (var3 && var5.isClient())
            {
                PacketDispatcher.sendPacketToServer(AetherPacketHandler.sendDungeonMemberQueue(var1, var2));
            }
        }
    }

    public void checkForQueue(Dungeon var1)
    {
        Side var2 = FMLCommonHandler.instance().getEffectiveSide();

        if (var1 != null && !var1.hasStarted())
        {
            var1.checkForQueue();
        }
    }

    public void disbandQueue(Dungeon var1, Party var2, int var3, int var4, int var5, PartyMember var6, boolean var7)
    {
        Side var8 = FMLCommonHandler.instance().getEffectiveSide();

        if (var2 != null && var1 != null && var1.isQueuedParty(var2))
        {
            var1.disbandQueue(var2);

            if (var8.isClient())
            {
                ClientNotificationHandler.createGeneric(var6.username, "进入地牢队列失败!", "");

                if (var7)
                {
                    PacketDispatcher.sendPacketToServer(AetherPacketHandler.sendDungeonQueueChange(false, var1, var3, var4, var5, var2));
                }
            }
        }
    }

    public void disbandMember(Dungeon var1, PartyMember var2, boolean var3)
    {
        Side var4 = FMLCommonHandler.instance().getEffectiveSide();
        System.out.println(var4 + ": is the side we\'re on.");

        if (var2 != null && var1 != null && var1.isQueuedParty(PartyController.instance().getParty(var2)))
        {
            System.out.println(var4 + ": is where we kill shit.");
            var1.disbandMember(var2);

            if (var4.isClient() && PartyController.instance().getParty(PartyController.instance().getMember(ClientNotificationHandler.clientUsername())).getName().equalsIgnoreCase(PartyController.instance().getParty(var2).getName()))
            {
                ClientNotificationHandler.createGeneric(var2.username, "离开地牢!", "");
            }

            if (var3 && var4.isClient())
            {
                PacketDispatcher.sendPacketToServer(AetherPacketHandler.sendDungeonDisbandMember(var1, var2));
            }
        }
    }

    public void finishDungeon(Dungeon var1, Party var2, TileEntityEntranceController var3, boolean var4)
    {
        Side var5 = FMLCommonHandler.instance().getEffectiveSide();

        if (var2 != null && var1 != null && var1.hasQueuedParty() && var3 != null)
        {
            var1.finishDungeon(var2);

            if (var4 && var5.isClient())
            {
                PacketDispatcher.sendPacketToAllPlayers(AetherPacketHandler.sendDungeonFinish(var1, var3, var2));
            }
        }
    }

    public void startTimer(Dungeon var1, Party var2, int var3)
    {
        if (var2 != null && var1 != null && var1.hasQueuedParty())
        {
            var1.startTimer(var3);
        }
    }

    public void addKey(Dungeon var1, Party var2, DungeonKey var3)
    {
        if (var2 != null && var1 != null && var1.hasQueuedParty())
        {
            var1.addKey(var3);
        }
    }

    public void removeKey(Dungeon var1, Party var2, DungeonKey var3)
    {
        if (var2 != null && var1 != null && var1.hasQueuedParty())
        {
            var1.removeKey(var3);
        }
    }

    public Dungeon getDungeon(Party var1)
    {
        if (var1 != null)
        {
            Iterator var2 = this.instances.values().iterator();

            while (var2.hasNext())
            {
                Dungeon var3 = (Dungeon) var2.next();

                if (var3.hasQueuedParty() && var3.getQueuedParty().getName().equalsIgnoreCase(var1.getName()))
                {
                    return var3;
                }
            }
        }

        return null;
    }

    public boolean isInDungeon(Party var1)
    {
        if (var1 != null)
        {
            Iterator var2 = this.instances.values().iterator();

            while (var2.hasNext())
            {
                Dungeon var3 = (Dungeon) var2.next();

                if (var3.isQueuedParty(var1))
                {
                    return true;
                }
            }
        }

        return false;
    }

    public ArrayList<Dungeon> getInstances()
    {
        ArrayList var1 = new ArrayList();
        Iterator var2 = this.instances.values().iterator();

        while (var2.hasNext())
        {
            Object var3 = var2.next();

            if (var3 instanceof Dungeon)
            {
                Dungeon var4 = (Dungeon) var3;
                var1.add(var4);
            }
        }

        return var1;
    }

    public void loadInstances(ArrayList var1)
    {
        Iterator var2 = var1.iterator();

        while (var2.hasNext())
        {
            Dungeon var3 = (Dungeon) var2.next();
            this.addInstance(var3);
        }
    }

    public void saveDungeons()
    {
        Side var1 = FMLCommonHandler.instance().getEffectiveSide();

        if (var1.isServer())
        {
            SerialDataHandler var2 = new SerialDataHandler("dungeons.dat");
            ArrayList var3 = this.getInstances();
            var2.serializeObjects(var3);
        }
    }
}
