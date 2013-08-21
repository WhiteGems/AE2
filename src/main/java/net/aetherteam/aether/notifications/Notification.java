package net.aetherteam.aether.notifications;

import net.aetherteam.aether.notifications.description.NotificationContents;

public class Notification
{
    private String headerText;
    private String senderName;
    private String receiverName;
    private NotificationType type;

    public Notification(NotificationType type, String senderName, String receiverName)
    {
        this.headerText = "收到信息!";
        this.senderName = senderName;
        this.receiverName = receiverName;
        this.type = type;
    }

    public Notification(NotificationType type, String headerText, String senderName, String receiverName)
    {
        this.headerText = headerText;
        this.senderName = senderName;
        this.receiverName = receiverName;
        this.type = type;
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

    public void executeAction(boolean accept)
    {
        if (accept)
        {
            this.type.action.executeAccept(this);
        }
        else
        {
            this.type.action.executeDecline(this);
        }
    }
}
