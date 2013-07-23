package net.aetherteam.aether.entities.bosses;

import java.util.ArrayList;
import java.util.Random;
import net.aetherteam.aether.dungeons.Dungeon;
import net.aetherteam.aether.dungeons.DungeonHandler;
import net.aetherteam.aether.entities.EntityAetherCoin;
import net.aetherteam.aether.enums.EnumBossType;
import net.aetherteam.aether.interfaces.IAetherBoss;
import net.aetherteam.aether.party.Party;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;

public abstract class EntityBossMob extends EntityCreature
    implements IMob, IAetherBoss
{
    Random random = new Random();

    public EntityBossMob(World par1World)
    {
        super(par1World);
        this.experienceValue = 5;
    }

    public void onLivingUpdate()
    {
        updateArmSwingProgress();
        float var1 = getBrightness(1.0F);

        if (var1 > 0.5F)
        {
            this.entityAge += 2;
        }

        super.onLivingUpdate();
    }

    public boolean attackEntityFrom(DamageSource src, int damage)
    {
        Dungeon dungeon = DungeonHandler.instance().getInstanceAt(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ));

        if ((dungeon != null) && (dungeon.hasQueuedParty()))
        {
            Party party = dungeon.getQueuedParty();
            int players = dungeon.getQueuedMembers().size() + 1;
            float damageFactor = (players - 1) * 0.075F;
            int newDamage = MathHelper.clamp_int((int)(damage - damage * damageFactor), 1, damage);
            return super.attackEntityFrom(src, newDamage);
        }

        if (!super.attackEntityFrom(src, damage))
        {
            return false;
        }

        Entity entity = src.getEntity();

        if (entity != null)
        {
            if ((this.riddenByEntity == entity) || (this.ridingEntity == entity))
            {
                return true;
            }

            if (entity != this)
            {
                this.entityToAttack = entity;
            }
        }

        return true;
    }

    protected void onDeathUpdate()
    {
        if (this.deathTime == 0)
        {
            if ((!this.worldObj.isRemote) && ((this.recentlyHit > 0) || (isPlayer())) && (!isChild()))
            {
                for (int amount = 0; amount < 10 + this.random.nextInt(15); amount++)
                {
                    this.worldObj.spawnEntityInWorld(new EntityAetherCoin(this.worldObj, this.posX, this.posY, this.posZ, 1));
                }
            }
        }

        super.onDeathUpdate();
    }

    public boolean canDespawn()
    {
        return false;
    }

    protected Entity findPlayerToAttack()
    {
        EntityPlayer var1 = this.worldObj.getClosestVulnerablePlayerToEntity(this, 16.0D);
        return (var1 != null) && (canEntityBeSeen(var1)) ? var1 : null;
    }

    public boolean attackEntityAsMob(Entity par1Entity)
    {
        int var2 = func_82193_c(par1Entity);

        if (isPotionActive(Potion.damageBoost))
        {
            var2 += (3 << getActivePotionEffect(Potion.damageBoost).getAmplifier());
        }

        if (isPotionActive(Potion.weakness))
        {
            var2 -= (2 << getActivePotionEffect(Potion.weakness).getAmplifier());
        }

        int var3 = 0;

        if ((par1Entity instanceof EntityLiving))
        {
            var2 += EnchantmentHelper.getEnchantmentModifierLiving(this, (EntityLiving)par1Entity);
            var3 += EnchantmentHelper.getKnockbackModifier(this, (EntityLiving)par1Entity);
        }

        boolean var4 = par1Entity.attackEntityFrom(DamageSource.causeMobDamage(this), var2);

        if (var4)
        {
            if (var3 > 0)
            {
                par1Entity.addVelocity(-MathHelper.sin(this.rotationYaw * (float)Math.PI / 180.0F) * var3 * 0.5F, 0.1D, MathHelper.cos(this.rotationYaw * (float)Math.PI / 180.0F) * var3 * 0.5F);
                this.motionX *= 0.6D;
                this.motionZ *= 0.6D;
            }

            int var5 = EnchantmentHelper.getFireAspectModifier(this);

            if (var5 > 0)
            {
                par1Entity.setFire(var5 * 4);
            }
        }

        return var4;
    }

    protected void attackEntity(Entity par1Entity, float par2)
    {
        if ((this.attackTime <= 0) && (par2 < 2.0F) && (par1Entity.boundingBox.maxY > this.boundingBox.minY) && (par1Entity.boundingBox.minY < this.boundingBox.maxY))
        {
            this.attackTime = 20;
            attackEntityAsMob(par1Entity);
        }
    }

    public float getBlockPathWeight(int par1, int par2, int par3)
    {
        return 0.5F - this.worldObj.getLightBrightness(par1, par2, par3);
    }

    protected boolean isValidLightLevel()
    {
        int var1 = MathHelper.floor_double(this.posX);
        int var2 = MathHelper.floor_double(this.boundingBox.minY);
        int var3 = MathHelper.floor_double(this.posZ);

        if (this.worldObj.getSavedLightValue(EnumSkyBlock.Sky, var1, var2, var3) > this.rand.nextInt(32))
        {
            return false;
        }

        int var4 = this.worldObj.getBlockLightValue(var1, var2, var3);

        if (this.worldObj.isRaining())
        {
            int var5 = this.worldObj.skylightSubtracted;
            this.worldObj.skylightSubtracted = 10;
            var4 = this.worldObj.getBlockLightValue(var1, var2, var3);
            this.worldObj.skylightSubtracted = var5;
        }

        return var4 <= this.rand.nextInt(8);
    }

    public boolean getCanSpawnHere()
    {
        return (isValidLightLevel()) && (super.getCanSpawnHere());
    }

    public int func_82193_c(Entity par1Entity)
    {
        return 2;
    }

    public void onUpdate()
    {
        super.onUpdate();

        if ((!this.worldObj.isRemote) && (getBossHP() != this.health))
        {
            setBossHP();
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

