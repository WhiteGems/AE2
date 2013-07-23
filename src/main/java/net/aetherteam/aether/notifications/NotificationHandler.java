package net.aetherteam.aether.notifications;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import java.util.ArrayList;
import java.util.Iterator;
import net.aetherteam.aether.notifications.client.ClientNotificationHandler;
import net.aetherteam.aether.packets.AetherPacketHandler;
import net.aetherteam.aether.party.Party;
import net.aetherteam.aether.party.PartyController;
import net.aetherteam.aether.party.members.PartyMember;

public class NotificationHandler
{
    private ArrayList receivedList = new ArrayList();
    private ArrayList sentList = new ArrayList();
    private static NotificationHandler clientNotifications = new NotificationHandler();
    private static NotificationHandler serverNotifications = new NotificationHandler();

    public static NotificationHandler instance()
    {
        Side var0 = FMLCommonHandler.instance().getEffectiveSide();
        return var0.isClient() ? clientNotifications : serverNotifications;
    }

    public void sendNotification(Notification var1)
    {
        Side var2 = FMLCommonHandler.instance().getEffectiveSide();

        if (!this.hasSentToBefore(var1.getReceiverName(), var1.getType(), var1.getSenderName()))
        {
            this.sentList.add(var1);

            if (var2.isClient())
            {
                PacketDispatcher.sendPacketToServer(AetherPacketHandler.sendNotificationChange(var1, true));
                ClientNotificationHandler.queueReceivedNotification(new Notification(NotificationType.GENERIC, "Request Sent!", "", var1.getReceiverName()));
            }
        }
    }

    public void receiveNotification(Notification var1)
    {
        Side var2 = FMLCommonHandler.instance().getEffectiveSide();

        if (!this.hasReceivedFromBefore(var1.getSenderName(), var1.getType()))
        {
            this.receivedList.add(var1);

            if (var2.isClient())
            {
                ClientNotificationHandler.queueReceivedNotification(var1);
            }
        }
    }

    public void removeNotification(Notification var1)
    {
        this.receivedList.remove(var1);
    }

    public void removeSentNotification(Notification var1, boolean var2)
    {
        Side var3 = FMLCommonHandler.instance().getEffectiveSide();
        Iterator var4 = this.sentList.iterator();

        while (var4.hasNext())
        {
            Notification var5 = (Notification)var4.next();

            if (var5.getType() == var1.getType() && var5.getReceiverName().equalsIgnoreCase(var1.getReceiverName()) && var5.getSenderName().equalsIgnoreCase(var1.getSenderName()))
            {
                this.sentList.remove(var5);
            }
        }

        if (var2 && var3 == Side.CLIENT)
        {
            PacketDispatcher.sendPacketToServer(AetherPacketHandler.sendNotificationChange(var1, false));
        }
    }

    public boolean hasSentToBefore(String var1, NotificationType var2, String var3)
    {
        Iterator var4 = this.sentList.iterator();
        Notification var5;

        do
        {
            if (!var4.hasNext())
            {
                return false;
            }

            var5 = (Notification)var4.next();
        }
        while (var5.getType() != var2 || !var5.getReceiverName().equalsIgnoreCase(var1) || !var5.getSenderName().equalsIgnoreCase(var3));

        return true;
    }

    public boolean hasReceivedFromBefore(String var1, NotificationType var2)
    {
        Iterator var3 = this.receivedList.iterator();
        Notification var4;

        do
        {
            if (!var3.hasNext())
            {
                return false;
            }

            var4 = (Notification)var3.next();
        }
        while (var4.getType() != var2 || !var4.getSenderName().equalsIgnoreCase(var1));

        return true;
    }

    public boolean isEmpty()
    {
        return this.receivedList.isEmpty();
    }

    public ArrayList getNotifications()
    {
        return this.receivedList;
    }

    public void clearNotifications()
    {
        Side var1 = FMLCommonHandler.instance().getEffectiveSide();

        if (var1.isClient() && this.getNotifications().size() > 0)
        {
            Iterator var2 = this.getNotifications().iterator();

            while (var2.hasNext())
            {
                Notification var3 = (Notification)var2.next();
                PartyMember var4 = PartyController.instance().getMember(var3.getSenderName());
                Party var5 = PartyController.instance().getParty(var4);
                this.removeSentNotification(var3, true);
                PartyController.instance().removePlayerRequest(var5, var4, var3.getReceiverName(), true);
                var2.remove();
            }
        }
    }
}
