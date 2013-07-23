package net.aetherteam.aether.entities.bosses;

import java.util.Random;
import net.aetherteam.aether.entities.EntityBattleSentry;
import net.aetherteam.aether.entities.EntitySentry;
import net.aetherteam.aether.entities.EntitySentryGolem;
import net.aetherteam.aether.entities.EntityTrackingGolem;
import net.aetherteam.aether.interfaces.IAetherMob;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityHostEye extends EntityMiniBoss
    implements IAetherMob
{
    public String dir = "/net/aetherteam/aether/client/sprites";
    public int moveTimer;
    public int dennis;
    public int rennis;
    public int chatTime;
    public Entity target;
    public boolean gotMovement;
    public boolean crushed;
    public float speedy;
    public float harvey;
    public int direction;
    public EntitySliderHostMimic host;

    public EntityHostEye(World world)
    {
        super(world);
        setSize(0.4F, 0.4F);
        this.health = getHealth();
        this.dennis = 1;
        this.jumpMovementFactor = 0.0F;
        this.texture = (this.dir + "/mobs/host/hosteye.png");

        if (this.target == null)
        {
            this.target = this.worldObj.getClosestPlayerToEntity(this, -1.0D);
        }
    }

    public EntityHostEye(World world, double x, double y, double z, float yaw, float pitch)
    {
        this(world);
        setPositionAndRotation(x, y, z, yaw, pitch);
    }

    public EntityHostEye(World world, double x, double y, double z, float yaw, float pitch, EntitySliderHostMimic host, EntityLiving target)
    {
        this(world, x, y, z, yaw, pitch);
        this.host = host;
        this.target = target;
    }

    public void entityInit()
    {
        super.entityInit();
    }

    protected void updateFallState(double par1, boolean par3)
    {
    }

    protected void fall(float par1)
    {
    }

    public boolean canDespawn()
    {
        return true;
    }

    protected String getLivingSound()
    {
        return null;
    }

    protected String getHurtSound()
    {
        return null;
    }

    protected String getDeathSound()
    {
        return null;
    }

    public void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setFloat("Speedy", this.speedy);
        nbttagcompound.setShort("MoveTimer", (short)this.moveTimer);
        nbttagcompound.setShort("Direction", (short)this.direction);
        nbttagcompound.setBoolean("GotMovement", this.gotMovement);
    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readEntityFromNBT(nbttagcompound);
        this.speedy = nbttagcompound.getFloat("Speedy");
        this.moveTimer = nbttagcompound.getShort("MoveTimer");
        this.direction = nbttagcompound.getShort("Direction");
        this.gotMovement = nbttagcompound.getBoolean("GotMovement");
    }

    public void onUpdate()
    {
        super.onUpdate();
        this.jumpMovementFactor = 0.0F;
        this.renderYawOffset = (this.rotationPitch = this.rotationYaw = 0.0F);

        if ((this.target != null) && ((this.target instanceof EntityLiving)))
        {
            EntityLiving e1 = (EntityLiving)this.target;

            if ((e1.getHealth() <= 0) || (!canEntityBeSeen(e1)))
            {
                this.target = null;
                stop();
                this.moveTimer = 0;
                return;
            }
        }
        else
        {
            if ((this.target != null) && (this.target.isDead))
            {
                this.target = null;
                stop();
                this.moveTimer = 0;
                return;
            }

            if (this.target == null)
            {
                this.target = this.worldObj.getClosestPlayerToEntity(this, -1.0D);

                if (this.target == null)
                {
                    this.target = null;
                    stop();
                    this.moveTimer = 0;

                    if (!this.worldObj.isRemote)
                    {
                        setDead();
                    }

                    return;
                }
            }
        }

        if ((this.host == null) || (this.host.isDead))
        {
            this.target = null;
            stop();
            this.moveTimer = 0;

            if (!this.worldObj.isRemote)
            {
                setDead();
            }

            return;
        }

        if (!this.host.canEntityBeSeen(this.target))
        {
            this.target = null;
            stop();
            this.moveTimer = 0;

            if (!this.worldObj.isRemote)
            {
                setDead();
            }

            return;
        }

        this.fallDistance = 0.0F;

        if (this.gotMovement)
        {
            if (this.isCollided)
            {
                double x = this.posX - 0.5D;
                double y = this.boundingBox.minY + 0.75D;
                double z = this.posZ - 0.5D;

                if (this.crushed)
                {
                    this.worldObj.playSoundEffect(this.posX, this.posY, this.posZ, "random.explode", 3.0F, (0.625F + (this.worldObj.rand.nextFloat() - this.worldObj.rand.nextFloat()) * 0.2F) * 0.7F);
                    this.worldObj.playSoundAtEntity(this, "aeboss.slider.collide", 2.5F, 1.0F / (this.rand.nextFloat() * 0.2F + 0.9F));
                }

                stop();
            }
            else
            {
                if (this.speedy < 2.0F)
                {
                    this.speedy += 0.035F;
                }

                this.motionX = 0.0D;
                this.motionY = 0.0D;
                this.motionZ = 0.0D;

                if (this.direction == 0)
                {
                    this.motionY = this.speedy;

                    if (this.boundingBox.minY > this.target.boundingBox.minY + 0.35D)
                    {
                        stop();
                        this.moveTimer = 8;
                    }
                }
                else if (this.direction == 1)
                {
                    this.motionY = (-this.speedy);

                    if (this.boundingBox.minY < this.target.boundingBox.minY - 0.25D)
                    {
                        stop();
                        this.moveTimer = 8;
                    }
                }
                else if (this.direction == 2)
                {
                    this.motionX = this.speedy;

                    if (this.posX > this.target.posX + 0.125D)
                    {
                        stop();
                        this.moveTimer = 8;
                    }
                }
                else if (this.direction == 3)
                {
                    this.motionX = (-this.speedy);

                    if (this.posX < this.target.posX - 0.125D)
                    {
                        stop();
                        this.moveTimer = 8;
                    }
                }
                else if (this.direction == 4)
                {
                    this.motionZ = this.speedy;

                    if (this.posZ > this.target.posZ + 0.125D)
                    {
                        stop();
                        this.moveTimer = 8;
                    }
                }
                else if (this.direction == 5)
                {
                    this.motionZ = (-this.speedy);

                    if (this.posZ < this.target.posZ - 0.125D)
                    {
                        stop();
                        this.moveTimer = 8;
                    }
                }
            }
        }
        else
        {
            this.motionY = 0.0D;

            if (this.moveTimer > 0)
            {
                this.moveTimer -= 1;
                this.motionX = 0.0D;
                this.motionY = 0.0D;
                this.motionZ = 0.0D;
            }
            else
            {
                double a = Math.abs(this.posX - this.target.posX);
                double b = Math.abs(this.boundingBox.minY - this.target.boundingBox.minY);
                double c = Math.abs(this.posZ - this.target.posZ);

                if (a > c)
                {
                    this.direction = 2;

                    if (this.posX > this.target.posX)
                    {
                        this.direction = 3;
                    }
                }
                else
                {
                    this.direction = 4;

                    if (this.posZ > this.target.posZ)
                    {
                        this.direction = 5;
                    }
                }

                if (((b > a) && (b > c)) || ((b > 0.25D) && (this.rand.nextInt(5) == 0)))
                {
                    this.direction = 0;

                    if (this.posY > this.target.posY)
                    {
                        this.direction = 1;
                    }
                }

                this.gotMovement = true;
            }
        }

        if (this.harvey > 0.01F)
        {
            this.harvey *= 0.8F;
        }
    }

    public void applyEntityCollision(Entity entity)
    {
        if (this.gotMovement)
        {
            if (((entity instanceof EntitySentry)) || ((entity instanceof EntityTrackingGolem)) || ((entity instanceof EntitySliderHostMimic)) || ((entity instanceof EntitySentryGuardian)) || ((entity instanceof EntitySentryGolem)) || ((entity instanceof EntityBattleSentry)))
            {
                return;
            }

            boolean flag = entity.attackEntityFrom(DamageSource.causeMobDamage(this.host), 6);

            if ((flag) && ((entity instanceof EntityLiving)))
            {
                this.worldObj.playSoundAtEntity(this, "aeboss.slider.collide", 2.5F, 1.0F / (this.rand.nextFloat() * 0.2F + 0.9F));

                if (((entity instanceof EntityCreature)) || ((entity instanceof EntityPlayer)))
                {
                    EntityLiving ek = (EntityLiving)entity;
                    ek.motionY += 0.35D;
                    ek.motionX *= 2.0D;
                    ek.motionZ *= 2.0D;
                }

                stop();
            }
        }
    }

    protected void dropFewItems(boolean var1, int var2)
    {
    }

    public void stop()
    {
        this.gotMovement = false;
        this.moveTimer = 12;
        this.direction = 0;
        this.speedy = 0.0F;
        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;
    }

    public boolean attackEntityFrom(DamageSource ds, int i)
    {
        return false;
    }

    public void addVelocity(double d, double d1, double d2)
    {
    }

    public void knockBack(Entity entity, int i, double d, double d1)
    {
    }

    public void addSquirrelButts(int x, int y, int z)
    {
        if (this.worldObj.isRemote)
        {
            double a = x + 0.5D + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.375D;
            double b = y + 0.5D + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.375D;
            double c = z + 0.5D + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.375D;
            this.worldObj.spawnParticle("explode", a, b, c, 0.0D, 0.0D, 0.0D);
        }
    }

    public int getMaxHealth()
    {
        return 10;
    }
}

