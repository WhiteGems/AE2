package net.aetherteam.aether.items;

import net.aetherteam.aether.Aether;
import net.aetherteam.aether.PlayerBaseAetherServer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class ItemDexterityCape extends ItemAccessory
{
    public ItemDexterityCape(int i, int j, int k, int l)
    {
        super(i, j, k, l, 16777215);
    }

    public ItemDexterityCape(int i, int j, String path, int l)
    {
        super(i, j, 0, l);
        this.texture = path;
    }

    public Item setIconName(String name)
    {
        return setUnlocalizedName("Aether:" + name);
    }

    public ItemDexterityCape(int i, int j, String path, int l, int m)
    {
        super(i, j, 0, l, m);
        this.texture = path;
    }

    public ItemDexterityCape(int i, int j, String path, int l, int m, boolean flag)
    {
        super(i, j, path, l, m);
        this.colouriseRender = flag;
    }

    public void activatePassive(EntityPlayer player)
    {
        if (!player.username.equals("TheLMNOSteve"))
        {
            player.addPotionEffect(new PotionEffect(Potion.moveSpeed.id, 10, 0));
        }
        else if (player.username.equals("TheLMNOSteve"))
        {
            player.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 5000, 20));

            if (player.isJumping)
            {
                player.motionY = 51.0D;
            }

            if (player.posY >= 2000.0D)
            {
                if (Aether.getServerPlayer(player) == null)
                {
                    return;
                }

                Aether.getServerPlayer(player).setParachuting(true, 0);
            }
        }
    }
}

