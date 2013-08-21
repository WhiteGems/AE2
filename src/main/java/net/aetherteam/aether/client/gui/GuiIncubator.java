package net.aetherteam.aether.client.gui;

import net.aetherteam.aether.containers.ContainerIncubator;
import net.aetherteam.aether.tile_entities.TileEntityIncubator;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiIncubator extends GuiContainer
{
    private static final ResourceLocation TEXTURE_INCUBATOR = new ResourceLocation("aether", "textures/gui/incubator.png");
    private TileEntityIncubator IncubatorInventory;

    public GuiIncubator(InventoryPlayer inventoryplayer, TileEntityIncubator tileentityIncubator)
    {
        super(new ContainerIncubator(inventoryplayer, tileentityIncubator));
        this.IncubatorInventory = tileentityIncubator;
    }

    protected void drawGuiContainerForegroundLayer()
    {
        this.fontRenderer.drawString("孵化器", 60, 6, 4210752);
        this.fontRenderer.drawString("物品栏", 8, this.ySize - 96 + 2, 4210752);
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the items)
     */
    protected void drawGuiContainerBackgroundLayer(float f, int ia, int ib)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.func_110577_a(TEXTURE_INCUBATOR);
        int j = (this.width - this.xSize) / 2;
        int k = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(j, k, 0, 0, this.xSize, this.ySize);
        int i1;

        if (this.IncubatorInventory.isBurning())
        {
            i1 = this.IncubatorInventory.getBurnTimeRemainingScaled(12);
            this.drawTexturedModalRect(j + 74, k + 47 - i1, 176, 12 - i1, 14, i1 + 2);
        }

        i1 = this.IncubatorInventory.getCookProgressScaled(54);
        this.drawTexturedModalRect(j + 103, k + 70 - i1, 179, 70 - i1, 10, i1);
    }
}
