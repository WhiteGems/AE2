package net.aetherteam.aether.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class ItemDexterityCape extends ItemAccessory
{
    public ItemDexterityCape(int var1, int var2, int var3, int var4)
    {
        super(var1, var2, var3, var4, 16777215);
    }

    public ItemDexterityCape(int var1, int var2, String var3, int var4)
    {
        super(var1, var2, 0, var4);
        this.texture = var3;
    }

    public Item setIconName(String var1)
    {
        return this.setUnlocalizedName("Aether:" + var1);
    }

    public ItemDexterityCape(int var1, int var2, String var3, int var4, int var5)
    {
        super(var1, var2, 0, var4, var5);
        this.texture = var3;
    }

    public ItemDexterityCape(int var1, int var2, String var3, int var4, int var5, boolean var6)
    {
        super(var1, var2, var3, var4, var5);
        this.colouriseRender = var6;
    }

    public void activatePassive(EntityPlayer var1)
    {
        var1.addPotionEffect(new PotionEffect(Potion.moveSpeed.id, 10, 0));
    }
}
