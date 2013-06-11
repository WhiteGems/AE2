package net.aetherteam.aether.entities.mounts;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.ArrayList;

import net.aetherteam.aether.Aether;
import net.aetherteam.aether.AetherMoaColour;
import net.aetherteam.aether.PlayerBaseAetherServer;
import net.aetherteam.aether.entities.EntityAetherAnimal;
import net.aetherteam.aether.items.AetherItems;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityMoa extends EntityAetherAnimal implements Mount
{
    float speed;
    public float field_752_b;
    public float destPos;
    public float field_757_d;
    public float field_756_e;
    public float field_755_h;
    public boolean riderJumped;
    public int timeUntilNextEgg;
    boolean followPlayer;

    public EntityMoa(World var1)
    {
        this(var1, false, false, false, (EntityPlayer) null);
    }

    public EntityMoa(World var1, boolean var2, boolean var3, boolean var4, AetherMoaColour var5, EntityPlayer var6, String var7)
    {
        this(var1, var2, var3, var4, var5, var6);

        if (var7 != null && !var7.isEmpty())
        {
            this.texture = var7;
        }
    }

    public EntityMoa(World var1, boolean var2, boolean var3, boolean var4, AetherMoaColour var5, EntityPlayer var6)
    {
        super(var1);
        this.followPlayer = false;
        this.destPos = 0.0F;
        this.field_755_h = 1.0F;
        this.stepHeight = 1.0F;
        this.setSaddled(var4);
        this.setGrown(var3);
        this.setBaby(var2);

        if (this.isBaby())
        {
            this.setSize(0.4F, 0.5F);

            if (var6 != null)
            {
                ;
            }
        }

        this.setColour(var5);
        this.texture = this.getColour().getTexture(this.getSaddled());
        this.setSize(1.0F, 2.0F);
        this.timeUntilNextEgg = this.rand.nextInt(6000) + 6000;
        this.speed = 0.3F;
        this.getNavigator().setAvoidsWater(true);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIPanic(this, 0.38F));
        this.tasks.addTask(2, new EntityAIWander(this, this.speed));
        this.tasks.addTask(4, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.tasks.addTask(5, new EntityAILookIdle(this));
        this.ignoreFrustumCheck = true;
    }

    public EntityMoa(World var1, boolean var2, boolean var3, boolean var4, EntityPlayer var5)
    {
        this(var1, var2, var3, var4, AetherMoaColour.pickRandomMoa(), var5);
    }

    protected void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(16, Byte.valueOf((byte) 0));
        this.dataWatcher.addObject(17, Byte.valueOf((byte) 0));
        this.dataWatcher.addObject(18, Byte.valueOf((byte) 0));
        this.dataWatcher.addObject(19, Integer.valueOf(0));
        this.dataWatcher.addObject(20, Short.valueOf((short) 0));
        this.dataWatcher.addObject(21, Byte.valueOf((byte) 0));
        this.dataWatcher.addObject(22, String.valueOf(this.texture));
        this.dataWatcher.addObject(23, Integer.valueOf(this.getColour().jumps));
        this.dataWatcher.addObject(24, Integer.valueOf(this.health));
    }

    /**
     * Returns true if the newer Entity AI code should be run
     */
    public boolean isAIEnabled()
    {
        return !this.getSaddled();
    }

    public boolean shouldRiderFaceForward(EntityPlayer var1)
    {
        return true;
    }

    /**
     * Plays step sound at given x, y, z for the entity
     */
    protected void playStepSound(int var1, int var2, int var3, int var4)
    {
        this.worldObj.playSoundAtEntity(this, "mob.pig.step", 0.15F, 1.0F);
    }

    /**
     * Causes this entity to do an upwards motion (jumping).
     */
    protected void jump()
    {
        this.motionY = 0.6D;
    }

    /**
     * returns true if all the conditions for steering the entity are met. For pigs, this is true if it is being ridden
     * by a player and the player is holding a carrot-on-a-stick
     */
    public boolean canBeSteered()
    {
        return true;
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

        if (this.riddenByEntity == null)
        {
            this.setTexture(this.getColour().getTexture(this.getSaddled()));
        } else
        {
            this.setTexture(this.getColour().getTexture(this.getSaddled(), (EntityPlayer) this.riddenByEntity));
        }

        this.fallDistance = 0.0F;

        if (!this.worldObj.isRemote && !this.isBaby() && --this.timeUntilNextEgg <= 0)
        {
            this.worldObj.playSoundAtEntity(this, "mob.chickenplop", 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
            boolean var1 = false;

            if (this.rand.nextInt(20) == 0)
            {
                var1 = true;
            }

            if (var1)
            {
                this.entityDropItem(new ItemStack(AetherItems.MoaMusicDisk, 1, 0), 0.0F);
            } else
            {
                this.entityDropItem(new ItemStack(AetherItems.MoaEgg, 1, this.getColour().ID), 0.0F);
            }

            this.timeUntilNextEgg = this.rand.nextInt(6000) + 6000;
        }

        if (this.isWellFed() && this.rand.nextInt(750) == 0)
        {
            this.setWellFed(false);
        }

        this.updateWingFields();

        if (this.onGround)
        {
            this.riderJumped = false;
            this.setJumpsRemaining(this.getColour().jumps);
        }

        if (!this.onGround && this.motionY < 0.0D)
        {
            if (this.riddenByEntity != null)
            {
                if (this.riddenByEntity instanceof EntityPlayer)
                {
                    EntityPlayer var2 = (EntityPlayer) this.riddenByEntity;

                    if (!var2.isSneaking())
                    {
                        this.motionY *= 0.6375D;
                    }
                }
            } else
            {
                this.motionY *= 0.6D;
            }
        }
    }

    /**
     * Returns the Y offset from the entity's position for any entity riding this one.
     */
    public double getMountedYOffset()
    {
        return 1.37D;
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
                EntityPlayer var1 = (EntityPlayer) this.riddenByEntity;
                this.setRotation(this.riddenByEntity.rotationYaw, this.riddenByEntity.rotationPitch);
                double var2 = 0.40499999701976774D;

                if (var1.isSneaking())
                {
                    var2 = 0.15D;
                }

                float var4 = (float) Math.PI;
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
                        this.motionX += -Math.sin((double) var13) * var2;
                        this.motionZ += Math.cos((double) var13) * var2;
                    } else if (var9)
                    {
                        var13 = var1.rotationYaw * var5;
                        this.motionX += Math.sin((double) var13) * var2;
                        this.motionZ += -Math.cos((double) var13) * var2;
                    }

                    if (var10)
                    {
                        var13 = var1.rotationYaw * var5;
                        this.motionX += Math.cos((double) var13) * var2;
                        this.motionZ += Math.sin((double) var13) * var2;
                    } else if (var11)
                    {
                        var13 = var1.rotationYaw * var5;
                        this.motionX += -Math.cos((double) var13) * var2;
                        this.motionZ += -Math.sin((double) var13) * var2;
                    }

                    if (this.onGround && var12)
                    {
                        this.onGround = false;
                        this.motionY = 0.875D;
                        this.riderJumped = true;
                        this.setJumpsRemaining(this.getJumpsRemaining() - 1);
                    } else if (this.handleWaterMovement() && var12)
                    {
                        this.motionY = 0.5D;
                        this.riderJumped = true;
                        this.setJumpsRemaining(this.getJumpsRemaining() - 1);
                    } else if (this.getJumpsRemaining() > 0 && !this.riderJumped && var12)
                    {
                        this.motionY = 0.75D;
                        this.riderJumped = true;
                        this.setJumpsRemaining(this.getJumpsRemaining() - 1);
                    }

                    if (this.riderJumped && !var12)
                    {
                        this.riderJumped = false;
                    }
                }

                double var15 = Math.abs(Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ));

                if (var15 > var2)
                {
                    double var14 = var2 / var15;
                    this.motionX *= var14;
                    this.motionZ *= var14;
                }
            } else
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
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound var1)
    {
        super.writeEntityToNBT(var1);
        var1.setShort("ColourNumber", (short) this.getColour().ID);
        var1.setBoolean("Saddled", this.getSaddled());
        var1.setBoolean("Grown", this.isGrown());
        var1.setBoolean("Baby", this.isBaby());
        var1.setBoolean("wellFed", this.isWellFed());
        var1.setInteger("petalsEaten", this.getPetalsEaten());
        var1.setBoolean("followPlayer", this.followPlayer);
        var1.setInteger("jumpsRemaining", this.getJumpsRemaining());
        var1.setBoolean("riderJumped", this.riderJumped);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound var1)
    {
        super.readEntityFromNBT(var1);
        this.setColour(AetherMoaColour.getColour(var1.getShort("ColourNumber")));
        this.setSaddled(var1.getBoolean("Saddled"));
        this.setGrown(var1.getBoolean("Grown"));
        this.setBaby(var1.getBoolean("Baby"));
        this.setWellFed(var1.getBoolean("wellFed"));
        this.setPetalsEaten(var1.getInteger("petalsEaten"));
        this.followPlayer = var1.getBoolean("followPlayer");
        this.setJumpsRemaining(var1.getInteger("jumpsRemaining"));
        this.riderJumped = var1.getBoolean("riderJumped");
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected String getLivingSound()
    {
        return "aemob.moa.say";
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected String getHurtSound()
    {
        return "aemob.moa.say";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected String getDeathSound()
    {
        return "aemob.moa.say";
    }

    /**
     * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a pig.
     */
    public boolean interact(EntityPlayer var1)
    {
        if (this.canAddSaddle() && var1.inventory.getCurrentItem() != null && var1.inventory.getCurrentItem().itemID == Item.saddle.itemID)
        {
            var1.inventory.setInventorySlotContents(var1.inventory.currentItem, (ItemStack) null);

            if (!this.worldObj.isRemote)
            {
                this.setGrown(false);
                this.setSaddled(true);
                this.setTexture(this.getColour().getTexture(this.getSaddled()));
            }

            return true;
        } else if (this.getSaddled() && (this.riddenByEntity == null || var1 == this.riddenByEntity))
        {
            if (!var1.worldObj.isRemote)
            {
                if (this.riddenByEntity == null)
                {
                    this.setTexture(this.getColour().getTexture(this.getSaddled(), var1));
                } else
                {
                    this.setTexture(this.getColour().getTexture(this.getSaddled()));
                }

                var1.mountEntity(this);
                var1.prevRotationYaw = var1.rotationYaw = this.rotationYaw;
            }

            return true;
        } else
        {
            ItemStack var2 = var1.inventory.getCurrentItem();

            if (!this.worldObj.isRemote)
            {
                if (var2 != null && var2.itemID == AetherItems.AechorPetal.itemID)
                {
                    if (!this.isWellFed() && this.isBaby())
                    {
                        this.increasePetalsEaten(1);
                        var1.inventory.decrStackSize(var1.inventory.currentItem, 1);

                        if (this.getPetalsEaten() >= this.getColour().jumps)
                        {
                            this.setBaby(false);
                            this.setGrown(true);
                        } else
                        {
                            this.setWellFed(true);
                        }
                    }

                    return true;
                }

                if (this.isBaby() || this.isGrown())
                {
                    if (!this.followPlayer)
                    {
                        this.followPlayer = true;
                        this.entityToAttack = var1;
                    } else
                    {
                        this.followPlayer = false;
                        this.entityToAttack = null;
                    }
                }
            }

            return super.interact(var1);
        }
    }

    public void updateWingFields()
    {
        this.field_756_e = this.field_752_b;
        this.field_757_d = this.destPos;
        this.destPos = (float) ((double) this.destPos + (double) (this.onGround ? -1 : 4) * 0.05D);

        if (this.destPos < 0.01F)
        {
            this.destPos = 0.01F;
        }

        if (this.destPos > 1.0F)
        {
            this.destPos = 1.0F;
        }

        if (this.onGround)
        {
            this.destPos = 0.0F;
        }

        if (!this.onGround && this.field_755_h < 1.0F)
        {
            this.field_755_h = 1.0F;
        }

        this.field_755_h = (float) ((double) this.field_755_h * 0.9D);
        this.field_752_b += this.field_755_h * 2.0F;
    }

    public boolean getSaddled()
    {
        return (this.dataWatcher.getWatchableObjectByte(16) & 1) != 0;
    }

    public void setSaddled(boolean var1)
    {
        if (var1)
        {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte) 1));
            this.health = this.getMaxHealth();
        } else
        {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte) 0));
        }
    }

    public boolean isBaby()
    {
        return (this.dataWatcher.getWatchableObjectByte(17) & 1) != 0;
    }

    public void setBaby(boolean var1)
    {
        if (var1)
        {
            this.dataWatcher.updateObject(17, Byte.valueOf((byte) 1));
        } else
        {
            this.dataWatcher.updateObject(17, Byte.valueOf((byte) 0));
        }
    }

    public boolean isGrown()
    {
        return (this.dataWatcher.getWatchableObjectByte(18) & 1) != 0;
    }

    public void setGrown(boolean var1)
    {
        if (var1)
        {
            this.dataWatcher.updateObject(18, Byte.valueOf((byte) 1));
        } else
        {
            this.dataWatcher.updateObject(18, Byte.valueOf((byte) 0));
        }
    }

    public int getPetalsEaten()
    {
        return this.dataWatcher.getWatchableObjectInt(19);
    }

    public void increasePetalsEaten(int var1)
    {
        this.dataWatcher.updateObject(19, Integer.valueOf(this.dataWatcher.getWatchableObjectInt(19) + var1));
    }

    public void setPetalsEaten(int var1)
    {
        this.dataWatcher.updateObject(19, Integer.valueOf(var1));
    }

    public AetherMoaColour getColour()
    {
        return AetherMoaColour.getColour(this.dataWatcher.getWatchableObjectShort(20));
    }

    public void setColour(AetherMoaColour var1)
    {
        this.dataWatcher.updateObject(20, Short.valueOf((short) var1.ID));
    }

    public boolean isWellFed()
    {
        return (this.dataWatcher.getWatchableObjectByte(21) & 1) != 0;
    }

    public void setWellFed(boolean var1)
    {
        if (var1)
        {
            this.dataWatcher.updateObject(21, Byte.valueOf((byte) 1));
        } else
        {
            this.dataWatcher.updateObject(21, Byte.valueOf((byte) 0));
        }
    }

    public int getJumpsRemaining()
    {
        return this.dataWatcher.getWatchableObjectInt(23);
    }

    public void setJumpsRemaining(int var1)
    {
        this.dataWatcher.updateObject(23, Integer.valueOf(var1));
    }

    public int getHealthTracked()
    {
        return this.dataWatcher.getWatchableObjectInt(24);
    }

    public void setHealthTracked()
    {
        this.dataWatcher.updateObject(24, Integer.valueOf(this.health));
    }

    @SideOnly(Side.CLIENT)

    /**
     * Returns the texture's file path as a String.
     */
    public String getTexture()
    {
        return this.texture;
    }

    public void setTexture(String var1)
    {
        this.texture = var1;
    }

    public boolean canAddSaddle()
    {
        return this.isGrown();
    }

    /**
     * Determines if an entity can be despawned, used on idle far away entities
     */
    public boolean canDespawn()
    {
        return !this.isBaby() && !this.isGrown() && !this.getSaddled();
    }

    /**
     * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
     * prevent them from trampling crops
     */
    protected boolean canTriggerWalking()
    {
        return this.onGround;
    }

    /**
     * Drop 0-2 items of this living's type. @param par1 - Whether this entity has recently been hit by a player. @param
     * par2 - Level of Looting used to kill this mob.
     */
    protected void dropFewItems(boolean var1, int var2)
    {
        this.dropItem(Item.feather.itemID, 3);
        this.dropSaddle();
    }

    protected void dropSaddle()
    {
        if (this.getSaddled())
        {
            this.dropItem(Item.saddle.itemID, 1);
        }
    }

    /**
     * Called when the mob is falling. Calculates and applies fall damage.
     */
    protected void fall(float var1)
    {}

    public int getMaxHealth()
    {
        return this.getSaddled() ? 40 : (!this.isGrown() && !this.isBaby() ? 10 : 20);
    }

    public EntityAgeable createChild(EntityAgeable var1)
    {
        return null;
    }
}
