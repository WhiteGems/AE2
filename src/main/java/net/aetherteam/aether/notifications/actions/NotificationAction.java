package net.aetherteam.aether.notifications.actions;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import net.aetherteam.aether.notifications.Notification;

public abstract class NotificationAction
{
    protected Side side = FMLCommonHandler.instance().getEffectiveSide();

    public abstract boolean executeAccept(Notification var1);

    public abstract boolean executeDecline(Notification var1);

    public abstract String acceptMessage(Notification var1);

    public abstract String failedMessage(Notification var1);
}
