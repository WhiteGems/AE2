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
    public boolean executeAccept(Notification var1)
    {
        PartyMember var2 = PartyController.instance().getMember(var1.getSenderName());
        Party var3 = PartyController.instance().getParty(var2);
        PartyMember var4 = PartyController.instance().getMember(var1.getReceiverName());
        Dungeon var5 = DungeonHandler.instance().getDungeon(var3);
        boolean var6 = false;

        if (var3.hasMember(var4) && var2 != null && var2.canRecruit())
        {
            DungeonHandler.instance().queueMember(var5, var4, true);
            var6 = true;
        }

        NotificationHandler.instance().removeNotification(var1);
        NotificationHandler.instance().removeSentNotification(var1, true);
        return var6;
    }

    public boolean executeDecline(Notification var1)
    {
        PartyMember var2 = PartyController.instance().getMember(var1.getSenderName());
        Party var3 = PartyController.instance().getParty(var2);
        PartyMember var4 = PartyController.instance().getMember(var1.getReceiverName());
        Dungeon var5 = DungeonHandler.instance().getDungeon(var3);

        if (var5 != null && var3.hasMember(var4) && var2 != null && var2.canRecruit())
        {
            DungeonHandler.instance().disbandQueue(var5, var3, var5.getControllerX(), var5.getControllerY(), var5.getControllerZ(), var4, true);
        }

        NotificationHandler.instance().removeNotification(var1);
        NotificationHandler.instance().removeSentNotification(var1, true);
        return true;
    }

    public String acceptMessage(Notification var1)
    {
        return "You have been queued into the Dungeon raid!";
    }

    public String failedMessage(Notification var1)
    {
        return "Sorry, the dungeon raid request no longer exists :(";
    }
}
