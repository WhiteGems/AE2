package net.aetherteam.aether.entities.mounts;

import java.util.ArrayList;
import net.aetherteam.aether.Aether;
import net.aetherteam.aether.PlayerBaseAetherServer;
import net.aetherteam.aether.entities.EntityAetherAnimal;
import net.aetherteam.aether.entities.ai.AIEntityControlledByPlayerPhyg;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityFlyingCow extends EntityAetherAnimal implements Mount
{
    private AIEntityControlledByPlayerPhyg entityAIControlled;
    float speed;
    public float wingFold;
    public float wingAngle;
    private float aimingForFold;
    public int jumps;
    public int jumpsRemaining;
    private boolean jpress;
    private int ticks;
    public boolean riderJumped;

    public EntityFlyingCow(World var1)
    {
        super(var1);
        this.texture = this.dir + "/mobs/flyingcow/flyingcow.png";
        this.speed = 0.3F;
        this.getNavigator().setAvoidsWater(true);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIPanic(this, 0.38F));
        this.tasks.addTask(2, new EntityAIWander(this, this.speed));
        this.tasks.addTask(4, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.tasks.addTask(5, new EntityAILookIdle(this));
        this.setSize(0.9F, 1.3F);
        this.jumpsRemaining = 0;
        this.jumps = 1;
        this.ticks = 0;
        this.stepHeight = 1.0F;
        this.ignoreFrustumCheck = true;
    }

    /**
     * Returns true if the newer Entity AI code should be run
     */
    public boolean isAIEnabled()
    {
        return this.riddenByEntity == null;
    }

    /**
     * Return the AI task for player control.
     */
    public AIEntityControlledByPlayerPhyg getAIControlledByPlayer()
    {
        return this.entityAIControlled;
    }

    /**
     * Determines if an entity can be despawned, used on idle far away entities
     */
    protected boolean canDespawn()
    {
        return !this.getSaddled();
    }

    /**
     * Checks if this entity is inside of an opaque block
     */
    public boolean isEntityInsideOpaqueBlock()
    {
        return this.riddenByEntity != null ? false : super.isEntityInsideOpaqueBlock();
    }

    /**
     * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
     * prevent them from trampling crops
     */
    protected boolean canTriggerWalking()
    {
        return this.onGround;
    }

    protected void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(16, Byte.valueOf((byte)0));
        this.dataWatcher.addObject(17, Integer.valueOf(this.health));
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound var1)
    {
        super.writeEntityToNBT(var1);
        var1.setShort("Jumps", (short)this.jumps);
        var1.setShort("Remaining", (short)this.jumpsRemaining);
        var1.setBoolean("getSaddled", this.getSaddled());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound var1)
    {
        super.readEntityFromNBT(var1);
        this.jumps = var1.getShort("Jumps");
        this.jumpsRemaining = var1.getShort("Remaining");
        this.setSaddled(var1.getBoolean("getSaddled"));
    }

    /**
     * Returns the Y offset from the entity's position for any entity riding this one.
     */
    public double getMountedYOffset()
    {
        return 1.25D;
    }

    public void updateEntityActionState()
    {
        if (!this.worldObj.isRemote)
        {
            if (this.riddenByEntity != null && this.riddenByEntity instanceof EntityPlayer)
            {
                this.moveForward = 0.0F;
                this.moveStrafing = 0.0F;
                this.isJumping = false;
                this.riddenByEntity.fallDistance = 0.0F;
                EntityPlayer var1 = (EntityPlayer)this.riddenByEntity;
                this.setRotation(this.riddenByEntity.rotationYaw, this.riddenByEntity.rotationPitch);
                double var2 = 0.40499999701976774D;

                if (var1.isSneaking())
                {
                    var2 = 0.15D;
                }

                float var4 = (float)Math.PI;
                float var5 = var4 / 180.0F;
                PlayerBaseAetherServer var6 = Aether.getServerPlayer(var1);

                if (var6 != null)
                {
                    ArrayList var7 = var6.mountInput;
                    boolean var8 = var7.contains(MountInput.FORWARD);
                    boolean var9 = var7.contains(MountInput.BACKWARD);
                    boolean var10 = var7.contains(MountInput.LEFT);
                    boolean var11 = var7.contains(MountInput.RIGHT);
                    boolean var12 = var7.contains(MountInput.JUMP);
                    float var13;

                    if (var8)
                    {
                        var13 = var1.rotationYaw * var5;
                        this.motionX += -Math.sin((double)var13) * var2;
                        this.motionZ += Math.cos((double)var13) * var2;
                    }
                    else if (var9)
                    {
                        var13 = var1.rotationYaw * var5;
                        this.motionX += Math.sin((double)var13) * var2;
                        this.motionZ += -Math.cos((double)var13) * var2;
                    }

                    if (var10)
                    {
                        var13 = var1.rotationYaw * var5;
                        this.motionX += Math.cos((double)var13) * var2;
                        this.motionZ += Math.sin((double)var13) * var2;
                    }
                    else if (var11)
                    {
                        var13 = var1.rotationYaw * var5;
                        this.motionX += -Math.cos((double)var13) * var2;
                        this.motionZ += -Math.sin((double)var13) * var2;
                    }

                    if (this.onGround && var12)
                    {
                        this.onGround = false;
                        this.jump();
                        this.riderJumped = true;
                    }
                    else if (this.handleWaterMovement() && var12)
                    {
                        this.jump();
                        this.riderJumped = true;
                    }

                    if (this.riderJumped && !var12)
                    {
                        this.riderJumped = false;
                    }
                }

                double var14 = Math.abs(Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ));

                if (var14 > var2)
                {
                    double var15 = var2 / var14;
                    this.motionX *= var15;
                    this.motionZ *= var15;
                }
            }
            else
            {
                super.updateEntityActionState();
            }
        }
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource var1, int var2)
    {
        return var1.getEntity() instanceof EntityPlayer && this.riddenByEntity == var1.getEntity() ? false : super.attackEntityFrom(var1, var2);
    }

    /**
     * Causes this entity to do an upwards motion (jumping).
     */
    protected void jump()
    {
        this.motionY = 1.45D;
    }

    public int getHealthTracked()
    {
        return this.dataWatcher.getWatchableObjectInt(17);
    }

    public boolean shouldRiderFaceForward(EntityPlayer var1)
    {
        return true;
    }

    /**
     * returns true if all the conditions for steering the entity are met. For pigs, this is true if it is being ridden
     * by a player and the player is holding a carrot-on-a-stick
     */
    public boolean canBeSteered()
    {
        return true;
    }

    public void setHealthTracked()
    {
        this.dataWatcher.updateObject(17, Integer.valueOf(this.health));
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();

        if (!this.worldObj.isRemote && this.getHealthTracked() != this.health)
        {
            this.setHealthTracked();
        }

        if (!this.onGround && this.motionY < 0.0D)
        {
            if (this.riddenByEntity != null)
            {
                if (this.riddenByEntity instanceof EntityPlayer)
                {
                    EntityPlayer var1 = (EntityPlayer)this.riddenByEntity;

                    if (!var1.isSneaking())
                    {
                        this.motionY *= 0.6375D;
                    }
                }
            }
            else
            {
                this.motionY *= 0.6D;
            }
        }

        if (this.onGround)
        {
            this.wingAngle *= 0.8F;
            this.aimingForFold = 0.1F;
            this.jpress = false;
            this.jumpsRemaining = this.jumps;
        }
        else
        {
            this.aimingForFold = 1.0F;
        }

        ++this.ticks;
        this.wingAngle = this.wingFold * (float)Math.sin((double)((float)this.ticks / 31.830988F));
        this.wingFold += (this.aimingForFold - this.wingFold) / 5.0F;
        this.fallDistance = 0.0F;

        if (this.getSaddled())
        {
            this.texture = this.dir + "/mobs/flyingcow/saddle.png";
        }
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected String getLivingSound()
    {
        return "mob.cow.say";
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected String getHurtSound()
    {
        return "mob.cow.hurt";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected String getDeathSound()
    {
        return "mob.cow.hurt";
    }

    /**
     * Returns the volume for the sounds this mob makes.
     */
    protected float getSoundVolume()
    {
        return 0.4F;
    }

    /**
     * Plays step sound at given x, y, z for the entity
     */
    protected void playStepSound(int var1, int var2, int var3, int var4)
    {
        this.worldObj.playSoundAtEntity(this, "mob.cow.step", 0.15F, 1.0F);
    }

    public EntityAnimal spawnBabyAnimal(EntityAnimal var1)
    {
        return new EntityFlyingCow(this.worldObj);
    }

    /**
     * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a pig.
     */
    public boolean interact(EntityPlayer var1)
    {
        if (!this.getSaddled() && var1.inventory.getCurrentItem() != null && var1.inventory.getCurrentItem().itemID == Item.saddle.itemID && !this.isChild())
        {
            var1.inventory.setInventorySlotContents(var1.inventory.currentItem, (ItemStack)null);
            this.setSaddled(true);
            this.texture = this.dir + "/mobs/flyingcow/saddle.png";
            return true;
        }
        else if (!this.getSaddled())
        {
            return super.interact(var1);
        }
        else if (this.getSaddled() && (this.riddenByEntity == null || var1 == this.riddenByEntity))
        {
            if (!var1.worldObj.isRemote)
            {
                var1.mountEntity(this);
                var1.prevRotationYaw = var1.rotationYaw = this.rotationYaw;
            }

            return true;
        }
        else
        {
            return true;
        }
    }

    public boolean getSaddled()
    {
        return (this.dataWatcher.getWatchableObjectByte(16) & 1) != 0;
    }

    public void setSaddled(boolean var1)
    {
        if (var1)
        {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte)1));
            this.health = this.getMaxHealth();
        }
        else
        {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte)0));
        }
    }

    /**
     * Drop 0-2 items of this living's type. @param par1 - Whether this entity has recently been hit by a player. @param
     * par2 - Level of Looting used to kill this mob.
     */
    protected void dropFewItems(boolean var1, int var2)
    {
        this.dropItem(Item.leather.itemID, 2);
        this.dropSaddle();
    }

    protected void dropSaddle()
    {
        if (this.getSaddled())
        {
            this.dropItem(Item.saddle.itemID, 1);
        }
    }

    public int getMaxHealth()
    {
        return this.getSaddled() ? 40 : 10;
    }

    public EntityAgeable createChild(EntityAgeable var1)
    {
        return this;
    }
}
