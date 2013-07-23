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

