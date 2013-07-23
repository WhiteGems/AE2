package net.aetherteam.aether.tile_entities;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.aetherteam.aether.AetherMoaColour;
import net.aetherteam.aether.blocks.AetherBlocks;
import net.aetherteam.aether.entities.mounts.EntityMoa;
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

public class TileEntityIncubator extends TileEntity
    implements IInventory, ISidedInventory
{
    private ItemStack[] IncubatorItemStacks;
    public int torchPower;
    public int progress;
    public int ticksRequired = 6000;
    public EntityPlayer playerUsing;

    public TileEntityIncubator()
    {
        this.IncubatorItemStacks = new ItemStack[2];
        this.progress = 0;
    }

    public int getSizeInventory()
    {
        return this.IncubatorItemStacks.length;
    }

    public ItemStack getStackInSlot(int i)
    {
        return this.IncubatorItemStacks[i];
    }

    public ItemStack decrStackSize(int i, int j)
    {
        if (this.IncubatorItemStacks[i] != null)
        {
            if (this.IncubatorItemStacks[i].stackSize <= j)
            {
                ItemStack itemstack = this.IncubatorItemStacks[i];
                this.IncubatorItemStacks[i] = null;
                return itemstack;
            }

            ItemStack itemstack1 = this.IncubatorItemStacks[i].splitStack(j);

            if (this.IncubatorItemStacks[i].stackSize == 0)
            {
                this.IncubatorItemStacks[i] = null;
            }

            return itemstack1;
        }

        return null;
    }

    public ItemStack getStackInSlotOnClosing(int par1)
    {
        if (this.IncubatorItemStacks[par1] != null)
        {
            ItemStack var2 = this.IncubatorItemStacks[par1];
            this.IncubatorItemStacks[par1] = null;
            return var2;
        }

        return null;
    }

    public void setInventorySlotContents(int i, ItemStack itemstack)
    {
        this.IncubatorItemStacks[i] = itemstack;

        if ((itemstack != null) && (itemstack.stackSize > getInventoryStackLimit()))
        {
            itemstack.stackSize = getInventoryStackLimit();
        }
    }

    public String getInvName()
    {
        return "Incubator";
    }

    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);
        NBTTagList nbttaglist = nbttagcompound.getTagList("Items");
        this.IncubatorItemStacks = new ItemStack[getSizeInventory()];

        for (int i = 0; i < nbttaglist.tagCount(); i++)
        {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.tagAt(i);
            byte byte0 = nbttagcompound1.getByte("Slot");

            if ((byte0 >= 0) && (byte0 < this.IncubatorItemStacks.length))
            {
                this.IncubatorItemStacks[byte0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }

        this.progress = nbttagcompound.getShort("BurnTime");
    }
    public void openChest()
    {
    }

    public void closeChest()
    {
    }

    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setShort("BurnTime", (short)this.progress);
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.IncubatorItemStacks.length; i++)
        {
            if (this.IncubatorItemStacks[i] != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte)i);
                this.IncubatorItemStacks[i].writeToNBT(nbttagcompound1);
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
        return this.progress * i / this.ticksRequired;
    }

    @SideOnly(Side.CLIENT)
    public int getBurnTimeRemainingScaled(int i)
    {
        return this.torchPower * i / 500;
    }

    public boolean isBurning()
    {
        return this.torchPower > 0;
    }

    public void updateEntity()
    {
        if (this.torchPower > 0)
        {
            this.torchPower -= 1;

            if (getStackInSlot(1) != null)
            {
                this.progress += 1;
            }
        }

        if ((this.IncubatorItemStacks[1] == null) || (this.IncubatorItemStacks[1].itemID != AetherItems.MoaEgg.itemID))
        {
            this.progress = 0;
        }

        if (this.progress >= this.ticksRequired)
        {
            if (this.IncubatorItemStacks[1] != null)
            {
                if (!this.worldObj.isRemote)
                {
                    EntityMoa moa = new EntityMoa(this.worldObj, true, false, false, AetherMoaColour.getColour(this.IncubatorItemStacks[1].getItemDamage()), this.playerUsing);
                    moa.setPosition(this.xCoord + 0.5D, this.yCoord + 1.5D, this.zCoord + 0.5D);
                    this.worldObj.spawnEntityInWorld(moa);
                }
            }

            if (!this.worldObj.isRemote)
            {
                decrStackSize(1, 1);
            }

            this.progress = 0;
        }

        if ((this.torchPower <= 0) && (this.IncubatorItemStacks[1] != null) && (this.IncubatorItemStacks[1].itemID == AetherItems.MoaEgg.itemID) && (getStackInSlot(0) != null) && (getStackInSlot(0).itemID == AetherBlocks.AmbrosiumTorch.blockID))
        {
            this.torchPower += 1000;

            if (!this.worldObj.isRemote)
            {
                decrStackSize(0, 1);
            }
        }
    }

    public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
    {
        this.playerUsing = par1EntityPlayer;
        return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) == this;
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

