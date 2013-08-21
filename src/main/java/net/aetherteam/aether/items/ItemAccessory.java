package net.aetherteam.aether.items;

import net.aetherteam.aether.PlayerAetherServer;
import net.aetherteam.aether.client.PlayerAetherClient;
import net.aetherteam.aether.interfaces.IAetherAccessory;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.ResourceLocation;

public class ItemAccessory extends ItemAether implements IAetherAccessory
{
    public int itemColour;
    public final int armorLevel;
    public final int armorType;
    public final int damageReduceAmount;
    public final int renderIndex;
    public ResourceLocation texture;
    public boolean colouriseRender;
    public static Icon ringSlot;
    public static Icon pendantSlot;
    public static Icon capeSlot;
    public static Icon miscSlot;
    public static Icon shieldSlot;
    public static Icon gloveSlot;
    private static final int[] damageReduceAmountArray = new int[] {3, 7, 5, 3, 0, 0, 0, 0, 0, 0, 0, 0};
    private static final int[] maxDamageArray = new int[] {11, 16, 15, 13, 10, 10, 8, 10, 10, 10, 10, 10};

    protected ItemAccessory(int i, int j, int k, int l, int col)
    {
        super(i);
        this.itemColour = 16777215;
        this.armorLevel = j;
        this.armorType = l;
        this.renderIndex = k;
        this.damageReduceAmount = damageReduceAmountArray[l];
        this.setMaxDamage(maxDamageArray[l] * 3 << j);
        this.maxStackSize = 1;
        this.itemColour = col;
        this.colouriseRender = true;
        this.texture = new ResourceLocation("aether", "textures/armor/Accessories.png");
    }

    public ItemAccessory(int i, int j, int k, int l)
    {
        this(i, j, k, l, 16777215);
    }

    public ItemAccessory(int i, int j, ResourceLocation path, int l)
    {
        this(i, j, 0, l);
        this.texture = path;
    }

    public ItemAccessory(int i, int j, ResourceLocation path, int l, int m)
    {
        this(i, j, 0, l, m);
        this.texture = path;
    }

    public ItemAccessory(int i, int j, ResourceLocation path, int l, int m, boolean flag)
    {
        this(i, j, path, l, m);
        this.colouriseRender = flag;
    }

    public int getColorFromItemStack(ItemStack par1ItemStack, int damage)
    {
        return this.itemColour;
    }

    public void registerIcons(IconRegister iconReg)
    {
        ringSlot = iconReg.registerIcon("aether:Ring Slot");
        pendantSlot = iconReg.registerIcon("aether:Pendant Slot");
        capeSlot = iconReg.registerIcon("aether:Cape Slot");
        miscSlot = iconReg.registerIcon("aether:Misc Slot");
        shieldSlot = iconReg.registerIcon("aether:Shield Slot");
        gloveSlot = iconReg.registerIcon("aether:Glove Slot");
        super.registerIcons(iconReg);
    }

    public boolean isTypeValid(int type)
    {
        return type == this.armorType ? true : ((type == 8 || type == 9) && (this.armorType == 8 || this.armorType == 9) ? true : (type == 7 || type == 11) && (this.armorType == 7 || this.armorType == 11));
    }

    public int[] getSlotType()
    {
        int[] slots = new int[2];

        if (this.armorType != 8 && this.armorType != 9)
        {
            if (this.armorType != 7 && this.armorType != 11)
            {
                slots[0] = this.armorType - 4;
                slots[1] = this.armorType - 4;
            }
            else
            {
                slots[0] = 3;
                slots[1] = 7;
            }
        }
        else
        {
            slots[0] = 4;
            slots[1] = 5;
        }

        return slots;
    }

    public boolean damageType()
    {
        return this.damageType(this.armorType);
    }

    public boolean damageType(int i)
    {
        return i < 4 || i == 6 || i == 10;
    }

    public void activatePassive(EntityPlayer player) {}

    public void activateServerPassive(EntityPlayer player, PlayerAetherServer playerBase)
    {
        this.activatePassive(player);
    }

    public void activateClientPassive(EntityPlayer player, PlayerAetherClient playerBase)
    {
        this.activatePassive(player);
    }
}
