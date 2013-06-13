package net.aetherteam.aether.entities.bosses;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.aetherteam.aether.Aether;
import net.aetherteam.aether.AetherCommonPlayerHandler;
import net.aetherteam.aether.AetherNameGen;
import net.aetherteam.aether.dungeons.Dungeon;
import net.aetherteam.aether.dungeons.DungeonHandler;
import net.aetherteam.aether.dungeons.keys.DungeonKey;
import net.aetherteam.aether.dungeons.keys.EnumKeyType;
import net.aetherteam.aether.entities.EntitySentry;
import net.aetherteam.aether.enums.EnumBossType;
import net.aetherteam.aether.interfaces.IAetherBoss;
import net.aetherteam.aether.packets.AetherPacketHandler;
import net.aetherteam.aether.party.Party;
import net.aetherteam.aether.party.PartyController;
import net.aetherteam.aether.party.members.PartyMember;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIMoveTwardsRestriction;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntitySentryGuardian extends EntityMiniBoss implements IAetherBoss
{
    private String bossName;
    private int heightOffsetUpdateTime;
    private float heightOffset = 0.5F;
    public int chatTime;
    private int attackTimer;

    public EntitySentryGuardian(World var1)
    {
        super(var1);
        this.isImmuneToFire = true;
        this.jumpMovementFactor = 0.0F;
        this.tasks.addTask(1, new EntityAIMoveTwardsRestriction(this, this.moveSpeed));
        this.bossName = AetherNameGen.gen();
        this.setSize(2.25F, 2.5F);
        this.moveSpeed = 1.6F;
        this.health = this.getMaxHealth();

        if (!this.getHasBeenAttacked())
        {
            this.texture = "/net/aetherteam/aether/client/sprites/mobs/sentrygolemboss/sentryGolemBoss.png";
        } else
        {
            this.texture = "/net/aetherteam/aether/client/sprites/mobs/sentrygolemboss/sentryGolemBoss_red.png";
        }
    }

    public EntitySentryGuardian(World var1, int var2, int var3, int var4, int var5, int var6)
    {
        super(var1);
        this.bossName = AetherNameGen.gen();
        this.setSize(2.25F, 2.5F);
        this.setPosition((double) var2 + 0.5D, (double) var3, (double) var4 + 0.5D);
        this.health = 250;

        if (!this.getHasBeenAttacked())
        {
            this.texture = "/net/aetherteam/aether/client/sprites/mobs/sentrygolemboss/sentryGolemBoss.png";
        } else
        {
            this.texture = "/net/aetherteam/aether/client/sprites/mobs/sentrygolemboss/sentryGolemBoss_red.png";
        }
    }

    public void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(16, Byte.valueOf((byte) 0));
    }

    public boolean getHasBeenAttacked()
    {
        return this.dataWatcher.getWatchableObjectByte(16) == 1;
    }

    public void setHasBeenAttacked(boolean var1)
    {
        if (var1)
        {
            this.texture = "/net/aetherteam/aether/client/sprites/mobs/sentrygolemboss/sentryGolemBoss_red.png";
            this.dataWatcher.updateObject(16, Byte.valueOf((byte) 1));
        } else
        {
            this.texture = "/net/aetherteam/aether/client/sprites/mobs/sentrygolemboss/sentryGolemBoss.png";
            this.dataWatcher.updateObject(16, Byte.valueOf((byte) 0));
        }
    }

    /**
     * Finds the closest player within 16 blocks to attack, or null if this Entity isn't interested in attacking
     * (Animals, Spiders at day, peaceful PigZombies).
     */
    protected Entity findPlayerToAttack()
    {
        if (!this.getHasBeenAttacked())
        {
            return null;
        } else
        {
            EntityPlayer var1 = this.worldObj.getClosestVulnerablePlayerToEntity(this, 16.0D);
            this.texture = "/net/aetherteam/aether/client/sprites/mobs/sentrygolemboss/sentryGolemBoss_red.png";
            return var1 != null && this.canEntityBeSeen(var1) ? var1 : null;
        }
    }

    public void spawnSentry()
    {
        if (!this.worldObj.isRemote)
        {
            EntitySentry var1 = new EntitySentry(this.worldObj, this.posX, this.posY, this.posZ);
            this.worldObj.spawnEntityInWorld(var1);
            var1.motionY = 1.0D;
            var1.fallDistance = -100.0F;
            var1.setAttackTarget(this.getAttackTarget());
            this.worldObj.playSoundAtEntity(this, "aemob.sentryGuardian.spawn", 2.0F, 1.0F);
        }
    }

    /**
     * Called when the mob's health reaches 0.
     */
    public void onDeath(DamageSource var1)
    {
        this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, 0.3F, false);
        this.spawnSentry();

        if (var1.getEntity() instanceof EntityPlayer)
        {
            EntityPlayer var2 = (EntityPlayer) var1.getEntity();
            Party var3 = PartyController.instance().getParty(PartyController.instance().getMember(var2));
            Dungeon var4 = DungeonHandler.instance().getInstanceAt(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ));

            if (var4 != null && var3 != null)
            {
                DungeonHandler.instance().addKey(var4, var3, new DungeonKey(EnumKeyType.Guardian));
                PacketDispatcher.sendPacketToAllPlayers(AetherPacketHandler.sendDungeonKey(var4, var3, EnumKeyType.Guardian));
            }
        }

        this.boss = new EntitySentryGuardian(this.worldObj);
        super.onDeath(var1);
    }

    /**
     * Determines if an entity can be despawned, used on idle far away entities
     */
    public boolean canDespawn()
    {
        return false;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();
        this.extinguish();
        this.jumpMovementFactor = 0.0F;

        if (this.health > 0)
        {
            double var1 = (double) (this.rand.nextFloat() - 0.5F);
            double var3 = (double) this.rand.nextFloat();
            double var5 = (double) (this.rand.nextFloat() - 0.5F);
            double var7 = this.posX + var1 * var3;
            double var9 = this.boundingBox.minY + var3 - 0.30000001192092896D;
            double var11 = this.posZ + var5 * var3;

            if (!this.getHasBeenAttacked())
            {
                this.worldObj.spawnParticle("reddust", var7, var9, var11, -0.30000001192092896D, 0.800000011920929D, 0.8999999761581421D);
            } else
            {
                this.worldObj.spawnParticle("reddust", var7, var9, var11, 0.0D, 0.0D, 0.0D);
            }
        }

        if (this.chatTime > 0)
        {
            --this.chatTime;
        }
    }

    public boolean attackEntityAsMob(Entity var1)
    {
        this.attackTimer = 10;
        this.worldObj.setEntityState(this, (byte) 4);
        boolean var2 = var1.attackEntityFrom(DamageSource.causeMobDamage(this), 7 + this.rand.nextInt(15));

        if (var2)
        {
            var1.motionY += 0.4000000059604645D;
            var1.motionZ += 0.4000000059604645D;
            var1.motionX += 0.4000000059604645D;
        }

        this.worldObj.playSoundAtEntity(this, "mob.irongolem.throw", 1.0F, 1.0F);
        return var2;
    }

    @SideOnly(Side.CLIENT)
    public int getAttackTimer()
    {
        return this.attackTimer;
    }

    /**
     * Returns the amount of damage a mob should deal.
     */
    public int getAttackStrength(Entity var1)
    {
        return 10;
    }

    public boolean isPotionApplicable(PotionEffect var1)
    {
        return var1.getPotionID() == Potion.poison.id ? false : super.isPotionApplicable(var1);
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource var1, int var2)
    {
        Entity var3 = var1.getSourceOfDamage();
        Entity var4 = var1.getEntity();

        if (var3 != null && var1.isProjectile())
        {
            if (var4 instanceof EntityPlayer && ((EntityPlayer) var4).getCurrentEquippedItem() != null)
            {
                this.chatItUp((EntityPlayer) var4, "也许该换成剑攻击, 我的" + ((EntityPlayer) var4).getCurrentEquippedItem().getItem().getItemDisplayName(((EntityPlayer) var4).getCurrentEquippedItem()) + "对付不了这玩意儿!");
                this.chatTime = 60;
            }

            return false;
        } else
        {
            if (var4 instanceof EntityPlayer)
            {
                EntityPlayer var5 = (EntityPlayer) var4;
                AetherCommonPlayerHandler var6 = Aether.getPlayerBase(var5);
                PartyMember var7 = PartyController.instance().getMember(var5);
                Party var8 = PartyController.instance().getParty(var7);
                Side var9 = FMLCommonHandler.instance().getEffectiveSide();

                if (var6 != null)
                {
                    boolean var10 = true;

                    if (!var5.isDead && var10)
                    {
                        var6.setCurrentBoss(this);
                    }
                }
            }

            this.setHasBeenAttacked(true);
            return super.attackEntityFrom(var1, var2);
        }
    }

    private void chatItUp(EntityPlayer var1, String var2)
    {
        if (this.chatTime <= 0)
        {
            Aether.proxy.displayMessage(var1, var2);
            this.chatTime = 60;
        }
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate()
    {
        if (this.attackTimer > 0)
        {
            --this.attackTimer;
        }

        if (!this.worldObj.isRemote)
        {
            if (this.rand.nextInt(100) == 1 && this.getEntityToAttack() != null)
            {
                this.spawnSentry();
            }

            --this.heightOffsetUpdateTime;

            if (this.heightOffsetUpdateTime <= 0)
            {
                this.heightOffsetUpdateTime = 100;
                this.heightOffset = 0.5F + (float) this.rand.nextGaussian() * 3.0F;
            }

            if (this.getEntityToAttack() != null && this.getEntityToAttack().posY + (double) this.getEntityToAttack().getEyeHeight() > this.posY + (double) this.getEyeHeight() + (double) this.heightOffset)
            {
                this.motionY += (0.700000011920929D - this.motionY) * 0.700000011920929D;
            }
        }

        if (!this.onGround && this.motionY < 0.0D)
        {
            this.motionY *= 0.8D;
        }

        super.onLivingUpdate();
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound var1)
    {
        super.readEntityFromNBT(var1);
        this.setHasBeenAttacked(var1.getBoolean("HasBeenAttacked"));
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound var1)
    {
        super.writeEntityToNBT(var1);
        var1.setBoolean("HasBeenAttacked", this.getHasBeenAttacked());
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected String getDeathSound()
    {
        return "aemob.sentryGuardian.death";
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected String getLivingSound()
    {
        return "aemob.sentryGuardian.living";
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected String getHurtSound()
    {
        return "aemob.sentryGuardian.hit";
    }

    /**
     * Called when the mob is falling. Calculates and applies fall damage.
     */
    protected void fall(float var1)
    {}

    public int getMaxHealth()
    {
        return 250;
    }

    public int getBossMaxHP()
    {
        return this.getMaxHealth();
    }

    public int getBossEntityID()
    {
        return this.entityId;
    }

    public String getBossTitle()
    {
        return "守卫者:" + this.bossName;
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
