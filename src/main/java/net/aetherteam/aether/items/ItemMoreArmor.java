package net.aetherteam.aether.items;

import net.minecraft.item.ItemStack;

public class ItemMoreArmor extends ItemAether implements net.aetherteam.aether.interfaces.AEItem
{
    public int itemColour;
    public final int armorLevel;
    public final int armorType;
    public final int damageReduceAmount;
    public final int renderIndex;
    public String texture;
    public boolean colouriseRender;
    private static final int[] damageReduceAmountArray = new int[]{3, 8, 6, 3, 0, 1, 0, 0, 0, 0, 2, 0};
    private static final int[] maxDamageArray = new int[]{11, 16, 15, 13, 10, 10, 8, 10, 10, 10, 10, 10};

    protected ItemMoreArmor(int var1, int var2, int var3, int var4, int var5)
    {
        super(var1);
        this.itemColour = 16777215;
        this.armorLevel = var2;
        this.armorType = var4;
        this.renderIndex = var3;
        this.damageReduceAmount = damageReduceAmountArray[var4];
        this.setMaxDamage(maxDamageArray[var4] * 3 << var2);
        this.maxStackSize = 1;
        this.itemColour = var5;
        this.colouriseRender = true;
        this.texture = "/armor/Accessories.png";
    }

    public ItemMoreArmor(int var1, int var2, int var3, int var4)
    {
        this(var1, var2, var3, var4, 16777215);
    }

    public ItemMoreArmor(int var1, int var2, String var3, int var4)
    {
        this(var1, var2, 0, var4);
        this.texture = var3;
    }

    public ItemMoreArmor(int var1, int var2, String var3, int var4, int var5)
    {
        this(var1, var2, 0, var4, var5);
        this.texture = var3;
    }

    public ItemMoreArmor(int var1, int var2, String var3, int var4, int var5, boolean var6)
    {
        this(var1, var2, var3, var4, var5);
        this.colouriseRender = var6;
    }

    public int getColorFromItemStack(ItemStack var1, int var2)
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

    public boolean isTypeValid(int var1)
    {
        return var1 == this.armorType ? true : ((var1 == 8 || var1 == 9) && (this.armorType == 8 || this.armorType == 9) ? true : (var1 == 7 || var1 == 11) && (this.armorType == 7 || this.armorType == 11));
    }

    public boolean damageType()
    {
        return this.damageType(this.armorType);
    }

    public boolean damageType(int var1)
    {
        return var1 < 4 || var1 == 6 || var1 == 10;
    }
}
