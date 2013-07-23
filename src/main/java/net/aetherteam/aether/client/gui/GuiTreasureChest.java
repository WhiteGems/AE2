package net.aetherteam.aether.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
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
        this.l = false;
        char c = 'Þ';
        int i = c - 'l';
        this.inventoryRows = (iinventory1.getSizeInventory() / 9);
        this.ySize = (i + this.inventoryRows * 18);

        switch (meta)
        {
            case 1:
                this.name = "Bronze Treasure Chest";
                break;

            case 3:
                this.name = "Silver Treasure Chest";
                break;

            case 5:
                this.name = "Gold Treasure Chest";

            case 2:
            case 4:
        }
    }

    protected void drawGuiContainerForegroundLayer()
    {
        this.m.drawString(this.name, 8, 6, 4210752);
        this.m.drawString(this.upperChestInventory.getInvName(), 8, this.ySize - 96 + 2, 4210752);
    }

    protected void drawGuiContainerBackgroundLayer(float f, int i1, int i2)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.g.renderEngine.b("/gui/container.png");
        int j = (this.height - this.xSize) / 2;
        int k = (this.i - this.ySize) / 2;
        drawTexturedModalRect(j, k, 0, 0, this.xSize, this.inventoryRows * 18 + 17);
        drawTexturedModalRect(j, k + this.inventoryRows * 18 + 17, 0, 126, this.xSize, 96);
    }
}

