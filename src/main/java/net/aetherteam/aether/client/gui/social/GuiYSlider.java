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

    protected int getHoverState(boolean par1)
    {
        return super.getHoverState(par1);
    }

    protected void mouseDragged(Minecraft par1Minecraft, int par2, int par3)
    {
        if (this.drawButton)
        {
            if (this.dragging)
            {
                this.sliderValue = ((par3 - (this.yPosition + 4)) / (this.height - 8));

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
            drawTexturedModalRect(this.xPosition, this.yPosition + (int) (this.sliderValue * (this.height - 20)), 0, 66, this.width, 20);
            drawTexturedModalRect(this.xPosition, this.yPosition + (int) (this.sliderValue * (this.height - 20)) + 4, 196, 66, 4, 20);
        }
    }

    public void drawButton(Minecraft par1Minecraft, int par2, int par3)
    {
        mouseDragged(par1Minecraft, par2, par3);
        GL11.glDisable(3553);
        drawGradientRect(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, -8947849, -8947849);

        this.field_82253_i = ((par2 >= this.xPosition) && (par3 >= this.yPosition + (int) (this.sliderValue * (this.height - 20))) && (par2 < this.xPosition + this.width) && (par3 < this.yPosition + 20 + (int) (this.sliderValue * (this.height - 20))));
        int barColor;
        if (this.dragging)
        {
            barColor = -12303292;
        } else
        {
            if (this.field_82253_i) barColor = -11184811;
            else barColor = -10066330;
        }
        drawGradientRect(this.xPosition, this.yPosition + (int) (this.sliderValue * (this.height - 20)), this.xPosition + this.width, this.yPosition + (int) (this.sliderValue * (this.height - 20)) + 20, barColor, barColor);
    }

    public boolean mousePressed(Minecraft par1Minecraft, int par2, int par3)
    {
        if (super.mousePressed(par1Minecraft, par2, par3))
        {
            this.sliderValue = ((par3 - (this.yPosition + 4)) / (this.height - 8));

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
        return false;
    }

    public void mouseReleased(int par1, int par2)
    {
        this.dragging = false;
    }
}

/* Location:           D:\Dev\Mc\forge_orl\mcp\jars\bin\aether.jar
 * Qualified Name:     net.aetherteam.aether.client.gui.social.GuiYSlider
 * JD-Core Version:    0.6.2
 */