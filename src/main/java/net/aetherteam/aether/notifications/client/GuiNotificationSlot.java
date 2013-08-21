package net.aetherteam.aether.notifications.client;

import net.aetherteam.aether.notifications.Notification;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import org.lwjgl.opengl.GL11;

public class GuiNotificationSlot extends Gui
{
    protected int width;
    protected int height;
    public int xPosition;
    public int yPosition;
    public String displayString;
    public int id;
    public boolean enabled;
    public boolean drawButton;
    protected boolean field_82253_i;
    public boolean selected = false;
    private static final int LEADER_TEXT_COLOR = 9430585;
    public Notification notification;

    public GuiNotificationSlot(Notification notification, int id, int x, int y, int width, int height)
    {
        this.width = width;
        this.height = height;
        this.enabled = true;
        this.drawButton = true;
        this.id = id;
        this.xPosition = x;
        this.yPosition = y;
        this.notification = notification;
    }

    public void drawPartySlot(int x, int y, int width, int height)
    {
        this.xPosition = x;
        this.yPosition = y;
        Minecraft mc = Minecraft.getMinecraft();
        this.drawGradientRect(this.xPosition, this.yPosition, this.xPosition + width, this.yPosition + height, this.selected ? -10439830 : -13421773, this.selected ? -11563178 : -11184811);
        FontRenderer fontRenderer = mc.fontRenderer;
        fontRenderer.drawStringWithShadow(this.notification.getTypeName(), x + height - 19, y + 2, 15066597);
        GL11.glPushMatrix();
        GL11.glScalef(0.75F, 0.75F, 1.0F);
        fontRenderer.drawString("From: ", (int)(((float)x + (float)height) / 0.75F - 36.0F + (float)this.notification.getTypeName().length()), (int)(((float)y + 12.0F) / 0.75F), 16777215);
        fontRenderer.drawString(this.notification.getSenderName(), (int)(((float)x + (float)height) / 0.75F - 6.0F + (float)this.notification.getTypeName().length()), (int)(((float)y + 12.0F) / 0.75F), 9430585);
        GL11.glPopMatrix();
    }

    public boolean mousePressed(Minecraft par1Minecraft, int par2, int par3)
    {
        return this.enabled && this.drawButton && par2 >= this.xPosition && par3 >= this.yPosition && par2 < this.xPosition + this.width && par3 < this.yPosition + this.height;
    }
}
