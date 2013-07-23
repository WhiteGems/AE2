package net.aetherteam.aether.entities.bosses;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import java.util.List;
import java.util.Random;
import net.aetherteam.aether.Aether;
import net.aetherteam.aether.AetherCommonPlayerHandler;
import net.aetherteam.aether.AetherNameGen;
import net.aetherteam.aether.CommonProxy;
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
import net.minecraft.block.Block;
import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityLabyrinthEye extends EntityMiniBoss
    implements IAetherBoss, IRangedAttackMob
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

    public EntityLabyrinthEye(World par1World)
    {
        super(par1World);
        this.isImmuneToFire = true;
        this.moveSpeed = 0.5F;
        this.tasks.addTask(4, new AIEntityArrowAttackCog(this, this.moveSpeed, 60, 10.0F));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(6, new EntityAILookIdle(this));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 16.0F, 0, true));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
        this.bossName = AetherNameGen.gen();
        this.texture = "/net/aetherteam/aether/client/sprites/mobs/cogboss/cogsleep.png";
        setSize(2.0F, 2.0F);

        for (int i = 0; i < 12; i++)
        {
            this.stageDone[i] = false;
        }
    }

    public void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(16, Byte.valueOf((byte)0));
        this.dataWatcher.addObject(17, Integer.valueOf(0));
    }

    public boolean isAIEnabled()
    {
        return true;
    }

    public boolean canDespawn()
    {
        return false;
    }

    private boolean isCourseTraversable(double d, double d1, double d2, double d3)
    {
        double d4 = (this.waypointX - this.posX) / d3;
        double d5 = (this.waypointY - this.posY) / d3;
        double d6 = (this.waypointZ - this.posZ) / d3;
        AxisAlignedBB axisalignedbb = this.boundingBox.copy();

        for (int i = 1; i < d3; i++)
        {
            axisalignedbb.offset(d4, d5, d6);

            if (this.worldObj.getCollidingBoundingBoxes(this, axisalignedbb).size() > 0)
            {
                return false;
            }
        }

        return true;
    }

    private void orbitPlayer()
    {
        double d = this.waypointX - this.posX;
        double d1 = this.waypointY - this.posY;
        double d2 = this.waypointZ - this.posZ;
        double d3 = MathHelper.sqrt_double(d * d + d1 * d1 + d2 * d2);
        double d4 = this.targetedEntity.getDistanceSqToEntity(this);
        double d5 = this.targetedEntity.posX - this.posX;
        double d6 = this.targetedEntity.boundingBox.minY + this.targetedEntity.height / 2.0F - (this.posY + this.height / 2.0F);
        double d7 = this.targetedEntity.posZ - this.posZ;
        this.renderYawOffset = (this.rotationYaw = -(float)Math.atan2(d5, d7) * 180.0F / (float)Math.PI);
        double randomHeight = this.rand.nextFloat() * 10.0D;
        double distance = MathHelper.sqrt_double(196.0D - randomHeight * randomHeight);
        double x = this.rand.nextFloat() * distance;
        double y = MathHelper.sqrt_double(distance * distance - x * x);

        if (this.rand.nextInt(8) == 0)
        {
            this.xpov = (this.rand.nextBoolean() ? -1 : 1);
            this.zpov = (this.rand.nextBoolean() ? -1 : 1);
        }

        this.waypointX = (this.targetedEntity.posX + x * this.xpov);
        this.waypointY = (this.targetedEntity.posY + randomHeight + 5.0D);
        this.waypointZ = (this.targetedEntity.posZ + y * this.zpov);

        if (isCourseTraversable(this.waypointX, this.waypointY, this.waypointZ, d3))
        {
            float Speed = 0.5F;
            this.motionX = (d / d3 * Speed);
            this.motionY = (d1 / d3 * Speed);
            this.motionZ = (d2 / d3 * Speed);
        }
    }

    protected void attackEntity(Entity entity, float f)
    {
        if ((entity instanceof EntityLiving))
        {
            this.target = ((EntityLiving)entity);

            if (f < 10.0F)
            {
                double d = entity.posX - this.posX;
                double d1 = entity.posZ - this.posZ;

                if (this.target != null)
                {
                    if ((this.target.isDead) || (this.target.getDistanceToEntity(this) > 12.0D))
                    {
                        this.target = null;
                        this.attackTime = 0;
                    }
                    else if ((getAwake()) && (this.attackTime >= this.timeUntilShoot))
                    {
                        func_82196_d(this.target);
                    }

                    if ((this.attackTime >= this.timeUntilShoot) && (canEntityBeSeen(this.target)))
                    {
                        this.attackTime = -10;
                    }

                    if (this.attackTime < this.timeUntilShoot)
                    {
                        this.attackTime += 2;
                    }

                    for (int stage = 0; stage < 13; stage++)
                    {
                        if ((isBossStage(stage)) && (this.stageDone[stage] == 0))
                        {
                            setStage(stage);
                            spawnLargeCog(this.target, stage);
                        }
                    }

                    if (this.stage == 12)
                    {
                        setSize(0.5F, 0.5F);
                    }
                }

                this.rotationYaw = ((float)(Math.atan2(d1, d) * 180.0D / Math.PI) - 90.0F);
                int x = MathHelper.floor_double(entity.posX);
                int y = MathHelper.floor_double(entity.posY);
                int z = MathHelper.floor_double(entity.posZ);
                this.worldObj.setBlock(x, y, z, AetherBlocks.ColdFire.blockID);
            }
        }
    }

    private void setStage(int stage)
    {
        this.dataWatcher.updateObject(17, Integer.valueOf(stage));
        this.stage = stage;
    }

    public int getStage()
    {
        return this.dataWatcher.getWatchableObjectInt(17);
    }

    public void func_82196_d(EntityLiving entityToAttack)
    {
        double d1 = entityToAttack.posX - this.posX;
        double d2 = entityToAttack.posZ - this.posZ;
        double d4 = Math.sqrt(d1 * d1 + d2 * d2) + (this.posY - entityToAttack.posY);
        d1 *= d4;
        d2 *= d4;
        EntityCog entityarrow = new EntityCog(this.worldObj, this.posX - 0.25D, this.posY + 2.35D, this.posZ - 0.25D, false, this);
        entityarrow.rotationYaw = this.renderYawOffset;
        entityarrow.renderYawOffset = this.renderYawOffset;
        entityarrow.rotationPitch = this.rotationPitch;
        double var3 = entityToAttack.posX + entityToAttack.motionX - this.posX;
        double var5 = entityToAttack.posY + -this.posY;
        double var7 = entityToAttack.posZ + entityToAttack.motionZ - this.posZ;
        float var9 = MathHelper.sqrt_double(var3 * var3 + var7 * var7);

        if (!this.worldObj.isRemote)
        {
            float distance = var9 * 0.075F;
            entityarrow.setThrowableHeading(var3, var5 + var9 * 0.2F, var7, distance, 0.0F);
            this.worldObj.spawnEntityInWorld(entityarrow);
        }
    }

    public void spawnLargeCog(EntityLiving entityToAttack, int stage)
    {
        if (this.stageDone[stage] != 0)
        {
            return;
        }

        double d1 = entityToAttack.posX - this.posX;
        double d2 = entityToAttack.posZ - this.posZ;
        double d4 = Math.sqrt(d1 * d1 + d2 * d2) + (this.posY - entityToAttack.posY);
        d1 *= d4;
        d2 *= d4;
        EntityCog entityarrow = new EntityCog(this.worldObj, this.posX - 0.25D, this.posY + 2.35D, this.posZ - 0.25D, true, this);
        entityarrow.rotationYaw = this.renderYawOffset;
        entityarrow.renderYawOffset = this.renderYawOffset;
        entityarrow.rotationPitch = this.rotationPitch;
        double var3 = entityToAttack.posX + entityToAttack.motionX - this.posX;
        double var5 = entityToAttack.posY + -this.posY;
        double var7 = entityToAttack.posZ + entityToAttack.motionZ - this.posZ;
        float var9 = MathHelper.sqrt_double(var3 * var3 + var7 * var7);

        if (!this.worldObj.isRemote)
        {
            float distance = var9 * 0.075F;
            entityarrow.setThrowableHeading(var3, var5 + var9 * 0.2F, var7, distance, 0.0F);
            this.worldObj.playSoundAtEntity(this, "aemob.labyrinthsEye.cogloss", 2.0F, 1.0F);
            playSound("random.break", 0.8F, 0.8F + this.worldObj.rand.nextFloat() * 0.4F);
            this.worldObj.spawnEntityInWorld(entityarrow);
        }

        this.stageDone[stage] = true;
    }

    protected String getDeathSound()
    {
        return "aemob.labyrinthsEye.eyedeath";
    }

    protected String getLivingSound()
    {
        return "aemob.labyrinthsEye.move";
    }

    public void onDeath(DamageSource source)
    {
        if ((source.getEntity() instanceof EntityPlayer))
        {
            EntityPlayer attackingPlayer = (EntityPlayer)source.getEntity();
            Party party = PartyController.instance().getParty(PartyController.instance().getMember(attackingPlayer));
            Dungeon dungeon = DungeonHandler.instance().getInstanceAt(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ));

            if ((dungeon != null) && (party != null))
            {
                DungeonHandler.instance().addKey(dungeon, party, new DungeonKey(EnumKeyType.Eye));
                PacketDispatcher.sendPacketToAllPlayers(AetherPacketHandler.sendDungeonKey(dungeon, party, EnumKeyType.Eye));
            }
        }

        this.boss = new EntityLabyrinthEye(this.worldObj);
        super.onDeath(source);
    }

    public void onUpdate()
    {
        this.motionX = (this.motionY = this.motionZ = 0.0D);
        super.onUpdate();
        this.motionX = (this.motionY = this.motionZ = 0.0D);
        extinguish();
        AxisAlignedBB bounds = AxisAlignedBB.getBoundingBox(this.posX, this.posY, this.posZ, this.posX, this.posY, this.posZ).expand(1.0D, 1.0D, 1.0D);
        List entityInBounds = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, bounds);

        if ((entityInBounds.size() > 0) && (getAwake()))
        {
            for (int i = 0; i < entityInBounds.size(); i++)
            {
                Entity attackedEntity = (Entity)entityInBounds.get(i);

                if ((attackedEntity instanceof EntityLiving))
                {
                    attackedEntity.attackEntityFrom(DamageSource.generic, MathHelper.clamp_int(this.rand.nextInt(6), 4, 6));
                    attackedEntity.motionX *= 1.2432525D;
                    attackedEntity.motionY *= 1.2432525D;
                    attackedEntity.motionZ *= 1.2432525D;
                    attackedEntity.velocityChanged = true;
                }
            }
        }

        if (this.targetedEntity != null)
        {
            if (this.chatTime >= 0)
            {
                this.chatTime -= 1;
            }
        }

        this.fallDistance = 0.0F;

        if (getAwake())
        {
            this.texture = "/net/aetherteam/aether/client/sprites/mobs/cogboss/cogawake.png";
        }
        else
        {
            this.texture = "/net/aetherteam/aether/client/sprites/mobs/cogboss/cogsleep.png";
        }

        if ((this.entityToAttack != null) && (getAwake()))
        {
            attackEntity(this.entityToAttack, getDistanceToEntity(this.entityToAttack));

            if (!(this.entityToAttack instanceof EntityPlayer));
        }
    }

    private boolean isBossStage(int stage)
    {
        switch (stage)
        {
            case 1:
                return (getHealth() <= 250) && (getHealth() >= 231);

            case 2:
                return (getHealth() < 231) && (getHealth() >= 212);

            case 3:
                return (getHealth() < 212) && (getHealth() >= 193);

            case 4:
                return (getHealth() < 193) && (getHealth() >= 174);

            case 5:
                return (getHealth() < 174) && (getHealth() >= 155);

            case 6:
                return (getHealth() < 155) && (getHealth() >= 136);

            case 7:
                return (getHealth() < 136) && (getHealth() >= 117);

            case 8:
                return (getHealth() < 117) && (getHealth() >= 98);

            case 9:
                return (getHealth() < 98) && (getHealth() >= 79);

            case 10:
                return (getHealth() < 79) && (getHealth() >= 60);

            case 11:
                return (getHealth() < 60) && (getHealth() >= 41);

            case 12:
                return (getHealth() < 41) && (getHealth() >= 22);

            case 13:
                return getHealth() < 3;
        }

        return false;
    }

    public boolean attackEntityFrom(DamageSource par1DamageSource, int par2)
    {
        Entity entity = par1DamageSource.getSourceOfDamage();
        Entity attacker = par1DamageSource.getEntity();
        setAwake(true);
        this.texture = "/net/aetherteam/aether/client/sprites/mobs/cogboss/cogawake.png";

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

    protected Entity findPlayerToAttack()
    {
        if (getAwake())
        {
            EntityPlayer var1 = this.worldObj.getClosestVulnerablePlayerToEntity(this, 16.0D);
            this.texture = "/net/aetherteam/aether/client/sprites/mobs/cogboss/cogawake.png";
            return (var1 != null) && (canEntityBeSeen(var1)) ? var1 : null;
        }

        return null;
    }

    public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeEntityToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setBoolean("Awake", getAwake());
        par1NBTTagCompound.setInteger("Stage", this.stage);
    }

    public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readEntityFromNBT(par1NBTTagCompound);
        setAwake(par1NBTTagCompound.getBoolean("Awake"));
        setStage(par1NBTTagCompound.getInteger("Stage"));
    }

    public boolean getAwake()
    {
        return this.dataWatcher.getWatchableObjectByte(16) == 1;
    }

    public void setAwake(boolean awake)
    {
        if (awake)
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
        return this.bossName + ", the Labyrinth's Eye";
    }

    public void attackEntityWithRangedAttack(EntityLiving entityliving, float f)
    {
    }
}

