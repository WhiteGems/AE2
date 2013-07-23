package net.aetherteam.aether.entities;

import java.util.List;
import java.util.Random;
import net.aetherteam.aether.entities.ai.AIEntityArrowAttackSentry;
import net.aetherteam.aether.interfaces.IAetherMob;
import net.aetherteam.aether.party.Party;
import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntitySentryGolem extends EntityDungeonMob
    implements IAetherMob, IRangedAttackMob
{
    private String bossName;
    private Party fightingParty;
    public EntityLiving target;
    public int timeTilToss = 50;
    public int tossCoolDown = 25;
    public float progress = 0.0F;
    public float sizeBlockThrowing = 0.0F;

    public EntitySentryGolem(World world)
    {
        super(world);
        setSize(1.0F, 2.0F);
        this.moveSpeed = 0.25F;
        this.texture = "/net/aetherteam/aether/client/sprites/mobs/sentrygolem/sentryGolemGreen.png";
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(6, new EntityAILookIdle(this));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 16.0F, 0, true));
        this.tasks.addTask(4, new AIEntityArrowAttackSentry(this, this.moveSpeed, 60, 10.0F));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
    }

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

    public void setHandState(byte par1)
    {
        this.dataWatcher.updateObject(18, Byte.valueOf(par1));
    }

    public int getFire()
    {
        return this.dataWatcher.getWatchableObjectInt(19);
    }

    public void setFire(int par1)
    {
        this.dataWatcher.updateObject(19, Integer.valueOf(par1));
    }

    protected String getLivingSound()
    {
        return "aemob.sentryGolem.say";
    }

    protected String getHurtSound()
    {
        return "aemob.sentryGolem.say";
    }

    protected String getDeathSound()
    {
        return "aemob.sentryGolem.death";
    }

    protected void playStepSound(int par1, int par2, int par3, int par4)
    {
        this.worldObj.playSoundAtEntity(this, "mob.cow.step", 0.15F, 1.0F);
    }

    public void onUpdate()
    {
        super.onUpdate();
        EntityPlayer entityplayer = this.worldObj.getClosestPlayerToEntity(this, 8.0D);

        if (this.entityToAttack == null)
        {
            if ((entityplayer != null) && (canEntityBeSeen(entityplayer)) && (!entityplayer.isDead) && (!entityplayer.capabilities.isCreativeMode))
            {
                this.entityToAttack = entityplayer;
            }
        }

        if (((this.timeTilToss != 50) || (this.tossCoolDown != 0) ||
                (this.entityToAttack == null)) || (
                    (this.timeTilToss != 0) && (this.tossCoolDown == 0)))
        {
            this.timeTilToss -= 1;
        }
        else if (this.tossCoolDown == 0)
        {
            this.timeTilToss = 50;
            this.tossCoolDown = 25;
        }
    }

    protected void attackEntity(Entity entity, float f)
    {
        this.target = ((EntityLiving)entity);

        if (f < 10.0F)
        {
            double d = entity.posX - this.posX;
            double d1 = entity.posZ - this.posZ;

            if (this.target != null)
            {
                if ((this.target.isDead) || (this.target.getDistanceToEntity(this) > 12.0D))
                {
                    this.target = null;
                    this.attackTime = 0;
                }

                if ((this.attackTime >= 20) && (canEntityBeSeen(this.target)))
                {
                    this.attackTime = -10;
                }

                if (this.attackTime < 20)
                {
                    this.attackTime += 2;
                }
            }

            this.rotationYaw = ((float)(Math.atan2(d1, d) * 180.0D / Math.PI) - 90.0F);
        }
    }

    public boolean getCanSpawnHere()
    {
        int i = MathHelper.floor_double(this.posX);
        int j = MathHelper.floor_double(this.boundingBox.minY);
        int k = MathHelper.floor_double(this.posZ);
        return (this.rand.nextInt(25) == 0) && (getBlockPathWeight(i, j, k) >= 0.0F) && (this.worldObj.checkNoEntityCollision(this.boundingBox)) && (this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox).size() == 0) && (!this.worldObj.isAnyLiquid(this.boundingBox)) && (this.worldObj.difficultySetting > 0);
    }

    public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeEntityToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setByte("armState", getHandState());
    }

    public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readEntityFromNBT(par1NBTTagCompound);

        if (par1NBTTagCompound.hasKey("armState"))
        {
            setHandState(par1NBTTagCompound.getByte("armState"));
        }
    }

    public int getMaxHealth()
    {
        return 20;
    }

    public void attackEntityWithRangedAttack(EntityLiving entityliving, float f)
    {
        if (this.entityToAttack != null)
        {
            double d1 = this.entityToAttack.posX - this.posX;
            double d2 = this.entityToAttack.posZ - this.posZ;
            double d4 = Math.sqrt(d1 * d1 + d2 * d2) + (this.posY - this.entityToAttack.posY);
            d1 *= d4;
            d2 *= d4;
            EntityProjectileSentry sentrybomb = new EntityProjectileSentry(this.worldObj, this.posX, this.posY + 2.35D, this.posZ, this);
            sentrybomb.rotationYaw = this.renderYawOffset;
            sentrybomb.renderYawOffset = this.renderYawOffset;
            sentrybomb.rotationPitch = this.rotationPitch;
            double d0 = entityliving.posX + entityliving.motionX - this.posX;
            double d5 = entityliving.posY - this.posY;
            double d6 = entityliving.posZ + entityliving.motionZ - this.posZ;
            float f1 = MathHelper.sqrt_double(d0 * d0 + d6 * d6);

            if (!this.worldObj.isRemote)
            {
                sentrybomb.setThrowableHeading(d0, d5 + f1 * 0.2F, d6, 0.75F, 8.0F);
                this.worldObj.spawnEntityInWorld(sentrybomb);
            }
        }
    }
}

