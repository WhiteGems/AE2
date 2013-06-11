package net.aetherteam.aether.items;

import net.aetherteam.aether.PlayerBaseAetherServer;
import net.aetherteam.aether.client.PlayerBaseAetherClient;
import net.minecraft.entity.player.EntityPlayer;

public class ItemIronBubble extends ItemAccessory
{
    public ItemIronBubble(int var1, int var2, int var3, int var4)
    {
        super(var1, var2, var3, var4, 16777215);
    }

    public ItemIronBubble(int var1, int var2, String var3, int var4)
    {
        super(var1, var2, 0, var4);
        this.texture = var3;
    }

    public ItemIronBubble(int var1, int var2, String var3, int var4, int var5)
    {
        super(var1, var2, 0, var4, var5);
        this.texture = var3;
    }

    public ItemIronBubble(int var1, int var2, String var3, int var4, int var5, boolean var6)
    {
        super(var1, var2, var3, var4, var5);
        this.colouriseRender = var6;
    }

    public void activateServerPassive(EntityPlayer var1, PlayerBaseAetherServer var2)
    {
        var1.setAir(0);
    }

    public void activateClientPassive(EntityPlayer var1, PlayerBaseAetherClient var2)
    {
        var1.setAir(0);
    }
}
