package net.aetherteam.mainmenu_api;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

public class GuiButtonItemStack extends GuiButton
{
    protected static RenderItem itemRenderer = new RenderItem();
    protected static FontRenderer fontRenderer;
    protected static Minecraft mc;
    protected static ItemStack is;

    public GuiButtonItemStack(FontRenderer fontRenderer, Minecraft mc, int par1, int par2, int par3, ItemStack item)
    {
        super(par1, par2, par3, 20, 20, "");
        this.fontRenderer = fontRenderer;
        this.mc = mc;
        is = item;
    }

    public void drawButton(Minecraft par1Minecraft, int par2, int par3)
    {
        super.drawButton(par1Minecraft, par2, par3);
        GL11.glPushMatrix();
        GL11.glEnable(32826);
        itemRenderer.renderItemAndEffectIntoGUI(fontRenderer, mc.renderEngine, is, this.xPosition + 2, this.yPosition + 2);
        GL11.glPopMatrix();
    }
}