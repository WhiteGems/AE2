package net.aetherteam.aether.items;

import net.aetherteam.aether.PlayerBaseAetherServer;
import net.aetherteam.aether.client.PlayerBaseAetherClient;
import net.aetherteam.aether.interfaces.IAetherAccessory;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;

public class ItemAccessory extends ItemAether implements IAetherAccessory
{
    public int itemColour;
    public final int armorLevel;
    public final int armorType;
    public final int damageReduceAmount;
    public final int renderIndex;
    public String texture;
    public boolean colouriseRender;
    public static Icon ringSlot;
    public static Icon pendantSlot;
    public static Icon capeSlot;
    public static Icon miscSlot;
    public static Icon shieldSlot;
    public static Icon gloveSlot;
    private static final int[] damageReduceAmountArray = new int[]{3, 7, 5, 3, 0, 0, 0, 0, 0, 0, 0, 0};
    private static final int[] maxDamageArray = new int[]{11, 16, 15, 13, 10, 10, 8, 10, 10, 10, 10, 10};

    protected ItemAccessory(int var1, int var2, int var3, int var4, int var5)
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

    public ItemAccessory(int var1, int var2, int var3, int var4)
    {
        this(var1, var2, var3, var4, 16777215);
    }

    public ItemAccessory(int var1, int var2, String var3, int var4)
    {
        this(var1, var2, 0, var4);
        this.texture = var3;
    }

    public ItemAccessory(int var1, int var2, String var3, int var4, int var5)
    {
        this(var1, var2, 0, var4, var5);
        this.texture = var3;
    }

    public ItemAccessory(int var1, int var2, String var3, int var4, int var5, boolean var6)
    {
        this(var1, var2, var3, var4, var5);
        this.colouriseRender = var6;
    }

    public int getColorFromItemStack(ItemStack var1, int var2)
    {
        return this.itemColour;
    }

    public void registerIcons(IconRegister var1)
    {
        ringSlot = var1.registerIcon("Aether:Ring Slot");
        pendantSlot = var1.registerIcon("Aether:Pendant Slot");
        capeSlot = var1.registerIcon("Aether:Cape Slot");
        miscSlot = var1.registerIcon("Aether:Misc Slot");
        shieldSlot = var1.registerIcon("Aether:Shield Slot");
        gloveSlot = var1.registerIcon("Aether:Glove Slot");
        super.registerIcons(var1);
    }

    public boolean isTypeValid(int var1)
    {
        return var1 == this.armorType ? true : ((var1 == 8 || var1 == 9) && (this.armorType == 8 || this.armorType == 9) ? true : (var1 == 7 || var1 == 11) && (this.armorType == 7 || this.armorType == 11));
    }

    public int[] getSlotType()
    {
        int[] var1 = new int[2];

        if (this.armorType != 8 && this.armorType != 9)
        {
            if (this.armorType != 7 && this.armorType != 11)
            {
                var1[0] = this.armorType - 4;
                var1[1] = this.armorType - 4;
            } else
            {
                var1[0] = 3;
                var1[1] = 7;
            }
        } else
        {
            var1[0] = 4;
            var1[1] = 5;
        }

        return var1;
    }

    public boolean damageType()
    {
        return this.damageType(this.armorType);
    }

    public boolean damageType(int var1)
    {
        return var1 < 4 || var1 == 6 || var1 == 10;
    }

    public void activatePassive(EntityPlayer var1) {}

    public void activateServerPassive(EntityPlayer var1, PlayerBaseAetherServer var2)
    {
        this.activatePassive(var1);
    }

    public void activateClientPassive(EntityPlayer var1, PlayerBaseAetherClient var2)
    {
        this.activatePassive(var1);
    }
}
