package net.aetherteam.aether.items;

import net.minecraft.item.ItemStack;

public class ItemMoreArmor extends ItemAether
{
    public int itemColour = 16777215;
    public final int armorLevel;
    public final int armorType;
    public final int damageReduceAmount;
    public final int renderIndex;
    public String texture;
    public boolean colouriseRender;
    private static final int[] damageReduceAmountArray = { 3, 8, 6, 3, 0, 1, 0, 0, 0, 0, 2, 0 };

    private static final int[] maxDamageArray = { 11, 16, 15, 13, 10, 10, 8, 10, 10, 10, 10, 10 };

    protected ItemMoreArmor(int i, int j, int k, int l, int col)
    {
        super(i);
        this.armorLevel = j;
        this.armorType = l;
        this.renderIndex = k;
        this.damageReduceAmount = damageReduceAmountArray[l];
        setMaxDamage(maxDamageArray[l] * 3 << j);
        this.maxStackSize = 1;
        this.itemColour = col;
        this.colouriseRender = true;
        this.texture = "/armor/Accessories.png";
    }

    public ItemMoreArmor(int i, int j, int k, int l)
    {
        this(i, j, k, l, 16777215);
    }

    public ItemMoreArmor(int i, int j, String path, int l)
    {
        this(i, j, 0, l);
        this.texture = path;
    }

    public ItemMoreArmor(int i, int j, String path, int l, int m)
    {
        this(i, j, 0, l, m);
        this.texture = path;
    }

    public ItemMoreArmor(int i, int j, String path, int l, int m, boolean flag)
    {
        this(i, j, path, l, m);
        this.colouriseRender = flag;
    }

    public int getColorFromItemStack(ItemStack par1ItemStack, int damage)
    {
        return this.itemColour;
    }

    public Class annotationType()
    {
        return null;
    }

    public String name()
    {
        return null;
    }

    public String[] names()
    {
        return null;
    }

    public boolean isTypeValid(int type)
    {
        if (type == this.armorType)
        {
            return true;
        }

        if (((type == 8) || (type == 9)) && ((this.armorType == 8) || (this.armorType == 9)))
        {
            return true;
        }

        if (((type == 7) || (type == 11)) && ((this.armorType == 7) || (this.armorType == 11)))
        {
            return true;
        }

        return false;
    }

    public boolean damageType()
    {
        return damageType(this.armorType);
    }

    public boolean damageType(int i)
    {
        if ((i < 4) || (i == 6) || (i == 10))
        {
            return true;
        }

        return false;
    }
}

