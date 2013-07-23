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

    public ItemDartShooter(int var1)
    {
        super(var1);
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
    public void getSubItems(int var1, CreativeTabs var2, List var3)
    {
        for (int var4 = 0; var4 < 4; ++var4)
        {
            var3.add(new ItemStack(var1, 1, var4));
        }
    }

    private int consumeItem(EntityPlayer var1, int var2, int var3)
    {
        InventoryPlayer var4 = var1.inventory;

        for (int var5 = 0; var5 < var4.getSizeInventory(); ++var5)
        {
            ItemStack var6 = var4.getStackInSlot(var5);

            if (var6 != null)
            {
                int var7 = var6.getItemDamage();

                if (var3 != 3 && var6.itemID == var2 && var6.getItemDamage() == var3)
                {
                    if (!var1.capabilities.isCreativeMode)
                    {
                        --var6.stackSize;
                    }

                    if (var6.stackSize == 0)
                    {
                        var6 = null;
                    }

                    var4.setInventorySlotContents(var5, var6);
                    return var7;
                }

                if (var3 == 3 && var6.itemID == var2)
                {
                    if (!var1.capabilities.isCreativeMode)
                    {
                        --var6.stackSize;
                    }

                    if (var6.stackSize == 0)
                    {
                        var6 = null;
                    }

                    var4.setInventorySlotContents(var5, var6);
                    return 3;
                }
            }
        }

        return -1;
    }

    /**
     * Gets an icon index based on an item's damage value
     */
    public Icon getIconFromDamage(int var1)
    {
        return this.icons[var1];
    }

    /**
     * Returns the unlocalized name of this item. This version accepts an ItemStack so different stacks can have
     * different names based on their damage or NBT.
     */
    public String getUnlocalizedName(ItemStack var1)
    {
        int var2 = MathHelper.clamp_int(var1.getItemDamage(), 0, 3);
        return "Aether:" + names[var2];
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister var1)
    {
        this.icons = new Icon[names.length];

        for (int var2 = 0; var2 < names.length; ++var2)
        {
            this.icons[var2] = var1.registerIcon("Aether:" + names[var2]);
        }
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack var1, World var2, EntityPlayer var3)
    {
        int var4;

        if (!var3.capabilities.isCreativeMode)
        {
            var4 = this.consumeItem(var3, AetherItems.Dart.itemID, var1.getItemDamage());
        }
        else
        {
            var4 = var1.getItemDamage();
        }

        if (var4 != -1)
        {
            var2.playSoundAtEntity(var3, "aemisc.shootDart", 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 0.2F));
            Object var5 = null;

            if (var4 == 1)
            {
                var5 = new EntityDartPoison(var2, var3);
            }
            else if (var4 == 2)
            {
                var5 = new EntityDartEnchanted(var2, var3);
            }
            else if (var4 == 0)
            {
                var5 = new EntityDartGolden(var2, var3);
            }
            else if (var4 == 3)
            {
                var5 = new EntityDartPhoenix(var2, var3, new ItemStack(AetherItems.Dart.itemID, 1, var4));
                var2.playSoundAtEntity(var3, "mob.ghast.fireball", 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 0.4F));
            }

            if (!var2.isRemote)
            {
                var2.spawnEntityInWorld((Entity)var5);

                if (!var3.capabilities.isCreativeMode)
                {
                    ((EntityDartGolden)var5).canBePickedUp = 1;
                }

                if (var3.capabilities.isCreativeMode)
                {
                    ((EntityDartGolden)var5).canBePickedUp = 2;
                }
            }
        }

        return var1;
    }
}
