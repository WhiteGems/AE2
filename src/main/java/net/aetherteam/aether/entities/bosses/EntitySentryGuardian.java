package net.aetherteam.aether.entities.bosses;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Random;
import net.aetherteam.aether.Aether;
import net.aetherteam.aether.AetherCommonPlayerHandler;
import net.aetherteam.aether.AetherNameGen;
import net.aetherteam.aether.CommonProxy;
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
import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIMoveTwardsRestriction;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntitySentryGuardian extends EntityMiniBoss
    implements IAetherBoss
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
        this.tasks.addTask(1, new EntityAIMoveTwardsRestriction(this, this.moveSpeed));
        this.bossName = AetherNameGen.gen();
        setSize(2.25F, 2.5F);
        this.moveSpeed = 1.6F;
        this.health = getMaxHealth();

        if (!getHasBeenAttacked())
        {
            this.texture = "/net/aetherteam/aether/client/sprites/mobs/sentrygolemboss/sentryGolemBoss.png";
        }
        else
        {
            this.texture = "/net/aetherteam/aether/client/sprites/mobs/sentrygolemboss/sentryGolemBoss_red.png";
        }
    }

    public EntitySentryGuardian(World world, int x, int y, int z, int rad, int dir)
    {
        super(world);
        this.bossName = AetherNameGen.gen();
        setSize(2.25F, 2.5F);
        setPosition(x + 0.5D, y, z + 0.5D);
        this.health = 250;

        if (!getHasBeenAttacked())
        {
            this.texture = "/net/aetherteam/aether/client/sprites/mobs/sentrygolemboss/sentryGolemBoss.png";
        }
        else
        {
            this.texture = "/net/aetherteam/aether/client/sprites/mobs/sentrygolemboss/sentryGolemBoss_red.png";
        }
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
            this.texture = "/net/aetherteam/aether/client/sprites/mobs/sentrygolemboss/sentryGolemBoss_red.png";
            this.dataWatcher.updateObject(16, Byte.valueOf((byte)1));
        }
        else
        {
            this.texture = "/net/aetherteam/aether/client/sprites/mobs/sentrygolemboss/sentryGolemBoss.png";
            this.dataWatcher.updateObject(16, Byte.valueOf((byte)0));
        }
    }

    protected Entity findPlayerToAttack()
    {
        if (getHasBeenAttacked())
        {
            EntityPlayer var1 = this.worldObj.getClosestVulnerablePlayerToEntity(this, 16.0D);
            this.texture = "/net/aetherteam/aether/client/sprites/mobs/sentrygolemboss/sentryGolemBoss_red.png";
            return (var1 != null) && (canEntityBeSeen(var1)) ? var1 : null;
        }

        return null;
    }

    public void spawnSentry()
    {
        if ((!this.worldObj.isRemote) && (this.cappedAmount < 5))
        {
            EntitySentry sentry = new EntitySentry(this.worldObj, this.posX, this.posY, this.posZ);
            this.worldObj.spawnEntityInWorld(sentry);
            sentry.motionY = 1.0D;
            sentry.fallDistance = -100.0F;
            sentry.setAttackTarget(getAttackTarget());
            this.cappedAmount += 1;
            sentry.setParent(this);
            this.worldObj.playSoundAtEntity(this, "aemob.sentryGuardian.spawn", 2.0F, 1.0F);
        }
    }

    public void onDeath(DamageSource source)
    {
        this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, 0.3F, false);
        spawnSentry();

        if ((source.getEntity() instanceof EntityPlayer))
        {
            EntityPlayer attackingPlayer = (EntityPlayer)source.getEntity();
            Party party = PartyController.instance().getParty(PartyController.instance().getMember(attackingPlayer));
            Dungeon dungeon = DungeonHandler.instance().getInstanceAt(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ));

            if ((dungeon != null) && (party != null))
            {
                DungeonHandler.instance().addKey(dungeon, party, new DungeonKey(EnumKeyType.Guardian));
                PacketDispatcher.sendPacketToAllPlayers(AetherPacketHandler.sendDungeonKey(dungeon, party, EnumKeyType.Guardian));
            }
        }

        this.boss = new EntitySentryGuardian(this.worldObj);
        super.onDeath(source);
    }

    public boolean canDespawn()
    {
        return false;
    }

    public void onUpdate()
    {
        super.onUpdate();
        extinguish();
        this.jumpMovementFactor = 0.0F;

        if (this.health > 0)
        {
            double a = this.rand.nextFloat() - 0.5F;
            double b = this.rand.nextFloat();
            double c = this.rand.nextFloat() - 0.5F;
            double d = this.posX + a * b;
            double e = this.boundingBox.minY + b - 0.300000011920929D;
            double f = this.posZ + c * b;

            if (!getHasBeenAttacked())
            {
                this.worldObj.spawnParticle("reddust", d, e, f, -0.300000011920929D, 0.800000011920929D, 0.8999999761581421D);
            }
            else
            {
                this.worldObj.spawnParticle("reddust", d, e, f, 0.0D, 0.0D, 0.0D);
            }
        }

        if (this.chatTime > 0)
        {
            this.chatTime -= 1;
        }
    }

    public boolean attackEntityAsMob(Entity par1Entity)
    {
        this.attackTimer = 10;
        this.worldObj.setEntityState(this, (byte)4);
        boolean var2 = par1Entity.attackEntityFrom(DamageSource.causeMobDamage(this), 7 + this.rand.nextInt(15));

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

    public int func_82193_c(Entity par1Entity)
    {
        return 10;
    }

    public boolean isPotionApplicable(PotionEffect par1PotionEffect)
    {
        return par1PotionEffect.getPotionID() == Potion.poison.id ? false : super.isPotionApplicable(par1PotionEffect);
    }

    public boolean attackEntityFrom(DamageSource par1DamageSource, int par2)
    {
        Entity entity = par1DamageSource.getSourceOfDamage();
        Entity attacker = par1DamageSource.getEntity();

        if ((entity != null) && (par1DamageSource.isProjectile()))
        {
            if (((attacker instanceof EntityPlayer)) && (((EntityPlayer)attacker).cd() != null))
            {
                chatItUp((EntityPlayer)attacker, "Better switch to a sword, my " + ((EntityPlayer)attacker).cd().getItem().getItemDisplayName(((EntityPlayer)attacker).cd()) + " doesn't seem to affect it.");
                this.chatTime = 60;
            }

            return false;
        }

        if ((attacker instanceof EntityPlayer))
        {
            EntityPlayer player = (EntityPlayer)attacker;
            AetherCommonPlayerHandler handler = Aether.getPlayerBase(player);
            PartyMember member = PartyController.instance().getMember(player);
            Party party = PartyController.instance().getParty(member);
            Side side = FMLCommonHandler.instance().getEffectiveSide();

            if (handler != null)
            {
                boolean shouldSetBoss = true;

                if ((!player.isDead) && (shouldSetBoss))
                {
                    handler.setCurrentBoss(this);
                }
            }
        }

        setHasBeenAttacked(true);
        return super.attackEntityFrom(par1DamageSource, par2);
    }

    private void chatItUp(EntityPlayer player, String s)
    {
        if (this.chatTime <= 0)
        {
            Aether.proxy.displayMessage(player, s);
            this.chatTime = 60;
        }
    }

    public void onLivingUpdate()
    {
        if (this.attackTimer > 0)
        {
            this.attackTimer -= 1;
        }

        if (!this.worldObj.isRemote)
        {
            if ((this.rand.nextInt(100) == 1) && (getEntityToAttack() != null))
            {
                spawnSentry();
            }

            this.heightOffsetUpdateTime -= 1;

            if (this.heightOffsetUpdateTime <= 0)
            {
                this.heightOffsetUpdateTime = 100;
                this.heightOffset = (0.5F + (float)this.rand.nextGaussian() * 3.0F);
            }

            if ((getEntityToAttack() != null) && (getEntityToAttack().posY + getEntityToAttack().getEyeHeight() > this.posY + getEyeHeight() + this.heightOffset))
            {
                this.motionY += (0.700000011920929D - this.motionY) * 0.700000011920929D;
            }
        }

        if ((!this.onGround) && (this.motionY < 0.0D))
        {
            this.motionY *= 0.8D;
        }

        super.onLivingUpdate();
    }

    public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readEntityFromNBT(par1NBTTagCompound);
        setHasBeenAttacked(par1NBTTagCompound.getBoolean("HasBeenAttacked"));
    }

    public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeEntityToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setBoolean("HasBeenAttacked", getHasBeenAttacked());
    }

    protected String getDeathSound()
    {
        return "aemob.sentryGuardian.death";
    }

    protected String getLivingSound()
    {
        return "aemob.sentryGuardian.living";
    }

    protected String getHurtSound()
    {
        return "aemob.sentryGuardian.hit";
    }

    protected void fall(float par1)
    {
    }

    public int getMaxHealth()
    {
        return 250;
    }

    public int getBossMaxHP()
    {
        return getMaxHealth();
    }

    public int getBossEntityID()
    {
        return this.entityId;
    }

    public String getBossTitle()
    {
        return this.bossName + ", the Sentry Guardian";
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
        this.cappedAmount -= 1;
    }
}

