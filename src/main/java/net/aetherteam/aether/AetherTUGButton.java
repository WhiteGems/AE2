package net.aetherteam.aether;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;

public class AetherTUGButton extends GuiButton
{
    public AetherTUGButton(int var1, int var2, int var3, int var4, int var5)
    {
        super(var1, var2, var3, var4, var5, "");
    }

    /**
     * Draws this button to the screen.
     */
    public void drawButton(Minecraft var1, int var2, int var3)
    {
        if (this.drawButton)
        {
            FontRenderer var4 = var1.fontRenderer;
            GL11.glPushMatrix();
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            Minecraft var5 = Minecraft.getMinecraft();
            ScaledResolution var6 = new ScaledResolution(var5.gameSettings, var5.displayWidth, var5.displayHeight);
            int var7 = var6.getScaledWidth();
            int var8 = var6.getScaledHeight();
            boolean var9 = var2 >= this.xPosition && var3 >= this.yPosition && var2 < this.xPosition + this.width && var3 < this.yPosition + this.height;
            float var10 = 1.0F;
            float var11 = 1.0F;
            float var12 = 1.0F;
            float var13 = 0.35F;

            if (var9)
            {
                var10 = 1.0F;
                var11 = 1.0F;
                var12 = 0.65F;
                var13 = 0.36F;
            }

            GL11.glColor4f(var10, var11, var12, 1.0F);
            GL11.glTranslatef((float)(var7 - 55) - 195.0F * var13 / 2.0F, 40.0F, 1.0F);
            GL11.glScalef(var13, var13, var13);
            var1.renderEngine.bindTexture("/net/aetherteam/aether/client/sprites/menu/tug.png");
            this.drawTexturedModalRect(0, 0, 0, 0, 195, 184);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glPopMatrix();
            this.field_82253_i = var2 >= this.xPosition && var3 >= this.yPosition && var2 < this.xPosition + this.width && var3 < this.yPosition + this.height;
            int var14 = this.getHoverState(this.field_82253_i);
            this.mouseDragged(var1, var2, var3);
            int var15 = 14737632;

            if (!this.enabled)
            {
                var15 = -6250336;
            }
            else if (this.field_82253_i)
            {
                var15 = 16777120;
            }

            this.drawCenteredString(var4, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, var15);
        }
    }
}
