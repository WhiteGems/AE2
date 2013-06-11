package net.aetherteam.aether.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;

public class ItemValkyrieCape extends ItemAccessory
{
    public ItemValkyrieCape(int var1, int var2, int var3, int var4)
    {
        super(var1, var2, var3, var4, 16777215);
    }

    public ItemValkyrieCape(int var1, int var2, String var3, int var4)
    {
        super(var1, var2, 0, var4);
        this.texture = var3;
    }

    public Item setIconName(String var1)
    {
        return this.setUnlocalizedName("Aether:" + var1);
    }

    public ItemValkyrieCape(int var1, int var2, String var3, int var4, int var5)
    {
        super(var1, var2, 0, var4, var5);
        this.texture = var3;
    }

    public ItemValkyrieCape(int var1, int var2, String var3, int var4, int var5, boolean var6)
    {
        super(var1, var2, var3, var4, var5);
        this.colouriseRender = var6;
    }

    public void activatePassive(EntityPlayer var1)
    {
        if (!var1.onGround && var1.motionY < 0.0D && !var1.isInWater() && !var1.isSneaking())
        {
            var1.motionY *= 0.6D;
        }

        var1.fallDistance = -1.0F;
    }
}
