package net.aetherteam.aether.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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

public class EntityNotchWave extends Entity
    implements IProjectile
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
    public ArrayList harvestBlockBans = new ArrayList();

    public EntityNotchWave(World world)
    {
        super(world);
        this.xTileSnowball = -1;
        this.yTileSnowball = -1;
        this.zTileSnowball = -1;
        this.inTileSnowball = 0;
        this.inGroundSnowball = false;
        this.shakeSnowball = 0;
        this.ticksInAirSnowball = 0;
        setSize(0.25F, 0.25F);
        this.harvestBlockBans.add(Integer.valueOf(AetherBlocks.BerryBushStem.blockID));
    }

    protected void entityInit()
    {
    }

    public boolean isInRangeToRenderDist(double d)
    {
        double d1 = this.boundingBox.getAverageEdgeLength() * 4.0D;
        d1 *= 64.0D;
        return d < d1 * d1;
    }

    public EntityNotchWave(World world, EntityLiving entityliving)
    {
        this(world);
        this.thrower = entityliving;
        setLocationAndAngles(entityliving.posX, entityliving.posY + entityliving.getEyeHeight(), entityliving.posZ, entityliving.rotationYaw, entityliving.rotationPitch);
        this.posX -= MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * 0.16F;
        this.posY -= 0.1000000014901161D;
        this.posZ -= MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * 0.16F;
        setPositionAndRotation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
        this.yOffset = 0.0F;
        float f = 0.4F;
        this.motionX = (-MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI) * f);
        this.motionZ = (MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI) * f);
        this.motionY = (-MathHelper.sin(this.rotationPitch / 180.0F * (float)Math.PI) * f);
        setSnowballHeading(this.motionX, this.motionY, this.motionZ, 1.5F, 1.0F);
        setThrowableHeading(this.motionX, this.motionY, this.motionZ, 1.5F, 1.0F);
    }

    public EntityNotchWave(World world, double d, double d1, double d2)
    {
        this(world);
        setPositionAndRotation(d, d1, d2, this.rotationYaw, this.rotationPitch);
        this.yOffset = 0.0F;
    }

    public void setSnowballHeading(double d, double d1, double d2, float f, float f1)
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
        this.ticksInGroundSnowball = 0;
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
        }
    }

    public void onUpdate()
    {
        this.lastTickPosX = this.posX;
        this.lastTickPosY = this.posY;
        this.lastTickPosZ = this.posZ;
        super.onUpdate();

        if (this.shakeSnowball > 0)
        {
            this.shakeSnowball -= 1;
        }

        if (this.ticksInAirSnowball > 100)
        {
            setDead();
        }

        if (this.inGroundSnowball)
        {
            setDead();
        }
        else
        {
            this.ticksInAirSnowball += 1;
        }

        Vec3 vec3d = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
        Vec3 vec3d1 = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
        MovingObjectPosition movingobjectposition = this.worldObj.rayTraceBlocks(vec3d, vec3d1);
        vec3d = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
        vec3d1 = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

        if (movingobjectposition != null)
        {
            vec3d1 = Vec3.createVectorHelper(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord);
        }

        if (!this.worldObj.isRemote)
        {
            Entity entity = null;
            List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(4.0D, 4.0D, 4.0D));
            double d = 0.0D;

            for (int l = 0; l < list.size(); l++)
            {
                Entity entity1 = (Entity)list.get(l);

                if ((entity1.canBeCollidedWith()) && ((entity1 != this.thrower) || (this.ticksInAirSnowball >= 5)))
                {
                    float f4 = 0.3F;

                    if (((!(entity1 instanceof EntityPhyg)) || (((EntityPhyg)entity1).getSaddled())) && ((!(entity1 instanceof EntityMoa)) || (((EntityMoa)entity1).getSaddled())) && ((!(entity1 instanceof EntityAerbunny)) || (!(((EntityAerbunny)entity1).riddenByEntity instanceof EntityPlayer))))
                    {
                        if (entity1 != this.thrower)
                        {
                            entity1.attackEntityFrom(DamageSource.causeMobDamage(this.thrower), 5);
                        }
                    }

                    AxisAlignedBB axisalignedbb = entity1.boundingBox.expand(f4, f4, f4);
                    MovingObjectPosition movingobjectposition1 = axisalignedbb.calculateIntercept(vec3d, vec3d1);

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

            for (int l = (int)(this.posX - 3.0D); l <= this.posX + 3.0D; l++)
            {
                for (int i1 = (int)(this.posY - 3.0D); i1 <= this.posY + 3.0D; i1++)
                {
                    for (int j1 = (int)(this.posZ - 3.0D); j1 <= this.posZ + 3.0D; j1++)
                    {
                        if (((Block.blocksList[this.worldObj.getBlockId(l, i1, j1)] instanceof BlockFlower)) && (this.thrower != null) && ((this.thrower instanceof EntityPlayer)))
                        {
                            int prevBlockId = this.worldObj.getBlockId(l, i1, j1);
                            int prevBlockMeta = this.worldObj.getBlockMetadata(l, i1, j1);

                            if (!this.harvestBlockBans.contains(Integer.valueOf(this.worldObj.getBlockId(l, i1, j1))))
                            {
                                Block.blocksList[this.worldObj.getBlockId(l, i1, j1)].harvestBlock(this.thrower.worldObj, (EntityPlayer)this.thrower, l, i1, j1, this.worldObj.getBlockMetadata(l, i1, j1));

                                if ((this.worldObj.getBlockId(l, i1, j1) == prevBlockId) && (this.worldObj.getBlockMetadata(l, i1, j1) == prevBlockMeta))
                                {
                                    Block.blocksList[this.worldObj.getBlockId(l, i1, j1)].removeBlockByPlayer(this.thrower.worldObj, (EntityPlayer)this.thrower, l, i1, j1);
                                }
                            }
                        }
                    }
                }
            }

            if (entity != null)
            {
                movingobjectposition = new MovingObjectPosition(entity);
            }
        }

        if (movingobjectposition != null)
        {
            for (int l = (int)(this.posX - 3.0D); l <= this.posX + 3.0D; l++)
            {
                for (int i1 = (int)(this.posY - 3.0D); i1 <= this.posY + 3.0D; i1++)
                {
                    for (int j1 = (int)(this.posZ - 3.0D); j1 <= this.posZ + 3.0D; j1++)
                    {
                        if (((Block.blocksList[this.worldObj.getBlockId(l, i1, j1)] instanceof BlockFlower)) && (this.thrower != null) && ((this.thrower instanceof EntityPlayer)))
                        {
                            int prevBlockId = this.worldObj.getBlockId(l, i1, j1);
                            int prevBlockMeta = this.worldObj.getBlockMetadata(l, i1, j1);

                            if (!this.harvestBlockBans.contains(Integer.valueOf(this.worldObj.getBlockId(l, i1, j1))))
                            {
                                Block.blocksList[this.worldObj.getBlockId(l, i1, j1)].harvestBlock(this.thrower.worldObj, (EntityPlayer)this.thrower, l, i1, j1, this.worldObj.getBlockMetadata(l, i1, j1));

                                if ((this.worldObj.getBlockId(l, i1, j1) == prevBlockId) && (this.worldObj.getBlockMetadata(l, i1, j1) == prevBlockMeta))
                                {
                                    Block.blocksList[this.worldObj.getBlockId(l, i1, j1)].removeBlockByPlayer(this.thrower.worldObj, (EntityPlayer)this.thrower, l, i1, j1);
                                }
                            }
                        }
                    }
                }
            }

            if ((movingobjectposition.entityHit == null) || (((!(movingobjectposition.entityHit instanceof EntityPhyg)) || (((EntityPhyg)movingobjectposition.entityHit).getSaddled())) && ((!(movingobjectposition.entityHit instanceof EntityMoa)) || (((EntityMoa)movingobjectposition.entityHit).getSaddled())) && ((!(movingobjectposition.entityHit instanceof EntityAerbunny)) || (!(((EntityAerbunny)movingobjectposition.entityHit).riddenByEntity instanceof EntityPlayer)))))
            {
                if ((movingobjectposition.entityHit != null) && (movingobjectposition.entityHit != this.thrower))
                {
                    if (!movingobjectposition.entityHit.attackEntityFrom(DamageSource.causeMobDamage(this.thrower), 0));

                    movingobjectposition.entityHit.addVelocity(this.motionX, 0.6D, this.motionZ);
                }
            }

            for (int j = 0; j < 8; j++)
            {
                this.worldObj.spawnParticle("explode", this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);
                this.worldObj.spawnParticle("explode", this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);
                this.worldObj.spawnParticle("smoke", this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);
                this.worldObj.spawnParticle("largesmoke", this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);
                this.worldObj.spawnParticle("flame", this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);
            }

            setDead();
        }

        this.posX += this.motionX;
        this.posY += this.motionY;
        this.posZ += this.motionZ;
        float f = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
        this.rotationYaw = ((float)(Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI));

        for (this.rotationPitch = ((float)(Math.atan2(this.motionY, f) * 180.0D / Math.PI)); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F);

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
        float f1 = 0.99F;
        float f2 = 0.03F;

        if (isInWater())
        {
            for (int k = 0; k < 4; k++)
            {
                float f3 = 0.25F;
                this.worldObj.spawnParticle("bubble", this.posX - this.motionX * f3, this.posY - this.motionY * f3, this.posZ - this.motionZ * f3, this.motionX, this.motionY, this.motionZ);
            }

            f1 = 0.8F;
        }

        this.motionX *= f1;
        this.motionY *= f1;
        this.motionZ *= f1;
        setPositionAndRotation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
    }

    public void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        nbttagcompound.setShort("xTile", (short)this.xTileSnowball);
        nbttagcompound.setShort("yTile", (short)this.yTileSnowball);
        nbttagcompound.setShort("zTile", (short)this.zTileSnowball);
        nbttagcompound.setByte("inTile", (byte)this.inTileSnowball);
        nbttagcompound.setByte("shake", (byte)this.shakeSnowball);
        nbttagcompound.setByte("inGround", (byte)(this.inGroundSnowball ? 1 : 0));
    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        this.xTileSnowball = nbttagcompound.getShort("xTile");
        this.yTileSnowball = nbttagcompound.getShort("yTile");
        this.zTileSnowball = nbttagcompound.getShort("zTile");
        this.inTileSnowball = (nbttagcompound.getByte("inTile") & 0xFF);
        this.shakeSnowball = (nbttagcompound.getByte("shake") & 0xFF);
        this.inGroundSnowball = (nbttagcompound.getByte("inGround") == 1);
    }

    public void onCollideWithPlayer(EntityPlayer entityplayer)
    {
        if ((this.inGroundSnowball) && (this.thrower == entityplayer) && (this.shakeSnowball <= 0) && (entityplayer.inventory.addItemStackToInventory(new ItemStack(Item.arrow, 1))))
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

    public void setThrowableHeading(double var1, double var3, double var5, float var7, float var8)
    {
    }
}

