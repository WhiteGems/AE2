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

    public MenuSlot(MenuBase menu, int id, int x, int y, int width, int height)
    {
        this.width = width;
        this.height = height;
        this.enabled = true;
        this.drawButton = true;
        this.id = id;
        this.xPosition = x;
        this.yPosition = y;
        this.menu = menu;
    }

    public void drawMenuSlot(Minecraft mc, int xPosition, int yPosition)
    {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.drawGradientRect(xPosition, yPosition, xPosition + this.width, yPosition + this.height, this.selected ? -10439830 : -13421773, this.selected ? -11563178 : -11184811);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.func_110577_a(this.menu.getIconPath());
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        float u = 0.0F;
        float v = 0.0F;
        float u1 = 1.0F;
        float v1 = 1.0F;
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(u, v);
        GL11.glVertex2f((float)(xPosition + 2), (float)(yPosition + 2));
        GL11.glTexCoord2f(u, v1);
        GL11.glVertex2f((float)(xPosition + 2), (float)(yPosition + 18));
        GL11.glTexCoord2f(u1, v1);
        GL11.glVertex2f((float)(xPosition + 18), (float)(yPosition + 18));
        GL11.glTexCoord2f(u1, v);
        GL11.glVertex2f((float)(xPosition + 18), (float)(yPosition + 2));
        GL11.glEnd();
        mc.fontRenderer.drawStringWithShadow(this.menu.getName(), xPosition + this.height, yPosition + 2, 15066597);
        GL11.glPushMatrix();
        GL11.glScalef(0.75F, 0.75F, 1.0F);
        mc.fontRenderer.drawString(this.menu.getVersion(), (int)(((float)xPosition + (float)this.height) / 0.75F), (int)(((float)yPosition + 12.0F) / 0.75F), 15066597);
        GL11.glPopMatrix();
    }

    public boolean mousePressed(Minecraft par1Minecraft, int par2, int par3)
    {
        return this.enabled && this.drawButton && par2 >= this.xPosition && par3 >= this.yPosition && par2 < this.xPosition + this.width && par3 < this.yPosition + this.height;
    }
}
