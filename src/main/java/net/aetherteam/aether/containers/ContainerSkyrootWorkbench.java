package net.aetherteam.aether.containers;

import net.aetherteam.aether.blocks.AetherBlocks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.world.World;

public class ContainerSkyrootWorkbench extends ContainerWorkbench
{
    private World worldObj;
    private int posX;
    private int posY;
    private int posZ;

    public ContainerSkyrootWorkbench(InventoryPlayer var1, World var2, int var3, int var4, int var5)
    {
        super(var1, var2, var3, var4, var5);
        this.worldObj = var2;
        this.posX = var3;
        this.posY = var4;
        this.posZ = var5;
    }

    public boolean canInteractWith(EntityPlayer var1)
    {
        return this.worldObj.getBlockId(this.posX, this.posY, this.posZ) != AetherBlocks.SkyrootCraftingTable.blockID ? false : var1.getDistanceSq((double)this.posX + 0.5D, (double)this.posY + 0.5D, (double)this.posZ + 0.5D) <= 64.0D;
    }
}
