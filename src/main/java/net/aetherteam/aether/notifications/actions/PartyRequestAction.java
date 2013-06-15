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

        if (PartyController.instance().getMember(notification.getReceiverName()) == null)
        {
            if (recruiter != null)
            {
                if (recruiter.canRecruit())
                {
                    PartyController.instance().joinParty(party, new PartyMember(notification.getReceiverName(), ""), true);
                    partyJoined = true;
                }
            }
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

        return "欢迎你加入 " + (party != null ? '"' + party.getName() + '"' + "" : "") + "公会!";
    }

    public String failedMessage(Notification notification)
    {
        return "很抱歉, 请求的公会不存在 :(";
    }
}

/* Location:           D:\Dev\Mc\forge_orl\mcp\jars\bin\aether.jar
 * Qualified Name:     net.aetherteam.aether.notifications.actions.PartyRequestAction
 * JD-Core Version:    0.6.2
 */