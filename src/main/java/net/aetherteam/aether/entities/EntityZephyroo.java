package net.aetherteam.aether.entities;

import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class EntityZephyroo extends EntityAetherAnimal
{
    private int timeTilJump = 10;

    public EntityZephyroo(World var1)
    {
        super(var1);
        this.texture = this.dir + "/mobs/Zephyroo.png";
        this.moveSpeed = 5.0F;
        this.setSize(0.2F, 2.0F);
    }

    /**
     * This method returns a value to be applied directly to entity speed, this factor is less than 1 when a slowdown
     * potion effect is applied, more than 1 when a haste potion effect is applied and 2 for fleeing entities.
     */
    public float getSpeedModifier()
    {
        return 3.0F;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();
        List var1 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(5.5D, 1.75D, 5.5D));
        boolean var2 = false;
        Iterator var3 = var1.iterator();

        while (var3.hasNext())
        {
            Entity var4 = (Entity) var3.next();

            if (var4 instanceof EntityPlayer)
            {
                EntityPlayer var5 = (EntityPlayer) var4;

                if (var5.username.equalsIgnoreCase("ClashJTM"))
                {
                    var2 = true;
                    this.makeLoveToClash(true);
                    this.inLove = 1;
                    this.attackEntity(var5, 20.0F);
                    this.faceEntity(var5, 5.5F, (float) this.getVerticalFaceSpeed());
                } else
                {
                    this.makeLoveToClash(false);
                    this.inLove = 0;
                    this.entityToAttack = null;
                }
            }
        }

        if (!var2)
        {
            this.makeLoveToClash(false);
            this.inLove = 0;
            this.entityToAttack = null;
        }
    }

    /**
     * Basic mob attack. Default to touch of death in EntityCreature. Overridden by each mob to define their attack.
     */
    protected void attackEntity(Entity var1, float var2)
    {
        if (var1 instanceof EntityPlayer)
        {
            if (var2 < 3.0F)
            {
                double var3 = var1.posX - this.posX;
                double var5 = var1.posZ - this.posZ;
                this.rotationYaw = (float) (Math.atan2(var5, var3) * 180.0D / Math.PI) - 90.0F;
                this.hasAttacked = true;
            }

            EntityPlayer var7 = (EntityPlayer) var1;
            this.entityToAttack = var1;
        } else
        {
            super.attackEntity(var1, var2);
        }
    }

    protected void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(16, new Integer(0));
        this.dataWatcher.addObject(17, new Byte((byte) 0));
    }

    public boolean isLovingClash()
    {
        return this.dataWatcher.getWatchableObjectByte(17) >= 1;
    }

    public void makeLoveToClash(boolean var1)
    {
        this.dataWatcher.updateObject(17, Byte.valueOf((byte) (var1 ? 1 : 0)));
    }

    public EntityAgeable createChild(EntityAgeable var1)
    {
        return new EntityZephyroo(this.worldObj);
    }

    public int getMaxHealth()
    {
        return 20;
    }

    protected void updateEntityActionState()
    {
        super.updateEntityActionState();
        this.fallDistance = 0.0F;

        if (this.onGround && (this.motionX > 1.0E-4D || this.motionZ > 1.0E-4D))
        {
            this.hop();
        } else
        {
            if (this.timeTilJump != 0)
            {
                --this.timeTilJump;
            }

            if (this.timeTilJump == 0)
            {
                this.timeTilJump = 10;
            }
        }

        this.dataWatcher.updateObject(16, Integer.valueOf(this.timeTilJump));
    }

    public float getTimeTilJump()
    {
        return (float) this.dataWatcher.getWatchableObjectInt(16);
    }

    protected void hop()
    {
        super.jump();
    }
}
