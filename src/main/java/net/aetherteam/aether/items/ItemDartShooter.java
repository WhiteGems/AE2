package net.aetherteam.aether.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.aetherteam.aether.entities.EntityDartEnchanted;
import net.aetherteam.aether.entities.EntityDartGolden;
import net.aetherteam.aether.entities.EntityDartPhoenix;
import net.aetherteam.aether.entities.EntityDartPoison;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class ItemDartShooter extends ItemAether
{
    public static final String[] names = new String[] {"Golden Dart Shooter", "Poison Dart Shooter", "Enchanted Dart Shooter", "Phoenix Dart Shooter"};
    @SideOnly(Side.CLIENT)
    private Icon[] icons;

    public ItemDartShooter(int i)
    {
        super(i);
        this.setHasSubtypes(true);
        this.maxStackSize = 1;
    }

    /**
     * Returns True is the item is renderer in full 3D when hold.
     */
    public boolean isFull3D()
    {
        return false;
    }

    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (int var4 = 0; var4 < 4; ++var4)
        {
            par3List.add(new ItemStack(par1, 1, var4));
        }
    }

    private int consumeItem(EntityPlayer player, int itemID, int maxDamage)
    {
        InventoryPlayer inv = player.inventory;

        for (int i = 0; i < inv.getSizeInventory(); ++i)
        {
            ItemStack stack = inv.getStackInSlot(i);

            if (stack != null)
            {
                int damage = stack.getItemDamage();

                if (maxDamage != 3 && stack.itemID == itemID && stack.getItemDamage() == maxDamage)
                {
                    if (!player.capabilities.isCreativeMode)
                    {
                        --stack.stackSize;
                    }

                    if (stack.stackSize == 0)
                    {
                        stack = null;
                    }

                    inv.setInventorySlotContents(i, stack);
                    return damage;
                }

                if (maxDamage == 3 && stack.itemID == itemID)
                {
                    if (!player.capabilities.isCreativeMode)
                    {
                        --stack.stackSize;
                    }

                    if (stack.stackSize == 0)
                    {
                        stack = null;
                    }

                    inv.setInventorySlotContents(i, stack);
                    return 3;
                }
            }
        }

        return -1;
    }

    /**
     * Gets an icon index based on an item's damage value
     */
    public Icon getIconFromDamage(int damage)
    {
        return this.icons[damage];
    }

    /**
     * Returns the unlocalized name of this item. This version accepts an ItemStack so different stacks can have
     * different names based on their damage or NBT.
     */
    public String getUnlocalizedName(ItemStack par1ItemStack)
    {
        int var2 = MathHelper.clamp_int(par1ItemStack.getItemDamage(), 0, 3);
        return "aether:" + names[var2];
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.icons = new Icon[names.length];

        for (int i = 0; i < names.length; ++i)
        {
            this.icons[i] = par1IconRegister.registerIcon("aether:" + names[i]);
        }
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        int consume;

        if (!entityplayer.capabilities.isCreativeMode)
        {
            consume = this.consumeItem(entityplayer, AetherItems.Dart.itemID, itemstack.getItemDamage());
        }
        else
        {
            consume = itemstack.getItemDamage();
        }

        if (consume != -1)
        {
            world.playSoundAtEntity(entityplayer, "aether:aemisc.shootDart", 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 0.2F));
            Object dart = null;

            if (consume == 1)
            {
                dart = new EntityDartPoison(world, entityplayer);
            }
            else if (consume == 2)
            {
                dart = new EntityDartEnchanted(world, entityplayer);
            }
            else if (consume == 0)
            {
                dart = new EntityDartGolden(world, entityplayer);
            }
            else if (consume == 3)
            {
                dart = new EntityDartPhoenix(world, entityplayer, new ItemStack(AetherItems.Dart.itemID, 1, consume));
                world.playSoundAtEntity(entityplayer, "mob.ghast.fireball", 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 0.4F));
            }

            if (!world.isRemote)
            {
                world.spawnEntityInWorld((Entity)dart);

                if (!entityplayer.capabilities.isCreativeMode)
                {
                    ((EntityDartGolden)dart).canBePickedUp = 1;
                }

                if (entityplayer.capabilities.isCreativeMode)
                {
                    ((EntityDartGolden)dart).canBePickedUp = 2;
                }
            }
        }

        return itemstack;
    }
}
