package net.aetherteam.aether.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.aetherteam.aether.containers.ContainerSkyrootWorkbench;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiSkyrootCrafting extends GuiContainer
{
    public GuiSkyrootCrafting(InventoryPlayer par1InventoryPlayer, World par2World, int par3, int par4, int par5)
    {
        super(new ContainerSkyrootWorkbench(par1InventoryPlayer, par2World, par3, par4, par5));
    }

    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        this.m.drawString(StatCollector.translateToLocal("container.crafting"), 28, 6, 4210752);
        this.m.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
    }

    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.g.renderEngine.b("/gui/crafting.png");
        int k = (this.height - this.xSize) / 2;
        int l = (this.i - this.ySize) / 2;
        drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
    }
}

