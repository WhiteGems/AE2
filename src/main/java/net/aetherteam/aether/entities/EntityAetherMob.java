package net.aetherteam.aether.entities;

import java.util.Random;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.world.World;

public class EntityAetherMob extends EntityMob
{
    Random random = new Random();

    public EntityAetherMob(World world)
    {
        super(world);
        this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(10.0D);
        this.setEntityHealth(10.0F);
    }

    /**
     * handles entity death timer, experience orb and particle creation
     */
    protected void onDeathUpdate()
    {
        if (this.deathTime == 18 && !this.worldObj.isRemote && (this.recentlyHit > 0 || this.isPlayer()) && !this.isChild())
        {
            for (int amount = 0; amount < this.random.nextInt(4); ++amount)
            {
                this.worldObj.spawnEntityInWorld(new EntityAetherCoin(this.worldObj, this.posX, this.posY, this.posZ, 1));
            }
        }

        super.onDeathUpdate();
    }
}
