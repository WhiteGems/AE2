package net.aetherteam.aether.entities.bosses;

import java.util.Random;
import net.aetherteam.aether.entities.EntityBattleSentry;
import net.aetherteam.aether.entities.EntitySentry;
import net.aetherteam.aether.entities.EntitySentryGolem;
import net.aetherteam.aether.entities.EntityTrackingGolem;
import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.EntityLiving;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntityCog extends EntityFlying
{
    public float rotationx;
    public float rotationy;
    public float rotationz;
    private EntityLiving parent;
    public double smotionX;
    public double smotionY;
    public double smotionZ;
    public int life;
    public int lifeSpan;
    private static final double topSpeed = 0.125D;
    private static final float sponge = (180F / (float)Math.PI);

    public EntityCog(World world)
    {
        super(world);
        this.texture = "/aether/mobs/cog.png";
        this.lifeSpan = 200;
        this.life = this.lifeSpan;
        setSize(0.9F, 0.9F);
        this.isImmuneToFire = true;
    }

    public void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(16, Byte.valueOf((byte)0));
    }

    public boolean isLarge()
    {
        return this.dataWatcher.getWatchableObjectByte(16) == 1;
    }

    public void setLarge(boolean large)
    {
        if (large)
        {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte)1));
        }
        else
        {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte)0));
        }
    }

    public int getMaxHealth()
    {
        return 5;
    }

    public EntityCog(World world, double x, double y, double z, boolean large, EntityLiving parent)
    {
        super(world);
        this.texture = "/aether/mobs/cog.png";
        this.lifeSpan = 200;
        this.life = this.lifeSpan;
        setSize(0.9F, 0.9F);
        setPositionAndRotation(x, y, z, this.rotationYaw, this.rotationPitch);
        this.isImmuneToFire = true;
        this.smotionX = ((0.2D + this.rand.nextFloat() * 0.15D) * (this.rand.nextInt(2) == 0 ? 1.0D : -1.0D));
        this.smotionY = ((0.2D + this.rand.nextFloat() * 0.15D) * (this.rand.nextInt(2) == 0 ? 1.0D : -1.0D));
        this.smotionZ = ((0.2D + this.rand.nextFloat() * 0.15D) * (this.rand.nextInt(2) == 0 ? 1.0D : -1.0D));
        setLarge(large);
        this.parent = parent;
    }

    public void onUpdate()
    {
        super.onUpdate();
        this.life -= 1;

        if ((this.life <= 0) && (!isLarge()))
        {
            this.isDead = true;
        }

        if ((this.parent != null) && (this.parent.isDead) && (isLarge()))
        {
            this.isDead = true;
        }
    }

    public void splode()
    {
        this.worldObj.playSoundAtEntity(this, "aemob.cog.wallFinal", 2.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.2F);

        for (int j = 0; j < 40; j++)
        {
            double a = (this.rand.nextFloat() - 0.5F) * 0.5F;
            double b = (this.rand.nextFloat() - 0.5F) * 0.5F;
            double c = (this.rand.nextFloat() - 0.5F) * 0.5F;
            this.worldObj.spawnParticle("reddust", this.posX, this.posY, this.posZ, a, b, c);
        }
    }

    public void updateEntityActionState()
    {
        this.motionX = this.smotionX;
        this.motionY = this.smotionY;
        this.motionZ = this.smotionZ;

        if (this.isCollided)
        {
            int i = MathHelper.floor_double(this.posX);
            int j = MathHelper.floor_double(this.boundingBox.minY);
            int k = MathHelper.floor_double(this.posZ);

            if ((this.smotionX > 0.0D) && (this.worldObj.getBlockId(i + 1, j, k) != 0))
            {
                this.motionX = (this.smotionX = -this.smotionX);
            }
            else if ((this.smotionX < 0.0D) && (this.worldObj.getBlockId(i - 1, j, k) != 0))
            {
                this.motionX = (this.smotionX = -this.smotionX);
            }

            if ((this.smotionY > 0.0D) && (this.worldObj.getBlockId(i, j + 1, k) != 0))
            {
                this.motionY = (this.smotionY = -this.smotionY);
            }
            else if ((this.smotionY < 0.0D) && (this.worldObj.getBlockId(i, j - 1, k) != 0))
            {
                this.motionY = (this.smotionY = -this.smotionY);
            }

            if ((this.smotionZ > 0.0D) && (this.worldObj.getBlockId(i, j, k + 1) != 0))
            {
                this.motionZ = (this.smotionZ = -this.smotionZ);
            }
            else if ((this.smotionZ < 0.0D) && (this.worldObj.getBlockId(i, j, k - 1) != 0))
            {
                this.motionZ = (this.smotionZ = -this.smotionZ);
            }

            splode();
        }
    }

    public void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setShort("LifeLeft", (short)this.life);
        nbttagcompound.setTag("motion", newDoubleNBTList(new double[] { this.smotionX, this.smotionY, this.smotionZ }));
        nbttagcompound.setBoolean("Large", isLarge());
    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readEntityFromNBT(nbttagcompound);
        this.life = nbttagcompound.getShort("LifeLeft");
        NBTTagList nbttaglist = nbttagcompound.getTagList("motion");
        this.smotionX = ((float)((NBTTagDouble)nbttaglist.tagAt(0)).data);
        this.smotionY = ((float)((NBTTagDouble)nbttaglist.tagAt(1)).data);
        this.smotionZ = ((float)((NBTTagDouble)nbttaglist.tagAt(2)).data);
        setLarge(nbttagcompound.getBoolean("Large"));
    }

    public void applyEntityCollision(Entity entity)
    {
        super.applyEntityCollision(entity);

        if ((entity != null) && ((entity instanceof EntityLiving)) && (!(entity instanceof EntityCog)) && (!(entity instanceof EntityLabyrinthEye)))
        {
            if (((entity instanceof EntitySentry)) || ((entity instanceof EntityTrackingGolem)) || ((entity instanceof EntitySliderHostMimic)) || ((entity instanceof EntitySentryGuardian)) || ((entity instanceof EntitySentryGolem)) || ((entity instanceof EntityBattleSentry)) || ((entity instanceof EntitySlider)) || ((entity instanceof EntityMiniSlider)))
            {
                return;
            }

            entity.attackEntityFrom(DamageSource.causeThrownDamage(this, this.parent), 5);
        }
    }

    public boolean attackEntityFrom(DamageSource ds, int i)
    {
        if (ds.getEntity() != null)
        {
            Vec3 vec3d = ds.getEntity().getLookVec();

            if (vec3d != null)
            {
                this.smotionX = vec3d.xCoord;
                this.smotionY = vec3d.yCoord;
                this.smotionZ = vec3d.zCoord;
            }

            return true;
        }

        return false;
    }

    public void setThrowableHeading(double par1, double par3, double par5, float par7, float par8)
    {
        float var9 = MathHelper.sqrt_double(par1 * par1 + par3 * par3 + par5 * par5);
        par1 /= var9;
        par3 /= var9;
        par5 /= var9;
        par1 += this.rand.nextGaussian() * 0.007499999832361937D * par8;
        par3 += this.rand.nextGaussian() * 0.007499999832361937D * par8;
        par5 += this.rand.nextGaussian() * 0.007499999832361937D * par8;
        par1 *= par7;
        par3 *= par7;
        par5 *= par7;
        this.motionX = par1;
        this.motionY = par3;
        this.motionZ = par5;
        float var10 = MathHelper.sqrt_double(par1 * par1 + par5 * par5);
        this.prevRotationYaw = (this.rotationYaw = (float)(Math.atan2(par1, par5) * 180.0D / Math.PI));
        this.prevRotationPitch = (this.rotationPitch = (float)(Math.atan2(par3, var10) * 180.0D / Math.PI));
    }
}

