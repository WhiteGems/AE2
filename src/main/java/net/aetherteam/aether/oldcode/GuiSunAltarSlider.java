package net.aetherteam.aether.oldcode;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldInfo;
import org.lwjgl.opengl.GL11;

public class GuiSunAltarSlider extends GuiButton
{
    public float sliderValue = 1.0F;

    public boolean dragging = false;
    private World worldObj;

    public GuiSunAltarSlider(World world, int par1, int par2, int par3, String par5Str, float par6)
    {
        super(par1, par2, par3, 150, 20, par5Str);
        this.worldObj = world;
        this.sliderValue = par6;
    }

    protected int getHoverState(boolean par1)
    {
        return 0;
    }

    public void setTime()
    {
        long shouldTime = ()(24000.0F * this.sliderValue);
        long worldTime = this.worldObj.M().getWorldTime();
        long remainder = worldTime % 24000L;
        long add = shouldTime > remainder ? shouldTime - remainder : shouldTime + 24000L - remainder;
        this.worldObj.M().setWorldTime(worldTime + add);
    }

    protected void mouseDragged(Minecraft par1Minecraft, int par2, int par3)
    {
        if (this.drawButton)
        {
            if (this.dragging)
            {
                this.sliderValue = ((par2 - (this.xPosition + 4)) / (this.width - 8));

                if (this.sliderValue < 0.0F)
                {
                    this.sliderValue = 0.0F;
                }

                if (this.sliderValue > 1.0F)
                {
                    this.sliderValue = 1.0F;
                }

                setTime();
            }

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            drawTexturedModalRect(this.xPosition + (int)(this.sliderValue * (this.width - 8)), this.yPosition, 0, 66, 4, 20);
            drawTexturedModalRect(this.xPosition + (int)(this.sliderValue * (this.width - 8)) + 4, this.yPosition, 196, 66, 4, 20);
        }
    }

    public void drawButton(Minecraft par1Minecraft, int par2, int par3)
    {
        this.sliderValue = ((float)(this.worldObj.M().getWorldTime() % 24000L) / 24000.0F);
        super.drawButton(par1Minecraft, par2, par3);
    }

    public boolean mousePressed(Minecraft par1Minecraft, int par2, int par3)
    {
        if (super.mousePressed(par1Minecraft, par2, par3))
        {
            this.sliderValue = ((par2 - (this.xPosition + 4)) / (this.width - 8));

            if (this.sliderValue < 0.0F)
            {
                this.sliderValue = 0.0F;
            }

            if (this.sliderValue > 1.0F)
            {
                this.sliderValue = 1.0F;
            }

            setTime();
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

