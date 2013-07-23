package net.aetherteam.aether.entities;

import net.aetherteam.aether.entities.ai.AIEntityArrowAttackSentry;
import net.aetherteam.aether.interfaces.IAetherMob;
import net.aetherteam.aether.party.Party;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntitySentryGolem extends EntityDungeonMob implements IAetherMob, IRangedAttackMob
{
    private String bossName;
    private Party fightingParty;
    public EntityLiving target;
    public int timeTilToss = 50;
    public int tossCoolDown = 25;
    public float progress = 0.0F;
    public float sizeBlockThrowing = 0.0F;

    public EntitySentryGolem(World var1)
    {
        super(var1);
        this.setSize(1.0F, 2.0F);
        this.moveSpeed = 0.25F;
        this.texture = "/net/aetherteam/aether/client/sprites/mobs/sentrygolem/sentryGolemGreen.png";
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(6, new EntityAILookIdle(this));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 16.0F, 0, true));
        this.tasks.addTask(4, new AIEntityArrowAttackSentry(this, this.moveSpeed, 60, 10.0F));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
    }

    /**
     * Returns true if the newer Entity AI code should be run
     */
    public boolean isAIEnabled()
    {
        return true;
    }

    protected void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(18, new Byte((byte)2));
        this.dataWatcher.addObject(19, new Integer(0));
    }

    public byte getHandState()
    {
        return this.dataWatcher.getWatchableObjectByte(18);
    }

    public void setHandState(byte var1)
    {
        this.dataWatcher.updateObject(18, Byte.valueOf(var1));
    }

    public int getFire()
    {
        return this.dataWatcher.getWatchableObjectInt(19);
    }

    /**
     * Sets entity to burn for x amount of seconds, cannot lower amount of existing fire.
     */
    public void setFire(int var1)
    {
        this.dataWatcher.updateObject(19, Integer.valueOf(var1));
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected String getLivingSound()
    {
        return "aemob.sentryGolem.say";
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected String getHurtSound()
    {
        return "aemob.sentryGolem.say";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected String getDeathSound()
    {
        return "aemob.sentryGolem.death";
    }

    /**
     * Plays step sound at given x, y, z for the entity
     */
    protected void playStepSound(int var1, int var2, int var3, int var4)
    {
        this.worldObj.playSoundAtEntity(this, "mob.cow.step", 0.15F, 1.0F);
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();
        EntityPlayer var1 = this.worldObj.getClosestPlayerToEntity(this, 8.0D);

        if (this.entityToAttack == null && var1 != null && this.canEntityBeSeen(var1) && !var1.isDead && !var1.capabilities.isCreativeMode)
        {
            this.entityToAttack = var1;
        }

        if (this.timeTilToss == 50 && this.tossCoolDown == 0 && this.entityToAttack != null)
        {
            ;
        }

        if (this.timeTilToss != 0 && this.tossCoolDown == 0)
        {
            --this.timeTilToss;
        }
        else if (this.tossCoolDown == 0)
        {
            this.timeTilToss = 50;
            this.tossCoolDown = 25;
        }
    }

    /**
     * Basic mob attack. Default to touch of death in EntityCreature. Overridden by each mob to define their attack.
     */
    protected void attackEntity(Entity var1, float var2)
    {
        this.target = (EntityLiving)var1;

        if (var2 < 10.0F)
        {
            double var3 = var1.posX - this.posX;
            double var5 = var1.posZ - this.posZ;

            if (this.target != null)
            {
                if (this.target.isDead || (double)this.target.getDistanceToEntity(this) > 12.0D)
                {
                    this.target = null;
                    this.attackTime = 0;
                }

                if (this.attackTime >= 20 && this.canEntityBeSeen(this.target))
                {
                    this.attackTime = -10;
                }

                if (this.attackTime < 20)
                {
                    this.attackTime += 2;
                }
            }

            this.rotationYaw = (float)(Math.atan2(var5, var3) * 180.0D / Math.PI) - 90.0F;
        }
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    public boolean getCanSpawnHere()
    {
        int var1 = MathHelper.floor_double(this.posX);
        int var2 = MathHelper.floor_double(this.boundingBox.minY);
        int var3 = MathHelper.floor_double(this.posZ);
        return this.rand.nextInt(25) == 0 && this.getBlockPathWeight(var1, var2, var3) >= 0.0F && this.worldObj.checkNoEntityCollision(this.boundingBox) && this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox).size() == 0 && !this.worldObj.isAnyLiquid(this.boundingBox) && this.worldObj.difficultySetting > 0;
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound var1)
    {
        super.writeEntityToNBT(var1);
        var1.setByte("armState", this.getHandState());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound var1)
    {
        super.readEntityFromNBT(var1);

        if (var1.hasKey("armState"))
        {
            this.setHandState(var1.getByte("armState"));
        }
    }

    public int getMaxHealth()
    {
        return 20;
    }

    /**
     * Attack the specified entity using a ranged attack.
     */
    public void attackEntityWithRangedAttack(EntityLiving var1, float var2)
    {
        if (this.entityToAttack != null)
        {
            double var3 = this.entityToAttack.posX - this.posX;
            double var5 = this.entityToAttack.posZ - this.posZ;
            double var7 = Math.sqrt(var3 * var3 + var5 * var5) + (this.posY - this.entityToAttack.posY);
            double var10000 = var3 * var7;
            var10000 = var5 * var7;
            EntityProjectileSentry var9 = new EntityProjectileSentry(this.worldObj, this.posX, this.posY + 2.35D, this.posZ, this);
            var9.rotationYaw = this.renderYawOffset;
            var9.renderYawOffset = this.renderYawOffset;
            var9.rotationPitch = this.rotationPitch;
            double var10 = var1.posX + var1.motionX - this.posX;
            double var12 = var1.posY - this.posY;
            double var14 = var1.posZ + var1.motionZ - this.posZ;
            float var16 = MathHelper.sqrt_double(var10 * var10 + var14 * var14);

            if (!this.worldObj.isRemote)
            {
                var9.setThrowableHeading(var10, var12 + (double)(var16 * 0.2F), var14, 0.75F, 8.0F);
                this.worldObj.spawnEntityInWorld(var9);
            }
        }
    }
}
