package net.aetherteam.aether.notifications.description;

import net.aetherteam.aether.notifications.Notification;

public class GenericContents extends NotificationContents
{
    public String getTitle(Notification notification)
    {
        return "普通请求:";
    }

    public String getDescription(Notification notification)
    {
        return "这只是一个描述信息!";
    }
}
