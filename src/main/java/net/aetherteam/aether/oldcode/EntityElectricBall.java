package net.aetherteam.aether.oldcode;

import cpw.mods.fml.common.registry.IThrowableEntity;
import java.util.List;
import java.util.Random;
import net.aetherteam.aether.entities.EntityNewZephyr;
import net.aetherteam.aether.entities.EntityTempest;
import net.aetherteam.aether.items.AetherItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntityElectricBall extends EntityLiving implements IThrowableEntity
{
    public float[] sinage = new float[3];
    private static final double topSpeed = 0.125D;
    private static final float sponge = (180F / (float)Math.PI);
    public Random random = new Random();
    private int xTile;
    private int yTile;
    private int zTile;
    private int inTile;
    protected boolean inGround;
    public int field_9406_a;
    protected EntityLiving shootingEntity;
    private int ticksAlive;
    private int ticksInAir;
    public double accelerationX;
    public double accelerationY;
    public double accelerationZ;

    public EntityElectricBall(World var1)
    {
        super(var1);
        this.texture = "/net/aetherteam/aether/client/sprites/projectiles/electroball/electroball.png";
        this.xTile = -1;
        this.yTile = -1;
        this.zTile = -1;
        this.inTile = 0;
        this.inGround = false;
        this.field_9406_a = 0;
        this.ticksInAir = 0;
        this.setSize(1.0F, 1.0F);

        for (int var2 = 0; var2 < 3; ++var2)
        {
            this.sinage[var2] = this.random.nextFloat() * 6.0F;
        }
    }

    public void updateAnims()
    {
        for (int var1 = 0; var1 < 3; ++var1)
        {
            this.sinage[var1] += 0.3F + (float)var1 * 0.13F;

            if (this.sinage[var1] > ((float)Math.PI * 2F))
            {
                this.sinage[var1] -= ((float)Math.PI * 2F);
            }
        }
    }

    public EntityElectricBall(World var1, EntityLiving var2, double var3, double var5, double var7)
    {
        super(var1);
        this.xTile = -1;
        this.yTile = -1;
        this.zTile = -1;
        this.inTile = 0;
        this.inGround = false;
        this.field_9406_a = 0;
        this.ticksInAir = 0;
        this.shootingEntity = var2;
        this.setSize(0.5F, 0.5F);
        this.setLocationAndAngles(var2.posX, var2.posY, var2.posZ, var2.rotationYaw, var2.rotationPitch);
        this.setPositionAndRotation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
        this.yOffset = 0.0F;
        this.motionX = this.motionY = this.motionZ = 0.0D;
        var3 += this.rand.nextGaussian() * 0.4D;
        var5 += this.rand.nextGaussian() * 0.4D;
        var7 += this.rand.nextGaussian() * 0.4D;
        double var9 = (double)MathHelper.sqrt_double(var3 * var3 + var5 * var5 + var7 * var7);
        this.accelerationX = var3 / var9 * 0.15000000000000002D;
        this.accelerationY = var5 / var9 * 0.1D;
        this.accelerationZ = var7 / var9 * 0.15000000000000002D;
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate() {}

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
        int var8;

        for (var8 = 0; var8 < var5.size(); ++var8)
        {
            Entity var9 = (Entity)var5.get(var8);

            if (var9.canBeCollidedWith() && (var9 != this.shootingEntity || this.ticksInAir >= 25))
            {
                float var10 = 0.3F;
                AxisAlignedBB var11 = var9.boundingBox.expand((double)var10, (double)var10, (double)var10);
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

        int var20;

        if (var3 != null)
        {
            int var17;
            EntityLightningBolt var19;

            if (var3.entityHit != null)
            {
                if (var3.entityHit != this.shootingEntity)
                {
                    if (!var3.entityHit.attackEntityFrom(DamageSource.causeMobDamage(this.shootingEntity), 0))
                    {
                        ;
                    }

                    if (var3.entityHit instanceof EntityPlayer && ((EntityPlayer)var3.entityHit).inventory.armorInventory[0] != null && ((EntityPlayer)var3.entityHit).inventory.armorInventory[0].itemID == AetherItems.SentryBoots.itemID)
                    {
                        this.setDead();
                    }
                    else if (var3.entityHit instanceof EntityPlayer && ((EntityPlayer)var3.entityHit).capabilities.isCreativeMode)
                    {
                        this.setDead();
                    }
                    else if (var3.entityHit != null && !(var3.entityHit instanceof EntityNewZephyr) && !(var3.entityHit instanceof EntityTempest))
                    {
                        var8 = MathHelper.floor_double(var3.entityHit.boundingBox.minX);
                        var17 = MathHelper.floor_double(var3.entityHit.boundingBox.minY);
                        var20 = MathHelper.floor_double(var3.entityHit.boundingBox.minZ);
                        var19 = new EntityLightningBolt(this.worldObj, (double)var8, (double)var17, (double)var20);
                        var19.setLocationAndAngles((double)var8, (double)var17, (double)var20, this.rotationYaw, 0.0F);
                        this.worldObj.spawnEntityInWorld(var19);
                    }
                }
            }
            else
            {
                var8 = MathHelper.floor_double(this.posX);
                var17 = MathHelper.floor_double(this.posY);
                var20 = MathHelper.floor_double(this.posZ);
                var19 = new EntityLightningBolt(this.worldObj, this.posX, this.posY, this.posZ);
                var19.setLocationAndAngles((double)var8, (double)var17, (double)var20, this.rotationYaw, 0.0F);
                this.worldObj.spawnEntityInWorld(var19);
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
        float var18 = 0.95F;

        if (this.handleWaterMovement())
        {
            for (var20 = 0; var20 < 4; ++var20)
            {
                float var21 = 0.25F;
                this.worldObj.spawnParticle("bubble", this.posX - this.motionX * (double)var21, this.posY - this.motionY * (double)var21, this.posZ - this.motionZ * (double)var21, this.motionX, this.motionY, this.motionZ);
            }

            var18 = 0.8F;
        }

        this.motionX += this.accelerationX;
        this.motionY += this.accelerationY;
        this.motionZ += this.accelerationZ;
        this.motionX *= (double)var18;
        this.motionY *= (double)var18;
        this.motionZ *= (double)var18;
        this.worldObj.spawnParticle("smoke", this.posX, this.posY + 0.5D, this.posZ, 0.0D, 0.0D, 0.0D);
        this.setPositionAndRotation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound var1)
    {
        var1.setShort("xTile", (short)this.xTile);
        var1.setShort("yTile", (short)this.yTile);
        var1.setShort("zTile", (short)this.zTile);
        var1.setByte("inTile", (byte)this.inTile);
        var1.setByte("shake", (byte)this.field_9406_a);
        var1.setByte("inGround", (byte)(this.inGround ? 1 : 0));
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
        return 1.0F;
    }

    public float getShadowSize()
    {
        return 0.0F;
    }

    public Entity getThrower()
    {
        return null;
    }

    public void setThrower(Entity var1) {}

    public int getMaxHealth()
    {
        return 0;
    }
}
