package net.aetherteam.aether.entities;

import java.util.ArrayList;
import java.util.List;

import net.aetherteam.aether.blocks.AetherBlocks;
import net.aetherteam.aether.entities.mounts.EntityAerbunny;
import net.aetherteam.aether.entities.mounts.EntityMoa;
import net.aetherteam.aether.entities.mounts.EntityPhyg;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IProjectile;
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

public class EntityNotchWave extends Entity implements IProjectile
{
    private int xTileSnowball;
    private int yTileSnowball;
    private int zTileSnowball;
    private int inTileSnowball;
    private boolean inGroundSnowball;
    public int shakeSnowball;
    private EntityLiving thrower;
    private int ticksInGroundSnowball;
    private int ticksInAirSnowball;
    public ArrayList harvestBlockBans;

    public EntityNotchWave(World var1)
    {
        super(var1);
        this.harvestBlockBans = new ArrayList();
        this.xTileSnowball = -1;
        this.yTileSnowball = -1;
        this.zTileSnowball = -1;
        this.inTileSnowball = 0;
        this.inGroundSnowball = false;
        this.shakeSnowball = 0;
        this.ticksInAirSnowball = 0;
        this.setSize(0.25F, 0.25F);
        this.harvestBlockBans.add(Integer.valueOf(AetherBlocks.BerryBushStem.blockID));
    }

    protected void entityInit() {}

    /**
     * Checks if the entity is in range to render by using the past in distance and comparing it to its average edge
     * length * 64 * renderDistanceWeight Args: distance
     */
    public boolean isInRangeToRenderDist(double var1)
    {
        double var3 = this.boundingBox.getAverageEdgeLength() * 4.0D;
        var3 *= 64.0D;
        return var1 < var3 * var3;
    }

