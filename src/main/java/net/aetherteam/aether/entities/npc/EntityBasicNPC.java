package net.aetherteam.aether.entities.npc;

import java.util.Iterator;
import java.util.List;
import net.aetherteam.aether.items.AetherItems;
import net.minecraft.entity.Entity;
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

    public EntityBasicNPC(World var1)
    {
        super(var1);
        this.addRandomArmor();
        this.texture = this.dir + "/npc/angel.png";
        this.pleasure = this.rand.nextBoolean() ? this.rand.nextFloat() : -this.rand.nextFloat();
        this.arousal = this.rand.nextBoolean() ? this.rand.nextFloat() : -this.rand.nextFloat();
        this.dominance = this.rand.nextBoolean() ? this.rand.nextFloat() : -this.rand.nextFloat();
        this.getNavigator().setAvoidsWater(true);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIPanic(this, 0.45F));
        this.tasks.addTask(2, new EntityAIWander(this, 0.38F));
        this.tasks.addTask(3, new EntityAITempt(this, 0.25F, AetherItems.EnchantedBerry.itemID, false));
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
            int var1 = this.rand.nextInt(2);
            float var2 = this.worldObj.difficultySetting == 3 ? 0.1F : 0.25F;

            if (this.rand.nextFloat() < 0.095F)
            {
                ++var1;
            }

            if (this.rand.nextFloat() < 0.095F)
            {
                ++var1;
            }

            if (this.rand.nextFloat() < 0.095F)
            {
                ++var1;
            }

            for (int var3 = 3; var3 >= 0; --var3)
            {
                ItemStack var4 = this.getCurrentArmor(var3);

                if (var3 < 3 && this.rand.nextFloat() < var2)
                {
                    break;
                }

                if (var4 == null)
                {
                    Item var5 = getArmorItemForSlot(var3 + 1, var1);

                    if (var5 != null)
                    {
                        this.setCurrentItemOrArmor(var3 + 1, new ItemStack(var5));
                    }
                }
            }
        }
    }

    public static Item getArmorItemForSlot(int var0, int var1)
    {
        switch (var0)
        {
            case 1:
                if (var1 == 0)
                {
                    return AetherItems.ZaniteBoots;
                }
                else
                {
                    if (var1 == 1)
                    {
                        return AetherItems.ValkyrieBoots;
                    }

                    return AetherItems.GravititeBoots;
                }

            case 2:
                if (var1 == 0)
                {
                    return AetherItems.ZaniteLeggings;
                }
                else
                {
                    if (var1 == 1)
                    {
                        return AetherItems.ValkyrieLeggings;
                    }

                    return AetherItems.GravititeLeggings;
                }

            case 3:
                if (var1 == 0)
                {
                    return AetherItems.ZaniteChestplate;
                }
                else
                {
                    if (var1 == 1)
                    {
                        return AetherItems.ValkyrieChestplate;
                    }

                    return AetherItems.GravititeChestplate;
                }

            case 4:
                if (var1 == 0)
                {
                    return AetherItems.ZaniteHelmet;
                }
                else
                {
                    if (var1 == 1)
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

    public void setPleasure(float var1)
    {
        this.dataWatcher.updateObject(14, String.valueOf(var1));
    }

    public void setArousal(float var1)
    {
        this.dataWatcher.updateObject(15, String.valueOf(var1));
    }

    public void setDominance(float var1)
    {
        this.dataWatcher.updateObject(16, String.valueOf(var1));
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

    public float getSpeedWithFactors(float var1)
    {
        float var3 = 1.0F;
        var3 += 0.5F * this.arousal;
        var3 += this.isViolent() && this.entityToAttack != null ? 4.0F : 0.0F;
        var3 += this.isSubmissive() ? -1.0F : 0.0F;
        float var2 = Math.max(var1 * var3, 0.1F);
        return var2;
    }

    public void rest(double var1, double var3, double var5, int var7)
    {
        this.motionX = 0.0D;
        this.motionZ = 0.0D;
        this.setPositionAndRotation(var1, var3, var5, this.rotationYaw, this.rotationPitch);

        if (this.ticksExisted % 20 == 0)
        {
            ++this.restSeconds;
        }

        if (this.restSeconds >= var7)
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
        this.moveSpeed = this.getSpeedWithFactors(2.0F);

        if (this.motionX == 0.0D && this.motionZ == 0.0D)
        {
            this.energy += 0.001D;
        }
        else
        {
            this.energy -= 0.001D;
        }

        int var1 = 6 + this.rand.nextInt(4);

        if (this.energy <= 0.1D && this.restSeconds <= var1)
        {
            this.rest(this.restX, this.restY, this.restZ, var1);
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
            this.moveSpeed = (float)((double)this.moveSpeed * this.energy);
        }

        if (this.isCurious())
        {
            List var2 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(5.5D, 1.75D, 5.5D));
            boolean var3 = false;
            Iterator var4 = var2.iterator();

            while (var4.hasNext())
            {
                Entity var5 = (Entity)var4.next();

                if (var5 instanceof EntityPlayer)
                {
                    var3 = true;
                    this.followEntity(var5, 20.0F);
                    this.faceEntity(var5, 5.5F, (float)this.getVerticalFaceSpeed());
                }
            }

            if (!var3)
            {
                this.entityToAttack = null;
            }
            else
            {
                this.moveSpeed /= 1.5F;
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
    protected void attackEntity(Entity var1, float var2)
    {
        if (this.isAngry() && this.isCurious())
        {
            super.attackEntity(var1, var2);
        }
    }

    protected void updateEntityActionState()
    {
        super.updateEntityActionState();
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource var1, int var2)
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

        return super.attackEntityFrom(var1, var2);
    }

    protected void followEntity(Entity var1, float var2)
    {
        this.entityToAttack = var1;
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
    public void writeEntityToNBT(NBTTagCompound var1)
    {
        super.writeEntityToNBT(var1);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound var1)
    {
        super.readEntityFromNBT(var1);
    }

    public int getMaxHealth()
    {
        return 100;
    }
}
