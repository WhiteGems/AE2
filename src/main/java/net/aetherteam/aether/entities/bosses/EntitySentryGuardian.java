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
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
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
    private int cappedAmount;

    public EntitySentryGuardian(World par1World)
    {
        super(par1World);
        this.isImmuneToFire = true;
        this.jumpMovementFactor = 0.0F;
        this.tasks.addTask(1, new EntityAIMoveTowardsRestriction(this, this.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111125_b()));
        this.bossName = AetherNameGen.gen();
        this.setSize(2.25F, 2.5F);
        this.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111128_a(1.600000023841858D);
        this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(250.0D);
        this.setEntityHealth(250.0F);
    }

    public EntitySentryGuardian(World world, int x, int y, int z, int rad, int dir)
    {
        super(world);
        this.bossName = AetherNameGen.gen();
        this.setSize(2.25F, 2.5F);
        this.setPosition((double)x + 0.5D, (double)y, (double)z + 0.5D);
        this.setEntityHealth(250.0F);
    }

    public void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(16, Byte.valueOf((byte)0));
    }

    public boolean getHasBeenAttacked()
    {
        return this.dataWatcher.getWatchableObjectByte(16) == 1;
    }

    public void setHasBeenAttacked(boolean attack)
    {
        if (attack)
        {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte)1));
        }
        else
        {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte)0));
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
        }
        else
        {
            EntityPlayer var1 = this.worldObj.getClosestVulnerablePlayerToEntity(this, 16.0D);
            return var1 != null && this.canEntityBeSeen(var1) ? var1 : null;
        }
    }

    public void spawnSentry()
    {
        if (!this.worldObj.isRemote && this.cappedAmount < 5)
        {
            EntitySentry sentry = new EntitySentry(this.worldObj, this.posX, this.posY, this.posZ);
            this.worldObj.spawnEntityInWorld(sentry);
            sentry.motionY = 1.0D;
            sentry.fallDistance = -100.0F;
            sentry.setAttackTarget(this.getAttackTarget());
            ++this.cappedAmount;
            sentry.setParent(this);
            this.worldObj.playSoundAtEntity(this, "aether:aemob.sentryGuardian.spawn", 2.0F, 1.0F);
        }
    }

    /**
     * Called when the mob's health reaches 0.
     */
    public void onDeath(DamageSource source)
    {
        this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, 0.3F, false);
        this.spawnSentry();

        if (source.getEntity() instanceof EntityPlayer)
        {
            EntityPlayer attackingPlayer = (EntityPlayer)source.getEntity();
            Party party = PartyController.instance().getParty(PartyController.instance().getMember(attackingPlayer));
            Dungeon dungeon = DungeonHandler.instance().getInstanceAt(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ));

            if (dungeon != null && party != null)
            {
                DungeonHandler.instance().addKey(dungeon, party, new DungeonKey(EnumKeyType.Guardian));
                PacketDispatcher.sendPacketToAllPlayers(AetherPacketHandler.sendDungeonKey(dungeon, party, EnumKeyType.Guardian));
            }
        }

        this.boss = new EntitySentryGuardian(this.worldObj);
        super.onDeath(source);
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

        if (this.func_110143_aJ() > 0.0F)
        {
            double a = (double)(this.rand.nextFloat() - 0.5F);
            double b = (double)this.rand.nextFloat();
            double c = (double)(this.rand.nextFloat() - 0.5F);
            double d = this.posX + a * b;
            double e = this.boundingBox.minY + b - 0.30000001192092896D;
            double f = this.posZ + c * b;

            if (!this.getHasBeenAttacked())
            {
                this.worldObj.spawnParticle("reddust", d, e, f, -0.30000001192092896D, 0.800000011920929D, 0.8999999761581421D);
            }
            else
            {
                this.worldObj.spawnParticle("reddust", d, e, f, 0.0D, 0.0D, 0.0D);
            }
        }

        if (this.chatTime > 0)
        {
            --this.chatTime;
        }
    }

    public boolean attackEntityAsMob(Entity par1Entity)
    {
        this.attackTimer = 10;
        this.worldObj.setEntityState(this, (byte)4);
        boolean var2 = par1Entity.attackEntityFrom(DamageSource.causeMobDamage(this), (float)(7 + this.rand.nextInt(15)));

        if (var2)
        {
            par1Entity.motionY += 0.4000000059604645D;
            par1Entity.motionZ += 0.4000000059604645D;
            par1Entity.motionX += 0.4000000059604645D;
            par1Entity.velocityChanged = true;
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
    public int getAttackStrength(Entity par1Entity)
    {
        return 10;
    }

    public boolean isPotionApplicable(PotionEffect par1PotionEffect)
    {
        return par1PotionEffect.getPotionID() == Potion.poison.id ? false : super.isPotionApplicable(par1PotionEffect);
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource source, float damage)
    {
        Entity entity = source.getSourceOfDamage();
        Entity attacker = source.getEntity();

        if (entity != null && source.isProjectile())
        {
            if (attacker instanceof EntityPlayer && ((EntityPlayer)attacker).getCurrentEquippedItem() != null)
            {
                this.chatItUp((EntityPlayer)attacker, "也许该换成剑攻击, 我的" + ((EntityPlayer)attacker).getCurrentEquippedItem().getItem().getItemDisplayName(((EntityPlayer)attacker).getCurrentEquippedItem()) + "对付不了这玩意儿!");
                this.chatTime = 60;
            }

            return false;
        }
        else
        {
            if (attacker instanceof EntityPlayer)
            {
                EntityPlayer player = (EntityPlayer)attacker;
                AetherCommonPlayerHandler handler = Aether.getPlayerBase(player);
                PartyMember member = PartyController.instance().getMember(player);
                Party party = PartyController.instance().getParty(member);
                Side side = FMLCommonHandler.instance().getEffectiveSide();
                System.out.println(handler);

                if (handler != null)
                {
                    boolean shouldSetBoss = true;

                    if (!player.isDead && shouldSetBoss)
                    {
                        handler.setCurrentBoss(this);
                    }
                }
            }

            this.setHasBeenAttacked(true);
            return super.attackEntityFrom(source, damage);
        }
    }

    private void chatItUp(EntityPlayer player, String s)
    {
        if (this.chatTime <= 0)
        {
            Aether.proxy.displayMessage(player, s);
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
                this.heightOffset = 0.5F + (float)this.rand.nextGaussian() * 3.0F;
            }

            if (this.getEntityToAttack() != null && this.getEntityToAttack().posY + (double)this.getEntityToAttack().getEyeHeight() > this.posY + (double)this.getEyeHeight() + (double)this.heightOffset)
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
    public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readEntityFromNBT(par1NBTTagCompound);
        this.setHasBeenAttacked(par1NBTTagCompound.getBoolean("HasBeenAttacked"));
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeEntityToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setBoolean("HasBeenAttacked", this.getHasBeenAttacked());
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected String getDeathSound()
    {
        return "aether:aemob.sentryGuardian.death";
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected String getLivingSound()
    {
        return "aether:aemob.sentryGuardian.living";
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected String getHurtSound()
    {
        return "aether:aemob.sentryGuardian.hit";
    }

    /**
     * Called when the mob is falling. Calculates and applies fall damage.
     */
    protected void fall(float par1) {}

    public int getBossMaxHP()
    {
        return 250;
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

    public void failedYou()
    {
        --this.cappedAmount;
    }
}
