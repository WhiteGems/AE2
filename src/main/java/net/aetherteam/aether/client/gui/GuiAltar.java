package net.aetherteam.aether.client.gui;

import net.aetherteam.aether.containers.ContainerAltar;
import net.aetherteam.aether.tile_entities.TileEntityAltar;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.InventoryPlayer;
import org.lwjgl.opengl.GL11;

public class GuiAltar extends GuiContainer
{
    private TileEntityAltar altarInventory;

    public GuiAltar(InventoryPlayer inventoryplayer, TileEntityAltar tileentityaltar)
    {
        super(new ContainerAltar(inventoryplayer, tileentityaltar));
        this.altarInventory = tileentityaltar;
    }

    protected void drawGuiContainerForegroundLayer()
    {
        this.m.drawString("Altar", 60, 6, 4210752);
        this.m.drawString("Inventory", 8, this.ySize - 96 + 2, 4210752);
    }

    protected void drawGuiContainerBackgroundLayer(float f, int ia, int ib)
    {
        int i = this.g.renderEngine.f("/net/aetherteam/aether/client/sprites/gui/enchanter.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        int j = (this.height - this.xSize) / 2;
        int k = (this.i - this.ySize) / 2;
        drawTexturedModalRect(j, k, 0, 0, this.xSize, this.ySize);

        if (this.altarInventory.isBurning())
        {
            int l = this.altarInventory.getBurnTimeRemainingScaled(12);
            drawTexturedModalRect(j + 57, k + 47 - l, 176, 12 - l, 14, l + 2);
        }

        int i1 = this.altarInventory.getCookProgressScaled(24);
        drawTexturedModalRect(j + 79, k + 35, 176, 14, i1 + 1, 16);
    }
}

