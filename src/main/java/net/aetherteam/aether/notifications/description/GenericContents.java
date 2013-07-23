package net.aetherteam.aether.notifications.description;

import net.aetherteam.aether.notifications.Notification;

public class GenericContents extends NotificationContents
{
    public String getTitle(Notification var1)
    {
        return "普通请求:";
    }

    public String getDescription(Notification var1)
    {
        return "这只是一个描述信息!";
    }
}
