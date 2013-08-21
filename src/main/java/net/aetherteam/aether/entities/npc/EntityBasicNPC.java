package net.aetherteam.aether.entities.npc;

import java.util.Iterator;
import java.util.List;
import net.aetherteam.aether.items.AetherItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
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
        this.addRandomArmor();
        this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(100.0D);
        this.setEntityHealth(100.0F);
        this.pleasure = this.rand.nextBoolean() ? this.rand.nextFloat() : -this.rand.nextFloat();
        this.arousal = this.rand.nextBoolean() ? this.rand.nextFloat() : -this.rand.nextFloat();
        this.dominance = this.rand.nextBoolean() ? this.rand.nextFloat() : -this.rand.nextFloat();
        this.getNavigator().setAvoidsWater(true);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIPanic(this, 0.44999998807907104D));
        this.tasks.addTask(2, new EntityAIWander(this, 0.3799999952316284D));
        this.tasks.addTask(3, new EntityAITempt(this, 0.25D, AetherItems.EnchantedBerry.itemID, false));
        this.tasks.addTask(4, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.tasks.addTask(5, new EntityAILookIdle(this));

        if (!this.worldObj.isRemote)
        {
            System.out.println("Pleasure: " + this.pleasure);
            System.out.println("Arousal: " + this.arousal);
            System.out.println("Dominance: " + this.dominance);
        }
    }

    /**
     * Returns true if the newer Entity AI code should be run
     */
    public boolean isAIEnabled()
    {
        return true;
    }

    /**
     * Makes entity wear random armor based on difficulty
     */
    protected void addRandomArmor()
    {
        if (this.rand.nextBoolean())
        {
            int i = this.rand.nextInt(2);
            float f = this.worldObj.difficultySetting == 3 ? 0.1F : 0.25F;

            if (this.rand.nextFloat() < 0.095F)
            {
                ++i;
            }

            if (this.rand.nextFloat() < 0.095F)
            {
                ++i;
            }

            if (this.rand.nextFloat() < 0.095F)
            {
                ++i;
            }

            for (int j = 3; j >= 0; --j)
            {
                ItemStack itemstack = this.getCurrentItemOrArmor(j);

                if (j < 3 && this.rand.nextFloat() < f)
                {
                    break;
                }

                if (itemstack == null)
                {
                    Item item = getArmorItemForSlot(j + 1, i);

                    if (item != null)
                    {
                        this.setCurrentItemOrArmor(j + 1, new ItemStack(item));
                    }
                }
            }
        }
    }

    /**
     * Params: Armor slot, Item tier
     */
    public static Item getArmorItemForSlot(int par0, int par1)
    {
        switch (par0)
        {
            case 1:
                if (par1 == 0)
                {
                    return AetherItems.ZaniteBoots;
                }
                else
                {
                    if (par1 == 1)
                    {
                        return AetherItems.ValkyrieBoots;
                    }

                    return AetherItems.GravititeBoots;
                }

            case 2:
                if (par1 == 0)
                {
                    return AetherItems.ZaniteLeggings;
                }
                else
                {
                    if (par1 == 1)
                    {
                        return AetherItems.ValkyrieLeggings;
                    }

                    return AetherItems.GravititeLeggings;
                }

            case 3:
                if (par1 == 0)
                {
                    return AetherItems.ZaniteChestplate;
                }
                else
                {
                    if (par1 == 1)
                    {
                        return AetherItems.ValkyrieChestplate;
                    }

                    return AetherItems.GravititeChestplate;
                }

            case 4:
                if (par1 == 0)
                {
                    return AetherItems.ZaniteHelmet;
                }
                else
                {
                    if (par1 == 1)
                    {
                        return AetherItems.ValkyrieHelmet;
                    }

                    return AetherItems.GravititeHelmet;
                }

            default:
                return null;
        }
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
        return this.getPleasure() <= -0.51F && (double)this.getArousal() >= 0.59D && this.getDominance() >= 0.25F;
    }

    public boolean isScared()
    {
        return this.getPleasure() <= -0.64F && (double)this.getArousal() >= 0.6D && this.getDominance() <= -0.43F;
    }

    public boolean isBored()
    {
        return this.getPleasure() <= -0.65F && (double)this.getArousal() <= -0.62D && this.getDominance() <= -0.33F;
    }

    public boolean isCurious()
    {
        return this.getPleasure() >= 0.22F && this.getArousal() >= 0.62F && this.getDominance() <= -0.01F;
    }

    public boolean isHappy()
    {
        return this.getPleasure() >= 0.5F && this.getArousal() >= 0.42F && this.getDominance() >= 0.23F;
    }

    public boolean isHungry()
    {
        return this.getPleasure() <= -0.44F && this.getArousal() >= 0.14F && this.getDominance() <= -0.21F;
    }

    public boolean isInhibited()
    {
        return this.getPleasure() <= -0.54F && this.getArousal() <= -0.04F && this.getDominance() <= -0.41F;
    }

    public boolean isPuzzled()
    {
        return this.getPleasure() <= -0.41F && this.getArousal() >= 0.48F && this.getDominance() <= -0.33F;
    }

    public boolean isViolent()
    {
        return (double)this.getPleasure() <= -0.5D && (double)this.getArousal() >= 0.62D && (double)this.getDominance() >= 0.38D;
    }

    public float getSpeedWithFactors(float moveSpeed)
    {
        float factor = 1.0F;
        factor += 0.5F * this.arousal;
        factor += this.isViolent() && this.entityToAttack != null ? 4.0F : 0.0F;
        factor += this.isSubmissive() ? -1.0F : 0.0F;
        float factoredMoveSpeed = Math.max(moveSpeed * factor, 0.1F);
        return factoredMoveSpeed;
    }

    public void rest(double x, double y, double z, int howLong)
    {
        this.motionX = 0.0D;
        this.motionZ = 0.0D;
        this.setPositionAndRotation(x, y, z, this.rotationYaw, this.rotationPitch);

        if (this.ticksExisted % 20 == 0)
        {
            ++this.restSeconds;
        }

        if (this.restSeconds >= howLong)
        {
            this.energy = 0.5D;
        }
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();

        if ((this.pleasure != this.getPleasure() || this.arousal != this.getArousal() || this.dominance != this.getDominance()) && !this.worldObj.isRemote)
        {
            this.setPleasure(this.pleasure);
            this.setArousal(this.arousal);
            this.setDominance(this.dominance);
        }

        if (!this.worldObj.isRemote)
        {
            this.setPleasure(MathHelper.clamp_float(this.getPleasure(), -1.0F, 1.0F));
            this.setArousal(MathHelper.clamp_float(this.getArousal(), -1.0F, 1.0F));
            this.setDominance(MathHelper.clamp_float(this.getDominance(), -1.0F, 1.0F));
        }

        this.energy = this.energy < 1.0D ? Math.max(this.energy, 0.1D) : 1.0D;
        this.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111128_a((double)this.getSpeedWithFactors(2.0F));

        if (this.motionX == 0.0D && this.motionZ == 0.0D)
        {
            this.energy += 0.001D;
        }
        else
        {
            this.energy -= 0.001D;
        }

        int restFor = 6 + this.rand.nextInt(4);

        if (this.energy <= 0.1D && this.restSeconds <= restFor)
        {
            this.rest(this.restX, this.restY, this.restZ, restFor);
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
            this.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111128_a(this.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111125_b() * this.energy);
        }

        if (this.isCurious())
        {
            List entitiesAround = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(5.5D, 1.75D, 5.5D));
            boolean foundPlayer = false;
            Iterator i$ = entitiesAround.iterator();

            while (i$.hasNext())
            {
                Entity entity = (Entity)i$.next();

                if (entity instanceof EntityPlayer)
                {
                    foundPlayer = true;
                    this.followEntity(entity, 20.0F);
                    this.faceEntity(entity, 5.5F, (float)this.getVerticalFaceSpeed());
                }
            }

            if (!foundPlayer)
            {
                this.entityToAttack = null;
            }
            else
            {
                this.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111128_a(this.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111125_b() / 1.5D);
            }
        }

        this.checkForAggression();
    }

    public void checkForAggression()
    {
        if (!this.isAngry() && !this.isCurious())
        {
            this.entityToAttack = null;
        }
    }

    /**
     * Basic mob attack. Default to touch of death in EntityCreature. Overridden by each mob to define their attack.
     */
    protected void attackEntity(Entity entity, float f)
    {
        if (this.isAngry() && this.isCurious())
        {
            super.attackEntity(entity, f);
        }
    }

    protected void updateEntityActionState()
    {
        super.updateEntityActionState();
    }

    public boolean attackEntityFrom(DamageSource source, int damage)
    {
        this.pleasure = (float)((double)this.pleasure - 0.7D);
        this.arousal = (float)((double)this.arousal + 0.3D);
        System.out.println("Pleasure: " + this.pleasure);
        System.out.println("Arousal: " + this.arousal);
        System.out.println("Dominance: " + this.dominance);

        if (this.isScared())
        {
            this.fleeingTick = 60;
        }

        return super.attackEntityFrom(source, (float)damage);
    }

    protected void followEntity(Entity entity, float distance)
    {
        this.entityToAttack = entity;
    }

    /**
     * Determines if an entity can be despawned, used on idle far away entities
     */
    public boolean canDespawn()
    {
        return false;
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeEntityToNBT(nbttagcompound);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readEntityFromNBT(nbttagcompound);
    }
}
