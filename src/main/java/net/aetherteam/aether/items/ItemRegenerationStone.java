package net.aetherteam.aether.items;

import net.aetherteam.aether.PlayerAetherServer;
import net.aetherteam.aether.client.PlayerAetherClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class ItemRegenerationStone extends ItemAccessory
{
    public ItemRegenerationStone(int i, int j, int k, int l)
    {
        super(i, j, k, l, 16777215);
    }

    public ItemRegenerationStone(int i, int j, ResourceLocation path, int l)
    {
        super(i, j, 0, l);
        this.texture = path;
    }

    public ItemRegenerationStone(int i, int j, ResourceLocation path, int l, int m)
    {
        super(i, j, 0, l, m);
        this.texture = path;
    }

    public ItemRegenerationStone(int i, int j, ResourceLocation path, int l, int m, boolean flag)
    {
        super(i, j, path, l, m);
        this.colouriseRender = flag;
    }

    public void activateServerPassive(EntityPlayer player, PlayerAetherServer playerBase)
    {
        if (player.ticksExisted % 200 == 0 && player.func_110143_aJ() < player.func_110138_aP())
        {
            player.heal(2.0F);
        }
    }

    public void activateClientPassive(EntityPlayer player, PlayerAetherClient playerBase)
    {
        if (player.ticksExisted % 200 == 0 && player.func_110143_aJ() < player.func_110138_aP())
        {
            player.heal(2.0F);
        }
    }
}
