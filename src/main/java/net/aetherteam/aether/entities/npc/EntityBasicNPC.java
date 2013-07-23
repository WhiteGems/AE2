package net.aetherteam.aether.entities.npc;

import java.io.PrintStream;
import java.util.List;
import java.util.Random;
import net.aetherteam.aether.items.AetherItems;
import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityBasicNPC extends EntityMob
{
    private double energy = 1.0D;
    public String dir = "/net/aetherteam/aether/client/sprites";

    private float pleasure = 0.0F;
    private float arousal = 0.0F;
    private float dominance = 0.0F;

    private int restSeconds = 0;
    double restX;
    double restY;
    double restZ;

    public EntityBasicNPC(World world)
    {
        super(world);
        addRandomArmor();
        this.texture = (this.dir + "/npc/angel.png");
        this.pleasure = (this.rand.nextBoolean() ? this.rand.nextFloat() : -this.rand.nextFloat());
        this.arousal = (this.rand.nextBoolean() ? this.rand.nextFloat() : -this.rand.nextFloat());
        this.dominance = (this.rand.nextBoolean() ? this.rand.nextFloat() : -this.rand.nextFloat());
        getNavigator().setAvoidsWater(true);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIPanic(this, 0.45F));
        this.tasks.addTask(2, new EntityAIWander(this, 0.38F));
        this.tasks.addTask(3, new EntityAITempt(this, 0.25F, AetherItems.EnchantedBerry.itemID, false));
        this.tasks.addTask(4, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.tasks.addTask(5, new EntityAILookIdle(this));

        if (this.worldObj.isRemote)
        {
            return;
        }

        System.out.println("Pleasure: " + this.pleasure);
        System.out.println("Arousal: " + this.arousal);
        System.out.println("Dominance: " + this.dominance);
    }

    public boolean isAIEnabled()
    {
        return true;
    }

    protected void addRandomArmor()
    {
        if (this.rand.nextBoolean())
        {
            int i = this.rand.nextInt(2);
            float f = this.worldObj.difficultySetting == 3 ? 0.1F : 0.25F;

            if (this.rand.nextFloat() < 0.095F)
            {
                i++;
            }

            if (this.rand.nextFloat() < 0.095F)
            {
                i++;
            }

            if (this.rand.nextFloat() < 0.095F)
            {
                i++;
            }

            for (int j = 3; j >= 0; j--)
            {
                ItemStack itemstack = getCurrentArmor(j);

                if ((j < 3) && (this.rand.nextFloat() < f))
                {
                    break;
                }

                if (itemstack == null)
                {
                    Item item = getArmorItemForSlot(j + 1, i);

                    if (item != null)
                    {
                        setCurrentItemOrArmor(j + 1, new ItemStack(item));
                    }
                }
            }
        }
    }

    public static Item getArmorItemForSlot(int par0, int par1)
    {
        switch (par0)
        {
            case 4:
                if (par1 == 0)
                {
                    return AetherItems.ZaniteHelmet;
                }

                if (par1 == 1)
                {
                    return AetherItems.ValkyrieHelmet;
                }

                return AetherItems.GravititeHelmet;

            case 3:
                if (par1 == 0)
                {
                    return AetherItems.ZaniteChestplate;
                }

                if (par1 == 1)
                {
                    return AetherItems.ValkyrieChestplate;
                }

                return AetherItems.GravititeChestplate;

            case 2:
                if (par1 == 0)
                {
                    return AetherItems.ZaniteLeggings;
                }

                if (par1 == 1)
                {
                    return AetherItems.ValkyrieLeggings;
                }

                return AetherItems.GravititeLeggings;

            case 1:
                if (par1 == 0)
                {
                    return AetherItems.ZaniteBoots;
                }

                if (par1 == 1)
                {
                    return AetherItems.ValkyrieBoots;
                }

                return AetherItems.GravititeBoots;
        }

        return null;
    }

    public void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(14, String.valueOf(0.0F));
        this.dataWatcher.addObject(15, String.valueOf(0.0F));
        this.dataWatcher.addObject(16, String.valueOf(0.0F));
    }

    public float getPleasure()
    {
        return Float.valueOf(this.dataWatcher.getWatchableObjectString(14)).floatValue();
    }

    public float getArousal()
    {
        return Float.valueOf(this.dataWatcher.getWatchableObjectString(15)).floatValue();
    }

    public float getDominance()
    {
        return Float.valueOf(this.dataWatcher.getWatchableObjectString(16)).floatValue();
    }

    public void setPleasure(float pleasure)
    {
        this.dataWatcher.updateObject(14, String.valueOf(pleasure));
    }

    public void setArousal(float arousal)
    {
        this.dataWatcher.updateObject(15, String.valueOf(arousal));
    }

    public void setDominance(float dominance)
    {
        this.dataWatcher.updateObject(16, String.valueOf(dominance));
    }

    public boolean isPleasured()
    {
        return this.pleasure > 0.0F;
    }

    public boolean isDispleasured()
    {
        return this.pleasure < 0.0F;
    }

    public boolean isAroused()
    {
        return this.arousal > 0.0F;
    }

    public boolean isNonaroused()
    {
        return this.arousal < 0.0F;
    }

    public boolean isDominant()
    {
        return this.dominance > 0.0F;
    }

    public boolean isSubmissive()
    {
        return this.dominance < 0.0F;
    }

    public boolean isAngry()
    {
        return (getPleasure() <= -0.51F) && (getArousal() >= 0.59D) && (getDominance() >= 0.25F);
    }

    public boolean isScared()
    {
        return (getPleasure() <= -0.64F) && (getArousal() >= 0.6D) && (getDominance() <= -0.43F);
    }

    public boolean isBored()
    {
        return (getPleasure() <= -0.65F) && (getArousal() <= -0.62D) && (getDominance() <= -0.33F);
    }

    public boolean isCurious()
    {
        return (getPleasure() >= 0.22F) && (getArousal() >= 0.62F) && (getDominance() <= -0.01F);
    }

    public boolean isHappy()
    {
        return (getPleasure() >= 0.5F) && (getArousal() >= 0.42F) && (getDominance() >= 0.23F);
    }

    public boolean isHungry()
    {
        return (getPleasure() <= -0.44F) && (getArousal() >= 0.14F) && (getDominance() <= -0.21F);
    }

    public boolean isInhibited()
    {
        return (getPleasure() <= -0.54F) && (getArousal() <= -0.04F) && (getDominance() <= -0.41F);
    }

    public boolean isPuzzled()
    {
        return (getPleasure() <= -0.41F) && (getArousal() >= 0.48F) && (getDominance() <= -0.33F);
    }

    public boolean isViolent()
    {
        return (getPleasure() <= -0.5D) && (getArousal() >= 0.62D) && (getDominance() >= 0.38D);
    }

    public float getSpeedWithFactors(float moveSpeed)
    {
        float factor = 1.0F;
        factor += 0.5F * this.arousal;
        factor += ((isViolent()) && (this.entityToAttack != null) ? 4.0F : 0.0F);
        factor += (isSubmissive() ? -1.0F : 0.0F);
        float factoredMoveSpeed = Math.max(moveSpeed * factor, 0.1F);
        return factoredMoveSpeed;
    }

    public void rest(double x, double y, double z, int howLong)
    {
        this.motionX = 0.0D;
        this.motionZ = 0.0D;
        setPositionAndRotation(x, y, z, this.rotationYaw, this.rotationPitch);

        if (this.ticksExisted % 20 == 0)
        {
            this.restSeconds += 1;
        }

        if (this.restSeconds >= howLong)
        {
            this.energy = 0.5D;
        }
    }

    public void onUpdate()
    {
        super.onUpdate();

        if (((this.pleasure != getPleasure()) || (this.arousal != getArousal()) || (this.dominance != getDominance())) && (!this.worldObj.isRemote))
        {
            setPleasure(this.pleasure);
            setArousal(this.arousal);
            setDominance(this.dominance);
        }

        if (!this.worldObj.isRemote)
        {
            setPleasure(MathHelper.clamp_float(getPleasure(), -1.0F, 1.0F));
            setArousal(MathHelper.clamp_float(getArousal(), -1.0F, 1.0F));
            setDominance(MathHelper.clamp_float(getDominance(), -1.0F, 1.0F));
        }

        this.energy = (this.energy < 1.0D ? Math.max(this.energy, 0.1D) : 1.0D);
        this.moveSpeed = getSpeedWithFactors(2.0F);

        if ((this.motionX != 0.0D) || (this.motionZ != 0.0D))
        {
            this.energy -= 0.001D;
        }
        else
        {
            this.energy += 0.001D;
        }

        int restFor = 6 + this.rand.nextInt(4);

        if ((this.energy <= 0.1D) && (this.restSeconds <= restFor))
        {
            rest(this.restX, this.restY, this.restZ, restFor);
        }
        else
        {
            this.restX = this.posX;
            this.restY = this.posY;
            this.restZ = this.posZ;
            this.restSeconds = 0;
        }

        if (!this.worldObj.isRemote)
        {
            this.moveSpeed = ((float)(this.moveSpeed * this.energy));
        }

        if (isCurious())
        {
            List entitiesAround = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(5.5D, 1.75D, 5.5D));
            boolean foundPlayer = false;

            for (Entity entity : entitiesAround)
            {
                if ((entity instanceof EntityPlayer))
                {
                    foundPlayer = true;
                    followEntity(entity, 20.0F);
                    faceEntity(entity, 5.5F, getVerticalFaceSpeed());
                }
            }

            if (!foundPlayer)
            {
                this.entityToAttack = null;
            }
            else
            {
                this.moveSpeed /= 1.5F;
            }
        }

        checkForAggression();
    }

    public void checkForAggression()
    {
        if ((isAngry()) || (isCurious()))
        {
            return;
        }

        this.entityToAttack = null;
    }

    protected void attackEntity(Entity entity, float f)
    {
        if ((!isAngry()) || (!isCurious()))
        {
            return;
        }

        super.attackEntity(entity, f);
    }

    protected void updateEntityActionState()
    {
        super.updateEntityActionState();
    }

    public boolean attackEntityFrom(DamageSource source, int damage)
    {
        this.pleasure = ((float)(this.pleasure - 0.7D));
        this.arousal = ((float)(this.arousal + 0.3D));
        System.out.println("Pleasure: " + this.pleasure);
        System.out.println("Arousal: " + this.arousal);
        System.out.println("Dominance: " + this.dominance);

        if (isScared())
        {
            this.fleeingTick = 60;
        }

        return super.attackEntityFrom(source, damage);
    }

    protected void followEntity(Entity entity, float distance)
    {
        this.entityToAttack = entity;
    }

    public boolean canDespawn()
    {
        return false;
    }

    public void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeEntityToNBT(nbttagcompound);
    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readEntityFromNBT(nbttagcompound);
    }

    public int getMaxHealth()
    {
        return 100;
    }
}

