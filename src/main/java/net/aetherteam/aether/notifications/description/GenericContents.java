package net.aetherteam.aether.notifications.description;

import net.aetherteam.aether.notifications.Notification;

public class GenericContents extends NotificationContents
{
    public String getTitle(Notification notification)
    {
        return "Generic Request:";
    }

    public String getDescription(Notification notification)
    {
        return "This is a generic description!";
    }
}

/* Location:           D:\Dev\Mc\forge_orl\mcp\jars\bin\aether.jar
 * Qualified Name:     net.aetherteam.aether.notifications.description.GenericContents
 * JD-Core Version:    0.6.2
 */