    public EntityNotchWave(World var1, EntityLiving var2)
    {
        this(var1);
        this.thrower = var2;
        this.setLocationAndAngles(var2.posX, var2.posY + (double) var2.getEyeHeight(), var2.posZ, var2.rotationYaw, var2.rotationPitch);
        this.posX -= (double) (MathHelper.cos(this.rotationYaw / 180.0F * (float) Math.PI) * 0.16F);
        this.posY -= 0.10000000149011612D;
        this.posZ -= (double) (MathHelper.sin(this.rotationYaw / 180.0F * (float) Math.PI) * 0.16F);
        this.setPositionAndRotation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
        this.yOffset = 0.0F;
        float var3 = 0.4F;
        this.motionX = (double) (-MathHelper.sin(this.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float) Math.PI) * var3);
        this.motionZ = (double) (MathHelper.cos(this.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float) Math.PI) * var3);
        this.motionY = (double) (-MathHelper.sin(this.rotationPitch / 180.0F * (float) Math.PI) * var3);
        this.setSnowballHeading(this.motionX, this.motionY, this.motionZ, 1.5F, 1.0F);
        this.setThrowableHeading(this.motionX, this.motionY, this.motionZ, 1.5F, 1.0F);
    }

    public EntityNotchWave(World var1, double var2, double var4, double var6)
    {
        this(var1);
        this.setPositionAndRotation(var2, var4, var6, this.rotationYaw, this.rotationPitch);
        this.yOffset = 0.0F;
    }

    public void setSnowballHeading(double var1, double var3, double var5, float var7, float var8)
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
        this.ticksInGroundSnowball = 0;
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

        if (this.ticksInAirSnowball > 100)
        {
            this.setDead();
        }

        if (this.inGroundSnowball)
        {
            this.setDead();
        } else
        {
            ++this.ticksInAirSnowball;
        }

        Vec3 var1 = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
        Vec3 var2 = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
        MovingObjectPosition var3 = this.worldObj.rayTraceBlocks(var1, var2);
        var1 = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
        var2 = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

        if (var3 != null)
        {
            var2 = Vec3.createVectorHelper(var3.hitVec.xCoord, var3.hitVec.yCoord, var3.hitVec.zCoord);
        }

        int var8;

        if (!this.worldObj.isRemote)
        {
            Entity var4 = null;
            List var5 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(4.0D, 4.0D, 4.0D));
            double var6 = 0.0D;

            for (var8 = 0; var8 < var5.size(); ++var8)
            {
                Entity var9 = (Entity) var5.get(var8);

                if (var9.canBeCollidedWith() && (var9 != this.thrower || this.ticksInAirSnowball >= 5))
                {
                    float var10 = 0.3F;

                    if ((!(var9 instanceof EntityPhyg) || ((EntityPhyg) var9).getSaddled()) && (!(var9 instanceof EntityMoa) || ((EntityMoa) var9).getSaddled()) && (!(var9 instanceof EntityAerbunny) || !(((EntityAerbunny) var9).riddenByEntity instanceof EntityPlayer)) && var9 != this.thrower)
                    {
                        var9.attackEntityFrom(DamageSource.causeMobDamage(this.thrower), 5);
                    }

                    AxisAlignedBB var11 = var9.boundingBox.expand((double) var10, (double) var10, (double) var10);
                    MovingObjectPosition var12 = var11.calculateIntercept(var1, var2);

                    if (var12 != null)
                    {
                        double var13 = var1.distanceTo(var12.hitVec);

                        if (var13 < var6 || var6 == 0.0D)
                        {
                            var4 = var9;
                            var6 = var13;
                        }
                    }
                }
            }

            for (var8 = (int) (this.posX - 3.0D); (double) var8 <= this.posX + 3.0D; ++var8)
            {
                for (int var24 = (int) (this.posY - 3.0D); (double) var24 <= this.posY + 3.0D; ++var24)
                {
                    for (int var17 = (int) (this.posZ - 3.0D); (double) var17 <= this.posZ + 3.0D; ++var17)
                    {
                        if (Block.blocksList[this.worldObj.getBlockId(var8, var24, var17)] instanceof BlockFlower && this.thrower != null && this.thrower instanceof EntityPlayer)
                        {
                            int var16 = this.worldObj.getBlockId(var8, var24, var17);
                            int var15 = this.worldObj.getBlockMetadata(var8, var24, var17);

                            if (!this.harvestBlockBans.contains(Integer.valueOf(this.worldObj.getBlockId(var8, var24, var17))))
                            {
                                Block.blocksList[this.worldObj.getBlockId(var8, var24, var17)].harvestBlock(this.thrower.worldObj, (EntityPlayer) this.thrower, var8, var24, var17, this.worldObj.getBlockMetadata(var8, var24, var17));

                                if (this.worldObj.getBlockId(var8, var24, var17) == var16 && this.worldObj.getBlockMetadata(var8, var24, var17) == var15)
                                {
                                    Block.blocksList[this.worldObj.getBlockId(var8, var24, var17)].removeBlockByPlayer(this.thrower.worldObj, (EntityPlayer) this.thrower, var8, var24, var17);
                                }
                            }
                        }
                    }
                }
            }

            if (var4 != null)
            {
                var3 = new MovingObjectPosition(var4);
            }
        }

        int var7;

        if (var3 != null)
        {
            int var18;

            for (var18 = (int) (this.posX - 3.0D); (double) var18 <= this.posX + 3.0D; ++var18)
            {
                for (int var21 = (int) (this.posY - 3.0D); (double) var21 <= this.posY + 3.0D; ++var21)
                {
                    for (int var19 = (int) (this.posZ - 3.0D); (double) var19 <= this.posZ + 3.0D; ++var19)
                    {
                        if (Block.blocksList[this.worldObj.getBlockId(var18, var21, var19)] instanceof BlockFlower && this.thrower != null && this.thrower instanceof EntityPlayer)
                        {
                            var7 = this.worldObj.getBlockId(var18, var21, var19);
                            var8 = this.worldObj.getBlockMetadata(var18, var21, var19);

                            if (!this.harvestBlockBans.contains(Integer.valueOf(this.worldObj.getBlockId(var18, var21, var19))))
                            {
                                Block.blocksList[this.worldObj.getBlockId(var18, var21, var19)].harvestBlock(this.thrower.worldObj, (EntityPlayer) this.thrower, var18, var21, var19, this.worldObj.getBlockMetadata(var18, var21, var19));

                                if (this.worldObj.getBlockId(var18, var21, var19) == var7 && this.worldObj.getBlockMetadata(var18, var21, var19) == var8)
                                {
                                    Block.blocksList[this.worldObj.getBlockId(var18, var21, var19)].removeBlockByPlayer(this.thrower.worldObj, (EntityPlayer) this.thrower, var18, var21, var19);
                                }
                            }
                        }
                    }
                }
            }

            if ((var3.entityHit == null || (!(var3.entityHit instanceof EntityPhyg) || ((EntityPhyg) var3.entityHit).getSaddled()) && (!(var3.entityHit instanceof EntityMoa) || ((EntityMoa) var3.entityHit).getSaddled()) && (!(var3.entityHit instanceof EntityAerbunny) || !(((EntityAerbunny) var3.entityHit).riddenByEntity instanceof EntityPlayer))) && var3.entityHit != null && var3.entityHit != this.thrower)
            {
                if (!var3.entityHit.attackEntityFrom(DamageSource.causeMobDamage(this.thrower), 0))
                {
                    ;
                }

                var3.entityHit.addVelocity(this.motionX, 0.6D, this.motionZ);
            }

            for (var18 = 0; var18 < 8; ++var18)
            {
                this.worldObj.spawnParticle("explode", this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);
                this.worldObj.spawnParticle("explode", this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);
                this.worldObj.spawnParticle("smoke", this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);
                this.worldObj.spawnParticle("largesmoke", this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);
                this.worldObj.spawnParticle("flame", this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);
            }

            this.setDead();
        }

        this.posX += this.motionX;
        this.posY += this.motionY;
        this.posZ += this.motionZ;
        float var22 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
        this.rotationYaw = (float) (Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);

        for (this.rotationPitch = (float) (Math.atan2(this.motionY, (double) var22) * 180.0D / Math.PI); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F)
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
        float var23 = 0.99F;
        float var20 = 0.03F;

        if (this.isInWater())
        {
            for (var7 = 0; var7 < 4; ++var7)
            {
                float var25 = 0.25F;
                this.worldObj.spawnParticle("bubble", this.posX - this.motionX * (double) var25, this.posY - this.motionY * (double) var25, this.posZ - this.motionZ * (double) var25, this.motionX, this.motionY, this.motionZ);
            }

            var23 = 0.8F;
        }

        this.motionX *= (double) var23;
        this.motionY *= (double) var23;
        this.motionZ *= (double) var23;
        this.setPositionAndRotation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound var1)
    {
        var1.setShort("xTile", (short) this.xTileSnowball);
        var1.setShort("yTile", (short) this.yTileSnowball);
        var1.setShort("zTile", (short) this.zTileSnowball);
        var1.setByte("inTile", (byte) this.inTileSnowball);
        var1.setByte("shake", (byte) this.shakeSnowball);
        var1.setByte("inGround", (byte) (this.inGroundSnowball ? 1 : 0));
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound var1)
    {
        this.xTileSnowball = var1.getShort("xTile");
        this.yTileSnowball = var1.getShort("yTile");
        this.zTileSnowball = var1.getShort("zTile");
        this.inTileSnowball = var1.getByte("inTile") & 255;
        this.shakeSnowball = var1.getByte("shake") & 255;
        this.inGroundSnowball = var1.getByte("inGround") == 1;
    }

    /**
     * Called by a player entity when they collide with an entity
     */
    public void onCollideWithPlayer(EntityPlayer var1)
    {
        if (this.inGroundSnowball && this.thrower == var1 && this.shakeSnowball <= 0 && var1.inventory.addItemStackToInventory(new ItemStack(Item.arrow, 1)))
        {
            this.worldObj.playSoundAtEntity(this, "random.pop", 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
            var1.onItemPickup(this, 1);
            this.setDead();
        }
    }

    public float getShadowSize()
    {
        return 0.0F;
    }

    /**
     * Similar to setArrowHeading, it's point the throwable entity to a x, y, z direction.
     */
    public void setThrowableHeading(double var1, double var3, double var5, float var7, float var8)
    {}
}
