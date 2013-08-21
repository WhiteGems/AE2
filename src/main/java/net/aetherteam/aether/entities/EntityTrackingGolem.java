package net.aetherteam.aether.entities;

import net.aetherteam.aether.AetherNameGen;
import net.aetherteam.aether.interfaces.IAetherMob;
import net.aetherteam.aether.party.Party;
import net.aetherteam.playercore_api.cores.IPlayerCoreCommon;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityTrackingGolem extends EntityDungeonMob implements IAetherMob
{
    private String bossName = AetherNameGen.gen();
    private Party fightingParty;
    private static final int START = 0;
    private static final int MIDDLE = 1;
    private static final int END = 2;

    public EntityTrackingGolem(World world)
    {
        super(world);
        this.setSize(1.0F, 2.0F);
        this.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111128_a(1.2000000476837158D);
        this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(20.0D);
        this.setEntityHealth(20.0F);
    }

    public void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(16, Byte.valueOf((byte)0));
    }

    public boolean getSeenEnemy()
    {
        return this.dataWatcher.getWatchableObjectByte(16) == 1;
    }

    public void setSeenEnemy(boolean seen)
    {
        if (seen)
        {
            this.worldObj.playSoundAtEntity(this, "aether:aemob.sentryGolem.seenEnemy", 5.0F, this.rand.nextFloat() * 0.4F + 0.8F);
            this.dataWatcher.updateObject(16, Byte.valueOf((byte)1));
        }
        else
        {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte)0));
        }
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected String getLivingSound()
    {
        return "aether:aemob.sentryGolem.say";
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected String getHurtSound()
    {
        return "aether:aemob.sentryGolem.say";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected String getDeathSound()
    {
        return "aether:aemob.sentryGolem.death";
    }

    /**
     * Plays step sound at given x, y, z for the entity
     */
    protected void playStepSound(int par1, int par2, int par3, int par4)
    {
        this.worldObj.playSoundAtEntity(this, "mob.cow.step", 0.15F, 1.0F);
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();
        EntityPlayer entityplayer = this.worldObj.getClosestPlayerToEntity(this, 8.0D);

        if (this.entityToAttack == null && entityplayer != null && this.canEntityBeSeen(entityplayer) && !entityplayer.isDead && !entityplayer.capabilities.isCreativeMode)
        {
            this.entityToAttack = entityplayer;
        }

        if (this.entityToAttack != null && this.entityToAttack instanceof EntityLivingBase && this.canEntityBeSeen(this.entityToAttack) && !this.entityToAttack.isDead)
        {
            ((IPlayerCoreCommon)this.entityToAttack).faceEntity(this, 3.5F, 40.0F);
            this.faceEntity(this.entityToAttack, 10.0F, 10.0F);

            if (!this.getSeenEnemy())
            {
                this.setSeenEnemy(true);
            }

            if (!this.worldObj.isRemote)
            {
                ((EntityLivingBase)this.entityToAttack).addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 10, 3));
                ((EntityLivingBase)this.entityToAttack).addPotionEffect(new PotionEffect(Potion.confusion.id, 100, 3));
            }
        }
        else
        {
            this.entityToAttack = null;
            this.setSeenEnemy(false);
        }
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    public boolean getCanSpawnHere()
    {
        int i = MathHelper.floor_double(this.posX);
        int j = MathHelper.floor_double(this.boundingBox.minY);
        int k = MathHelper.floor_double(this.posZ);
        return this.rand.nextInt(25) == 0 && this.getBlockPathWeight(i, j, k) >= 0.0F && this.worldObj.checkNoEntityCollision(this.boundingBox) && this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox).size() == 0 && !this.worldObj.isAnyLiquid(this.boundingBox) && this.worldObj.difficultySetting > 0;
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setBoolean("seen", this.getSeenEnemy());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readEntityFromNBT(nbttagcompound);
        this.setSeenEnemy(nbttagcompound.getBoolean("seen"));
    }
}
