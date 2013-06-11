package net.aetherteam.aether.entities;

import cpw.mods.fml.common.registry.IThrowableEntity;

import java.util.List;
import java.util.Random;

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

public class EntityTempestBall extends EntityLiving implements IThrowableEntity
{
    public String dir = "/net/aetherteam/aether/client/sprites";
    public float[] sinage = new float[3];
    private static final double topSpeed = 0.125D;
    private static final float sponge = (180F / (float) Math.PI);
    public Random random = new Random();
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

    public EntityTempestBall(World var1)
    {
        super(var1);
        this.setSize(0.25F, 0.25F);
    }

    public EntityTempestBall(World var1, EntityLiving var2, double var3, double var5, double var7)
    {
        super(var1);
        this.shootingEntity = var2;
        this.setSize(0.25F, 0.25F);
        this.setLocationAndAngles(var2.posX, var2.posY, var2.posZ, var2.rotationYaw, var2.rotationPitch);
        this.setPositionAndRotation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
        this.yOffset = 0.0F;
        this.motionX = this.motionY = this.motionZ = 0.0D;
        var3 += this.rand.nextGaussian() * 0.4D;
        var5 += this.rand.nextGaussian() * 0.4D;
        var7 += this.rand.nextGaussian() * 0.4D;
        double var9 = (double) MathHelper.sqrt_double(var3 * var3 + var5 * var5 + var7 * var7);
        this.accelerationX = var3 / var9 * 0.08000000000000002D;
        this.accelerationY = var5 / var9 * 0.08000000000000002D;
        this.accelerationZ = var7 / var9 * 0.08000000000000002D;
    }

    public void updateAnims()
    {
        for (int var1 = 0; var1 < 3; ++var1)
        {
            this.sinage[var1] += 0.3F + (float) var1 * 0.13F;

            if (this.sinage[var1] > ((float) Math.PI * 2F))
            {
                this.sinage[var1] -= ((float) Math.PI * 2F);
            }
        }
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate()
    {}

    /**
     * Gets called every tick from main Entity class
     */
    public void onEntityUpdate()
    {
        super.onEntityUpdate();
        this.updateAnims();

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
            int var1 = this.worldObj.getBlockId(this.xTile, this.yTile, this.zTile);

            if (var1 == this.inTile)
            {
                ++this.ticksAlive;
                return;
            }

            this.inGround = false;
            this.motionX *= (double) (this.rand.nextFloat() * 0.2F);
            this.motionY *= (double) (this.rand.nextFloat() * 0.2F);
            this.motionZ *= (double) (this.rand.nextFloat() * 0.2F);
            this.ticksAlive = 0;
            this.ticksInAir = 0;
        } else
        {
            ++this.ticksInAir;
        }

        Vec3 var15 = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
        Vec3 var2 = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
        MovingObjectPosition var3 = this.worldObj.rayTraceBlocks(var15, var2);
        var15 = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
        var2 = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

        if (var3 != null)
        {
            var2 = Vec3.createVectorHelper(var3.hitVec.xCoord, var3.hitVec.yCoord, var3.hitVec.zCoord);
        }

        Entity var4 = null;
        List var5 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
        double var6 = 0.0D;

        for (int var8 = 0; var8 < var5.size(); ++var8)
        {
            Entity var9 = (Entity) var5.get(var8);

            if (var9.canBeCollidedWith() && (var9 != this.shootingEntity || this.ticksInAir >= 25))
            {
                float var10 = 0.3F;
                AxisAlignedBB var11 = var9.boundingBox.expand((double) var10, (double) var10, (double) var10);
                MovingObjectPosition var12 = var11.calculateIntercept(var15, var2);

                if (var12 != null)
                {
                    double var13 = var15.distanceTo(var12.hitVec);

                    if (var13 < var6 || var6 == 0.0D)
                    {
                        var4 = var9;
                        var6 = var13;
                    }
                }
            }
        }

        if (var4 != null)
        {
            var3 = new MovingObjectPosition(var4);
        }

        if (var3 != null)
        {
            if (var3.entityHit != null && var3.entityHit != this.shootingEntity)
            {
                if (var3.entityHit instanceof EntityPlayer && ((EntityPlayer) var3.entityHit).inventory.armorInventory[0] != null && ((EntityPlayer) var3.entityHit).inventory.armorInventory[0].itemID == AetherItems.SentryBoots.itemID)
                {
                    this.setDead();
                } else if (var3.entityHit instanceof EntityPlayer && ((EntityPlayer) var3.entityHit).capabilities.isCreativeMode)
                {
                    this.setDead();
                }
            }

            EntityColdLightningBolt var18 = new EntityColdLightningBolt(this.worldObj, this.posX, this.posY, this.posZ);
            var18.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
            this.worldObj.spawnEntityInWorld(var18);

            if (!this.worldObj.isRemote)
            {
                this.setDead();
            }
        }

        this.posX += this.motionX;
        this.posY += this.motionY;
        this.posZ += this.motionZ;
        float var17 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
        this.rotationYaw = (float) (Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);

        for (this.rotationPitch = (float) (Math.atan2(this.motionY, (double) var17) * 180.0D / Math.PI); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F)
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
        float var16 = 0.95F;

        if (this.handleWaterMovement())
        {
            for (int var20 = 0; var20 < 4; ++var20)
            {
                float var19 = 0.25F;
                this.worldObj.spawnParticle("bubble", this.posX - this.motionX * (double) var19, this.posY - this.motionY * (double) var19, this.posZ - this.motionZ * (double) var19, this.motionX, this.motionY, this.motionZ);
            }

            var16 = 0.8F;
        }

        this.motionX += this.accelerationX;
        this.motionY += this.accelerationY;
        this.motionZ += this.accelerationZ;
        this.motionX *= (double) var16;
        this.motionY *= (double) var16;
        this.motionZ *= (double) var16;
        this.worldObj.spawnParticle("smoke", this.posX, this.posY + 0.5D, this.posZ, 0.0D, 0.0D, 0.0D);
        this.setPositionAndRotation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
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
        var1.setByte("shake", (byte) this.field_9406_a);
        var1.setByte("inGround", (byte) (this.inGround ? 1 : 0));
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
        this.field_9406_a = var1.getByte("shake") & 255;
        this.inGround = var1.getByte("inGround") == 1;
    }

