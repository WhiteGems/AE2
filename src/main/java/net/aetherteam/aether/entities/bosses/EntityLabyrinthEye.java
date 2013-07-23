package net.aetherteam.aether.entities.bosses;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import java.util.List;
import net.aetherteam.aether.Aether;
import net.aetherteam.aether.AetherCommonPlayerHandler;
import net.aetherteam.aether.AetherNameGen;
import net.aetherteam.aether.blocks.AetherBlocks;
import net.aetherteam.aether.dungeons.Dungeon;
import net.aetherteam.aether.dungeons.DungeonHandler;
import net.aetherteam.aether.dungeons.keys.DungeonKey;
import net.aetherteam.aether.dungeons.keys.EnumKeyType;
import net.aetherteam.aether.entities.ai.AIEntityArrowAttackCog;
import net.aetherteam.aether.interfaces.IAetherBoss;
import net.aetherteam.aether.packets.AetherPacketHandler;
import net.aetherteam.aether.party.Party;
import net.aetherteam.aether.party.PartyController;
import net.aetherteam.aether.party.members.PartyMember;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityLabyrinthEye extends EntityMiniBoss implements IAetherBoss, IRangedAttackMob
{
    public boolean isAwake;
    public String bossName;
    private EntityLiving target;
    public int cogTimer;
    private int heightOffsetUpdateTime;
    private float heightOffset = 0.5F;
    private int chatTime;
    public int timeUntilShoot = 30;
    private boolean[] stageDone = new boolean[13];
    public int courseChangeCooldown;
    public double waypointX;
    public double waypointY;
    public double waypointZ;
    private Entity targetedEntity;
    public int prevAttackCounter;
    public int attackCounter;
    public float sinage;
    int xpov = 1;
    int zpov = 1;
    private double targetX;
    private double targetY;
    private double targetZ;
    private int updateTime;
    private int stage;

    public EntityLabyrinthEye(World var1)
    {
        super(var1);
        this.isImmuneToFire = true;
        this.moveSpeed = 0.5F;
        this.tasks.addTask(4, new AIEntityArrowAttackCog(this, this.moveSpeed, 60, 10.0F));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(6, new EntityAILookIdle(this));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 16.0F, 0, true));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
        this.bossName = AetherNameGen.gen();
        this.texture = "/net/aetherteam/aether/client/sprites/mobs/cogboss/cogsleep.png";
        this.setSize(2.0F, 2.0F);

        for (int var2 = 0; var2 < 12; ++var2)
        {
            this.stageDone[var2] = false;
        }
    }

    public void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(16, Byte.valueOf((byte)0));
        this.dataWatcher.addObject(17, Integer.valueOf(0));
    }

    /**
     * Returns true if the newer Entity AI code should be run
     */
    public boolean isAIEnabled()
    {
        return true;
    }

    /**
     * Determines if an entity can be despawned, used on idle far away entities
     */
    public boolean canDespawn()
    {
        return false;
    }

    private boolean isCourseTraversable(double var1, double var3, double var5, double var7)
    {
        double var9 = (this.waypointX - this.posX) / var7;
        double var11 = (this.waypointY - this.posY) / var7;
        double var13 = (this.waypointZ - this.posZ) / var7;
        AxisAlignedBB var15 = this.boundingBox.copy();

        for (int var16 = 1; (double)var16 < var7; ++var16)
        {
            var15.offset(var9, var11, var13);

            if (this.worldObj.getCollidingBoundingBoxes(this, var15).size() > 0)
            {
                return false;
            }
        }

        return true;
    }

    private void orbitPlayer()
    {
        double var1 = this.waypointX - this.posX;
        double var3 = this.waypointY - this.posY;
        double var5 = this.waypointZ - this.posZ;
        double var7 = (double)MathHelper.sqrt_double(var1 * var1 + var3 * var3 + var5 * var5);
        double var9 = this.targetedEntity.getDistanceSqToEntity(this);
        double var11 = this.targetedEntity.posX - this.posX;
        double var13 = this.targetedEntity.boundingBox.minY + (double)(this.targetedEntity.height / 2.0F) - (this.posY + (double)(this.height / 2.0F));
        double var15 = this.targetedEntity.posZ - this.posZ;
        this.renderYawOffset = this.rotationYaw = -((float)Math.atan2(var11, var15)) * 180.0F / (float)Math.PI;
        double var17 = (double)this.rand.nextFloat() * 10.0D;
        double var19 = (double)MathHelper.sqrt_double(196.0D - var17 * var17);
        double var21 = (double)this.rand.nextFloat() * var19;
        double var23 = (double)MathHelper.sqrt_double(var19 * var19 - var21 * var21);

        if (this.rand.nextInt(8) == 0)
        {
            this.xpov = this.rand.nextBoolean() ? -1 : 1;
            this.zpov = this.rand.nextBoolean() ? -1 : 1;
        }

        this.waypointX = this.targetedEntity.posX + var21 * (double)this.xpov;
        this.waypointY = this.targetedEntity.posY + var17 + 5.0D;
        this.waypointZ = this.targetedEntity.posZ + var23 * (double)this.zpov;

        if (this.isCourseTraversable(this.waypointX, this.waypointY, this.waypointZ, var7))
        {
            float var25 = 0.5F;
            this.motionX = var1 / var7 * (double)var25;
            this.motionY = var3 / var7 * (double)var25;
            this.motionZ = var5 / var7 * (double)var25;
        }
    }

    /**
     * Basic mob attack. Default to touch of death in EntityCreature. Overridden by each mob to define their attack.
     */
    protected void attackEntity(Entity var1, float var2)
    {
        if (var1 instanceof EntityLiving)
        {
            this.target = (EntityLiving)var1;

            if (var2 < 10.0F)
            {
                double var3 = var1.posX - this.posX;
                double var5 = var1.posZ - this.posZ;
                int var7;

                if (this.target != null)
                {
                    if (!this.target.isDead && (double)this.target.getDistanceToEntity(this) <= 12.0D)
                    {
                        if (this.getAwake() && this.attackTime >= this.timeUntilShoot)
                        {
                            this.attackEntityWithRangedAttack(this.target);
                        }
                    }
                    else
                    {
                        this.target = null;
                        this.attackTime = 0;
                    }

                    if (this.attackTime >= this.timeUntilShoot && this.canEntityBeSeen(this.target))
                    {
                        this.attackTime = -10;
                    }

                    if (this.attackTime < this.timeUntilShoot)
                    {
                        this.attackTime += 2;
                    }

                    for (var7 = 0; var7 < 13; ++var7)
                    {
                        if (this.isBossStage(var7) && !this.stageDone[var7])
                        {
                            this.setStage(var7);
                            this.spawnLargeCog(this.target, var7);
                        }
                    }

                    if (this.stage == 12)
                    {
                        this.setSize(0.5F, 0.5F);
                    }
                }

                this.rotationYaw = (float)(Math.atan2(var5, var3) * 180.0D / Math.PI) - 90.0F;
                var7 = MathHelper.floor_double(var1.posX);
                int var8 = MathHelper.floor_double(var1.posY);
                int var9 = MathHelper.floor_double(var1.posZ);
                this.worldObj.setBlock(var7, var8, var9, AetherBlocks.ColdFire.blockID);
            }
        }
    }

    private void setStage(int var1)
    {
        this.dataWatcher.updateObject(17, Integer.valueOf(var1));
        this.stage = var1;
    }

    public int getStage()
    {
        return this.dataWatcher.getWatchableObjectInt(17);
    }

    /**
     * Attack the specified entity using a ranged attack.
     */
    public void attackEntityWithRangedAttack(EntityLiving var1)
    {
        double var2 = var1.posX - this.posX;
        double var4 = var1.posZ - this.posZ;
        double var6 = Math.sqrt(var2 * var2 + var4 * var4) + (this.posY - var1.posY);
        double var10000 = var2 * var6;
        var10000 = var4 * var6;
        EntityCog var8 = new EntityCog(this.worldObj, this.posX - 0.25D, this.posY + 2.35D, this.posZ - 0.25D, false, this);
        var8.rotationYaw = this.renderYawOffset;
        var8.renderYawOffset = this.renderYawOffset;
        var8.rotationPitch = this.rotationPitch;
        double var9 = var1.posX + var1.motionX - this.posX;
        double var11 = var1.posY + -this.posY;
        double var13 = var1.posZ + var1.motionZ - this.posZ;
        float var15 = MathHelper.sqrt_double(var9 * var9 + var13 * var13);

        if (!this.worldObj.isRemote)
        {
            float var16 = var15 * 0.075F;
            var8.setThrowableHeading(var9, var11 + (double)(var15 * 0.2F), var13, var16, 0.0F);
            this.worldObj.spawnEntityInWorld(var8);
        }
    }

    public void spawnLargeCog(EntityLiving var1, int var2)
    {
        if (!this.stageDone[var2])
        {
            double var3 = var1.posX - this.posX;
            double var5 = var1.posZ - this.posZ;
            double var7 = Math.sqrt(var3 * var3 + var5 * var5) + (this.posY - var1.posY);
            double var10000 = var3 * var7;
            var10000 = var5 * var7;
            EntityCog var9 = new EntityCog(this.worldObj, this.posX - 0.25D, this.posY + 2.35D, this.posZ - 0.25D, true, this);
            var9.rotationYaw = this.renderYawOffset;
            var9.renderYawOffset = this.renderYawOffset;
            var9.rotationPitch = this.rotationPitch;
            double var10 = var1.posX + var1.motionX - this.posX;
            double var12 = var1.posY + -this.posY;
            double var14 = var1.posZ + var1.motionZ - this.posZ;
            float var16 = MathHelper.sqrt_double(var10 * var10 + var14 * var14);

            if (!this.worldObj.isRemote)
            {
                float var17 = var16 * 0.075F;
                var9.setThrowableHeading(var10, var12 + (double)(var16 * 0.2F), var14, var17, 0.0F);
                this.worldObj.playSoundAtEntity(this, "aemob.labyrinthsEye.cogloss", 2.0F, 1.0F);
                this.playSound("random.break", 0.8F, 0.8F + this.worldObj.rand.nextFloat() * 0.4F);
                this.worldObj.spawnEntityInWorld(var9);
            }

            this.stageDone[var2] = true;
        }
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected String getDeathSound()
    {
        return "aemob.labyrinthsEye.eyedeath";
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected String getLivingSound()
    {
        return "aemob.labyrinthsEye.move";
    }

    /**
     * Called when the mob's health reaches 0.
     */
    public void onDeath(DamageSource var1)
    {
        if (var1.getEntity() instanceof EntityPlayer)
        {
            EntityPlayer var2 = (EntityPlayer)var1.getEntity();
            Party var3 = PartyController.instance().getParty(PartyController.instance().getMember(var2));
            Dungeon var4 = DungeonHandler.instance().getInstanceAt(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ));

            if (var4 != null && var3 != null)
            {
                DungeonHandler.instance().addKey(var4, var3, new DungeonKey(EnumKeyType.Eye));
                PacketDispatcher.sendPacketToAllPlayers(AetherPacketHandler.sendDungeonKey(var4, var3, EnumKeyType.Eye));
            }
        }

        this.boss = new EntityLabyrinthEye(this.worldObj);
        super.onDeath(var1);
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        this.motionX = this.motionY = this.motionZ = 0.0D;
        super.onUpdate();
        this.motionX = this.motionY = this.motionZ = 0.0D;
        this.extinguish();
        AxisAlignedBB var1 = AxisAlignedBB.getBoundingBox(this.posX, this.posY, this.posZ, this.posX, this.posY, this.posZ).expand(1.0D, 1.0D, 1.0D);
        List var2 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, var1);

        if (var2.size() > 0 && this.getAwake())
        {
            for (int var3 = 0; var3 < var2.size(); ++var3)
            {
                Entity var4 = (Entity)var2.get(var3);

                if (var4 instanceof EntityLiving)
                {
                    var4.attackEntityFrom(DamageSource.generic, MathHelper.clamp_int(this.rand.nextInt(6), 4, 6));
                    var4.motionX *= 1.2432525D;
                    var4.motionY *= 1.2432525D;
                    var4.motionZ *= 1.2432525D;
                    var4.velocityChanged = true;
                }
            }
        }

        if (this.targetedEntity != null && this.chatTime >= 0)
        {
            --this.chatTime;
        }

        this.fallDistance = 0.0F;

        if (this.getAwake())
        {
            this.texture = "/net/aetherteam/aether/client/sprites/mobs/cogboss/cogawake.png";
        }
        else
        {
            this.texture = "/net/aetherteam/aether/client/sprites/mobs/cogboss/cogsleep.png";
        }

        if (this.entityToAttack != null && this.getAwake())
        {
            this.attackEntity(this.entityToAttack, this.getDistanceToEntity(this.entityToAttack));

            if (this.entityToAttack instanceof EntityPlayer)
            {
                ;
            }
        }
    }

    private boolean isBossStage(int var1)
    {
        switch (var1)
        {
            case 1:
                return this.getHealth() <= 250 && this.getHealth() >= 231;

            case 2:
                return this.getHealth() < 231 && this.getHealth() >= 212;

            case 3:
                return this.getHealth() < 212 && this.getHealth() >= 193;

            case 4:
                return this.getHealth() < 193 && this.getHealth() >= 174;

            case 5:
                return this.getHealth() < 174 && this.getHealth() >= 155;

            case 6:
                return this.getHealth() < 155 && this.getHealth() >= 136;

            case 7:
                return this.getHealth() < 136 && this.getHealth() >= 117;

            case 8:
                return this.getHealth() < 117 && this.getHealth() >= 98;

            case 9:
                return this.getHealth() < 98 && this.getHealth() >= 79;

            case 10:
                return this.getHealth() < 79 && this.getHealth() >= 60;

            case 11:
                return this.getHealth() < 60 && this.getHealth() >= 41;

            case 12:
                return this.getHealth() < 41 && this.getHealth() >= 22;

            case 13:
                return this.getHealth() < 3;

            default:
                return false;
        }
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource var1, int var2)
    {
        Entity var3 = var1.getSourceOfDamage();
        Entity var4 = var1.getEntity();
        this.setAwake(true);
        this.texture = "/net/aetherteam/aether/client/sprites/mobs/cogboss/cogawake.png";

        if (var3 != null && var1.isProjectile())
        {
            if (var4 instanceof EntityPlayer && ((EntityPlayer)var4).getCurrentEquippedItem() != null)
            {
                this.chatItUp((EntityPlayer) var4, "也许该换成剑来攻击, 我的" + ((EntityPlayer) var4).getCurrentEquippedItem().getItem().getItemDisplayName(((EntityPlayer) var4).getCurrentEquippedItem()) + "对付不了这玩意儿!");
                this.chatTime = 60;
            }

            return false;
        }
        else
        {
            if (var4 instanceof EntityPlayer)
            {
                EntityPlayer var5 = (EntityPlayer)var4;
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
     * Finds the closest player within 16 blocks to attack, or null if this Entity isn't interested in attacking
     * (Animals, Spiders at day, peaceful PigZombies).
     */
    protected Entity findPlayerToAttack()
    {
        if (!this.getAwake())
        {
            return null;
        }
        else
        {
            EntityPlayer var1 = this.worldObj.getClosestVulnerablePlayerToEntity(this, 16.0D);
            this.texture = "/net/aetherteam/aether/client/sprites/mobs/cogboss/cogawake.png";
            return var1 != null && this.canEntityBeSeen(var1) ? var1 : null;
        }
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound var1)
    {
        super.writeEntityToNBT(var1);
        var1.setBoolean("Awake", this.getAwake());
        var1.setInteger("Stage", this.stage);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound var1)
    {
        super.readEntityFromNBT(var1);
        this.setAwake(var1.getBoolean("Awake"));
        this.setStage(var1.getInteger("Stage"));
    }

    public boolean getAwake()
    {
        return this.dataWatcher.getWatchableObjectByte(16) == 1;
    }

    public void setAwake(boolean var1)
    {
        if (var1)
        {
            this.texture = "/net/aetherteam/aether/client/sprites/mobs/cog/cogawake.png";
            this.dataWatcher.updateObject(16, Byte.valueOf((byte)1));
        }
        else
        {
            this.texture = "/net/aetherteam/aether/client/sprites/mobs/cog/cogsleep.png";
            this.dataWatcher.updateObject(16, Byte.valueOf((byte)0));
        }
    }

    public int getMaxHealth()
    {
        return 250;
    }

    public String getBossTitle()
    {
        return "迷宫之眼:" + this.bossName;
    }

    /**
     * Attack the specified entity using a ranged attack.
     */
    public void attackEntityWithRangedAttack(EntityLiving var1, float var2) {}
}
