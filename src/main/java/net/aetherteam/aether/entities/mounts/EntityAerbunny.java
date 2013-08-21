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
import net.minecraft.entity.SharedMonsterAttributes;
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

    /** Number of ticks since last jump */
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

    public EntityAerbunny(World world)
    {
        super(world);
        this.aiEatGrass = new AIEntityEatBlock(this, AetherBlocks.AetherGrass, AetherBlocks.AetherDirt);
        this.getNavigator().setAvoidsWater(true);
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIAvoidEntity(this, EntityPlayer.class, 16.0F, 2.5999999046325684D, 2.799999952316284D));
        this.tasks.addTask(3, new EntityAIWander(this, 2.5D));
        this.tasks.addTask(4, new EntityAIWatchClosest(this, EntityPlayer.class, 10.0F));
        this.tasks.addTask(5, this.aiEatGrass);
        this.tasks.addTask(6, new AIEntityAerbunnyHop(this));
        this.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111128_a(2.5D);
        this.setSize(0.4F, 0.4F);
        this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(5.0D);
        this.setEntityHealth(5.0F);
        this.age = this.rand.nextInt(64);
        this.mate = 0;
        this.ignoreFrustumCheck = true;
        this.ridinghandler = new RidingHandlerAerbunny(this);
    }

    /**
     * Checks if the entity is in range to render by using the past in distance and comparing it to its average edge
     * length * 64 * renderDistanceWeight Args: distance
     */
    public boolean isInRangeToRenderDist(double par1)
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
        for (int multiply = 0; multiply < 5; ++multiply)
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

    public void setPuffiness(int i)
    {
        Integer var2 = Integer.valueOf(this.dataWatcher.getWatchableObjectInt(16));
        this.dataWatcher.updateObject(16, Integer.valueOf(i));
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
        }
        else if (this.jumpTicks > 0)
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
    protected void fall(float f) {}

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
    public void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setBoolean("Fear", this.fear);
        nbttagcompound.setShort("RepAge", (short)this.age);
        nbttagcompound.setShort("RepMate", (short)this.mate);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readEntityFromNBT(nbttagcompound);
        this.fear = nbttagcompound.getBoolean("Fear");
        this.age = nbttagcompound.getShort("RepAge");
        this.mate = nbttagcompound.getShort("RepMate");
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource damagesource, float i)
    {
        return damagesource.getEntity() instanceof EntityPlayer && this.getRidingHandler().getRider() instanceof EntityPlayer ? false : super.attackEntityFrom(damagesource, i);
    }

    public void cloudPoop()
    {
        double a = (double)(this.rand.nextFloat() - 0.5F);
        double c = (double)(this.rand.nextFloat() - 0.5F);
        double d = this.posX + a * 0.4000000059604645D;
        double e = this.boundingBox.minY;
        double f = this.posZ + a * 0.4000000059604645D;
        this.worldObj.spawnParticle("flame", d, e, f, 0.0D, -0.07500000298023224D, 0.0D);
    }

    /**
     * returns true if this entity is by a ladder, false otherwise
     */
    public boolean isOnLadder()
    {
        return this.moveForward != 0.0F;
    }

    public EntityAnimal spawnBabyAnimal(EntityAnimal entityanimal)
    {
        return new EntityAerbunny(this.worldObj);
    }

    public boolean isWheat(ItemStack itemstack)
    {
        return itemstack.itemID == Item.wheat.itemID;
    }

    /**
     * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a pig.
     */
    public boolean interact(EntityPlayer entityplayer)
    {
        ItemStack itemstack = entityplayer.inventory.getCurrentItem();

        if (itemstack != null && this.isWheat(itemstack))
        {
            return super.interact(entityplayer);
        }
        else
        {
            if (this.riddenByEntity == null && !this.getRidingHandler().isBeingRidden())
            {
                this.getRidingHandler().setRider(entityplayer);
                this.worldObj.playSoundAtEntity(this, "aether:aemob.aerbunny.lift", 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
            }
            else
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
        return "aether:aemob.aerbunny.hurt";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected String getDeathSound()
    {
        return "aether:aemob.aerbunny.die";
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

    public RidingHandler getRidingHandler()
    {
        return this.ridinghandler;
    }

    public EntityAgeable createChild(EntityAgeable entityageable)
    {
        return null;
    }
}
