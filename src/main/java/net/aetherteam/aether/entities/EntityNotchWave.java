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
import net.minecraft.entity.EntityLivingBase;
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
    private EntityLivingBase thrower;
    private int ticksInGroundSnowball;
    private int ticksInAirSnowball;
    public ArrayList<Integer> harvestBlockBans;

    public EntityNotchWave(World world)
    {
        super(world);
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
    public boolean isInRangeToRenderDist(double d)
    {
        double d1 = this.boundingBox.getAverageEdgeLength() * 4.0D;
        d1 *= 64.0D;
        return d < d1 * d1;
    }

    public EntityNotchWave(World world, EntityLivingBase entityliving)
    {
        this(world);
        this.thrower = entityliving;
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
        this.setThrowableHeading(this.motionX, this.motionY, this.motionZ, 1.5F, 1.0F);
    }

    public EntityNotchWave(World world, double d, double d1, double d2)
    {
        this(world);
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

        if (this.ticksInAirSnowball > 100)
        {
            this.setDead();
        }

        if (this.inGroundSnowball)
        {
            this.setDead();
        }
        else
        {
            ++this.ticksInAirSnowball;
        }

        Vec3 vec3d = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
        Vec3 vec3d1 = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
        MovingObjectPosition movingobjectposition = this.worldObj.clip(vec3d, vec3d1);
        vec3d = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
        vec3d1 = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

        if (movingobjectposition != null)
        {
            vec3d1 = Vec3.createVectorHelper(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord);
        }

        int f3;

        if (!this.worldObj.isRemote)
        {
            Entity f = null;
            List f1 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(4.0D, 4.0D, 4.0D));
            double f2 = 0.0D;

            for (f3 = 0; f3 < f1.size(); ++f3)
            {
                Entity i1 = (Entity)f1.get(f3);

                if (i1.canBeCollidedWith() && (i1 != this.thrower || this.ticksInAirSnowball >= 5))
                {
                    float j1 = 0.3F;

                    if ((!(i1 instanceof EntityPhyg) || ((EntityPhyg)i1).getSaddled()) && (!(i1 instanceof EntityMoa) || ((EntityMoa)i1).getSaddled()) && (!(i1 instanceof EntityAerbunny) || !(((EntityAerbunny)i1).riddenByEntity instanceof EntityPlayer)) && i1 != this.thrower)
                    {
                        i1.attackEntityFrom(DamageSource.causeMobDamage(this.thrower), 5.0F);
                    }

                    AxisAlignedBB prevBlockId = i1.boundingBox.expand((double)j1, (double)j1, (double)j1);
                    MovingObjectPosition prevBlockMeta = prevBlockId.calculateIntercept(vec3d, vec3d1);

                    if (prevBlockMeta != null)
                    {
                        double d1 = vec3d.distanceTo(prevBlockMeta.hitVec);

                        if (d1 < f2 || f2 == 0.0D)
                        {
                            f = i1;
                            f2 = d1;
                        }
                    }
                }
            }

            for (f3 = (int)(this.posX - 3.0D); (double)f3 <= this.posX + 3.0D; ++f3)
            {
                for (int var21 = (int)(this.posY - 3.0D); (double)var21 <= this.posY + 3.0D; ++var21)
                {
                    for (int var24 = (int)(this.posZ - 3.0D); (double)var24 <= this.posZ + 3.0D; ++var24)
                    {
                        if (Block.blocksList[this.worldObj.getBlockId(f3, var21, var24)] instanceof BlockFlower && this.thrower != null && this.thrower instanceof EntityPlayer)
                        {
                            int var23 = this.worldObj.getBlockId(f3, var21, var24);
                            int var25 = this.worldObj.getBlockMetadata(f3, var21, var24);

                            if (!this.harvestBlockBans.contains(Integer.valueOf(this.worldObj.getBlockId(f3, var21, var24))))
                            {
                                Block.blocksList[this.worldObj.getBlockId(f3, var21, var24)].harvestBlock(this.thrower.worldObj, (EntityPlayer)this.thrower, f3, var21, var24, this.worldObj.getBlockMetadata(f3, var21, var24));

                                if (this.worldObj.getBlockId(f3, var21, var24) == var23 && this.worldObj.getBlockMetadata(f3, var21, var24) == var25)
                                {
                                    Block.blocksList[this.worldObj.getBlockId(f3, var21, var24)].removeBlockByPlayer(this.thrower.worldObj, (EntityPlayer)this.thrower, f3, var21, var24);
                                }
                            }
                        }
                    }
                }
            }

            if (f != null)
            {
                movingobjectposition = new MovingObjectPosition(f);
            }
        }

        int k;

        if (movingobjectposition != null)
        {
            int var15;

            for (var15 = (int)(this.posX - 3.0D); (double)var15 <= this.posX + 3.0D; ++var15)
            {
                for (int var16 = (int)(this.posY - 3.0D); (double)var16 <= this.posY + 3.0D; ++var16)
                {
                    for (int var19 = (int)(this.posZ - 3.0D); (double)var19 <= this.posZ + 3.0D; ++var19)
                    {
                        if (Block.blocksList[this.worldObj.getBlockId(var15, var16, var19)] instanceof BlockFlower && this.thrower != null && this.thrower instanceof EntityPlayer)
                        {
                            k = this.worldObj.getBlockId(var15, var16, var19);
                            f3 = this.worldObj.getBlockMetadata(var15, var16, var19);

                            if (!this.harvestBlockBans.contains(Integer.valueOf(this.worldObj.getBlockId(var15, var16, var19))))
                            {
                                Block.blocksList[this.worldObj.getBlockId(var15, var16, var19)].harvestBlock(this.thrower.worldObj, (EntityPlayer)this.thrower, var15, var16, var19, this.worldObj.getBlockMetadata(var15, var16, var19));

                                if (this.worldObj.getBlockId(var15, var16, var19) == k && this.worldObj.getBlockMetadata(var15, var16, var19) == f3)
                                {
                                    Block.blocksList[this.worldObj.getBlockId(var15, var16, var19)].removeBlockByPlayer(this.thrower.worldObj, (EntityPlayer)this.thrower, var15, var16, var19);
                                }
                            }
                        }
                    }
                }
            }

            if ((movingobjectposition.entityHit == null || (!(movingobjectposition.entityHit instanceof EntityPhyg) || ((EntityPhyg)movingobjectposition.entityHit).getSaddled()) && (!(movingobjectposition.entityHit instanceof EntityMoa) || ((EntityMoa)movingobjectposition.entityHit).getSaddled()) && (!(movingobjectposition.entityHit instanceof EntityAerbunny) || !(((EntityAerbunny)movingobjectposition.entityHit).riddenByEntity instanceof EntityPlayer))) && movingobjectposition.entityHit != null && movingobjectposition.entityHit != this.thrower)
            {
                if (!movingobjectposition.entityHit.attackEntityFrom(DamageSource.causeMobDamage(this.thrower), 0.0F))
                {
                    ;
                }

                movingobjectposition.entityHit.addVelocity(this.motionX, 0.6D, this.motionZ);
            }

            for (var15 = 0; var15 < 8; ++var15)
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
        float var17 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
        this.rotationYaw = (float)(Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);

        for (this.rotationPitch = (float)(Math.atan2(this.motionY, (double)var17) * 180.0D / Math.PI); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F)
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
        float var20 = 0.03F;

        if (this.isInWater())
        {
            for (k = 0; k < 4; ++k)
            {
                float var22 = 0.25F;
                this.worldObj.spawnParticle("bubble", this.posX - this.motionX * (double)var22, this.posY - this.motionY * (double)var22, this.posZ - this.motionZ * (double)var22, this.motionX, this.motionY, this.motionZ);
            }

            var18 = 0.8F;
        }

        this.motionX *= (double)var18;
        this.motionY *= (double)var18;
        this.motionZ *= (double)var18;
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

    /**
     * Similar to setArrowHeading, it's point the throwable entity to a x, y, z direction.
     */
    public void setThrowableHeading(double var1, double var3, double var5, float var7, float var8) {}
}
