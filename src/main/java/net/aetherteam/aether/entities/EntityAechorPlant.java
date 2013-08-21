package net.aetherteam.aether.entities;

import java.util.List;
import net.aetherteam.aether.blocks.AetherBlocks;
import net.aetherteam.aether.items.AetherItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
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
    public EntityLivingBase target;
    public int attTime;
    public int smokeTime;
    public boolean seeprey;
    public boolean grounded;
    public boolean noDespawn;
    public float sinage;

    public EntityAechorPlant(World world1)
    {
        super(world1);
        this.setSize(this.rand.nextInt(4) + 1);
        this.sinage = this.rand.nextFloat() * 6.0F;
        this.smokeTime = this.attTime = 0;
        this.seeprey = false;
        this.setSize(0.75F + (float)this.getSize() * 0.125F, 0.5F + (float)this.getSize() * 0.075F);
        this.setPosition(this.posX, this.posY, this.posZ);
        this.setPoisonAmount(2);
        this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(20.0D);
        this.setEntityHealth((float)(10 + this.getSize() * 2));
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    public boolean getCanSpawnHere()
    {
        int i = MathHelper.floor_double(this.posX);
        int j = MathHelper.floor_double(this.boundingBox.minY);
        int k = MathHelper.floor_double(this.posZ);
        return this.worldObj.getBlockId(i, j - 1, k) == AetherBlocks.AetherGrass.blockID && this.worldObj.getBlockLightValue(i, j, k) > 8 && super.getCanSpawnHere();
    }

    public boolean isPotionApplicable(PotionEffect par1PotionEffect)
    {
        return par1PotionEffect.getPotionID() == Potion.poison.id ? false : super.isPotionApplicable(par1PotionEffect);
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate()
    {
        if (this.func_110143_aJ() > 0.0F && this.grounded)
        {
            ++this.entityAge;
            this.despawnEntity();
        }
        else
        {
            super.onLivingUpdate();

            if (this.func_110143_aJ() <= 0.0F)
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

        int j;

        if (this.target == null || this.velocityChanged)
        {
            List i = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(10.0D, 10.0D, 10.0D));

            for (j = 0; j < i.size(); ++j)
            {
                Entity k = (Entity)i.get(j);

                if (k instanceof EntityLivingBase && !(k instanceof EntityAechorPlant) && !(k instanceof EntityCreeper))
                {
                    if (k instanceof EntityPlayer)
                    {
                        EntityPlayer player1 = (EntityPlayer)k;
                        boolean flag = false;

                        if (flag)
                        {
                            continue;
                        }
                    }

                    this.target = (EntityLivingBase)k;
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
            j = MathHelper.floor_double(this.boundingBox.minY);
            int var9 = MathHelper.floor_double(this.posZ);

            if (this.worldObj.getBlockId(var7, j - 1, var9) != AetherBlocks.AetherGrass.blockID && this.grounded)
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
            double d1 = this.target.posX - this.posX;
            double d2 = this.target.posZ - this.posZ;
            double d3 = 1.5D / Math.sqrt(d1 * d1 + d2 * d2 + 0.1D);
            double d4 = 0.1D + Math.sqrt(d1 * d1 + d2 * d2 + 0.1D) * 0.5D + (this.posY - this.target.posY) * 0.25D;
            d1 *= d4;
            d2 *= d4;
            EntityPoisonNeedle entityarrow = new EntityPoisonNeedle(this.worldObj, this);
            entityarrow.posY = this.posY + 0.6D;
            this.worldObj.playSoundAtEntity(this, "aether:aemisc.shootDart", 2.0F, 1.0F / (this.rand.nextFloat() * 0.4F + 0.8F));

            if (!this.worldObj.isRemote)
            {
                entityarrow.setArrowHeading(d1, d4, d2, 0.285F + (float)d4 * 0.55F, 1.0F);
                this.worldObj.spawnEntityInWorld(entityarrow);
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

    public void knockBack(Entity entity, int ii, double dd, double dd1)
    {
        for (int i = 0; i < 8; ++i)
        {
            double d1 = this.posX + (double)(this.rand.nextFloat() - this.rand.nextFloat()) * 0.5D;
            double d2 = this.posY + 0.25D + (double)(this.rand.nextFloat() - this.rand.nextFloat()) * 0.5D;
            double d3 = this.posZ + (double)(this.rand.nextFloat() - this.rand.nextFloat()) * 0.5D;
            double d4 = (double)(this.rand.nextFloat() - this.rand.nextFloat()) * 0.5D;
            double d5 = (double)(this.rand.nextFloat() - this.rand.nextFloat()) * 0.5D;
            this.worldObj.spawnParticle("portal", d1, d2, d3, d4, 0.25D, d5);
        }

        if (this.func_110143_aJ() <= 0.0F)
        {
            super.knockBack(entity, (float)ii, dd, dd1);
        }
    }

    /**
     * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a pig.
     */
    public boolean interact(EntityPlayer entityplayer)
    {
        boolean flag = false;
        ItemStack stack = entityplayer.inventory.getCurrentItem();

        if (stack != null && stack.itemID == AetherItems.SkyrootBucket.itemID && this.getPoisonLeft() > 0)
        {
            this.decrementPoison();
            entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, (ItemStack)null);
            entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, new ItemStack(AetherItems.SkyrootBucket, 1, 2));
            return true;
        }
        else
        {
            if (flag)
            {
                this.noDespawn = true;
                String s = "heart";

                for (int i = 0; i < 7; ++i)
                {
                    double d = this.rand.nextGaussian() * 0.02D;
                    double d1 = this.rand.nextGaussian() * 0.02D;
                    double d2 = this.rand.nextGaussian() * 0.02D;
                    this.worldObj.spawnParticle(s, this.posX + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, this.posY + 0.5D + (double)(this.rand.nextFloat() * this.height), this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, d, d1, d2);
                }
            }

            return false;
        }
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setBoolean("Grounded", this.grounded);
        nbttagcompound.setBoolean("NoDespawn", this.noDespawn);
        nbttagcompound.setShort("AttTime", (short)this.attTime);
        nbttagcompound.setShort("Size", (short)this.getSize());
        nbttagcompound.setInteger("Poison", this.getPoisonLeft());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readEntityFromNBT(nbttagcompound);
        this.grounded = nbttagcompound.getBoolean("Grounded");
        this.noDespawn = nbttagcompound.getBoolean("NoDespawn");
        this.attTime = nbttagcompound.getShort("AttTime");
        this.setSize(nbttagcompound.getShort("Size"));
        this.setPoisonAmount(nbttagcompound.getInteger("Poison"));
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

    public void setPoisonAmount(int amount)
    {
        this.dataWatcher.updateObject(16, Integer.valueOf(amount));
    }

    public void decrementPoison()
    {
        this.dataWatcher.updateObject(16, Integer.valueOf(this.dataWatcher.getWatchableObjectInt(16) - 1));
    }

    public int getSize()
    {
        return this.dataWatcher.getWatchableObjectShort(17);
    }

    public void setSize(int amount)
    {
        this.dataWatcher.updateObject(17, Short.valueOf((short)amount));
    }

    /**
     * Drop 0-2 items of this living's type. @param par1 - Whether this entity has recently been hit by a player. @param
     * par2 - Level of Looting used to kill this mob.
     */
    protected void dropFewItems(boolean var1, int var2)
    {
        this.dropItem(AetherItems.AechorPetal.itemID, 2);
    }

    public EntityAgeable createChild(EntityAgeable entityageable)
    {
        return null;
    }
}
