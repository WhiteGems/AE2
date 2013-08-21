package net.aetherteam.aether.entities;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public abstract class EntityProjectileBase extends Entity
{
    public float speed;
    public float slowdown;
    public float curvature;
    public float precision;
    public float hitBox;
    public int dmg;
    public ItemStack item;
    public int ttlInGround;
    public int xTile;
    public int yTile;
    public int zTile;
    public int inTile;
    public int inData;
    public boolean inGround;
    public int arrowShake;
    public EntityLivingBase shooter;
    public int ticksInGround;
    public int ticksFlying;
    public boolean shotByPlayer;
    public int canBePickedUp;

    public EntityProjectileBase(World world)
    {
        super(world);
        this.xTile = -1;
        this.yTile = -1;
        this.zTile = -1;
    }

    public EntityProjectileBase(World world, double d, double d1, double d2)
    {
        this(world);
        this.setPositionAndRotation(d, d1 - 1.0D, d2, this.rotationYaw, this.rotationPitch);
    }

    public EntityProjectileBase(World world, EntityLivingBase entityliving)
    {
        this(world);
        this.shooter = entityliving;
        this.shotByPlayer = entityliving instanceof EntityPlayer;
        this.setLocationAndAngles(entityliving.posX, entityliving.posY + (double)entityliving.getEyeHeight(), entityliving.posZ, entityliving.rotationYaw, entityliving.rotationPitch);
        this.posX -= (double)(MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * 0.16F);
        this.posY -= 0.10000000149011612D;
        this.posZ -= (double)(MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * 0.16F);
        this.setPositionAndRotation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
        this.motionX = (double)(-MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI));
        this.motionZ = (double)(MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI));
        this.motionY = (double)(-MathHelper.sin(this.rotationPitch / 180.0F * (float)Math.PI));
        this.setArrowHeading(this.motionX, this.motionY, this.motionZ, this.speed, this.precision);
    }

    protected void entityInit()
    {
        this.xTile = -1;
        this.yTile = -1;
        this.zTile = -1;
        this.inTile = 0;
        this.inGround = false;
        this.arrowShake = 0;
        this.ticksFlying = 0;
        this.setSize(0.5F, 0.5F);
        this.yOffset = 0.0F;
        this.hitBox = 0.3F;
        this.speed = 1.0F;
        this.slowdown = 0.99F;
        this.curvature = 0.03F;
        this.dmg = 4;
        this.precision = 1.0F;
        this.ttlInGround = 1200;
        this.item = null;
    }

    /**
     * Will get destroyed next tick.
     */
    public void setDead()
    {
        this.shooter = null;
        super.setDead();
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
        this.ticksInGround = 0;
    }

    @SideOnly(Side.CLIENT)

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
            this.prevRotationYaw = this.rotationYaw;
            this.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
            this.ticksInGround = 0;
        }
    }

    /**
     * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
     * prevent them from trampling crops
     */
    protected boolean canTriggerWalking()
    {
        return false;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();

        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F)
        {
            float var16 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);
            this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(this.motionY, (double)var16) * 180.0D / Math.PI);
        }

        int var161 = this.worldObj.getBlockId(this.xTile, this.yTile, this.zTile);

        if (var161 > 0)
        {
            Block.blocksList[var161].setBlockBoundsBasedOnState(this.worldObj, this.xTile, this.yTile, this.zTile);
            AxisAlignedBB var17 = Block.blocksList[var161].getCollisionBoundingBoxFromPool(this.worldObj, this.xTile, this.yTile, this.zTile);

            if (var17 != null && var17.isVecInside(this.worldObj.getWorldVec3Pool().getVecFromPool(this.posX, this.posY, this.posZ)))
            {
                this.inGround = true;
            }
        }

        if (this.arrowShake > 0)
        {
            --this.arrowShake;
        }

        if (this.inGround)
        {
            int var171 = this.worldObj.getBlockId(this.xTile, this.yTile, this.zTile);
            int var3 = this.worldObj.getBlockMetadata(this.xTile, this.yTile, this.zTile);

            if (var171 == this.inTile && var3 == this.inData)
            {
                ++this.ticksInGround;

                if (this.ticksInGround == 1200 && !this.worldObj.isRemote)
                {
                    this.setDead();
                }
            }
            else
            {
                this.inGround = false;
                this.motionX *= (double)(this.rand.nextFloat() * 0.2F);
                this.motionY *= (double)(this.rand.nextFloat() * 0.2F);
                this.motionZ *= (double)(this.rand.nextFloat() * 0.2F);
                this.ticksInGround = 0;
                this.ticksFlying = 0;
            }
        }
        else
        {
            ++this.ticksFlying;
            Vec3 var18 = this.worldObj.getWorldVec3Pool().getVecFromPool(this.posX, this.posY, this.posZ);
            Vec3 var19 = this.worldObj.getWorldVec3Pool().getVecFromPool(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
            MovingObjectPosition var4 = this.worldObj.rayTraceBlocks_do_do(var18, var19, false, true);
            var18 = this.worldObj.getWorldVec3Pool().getVecFromPool(this.posX, this.posY, this.posZ);
            var19 = this.worldObj.getWorldVec3Pool().getVecFromPool(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

            if (var4 != null)
            {
                var19 = this.worldObj.getWorldVec3Pool().getVecFromPool(var4.hitVec.xCoord, var4.hitVec.yCoord, var4.hitVec.zCoord);
            }

            Entity var5 = null;
            List var6 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
            double var7 = 0.0D;
            Iterator var9 = var6.iterator();
            float var11;
            MovingObjectPosition var23;

            while (var9.hasNext())
            {
                Entity var20 = (Entity)var9.next();

                if (var20.canBeCollidedWith() && (var20 != this.shooter || this.ticksFlying >= 5))
                {
                    var11 = 0.3F;
                    AxisAlignedBB f2 = var20.boundingBox.expand((double)var11, (double)var11, (double)var11);
                    var23 = f2.calculateIntercept(var18, var19);

                    if (var23 != null)
                    {
                        double var26 = var18.distanceTo(var23.hitVec);

                        if (var26 < var7 || var7 == 0.0D)
                        {
                            var5 = var20;
                            var7 = var26;
                        }
                    }
                }
            }

            if (var5 != null)
            {
                var4 = new MovingObjectPosition(var5);
            }

            if (var4 != null)
            {
                float var201;

                if (var4.entityHit != null && this.onHitTarget(var4.entityHit))
                {
                    if (this.shooter != null && var4.entityHit == this.shooter)
                    {
                        return;
                    }

                    var201 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
                    int var21 = MathHelper.ceiling_double_int((double)var201 * (double)this.dmg);
                    var23 = null;
                    DamageSource var231;

                    if (this.shooter == null)
                    {
                        var231 = (new CustomDamageSource("dart", this, this)).setDeathMessage(" died covered in darts.").setProjectile();
                    }
                    else
                    {
                        var231 = (new CustomDamageSource("dart", this, this.shooter)).setDeathMessage(" died covered in " + this.shooter.getEntityName() + "\'s darts.").setProjectile();
                    }

                    if (this.isBurning())
                    {
                        var4.entityHit.setFire(5);
                    }

                    if (var4.entityHit.attackEntityFrom(var231, (float)var21))
                    {
                        this.worldObj.playSoundAtEntity(this, "random.bowhit", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));

                        if (!this.worldObj.isRemote)
                        {
                            this.setDead();
                        }
                    }
                }
                else if (this.onHitBlock(var4))
                {
                    this.xTile = var4.blockX;
                    this.yTile = var4.blockY;
                    this.zTile = var4.blockZ;
                    this.inTile = this.worldObj.getBlockId(this.xTile, this.yTile, this.zTile);
                    this.inData = this.worldObj.getBlockMetadata(this.xTile, this.yTile, this.zTile);
                    this.motionX = (double)((float)(var4.hitVec.xCoord - this.posX));
                    this.motionY = (double)((float)(var4.hitVec.yCoord - this.posY));
                    this.motionZ = (double)((float)(var4.hitVec.zCoord - this.posZ));
                    var201 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
                    this.posX -= this.motionX / (double)var201 * 0.05000000074505806D;
                    this.posY -= this.motionY / (double)var201 * 0.05000000074505806D;
                    this.posZ -= this.motionZ / (double)var201 * 0.05000000074505806D;
                    this.worldObj.playSoundAtEntity(this, "aether:random.bowhit", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
                    this.inGround = true;
                    this.arrowShake = 7;
                }
            }

            this.handleMotionUpdate();
            float var22 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.rotationYaw = (float)(Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);

            for (this.rotationPitch = (float)(Math.atan2(this.motionY, (double)var22) * 180.0D / Math.PI); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F)
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
            float var25 = 0.99F;
            var11 = 0.05F;

            if (this.isInWater())
            {
                for (int var24 = 0; var24 < 4; ++var24)
                {
                    float var27 = 0.25F;
                    this.worldObj.spawnParticle("bubble", this.posX - this.motionX * (double)var27, this.posY - this.motionY * (double)var27, this.posZ - this.motionZ * (double)var27, this.motionX, this.motionY, this.motionZ);
                }

                var25 = 0.8F;
            }

            this.posX += this.motionX;
            this.posY += this.motionY;
            this.posZ += this.motionZ;
            this.handleMotionUpdate();
            this.setPositionAndRotation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
            this.doBlockCollisions();
        }
    }

    public void handleMotionUpdate()
    {
        float slow = this.slowdown;

        if (this.handleWaterMovement())
        {
            for (int k = 0; k < 4; ++k)
            {
                float f6 = 0.25F;
                this.worldObj.spawnParticle("bubble", this.posX - this.motionX * (double)f6, this.posY - this.motionY * (double)f6, this.posZ - this.motionZ * (double)f6, this.motionX, this.motionY, this.motionZ);
            }

            slow *= 0.8F;
        }

        this.motionX *= (double)slow;
        this.motionY *= (double)slow;
        this.motionZ *= (double)slow;
        this.motionY -= (double)this.curvature;
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
        nbttagcompound.setByte("inData", (byte)this.inData);
        nbttagcompound.setByte("shake", (byte)this.arrowShake);
        nbttagcompound.setByte("inGround", (byte)(this.inGround ? 1 : 0));
        nbttagcompound.setBoolean("player", this.shotByPlayer);
        nbttagcompound.setByte("pickup", (byte)this.canBePickedUp);
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
        this.inData = nbttagcompound.getByte("inData") & 255;
        this.arrowShake = nbttagcompound.getByte("shake") & 255;
        this.inGround = nbttagcompound.getByte("inGround") == 1;
        this.shotByPlayer = nbttagcompound.getBoolean("player");
        this.canBePickedUp = nbttagcompound.getByte("pickup");
    }

    /**
     * Called by a player entity when they collide with an entity
     */
    public void onCollideWithPlayer(EntityPlayer par1EntityPlayer)
    {
        if (!this.worldObj.isRemote && this.inGround && this.arrowShake <= 0)
        {
            boolean var2 = this.canBePickedUp == 1 || this.canBePickedUp == 2 && par1EntityPlayer.capabilities.isCreativeMode;

            if (this.canBePickedUp == 1 && !par1EntityPlayer.inventory.addItemStackToInventory(this.item.copy()))
            {
                var2 = false;
            }

            if (var2)
            {
                this.worldObj.playSoundAtEntity(this, "random.pop", 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                par1EntityPlayer.onItemPickup(this, 1);
                this.setDead();
            }
        }
    }

    public boolean canBeShot(Entity ent)
    {
        return ent instanceof EntityLivingBase && (EntityLivingBase)ent == this.shooter ? false : ent.canBeCollidedWith() && (!(ent instanceof EntityLivingBase) || ((EntityLivingBase)ent).deathTime <= 0);
    }

    public boolean onHit()
    {
        return true;
    }

    public boolean onHitTarget(Entity target)
    {
        this.worldObj.playSoundAtEntity(this, "random.drr", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
        return true;
    }

    public void tickFlying() {}

    public void tickInGround() {}

    public boolean onHitBlock(MovingObjectPosition mop)
    {
        return this.onHitBlock();
    }

    public boolean onHitBlock()
    {
        this.worldObj.playSoundAtEntity(this, "random.drr", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
        return true;
    }

    public float getShadowSize()
    {
        return 0.0F;
    }
}
