package net.aetherteam.aether.notifications.description;

import net.aetherteam.aether.notifications.Notification;

public abstract class NotificationContents
{
    public abstract String getTitle(Notification paramNotification);

    public abstract String getDescription(Notification paramNotification);
}

/* Location:           D:\Dev\Mc\forge_orl\mcp\jars\bin\aether.jar
 * Qualified Name:     net.aetherteam.aether.notifications.description.NotificationContents
 * JD-Core Version:    0.6.2
 */