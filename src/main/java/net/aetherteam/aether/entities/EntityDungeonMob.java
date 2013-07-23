package net.aetherteam.aether.entities;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import java.util.ArrayList;
import java.util.Random;
import net.aetherteam.aether.blocks.AetherBlocks;
import net.aetherteam.aether.dungeons.Dungeon;
import net.aetherteam.aether.dungeons.DungeonHandler;
import net.aetherteam.aether.party.Party;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;

public class EntityDungeonMob extends EntityCreature
    implements IMob
{
    Random random = new Random();
    protected int attackStrength;

    public EntityDungeonMob(World world)
    {
        super(world);
        this.attackStrength = 2;
    }

    public int getMaxHealth()
    {
        return 20;
    }

    public void onLivingUpdate()
    {
        float f = getBrightness(1.0F);
        Side side = FMLCommonHandler.instance().getEffectiveSide();

        if (side.isServer())
        {
            Dungeon dungeon = DungeonHandler.instance().getInstanceAt(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ));

            if ((dungeon != null) && (!dungeon.isActive()))
            {
                setDead();
            }
        }

        if (f > 0.5F)
        {
            this.entityAge += 2;
        }

        super.onLivingUpdate();
    }

    protected int getDropItemId()
    {
        if (this.rand.nextInt(5) == 0)
        {
            return AetherBlocks.LightDungeonStone.blockID;
        }

        return AetherBlocks.DungeonStone.blockID;
    }

    protected Entity findPlayerToAttack()
    {
        EntityPlayer entityplayer = this.worldObj.getClosestPlayerToEntity(this, 16.0D);

        if ((entityplayer != null) && (canEntityBeSeen(entityplayer)) && (!entityplayer.capabilities.isCreativeMode))
        {
            return entityplayer;
        }

        return null;
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

    protected void attackEntity(Entity entity, float f)
    {
        if ((this.attackTime <= 0) && (f < 2.0F) && (entity.boundingBox.maxY > this.boundingBox.minY) && (entity.boundingBox.minY < this.boundingBox.maxY))
        {
            this.attackTime = 20;
            entity.attackEntityFrom(DamageSource.causeMobDamage(this), this.attackStrength);
        }
    }

    public float getBlockPathWeight(int i, int j, int k)
    {
        return 0.5F - this.worldObj.getLightBrightness(i, j, k);
    }

    public void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeEntityToNBT(nbttagcompound);
    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readEntityFromNBT(nbttagcompound);
    }

    public boolean getCanSpawnHere()
    {
        int i = MathHelper.floor_double(this.posX);
        int j = MathHelper.floor_double(this.boundingBox.minY);
        int k = MathHelper.floor_double(this.posZ);

        if (this.worldObj.getSavedLightValue(EnumSkyBlock.Sky, i, j, k) > this.rand.nextInt(32))
        {
            return false;
        }

        int l = this.worldObj.getBlockLightValue(i, j, k);

        if (this.worldObj.isRaining())
        {
            int i1 = this.worldObj.skylightSubtracted;
            this.worldObj.skylightSubtracted = 10;
            l = this.worldObj.getBlockLightValue(i, j, k);
            this.worldObj.skylightSubtracted = i1;
        }

        return (l <= this.rand.nextInt(8)) && (super.getCanSpawnHere());
    }

    protected void onDeathUpdate()
    {
        if (this.deathTime == 18)
        {
            if ((!this.worldObj.isRemote) && ((this.recentlyHit > 0) || (isPlayer())) && (!isChild()))
            {
                for (int amount = 0; amount < this.random.nextInt(4); amount++)
                {
                    this.worldObj.spawnEntityInWorld(new EntityAetherCoin(this.worldObj, this.posX, this.posY, this.posZ, 1));
                }
            }
        }

        super.onDeathUpdate();
    }
}

