package net.aetherteam.aether.client.gui;

import net.aetherteam.aether.containers.ContainerEnchanter;
import net.aetherteam.aether.tile_entities.TileEntityEnchanter;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import org.lwjgl.opengl.GL11;

public class GuiEnchanter extends GuiContainer
{
    private TileEntityEnchanter enchanterInventory;

    public GuiEnchanter(InventoryPlayer var1, TileEntityEnchanter var2)
    {
        super(new ContainerEnchanter(var1, var2));
        this.enchanterInventory = var2;
    }

    protected void drawGuiContainerForegroundLayer()
    {
        this.fontRenderer.drawString("祭坛", 60, 6, 4210752);
        this.fontRenderer.drawString("物品栏", 8, this.ySize - 96 + 2, 4210752);
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the items)
     */
    protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture("/net/aetherteam/aether/client/sprites/gui/enchanter.png");
        int var4 = (this.width - this.xSize) / 2;
        int var5 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var4, var5, 0, 0, this.xSize, this.ySize);
        int var6;

        if (this.enchanterInventory.isBurning())
        {
            var6 = this.enchanterInventory.getBurnTimeRemainingScaled(12);
            this.drawTexturedModalRect(var4 + 57, var5 + 47 - var6, 176, 12 - var6, 14, var6 + 2);
        }

        var6 = this.enchanterInventory.getCookProgressScaled(24);
        this.drawTexturedModalRect(var4 + 79, var5 + 35, 176, 14, var6 + 1, 16);
    }
}
