package net.aetherteam.aether.entities;

import net.aetherteam.aether.interfaces.IAetherMob;
import net.aetherteam.aether.items.AetherItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.world.World;

public class EntityCarrionSprout extends EntityAetherAnimal implements IAetherMob
{
    public float sinage;

    public EntityCarrionSprout(World var1)
    {
        super(var1);
        this.setSize(1.0F, 1.25F);
        this.jumpMovementFactor = 0.0F;
        this.moveSpeed = 0.0F;
        this.texture = this.dir + "/mobs/carrionsprout/sprout.png";
        this.sinage = this.rand.nextFloat() * 6.0F;
    }

    /**
     * Causes this entity to do an upwards motion (jumping).
     */
    protected void jump()
    {}

    /**
     * Drop 0-2 items of this living's type. @param par1 - Whether this entity has recently been hit by a player. @param
     * par2 - Level of Looting used to kill this mob.
     */
    protected void dropFewItems(boolean var1, int var2)
    {
        this.dropItem(AetherItems.Strawberry.itemID, 2);
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate()
    {
        super.onLivingUpdate();

        if (this.health <= 0)
        {
            if (this.health <= 0)
            {
                return;
            }
        } else
        {
            ++this.entityAge;
            this.despawnEntity();
        }

        if (this.hurtTime > 0)
        {
            this.sinage += 0.9F;
        } else
        {
            this.sinage += 0.15F;
        }

        if (this.sinage > ((float) Math.PI * 2F))
        {
            this.sinage -= ((float) Math.PI * 2F);
        }

        if (!this.isDead && !this.isCollided)
        {
            this.motionX = this.motionZ = 0.0D;
        }
    }

    /**
     * Adds to the current velocity of the entity. Args: x, y, z
     */
    public void addVelocity(double var1, double var3, double var5)
    {}

    /**
     * knocks back this entity
     */
    public void knockBack(Entity var1, int var2, double var3, double var5)
    {}

    /**
     * Applies a velocity to each of the entities pushing them away from each other. Args: entity
     */
    public void applyEntityCollision(Entity var1)
    {
        if (var1 instanceof EntityCarrionSprout)
        {
            super.applyEntityCollision(var1);
        }
    }

    public int getMaxHealth()
    {
        return 10;
    }

    public EntityAgeable createChild(EntityAgeable var1)
    {
        return null;
    }
}
