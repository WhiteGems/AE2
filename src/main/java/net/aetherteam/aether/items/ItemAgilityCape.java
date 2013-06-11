package net.aetherteam.aether.items;

import net.aetherteam.aether.PlayerBaseAetherServer;
import net.aetherteam.aether.client.PlayerBaseAetherClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;

public class ItemAgilityCape extends ItemAccessory
{
    public ItemAgilityCape(int var1, int var2, int var3, int var4)
    {
        super(var1, var2, var3, var4, 16777215);
    }

    public Item setIconName(String var1)
    {
        return this.setUnlocalizedName("Aether:" + var1);
    }

    public ItemAgilityCape(int var1, int var2, String var3, int var4)
    {
        super(var1, var2, 0, var4);
        this.texture = var3;
    }

    public ItemAgilityCape(int var1, int var2, String var3, int var4, int var5)
    {
        super(var1, var2, 0, var4, var5);
        this.texture = var3;
    }

    public ItemAgilityCape(int var1, int var2, String var3, int var4, int var5, boolean var6)
    {
        super(var1, var2, var3, var4, var5);
        this.colouriseRender = var6;
    }

    public void activateServerPassive(EntityPlayer var1, PlayerBaseAetherServer var2)
    {
        if (var2.wearingAccessory(this.itemID))
        {
            var1.stepHeight = 1.0F;
        } else
        {
            var1.stepHeight = var2.prevStepHeight;
        }
    }

    public void activateClientPassive(EntityPlayer var1, PlayerBaseAetherClient var2)
    {
        if (var2.wearingAccessory(this.itemID))
        {
            var1.stepHeight = 1.0F;
        } else
        {
            var1.stepHeight = var2.prevStepHeight;
        }
    }
}
