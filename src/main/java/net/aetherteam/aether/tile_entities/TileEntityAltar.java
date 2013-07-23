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
import net.aetherteam.aether.CommonProxy;
import net.aetherteam.aether.entities.altar.EntityFakeItem;
import net.aetherteam.aether.items.AetherItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.NetServerHandler;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.ISidedInventory;

public class TileEntityAltar extends TileEntity
    implements IInventory, ISidedInventory
{
    private static List enchantments = new ArrayList();
    private Random rand;
    private EntityFakeItem renderedItem = null;
    private ItemStack[] enchanterItemStacks;
    public ItemStack enchantableItem;
    public ItemStack ambrosiumItems;
    public int enchantProgress;
    public int enchantPowerRemaining;
    public int enchantTimeForItem;
    private float ambRotationSpeed = 0.05F;
    private float ambRotation = 0.0F;
    private double ambSpinningSpeed = 0.0D;
    private double itemFloatingSpeed = 0.0D;
    private AetherEnchantment currentEnchantment;

    public TileEntityAltar()
    {
        this.enchanterItemStacks = new ItemStack[3];
        this.enchantProgress = 0;
        this.enchantPowerRemaining = 0;
        this.enchantTimeForItem = 0;
        this.rand = new Random();
    }

    public int getSizeInventory()
    {
        return this.enchanterItemStacks.length;
    }

    public ItemStack getStackInSlot(int i)
    {
        return this.enchanterItemStacks[i];
    }

    public ItemStack decrStackSize(int i, int j)
    {
        if (this.enchanterItemStacks[i] != null)
        {
            if (this.enchanterItemStacks[i].stackSize <= j)
            {
                ItemStack itemstack = this.enchanterItemStacks[i];
                this.enchanterItemStacks[i] = null;

                if (!this.worldObj.isRemote)
                {
                    sendToAllInOurWorld(getDescriptionPacket());
                }

                return itemstack;
            }

            ItemStack itemstack1 = this.enchanterItemStacks[i].splitStack(j);

            if (this.enchanterItemStacks[i].stackSize == 0)
            {
                this.enchanterItemStacks[i] = null;
            }

            if (!this.worldObj.isRemote)
            {
                sendToAllInOurWorld(getDescriptionPacket());
            }

            return itemstack1;
        }

        return null;
    }

    public ItemStack getStackInSlotOnClosing(int par1)
    {
        if (this.enchanterItemStacks[par1] != null)
        {
            ItemStack var2 = this.enchanterItemStacks[par1];
            this.enchanterItemStacks[par1] = null;
            return var2;
        }

        return null;
    }

    public void setInventorySlotContents(int i, ItemStack itemstack)
    {
        this.enchanterItemStacks[i] = itemstack;

        if (i == 0)
        {
            this.currentEnchantment = null;
        }

        if ((itemstack != null) && (itemstack.stackSize > getInventoryStackLimit()))
        {
            itemstack.stackSize = getInventoryStackLimit();
        }

        if (!this.worldObj.isRemote)
        {
            sendToAllInOurWorld(getDescriptionPacket());
        }
    }

    public String getInvName()
    {
        return "Enchanter";
    }

    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);
        NBTTagList nbttaglist = nbttagcompound.getTagList("Items");
        this.enchanterItemStacks = new ItemStack[getSizeInventory()];

        for (int i = 0; i < nbttaglist.tagCount(); i++)
        {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.tagAt(i);
            byte byte0 = nbttagcompound1.getByte("Slot");

            if ((byte0 >= 0) && (byte0 < this.enchanterItemStacks.length))
            {
                this.enchanterItemStacks[byte0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }

        this.enchantProgress = nbttagcompound.getShort("BurnTime");
        this.enchantTimeForItem = nbttagcompound.getShort("CookTime");
    }

    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setShort("BurnTime", (short)this.enchantProgress);
        nbttagcompound.setShort("CookTime", (short)this.enchantTimeForItem);
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.enchanterItemStacks.length; i++)
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

    public int getInventoryStackLimit()
    {
        return 16;
    }

    public void addEnchantable(ItemStack stack)
    {
        if ((stack == null) || (!isEnchantable(stack)))
        {
            return;
        }

        ItemStack enchantableStack = getStackInSlot(0);
        int stackSizeLimit = getInventoryStackLimit();

        if (isLimitedToOne(stack))
        {
            stackSizeLimit = 1;
        }

        if (enchantableStack == null)
        {
            if (stack.stackSize > stackSizeLimit)
            {
                enchantableStack = new ItemStack(stack.itemID, stackSizeLimit, stack.getItemDamage());
                stack.stackSize -= stackSizeLimit;
                setInventorySlotContents(0, enchantableStack);
            }
            else
            {
                enchantableStack = new ItemStack(stack.itemID, stack.stackSize, stack.getItemDamage());
                stack.stackSize = 0;
                setInventorySlotContents(0, enchantableStack);
            }
        }
        else if ((enchantableStack.itemID == stack.itemID) && (enchantableStack.getItemDamage() == stack.getItemDamage()))
        {
            if (canCombineStackWithRemainder(stack, enchantableStack, stackSizeLimit))
            {
                combineStackWithRemainder(stack, enchantableStack, stackSizeLimit);
            }
            else if (stackIsFull(enchantableStack, stackSizeLimit))
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

        onInventoryChanged();

        if (!this.worldObj.isRemote)
        {
            sendToAllInOurWorld(getDescriptionPacket());
        }
    }

    public boolean canEnchant()
    {
        if (this.worldObj.getBlockId(this.xCoord, this.yCoord + 1, this.zCoord) != 0)
        {
            return false;
        }

        return true;
    }

    public int getRemainingStackSize(ItemStack newStack, ItemStack currentStack, int stackSizeLimit)
    {
        if (canCombineStackWithRemainder(newStack, currentStack, stackSizeLimit))
        {
            int remainder = currentStack.stackSize + newStack.stackSize - stackSizeLimit;
            return remainder;
        }

        return 0;
    }

    public boolean canCombineStackWithRemainder(ItemStack newStack, ItemStack currentStack, int stackSizeLimit)
    {
        if ((newStack.itemID != currentStack.itemID) || (newStack.getItemDamage() != currentStack.getItemDamage()))
        {
            return false;
        }

        if (stackIsFull(currentStack, stackSizeLimit))
        {
            return false;
        }

        if (currentStack.stackSize + newStack.stackSize > stackSizeLimit)
        {
            return true;
        }

        return false;
    }

    public boolean stackIsFull(ItemStack stack, int stackSizeLimit)
    {
        if (stack.stackSize == stackSizeLimit)
        {
            return true;
        }

        return false;
    }

    public void combineStackWithRemainder(ItemStack newStack, ItemStack currentStack, int stackSizeLimit)
    {
        if (!canCombineStackWithRemainder(newStack, currentStack, stackSizeLimit))
        {
            return;
        }

        int remainder = getRemainingStackSize(newStack, currentStack, stackSizeLimit);
        newStack.stackSize = remainder;
        currentStack.stackSize = getInventoryStackLimit();
    }

    public void addAmbrosium(ItemStack stack)
    {
        if ((stack == null) || (stack.itemID != AetherItems.AmbrosiumShard.itemID))
        {
            return;
        }

        ItemStack ambrosiumStack = getStackInSlot(1);

        if ((ambrosiumStack != null) && (ambrosiumStack.stackSize >= getInventoryStackLimit()))
        {
            return;
        }

        if (ambrosiumStack == null)
        {
            ambrosiumStack = new ItemStack(AetherItems.AmbrosiumShard.itemID, 1, 0);
            setInventorySlotContents(1, ambrosiumStack);
            stack.stackSize -= 1;
        }
        else
        {
            ambrosiumStack.stackSize += 1;
            stack.stackSize -= 1;
        }

        onInventoryChanged();

        if (!this.worldObj.isRemote)
        {
            sendToAllInOurWorld(getDescriptionPacket());
        }
    }

    public void dropNextStack()
    {
        if (this.enchanterItemStacks[1] != null)
        {
            if (!this.worldObj.isRemote)
            {
                EntityItem entityitem = new EntityItem(this.worldObj, this.xCoord + 0.5F, this.yCoord + 1.0F, this.zCoord + 0.5F, this.enchanterItemStacks[1]);
                entityitem.delayBeforeCanPickup = 10;
                this.worldObj.spawnEntityInWorld(entityitem);
            }

            decrStackSize(1, this.enchanterItemStacks[1].stackSize);
        }
        else if (this.enchanterItemStacks[0] != null)
        {
            if (!this.worldObj.isRemote)
            {
                EntityItem entityitem = new EntityItem(this.worldObj, this.xCoord + 0.5F, this.yCoord + 1.0F, this.zCoord + 0.5F, this.enchanterItemStacks[0]);
                entityitem.delayBeforeCanPickup = 10;
                this.worldObj.spawnEntityInWorld(entityitem);
            }

            decrStackSize(0, this.enchanterItemStacks[0].stackSize);
        }
    }

    @SideOnly(Side.CLIENT)
    public int getCookProgressScaled(int i)
    {
        if (this.enchantTimeForItem == 0)
        {
            return 0;
        }

        return this.enchantProgress * i / this.enchantTimeForItem;
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

    public void updateEntity()
    {
        if (!this.worldObj.isRemote)
        {
            if (this.renderedItem == null)
            {
                if (getEnchanterStacks(0) != null)
                {
                    this.renderedItem = new EntityFakeItem(this.worldObj, this.xCoord + 0.5D, this.yCoord + 1.15D, this.zCoord + 0.5D, getEnchanterStacks(0));
                    this.worldObj.spawnEntityInWorld(this.renderedItem);
                }
            }
            else if (getEnchanterStacks(0) == null)
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
            float rotationSpeed = this.ambRotationSpeed * (this.enchanterItemStacks[1].stackSize * 0.5F);
            this.ambRotation += rotationSpeed;
            double spinningSpeed;
            double spinningSpeed;

            if (this.enchanterItemStacks[1].stackSize < 4)
            {
                spinningSpeed = 0.2D * (this.enchanterItemStacks[1].stackSize * 0.5D);
            }
            else
            {
                spinningSpeed = 0.35D;
            }

            this.ambSpinningSpeed = spinningSpeed;
        }
        else
        {
            this.ambRotation = 0.0F;
            this.ambSpinningSpeed = 0.0D;
        }

        if (this.currentEnchantment != null)
        {
            ItemStack inputStack = getStackInSlot(0);
            ItemStack fuelStack = getStackInSlot(1);

            if ((canEnchant()) && (fuelStack != null) && (fuelStack.stackSize >= this.currentEnchantment.enchantAmbrosiumNeeded) && (inputStack != null))
            {
                Aether.proxy.spawnAltarParticles(this.worldObj, this.xCoord, this.yCoord, this.zCoord, this.rand);

                if (!this.worldObj.isRemote)
                {
                    ItemStack outputStack = this.currentEnchantment.enchantTo.copy();
                    outputStack.stackSize = inputStack.stackSize;
                    EntityItem entityitem = new EntityItem(this.worldObj, this.xCoord + 0.5F, this.yCoord + 1.0F, this.zCoord + 0.5F, outputStack);
                    entityitem.delayBeforeCanPickup = 10;
                    this.worldObj.spawnEntityInWorld(entityitem);
                }

                decrStackSize(0, inputStack.stackSize);
                decrStackSize(1, this.currentEnchantment.enchantAmbrosiumNeeded);
                this.currentEnchantment = null;
            }
        }
        else
        {
            ItemStack itemstack = getStackInSlot(0);

            for (int i = 0; i < enchantments.size(); i++)
            {
                if ((itemstack != null) && (enchantments.get(i) != null) && (itemstack.itemID == ((AetherEnchantment)enchantments.get(i)).enchantFrom.itemID) && (itemstack.getItemDamage() == ((AetherEnchantment)enchantments.get(i)).enchantFrom.getItemDamage()))
                {
                    if (this.enchanterItemStacks[2] == null)
                    {
                        this.currentEnchantment = ((AetherEnchantment)enchantments.get(i));
                    }
                    else if ((this.enchanterItemStacks[2].itemID == ((AetherEnchantment)enchantments.get(i)).enchantTo.itemID) && (((AetherEnchantment)enchantments.get(i)).enchantTo.getItem().getItemStackLimit() > this.enchanterItemStacks[2].stackSize))
                    {
                        this.currentEnchantment = ((AetherEnchantment)enchantments.get(i));
                    }
                }
            }
        }
    }

    public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
    {
        return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) == this;
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
        for (int i = 0; i < enchantments.size(); i++)
        {
            if ((stack != null) && (enchantments.get(i) != null) && (stack.itemID == ((AetherEnchantment)enchantments.get(i)).enchantFrom.itemID) && (stack.getItemDamage() == ((AetherEnchantment)enchantments.get(i)).enchantFrom.getItemDamage()))
            {
                return true;
            }
        }

        return false;
    }

    public boolean isLimitedToOne(ItemStack stack)
    {
        for (int i = 0; i < enchantments.size(); i++)
        {
            if ((stack != null) && (enchantments.get(i) != null) && (stack.itemID == ((AetherEnchantment)enchantments.get(i)).enchantFrom.itemID) && (stack.getItemDamage() == ((AetherEnchantment)enchantments.get(i)).enchantFrom.getItemDamage()))
            {
                if (((AetherEnchantment)enchantments.get(i)).limitStackToOne)
                {
                    return true;
                }
            }
        }

        return false;
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

    public void openChest()
    {
    }

    public void closeChest()
    {
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

    private void sendToAllInOurWorld(Packet pkt)
    {
        ServerConfigurationManager scm = FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager();

        for (Iterator i$ = scm.playerEntityList.iterator(); i$.hasNext();)
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
        if (this.worldObj.isRemote)
        {
            if (FMLClientHandler.instance().getClient().isGamePaused)
            {
                return 0.0D;
            }
        }

        return this.itemFloatingSpeed;
    }

    public double getAmbSpinning()
    {
        if (this.worldObj.isRemote)
        {
            if (FMLClientHandler.instance().getClient().isGamePaused)
            {
                return 0.0D;
            }
        }

        return this.ambSpinningSpeed;
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

