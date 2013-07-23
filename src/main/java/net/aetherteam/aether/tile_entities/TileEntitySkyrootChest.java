package net.aetherteam.aether.tile_entities;

import java.util.Random;
import net.aetherteam.aether.blocks.AetherBlocks;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TileEntitySkyrootChest extends TileEntity
    implements IInventory
{
    private ItemStack[] chestContents = new ItemStack[36];

    public boolean adjacentChestChecked = false;
    public TileEntitySkyrootChest adjacentChestZNeg;
    public TileEntitySkyrootChest adjacentChestXPos;
    public TileEntitySkyrootChest adjacentChestXNeg;
    public TileEntitySkyrootChest adjacentChestZPosition;
    public float lidAngle;
    public float prevLidAngle;
    public int numUsingPlayers;
    private int ticksSinceSync;

    public int getSizeInventory()
    {
        return 27;
    }

    public ItemStack getStackInSlot(int par1)
    {
        return getChestContents()[par1];
    }

    public ItemStack decrStackSize(int par1, int par2)
    {
        if (getChestContents()[par1] != null)
        {
            if (getChestContents()[par1].stackSize <= par2)
            {
                ItemStack var3 = getChestContents()[par1];
                getChestContents()[par1] = null;
                onInventoryChanged();
                return var3;
            }

            ItemStack var3 = getChestContents()[par1].splitStack(par2);

            if (getChestContents()[par1].stackSize == 0)
            {
                getChestContents()[par1] = null;
            }

            onInventoryChanged();
            return var3;
        }

        return null;
    }

    public ItemStack getStackInSlotOnClosing(int par1)
    {
        if (getChestContents()[par1] != null)
        {
            ItemStack var2 = getChestContents()[par1];
            getChestContents()[par1] = null;
            return var2;
        }

        return null;
    }

    public void setInventorySlotContents(int par1, ItemStack par2ItemStack)
    {
        getChestContents()[par1] = par2ItemStack;

        if ((par2ItemStack != null) && (par2ItemStack.stackSize > getInventoryStackLimit()))
        {
            par2ItemStack.stackSize = getInventoryStackLimit();
        }

        onInventoryChanged();
    }

    public String getInvName()
    {
        return "container.chest";
    }

    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);
        NBTTagList var2 = par1NBTTagCompound.getTagList("Items");
        setChestContents(new ItemStack[getSizeInventory()]);

        for (int var3 = 0; var3 < var2.tagCount(); var3++)
        {
            NBTTagCompound var4 = (NBTTagCompound)var2.tagAt(var3);
            int var5 = var4.getByte("Slot") & 0xFF;

            if ((var5 >= 0) && (var5 < getChestContents().length))
            {
                getChestContents()[var5] = ItemStack.loadItemStackFromNBT(var4);
            }
        }
    }

    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);
        NBTTagList var2 = new NBTTagList();

        for (int var3 = 0; var3 < getChestContents().length; var3++)
        {
            if (getChestContents()[var3] != null)
            {
                NBTTagCompound var4 = new NBTTagCompound();
                var4.setByte("Slot", (byte)var3);
                getChestContents()[var3].writeToNBT(var4);
                var2.appendTag(var4);
            }
        }

        par1NBTTagCompound.setTag("Items", var2);
    }

    public int getInventoryStackLimit()
    {
        return 64;
    }

    public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
    {
        return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) == this;
    }

    public void updateContainingBlockInfo()
    {
        super.updateContainingBlockInfo();
        this.adjacentChestChecked = false;
    }

    public void checkForAdjacentChests()
    {
        if (!this.adjacentChestChecked)
        {
            this.adjacentChestChecked = true;
            this.adjacentChestZNeg = null;
            this.adjacentChestXPos = null;
            this.adjacentChestXNeg = null;
            this.adjacentChestZPosition = null;

            if (this.worldObj.getBlockId(this.xCoord - 1, this.yCoord, this.zCoord) == AetherBlocks.SkyrootChest.blockID)
            {
                this.adjacentChestXNeg = ((TileEntitySkyrootChest)this.worldObj.getBlockTileEntity(this.xCoord - 1, this.yCoord, this.zCoord));
            }

            if (this.worldObj.getBlockId(this.xCoord + 1, this.yCoord, this.zCoord) == AetherBlocks.SkyrootChest.blockID)
            {
                this.adjacentChestXPos = ((TileEntitySkyrootChest)this.worldObj.getBlockTileEntity(this.xCoord + 1, this.yCoord, this.zCoord));
            }

            if (this.worldObj.getBlockId(this.xCoord, this.yCoord, this.zCoord - 1) == AetherBlocks.SkyrootChest.blockID)
            {
                this.adjacentChestZNeg = ((TileEntitySkyrootChest)this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord - 1));
            }

            if (this.worldObj.getBlockId(this.xCoord, this.yCoord, this.zCoord + 1) == AetherBlocks.SkyrootChest.blockID)
            {
                this.adjacentChestZPosition = ((TileEntitySkyrootChest)this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord + 1));
            }

            if (this.adjacentChestZNeg != null)
            {
                this.adjacentChestZNeg.updateContainingBlockInfo();
            }

            if (this.adjacentChestZPosition != null)
            {
                this.adjacentChestZPosition.updateContainingBlockInfo();
            }

            if (this.adjacentChestXPos != null)
            {
                this.adjacentChestXPos.updateContainingBlockInfo();
            }

            if (this.adjacentChestXNeg != null)
            {
                this.adjacentChestXNeg.updateContainingBlockInfo();
            }
        }
    }

    public void updateEntity()
    {
        super.updateEntity();
        checkForAdjacentChests();

        if (++this.ticksSinceSync % 20 * 4 == 0);

        this.prevLidAngle = this.lidAngle;
        float var1 = 0.1F;

        if ((this.numUsingPlayers > 0) && (this.lidAngle == 0.0F) && (this.adjacentChestZNeg == null) && (this.adjacentChestXNeg == null))
        {
            double var2 = this.xCoord + 0.5D;
            double var4 = this.zCoord + 0.5D;

            if (this.adjacentChestZPosition != null)
            {
                var4 += 0.5D;
            }

            if (this.adjacentChestXPos != null)
            {
                var2 += 0.5D;
            }

            this.worldObj.playSoundEffect(var2, this.yCoord + 0.5D, var4, "random.chestopen", 0.5F, this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
        }

        if (((this.numUsingPlayers == 0) && (this.lidAngle > 0.0F)) || ((this.numUsingPlayers > 0) && (this.lidAngle < 1.0F)))
        {
            float var8 = this.lidAngle;

            if (this.numUsingPlayers > 0)
            {
                this.lidAngle += var1;
            }
            else
            {
                this.lidAngle -= var1;
            }

            if (this.lidAngle > 1.0F)
            {
                this.lidAngle = 1.0F;
            }

            float var3 = 0.5F;

            if ((this.lidAngle < var3) && (var8 >= var3) && (this.adjacentChestZNeg == null) && (this.adjacentChestXNeg == null))
            {
                double var4 = this.xCoord + 0.5D;
                double var6 = this.zCoord + 0.5D;

                if (this.adjacentChestZPosition != null)
                {
                    var6 += 0.5D;
                }

                if (this.adjacentChestXPos != null)
                {
                    var4 += 0.5D;
                }

                this.worldObj.playSoundEffect(var4, this.yCoord + 0.5D, var6, "random.chestclosed", 0.5F, this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
            }

            if (this.lidAngle < 0.0F)
            {
                this.lidAngle = 0.0F;
            }
        }
    }

    public boolean receiveClientEvent(int par1, int par2)
    {
        if (par1 == 1)
        {
            this.numUsingPlayers = par2;
        }

        return true;
    }

    public void openChest()
    {
        this.numUsingPlayers += 1;
        this.worldObj.addBlockEvent(this.xCoord, this.yCoord, this.zCoord, AetherBlocks.SkyrootChest.blockID, 1, this.numUsingPlayers);
    }

    public void closeChest()
    {
        this.numUsingPlayers -= 1;
        this.worldObj.addBlockEvent(this.xCoord, this.yCoord, this.zCoord, AetherBlocks.SkyrootChest.blockID, 1, this.numUsingPlayers);
    }

    public void invalidate()
    {
        updateContainingBlockInfo();
        checkForAdjacentChests();
        super.invalidate();
    }

    public ItemStack[] getChestContents()
    {
        return this.chestContents;
    }

    public void setChestContents(ItemStack[] chestContents)
    {
        this.chestContents = chestContents;
    }

    public boolean isInvNameLocalized()
    {
        return false;
    }

    public boolean isStackValidForSlot(int i, ItemStack itemstack)
    {
        return false;
    }
}

