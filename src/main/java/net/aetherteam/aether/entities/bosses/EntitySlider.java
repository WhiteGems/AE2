package net.aetherteam.aether.entities.bosses;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.aetherteam.aether.Aether;
import net.aetherteam.aether.AetherCommonPlayerHandler;
import net.aetherteam.aether.AetherLoot;
import net.aetherteam.aether.AetherNameGen;
import net.aetherteam.aether.blocks.AetherBlocks;
import net.aetherteam.aether.dungeons.Dungeon;
import net.aetherteam.aether.dungeons.DungeonHandler;
import net.aetherteam.aether.entities.EntityAetherCoin;
import net.aetherteam.aether.entities.EntitySentry;
import net.aetherteam.aether.entities.dungeon.EntityRewardItem;
import net.aetherteam.aether.enums.EnumBossType;
import net.aetherteam.aether.interfaces.IAetherBoss;
import net.aetherteam.aether.items.AetherItems;
import net.aetherteam.aether.packets.AetherPacketHandler;
import net.aetherteam.aether.party.Party;
import net.aetherteam.aether.party.PartyController;
import net.aetherteam.aether.party.members.PartyMember;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntitySlider extends EntityBossMob implements IAetherBoss
{
    private boolean[] stageDone;
    public Random random;
    public ArrayList<PartyMember> queuedMembers;
    public ArrayList<String> deadMembers;
    ArrayList<String> activeMembers;
    public AxisAlignedBB originalAABB;
    public ArrayList<Integer> blockBans;
    public int moveTimer;
    public int dennis;
    public int rennis;
    public int chatTime;
    public Entity target;
    public boolean gotMovement;
    public boolean crushed;
    public float speedy;
    public float harvey;
    public int direction;
    private int dungeonX;
    private int dungeonY;
    private int dungeonZ;

    public EntitySlider(World world)
    {
        super(world);
        this.stageDone = new boolean[5];
        this.random = new Random();
        this.queuedMembers = new ArrayList();
        this.deadMembers = new ArrayList();
        this.activeMembers = new ArrayList();
        this.blockBans = new ArrayList();
        this.rotationYaw = 0.0F;
        this.rotationPitch = 0.0F;
        this.setSize(2.0F, 2.0F);
        this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(500.0D);
        this.setEntityHealth(500.0F);
        this.dennis = 1;
        this.jumpMovementFactor = 0.0F;
        this.chatTime = 60;
        this.stageDone[0] = false;
        this.stageDone[1] = false;
        this.stageDone[2] = false;
        this.stageDone[3] = false;
        this.stageDone[4] = false;
        this.setBossName(AetherNameGen.gen());
        this.originalAABB = AxisAlignedBB.getBoundingBox(-7.0D, -1.0D, -7.5D, 7.0D, 9.0D, 7.5D).addCoord(this.posX, this.posY, this.posZ);
    }

    public EntitySlider(World world, double x, double y, double z)
    {
        this(world);
        this.setPosition(x, y, z);
    }

    /**
     * Disables a mob's ability to move on its own while true.
     */
    protected boolean isMovementCeased()
    {
        return true;
    }

    public void entityInit()
    {
        super.entityInit();
        this.posX = Math.floor(this.posX + 0.5D);
        this.posY = Math.floor(this.posY + 0.5D);
        this.posZ = Math.floor(this.posZ + 0.5D);
        this.dataWatcher.addObject(16, Byte.valueOf((byte)0));
        this.dataWatcher.addObject(17, Byte.valueOf((byte)0));
        this.dataWatcher.addObject(18, String.valueOf(AetherNameGen.gen()));
        this.dataWatcher.addObject(19, Byte.valueOf((byte)0));
        this.dataWatcher.addObject(20, Byte.valueOf((byte)0));
        this.dataWatcher.addObject(21, Byte.valueOf((byte)0));
    }

    public boolean getAwake()
    {
        return (this.dataWatcher.getWatchableObjectByte(16) & 1) != 0;
    }

    public void setAwake(boolean awake)
    {
        if (!this.worldObj.isRemote)
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
    }

    /**
     * Called when the mob's health reaches 0.
     */
    public void onDeath(DamageSource source)
    {
        this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, 0.3F, false);

        if (source.getEntity() instanceof EntityPlayer)
        {
            EntityPlayer attackingPlayer = (EntityPlayer)source.getEntity();
            Party party = PartyController.instance().getParty(PartyController.instance().getMember(attackingPlayer));
            Dungeon dungeon = DungeonHandler.instance().getInstanceAt(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ));

            if (dungeon != null && party != null)
            {
                Side side = FMLCommonHandler.instance().getEffectiveSide();

                if (side.isServer())
                {
                    DungeonHandler.instance().startTimer(dungeon, party, 60);
                    PacketDispatcher.sendPacketToAllPlayers(AetherPacketHandler.sendDungeonTimerStart(dungeon, party, 60));
                }
            }
        }

        super.onDeath(source);
    }

    public boolean getCritical()
    {
        return (this.dataWatcher.getWatchableObjectByte(17) & 1) != 0;
    }

    public void setCritical(boolean critical)
    {
        if (!this.worldObj.isRemote)
        {
            if (critical)
            {
                this.dataWatcher.updateObject(17, Byte.valueOf((byte)1));
            }
            else
            {
                this.dataWatcher.updateObject(17, Byte.valueOf((byte)0));
            }
        }
    }

    public String getBossName()
    {
        return this.dataWatcher.getWatchableObjectString(18);
    }

    public void setBossName(String name)
    {
        if (!this.worldObj.isRemote)
        {
            this.dataWatcher.updateObject(18, String.valueOf(name));
        }
    }

    public boolean hasStartedMusic()
    {
        return (this.dataWatcher.getWatchableObjectByte(19) & 1) != 0;
    }

    public void startMusic(boolean start)
    {
        if (start)
        {
            this.dataWatcher.updateObject(19, Byte.valueOf((byte)1));
        }
        else
        {
            this.dataWatcher.updateObject(19, Byte.valueOf((byte)0));
        }
    }

    public boolean hasPlayedMusic()
    {
        return (this.dataWatcher.getWatchableObjectByte(20) & 1) != 0;
    }

    public void playMusic(boolean play)
    {
        if (play)
        {
            this.dataWatcher.updateObject(20, Byte.valueOf((byte)1));
        }
        else
        {
            this.dataWatcher.updateObject(20, Byte.valueOf((byte)0));
        }
    }

    public boolean hasFinishedSoundtrack()
    {
        return (this.dataWatcher.getWatchableObjectByte(21) & 1) != 0;
    }

    public void finishMusic(boolean finish)
    {
        if (finish)
        {
            this.dataWatcher.updateObject(21, Byte.valueOf((byte)1));
            this.playMusicFile("aether:Slider Finish");
        }
        else
        {
            this.dataWatcher.updateObject(21, Byte.valueOf((byte)0));
        }
    }

    /**
     * Takes in the distance the entity has fallen this tick and whether its on the ground to update the fall distance
     * and deal fall damage if landing on the ground.  Args: distanceFallenThisTick, onGround
     */
    protected void updateFallState(double par1, boolean par3) {}

    /**
     * Called when the mob is falling. Calculates and applies fall damage.
     */
    protected void fall(float par1) {}

    /**
     * Determines if an entity can be despawned, used on idle far away entities
     */
    public boolean canDespawn()
    {
        return false;
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected String getLivingSound()
    {
        return !this.getAwake() ? "ambient.cave.cave" : null;
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
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setFloat("Speedy", this.speedy);
        nbttagcompound.setShort("MoveTimer", (short)this.moveTimer);
        nbttagcompound.setShort("Direction", (short)this.direction);
        nbttagcompound.setBoolean("GotMovement", this.gotMovement);
        nbttagcompound.setBoolean("Awake", this.getAwake());
        nbttagcompound.setBoolean("Critical", this.getCritical());
        nbttagcompound.setInteger("DungeonX", this.dungeonX);
        nbttagcompound.setInteger("DungeonY", this.dungeonY);
        nbttagcompound.setInteger("DungeonZ", this.dungeonZ);
        nbttagcompound.setString("BossName", this.getBossName());
        nbttagcompound.setBoolean("PlayedMusic", this.hasPlayedMusic());
        nbttagcompound.setBoolean("StartedMusic", this.hasStartedMusic());
        nbttagcompound.setBoolean("FinishedMusic", this.hasFinishedSoundtrack());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readEntityFromNBT(nbttagcompound);
        this.speedy = nbttagcompound.getFloat("Speedy");
        this.moveTimer = nbttagcompound.getShort("MoveTimer");
        this.direction = nbttagcompound.getShort("Direction");
        this.gotMovement = nbttagcompound.getBoolean("GotMovement");
        this.setAwake(nbttagcompound.getBoolean("Awake"));
        this.setCritical(nbttagcompound.getBoolean("Critical"));
        this.dungeonX = nbttagcompound.getInteger("DungeonX");
        this.dungeonY = nbttagcompound.getInteger("DungeonY");
        this.dungeonZ = nbttagcompound.getInteger("DungeonZ");
        this.setBossName(nbttagcompound.getString("BossName"));
        this.playMusic(nbttagcompound.getBoolean("PlayedMusic"));
        this.startMusic(nbttagcompound.getBoolean("StartedMusic"));
        this.finishMusic(nbttagcompound.getBoolean("FinishedMusic"));
    }

    public boolean isSoundOn()
    {
        return this.worldObj.isRemote && this.isClient() && Aether.proxy.getClient().sndManager != null && Aether.proxy.getClient().sndManager.sndSystem != null;
    }

    public boolean isClient()
    {
        return Aether.proxy.getClient() != null;
    }

    public boolean isMusicPlaying()
    {
        return Aether.proxy.getClient() != null && Aether.proxy.getClient().sndManager.sndSystem != null && Aether.proxy.getClient().sndManager.sndSystem.playing("streaming");
    }

    public void turnMusicOff()
    {
        if (this.isSoundOn())
        {
            Aether.proxy.getClient().sndManager.sndSystem.stop("BgMusic");
        }
    }

    public void playMusicFile(String fileName)
    {
        if (this.isSoundOn())
        {
            Aether.proxy.playMusic(fileName);
        }
    }

    public boolean isDead()
    {
        return this.func_110143_aJ() <= 0.0F || this.isDead;
    }

    public void resetSlider()
    {
        this.setAwake(false);
        this.stop();
        this.openDoor();
        this.moveTimer = 0;
        this.setEntityHealth(500.0F);

        if (!this.worldObj.isRemote)
        {
            this.setPosition((double)((float)this.dungeonX + 8.0F), (double)((float)this.dungeonY + 1.0F), (double)((float)this.dungeonZ + 8.0F));
        }

        List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(35.0D, 15.0D, 35.0D));

        for (int j = 0; j < list.size(); ++j)
        {
            Entity entity1 = (Entity)list.get(j);

            if (entity1 instanceof EntitySentry)
            {
                ;
            }
        }

        this.target = null;
        this.deadMembers.clear();
        this.activeMembers.clear();
        this.queuedMembers.clear();
        this.openDoor();
        this.startMusic(false);
        this.turnMusicOff();
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();

        if (!this.worldObj.isRemote && (float)this.getBossHP() != this.func_110143_aJ())
        {
            this.setBossHP();
        }

        DungeonHandler handler = DungeonHandler.instance();
        Dungeon dungeon = handler.getInstanceAt(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ));

        if (dungeon != null && this.dungeonX == 0 && this.dungeonY == 0 && this.dungeonZ == 0)
        {
            this.setDungeon((int)this.posX - 8, (int)this.posY - 2, (int)this.posZ - 8);
        }

        if (dungeon != null)
        {
            if (!dungeon.isActive())
            {
                this.setDead();
            }

            if (this.getAwake())
            {
                this.queuedMembers = dungeon.getQueuedMembers();
            }
        }

        this.jumpMovementFactor = 0.0F;
        this.renderYawOffset = this.rotationPitch = this.rotationYaw = 0.0F;

        if ((!this.hasPlayedMusic() || !this.isMusicPlaying() && this.hasPlayedMusic()) && this.hasStartedMusic() && !this.hasFinishedSoundtrack() && !this.isDead() && this.getAwake())
        {
            if (!this.hasFinishedSoundtrack())
            {
                this.playMusicFile("aether:Slider Battle");
            }

            this.playMusic(true);
        }

        if (this.hasFinishedSoundtrack() || this.isDead() || this.func_110143_aJ() <= 0.0F)
        {
            this.playMusicFile("aether:Slider Finish");
        }

        if (this.getAwake())
        {
            this.activeMembers.clear();
            Iterator allDead = this.queuedMembers.iterator();
            PartyMember a;

            while (allDead.hasNext())
            {
                a = (PartyMember)allDead.next();
                EntityPlayer playerGuy = this.worldObj.getPlayerEntityByName(a.username);

                if (playerGuy != null && (int)playerGuy.posX >= this.dungeonX && (int)playerGuy.posX < this.dungeonX + 16 && (int)playerGuy.posZ >= this.dungeonZ + 1 && (int)playerGuy.posZ < this.dungeonZ + 16 && !this.activeMembers.contains(playerGuy.username.toLowerCase()))
                {
                    this.activeMembers.add(playerGuy.username.toLowerCase());
                }
            }

            allDead = this.queuedMembers.iterator();

            while (allDead.hasNext())
            {
                a = (PartyMember)allDead.next();

                if (!this.activeMembers.contains(a.username.toLowerCase()) && !this.deadMembers.contains(a.username.toLowerCase()))
                {
                    this.deadMembers.add(a.username.toLowerCase());
                }
            }

            boolean var15 = this.deadMembers.size() >= this.queuedMembers.size();
            this.target = this.worldObj.getClosestPlayerToEntity(this, -1.0D);

            if (this.target != null && this.target instanceof EntityPlayer)
            {
                EntityPlayer var16 = (EntityPlayer)this.target;

                if (var15)
                {
                    this.resetSlider();
                    return;
                }
            }
            else if (this.target == null)
            {
                this.target = this.worldObj.getClosestPlayerToEntity(this, -1.0D);
            }

            if (this.isBossStage(0))
            {
                this.spawnSentries(4, 0);
            }

            if (this.isBossStage(1))
            {
                this.spawnSentries(4, 1);
            }

            if (this.isBossStage(2))
            {
                this.spawnSentries(4, 2);
            }

            if (this.isBossStage(3))
            {
                this.spawnSentries(4, 3);
            }

            if (this.isBossStage(4))
            {
                this.spawnSentries(8, 4);
                this.setCritical(true);
            }
            else
            {
                this.setCritical(false);
            }

            this.fallDistance = 0.0F;
            double var17;
            double b;
            double c;

            if (this.gotMovement)
            {
                if (this.isCollided)
                {
                    var17 = this.posX - 0.5D;
                    b = this.boundingBox.minY + 0.75D;
                    c = this.posZ - 0.5D;
                    this.crushed = false;

                    if (b < 124.0D && b > 4.0D)
                    {
                        int i;
                        double a1;
                        double b1;

                        if (this.direction == 0)
                        {
                            for (i = 0; i < 25; ++i)
                            {
                                a1 = (double)(i / 5 - 2) * 0.75D;
                                b1 = (double)(i % 5 - 2) * 0.75D;
                                this.blockCrush((int)(var17 + a1), (int)(b + 1.5D), (int)(c + b1));
                            }
                        }
                        else if (this.direction == 1)
                        {
                            for (i = 0; i < 25; ++i)
                            {
                                a1 = (double)(i / 5 - 2) * 0.75D;
                                b1 = (double)(i % 5 - 2) * 0.75D;
                                this.blockCrush((int)(var17 + a1), (int)(b - 1.5D), (int)(c + b1));
                            }
                        }
                        else if (this.direction == 2)
                        {
                            for (i = 0; i < 25; ++i)
                            {
                                a1 = (double)(i / 5 - 2) * 0.75D;
                                b1 = (double)(i % 5 - 2) * 0.75D;
                                this.blockCrush((int)(var17 + 1.5D), (int)(b + a1), (int)(c + b1));
                            }
                        }
                        else if (this.direction == 3)
                        {
                            for (i = 0; i < 25; ++i)
                            {
                                a1 = (double)(i / 5 - 2) * 0.75D;
                                b1 = (double)(i % 5 - 2) * 0.75D;
                                this.blockCrush((int)(var17 - 1.5D), (int)(b + a1), (int)(c + b1));
                            }
                        }
                        else if (this.direction == 4)
                        {
                            for (i = 0; i < 25; ++i)
                            {
                                a1 = (double)(i / 5 - 2) * 0.75D;
                                b1 = (double)(i % 5 - 2) * 0.75D;
                                this.blockCrush((int)(var17 + a1), (int)(b + b1), (int)(c + 1.5D));
                            }
                        }
                        else if (this.direction == 5)
                        {
                            for (i = 0; i < 25; ++i)
                            {
                                a1 = (double)(i / 5 - 2) * 0.75D;
                                b1 = (double)(i % 5 - 2) * 0.75D;
                                this.blockCrush((int)(var17 + a1), (int)(b + b1), (int)(c - 1.5D));
                            }
                        }
                    }

                    if (this.crushed)
                    {
                        this.worldObj.playSoundEffect(this.posX, this.posY, this.posZ, "random.explode", 3.0F, (0.625F + (this.worldObj.rand.nextFloat() - this.worldObj.rand.nextFloat()) * 0.2F) * 0.7F);
                        this.worldObj.playSoundAtEntity(this, "aether:aeboss.slider.collide", 2.5F, 1.0F / (this.rand.nextFloat() * 0.2F + 0.9F));
                    }

                    this.stop();
                }
                else
                {
                    if (this.speedy < 2.0F)
                    {
                        this.speedy += this.getCritical() ? 0.07F : 0.035F;
                    }

                    this.motionX = 0.0D;
                    this.motionY = 0.0D;
                    this.motionZ = 0.0D;

                    if (this.direction == 0)
                    {
                        this.motionY = (double)this.speedy;

                        if (this.boundingBox.minY > this.target.boundingBox.minY + 0.35D)
                        {
                            this.stop();
                            this.moveTimer = this.getCritical() ? 4 : 8;
                        }
                    }
                    else if (this.direction == 1)
                    {
                        this.motionY = (double)(-this.speedy);

                        if (this.boundingBox.minY < this.target.boundingBox.minY - 0.25D)
                        {
                            this.stop();
                            this.moveTimer = this.getCritical() ? 4 : 8;
                        }
                    }
                    else if (this.direction == 2)
                    {
                        this.motionX = (double)this.speedy;

                        if (this.posX > this.target.posX + 0.125D)
                        {
                            this.stop();
                            this.moveTimer = this.getCritical() ? 4 : 8;
                        }
                    }
                    else if (this.direction == 3)
                    {
                        this.motionX = (double)(-this.speedy);

                        if (this.posX < this.target.posX - 0.125D)
                        {
                            this.stop();
                            this.moveTimer = this.getCritical() ? 4 : 8;
                        }
                    }
                    else if (this.direction == 4)
                    {
                        this.motionZ = (double)this.speedy;

                        if (this.posZ > this.target.posZ + 0.125D)
                        {
                            this.stop();
                            this.moveTimer = this.getCritical() ? 4 : 8;
                        }
                    }
                    else if (this.direction == 5)
                    {
                        this.motionZ = (double)(-this.speedy);

                        if (this.posZ < this.target.posZ - 0.125D)
                        {
                            this.stop();
                            this.moveTimer = this.getCritical() ? 4 : 8;
                        }
                    }
                }
            }
            else
            {
                this.motionY = 0.0D;

                if (this.moveTimer > 0)
                {
                    --this.moveTimer;

                    if (this.getCritical() && this.rand.nextInt(2) == 0)
                    {
                        --this.moveTimer;
                    }

                    this.motionX = 0.0D;
                    this.motionY = 0.0D;
                    this.motionZ = 0.0D;
                }
                else
                {
                    var17 = Math.abs(this.posX - this.target.posX);
                    b = Math.abs(this.boundingBox.minY - this.target.boundingBox.minY);
                    c = Math.abs(this.posZ - this.target.posZ);

                    if (var17 > c)
                    {
                        this.direction = 2;

                        if (this.posX > this.target.posX)
                        {
                            this.direction = 3;
                        }
                    }
                    else
                    {
                        this.direction = 4;

                        if (this.posZ > this.target.posZ)
                        {
                            this.direction = 5;
                        }
                    }

                    if (b > var17 && b > c || b > 0.25D && this.rand.nextInt(5) == 0)
                    {
                        this.direction = 0;

                        if (this.posY > this.target.posY)
                        {
                            this.direction = 1;
                        }
                    }

                    this.worldObj.playSoundAtEntity(this, "aether:aeboss.slider.move", 2.5F, 1.0F / (this.rand.nextFloat() * 0.2F + 0.9F));
                    this.gotMovement = true;
                }
            }
        }

        if (this.harvey > 0.01F)
        {
            this.harvey *= 0.8F;
        }

        if (this.chatTime > 0)
        {
            --this.chatTime;
        }
    }

    private boolean isBossStage(int stage)
    {
        switch (stage)
        {
            case 0:
                return this.func_110143_aJ() <= 500.0F && this.func_110143_aJ() >= 400.0F;

            case 1:
                return this.func_110143_aJ() < 400.0F && this.func_110143_aJ() >= 300.0F;

            case 2:
                return this.func_110143_aJ() < 300.0F && this.func_110143_aJ() >= 200.0F;

            case 3:
                return this.func_110143_aJ() < 200.0F && this.func_110143_aJ() >= 125.0F;

            case 4:
                return this.func_110143_aJ() < 125.0F;

            default:
                return false;
        }
    }

    private void spawnSentries(int amount, int stage)
    {
        if (!this.stageDone[stage])
        {
            if (amount < 0)
            {
                amount = 0;
            }

            int spot = 0;

            for (int sentries = 0; sentries < amount; ++sentries)
            {
                if (spot > 3)
                {
                    spot = 0;
                }

                int[] coords = this.getSpotCoords(spot);
                EntitySentry entitysentry = new EntitySentry(this.worldObj);
                entitysentry.setPosition((double)coords[0] + 0.5D, (double)coords[1] + 1.5D, (double)coords[2] + 0.5D);
                this.worldObj.spawnEntityInWorld(entitysentry);
                ++spot;
            }

            this.stageDone[stage] = true;
        }
    }

    private int[] getSpotCoords(int spot)
    {
        int[] spotCoords = new int[3];
        int x = this.dungeonX;
        int y = this.dungeonY;
        int z = this.dungeonZ;

        switch (spot)
        {
            case 0:
                spotCoords[0] = x + 13;
                spotCoords[2] = z + 13;
                break;

            case 1:
                spotCoords[0] = x + 13;
                spotCoords[2] = z + 1;
                break;

            case 2:
                spotCoords[0] = x + 1;
                spotCoords[2] = z + 13;
                break;

            case 3:
                spotCoords[0] = x + 1;
                spotCoords[2] = z + 1;
        }

        spotCoords[1] = y;
        return spotCoords;
    }

    private void openDoor()
    {
        int x = this.dungeonX - 1;

        for (int y = this.dungeonY; y < this.dungeonY + 4; ++y)
        {
            for (int z = this.dungeonZ + 6; z < this.dungeonZ + 10; ++z)
            {
                this.worldObj.setBlock(x, y, z, 0);
            }
        }
    }

    private void finishDoor()
    {
        for (int y = this.dungeonY; y < this.dungeonY + 4; ++y)
        {
            for (int z = this.dungeonZ + 5; z < this.dungeonZ + 10; ++z)
            {
                this.worldObj.setBlock(this.dungeonX - 1, y, z, AetherBlocks.BronzeDoor.blockID);
            }
        }

        this.worldObj.setBlock(this.dungeonX - 1, this.dungeonY + 1, this.dungeonZ + 7, AetherBlocks.BronzeDoor.blockID, 1, 2);
        this.worldObj.setBlock(this.dungeonX, this.dungeonY - 1, this.dungeonZ + 7, AetherBlocks.BronzeDoorController.blockID);
    }

    /**
     * Applies a velocity to each of the entities pushing them away from each other. Args: entity
     */
    public void applyEntityCollision(Entity entity)
    {
        if (this.getAwake() && this.gotMovement)
        {
            if (entity instanceof EntitySentry)
            {
                return;
            }

            if (entity instanceof EntityLiving)
            {
                this.worldObj.playSoundAtEntity(this, "aether:aeboss.slider.collide", 2.5F, 1.0F / (this.rand.nextFloat() * 0.2F + 0.9F));

                if (entity instanceof EntityCreature || entity instanceof EntityPlayer)
                {
                    EntityLiving flag = (EntityLiving)entity;
                    flag.motionY += 0.35D;
                    flag.motionX *= 2.0D;
                    flag.motionZ *= 2.0D;
                }

                this.stop();
            }

            entity.attackEntityFrom(DamageSource.causeMobDamage(this), 6.0F);
        }
    }

    /**
     * Drop 0-2 items of this living's type. @param par1 - Whether this entity has recently been hit by a player. @param
     * par2 - Level of Looting used to kill this mob.
     */
    protected void dropFewItems(boolean var1, int var2)
    {
        for (int i = 0; i < 20 + this.rand.nextInt(10); ++i)
        {
            this.dropItem(AetherBlocks.DungeonStone.blockID, 1);
        }
    }

    public void stop()
    {
        this.gotMovement = false;
        this.moveTimer = 12;
        this.direction = 0;
        this.speedy = 0.0F;
        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;
    }

    private void chatItUp(EntityPlayer player, String s)
    {
        Side side = FMLCommonHandler.instance().getEffectiveSide();

        if (this.chatTime <= 0 && Aether.proxy.getClient() != null && side.isClient())
        {
            Aether.proxy.displayMessage(player, s);
            this.chatTime = 60;
        }
    }

    public void teleportMembersFromParty(ArrayList<PartyMember> members)
    {
        Side side = FMLCommonHandler.instance().getEffectiveSide();

        if (side.isServer())
        {
            MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
            ServerConfigurationManager configManager = server.getConfigurationManager();

            for (int playerAmount = 0; playerAmount < configManager.playerEntityList.size(); ++playerAmount)
            {
                Object player = configManager.playerEntityList.get(playerAmount);
                Iterator i$ = members.iterator();

                while (i$.hasNext())
                {
                    PartyMember member = (PartyMember)i$.next();

                    if (player instanceof EntityPlayerMP && ((EntityPlayerMP)player).username.equalsIgnoreCase(member.username))
                    {
                        ((EntityPlayerMP)player).setPositionAndUpdate((double)((float)((double)this.dungeonX + 0.5D)), (double)((float)this.dungeonY), (double)((float)((double)this.dungeonZ + 8.0D)));
                    }
                }
            }
        }
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource source, float damage)
    {
        if (source.getEntity() instanceof EntitySentry)
        {
            return false;
        }
        else if (source.getEntity() != null && source.getEntity() instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer)source.getEntity();
            ItemStack stack = player.getCurrentEquippedItem();

            if (stack != null && stack.getItem() != null)
            {
                if (!(stack.getItem() instanceof ItemPickaxe) && !(stack.getItem() instanceof ItemTool))
                {
                    this.chatItUp(player, "Hmm. It\'s a rock-solid block. A " + stack.getItem().getItemDisplayName(stack) + " wouldn\'t work on this.");
                    return false;
                }
                else
                {
                    AetherCommonPlayerHandler handler = Aether.getPlayerBase(player);
                    Side side = FMLCommonHandler.instance().getEffectiveSide();
                    boolean flag;

                    if (handler != null)
                    {
                        flag = true;

                        if (!player.isDead && flag)
                        {
                            handler.setCurrentBoss(this);
                        }
                    }

                    if (this.func_110143_aJ() > 0.0F && !this.isDead)
                    {
                        this.startMusic(true);
                    }
                    else
                    {
                        this.finishMusic(true);
                        this.playMusicFile("aether:Slider Finish");
                    }

                    Dungeon dungeon = DungeonHandler.instance().getInstanceAt(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ));
                    int x;

                    if (dungeon != null && dungeon.hasQueuedParty())
                    {
                        Party a = dungeon.getQueuedParty();
                        x = dungeon.getQueuedMembers().size();
                        float c = (float)(x - 1) * 0.045F;
                        float z = MathHelper.clamp_float((float)((int)(damage - damage * c)), 1.0F, damage);
                        flag = super.attackEntityFrom(source, z);
                    }
                    else
                    {
                        flag = super.attackEntityFrom(source, Math.max(0.0F, damage));
                    }

                    if (flag)
                    {
                        if (dungeon != null)
                        {
                            this.queuedMembers = dungeon.getQueuedMembers();
                        }

                        for (int var16 = 0; var16 < (this.func_110143_aJ() <= 0.0F ? 16 : 48); ++var16)
                        {
                            double var21 = this.posX + (double)(this.rand.nextFloat() - this.rand.nextFloat()) * 1.5D;
                            double var22 = this.boundingBox.minY + 1.75D + (double)(this.rand.nextFloat() - this.rand.nextFloat()) * 1.5D;
                            double member = this.posZ + (double)(this.rand.nextFloat() - this.rand.nextFloat()) * 1.5D;

                            if (this.func_110143_aJ() <= 0.0F)
                            {
                                this.worldObj.spawnParticle("explode", var21, var22, member, 0.0D, 0.0D, 0.0D);
                            }
                        }

                        ArrayList var26;

                        if (this.func_110143_aJ() <= 0.0F)
                        {
                            this.isDead = true;

                            if (!this.worldObj.isRemote)
                            {
                                boolean var17 = false;

                                if (this.random.nextInt(10) == 0)
                                {
                                    var17 = true;
                                }

                                for (x = 0; x < 25 + this.random.nextInt(25); ++x)
                                {
                                    this.worldObj.spawnEntityInWorld(new EntityAetherCoin(this.worldObj, this.posX, this.posY, this.posZ, 1));
                                }

                                for (x = 0; x < 3; ++x)
                                {
                                    this.worldObj.spawnEntityInWorld(new EntityAetherCoin(this.worldObj, this.posX, this.posY, this.posZ, 25));
                                }

                                if (dungeon != null)
                                {
                                    var26 = dungeon.getQueuedMembers();
                                    Iterator var20 = var26.iterator();

                                    while (var20.hasNext())
                                    {
                                        PartyMember var24 = (PartyMember)var20.next();

                                        for (int i$ = 0; i$ < 5 + this.random.nextInt(3); ++i$)
                                        {
                                            this.worldObj.spawnEntityInWorld(new EntityRewardItem(this.worldObj, this.posX, this.posY, this.posZ, AetherLoot.BRONZE.getRandomItem(this.random), var24.username));
                                        }

                                        this.worldObj.spawnEntityInWorld(new EntityRewardItem(this.worldObj, this.posX, this.posY, this.posZ, new ItemStack(AetherItems.Key, 1, 0), var24.username));

                                        if (var17)
                                        {
                                            this.worldObj.spawnEntityInWorld(new EntityRewardItem(this.worldObj, this.posX, this.posY, this.posZ, new ItemStack(AetherItems.LabyrinthMusicDisk, 1, 0), var24.username));
                                        }
                                    }
                                }
                            }

                            this.openDoor();
                            this.finishMusic(true);
                            this.worldObj.playSoundEffect(this.posX, this.posY, this.posZ, "aether:aemisc.achieveBronzeNew", 1.0F, 1.0F);
                        }
                        else if (!this.getAwake())
                        {
                            Side var18 = FMLCommonHandler.instance().getEffectiveSide();

                            if (var18.isServer() && !this.worldObj.isRemote && dungeon != null)
                            {
                                var26 = dungeon.getQueuedMembers();
                                ArrayList var25 = new ArrayList();
                                EntityPlayer var29 = (EntityPlayer)source.getEntity();
                                Iterator var28 = var26.iterator();

                                while (var28.hasNext())
                                {
                                    PartyMember var31 = (PartyMember)var28.next();

                                    if (!var31.username.equalsIgnoreCase(var29.username))
                                    {
                                        var25.add(var31);
                                    }
                                }

                                this.teleportMembersFromParty(var25);
                            }

                            this.worldObj.playSoundAtEntity(this, "aether:aeboss.slider.awake", 2.5F, 1.0F / (this.rand.nextFloat() * 0.2F + 0.9F));
                            this.setAwake(true);
                            this.target = source.getEntity();
                            x = this.dungeonX - 1;

                            for (int var27 = this.dungeonY; var27 < this.dungeonY + 8; ++var27)
                            {
                                for (int var30 = this.dungeonZ + 5; var30 < this.dungeonZ + 11; ++var30)
                                {
                                    this.worldObj.setBlock(x, var27, var30, AetherBlocks.LockedDungeonStone.blockID);
                                }
                            }
                        }
                        else if (this.gotMovement)
                        {
                            this.speedy *= 1.5F;
                        }

                        double var19 = Math.abs(this.posX - source.getEntity().posX);
                        double var23 = Math.abs(this.posZ - source.getEntity().posZ);

                        if (var19 > var23)
                        {
                            this.dennis = 1;
                            this.rennis = 0;

                            if (this.posX > source.getEntity().posX)
                            {
                                this.dennis = -1;
                            }
                        }
                        else
                        {
                            this.rennis = 1;
                            this.dennis = 0;

                            if (this.posZ > source.getEntity().posZ)
                            {
                                this.rennis = -1;
                            }
                        }

                        this.harvey = 0.7F - this.func_110143_aJ() / 875.0F;
                    }

                    return flag;
                }
            }
            else
            {
                this.chatItUp(player, "Hmm. It\'s a rock-solid block. My fist wouldn\'t work on this.");
                return false;
            }
        }
        else
        {
            return false;
        }
    }

    private void unlockBlock(int i, int j, int k)
    {
        int id = this.worldObj.getBlockId(i, j, k);

        if (id == AetherBlocks.LockedDungeonStone.blockID)
        {
            this.worldObj.setBlock(i, j, k, AetherBlocks.DungeonStone.blockID, this.worldObj.getBlockMetadata(i, j, k), 4);
            this.unlockBlock(i + 1, j, k);
            this.unlockBlock(i - 1, j, k);
            this.unlockBlock(i, j + 1, k);
            this.unlockBlock(i, j - 1, k);
            this.unlockBlock(i, j, k + 1);
            this.unlockBlock(i, j, k - 1);
        }

        if (id == AetherBlocks.LockedLightDungeonStone.blockID)
        {
            this.worldObj.setBlock(i, j, k, AetherBlocks.LightDungeonStone.blockID, this.worldObj.getBlockMetadata(i, j, k), 4);
            this.unlockBlock(i + 1, j, k);
            this.unlockBlock(i - 1, j, k);
            this.unlockBlock(i, j + 1, k);
            this.unlockBlock(i, j - 1, k);
            this.unlockBlock(i, j, k + 1);
            this.unlockBlock(i, j, k - 1);
        }
    }

    /**
     * Adds to the current velocity of the entity. Args: x, y, z
     */
    public void addVelocity(double d, double d1, double d2) {}

    /**
     * Sets that this entity has been attacked.
     */
    public void setBeenAttacked() {}

    public void blockCrush(int x, int y, int z)
    {
        if (!this.worldObj.isRemote)
        {
            int a = this.worldObj.getBlockId(x, y, z);
            int b = this.worldObj.getBlockMetadata(x, y, z);
            Collections.addAll(this.blockBans, new Integer[] {Integer.valueOf(AetherBlocks.LockedDungeonStone.blockID), Integer.valueOf(AetherBlocks.LockedLightDungeonStone.blockID), Integer.valueOf(AetherBlocks.TreasureChest.blockID), Integer.valueOf(AetherBlocks.DungeonHolystone.blockID), Integer.valueOf(AetherBlocks.DungeonEntrance.blockID), Integer.valueOf(AetherBlocks.DungeonEntranceController.blockID), Integer.valueOf(AetherBlocks.BronzeDoor.blockID), Integer.valueOf(AetherBlocks.BronzeDoorController.blockID)});

            if (a == 0 || this.blockBans.contains(Integer.valueOf(a)))
            {
                return;
            }

            Block.blocksList[a].onBlockDestroyedByPlayer(this.worldObj, x, y, z, 0);
            Block.blocksList[a].dropBlockAsItem(this.worldObj, x, y, z, b, 1);
            this.worldObj.setBlock(x, y, z, 0);
            this.crushed = true;

            if (this.worldObj.isRemote)
            {
                FMLClientHandler.instance().getClient().effectRenderer.addBlockDestroyEffects(x, y, z, a, b);
            }
        }

        if (this.worldObj.isRemote && FMLClientHandler.instance().getClient().gameSettings.fancyGraphics)
        {
            this.addSquirrelButts(x, y, z);
        }
    }

    public void addSquirrelButts(int x, int y, int z)
    {
        if (this.worldObj.isRemote)
        {
            double a = (double)x + 0.5D + (double)(this.rand.nextFloat() - this.rand.nextFloat()) * 0.375D;
            double b = (double)y + 0.5D + (double)(this.rand.nextFloat() - this.rand.nextFloat()) * 0.375D;
            double c = (double)z + 0.5D + (double)(this.rand.nextFloat() - this.rand.nextFloat()) * 0.375D;
            this.worldObj.spawnParticle("explode", a, b, c, 0.0D, 0.0D, 0.0D);
        }
    }

    public void setDungeon(int i, int j, int k)
    {
        this.dungeonX = i;
        this.dungeonY = j;
        this.dungeonZ = k;
    }

    public int getBossHP()
    {
        return this.dataWatcher.getWatchableObjectInt(26);
    }

    public void setBossHP()
    {
        this.dataWatcher.updateObject(26, Integer.valueOf((int)this.func_110143_aJ()));
    }

    public int getBossMaxHP()
    {
        return 500;
    }

    public int getBossEntityID()
    {
        return this.entityId;
    }

    public String getBossTitle()
    {
        return this.getBossName() + ", the Slider";
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
        return EnumBossType.BOSS;
    }
}
