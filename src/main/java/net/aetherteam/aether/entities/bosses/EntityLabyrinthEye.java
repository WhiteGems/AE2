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
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
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
    private EntityLivingBase target;
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
        this.bossName = AetherNameGen.gen();

        for (int i = 0; i < 12; ++i)
        {
            this.stageDone[i] = false;
        }

        this.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111128_a(0.5D);
        this.setSize(2.0F, 2.0F);
        this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(250.0D);
        this.setEntityHealth(250.0F);
        this.tasks.addTask(4, new AIEntityArrowAttackCog(this, 0.5F, 60, 10.0F));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(6, new EntityAILookIdle(this));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
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

    private boolean isCourseTraversable(double d, double d1, double d2, double d3)
    {
        double d4 = (this.waypointX - this.posX) / d3;
        double d5 = (this.waypointY - this.posY) / d3;
        double d6 = (this.waypointZ - this.posZ) / d3;
        AxisAlignedBB axisalignedbb = this.boundingBox.copy();

        for (int i = 1; (double)i < d3; ++i)
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
        double d3 = (double)MathHelper.sqrt_double(d * d + d1 * d1 + d2 * d2);
        double d4 = this.targetedEntity.getDistanceSqToEntity(this);
        double d5 = this.targetedEntity.posX - this.posX;
        double d6 = this.targetedEntity.boundingBox.minY + (double)(this.targetedEntity.height / 2.0F) - (this.posY + (double)(this.height / 2.0F));
        double d7 = this.targetedEntity.posZ - this.posZ;
        this.renderYawOffset = this.rotationYaw = -((float)Math.atan2(d5, d7)) * 180.0F / (float)Math.PI;
        double randomHeight = (double)this.rand.nextFloat() * 10.0D;
        double distance = (double)MathHelper.sqrt_double(196.0D - randomHeight * randomHeight);
        double x = (double)this.rand.nextFloat() * distance;
        double y = (double)MathHelper.sqrt_double(distance * distance - x * x);

        if (this.rand.nextInt(8) == 0)
        {
            this.xpov = this.rand.nextBoolean() ? -1 : 1;
            this.zpov = this.rand.nextBoolean() ? -1 : 1;
        }

        this.waypointX = this.targetedEntity.posX + x * (double)this.xpov;
        this.waypointY = this.targetedEntity.posY + randomHeight + 5.0D;
        this.waypointZ = this.targetedEntity.posZ + y * (double)this.zpov;

        if (this.isCourseTraversable(this.waypointX, this.waypointY, this.waypointZ, d3))
        {
            float Speed = 0.5F;
            this.motionX = d / d3 * (double)Speed;
            this.motionY = d1 / d3 * (double)Speed;
            this.motionZ = d2 / d3 * (double)Speed;
        }
    }

    /**
     * Basic mob attack. Default to touch of death in EntityCreature. Overridden by each mob to define their attack.
     */
    protected void attackEntity(Entity entity, float f)
    {
        if (entity instanceof EntityLivingBase)
        {
            this.target = (EntityLivingBase)entity;

            if (f < 10.0F)
            {
                double d = entity.posX - this.posX;
                double d1 = entity.posZ - this.posZ;
                int x;

                if (this.target != null)
                {
                    if (!this.target.isDead && (double)this.target.getDistanceToEntity(this) <= 12.0D)
                    {
                        if (this.getAwake() && this.attackTime >= this.timeUntilShoot)
                        {
                            this.attackEntity(this.target);
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

                    for (x = 0; x < 13; ++x)
                    {
                        if (this.isBossStage(x) && !this.stageDone[x])
                        {
                            this.setStage(x);
                            this.spawnLargeCog(this.target, x);
                        }
                    }

                    if (this.stage == 12)
                    {
                        this.setSize(0.5F, 0.5F);
                    }
                }

                this.rotationYaw = (float)(Math.atan2(d1, d) * 180.0D / Math.PI) - 90.0F;
                x = MathHelper.floor_double(entity.posX);
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

    public void attackEntity(EntityLivingBase entityToAttack)
    {
        double d1 = entityToAttack.posX - this.posX;
        double d2 = entityToAttack.posZ - this.posZ;
        double d4 = Math.sqrt(d1 * d1 + d2 * d2) + (this.posY - entityToAttack.posY);
        double var10000 = d1 * d4;
        var10000 = d2 * d4;
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
            entityarrow.setThrowableHeading(var3, var5 + (double)(var9 * 0.2F), var7, distance, 0.0F);
            this.worldObj.spawnEntityInWorld(entityarrow);
        }
    }

    public void spawnLargeCog(EntityLivingBase entityToAttack, int stage)
    {
        if (!this.stageDone[stage])
        {
            double d1 = entityToAttack.posX - this.posX;
            double d2 = entityToAttack.posZ - this.posZ;
            double d4 = Math.sqrt(d1 * d1 + d2 * d2) + (this.posY - entityToAttack.posY);
            double var10000 = d1 * d4;
            var10000 = d2 * d4;
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
                entityarrow.setThrowableHeading(var3, var5 + (double)(var9 * 0.2F), var7, distance, 0.0F);
                this.worldObj.playSoundAtEntity(this, "aether:aemob.labyrinthsEye.cogloss", 2.0F, 1.0F);
                this.playSound("random.break", 0.8F, 0.8F + this.worldObj.rand.nextFloat() * 0.4F);
                this.worldObj.spawnEntityInWorld(entityarrow);
            }

            this.stageDone[stage] = true;
        }
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected String getDeathSound()
    {
        return "aether:aemob.labyrinthsEye.eyedeath";
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected String getLivingSound()
    {
        return "aether:aemob.labyrinthsEye.move";
    }

    /**
     * Called when the mob's health reaches 0.
     */
    public void onDeath(DamageSource source)
    {
        if (source.getEntity() instanceof EntityPlayer)
        {
            EntityPlayer attackingPlayer = (EntityPlayer)source.getEntity();
            Party party = PartyController.instance().getParty(PartyController.instance().getMember(attackingPlayer));
            Dungeon dungeon = DungeonHandler.instance().getInstanceAt(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ));

            if (dungeon != null && party != null)
            {
                DungeonHandler.instance().addKey(dungeon, party, new DungeonKey(EnumKeyType.Eye));
                PacketDispatcher.sendPacketToAllPlayers(AetherPacketHandler.sendDungeonKey(dungeon, party, EnumKeyType.Eye));
            }
        }

        this.boss = new EntityLabyrinthEye(this.worldObj);
        super.onDeath(source);
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
        AxisAlignedBB bounds = AxisAlignedBB.getBoundingBox(this.posX, this.posY, this.posZ, this.posX, this.posY, this.posZ).expand(1.0D, 1.0D, 1.0D);
        List entityInBounds = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, bounds);

        if (entityInBounds.size() > 0 && this.getAwake())
        {
            for (int i = 0; i < entityInBounds.size(); ++i)
            {
                Entity attackedEntity = (Entity)entityInBounds.get(i);

                if (attackedEntity instanceof EntityLiving)
                {
                    attackedEntity.attackEntityFrom(DamageSource.generic, (float)MathHelper.clamp_int(this.rand.nextInt(6), 4, 6));
                    attackedEntity.motionX *= 1.2432525D;
                    attackedEntity.motionY *= 1.2432525D;
                    attackedEntity.motionZ *= 1.2432525D;
                    attackedEntity.velocityChanged = true;
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
            ;
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

    private boolean isBossStage(int stage)
    {
        switch (stage)
        {
            case 1:
                return this.func_110143_aJ() <= 250.0F && this.func_110143_aJ() >= 231.0F;

            case 2:
                return this.func_110143_aJ() < 231.0F && this.func_110143_aJ() >= 212.0F;

            case 3:
                return this.func_110143_aJ() < 212.0F && this.func_110143_aJ() >= 193.0F;

            case 4:
                return this.func_110143_aJ() < 193.0F && this.func_110143_aJ() >= 174.0F;

            case 5:
                return this.func_110143_aJ() < 174.0F && this.func_110143_aJ() >= 155.0F;

            case 6:
                return this.func_110143_aJ() < 155.0F && this.func_110143_aJ() >= 136.0F;

            case 7:
                return this.func_110143_aJ() < 136.0F && this.func_110143_aJ() >= 117.0F;

            case 8:
                return this.func_110143_aJ() < 117.0F && this.func_110143_aJ() >= 98.0F;

            case 9:
                return this.func_110143_aJ() < 98.0F && this.func_110143_aJ() >= 79.0F;

            case 10:
                return this.func_110143_aJ() < 79.0F && this.func_110143_aJ() >= 60.0F;

            case 11:
                return this.func_110143_aJ() < 60.0F && this.func_110143_aJ() >= 41.0F;

            case 12:
                return this.func_110143_aJ() < 41.0F && this.func_110143_aJ() >= 22.0F;

            case 13:
                return this.func_110143_aJ() < 3.0F;

            default:
                return false;
        }
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource source, float damage)
    {
        Entity entity = source.getSourceOfDamage();
        Entity attacker = source.getEntity();
        this.setAwake(true);

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
            return var1 != null && this.canEntityBeSeen(var1) ? var1 : null;
        }
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeEntityToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setBoolean("Awake", this.getAwake());
        par1NBTTagCompound.setInteger("Stage", this.stage);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readEntityFromNBT(par1NBTTagCompound);
        this.setAwake(par1NBTTagCompound.getBoolean("Awake"));
        this.setStage(par1NBTTagCompound.getInteger("Stage"));
    }

    public boolean getAwake()
    {
        return this.dataWatcher.getWatchableObjectByte(16) == 1;
    }

    public void setAwake(boolean awake)
    {
        if (awake)
        {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte)1));
        }
        else
        {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte)0));
        }
    }

    public String getBossTitle()
    {
        return this.bossName + ", the Labyrinth\'s Eye";
    }

    /**
     * Attack the specified entity using a ranged attack.
     */
    public void attackEntityWithRangedAttack(EntityLivingBase entityliving, float f) {}
}