    public float getCollisionBorderSize()
    {
        return 0.25F;
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource var1, int var2)
    {
        this.setBeenAttacked();

        if (var1.getEntity() == null)
        {
            return false;
        } else
        {
            Vec3 var3 = var1.getEntity().getLookVec();

            if (var3 != null)
            {
                this.motionX = var3.xCoord;
                this.motionY = var3.yCoord;
                this.motionZ = var3.zCoord;
                this.accelerationX = this.motionX * 0.1D;
                this.accelerationY = this.motionY * 0.1D;
                this.accelerationZ = this.motionZ * 0.1D;

                for (int var4 = 0; var4 < 10; ++var4)
                {
                    double var5 = this.rand.nextGaussian() * 0.02D;
                    double var7 = this.rand.nextGaussian() * 0.02D;
                    double var9 = this.rand.nextGaussian() * 0.02D;
                    double var11 = 5.0D;
                    this.shootingEntity.worldObj.spawnParticle("flame", this.shootingEntity.posX + (double) (this.rand.nextFloat() * this.shootingEntity.width * 2.0F) - (double) this.shootingEntity.width - var5 * var11, this.shootingEntity.posY + (double) (this.rand.nextFloat() * (this.shootingEntity.height - 0.6F)) - var7 * var11, this.shootingEntity.posZ + (double) (this.rand.nextFloat() * this.shootingEntity.width * 2.0F) - (double) this.shootingEntity.width - var9 * var11, var5, var7, var9);
                    this.shootingEntity.worldObj.spawnParticle("largeexplode", this.shootingEntity.posX + (double) (this.rand.nextFloat() * this.shootingEntity.width * 2.0F) - (double) this.shootingEntity.width - var5 * var11, this.shootingEntity.posY + (double) (this.rand.nextFloat() * (this.shootingEntity.height - 0.6F)) - var7 * var11, this.shootingEntity.posZ + (double) (this.rand.nextFloat() * this.shootingEntity.width * 2.0F) - (double) this.shootingEntity.width - var9 * var11, var5, var7, var9);
                }

                this.shootingEntity.attackEntityFrom(var1, 2);
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
        return this.shootingEntity;
    }

    public void setThrower(Entity var1)
    {
        this.shootingEntity = (EntityLiving) var1;
    }

    public int getMaxHealth()
    {
        return 0;
    }
}
