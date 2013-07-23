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
        PartyMember var2 = PartyController.instance().getMember(notification.getSenderName());
        Party var3 = PartyController.instance().getParty(var2);
        boolean var4 = false;

        if (PartyController.instance().getMember(notification.getReceiverName()) == null && var2 != null && var2.canRecruit())
        {
            PartyController.instance().joinParty(var3, new PartyMember(notification.getReceiverName(), ""), true);
            var4 = true;
        }

        NotificationHandler.instance().removeNotification(notification);
        NotificationHandler.instance().removeSentNotification(notification, true);
        PartyController.instance().removePlayerRequest(var3, var2, notification.getReceiverName(), true);
        return var4;
    }

    public boolean executeDecline(Notification notification)
    {
        PartyMember var2 = PartyController.instance().getMember(notification.getSenderName());
        Party var3 = PartyController.instance().getParty(var2);
        NotificationHandler.instance().removeNotification(notification);
        NotificationHandler.instance().removeSentNotification(notification, true);
        PartyController.instance().removePlayerRequest(var3, var2, notification.getReceiverName(), true);
        return true;
    }

    public String acceptMessage(Notification notification)
    {
        PartyMember recruiter = PartyController.instance().getMember(notification.getSenderName());
        Party party = PartyController.instance().getParty(recruiter);

        return "欢迎你加入 " + (party != null ? '"' + party.getName() + '"' + "" : "") + "公会!";
    }

    public String failedMessage(Notification notification)
    {
        return "很抱歉, 请求的公会不存在 :(";
    }
}
