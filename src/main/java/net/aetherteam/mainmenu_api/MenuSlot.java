package net.aetherteam.mainmenu_api;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import org.lwjgl.opengl.GL11;

public class MenuSlot extends Gui
{
    protected MenuBase menu;
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

    public MenuSlot(MenuBase var1, int var2, int var3, int var4, int var5, int var6)
    {
        this.width = var5;
        this.height = var6;
        this.enabled = true;
        this.drawButton = true;
        this.id = var2;
        this.xPosition = var3;
        this.yPosition = var4;
        this.menu = var1;
    }

    public void drawMenuSlot(Minecraft var1, int var2, int var3)
    {
        this.xPosition = var2;
        this.yPosition = var3;
        this.drawGradientRect(var2, var3, var2 + this.width, var3 + this.height, this.selected ? -10439830 : -13421773, this.selected ? -11563178 : -11184811);
        int var4 = var1.renderEngine.getTexture(this.menu.getIconPath());
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, var4);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        float var5 = 0.0F;
        float var6 = 0.0F;
        float var7 = 1.0F;
        float var8 = 1.0F;
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(var5, var6);
        GL11.glVertex2f((float)(var2 + 2), (float)(var3 + 2));
        GL11.glTexCoord2f(var5, var8);
        GL11.glVertex2f((float)(var2 + 2), (float)(var3 + 18));
        GL11.glTexCoord2f(var7, var8);
        GL11.glVertex2f((float)(var2 + 18), (float)(var3 + 18));
        GL11.glTexCoord2f(var7, var6);
        GL11.glVertex2f((float)(var2 + 18), (float)(var3 + 2));
        GL11.glEnd();
        var1.renderEngine.resetBoundTexture();
        var1.fontRenderer.drawStringWithShadow(this.menu.getName(), var2 + this.height, var3 + 2, 15066597);
        GL11.glPushMatrix();
        GL11.glScalef(0.75F, 0.75F, 1.0F);
        var1.fontRenderer.drawString(this.menu.getVersion(), (int)(((float)var2 + (float)this.height) / 0.75F), (int)(((float)var3 + 12.0F) / 0.75F), 15066597);
        GL11.glPopMatrix();
    }

    public boolean mousePressed(Minecraft var1, int var2, int var3)
    {
        return this.enabled && this.drawButton && var2 >= this.xPosition && var3 >= this.yPosition && var2 < this.xPosition + this.width && var3 < this.yPosition + this.height;
    }
}
