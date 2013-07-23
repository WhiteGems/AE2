package net.aetherteam.aether.tile_entities;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.aetherteam.aether.Aether;
import net.aetherteam.aether.AetherEnchantment;
import net.aetherteam.aether.entities.altar.EntityFakeItem;
import net.aetherteam.aether.items.AetherItems;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.ISidedInventory;

public class TileEntityAltar extends TileEntity implements IInventory, ISidedInventory
{
    private static List enchantments = new ArrayList();
    private Random rand = new Random();
    private EntityFakeItem renderedItem = null;
    private ItemStack[] enchanterItemStacks = new ItemStack[3];
    public ItemStack enchantableItem;
    public ItemStack ambrosiumItems;
    public int enchantProgress = 0;
    public int enchantPowerRemaining = 0;
    public int enchantTimeForItem = 0;
    private float ambRotationSpeed = 0.05F;
    private float ambRotation = 0.0F;
    private double ambSpinningSpeed = 0.0D;
    private double itemFloatingSpeed = 0.0D;
    private AetherEnchantment currentEnchantment;

    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory()
    {
        return this.enchanterItemStacks.length;
    }

    /**
     * Returns the stack in slot i
     */
    public ItemStack getStackInSlot(int var1)
    {
        return this.enchanterItemStacks[var1];
    }

    /**
     * Removes from an inventory slot (first arg) up to a specified number (second arg) of items and returns them in a
     * new stack.
     */
    public ItemStack decrStackSize(int var1, int var2)
    {
        if (this.enchanterItemStacks[var1] != null)
        {
            ItemStack var3;

            if (this.enchanterItemStacks[var1].stackSize <= var2)
            {
                var3 = this.enchanterItemStacks[var1];
                this.enchanterItemStacks[var1] = null;

                if (!this.worldObj.isRemote)
                {
                    this.sendToAllInOurWorld(this.getDescriptionPacket());
                }

                return var3;
            }
            else
            {
                var3 = this.enchanterItemStacks[var1].splitStack(var2);

                if (this.enchanterItemStacks[var1].stackSize == 0)
                {
                    this.enchanterItemStacks[var1] = null;
                }

                if (!this.worldObj.isRemote)
                {
                    this.sendToAllInOurWorld(this.getDescriptionPacket());
                }

                return var3;
            }
        }
        else
        {
            return null;
        }
    }

