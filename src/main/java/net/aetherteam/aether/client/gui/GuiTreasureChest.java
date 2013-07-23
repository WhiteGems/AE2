package net.aetherteam.aether.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import org.lwjgl.opengl.GL11;

public class GuiTreasureChest extends GuiContainer
{
    private IInventory upperChestInventory;
    private IInventory lowerChestInventory;
    private int inventoryRows = 0;
    private String name;

    public GuiTreasureChest(IInventory var1, IInventory var2, int var3)
    {
        super(new ContainerChest(var1, var2));
        this.upperChestInventory = var1;
        this.lowerChestInventory = var2;
        this.allowUserInput = false;
        short var4 = 222;
        int var5 = var4 - 108;
        this.inventoryRows = var2.getSizeInventory() / 9;
        this.ySize = var5 + this.inventoryRows * 18;

        switch (var3)
        {
            case 1:
                this.name = "青铜宝箱";

            case 2:
            case 4:
            default:
                break;

            case 3:
                this.name = "白银宝箱";
                break;

            case 5:
                this.name = "黄金宝箱";
        }
    }

    protected void drawGuiContainerForegroundLayer()
    {
        this.fontRenderer.drawString(this.name, 8, 6, 4210752);
        this.fontRenderer.drawString(this.upperChestInventory.getInvName(), 8, this.ySize - 96 + 2, 4210752);
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the items)
     */
    protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture("/gui/container.png");
        int var4 = (this.width - this.xSize) / 2;
        int var5 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var4, var5, 0, 0, this.xSize, this.inventoryRows * 18 + 17);
        this.drawTexturedModalRect(var4, var5 + this.inventoryRows * 18 + 17, 0, 126, this.xSize, 96);
    }
}
