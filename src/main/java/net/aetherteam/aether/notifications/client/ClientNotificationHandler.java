package net.aetherteam.aether.notifications.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.aetherteam.aether.client.gui.social.dialogue.GuiDialogueBox;
import net.aetherteam.aether.notifications.Notification;
import net.aetherteam.aether.notifications.NotificationType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.CallableMPL2;
import net.minecraft.server.integrated.IntegratedPlayerList;

public class ClientNotificationHandler
{
    @SideOnly(Side.CLIENT)
    private static GuiNotification notificationGui = new GuiNotification(Minecraft.getMinecraft());

    @SideOnly(Side.CLIENT)
    public static void updateNotifications()
    {
        notificationGui.updateNotificationWindow();
    }

    @SideOnly(Side.CLIENT)
    public static void queueReceivedNotification(Notification notification)
    {
        notificationGui.queueReceivedNotification(notification);
    }

    @SideOnly(Side.CLIENT)
    public static void createGeneric(String header, String lower, String toPlayer)
    {
        queueReceivedNotification(new Notification(NotificationType.GENERIC, header, lower, toPlayer));
    }

    @SideOnly(Side.CLIENT)
    public static String clientUsername()
    {
        return Minecraft.getMinecraft().thePlayer.bS;
    }

    @SideOnly(Side.CLIENT)
    public static void openDialogueBox(String trueText, String falseText, boolean flag)
    {
        Minecraft.getMinecraft().displayGuiScreen(new GuiDialogueBox(null, trueText, falseText, flag));
    }

    @SideOnly(Side.CLIENT)
    public static void openDialogueBox(String text)
    {
        Minecraft.getMinecraft().displayGuiScreen(new GuiDialogueBox(null, text, "", true));
    }

    @SideOnly(Side.CLIENT)
    public static boolean isOpenToLAN()
    {
        return (Minecraft.getMinecraft().D() != null) && (Minecraft.getMinecraft().D().an());
    }
}

