package net.aetherteam.aether.entities;

import cpw.mods.fml.common.registry.IThrowableEntity;
import java.util.List;
import net.aetherteam.aether.items.AetherItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntityZephyrSnowball extends Entity implements IThrowableEntity
{
    private int xTile = -1;
    private int yTile = -1;
    private int zTile = -1;
    private int inTile = 0;
    protected boolean inGround = false;
    public int field_9406_a = 0;
    protected EntityLiving shootingEntity;
    private int ticksAlive;
    private int ticksInAir = 0;
    public double accelerationX;
    public double accelerationY;
    public double accelerationZ;

    public EntityZephyrSnowball(World world)
    {
        super(world);
        this.setSize(1.0F, 1.0F);
    }

    protected void entityInit() {}

    public EntityZephyrSnowball(World world, EntityLiving entityliving, double d, double d1, double d2)
    {
        super(world);
        this.shootingEntity = entityliving;
        this.setSize(0.5F, 0.5F);
        this.setLocationAndAngles(entityliving.posX, entityliving.posY, entityliving.posZ, entityliving.rotationYaw, entityliving.rotationPitch);
        this.setPositionAndRotation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
        this.yOffset = 0.0F;
        this.motionX = this.motionY = this.motionZ = 0.0D;
        d += this.rand.nextGaussian() * 0.4D;
        d1 += this.rand.nextGaussian() * 0.4D;
        d2 += this.rand.nextGaussian() * 0.4D;
        double d3 = (double)MathHelper.sqrt_double(d * d + d1 * d1 + d2 * d2);
        this.accelerationX = d / d3 * 0.08000000000000002D;
        this.accelerationY = d1 / d3 * 0.08000000000000002D;
        this.accelerationZ = d2 / d3 * 0.08000000000000002D;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();

        if (this.motionX * this.motionY * this.motionZ < 0.1D && this.ticksInAir > 40 && !this.worldObj.isRemote)
        {
            this.setDead();
        }

        if (this.ticksInAir > 200 && !this.worldObj.isRemote)
        {
            this.setDead();
        }

        if (this.field_9406_a > 0)
        {
            --this.field_9406_a;
        }

        if (this.inGround)
        {
            int vec3d = this.worldObj.getBlockId(this.xTile, this.yTile, this.zTile);

            if (vec3d == this.inTile)
            {
                ++this.ticksAlive;
                return;
            }

            this.inGround = false;
            this.motionX *= (double)(this.rand.nextFloat() * 0.2F);
            this.motionY *= (double)(this.rand.nextFloat() * 0.2F);
            this.motionZ *= (double)(this.rand.nextFloat() * 0.2F);
            this.ticksAlive = 0;
            this.ticksInAir = 0;
        }
        else
        {
            ++this.ticksInAir;
        }

        Vec3 var15 = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
        Vec3 vec3d1 = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
        MovingObjectPosition movingobjectposition = this.worldObj.clip(var15, vec3d1);
        var15 = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
        vec3d1 = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

        if (movingobjectposition != null)
        {
            vec3d1 = Vec3.createVectorHelper(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord);
        }

        Entity entity = null;
        List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
        double d = 0.0D;

        for (int f = 0; f < list.size(); ++f)
        {
            Entity f1 = (Entity)list.get(f);

            if (f1.canBeCollidedWith() && (f1 != this.shootingEntity || this.ticksInAir >= 25))
            {
                float k = 0.3F;
                AxisAlignedBB f3 = f1.boundingBox.expand((double)k, (double)k, (double)k);
                MovingObjectPosition movingobjectposition1 = f3.calculateIntercept(var15, vec3d1);

                if (movingobjectposition1 != null)
                {
                    double d1 = var15.distanceTo(movingobjectposition1.hitVec);

                    if (d1 < d || d == 0.0D)
                    {
                        entity = f1;
                        d = d1;
                    }
                }
            }
        }

        if (entity != null)
        {
            movingobjectposition = new MovingObjectPosition(entity);
        }

        if (movingobjectposition != null)
        {
            if (movingobjectposition.entityHit != null && movingobjectposition.entityHit != this.shootingEntity)
            {
                if (movingobjectposition.entityHit instanceof EntityPlayer && ((EntityPlayer)movingobjectposition.entityHit).inventory.armorInventory[0] != null && ((EntityPlayer)movingobjectposition.entityHit).inventory.armorInventory[0].itemID == AetherItems.SentryBoots.itemID)
                {
                    this.setDead();
                }
                else if (movingobjectposition.entityHit instanceof EntityPlayer && ((EntityPlayer)movingobjectposition.entityHit).capabilities.isCreativeMode)
                {
                    this.setDead();
                }
                else if (movingobjectposition.entityHit != null && !(movingobjectposition.entityHit instanceof EntityNewZephyr) && !(movingobjectposition.entityHit instanceof EntityTempest))
                {
                    movingobjectposition.entityHit.motionX += this.motionX;
                    movingobjectposition.entityHit.motionY += 0.5D;
                    movingobjectposition.entityHit.motionZ += this.motionZ;
                }
            }

            if (!this.worldObj.isRemote)
            {
                this.setDead();
            }
        }

        this.posX += this.motionX;
        this.posY += this.motionY;
        this.posZ += this.motionZ;
        float var16 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
        this.rotationYaw = (float)(Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);

        for (this.rotationPitch = (float)(Math.atan2(this.motionY, (double)var16) * 180.0D / Math.PI); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F)
        {
            ;
        }

        while (this.rotationPitch - this.prevRotationPitch >= 180.0F)
        {
            this.prevRotationPitch += 360.0F;
        }

        while (this.rotationYaw - this.prevRotationYaw < -180.0F)
        {
            this.prevRotationYaw -= 360.0F;
        }

        while (this.rotationYaw - this.prevRotationYaw >= 180.0F)
        {
            this.prevRotationYaw += 360.0F;
        }

        this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
        this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
        float var17 = 0.95F;

        if (this.handleWaterMovement())
        {
            for (int var19 = 0; var19 < 4; ++var19)
            {
                float var18 = 0.25F;
                this.worldObj.spawnParticle("bubble", this.posX - this.motionX * (double)var18, this.posY - this.motionY * (double)var18, this.posZ - this.motionZ * (double)var18, this.motionX, this.motionY, this.motionZ);
            }

            var17 = 0.8F;
        }

        this.motionX += this.accelerationX;
        this.motionY += this.accelerationY;
        this.motionZ += this.accelerationZ;
        this.motionX *= (double)var17;
        this.motionY *= (double)var17;
        this.motionZ *= (double)var17;
        this.worldObj.spawnParticle("smoke", this.posX, this.posY + 0.5D, this.posZ, 0.0D, 0.0D, 0.0D);
        this.setPositionAndRotation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        nbttagcompound.setShort("xTile", (short)this.xTile);
        nbttagcompound.setShort("yTile", (short)this.yTile);
        nbttagcompound.setShort("zTile", (short)this.zTile);
        nbttagcompound.setByte("inTile", (byte)this.inTile);
        nbttagcompound.setByte("shake", (byte)this.field_9406_a);
        nbttagcompound.setByte("inGround", (byte)(this.inGround ? 1 : 0));
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        this.xTile = nbttagcompound.getShort("xTile");
        this.yTile = nbttagcompound.getShort("yTile");
        this.zTile = nbttagcompound.getShort("zTile");
        this.inTile = nbttagcompound.getByte("inTile") & 255;
        this.field_9406_a = nbttagcompound.getByte("shake") & 255;
        this.inGround = nbttagcompound.getByte("inGround") == 1;
    }

    public float getCollisionBorderSize()
    {
        return 1.0F;
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource damagesource, float i)
    {
        this.setBeenAttacked();

        if (damagesource.getEntity() == null)
        {
            return false;
        }
        else
        {
            Vec3 vec3d = damagesource.getEntity().getLookVec();

            if (vec3d != null)
            {
                this.motionX = vec3d.xCoord;
                this.motionY = vec3d.yCoord;
                this.motionZ = vec3d.zCoord;
                this.accelerationX = this.motionX * 0.1D;
                this.accelerationY = this.motionY * 0.1D;
                this.accelerationZ = this.motionZ * 0.1D;

                for (int j = 0; j < 10; ++j)
                {
                    double d = this.rand.nextGaussian() * 0.02D;
                    double d1 = this.rand.nextGaussian() * 0.02D;
                    double d2 = this.rand.nextGaussian() * 0.02D;
                    double d3 = 5.0D;
                    this.shootingEntity.worldObj.spawnParticle("flame", this.shootingEntity.posX + (double)(this.rand.nextFloat() * this.shootingEntity.width * 2.0F) - (double)this.shootingEntity.width - d * d3, this.shootingEntity.posY + (double)(this.rand.nextFloat() * (this.shootingEntity.height - 0.6F)) - d1 * d3, this.shootingEntity.posZ + (double)(this.rand.nextFloat() * this.shootingEntity.width * 2.0F) - (double)this.shootingEntity.width - d2 * d3, d, d1, d2);
                    this.shootingEntity.worldObj.spawnParticle("largeexplode", this.shootingEntity.posX + (double)(this.rand.nextFloat() * this.shootingEntity.width * 2.0F) - (double)this.shootingEntity.width - d * d3, this.shootingEntity.posY + (double)(this.rand.nextFloat() * (this.shootingEntity.height - 0.6F)) - d1 * d3, this.shootingEntity.posZ + (double)(this.rand.nextFloat() * this.shootingEntity.width * 2.0F) - (double)this.shootingEntity.width - d2 * d3, d, d1, d2);
                }
            }

            return true;
        }
    }

    public float getShadowSize()
    {
        return 0.0F;
    }

    public Entity getThrower()
    {
        return null;
    }

    public void setThrower(Entity entity) {}
}
