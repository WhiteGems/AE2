package net.aetherteam.aether.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderEngine;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import org.lwjgl.opengl.GL11;

public class GuiTreasureChest extends GuiContainer
{
    private IInventory upperChestInventory;
    private IInventory lowerChestInventory;
    private int inventoryRows;
    private String name;

    public GuiTreasureChest(IInventory iinventory, IInventory iinventory1, int meta)
    {
        super(new ContainerChest(iinventory, iinventory1));
        this.inventoryRows = 0;
        this.upperChestInventory = iinventory;
        this.lowerChestInventory = iinventory1;
        this.allowUserInput = false;
        char c = 'Þ';
        int i = c - 'l';
        this.inventoryRows = (iinventory1.getSizeInventory() / 9);
        this.ySize = (i + this.inventoryRows * 18);
        switch (meta)
        {
            case 1:
                this.name = "青铜宝箱";
                break;
            case 3:
                this.name = "白银宝箱";
                break;
            case 5:
                this.name = "黄金宝箱";
            case 2:
            case 4:
        }
    }

    protected void drawGuiContainerForegroundLayer()
    {
        this.fontRenderer.drawString(this.name, 8, 6, 4210752);
        this.fontRenderer.drawString(this.upperChestInventory.getInvName(), 8, this.ySize - 96 + 2, 4210752);
    }

    protected void drawGuiContainerBackgroundLayer(float f, int i1, int i2)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture("/gui/container.png");
        int j = (this.width - this.xSize) / 2;
        int k = (this.height - this.ySize) / 2;
        drawTexturedModalRect(j, k, 0, 0, this.xSize, this.inventoryRows * 18 + 17);
        drawTexturedModalRect(j, k + this.inventoryRows * 18 + 17, 0, 126, this.xSize, 96);
    }
}

/* Location:           D:\Dev\Mc\forge_orl\mcp\jars\bin\aether.jar
 * Qualified Name:     net.aetherteam.aether.client.gui.GuiTreasureChest
 * JD-Core Version:    0.6.2
 */