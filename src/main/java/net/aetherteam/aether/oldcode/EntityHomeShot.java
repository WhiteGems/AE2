package net.aetherteam.aether.oldcode;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityHomeShot extends EntityFlying
{
    public float[] sinage;
    public EntityLiving target;
    public boolean firstRun;
    public int life;
    public int lifeSpan;
    private static final double topSpeed = 0.125D;
    private static final float sponge = (180F / (float) Math.PI);

    public EntityHomeShot(World var1)
    {
        super(var1);
        this.texture = "/aether/mobs/electroball.png";
        this.lifeSpan = 200;
        this.life = this.lifeSpan;
        this.setSize(0.7F, 0.7F);
        this.firstRun = true;
        this.sinage = new float[3];
        this.isImmuneToFire = true;

        for (int var2 = 0; var2 < 3; ++var2)
        {
            this.sinage[var2] = this.rand.nextFloat() * 6.0F;
        }
    }

    public int getMaxHealth()
    {
        return 20;
    }

    public EntityHomeShot(World var1, double var2, double var4, double var6, EntityLiving var8)
    {
        super(var1);
        this.texture = "/aether/mobs/electroball.png";
        this.lifeSpan = 200;
        this.life = this.lifeSpan;
        this.setSize(0.7F, 0.7F);
        this.setPosition(var2, var4, var6);
        this.target = var8;
        this.sinage = new float[3];
        this.isImmuneToFire = true;

        for (int var9 = 0; var9 < 3; ++var9)
        {
            this.sinage[var9] = this.rand.nextFloat() * 6.0F;
        }
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();
        --this.life;

        if (this.firstRun && this.target == null)
        {
            this.target = (EntityLiving) this.findPlayerToAttack();
            this.firstRun = false;
        }

        if (this.target != null && !this.target.isDead && this.target.getHealth() > 0)
        {
            if (this.life <= 0)
            {
                this.isDead = true;
            } else
            {
                this.updateAnims();
                this.faceIt();
                this.moveIt(this.target, 0.02D);
            }
        } else
        {
            this.isDead = true;
        }
    }

    public void moveIt(Entity var1, double var2)
    {
        double var4 = (double) (this.rotationYaw / (180F / (float) Math.PI));
        this.motionX -= Math.sin(var4) * var2;
        this.motionZ += Math.cos(var4) * var2;
        double var6 = var1.posY - 0.75D;

        if (var6 < this.boundingBox.minY - 0.5D)
        {
            this.motionY -= var2 / 2.0D;
        } else if (var6 > this.boundingBox.minY + 0.5D)
        {
            this.motionY += var2 / 2.0D;
        } else
        {
            this.motionY += (var6 - this.boundingBox.minY) * (var2 / 2.0D);
        }

        if (this.onGround)
        {
            this.onGround = false;
            this.motionY = 0.10000000149011612D;
        }
    }

    public void faceIt()
    {
        this.faceEntity(this.target, 10.0F, 10.0F);
    }

    public void updateAnims()
    {
        for (int var1 = 0; var1 < 3; ++var1)
        {
            this.sinage[var1] += 0.3F + (float) var1 * 0.13F;

            if (this.sinage[var1] > ((float) Math.PI * 2F))
            {
                this.sinage[var1] -= ((float) Math.PI * 2F);
            }
        }
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound var1)
    {
        super.writeEntityToNBT(var1);
        var1.setShort("LifeLeft", (short) this.life);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound var1)
    {
        super.readEntityFromNBT(var1);
        this.life = var1.getShort("LifeLeft");
    }

    public void checkOverLimit()
    {
        double var1 = this.target.posX - this.posX;
        double var3 = this.target.posY - this.posY;
        double var5 = this.target.posZ - this.posZ;
        double var7 = Math.sqrt(var1 * var1 + var3 * var3 + var5 * var5);

        if (var7 > 0.125D)
        {
            double var9 = 0.125D / var7;
            this.motionX *= var9;
            this.motionY *= var9;
            this.motionZ *= var9;
        }
    }

    public Entity findPlayerToAttack()
    {
        EntityPlayer var1 = this.worldObj.getClosestPlayerToEntity(this, 16.0D);
        return var1 != null && this.canEntityBeSeen(var1) ? var1 : null;
    }

    /**
     * Applies a velocity to each of the entities pushing them away from each other. Args: entity
     */
    public void applyEntityCollision(Entity var1)
    {
        super.applyEntityCollision(var1);

        if (var1 != null && this.target != null && var1 == this.target)
        {
            boolean var2 = var1.attackEntityFrom(DamageSource.causeMobDamage(this), 1);

            if (var2)
            {
                this.moveIt(var1, -0.1D);
            }
        }
    }

    public boolean attackEntityFrom(Entity var1, int var2)
    {
        if (var1 != null)
        {
            this.moveIt(var1, -0.15D - (double) var2 / 8.0D);
            return true;
        } else
        {
            return false;
        }
    }
}
