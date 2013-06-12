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

/* Location:           D:\Dev\Mc\forge_orl\mcp\jars\bin\aether.jar
 * Qualified Name:     net.aetherteam.aether.notifications.description.GenericContents
 * JD-Core Version:    0.6.2
 */