package net.aetherteam.aether.data;

import net.minecraft.util.MathHelper;

public class PlayerClientInfo
{
    private int halfHearts;
    private int maxHealth;
    private int hunger;
    private int armourValue;
    private int aetherCoins;

    public PlayerClientInfo(int halfHearts, int maxHealth, int hunger, int armourValue, int aetherCoins)
    {
        this.maxHealth = maxHealth;
        this.halfHearts = MathHelper.clamp_int(halfHearts, 0, this.maxHealth);
        this.hunger = hunger;
        this.armourValue = armourValue;
        this.aetherCoins = aetherCoins;
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
