package net.aetherteam.aether.entities;

import net.aetherteam.aether.blocks.AetherBlocks;
import net.aetherteam.aether.entities.bosses.EntitySentryGuardian;
import net.aetherteam.aether.entities.bosses.EntitySlider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntitySentry extends EntityDungeonMob
{
    public String dir = "/net/aetherteam/aether/client/sprites";

    /** Reference to the World object. */
    private World worldObj;
    private boolean shouldExplode = false;
    public float field_100021_a;
    public float field_100020_b;
    private int jcount;
    public int size = 2;
    public int counter;
    public int lostyou;
    private EntitySentryGuardian parent;

    public EntitySentry(World world)
    {
        super(world);
        this.yOffset = 0.0F;
        this.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111128_a(1.0D);
        this.field_100021_a = 1.0F;
        this.field_100020_b = 1.0F;
        this.jcount = this.rand.nextInt(20) + 10;
        this.func_100019_e(this.size);
        this.worldObj = world;
    }

    public EntitySentry(World world, double x, double y, double z)
    {
        super(world);
        this.yOffset = 0.0F;
        this.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111128_a(1.0D);
        this.field_100021_a = 1.0F;
        this.field_100020_b = 1.0F;
        this.jcount = this.rand.nextInt(20) + 10;
        this.func_100019_e(this.size);
        this.setPosition(x, y, z);
        this.worldObj = world;
    }

    /**
     * Called when the mob's health reaches 0.
     */
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
        this.setEntityHealth(10.0F);
        this.width = 0.85F;
        this.height = 0.85F;
        this.setPosition(this.posX, this.posY, this.posZ);
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setInteger("Size", this.size - 1);
        nbttagcompound.setInteger("LostYou", this.lostyou);
        nbttagcompound.setInteger("Counter", this.counter);
        nbttagcompound.setBoolean("Awake", this.getAwake());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readEntityFromNBT(nbttagcompound);
        this.size = nbttagcompound.getInteger("Size") + 1;
        this.lostyou = nbttagcompound.getInteger("LostYou");
        this.counter = nbttagcompound.getInteger("Counter");
        this.setAwake(nbttagcompound.getBoolean("Awake"));
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        boolean flag = this.onGround;
        super.onUpdate();

        if (this.onGround && !flag)
        {
            this.worldObj.playSoundAtEntity(this, "mob.slime.small", this.getSoundVolume(), ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F) / 0.8F);
        }
        else if (!this.onGround && flag && this.entityToAttack != null)
        {
            this.motionX *= 3.0D;
            this.motionZ *= 3.0D;
        }

        if (this.entityToAttack != null && this.entityToAttack.isDead)
        {
            this.entityToAttack = null;
        }

        if (this.shouldExplode && !this.worldObj.isRemote)
        {
            this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, 0.1F, false);
            this.setEntityHealth(0.0F);
            this.setDead();
        }
    }

    public boolean attackEntityFrom(DamageSource ds, int i)
    {
        boolean flag = super.attackEntityFrom(ds, i);

        if (flag && ds.getEntity() instanceof EntityLiving)
        {
            this.setAwake(true);
            this.lostyou = 0;
            this.entityToAttack = ds.getEntity();
        }

        return flag;
    }

    public void setArrowHeading(double d, double d1, double d2, float f, float f1)
    {
        float f2 = MathHelper.sqrt_double(d * d + d1 * d1 + d2 * d2);
        d /= (double)f2;
        d1 /= (double)f2;
        d2 /= (double)f2;
        d += this.rand.nextGaussian() * 0.007499999832361937D * (double)f1;
        d1 += this.rand.nextGaussian() * 0.007499999832361937D * (double)f1;
        d2 += this.rand.nextGaussian() * 0.007499999832361937D * (double)f1;
        d *= (double)f;
        d1 *= (double)f;
        d2 *= (double)f;
        this.motionX = d;
        this.motionY = d1;
        this.motionZ = d2;
        float f3 = MathHelper.sqrt_double(d * d + d2 * d2);
        this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(d, d2) * 180.0D / Math.PI);
        this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(d1, (double)f3) * 180.0D / Math.PI);
    }

    public void shutdown()
    {
        this.counter = -64;
        this.setAwake(false);
        this.entityToAttack = null;
        this.setPathToEntity((PathEntity)null);
        this.moveStrafing = 0.0F;
        this.moveForward = 0.0F;
        this.isJumping = false;
        this.motionX = 0.0D;
        this.motionZ = 0.0D;
    }

    /**
     * Applies a velocity to each of the entities pushing them away from each other. Args: entity
     */
    public void applyEntityCollision(Entity entity)
    {
        if (!this.worldObj.isRemote && !this.isDead && this.entityToAttack != null && entity != null && this.entityToAttack == entity)
        {
            if (entity instanceof EntityPlayer && !((EntityPlayer)entity).capabilities.isCreativeMode)
            {
                ;
            }

            if (entity instanceof EntitySlider)
            {
                return;
            }

            this.shouldExplode = true;
            entity.attackEntityFrom(DamageSource.causeMobDamage(this), 2.0F);

            if (entity instanceof EntityLivingBase)
            {
                EntityLivingBase f = (EntityLivingBase)entity;
                double i = f.posX - this.posX;
                double d2;

                for (d2 = f.posZ - this.posZ; i * i + d2 * d2 < 1.0E-4D; d2 = (Math.random() - Math.random()) * 0.01D)
                {
                    i = (Math.random() - Math.random()) * 0.01D;
                }

                if (entity instanceof EntityPlayerMP)
                {
                    f.knockBack(this, 5.0F, -i, -d2);
                    f.addVelocity(4.0D, 0.0D, 0.0D);
                    f.addVelocity(0.0D, 4.0D, 0.0D);
                    f.addVelocity(0.0D, 0.0D, 4.0D);
                }
                else
                {
                    f.knockBack(this, 5.0F, -i, -d2);
                    f.addVelocity(4.0D, 0.0D, 0.0D);
                    f.addVelocity(0.0D, 4.0D, 0.0D);
                    f.addVelocity(0.0D, 0.0D, 4.0D);
                }
            }

            float var11 = 0.01745329F;

            for (int var12 = 0; var12 < 40; ++var12)
            {
                double d1 = (double)((float)this.posX + this.rand.nextFloat() * 0.25F);
                double d3 = (double)((float)this.posY + 0.5F);
                double d4 = (double)((float)this.posZ + this.rand.nextFloat() * 0.25F);
                float f1 = this.rand.nextFloat() * 360.0F;
                this.worldObj.spawnParticle("explode", d1, d3, d4, -Math.sin((double)(var11 * f1)) * 0.75D, 0.125D, Math.cos((double)(var11 * f1)) * 0.75D);
            }
        }
    }

    protected void updateEntityActionState()
    {
        EntityPlayer entityplayer = this.worldObj.getClosestPlayerToEntity(this, 8.0D);

        if (!this.getAwake() && this.counter >= 8)
        {
            if (entityplayer != null && this.canEntityBeSeen(entityplayer) && !entityplayer.capabilities.isCreativeMode)
            {
                this.faceEntity(entityplayer, 10.0F, 10.0F);
                this.entityToAttack = entityplayer;
                this.setAwake(true);
                this.lostyou = 0;
            }

            this.counter = 0;
        }
        else if (this.getAwake() && this.counter >= 8)
        {
            if (this.entityToAttack == null)
            {
                if (entityplayer != null && this.canEntityBeSeen(entityplayer))
                {
                    this.entityToAttack = entityplayer;
                    this.setAwake(true);
                    this.lostyou = 0;
                }
                else
                {
                    ++this.lostyou;

                    if (this.lostyou >= 4)
                    {
                        this.shutdown();
                    }
                }
            }
            else if (this.canEntityBeSeen(this.entityToAttack) && this.getDistanceToEntity(this.entityToAttack) < 16.0F)
            {
                this.lostyou = 0;
            }
            else
            {
                ++this.lostyou;

                if (this.lostyou >= 4)
                {
                    this.shutdown();
                }
            }

            this.counter = 0;
        }
        else
        {
            ++this.counter;
        }

        if (this.getAwake())
        {
            if (this.entityToAttack != null)
            {
                this.faceEntity(this.entityToAttack, 10.0F, 10.0F);
            }

            if (this.onGround && this.jcount-- <= 0)
            {
                this.jcount = this.rand.nextInt(20) + 10;
                this.isJumping = true;
                this.moveStrafing = 0.5F - this.rand.nextFloat();
                this.moveForward = 1.0F;
                this.worldObj.playSoundAtEntity(this, "mob.slime.small", this.getSoundVolume(), ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F) * 0.8F);

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
                    this.moveStrafing = this.moveForward = 0.0F;
                }
            }
        }
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected String getHurtSound()
    {
        return "mob.slime.small";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected String getDeathSound()
    {
        return "mob.slime.small";
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    public boolean getCanSpawnHere()
    {
        return super.getCanSpawnHere();
    }

    /**
     * Returns the volume for the sounds this mob makes.
     */
    protected float getSoundVolume()
    {
        return 0.6F;
    }

    /**
     * Returns the item ID for the item the mob drops on death.
     */
    protected int getDropItemId()
    {
        return this.rand.nextInt(5) == 0 ? AetherBlocks.LightDungeonStone.blockID : AetherBlocks.DungeonStone.blockID;
    }

    public void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(16, Byte.valueOf((byte)0));
    }

    public boolean getAwake()
    {
        return (this.dataWatcher.getWatchableObjectByte(16) & 1) != 0;
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
