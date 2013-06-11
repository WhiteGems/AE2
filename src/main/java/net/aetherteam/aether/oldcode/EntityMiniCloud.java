package net.aetherteam.aether.oldcode;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntityMiniCloud extends EntityFlying
{
    public String dir = "/net/aetherteam/aether/client/sprites";
    public int shotTimer;
    public int lifeSpan;
    public boolean gotPlayer;
    public boolean toLeft;
    public EntityLiving dude;
    public double targetX;
    public double targetY;
    public double targetZ;

    public EntityMiniCloud(World var1)
    {
        super(var1);
        this.texture = this.dir + "/mobs/cloudsentry/cloudsentry.png";
        this.setSize(0.0F, 0.0F);
        this.noClip = true;
        this.entityCollisionReduction = 1.75F;
    }

    public int getMaxHealth()
    {
        return 5;
    }

    public EntityMiniCloud(World var1, EntityPlayer var2, boolean var3)
    {
        super(var1);
        this.texture = this.dir + "/mobs/cloudsentry/cloudsentry.png";
        this.setSize(0.5F, 0.45F);
        this.dude = var2;
        this.toLeft = var3;
        this.lifeSpan = 3600;
        this.getTargetPos();
        this.setPosition(this.targetX, this.targetY, this.targetZ);
        this.rotationPitch = this.dude.rotationPitch;
        this.rotationYaw = this.dude.rotationYaw;
        this.entityCollisionReduction = 1.75F;
        this.spawnExplosionParticle();
    }

    /**
     * Checks if the entity is in range to render by using the past in distance and comparing it to its average edge
     * length * 64 * renderDistanceWeight Args: distance
     */
    public boolean isInRangeToRenderDist(double var1)
    {
        return true;
    }

    public void getTargetPos()
    {
        if (this.getDistanceToEntity(this.dude) > 2.0F)
        {
            this.targetX = this.dude.posX;
            this.targetY = this.dude.posY - 0.10000000149011612D;
            this.targetZ = this.dude.posZ;
        } else
        {
            double var1 = (double) this.dude.rotationYaw;

            if (this.toLeft)
            {
                var1 -= 90.0D;
            } else
            {
                var1 += 90.0D;
            }

            var1 /= -(180D / Math.PI);
            this.targetX = this.dude.posX + Math.sin(var1) * 1.05D;
            this.targetY = this.dude.posY - 0.10000000149011612D;
            this.targetZ = this.dude.posZ + Math.cos(var1) * 1.05D;
        }
    }

    public boolean atShoulder()
    {
        double var1 = this.posX - this.targetX;
        double var3 = this.posY - this.targetY;
        double var5 = this.posZ - this.targetZ;
        return Math.sqrt(var1 * var1 + var3 * var3 + var5 * var5) < 0.3D;
    }

    public void approachTarget()
    {
        double var1 = this.targetX - this.posX;
        double var3 = this.targetY - this.posY;
        double var5 = this.targetZ - this.posZ;
        double var7 = Math.sqrt(var1 * var1 + var3 * var3 + var5 * var5) * 3.25D;
        this.motionX = (this.motionX + var1 / var7) / 2.0D;
        this.motionY = (this.motionY + var3 / var7) / 2.0D;
        this.motionZ = (this.motionZ + var5 / var7) / 2.0D;
        Math.atan2(var1, var5);
    }

    protected Entity findPlayer()
    {
        EntityPlayer var1 = this.worldObj.getClosestPlayerToEntity(this, 16.0D);
        return var1 != null && this.canEntityBeSeen(var1) ? var1 : null;
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound var1)
    {
        super.writeEntityToNBT(var1);
        var1.setShort("LifeSpan", (short) this.lifeSpan);
        var1.setShort("ShotTimer", (short) this.shotTimer);
        this.gotPlayer = this.dude != null;
        var1.setBoolean("GotPlayer", this.gotPlayer);
        var1.setBoolean("ToLeft", this.toLeft);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound var1)
    {
        super.readEntityFromNBT(var1);
        this.lifeSpan = var1.getShort("LifeSpan");
        this.shotTimer = var1.getShort("ShotTimer");
        this.gotPlayer = var1.getBoolean("GotPlayer");
        this.toLeft = var1.getBoolean("ToLeft");
    }

    public void updateEntityActionState()
    {
        super.updateEntityActionState();
        --this.lifeSpan;

        if (this.lifeSpan <= 0)
        {
            this.spawnExplosionParticle();
            this.isDead = true;
        } else
        {
            if (this.shotTimer > 0)
            {
                --this.shotTimer;
            }

            if (this.gotPlayer && this.dude == null)
            {
                this.gotPlayer = false;
                this.dude = (EntityLiving) this.findPlayer();
            }

            if (this.dude != null && !this.dude.isDead)
            {
                this.getTargetPos();

                if (this.atShoulder())
                {
                    this.motionX *= 0.65D;
                    this.motionY *= 0.65D;
                    this.motionZ *= 0.65D;
                    this.rotationYaw = this.dude.rotationYaw + (this.toLeft ? 1.0F : -1.0F);
                    this.rotationPitch = this.dude.rotationPitch;

                    if (this.shotTimer <= 0 && this.dude instanceof EntityPlayer && ((EntityPlayer) this.dude).swingProgress > 0.0F)
                    {
                        float var7 = this.rotationYaw - (this.toLeft ? 1.0F : -1.0F);
                        double var1 = this.posX + Math.sin((double) var7 / -(180D / Math.PI)) * 1.6D;
                        double var3 = this.posY - 0.25D;
                        double var5 = this.posZ + Math.cos((double) var7 / -(180D / Math.PI)) * 1.6D;
                        EntityFiroBall var8 = new EntityFiroBall(this.worldObj, var1, var3, var5, true, true);
                        this.worldObj.spawnEntityInWorld(var8);
                        Vec3 var9 = this.getLookVec();

                        if (var9 != null)
                        {
                            var8.smotionX = var9.xCoord * 1.5D;
                            var8.smotionY = var9.yCoord * 1.5D;
                            var8.smotionZ = var9.zCoord * 1.5D;
                        }

                        var8.smacked = true;
                        this.worldObj.playSoundAtEntity(this, "mob.zephyr.zephyrshoot", 0.75F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
                        this.shotTimer = 40;
                    }
                } else
                {
                    this.approachTarget();
                }
            } else
            {
                this.spawnExplosionParticle();
                this.isDead = true;
            }
        }
    }

    public boolean attackEntityFrom(EntityLiving var1, int var2)
    {
        return var1 != null && var1 == this.dude ? false : super.attackEntityFrom(DamageSource.causeMobDamage(var1), var2);
    }
}
