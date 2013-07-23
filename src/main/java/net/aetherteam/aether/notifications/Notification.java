package net.aetherteam.aether.notifications;

import net.aetherteam.aether.notifications.description.NotificationContents;

public class Notification
{
    private String headerText;
    private String senderName;
    private String receiverName;
    private NotificationType type;

    public Notification(NotificationType var1, String var2, String var3)
    {
        this.headerText = "Notification Received!";
        this.senderName = var2;
        this.receiverName = var3;
        this.type = var1;
    }

    public Notification(NotificationType var1, String var2, String var3, String var4)
    {
        this.headerText = var2;
        this.senderName = var3;
        this.receiverName = var4;
        this.type = var1;
    }

    public String getHeaderText()
    {
        return this.headerText;
    }

    public String getSenderName()
    {
        return this.senderName;
    }

    public String getReceiverName()
    {
        return this.receiverName;
    }

    public String getTypeName()
    {
        return this.type.typeName;
    }

    public NotificationContents getTypeContents()
    {
        return this.type.typeContents;
    }

    public NotificationType getType()
    {
        return this.type;
    }

    public void executeAction(boolean var1)
    {
        if (var1)
        {
            this.type.action.executeAccept(this);
        }
        else
        {
            this.type.action.executeDecline(this);
        }
    }
}
