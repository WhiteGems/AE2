package net.aetherteam.aether.entities.bosses;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import java.util.ArrayList;
import java.util.List;
import net.aetherteam.aether.Aether;
import net.aetherteam.aether.AetherCommonPlayerHandler;
import net.aetherteam.aether.AetherNameGen;
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
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntitySliderHostMimic extends EntityMiniBoss implements IAetherBoss
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
        this.setSize(2.0F, 2.5F);
        this.bossName = AetherNameGen.gen();
        this.isImmuneToFire = true;
        this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(175.0D);
        this.setEntityHealth(175.0F);
        this.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111128_a(1.2000000476837158D);
        this.hasBeenAttacked = false;
        this.scareTime = 0;
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
            this.scareItUp();
            this.worldObj.playSoundAtEntity(this, "aether:aeboss.slider.awake", 2.5F, 1.0F / (this.rand.nextFloat() * 0.2F + 0.9F));
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

    /**
     * Finds the closest player within 16 blocks to attack, or null if this Entity isn't interested in attacking
     * (Animals, Spiders at day, peaceful PigZombies).
     */
    protected Entity findPlayerToAttack()
    {
        EntityPlayer var1 = this.worldObj.getClosestVulnerablePlayerToEntity(this, 8.5D);
        return var1 != null && this.canEntityBeSeen(var1) ? var1 : null;
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected String getLivingSound()
    {
        return "ambient.cave.cave";
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected String getHurtSound()
    {
        return "step.stone";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected String getDeathSound()
    {
        return "aether:aeboss.slider.die";
    }

    /**
     * Plays step sound at given x, y, z for the entity
     */
    protected void playStepSound(int par1, int par2, int par3, int par4)
    {
        this.worldObj.playSoundAtEntity(this, "mob.cow.step", 0.15F, 1.0F);
    }

    /**
     * Takes in the distance the entity has fallen this tick and whether its on the ground to update the fall distance
     * and deal fall damage if landing on the ground.  Args: distanceFallenThisTick, onGround
     */
    protected void updateFallState(double par1, boolean par3)
    {
        if (this.isAwake())
        {
            super.updateFallState(par1, par3);
        }
    }

    /**
     * Called when the mob is falling. Calculates and applies fall damage.
     */
    protected void fall(float par1)
    {
        if (this.isAwake())
        {
            super.fall(par1);
        }
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

        if (this.chatTime >= 0)
        {
            --this.chatTime;
        }

        if (!this.isAwake())
        {
            this.motionX = 0.0D;
            this.motionY = 0.0D;
            this.motionZ = 0.0D;
            this.jumpMovementFactor = 0.0F;
            this.renderYawOffset = this.rotationPitch = this.rotationYaw = 0.0F;
        }

        EntityPlayer entityplayer = this.worldObj.getClosestPlayerToEntity(this, 8.5D);

        if (this.entityToAttack == null && entityplayer != null && this.canEntityBeSeen(entityplayer) && !entityplayer.isDead && !entityplayer.capabilities.isCreativeMode)
        {
            this.entityToAttack = entityplayer;
            this.setSendMode(true);
        }

        if (this.entityToAttack != null && this.entityToAttack instanceof EntityLivingBase && this.canEntityBeSeen(this.entityToAttack) && !this.entityToAttack.isDead)
        {
            this.faceEntity(this.entityToAttack, 10.0F, 10.0F);

            if (!this.isAwake())
            {
                this.setAwake(true);
            }

            if (!this.hasBeenAttacked)
            {
                this.hasBeenAttacked = true;
            }

            if (this.isSendMode())
            {
                this.motionX = 0.0D;
                this.motionY = 0.0D;
                this.motionZ = 0.0D;
                this.jumpMovementFactor = 0.0F;
                this.renderYawOffset = this.rotationPitch = this.rotationYaw = 0.0F;

                if (this.Eyes.size() < this.eyeCap)
                {
                    if (this.sendDelay <= 0 && !this.worldObj.isRemote)
                    {
                        this.sendEye((EntityLivingBase)this.entityToAttack);
                    }
                }
                else if (this.sendRespawnDelay <= 0)
                {
                    if (!this.worldObj.isRemote)
                    {
                        this.sendEye((EntityLivingBase)this.entityToAttack);
                        this.sendRespawnDelay = 100;
                    }
                }
                else
                {
                    this.setSendMode(false);
                }
            }
        }
        else
        {
            this.entityToAttack = null;
            this.hasBeenAttacked = false;
            this.killEyes();
            this.setAwake(false);
            this.setSendMode(false);
        }

        if (!this.hasBeenAttacked && this.isAwake())
        {
            this.killEyes();
        }

        if (this.Eyes.size() > this.eyeCap)
        {
            ((Entity)this.Eyes.remove(0)).setDead();
        }

        if (this.scareTime > 0)
        {
            --this.scareTime;
        }

        if (this.sendDelay > 0)
        {
            --this.sendDelay;
        }

        if (this.sendRespawnDelay > 0)
        {
            --this.sendRespawnDelay;
        }
    }

    /**
     * Basic mob attack. Default to touch of death in EntityCreature. Overridden by each mob to define their attack.
     */
    protected void attackEntity(Entity entity, float f)
    {
        if (entity instanceof EntityLivingBase)
        {
            EntityLivingBase target = (EntityLivingBase)entity;

            if (f < 8.5F && this.canEntityBeSeen(target))
            {
                double d = entity.posX - this.posX;
                double d1 = entity.posZ - this.posZ;

                if (target != null)
                {
                    if (target.isDead || !this.canEntityBeSeen(target))
                    {
                        target = null;
                    }

                    if (this.Eyes.size() <= 0 && this.canEntityBeSeen(target))
                    {
                        this.setSendMode(true);
                    }
                }

                this.rotationYaw = (float)(Math.atan2(d1, d) * 180.0D / Math.PI) - 90.0F;
                this.hasAttacked = true;
            }
        }
    }

    public void sendEye(EntityLivingBase target)
    {
        while (this.Eyes.size() > this.eyeCap)
        {
            ((Entity)this.Eyes.remove(0)).setDead();
        }

        this.hasBeenAttacked = true;

        if (!this.isAwake())
        {
            this.setAwake(true);
        }

        EntityHostEye eye = new EntityHostEye(this.worldObj, this.posX - 1.5D, this.posY + 1.5D, this.posZ - 1.5D, this.rotationYaw, this.rotationPitch, this, target);
        this.worldObj.spawnEntityInWorld(eye);
        this.worldObj.playSoundAtEntity(this, "aether:aeboss.slider.awake", 2.5F, 1.0F / (this.rand.nextFloat() * 0.2F + 0.9F));
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
            this.worldObj.playSoundAtEntity(this, "aether:aemob.sentryGolem.creepySeen", 5.0F, 1.0F);
            this.scareTime = 2000;
        }
    }

    /**
     * Adds to the current velocity of the entity. Args: x, y, z
     */
    public void addVelocity(double d, double d1, double d2)
    {
        if (this.isAwake() && !this.isSendMode())
        {
            super.addVelocity(d, d1, d2);
        }
    }

    /**
     * Sets that this entity has been attacked.
     */
    public void setBeenAttacked()
    {
        if (this.isAwake() && !this.isSendMode())
        {
            super.setBeenAttacked();
        }
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource source, float damage)
    {
        this.hasBeenAttacked = true;
        Entity entity = source.getSourceOfDamage();
        Entity attacker = source.getEntity();

        if (entity != null && source.isProjectile())
        {
            if (attacker instanceof EntityPlayer && ((EntityPlayer)attacker).getCurrentEquippedItem() != null)
            {
                this.chatItUp((EntityPlayer)attacker, "Better switch to a sword, my " + ((EntityPlayer)attacker).getCurrentEquippedItem().getItem().getItemDisplayName(((EntityPlayer)attacker).getCurrentEquippedItem()) + " doesn\'t seem to affect it.");
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

                if (handler != null)
                {
                    boolean shouldSetBoss = true;

                    if (!player.isDead && shouldSetBoss)
                    {
                        handler.setCurrentBoss(this);
                    }
                }
            }

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
        this.hasBeenAttacked = nbttagcompound.getBoolean("HasBeenAttacked");
        this.killEyes();
        this.setAwake(nbttagcompound.getBoolean("Awake"));
        this.setSendMode(nbttagcompound.getBoolean("SendMode"));
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readEntityFromNBT(nbttagcompound);
        nbttagcompound.setBoolean("HasBeenAttacked", this.hasBeenAttacked);
        nbttagcompound.setBoolean("Awake", this.isAwake());
        nbttagcompound.setBoolean("SendMode", this.isSendMode());
    }

    /**
     * Called when the mob's health reaches 0.
     */
    public void onDeath(DamageSource source)
    {
        this.boss = new EntitySliderHostMimic(this.worldObj);

        if (source.getEntity() instanceof EntityPlayer)
        {
            EntityPlayer attackingPlayer = (EntityPlayer)source.getEntity();
            Party party = PartyController.instance().getParty(PartyController.instance().getMember(attackingPlayer));
            Dungeon dungeon = DungeonHandler.instance().getInstanceAt(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ));

            if (dungeon != null && party != null)
            {
                DungeonHandler.instance().addKey(dungeon, party, new DungeonKey(EnumKeyType.Host));
                PacketDispatcher.sendPacketToAllPlayers(AetherPacketHandler.sendDungeonKey(dungeon, party, EnumKeyType.Host));
            }
        }

        this.killEyes();
        super.onDeath(source);
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
