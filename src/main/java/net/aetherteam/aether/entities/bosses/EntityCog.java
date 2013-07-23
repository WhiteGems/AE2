package net.aetherteam.aether.entities.bosses;

import net.aetherteam.aether.entities.EntityBattleSentry;
import net.aetherteam.aether.entities.EntitySentry;
import net.aetherteam.aether.entities.EntitySentryGolem;
import net.aetherteam.aether.entities.EntityTrackingGolem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.EntityLiving;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagList;
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

    public EntityCog(World var1)
    {
        super(var1);
        this.texture = "/aether/mobs/cog.png";
        this.lifeSpan = 200;
        this.life = this.lifeSpan;
        this.setSize(0.9F, 0.9F);
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

    public void setLarge(boolean var1)
    {
        if (var1)
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

    public EntityCog(World var1, double var2, double var4, double var6, boolean var8, EntityLiving var9)
    {
        super(var1);
        this.texture = "/aether/mobs/cog.png";
        this.lifeSpan = 200;
        this.life = this.lifeSpan;
        this.setSize(0.9F, 0.9F);
        this.setPositionAndRotation(var2, var4, var6, this.rotationYaw, this.rotationPitch);
        this.isImmuneToFire = true;
        this.smotionX = (0.2D + (double)this.rand.nextFloat() * 0.15D) * (this.rand.nextInt(2) == 0 ? 1.0D : -1.0D);
        this.smotionY = (0.2D + (double)this.rand.nextFloat() * 0.15D) * (this.rand.nextInt(2) == 0 ? 1.0D : -1.0D);
        this.smotionZ = (0.2D + (double)this.rand.nextFloat() * 0.15D) * (this.rand.nextInt(2) == 0 ? 1.0D : -1.0D);
        this.setLarge(var8);
        this.parent = var9;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();
        --this.life;

        if (this.life <= 0 && !this.isLarge())
        {
            this.isDead = true;
        }

        if (this.parent != null && this.parent.isDead && this.isLarge())
        {
            this.isDead = true;
        }
    }

    public void splode()
    {
        this.worldObj.playSoundAtEntity(this, "aemob.cog.wallFinal", 2.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.2F);

        for (int var1 = 0; var1 < 40; ++var1)
        {
            double var2 = (double)((this.rand.nextFloat() - 0.5F) * 0.5F);
            double var4 = (double)((this.rand.nextFloat() - 0.5F) * 0.5F);
            double var6 = (double)((this.rand.nextFloat() - 0.5F) * 0.5F);
            this.worldObj.spawnParticle("reddust", this.posX, this.posY, this.posZ, var2, var4, var6);
        }
    }

    public void updateEntityActionState()
    {
        this.motionX = this.smotionX;
        this.motionY = this.smotionY;
        this.motionZ = this.smotionZ;

        if (this.isCollided)
        {
            int var1 = MathHelper.floor_double(this.posX);
            int var2 = MathHelper.floor_double(this.boundingBox.minY);
            int var3 = MathHelper.floor_double(this.posZ);

            if (this.smotionX > 0.0D && this.worldObj.getBlockId(var1 + 1, var2, var3) != 0)
            {
                this.motionX = this.smotionX = -this.smotionX;
            }
            else if (this.smotionX < 0.0D && this.worldObj.getBlockId(var1 - 1, var2, var3) != 0)
            {
                this.motionX = this.smotionX = -this.smotionX;
            }

            if (this.smotionY > 0.0D && this.worldObj.getBlockId(var1, var2 + 1, var3) != 0)
            {
                this.motionY = this.smotionY = -this.smotionY;
            }
            else if (this.smotionY < 0.0D && this.worldObj.getBlockId(var1, var2 - 1, var3) != 0)
            {
                this.motionY = this.smotionY = -this.smotionY;
            }

            if (this.smotionZ > 0.0D && this.worldObj.getBlockId(var1, var2, var3 + 1) != 0)
            {
                this.motionZ = this.smotionZ = -this.smotionZ;
            }
            else if (this.smotionZ < 0.0D && this.worldObj.getBlockId(var1, var2, var3 - 1) != 0)
            {
                this.motionZ = this.smotionZ = -this.smotionZ;
            }

            this.splode();
        }
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound var1)
    {
        super.writeEntityToNBT(var1);
        var1.setShort("LifeLeft", (short)this.life);
        var1.setTag("motion", this.newDoubleNBTList(new double[] {this.smotionX, this.smotionY, this.smotionZ}));
        var1.setBoolean("Large", this.isLarge());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound var1)
    {
        super.readEntityFromNBT(var1);
        this.life = var1.getShort("LifeLeft");
        NBTTagList var2 = var1.getTagList("motion");
        this.smotionX = (double)((float)((NBTTagDouble)var2.tagAt(0)).data);
        this.smotionY = (double)((float)((NBTTagDouble)var2.tagAt(1)).data);
        this.smotionZ = (double)((float)((NBTTagDouble)var2.tagAt(2)).data);
        this.setLarge(var1.getBoolean("Large"));
    }

    /**
     * Applies a velocity to each of the entities pushing them away from each other. Args: entity
     */
    public void applyEntityCollision(Entity var1)
    {
        super.applyEntityCollision(var1);

        if (var1 != null && var1 instanceof EntityLiving && !(var1 instanceof EntityCog) && !(var1 instanceof EntityLabyrinthEye))
        {
            if (var1 instanceof EntitySentry || var1 instanceof EntityTrackingGolem || var1 instanceof EntitySliderHostMimic || var1 instanceof EntitySentryGuardian || var1 instanceof EntitySentryGolem || var1 instanceof EntityBattleSentry || var1 instanceof EntitySlider || var1 instanceof EntityMiniSlider)
            {
                return;
            }

            var1.attackEntityFrom(DamageSource.causeThrownDamage(this, this.parent), 5);
        }
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource var1, int var2)
    {
        if (var1.getEntity() != null)
        {
            Vec3 var3 = var1.getEntity().getLookVec();

            if (var3 != null)
            {
                this.smotionX = var3.xCoord;
                this.smotionY = var3.yCoord;
                this.smotionZ = var3.zCoord;
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    public void setThrowableHeading(double var1, double var3, double var5, float var7, float var8)
    {
        float var9 = MathHelper.sqrt_double(var1 * var1 + var3 * var3 + var5 * var5);
        var1 /= (double)var9;
        var3 /= (double)var9;
        var5 /= (double)var9;
        var1 += this.rand.nextGaussian() * 0.007499999832361937D * (double)var8;
        var3 += this.rand.nextGaussian() * 0.007499999832361937D * (double)var8;
        var5 += this.rand.nextGaussian() * 0.007499999832361937D * (double)var8;
        var1 *= (double)var7;
        var3 *= (double)var7;
        var5 *= (double)var7;
        this.motionX = var1;
        this.motionY = var3;
        this.motionZ = var5;
        float var10 = MathHelper.sqrt_double(var1 * var1 + var5 * var5);
        this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(var1, var5) * 180.0D / Math.PI);
        this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(var3, (double)var10) * 180.0D / Math.PI);
    }
}
