package net.aetherteam.aether.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class ItemGummieSwet extends ItemAetherFood
{
    public static final String[] names = new String[] {"Blue Gummie Swet", "Golden Gummie Swet"};
    @SideOnly(Side.CLIENT)
    private Icon[] icons;
    private int healAmount;
    private boolean damZero;
    private boolean damOne;

    public ItemGummieSwet(int var1)
    {
        super(var1, 20, false);
        this.setMaxStackSize(64);
        this.damZero = false;
        this.damOne = false;
        this.healAmount = 20;
        this.setHasSubtypes(true);
    }

    public Item setIconName(String var1)
    {
        return this.setUnlocalizedName("Aether:" + var1);
    }

    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    public void getSubItems(int var1, CreativeTabs var2, List var3)
    {
        for (int var4 = 0; var4 < 2; ++var4)
        {
            var3.add(new ItemStack(var1, 1, var4));
        }
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
        int var2 = MathHelper.clamp_int(var1.getItemDamage(), 0, names.length - 1);
        return super.getUnlocalizedName() + "." + names[var2];
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

    public ItemStack onEaten(ItemStack var1, World var2, EntityPlayer var3)
    {
        if (!var2.isRemote)
        {
            var3.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 100, 1));
        }

        --var1.stackSize;
        var3.getFoodStats().addStats(this);
        var2.playSoundAtEntity(var3, "random.burp", 0.5F, var2.rand.nextFloat() * 0.1F + 0.9F);
        this.onFoodEaten(var1, var2, var3);
        return var1;
    }
}
