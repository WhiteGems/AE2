package net.aetherteam.aether.entities;

import java.util.List;
import net.aetherteam.aether.Aether;
import net.aetherteam.aether.blocks.AetherBlocks;
import net.aetherteam.aether.interfaces.IAetherMob;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityCockatrice extends EntityAetherMob implements IAetherMob
{
    public EntityLivingBase target;
    public float field_752_b;
    public float destPos = 0.0F;
    public float field_757_d;
    public float field_756_e;
    public float field_755_h = 1.0F;
    public int timeUntilNextEgg;
    public int jumps;
    public int jrem;
    public boolean jpress;
    public boolean gotrider;

    public EntityCockatrice(World world)
    {
        super(world);
        this.stepHeight = 1.0F;
        this.jrem = 0;
        this.jumps = 3;
        this.setSize(1.0F, 2.0F);
        this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(10.0D);
        this.setEntityHealth(10.0F);
        this.timeUntilNextEgg = this.rand.nextInt(6000) + 6000;
    }

    public boolean isPotionApplicable(PotionEffect par1PotionEffect)
    {
        return par1PotionEffect.getPotionID() == Potion.poison.id ? false : super.isPotionApplicable(par1PotionEffect);
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    public boolean getCanSpawnHere()
    {
        int i = MathHelper.floor_double(this.posX);
        int j = MathHelper.floor_double(this.boundingBox.minY);
        int k = MathHelper.floor_double(this.posZ);
        return this.rand.nextInt(25) == 0 && this.getBlockPathWeight(i, j, k) >= 0.0F && this.worldObj.checkNoEntityCollision(this.boundingBox) && this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox).size() == 0 && !this.worldObj.isAnyLiquid(this.boundingBox) && this.worldObj.getBlockId(i, j - 1, k) != AetherBlocks.DungeonStone.blockID && this.worldObj.getBlockId(i, j - 1, k) != AetherBlocks.LightDungeonStone.blockID && this.worldObj.getBlockId(i, j - 1, k) != AetherBlocks.LockedDungeonStone.blockID && this.worldObj.getBlockId(i, j - 1, k) != AetherBlocks.LockedLightDungeonStone.blockID && this.worldObj.getBlockId(i, j - 1, k) != AetherBlocks.Holystone.blockID && this.worldObj.difficultySetting > 0 && !this.worldObj.isDaytime();
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();
        this.ignoreFrustumCheck = this.riddenByEntity == Aether.proxy.getClientPlayer();

        if (!this.worldObj.isRemote && this.gotrider)
        {
            if (this.riddenByEntity != null)
            {
                return;
            }

            List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(0.5D, 0.75D, 0.5D));
            byte i = 0;

            if (i < list.size())
            {
                Entity entity = (Entity)list.get(i);
                entity.mountEntity(this);
            }

            this.gotrider = false;
        }

        if (!this.worldObj.isRemote && this.worldObj.difficultySetting == 0)
        {
            this.setDead();
        }
    }

    /**
     * Basic mob attack. Default to touch of death in EntityCreature. Overridden by each mob to define their attack.
     */
    protected void attackEntity(Entity entity, float f)
    {
        if (entity instanceof EntityLivingBase)
        {
            this.target = (EntityLivingBase)entity;

            if (f < 10.0F)
            {
                double d = entity.posX - this.posX;
                double d1 = entity.posZ - this.posZ;

                if (this.target != null)
                {
                    if (this.target.isDead || (double)this.target.getDistanceToEntity(this) > 12.0D)
                    {
                        this.target = null;
                        this.attackTime = 0;
                    }

                    if (this.attackTime >= 20 && this.canEntityBeSeen(this.target))
                    {
                        this.shootTarget();
                        this.attackTime = -10;
                    }

                    if (this.attackTime < 20)
                    {
                        this.attackTime += 2;
                    }
                }

                this.rotationYaw = (float)(Math.atan2(d1, d) * 180.0D / Math.PI) - 90.0F;
                this.hasAttacked = true;
            }
        }
    }

    public void shootTarget()
    {
        if (this.worldObj.difficultySetting != 0)
        {
            double d1 = this.target.posX - this.posX;
            double d2 = this.target.posZ - this.posZ;
            double d3 = 1.5D / Math.sqrt(d1 * d1 + d2 * d2 + 0.1D);
            double d4 = 0.1D + Math.sqrt(d1 * d1 + d2 * d2 + 0.1D) * 0.5D + (this.posY - this.target.posY) * 0.25D;
            double var10000 = d1 * d3;
            var10000 = d2 * d3;
            EntityPoisonNeedle entityarrow = new EntityPoisonNeedle(this.worldObj, this);
            entityarrow.posY = this.posY + 0.5D;
            this.worldObj.playSoundAtEntity(this, "aether:aemisc.shootDart", 2.0F, 1.0F / (this.rand.nextFloat() * 0.4F + 0.8F));

            if (!this.worldObj.isRemote)
            {
                this.worldObj.spawnEntityInWorld(entityarrow);
            }
        }
    }

    public void updateWingFields()
    {
        this.field_756_e = this.field_752_b;
        this.field_757_d = this.destPos;
        this.destPos = (float)((double)this.destPos + (double)(this.onGround ? -1 : 4) * 0.05D);

        if (this.destPos < 0.01F)
        {
            this.destPos = 0.01F;
        }

        if (this.destPos > 1.0F)
        {
            this.destPos = 1.0F;
        }

        if (this.onGround)
        {
            this.destPos = 0.0F;
        }

        if (!this.onGround && this.field_755_h < 1.0F)
        {
            this.field_755_h = 1.0F;
        }

        this.field_755_h = (float)((double)this.field_755_h * 0.9D);
        this.field_752_b += this.field_755_h * 2.0F;
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate()
    {
        super.onLivingUpdate();
        this.updateWingFields();

        if (!this.worldObj.isRemote && --this.timeUntilNextEgg <= 0)
        {
            this.timeUntilNextEgg = this.rand.nextInt(6000) + 6000;
        }
    }

    /**
     * Called when the mob is falling. Calculates and applies fall damage.
     */
    protected void fall(float f) {}

    public boolean attackEntityFrom(DamageSource src, int i)
    {
        Entity entity = src.getEntity();

        if (entity != null && this.riddenByEntity != null && entity == this.riddenByEntity)
        {
            return false;
        }
        else
        {
            boolean flag = super.attackEntityFrom(src, (float)i);

            if (flag && this.riddenByEntity != null && (this.func_110143_aJ() <= 0.0F || this.rand.nextInt(3) == 0))
            {
                this.riddenByEntity.mountEntity(this);
            }

            return flag;
        }
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setShort("Jumps", (short)this.jumps);
        nbttagcompound.setShort("Remaining", (short)this.jrem);

        if (this.riddenByEntity != null)
        {
            this.gotrider = true;
        }

        nbttagcompound.setBoolean("GotRider", this.gotrider);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readEntityFromNBT(nbttagcompound);
        this.jumps = nbttagcompound.getShort("Jumps");
        this.jrem = nbttagcompound.getShort("Remaining");
        this.gotrider = nbttagcompound.getBoolean("GotRider");
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected String getLivingSound()
    {
        return "aether:aemob.moa.say";
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected String getHurtSound()
    {
        return "aether:aemob.moa.say";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected String getDeathSound()
    {
        return "aether:aemob.moa.say";
    }

    /**
     * Drop 0-2 items of this living's type. @param par1 - Whether this entity has recently been hit by a player. @param
     * par2 - Level of Looting used to kill this mob.
     */
    protected void dropFewItems(boolean var1, int var2)
    {
        this.dropItem(Item.feather.itemID, 3);
    }
}
