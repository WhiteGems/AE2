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

    public EntityMiniSlider(World var1)
    {
        super(var1);
        this.dir = "/net/aetherteam/aether/client/sprites";
        this.reform = false;
        this.setSize(0.4F, 0.4F);
        this.health = this.getHealth();
        this.dennis = 1;
        this.jumpMovementFactor = 0.0F;
        this.texture = this.dir + "/mobs/cog.png";

        if (this.target == null)
        {
            this.target = this.worldObj.getClosestPlayerToEntity(this, -1.0D);
        }
    }

    public EntityMiniSlider(World var1, double var2, double var4, double var6, float var8, float var9)
    {
        this(var1);
        this.setPositionAndRotation(var2, var4, var6, var8, var9);
    }

    public EntityMiniSlider(World var1, double var2, double var4, double var6, float var8, float var9, EntitySlider var10, EntityLiving var11, double var12, double var14, double var16)
    {
        this(var1, var2, var4, var6, var8, var9);
        this.slider = var10;
        this.target = var11;
        this.moveTimer = 60;
        this.dataWatcher.updateObject(16, Integer.valueOf((int)var12));
        this.dataWatcher.updateObject(17, Integer.valueOf((int)var14));
        this.dataWatcher.updateObject(18, Integer.valueOf((int)var16));
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

    public void setOrgX(int var1)
    {
        this.dataWatcher.updateObject(20, Integer.valueOf(var1));
    }

    public void setOrgY(int var1)
    {
        this.dataWatcher.updateObject(21, Integer.valueOf(var1));
    }

    public void setOrgZ(int var1)
    {
        this.dataWatcher.updateObject(22, Integer.valueOf(var1));
    }

    public boolean isReformed()
    {
        return (this.dataWatcher.getWatchableObjectByte(19) & 1) != 0;
    }

    public void setReformed(boolean var1)
    {
        if (!this.worldObj.isRemote)
        {
            if (var1)
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
    protected void updateFallState(double var1, boolean var3) {}

    /**
     * Called when the mob is falling. Calculates and applies fall damage.
     */
    protected void fall(float var1) {}

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
    public void writeEntityToNBT(NBTTagCompound var1)
    {
        super.writeEntityToNBT(var1);
        var1.setFloat("Speedy", this.speedy);
        var1.setShort("MoveTimer", (short)this.moveTimer);
        var1.setShort("Direction", (short)this.direction);
        var1.setBoolean("GotMovement", this.gotMovement);
        var1.setBoolean("Reformed", this.isReformed());
        var1.setInteger("OrgX", this.getOrgX());
        var1.setInteger("OrgY", this.getOrgY());
        var1.setInteger("OrgZ", this.getOrgZ());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound var1)
    {
        super.readEntityFromNBT(var1);
        this.speedy = var1.getFloat("Speedy");
        this.moveTimer = var1.getShort("MoveTimer");
        this.direction = var1.getShort("Direction");
        this.gotMovement = var1.getBoolean("GotMovement");
        this.setReformed(var1.getBoolean("Reformed"));
        this.setOrgX(var1.getInteger("OrgX"));
        this.setOrgY(var1.getInteger("OrgY"));
        this.setOrgZ(var1.getInteger("OrgZ"));
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
            EntityLiving var1 = (EntityLiving)this.target;

            if (var1.getHealth() <= 0 || !this.canEntityBeSeen(var1))
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
            Vec3 var2 = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
            MovingObjectPosition var17 = this.worldObj.rayTraceBlocks(var16, var2);
            var16 = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
            var2 = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

            if (var17 != null)
            {
                var2 = Vec3.createVectorHelper(var17.hitVec.xCoord, var17.hitVec.yCoord, var17.hitVec.zCoord);
            }

            if (!this.worldObj.isRemote)
            {
                Object var4 = null;
                List var18 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(4.0D, 4.0D, 4.0D));
                double var6 = 0.0D;

                for (int var8 = 0; var8 < var18.size(); ++var8)
                {
                    Entity var9 = (Entity)var18.get(var8);

                    if (var9.canBeCollidedWith() && var9 != this)
                    {
                        float var10 = 0.3F;

                        if (var9 instanceof EntitySlider)
                        {
                            this.slider = (EntitySlider)var9;
                            var17 = null;
                        }

                        AxisAlignedBB var11 = var9.boundingBox.expand((double)var10, (double)var10, (double)var10);
                        MovingObjectPosition var12 = var11.calculateIntercept(var16, var2);

                        if (var12 != null)
                        {
                            double var13 = var16.distanceTo(var12.hitVec);

                            if (var13 < var6 || var6 == 0.0D)
                            {
                                var6 = var13;
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
            double var3;
            double var5;
            double var15;

            if (this.gotMovement)
            {
                if (this.isCollided)
                {
                    var15 = this.posX - 0.5D;
                    var3 = this.boundingBox.minY + 0.75D;
                    var5 = this.posZ - 0.5D;

                    if (this.crushed)
                    {
                        this.worldObj.playSoundEffect(this.posX, this.posY, this.posZ, "random.explode", 3.0F, (0.625F + (this.worldObj.rand.nextFloat() - this.worldObj.rand.nextFloat()) * 0.2F) * 0.7F);
                        this.worldObj.playSoundAtEntity(this, "aeboss.slider.collide", 2.5F, 1.0F / (this.rand.nextFloat() * 0.2F + 0.9F));
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
                    var3 = Math.abs(this.boundingBox.minY - (this.reform ? (double)this.getOrgY() : this.target.boundingBox.minY));
                    var5 = Math.abs(this.posZ - (this.reform ? (double)this.getOrgZ() : this.target.posZ));

                    if (var15 > var5)
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

                    if (var3 > var15 && var3 > var5 || var3 > 0.25D && this.rand.nextInt(5) == 0)
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
    public void applyEntityCollision(Entity var1)
    {
        if (this.gotMovement)
        {
            if (var1 instanceof EntitySentry || var1 instanceof EntityTrackingGolem || var1 instanceof EntitySliderHostMimic || var1 instanceof EntitySentryGuardian || var1 instanceof EntitySentryGolem || var1 instanceof EntityBattleSentry || var1 instanceof EntitySlider || var1 instanceof EntityMiniSlider)
            {
                return;
            }

            boolean var2 = var1.attackEntityFrom(DamageSource.causeMobDamage(this.slider), 6);

            if (var2 && var1 instanceof EntityLiving)
            {
                this.worldObj.playSoundAtEntity(this, "aeboss.slider.collide", 2.5F, 1.0F / (this.rand.nextFloat() * 0.2F + 0.9F));

                if (var1 instanceof EntityCreature || var1 instanceof EntityPlayer)
                {
                    EntityLiving var3 = (EntityLiving)var1;
                    var3.motionY += 0.35D;
                    var3.motionX *= 2.0D;
                    var3.motionZ *= 2.0D;
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
    public boolean attackEntityFrom(DamageSource var1, int var2)
    {
        if (this.slider != null)
        {
            this.slider.attackEntityFrom(DamageSource.causeMobDamage(this), var2);
        }

        this.heal(var2);
        return super.attackEntityFrom(var1, var2);
    }

    /**
     * Adds to the current velocity of the entity. Args: x, y, z
     */
    public void addVelocity(double var1, double var3, double var5) {}

    /**
     * knocks back this entity
     */
    public void knockBack(Entity var1, int var2, double var3, double var5) {}

    public void addSquirrelButts(int var1, int var2, int var3)
    {
        if (this.worldObj.isRemote)
        {
            double var4 = (double)var1 + 0.5D + (double)(this.rand.nextFloat() - this.rand.nextFloat()) * 0.375D;
            double var6 = (double)var2 + 0.5D + (double)(this.rand.nextFloat() - this.rand.nextFloat()) * 0.375D;
            double var8 = (double)var3 + 0.5D + (double)(this.rand.nextFloat() - this.rand.nextFloat()) * 0.375D;
            this.worldObj.spawnParticle("explode", var4, var6, var8, 0.0D, 0.0D, 0.0D);
        }
    }

    public int getMaxHealth()
    {
        return 10;
    }
}
