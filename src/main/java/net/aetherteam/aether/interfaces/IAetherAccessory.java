package net.aetherteam.aether.interfaces;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.aetherteam.aether.PlayerBaseAetherServer;
import net.aetherteam.aether.client.PlayerBaseAetherClient;
import net.minecraft.entity.player.EntityPlayer;

public interface IAetherAccessory
{
    int[] damageReduceAmountArray = new int[] {3, 8, 6, 3, 0, 1, 0, 0, 0, 0, 2, 0};
    int[] maxDamageArray = new int[] {11, 16, 15, 13, 10, 10, 8, 10, 10, 10, 10, 10};

    boolean isTypeValid(int var1);

    boolean damageType();

    boolean damageType(int var1);

    void activatePassive(EntityPlayer var1);

    void activateServerPassive(EntityPlayer var1, PlayerBaseAetherServer var2);

    @SideOnly(Side.CLIENT)
    void activateClientPassive(EntityPlayer var1, PlayerBaseAetherClient var2);
}
