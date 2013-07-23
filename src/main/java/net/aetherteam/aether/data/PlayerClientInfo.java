package net.aetherteam.aether.data;

import net.minecraft.util.MathHelper;

public class PlayerClientInfo
{
    private int halfHearts;
    private int maxHealth;
    private int hunger;
    private int armourValue;
    private int aetherCoins;

    public PlayerClientInfo(int var1, int var2, int var3, int var4, int var5)
    {
        this.maxHealth = var2;
        this.halfHearts = MathHelper.clamp_int(var1, 0, this.maxHealth);
        this.hunger = var3;
        this.armourValue = var4;
        this.aetherCoins = var5;
    }

    public int getHalfHearts()
    {
        return this.halfHearts;
    }

    public int getMaxHealth()
    {
        return this.maxHealth;
    }

    public int getHunger()
    {
        return this.hunger;
    }

    public int getArmourValue()
    {
        return this.armourValue;
    }

    public int getAetherCoins()
    {
        return this.aetherCoins;
    }
}
