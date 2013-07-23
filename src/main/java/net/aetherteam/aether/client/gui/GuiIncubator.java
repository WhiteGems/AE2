package net.aetherteam.aether.client.gui;

import net.aetherteam.aether.containers.ContainerIncubator;
import net.aetherteam.aether.tile_entities.TileEntityIncubator;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import org.lwjgl.opengl.GL11;

public class GuiIncubator extends GuiContainer
{
    private TileEntityIncubator IncubatorInventory;

    public GuiIncubator(InventoryPlayer var1, TileEntityIncubator var2)
    {
        super(new ContainerIncubator(var1, var2));
        this.IncubatorInventory = var2;
    }

    protected void drawGuiContainerForegroundLayer()
    {
        this.fontRenderer.drawString("孵化器", 60, 6, 4210752);
        this.fontRenderer.drawString("物品栏", 8, this.ySize - 96 + 2, 4210752);
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the items)
     */
    protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture("/net/aetherteam/aether/client/sprites/gui/incubator.png");
        int var4 = (this.width - this.xSize) / 2;
        int var5 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var4, var5, 0, 0, this.xSize, this.ySize);
        int var6;

        if (this.IncubatorInventory.isBurning())
        {
            var6 = this.IncubatorInventory.getBurnTimeRemainingScaled(12);
            this.drawTexturedModalRect(var4 + 74, var5 + 47 - var6, 176, 12 - var6, 14, var6 + 2);
        }

        var6 = this.IncubatorInventory.getCookProgressScaled(54);
        this.drawTexturedModalRect(var4 + 103, var5 + 70 - var6, 179, 70 - var6, 10, var6);
    }
}
