package net.aetherteam.aether.entities;

import java.util.List;

import net.aetherteam.aether.Aether;
import net.aetherteam.aether.blocks.AetherBlocks;
import net.aetherteam.aether.interfaces.IAetherMob;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityCockatrice extends EntityAetherMob implements IAetherMob
{
    public String dir = "/net/aetherteam/aether/client/sprites";
    public EntityLiving target;
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

    public EntityCockatrice(World var1)
    {
        super(var1);
        this.stepHeight = 1.0F;
        this.jrem = 0;
        this.jumps = 3;
        this.texture = this.dir + "/mobs/cockatrice/cockatrice.png";
        this.setSize(1.0F, 2.0F);
        this.timeUntilNextEgg = this.rand.nextInt(6000) + 6000;
    }

    public int getMaxHealth()
    {
        return 10;
    }

    public boolean isPotionApplicable(PotionEffect var1)
    {
        return var1.getPotionID() == Potion.poison.id ? false : super.isPotionApplicable(var1);
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    public boolean getCanSpawnHere()
    {
        int var1 = MathHelper.floor_double(this.posX);
        int var2 = MathHelper.floor_double(this.boundingBox.minY);
        int var3 = MathHelper.floor_double(this.posZ);
        return this.rand.nextInt(25) == 0 && this.getBlockPathWeight(var1, var2, var3) >= 0.0F && this.worldObj.checkNoEntityCollision(this.boundingBox) && this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox).size() == 0 && !this.worldObj.isAnyLiquid(this.boundingBox) && this.worldObj.getBlockId(var1, var2 - 1, var3) != AetherBlocks.DungeonStone.blockID && this.worldObj.getBlockId(var1, var2 - 1, var3) != AetherBlocks.LightDungeonStone.blockID && this.worldObj.getBlockId(var1, var2 - 1, var3) != AetherBlocks.LockedDungeonStone.blockID && this.worldObj.getBlockId(var1, var2 - 1, var3) != AetherBlocks.LockedLightDungeonStone.blockID && this.worldObj.getBlockId(var1, var2 - 1, var3) != AetherBlocks.Holystone.blockID && this.worldObj.difficultySetting > 0 && !this.worldObj.isDaytime();
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

            List var1 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(0.5D, 0.75D, 0.5D));
            byte var2 = 0;

            if (var2 < var1.size())
            {
                Entity var3 = (Entity) var1.get(var2);
                var3.mountEntity(this);
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
    protected void attackEntity(Entity var1, float var2)
    {
        if (var1 instanceof EntityLiving)
        {
            this.target = (EntityLiving) var1;

            if (var2 < 10.0F)
            {
                double var3 = var1.posX - this.posX;
                double var5 = var1.posZ - this.posZ;

                if (this.target != null)
                {
                    if (this.target.isDead || (double) this.target.getDistanceToEntity(this) > 12.0D)
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

                this.rotationYaw = (float) (Math.atan2(var5, var3) * 180.0D / Math.PI) - 90.0F;
                this.hasAttacked = true;
            }
        }
    }

    public void shootTarget()
    {
        if (this.worldObj.difficultySetting != 0)
        {
            double var1 = this.target.posX - this.posX;
            double var3 = this.target.posZ - this.posZ;
            double var5 = 1.5D / Math.sqrt(var1 * var1 + var3 * var3 + 0.1D);
            double var7 = 0.1D + Math.sqrt(var1 * var1 + var3 * var3 + 0.1D) * 0.5D + (this.posY - this.target.posY) * 0.25D;
            double var10000 = var1 * var5;
            var10000 = var3 * var5;
            EntityPoisonNeedle var9 = new EntityPoisonNeedle(this.worldObj, this);
            var9.posY = this.posY + 0.5D;
            this.worldObj.playSoundAtEntity(this, "aemisc.shootDart", 2.0F, 1.0F / (this.rand.nextFloat() * 0.4F + 0.8F));

            if (!this.worldObj.isRemote)
            {
                this.worldObj.spawnEntityInWorld(var9);
            }
        }
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate()
    {
        super.onLivingUpdate();
        this.field_756_e = this.field_752_b;
        this.field_757_d = this.destPos;
        this.destPos = (float) ((double) this.destPos + (double) (this.onGround ? -1 : 4) * 0.05D);

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
            this.jpress = false;
            this.jrem = this.jumps;
        }

        if (!this.onGround && this.field_755_h < 1.0F)
        {
            this.field_755_h = 1.0F;
        }

        this.field_755_h = (float) ((double) this.field_755_h * 0.9D);

        if (!this.onGround && this.motionY < 0.0D)
        {
            if (this.riddenByEntity == null)
            {
                this.motionY *= 0.6D;
            } else
            {
                this.motionY *= 0.6375D;
            }
        }

        this.field_752_b += this.field_755_h * 2.0F;

        if (!this.worldObj.isRemote && --this.timeUntilNextEgg <= 0)
        {
            this.timeUntilNextEgg = this.rand.nextInt(6000) + 6000;
        }
    }

    /**
     * Called when the mob is falling. Calculates and applies fall damage.
     */
    protected void fall(float var1)
    {}

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource var1, int var2)
    {
        Entity var3 = var1.getEntity();

        if (var3 != null && this.riddenByEntity != null && var3 == this.riddenByEntity)
        {
            return false;
        } else
        {
            boolean var4 = super.attackEntityFrom(var1, var2);

            if (var4 && this.riddenByEntity != null && (this.health <= 0 || this.rand.nextInt(3) == 0))
            {
                this.riddenByEntity.mountEntity(this);
            }

            return var4;
        }
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound var1)
    {
        super.writeEntityToNBT(var1);
        var1.setShort("Jumps", (short) this.jumps);
        var1.setShort("Remaining", (short) this.jrem);

        if (this.riddenByEntity != null)
        {
            this.gotrider = true;
        }

        var1.setBoolean("GotRider", this.gotrider);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound var1)
    {
        super.readEntityFromNBT(var1);
        this.jumps = var1.getShort("Jumps");
        this.jrem = var1.getShort("Remaining");
        this.gotrider = var1.getBoolean("GotRider");
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected String getLivingSound()
    {
        return "aemob.moa.say";
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected String getHurtSound()
    {
        return "aemob.moa.say";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected String getDeathSound()
    {
        return "aemob.moa.say";
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
