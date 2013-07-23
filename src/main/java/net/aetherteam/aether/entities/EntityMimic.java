package net.aetherteam.aether.entities;

import net.aetherteam.aether.blocks.AetherBlocks;
import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityMimic extends EntityDungeonMob
{
    public String dir = "/net/aetherteam/aether/client/sprites";
    public float mouth;
    public float legs;
    private float legsDirection = 1.0F;

    public EntityMimic(World var1)
    {
        super(var1);
        this.texture = this.dir + "/mobs/mimic/mimic1.png";
        this.yOffset = 0.0F;
        this.setSize(1.0F, 2.0F);
        this.health = 40;
        this.attackStrength = 5;
        this.entityToAttack = var1.getClosestPlayerToEntity(this, 64.0D);
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();
        this.mouth = (float)(Math.cos((double)((float)this.ticksExisted / 10.0F * (float)Math.PI)) + 1.0D) * 0.6F;
        this.legs *= 0.9F;

        if (this.motionX > 0.001D || this.motionX < -0.001D || this.motionZ > 0.001D || this.motionZ < -0.001D)
        {
            this.legs += this.legsDirection * 0.2F;

            if (this.legs > 1.0F)
            {
                this.legsDirection = -1.0F;
            }

            if (this.legs < -1.0F)
            {
                this.legsDirection = 1.0F;
            }
        }
    }

    /**
     * Applies a velocity to each of the entities pushing them away from each other. Args: entity
     */
    public void applyEntityCollision(Entity var1)
    {
        if (!this.isDead && var1 != null)
        {
            var1.attackEntityFrom(DamageSource.causeMobDamage(this), 4);
        }
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected String getHurtSound()
    {
        return "mob.slime.small";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected String getDeathSound()
    {
        return "mob.slime.small";
    }

    /**
     * Returns the volume for the sounds this mob makes.
     */
    protected float getSoundVolume()
    {
        return 0.6F;
    }

    /**
     * Returns the item ID for the item the mob drops on death.
     */
    protected int getDropItemId()
    {
        return AetherBlocks.SkyrootChest.blockID;
    }
}
