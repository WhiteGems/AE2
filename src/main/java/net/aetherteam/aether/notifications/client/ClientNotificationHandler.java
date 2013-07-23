package net.aetherteam.aether.notifications.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.aetherteam.aether.client.gui.social.dialogue.GuiDialogueBox;
import net.aetherteam.aether.notifications.Notification;
import net.aetherteam.aether.notifications.NotificationType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

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
    public static void queueReceivedNotification(Notification var0)
    {
        notificationGui.queueReceivedNotification(var0);
    }

    @SideOnly(Side.CLIENT)
    public static void createGeneric(String var0, String var1, String var2)
    {
        queueReceivedNotification(new Notification(NotificationType.GENERIC, var0, var1, var2));
    }

    @SideOnly(Side.CLIENT)
    public static String clientUsername()
    {
        return Minecraft.getMinecraft().thePlayer.username;
    }

    @SideOnly(Side.CLIENT)
    public static void openDialogueBox(String var0, String var1, boolean var2)
    {
        Minecraft.getMinecraft().displayGuiScreen(new GuiDialogueBox((GuiScreen)null, var0, var1, var2));
    }

    @SideOnly(Side.CLIENT)
    public static void openDialogueBox(String var0)
    {
        Minecraft.getMinecraft().displayGuiScreen(new GuiDialogueBox((GuiScreen)null, var0, "", true));
    }

    @SideOnly(Side.CLIENT)
    public static boolean isOpenToLAN()
    {
        return Minecraft.getMinecraft().getIntegratedServer() != null && Minecraft.getMinecraft().getIntegratedServer().getPublic();
    }
}
