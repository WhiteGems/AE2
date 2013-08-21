package net.aetherteam.aether.notifications.description;

import net.aetherteam.aether.notifications.Notification;
import net.aetherteam.aether.party.Party;
import net.aetherteam.aether.party.PartyController;
import net.aetherteam.aether.party.members.PartyMember;

public class PartyRequestContents extends NotificationContents
{
    public String getTitle(Notification notification)
    {
        return "\u00a7r\u00a7n" + notification.getTypeName() + "\u00a7r - " + "\u00a7r\u00a7o来自于 " + notification.getSenderName();
    }

    public String getDescription(Notification notification)
    {
        PartyMember recruiter = PartyController.instance().getMember(notification.getSenderName());
        Party party = PartyController.instance().getParty(recruiter);
        String description = null;

        if (party != null)
        {
            description = "你想加入我的公会吗?" + " 我的公会是 " + '\"' + party.getName() + '\"' + ".";
        }
        else
        {
            description = "很抱歉, 我已退出公会, 你可以拒绝此请求";
        }

        return description;
    }
}
