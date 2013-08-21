package net.aetherteam.aether.client.gui;

import net.aetherteam.aether.containers.ContainerAltar;
import net.aetherteam.aether.tile_entities.TileEntityAltar;
import net.minecraft.client.gui.inventory.GuiContainer;
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
        this.fontRenderer.drawString("祭坛", 60, 6, 4210752);
        this.fontRenderer.drawString("物品栏", 8, this.ySize - 96 + 2, 4210752);
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the items)
     */
    protected void drawGuiContainerBackgroundLayer(float f, int ia, int ib)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        int j = (this.width - this.xSize) / 2;
        int k = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(j, k, 0, 0, this.xSize, this.ySize);
        int i1;

        if (this.altarInventory.isBurning())
        {
            i1 = this.altarInventory.getBurnTimeRemainingScaled(12);
            this.drawTexturedModalRect(j + 57, k + 47 - i1, 176, 12 - i1, 14, i1 + 2);
        }

        i1 = this.altarInventory.getCookProgressScaled(24);
        this.drawTexturedModalRect(j + 79, k + 35, 176, 14, i1 + 1, 16);
    }
}