    /**
     * When some containers are closed they call this on each slot, then drop whatever it returns as an EntityItem -
     * like when you close a workbench GUI.
     */
    public ItemStack getStackInSlotOnClosing(int var1)
    {
        if (this.enchanterItemStacks[var1] != null)
        {
            ItemStack var2 = this.enchanterItemStacks[var1];
            this.enchanterItemStacks[var1] = null;
            return var2;
        }
        else
        {
            return null;
        }
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public void setInventorySlotContents(int var1, ItemStack var2)
    {
        this.enchanterItemStacks[var1] = var2;

        if (var1 == 0)
        {
            this.currentEnchantment = null;
        }

        if (var2 != null && var2.stackSize > this.getInventoryStackLimit())
        {
            var2.stackSize = this.getInventoryStackLimit();
        }

        if (!this.worldObj.isRemote)
        {
            this.sendToAllInOurWorld(this.getDescriptionPacket());
        }
    }

    /**
     * Returns the name of the inventory.
     */
    public String getInvName()
    {
        return "Enchanter";
    }

    /**
     * Reads a tile entity from NBT.
     */
    public void readFromNBT(NBTTagCompound var1)
    {
        super.readFromNBT(var1);
        NBTTagList var2 = var1.getTagList("Items");
        this.enchanterItemStacks = new ItemStack[this.getSizeInventory()];

        for (int var3 = 0; var3 < var2.tagCount(); ++var3)
        {
            NBTTagCompound var4 = (NBTTagCompound)var2.tagAt(var3);
            byte var5 = var4.getByte("Slot");

            if (var5 >= 0 && var5 < this.enchanterItemStacks.length)
            {
                this.enchanterItemStacks[var5] = ItemStack.loadItemStackFromNBT(var4);
            }
        }

        this.enchantProgress = var1.getShort("BurnTime");
        this.enchantTimeForItem = var1.getShort("CookTime");
    }

    /**
     * Writes a tile entity to NBT.
     */
    public void writeToNBT(NBTTagCompound var1)
    {
        super.writeToNBT(var1);
        var1.setShort("BurnTime", (short)this.enchantProgress);
        var1.setShort("CookTime", (short)this.enchantTimeForItem);
        NBTTagList var2 = new NBTTagList();

        for (int var3 = 0; var3 < this.enchanterItemStacks.length; ++var3)
        {
            if (this.enchanterItemStacks[var3] != null)
            {
                NBTTagCompound var4 = new NBTTagCompound();
                var4.setByte("Slot", (byte)var3);
                this.enchanterItemStacks[var3].writeToNBT(var4);
                var2.appendTag(var4);
            }
        }

        var1.setTag("Items", var2);
    }

    /**
     * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended. *Isn't
     * this more of a set than a get?*
     */
    public int getInventoryStackLimit()
    {
        return 16;
    }

    public void addEnchantable(ItemStack var1)
    {
        if (var1 != null && this.isEnchantable(var1))
        {
            ItemStack var2 = this.getStackInSlot(0);
            int var3 = this.getInventoryStackLimit();

            if (this.isLimitedToOne(var1))
            {
                var3 = 1;
            }

            if (var2 == null)
            {
                if (var1.stackSize > var3)
                {
                    var2 = new ItemStack(var1.itemID, var3, var1.getItemDamage());
                    var1.stackSize -= var3;
                    this.setInventorySlotContents(0, var2);
                }
                else
                {
                    var2 = new ItemStack(var1.itemID, var1.stackSize, var1.getItemDamage());
                    var1.stackSize = 0;
                    this.setInventorySlotContents(0, var2);
                }
            }
            else if (var2.itemID == var1.itemID && var2.getItemDamage() == var1.getItemDamage())
            {
                if (this.canCombineStackWithRemainder(var1, var2, var3))
                {
                    this.combineStackWithRemainder(var1, var2, var3);
                }
                else if (this.stackIsFull(var2, var3))
                {
                    if (this.worldObj.isRemote)
                    {
                        FMLClientHandler.instance().getClient().ingameGUI.getChatGUI().printChatMessage("Altar is at full capacity.");
                    }
                }
                else
                {
                    var2.stackSize += var1.stackSize;
                    var1.stackSize = 0;
                }
            }

            this.onInventoryChanged();

            if (!this.worldObj.isRemote)
            {
                this.sendToAllInOurWorld(this.getDescriptionPacket());
            }
        }
    }

    public boolean canEnchant()
    {
        return this.worldObj.getBlockId(this.xCoord, this.yCoord + 1, this.zCoord) == 0;
    }

    public int getRemainingStackSize(ItemStack var1, ItemStack var2, int var3)
    {
        if (this.canCombineStackWithRemainder(var1, var2, var3))
        {
            int var4 = var2.stackSize + var1.stackSize - var3;
            return var4;
        }
        else
        {
            return 0;
        }
    }

    public boolean canCombineStackWithRemainder(ItemStack var1, ItemStack var2, int var3)
    {
        return var1.itemID == var2.itemID && var1.getItemDamage() == var2.getItemDamage() ? (this.stackIsFull(var2, var3) ? false : var2.stackSize + var1.stackSize > var3) : false;
    }

    public boolean stackIsFull(ItemStack var1, int var2)
    {
        return var1.stackSize == var2;
    }

    public void combineStackWithRemainder(ItemStack var1, ItemStack var2, int var3)
    {
        if (this.canCombineStackWithRemainder(var1, var2, var3))
        {
            int var4 = this.getRemainingStackSize(var1, var2, var3);
            var1.stackSize = var4;
            var2.stackSize = this.getInventoryStackLimit();
        }
    }

    public void addAmbrosium(ItemStack var1)
    {
        if (var1 != null && var1.itemID == AetherItems.AmbrosiumShard.itemID)
        {
            ItemStack var2 = this.getStackInSlot(1);

            if (var2 == null || var2.stackSize < this.getInventoryStackLimit())
            {
                if (var2 == null)
                {
                    var2 = new ItemStack(AetherItems.AmbrosiumShard.itemID, 1, 0);
                    this.setInventorySlotContents(1, var2);
                    --var1.stackSize;
                }
                else
                {
                    ++var2.stackSize;
                    --var1.stackSize;
                }

                this.onInventoryChanged();

                if (!this.worldObj.isRemote)
                {
                    this.sendToAllInOurWorld(this.getDescriptionPacket());
                }
            }
        }
    }

    public void dropNextStack()
    {
        EntityItem var1;

        if (this.enchanterItemStacks[1] != null)
        {
            if (!this.worldObj.isRemote)
            {
                var1 = new EntityItem(this.worldObj, (double)((float)this.xCoord + 0.5F), (double)((float)this.yCoord + 1.0F), (double)((float)this.zCoord + 0.5F), this.enchanterItemStacks[1]);
                var1.delayBeforeCanPickup = 10;
                this.worldObj.spawnEntityInWorld(var1);
            }

            this.decrStackSize(1, this.enchanterItemStacks[1].stackSize);
        }
        else if (this.enchanterItemStacks[0] != null)
        {
            if (!this.worldObj.isRemote)
            {
                var1 = new EntityItem(this.worldObj, (double)((float)this.xCoord + 0.5F), (double)((float)this.yCoord + 1.0F), (double)((float)this.zCoord + 0.5F), this.enchanterItemStacks[0]);
                var1.delayBeforeCanPickup = 10;
                this.worldObj.spawnEntityInWorld(var1);
            }

            this.decrStackSize(0, this.enchanterItemStacks[0].stackSize);
        }
    }

    @SideOnly(Side.CLIENT)
    public int getCookProgressScaled(int var1)
    {
        return this.enchantTimeForItem == 0 ? 0 : this.enchantProgress * var1 / this.enchantTimeForItem;
    }

    @SideOnly(Side.CLIENT)
    public int getBurnTimeRemainingScaled(int var1)
    {
        return this.enchantPowerRemaining * var1 / 500;
    }

    public boolean isBurning()
    {
        return this.enchantPowerRemaining > 0;
    }

    /**
     * Allows the entity to update its state. Overridden in most subclasses, e.g. the mob spawner uses this to count
     * ticks and creates a new spawn inside its implementation.
     */
    public void updateEntity()
    {
        if (!this.worldObj.isRemote)
        {
            if (this.renderedItem == null)
            {
                if (this.getEnchanterStacks(0) != null)
                {
                    this.renderedItem = new EntityFakeItem(this.worldObj, (double)this.xCoord + 0.5D, (double)this.yCoord + 1.15D, (double)this.zCoord + 0.5D, this.getEnchanterStacks(0));
                    this.worldObj.spawnEntityInWorld(this.renderedItem);
                }
            }
            else if (this.getEnchanterStacks(0) == null)
            {
                this.renderedItem.setDead();
                this.renderedItem = null;
            }
        }

        if (this.enchanterItemStacks[0] != null)
        {
            this.itemFloatingSpeed = 0.03D;
        }
        else
        {
            this.itemFloatingSpeed = 0.0D;
        }

        if (this.enchanterItemStacks[1] != null)
        {
            float var1 = this.ambRotationSpeed * (float)this.enchanterItemStacks[1].stackSize * 0.5F;
            this.ambRotation += var1;
            double var2;

            if (this.enchanterItemStacks[1].stackSize < 4)
            {
                var2 = 0.2D * (double)this.enchanterItemStacks[1].stackSize * 0.5D;
            }
            else
            {
                var2 = 0.35D;
            }

            this.ambSpinningSpeed = var2;
        }
        else
        {
            this.ambRotation = 0.0F;
            this.ambSpinningSpeed = 0.0D;
        }

        ItemStack var5;

        if (this.currentEnchantment != null)
        {
            var5 = this.getStackInSlot(0);
            ItemStack var6 = this.getStackInSlot(1);

            if (this.canEnchant() && var6 != null && var6.stackSize >= this.currentEnchantment.enchantAmbrosiumNeeded && var5 != null)
            {
                Aether.proxy.spawnAltarParticles(this.worldObj, this.xCoord, this.yCoord, this.zCoord, this.rand);

                if (!this.worldObj.isRemote)
                {
                    ItemStack var3 = this.currentEnchantment.enchantTo.copy();
                    var3.stackSize = var5.stackSize;
                    EntityItem var4 = new EntityItem(this.worldObj, (double)((float)this.xCoord + 0.5F), (double)((float)this.yCoord + 1.0F), (double)((float)this.zCoord + 0.5F), var3);
                    var4.delayBeforeCanPickup = 10;
                    this.worldObj.spawnEntityInWorld(var4);
                }

                this.decrStackSize(0, var5.stackSize);
                this.decrStackSize(1, this.currentEnchantment.enchantAmbrosiumNeeded);
                this.currentEnchantment = null;
            }
        }
        else
        {
            var5 = this.getStackInSlot(0);

            for (int var7 = 0; var7 < enchantments.size(); ++var7)
            {
                if (var5 != null && enchantments.get(var7) != null && var5.itemID == ((AetherEnchantment)enchantments.get(var7)).enchantFrom.itemID && var5.getItemDamage() == ((AetherEnchantment)enchantments.get(var7)).enchantFrom.getItemDamage())
                {
                    if (this.enchanterItemStacks[2] == null)
                    {
                        this.currentEnchantment = (AetherEnchantment)enchantments.get(var7);
                    }
                    else if (this.enchanterItemStacks[2].itemID == ((AetherEnchantment)enchantments.get(var7)).enchantTo.itemID && ((AetherEnchantment)enchantments.get(var7)).enchantTo.getItem().getItemStackLimit() > this.enchanterItemStacks[2].stackSize)
                    {
                        this.currentEnchantment = (AetherEnchantment)enchantments.get(var7);
                    }
                }
            }
        }
    }

    /**
     * Do not make give this method the name canInteractWith because it clashes with Container
     */
    public boolean isUseableByPlayer(EntityPlayer var1)
    {
        return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : var1.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
    }

    public static void addEnchantment(ItemStack var0, ItemStack var1, int var2)
    {
        enchantments.add(new AetherEnchantment(var0, var1, var2));
    }

    public static void addEnchantment(ItemStack var0, ItemStack var1, int var2, boolean var3)
    {
        enchantments.add(new AetherEnchantment(var0, var1, var2, var3));
    }

    public boolean isEnchantable(ItemStack var1)
    {
        for (int var2 = 0; var2 < enchantments.size(); ++var2)
        {
            if (var1 != null && enchantments.get(var2) != null && var1.itemID == ((AetherEnchantment)enchantments.get(var2)).enchantFrom.itemID && var1.getItemDamage() == ((AetherEnchantment)enchantments.get(var2)).enchantFrom.getItemDamage())
            {
                return true;
            }
        }

        return false;
    }

    public boolean isLimitedToOne(ItemStack var1)
    {
        for (int var2 = 0; var2 < enchantments.size(); ++var2)
        {
            if (var1 != null && enchantments.get(var2) != null && var1.itemID == ((AetherEnchantment)enchantments.get(var2)).enchantFrom.itemID && var1.getItemDamage() == ((AetherEnchantment)enchantments.get(var2)).enchantFrom.getItemDamage() && ((AetherEnchantment)enchantments.get(var2)).limitStackToOne)
            {
                return true;
            }
        }

        return false;
    }

    public int getStartInventorySide(ForgeDirection var1)
    {
        return var1 == ForgeDirection.DOWN ? 1 : (var1 == ForgeDirection.UP ? 0 : 2);
    }

    public int getSizeInventorySide(ForgeDirection var1)
    {
        return 1;
    }

    public void openChest() {}

    public void closeChest() {}

    public void onDataPacket(INetworkManager var1, Packet132TileEntityData var2)
    {
        this.readFromNBT(var2.customParam1);
    }

    /**
     * Overriden in a sign to provide the text.
     */
    public Packet getDescriptionPacket()
    {
        NBTTagCompound var1 = new NBTTagCompound();
        this.writeToNBT(var1);
        return new Packet132TileEntityData(this.xCoord, this.yCoord, this.zCoord, 1, var1);
    }

    private void sendToAllInOurWorld(Packet var1)
    {
        ServerConfigurationManager var2 = FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager();
        Iterator var3 = var2.playerEntityList.iterator();

        while (var3.hasNext())
        {
            Object var4 = var3.next();
            EntityPlayerMP var5 = (EntityPlayerMP)var4;

            if (var5.worldObj == this.worldObj)
            {
                var5.playerNetServerHandler.sendPacketToPlayer(var1);
            }
        }
    }

    public ItemStack getEnchanterStacks(int var1)
    {
        return this.enchanterItemStacks[var1];
    }

    public float getAmbRotation()
    {
        return this.ambRotation;
    }

    public double getItemFloating()
    {
        return this.worldObj.isRemote && FMLClientHandler.instance().getClient().isGamePaused ? 0.0D : this.itemFloatingSpeed;
    }

    public double getAmbSpinning()
    {
        return this.worldObj.isRemote && FMLClientHandler.instance().getClient().isGamePaused ? 0.0D : this.ambSpinningSpeed;
    }

    /**
     * If this returns false, the inventory name will be used as an unlocalized name, and translated into the player's
     * language. Otherwise it will be used directly.
     */
    public boolean isInvNameLocalized()
    {
        return false;
    }

    /**
     * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot.
     */
    public boolean isStackValidForSlot(int var1, ItemStack var2)
    {
        return false;
    }
}
