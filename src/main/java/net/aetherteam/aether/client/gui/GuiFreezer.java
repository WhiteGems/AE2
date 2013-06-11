package net.aetherteam.aether.client.gui;

import net.aetherteam.aether.containers.ContainerFreezer;
import net.aetherteam.aether.tile_entities.TileEntityFreezer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderEngine;
import net.minecraft.entity.player.InventoryPlayer;
import org.lwjgl.opengl.GL11;

public class GuiFreezer extends GuiContainer
{
    private TileEntityFreezer freezerInventory;

    public GuiFreezer(InventoryPlayer inventoryplayer, TileEntityFreezer tileentityFreezer)
    {
        super(new ContainerFreezer(inventoryplayer, tileentityFreezer));
        this.freezerInventory = tileentityFreezer;
    }

    protected void drawGuiContainerForegroundLayer()
    {
        this.fontRenderer.drawString("Freezer", 60, 6, 4210752);
        this.fontRenderer.drawString("Inventory", 8, this.ySize - 96 + 2, 4210752);
    }

    protected void drawGuiContainerBackgroundLayer(float f, int ia, int ib)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture("/net/aetherteam/aether/client/sprites/gui/enchanter.png");
        int j = (this.width - this.xSize) / 2;
        int k = (this.height - this.ySize) / 2;
        drawTexturedModalRect(j, k, 0, 0, this.xSize, this.ySize);
        if (this.freezerInventory.isBurning())
        {
            int l = this.freezerInventory.getBurnTimeRemainingScaled(12);
            drawTexturedModalRect(j + 57, k + 47 - l, 176, 12 - l, 14, l + 2);
        }
        int i1 = this.freezerInventory.getCookProgressScaled(24);
        drawTexturedModalRect(j + 79, k + 35, 176, 14, i1 + 1, 16);
    }
}

/* Location:           D:\Dev\Mc\forge_orl\mcp\jars\bin\aether.jar
 * Qualified Name:     net.aetherteam.aether.client.gui.GuiFreezer
 * JD-Core Version:    0.6.2
 */