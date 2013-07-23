package net.aetherteam.aether.entities;

import java.util.Random;
import net.aetherteam.aether.blocks.AetherBlocks;
import net.aetherteam.aether.entities.bosses.EntitySentryGuardian;
import net.aetherteam.aether.entities.bosses.EntitySlider;
import net.minecraft.block.Block;
import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntitySentry extends EntityDungeonMob
{
    public String dir = "/net/aetherteam/aether/client/sprites";
    private World q;
    private boolean shouldExplode = false;
    public float field_100021_a;
    public float field_100020_b;
    private int jcount;
    public int size;
    public int counter;
    public int lostyou;
    private EntitySentryGuardian parent;

    public EntitySentry(World world)
    {
        super(world);
        this.texture = (this.dir + "/mobs/sentry/sentry.png");
        this.size = 2;
        this.yOffset = 0.0F;
        this.moveSpeed = 1.0F;
        this.field_100021_a = 1.0F;
        this.field_100020_b = 1.0F;
        this.jcount = (this.rand.nextInt(20) + 10);
        func_100019_e(this.size);
        this.worldObj = world;
    }

    public EntitySentry(World world, double x, double y, double z)
    {
        super(world);
        this.texture = (this.dir + "/mobs/sentry/sentry.png");
        this.size = 2;
        this.yOffset = 0.0F;
        this.moveSpeed = 1.0F;
        this.field_100021_a = 1.0F;
        this.field_100020_b = 1.0F;
        this.jcount = (this.rand.nextInt(20) + 10);
        func_100019_e(this.size);
        setPosition(x, y, z);
        this.worldObj = world;
    }

    public void onDeath(DamageSource source)
    {
        if (this.parent != null)
        {
            this.parent.failedYou();
        }

        super.onDeath(source);
    }

    public void func_100019_e(int i)
    {
        setEntityHealth(10);
        this.width = 0.85F;
        this.height = 0.85F;
        setPosition(this.posX, this.posY, this.posZ);
    }

    public void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setInteger("Size", this.size - 1);
        nbttagcompound.setInteger("LostYou", this.lostyou);
        nbttagcompound.setInteger("Counter", this.counter);
        nbttagcompound.setBoolean("Awake", getAwake());
    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readEntityFromNBT(nbttagcompound);
        this.size = (nbttagcompound.getInteger("Size") + 1);
        this.lostyou = nbttagcompound.getInteger("LostYou");
        this.counter = nbttagcompound.getInteger("Counter");
        setAwake(nbttagcompound.getBoolean("Awake"));
    }

    public void onUpdate()
    {
        boolean flag = this.onGround;
        super.onUpdate();

        if ((this.onGround) && (!flag))
        {
            this.worldObj.playSoundAtEntity(this, "mob.slime.small", getSoundVolume(), ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F) / 0.8F);
        }
        else if ((!this.onGround) && (flag) && (this.entityToAttack != null))
        {
            this.motionX *= 3.0D;
            this.motionZ *= 3.0D;
        }

        if ((this.entityToAttack != null) && (this.entityToAttack.isDead))
        {
            this.entityToAttack = null;
        }

        if (this.shouldExplode)
        {
            if (!this.worldObj.isRemote)
            {
                this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, 0.1F, false);
                setEntityHealth(0);
                setDead();
            }
        }
    }

    public boolean attackEntityFrom(DamageSource ds, int i)
    {
        boolean flag = super.attackEntityFrom(ds, i);

        if ((flag) && ((ds.getEntity() instanceof EntityLiving)))
        {
            setAwake(true);
            this.lostyou = 0;
            this.entityToAttack = ds.getEntity();
            this.texture = (this.dir + "/mobs/sentry/sentry_lit.png");
        }

        return flag;
    }

    public void setArrowHeading(double d, double d1, double d2, float f, float f1)
    {
        float f2 = MathHelper.sqrt_double(d * d + d1 * d1 + d2 * d2);
        d /= f2;
        d1 /= f2;
        d2 /= f2;
        d += this.rand.nextGaussian() * 0.007499999832361937D * f1;
        d1 += this.rand.nextGaussian() * 0.007499999832361937D * f1;
        d2 += this.rand.nextGaussian() * 0.007499999832361937D * f1;
        d *= f;
        d1 *= f;
        d2 *= f;
        this.motionX = d;
        this.motionY = d1;
        this.motionZ = d2;
        float f3 = MathHelper.sqrt_double(d * d + d2 * d2);
        this.prevRotationYaw = (this.rotationYaw = (float)(Math.atan2(d, d2) * 180.0D / Math.PI));
        this.prevRotationPitch = (this.rotationPitch = (float)(Math.atan2(d1, f3) * 180.0D / Math.PI));
    }

    public void shutdown()
    {
        this.counter = -64;
        setAwake(false);
        this.entityToAttack = null;
        this.texture = (this.dir + "/mobs/sentry/sentry.png");
        setPathToEntity(null);
        this.moveStrafing = 0.0F;
        this.moveForward = 0.0F;
        this.isJumping = false;
        this.motionX = 0.0D;
        this.motionZ = 0.0D;
    }

    public void applyEntityCollision(Entity entity)
    {
        if (!this.worldObj.isRemote)
        {
            if ((!this.isDead) && (this.entityToAttack != null) && (entity != null) && (this.entityToAttack == entity))
            {
                if (((!(entity instanceof EntityPlayer)) ||
                        (((EntityPlayer)entity).capabilities.isCreativeMode)) ||
                        ((entity instanceof EntitySlider)))
                {
                    return;
                }

                this.shouldExplode = true;
                entity.attackEntityFrom(DamageSource.causeMobDamage(this), 2);

                if ((entity instanceof EntityLiving))
                {
                    EntityLiving entityliving = (EntityLiving)entity;
                    double d = entityliving.posX - this.posX;

                    for (double d2 = entityliving.posZ - this.posZ; d * d + d2 * d2 < 0.0001D; d2 = (Math.random() - Math.random()) * 0.01D)
                    {
                        d = (Math.random() - Math.random()) * 0.01D;
                    }

                    if ((entity instanceof EntityPlayerMP))
                    {
                        entityliving.knockBack(this, 5, -d, -d2);
                        entityliving.addVelocity(4.0D, 0.0D, 0.0D);
                        entityliving.addVelocity(0.0D, 4.0D, 0.0D);
                        entityliving.addVelocity(0.0D, 0.0D, 4.0D);
                    }
                    else
                    {
                        entityliving.knockBack(this, 5, -d, -d2);
                        entityliving.addVelocity(4.0D, 0.0D, 0.0D);
                        entityliving.addVelocity(0.0D, 4.0D, 0.0D);
                        entityliving.addVelocity(0.0D, 0.0D, 4.0D);
                    }
                }

                float f = 0.01745329F;

                for (int i = 0; i < 40; i++)
                {
                    double d1 = (float)this.posX + this.rand.nextFloat() * 0.25F;
                    double d3 = (float)this.posY + 0.5F;
                    double d4 = (float)this.posZ + this.rand.nextFloat() * 0.25F;
                    float f1 = this.rand.nextFloat() * 360.0F;
                    this.worldObj.spawnParticle("explode", d1, d3, d4, -Math.sin(f * f1) * 0.75D, 0.125D, Math.cos(f * f1) * 0.75D);
                }
            }
        }
    }

    protected void updateEntityActionState()
    {
        EntityPlayer entityplayer = this.worldObj.getClosestPlayerToEntity(this, 8.0D);

        if ((!getAwake()) && (this.counter >= 8))
        {
            if ((entityplayer != null) && (canEntityBeSeen(entityplayer)) && (!entityplayer.capabilities.isCreativeMode))
            {
                faceEntity(entityplayer, 10.0F, 10.0F);
                this.entityToAttack = entityplayer;
                setAwake(true);
                this.lostyou = 0;
                this.texture = (this.dir + "/mobs/sentry/sentry_lit.png");
            }

            this.counter = 0;
        }
        else if ((getAwake()) && (this.counter >= 8))
        {
            if (this.entityToAttack == null)
            {
                if ((entityplayer != null) && (canEntityBeSeen(entityplayer)))
                {
                    this.entityToAttack = entityplayer;
                    setAwake(true);
                    this.lostyou = 0;
                }
                else
                {
                    this.lostyou += 1;

                    if (this.lostyou >= 4)
                    {
                        shutdown();
                    }
                }
            }
            else if ((!canEntityBeSeen(this.entityToAttack)) || (getDistanceToEntity(this.entityToAttack) >= 16.0F))
            {
                this.lostyou += 1;

                if (this.lostyou >= 4)
                {
                    shutdown();
                }
            }
            else
            {
                this.lostyou = 0;
            }

            this.counter = 0;
        }
        else
        {
            this.counter += 1;
        }

        if (!getAwake())
        {
            return;
        }

        if (this.entityToAttack != null)
        {
            faceEntity(this.entityToAttack, 10.0F, 10.0F);
        }

        if ((this.onGround) && (this.jcount-- <= 0))
        {
            this.jcount = (this.rand.nextInt(20) + 10);
            this.isJumping = true;
            this.moveStrafing = (0.5F - this.rand.nextFloat());
            this.moveForward = 1.0F;
            this.worldObj.playSoundAtEntity(this, "mob.slime.small", getSoundVolume(), ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F) * 0.8F);

            if (this.entityToAttack != null)
            {
                this.jcount /= 2;
                this.moveForward = 1.0F;
            }
        }
        else
        {
            this.isJumping = false;

            if (this.onGround)
            {
                this.moveStrafing = (this.moveForward = 0.0F);
            }
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

    public boolean getCanSpawnHere()
    {
        return super.getCanSpawnHere();
    }

    protected float getSoundVolume()
    {
        return 0.6F;
    }

    protected int getDropItemId()
    {
        if (this.rand.nextInt(5) == 0)
        {
            return AetherBlocks.LightDungeonStone.blockID;
        }

        return AetherBlocks.DungeonStone.blockID;
    }

    public void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(16, Byte.valueOf((byte)0));
    }

    public boolean getAwake()
    {
        return (this.dataWatcher.getWatchableObjectByte(16) & 0x1) != 0;
    }

    public void setAwake(boolean awake)
    {
        if (awake)
        {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte)1));
        }
        else
        {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte)0));
        }
    }

    public void setParent(EntitySentryGuardian entitySentryGuardian)
    {
        this.parent = entitySentryGuardian;
    }
}

