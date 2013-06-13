package net.aetherteam.aether.notifications;

import net.aetherteam.aether.notifications.actions.DungeonRequestAction;
import net.aetherteam.aether.notifications.actions.NotificationAction;
import net.aetherteam.aether.notifications.actions.PartyRequestAction;
import net.aetherteam.aether.notifications.description.DungeonRequestContents;
import net.aetherteam.aether.notifications.description.GenericContents;
import net.aetherteam.aether.notifications.description.NotificationContents;
import net.aetherteam.aether.notifications.description.PartyRequestContents;

public enum NotificationType
{
    GENERIC("发送请求!", null),
    DUNGEON("地牢请求", new DungeonRequestAction(), new DungeonRequestContents()),
    PARTY_REQUEST("公会请求", new PartyRequestAction(), new PartyRequestContents());

    public String typeName;
    public NotificationContents typeContents;
    public NotificationAction action;

    private NotificationType(String typeName, NotificationAction action)
    {
        this.typeName = typeName;
        this.action = action;
        this.typeContents = new GenericContents();
    }

    private NotificationType(String typeName, NotificationAction action, NotificationContents typeContents)
    {
        this.typeName = typeName;
        this.action = action;
        this.typeContents = typeContents;
    }

    public static NotificationType getTypeFromString(String name)
    {
        for (NotificationType type : values())
        {
            if (type.name().equalsIgnoreCase(name))
            {
                return type;
            }
        }

        return null;
    }
}

/* Location:           D:\Dev\Mc\forge_orl\mcp\jars\bin\aether.jar
 * Qualified Name:     net.aetherteam.aether.notifications.NotificationType
 * JD-Core Version:    0.6.2
 */