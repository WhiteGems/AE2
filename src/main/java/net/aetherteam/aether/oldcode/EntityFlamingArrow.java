package net.aetherteam.aether.oldcode;

import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
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
    private int xTile;
    private int yTile;
    private int zTile;
    private int inTile;
    private int inData;
    private boolean inGround;
    public boolean doesArrowBelongToPlayer;
    public int arrowShake;
    public EntityLiving owner;
    private int ticksInGround;
    private int ticksInAir;

    public EntityFlamingArrow(World world)
    {
        super(world);
        this.xTile = -1;
        this.yTile = -1;
        this.zTile = -1;
        this.inTile = 0;
        this.inData = 0;
        this.inGround = false;
        this.doesArrowBelongToPlayer = false;
        this.arrowShake = 0;
        this.ticksInAir = 0;
        setSize(0.5F, 0.5F);
        setFire(1);
    }

    public EntityFlamingArrow(World world, double d, double d1, double d2)
    {
        super(world);
        this.xTile = -1;
        this.yTile = -1;
        this.zTile = -1;
        this.inTile = 0;
        this.inData = 0;
        this.inGround = false;
        this.doesArrowBelongToPlayer = false;
        this.arrowShake = 0;
        this.ticksInAir = 0;
        setSize(0.5F, 0.5F);
        setPositionAndRotation(d, d1, d2, this.rotationYaw, this.rotationPitch);
        this.yOffset = 0.0F;
    }

    public EntityFlamingArrow(World world, EntityLiving entityliving)
    {
        super(world);
        this.xTile = -1;
        this.yTile = -1;
        this.zTile = -1;
        this.inTile = 0;
        this.inData = 0;
        this.inGround = false;
        this.doesArrowBelongToPlayer = false;
        this.arrowShake = 0;
        this.ticksInAir = 0;
        this.owner = entityliving;
        this.doesArrowBelongToPlayer = (entityliving instanceof EntityPlayer);
        setSize(0.5F, 0.5F);
        setLocationAndAngles(entityliving.posX, entityliving.posY + entityliving.getEyeHeight(), entityliving.posZ, entityliving.rotationYaw, entityliving.rotationPitch);
        this.posX -= MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * 0.16F;
        this.posY -= 0.1000000014901161D;
        this.posZ -= MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * 0.16F;
        setPositionAndRotation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
        this.yOffset = 0.0F;
        this.motionX = (-MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI));
        this.motionZ = (MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI));
        this.motionY = (-MathHelper.sin(this.rotationPitch / 180.0F * (float)Math.PI));
        setArrowHeading(this.motionX, this.motionY, this.motionZ, 1.5F, 1.0F);
    }

    protected void entityInit()
    {
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
            this.prevRotationPitch = this.rotationPitch;
            this.prevRotationYaw = this.rotationYaw;
            setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
            this.ticksInGround = 0;
        }
    }

    public void onUpdate()
    {
        super.onUpdate();

        if ((this.prevRotationPitch == 0.0F) && (this.prevRotationYaw == 0.0F))
        {
            float f = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.prevRotationYaw = (this.rotationYaw = (float)(Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI));
            this.prevRotationPitch = (this.rotationPitch = (float)(Math.atan2(this.motionY, f) * 180.0D / Math.PI));
        }

        int i = this.worldObj.getBlockId(this.xTile, this.yTile, this.zTile);

        if (i > 0)
        {
            Block.blocksList[i].setBlockBoundsBasedOnState(this.worldObj, this.xTile, this.yTile, this.zTile);
            AxisAlignedBB axisalignedbb = Block.blocksList[i].getCollisionBoundingBoxFromPool(this.worldObj, this.xTile, this.yTile, this.zTile);

            if ((axisalignedbb != null) && (axisalignedbb.isVecInside(Vec3.createVectorHelper(this.posX, this.posY, this.posZ))))
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
            int j = this.worldObj.getBlockId(this.xTile, this.yTile, this.zTile);
            int k = this.worldObj.getBlockMetadata(this.xTile, this.yTile, this.zTile);

            if ((j != this.inTile) || (k != this.inData))
            {
                this.inGround = false;
                this.motionX *= this.rand.nextFloat() * 0.2F;
                this.motionY *= this.rand.nextFloat() * 0.2F;
                this.motionZ *= this.rand.nextFloat() * 0.2F;
                this.ticksInGround = 0;
                this.ticksInAir = 0;
                return;
            }

            this.ticksInGround += 1;

            if (this.ticksInGround == 1200)
            {
                setDead();
            }

            return;
        }

        this.worldObj.spawnParticle(this.rand.nextBoolean() ? "flame" : "smoke", this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);
        this.ticksInAir += 1;
        Vec3 vec3d = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
        Vec3 vec3d1 = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
        MovingObjectPosition movingobjectposition = this.worldObj.rayTraceBlocks_do_do(vec3d, vec3d1, false, true);
        vec3d = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
        vec3d1 = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

        if (movingobjectposition != null)
        {
            vec3d1 = Vec3.createVectorHelper(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord);
        }

        Entity entity = null;
        List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
        double d = 0.0D;

        for (int l = 0; l < list.size(); l++)
        {
            Entity entity1 = (Entity)list.get(l);

            if ((entity1.canBeCollidedWith()) && ((entity1 != this.owner) || (this.ticksInAir >= 5)))
            {
                float f4 = 0.3F;
                AxisAlignedBB axisalignedbb1 = entity1.boundingBox.expand(f4, f4, f4);
                MovingObjectPosition movingobjectposition1 = axisalignedbb1.calculateIntercept(vec3d, vec3d1);

                if (movingobjectposition1 != null)
                {
                    double d1 = vec3d.distanceTo(movingobjectposition1.hitVec);

                    if ((d1 < d) || (d == 0.0D))
                    {
                        entity = entity1;
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
            if (movingobjectposition.entityHit != null)
            {
                if (movingobjectposition.entityHit.attackEntityFrom(DamageSource.causeMobDamage(this.owner), 4))
                {
                    this.worldObj.playSoundAtEntity(this, "random.drr", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
                    movingobjectposition.entityHit.setFire(100);
                    int x = MathHelper.floor_double(movingobjectposition.entityHit.boundingBox.minX);
                    int y = MathHelper.floor_double(movingobjectposition.entityHit.boundingBox.minY);
                    int z = MathHelper.floor_double(movingobjectposition.entityHit.boundingBox.minZ);
                    this.worldObj.setBlock(x, y, z, 51);
                    setDead();
                }
                else
                {
                    this.motionX *= -0.1000000014901161D;
                    this.motionY *= -0.1000000014901161D;
                    this.motionZ *= -0.1000000014901161D;
                    this.rotationYaw += 180.0F;
                    this.prevRotationYaw += 180.0F;
                    this.ticksInAir = 0;
                }
            }
            else
            {
                this.xTile = movingobjectposition.blockX;
                this.yTile = movingobjectposition.blockY;
                this.zTile = movingobjectposition.blockZ;
                this.inTile = this.worldObj.getBlockId(this.xTile, this.yTile, this.zTile);
                this.inData = this.worldObj.getBlockMetadata(this.xTile, this.yTile, this.zTile);
                this.motionX = ((float)(movingobjectposition.hitVec.xCoord - this.posX));
                this.motionY = ((float)(movingobjectposition.hitVec.yCoord - this.posY));
                this.motionZ = ((float)(movingobjectposition.hitVec.zCoord - this.posZ));
                float f1 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
                this.posX -= this.motionX / f1 * 0.0500000007450581D;
                this.posY -= this.motionY / f1 * 0.0500000007450581D;
                this.posZ -= this.motionZ / f1 * 0.0500000007450581D;
                this.worldObj.playSoundAtEntity(this, "random.drr", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
                int xPos = MathHelper.floor_double(this.posX);
                int yPos = MathHelper.floor_double(this.posY);
                int zPos = MathHelper.floor_double(this.posZ);
                this.worldObj.setBlock(xPos, yPos, zPos, 51);
                this.inGround = true;
                this.arrowShake = 7;
            }
        }

        this.posX += this.motionX;
        this.posY += this.motionY;
        this.posZ += this.motionZ;
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
        float f3 = 0.99F;
        float f5 = 0.03F;

        if (isInWater())
        {
            for (int i1 = 0; i1 < 4; i1++)
            {
                float f6 = 0.25F;
                this.worldObj.spawnParticle("bubble", this.posX - this.motionX * f6, this.posY - this.motionY * f6, this.posZ - this.motionZ * f6, this.motionX, this.motionY, this.motionZ);
            }

            f3 = 0.8F;
        }

        this.motionX *= f3;
        this.motionY *= f3;
        this.motionZ *= f3;
        this.motionY -= f5;
        setPositionAndRotation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
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
        nbttagcompound.setBoolean("player", this.doesArrowBelongToPlayer);
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
        this.doesArrowBelongToPlayer = nbttagcompound.getBoolean("player");
    }

    public void onCollideWithPlayer(EntityPlayer entityplayer)
    {
        if (this.worldObj.isRemote)
        {
            return;
        }

        if ((this.inGround) && (this.doesArrowBelongToPlayer) && (this.arrowShake <= 0) && (entityplayer.inventory.addItemStackToInventory(new ItemStack(Item.arrow, 1))))
        {
            this.worldObj.playSoundAtEntity(this, "random.pop", 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
            entityplayer.onItemPickup(this, 1);
            setDead();
        }
    }

    public float getShadowSize()
    {
        return 0.0F;
    }
}

