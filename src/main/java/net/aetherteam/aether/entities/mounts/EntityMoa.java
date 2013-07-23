package net.aetherteam.aether.entities.mounts;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Random;
import net.aetherteam.aether.Aether;
import net.aetherteam.aether.AetherMoaColour;
import net.aetherteam.aether.PlayerBaseAetherServer;
import net.aetherteam.aether.entities.EntityAetherAnimal;
import net.aetherteam.aether.items.AetherItems;
import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityMoa extends EntityAetherAnimal
    implements Mount
{
    float speed;
    public float field_752_b;
    public float destPos;
    public float field_757_d;
    public float field_756_e;
    public float field_755_h;
    public boolean riderJumped;
    public int timeUntilNextEgg;
    boolean followPlayer = false;

    public EntityMoa(World world)
    {
        this(world, false, false, false, null);
    }

    public EntityMoa(World world, boolean babyBool, boolean grownBool, boolean saddledBool, AetherMoaColour moaColour, EntityPlayer player, String texture)
    {
        this(world, babyBool, grownBool, saddledBool, moaColour, player);

        if ((texture != null) && (!texture.isEmpty()))
        {
            this.texture = texture;
        }
    }

    public EntityMoa(World world, boolean babyBool, boolean grownBool, boolean saddledBool, AetherMoaColour moaColour, EntityPlayer player)
    {
        super(world);
        this.destPos = 0.0F;
        this.field_755_h = 1.0F;
        this.stepHeight = 1.0F;
        setSaddled(saddledBool);
        setGrown(grownBool);
        setBaby(babyBool);

        if (isBaby())
        {
            setSize(0.4F, 0.5F);

            if (player == null);
        }

        setColour(moaColour);
        this.texture = getColour().getTexture(getSaddled());
        setSize(1.0F, 2.0F);
        this.timeUntilNextEgg = (this.rand.nextInt(6000) + 6000);
        this.speed = 0.3F;
        getNavigator().setAvoidsWater(true);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIPanic(this, 0.38F));
        this.tasks.addTask(2, new EntityAIWander(this, this.speed));
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
        this.dataWatcher.addObject(22, String.valueOf(this.texture));
        this.dataWatcher.addObject(23, Integer.valueOf(getColour().jumps));
        this.dataWatcher.addObject(24, Integer.valueOf(this.health));
    }

    public boolean isAIEnabled()
    {
        return !getSaddled();
    }

    public boolean shouldRiderFaceForward(EntityPlayer player)
    {
        return true;
    }

    protected void playStepSound(int par1, int par2, int par3, int par4)
    {
        this.worldObj.playSoundAtEntity(this, "mob.pig.step", 0.15F, 1.0F);
    }

    protected void jump()
    {
        this.motionY = 0.6D;
    }

    public boolean canBeSteered()
    {
        return true;
    }

    public void onUpdate()
    {
        super.onUpdate();

        if ((!this.worldObj.isRemote) && (getHealthTracked() != this.health))
        {
            setHealthTracked();
        }

        if (this.riddenByEntity == null)
        {
            setTexture(getColour().getTexture(getSaddled()));
        }
        else
        {
            setTexture(getColour().getTexture(getSaddled(), (EntityPlayer)this.riddenByEntity));
        }

        this.fallDistance = 0.0F;

        if ((!this.worldObj.isRemote) && (!isBaby()) && (--this.timeUntilNextEgg <= 0))
        {
            this.worldObj.playSoundAtEntity(this, "mob.chickenplop", 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
            boolean spawnMusicDisk = false;

            if (this.rand.nextInt(20) == 0)
            {
                spawnMusicDisk = true;
            }

            if (spawnMusicDisk)
            {
                entityDropItem(new ItemStack(AetherItems.MoaMusicDisk, 1, 0), 0.0F);
            }
            else
            {
                entityDropItem(new ItemStack(AetherItems.MoaEgg, 1, getColour().ID), 0.0F);
            }

            this.timeUntilNextEgg = (this.rand.nextInt(6000) + 6000);
        }

        if ((isWellFed()) && (this.rand.nextInt(750) == 0))
        {
            setWellFed(false);
        }

        updateWingFields();

        if (this.onGround)
        {
            this.riderJumped = false;
            setJumpsRemaining(getColour().jumps);
        }

        if ((!this.onGround) && (this.motionY < 0.0D))
        {
            if (this.riddenByEntity != null)
            {
                if ((this.riddenByEntity instanceof EntityPlayer))
                {
                    EntityPlayer player = (EntityPlayer)this.riddenByEntity;

                    if (!player.isSneaking())
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

    public double getMountedYOffset()
    {
        return 1.37D;
    }

    public void updateEntityActionState()
    {
        if (!this.worldObj.isRemote)
        {
            if ((this.riddenByEntity != null) && ((this.riddenByEntity instanceof EntityPlayer)))
            {
                this.moveForward = 0.0F;
                this.moveStrafing = 0.0F;
                this.isJumping = false;
                this.riddenByEntity.fallDistance = 0.0F;
                EntityPlayer player = (EntityPlayer)this.riddenByEntity;
                setRotation(this.riddenByEntity.rotationYaw, this.riddenByEntity.rotationPitch);
                double mountSpeed = 0.4049999970197677D;

                if (player.isSneaking())
                {
                    mountSpeed = 0.15D;
                }

                float magicRotationFloat = (float)Math.PI;
                float f1 = magicRotationFloat / 180.0F;
                PlayerBaseAetherServer playerBase = Aether.getServerPlayer(player);

                if (playerBase != null)
                {
                    ArrayList directions = playerBase.mountInput;
                    boolean moveForward = directions.contains(MountInput.FORWARD);
                    boolean moveBackward = directions.contains(MountInput.BACKWARD);
                    boolean moveLeft = directions.contains(MountInput.LEFT);
                    boolean moveRight = directions.contains(MountInput.RIGHT);
                    boolean jump = directions.contains(MountInput.JUMP);

                    if (moveForward)
                    {
                        float rotationFactor_FORWARD = player.rotationYaw * f1;
                        this.motionX += -Math.sin(rotationFactor_FORWARD) * mountSpeed;
                        this.motionZ += Math.cos(rotationFactor_FORWARD) * mountSpeed;
                    }
                    else if (moveBackward)
                    {
                        float rotationFactor_BACKWARD = player.rotationYaw * f1;
                        this.motionX += Math.sin(rotationFactor_BACKWARD) * mountSpeed;
                        this.motionZ += -Math.cos(rotationFactor_BACKWARD) * mountSpeed;
                    }

                    if (moveLeft)
                    {
                        float rotationFactor_LEFT = player.rotationYaw * f1;
                        this.motionX += Math.cos(rotationFactor_LEFT) * mountSpeed;
                        this.motionZ += Math.sin(rotationFactor_LEFT) * mountSpeed;
                    }
                    else if (moveRight)
                    {
                        float rotationFactor_RIGHT = player.rotationYaw * f1;
                        this.motionX += -Math.cos(rotationFactor_RIGHT) * mountSpeed;
                        this.motionZ += -Math.sin(rotationFactor_RIGHT) * mountSpeed;
                    }

                    if ((this.onGround) && (jump))
                    {
                        this.onGround = false;
                        this.motionY = 0.875D;
                        this.riderJumped = true;
                        setJumpsRemaining(getJumpsRemaining() - 1);
                    }
                    else if ((handleWaterMovement()) && (jump))
                    {
                        this.motionY = 0.5D;
                        this.riderJumped = true;
                        setJumpsRemaining(getJumpsRemaining() - 1);
                    }
                    else if ((getJumpsRemaining() > 0) && (!this.riderJumped) && (jump))
                    {
                        this.motionY = 0.75D;
                        this.riderJumped = true;
                        setJumpsRemaining(getJumpsRemaining() - 1);
                    }

                    if ((this.riderJumped) && (!jump))
                    {
                        this.riderJumped = false;
                    }
                }

                double d = Math.abs(Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ));

                if (d > mountSpeed)
                {
                    double d1 = mountSpeed / d;
                    this.motionX *= d1;
                    this.motionZ *= d1;
                }

                return;
            }

            super.updateEntityActionState();
            return;
        }
    }

    public boolean attackEntityFrom(DamageSource damagesource, int i)
    {
        if (((damagesource.getEntity() instanceof EntityPlayer)) && (this.riddenByEntity == damagesource.getEntity()))
        {
            return false;
        }

        return super.attackEntityFrom(damagesource, i);
    }

    public void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setShort("ColourNumber", (short)getColour().ID);
        nbttagcompound.setBoolean("Saddled", getSaddled());
        nbttagcompound.setBoolean("Grown", isGrown());
        nbttagcompound.setBoolean("Baby", isBaby());
        nbttagcompound.setBoolean("wellFed", isWellFed());
        nbttagcompound.setInteger("petalsEaten", getPetalsEaten());
        nbttagcompound.setBoolean("followPlayer", this.followPlayer);
        nbttagcompound.setInteger("jumpsRemaining", getJumpsRemaining());
        nbttagcompound.setBoolean("riderJumped", this.riderJumped);
    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readEntityFromNBT(nbttagcompound);
        setColour(AetherMoaColour.getColour(nbttagcompound.getShort("ColourNumber")));
        setSaddled(nbttagcompound.getBoolean("Saddled"));
        setGrown(nbttagcompound.getBoolean("Grown"));
        setBaby(nbttagcompound.getBoolean("Baby"));
        setWellFed(nbttagcompound.getBoolean("wellFed"));
        setPetalsEaten(nbttagcompound.getInteger("petalsEaten"));
        this.followPlayer = nbttagcompound.getBoolean("followPlayer");
        setJumpsRemaining(nbttagcompound.getInteger("jumpsRemaining"));
        this.riderJumped = nbttagcompound.getBoolean("riderJumped");
    }

    protected String getLivingSound()
    {
        return "aemob.moa.say";
    }

    protected String getHurtSound()
    {
        return "aemob.moa.say";
    }

    protected String getDeathSound()
    {
        return "aemob.moa.say";
    }

    public boolean interact(EntityPlayer entityplayer)
    {
        if ((canAddSaddle()) && (entityplayer.inventory.getCurrentItem() != null) && (entityplayer.inventory.getCurrentItem().itemID == Item.saddle.itemID))
        {
            entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, null);

            if (!this.worldObj.isRemote)
            {
                setGrown(false);
                setSaddled(true);
                setTexture(getColour().getTexture(getSaddled()));
            }

            return true;
        }

        if ((getSaddled()) && ((this.riddenByEntity == null) || (entityplayer == this.riddenByEntity)))
        {
            if (!entityplayer.worldObj.isRemote)
            {
                if (this.riddenByEntity == null)
                {
                    setTexture(getColour().getTexture(getSaddled(), entityplayer));
                }
                else
                {
                    setTexture(getColour().getTexture(getSaddled()));
                }

                entityplayer.mountEntity(this);
                entityplayer.prevRotationYaw = (entityplayer.rotationYaw = this.rotationYaw);
            }

            return true;
        }

        ItemStack itemstack = entityplayer.inventory.getCurrentItem();

        if (!this.worldObj.isRemote)
        {
            if ((itemstack != null) && (itemstack.itemID == AetherItems.AechorPetal.itemID))
            {
                if ((!isWellFed()) && (isBaby()))
                {
                    increasePetalsEaten(1);
                    entityplayer.inventory.decrStackSize(entityplayer.inventory.currentItem, 1);

                    if (getPetalsEaten() >= getColour().jumps)
                    {
                        setBaby(false);
                        setGrown(true);
                    }
                    else
                    {
                        setWellFed(true);
                    }
                }

                return true;
            }

            if ((isBaby()) || (isGrown()))
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

    public void updateWingFields()
    {
        this.field_756_e = this.field_752_b;
        this.field_757_d = this.destPos;
        this.destPos = ((float)(this.destPos + (this.onGround ? -1 : 4) * 0.05D));

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

        if ((!this.onGround) && (this.field_755_h < 1.0F))
        {
            this.field_755_h = 1.0F;
        }

        this.field_755_h = ((float)(this.field_755_h * 0.9D));
        this.field_752_b += this.field_755_h * 2.0F;
    }

    public boolean getSaddled()
    {
        return (this.dataWatcher.getWatchableObjectByte(16) & 0x1) != 0;
    }

    public void setSaddled(boolean saddled)
    {
        if (saddled)
        {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte)1));
            this.health = getMaxHealth();
        }
        else
        {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte)0));
        }
    }

    public boolean isBaby()
    {
        return (this.dataWatcher.getWatchableObjectByte(17) & 0x1) != 0;
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
        return (this.dataWatcher.getWatchableObjectByte(18) & 0x1) != 0;
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
        return (this.dataWatcher.getWatchableObjectByte(21) & 0x1) != 0;
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
        this.dataWatcher.updateObject(24, Integer.valueOf(this.health));
    }

    @SideOnly(Side.CLIENT)
    public String getTexture()
    {
        return this.texture;
    }

    public void setTexture(String texture)
    {
        this.texture = texture;
    }

    public boolean canAddSaddle()
    {
        return isGrown();
    }

    public boolean canDespawn()
    {
        return (!isBaby()) && (!isGrown()) && (!getSaddled());
    }

    protected boolean canTriggerWalking()
    {
        return this.onGround;
    }

    protected void dropFewItems(boolean var1, int var2)
    {
        dropItem(Item.feather.itemID, 3);
        dropSaddle();
    }

    protected void dropSaddle()
    {
        if (getSaddled())
        {
            dropItem(Item.saddle.itemID, 1);
        }
    }

    protected void fall(float f)
    {
    }

    public int getMaxHealth()
    {
        return (isGrown()) || (isBaby()) ? 20 : getSaddled() ? 40 : 10;
    }

    public EntityAgeable createChild(EntityAgeable entityageable)
    {
        return null;
    }
}

