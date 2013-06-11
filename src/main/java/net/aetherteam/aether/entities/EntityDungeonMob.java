package net.aetherteam.aether.entities;

import java.util.Random;

import net.aetherteam.aether.blocks.AetherBlocks;
import net.aetherteam.aether.dungeons.Dungeon;
import net.aetherteam.aether.dungeons.DungeonHandler;
import net.aetherteam.aether.party.Party;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;

public class EntityDungeonMob extends EntityCreature implements IMob
{
    Random random = new Random();
    protected int attackStrength = 2;

    public EntityDungeonMob(World var1)
    {
        super(var1);
    }

    public int getMaxHealth()
    {
        return 20;
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate()
    {
        float var1 = this.getBrightness(1.0F);

        if (var1 > 0.5F)
        {
            this.entityAge += 2;
        }

        super.onLivingUpdate();
    }

    /**
     * Returns the item ID for the item the mob drops on death.
     */
    protected int getDropItemId()
    {
        return this.rand.nextInt(5) == 0 ? AetherBlocks.LightDungeonStone.blockID : AetherBlocks.DungeonStone.blockID;
    }

    /**
     * Finds the closest player within 16 blocks to attack, or null if this Entity isn't interested in attacking
     * (Animals, Spiders at day, peaceful PigZombies).
     */
    protected Entity findPlayerToAttack()
    {
        EntityPlayer var1 = this.worldObj.getClosestPlayerToEntity(this, 16.0D);
        return var1 != null && this.canEntityBeSeen(var1) && !var1.capabilities.isCreativeMode ? var1 : null;
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
     * Basic mob attack. Default to touch of death in EntityCreature. Overridden by each mob to define their attack.
     */
    protected void attackEntity(Entity var1, float var2)
    {
        if (this.attackTime <= 0 && var2 < 2.0F && var1.boundingBox.maxY > this.boundingBox.minY && var1.boundingBox.minY < this.boundingBox.maxY)
        {
            this.attackTime = 20;
            var1.attackEntityFrom(DamageSource.causeMobDamage(this), this.attackStrength);
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

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    public boolean getCanSpawnHere()
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

            return var4 <= this.rand.nextInt(8) && super.getCanSpawnHere();
        }
    }

    /**
     * handles entity death timer, experience orb and particle creation
     */
    protected void onDeathUpdate()
    {
        if (this.deathTime == 18 && !this.worldObj.isRemote && (this.recentlyHit > 0 || this.isPlayer()) && !this.isChild())
        {
            for (int var1 = 0; var1 < this.random.nextInt(4); ++var1)
            {
                this.worldObj.spawnEntityInWorld(new EntityAetherCoin(this.worldObj, this.posX, this.posY, this.posZ, 1));
            }
        }

        super.onDeathUpdate();
    }
}
