package net.aetherteam.aether.items;

import net.aetherteam.aether.PlayerAetherServer;
import net.aetherteam.aether.client.PlayerAetherClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class ItemAgilityCape extends ItemAccessory
{
    public ItemAgilityCape(int i, int j, int k, int l)
    {
        super(i, j, k, l, 16777215);
    }

    public ItemAgilityCape(int i, int j, ResourceLocation path, int l)
    {
        super(i, j, 0, l);
        this.texture = path;
    }

    public ItemAgilityCape(int i, int j, ResourceLocation path, int l, int m)
    {
        super(i, j, 0, l, m);
        this.texture = path;
    }

    public ItemAgilityCape(int i, int j, ResourceLocation path, int l, int m, boolean flag)
    {
        super(i, j, path, l, m);
        this.colouriseRender = flag;
    }

    public void activateServerPassive(EntityPlayer player, PlayerAetherServer playerBase)
    {
        if (playerBase.wearingAccessory(this.itemID))
        {
            player.stepHeight = 1.0F;
        }
        else
        {
            player.stepHeight = playerBase.prevStepHeight;
        }
    }

    public void activateClientPassive(EntityPlayer player, PlayerAetherClient playerBase)
    {
        if (playerBase.wearingAccessory(this.itemID))
        {
            player.stepHeight = 1.0F;
        }
        else
        {
            player.stepHeight = playerBase.prevStepHeight;
        }
    }
}
