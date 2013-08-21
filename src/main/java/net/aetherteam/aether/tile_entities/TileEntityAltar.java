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

public class TileEntityAltar extends TileEntity implements IInventory
{
    private static List<AetherEnchantment> enchantments = new ArrayList();
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
    public ItemStack getStackInSlot(int i)
    {
        return this.enchanterItemStacks[i];
    }

    /**
     * Removes from an inventory slot (first arg) up to a specified number (second arg) of items and returns them in a
     * new stack.
     */
    public ItemStack decrStackSize(int i, int j)
    {
        if (this.enchanterItemStacks[i] != null)
        {
            ItemStack itemstack1;

            if (this.enchanterItemStacks[i].stackSize <= j)
            {
                itemstack1 = this.enchanterItemStacks[i];
                this.enchanterItemStacks[i] = null;

                if (!this.worldObj.isRemote)
                {
                    this.sendToAllInOurWorld(this.getDescriptionPacket());
                }

                return itemstack1;
            }
            else
            {
                itemstack1 = this.enchanterItemStacks[i].splitStack(j);

                if (this.enchanterItemStacks[i].stackSize == 0)
                {
                    this.enchanterItemStacks[i] = null;
                }

                if (!this.worldObj.isRemote)
                {
                    this.sendToAllInOurWorld(this.getDescriptionPacket());
                }

                return itemstack1;
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
    public ItemStack getStackInSlotOnClosing(int par1)
    {
        if (this.enchanterItemStacks[par1] != null)
        {
            ItemStack var2 = this.enchanterItemStacks[par1];
            this.enchanterItemStacks[par1] = null;
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
    public void setInventorySlotContents(int i, ItemStack itemstack)
    {
        this.enchanterItemStacks[i] = itemstack;

        if (i == 0)
        {
            this.currentEnchantment = null;
        }

        if (itemstack != null && itemstack.stackSize > this.getInventoryStackLimit())
        {
            itemstack.stackSize = this.getInventoryStackLimit();
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
    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);
        NBTTagList nbttaglist = nbttagcompound.getTagList("Items");
        this.enchanterItemStacks = new ItemStack[this.getSizeInventory()];

        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.tagAt(i);
            byte byte0 = nbttagcompound1.getByte("Slot");

            if (byte0 >= 0 && byte0 < this.enchanterItemStacks.length)
            {
                this.enchanterItemStacks[byte0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }

        this.enchantProgress = nbttagcompound.getShort("BurnTime");
        this.enchantTimeForItem = nbttagcompound.getShort("CookTime");
    }

    /**
     * Writes a tile entity to NBT.
     */
    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setShort("BurnTime", (short)this.enchantProgress);
        nbttagcompound.setShort("CookTime", (short)this.enchantTimeForItem);
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.enchanterItemStacks.length; ++i)
        {
            if (this.enchanterItemStacks[i] != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte)i);
                this.enchanterItemStacks[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }

        nbttagcompound.setTag("Items", nbttaglist);
    }

    /**
     * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended. *Isn't
     * this more of a set than a get?*
     */
    public int getInventoryStackLimit()
    {
        return 16;
    }

    public void addEnchantable(ItemStack stack)
    {
        if (stack != null && this.isEnchantable(stack))
        {
            ItemStack enchantableStack = this.getStackInSlot(0);
            int stackSizeLimit = this.getInventoryStackLimit();

            if (this.isLimitedToOne(stack))
            {
                stackSizeLimit = 1;
            }

            if (enchantableStack == null)
            {
                if (stack.stackSize > stackSizeLimit)
                {
                    enchantableStack = new ItemStack(stack.itemID, stackSizeLimit, stack.getItemDamage());
                    stack.stackSize -= stackSizeLimit;
                    this.setInventorySlotContents(0, enchantableStack);
                }
                else
                {
                    enchantableStack = new ItemStack(stack.itemID, stack.stackSize, stack.getItemDamage());
                    stack.stackSize = 0;
                    this.setInventorySlotContents(0, enchantableStack);
                }
            }
            else if (enchantableStack.itemID == stack.itemID && enchantableStack.getItemDamage() == stack.getItemDamage())
            {
                if (this.canCombineStackWithRemainder(stack, enchantableStack, stackSizeLimit))
                {
                    this.combineStackWithRemainder(stack, enchantableStack, stackSizeLimit);
                }
                else if (this.stackIsFull(enchantableStack, stackSizeLimit))
                {
                    if (this.worldObj.isRemote)
                    {
                        FMLClientHandler.instance().getClient().ingameGUI.getChatGUI().printChatMessage("Altar is at full capacity.");
                    }
                }
                else
                {
                    enchantableStack.stackSize += stack.stackSize;
                    stack.stackSize = 0;
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

    public int getRemainingStackSize(ItemStack newStack, ItemStack currentStack, int stackSizeLimit)
    {
        if (this.canCombineStackWithRemainder(newStack, currentStack, stackSizeLimit))
        {
            int remainder = currentStack.stackSize + newStack.stackSize - stackSizeLimit;
            return remainder;
        }
        else
        {
            return 0;
        }
    }

    public boolean canCombineStackWithRemainder(ItemStack newStack, ItemStack currentStack, int stackSizeLimit)
    {
        return newStack.itemID == currentStack.itemID && newStack.getItemDamage() == currentStack.getItemDamage() ? (this.stackIsFull(currentStack, stackSizeLimit) ? false : currentStack.stackSize + newStack.stackSize > stackSizeLimit) : false;
    }

    public boolean stackIsFull(ItemStack stack, int stackSizeLimit)
    {
        return stack.stackSize == stackSizeLimit;
    }

    public void combineStackWithRemainder(ItemStack newStack, ItemStack currentStack, int stackSizeLimit)
    {
        if (this.canCombineStackWithRemainder(newStack, currentStack, stackSizeLimit))
        {
            int remainder = this.getRemainingStackSize(newStack, currentStack, stackSizeLimit);
            newStack.stackSize = remainder;
            currentStack.stackSize = this.getInventoryStackLimit();
        }
    }

    public void addAmbrosium(ItemStack stack)
    {
        if (stack != null && stack.itemID == AetherItems.AmbrosiumShard.itemID)
        {
            ItemStack ambrosiumStack = this.getStackInSlot(1);

            if (ambrosiumStack == null || ambrosiumStack.stackSize < this.getInventoryStackLimit())
            {
                if (ambrosiumStack == null)
                {
                    ambrosiumStack = new ItemStack(AetherItems.AmbrosiumShard.itemID, 1, 0);
                    this.setInventorySlotContents(1, ambrosiumStack);
                    --stack.stackSize;
                }
                else
                {
                    ++ambrosiumStack.stackSize;
                    --stack.stackSize;
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
        EntityItem entityitem;

        if (this.enchanterItemStacks[1] != null)
        {
            if (!this.worldObj.isRemote)
            {
                entityitem = new EntityItem(this.worldObj, (double)((float)this.xCoord + 0.5F), (double)((float)this.yCoord + 1.0F), (double)((float)this.zCoord + 0.5F), this.enchanterItemStacks[1]);
                entityitem.delayBeforeCanPickup = 10;
                this.worldObj.spawnEntityInWorld(entityitem);
            }

            this.decrStackSize(1, this.enchanterItemStacks[1].stackSize);
        }
        else if (this.enchanterItemStacks[0] != null)
        {
            if (!this.worldObj.isRemote)
            {
                entityitem = new EntityItem(this.worldObj, (double)((float)this.xCoord + 0.5F), (double)((float)this.yCoord + 1.0F), (double)((float)this.zCoord + 0.5F), this.enchanterItemStacks[0]);
                entityitem.delayBeforeCanPickup = 10;
                this.worldObj.spawnEntityInWorld(entityitem);
            }

            this.decrStackSize(0, this.enchanterItemStacks[0].stackSize);
        }
    }

    @SideOnly(Side.CLIENT)
    public int getCookProgressScaled(int i)
    {
        return this.enchantTimeForItem == 0 ? 0 : this.enchantProgress * i / this.enchantTimeForItem;
    }

    @SideOnly(Side.CLIENT)
    public int getBurnTimeRemainingScaled(int i)
    {
        return this.enchantPowerRemaining * i / 500;
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
            float itemstack = this.ambRotationSpeed * (float)this.enchanterItemStacks[1].stackSize * 0.5F;
            this.ambRotation += itemstack;
            double i;

            if (this.enchanterItemStacks[1].stackSize < 4)
            {
                i = 0.2D * (double)this.enchanterItemStacks[1].stackSize * 0.5D;
            }
            else
            {
                i = 0.35D;
            }

            this.ambSpinningSpeed = i;
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
                    ItemStack outputStack = this.currentEnchantment.enchantTo.copy();
                    outputStack.stackSize = var5.stackSize;
                    EntityItem entityitem = new EntityItem(this.worldObj, (double)((float)this.xCoord + 0.5F), (double)((float)this.yCoord + 1.0F), (double)((float)this.zCoord + 0.5F), outputStack);
                    entityitem.delayBeforeCanPickup = 10;
                    this.worldObj.spawnEntityInWorld(entityitem);
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
    public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
    {
        return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : par1EntityPlayer.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
    }

    public static void addEnchantment(ItemStack from, ItemStack to, int i)
    {
        enchantments.add(new AetherEnchantment(from, to, i));
    }

    public static void addEnchantment(ItemStack from, ItemStack to, int i, boolean limit)
    {
        enchantments.add(new AetherEnchantment(from, to, i, limit));
    }

    public boolean isEnchantable(ItemStack stack)
    {
        for (int i = 0; i < enchantments.size(); ++i)
        {
            if (stack != null && enchantments.get(i) != null && stack.itemID == ((AetherEnchantment)enchantments.get(i)).enchantFrom.itemID && stack.getItemDamage() == ((AetherEnchantment)enchantments.get(i)).enchantFrom.getItemDamage())
            {
                return true;
            }
        }

        return false;
    }

    public boolean isLimitedToOne(ItemStack stack)
    {
        for (int i = 0; i < enchantments.size(); ++i)
        {
            if (stack != null && enchantments.get(i) != null && stack.itemID == ((AetherEnchantment)enchantments.get(i)).enchantFrom.itemID && stack.getItemDamage() == ((AetherEnchantment)enchantments.get(i)).enchantFrom.getItemDamage() && ((AetherEnchantment)enchantments.get(i)).limitStackToOne)
            {
                return true;
            }
        }

        return false;
    }

    public void openChest() {}

    public void closeChest() {}

    public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt)
    {
        this.readFromNBT(pkt.customParam1);
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

    private void sendToAllInOurWorld(Packet pkt)
    {
        ServerConfigurationManager scm = FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager();
        Iterator i$ = scm.playerEntityList.iterator();

        while (i$.hasNext())
        {
            Object obj = i$.next();
            EntityPlayerMP player = (EntityPlayerMP)obj;

            if (player.worldObj == this.worldObj)
            {
                player.playerNetServerHandler.sendPacketToPlayer(pkt);
            }
        }
    }

    public ItemStack getEnchanterStacks(int index)
    {
        return this.enchanterItemStacks[index];
    }

    public float getAmbRotation()
    {
        return this.ambRotation;
    }

    public double getItemFloating()
    {
        return this.worldObj.isRemote && Aether.isGamePaused() ? 0.0D : this.itemFloatingSpeed;
    }

    public double getAmbSpinning()
    {
        return this.worldObj.isRemote && Aether.isGamePaused() ? 0.0D : this.ambSpinningSpeed;
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
    public boolean isItemValidForSlot(int i, ItemStack itemstack)
    {
        return false;
    }
}
