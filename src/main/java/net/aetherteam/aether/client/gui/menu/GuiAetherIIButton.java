package net.aetherteam.aether.client.gui.menu;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiAetherIIButton extends GuiButton
{
    private int color;
    private int color2;
    public int scrollMax = 100;
    public int scrollHeight;
    public int scrollMin;
    public int scrollCrop;
    public int scrollCropMax;
    public boolean retracting;

    public GuiAetherIIButton(int var1, int var2, int var3, String var4)
    {
        super(var1, var2, var3, var4);
        this.scrollHeight = this.scrollMax;
        this.scrollMin = 115;
        this.scrollCrop = 20;
        this.scrollCropMax = 90;
        this.retracting = false;
        this.color = 0;
        this.color2 = 0;
    }

    public GuiAetherIIButton(int i, int j, int k, int color, int var5)
    {
        super(i, j, k, "以太官方服务器");
        this.scrollHeight = this.scrollMax;
        this.scrollMin = 115;
        this.scrollCrop = 20;
        this.scrollCropMax = 90;
        this.retracting = false;
        this.color = color;
        this.color2 = color;
    }

    public GuiAetherIIButton(int var1, int var2, int var3, int var4, int var5, String var6)
    {
        super(var1, var2, var3, var4, var5, var6);
        this.scrollHeight = this.scrollMax;
        this.scrollMin = 115;
        this.scrollCrop = 20;
        this.scrollCropMax = 90;
        this.retracting = false;
        this.color = 0;
        this.color2 = this.color;
        this.enabled = true;
        this.drawButton = true;
    }

    /**
     * Returns 0 if the button is disabled, 1 if the mouse is NOT hovering over this button and 2 if it IS hovering over
     * this button.
     */
    protected int getHoverState(boolean var1)
    {
        byte var2 = 1;

        if (!this.enabled)
        {
            var2 = 0;
        }
        else if (var1)
        {
            if (var2 < 2)
            {
                ++var2;
            }

            if (this.scrollCrop < this.scrollCropMax)
            {
                ++this.scrollCrop;
            }

            if (this.scrollHeight < this.scrollMin)
            {
                ++this.scrollHeight;
            }
        }
        else
        {
            if (this.scrollCrop > this.scrollCropMax)
            {
                --this.scrollCrop;
            }

            if (this.scrollHeight > this.scrollMax)
            {
                --this.scrollHeight;
            }

            if (this.scrollHeight == this.scrollMax)
            {
                this.retracting = false;
            }
        }

        return var2;
    }

    /**
     * Draws this button to the screen.
     */
    public void drawButton(Minecraft var1, int var2, int var3)
    {
        if (this.drawButton)
        {
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            FontRenderer var4 = var1.fontRenderer;
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, var1.renderEngine.getTexture("/net/aetherteam/aether/client/sprites/menu/aether2_buttons.png"));
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            boolean var5 = var2 >= this.xPosition && var3 >= this.yPosition && var2 < this.xPosition + this.width && var3 < this.yPosition + this.height;
            int var6 = this.getHoverState(var5);
            this.drawTexturedModalRect(this.xPosition + this.scrollHeight - 90, this.yPosition, 0, 46 + var6 * 20, this.width / 2, this.height);
            this.drawTexturedModalRect(this.xPosition + this.scrollHeight + this.width / 2 - 90, this.yPosition, 200 - this.width / 2, 46 + var6 * 20, this.width / 2, this.height);
            this.mouseDragged(var1, var2, var3);
            GL11.glDisable(GL11.GL_BLEND);
            var1.renderEngine.resetBoundTexture();

            if (!this.enabled)
            {
                this.drawString(var4, this.displayString, this.xPosition + this.width / 10 + this.scrollHeight - 80, this.yPosition + (this.height - 8) / 2, -6250336);
            }
            else if (var5)
            {
                this.drawString(var4, this.displayString, this.xPosition + this.width / 10 + this.scrollHeight - 80, this.yPosition + (this.height - 8) / 2, 7851212);
            }
            else
            {
                this.drawString(var4, this.displayString, this.xPosition + this.width / 10 + this.scrollHeight - 80, this.yPosition + (this.height - 8) / 2, 14737632);
            }

            if (this.color != 0 || this.color2 != 0)
            {
                if (var5)
                {
                    this.drawString(var4, this.displayString, this.xPosition + this.width / 10 + this.scrollHeight - 80, this.yPosition + (this.height - 8) / 2, this.color);
                }
                else
                {
                    this.drawString(var4, this.displayString, this.xPosition + this.width / 10 + this.scrollHeight - 80, this.yPosition + (this.height - 8) / 2, this.color2);
                }
            }
        }
    }

    /**
     * Fired when the mouse button is dragged. Equivalent of MouseListener.mouseDragged(MouseEvent e).
     */
    protected void mouseDragged(Minecraft var1, int var2, int var3) {}

    /**
     * Fired when the mouse button is released. Equivalent of MouseListener.mouseReleased(MouseEvent e).
     */
    public void mouseReleased(int var1, int var2) {}

    /**
     * Returns true if the mouse has been pressed on this control. Equivalent of MouseListener.mousePressed(MouseEvent
     * e).
     */
    public boolean mousePressed(Minecraft var1, int var2, int var3)
    {
        return this.enabled && this.drawButton && var2 >= this.xPosition && var3 >= this.yPosition && var2 < this.xPosition + this.width && var3 < this.yPosition + this.height;
    }
}