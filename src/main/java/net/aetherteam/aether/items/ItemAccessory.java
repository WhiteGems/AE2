package net.aetherteam.aether.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.aetherteam.aether.PlayerBaseAetherServer;
import net.aetherteam.aether.client.PlayerBaseAetherClient;
import net.aetherteam.aether.interfaces.IAetherAccessory;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;

public class ItemAccessory extends ItemAether
    implements IAetherAccessory
{
    public int itemColour = 16777215;
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
    private static final int[] damageReduceAmountArray = { 3, 7, 5, 3, 0, 0, 0, 0, 0, 0, 0, 0 };

    private static final int[] maxDamageArray = { 11, 16, 15, 13, 10, 10, 8, 10, 10, 10, 10, 10 };

    protected ItemAccessory(int i, int j, int k, int l, int col)
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

    public ItemAccessory(int i, int j, int k, int l)
    {
        this(i, j, k, l, 16777215);
    }

    public ItemAccessory(int i, int j, String path, int l)
    {
        this(i, j, 0, l);
        this.texture = path;
    }

    public ItemAccessory(int i, int j, String path, int l, int m)
    {
        this(i, j, 0, l, m);
        this.texture = path;
    }

    public ItemAccessory(int i, int j, String path, int l, int m, boolean flag)
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
        ringSlot = iconReg.registerIcon("Aether:Ring Slot");
        pendantSlot = iconReg.registerIcon("Aether:Pendant Slot");
        capeSlot = iconReg.registerIcon("Aether:Cape Slot");
        miscSlot = iconReg.registerIcon("Aether:Misc Slot");
        shieldSlot = iconReg.registerIcon("Aether:Shield Slot");
        gloveSlot = iconReg.registerIcon("Aether:Glove Slot");
        super.registerIcons(iconReg);
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

    public int[] getSlotType()
    {
        int[] slots = new int[2];

        if ((this.armorType == 8) || (this.armorType == 9))
        {
            slots[0] = 4;
            slots[1] = 5;
        }
        else if ((this.armorType == 7) || (this.armorType == 11))
        {
            slots[0] = 3;
            slots[1] = 7;
        }
        else
        {
            slots[0] = (this.armorType - 4);
            slots[1] = (this.armorType - 4);
        }

        return slots;
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

    public void activatePassive(EntityPlayer player)
    {
    }

    public void activateServerPassive(EntityPlayer player, PlayerBaseAetherServer playerBase)
    {
        activatePassive(player);
    }

    @SideOnly(Side.CLIENT)
    public void activateClientPassive(EntityPlayer player, PlayerBaseAetherClient playerBase)
    {
        activatePassive(player);
    }
}

