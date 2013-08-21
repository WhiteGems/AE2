package net.aetherteam.mainmenu_api;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiAetherButton extends GuiButton
{
    private static final ResourceLocation TEXTURE_BUTTONS = new ResourceLocation("mainmenu_api", "textures/icons/aether/buttons.png");
    public int scrollMax = 100;
    public int scrollHeight;
    public int scrollMin;
    public int scrollCrop;
    public int scrollCropMax;
    public boolean retracting;

    public GuiAetherButton(int i, int j, int k, String s)
    {
        super(i, j, k, s);
        this.scrollHeight = this.scrollMax;
        this.scrollMin = 115;
        this.scrollCrop = 20;
        this.scrollCropMax = 90;
        this.retracting = false;
    }

    public GuiAetherButton(int i, int j, int k, int l, int i1, String s)
    {
        super(i, j, k, l, i1, s);
        this.scrollHeight = this.scrollMax;
        this.scrollMin = 115;
        this.scrollCrop = 20;
        this.scrollCropMax = 90;
        this.retracting = false;
        this.enabled = true;
        this.drawButton = true;
    }

    /**
     * Returns 0 if the button is disabled, 1 if the mouse is NOT hovering over this button and 2 if it IS hovering over
     * this button.
     */
    protected int getHoverState(boolean flag)
    {
        byte byte0 = 1;

        if (!this.enabled)
        {
            byte0 = 0;
        }
        else if (flag)
        {
            if (byte0 < 2)
            {
                ++byte0;
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

        return byte0;
    }

    /**
     * Draws this button to the screen.
     */
    public void drawButton(Minecraft minecraft, int i, int j)
    {
        if (this.drawButton)
        {
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            FontRenderer fontrenderer = minecraft.fontRenderer;
            minecraft.renderEngine.func_110577_a(TEXTURE_BUTTONS);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            boolean flag = i >= this.xPosition && j >= this.yPosition && i < this.xPosition + this.width && j < this.yPosition + this.height;
            int k = this.getHoverState(flag);
            this.drawTexturedModalRect(this.xPosition + this.scrollHeight - 90, this.yPosition, 0, 46 + k * 20, this.width / 2, this.height);
            this.drawTexturedModalRect(this.xPosition + this.scrollHeight + this.width / 2 - 90, this.yPosition, 200 - this.width / 2, 46 + k * 20, this.width / 2, this.height);
            this.mouseDragged(minecraft, i, j);
            GL11.glDisable(GL11.GL_BLEND);

            if (!this.enabled)
            {
                this.drawString(fontrenderer, this.displayString, this.xPosition + this.width / 10 + this.scrollHeight - 80, this.yPosition + (this.height - 8) / 2, -6250336);
            }
            else if (flag)
            {
                this.drawString(fontrenderer, this.displayString, this.xPosition + this.width / 10 + this.scrollHeight - 80, this.yPosition + (this.height - 8) / 2, 7851212);
            }
            else
            {
                this.drawString(fontrenderer, this.displayString, this.xPosition + this.width / 10 + this.scrollHeight - 80, this.yPosition + (this.height - 8) / 2, 14737632);
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
        return this.enabled && var2 >= this.xPosition && var3 >= this.yPosition && var2 < this.xPosition + this.width && var3 < this.yPosition + this.height;
    }
}
