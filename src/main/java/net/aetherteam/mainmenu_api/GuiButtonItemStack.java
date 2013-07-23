package net.aetherteam.mainmenu_api;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class GuiButtonItemStack extends GuiButton
{
    protected static RenderItem itemRenderer = new RenderItem();
    protected static FontRenderer fontRenderer;
    protected static Minecraft mc;
    protected static ItemStack is;

    public GuiButtonItemStack(FontRenderer var1, Minecraft var2, int var3, int var4, int var5, ItemStack var6)
    {
        super(var3, var4, var5, 20, 20, "");
        fontRenderer = var1;
        mc = var2;
        is = var6;
    }

    /**
     * Draws this button to the screen.
     */
    public void drawButton(Minecraft var1, int var2, int var3)
    {
        super.drawButton(var1, var2, var3);
        GL11.glPushMatrix();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        itemRenderer.renderItemAndEffectIntoGUI(fontRenderer, mc.renderEngine, is, this.xPosition + 2, this.yPosition + 2);
        GL11.glPopMatrix();
    }
}
