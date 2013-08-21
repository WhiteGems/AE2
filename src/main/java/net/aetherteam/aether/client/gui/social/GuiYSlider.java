package net.aetherteam.aether.client.gui.social;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import org.lwjgl.opengl.GL11;

public class GuiYSlider extends GuiButton
{
    private static final int BACKGROUND_COLOR = -8947849;
    private static final int DEFAULT_BAR_COLOR = -10066330;
    private static final int HOVER_BAR_COLOR = -11184811;
    private static final int DRAG_BAR_COLOR = -12303292;
    public float sliderValue = 0.0F;
    public boolean dragging = false;

    public GuiYSlider(int id, int x, int y, int width, int height)
    {
        super(id, x, y, width, height, "");
    }

    /**
     * Returns 0 if the button is disabled, 1 if the mouse is NOT hovering over this button and 2 if it IS hovering over
     * this button.
     */
    protected int getHoverState(boolean par1)
    {
        return super.getHoverState(par1);
    }

    /**
     * Fired when the mouse button is dragged. Equivalent of MouseListener.mouseDragged(MouseEvent e).
     */
    protected void mouseDragged(Minecraft par1Minecraft, int par2, int par3)
    {
        if (this.drawButton)
        {
            if (this.dragging)
            {
                this.sliderValue = (float)(par3 - (this.yPosition + 4)) / (float)(this.height - 8);

                if (this.sliderValue < 0.0F)
                {
                    this.sliderValue = 0.0F;
                }

                if (this.sliderValue > 1.0F)
                {
                    this.sliderValue = 1.0F;
                }
            }

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.drawTexturedModalRect(this.xPosition, this.yPosition + (int)(this.sliderValue * (float)(this.height - 20)), 0, 66, this.width, 20);
            this.drawTexturedModalRect(this.xPosition, this.yPosition + (int)(this.sliderValue * (float)(this.height - 20)) + 4, 196, 66, 4, 20);
        }
    }

    /**
     * Draws this button to the screen.
     */
    public void drawButton(Minecraft par1Minecraft, int par2, int par3)
    {
        this.mouseDragged(par1Minecraft, par2, par3);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        this.drawGradientRect(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, -8947849, -8947849);
        this.field_82253_i = par2 >= this.xPosition && par3 >= this.yPosition + (int)(this.sliderValue * (float)(this.height - 20)) && par2 < this.xPosition + this.width && par3 < this.yPosition + 20 + (int)(this.sliderValue * (float)(this.height - 20));
        int barColor;

        if (this.dragging)
        {
            barColor = -12303292;
        }
        else if (this.field_82253_i)
        {
            barColor = -11184811;
        }
        else
        {
            barColor = -10066330;
        }

        this.drawGradientRect(this.xPosition, this.yPosition + (int)(this.sliderValue * (float)(this.height - 20)), this.xPosition + this.width, this.yPosition + (int)(this.sliderValue * (float)(this.height - 20)) + 20, barColor, barColor);
    }

    /**
     * Returns true if the mouse has been pressed on this control. Equivalent of MouseListener.mousePressed(MouseEvent
     * e).
     */
    public boolean mousePressed(Minecraft par1Minecraft, int par2, int par3)
    {
        if (super.mousePressed(par1Minecraft, par2, par3))
        {
            this.sliderValue = (float)(par3 - (this.yPosition + 4)) / (float)(this.height - 8);

            if (this.sliderValue < 0.0F)
            {
                this.sliderValue = 0.0F;
            }

            if (this.sliderValue > 1.0F)
            {
                this.sliderValue = 1.0F;
            }

            this.dragging = true;
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Fired when the mouse button is released. Equivalent of MouseListener.mouseReleased(MouseEvent e).
     */
    public void mouseReleased(int par1, int par2)
    {
        this.dragging = false;
    }
}
