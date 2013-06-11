package net.aetherteam.aether.entities.mounts;

import java.util.Random;

import net.aetherteam.aether.Aether;
import net.aetherteam.aether.blocks.AetherBlocks;
import net.aetherteam.aether.entities.EntityAetherAnimal;
import net.aetherteam.aether.entities.ai.AIEntityAerbunnyHop;
import net.aetherteam.aether.entities.ai.AIEntityEatBlock;
import net.aetherteam.aether.entities.mounts_old.Ridable;
import net.aetherteam.aether.entities.mounts_old.RidingHandler;
import net.aetherteam.aether.entities.mounts_old.RidingHandlerAerbunny;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
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

public class EntityAerbunny extends EntityAetherAnimal implements Ridable
{
    public AIEntityEatBlock aiEatGrass;
    private int jumpTicks;
    private int jumps;
    public RidingHandler ridinghandler;
    public int age;
    public int mate;
    public boolean grab;
    public boolean fear;
    public boolean gotrider;
    public Entity runFrom;
    public float puffiness;

    public EntityAerbunny(World var1)
    {
        super(var1);
        this.aiEatGrass = new AIEntityEatBlock(this, AetherBlocks.AetherGrass, AetherBlocks.AetherDirt);
        this.moveSpeed = 2.5F;
        this.getNavigator().setAvoidsWater(true);
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIAvoidEntity(this, EntityPlayer.class, 16.0F, 2.6F, 2.8F));
        this.tasks.addTask(3, new EntityAIWander(this, 2.5F));
        this.tasks.addTask(4, new EntityAIWatchClosest(this, EntityPlayer.class, 10.0F));
        this.tasks.addTask(5, this.aiEatGrass);
        this.tasks.addTask(6, new AIEntityAerbunnyHop(this));
        this.texture = this.dir + "/mobs/aerbunny/aerbunny.png";
        this.setSize(0.4F, 0.4F);
        this.health = 6;
        this.age = this.rand.nextInt(64);
        this.mate = 0;
        this.ignoreFrustumCheck = true;
        this.ridinghandler = new RidingHandlerAerbunny(this);
    }

    /**
     * Checks if the entity is in range to render by using the past in distance and comparing it to its average edge
     * length * 64 * renderDistanceWeight Args: distance
     */
    public boolean isInRangeToRenderDist(double var1)
    {
        return true;
    }

    /**
     * Returns true if the newer Entity AI code should be run
     */
    protected boolean isAIEnabled()
    {
        return true;
    }

    /**
     * Causes this entity to do an upwards motion (jumping).
     */
    protected void jump()
    {
        for (int var1 = 0; var1 < 5; ++var1)
        {
            Aether.proxy.spawnCloudSmoke(this.worldObj, this.posX, this.posY + 0.5D, this.posZ, new Random(), Double.valueOf(0.5D));
        }

        this.setPuffiness(11);
        --this.jumps;
        super.jump();
    }

    public int getPuffiness()
    {
        return this.dataWatcher.getWatchableObjectInt(16);
    }

    public void setPuffiness(int var1)
    {
        Integer var2 = Integer.valueOf(this.dataWatcher.getWatchableObjectInt(16));
        this.dataWatcher.updateObject(16, Integer.valueOf(var1));
    }

    protected void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(16, new Integer(0));
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        this.setPuffiness(this.getPuffiness() - 1);

        if (this.getPuffiness() < 0)
        {
            this.setPuffiness(0);
        }

        super.onUpdate();
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate()
    {
        if (this.onGround)
        {
            this.jumps = 1;
            this.jumpTicks = 10;
        } else if (this.jumpTicks > 0)
        {
            --this.jumpTicks;
        }

        if (this.isJumping && !this.isInWater() && !this.handleLavaMovement() && !this.onGround && this.jumpTicks == 0 && this.jumps > 0)
        {
            this.jump();
            this.jumpTicks = 10;
        }

        if (this.motionY < -0.1D)
        {
            this.motionY = -0.1D;
        }

        super.onLivingUpdate();
    }

    /**
     * Called when the mob is falling. Calculates and applies fall damage.
     */
    protected void fall(float var1)
    {}

    /**
     * Checks if this entity is inside of an opaque block
     */
    public boolean isEntityInsideOpaqueBlock()
    {
        return false;
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound var1)
    {
        super.writeEntityToNBT(var1);
        var1.setBoolean("Fear", this.fear);
        var1.setShort("RepAge", (short) this.age);
        var1.setShort("RepMate", (short) this.mate);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound var1)
    {
        super.readEntityFromNBT(var1);
        this.fear = var1.getBoolean("Fear");
        this.age = var1.getShort("RepAge");
        this.mate = var1.getShort("RepMate");
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource var1, int var2)
    {
        return var1.getEntity() instanceof EntityPlayer && this.getRidingHandler().getRider() instanceof EntityPlayer ? false : super.attackEntityFrom(var1, var2);
    }

    public void cloudPoop()
    {
        double var1 = (double) (this.rand.nextFloat() - 0.5F);
        double var5 = (double) (this.rand.nextFloat() - 0.5F);
        double var7 = this.posX + var1 * 0.4000000059604645D;
        double var9 = this.boundingBox.minY;
        double var11 = this.posZ + var1 * 0.4000000059604645D;
        this.worldObj.spawnParticle("flame", var7, var9, var11, 0.0D, -0.07500000298023224D, 0.0D);
    }

    /**
     * returns true if this entity is by a ladder, false otherwise
     */
    public boolean isOnLadder()
    {
        return this.moveForward != 0.0F;
    }

    public EntityAnimal spawnBabyAnimal(EntityAnimal var1)
    {
        return new EntityAerbunny(this.worldObj);
    }

    public boolean isWheat(ItemStack var1)
    {
        return var1.itemID == Item.wheat.itemID;
    }

    /**
     * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a pig.
     */
    public boolean interact(EntityPlayer var1)
    {
        ItemStack var2 = var1.inventory.getCurrentItem();

        if (var2 != null && this.isWheat(var2))
        {
            return super.interact(var1);
        } else
        {
            if (this.riddenByEntity == null && !this.getRidingHandler().isBeingRidden())
            {
                this.getRidingHandler().setRider(var1);
                this.worldObj.playSoundAtEntity(this, "aemob.aerbunny.lift", 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
            } else
            {
                this.getRidingHandler().onUnMount();
            }

            return true;
        }
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected String getLivingSound()
    {
        return null;
    }

    /**
     * Drop 0-2 items of this living's type. @param par1 - Whether this entity has recently been hit by a player. @param
     * par2 - Level of Looting used to kill this mob.
     */
    protected void dropFewItems(boolean var1, int var2)
    {
        this.dropItem(Item.silk.itemID, 1);
    }

    public void proceed()
    {
        this.mate = 0;
        this.age = this.rand.nextInt(64);
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
     * Returns the sound this mob makes when it is hurt.
     */
    protected String getHurtSound()
    {
        return "aemob.aerbunny.hurt";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected String getDeathSound()
    {
        return "aemob.aerbunny.die";
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    public boolean getCanSpawnHere()
    {
        return super.getCanSpawnHere();
    }

    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    public boolean canBeCollidedWith()
    {
        return this.getRidingHandler().getRider() instanceof EntityPlayer ? false : super.canBeCollidedWith();
    }

    public int getMaxHealth()
    {
        return 5;
    }

    public RidingHandler getRidingHandler()
    {
        return this.ridinghandler;
    }

    public EntityAgeable createChild(EntityAgeable var1)
    {
        return null;
    }
}
