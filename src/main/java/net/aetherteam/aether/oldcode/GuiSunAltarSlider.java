package net.aetherteam.aether.oldcode;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class GuiSunAltarSlider extends GuiButton
{
    public float sliderValue = 1.0F;
    public boolean dragging = false;
    private World worldObj;

    public GuiSunAltarSlider(World var1, int var2, int var3, int var4, String var5, float var6)
    {
        super(var2, var3, var4, 150, 20, var5);
        this.worldObj = var1;
        this.sliderValue = var6;
    }

    /**
     * Returns 0 if the button is disabled, 1 if the mouse is NOT hovering over this button and 2 if it IS hovering over
     * this button.
     */
    protected int getHoverState(boolean var1)
    {
        return 0;
    }

    public void setTime()
    {
        long var1 = (long) (24000.0F * this.sliderValue);
        long var3 = this.worldObj.getWorldInfo().getWorldTime();
        long var5 = var3 % 24000L;
        long var7 = var1 > var5 ? var1 - var5 : var1 + 24000L - var5;
        this.worldObj.getWorldInfo().setWorldTime(var3 + var7);
    }

    /**
     * Fired when the mouse button is dragged. Equivalent of MouseListener.mouseDragged(MouseEvent e).
     */
    protected void mouseDragged(Minecraft var1, int var2, int var3)
    {
        if (this.drawButton)
        {
            if (this.dragging)
            {
                this.sliderValue = (float) (var2 - (this.xPosition + 4)) / (float) (this.width - 8);

                if (this.sliderValue < 0.0F)
                {
                    this.sliderValue = 0.0F;
                }

                if (this.sliderValue > 1.0F)
                {
                    this.sliderValue = 1.0F;
                }

                this.setTime();
            }

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.drawTexturedModalRect(this.xPosition + (int) (this.sliderValue * (float) (this.width - 8)), this.yPosition, 0, 66, 4, 20);
            this.drawTexturedModalRect(this.xPosition + (int) (this.sliderValue * (float) (this.width - 8)) + 4, this.yPosition, 196, 66, 4, 20);
        }
    }

    /**
     * Draws this button to the screen.
     */
    public void drawButton(Minecraft var1, int var2, int var3)
    {
        this.sliderValue = (float) (this.worldObj.getWorldInfo().getWorldTime() % 24000L) / 24000.0F;
        super.drawButton(var1, var2, var3);
    }

    /**
     * Returns true if the mouse has been pressed on this control. Equivalent of MouseListener.mousePressed(MouseEvent
     * e).
     */
    public boolean mousePressed(Minecraft var1, int var2, int var3)
    {
        if (super.mousePressed(var1, var2, var3))
        {
            this.sliderValue = (float) (var2 - (this.xPosition + 4)) / (float) (this.width - 8);

            if (this.sliderValue < 0.0F)
            {
                this.sliderValue = 0.0F;
            }

            if (this.sliderValue > 1.0F)
            {
                this.sliderValue = 1.0F;
            }

            this.setTime();
            this.dragging = true;
            return true;
        } else
        {
            return false;
        }
    }

    /**
     * Fired when the mouse button is released. Equivalent of MouseListener.mouseReleased(MouseEvent e).
     */
    public void mouseReleased(int var1, int var2)
    {
        this.dragging = false;
    }
}
