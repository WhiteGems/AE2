package net.aetherteam.aether.entities;

import net.aetherteam.aether.interfaces.IAetherMob;
import net.aetherteam.aether.items.AetherItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.world.World;

public class EntityCarrionSprout extends EntityAetherAnimal implements IAetherMob
{
    public float sinage;

    public EntityCarrionSprout(World world)
    {
        super(world);
        this.setSize(1.0F, 1.25F);
        this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(10.0D);
        this.setEntityHealth(10.0F);
        this.jumpMovementFactor = 0.0F;
        this.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111128_a(0.0D);
        this.sinage = this.rand.nextFloat() * 6.0F;
    }

    /**
     * Causes this entity to do an upwards motion (jumping).
     */
    protected void jump() {}

    /**
     * Drop 0-2 items of this living's type. @param par1 - Whether this entity has recently been hit by a player. @param
     * par2 - Level of Looting used to kill this mob.
     */
    protected void dropFewItems(boolean var1, int var2)
    {
        this.dropItem(AetherItems.Wyndberry.itemID, 2);
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate()
    {
        super.onLivingUpdate();

        if (this.func_110143_aJ() > 0.0F)
        {
            ++this.entityAge;
            this.despawnEntity();

            if (this.hurtTime > 0)
            {
                this.sinage += 0.9F;
            }
            else
            {
                this.sinage += 0.15F;
            }

            if (this.sinage > ((float)Math.PI * 2F))
            {
                this.sinage -= ((float)Math.PI * 2F);
            }

            if (!this.isDead && !this.isCollided)
            {
                this.motionX = this.motionZ = 0.0D;
            }
        }
    }

    /**
     * Adds to the current velocity of the entity. Args: x, y, z
     */
    public void addVelocity(double d, double d1, double d2) {}

    public void knockBack(Entity entity, int i, double d, double d1) {}

    /**
     * Applies a velocity to each of the entities pushing them away from each other. Args: entity
     */
    public void applyEntityCollision(Entity entity)
    {
        if (entity instanceof EntityCarrionSprout)
        {
            super.applyEntityCollision(entity);
        }
    }

    public EntityAgeable createChild(EntityAgeable entityageable)
    {
        return null;
    }
}
