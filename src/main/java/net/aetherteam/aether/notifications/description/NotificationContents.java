package net.aetherteam.aether.notifications.description;

import net.aetherteam.aether.notifications.Notification;

public abstract class NotificationContents
{
    public abstract String getTitle(Notification paramNotification);

    public abstract String getDescription(Notification paramNotification);
}

