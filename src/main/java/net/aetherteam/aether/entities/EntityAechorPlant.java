package net.aetherteam.aether.entities;

import java.util.List;
import java.util.Random;
import net.aetherteam.aether.blocks.AetherBlocks;
import net.aetherteam.aether.items.AetherItems;
import net.minecraft.block.Block;
import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
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

    public EntityAechorPlant(World world1)
    {
        super(world1);
        this.texture = (this.dir + "/mobs/aechorplant/aechorplant.png");
        setSize(this.rand.nextInt(4) + 1);
        this.health = (10 + getSize() * 2);
        this.sinage = (this.rand.nextFloat() * 6.0F);
        this.smokeTime = (this.attTime = 0);
        this.seeprey = false;
        setSize(0.75F + getSize() * 0.125F, 0.5F + getSize() * 0.075F);
        setPosition(this.posX, this.posY, this.posZ);
        setPoisonAmount(2);
    }

    public boolean getCanSpawnHere()
    {
        int i = MathHelper.floor_double(this.posX);
        int j = MathHelper.floor_double(this.boundingBox.minY);
        int k = MathHelper.floor_double(this.posZ);
        return (this.worldObj.getBlockId(i, j - 1, k) == AetherBlocks.AetherGrass.blockID) && (this.worldObj.getBlockLightValue(i, j, k) > 8) && (super.getCanSpawnHere());
    }

    public boolean isPotionApplicable(PotionEffect par1PotionEffect)
    {
        return par1PotionEffect.getPotionID() == Potion.poison.id ? false : super.isPotionApplicable(par1PotionEffect);
    }

    public void onLivingUpdate()
    {
        if ((this.health <= 0) || (!this.grounded))
        {
            super.onLivingUpdate();

            if (this.health > 0);
        }
        else
        {
            this.entityAge += 1;
            despawnEntity();
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

        if ((this.target == null) || (this.velocityChanged))
        {
            List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(10.0D, 10.0D, 10.0D));

            for (int j = 0; j < list.size(); j++)
            {
                Entity entity1 = (Entity)list.get(j);
                EntityPlayer player1;
                boolean flag;
                int i;
                int j;
                int k;

                if (((entity1 instanceof EntityLiving)) && (!(entity1 instanceof EntityAechorPlant)) && (!(entity1 instanceof EntityCreeper)))
                {
                    if ((entity1 instanceof EntityPlayer))
                    {
                        EntityPlayer player1 = (EntityPlayer)entity1;
                        boolean flag = false;

                        if (flag);
                    }
                    else
                    {
                        this.target = ((EntityLiving)entity1);
                        break;
                    }
                }
            }
        }

        if (this.target != null)
        {
            if ((this.target.isDead) || (this.target.getDistanceToEntity(this) > 12.0D))
            {
                this.target = null;
                this.attTime = 0;
            }
            else if ((this.target instanceof EntityPlayer))
            {
                player1 = (EntityPlayer)this.target;
                flag = false;

                if (flag)
                {
                    this.target = null;
                    this.attTime = 0;
                }
            }

            if ((this.target != null) && (this.attTime >= 20) && (canEntityBeSeen(this.target)) && (this.target.getDistanceToEntity(this) < 12.0D))
            {
                shootTarget();
                this.attTime = -10;
            }

            if (this.attTime < 20)
            {
                this.attTime += 1;
            }
        }

        this.smokeTime += 1;

        if (this.smokeTime >= (this.seeprey ? 3 : 8))
        {
            this.smokeTime = 0;
            i = MathHelper.floor_double(this.posX);
            j = MathHelper.floor_double(this.boundingBox.minY);
            k = MathHelper.floor_double(this.posZ);

            if ((this.worldObj.getBlockId(i, j - 1, k) != AetherBlocks.AetherGrass.blockID) && (this.grounded))
            {
                this.isDead = true;
            }
        }

        this.seeprey = ((this.target != null) || (this.worldObj.getClosestPlayerToEntity(this, 10.0D) != null));
    }

    public void shootTarget()
    {
        if ((this.worldObj.difficultySetting == 0) || (this.target == null) || (!canEntityBeSeen(this.target)) || (MathHelper.floor_double(this.target.posY) != MathHelper.floor_double(this.posY)))
        {
            return;
        }

        double d1 = this.target.posX - this.posX;
        double d2 = this.target.posZ - this.posZ;
        double d3 = 1.5D / Math.sqrt(d1 * d1 + d2 * d2 + 0.1D);
        double d4 = 0.1D + Math.sqrt(d1 * d1 + d2 * d2 + 0.1D) * 0.5D + (this.posY - this.target.posY) * 0.25D;
        d1 *= d4;
        d2 *= d4;
        EntityPoisonNeedle entityarrow = new EntityPoisonNeedle(this.worldObj, this);
        this.posY += 0.6D;
        this.worldObj.playSoundAtEntity(this, "aemisc.shootDart", 2.0F, 1.0F / (this.rand.nextFloat() * 0.4F + 0.8F));

        if (!this.worldObj.isRemote)
        {
            entityarrow.setArrowHeading(d1, d4, d2, 0.285F + (float)d4 * 0.55F, 1.0F);
            this.worldObj.spawnEntityInWorld(entityarrow);
        }
    }

    protected String getHurtSound()
    {
        return "damage.hurtflesh";
    }

    protected String getDeathSound()
    {
        return "damage.fallbig";
    }

    public void knockBack(Entity entity, int ii, double dd, double dd1)
    {
        for (int i = 0; i < 8; i++)
        {
            double d1 = this.posX + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.5D;
            double d2 = this.posY + 0.25D + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.5D;
            double d3 = this.posZ + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.5D;
            double d4 = (this.rand.nextFloat() - this.rand.nextFloat()) * 0.5D;
            double d5 = (this.rand.nextFloat() - this.rand.nextFloat()) * 0.5D;
            this.worldObj.spawnParticle("portal", d1, d2, d3, d4, 0.25D, d5);
        }

        if (this.health > 0)
        {
            return;
        }

        super.knockBack(entity, ii, dd, dd1);
    }

    public boolean interact(EntityPlayer entityplayer)
    {
        boolean flag = false;
        ItemStack stack = entityplayer.inventory.getCurrentItem();

        if ((stack != null) &&
                (stack.itemID == AetherItems.SkyrootBucket.itemID) && (getPoisonLeft() > 0))
        {
            decrementPoison();
            entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, null);
            entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, new ItemStack(AetherItems.SkyrootBucket, 1, 2));
            return true;
        }

        if (flag)
        {
            this.noDespawn = true;
            String s = "heart";

            for (int i = 0; i < 7; i++)
            {
                double d = this.rand.nextGaussian() * 0.02D;
                double d1 = this.rand.nextGaussian() * 0.02D;
                double d2 = this.rand.nextGaussian() * 0.02D;
                this.worldObj.spawnParticle(s, this.posX + this.rand.nextFloat() * this.width * 2.0F - this.width, this.posY + 0.5D + this.rand.nextFloat() * this.height, this.posZ + this.rand.nextFloat() * this.width * 2.0F - this.width, d, d1, d2);
            }
        }

        return false;
    }

    public void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setBoolean("Grounded", this.grounded);
        nbttagcompound.setBoolean("NoDespawn", this.noDespawn);
        nbttagcompound.setShort("AttTime", (short)this.attTime);
        nbttagcompound.setShort("Size", (short)getSize());
        nbttagcompound.setInteger("Poison", getPoisonLeft());
    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readEntityFromNBT(nbttagcompound);
        this.grounded = nbttagcompound.getBoolean("Grounded");
        this.noDespawn = nbttagcompound.getBoolean("NoDespawn");
        this.attTime = nbttagcompound.getShort("AttTime");
        setSize(nbttagcompound.getShort("Size"));
        setPoisonAmount(nbttagcompound.getInteger("Poison"));
        setSize(0.75F + getSize() * 0.125F, 0.5F + getSize() * 0.075F);
        setPosition(this.posX, this.posY, this.posZ);
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

    protected void dropFewItems(boolean var1, int var2)
    {
        dropItem(AetherItems.AechorPetal.itemID, 2);
    }

    public int getMaxHealth()
    {
        return 20;
    }

    public EntityAgeable createChild(EntityAgeable entityageable)
    {
        return null;
    }
}

