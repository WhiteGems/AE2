package net.aetherteam.aether.entities;

import net.aetherteam.aether.items.AetherItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EntityDartGolden extends EntityProjectileBase
{
    public EntityLivingBase victim;
    public static int texfxindex = 94;

    public EntityDartGolden(World world)
    {
        super(world);
    }

    public EntityDartGolden(World world, double x, double y, double z)
    {
        super(world, x, y, z);
    }

    public EntityDartGolden(World world, EntityLivingBase ent)
    {
        super(world, ent);
    }

    public void entityInit()
    {
        super.entityInit();
        this.item = new ItemStack(AetherItems.Dart, 1, 0);
        this.curvature = 0.0F;
        this.dmg = 4;
        this.speed = 1.5F;
    }

    /**
     * Returns if this entity is in water and will end up adding the waters velocity to the entity
     */
    public boolean handleWaterMovement()
    {
        return this.victim == null && super.handleWaterMovement();
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();

        if (this.ticksFlying == 200 && !this.worldObj.isRemote)
        {
            this.setDead();
        }
    }

    /**
     * Will get destroyed next tick.
     */
    public void setDead()
    {
        this.victim = null;
        super.setDead();
    }

    public boolean onHitBlock()
    {
        this.curvature = 0.03F;
        this.worldObj.playSoundAtEntity(this, "random.drr", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
        return this.victim == null;
    }

    public boolean canBeShot(Entity ent)
    {
        return super.canBeShot(ent) && this.victim == null;
    }
}
