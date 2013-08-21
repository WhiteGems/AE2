package net.aetherteam.aether.entities.mounts;

import java.util.ArrayList;
import net.aetherteam.aether.Aether;
import net.aetherteam.aether.AetherMoaColour;
import net.aetherteam.aether.PlayerAetherServer;
import net.aetherteam.aether.entities.EntityAetherAnimal;
import net.aetherteam.aether.items.AetherItems;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.SharedMonsterAttributes;
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
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class EntityMoa extends EntityAetherAnimal implements Mount
{
    ResourceLocation texture;
    float speed;
    public float field_752_b;
    public float destPos;
    public float field_757_d;
    public float field_756_e;
    public float field_755_h;
    public boolean riderJumped;
    public int timeUntilNextEgg;
    boolean followPlayer;

    public EntityMoa(World world)
    {
        this(world, false, false, false, (EntityPlayer)null);
    }

    public EntityMoa(World world, boolean babyBool, boolean grownBool, boolean saddledBool, AetherMoaColour moaColour, EntityPlayer player, ResourceLocation texture)
    {
        this(world, babyBool, grownBool, saddledBool, moaColour, player);

        if (texture != null)
        {
            this.texture = texture;
        }
    }

    public EntityMoa(World world, boolean babyBool, boolean grownBool, boolean saddledBool, AetherMoaColour moaColour, EntityPlayer player)
    {
        super(world);
        this.followPlayer = false;
        this.destPos = 0.0F;
        this.field_755_h = 1.0F;
        this.stepHeight = 1.0F;
        this.setSaddled(saddledBool);
        this.setGrown(grownBool);
        this.setBaby(babyBool);
        this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(10.0D);
        this.setEntityHealth(10.0F);

        if (this.isBaby())
        {
            this.setSize(0.4F, 0.5F);

            if (player != null)
            {
                ;
            }
        }

        this.setColour(moaColour);
        this.texture = this.getColour().getTexture(this.getSaddled());
        this.setSize(1.0F, 2.0F);
        this.timeUntilNextEgg = this.rand.nextInt(6000) + 6000;
        this.speed = 0.3F;
        this.getNavigator().setAvoidsWater(true);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIPanic(this, 0.3799999952316284D));
        this.tasks.addTask(2, new EntityAIWander(this, (double)this.speed));
        this.tasks.addTask(4, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.tasks.addTask(5, new EntityAILookIdle(this));
        this.ignoreFrustumCheck = true;
    }

    public EntityMoa(World world, boolean babyBool, boolean grownBool, boolean saddledBool, EntityPlayer player)
    {
        this(world, babyBool, grownBool, saddledBool, AetherMoaColour.pickRandomMoa(), player);
    }

    protected void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(16, Byte.valueOf((byte)0));
        this.dataWatcher.addObject(17, Byte.valueOf((byte)0));
        this.dataWatcher.addObject(18, Byte.valueOf((byte)0));
        this.dataWatcher.addObject(19, Integer.valueOf(0));
        this.dataWatcher.addObject(20, Short.valueOf((short)0));
        this.dataWatcher.addObject(21, Byte.valueOf((byte)0));
        this.dataWatcher.addObject(23, Integer.valueOf(this.getColour().jumps));
        this.dataWatcher.addObject(24, Integer.valueOf((int)this.func_110143_aJ()));
    }

    /**
     * Returns true if the newer Entity AI code should be run
     */
    public boolean isAIEnabled()
    {
        return !this.getSaddled();
    }

    public boolean shouldRiderFaceForward(EntityPlayer player)
    {
        return true;
    }

    /**
     * Plays step sound at given x, y, z for the entity
     */
    protected void playStepSound(int par1, int par2, int par3, int par4)
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

        if (!this.worldObj.isRemote && (float)this.getHealthTracked() != this.func_110143_aJ())
        {
            this.setHealthTracked();
        }

        if (this.riddenByEntity == null)
        {
            this.texture = this.getColour().getTexture(this.getSaddled());
        }
        else
        {
            this.texture = this.getColour().getTexture(this.getSaddled(), (EntityPlayer)this.riddenByEntity);
        }

        this.fallDistance = 0.0F;

        if (!this.worldObj.isRemote && !this.isBaby() && --this.timeUntilNextEgg <= 0)
        {
            this.worldObj.playSoundAtEntity(this, "mob.chickenplop", 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
            boolean player = false;

            if (this.rand.nextInt(20) == 0)
            {
                player = true;
            }

            if (player)
            {
                this.entityDropItem(new ItemStack(AetherItems.MoaMusicDisk, 1, 0), 0.0F);
            }
            else
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
                    EntityPlayer var2 = (EntityPlayer)this.riddenByEntity;

                    if (!var2.isSneaking())
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
                EntityPlayer player = (EntityPlayer)this.riddenByEntity;
                this.setRotation(this.riddenByEntity.rotationYaw, this.riddenByEntity.rotationPitch);
                double mountSpeed = 0.40499999701976774D;

                if (player.isSneaking())
                {
                    mountSpeed = 0.15D;
                }

                float magicRotationFloat = (float)Math.PI;
                float f1 = magicRotationFloat / 180.0F;
                PlayerAetherServer playerBase = Aether.getServerPlayer(player);

                if (playerBase != null)
                {
                    ArrayList d = playerBase.mountInput;
                    boolean moveForward = d.contains(MountInput.FORWARD);
                    boolean d1 = d.contains(MountInput.BACKWARD);
                    boolean moveLeft = d.contains(MountInput.LEFT);
                    boolean moveRight = d.contains(MountInput.RIGHT);
                    boolean jump = d.contains(MountInput.JUMP);
                    float rotationFactor_RIGHT;

                    if (moveForward)
                    {
                        rotationFactor_RIGHT = player.rotationYaw * f1;
                        this.motionX += -Math.sin((double)rotationFactor_RIGHT) * mountSpeed;
                        this.motionZ += Math.cos((double)rotationFactor_RIGHT) * mountSpeed;
                    }
                    else if (d1)
                    {
                        rotationFactor_RIGHT = player.rotationYaw * f1;
                        this.motionX += Math.sin((double)rotationFactor_RIGHT) * mountSpeed;
                        this.motionZ += -Math.cos((double)rotationFactor_RIGHT) * mountSpeed;
                    }

                    if (moveLeft)
                    {
                        rotationFactor_RIGHT = player.rotationYaw * f1;
                        this.motionX += Math.cos((double)rotationFactor_RIGHT) * mountSpeed;
                        this.motionZ += Math.sin((double)rotationFactor_RIGHT) * mountSpeed;
                    }
                    else if (moveRight)
                    {
                        rotationFactor_RIGHT = player.rotationYaw * f1;
                        this.motionX += -Math.cos((double)rotationFactor_RIGHT) * mountSpeed;
                        this.motionZ += -Math.sin((double)rotationFactor_RIGHT) * mountSpeed;
                    }

                    if (this.onGround && jump)
                    {
                        this.onGround = false;
                        this.motionY = 0.875D;
                        this.riderJumped = true;
                        this.setJumpsRemaining(this.getJumpsRemaining() - 1);
                    }
                    else if (this.handleWaterMovement() && jump)
                    {
                        this.motionY = 0.5D;
                        this.riderJumped = true;
                        this.setJumpsRemaining(this.getJumpsRemaining() - 1);
                    }
                    else if (this.getJumpsRemaining() > 0 && !this.riderJumped && jump)
                    {
                        this.motionY = 0.75D;
                        this.riderJumped = true;
                        this.setJumpsRemaining(this.getJumpsRemaining() - 1);
                    }

                    if (this.riderJumped && !jump)
                    {
                        this.riderJumped = false;
                    }
                }

                double d1 = Math.abs(Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ));

                if (d1 > mountSpeed)
                {
                    double d11 = mountSpeed / d1;
                    this.motionX *= d11;
                    this.motionZ *= d11;
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
    public boolean attackEntityFrom(DamageSource damagesource, float i)
    {
        return damagesource.getEntity() instanceof EntityPlayer && this.riddenByEntity == damagesource.getEntity() ? false : super.attackEntityFrom(damagesource, i);
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setShort("ColourNumber", (short)this.getColour().ID);
        nbttagcompound.setBoolean("Saddled", this.getSaddled());
        nbttagcompound.setBoolean("Grown", this.isGrown());
        nbttagcompound.setBoolean("Baby", this.isBaby());
        nbttagcompound.setBoolean("wellFed", this.isWellFed());
        nbttagcompound.setInteger("petalsEaten", this.getPetalsEaten());
        nbttagcompound.setBoolean("followPlayer", this.followPlayer);
        nbttagcompound.setInteger("jumpsRemaining", this.getJumpsRemaining());
        nbttagcompound.setBoolean("riderJumped", this.riderJumped);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readEntityFromNBT(nbttagcompound);
        this.setColour(AetherMoaColour.getColour(nbttagcompound.getShort("ColourNumber")));
        this.setSaddled(nbttagcompound.getBoolean("Saddled"));
        this.setGrown(nbttagcompound.getBoolean("Grown"));
        this.setBaby(nbttagcompound.getBoolean("Baby"));
        this.setWellFed(nbttagcompound.getBoolean("wellFed"));
        this.setPetalsEaten(nbttagcompound.getInteger("petalsEaten"));
        this.followPlayer = nbttagcompound.getBoolean("followPlayer");
        this.setJumpsRemaining(nbttagcompound.getInteger("jumpsRemaining"));
        this.riderJumped = nbttagcompound.getBoolean("riderJumped");
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected String getLivingSound()
    {
        return "aether:aemob.moa.say";
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected String getHurtSound()
    {
        return "aether:aemob.moa.say";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected String getDeathSound()
    {
        return "aether:aemob.moa.say";
    }

    /**
     * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a pig.
     */
    public boolean interact(EntityPlayer entityplayer)
    {
        if (this.canAddSaddle() && entityplayer.inventory.getCurrentItem() != null && entityplayer.inventory.getCurrentItem().itemID == Item.saddle.itemID)
        {
            entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, (ItemStack)null);

            if (!this.worldObj.isRemote)
            {
                this.setGrown(false);
                this.setSaddled(true);
                this.texture = this.getColour().getTexture(this.getSaddled());
            }

            return true;
        }
        else if (this.getSaddled() && (this.riddenByEntity == null || entityplayer == this.riddenByEntity))
        {
            if (!entityplayer.worldObj.isRemote)
            {
                if (this.riddenByEntity == null)
                {
                    this.texture = this.getColour().getTexture(this.getSaddled(), entityplayer);
                }
                else
                {
                    this.texture = this.getColour().getTexture(this.getSaddled());
                }

                entityplayer.mountEntity(this);
                entityplayer.prevRotationYaw = entityplayer.rotationYaw = this.rotationYaw;
            }

            return true;
        }
        else
        {
            ItemStack itemstack = entityplayer.inventory.getCurrentItem();

            if (!this.worldObj.isRemote)
            {
                if (itemstack != null && itemstack.itemID == AetherItems.AechorPetal.itemID)
                {
                    if (!this.isWellFed() && this.isBaby())
                    {
                        this.increasePetalsEaten(1);
                        entityplayer.inventory.decrStackSize(entityplayer.inventory.currentItem, 1);

                        if (this.getPetalsEaten() >= this.getColour().jumps)
                        {
                            this.setBaby(false);
                            this.setGrown(true);
                        }
                        else
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
                        this.entityToAttack = entityplayer;
                    }
                    else
                    {
                        this.followPlayer = false;
                        this.entityToAttack = null;
                    }
                }
            }

            return super.interact(entityplayer);
        }
    }

    public void updateWingFields()
    {
        this.field_756_e = this.field_752_b;
        this.field_757_d = this.destPos;
        this.destPos = (float)((double)this.destPos + (double)(this.onGround ? -1 : 4) * 0.05D);

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

        this.field_755_h = (float)((double)this.field_755_h * 0.9D);
        this.field_752_b += this.field_755_h * 2.0F;
    }

    public boolean getSaddled()
    {
        return (this.dataWatcher.getWatchableObjectByte(16) & 1) != 0;
    }

    public void setSaddled(boolean saddled)
    {
        if (saddled)
        {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte)1));
            this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(40.0D);
            this.setEntityHealth(40.0F);
        }
        else
        {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte)0));
            this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(10.0D);
        }
    }

    public boolean isBaby()
    {
        return (this.dataWatcher.getWatchableObjectByte(17) & 1) != 0;
    }

    public void setBaby(boolean isBaby)
    {
        if (isBaby)
        {
            this.dataWatcher.updateObject(17, Byte.valueOf((byte)1));
        }
        else
        {
            this.dataWatcher.updateObject(17, Byte.valueOf((byte)0));
        }
    }

    public boolean isGrown()
    {
        return (this.dataWatcher.getWatchableObjectByte(18) & 1) != 0;
    }

    public void setGrown(boolean isGrown)
    {
        if (isGrown)
        {
            this.dataWatcher.updateObject(18, Byte.valueOf((byte)1));
        }
        else
        {
            this.dataWatcher.updateObject(18, Byte.valueOf((byte)0));
        }
    }

    public int getPetalsEaten()
    {
        return this.dataWatcher.getWatchableObjectInt(19);
    }

    public void increasePetalsEaten(int amount)
    {
        this.dataWatcher.updateObject(19, Integer.valueOf(this.dataWatcher.getWatchableObjectInt(19) + amount));
    }

    public void setPetalsEaten(int amount)
    {
        this.dataWatcher.updateObject(19, Integer.valueOf(amount));
    }

    public AetherMoaColour getColour()
    {
        return AetherMoaColour.getColour(this.dataWatcher.getWatchableObjectShort(20));
    }

    public void setColour(AetherMoaColour colour)
    {
        this.dataWatcher.updateObject(20, Short.valueOf((short)colour.ID));
    }

    public boolean isWellFed()
    {
        return (this.dataWatcher.getWatchableObjectByte(21) & 1) != 0;
    }

    public int getClassicTounge()
    {
        String[] array = this.dataWatcher.getWatchableObjectString(20).split(",");
        return Integer.parseInt(array[8]);
    }

    public int getClassicTeeth()
    {
        String[] array = this.dataWatcher.getWatchableObjectString(20).split(",");
        return Integer.parseInt(array[9]);
    }

    public void setWellFed(boolean wellFed)
    {
        if (wellFed)
        {
            this.dataWatcher.updateObject(21, Byte.valueOf((byte)1));
        }
        else
        {
            this.dataWatcher.updateObject(21, Byte.valueOf((byte)0));
        }
    }

    public int getJumpsRemaining()
    {
        return this.dataWatcher.getWatchableObjectInt(23);
    }

    public void setJumpsRemaining(int jumps)
    {
        this.dataWatcher.updateObject(23, Integer.valueOf(jumps));
    }

    public int getHealthTracked()
    {
        return this.dataWatcher.getWatchableObjectInt(24);
    }

    public void setHealthTracked()
    {
        this.dataWatcher.updateObject(24, Integer.valueOf((int)this.func_110143_aJ()));
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
    protected void fall(float f) {}

    public EntityAgeable createChild(EntityAgeable entityageable)
    {
        return null;
    }

    public ResourceLocation getTexture()
    {
        return this.texture;
    }
}
