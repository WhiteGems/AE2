package net.aetherteam.aether.entities;

import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntityLightningKnife extends Entity
{
    private int xTileSnowball = -1;
    private int yTileSnowball = -1;
    private int zTileSnowball = -1;
    private int inTileSnowball = 0;
    private boolean inGroundSnowball = false;
    public int shakeSnowball = 0;
    private EntityLivingBase thrower;
    private int ticksInGroundSnowball;
    private int ticksInAirSnowball = 0;

    public EntityLightningKnife(World world)
    {
        super(world);
        this.setSize(0.25F, 0.25F);
    }

    protected void entityInit() {}

    /**
     * Checks if the entity is in range to render by using the past in distance and comparing it to its average edge
     * length * 64 * renderDistanceWeight Args: distance
     */
    public boolean isInRangeToRenderDist(double d)
    {
        double d1 = this.boundingBox.getAverageEdgeLength() * 4.0D;
        d1 *= 64.0D;
        return d < d1 * d1;
    }

    public EntityLightningKnife(World world, EntityLivingBase entityliving)
    {
        super(world);
        this.thrower = entityliving;
        this.setSize(0.25F, 0.25F);
        this.setLocationAndAngles(entityliving.posX, entityliving.posY + (double)entityliving.getEyeHeight(), entityliving.posZ, entityliving.rotationYaw, entityliving.rotationPitch);
        this.posX -= (double)(MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * 0.16F);
        this.posY -= 0.10000000149011612D;
        this.posZ -= (double)(MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * 0.16F);
        this.setPositionAndRotation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
        this.yOffset = 0.0F;
        float f = 0.4F;
        this.motionX = (double)(-MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI) * f);
        this.motionZ = (double)(MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI) * f);
        this.motionY = (double)(-MathHelper.sin(this.rotationPitch / 180.0F * (float)Math.PI) * f);
        this.setSnowballHeading(this.motionX, this.motionY, this.motionZ, 1.5F, 1.0F);
    }

    public EntityLightningKnife(World world, double d, double d1, double d2)
    {
        super(world);
        this.ticksInGroundSnowball = 0;
        this.setSize(0.25F, 0.25F);
        this.setPositionAndRotation(d, d1, d2, this.rotationYaw, this.rotationPitch);
        this.yOffset = 0.0F;
    }

    public void setSnowballHeading(double d, double d1, double d2, float f, float f1)
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
        this.ticksInGroundSnowball = 0;
    }

    /**
     * Sets the velocity to the args. Args: x, y, z
     */
    public void setVelocity(double d, double d1, double d2)
    {
        this.motionX = d;
        this.motionY = d1;
        this.motionZ = d2;

        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F)
        {
            float f = MathHelper.sqrt_double(d * d + d2 * d2);
            this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(d, d2) * 180.0D / Math.PI);
            this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(d1, (double)f) * 180.0D / Math.PI);
        }
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        this.lastTickPosX = this.posX;
        this.lastTickPosY = this.posY;
        this.lastTickPosZ = this.posZ;
        super.onUpdate();

        if (this.shakeSnowball > 0)
        {
            --this.shakeSnowball;
        }

        if (this.inGroundSnowball)
        {
            int vec3d = this.worldObj.getBlockId(this.xTileSnowball, this.yTileSnowball, this.zTileSnowball);

            if (vec3d == this.inTileSnowball)
            {
                ++this.ticksInGroundSnowball;

                if (this.ticksInGroundSnowball == 1200)
                {
                    this.setDead();
                }

                return;
            }

            this.inGroundSnowball = false;
            this.motionX *= (double)(this.rand.nextFloat() * 0.2F);
            this.motionY *= (double)(this.rand.nextFloat() * 0.2F);
            this.motionZ *= (double)(this.rand.nextFloat() * 0.2F);
            this.ticksInGroundSnowball = 0;
            this.ticksInAirSnowball = 0;
        }
        else
        {
            ++this.ticksInAirSnowball;
        }

        Vec3 var15 = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
        Vec3 vec3d1 = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
        MovingObjectPosition movingobjectposition = this.worldObj.rayTraceBlocks_do_do(var15, vec3d1, false, true);
        var15 = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
        vec3d1 = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

        if (movingobjectposition != null)
        {
            vec3d1 = Vec3.createVectorHelper(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord);
        }

        if (!this.worldObj.isRemote)
        {
            Entity f = null;
            List f1 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
            double f2 = 0.0D;

            for (int f3 = 0; f3 < f1.size(); ++f3)
            {
                Entity entity1 = (Entity)f1.get(f3);

                if (entity1.canBeCollidedWith() && (entity1 != this.thrower || this.ticksInAirSnowball >= 5))
                {
                    float f4 = 0.3F;
                    AxisAlignedBB axisalignedbb = entity1.boundingBox.expand((double)f4, (double)f4, (double)f4);
                    MovingObjectPosition movingobjectposition1 = axisalignedbb.calculateIntercept(var15, vec3d1);

                    if (movingobjectposition1 != null)
                    {
                        double d1 = var15.distanceTo(movingobjectposition1.hitVec);

                        if (d1 < f2 || f2 == 0.0D)
                        {
                            f = entity1;
                            f2 = d1;
                        }
                    }
                }
            }

            if (f != null)
            {
                movingobjectposition = new MovingObjectPosition(f);
            }
        }

        if (movingobjectposition != null)
        {
            EntityLightningBolt k;
            int var17;
            int var16;
            int var20;

            if (movingobjectposition.entityHit != null)
            {
                if (!movingobjectposition.entityHit.attackEntityFrom(DamageSource.causeMobDamage(this.thrower), 0.0F))
                {
                    ;
                }

                var16 = MathHelper.floor_double(movingobjectposition.entityHit.boundingBox.minX);
                var17 = MathHelper.floor_double(movingobjectposition.entityHit.boundingBox.minY);
                var20 = MathHelper.floor_double(movingobjectposition.entityHit.boundingBox.minZ);
                k = new EntityLightningBolt(this.worldObj, (double)var16, (double)var17, (double)var20);
                k.setLocationAndAngles((double)var16, (double)var17, (double)var20, this.rotationYaw, 0.0F);
                this.worldObj.spawnEntityInWorld(k);
            }
            else
            {
                var16 = MathHelper.floor_double(this.posX);
                var17 = MathHelper.floor_double(this.posY);
                var20 = MathHelper.floor_double(this.posZ);
                k = new EntityLightningBolt(this.worldObj, this.posX, this.posY, this.posZ);
                k.setLocationAndAngles((double)var16, (double)var17, (double)var20, this.rotationYaw, 0.0F);
                this.worldObj.spawnEntityInWorld(k);
            }

            for (var16 = 0; var16 < 8; ++var16)
            {
                this.worldObj.spawnParticle("largesmoke", this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);
            }

            this.setDead();
        }

        this.posX += this.motionX;
        this.posY += this.motionY;
        this.posZ += this.motionZ;
        float var18 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
        this.rotationYaw = (float)(Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);

        for (this.rotationPitch = (float)(Math.atan2(this.motionY, (double)var18) * 180.0D / Math.PI); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F)
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
        float var19 = 0.99F;
        float var21 = 0.03F;

        if (this.isInWater())
        {
            for (int var22 = 0; var22 < 4; ++var22)
            {
                float var23 = 0.25F;
                this.worldObj.spawnParticle("bubble", this.posX - this.motionX * (double)var23, this.posY - this.motionY * (double)var23, this.posZ - this.motionZ * (double)var23, this.motionX, this.motionY, this.motionZ);
            }

            var19 = 0.8F;
        }

        this.motionX *= (double)var19;
        this.motionY *= (double)var19;
        this.motionZ *= (double)var19;
        this.motionY -= (double)var21;
        this.setPositionAndRotation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        nbttagcompound.setShort("xTile", (short)this.xTileSnowball);
        nbttagcompound.setShort("yTile", (short)this.yTileSnowball);
        nbttagcompound.setShort("zTile", (short)this.zTileSnowball);
        nbttagcompound.setByte("inTile", (byte)this.inTileSnowball);
        nbttagcompound.setByte("shake", (byte)this.shakeSnowball);
        nbttagcompound.setByte("inGround", (byte)(this.inGroundSnowball ? 1 : 0));
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        this.xTileSnowball = nbttagcompound.getShort("xTile");
        this.yTileSnowball = nbttagcompound.getShort("yTile");
        this.zTileSnowball = nbttagcompound.getShort("zTile");
        this.inTileSnowball = nbttagcompound.getByte("inTile") & 255;
        this.shakeSnowball = nbttagcompound.getByte("shake") & 255;
        this.inGroundSnowball = nbttagcompound.getByte("inGround") == 1;
    }

    /**
     * Called by a player entity when they collide with an entity
     */
    public void onCollideWithPlayer(EntityPlayer entityplayer)
    {
        if (this.inGroundSnowball && this.thrower == entityplayer && this.shakeSnowball <= 0 && entityplayer.inventory.addItemStackToInventory(new ItemStack(Item.arrow, 1)))
        {
            this.worldObj.playSoundAtEntity(this, "random.pop", 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
            entityplayer.onItemPickup(this, 1);
            this.setDead();
        }
    }

    public float getShadowSize()
    {
        return 0.0F;
    }
}
