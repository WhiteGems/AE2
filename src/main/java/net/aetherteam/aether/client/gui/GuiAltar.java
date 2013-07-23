package net.aetherteam.aether.client.gui;

import net.aetherteam.aether.containers.ContainerAltar;
import net.aetherteam.aether.tile_entities.TileEntityAltar;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import org.lwjgl.opengl.GL11;

public class GuiAltar extends GuiContainer
{
    private TileEntityAltar altarInventory;

    public GuiAltar(InventoryPlayer var1, TileEntityAltar var2)
    {
        super(new ContainerAltar(var1, var2));
        this.altarInventory = var2;
    }

    protected void drawGuiContainerForegroundLayer()
    {
        this.fontRenderer.drawString("Altar", 60, 6, 4210752);
        this.fontRenderer.drawString("Inventory", 8, this.ySize - 96 + 2, 4210752);
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the items)
     */
    protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3)
    {
        int var4 = this.mc.renderEngine.getTexture("/net/aetherteam/aether/client/sprites/gui/enchanter.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        int var5 = (this.width - this.xSize) / 2;
        int var6 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var5, var6, 0, 0, this.xSize, this.ySize);
        int var7;

        if (this.altarInventory.isBurning())
        {
            var7 = this.altarInventory.getBurnTimeRemainingScaled(12);
            this.drawTexturedModalRect(var5 + 57, var6 + 47 - var7, 176, 12 - var7, 14, var7 + 2);
        }

        var7 = this.altarInventory.getCookProgressScaled(24);
        this.drawTexturedModalRect(var5 + 79, var6 + 35, 176, 14, var7 + 1, 16);
    }
}
