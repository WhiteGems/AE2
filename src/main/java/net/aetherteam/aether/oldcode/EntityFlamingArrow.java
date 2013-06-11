package net.aetherteam.aether.oldcode;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
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

public class EntityFlamingArrow extends Entity
{
    private int xTile = -1;
    private int yTile = -1;
    private int zTile = -1;
    private int inTile = 0;
    private int inData = 0;
    private boolean inGround = false;
    public boolean doesArrowBelongToPlayer = false;
    public int arrowShake = 0;
    public EntityLiving owner;
    private int ticksInGround;
    private int ticksInAir = 0;

    public EntityFlamingArrow(World var1)
    {
        super(var1);
        this.setSize(0.5F, 0.5F);
        this.setFire(1);
    }

    public EntityFlamingArrow(World var1, double var2, double var4, double var6)
    {
        super(var1);
        this.setSize(0.5F, 0.5F);
        this.setPositionAndRotation(var2, var4, var6, this.rotationYaw, this.rotationPitch);
        this.yOffset = 0.0F;
    }

    public EntityFlamingArrow(World var1, EntityLiving var2)
    {
        super(var1);
        this.owner = var2;
        this.doesArrowBelongToPlayer = var2 instanceof EntityPlayer;
        this.setSize(0.5F, 0.5F);
        this.setLocationAndAngles(var2.posX, var2.posY + (double) var2.getEyeHeight(), var2.posZ, var2.rotationYaw, var2.rotationPitch);
        this.posX -= (double) (MathHelper.cos(this.rotationYaw / 180.0F * (float) Math.PI) * 0.16F);
        this.posY -= 0.10000000149011612D;
        this.posZ -= (double) (MathHelper.sin(this.rotationYaw / 180.0F * (float) Math.PI) * 0.16F);
        this.setPositionAndRotation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
        this.yOffset = 0.0F;
        this.motionX = (double) (-MathHelper.sin(this.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float) Math.PI));
        this.motionZ = (double) (MathHelper.cos(this.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float) Math.PI));
        this.motionY = (double) (-MathHelper.sin(this.rotationPitch / 180.0F * (float) Math.PI));
        this.setArrowHeading(this.motionX, this.motionY, this.motionZ, 1.5F, 1.0F);
    }

    protected void entityInit() {}

    public void setArrowHeading(double var1, double var3, double var5, float var7, float var8)
    {
        float var9 = MathHelper.sqrt_double(var1 * var1 + var3 * var3 + var5 * var5);
        var1 /= (double) var9;
        var3 /= (double) var9;
        var5 /= (double) var9;
        var1 += this.rand.nextGaussian() * 0.007499999832361937D * (double) var8;
        var3 += this.rand.nextGaussian() * 0.007499999832361937D * (double) var8;
        var5 += this.rand.nextGaussian() * 0.007499999832361937D * (double) var8;
        var1 *= (double) var7;
        var3 *= (double) var7;
        var5 *= (double) var7;
        this.motionX = var1;
        this.motionY = var3;
        this.motionZ = var5;
        float var10 = MathHelper.sqrt_double(var1 * var1 + var5 * var5);
        this.prevRotationYaw = this.rotationYaw = (float) (Math.atan2(var1, var5) * 180.0D / Math.PI);
        this.prevRotationPitch = this.rotationPitch = (float) (Math.atan2(var3, (double) var10) * 180.0D / Math.PI);
        this.ticksInGround = 0;
    }

    /**
     * Sets the velocity to the args. Args: x, y, z
     */
    public void setVelocity(double var1, double var3, double var5)
    {
        this.motionX = var1;
        this.motionY = var3;
        this.motionZ = var5;

        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F)
        {
            float var7 = MathHelper.sqrt_double(var1 * var1 + var5 * var5);
            this.prevRotationYaw = this.rotationYaw = (float) (Math.atan2(var1, var5) * 180.0D / Math.PI);
            this.prevRotationPitch = this.rotationPitch = (float) (Math.atan2(var3, (double) var7) * 180.0D / Math.PI);
            this.prevRotationPitch = this.rotationPitch;
            this.prevRotationYaw = this.rotationYaw;
            this.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
            this.ticksInGround = 0;
        }
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();

        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F)
        {
            float var1 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.prevRotationYaw = this.rotationYaw = (float) (Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);
            this.prevRotationPitch = this.rotationPitch = (float) (Math.atan2(this.motionY, (double) var1) * 180.0D / Math.PI);
        }

        int var21 = this.worldObj.getBlockId(this.xTile, this.yTile, this.zTile);

        if (var21 > 0)
        {
            Block.blocksList[var21].setBlockBoundsBasedOnState(this.worldObj, this.xTile, this.yTile, this.zTile);
            AxisAlignedBB var2 = Block.blocksList[var21].getCollisionBoundingBoxFromPool(this.worldObj, this.xTile, this.yTile, this.zTile);

            if (var2 != null && var2.isVecInside(Vec3.createVectorHelper(this.posX, this.posY, this.posZ)))
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
            int var23 = this.worldObj.getBlockId(this.xTile, this.yTile, this.zTile);
            int var22 = this.worldObj.getBlockMetadata(this.xTile, this.yTile, this.zTile);

            if (var23 == this.inTile && var22 == this.inData)
            {
                ++this.ticksInGround;

                if (this.ticksInGround == 1200)
                {
                    this.setDead();
                }
            } else
            {
                this.inGround = false;
                this.motionX *= (double) (this.rand.nextFloat() * 0.2F);
                this.motionY *= (double) (this.rand.nextFloat() * 0.2F);
                this.motionZ *= (double) (this.rand.nextFloat() * 0.2F);
                this.ticksInGround = 0;
                this.ticksInAir = 0;
            }
        } else
        {
            this.worldObj.spawnParticle(this.rand.nextBoolean() ? "flame" : "smoke", this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);
            ++this.ticksInAir;
            Vec3 var24 = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
            Vec3 var3 = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
            MovingObjectPosition var4 = this.worldObj.rayTraceBlocks_do_do(var24, var3, false, true);
            var24 = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
            var3 = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

            if (var4 != null)
            {
                var3 = Vec3.createVectorHelper(var4.hitVec.xCoord, var4.hitVec.yCoord, var4.hitVec.zCoord);
            }

            Entity var5 = null;
            List var6 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
            double var7 = 0.0D;
            float var11;
            int var9;

            for (var9 = 0; var9 < var6.size(); ++var9)
            {
                Entity var10 = (Entity) var6.get(var9);

                if (var10.canBeCollidedWith() && (var10 != this.owner || this.ticksInAir >= 5))
                {
                    var11 = 0.3F;
                    AxisAlignedBB var12 = var10.boundingBox.expand((double) var11, (double) var11, (double) var11);
                    MovingObjectPosition var13 = var12.calculateIntercept(var24, var3);

                    if (var13 != null)
                    {
                        double var14 = var24.distanceTo(var13.hitVec);

                        if (var14 < var7 || var7 == 0.0D)
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

            int var16;
            float var25;

            if (var4 != null)
            {
                int var19;
                int var17;

                if (var4.entityHit != null)
                {
                    if (var4.entityHit.attackEntityFrom(DamageSource.causeMobDamage(this.owner), 4))
                    {
                        this.worldObj.playSoundAtEntity(this, "random.drr", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
                        var4.entityHit.setFire(100);
                        var9 = MathHelper.floor_double(var4.entityHit.boundingBox.minX);
                        var17 = MathHelper.floor_double(var4.entityHit.boundingBox.minY);
                        var19 = MathHelper.floor_double(var4.entityHit.boundingBox.minZ);
                        this.worldObj.setBlock(var9, var17, var19, 51);
                        this.setDead();
                    } else
                    {
                        this.motionX *= -0.10000000149011612D;
                        this.motionY *= -0.10000000149011612D;
                        this.motionZ *= -0.10000000149011612D;
                        this.rotationYaw += 180.0F;
                        this.prevRotationYaw += 180.0F;
                        this.ticksInAir = 0;
                    }
                } else
                {
                    this.xTile = var4.blockX;
                    this.yTile = var4.blockY;
                    this.zTile = var4.blockZ;
                    this.inTile = this.worldObj.getBlockId(this.xTile, this.yTile, this.zTile);
                    this.inData = this.worldObj.getBlockMetadata(this.xTile, this.yTile, this.zTile);
                    this.motionX = (double) ((float) (var4.hitVec.xCoord - this.posX));
                    this.motionY = (double) ((float) (var4.hitVec.yCoord - this.posY));
                    this.motionZ = (double) ((float) (var4.hitVec.zCoord - this.posZ));
                    var25 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
                    this.posX -= this.motionX / (double) var25 * 0.05000000074505806D;
                    this.posY -= this.motionY / (double) var25 * 0.05000000074505806D;
                    this.posZ -= this.motionZ / (double) var25 * 0.05000000074505806D;
                    this.worldObj.playSoundAtEntity(this, "random.drr", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
                    var17 = MathHelper.floor_double(this.posX);
                    var19 = MathHelper.floor_double(this.posY);
                    var16 = MathHelper.floor_double(this.posZ);
                    this.worldObj.setBlock(var17, var19, var16, 51);
                    this.inGround = true;
                    this.arrowShake = 7;
                }
            }

            this.posX += this.motionX;
            this.posY += this.motionY;
            this.posZ += this.motionZ;
            var25 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.rotationYaw = (float) (Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);

            for (this.rotationPitch = (float) (Math.atan2(this.motionY, (double) var25) * 180.0D / Math.PI); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F)
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
            float var18 = 0.99F;
            var11 = 0.03F;

            if (this.isInWater())
            {
                for (var16 = 0; var16 < 4; ++var16)
                {
                    float var20 = 0.25F;
                    this.worldObj.spawnParticle("bubble", this.posX - this.motionX * (double) var20, this.posY - this.motionY * (double) var20, this.posZ - this.motionZ * (double) var20, this.motionX, this.motionY, this.motionZ);
                }

                var18 = 0.8F;
            }

            this.motionX *= (double) var18;
            this.motionY *= (double) var18;
            this.motionZ *= (double) var18;
            this.motionY -= (double) var11;
            this.setPositionAndRotation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
        }
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound var1)
    {
        var1.setShort("xTile", (short) this.xTile);
        var1.setShort("yTile", (short) this.yTile);
        var1.setShort("zTile", (short) this.zTile);
        var1.setByte("inTile", (byte) this.inTile);
        var1.setByte("inData", (byte) this.inData);
        var1.setByte("shake", (byte) this.arrowShake);
        var1.setByte("inGround", (byte) (this.inGround ? 1 : 0));
        var1.setBoolean("player", this.doesArrowBelongToPlayer);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound var1)
    {
        this.xTile = var1.getShort("xTile");
        this.yTile = var1.getShort("yTile");
        this.zTile = var1.getShort("zTile");
        this.inTile = var1.getByte("inTile") & 255;
        this.inData = var1.getByte("inData") & 255;
        this.arrowShake = var1.getByte("shake") & 255;
        this.inGround = var1.getByte("inGround") == 1;
        this.doesArrowBelongToPlayer = var1.getBoolean("player");
    }

    /**
     * Called by a player entity when they collide with an entity
     */
    public void onCollideWithPlayer(EntityPlayer var1)
    {
        if (!this.worldObj.isRemote)
        {
            if (this.inGround && this.doesArrowBelongToPlayer && this.arrowShake <= 0 && var1.inventory.addItemStackToInventory(new ItemStack(Item.arrow, 1)))
            {
                this.worldObj.playSoundAtEntity(this, "random.pop", 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                var1.onItemPickup(this, 1);
                this.setDead();
            }
        }
    }

    public float getShadowSize()
    {
        return 0.0F;
    }
}
