package net.aetherteam.aether.entities;

import java.util.List;
import java.util.Random;
import net.aetherteam.aether.AetherNameGen;
import net.aetherteam.aether.interfaces.IAetherMob;
import net.aetherteam.aether.party.Party;
import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityTrackingGolem extends EntityDungeonMob
    implements IAetherMob
{
    private String bossName;
    private Party fightingParty;
    private static final int START = 0;
    private static final int MIDDLE = 1;
    private static final int END = 2;

    public EntityTrackingGolem(World world)
    {
        super(world);
        this.bossName = AetherNameGen.gen();
        setSize(1.0F, 2.0F);
        this.moveSpeed = 1.2F;

        if (!getSeenEnemy())
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

    public void setSeenEnemy(boolean seen)
    {
        if (seen)
        {
            this.worldObj.playSoundAtEntity(this, "aemob.sentryGolem.seenEnemy", 5.0F, this.rand.nextFloat() * 0.4F + 0.8F);
            this.dataWatcher.updateObject(16, Byte.valueOf((byte)1));
        }
        else
        {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte)0));
        }
    }

    protected String getLivingSound()
    {
        return "aemob.sentryGolem.say";
    }

    protected String getHurtSound()
    {
        return "aemob.sentryGolem.say";
    }

    protected String getDeathSound()
    {
        return "aemob.sentryGolem.death";
    }

    protected void playStepSound(int par1, int par2, int par3, int par4)
    {
        this.worldObj.playSoundAtEntity(this, "mob.cow.step", 0.15F, 1.0F);
    }

    public void onUpdate()
    {
        super.onUpdate();
        EntityPlayer entityplayer = this.worldObj.getClosestPlayerToEntity(this, 8.0D);

        if (this.entityToAttack == null)
        {
            if ((entityplayer != null) && (canEntityBeSeen(entityplayer)) && (!entityplayer.isDead) && (!entityplayer.capabilities.isCreativeMode))
            {
                this.entityToAttack = entityplayer;
                entityplayer.faceEntity(this, 3.5F, entityplayer.getVerticalFaceSpeed());
            }
        }

        if ((this.entityToAttack != null) && ((this.entityToAttack instanceof EntityLiving)) && (canEntityBeSeen(this.entityToAttack)) && (!this.entityToAttack.isDead))
        {
            ((EntityLiving)this.entityToAttack).faceEntity(this, 3.5F, ((EntityLiving)this.entityToAttack).getVerticalFaceSpeed());
            faceEntity(this.entityToAttack, 10.0F, 10.0F);

            if (!getSeenEnemy())
            {
                setSeenEnemy(true);
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
            setSeenEnemy(false);
        }

        if (!getSeenEnemy())
        {
            this.texture = "/net/aetherteam/aether/client/sprites/mobs/sentrygolem/sentryGolem.png";
        }
        else
        {
            this.texture = "/net/aetherteam/aether/client/sprites/mobs/sentrygolem/sentryGolem_red.png";
        }
    }

    public boolean getCanSpawnHere()
    {
        int i = MathHelper.floor_double(this.posX);
        int j = MathHelper.floor_double(this.boundingBox.minY);
        int k = MathHelper.floor_double(this.posZ);
        return (this.rand.nextInt(25) == 0) && (getBlockPathWeight(i, j, k) >= 0.0F) && (this.worldObj.checkNoEntityCollision(this.boundingBox)) && (this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox).size() == 0) && (!this.worldObj.isAnyLiquid(this.boundingBox)) && (this.worldObj.difficultySetting > 0);
    }

    public void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setBoolean("seen", getSeenEnemy());
    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readEntityFromNBT(nbttagcompound);
        setSeenEnemy(nbttagcompound.getBoolean("seen"));
    }

    public String getTexture()
    {
        if (!getSeenEnemy())
        {
            return this.texture = "/net/aetherteam/aether/client/sprites/mobs/sentrygolem/sentryGolem.png";
        }

        return this.texture = "/net/aetherteam/aether/client/sprites/mobs/sentrygolem/sentryGolem_red.png";
    }

    public int getMaxHealth()
    {
        return 20;
    }
}

