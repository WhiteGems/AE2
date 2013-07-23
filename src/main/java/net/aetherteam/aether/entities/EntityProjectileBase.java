package net.aetherteam.aether.entities;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3Pool;
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
    public int xTile = -1;

    public int yTile = -1;

    public int zTile = -1;
    public int inTile;
    public int inData;
    public boolean inGround;
    public int arrowShake;
    public EntityLiving shooter;
    public int ticksInGround;
    public int ticksFlying;
    public boolean shotByPlayer;
    public int canBePickedUp;

    public EntityProjectileBase(World world)
    {
        super(world);
    }

    public EntityProjectileBase(World world, double d, double d1, double d2)
    {
        this(world);
        setPositionAndRotation(d, d1 - 1.0D, d2, this.rotationYaw, this.rotationPitch);
    }

    public EntityProjectileBase(World world, EntityLiving entityliving)
    {
        this(world);
        this.shooter = entityliving;
        this.shotByPlayer = (entityliving instanceof EntityPlayer);
        setLocationAndAngles(entityliving.posX, entityliving.posY + entityliving.getEyeHeight(), entityliving.posZ, entityliving.rotationYaw, entityliving.rotationPitch);
        this.posX -= MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * 0.16F;
        this.posY -= 0.1000000014901161D;
        this.posZ -= MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * 0.16F;
        setPositionAndRotation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
        this.motionX = (-MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI));
        this.motionZ = (MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI));
        this.motionY = (-MathHelper.sin(this.rotationPitch / 180.0F * (float)Math.PI));
        setArrowHeading(this.motionX, this.motionY, this.motionZ, this.speed, this.precision);
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
        setSize(0.5F, 0.5F);
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

    public void setDead()
    {
        this.shooter = null;
        super.setDead();
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
        this.ticksInGround = 0;
    }

    @SideOnly(Side.CLIENT)
    public void setVelocity(double d, double d1, double d2)
    {
        this.motionX = d;
        this.motionY = d1;
        this.motionZ = d2;

        if ((this.prevRotationPitch == 0.0F) && (this.prevRotationYaw == 0.0F))
        {
            float f = MathHelper.sqrt_double(d * d + d2 * d2);
            this.prevRotationYaw = (this.rotationYaw = (float)(Math.atan2(d, d2) * 180.0D / Math.PI));
            this.prevRotationPitch = (this.rotationPitch = (float)(Math.atan2(d1, f) * 180.0D / Math.PI));
            this.prevRotationYaw = this.rotationYaw;
            setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
            this.ticksInGround = 0;
        }
    }

    protected boolean canTriggerWalking()
    {
        return false;
    }

    public void onUpdate()
    {
        super.onUpdate();

        if ((this.prevRotationPitch == 0.0F) && (this.prevRotationYaw == 0.0F))
        {
            float var1 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.prevRotationYaw = (this.rotationYaw = (float)(Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI));
            this.prevRotationPitch = (this.rotationPitch = (float)(Math.atan2(this.motionY, var1) * 180.0D / Math.PI));
        }

        int var16 = this.worldObj.getBlockId(this.xTile, this.yTile, this.zTile);

        if (var16 > 0)
        {
            Block.blocksList[var16].setBlockBoundsBasedOnState(this.worldObj, this.xTile, this.yTile, this.zTile);
            AxisAlignedBB var2 = Block.blocksList[var16].getCollisionBoundingBoxFromPool(this.worldObj, this.xTile, this.yTile, this.zTile);

            if ((var2 != null) && (var2.isVecInside(this.worldObj.U().getVecFromPool(this.posX, this.posY, this.posZ))))
            {
                this.inGround = true;
            }
        }

        if (this.arrowShake > 0)
        {
            this.arrowShake -= 1;
        }

        if (this.inGround)
        {
            int var18 = this.worldObj.getBlockId(this.xTile, this.yTile, this.zTile);
            int var19 = this.worldObj.getBlockMetadata(this.xTile, this.yTile, this.zTile);

            if ((var18 == this.inTile) && (var19 == this.inData))
            {
                this.ticksInGround += 1;

                if (this.ticksInGround == 1200)
                {
                    if (!this.worldObj.isRemote)
                    {
                        setDead();
                    }
                }
            }
            else
            {
                this.inGround = false;
                this.motionX *= this.rand.nextFloat() * 0.2F;
                this.motionY *= this.rand.nextFloat() * 0.2F;
                this.motionZ *= this.rand.nextFloat() * 0.2F;
                this.ticksInGround = 0;
                this.ticksFlying = 0;
            }
        }
        else
        {
            this.ticksFlying += 1;
            Vec3 var17 = this.worldObj.U().getVecFromPool(this.posX, this.posY, this.posZ);
            Vec3 var3 = this.worldObj.U().getVecFromPool(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
            MovingObjectPosition var4 = this.worldObj.rayTraceBlocks_do_do(var17, var3, false, true);
            var17 = this.worldObj.U().getVecFromPool(this.posX, this.posY, this.posZ);
            var3 = this.worldObj.U().getVecFromPool(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

            if (var4 != null)
            {
                var3 = this.worldObj.U().getVecFromPool(var4.hitVec.xCoord, var4.hitVec.yCoord, var4.hitVec.zCoord);
            }

            Entity var5 = null;
            List var6 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
            double var7 = 0.0D;
            Iterator var9 = var6.iterator();

            while (var9.hasNext())
            {
                Entity var10 = (Entity)var9.next();

                if ((var10.canBeCollidedWith()) && ((var10 != this.shooter) || (this.ticksFlying >= 5)))
                {
                    float var11 = 0.3F;
                    AxisAlignedBB var12 = var10.boundingBox.expand(var11, var11, var11);
                    MovingObjectPosition var13 = var12.calculateIntercept(var17, var3);

                    if (var13 != null)
                    {
                        double var14 = var17.distanceTo(var13.hitVec);

                        if ((var14 < var7) || (var7 == 0.0D))
                        {
                            var5 = var10;
                            var7 = var14;
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
                if ((var4.entityHit != null) && (onHitTarget(var4.entityHit)))
                {
                    if ((this.shooter != null) && (var4.entityHit == this.shooter))
                    {
                        return;
                    }

                    float var20 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
                    int var24 = MathHelper.ceiling_double_int(var20 * this.dmg);
                    DamageSource var22 = null;

                    if (this.shooter == null)
                    {
                        var22 = new CustomDamageSource("dart", this, this).setDeathMessage(" died covered in darts.").setProjectile();
                    }
                    else
                    {
                        var22 = new CustomDamageSource("dart", this, this.shooter).setDeathMessage(" died covered in " + this.shooter.getEntityName() + "'s darts.").setProjectile();
                    }

                    if (isBurning())
                    {
                        var4.entityHit.setFire(5);
                    }

                    if (var4.entityHit.attackEntityFrom(var22, var24))
                    {
                        this.worldObj.playSoundAtEntity(this, "random.bowhit", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));

                        if (!this.worldObj.isRemote)
                        {
                            setDead();
                        }
                    }
                }
                else if (onHitBlock(var4))
                {
                    this.xTile = var4.blockX;
                    this.yTile = var4.blockY;
                    this.zTile = var4.blockZ;
                    this.inTile = this.worldObj.getBlockId(this.xTile, this.yTile, this.zTile);
                    this.inData = this.worldObj.getBlockMetadata(this.xTile, this.yTile, this.zTile);
                    this.motionX = ((float)(var4.hitVec.xCoord - this.posX));
                    this.motionY = ((float)(var4.hitVec.yCoord - this.posY));
                    this.motionZ = ((float)(var4.hitVec.zCoord - this.posZ));
                    float var20 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
                    this.posX -= this.motionX / var20 * 0.0500000007450581D;
                    this.posY -= this.motionY / var20 * 0.0500000007450581D;
                    this.posZ -= this.motionZ / var20 * 0.0500000007450581D;
                    this.worldObj.playSoundAtEntity(this, "random.bowhit", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
                    this.inGround = true;
                    this.arrowShake = 7;
                }
            }

            handleMotionUpdate();
            float f2 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.rotationYaw = ((float)(Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI));

            for (this.rotationPitch = ((float)(Math.atan2(this.motionY, f2) * 180.0D / Math.PI)); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F);

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

            this.rotationPitch = (this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F);
            this.rotationYaw = (this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F);
            float var23 = 0.99F;
            float var11 = 0.05F;

            if (isInWater())
            {
                for (int var26 = 0; var26 < 4; var26++)
                {
                    float var27 = 0.25F;
                    this.worldObj.spawnParticle("bubble", this.posX - this.motionX * var27, this.posY - this.motionY * var27, this.posZ - this.motionZ * var27, this.motionX, this.motionY, this.motionZ);
                }

                var23 = 0.8F;
            }

            this.posX += this.motionX;
            this.posY += this.motionY;
            this.posZ += this.motionZ;
            handleMotionUpdate();
            setPositionAndRotation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
            doBlockCollisions();
        }
    }

    public void handleMotionUpdate()
    {
        float slow = this.slowdown;

        if (handleWaterMovement())
        {
            for (int k = 0; k < 4; k++)
            {
                float f6 = 0.25F;
                this.worldObj.spawnParticle("bubble", this.posX - this.motionX * f6, this.posY - this.motionY * f6, this.posZ - this.motionZ * f6, this.motionX, this.motionY, this.motionZ);
            }

            slow *= 0.8F;
        }

        this.motionX *= slow;
        this.motionY *= slow;
        this.motionZ *= slow;
        this.motionY -= this.curvature;
    }

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

    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        this.xTile = nbttagcompound.getShort("xTile");
        this.yTile = nbttagcompound.getShort("yTile");
        this.zTile = nbttagcompound.getShort("zTile");
        this.inTile = (nbttagcompound.getByte("inTile") & 0xFF);
        this.inData = (nbttagcompound.getByte("inData") & 0xFF);
        this.arrowShake = (nbttagcompound.getByte("shake") & 0xFF);
        this.inGround = (nbttagcompound.getByte("inGround") == 1);
        this.shotByPlayer = nbttagcompound.getBoolean("player");
        this.canBePickedUp = nbttagcompound.getByte("pickup");
    }

    public void onCollideWithPlayer(EntityPlayer par1EntityPlayer)
    {
        if ((!this.worldObj.isRemote) && (this.inGround) && (this.arrowShake <= 0))
        {
            boolean var2 = (this.canBePickedUp == 1) || ((this.canBePickedUp == 2) && (par1EntityPlayer.capabilities.isCreativeMode));

            if ((this.canBePickedUp == 1) && (!par1EntityPlayer.inventory.addItemStackToInventory(this.item.copy())))
            {
                var2 = false;
            }

            if (var2)
            {
                this.worldObj.playSoundAtEntity(this, "random.pop", 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                par1EntityPlayer.onItemPickup(this, 1);
                setDead();
            }
        }
    }

    public boolean canBeShot(Entity ent)
    {
        if ((ent instanceof EntityLiving))
        {
            if ((EntityLiving)ent == this.shooter)
            {
                return false;
            }
        }

        return (ent.canBeCollidedWith()) && ((!(ent instanceof EntityLiving)) || (((EntityLiving)ent).deathTime <= 0));
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

    public void tickFlying()
    {
    }

    public void tickInGround()
    {
    }

    public boolean onHitBlock(MovingObjectPosition mop)
    {
        return onHitBlock();
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

