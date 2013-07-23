package net.aetherteam.aether.oldcode;

import java.util.Random;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
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
    private static final float sponge = (180F / (float)Math.PI);

    public EntityHomeShot(World world)
    {
        super(world);
        this.texture = "/aether/mobs/electroball.png";
        this.lifeSpan = 200;
        this.life = this.lifeSpan;
        setSize(0.7F, 0.7F);
        this.firstRun = true;
        this.sinage = new float[3];
        this.isImmuneToFire = true;

        for (int i = 0; i < 3; i++)
        {
            this.sinage[i] = (this.rand.nextFloat() * 6.0F);
        }
    }

    public int getMaxHealth()
    {
        return 20;
    }

    public EntityHomeShot(World world, double x, double y, double z, EntityLiving ep)
    {
        super(world);
        this.texture = "/aether/mobs/electroball.png";
        this.lifeSpan = 200;
        this.life = this.lifeSpan;
        setSize(0.7F, 0.7F);
        setPosition(x, y, z);
        this.target = ep;
        this.sinage = new float[3];
        this.isImmuneToFire = true;

        for (int i = 0; i < 3; i++)
        {
            this.sinage[i] = (this.rand.nextFloat() * 6.0F);
        }
    }

    public void onUpdate()
    {
        super.onUpdate();
        this.life -= 1;

        if ((this.firstRun) && (this.target == null))
        {
            this.target = ((EntityLiving)findPlayerToAttack());
            this.firstRun = false;
        }

        if ((this.target == null) || (this.target.isDead) || (this.target.getHealth() <= 0))
        {
            this.isDead = true;
        }
        else if (this.life <= 0)
        {
            this.isDead = true;
        }
        else
        {
            updateAnims();
            faceIt();
            moveIt(this.target, 0.02D);
        }
    }

    public void moveIt(Entity e1, double sped)
    {
        double angle1 = this.rotationYaw / (180F / (float)Math.PI);
        this.motionX -= Math.sin(angle1) * sped;
        this.motionZ += Math.cos(angle1) * sped;
        double a = e1.posY - 0.75D;

        if (a < this.boundingBox.minY - 0.5D)
        {
            this.motionY -= sped / 2.0D;
        }
        else if (a > this.boundingBox.minY + 0.5D)
        {
            this.motionY += sped / 2.0D;
        }
        else
        {
            this.motionY += (a - this.boundingBox.minY) * (sped / 2.0D);
        }

        if (this.onGround)
        {
            this.onGround = false;
            this.motionY = 0.1000000014901161D;
        }
    }

    public void faceIt()
    {
        faceEntity(this.target, 10.0F, 10.0F);
    }

    public void updateAnims()
    {
        for (int i = 0; i < 3; i++)
        {
            this.sinage[i] += 0.3F + i * 0.13F;

            if (this.sinage[i] > ((float)Math.PI * 2F))
            {
                this.sinage[i] -= ((float)Math.PI * 2F);
            }
        }
    }

    public void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setShort("LifeLeft", (short)this.life);
    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readEntityFromNBT(nbttagcompound);
        this.life = nbttagcompound.getShort("LifeLeft");
    }

    public void checkOverLimit()
    {
        double a = this.target.posX - this.posX;
        double b = this.target.posY - this.posY;
        double c = this.target.posZ - this.posZ;
        double d = Math.sqrt(a * a + b * b + c * c);

        if (d > 0.125D)
        {
            double e = 0.125D / d;
            this.motionX *= e;
            this.motionY *= e;
            this.motionZ *= e;
        }
    }

    public Entity findPlayerToAttack()
    {
        EntityPlayer entityplayer = this.worldObj.getClosestPlayerToEntity(this, 16.0D);

        if ((entityplayer != null) && (canEntityBeSeen(entityplayer)))
        {
            return entityplayer;
        }

        return null;
    }

    public void applyEntityCollision(Entity entity)
    {
        super.applyEntityCollision(entity);

        if ((entity != null) && (this.target != null) && (entity == this.target))
        {
            boolean flag = entity.attackEntityFrom(DamageSource.causeMobDamage(this), 1);

            if (flag)
            {
                moveIt(entity, -0.1D);
            }
        }
    }

    public boolean attackEntityFrom(Entity entity, int i)
    {
        if (entity != null)
        {
            moveIt(entity, -0.15D - i / 8.0D);
            return true;
        }

        return false;
    }
}

