package net.aetherteam.aether.entities;

import net.aetherteam.aether.blocks.AetherBlocks;
import net.aetherteam.aether.entities.bosses.EntitySentryGuardian;
import net.aetherteam.aether.entities.bosses.EntitySlider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
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
    public int size;
    public int counter;
    public int lostyou;
    private EntitySentryGuardian parent;

    public EntitySentry(World var1)
    {
        super(var1);
        this.texture = this.dir + "/mobs/sentry/sentry.png";
        this.size = 2;
        this.yOffset = 0.0F;
        this.moveSpeed = 1.0F;
        this.field_100021_a = 1.0F;
        this.field_100020_b = 1.0F;
        this.jcount = this.rand.nextInt(20) + 10;
        this.func_100019_e(this.size);
        this.worldObj = var1;
    }

    public EntitySentry(World var1, double var2, double var4, double var6)
    {
        super(var1);
        this.texture = this.dir + "/mobs/sentry/sentry.png";
        this.size = 2;
        this.yOffset = 0.0F;
        this.moveSpeed = 1.0F;
        this.field_100021_a = 1.0F;
        this.field_100020_b = 1.0F;
        this.jcount = this.rand.nextInt(20) + 10;
        this.func_100019_e(this.size);
        this.setPosition(var2, var4, var6);
        this.worldObj = var1;
    }

    /**
     * Called when the mob's health reaches 0.
     */
    public void onDeath(DamageSource var1)
    {
        if (this.parent != null)
        {
            this.parent.failedYou();
        }

        super.onDeath(var1);
    }

    public void func_100019_e(int var1)
    {
        this.setEntityHealth(10);
        this.width = 0.85F;
        this.height = 0.85F;
        this.setPosition(this.posX, this.posY, this.posZ);
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound var1)
    {
        super.writeEntityToNBT(var1);
        var1.setInteger("Size", this.size - 1);
        var1.setInteger("LostYou", this.lostyou);
        var1.setInteger("Counter", this.counter);
        var1.setBoolean("Awake", this.getAwake());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound var1)
    {
        super.readEntityFromNBT(var1);
        this.size = var1.getInteger("Size") + 1;
        this.lostyou = var1.getInteger("LostYou");
        this.counter = var1.getInteger("Counter");
        this.setAwake(var1.getBoolean("Awake"));
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        boolean var1 = this.onGround;
        super.onUpdate();

        if (this.onGround && !var1)
        {
            this.worldObj.playSoundAtEntity(this, "mob.slime.small", this.getSoundVolume(), ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F) / 0.8F);
        }
        else if (!this.onGround && var1 && this.entityToAttack != null)
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
            this.setEntityHealth(0);
            this.setDead();
        }
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource var1, int var2)
    {
        boolean var3 = super.attackEntityFrom(var1, var2);

        if (var3 && var1.getEntity() instanceof EntityLiving)
        {
            this.setAwake(true);
            this.lostyou = 0;
            this.entityToAttack = var1.getEntity();
            this.texture = this.dir + "/mobs/sentry/sentry_lit.png";
        }

        return var3;
    }

    public void setArrowHeading(double var1, double var3, double var5, float var7, float var8)
    {
        float var9 = MathHelper.sqrt_double(var1 * var1 + var3 * var3 + var5 * var5);
        var1 /= (double)var9;
        var3 /= (double)var9;
        var5 /= (double)var9;
        var1 += this.rand.nextGaussian() * 0.007499999832361937D * (double)var8;
        var3 += this.rand.nextGaussian() * 0.007499999832361937D * (double)var8;
        var5 += this.rand.nextGaussian() * 0.007499999832361937D * (double)var8;
        var1 *= (double)var7;
        var3 *= (double)var7;
        var5 *= (double)var7;
        this.motionX = var1;
        this.motionY = var3;
        this.motionZ = var5;
        float var10 = MathHelper.sqrt_double(var1 * var1 + var5 * var5);
        this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(var1, var5) * 180.0D / Math.PI);
        this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(var3, (double)var10) * 180.0D / Math.PI);
    }

    public void shutdown()
    {
        this.counter = -64;
        this.setAwake(false);
        this.entityToAttack = null;
        this.texture = this.dir + "/mobs/sentry/sentry.png";
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
    public void applyEntityCollision(Entity var1)
    {
        if (!this.worldObj.isRemote && !this.isDead && this.entityToAttack != null && var1 != null && this.entityToAttack == var1)
        {
            if (var1 instanceof EntityPlayer && !((EntityPlayer)var1).capabilities.isCreativeMode)
            {
                ;
            }

            if (var1 instanceof EntitySlider)
            {
                return;
            }

            this.shouldExplode = true;
            var1.attackEntityFrom(DamageSource.causeMobDamage(this), 2);

            if (var1 instanceof EntityLiving)
            {
                EntityLiving var2 = (EntityLiving)var1;
                double var3 = var2.posX - this.posX;
                double var5;

                for (var5 = var2.posZ - this.posZ; var3 * var3 + var5 * var5 < 1.0E-4D; var5 = (Math.random() - Math.random()) * 0.01D)
                {
                    var3 = (Math.random() - Math.random()) * 0.01D;
                }

                if (var1 instanceof EntityPlayerMP)
                {
                    var2.knockBack(this, 5, -var3, -var5);
                    var2.addVelocity(4.0D, 0.0D, 0.0D);
                    var2.addVelocity(0.0D, 4.0D, 0.0D);
                    var2.addVelocity(0.0D, 0.0D, 4.0D);
                }
                else
                {
                    var2.knockBack(this, 5, -var3, -var5);
                    var2.addVelocity(4.0D, 0.0D, 0.0D);
                    var2.addVelocity(0.0D, 4.0D, 0.0D);
                    var2.addVelocity(0.0D, 0.0D, 4.0D);
                }
            }

            float var11 = 0.01745329F;

            for (int var12 = 0; var12 < 40; ++var12)
            {
                double var4 = (double)((float)this.posX + this.rand.nextFloat() * 0.25F);
                double var6 = (double)((float)this.posY + 0.5F);
                double var8 = (double)((float)this.posZ + this.rand.nextFloat() * 0.25F);
                float var10 = this.rand.nextFloat() * 360.0F;
                this.worldObj.spawnParticle("explode", var4, var6, var8, -Math.sin((double)(var11 * var10)) * 0.75D, 0.125D, Math.cos((double)(var11 * var10)) * 0.75D);
            }
        }
    }

    protected void updateEntityActionState()
    {
        EntityPlayer var1 = this.worldObj.getClosestPlayerToEntity(this, 8.0D);

        if (!this.getAwake() && this.counter >= 8)
        {
            if (var1 != null && this.canEntityBeSeen(var1) && !var1.capabilities.isCreativeMode)
            {
                this.faceEntity(var1, 10.0F, 10.0F);
                this.entityToAttack = var1;
                this.setAwake(true);
                this.lostyou = 0;
                this.texture = this.dir + "/mobs/sentry/sentry_lit.png";
            }

            this.counter = 0;
        }
        else if (this.getAwake() && this.counter >= 8)
        {
            if (this.entityToAttack == null)
            {
                if (var1 != null && this.canEntityBeSeen(var1))
                {
                    this.entityToAttack = var1;
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

    public void setAwake(boolean var1)
    {
        if (var1)
        {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte)1));
        }
        else
        {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte)0));
        }
    }

    public void setParent(EntitySentryGuardian var1)
    {
        this.parent = var1;
    }
}
