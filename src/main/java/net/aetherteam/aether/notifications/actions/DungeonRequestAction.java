package net.aetherteam.aether.notifications.actions;

import net.aetherteam.aether.dungeons.Dungeon;
import net.aetherteam.aether.dungeons.DungeonHandler;
import net.aetherteam.aether.notifications.Notification;
import net.aetherteam.aether.notifications.NotificationHandler;
import net.aetherteam.aether.party.Party;
import net.aetherteam.aether.party.PartyController;
import net.aetherteam.aether.party.members.PartyMember;

public class DungeonRequestAction extends NotificationAction
{
    public boolean executeAccept(Notification notification)
    {
        PartyMember requester = PartyController.instance().getMember(notification.getSenderName());
        Party party = PartyController.instance().getParty(requester);
        PartyMember member = PartyController.instance().getMember(notification.getReceiverName());
        Dungeon dungeon = DungeonHandler.instance().getDungeon(party);
        boolean dungeonQueued = false;

        if (party.hasMember(member))
        {
            if (requester != null)
            {
                if (requester.canRecruit())
                {
                    DungeonHandler.instance().queueMember(dungeon, member, true);
                    dungeonQueued = true;
                }
            }
        }

        NotificationHandler.instance().removeNotification(notification);
        NotificationHandler.instance().removeSentNotification(notification, true);
        return dungeonQueued;
    }

    public boolean executeDecline(Notification notification)
    {
        PartyMember requester = PartyController.instance().getMember(notification.getSenderName());
        Party party = PartyController.instance().getParty(requester);
        PartyMember member = PartyController.instance().getMember(notification.getReceiverName());
        Dungeon dungeon = DungeonHandler.instance().getDungeon(party);

        if (dungeon != null)
        {
            if (party.hasMember(member))
            {
                if (requester != null)
                {
                    if (requester.canRecruit())
                    {
                        DungeonHandler.instance().disbandQueue(dungeon, party, dungeon.getControllerX(), dungeon.getControllerY(), dungeon.getControllerZ(), member, true);
                    }
                }
            }
        }

        NotificationHandler.instance().removeNotification(notification);
        NotificationHandler.instance().removeSentNotification(notification, true);
        return true;
    }

    public String acceptMessage(Notification notification)
    {
        return "You have been queued into the Dungeon raid!";
    }

    public String failedMessage(Notification notification)
    {
        return "Sorry, the dungeon raid request no longer exists :(";
    }
}

