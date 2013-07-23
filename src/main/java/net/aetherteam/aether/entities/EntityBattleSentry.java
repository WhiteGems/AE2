package net.aetherteam.aether.entities;

import java.util.Random;
import net.aetherteam.aether.blocks.AetherBlocks;
import net.minecraft.block.Block;
import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityBattleSentry extends EntityDungeonMob
{
    public String dir = "/net/aetherteam/aether/client/sprites";
    private World q;
    private int timeTilHide;
    public float field_100021_a;
    public float field_100020_b;
    private int jcount;
    public int size;
    public int counter;
    public int lostyou;

    public EntityBattleSentry(World world)
    {
        super(world);
        this.texture = (this.dir + "/mobs/sentryMelee/sentryMelee.png");
        this.size = 2;
        this.yOffset = 0.0F;
        this.moveSpeed = 1.0F;
        this.field_100021_a = 1.0F;
        this.field_100020_b = 1.0F;
        this.jcount = (this.rand.nextInt(20) + 10);
        func_100019_e(this.size);
        this.worldObj = world;
        this.timeTilHide = 0;
    }

    public EntityBattleSentry(World world, double x, double y, double z)
    {
        super(world);
        this.texture = (this.dir + "/mobs/sentryMelee/sentryMelee.png");
        this.size = 2;
        this.yOffset = 0.0F;
        this.moveSpeed = 1.0F;
        this.field_100021_a = 1.0F;
        this.field_100020_b = 1.0F;
        this.jcount = (this.rand.nextInt(20) + 10);
        func_100019_e(this.size);
        this.rotationYaw = (this.rand.nextInt(4) * ((float)Math.PI / 2F));
        setPosition(x, y, z);
        this.worldObj = world;
        this.timeTilHide = 0;
    }

    public void func_100019_e(int i)
    {
        setEntityHealth(10);
        this.width = 0.85F;
        this.height = 0.85F;
        setPosition(this.posX, this.posY, this.posZ);
    }

    public void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setInteger("Size", this.size - 1);
        nbttagcompound.setBoolean("seen", isInView());
        setHasBeenAttacked(nbttagcompound.getBoolean("HasBeenAttacked"));
    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readEntityFromNBT(nbttagcompound);
        this.size = (nbttagcompound.getInteger("Size") + 1);
        setInView(nbttagcompound.getBoolean("seen"));
        nbttagcompound.setBoolean("HasBeenAttacked", getHasBeenAttacked());
    }

    public boolean attackEntityFrom(DamageSource par1DamageSource, int par2)
    {
        if (par1DamageSource.getEntity() != null)
        {
            setHasBeenAttacked(true);
            this.timeTilHide = 50;
        }

        return super.attackEntityFrom(par1DamageSource, par2);
    }

    public void onUpdate()
    {
        boolean flag = this.onGround;
        super.onUpdate();

        if ((this.onGround) && (!flag))
        {
            this.worldObj.playSoundAtEntity(this, "mob.slime.small", getSoundVolume(), ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F) / 0.8F);
        }
        else if ((!this.onGround) && (flag))
        {
            this.motionX *= 3.0D;
            this.motionZ *= 3.0D;
        }

        if ((this.entityToAttack != null) && (this.entityToAttack.isDead))
        {
            this.entityToAttack = null;
        }

        if (this.timeTilHide != 0)
        {
            setHasBeenAttacked(true);
            this.timeTilHide -= 1;
        }
        else
        {
            setHasBeenAttacked(false);
        }
    }

    public void onLivingUpdate()
    {
        super.onLivingUpdate();

        if (((this.entityToAttack instanceof EntityPlayer)) &&
                ((EntityPlayer)this.entityToAttack != null))
        {
            setInView(!isInFieldOfVision((EntityPlayer)this.entityToAttack, this));
        }
    }

    private boolean isInFieldOfVision(EntityPlayer entityPlayer, Entity entity)
    {
        float f = entityPlayer.rotationPitch;
        float f1 = entityPlayer.rotationYaw;
        entityPlayer.faceEntity(entity, 360.0F, 360.0F);
        float f2 = entityPlayer.rotationPitch;
        float f3 = entityPlayer.rotationYaw;
        entityPlayer.rotationPitch = f;
        entityPlayer.rotationYaw = f1;
        f = f2;
        f1 = f3;
        float f4 = 70.0F;
        float f5 = 65.0F;
        float f6 = entityPlayer.rotationPitch - f4;
        float f7 = entityPlayer.rotationPitch + f4;
        float f8 = entityPlayer.rotationYaw - f5;
        float f9 = entityPlayer.rotationYaw + f5;
        boolean flag = GetFlag(f6, f7, f, 0.0F, 360.0F);
        boolean flag1 = GetFlag(f8, f9, f1, -180.0F, 180.0F);
        return (flag) && (flag1);
    }

    public boolean GetFlag(float f, float f1, float f2, float f3, float f4)
    {
        if (f < f3)
        {
            if (f2 >= f + f4)
            {
                return true;
            }

            if (f2 <= f1)
            {
                return true;
            }
        }

        if (f1 >= f4)
        {
            if (f2 <= f1 - f4)
            {
                return true;
            }

            if (f2 >= f)
            {
                return true;
            }
        }

        if ((f1 < f4) && (f >= f3))
        {
            return (f2 <= f1) && (f2 > f);
        }

        return false;
    }

    protected void updateEntityActionState()
    {
        super.updateEntityActionState();
        EntityPlayer entityplayer = this.worldObj.getClosestPlayerToEntity(this, 8.0D);

        if ((entityplayer != null) && (canEntityBeSeen(entityplayer)) && (!entityplayer.capabilities.isCreativeMode))
        {
            faceEntity(entityplayer, 10.0F, 10.0F);
            this.entityToAttack = entityplayer;
            this.texture = (this.dir + "/mobs/sentry/sentry_lit.png");
        }

        if (this.entityToAttack != null)
        {
            faceEntity(this.entityToAttack, 10.0F, 10.0F);
        }

        if (((this.onGround) && (this.jcount-- <= 0) && (!isInView())) || (getHasBeenAttacked()))
        {
            this.jcount = (this.rand.nextInt(20) + 10);
            this.isJumping = true;
            this.moveStrafing = (0.5F - this.rand.nextFloat());
            this.moveForward = 1.0F;
            this.worldObj.playSoundAtEntity(this, "mob.slime.small", getSoundVolume(), ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F) * 0.8F);
            this.jcount /= 2;
            this.moveForward = 1.0F;
        }
        else
        {
            this.isJumping = false;

            if (this.onGround)
            {
                this.moveStrafing = (this.moveForward = 0.0F);
            }
        }
    }

    public boolean canBeCollidedWith()
    {
        return true;
    }

    public void onCollideWithPlayer(EntityPlayer par1EntityPlayer)
    {
        super.onCollideWithPlayer(par1EntityPlayer);

        if (!getHasBeenAttacked())
        {
            setHasBeenAttacked(true);
            this.timeTilHide = 15;
        }
    }

    protected String getHurtSound()
    {
        return "mob.slime.small";
    }

    protected String getDeathSound()
    {
        return "mob.slime.small";
    }

    public boolean getCanSpawnHere()
    {
        return super.getCanSpawnHere();
    }

    protected float getSoundVolume()
    {
        return 0.6F;
    }

    public int func_82193_c(Entity par1Entity)
    {
        return 2;
    }

    protected int getDropItemId()
    {
        if (this.rand.nextInt(5) == 0)
        {
            return AetherBlocks.LightDungeonStone.blockID;
        }

        return AetherBlocks.DungeonStone.blockID;
    }

    public void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(16, Byte.valueOf((byte)0));
        this.dataWatcher.addObject(17, Byte.valueOf((byte)0));
    }

    public boolean isInView()
    {
        return this.dataWatcher.getWatchableObjectByte(16) == 0;
    }

    public void setInView(boolean awake)
    {
        if (awake)
        {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte)1));
        }
        else
        {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte)0));
        }
    }

    public boolean getHasBeenAttacked()
    {
        return this.dataWatcher.getWatchableObjectByte(17) == 1;
    }

    public void setHasBeenAttacked(boolean attack)
    {
        if (attack)
        {
            this.dataWatcher.updateObject(17, Byte.valueOf((byte)1));
        }
        else
        {
            this.dataWatcher.updateObject(17, Byte.valueOf((byte)0));
        }
    }

    public int getMaxHealth()
    {
        return 10;
    }
}

