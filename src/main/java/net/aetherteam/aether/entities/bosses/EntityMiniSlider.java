package net.aetherteam.aether.entities.bosses;

import java.util.List;
import net.aetherteam.aether.entities.EntityBattleSentry;
import net.aetherteam.aether.entities.EntitySentry;
import net.aetherteam.aether.entities.EntitySentryGolem;
import net.aetherteam.aether.entities.EntityTrackingGolem;
import net.aetherteam.aether.interfaces.IAetherMob;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntityMiniSlider extends EntityMiniBoss implements IAetherMob
{
    public String dir;
    public boolean reform;
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
    public EntitySlider slider;

    public EntityMiniSlider(World world)
    {
        super(world);
        this.dir = "/net/aetherteam/aether/client/sprites";
        this.reform = false;
        this.setSize(0.4F, 0.4F);
        this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(10.0D);
        this.setEntityHealth(10.0F);
        this.dennis = 1;
        this.jumpMovementFactor = 0.0F;

        if (this.target == null)
        {
            this.target = this.worldObj.getClosestPlayerToEntity(this, -1.0D);
        }
    }

    public EntityMiniSlider(World world, double x, double y, double z, float yaw, float pitch)
    {
        this(world);
        this.setPositionAndRotation(x, y, z, yaw, pitch);
    }

    public EntityMiniSlider(World world, double x, double y, double z, float yaw, float pitch, EntitySlider slider, EntityLiving target, double sliderX, double sliderY, double sliderZ)
    {
        this(world, x, y, z, yaw, pitch);
        this.slider = slider;
        this.target = target;
        this.moveTimer = 60;
        this.dataWatcher.updateObject(16, Integer.valueOf((int)sliderX));
        this.dataWatcher.updateObject(17, Integer.valueOf((int)sliderY));
        this.dataWatcher.updateObject(18, Integer.valueOf((int)sliderZ));
    }

    public void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(16, Integer.valueOf((int)this.posX));
        this.dataWatcher.addObject(17, Integer.valueOf((int)this.posY));
        this.dataWatcher.addObject(18, Integer.valueOf((int)this.posZ));
        this.dataWatcher.addObject(19, Byte.valueOf((byte)0));
    }

    public int getOrgX()
    {
        return this.dataWatcher.getWatchableObjectInt(16);
    }

    public int getOrgY()
    {
        return this.dataWatcher.getWatchableObjectInt(17);
    }

    public int getOrgZ()
    {
        return this.dataWatcher.getWatchableObjectInt(18);
    }

    public void setOrgX(int x)
    {
        this.dataWatcher.updateObject(20, Integer.valueOf(x));
    }

    public void setOrgY(int y)
    {
        this.dataWatcher.updateObject(21, Integer.valueOf(y));
    }

    public void setOrgZ(int z)
    {
        this.dataWatcher.updateObject(22, Integer.valueOf(z));
    }

    public boolean isReformed()
    {
        return (this.dataWatcher.getWatchableObjectByte(19) & 1) != 0;
    }

    public void setReformed(boolean reformed)
    {
        if (!this.worldObj.isRemote)
        {
            if (reformed)
            {
                this.dataWatcher.updateObject(19, Byte.valueOf((byte)1));
            }
            else
            {
                this.dataWatcher.updateObject(19, Byte.valueOf((byte)0));
            }
        }
    }

    /**
     * Takes in the distance the entity has fallen this tick and whether its on the ground to update the fall distance
     * and deal fall damage if landing on the ground.  Args: distanceFallenThisTick, onGround
     */
    protected void updateFallState(double par1, boolean par3) {}

    /**
     * Called when the mob is falling. Calculates and applies fall damage.
     */
    protected void fall(float par1) {}

    /**
     * Determines if an entity can be despawned, used on idle far away entities
     */
    public boolean canDespawn()
    {
        return true;
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected String getLivingSound()
    {
        return null;
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected String getHurtSound()
    {
        return null;
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected String getDeathSound()
    {
        return null;
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setFloat("Speedy", this.speedy);
        nbttagcompound.setShort("MoveTimer", (short)this.moveTimer);
        nbttagcompound.setShort("Direction", (short)this.direction);
        nbttagcompound.setBoolean("GotMovement", this.gotMovement);
        nbttagcompound.setBoolean("Reformed", this.isReformed());
        nbttagcompound.setInteger("OrgX", this.getOrgX());
        nbttagcompound.setInteger("OrgY", this.getOrgY());
        nbttagcompound.setInteger("OrgZ", this.getOrgZ());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readEntityFromNBT(nbttagcompound);
        this.speedy = nbttagcompound.getFloat("Speedy");
        this.moveTimer = nbttagcompound.getShort("MoveTimer");
        this.direction = nbttagcompound.getShort("Direction");
        this.gotMovement = nbttagcompound.getBoolean("GotMovement");
        this.setReformed(nbttagcompound.getBoolean("Reformed"));
        this.setOrgX(nbttagcompound.getInteger("OrgX"));
        this.setOrgY(nbttagcompound.getInteger("OrgY"));
        this.setOrgZ(nbttagcompound.getInteger("OrgZ"));
    }

    public void reform()
    {
        this.reform = true;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();
        this.jumpMovementFactor = 0.0F;
        this.renderYawOffset = this.rotationPitch = this.rotationYaw = 0.0F;

        if (this.target != null && this.target instanceof EntityLiving)
        {
            EntityLiving a = (EntityLiving)this.target;

            if (a.func_110143_aJ() <= 0.0F || !this.canEntityBeSeen(a))
            {
                this.target = null;
                this.stop();
                this.moveTimer = 0;
                return;
            }
        }
        else
        {
            if (this.target != null && this.target.isDead)
            {
                this.target = null;
                this.stop();
                this.moveTimer = 0;
                return;
            }

            if (this.target == null)
            {
                this.target = this.worldObj.getClosestPlayerToEntity(this, -1.0D);

                if (this.target == null)
                {
                    this.target = null;
                    this.stop();
                    this.moveTimer = 0;

                    if (!this.worldObj.isRemote)
                    {
                        this.setDead();
                    }

                    return;
                }
            }
        }

        if (this.posX == (double)this.getOrgX() && this.posY == (double)this.getOrgY() && this.posZ == (double)this.getOrgZ())
        {
            if (!this.isReformed())
            {
                this.setReformed(true);
            }

            this.stop();
            this.moveTimer = 0;
        }

        if (this.slider == null)
        {
            Vec3 var16 = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
            Vec3 vec3d1 = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
            MovingObjectPosition var17 = this.worldObj.clip(var16, vec3d1);
            var16 = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
            vec3d1 = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

            if (var17 != null)
            {
                vec3d1 = Vec3.createVectorHelper(var17.hitVec.xCoord, var17.hitVec.yCoord, var17.hitVec.zCoord);
            }

            if (!this.worldObj.isRemote)
            {
                Object entity = null;
                List var18 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(4.0D, 4.0D, 4.0D));
                double d = 0.0D;

                for (int l = 0; l < var18.size(); ++l)
                {
                    Entity entity1 = (Entity)var18.get(l);

                    if (entity1.canBeCollidedWith() && entity1 != this)
                    {
                        float f4 = 0.3F;

                        if (entity1 instanceof EntitySlider)
                        {
                            this.slider = (EntitySlider)entity1;
                            var17 = null;
                        }

                        AxisAlignedBB axisalignedbb = entity1.boundingBox.expand((double)f4, (double)f4, (double)f4);
                        MovingObjectPosition movingobjectposition1 = axisalignedbb.calculateIntercept(var16, vec3d1);

                        if (movingobjectposition1 != null)
                        {
                            double d1 = var16.distanceTo(movingobjectposition1.hitVec);

                            if (d1 < d || d == 0.0D)
                            {
                                d = d1;
                            }
                        }
                    }
                }
            }

            if (this.slider == null || this.slider.isDead)
            {
                this.target = null;
                this.stop();
                this.moveTimer = 0;

                if (!this.worldObj.isRemote)
                {
                    this.setDead();
                }
            }
        }
        else if (this.slider.target != this.target)
        {
            this.target = null;
            this.stop();
            this.moveTimer = 0;

            if (!this.worldObj.isRemote)
            {
                this.setDead();
            }
        }
        else
        {
            this.fallDistance = 0.0F;
            double b;
            double c;
            double var15;

            if (this.gotMovement)
            {
                if (this.isCollided)
                {
                    var15 = this.posX - 0.5D;
                    b = this.boundingBox.minY + 0.75D;
                    c = this.posZ - 0.5D;

                    if (this.crushed)
                    {
                        this.worldObj.playSoundEffect(this.posX, this.posY, this.posZ, "random.explode", 3.0F, (0.625F + (this.worldObj.rand.nextFloat() - this.worldObj.rand.nextFloat()) * 0.2F) * 0.7F);
                        this.worldObj.playSoundAtEntity(this, "aether:aeboss.slider.collide", 2.5F, 1.0F / (this.rand.nextFloat() * 0.2F + 0.9F));
                    }

                    this.stop();
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
                        this.motionY = (double)this.speedy;

                        if (this.boundingBox.minY > (this.reform ? (double)this.getOrgY() : this.target.boundingBox.minY + 0.35D))
                        {
                            this.stop();
                            this.moveTimer = 8;
                        }
                    }
                    else if (this.direction == 1)
                    {
                        this.motionY = (double)(-this.speedy);

                        if (this.boundingBox.minY < (this.reform ? (double)this.getOrgY() : this.target.boundingBox.minY - 0.25D))
                        {
                            this.stop();
                            this.moveTimer = 8;
                        }
                    }
                    else if (this.direction == 2)
                    {
                        this.motionX = (double)this.speedy;

                        if (this.posX > (this.reform ? (double)this.getOrgX() - 1.0D : this.target.posX + 0.125D))
                        {
                            this.stop();
                            this.moveTimer = 8;
                        }
                    }
                    else if (this.direction == 3)
                    {
                        this.motionX = (double)(-this.speedy);

                        if (this.posX < (this.reform ? (double)this.getOrgX() - 1.0D : this.target.posX - 0.125D))
                        {
                            this.stop();
                            this.moveTimer = 8;
                        }
                    }
                    else if (this.direction == 4)
                    {
                        this.motionZ = (double)this.speedy;

                        if (this.posZ > (this.reform ? (double)this.getOrgZ() - 1.0D : this.target.posZ + 0.125D))
                        {
                            this.stop();
                            this.moveTimer = 8;
                        }
                    }
                    else if (this.direction == 5)
                    {
                        this.motionZ = (double)(-this.speedy);

                        if (this.posZ < (this.reform ? (double)this.getOrgZ() - 1.0D : this.target.posZ - 0.125D))
                        {
                            this.stop();
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
                    --this.moveTimer;
                    this.motionX = 0.0D;
                    this.motionY = 0.0D;
                    this.motionZ = 0.0D;
                }
                else
                {
                    var15 = Math.abs(this.posX - (this.reform ? (double)this.getOrgX() : this.target.posX));
                    b = Math.abs(this.boundingBox.minY - (this.reform ? (double)this.getOrgY() : this.target.boundingBox.minY));
                    c = Math.abs(this.posZ - (this.reform ? (double)this.getOrgZ() : this.target.posZ));

                    if (var15 > c)
                    {
                        this.direction = 2;

                        if (this.posX > (this.reform ? (double)this.getOrgX() - 1.0D : this.target.posX))
                        {
                            this.direction = 3;
                        }
                    }
                    else
                    {
                        this.direction = 4;

                        if (this.posZ > (this.reform ? (double)this.getOrgZ() - 1.0D : this.target.posZ))
                        {
                            this.direction = 5;
                        }
                    }

                    if (b > var15 && b > c || b > 0.25D && this.rand.nextInt(5) == 0)
                    {
                        this.direction = 0;

                        if (this.posY > (this.reform ? (double)this.getOrgY() : this.target.posY))
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
    }

    /**
     * Applies a velocity to each of the entities pushing them away from each other. Args: entity
     */
    public void applyEntityCollision(Entity entity)
    {
        if (this.gotMovement)
        {
            if (entity instanceof EntitySentry || entity instanceof EntityTrackingGolem || entity instanceof EntitySliderHostMimic || entity instanceof EntitySentryGuardian || entity instanceof EntitySentryGolem || entity instanceof EntityBattleSentry || entity instanceof EntitySlider || entity instanceof EntityMiniSlider)
            {
                return;
            }

            boolean flag = entity.attackEntityFrom(DamageSource.causeMobDamage(this.slider), 6.0F);

            if (flag && entity instanceof EntityLiving)
            {
                this.worldObj.playSoundAtEntity(this, "aether:aeboss.slider.collide", 2.5F, 1.0F / (this.rand.nextFloat() * 0.2F + 0.9F));

                if (entity instanceof EntityCreature || entity instanceof EntityPlayer)
                {
                    EntityLiving ek = (EntityLiving)entity;
                    ek.motionY += 0.35D;
                    ek.motionX *= 2.0D;
                    ek.motionZ *= 2.0D;
                }

                this.stop();
            }
        }
    }

    /**
     * Drop 0-2 items of this living's type. @param par1 - Whether this entity has recently been hit by a player. @param
     * par2 - Level of Looting used to kill this mob.
     */
    protected void dropFewItems(boolean var1, int var2) {}

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

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource source, float damage)
    {
        if (this.slider != null)
        {
            this.slider.attackEntityFrom(DamageSource.causeMobDamage(this), damage);
        }

        this.heal(damage);
        return super.attackEntityFrom(source, damage);
    }

    /**
     * Adds to the current velocity of the entity. Args: x, y, z
     */
    public void addVelocity(double d, double d1, double d2) {}

    public void knockBack(Entity entity, int i, double d, double d1) {}

    public void addSquirrelButts(int x, int y, int z)
    {
        if (this.worldObj.isRemote)
        {
            double a = (double)x + 0.5D + (double)(this.rand.nextFloat() - this.rand.nextFloat()) * 0.375D;
            double b = (double)y + 0.5D + (double)(this.rand.nextFloat() - this.rand.nextFloat()) * 0.375D;
            double c = (double)z + 0.5D + (double)(this.rand.nextFloat() - this.rand.nextFloat()) * 0.375D;
            this.worldObj.spawnParticle("explode", a, b, c, 0.0D, 0.0D, 0.0D);
        }
    }
}
