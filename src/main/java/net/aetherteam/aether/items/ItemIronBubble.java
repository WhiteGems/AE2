package net.aetherteam.aether.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.aetherteam.aether.PlayerBaseAetherServer;
import net.aetherteam.aether.client.PlayerBaseAetherClient;
import net.minecraft.entity.player.EntityPlayer;

public class ItemIronBubble extends ItemAccessory
{
    public ItemIronBubble(int i, int j, int k, int l)
    {
        super(i, j, k, l, 16777215);
    }

    public ItemIronBubble(int i, int j, String path, int l)
    {
        super(i, j, 0, l);
        this.texture = path;
    }

    public ItemIronBubble(int i, int j, String path, int l, int m)
    {
        super(i, j, 0, l, m);
        this.texture = path;
    }

    public ItemIronBubble(int i, int j, String path, int l, int m, boolean flag)
    {
        super(i, j, path, l, m);
        this.colouriseRender = flag;
    }

    public void activateServerPassive(EntityPlayer player, PlayerBaseAetherServer playerBase)
    {
        player.setAir(0);
    }

    @SideOnly(Side.CLIENT)
    public void activateClientPassive(EntityPlayer player, PlayerBaseAetherClient playerBase)
    {
        player.setAir(0);
    }
}

