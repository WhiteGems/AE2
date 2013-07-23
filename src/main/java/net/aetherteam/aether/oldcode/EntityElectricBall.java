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
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntityElectricBall extends EntityLiving
    implements IThrowableEntity
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

    public EntityElectricBall(World world)
    {
        super(world);
        this.texture = "/net/aetherteam/aether/client/sprites/projectiles/electroball/electroball.png";
        this.xTile = -1;
        this.yTile = -1;
        this.zTile = -1;
        this.inTile = 0;
        this.inGround = false;
        this.field_9406_a = 0;
        this.ticksInAir = 0;
        setSize(1.0F, 1.0F);

        for (int i = 0; i < 3; i++)
        {
            this.sinage[i] = (this.random.nextFloat() * 6.0F);
        }
    }

    public void updateAnims()
    {
        for (int i = 0; i < 3; i++)
        {
            this.sinage[i] += 0.3F + i * 0.13F;

            if (this.sinage[i] > ((float)Math.PI * 2F))
            {
                this.sinage[i] -= ((float)Math.PI * 2F);
            }
        }
    }

    public EntityElectricBall(World world, EntityLiving entityliving, double d, double d1, double d2)
    {
        super(world);
        this.xTile = -1;
        this.yTile = -1;
        this.zTile = -1;
        this.inTile = 0;
        this.inGround = false;
        this.field_9406_a = 0;
        this.ticksInAir = 0;
        this.shootingEntity = entityliving;
        setSize(0.5F, 0.5F);
        setLocationAndAngles(entityliving.posX, entityliving.posY, entityliving.posZ, entityliving.rotationYaw, entityliving.rotationPitch);
        setPositionAndRotation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
        this.yOffset = 0.0F;
        this.motionX = (this.motionY = this.motionZ = 0.0D);
        d += this.rand.nextGaussian() * 0.4D;
        d1 += this.rand.nextGaussian() * 0.4D;
        d2 += this.rand.nextGaussian() * 0.4D;
        double d3 = MathHelper.sqrt_double(d * d + d1 * d1 + d2 * d2);
        this.accelerationX = (d / d3 * 0.15D);
        this.accelerationY = (d1 / d3 * 0.1D);
        this.accelerationZ = (d2 / d3 * 0.15D);
    }

    public void onLivingUpdate()
    {
    }

    public void onEntityUpdate()
    {
        super.onEntityUpdate();
        updateAnims();

        if ((this.motionX * this.motionY * this.motionZ < 0.1D) && (this.ticksInAir > 40))
        {
            if (!this.worldObj.isRemote)
            {
                setDead();
            }
        }

        if (this.ticksInAir > 200)
        {
            if (!this.worldObj.isRemote)
            {
                setDead();
            }
        }

        if (this.field_9406_a > 0)
        {
            this.field_9406_a -= 1;
        }

        if (this.inGround)
        {
            int i = this.worldObj.getBlockId(this.xTile, this.yTile, this.zTile);

            if (i != this.inTile)
            {
                this.inGround = false;
                this.motionX *= this.rand.nextFloat() * 0.2F;
                this.motionY *= this.rand.nextFloat() * 0.2F;
                this.motionZ *= this.rand.nextFloat() * 0.2F;
                this.ticksAlive = 0;
                this.ticksInAir = 0;
            }
            else
            {
                this.ticksAlive += 1;
                return;
            }
        }
        else
        {
            this.ticksInAir += 1;
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

        Entity entity = null;
        List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
        double d = 0.0D;

        for (int j = 0; j < list.size(); j++)
        {
            Entity entity1 = (Entity)list.get(j);

            if ((entity1.canBeCollidedWith()) && ((entity1 != this.shootingEntity) || (this.ticksInAir >= 25)))
            {
                float f2 = 0.3F;
                AxisAlignedBB axisalignedbb = entity1.boundingBox.expand(f2, f2, f2);
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

        if (entity != null)
        {
            movingobjectposition = new MovingObjectPosition(entity);
        }

        if (movingobjectposition != null)
        {
            if (movingobjectposition.entityHit != null)
            {
                if (movingobjectposition.entityHit != this.shootingEntity)
                {
                    if ((movingobjectposition.entityHit.attackEntityFrom(DamageSource.causeMobDamage(this.shootingEntity), 0)) || (
                                ((movingobjectposition.entityHit instanceof EntityPlayer)) && (((EntityPlayer)movingobjectposition.entityHit).inventory.armorInventory[0] != null) && (((EntityPlayer)movingobjectposition.entityHit).inventory.armorInventory[0].itemID == AetherItems.SentryBoots.itemID)))
                    {
                        setDead();
                    }
                    else if (((movingobjectposition.entityHit instanceof EntityPlayer)) && (((EntityPlayer)movingobjectposition.entityHit).capabilities.isCreativeMode))
                    {
                        setDead();
                    }
                    else if ((movingobjectposition.entityHit != null) && (!(movingobjectposition.entityHit instanceof EntityNewZephyr)) && (!(movingobjectposition.entityHit instanceof EntityTempest)))
                    {
                        int x = MathHelper.floor_double(movingobjectposition.entityHit.boundingBox.minX);
                        int y = MathHelper.floor_double(movingobjectposition.entityHit.boundingBox.minY);
                        int z = MathHelper.floor_double(movingobjectposition.entityHit.boundingBox.minZ);
                        EntityLightningBolt entitylightningbolt = new EntityLightningBolt(this.worldObj, x, y, z);
                        entitylightningbolt.setLocationAndAngles(x, y, z, this.rotationYaw, 0.0F);
                        this.worldObj.spawnEntityInWorld(entitylightningbolt);
                    }
                }
            }
            else
            {
                int i = MathHelper.floor_double(this.posX);
                int j = MathHelper.floor_double(this.posY);
                int k = MathHelper.floor_double(this.posZ);
                EntityLightningBolt entitylightningbolt = new EntityLightningBolt(this.worldObj, this.posX, this.posY, this.posZ);
                entitylightningbolt.setLocationAndAngles(i, j, k, this.rotationYaw, 0.0F);
                this.worldObj.spawnEntityInWorld(entitylightningbolt);
            }

            if (!this.worldObj.isRemote)
            {
                setDead();
            }
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
        float f1 = 0.95F;

        if (handleWaterMovement())
        {
            for (int k = 0; k < 4; k++)
            {
                float f3 = 0.25F;
                this.worldObj.spawnParticle("bubble", this.posX - this.motionX * f3, this.posY - this.motionY * f3, this.posZ - this.motionZ * f3, this.motionX, this.motionY, this.motionZ);
            }

            f1 = 0.8F;
        }

        this.motionX += this.accelerationX;
        this.motionY += this.accelerationY;
        this.motionZ += this.accelerationZ;
        this.motionX *= f1;
        this.motionY *= f1;
        this.motionZ *= f1;
        this.worldObj.spawnParticle("smoke", this.posX, this.posY + 0.5D, this.posZ, 0.0D, 0.0D, 0.0D);
        setPositionAndRotation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
    }

    public void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        nbttagcompound.setShort("xTile", (short)this.xTile);
        nbttagcompound.setShort("yTile", (short)this.yTile);
        nbttagcompound.setShort("zTile", (short)this.zTile);
        nbttagcompound.setByte("inTile", (byte)this.inTile);
        nbttagcompound.setByte("shake", (byte)this.field_9406_a);
        nbttagcompound.setByte("inGround", (byte)(this.inGround ? 1 : 0));
    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        this.xTile = nbttagcompound.getShort("xTile");
        this.yTile = nbttagcompound.getShort("yTile");
        this.zTile = nbttagcompound.getShort("zTile");
        this.inTile = (nbttagcompound.getByte("inTile") & 0xFF);
        this.field_9406_a = (nbttagcompound.getByte("shake") & 0xFF);
        this.inGround = (nbttagcompound.getByte("inGround") == 1);
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

    public void setThrower(Entity entity)
    {
    }

    public int getMaxHealth()
    {
        return 0;
    }
}

