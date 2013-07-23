package net.aetherteam.aether.entities;

import java.util.Random;
import net.aetherteam.aether.interfaces.IAetherMob;
import net.aetherteam.aether.items.AetherItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class EntityCarrionSprout extends EntityAetherAnimal
    implements IAetherMob
{
    public float sinage;

    public EntityCarrionSprout(World world)
    {
        super(world);
        setSize(1.0F, 1.25F);
        this.jumpMovementFactor = 0.0F;
        this.moveSpeed = 0.0F;
        this.texture = (this.dir + "/mobs/carrionsprout/sprout.png");
        this.sinage = (this.rand.nextFloat() * 6.0F);
    }

    protected void jump()
    {
    }

    protected void dropFewItems(boolean var1, int var2)
    {
        dropItem(AetherItems.Wyndberry.itemID, 2);
    }

    public void onLivingUpdate()
    {
        super.onLivingUpdate();

        if (this.health <= 0)
        {
            if (this.health > 0);
        }
        else
        {
            this.entityAge += 1;
            despawnEntity();
        }

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

        if ((!this.isDead) && (!this.isCollided))
        {
            this.motionX = (this.motionZ = 0.0D);
        }
    }

    public void addVelocity(double d, double d1, double d2)
    {
    }

    public void knockBack(Entity entity, int i, double d, double d1)
    {
    }

    public void applyEntityCollision(Entity entity)
    {
        if ((entity instanceof EntityCarrionSprout))
        {
            super.applyEntityCollision(entity);
        }
    }

    public int getMaxHealth()
    {
        return 10;
    }

    public EntityAgeable createChild(EntityAgeable entityageable)
    {
        return null;
    }
}

