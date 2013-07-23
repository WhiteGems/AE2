package net.aetherteam.aether.notifications.actions;

import net.aetherteam.aether.notifications.Notification;
import net.aetherteam.aether.notifications.NotificationHandler;
import net.aetherteam.aether.party.Party;
import net.aetherteam.aether.party.PartyController;
import net.aetherteam.aether.party.members.PartyMember;

public class PartyRequestAction extends NotificationAction
{
    public boolean executeAccept(Notification var1)
    {
        PartyMember var2 = PartyController.instance().getMember(var1.getSenderName());
        Party var3 = PartyController.instance().getParty(var2);
        boolean var4 = false;

        if (PartyController.instance().getMember(var1.getReceiverName()) == null && var2 != null && var2.canRecruit())
        {
            PartyController.instance().joinParty(var3, new PartyMember(var1.getReceiverName(), ""), true);
            var4 = true;
        }

        NotificationHandler.instance().removeNotification(var1);
        NotificationHandler.instance().removeSentNotification(var1, true);
        PartyController.instance().removePlayerRequest(var3, var2, var1.getReceiverName(), true);
        return var4;
    }

    public boolean executeDecline(Notification var1)
    {
        PartyMember var2 = PartyController.instance().getMember(var1.getSenderName());
        Party var3 = PartyController.instance().getParty(var2);
        NotificationHandler.instance().removeNotification(var1);
        NotificationHandler.instance().removeSentNotification(var1, true);
        PartyController.instance().removePlayerRequest(var3, var2, var1.getReceiverName(), true);
        return true;
    }

    public String acceptMessage(Notification var1)
    {
        PartyMember var2 = PartyController.instance().getMember(var1.getSenderName());
        Party var3 = PartyController.instance().getParty(var2);
        return "You have joined the " + (var3 != null ? '\"' + var3.getName() + '\"' + " " : "") + "party!";
    }

    public String failedMessage(Notification var1)
    {
        return "Sorry, the requested party no longer exists :(";
    }
}
