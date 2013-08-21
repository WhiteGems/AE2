package net.aetherteam.aether.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class ItemValkyrieCape extends ItemAccessory
{
    public ItemValkyrieCape(int i, int j, int k, int l)
    {
        super(i, j, k, l, 16777215);
    }

    public ItemValkyrieCape(int i, int j, ResourceLocation path, int l)
    {
        super(i, j, 0, l);
        this.texture = path;
    }

    public Item setIconName(String name)
    {
        this.field_111218_cA = "aether:" + name;
        return this.setUnlocalizedName("aether:" + name);
    }

    public ItemValkyrieCape(int i, int j, ResourceLocation path, int l, int m)
    {
        super(i, j, 0, l, m);
        this.texture = path;
    }

    public ItemValkyrieCape(int i, int j, ResourceLocation path, int l, int m, boolean flag)
    {
        super(i, j, path, l, m);
        this.colouriseRender = flag;
    }

    public void activatePassive(EntityPlayer player)
    {
        if (!player.onGround && player.motionY < 0.0D && !player.isInWater() && !player.isSneaking())
        {
            player.motionY *= 0.6D;
        }

        player.fallDistance = -1.0F;
    }
}
