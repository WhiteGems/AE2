package net.aetherteam.aether.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
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

    /** The amount this food item heals the player. */
    private int healAmount;
    private boolean damZero;
    private boolean damOne;

    public ItemGummieSwet(int i)
    {
        super(i, 20, false);
        this.setMaxStackSize(64);
        this.damZero = false;
        this.damOne = false;
        this.healAmount = 20;
        this.setHasSubtypes(true);
    }

    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (int var4 = 0; var4 < 2; ++var4)
        {
            par3List.add(new ItemStack(par1, 1, var4));
        }
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
        int var2 = MathHelper.clamp_int(par1ItemStack.getItemDamage(), 0, names.length - 1);
        return super.getUnlocalizedName() + "." + names[var2];
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

    public ItemStack onEaten(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        if (!par2World.isRemote)
        {
            par3EntityPlayer.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 100, 1));
        }

        --par1ItemStack.stackSize;
        par3EntityPlayer.getFoodStats().addStats(this);
        par2World.playSoundAtEntity(par3EntityPlayer, "random.burp", 0.5F, par2World.rand.nextFloat() * 0.1F + 0.9F);
        this.onFoodEaten(par1ItemStack, par2World, par3EntityPlayer);
        return par1ItemStack;
    }
}
