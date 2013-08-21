package net.aetherteam.aether.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiTreasureChest extends GuiContainer
{
    private static final ResourceLocation TEXTURE_CONTAINER = new ResourceLocation("textures/gui/container/generic_54.png");
    private IInventory upperChestInventory;
    private IInventory lowerChestInventory;
    private int inventoryRows = 0;
    private String name;

    public GuiTreasureChest(IInventory iinventory, IInventory iinventory1, int meta)
    {
        super(new ContainerChest(iinventory, iinventory1));
        this.upperChestInventory = iinventory;
        this.lowerChestInventory = iinventory1;
        this.allowUserInput = false;
        short c = 222;
        int i = c - 108;
        this.inventoryRows = iinventory1.getSizeInventory() / 9;
        this.ySize = i + this.inventoryRows * 18;

        switch (meta)
        {
            case 1:
                this.name = "Bronze Treasure Chest";

            case 2:
            case 4:
            default:
                break;

            case 3:
                this.name = "Silver Treasure Chest";
                break;

            case 5:
                this.name = "Gold Treasure Chest";
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
    protected void drawGuiContainerBackgroundLayer(float f, int i1, int i2)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.func_110577_a(TEXTURE_CONTAINER);
        int j = (this.width - this.xSize) / 2;
        int k = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(j, k, 0, 0, this.xSize, this.inventoryRows * 18 + 17);
        this.drawTexturedModalRect(j, k + this.inventoryRows * 18 + 17, 0, 126, this.xSize, 96);
    }
}
