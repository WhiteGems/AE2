package net.aetherteam.aether.notifications.description;

import net.aetherteam.aether.notifications.Notification;

public class GenericContents extends NotificationContents
{
    public String getTitle(Notification var1)
    {
        return "Generic Request:";
    }

    public String getDescription(Notification var1)
    {
        return "This is a generic description!";
    }
}
