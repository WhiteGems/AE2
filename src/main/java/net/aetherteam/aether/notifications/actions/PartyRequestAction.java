package net.aetherteam.aether.notifications.actions;

import net.aetherteam.aether.notifications.Notification;
import net.aetherteam.aether.notifications.NotificationHandler;
import net.aetherteam.aether.party.Party;
import net.aetherteam.aether.party.PartyController;
import net.aetherteam.aether.party.members.PartyMember;

public class PartyRequestAction extends NotificationAction
{
    public boolean executeAccept(Notification notification)
    {
        PartyMember recruiter = PartyController.instance().getMember(notification.getSenderName());
        Party party = PartyController.instance().getParty(recruiter);
        boolean partyJoined = false;

        if (PartyController.instance().getMember(notification.getReceiverName()) == null && recruiter != null && recruiter.canRecruit())
        {
            PartyController.instance().joinParty(party, new PartyMember(notification.getReceiverName()), true);
            partyJoined = true;
        }

        NotificationHandler.instance().removeNotification(notification);
        NotificationHandler.instance().removeSentNotification(notification, true);
        PartyController.instance().removePlayerRequest(party, recruiter, notification.getReceiverName(), true);
        return partyJoined;
    }

    public boolean executeDecline(Notification notification)
    {
        PartyMember recruiter = PartyController.instance().getMember(notification.getSenderName());
        Party party = PartyController.instance().getParty(recruiter);
        NotificationHandler.instance().removeNotification(notification);
        NotificationHandler.instance().removeSentNotification(notification, true);
        PartyController.instance().removePlayerRequest(party, recruiter, notification.getReceiverName(), true);
        return true;
    }

    public String acceptMessage(Notification notification)
    {
        PartyMember recruiter = PartyController.instance().getMember(notification.getSenderName());
        Party party = PartyController.instance().getParty(recruiter);
        return "You have joined the " + (party != null ? '\"' + party.getName() + '\"' + " " : "") + "party!";
    }

    public String failedMessage(Notification notification)
    {
        return "Sorry, the requested party no longer exists :(";
    }
}
