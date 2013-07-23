package net.aetherteam.aether.entities;

import net.aetherteam.aether.entities.util.Pos;
import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntitySprite extends EntityMob
{
    boolean isAnnoyed;
    Pos targetPos;
    Entity Holding;

    public EntitySprite(World par1World)
    {
        super(par1World);
        this.isAnnoyed = false;
    }

    public void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(16, Byte.valueOf((byte)0));
    }

    public boolean isSitting()
    {
        return this.dataWatcher.getWatchableObjectByte(16) == 0;
    }

    public void onUpdate()
    {
        super.onUpdate();

        if (this.isAnnoyed)
        {
            setSitting(false);
        }

        if (isSitting())
        {
            this.motionX = 0.0D;
            this.motionZ = 0.0D;
            this.jumpMovementFactor = 0.0F;
            this.renderYawOffset = (this.rotationPitch = this.rotationYaw = 0.0F);
        }
        else if (this.Holding != null)
        {
            this.motionY = 0.0D;
            this.motionX = 0.0D;
            this.motionZ = 0.0D;
            boolean targetReached = true;

            if (this.targetPos.y > this.posY)
            {
                this.motionY = 0.5D;
                targetReached = false;
            }

            if (this.targetPos.x < this.posX - 4.0D)
            {
                this.motionX = 0.5D;
                targetReached = false;
            }

            if (this.targetPos.x > this.posX + 4.0D)
            {
                this.motionX = -0.5D;
                targetReached = false;
            }

            if (this.targetPos.z < this.posZ - 4.0D)
            {
                this.motionZ = 0.5D;
                targetReached = false;
            }

            if (this.targetPos.z < this.posZ + 4.0D)
            {
                this.motionZ = -0.5D;
                targetReached = false;
            }

            if (targetReached)
            {
                this.Holding.mountEntity(this.Holding);
                this.Holding = null;
                this.isAnnoyed = false;
                this.entityToAttack = null;
                setSitting(true);
            }
            else
            {
                this.Holding.mountEntity(this);
            }
        }
    }

    Pos findLowestPoint(World objWorld)
    {
        Pos vec = new Pos((int)this.posX, (int)this.posY + 10, (int)this.posZ);
        int hieght = 128;

        for (int i = (int)this.posX - 8; i < this.posX + 8.0D; i++)
        {
            for (int k = (int)this.posX - 8; k < this.posX + 8.0D; k++)
                if (hieght > objWorld.getHeightValue(i, k))
                {
                    hieght = objWorld.getHeightValue(i, k);
                    vec.x = i;
                    vec.z = k;
                }
        }

        if (hieght + 10 > vec.y)
        {
            vec.y = (hieght + 10);
        }

        return vec;
    }

    protected void attackEntity(Entity entity, float f)
    {
        EntityLiving target = (EntityLiving)entity;

        if ((f < 1.0F) && (canEntityBeSeen(target)))
        {
            if ((this.Holding == null) && (this.isAnnoyed))
            {
                this.targetPos = findLowestPoint(this.worldObj);
                this.Holding = target;
            }
        }
    }

    public boolean attackEntityFrom(DamageSource par1DamageSource, int par2)
    {
        this.isAnnoyed = true;
        return super.attackEntityFrom(par1DamageSource, par2);
    }

    public void setSitting(boolean sitting)
    {
        if (sitting)
        {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte)0));
        }
        else
        {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte)1));
        }
    }

    public boolean canDespawn()
    {
        return false;
    }

    public void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeEntityToNBT(nbttagcompound);
        this.isAnnoyed = nbttagcompound.getBoolean("IsAnnoyed");
        setSitting(nbttagcompound.getBoolean("IsSitting"));
    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readEntityFromNBT(nbttagcompound);
        nbttagcompound.setBoolean("IsAnnoyed", this.isAnnoyed);
        nbttagcompound.setBoolean("IsSitting", isSitting());
    }

    public int getMaxHealth()
    {
        return 10;
    }
}

