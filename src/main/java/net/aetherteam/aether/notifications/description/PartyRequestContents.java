package net.aetherteam.aether.notifications.description;

import net.aetherteam.aether.notifications.Notification;
import net.aetherteam.aether.party.Party;
import net.aetherteam.aether.party.PartyController;
import net.aetherteam.aether.party.members.PartyMember;

public class PartyRequestContents extends NotificationContents
{
    public String getTitle(Notification var1)
    {
        return "\u00a7r\u00a7n" + var1.getTypeName() + "\u00a7r - " + "\u00a7r\u00a7ofrom " + var1.getSenderName();
    }

    public String getDescription(Notification var1)
    {
        PartyMember var2 = PartyController.instance().getMember(var1.getSenderName());
        Party var3 = PartyController.instance().getParty(var2);
        String var4 = null;

        if (var3 != null)
        {
            var4 = "Would you like to join my party?" + " My party is called " + '\"' + var3.getName() + '\"' + ".";
        }
        else
        {
            var4 = "Sorry, but I am no longer in a party. You can deny this party request.";
        }

        return var4;
    }
}
