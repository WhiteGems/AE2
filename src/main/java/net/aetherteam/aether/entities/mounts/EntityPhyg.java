package net.aetherteam.aether.entities.mounts;

import java.util.ArrayList;
import java.util.Random;
import net.aetherteam.aether.Aether;
import net.aetherteam.aether.PlayerBaseAetherServer;
import net.aetherteam.aether.entities.EntityAetherAnimal;
import net.aetherteam.aether.items.AetherItems;
import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityPhyg extends EntityAetherAnimal
    implements Mount
{
    float speed;
    public float wingFold;
    public float wingAngle;
    private float aimingForFold;
    public int jumps;
    public int jumpsRemaining;
    private boolean jpress;
    private int ticks;
    public boolean riderJumped;

    public EntityPhyg(World world)
    {
        super(world);
        this.speed = 0.3F;
        getNavigator().setAvoidsWater(true);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIPanic(this, 0.38F));
        this.tasks.addTask(2, new EntityAIWander(this, this.speed));
        this.tasks.addTask(6, new EntityAIMate(this, 0.25F));
        this.tasks.addTask(3, new EntityAITempt(this, 0.25F, AetherItems.EnchantedBerry.itemID, false));
        this.tasks.addTask(4, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.tasks.addTask(5, new EntityAILookIdle(this));
        this.texture = (this.dir + "/mobs/phyg/phyg.png");
        setSize(0.9F, 1.3F);
        this.jumpsRemaining = 0;
        this.jumps = 1;
        this.ticks = 0;
        this.stepHeight = 1.0F;
        this.ignoreFrustumCheck = true;
        this.health = getMaxHealth();
    }

    public boolean isAIEnabled()
    {
        return this.riddenByEntity == null;
    }

    public boolean isEntityInsideOpaqueBlock()
    {
        if (this.riddenByEntity != null)
        {
            return false;
        }

        return super.isEntityInsideOpaqueBlock();
    }

    protected boolean canDespawn()
    {
        return !getSaddled();
    }

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

    public int getHealthTracked()
    {
        return this.dataWatcher.getWatchableObjectInt(17);
    }

    public boolean shouldRiderFaceForward(EntityPlayer player)
    {
        return true;
    }

    public boolean canBeSteered()
    {
        return true;
    }

    public void setHealthTracked()
    {
        this.dataWatcher.updateObject(17, Integer.valueOf(this.health));
    }

    public void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setShort("Jumps", (short)this.jumps);
        nbttagcompound.setShort("Remaining", (short)this.jumpsRemaining);
        nbttagcompound.setBoolean("getSaddled", getSaddled());
    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readEntityFromNBT(nbttagcompound);
        this.jumps = nbttagcompound.getShort("Jumps");
        this.jumpsRemaining = nbttagcompound.getShort("Remaining");
        setSaddled(nbttagcompound.getBoolean("getSaddled"));
    }

    protected void playStepSound(int par1, int par2, int par3, int par4)
    {
        this.worldObj.playSoundAtEntity(this, "mob.pig.step", 0.15F, 1.0F);
    }

    protected void jump()
    {
        this.motionY = 1.45D;
    }

    public void onUpdate()
    {
        super.onUpdate();

        if ((!this.worldObj.isRemote) && (getHealthTracked() != this.health))
        {
            setHealthTracked();
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

        this.ticks += 1;
        this.wingAngle = (this.wingFold * (float)Math.sin(this.ticks / 31.830988F));
        this.wingFold += (this.aimingForFold - this.wingFold) / 5.0F;
        this.fallDistance = 0.0F;

        if (getSaddled())
        {
            this.texture = (this.dir + "/mobs/phyg/saddle.png");
        }
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
                        jump();
                        this.riderJumped = true;
                    }
                    else if ((handleWaterMovement()) && (jump))
                    {
                        jump();
                        this.riderJumped = true;
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

    protected String getLivingSound()
    {
        return "mob.pig.say";
    }

    protected String getHurtSound()
    {
        return "mob.pig.say";
    }

    protected String getDeathSound()
    {
        return "mob.pig.death";
    }

    public EntityAnimal spawnBabyAnimal(EntityAnimal entityanimal)
    {
        return new EntityPhyg(this.worldObj);
    }

    public double getMountedYOffset()
    {
        return 0.75D;
    }

    public boolean interact(EntityPlayer entityplayer)
    {
        if (super.interact(entityplayer))
        {
            return super.interact(entityplayer);
        }

        if ((!getSaddled()) && (entityplayer.inventory.getCurrentItem() != null) && (entityplayer.inventory.getCurrentItem().itemID == Item.saddle.itemID) && (!isChild()))
        {
            entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, null);
            setSaddled(true);
            this.texture = (this.dir + "/mobs/phyg/saddle.png");
            return true;
        }

        if ((getSaddled()) && ((this.riddenByEntity == null) || (entityplayer == this.riddenByEntity)))
        {
            if (!entityplayer.worldObj.isRemote)
            {
                entityplayer.mountEntity(this);
                entityplayer.prevRotationYaw = (entityplayer.rotationYaw = this.rotationYaw);
            }

            return true;
        }

        return true;
    }

    protected void dropFewItems(boolean var1, int var2)
    {
        dropItem(this.rand.nextBoolean() ? Item.feather.itemID : Item.porkRaw.itemID, 1);
        dropSaddle();
    }

    protected void dropSaddle()
    {
        if (getSaddled())
        {
            dropItem(Item.saddle.itemID, 1);
        }
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

    public int getMaxHealth()
    {
        return getSaddled() ? 40 : 10;
    }

    public boolean isBreedingItem(ItemStack par1ItemStack)
    {
        return (par1ItemStack != null) && (par1ItemStack.getItem().itemID == AetherItems.EnchantedBerry.itemID);
    }

    public EntityAgeable createChild(EntityAgeable entityageable)
    {
        return spawnBabyAnimal((EntityAnimal)entityageable);
    }
}

