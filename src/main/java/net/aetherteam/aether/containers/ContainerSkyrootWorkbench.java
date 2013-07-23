package net.aetherteam.aether.containers;

import net.aetherteam.aether.blocks.AetherBlocks;
import net.minecraft.block.Block;
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

    public ContainerSkyrootWorkbench(InventoryPlayer par1InventoryPlayer, World par2World, int par3, int par4, int par5)
    {
        super(par1InventoryPlayer, par2World, par3, par4, par5);
        this.worldObj = par2World;
        this.posX = par3;
        this.posY = par4;
        this.posZ = par5;
    }

    public boolean canInteractWith(EntityPlayer par1EntityPlayer)
    {
        return this.worldObj.getBlockId(this.posX, this.posY, this.posZ) == AetherBlocks.SkyrootCraftingTable.blockID;
    }
}

