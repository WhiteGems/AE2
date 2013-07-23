package net.aetherteam.aether.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class ItemInvisibilityCloak extends ItemAccessory
{
    public ItemInvisibilityCloak(int i, int j, int k, int l)
    {
        super(i, j, k, l, 16777215);
    }

    public ItemInvisibilityCloak(int i, int j, String path, int l)
    {
        super(i, j, 0, l);
        this.texture = path;
    }

    public ItemInvisibilityCloak(int i, int j, String path, int l, int m)
    {
        super(i, j, 0, l, m);
        this.texture = path;
    }

    public ItemInvisibilityCloak(int i, int j, String path, int l, int m, boolean flag)
    {
        super(i, j, path, l, m);
        this.colouriseRender = flag;
    }

    public void activatePassive(EntityPlayer player)
    {
        player.addPotionEffect(new PotionEffect(Potion.invisibility.id, 10, 0));
    }
}

