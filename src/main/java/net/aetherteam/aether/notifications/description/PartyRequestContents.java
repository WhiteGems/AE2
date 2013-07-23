package net.aetherteam.aether.notifications.description;

import net.aetherteam.aether.notifications.Notification;
import net.aetherteam.aether.party.Party;
import net.aetherteam.aether.party.PartyController;
import net.aetherteam.aether.party.members.PartyMember;

public class PartyRequestContents extends NotificationContents
{
    public String getTitle(Notification notification)
    {
        return "§r§n" + notification.getTypeName() + "§r - " + new StringBuilder().append("§r§ofrom ").append(notification.getSenderName()).toString();
    }

    public String getDescription(Notification notification)
    {
        PartyMember recruiter = PartyController.instance().getMember(notification.getSenderName());
        Party party = PartyController.instance().getParty(recruiter);
        String description = null;

        if (party != null)
        {
            description = "Would you like to join my party?" + " My party is called " + new StringBuilder().append('"').append(party.getName()).append('"').toString() + ".";
        }
        else
        {
            description = "Sorry, but I am no longer in a party. You can deny this party request.";
        }

        return description;
    }
}

