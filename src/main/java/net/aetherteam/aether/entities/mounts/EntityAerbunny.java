package net.aetherteam.aether.entities.mounts;

import java.util.Random;
import net.aetherteam.aether.Aether;
import net.aetherteam.aether.CommonProxy;
import net.aetherteam.aether.blocks.AetherBlocks;
import net.aetherteam.aether.entities.EntityAetherAnimal;
import net.aetherteam.aether.entities.ai.AIEntityAerbunnyHop;
import net.aetherteam.aether.entities.ai.AIEntityEatBlock;
import net.aetherteam.aether.entities.mounts_old.Ridable;
import net.aetherteam.aether.entities.mounts_old.RidingHandler;
import net.aetherteam.aether.entities.mounts_old.RidingHandlerAerbunny;
import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityAerbunny extends EntityAetherAnimal
    implements Ridable
{
    public AIEntityEatBlock aiEatGrass = new AIEntityEatBlock(this, AetherBlocks.AetherGrass, AetherBlocks.AetherDirt);
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
        this.moveSpeed = 2.5F;
        getNavigator().setAvoidsWater(true);
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIAvoidEntity(this, EntityPlayer.class, 16.0F, 2.6F, 2.8F));
        this.tasks.addTask(3, new EntityAIWander(this, 2.5F));
        this.tasks.addTask(4, new EntityAIWatchClosest(this, EntityPlayer.class, 10.0F));
        this.tasks.addTask(5, this.aiEatGrass);
        this.tasks.addTask(6, new AIEntityAerbunnyHop(this));
        this.texture = (this.dir + "/mobs/aerbunny/aerbunny.png");
        setSize(0.4F, 0.4F);
        this.health = 6;
        this.age = this.rand.nextInt(64);
        this.mate = 0;
        this.ignoreFrustumCheck = true;
        this.ridinghandler = new RidingHandlerAerbunny(this);
    }

    public boolean isInRangeToRenderDist(double par1)
    {
        return true;
    }

    protected boolean isAIEnabled()
    {
        return true;
    }

    protected void jump()
    {
        for (int multiply = 0; multiply < 5; multiply++)
        {
            Aether.proxy.spawnCloudSmoke(this.worldObj, this.posX, this.posY + 0.5D, this.posZ, new Random(), Double.valueOf(0.5D));
        }

        setPuffiness(11);
        this.jumps -= 1;
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

    public void onUpdate()
    {
        setPuffiness(getPuffiness() - 1);

        if (getPuffiness() < 0)
        {
            setPuffiness(0);
        }

        super.onUpdate();
    }

    public void onLivingUpdate()
    {
        if (this.onGround)
        {
            this.jumps = 1;
            this.jumpTicks = 10;
        }
        else if (this.jumpTicks > 0)
        {
            this.jumpTicks -= 1;
        }

        if (this.isJumping)
        {
            if ((!isInWater()) && (!handleLavaMovement()))
            {
                if ((!this.onGround) && (this.jumpTicks == 0) && (this.jumps > 0))
                {
                    jump();
                    this.jumpTicks = 10;
                }
            }
        }

        if (this.motionY < -0.1D)
        {
            this.motionY = -0.1D;
        }

        super.onLivingUpdate();
    }

    protected void fall(float f)
    {
    }

    public boolean isEntityInsideOpaqueBlock()
    {
        return false;
    }

    public void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setBoolean("Fear", this.fear);
        nbttagcompound.setShort("RepAge", (short)this.age);
        nbttagcompound.setShort("RepMate", (short)this.mate);
    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readEntityFromNBT(nbttagcompound);
        this.fear = nbttagcompound.getBoolean("Fear");
        this.age = nbttagcompound.getShort("RepAge");
        this.mate = nbttagcompound.getShort("RepMate");
    }

    public boolean attackEntityFrom(DamageSource damagesource, int i)
    {
        if (((damagesource.getEntity() instanceof EntityPlayer)) && ((getRidingHandler().getRider() instanceof EntityPlayer)))
        {
            return false;
        }

        return super.attackEntityFrom(damagesource, i);
    }

    public void cloudPoop()
    {
        double a = this.rand.nextFloat() - 0.5F;
        double c = this.rand.nextFloat() - 0.5F;
        double d = this.posX + a * 0.4000000059604645D;
        double e = this.boundingBox.minY;
        double f = this.posZ + a * 0.4000000059604645D;
        this.worldObj.spawnParticle("flame", d, e, f, 0.0D, -0.07500000298023224D, 0.0D);
    }

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

    public boolean interact(EntityPlayer entityplayer)
    {
        ItemStack itemstack = entityplayer.inventory.getCurrentItem();

        if ((itemstack != null) && (isWheat(itemstack)))
        {
            return super.interact(entityplayer);
        }

        if ((this.riddenByEntity == null) && (!getRidingHandler().isBeingRidden()))
        {
            getRidingHandler().setRider(entityplayer);
            this.worldObj.playSoundAtEntity(this, "aemob.aerbunny.lift", 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
        }
        else
        {
            getRidingHandler().onUnMount();
        }

        return true;
    }

    protected String getLivingSound()
    {
        return null;
    }

    protected void dropFewItems(boolean var1, int var2)
    {
        dropItem(Item.silk.itemID, 1);
    }

    public void proceed()
    {
        this.mate = 0;
        this.age = this.rand.nextInt(64);
    }

    protected boolean canTriggerWalking()
    {
        return this.onGround;
    }

    protected String getHurtSound()
    {
        return "aemob.aerbunny.hurt";
    }

    protected String getDeathSound()
    {
        return "aemob.aerbunny.die";
    }

    public boolean getCanSpawnHere()
    {
        return super.getCanSpawnHere();
    }

    public boolean canBeCollidedWith()
    {
        if ((getRidingHandler().getRider() instanceof EntityPlayer))
        {
            return false;
        }

        return super.canBeCollidedWith();
    }

    public int getMaxHealth()
    {
        return 5;
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

