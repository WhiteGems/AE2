package net.aetherteam.aether.entities.bosses;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.aetherteam.aether.Aether;
import net.aetherteam.aether.AetherCommonPlayerHandler;
import net.aetherteam.aether.AetherNameGen;
import net.aetherteam.aether.CommonProxy;
import net.aetherteam.aether.dungeons.Dungeon;
import net.aetherteam.aether.dungeons.DungeonHandler;
import net.aetherteam.aether.dungeons.keys.DungeonKey;
import net.aetherteam.aether.dungeons.keys.EnumKeyType;
import net.aetherteam.aether.enums.EnumBossType;
import net.aetherteam.aether.interfaces.IAetherBoss;
import net.aetherteam.aether.packets.AetherPacketHandler;
import net.aetherteam.aether.party.Party;
import net.aetherteam.aether.party.PartyController;
import net.aetherteam.aether.party.members.PartyMember;
import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntitySliderHostMimic extends EntityMiniBoss
    implements IAetherBoss
{
    public boolean hasBeenAttacked;
    public int eyeCap = 4;
    public List Eyes = new ArrayList();
    public int scareTime;
    public String bossName;
    public int sendDelay = 15;
    public int sendRespawnDelay = 10;
    private int chatTime;

    public EntitySliderHostMimic(World world)
    {
        super(world);
        setSize(2.0F, 2.5F);
        this.bossName = AetherNameGen.gen();
        this.isImmuneToFire = true;
        this.health = getMaxHealth();
        this.moveSpeed = 1.2F;
        this.hasBeenAttacked = false;
        this.scareTime = 0;
        this.texture = "/net/aetherteam/aether/client/sprites/mobs/host/hostblue.png";
    }

    public void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(16, Byte.valueOf((byte)0));
        this.dataWatcher.addObject(17, Byte.valueOf((byte)0));
    }

    public boolean isAwake()
    {
        return this.dataWatcher.getWatchableObjectByte(16) == 1;
    }

    public void setAwake(boolean awake)
    {
        if (awake)
        {
            scareItUp();
            this.worldObj.playSoundAtEntity(this, "aeboss.slider.awake", 2.5F, 1.0F / (this.rand.nextFloat() * 0.2F + 0.9F));
            this.dataWatcher.updateObject(16, Byte.valueOf((byte)1));
        }
        else
        {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte)0));
        }
    }

    public boolean isSendMode()
    {
        return this.dataWatcher.getWatchableObjectByte(17) == 1;
    }

    public void setSendMode(boolean sendMode)
    {
        if (sendMode)
        {
            this.dataWatcher.updateObject(17, Byte.valueOf((byte)1));
        }
        else
        {
            this.dataWatcher.updateObject(17, Byte.valueOf((byte)0));
        }
    }

    protected Entity findPlayerToAttack()
    {
        EntityPlayer var1 = this.worldObj.getClosestVulnerablePlayerToEntity(this, 8.5D);
        return (var1 != null) && (canEntityBeSeen(var1)) ? var1 : null;
    }

    protected String getLivingSound()
    {
        return "ambient.cave.cave";
    }

    protected String getHurtSound()
    {
        return "step.stone";
    }

    protected String getDeathSound()
    {
        return "aeboss.slider.die";
    }

    protected void playStepSound(int par1, int par2, int par3, int par4)
    {
        this.worldObj.playSoundAtEntity(this, "mob.cow.step", 0.15F, 1.0F);
    }

    protected void updateFallState(double par1, boolean par3)
    {
        if (isAwake())
        {
            super.updateFallState(par1, par3);
        }
    }

    protected void fall(float par1)
    {
        if (isAwake())
        {
            super.fall(par1);
        }
    }

    public boolean canDespawn()
    {
        return false;
    }

    public void onUpdate()
    {
        super.onUpdate();
        extinguish();

        if (this.chatTime >= 0)
        {
            this.chatTime -= 1;
        }

        if (!isAwake())
        {
            this.texture = "/net/aetherteam/aether/client/sprites/bosses/slider/sliderSleep.png";
            this.motionX = 0.0D;
            this.motionY = 0.0D;
            this.motionZ = 0.0D;
            this.jumpMovementFactor = 0.0F;
            this.renderYawOffset = (this.rotationPitch = this.rotationYaw = 0.0F);
        }

        EntityPlayer entityplayer = this.worldObj.getClosestPlayerToEntity(this, 8.5D);

        if (this.entityToAttack == null)
        {
            if ((entityplayer != null) && (canEntityBeSeen(entityplayer)) && (!entityplayer.isDead) && (!entityplayer.capabilities.isCreativeMode))
            {
                this.entityToAttack = entityplayer;
                setSendMode(true);
            }
        }

        if ((this.entityToAttack != null) && ((this.entityToAttack instanceof EntityLiving)) && (canEntityBeSeen(this.entityToAttack)) && (!this.entityToAttack.isDead))
        {
            faceEntity(this.entityToAttack, 10.0F, 10.0F);

            if (!isAwake())
            {
                setAwake(true);
            }

            if (!this.hasBeenAttacked)
            {
                this.hasBeenAttacked = true;
            }

            if (isSendMode())
            {
                this.motionX = 0.0D;
                this.motionY = 0.0D;
                this.motionZ = 0.0D;
                this.jumpMovementFactor = 0.0F;
                this.renderYawOffset = (this.rotationPitch = this.rotationYaw = 0.0F);

                if (this.Eyes.size() < this.eyeCap)
                {
                    if (this.sendDelay <= 0)
                    {
                        if (!this.worldObj.isRemote)
                        {
                            sendEye((EntityLiving)this.entityToAttack);
                        }
                    }
                }
                else if (this.sendRespawnDelay <= 0)
                {
                    if (!this.worldObj.isRemote)
                    {
                        sendEye((EntityLiving)this.entityToAttack);
                        this.sendRespawnDelay = 100;
                    }
                }
                else
                {
                    setSendMode(false);
                }
            }
        }
        else
        {
            this.entityToAttack = null;
            this.hasBeenAttacked = false;
            killEyes();
            setAwake(false);
            setSendMode(false);
        }

        if ((!this.hasBeenAttacked) && (isAwake()))
        {
            this.texture = "/net/aetherteam/aether/client/sprites/mobs/host/hostblue.png";
            killEyes();
        }

        if (this.Eyes.size() > this.eyeCap)
        {
            ((Entity)this.Eyes.remove(0)).setDead();
        }

        if ((this.hasBeenAttacked) || ((this.entityToAttack != null) && (canEntityBeSeen(this.entityToAttack))))
        {
            this.texture = "/net/aetherteam/aether/client/sprites/mobs/host/hostred.png";
        }

        if (this.scareTime > 0)
        {
            this.scareTime -= 1;
        }

        if (this.sendDelay > 0)
        {
            this.sendDelay -= 1;
        }

        if (this.sendRespawnDelay > 0)
        {
            this.sendRespawnDelay -= 1;
        }
    }

    protected void attackEntity(Entity entity, float f)
    {
        EntityLiving target = (EntityLiving)entity;

        if ((f < 8.5F) && (canEntityBeSeen(target)))
        {
            double d = entity.posX - this.posX;
            double d1 = entity.posZ - this.posZ;

            if (target != null)
            {
                if ((target.isDead) || (!canEntityBeSeen(target)))
                {
                    target = null;
                }

                if ((this.Eyes.size() <= 0) && (canEntityBeSeen(target)))
                {
                    setSendMode(true);
                }
            }

            this.rotationYaw = ((float)(Math.atan2(d1, d) * 180.0D / Math.PI) - 90.0F);
            this.hasAttacked = true;
        }
    }

    public void sendEye(EntityLiving target)
    {
        while (this.Eyes.size() > this.eyeCap)
        {
            ((Entity)this.Eyes.remove(0)).setDead();
        }

        this.hasBeenAttacked = true;

        if (!isAwake())
        {
            setAwake(true);
        }

        EntityHostEye eye = new EntityHostEye(this.worldObj, this.posX - 1.5D, this.posY + 1.5D, this.posZ - 1.5D, this.rotationYaw, this.rotationPitch, this, target);
        this.worldObj.spawnEntityInWorld(eye);
        this.worldObj.playSoundAtEntity(this, "aeboss.slider.awake", 2.5F, 1.0F / (this.rand.nextFloat() * 0.2F + 0.9F));
        this.Eyes.add(eye);
        this.sendDelay = 30;
    }

    public void killEyes()
    {
        while (this.Eyes.size() != 0)
        {
            ((Entity)this.Eyes.remove(0)).setDead();
        }
    }

    private void scareItUp()
    {
        if (this.scareTime <= 0)
        {
            this.worldObj.playSoundAtEntity(this, "aemob.sentryGolem.creepySeen", 5.0F, 1.0F);
            this.scareTime = 2000;
        }
    }

    public void addVelocity(double d, double d1, double d2)
    {
        if ((isAwake()) && (!isSendMode()))
        {
            super.addVelocity(d, d1, d2);
        }
    }

    public void knockBack(Entity entity, int i, double d, double d1)
    {
        if ((isAwake()) && (!isSendMode()))
        {
            super.knockBack(entity, i, d, d1);
        }
    }

    public boolean attackEntityFrom(DamageSource par1DamageSource, int par2)
    {
        this.hasBeenAttacked = true;
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
        this.hasBeenAttacked = nbttagcompound.getBoolean("HasBeenAttacked");
        killEyes();
        setAwake(nbttagcompound.getBoolean("Awake"));
        setSendMode(nbttagcompound.getBoolean("SendMode"));
    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readEntityFromNBT(nbttagcompound);
        nbttagcompound.setBoolean("HasBeenAttacked", this.hasBeenAttacked);
        nbttagcompound.setBoolean("Awake", isAwake());
        nbttagcompound.setBoolean("SendMode", isSendMode());
    }

    public void onDeath(DamageSource source)
    {
        this.boss = new EntitySliderHostMimic(this.worldObj);

        if ((source.getEntity() instanceof EntityPlayer))
        {
            EntityPlayer attackingPlayer = (EntityPlayer)source.getEntity();
            Party party = PartyController.instance().getParty(PartyController.instance().getMember(attackingPlayer));
            Dungeon dungeon = DungeonHandler.instance().getInstanceAt(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ));

            if ((dungeon != null) && (party != null))
            {
                DungeonHandler.instance().addKey(dungeon, party, new DungeonKey(EnumKeyType.Host));
                PacketDispatcher.sendPacketToAllPlayers(AetherPacketHandler.sendDungeonKey(dungeon, party, EnumKeyType.Host));
            }
        }

        killEyes();
        super.onDeath(source);
    }

    public int getMaxHealth()
    {
        return 175;
    }

    public int getBossMaxHP()
    {
        return 175;
    }

    public int getBossEntityID()
    {
        return this.entityId;
    }

    public String getBossTitle()
    {
        return this.bossName + ", the Slider Host Mimic";
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

