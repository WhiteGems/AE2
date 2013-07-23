package net.aetherteam.aether.entities;

import java.util.Random;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.world.World;

public class EntityAetherMob extends EntityMob
{
    Random random = new Random();

    public EntityAetherMob(World world)
    {
        super(world);
    }

    protected void onDeathUpdate()
    {
        if (this.deathTime == 18)
        {
            if ((!this.worldObj.isRemote) && ((this.recentlyHit > 0) || (isPlayer())) && (!isChild()))
            {
                for (int amount = 0; amount < this.random.nextInt(4); amount++)
                {
                    this.worldObj.spawnEntityInWorld(new EntityAetherCoin(this.worldObj, this.posX, this.posY, this.posZ, 1));
                }
            }
        }

        super.onDeathUpdate();
    }

    public int getMaxHealth()
    {
        return 10;
    }
}

