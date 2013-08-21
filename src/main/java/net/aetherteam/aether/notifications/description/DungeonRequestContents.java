package net.aetherteam.aether.notifications.description;

import net.aetherteam.aether.notifications.Notification;
import net.aetherteam.aether.party.Party;
import net.aetherteam.aether.party.PartyController;
import net.aetherteam.aether.party.members.PartyMember;

public class DungeonRequestContents extends NotificationContents
{
    public String getTitle(Notification notification)
    {
        return "\u00a7r\u00a7n" + notification.getTypeName() + "\u00a7r - " + "\u00a7r\u00a7ofrom " + notification.getSenderName();
    }

    public String getDescription(Notification notification)
    {
        PartyMember recruiter = PartyController.instance().getMember(notification.getSenderName());
        Party party = PartyController.instance().getParty(recruiter);
        String description = null;

        if (party != null)
        {
            description = "Would you like to raid a dungeon with us?" + " My party is called " + '\"' + party.getName() + '\"' + ". WARNING: Dungeons can be a very dangerous place, resulting in the loss of precious items.";
        }
        else
        {
            description = "Sorry, but I am no longer in a party. You can deny this dungeon raid request.";
        }

        return description;
    }
}
