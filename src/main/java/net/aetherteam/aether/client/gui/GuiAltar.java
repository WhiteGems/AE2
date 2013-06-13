package net.aetherteam.aether.client.gui;

import net.aetherteam.aether.containers.ContainerAltar;
import net.aetherteam.aether.tile_entities.TileEntityAltar;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderEngine;
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

    protected void drawGuiContainerBackgroundLayer(float f, int ia, int ib)
    {
        int i = this.mc.renderEngine.getTexture("/net/aetherteam/aether/client/sprites/gui/enchanter.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        int j = (this.width - this.xSize) / 2;
        int k = (this.height - this.ySize) / 2;
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

/* Location:           D:\Dev\Mc\forge_orl\mcp\jars\bin\aether.jar
 * Qualified Name:     net.aetherteam.aether.client.gui.GuiAltar
 * JD-Core Version:    0.6.2
 */