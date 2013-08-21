package net.aetherteam.aether.entities;

import net.aetherteam.aether.blocks.AetherBlocks;
import net.aetherteam.playercore_api.cores.IPlayerCoreCommon;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityBattleSentry extends EntityDungeonMob
{
    /** Reference to the World object. */
    private World worldObj;
    private int timeTilHide;
    public float field_100021_a;
    public float field_100020_b;
    private int jcount;
    public int size = 2;
    public int counter;
    public int lostyou;

    public EntityBattleSentry(World world)
    {
        super(world);
        this.yOffset = 0.0F;
        this.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111128_a(1.0D);
        this.field_100021_a = 1.0F;
        this.field_100020_b = 1.0F;
        this.jcount = this.rand.nextInt(20) + 10;
        this.func_100019_e(this.size);
        this.worldObj = world;
        this.timeTilHide = 0;
    }

    public EntityBattleSentry(World world, double x, double y, double z)
    {
        super(world);
        this.yOffset = 0.0F;
        this.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111128_a(1.0D);
        this.field_100021_a = 1.0F;
        this.field_100020_b = 1.0F;
        this.jcount = this.rand.nextInt(20) + 10;
        this.func_100019_e(this.size);
        this.rotationYaw = (float)this.rand.nextInt(4) * ((float)Math.PI / 2F);
        this.setPosition(x, y, z);
        this.worldObj = world;
        this.timeTilHide = 0;
    }

    public void func_100019_e(int i)
    {
        this.setEntityHealth(10.0F);
        this.width = 0.85F;
        this.height = 0.85F;
        this.setPosition(this.posX, this.posY, this.posZ);
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setInteger("Size", this.size - 1);
        nbttagcompound.setBoolean("seen", this.isInView());
        this.setHasBeenAttacked(nbttagcompound.getBoolean("HasBeenAttacked"));
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readEntityFromNBT(nbttagcompound);
        this.size = nbttagcompound.getInteger("Size") + 1;
        this.setInView(nbttagcompound.getBoolean("seen"));
        nbttagcompound.setBoolean("HasBeenAttacked", this.getHasBeenAttacked());
    }

    public boolean attackEntityFrom(DamageSource par1DamageSource, int par2)
    {
        if (par1DamageSource.getEntity() != null)
        {
            this.setHasBeenAttacked(true);
            this.timeTilHide = 50;
        }

        return super.attackEntityFrom(par1DamageSource, par2);
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        boolean flag = this.onGround;
        super.onUpdate();

        if (this.onGround && !flag)
        {
            this.worldObj.playSoundAtEntity(this, "mob.slime.small", this.getSoundVolume(), ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F) / 0.8F);
        }
        else if (!this.onGround && flag)
        {
            this.motionX *= 3.0D;
            this.motionZ *= 3.0D;
        }

        if (this.entityToAttack != null && this.entityToAttack.isDead)
        {
            this.entityToAttack = null;
        }

        if (this.timeTilHide != 0)
        {
            this.setHasBeenAttacked(true);
            --this.timeTilHide;
        }
        else
        {
            this.setHasBeenAttacked(false);
        }
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate()
    {
        super.onLivingUpdate();

        if (this.entityToAttack instanceof EntityPlayer && (EntityPlayer)this.entityToAttack != null)
        {
            this.setInView(!this.isInFieldOfVision((EntityPlayer)this.entityToAttack, this));
        }
    }

    private boolean isInFieldOfVision(EntityPlayer entityPlayer, Entity entity)
    {
        float f = entityPlayer.rotationPitch;
        float f1 = entityPlayer.rotationYaw;
        ((IPlayerCoreCommon)entityPlayer).faceEntity(entity, 360.0F, 360.0F);
        float f2 = entityPlayer.rotationPitch;
        float f3 = entityPlayer.rotationYaw;
        entityPlayer.rotationPitch = f;
        entityPlayer.rotationYaw = f1;
        float f4 = 70.0F;
        float f5 = 65.0F;
        float f6 = entityPlayer.rotationPitch - f4;
        float f7 = entityPlayer.rotationPitch + f4;
        float f8 = entityPlayer.rotationYaw - f5;
        float f9 = entityPlayer.rotationYaw + f5;
        boolean flag = this.GetFlag(f6, f7, f2, 0.0F, 360.0F);
        boolean flag1 = this.GetFlag(f8, f9, f3, -180.0F, 180.0F);
        return flag && flag1;
    }

    public boolean GetFlag(float f, float f1, float f2, float f3, float f4)
    {
        if (f < f3)
        {
            if (f2 >= f + f4)
            {
                return true;
            }

            if (f2 <= f1)
            {
                return true;
            }
        }

        if (f1 >= f4)
        {
            if (f2 <= f1 - f4)
            {
                return true;
            }

            if (f2 >= f)
            {
                return true;
            }
        }

        return f1 < f4 && f >= f3 ? f2 <= f1 && f2 > f : false;
    }

    protected void updateEntityActionState()
    {
        super.updateEntityActionState();
        EntityPlayer entityplayer = this.worldObj.getClosestPlayerToEntity(this, 8.0D);

        if (entityplayer != null && this.canEntityBeSeen(entityplayer) && !entityplayer.capabilities.isCreativeMode)
        {
            this.faceEntity(entityplayer, 10.0F, 10.0F);
            this.entityToAttack = entityplayer;
        }

        if (this.entityToAttack != null)
        {
            this.faceEntity(this.entityToAttack, 10.0F, 10.0F);
        }

        if ((!this.onGround || this.jcount-- > 0 || this.isInView()) && !this.getHasBeenAttacked())
        {
            this.isJumping = false;

            if (this.onGround)
            {
                this.moveStrafing = this.moveForward = 0.0F;
            }
        }
        else
        {
            this.jcount = this.rand.nextInt(20) + 10;
            this.isJumping = true;
            this.moveStrafing = 0.5F - this.rand.nextFloat();
            this.moveForward = 1.0F;
            this.worldObj.playSoundAtEntity(this, "mob.slime.small", this.getSoundVolume(), ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F) * 0.8F);
            this.jcount /= 2;
            this.moveForward = 1.0F;
        }
    }

    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    public boolean canBeCollidedWith()
    {
        return true;
    }

    /**
     * Called by a player entity when they collide with an entity
     */
    public void onCollideWithPlayer(EntityPlayer par1EntityPlayer)
    {
        super.onCollideWithPlayer(par1EntityPlayer);

        if (!this.getHasBeenAttacked())
        {
            this.setHasBeenAttacked(true);
            this.timeTilHide = 15;
        }
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected String getHurtSound()
    {
        return "mob.slime.small";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected String getDeathSound()
    {
        return "mob.slime.small";
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    public boolean getCanSpawnHere()
    {
        return super.getCanSpawnHere();
    }

    /**
     * Returns the volume for the sounds this mob makes.
     */
    protected float getSoundVolume()
    {
        return 0.6F;
    }

    /**
     * Returns the amount of damage a mob should deal.
     */
    public int getAttackStrength(Entity par1Entity)
    {
        return 2;
    }

    /**
     * Returns the item ID for the item the mob drops on death.
     */
    protected int getDropItemId()
    {
        return this.rand.nextInt(5) == 0 ? AetherBlocks.LightDungeonStone.blockID : AetherBlocks.DungeonStone.blockID;
    }

    public void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(16, Byte.valueOf((byte)0));
        this.dataWatcher.addObject(17, Byte.valueOf((byte)0));
    }

    public boolean isInView()
    {
        return this.dataWatcher.getWatchableObjectByte(16) == 0;
    }

    public void setInView(boolean awake)
    {
        if (awake)
        {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte)1));
        }
        else
        {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte)0));
        }
    }

    public boolean getHasBeenAttacked()
    {
        return this.dataWatcher.getWatchableObjectByte(17) == 1;
    }

    public void setHasBeenAttacked(boolean attack)
    {
        if (attack)
        {
            this.dataWatcher.updateObject(17, Byte.valueOf((byte)1));
        }
        else
        {
            this.dataWatcher.updateObject(17, Byte.valueOf((byte)0));
        }
    }
}
