package net.aetherteam.aether.entities;

import net.aetherteam.aether.AetherNameGen;
import net.aetherteam.aether.interfaces.IAetherMob;
import net.aetherteam.aether.party.Party;
import net.minecraft.entity.EntityLiving;
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

    public EntityTrackingGolem(World var1)
    {
        super(var1);
        this.setSize(1.0F, 2.0F);
        this.moveSpeed = 1.2F;

        if (!this.getSeenEnemy())
        {
            this.texture = "/net/aetherteam/aether/client/sprites/mobs/sentrygolem/sentryGolem.png";
        }
        else
        {
            this.texture = "/net/aetherteam/aether/client/sprites/mobs/sentrygolem/sentryGolem_red.png";
        }
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

    public void setSeenEnemy(boolean var1)
    {
        if (var1)
        {
            this.worldObj.playSoundAtEntity(this, "aemob.sentryGolem.seenEnemy", 5.0F, this.rand.nextFloat() * 0.4F + 0.8F);
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
        return "aemob.sentryGolem.say";
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected String getHurtSound()
    {
        return "aemob.sentryGolem.say";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected String getDeathSound()
    {
        return "aemob.sentryGolem.death";
    }

    /**
     * Plays step sound at given x, y, z for the entity
     */
    protected void playStepSound(int var1, int var2, int var3, int var4)
    {
        this.worldObj.playSoundAtEntity(this, "mob.cow.step", 0.15F, 1.0F);
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();
        EntityPlayer var1 = this.worldObj.getClosestPlayerToEntity(this, 8.0D);

        if (this.entityToAttack == null && var1 != null && this.canEntityBeSeen(var1) && !var1.isDead && !var1.capabilities.isCreativeMode)
        {
            this.entityToAttack = var1;
            var1.faceEntity(this, 3.5F, (float)var1.getVerticalFaceSpeed());
        }

        if (this.entityToAttack != null && this.entityToAttack instanceof EntityLiving && this.canEntityBeSeen(this.entityToAttack) && !this.entityToAttack.isDead)
        {
            ((EntityLiving)this.entityToAttack).faceEntity(this, 3.5F, (float)((EntityLiving)this.entityToAttack).getVerticalFaceSpeed());
            this.faceEntity(this.entityToAttack, 10.0F, 10.0F);

            if (!this.getSeenEnemy())
            {
                this.setSeenEnemy(true);
            }

            if (!this.worldObj.isRemote)
            {
                ((EntityLiving)this.entityToAttack).addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 10, 3));
                ((EntityLiving)this.entityToAttack).addPotionEffect(new PotionEffect(Potion.confusion.id, 100, 3));
            }
        }
        else
        {
            this.entityToAttack = null;
            this.setSeenEnemy(false);
        }

        if (!this.getSeenEnemy())
        {
            this.texture = "/net/aetherteam/aether/client/sprites/mobs/sentrygolem/sentryGolem.png";
        }
        else
        {
            this.texture = "/net/aetherteam/aether/client/sprites/mobs/sentrygolem/sentryGolem_red.png";
        }
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    public boolean getCanSpawnHere()
    {
        int var1 = MathHelper.floor_double(this.posX);
        int var2 = MathHelper.floor_double(this.boundingBox.minY);
        int var3 = MathHelper.floor_double(this.posZ);
        return this.rand.nextInt(25) == 0 && this.getBlockPathWeight(var1, var2, var3) >= 0.0F && this.worldObj.checkNoEntityCollision(this.boundingBox) && this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox).size() == 0 && !this.worldObj.isAnyLiquid(this.boundingBox) && this.worldObj.difficultySetting > 0;
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound var1)
    {
        super.writeEntityToNBT(var1);
        var1.setBoolean("seen", this.getSeenEnemy());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound var1)
    {
        super.readEntityFromNBT(var1);
        this.setSeenEnemy(var1.getBoolean("seen"));
    }

    /**
     * Returns the texture's file path as a String.
     */
    public String getTexture()
    {
        return !this.getSeenEnemy() ? (this.texture = "/net/aetherteam/aether/client/sprites/mobs/sentrygolem/sentryGolem.png") : (this.texture = "/net/aetherteam/aether/client/sprites/mobs/sentrygolem/sentryGolem_red.png");
    }

    public int getMaxHealth()
    {
        return 20;
    }
}
