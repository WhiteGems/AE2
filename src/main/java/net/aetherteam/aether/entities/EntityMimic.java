package net.aetherteam.aether.entities;

import net.aetherteam.aether.blocks.AetherBlocks;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityMimic extends EntityDungeonMob
{
    public String dir = "/net/aetherteam/aether/client/sprites";
    public float mouth;
    public float legs;
    private float legsDirection = 1.0F;

    public EntityMimic(World world)
    {
        super(world);
        this.texture = (this.dir + "/mobs/mimic/mimic1.png");
        this.yOffset = 0.0F;
        setSize(1.0F, 2.0F);
        this.health = 40;
        this.attackStrength = 5;
        this.entityToAttack = world.getClosestPlayerToEntity(this, 64.0D);
    }

    public void onUpdate()
    {
        super.onUpdate();
        this.mouth = ((float)(Math.cos(this.ticksExisted / 10.0F * (float)Math.PI) + 1.0D) * 0.6F);
        this.legs *= 0.9F;

        if ((this.motionX > 0.001D) || (this.motionX < -0.001D) || (this.motionZ > 0.001D) || (this.motionZ < -0.001D))
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

    public void applyEntityCollision(Entity entity)
    {
        if ((!this.isDead) && (entity != null))
        {
            entity.attackEntityFrom(DamageSource.causeMobDamage(this), 4);
        }
    }

    protected String getHurtSound()
    {
        return "mob.slime.small";
    }

    protected String getDeathSound()
    {
        return "mob.slime.small";
    }

    protected float getSoundVolume()
    {
        return 0.6F;
    }

    protected int getDropItemId()
    {
        return AetherBlocks.SkyrootChest.blockID;
    }
}

