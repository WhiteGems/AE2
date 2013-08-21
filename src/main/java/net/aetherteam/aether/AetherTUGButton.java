package net.aetherteam.aether;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class AetherTUGButton extends GuiButton
{
    private static final ResourceLocation TEXTURE_TUG_BUTTON = new ResourceLocation("aether", "textures/menu/tug.png");

    public AetherTUGButton(int par1, int par2, int par3, int par4, int par5)
    {
        super(par1, par2, par3, par4, par5, "");
    }

    /**
     * Draws this button to the screen.
     */
    public void drawButton(Minecraft par1Minecraft, int mouseX, int mouseY)
    {
        if (this.drawButton)
        {
            FontRenderer fontrenderer = par1Minecraft.fontRenderer;
            GL11.glPushMatrix();
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            Minecraft mc = Minecraft.getMinecraft();
            ScaledResolution scaledresolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
            int width = scaledresolution.getScaledWidth();
            int height = scaledresolution.getScaledHeight();
            boolean flag = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
            float R = 1.0F;
            float G = 1.0F;
            float B = 1.0F;
            float scaling = 0.35F;

            if (flag)
            {
                R = 1.0F;
                G = 1.0F;
                B = 0.65F;
                scaling = 0.36F;
            }

            GL11.glColor4f(R, G, B, 1.0F);
            GL11.glTranslatef((float)(width - 55) - 195.0F * scaling / 2.0F, 40.0F, 1.0F);
            GL11.glScalef(scaling, scaling, scaling);
            mc.renderEngine.func_110577_a(TEXTURE_TUG_BUTTON);
            this.drawTexturedModalRect(0, 0, 0, 0, 195, 184);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glPopMatrix();
            this.field_82253_i = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
            int k = this.getHoverState(this.field_82253_i);
            this.mouseDragged(par1Minecraft, mouseX, mouseY);
            int l = 14737632;

            if (!this.enabled)
            {
                l = -6250336;
            }
            else if (this.field_82253_i)
            {
                l = 16777120;
            }

            this.drawCenteredString(fontrenderer, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, l);
        }
    }
}
