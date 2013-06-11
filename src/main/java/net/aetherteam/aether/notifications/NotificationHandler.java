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

        if (side.isClient())
        {
            return clientNotifications;
        }
        return serverNotifications;
    }

    public void sendNotification(Notification notification)
    {
        Side side = FMLCommonHandler.instance().getEffectiveSide();

        if (!hasSentToBefore(notification.getReceiverName(), notification.getType(), notification.getSenderName()))
        {
            this.sentList.add(notification);

            if (side.isClient())
            {
                PacketDispatcher.sendPacketToServer(AetherPacketHandler.sendNotificationChange(notification, true));
                ClientNotificationHandler.queueReceivedNotification(new Notification(NotificationType.GENERIC, "Request Sent!", "", notification.getReceiverName()));
            }
        }
    }

    public void receiveNotification(Notification notification)
    {
        Side side = FMLCommonHandler.instance().getEffectiveSide();

        if (!hasReceivedFromBefore(notification.getSenderName(), notification.getType()))
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

        for (Notification notif : this.sentList)
        {
            if ((notif.getType() == notification.getType()) && (notif.getReceiverName().equalsIgnoreCase(notification.getReceiverName())) && (notif.getSenderName().equalsIgnoreCase(notification.getSenderName())))
            {
                this.sentList.remove(notif);
            }
        }

        if ((sendPackets) && (side == Side.CLIENT))
        {
            PacketDispatcher.sendPacketToServer(AetherPacketHandler.sendNotificationChange(notification, false));
        }
    }

    public boolean hasSentToBefore(String receivingPlayer, NotificationType type, String sendingPlayer)
    {
        for (Notification notification : this.sentList)
        {
            if ((notification.getType() == type) && (notification.getReceiverName().equalsIgnoreCase(receivingPlayer)) && (notification.getSenderName().equalsIgnoreCase(sendingPlayer)))
            {
                return true;
            }
        }

        return false;
    }

    public boolean hasReceivedFromBefore(String sendingPlayer, NotificationType type)
    {
        for (Notification notification : this.receivedList)
        {
            if ((notification.getType() == type) && (notification.getSenderName().equalsIgnoreCase(sendingPlayer)))
            {
                return true;
            }
        }

        return false;
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
        Side side = FMLCommonHandler.instance().getEffectiveSide();

        if ((side.isClient()) && (getNotifications().size() > 0))
        {
            Iterator it = getNotifications().iterator();
            while (it.hasNext())
            {
                Notification notification = (Notification) it.next();

                PartyMember recruiter = PartyController.instance().getMember(notification.getSenderName());
                Party party = PartyController.instance().getParty(recruiter);

                removeSentNotification(notification, true);
                PartyController.instance().removePlayerRequest(party, recruiter, notification.getReceiverName(), true);

                it.remove();
            }
        }
    }
}

/* Location:           D:\Dev\Mc\forge_orl\mcp\jars\bin\aether.jar
 * Qualified Name:     net.aetherteam.aether.notifications.NotificationHandler
 * JD-Core Version:    0.6.2
 */