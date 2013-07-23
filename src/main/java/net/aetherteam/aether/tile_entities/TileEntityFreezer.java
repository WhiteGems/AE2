package net.aetherteam.aether.tile_entities;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.List;
import net.aetherteam.aether.AetherFrozen;
import net.aetherteam.aether.blocks.AetherBlocks;
import net.aetherteam.aether.items.AetherItems;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.ISidedInventory;

public class TileEntityFreezer extends TileEntity
    implements IInventory, ISidedInventory
{
    private static List frozen = new ArrayList();
    private ItemStack[] frozenItemStacks;
    public int frozenProgress;
    public int frozenPowerRemaining;
    public int frozenTimeForItem;
    private AetherFrozen currentFrozen;

    public TileEntityFreezer()
    {
        this.frozenItemStacks = new ItemStack[3];
        this.frozenProgress = 0;
        this.frozenPowerRemaining = 0;
        this.frozenTimeForItem = 0;
    }

    public int getSizeInventory()
    {
        return this.frozenItemStacks.length;
    }

    public ItemStack getStackInSlot(int i)
    {
        return this.frozenItemStacks[i];
    }

    public ItemStack decrStackSize(int i, int j)
    {
        if (this.frozenItemStacks[i] != null)
        {
            if (this.frozenItemStacks[i].stackSize <= j)
            {
                ItemStack itemstack = this.frozenItemStacks[i];
                this.frozenItemStacks[i] = null;
                return itemstack;
            }

            ItemStack itemstack1 = this.frozenItemStacks[i].splitStack(j);

            if (this.frozenItemStacks[i].stackSize == 0)
            {
                this.frozenItemStacks[i] = null;
            }

            return itemstack1;
        }

        return null;
    }

    public ItemStack getStackInSlotOnClosing(int par1)
    {
        if (this.frozenItemStacks[par1] != null)
        {
            ItemStack var2 = this.frozenItemStacks[par1];
            this.frozenItemStacks[par1] = null;
            return var2;
        }

        return null;
    }

    public void setInventorySlotContents(int i, ItemStack itemstack)
    {
        this.frozenItemStacks[i] = itemstack;

        if ((itemstack != null) && (itemstack.stackSize > getInventoryStackLimit()))
        {
            itemstack.stackSize = getInventoryStackLimit();
        }
    }

    public String getInvName()
    {
        return "Freezer";
    }

    public void openChest()
    {
    }

    public void closeChest()
    {
    }

    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);
        NBTTagList nbttaglist = nbttagcompound.getTagList("Items");
        this.frozenItemStacks = new ItemStack[getSizeInventory()];

        for (int i = 0; i < nbttaglist.tagCount(); i++)
        {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.tagAt(i);
            byte byte0 = nbttagcompound1.getByte("Slot");

            if ((byte0 >= 0) && (byte0 < this.frozenItemStacks.length))
            {
                this.frozenItemStacks[byte0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }

        this.frozenProgress = nbttagcompound.getShort("BurnTime");
        this.frozenTimeForItem = nbttagcompound.getShort("CookTime");
    }

    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setShort("BurnTime", (short)this.frozenProgress);
        nbttagcompound.setShort("CookTime", (short)this.frozenTimeForItem);
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.frozenItemStacks.length; i++)
        {
            if (this.frozenItemStacks[i] != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte)i);
                this.frozenItemStacks[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }

        nbttagcompound.setTag("Items", nbttaglist);
    }

    public int getInventoryStackLimit()
    {
        return 64;
    }

    @SideOnly(Side.CLIENT)
    public int getCookProgressScaled(int i)
    {
        if (this.frozenTimeForItem == 0)
        {
            return 0;
        }

        return this.frozenProgress * i / this.frozenTimeForItem;
    }

    @SideOnly(Side.CLIENT)
    public int getBurnTimeRemainingScaled(int i)
    {
        return this.frozenPowerRemaining * i / 500;
    }

    public boolean isBurning()
    {
        return this.frozenPowerRemaining > 0;
    }

    public void updateEntity()
    {
        if (this.frozenPowerRemaining > 0)
        {
            this.frozenPowerRemaining -= 1;

            if (this.currentFrozen != null)
            {
                this.frozenProgress += 1;
            }
        }

        if ((this.currentFrozen != null) && ((this.frozenItemStacks[0] == null) || (this.frozenItemStacks[0].itemID != this.currentFrozen.frozenFrom.itemID)))
        {
            this.currentFrozen = null;
            this.frozenProgress = 0;
        }

        if ((this.currentFrozen != null) && (this.frozenProgress >= this.currentFrozen.frozenPowerNeeded))
        {
            if (!this.worldObj.isRemote)
            {
                if (this.frozenItemStacks[2] == null)
                {
                    setInventorySlotContents(2, new ItemStack(this.currentFrozen.frozenTo.getItem(), 1, this.currentFrozen.frozenTo.getItemDamage()));
                }
                else
                {
                    setInventorySlotContents(2, new ItemStack(this.currentFrozen.frozenTo.getItem(), getStackInSlot(2).stackSize + 1, this.currentFrozen.frozenTo.getItemDamage()));
                }

                if ((getStackInSlot(0).itemID == Item.bucketWater.itemID) || (getStackInSlot(0).itemID == Item.bucketLava.itemID))
                {
                    setInventorySlotContents(0, new ItemStack(Item.bucketEmpty));
                }
                else if (getStackInSlot(0).itemID == AetherItems.SkyrootBucket.itemID)
                {
                    setInventorySlotContents(0, new ItemStack(AetherItems.SkyrootBucket));
                }
                else
                {
                    decrStackSize(0, 1);
                }
            }

            this.frozenProgress = 0;
            this.currentFrozen = null;
            this.frozenTimeForItem = 0;
        }

        if ((this.frozenPowerRemaining <= 0) && (this.currentFrozen != null) && (getStackInSlot(1) != null) && (getStackInSlot(1).itemID == AetherBlocks.Icestone.blockID))
        {
            this.frozenPowerRemaining += 500;

            if (!this.worldObj.isRemote)
            {
                decrStackSize(1, 1);
            }
        }

        if (this.currentFrozen == null)
        {
            ItemStack itemstack = getStackInSlot(0);

            for (int i = 0; i < frozen.size(); i++)
            {
                if ((itemstack != null) && (frozen.get(i) != null) && (itemstack.itemID == ((AetherFrozen)frozen.get(i)).frozenFrom.itemID) && (itemstack.getItemDamage() == ((AetherFrozen)frozen.get(i)).frozenFrom.getItemDamage()))
                {
                    if (this.frozenItemStacks[2] == null)
                    {
                        this.currentFrozen = ((AetherFrozen)frozen.get(i));
                        this.frozenTimeForItem = this.currentFrozen.frozenPowerNeeded;
                    }
                    else if ((this.frozenItemStacks[2].itemID == ((AetherFrozen)frozen.get(i)).frozenTo.itemID) && (((AetherFrozen)frozen.get(i)).frozenTo.getItem().getItemStackLimit() > this.frozenItemStacks[2].stackSize))
                    {
                        this.currentFrozen = ((AetherFrozen)frozen.get(i));
                        this.frozenTimeForItem = this.currentFrozen.frozenPowerNeeded;
                    }
                }
            }
        }
    }

    public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
    {
        return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) == this;
    }

    public static void addFreezable(ItemStack from, ItemStack to, int i)
    {
        frozen.add(new AetherFrozen(from, to, i));
    }

    public int getStartInventorySide(ForgeDirection side)
    {
        if (side == ForgeDirection.DOWN)
        {
            return 1;
        }

        if (side == ForgeDirection.UP)
        {
            return 0;
        }

        return 2;
    }

    public int getSizeInventorySide(ForgeDirection side)
    {
        return 1;
    }

    public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt)
    {
        readFromNBT(pkt.customParam1);
    }

    public Packet getDescriptionPacket()
    {
        NBTTagCompound var1 = new NBTTagCompound();
        writeToNBT(var1);
        return new Packet132TileEntityData(this.xCoord, this.yCoord, this.zCoord, 1, var1);
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

