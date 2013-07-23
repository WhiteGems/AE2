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
    GENERIC("Request sent!", (NotificationAction)null),
    DUNGEON("Dungeon Request", new DungeonRequestAction(), new DungeonRequestContents()),
    PARTY_REQUEST("Party Request", new PartyRequestAction(), new PartyRequestContents());
    public String typeName;
    public NotificationContents typeContents;
    public NotificationAction action;

    private NotificationType(String var3, NotificationAction var4)
    {
        this.typeName = var3;
        this.action = var4;
        this.typeContents = new GenericContents();
    }

    private NotificationType(String var3, NotificationAction var4, NotificationContents var5)
    {
        this.typeName = var3;
        this.action = var4;
        this.typeContents = var5;
    }

    public static NotificationType getTypeFromString(String var0)
    {
        NotificationType[] var1 = values();
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3)
        {
            NotificationType var4 = var1[var3];

            if (var4.name().equalsIgnoreCase(var0))
            {
                return var4;
            }
        }

        return null;
    }
}
