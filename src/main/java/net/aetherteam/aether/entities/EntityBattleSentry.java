package net.aetherteam.aether.entities;

import net.aetherteam.aether.blocks.AetherBlocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityBattleSentry extends EntityAetherMob
{
    public String dir = "/net/aetherteam/aether/client/sprites";

    /**
     * Reference to the World object.
     */
    private World worldObj;
    private int timeTilHide;
    public float field_100021_a;
    public float field_100020_b;
    private int jcount;
    public int size;
    public int counter;
    public int lostyou;

    public EntityBattleSentry(World var1)
    {
        super(var1);
        this.texture = this.dir + "/mobs/sentryMelee/sentryMelee.png";
        this.size = 2;
        this.yOffset = 0.0F;
        this.moveSpeed = 1.0F;
        this.field_100021_a = 1.0F;
        this.field_100020_b = 1.0F;
        this.jcount = this.rand.nextInt(20) + 10;
        this.func_100019_e(this.size);
        this.worldObj = var1;
        this.timeTilHide = 0;
    }

    public EntityBattleSentry(World var1, double var2, double var4, double var6)
    {
        super(var1);
        this.texture = this.dir + "/mobs/sentryMelee/sentryMelee.png";
        this.size = 2;
        this.yOffset = 0.0F;
        this.moveSpeed = 1.0F;
        this.field_100021_a = 1.0F;
        this.field_100020_b = 1.0F;
        this.jcount = this.rand.nextInt(20) + 10;
        this.func_100019_e(this.size);
        this.rotationYaw = (float) this.rand.nextInt(4) * ((float) Math.PI / 2F);
        this.setPosition(var2, var4, var6);
        this.worldObj = var1;
        this.timeTilHide = 0;
    }

    public void func_100019_e(int var1)
    {
        this.setEntityHealth(10);
        this.width = 0.85F;
        this.height = 0.85F;
        this.setPosition(this.posX, this.posY, this.posZ);
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound var1)
    {
        super.writeEntityToNBT(var1);
        var1.setInteger("Size", this.size - 1);
        var1.setBoolean("seen", this.isInView());
        this.setHasBeenAttacked(var1.getBoolean("HasBeenAttacked"));
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound var1)
    {
        super.readEntityFromNBT(var1);
        this.size = var1.getInteger("Size") + 1;
        this.setInView(var1.getBoolean("seen"));
        var1.setBoolean("HasBeenAttacked", this.getHasBeenAttacked());
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource var1, int var2)
    {
        if (var1.getEntity() != null)
        {
            this.setHasBeenAttacked(true);
            this.timeTilHide = 50;
        }

        return super.attackEntityFrom(var1, var2);
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        boolean var1 = this.onGround;
        super.onUpdate();

        if (this.onGround && !var1)
        {
            this.worldObj.playSoundAtEntity(this, "mob.slime.small", this.getSoundVolume(), ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F) / 0.8F);
        } else if (!this.onGround && var1)
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
        } else
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

        if (this.entityToAttack instanceof EntityPlayer && (EntityPlayer) this.entityToAttack != null)
        {
            this.setInView(!this.isInFieldOfVision((EntityPlayer) this.entityToAttack, this));
        }
    }

    private boolean isInFieldOfVision(EntityPlayer var1, Entity var2)
    {
        float var3 = var1.rotationPitch;
        float var4 = var1.rotationYaw;
        var1.faceEntity(var2, 360.0F, 360.0F);
        float var5 = var1.rotationPitch;
        float var6 = var1.rotationYaw;
        var1.rotationPitch = var3;
        var1.rotationYaw = var4;
        float var7 = 70.0F;
        float var8 = 65.0F;
        float var9 = var1.rotationPitch - var7;
        float var10 = var1.rotationPitch + var7;
        float var11 = var1.rotationYaw - var8;
        float var12 = var1.rotationYaw + var8;
        boolean var13 = this.GetFlag(var9, var10, var5, 0.0F, 360.0F);
        boolean var14 = this.GetFlag(var11, var12, var6, -180.0F, 180.0F);
        return var13 && var14;
    }

    public boolean GetFlag(float var1, float var2, float var3, float var4, float var5)
    {
        if (var1 < var4)
        {
            if (var3 >= var1 + var5)
            {
                return true;
            }

            if (var3 <= var2)
            {
                return true;
            }
        }

        if (var2 >= var5)
        {
            if (var3 <= var2 - var5)
            {
                return true;
            }

            if (var3 >= var1)
            {
                return true;
            }
        }

        return var2 < var5 && var1 >= var4 ? var3 <= var2 && var3 > var1 : false;
    }

    protected void updateEntityActionState()
    {
        super.updateEntityActionState();
        EntityPlayer var1 = this.worldObj.getClosestPlayerToEntity(this, 8.0D);

        if (var1 != null && this.canEntityBeSeen(var1) && !var1.capabilities.isCreativeMode)
        {
            this.faceEntity(var1, 10.0F, 10.0F);
            this.entityToAttack = var1;
            this.texture = this.dir + "/mobs/sentry/sentry_lit.png";
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
        } else
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
    public void onCollideWithPlayer(EntityPlayer var1)
    {
        super.onCollideWithPlayer(var1);

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
    public int getAttackStrength(Entity var1)
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
        this.dataWatcher.addObject(16, Byte.valueOf((byte) 0));
        this.dataWatcher.addObject(17, Byte.valueOf((byte) 0));
    }

    public boolean isInView()
    {
        return this.dataWatcher.getWatchableObjectByte(16) == 0;
    }

    public void setInView(boolean var1)
    {
        if (var1)
        {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte) 1));
        } else
        {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte) 0));
        }
    }

    public boolean getHasBeenAttacked()
    {
        return this.dataWatcher.getWatchableObjectByte(17) == 1;
    }

    public void setHasBeenAttacked(boolean var1)
    {
        if (var1)
        {
            this.dataWatcher.updateObject(17, Byte.valueOf((byte) 1));
        } else
        {
            this.dataWatcher.updateObject(17, Byte.valueOf((byte) 0));
        }
    }

    public int getMaxHealth()
    {
        return 10;
    }
}
