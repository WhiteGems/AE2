package net.aetherteam.aether.items;

import net.aetherteam.aether.PlayerAetherServer;
import net.aetherteam.aether.client.PlayerAetherClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class ItemIronBubble extends ItemAccessory
{
    public ItemIronBubble(int i, int j, int k, int l)
    {
        super(i, j, k, l, 16777215);
    }

    public ItemIronBubble(int i, int j, ResourceLocation path, int l)
    {
        super(i, j, 0, l);
        this.texture = path;
    }

    public ItemIronBubble(int i, int j, ResourceLocation path, int l, int m)
    {
        super(i, j, 0, l, m);
        this.texture = path;
    }

    public ItemIronBubble(int i, int j, ResourceLocation path, int l, int m, boolean flag)
    {
        super(i, j, path, l, m);
        this.colouriseRender = flag;
    }

    public void activateServerPassive(EntityPlayer player, PlayerAetherServer playerBase)
    {
        player.setAir(0);
    }

    public void activateClientPassive(EntityPlayer player, PlayerAetherClient playerBase)
    {
        player.setAir(0);
    }
}
