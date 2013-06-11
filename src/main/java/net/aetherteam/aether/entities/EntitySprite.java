package net.aetherteam.aether.entities;

import net.aetherteam.aether.entities.util.Pos;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntitySprite extends EntityMob
{
    boolean isAnnoyed = false;
    Pos targetPos;
    Entity Holding;

    public EntitySprite(World var1)
    {
        super(var1);
    }

    public void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(16, Byte.valueOf((byte) 0));
    }

    public boolean isSitting()
    {
        return this.dataWatcher.getWatchableObjectByte(16) == 0;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();

        if (this.isAnnoyed)
        {
            this.setSitting(false);
        }

        if (this.isSitting())
        {
            this.motionX = 0.0D;
            this.motionZ = 0.0D;
            this.jumpMovementFactor = 0.0F;
            this.renderYawOffset = this.rotationPitch = this.rotationYaw = 0.0F;
        } else if (this.Holding != null)
        {
            this.motionY = 0.0D;
            this.motionX = 0.0D;
            this.motionZ = 0.0D;
            boolean var1 = true;

            if ((double) this.targetPos.y > this.posY)
            {
                this.motionY = 0.5D;
                var1 = false;
            }

            if ((double) this.targetPos.x < this.posX - 4.0D)
            {
                this.motionX = 0.5D;
                var1 = false;
            }

            if ((double) this.targetPos.x > this.posX + 4.0D)
            {
                this.motionX = -0.5D;
                var1 = false;
            }

            if ((double) this.targetPos.z < this.posZ - 4.0D)
            {
                this.motionZ = 0.5D;
                var1 = false;
            }

            if ((double) this.targetPos.z < this.posZ + 4.0D)
            {
                this.motionZ = -0.5D;
                var1 = false;
            }

            if (var1)
            {
                this.Holding.mountEntity(this.Holding);
                this.Holding = null;
                this.isAnnoyed = false;
                this.entityToAttack = null;
                this.setSitting(true);
            } else
            {
                this.Holding.mountEntity(this);
            }
        }
    }

    Pos findLowestPoint(World var1)
    {
        Pos var2 = new Pos((int) this.posX, (int) this.posY + 10, (int) this.posZ);
        int var3 = 128;

        for (int var4 = (int) this.posX - 8; (double) var4 < this.posX + 8.0D; ++var4)
        {
            for (int var5 = (int) this.posX - 8; (double) var5 < this.posX + 8.0D; ++var5)
            {
                if (var3 > var1.getHeightValue(var4, var5))
                {
                    var3 = var1.getHeightValue(var4, var5);
                    var2.x = var4;
                    var2.z = var5;
                }
            }
        }

        if (var3 + 10 > var2.y)
        {
            var2.y = var3 + 10;
        }

        return var2;
    }

    /**
     * Basic mob attack. Default to touch of death in EntityCreature. Overridden by each mob to define their attack.
     */
    protected void attackEntity(Entity var1, float var2)
    {
        EntityLiving var3 = (EntityLiving) var1;

        if (var2 < 1.0F && this.canEntityBeSeen(var3) && this.Holding == null && this.isAnnoyed)
        {
            this.targetPos = this.findLowestPoint(this.worldObj);
            this.Holding = var3;
        }
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource var1, int var2)
    {
        this.isAnnoyed = true;
        return super.attackEntityFrom(var1, var2);
    }

    public void setSitting(boolean var1)
    {
        if (var1)
        {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte) 0));
        } else
        {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte) 1));
        }
    }

    /**
     * Determines if an entity can be despawned, used on idle far away entities
     */
    public boolean canDespawn()
    {
        return false;
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound var1)
    {
        super.writeEntityToNBT(var1);
        this.isAnnoyed = var1.getBoolean("IsAnnoyed");
        this.setSitting(var1.getBoolean("IsSitting"));
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound var1)
    {
        super.readEntityFromNBT(var1);
        var1.setBoolean("IsAnnoyed", this.isAnnoyed);
        var1.setBoolean("IsSitting", this.isSitting());
    }

    public int getMaxHealth()
    {
        return 10;
    }
}
