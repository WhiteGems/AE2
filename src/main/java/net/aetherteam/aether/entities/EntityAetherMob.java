package net.aetherteam.aether.entities;

import java.util.Random;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.world.World;

public class EntityAetherMob extends EntityMob
{
    Random random = new Random();

    public EntityAetherMob(World var1)
    {
        super(var1);
    }

    /**
     * handles entity death timer, experience orb and particle creation
     */
    protected void onDeathUpdate()
    {
        if (this.deathTime == 18 && !this.worldObj.isRemote && (this.recentlyHit > 0 || this.isPlayer()) && !this.isChild())
        {
            for (int var1 = 0; var1 < this.random.nextInt(4); ++var1)
            {
                this.worldObj.spawnEntityInWorld(new EntityAetherCoin(this.worldObj, this.posX, this.posY, this.posZ, 1));
            }
        }

        super.onDeathUpdate();
    }

    public int getMaxHealth()
    {
        return 10;
    }
}
