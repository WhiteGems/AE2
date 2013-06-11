package net.aetherteam.aether.notifications.actions;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import net.aetherteam.aether.notifications.Notification;

public abstract class NotificationAction
{
    protected Side side = FMLCommonHandler.instance().getEffectiveSide();

    public abstract boolean executeAccept(Notification paramNotification);

    public abstract boolean executeDecline(Notification paramNotification);

    public abstract String acceptMessage(Notification paramNotification);

    public abstract String failedMessage(Notification paramNotification);
}

/* Location:           D:\Dev\Mc\forge_orl\mcp\jars\bin\aether.jar
 * Qualified Name:     net.aetherteam.aether.notifications.actions.NotificationAction
 * JD-Core Version:    0.6.2
 */