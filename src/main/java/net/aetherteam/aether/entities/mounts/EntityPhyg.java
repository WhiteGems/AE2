package net.aetherteam.aether.entities.mounts;

import java.util.ArrayList;
import net.aetherteam.aether.Aether;
import net.aetherteam.aether.PlayerAetherServer;
import net.aetherteam.aether.entities.EntityAetherAnimal;
import net.aetherteam.aether.items.AetherItems;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityPhyg extends EntityAetherAnimal implements Mount
{
    float speed = 0.3F;
    public float wingFold;
    public float wingAngle;
    private float aimingForFold;
    public int jumps;
    public int jumpsRemaining;
    private int ticks;
    public boolean riderJumped;

    public EntityPhyg(World world)
    {
        super(world);
        this.getNavigator().setAvoidsWater(true);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIPanic(this, 0.3799999952316284D));
        this.tasks.addTask(2, new EntityAIWander(this, (double)this.speed));
        this.tasks.addTask(6, new EntityAIMate(this, 0.25D));
        this.tasks.addTask(3, new EntityAITempt(this, 0.25D, AetherItems.EnchantedBerry.itemID, false));
        this.tasks.addTask(4, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.tasks.addTask(5, new EntityAILookIdle(this));
        this.setSize(0.9F, 1.3F);
        this.jumpsRemaining = 0;
        this.jumps = 1;
        this.ticks = 0;
        this.stepHeight = 1.0F;
        this.ignoreFrustumCheck = true;
        this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(10.0D);
        this.setEntityHealth(10.0F);
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource damagesource, float i)
    {
        return damagesource.getEntity() instanceof EntityPlayer && this.riddenByEntity == damagesource.getEntity() ? false : super.attackEntityFrom(damagesource, i);
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
     * Determines if an entity can be despawned, used on idle far away entities
     */
    protected boolean canDespawn()
    {
        return !this.getSaddled();
    }

    /**
     * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
     * prevent them from trampling crops
     */
    protected boolean canTriggerWalking()
    {
        return this.onGround;
    }

    public EntityAgeable createChild(EntityAgeable entityageable)
    {
        return this.spawnBabyAnimal((EntityAnimal)entityageable);
    }

    /**
     * Drop 0-2 items of this living's type. @param par1 - Whether this entity has recently been hit by a player. @param
     * par2 - Level of Looting used to kill this mob.
     */
    protected void dropFewItems(boolean var1, int var2)
    {
        this.dropItem(this.rand.nextBoolean() ? Item.feather.itemID : Item.porkRaw.itemID, 1);
        this.dropSaddle();
    }

    protected void dropSaddle()
    {
        if (this.getSaddled())
        {
            this.dropItem(Item.saddle.itemID, 1);
        }
    }

    protected void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(16, Byte.valueOf((byte)0));
        this.dataWatcher.addObject(17, Integer.valueOf((int)this.func_110143_aJ()));
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected String getDeathSound()
    {
        return "mob.pig.death";
    }

    public int getHealthTracked()
    {
        return this.dataWatcher.getWatchableObjectInt(17);
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected String getHurtSound()
    {
        return "mob.pig.say";
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected String getLivingSound()
    {
        return "mob.pig.say";
    }

    /**
     * Returns the Y offset from the entity's position for any entity riding this one.
     */
    public double getMountedYOffset()
    {
        return 0.75D;
    }

    public boolean getSaddled()
    {
        return (this.dataWatcher.getWatchableObjectByte(16) & 1) != 0;
    }

    /**
     * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a pig.
     */
    public boolean interact(EntityPlayer entityplayer)
    {
        if (super.interact(entityplayer))
        {
            return super.interact(entityplayer);
        }
        else if (!this.getSaddled() && entityplayer.inventory.getCurrentItem() != null && entityplayer.inventory.getCurrentItem().itemID == Item.saddle.itemID && !this.isChild())
        {
            entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, (ItemStack)null);
            this.setSaddled(true);
            return true;
        }
        else if (this.getSaddled() && (this.riddenByEntity == null || entityplayer == this.riddenByEntity))
        {
            if (!entityplayer.worldObj.isRemote)
            {
                entityplayer.mountEntity(this);
                entityplayer.prevRotationYaw = entityplayer.rotationYaw = this.rotationYaw;
            }

            return true;
        }
        else
        {
            return true;
        }
    }

    /**
     * Returns true if the newer Entity AI code should be run
     */
    public boolean isAIEnabled()
    {
        return this.riddenByEntity == null;
    }

    /**
     * Checks if the parameter is an item which this animal can be fed to breed it (wheat, carrots or seeds depending on
     * the animal type)
     */
    public boolean isBreedingItem(ItemStack par1ItemStack)
    {
        return par1ItemStack != null && par1ItemStack.getItem().itemID == AetherItems.EnchantedBerry.itemID;
    }

    /**
     * Checks if this entity is inside of an opaque block
     */
    public boolean isEntityInsideOpaqueBlock()
    {
        return this.riddenByEntity != null ? false : super.isEntityInsideOpaqueBlock();
    }

    /**
     * Causes this entity to do an upwards motion (jumping).
     */
    protected void jump()
    {
        this.motionY = 1.45D;
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

        if (!this.onGround && this.motionY < 0.0D)
        {
            if (this.riddenByEntity != null)
            {
                if (this.riddenByEntity instanceof EntityPlayer)
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
            ;
        }
    }

    /**
     * Plays step sound at given x, y, z for the entity
     */
    protected void playStepSound(int par1, int par2, int par3, int par4)
    {
        this.worldObj.playSoundAtEntity(this, "mob.pig.step", 0.15F, 1.0F);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readEntityFromNBT(nbttagcompound);
        this.jumps = nbttagcompound.getShort("Jumps");
        this.jumpsRemaining = nbttagcompound.getShort("Remaining");
        this.setSaddled(nbttagcompound.getBoolean("getSaddled"));
    }

    public void setHealthTracked()
    {
        this.dataWatcher.updateObject(17, Integer.valueOf((int)this.func_110143_aJ()));
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
            this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(10.0D);
            this.dataWatcher.updateObject(16, Byte.valueOf((byte)0));
        }
    }

    public boolean shouldRiderFaceForward(EntityPlayer player)
    {
        return true;
    }

    public EntityAnimal spawnBabyAnimal(EntityAnimal entityanimal)
    {
        return new EntityPhyg(this.worldObj);
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
                        this.jump();
                        this.riderJumped = true;
                    }
                    else if (this.handleWaterMovement() && jump)
                    {
                        this.jump();
                        this.riderJumped = true;
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
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setShort("Jumps", (short)this.jumps);
        nbttagcompound.setShort("Remaining", (short)this.jumpsRemaining);
        nbttagcompound.setBoolean("getSaddled", this.getSaddled());
    }
}
