package net.aetherteam.aether.client.gui;

import net.aetherteam.aether.containers.ContainerEnchanter;
import net.aetherteam.aether.tile_entities.TileEntityEnchanter;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiEnchanter extends GuiContainer
{
    private static final ResourceLocation TEXTURE_ENCHANTER = new ResourceLocation("aether", "textures/gui/enchanter.png");
    private TileEntityEnchanter enchanterInventory;

    public GuiEnchanter(InventoryPlayer inventoryplayer, TileEntityEnchanter tileentityEnchanter)
    {
        super(new ContainerEnchanter(inventoryplayer, tileentityEnchanter));
        this.enchanterInventory = tileentityEnchanter;
    }

    protected void drawGuiContainerForegroundLayer()
    {
        this.fontRenderer.drawString("Altar", 60, 6, 4210752);
        this.fontRenderer.drawString("Inventory", 8, this.ySize - 96 + 2, 4210752);
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the items)
     */
    protected void drawGuiContainerBackgroundLayer(float f, int ia, int ib)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.func_110577_a(TEXTURE_ENCHANTER);
        int j = (this.width - this.xSize) / 2;
        int k = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(j, k, 0, 0, this.xSize, this.ySize);
        int i1;

        if (this.enchanterInventory.isBurning())
        {
            i1 = this.enchanterInventory.getBurnTimeRemainingScaled(12);
            this.drawTexturedModalRect(j + 57, k + 47 - i1, 176, 12 - i1, 14, i1 + 2);
        }

        i1 = this.enchanterInventory.getCookProgressScaled(24);
        this.drawTexturedModalRect(j + 79, k + 35, 176, 14, i1 + 1, 16);
    }
}
