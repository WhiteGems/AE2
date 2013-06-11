package net.aetherteam.aether.entities.bosses;

import java.util.Random;

import net.aetherteam.aether.dungeons.Dungeon;
import net.aetherteam.aether.dungeons.DungeonHandler;
import net.aetherteam.aether.entities.EntityAetherCoin;
import net.aetherteam.aether.enums.EnumBossType;
import net.aetherteam.aether.interfaces.IAetherBoss;
import net.aetherteam.aether.party.Party;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;

public abstract class EntityBossMob extends EntityCreature implements IMob, IAetherBoss
{
    Random random = new Random();

    public EntityBossMob(World var1)
    {
        super(var1);
        this.experienceValue = 5;
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate()
    {
        this.updateArmSwingProgress();
        float var1 = this.getBrightness(1.0F);

        if (var1 > 0.5F)
        {
            this.entityAge += 2;
        }

        super.onLivingUpdate();
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource var1, int var2)
    {
        Dungeon var3 = DungeonHandler.instance().getInstanceAt(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ));

        if (var3 != null && var3.hasQueuedParty())
        {
            Party var8 = var3.getQueuedParty();
            int var5 = var3.getQueuedMembers().size() + 1;
            float var6 = (float) (var5 - 1) * 0.075F;
            int var7 = MathHelper.clamp_int((int) ((float) var2 - (float) var2 * var6), 1, var2);
            return super.attackEntityFrom(var1, var7);
        } else if (!super.attackEntityFrom(var1, var2))
        {
            return false;
        } else
        {
            Entity var4 = var1.getEntity();

            if (var4 != null)
            {
                if (this.riddenByEntity == var4 || this.ridingEntity == var4)
                {
                    return true;
                }

                if (var4 != this)
                {
                    this.entityToAttack = var4;
                }
            }

            return true;
        }
    }

    /**
     * handles entity death timer, experience orb and particle creation
     */
    protected void onDeathUpdate()
    {
        if (this.deathTime == 0 && !this.worldObj.isRemote && (this.recentlyHit > 0 || this.isPlayer()) && !this.isChild())
        {
            for (int var1 = 0; var1 < 10 + this.random.nextInt(15); ++var1)
            {
                this.worldObj.spawnEntityInWorld(new EntityAetherCoin(this.worldObj, this.posX, this.posY, this.posZ, 1));
            }
        }

        super.onDeathUpdate();
    }

    /**
     * Determines if an entity can be despawned, used on idle far away entities
     */
    public boolean canDespawn()
    {
        return false;
    }

    /**
     * Finds the closest player within 16 blocks to attack, or null if this Entity isn't interested in attacking
     * (Animals, Spiders at day, peaceful PigZombies).
     */
    protected Entity findPlayerToAttack()
    {
        EntityPlayer var1 = this.worldObj.getClosestVulnerablePlayerToEntity(this, 16.0D);
        return var1 != null && this.canEntityBeSeen(var1) ? var1 : null;
    }

    public boolean attackEntityAsMob(Entity var1)
    {
        int var2 = this.getAttackStrength(var1);

        if (this.isPotionActive(Potion.damageBoost))
        {
            var2 += 3 << this.getActivePotionEffect(Potion.damageBoost).getAmplifier();
        }

        if (this.isPotionActive(Potion.weakness))
        {
            var2 -= 2 << this.getActivePotionEffect(Potion.weakness).getAmplifier();
        }

        int var3 = 0;

        if (var1 instanceof EntityLiving)
        {
            var2 += EnchantmentHelper.getEnchantmentModifierLiving(this, (EntityLiving) var1);
            var3 += EnchantmentHelper.getKnockbackModifier(this, (EntityLiving) var1);
        }

        boolean var4 = var1.attackEntityFrom(DamageSource.causeMobDamage(this), var2);

        if (var4)
        {
            if (var3 > 0)
            {
                var1.addVelocity((double) (-MathHelper.sin(this.rotationYaw * (float) Math.PI / 180.0F) * (float) var3 * 0.5F), 0.1D, (double) (MathHelper.cos(this.rotationYaw * (float) Math.PI / 180.0F) * (float) var3 * 0.5F));
                this.motionX *= 0.6D;
                this.motionZ *= 0.6D;
            }

            int var5 = EnchantmentHelper.getFireAspectModifier(this);

            if (var5 > 0)
            {
                var1.setFire(var5 * 4);
            }
        }

        return var4;
    }

    /**
     * Basic mob attack. Default to touch of death in EntityCreature. Overridden by each mob to define their attack.
     */
    protected void attackEntity(Entity var1, float var2)
    {
        if (this.attackTime <= 0 && var2 < 2.0F && var1.boundingBox.maxY > this.boundingBox.minY && var1.boundingBox.minY < this.boundingBox.maxY)
        {
            this.attackTime = 20;
            this.attackEntityAsMob(var1);
        }
    }

    /**
     * Takes a coordinate in and returns a weight to determine how likely this creature will try to path to the block.
     * Args: x, y, z
     */
    public float getBlockPathWeight(int var1, int var2, int var3)
    {
        return 0.5F - this.worldObj.getLightBrightness(var1, var2, var3);
    }

    protected boolean isValidLightLevel()
    {
        int var1 = MathHelper.floor_double(this.posX);
        int var2 = MathHelper.floor_double(this.boundingBox.minY);
        int var3 = MathHelper.floor_double(this.posZ);

        if (this.worldObj.getSavedLightValue(EnumSkyBlock.Sky, var1, var2, var3) > this.rand.nextInt(32))
        {
            return false;
        } else
        {
            int var4 = this.worldObj.getBlockLightValue(var1, var2, var3);

            if (this.worldObj.isThundering())
            {
                int var5 = this.worldObj.skylightSubtracted;
                this.worldObj.skylightSubtracted = 10;
                var4 = this.worldObj.getBlockLightValue(var1, var2, var3);
                this.worldObj.skylightSubtracted = var5;
            }

            return var4 <= this.rand.nextInt(8);
        }
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    public boolean getCanSpawnHere()
    {
        return this.isValidLightLevel() && super.getCanSpawnHere();
    }

    /**
     * Returns the amount of damage a mob should deal.
     */
    public int getAttackStrength(Entity var1)
    {
        return 2;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();

        if (!this.worldObj.isRemote && this.getBossHP() != this.health)
        {
            this.setBossHP();
        }
    }

    protected void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(26, Integer.valueOf(this.health));
    }

    public int getBossHP()
    {
        return this.dataWatcher.getWatchableObjectInt(26);
    }

    public void setBossHP()
    {
        this.dataWatcher.updateObject(26, Integer.valueOf(this.health));
    }

    public int getBossEntityID()
    {
        return this.entityId;
    }

    public String getBossTitle()
    {
        return "";
    }

    public Entity getBossEntity()
    {
        return this;
    }

    public int getBossStage()
    {
        return 0;
    }

    public EnumBossType getBossType()
    {
        return EnumBossType.MINI;
    }
}
