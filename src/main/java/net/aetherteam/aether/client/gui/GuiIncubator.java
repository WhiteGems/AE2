package net.aetherteam.aether.client.gui;

import net.aetherteam.aether.containers.ContainerIncubator;
import net.aetherteam.aether.tile_entities.TileEntityIncubator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.InventoryPlayer;
import org.lwjgl.opengl.GL11;

public class GuiIncubator extends GuiContainer
{
    private TileEntityIncubator IncubatorInventory;

    public GuiIncubator(InventoryPlayer inventoryplayer, TileEntityIncubator tileentityIncubator)
    {
        super(new ContainerIncubator(inventoryplayer, tileentityIncubator));
        this.IncubatorInventory = tileentityIncubator;
    }

    protected void drawGuiContainerForegroundLayer()
    {
        this.m.drawString("Incubator", 60, 6, 4210752);
        this.m.drawString("Inventory", 8, this.ySize - 96 + 2, 4210752);
    }

    protected void drawGuiContainerBackgroundLayer(float f, int ia, int ib)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.g.renderEngine.b("/net/aetherteam/aether/client/sprites/gui/incubator.png");
        int j = (this.height - this.xSize) / 2;
        int k = (this.i - this.ySize) / 2;
        drawTexturedModalRect(j, k, 0, 0, this.xSize, this.ySize);

        if (this.IncubatorInventory.isBurning())
        {
            int l = this.IncubatorInventory.getBurnTimeRemainingScaled(12);
            drawTexturedModalRect(j + 74, k + 47 - l, 176, 12 - l, 14, l + 2);
        }

        int i1 = this.IncubatorInventory.getCookProgressScaled(54);
        drawTexturedModalRect(j + 103, k + 70 - i1, 179, 70 - i1, 10, i1);
    }
}

