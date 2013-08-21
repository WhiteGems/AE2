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
    private ArrayList<Notification> receivedList = new ArrayList();
    private ArrayList<Notification> sentList = new ArrayList();
    private static NotificationHandler clientNotifications = new NotificationHandler();
    private static NotificationHandler serverNotifications = new NotificationHandler();

    public static NotificationHandler instance()
    {
        Side side = FMLCommonHandler.instance().getEffectiveSide();
        return side.isClient() ? clientNotifications : serverNotifications;
    }

    public void sendNotification(Notification notification)
    {
        Side side = FMLCommonHandler.instance().getEffectiveSide();

        if (!this.hasSentToBefore(notification.getReceiverName(), notification.getType(), notification.getSenderName()))
        {
            this.sentList.add(notification);

            if (side.isClient())
            {
                PacketDispatcher.sendPacketToServer(AetherPacketHandler.sendNotificationChange(notification, true));
                ClientNotificationHandler.queueReceivedNotification(new Notification(NotificationType.GENERIC, "发送请求!", "", notification.getReceiverName()));
            }
        }
    }

    public void receiveNotification(Notification notification)
    {
        Side side = FMLCommonHandler.instance().getEffectiveSide();

        if (!this.hasReceivedFromBefore(notification.getSenderName(), notification.getType()))
        {
            this.receivedList.add(notification);

            if (side.isClient())
            {
                ClientNotificationHandler.queueReceivedNotification(notification);
            }
        }
    }

    public void removeNotification(Notification notification)
    {
        this.receivedList.remove(notification);
    }

    public void removeSentNotification(Notification notification, boolean sendPackets)
    {
        Side side = FMLCommonHandler.instance().getEffectiveSide();
        Iterator i$ = this.sentList.iterator();

        while (i$.hasNext())
        {
            Notification notif = (Notification)i$.next();

            if (notif.getType() == notification.getType() && notif.getReceiverName().equalsIgnoreCase(notification.getReceiverName()) && notif.getSenderName().equalsIgnoreCase(notification.getSenderName()))
            {
                this.sentList.remove(notif);
            }
        }

        if (sendPackets && side == Side.CLIENT)
        {
            PacketDispatcher.sendPacketToServer(AetherPacketHandler.sendNotificationChange(notification, false));
        }
    }

    public boolean hasSentToBefore(String receivingPlayer, NotificationType type, String sendingPlayer)
    {
        Iterator i$ = this.sentList.iterator();
        Notification notification;

        do
        {
            if (!i$.hasNext())
            {
                return false;
            }

            notification = (Notification)i$.next();
        }
        while (notification.getType() != type || !notification.getReceiverName().equalsIgnoreCase(receivingPlayer) || !notification.getSenderName().equalsIgnoreCase(sendingPlayer));

        return true;
    }

    public boolean hasReceivedFromBefore(String sendingPlayer, NotificationType type)
    {
        Iterator i$ = this.receivedList.iterator();
        Notification notification;

        do
        {
            if (!i$.hasNext())
            {
                return false;
            }

            notification = (Notification)i$.next();
        }
        while (notification.getType() != type || !notification.getSenderName().equalsIgnoreCase(sendingPlayer));

        return true;
    }

    public boolean isEmpty()
    {
        return this.receivedList.isEmpty();
    }

    public ArrayList<Notification> getNotifications()
    {
        return this.receivedList;
    }

    public void clearNotifications()
    {
        Side side = FMLCommonHandler.instance().getEffectiveSide();

        if (side.isClient() && this.getNotifications().size() > 0)
        {
            Iterator it = this.getNotifications().iterator();

            while (it.hasNext())
            {
                Notification notification = (Notification)it.next();
                PartyMember recruiter = PartyController.instance().getMember(notification.getSenderName());
                Party party = PartyController.instance().getParty(recruiter);
                this.removeSentNotification(notification, true);
                PartyController.instance().removePlayerRequest(party, recruiter, notification.getReceiverName(), true);
                it.remove();
            }
        }
    }
}
