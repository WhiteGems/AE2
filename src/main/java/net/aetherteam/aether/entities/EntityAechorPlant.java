package net.aetherteam.aether.entities;

import java.util.List;
import net.aetherteam.aether.blocks.AetherBlocks;
import net.aetherteam.aether.items.AetherItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityAechorPlant extends EntityAetherAnimal
{
    public EntityLiving target;
    public int attTime;
    public int smokeTime;
    public boolean seeprey;
    public boolean grounded;
    public boolean noDespawn;
    public float sinage;

    public EntityAechorPlant(World var1)
    {
        super(var1);
        this.texture = this.dir + "/mobs/aechorplant/aechorplant.png";
        this.setSize(this.rand.nextInt(4) + 1);
        this.health = 10 + this.getSize() * 2;
        this.sinage = this.rand.nextFloat() * 6.0F;
        this.smokeTime = this.attTime = 0;
        this.seeprey = false;
        this.setSize(0.75F + (float)this.getSize() * 0.125F, 0.5F + (float)this.getSize() * 0.075F);
        this.setPosition(this.posX, this.posY, this.posZ);
        this.setPoisonAmount(2);
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    public boolean getCanSpawnHere()
    {
        int var1 = MathHelper.floor_double(this.posX);
        int var2 = MathHelper.floor_double(this.boundingBox.minY);
        int var3 = MathHelper.floor_double(this.posZ);
        return this.worldObj.getBlockId(var1, var2 - 1, var3) == AetherBlocks.AetherGrass.blockID && this.worldObj.getBlockLightValue(var1, var2, var3) > 8 && super.getCanSpawnHere();
    }

    public boolean isPotionApplicable(PotionEffect var1)
    {
        return var1.getPotionID() == Potion.poison.id ? false : super.isPotionApplicable(var1);
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate()
    {
        if (this.health > 0 && this.grounded)
        {
            ++this.entityAge;
            this.despawnEntity();
        }
        else
        {
            super.onLivingUpdate();

            if (this.health <= 0)
            {
                return;
            }
        }

        if (this.onGround)
        {
            this.grounded = true;
        }

        if (this.hurtTime > 0)
        {
            this.sinage += 0.9F;
        }
        else if (this.seeprey)
        {
            this.sinage += 0.3F;
        }
        else
        {
            this.sinage += 0.1F;
        }

        if (this.sinage > ((float)Math.PI * 2F))
        {
            this.sinage -= ((float)Math.PI * 2F);
        }

        int var2;

        if (this.target == null || this.velocityChanged)
        {
            List var1 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(10.0D, 10.0D, 10.0D));

            for (var2 = 0; var2 < var1.size(); ++var2)
            {
                Entity var3 = (Entity)var1.get(var2);

                if (var3 instanceof EntityLiving && !(var3 instanceof EntityAechorPlant) && !(var3 instanceof EntityCreeper))
                {
                    if (var3 instanceof EntityPlayer)
                    {
                        EntityPlayer var4 = (EntityPlayer)var3;
                        boolean var5 = false;

                        if (var5)
                        {
                            continue;
                        }
                    }

                    this.target = (EntityLiving)var3;
                    break;
                }
            }
        }

        if (this.target != null)
        {
            if (!this.target.isDead && (double)this.target.getDistanceToEntity(this) <= 12.0D)
            {
                if (this.target instanceof EntityPlayer)
                {
                    EntityPlayer var6 = (EntityPlayer)this.target;
                    boolean var8 = false;

                    if (var8)
                    {
                        this.target = null;
                        this.attTime = 0;
                    }
                }
            }
            else
            {
                this.target = null;
                this.attTime = 0;
            }

            if (this.target != null && this.attTime >= 20 && this.canEntityBeSeen(this.target) && (double)this.target.getDistanceToEntity(this) < 12.0D)
            {
                this.shootTarget();
                this.attTime = -10;
            }

            if (this.attTime < 20)
            {
                ++this.attTime;
            }
        }

        ++this.smokeTime;

        if (this.smokeTime >= (this.seeprey ? 3 : 8))
        {
            this.smokeTime = 0;
            int var7 = MathHelper.floor_double(this.posX);
            var2 = MathHelper.floor_double(this.boundingBox.minY);
            int var9 = MathHelper.floor_double(this.posZ);

            if (this.worldObj.getBlockId(var7, var2 - 1, var9) != AetherBlocks.AetherGrass.blockID && this.grounded)
            {
                this.isDead = true;
            }
        }

        this.seeprey = this.target != null || this.worldObj.getClosestPlayerToEntity(this, 10.0D) != null;
    }

    public void shootTarget()
    {
        if (this.worldObj.difficultySetting != 0 && this.target != null && this.canEntityBeSeen(this.target) && MathHelper.floor_double(this.target.posY) == MathHelper.floor_double(this.posY))
        {
            double var1 = this.target.posX - this.posX;
            double var3 = this.target.posZ - this.posZ;
            double var5 = 1.5D / Math.sqrt(var1 * var1 + var3 * var3 + 0.1D);
            double var7 = 0.1D + Math.sqrt(var1 * var1 + var3 * var3 + 0.1D) * 0.5D + (this.posY - this.target.posY) * 0.25D;
            var1 *= var7;
            var3 *= var7;
            EntityPoisonNeedle var9 = new EntityPoisonNeedle(this.worldObj, this);
            var9.posY = this.posY + 0.6D;
            this.worldObj.playSoundAtEntity(this, "aemisc.shootDart", 2.0F, 1.0F / (this.rand.nextFloat() * 0.4F + 0.8F));

            if (!this.worldObj.isRemote)
            {
                var9.setArrowHeading(var1, var7, var3, 0.285F + (float)var7 * 0.55F, 1.0F);
                this.worldObj.spawnEntityInWorld(var9);
            }
        }
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected String getHurtSound()
    {
        return "damage.hurtflesh";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected String getDeathSound()
    {
        return "damage.fallbig";
    }

    /**
     * knocks back this entity
     */
    public void knockBack(Entity var1, int var2, double var3, double var5)
    {
        for (int var7 = 0; var7 < 8; ++var7)
        {
            double var8 = this.posX + (double)(this.rand.nextFloat() - this.rand.nextFloat()) * 0.5D;
            double var10 = this.posY + 0.25D + (double)(this.rand.nextFloat() - this.rand.nextFloat()) * 0.5D;
            double var12 = this.posZ + (double)(this.rand.nextFloat() - this.rand.nextFloat()) * 0.5D;
            double var14 = (double)(this.rand.nextFloat() - this.rand.nextFloat()) * 0.5D;
            double var16 = (double)(this.rand.nextFloat() - this.rand.nextFloat()) * 0.5D;
            this.worldObj.spawnParticle("portal", var8, var10, var12, var14, 0.25D, var16);
        }

        if (this.health <= 0)
        {
            super.knockBack(var1, var2, var3, var5);
        }
    }

    /**
     * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a pig.
     */
    public boolean interact(EntityPlayer var1)
    {
        boolean var2 = false;
        ItemStack var3 = var1.inventory.getCurrentItem();

        if (var3 != null && var3.itemID == AetherItems.SkyrootBucket.itemID && this.getPoisonLeft() > 0)
        {
            this.decrementPoison();
            var1.inventory.setInventorySlotContents(var1.inventory.currentItem, (ItemStack)null);
            var1.inventory.setInventorySlotContents(var1.inventory.currentItem, new ItemStack(AetherItems.SkyrootBucket, 1, 2));
            return true;
        }
        else
        {
            if (var2)
            {
                this.noDespawn = true;
                String var4 = "heart";

                for (int var5 = 0; var5 < 7; ++var5)
                {
                    double var6 = this.rand.nextGaussian() * 0.02D;
                    double var8 = this.rand.nextGaussian() * 0.02D;
                    double var10 = this.rand.nextGaussian() * 0.02D;
                    this.worldObj.spawnParticle(var4, this.posX + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, this.posY + 0.5D + (double)(this.rand.nextFloat() * this.height), this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, var6, var8, var10);
                }
            }

            return false;
        }
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound var1)
    {
        super.writeEntityToNBT(var1);
        var1.setBoolean("Grounded", this.grounded);
        var1.setBoolean("NoDespawn", this.noDespawn);
        var1.setShort("AttTime", (short)this.attTime);
        var1.setShort("Size", (short)this.getSize());
        var1.setInteger("Poison", this.getPoisonLeft());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound var1)
    {
        super.readEntityFromNBT(var1);
        this.grounded = var1.getBoolean("Grounded");
        this.noDespawn = var1.getBoolean("NoDespawn");
        this.attTime = var1.getShort("AttTime");
        this.setSize(var1.getShort("Size"));
        this.setPoisonAmount(var1.getInteger("Poison"));
        this.setSize(0.75F + (float)this.getSize() * 0.125F, 0.5F + (float)this.getSize() * 0.075F);
        this.setPosition(this.posX, this.posY, this.posZ);
    }

    public void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(16, Integer.valueOf(0));
        this.dataWatcher.addObject(17, Short.valueOf((short)0));
    }

    public int getPoisonLeft()
    {
        return this.dataWatcher.getWatchableObjectInt(16);
    }

    public void setPoisonAmount(int var1)
    {
        this.dataWatcher.updateObject(16, Integer.valueOf(var1));
    }

    public void decrementPoison()
    {
        this.dataWatcher.updateObject(16, Integer.valueOf(this.dataWatcher.getWatchableObjectInt(16) - 1));
    }

    public int getSize()
    {
        return this.dataWatcher.getWatchableObjectShort(17);
    }

    public void setSize(int var1)
    {
        this.dataWatcher.updateObject(17, Short.valueOf((short)var1));
    }

    /**
     * Drop 0-2 items of this living's type. @param par1 - Whether this entity has recently been hit by a player. @param
     * par2 - Level of Looting used to kill this mob.
     */
    protected void dropFewItems(boolean var1, int var2)
    {
        this.dropItem(AetherItems.AechorPetal.itemID, 2);
    }

    public int getMaxHealth()
    {
        return 20;
    }

    public EntityAgeable createChild(EntityAgeable var1)
    {
        return null;
    }
}
