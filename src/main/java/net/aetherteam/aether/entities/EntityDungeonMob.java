package net.aetherteam.aether.entities;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import java.util.Random;
import net.aetherteam.aether.blocks.AetherBlocks;
import net.aetherteam.aether.dungeons.Dungeon;
import net.aetherteam.aether.dungeons.DungeonHandler;
import net.aetherteam.aether.party.Party;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.SharedMonsterAttributes;
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

    public EntityDungeonMob(World world)
    {
        super(world);
        this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(20.0D);
        this.setEntityHealth(20.0F);
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate()
    {
        float f = this.getBrightness(1.0F);
        Side side = FMLCommonHandler.instance().getEffectiveSide();

        if (side.isServer())
        {
            Dungeon dungeon = DungeonHandler.instance().getInstanceAt(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ));

            if (dungeon != null && !dungeon.isActive())
            {
                this.setDead();
            }
        }

        if (f > 0.5F)
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
        EntityPlayer entityplayer = this.worldObj.getClosestPlayerToEntity(this, 16.0D);
        return entityplayer != null && this.canEntityBeSeen(entityplayer) && !entityplayer.capabilities.isCreativeMode ? entityplayer : null;
    }

    public boolean attackEntityFrom(DamageSource src, int damage)
    {
        Dungeon dungeon = DungeonHandler.instance().getInstanceAt(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ));

        if (dungeon != null && dungeon.hasQueuedParty())
        {
            Party entity1 = dungeon.getQueuedParty();
            int players = dungeon.getQueuedMembers().size() + 1;
            float damageFactor = (float)(players - 1) * 0.075F;
            int newDamage = MathHelper.clamp_int((int)((float)damage - (float)damage * damageFactor), 1, damage);
            return super.attackEntityFrom(src, (float)newDamage);
        }
        else if (!super.attackEntityFrom(src, (float)damage))
        {
            return false;
        }
        else
        {
            Entity entity = src.getEntity();

            if (entity != null)
            {
                if (this.riddenByEntity == entity || this.ridingEntity == entity)
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
    }

    /**
     * Basic mob attack. Default to touch of death in EntityCreature. Overridden by each mob to define their attack.
     */
    protected void attackEntity(Entity entity, float f)
    {
        if (this.attackTime <= 0 && f < 2.0F && entity.boundingBox.maxY > this.boundingBox.minY && entity.boundingBox.minY < this.boundingBox.maxY)
        {
            this.attackTime = 20;
            entity.attackEntityFrom(DamageSource.causeMobDamage(this), (float)this.attackStrength);
        }
    }

    /**
     * Takes a coordinate in and returns a weight to determine how likely this creature will try to path to the block.
     * Args: x, y, z
     */
    public float getBlockPathWeight(int i, int j, int k)
    {
        return 0.5F - this.worldObj.getLightBrightness(i, j, k);
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

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    public boolean getCanSpawnHere()
    {
        int i = MathHelper.floor_double(this.posX);
        int j = MathHelper.floor_double(this.boundingBox.minY);
        int k = MathHelper.floor_double(this.posZ);

        if (this.worldObj.getSavedLightValue(EnumSkyBlock.Sky, i, j, k) > this.rand.nextInt(32))
        {
            return false;
        }
        else
        {
            int l = this.worldObj.getBlockLightValue(i, j, k);

            if (this.worldObj.isThundering())
            {
                int i1 = this.worldObj.skylightSubtracted;
                this.worldObj.skylightSubtracted = 10;
                l = this.worldObj.getBlockLightValue(i, j, k);
                this.worldObj.skylightSubtracted = i1;
            }

            return l <= this.rand.nextInt(8) && super.getCanSpawnHere();
        }
    }

    /**
     * handles entity death timer, experience orb and particle creation
     */
    protected void onDeathUpdate()
    {
        if (this.deathTime == 18 && !this.worldObj.isRemote && (this.recentlyHit > 0 || this.isPlayer()) && !this.isChild())
        {
            for (int amount = 0; amount < this.random.nextInt(4); ++amount)
            {
                this.worldObj.spawnEntityInWorld(new EntityAetherCoin(this.worldObj, this.posX, this.posY, this.posZ, 1));
            }
        }

        super.onDeathUpdate();
    }
}
