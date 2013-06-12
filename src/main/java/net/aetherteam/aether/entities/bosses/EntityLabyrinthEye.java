package net.aetherteam.aether.entities.bosses;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import net.aetherteam.aether.Aether;
import net.aetherteam.aether.AetherCommonPlayerHandler;
import net.aetherteam.aether.AetherNameGen;
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
    private boolean[] stageDone = new boolean[5];

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
        this.stageDone[0] = false;
        this.stageDone[1] = false;
        this.stageDone[2] = false;
        this.stageDone[3] = false;
        this.stageDone[4] = false;
    }

    public void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(16, Byte.valueOf((byte) 0));
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

    /**
     * Basic mob attack. Default to touch of death in EntityCreature. Overridden by each mob to define their attack.
     */
    protected void attackEntity(Entity var1, float var2)
    {
        this.target = (EntityLiving) var1;

        if (var2 < 10.0F)
        {
            double var3 = var1.posX - this.posX;
            double var5 = var1.posZ - this.posZ;

            if (this.target != null)
            {
                if (!this.target.isDead && (double) this.target.getDistanceToEntity(this) <= 12.0D)
                {
                    if (this.getAwake() && this.attackTime >= this.timeUntilShoot)
                    {
                        for (int var7 = 0; var7 < 4; ++var7)
                        {
                            if (this.isBossStage(var7))
                            {
                                this.spawnLargeCog(this.target, var7);
                            }
                        }

                        this.attackEntityWithRangedAttack(this.target);
                    }
                } else
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
            }

            this.rotationYaw = (float) (Math.atan2(var5, var3) * 180.0D / Math.PI) - 90.0F;
        }
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
            var8.setThrowableHeading(var9, var11 + (double) (var15 * 0.2F), var13, var16, 0.0F);
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
                var9.setThrowableHeading(var10, var12 + (double) (var16 * 0.2F), var14, var17, 0.0F);
                this.worldObj.spawnEntityInWorld(var9);
            }

            this.stageDone[var2] = true;
        }
    }

    /**
     * Called when the mob's health reaches 0.
     */
    public void onDeath(DamageSource var1)
    {
        if (var1.getEntity() instanceof EntityPlayer)
        {
            EntityPlayer var2 = (EntityPlayer) var1.getEntity();
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
        super.onUpdate();
        this.extinguish();

        if (this.chatTime >= 0)
        {
            --this.chatTime;
        }

        this.fallDistance = 0.0F;

        if (this.getAwake())
        {
            this.texture = "/net/aetherteam/aether/client/sprites/mobs/cogboss/cogawake.png";
        } else
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
        } else
        {
            this.motionX = this.motionY = this.motionZ = 0.0D;
        }

        if (!this.worldObj.isRemote)
        {
            if (this.rand.nextInt(100) == 1 && this.getEntityToAttack() != null)
            {
                ;
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
    }

    private boolean isBossStage(int var1)
    {
        switch (var1)
        {
            case 0:
                return this.getHealth() <= 250 && this.getHealth() >= 200;

            case 1:
                return this.getHealth() < 200 && this.getHealth() >= 150;

            case 2:
                return this.getHealth() < 150 && this.getHealth() >= 100;

            case 3:
                return this.getHealth() < 100 && this.getHealth() >= 50;

            case 4:
                return this.getHealth() < 50;

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
            if (var4 instanceof EntityPlayer && ((EntityPlayer) var4).getCurrentEquippedItem() != null)
            {
                this.chatItUp((EntityPlayer) var4, "也许该换成剑来攻击，我的" + ((EntityPlayer) var4).getCurrentEquippedItem().getItem().getItemDisplayName(((EntityPlayer) var4).getCurrentEquippedItem()) + "对付不了这玩意儿！");
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
        } else
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
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound var1)
    {
        super.readEntityFromNBT(var1);
        this.setAwake(var1.getBoolean("Awake"));
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
            this.dataWatcher.updateObject(16, Byte.valueOf((byte) 1));
        } else
        {
            this.texture = "/net/aetherteam/aether/client/sprites/mobs/cog/cogsleep.png";
            this.dataWatcher.updateObject(16, Byte.valueOf((byte) 0));
        }
    }

    public int getMaxHealth()
    {
        return 250;
    }

    public String getBossTitle()
    {
        return this.bossName + ", the Labyrinth\'s Eye";
    }

    /**
     * Attack the specified entity using a ranged attack.
     */
    public void attackEntityWithRangedAttack(EntityLiving var1, float var2)
    {}
}
