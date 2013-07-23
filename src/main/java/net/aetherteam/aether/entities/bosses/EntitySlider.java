package net.aetherteam.aether.entities.bosses;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import net.aetherteam.aether.Aether;
import net.aetherteam.aether.AetherCommonPlayerHandler;
import net.aetherteam.aether.AetherLoot;
import net.aetherteam.aether.AetherNameGen;
import net.aetherteam.aether.CommonProxy;
import net.aetherteam.aether.blocks.AetherBlocks;
import net.aetherteam.aether.dungeons.Dungeon;
import net.aetherteam.aether.dungeons.DungeonHandler;
import net.aetherteam.aether.entities.EntityAetherCoin;
import net.aetherteam.aether.entities.EntitySentry;
import net.aetherteam.aether.entities.dungeon.EntityRewardItem;
import net.aetherteam.aether.enums.EnumBossType;
import net.aetherteam.aether.interfaces.IAetherBoss;
import net.aetherteam.aether.items.AetherItems;
import net.aetherteam.aether.oldcode.NameGenerator;
import net.aetherteam.aether.packets.AetherPacketHandler;
import net.aetherteam.aether.party.Party;
import net.aetherteam.aether.party.PartyController;
import net.aetherteam.aether.party.members.PartyMember;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundPoolEntry;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
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
import paulscode.sound.SoundSystem;

public class EntitySlider extends EntityBossMob
    implements IAetherBoss
{
    public String dir = "/net/aetherteam/aether/client/sprites";
    private boolean[] stageDone = new boolean[5];
    public Random random = new Random();

    public ArrayList queuedMembers = new ArrayList();
    public ArrayList deadMembers = new ArrayList();
    ArrayList activeMembers = new ArrayList();
    public AxisAlignedBB originalAABB;
    public ArrayList blockBans = new ArrayList();
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
        this.rotationYaw = 0.0F;
        this.rotationPitch = 0.0F;
        setSize(2.0F, 2.0F);
        this.health = getMaxHealth();
        this.dennis = 1;
        this.jumpMovementFactor = 0.0F;
        this.chatTime = 60;
        this.stageDone[0] = false;
        this.stageDone[1] = false;
        this.stageDone[2] = false;
        this.stageDone[3] = false;
        this.stageDone[4] = false;
        this.texture = (this.dir + "/bosses/slider/sliderSleep.png");
        setBossName(NameGenerator.next(6, 12));
        this.originalAABB = AxisAlignedBB.getBoundingBox(-7.0D, -1.0D, -7.5D, 7.0D, 9.0D, 7.5D).addCoord(this.posX, this.posY, this.posZ);
    }

    public EntitySlider(World world, double x, double y, double z)
    {
        this(world);
        setPosition(x, y, z);
    }

    public int getMaxHealth()
    {
        return 500;
    }

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
        return (this.dataWatcher.getWatchableObjectByte(16) & 0x1) != 0;
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

    public void onDeath(DamageSource source)
    {
        this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, 0.3F, false);

        if ((source.getEntity() instanceof EntityPlayer))
        {
            EntityPlayer attackingPlayer = (EntityPlayer)source.getEntity();
            Party party = PartyController.instance().getParty(PartyController.instance().getMember(attackingPlayer));
            Dungeon dungeon = DungeonHandler.instance().getInstanceAt(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ));

            if ((dungeon != null) && (party != null))
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
        return (this.dataWatcher.getWatchableObjectByte(17) & 0x1) != 0;
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
        return (this.dataWatcher.getWatchableObjectByte(19) & 0x1) != 0;
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
        return (this.dataWatcher.getWatchableObjectByte(20) & 0x1) != 0;
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
        return (this.dataWatcher.getWatchableObjectByte(21) & 0x1) != 0;
    }

    public void finishMusic(boolean finish)
    {
        if (finish)
        {
            this.dataWatcher.updateObject(21, Byte.valueOf((byte)1));
            playMusicFile("Slider Finish");
        }
        else
        {
            this.dataWatcher.updateObject(21, Byte.valueOf((byte)0));
        }
    }

    protected void updateFallState(double par1, boolean par3)
    {
    }

    protected void fall(float par1)
    {
    }

    public boolean canDespawn()
    {
        return false;
    }

    protected String getLivingSound()
    {
        return !getAwake() ? "ambient.cave.cave" : null;
    }

    protected String getHurtSound()
    {
        return "step.stone";
    }

    protected String getDeathSound()
    {
        return "aeboss.slider.die";
    }

    public void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setFloat("Speedy", this.speedy);
        nbttagcompound.setShort("MoveTimer", (short)this.moveTimer);
        nbttagcompound.setShort("Direction", (short)this.direction);
        nbttagcompound.setBoolean("GotMovement", this.gotMovement);
        nbttagcompound.setBoolean("Awake", getAwake());
        nbttagcompound.setBoolean("Critical", getCritical());
        nbttagcompound.setInteger("DungeonX", this.dungeonX);
        nbttagcompound.setInteger("DungeonY", this.dungeonY);
        nbttagcompound.setInteger("DungeonZ", this.dungeonZ);
        nbttagcompound.setString("BossName", getBossName());
        nbttagcompound.setBoolean("PlayedMusic", hasPlayedMusic());
        nbttagcompound.setBoolean("StartedMusic", hasStartedMusic());
        nbttagcompound.setBoolean("FinishedMusic", hasFinishedSoundtrack());
    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readEntityFromNBT(nbttagcompound);
        this.speedy = nbttagcompound.getFloat("Speedy");
        this.moveTimer = nbttagcompound.getShort("MoveTimer");
        this.direction = nbttagcompound.getShort("Direction");
        this.gotMovement = nbttagcompound.getBoolean("GotMovement");
        setAwake(nbttagcompound.getBoolean("Awake"));
        setCritical(nbttagcompound.getBoolean("Critical"));
        this.dungeonX = nbttagcompound.getInteger("DungeonX");
        this.dungeonY = nbttagcompound.getInteger("DungeonY");
        this.dungeonZ = nbttagcompound.getInteger("DungeonZ");
        setBossName(nbttagcompound.getString("BossName"));
        playMusic(nbttagcompound.getBoolean("PlayedMusic"));
        startMusic(nbttagcompound.getBoolean("StartedMusic"));
        finishMusic(nbttagcompound.getBoolean("FinishedMusic"));
    }

    public boolean isSoundOn()
    {
        return (this.worldObj.isRemote) && (isClient()) && (Aether.proxy.getClient().sndManager != null) && (SoundPoolEntry.soundName != null);
    }

    public boolean isClient()
    {
        return Aether.proxy.getClient() != null;
    }

    public boolean isMusicPlaying()
    {
        return (Aether.proxy.getClient() != null) && (SoundPoolEntry.soundName != null) && (SoundPoolEntry.soundName.playing("streaming"));
    }

    public void turnMusicOff()
    {
        if (isSoundOn());
    }

    public void playMusicFile(String fileName)
    {
        if (isSoundOn())
        {
            Aether.proxy.getClient().sndManager.a(fileName, (float)this.posX, (float)this.posY, (float)this.posZ);
        }
    }

    public boolean isDead()
    {
        return (this.health <= 0) || (this.isDead);
    }

    public void resetSlider()
    {
        setAwake(false);
        stop();
        openDoor();
        this.moveTimer = 0;
        this.health = 500;

        if (!this.worldObj.isRemote)
        {
            setPosition(this.dungeonX + 8.0F, this.dungeonY + 1.0F, this.dungeonZ + 8.0F);
        }

        List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(35.0D, 15.0D, 35.0D));

        for (int j = 0; j < list.size(); j++)
        {
            Entity entity1 = (Entity)list.get(j);

            if (!(entity1 instanceof EntitySentry));
        }

        this.target = null;
        this.deadMembers.clear();
        this.activeMembers.clear();
        this.queuedMembers.clear();
        openDoor();
        startMusic(false);
        turnMusicOff();
    }

    public void onUpdate()
    {
        super.onUpdate();

        if ((!this.worldObj.isRemote) && (getBossHP() != this.health))
        {
            setBossHP();
        }

        DungeonHandler handler = DungeonHandler.instance();
        Dungeon dungeon = handler.getInstanceAt(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ));

        if ((dungeon != null) && (this.dungeonX == 0) && (this.dungeonY == 0) && (this.dungeonZ == 0))
        {
            setDungeon((int)this.posX - 8, (int)this.posY - 2, (int)this.posZ - 8);
        }

        if (dungeon != null)
        {
            if (!dungeon.isActive())
            {
                setDead();
            }

            if (getAwake())
            {
                this.queuedMembers = dungeon.getQueuedMembers();
            }
        }

        this.jumpMovementFactor = 0.0F;
        this.renderYawOffset = (this.rotationPitch = this.rotationYaw = 0.0F);

        if (((!hasPlayedMusic()) || ((!isMusicPlaying()) && (hasPlayedMusic()))) && (hasStartedMusic()) && (!hasFinishedSoundtrack()) && (!isDead()) && (getAwake()))
        {
            if (!hasFinishedSoundtrack())
            {
                playMusicFile("Slider Battle");
            }

            playMusic(true);
        }

        if ((hasFinishedSoundtrack()) || (isDead()) || (this.health <= 0))
        {
            playMusicFile("Slider Finish");
        }

        if (getCritical())
        {
            this.texture = (this.dir + "/bosses/slider/sliderAwake_red.png");
        }
        else if (getAwake())
        {
            this.texture = (this.dir + "/bosses/slider/sliderAwake.png");
        }
        else
        {
            this.texture = (this.dir + "/bosses/slider/sliderSleep.png");
        }

        if (getAwake())
        {
            this.activeMembers.clear();

            for (PartyMember member : this.queuedMembers)
            {
                EntityPlayer playerGuy = this.worldObj.getPlayerEntityByName(member.username);

                if ((playerGuy != null) && ((int)playerGuy.posX >= this.dungeonX) && ((int)playerGuy.posX < this.dungeonX + 16) && ((int)playerGuy.posZ >= this.dungeonZ + 1) && ((int)playerGuy.posZ < this.dungeonZ + 16) && (!this.activeMembers.contains(playerGuy.username.toLowerCase())))
                {
                    this.activeMembers.add(playerGuy.username.toLowerCase());
                }
            }

            for (PartyMember member : this.queuedMembers)
            {
                if ((!this.activeMembers.contains(member.username.toLowerCase())) && (!this.deadMembers.contains(member.username.toLowerCase())))
                {
                    this.deadMembers.add(member.username.toLowerCase());
                }
            }

            boolean allDead = this.deadMembers.size() >= this.queuedMembers.size();
            this.target = this.worldObj.getClosestPlayerToEntity(this, -1.0D);

            if ((this.target != null) && ((this.target instanceof EntityPlayer)))
            {
                EntityPlayer player = (EntityPlayer)this.target;

                if (allDead)
                {
                    resetSlider();
                    return;
                }
            }
            else if (this.target == null)
            {
                this.target = this.worldObj.getClosestPlayerToEntity(this, -1.0D);
            }

            if (isBossStage(0))
            {
                spawnSentries(4, 0);
            }

            if (isBossStage(1))
            {
                spawnSentries(4, 1);
            }

            if (isBossStage(2))
            {
                spawnSentries(4, 2);
            }

            if (isBossStage(3))
            {
                spawnSentries(4, 3);
            }

            if (isBossStage(4))
            {
                spawnSentries(8, 4);
                setCritical(true);
            }
            else
            {
                setCritical(false);
            }

            this.fallDistance = 0.0F;

            if (this.gotMovement)
            {
                if (this.isCollided)
                {
                    double x = this.posX - 0.5D;
                    double y = this.boundingBox.minY + 0.75D;
                    double z = this.posZ - 0.5D;
                    this.crushed = false;

                    if ((y < 124.0D) && (y > 4.0D))
                    {
                        if (this.direction == 0)
                            for (int i = 0; i < 25; i++)
                            {
                                double a = (i / 5 - 2) * 0.75D;
                                double b = (i % 5 - 2) * 0.75D;
                                blockCrush((int)(x + a), (int)(y + 1.5D), (int)(z + b));
                            }
                        else if (this.direction == 1)
                            for (int i = 0; i < 25; i++)
                            {
                                double a = (i / 5 - 2) * 0.75D;
                                double b = (i % 5 - 2) * 0.75D;
                                blockCrush((int)(x + a), (int)(y - 1.5D), (int)(z + b));
                            }
                        else if (this.direction == 2)
                            for (int i = 0; i < 25; i++)
                            {
                                double a = (i / 5 - 2) * 0.75D;
                                double b = (i % 5 - 2) * 0.75D;
                                blockCrush((int)(x + 1.5D), (int)(y + a), (int)(z + b));
                            }
                        else if (this.direction == 3)
                        {
                            for (int i = 0; i < 25; i++)
                            {
                                double a = (i / 5 - 2) * 0.75D;
                                double b = (i % 5 - 2) * 0.75D;
                                blockCrush((int)(x - 1.5D), (int)(y + a), (int)(z + b));
                            }
                        }
                        else if (this.direction == 4)
                        {
                            for (int i = 0; i < 25; i++)
                            {
                                double a = (i / 5 - 2) * 0.75D;
                                double b = (i % 5 - 2) * 0.75D;
                                blockCrush((int)(x + a), (int)(y + b), (int)(z + 1.5D));
                            }
                        }
                        else if (this.direction == 5)
                        {
                            for (int i = 0; i < 25; i++)
                            {
                                double a = (i / 5 - 2) * 0.75D;
                                double b = (i % 5 - 2) * 0.75D;
                                blockCrush((int)(x + a), (int)(y + b), (int)(z - 1.5D));
                            }
                        }
                    }

                    if (this.crushed)
                    {
                        this.worldObj.playSoundEffect(this.posX, this.posY, this.posZ, "random.explode", 3.0F, (0.625F + (this.worldObj.rand.nextFloat() - this.worldObj.rand.nextFloat()) * 0.2F) * 0.7F);
                        this.worldObj.playSoundAtEntity(this, "aeboss.slider.collide", 2.5F, 1.0F / (this.rand.nextFloat() * 0.2F + 0.9F));
                    }

                    stop();
                }
                else
                {
                    if (this.speedy < 2.0F)
                    {
                        this.speedy += (getCritical() ? 0.07F : 0.035F);
                    }

                    this.motionX = 0.0D;
                    this.motionY = 0.0D;
                    this.motionZ = 0.0D;

                    if (this.direction == 0)
                    {
                        this.motionY = this.speedy;

                        if (this.boundingBox.minY > this.target.boundingBox.minY + 0.35D)
                        {
                            stop();
                            this.moveTimer = (getCritical() ? 4 : 8);
                        }
                    }
                    else if (this.direction == 1)
                    {
                        this.motionY = (-this.speedy);

                        if (this.boundingBox.minY < this.target.boundingBox.minY - 0.25D)
                        {
                            stop();
                            this.moveTimer = (getCritical() ? 4 : 8);
                        }
                    }
                    else if (this.direction == 2)
                    {
                        this.motionX = this.speedy;

                        if (this.posX > this.target.posX + 0.125D)
                        {
                            stop();
                            this.moveTimer = (getCritical() ? 4 : 8);
                        }
                    }
                    else if (this.direction == 3)
                    {
                        this.motionX = (-this.speedy);

                        if (this.posX < this.target.posX - 0.125D)
                        {
                            stop();
                            this.moveTimer = (getCritical() ? 4 : 8);
                        }
                    }
                    else if (this.direction == 4)
                    {
                        this.motionZ = this.speedy;

                        if (this.posZ > this.target.posZ + 0.125D)
                        {
                            stop();
                            this.moveTimer = (getCritical() ? 4 : 8);
                        }
                    }
                    else if (this.direction == 5)
                    {
                        this.motionZ = (-this.speedy);

                        if (this.posZ < this.target.posZ - 0.125D)
                        {
                            stop();
                            this.moveTimer = (getCritical() ? 4 : 8);
                        }
                    }
                }
            }
            else
            {
                this.motionY = 0.0D;

                if (this.moveTimer > 0)
                {
                    this.moveTimer -= 1;

                    if ((getCritical()) && (this.rand.nextInt(2) == 0))
                    {
                        this.moveTimer -= 1;
                    }

                    this.motionX = 0.0D;
                    this.motionY = 0.0D;
                    this.motionZ = 0.0D;
                }
                else
                {
                    double a = Math.abs(this.posX - this.target.posX);
                    double b = Math.abs(this.boundingBox.minY - this.target.boundingBox.minY);
                    double c = Math.abs(this.posZ - this.target.posZ);

                    if (a > c)
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

                    if (((b > a) && (b > c)) || ((b > 0.25D) && (this.rand.nextInt(5) == 0)))
                    {
                        this.direction = 0;

                        if (this.posY > this.target.posY)
                        {
                            this.direction = 1;
                        }
                    }

                    this.worldObj.playSoundAtEntity(this, "aeboss.slider.move", 2.5F, 1.0F / (this.rand.nextFloat() * 0.2F + 0.9F));
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
            this.chatTime -= 1;
        }
    }

    private boolean isBossStage(int stage)
    {
        switch (stage)
        {
            case 0:
                return (getHealth() <= 500) && (getHealth() >= 400);

            case 1:
                return (getHealth() < 400) && (getHealth() >= 300);

            case 2:
                return (getHealth() < 300) && (getHealth() >= 200);

            case 3:
                return (getHealth() < 200) && (getHealth() >= 125);

            case 4:
                return getHealth() < 125;
        }

        return false;
    }

    private void spawnSentries(int amount, int stage)
    {
        if (this.stageDone[stage] != 0)
        {
            return;
        }

        if (amount < 0)
        {
            amount = 0;
        }

        int spot = 0;

        for (int sentries = 0; sentries < amount; sentries++)
        {
            if (spot > 3)
            {
                spot = 0;
            }

            int[] coords = getSpotCoords(spot);
            EntitySentry entitysentry = new EntitySentry(this.worldObj);
            entitysentry.setPosition(coords[0] + 0.5D, coords[1] + 1.5D, coords[2] + 0.5D);
            this.worldObj.spawnEntityInWorld(entitysentry);
            spot++;
        }

        this.stageDone[stage] = true;
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
                spotCoords[0] = (x + 13);
                spotCoords[2] = (z + 13);
                break;

            case 1:
                spotCoords[0] = (x + 13);
                spotCoords[2] = (z + 1);
                break;

            case 2:
                spotCoords[0] = (x + 1);
                spotCoords[2] = (z + 13);
                break;

            case 3:
                spotCoords[0] = (x + 1);
                spotCoords[2] = (z + 1);
        }

        spotCoords[1] = y;
        return spotCoords;
    }

    private void openDoor()
    {
        int x = this.dungeonX - 1;

        for (int y = this.dungeonY; y < this.dungeonY + 4; y++)
        {
            for (int z = this.dungeonZ + 6; z < this.dungeonZ + 10; z++)
            {
                this.worldObj.setBlock(x, y, z, 0);
            }
        }
    }

    private void finishDoor()
    {
        for (int y = this.dungeonY; y < this.dungeonY + 4; y++)
        {
            for (int z = this.dungeonZ + 5; z < this.dungeonZ + 10; z++)
            {
                this.worldObj.setBlock(this.dungeonX - 1, y, z, AetherBlocks.BronzeDoor.blockID);
            }
        }

        this.worldObj.setBlock(this.dungeonX - 1, this.dungeonY + 1, this.dungeonZ + 7, AetherBlocks.BronzeDoor.blockID, 1, 2);
        this.worldObj.setBlock(this.dungeonX, this.dungeonY - 1, this.dungeonZ + 7, AetherBlocks.BronzeDoorController.blockID);
    }

    public void applyEntityCollision(Entity entity)
    {
        boolean flag;

        if ((getAwake()) && (this.gotMovement))
        {
            if ((entity instanceof EntitySentry))
            {
                return;
            }

            if ((entity instanceof EntityLiving))
            {
                this.worldObj.playSoundAtEntity(this, "aeboss.slider.collide", 2.5F, 1.0F / (this.rand.nextFloat() * 0.2F + 0.9F));

                if (((entity instanceof EntityCreature)) || ((entity instanceof EntityPlayer)))
                {
                    EntityLiving ek = (EntityLiving)entity;
                    ek.motionY += 0.35D;
                    ek.motionX *= 2.0D;
                    ek.motionZ *= 2.0D;
                }

                stop();
            }

            flag = entity.attackEntityFrom(DamageSource.causeMobDamage(this), 6);
        }
    }

    protected void dropFewItems(boolean var1, int var2)
    {
        for (int i = 0; i < 20 + this.rand.nextInt(10); i++)
        {
            dropItem(AetherBlocks.DungeonStone.blockID, 1);
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

        if ((this.chatTime <= 0) && (Aether.proxy.getClient() != null) && (side.isClient()))
        {
            Aether.proxy.displayMessage(player, s);
            this.chatTime = 60;
        }
    }

    public void teleportMembersFromParty(ArrayList members)
    {
        Side side = FMLCommonHandler.instance().getEffectiveSide();

        if (side.isServer())
        {
            MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
            ServerConfigurationManager configManager = server.getConfigurationManager();
            Object player;

            for (int playerAmount = 0; playerAmount < configManager.playerEntityList.size(); playerAmount++)
            {
                player = configManager.playerEntityList.get(playerAmount);

                for (PartyMember member : members)
                {
                    if (((player instanceof EntityPlayerMP)) && (((EntityPlayerMP)player).username.equalsIgnoreCase(member.username)))
                    {
                        ((EntityPlayerMP)player).setPositionAndUpdate((float)(this.dungeonX + 0.5D), this.dungeonY, (float)(this.dungeonZ + 8.0D));
                    }
                }
            }
        }
    }

    public boolean attackEntityFrom(DamageSource source, int damage)
    {
        if ((source.getEntity() instanceof EntitySentry))
        {
            return false;
        }

        if ((source.getEntity() == null) || (!(source.getEntity() instanceof EntityPlayer)))
        {
            return false;
        }

        EntityPlayer player = (EntityPlayer)source.getEntity();
        ItemStack stack = player.cd();

        if ((stack != null) && (stack.getItem() != null))
        {
            if ((!(stack.getItem() instanceof ItemPickaxe)) && (!(stack.getItem() instanceof ItemTool)))
            {
                chatItUp(player, "Hmm. It's a rock-solid block. A " + stack.getItem().getItemDisplayName(stack) + " wouldn't work on this.");
                return false;
            }
        }
        else
        {
            chatItUp(player, "Hmm. It's a rock-solid block. My fist wouldn't work on this.");
            return false;
        }

        AetherCommonPlayerHandler handler = Aether.getPlayerBase(player);
        Side side = FMLCommonHandler.instance().getEffectiveSide();

        if (handler != null)
        {
            boolean shouldSetBoss = true;

            if ((!player.isDead) && (shouldSetBoss))
            {
                handler.setCurrentBoss(this);
            }
        }

        if ((this.health <= 0) || (this.isDead))
        {
            finishMusic(true);
            playMusicFile("Slider Finish");
        }
        else
        {
            startMusic(true);
        }

        Dungeon dungeon = DungeonHandler.instance().getInstanceAt(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ));
        boolean flag;
        boolean flag;

        if ((dungeon != null) && (dungeon.hasQueuedParty()))
        {
            Party party = dungeon.getQueuedParty();
            int players = dungeon.getQueuedMembers().size();
            float damageFactor = (players - 1) * 0.045F;
            int newDamage = MathHelper.clamp_int((int)(damage - damage * damageFactor), 1, damage);
            flag = super.attackEntityFrom(source, newDamage);
        }
        else
        {
            flag = super.attackEntityFrom(source, Math.max(0, damage));
        }

        if (flag)
        {
            if (dungeon != null)
            {
                this.queuedMembers = dungeon.getQueuedMembers();
            }

            for (int j = 0; j < (this.health <= 0 ? 16 : 48); j++)
            {
                double a = this.posX + (this.rand.nextFloat() - this.rand.nextFloat()) * 1.5D;
                double b = this.boundingBox.minY + 1.75D + (this.rand.nextFloat() - this.rand.nextFloat()) * 1.5D;
                double c = this.posZ + (this.rand.nextFloat() - this.rand.nextFloat()) * 1.5D;

                if (this.health <= 0)
                {
                    this.worldObj.spawnParticle("explode", a, b, c, 0.0D, 0.0D, 0.0D);
                }
            }

            if (this.health <= 0)
            {
                this.isDead = true;
                boolean spawnMusicDisk;

                if (!this.worldObj.isRemote)
                {
                    spawnMusicDisk = false;

                    if (this.random.nextInt(10) == 0)
                    {
                        spawnMusicDisk = true;
                    }

                    for (int amount = 0; amount < 25 + this.random.nextInt(25); amount++)
                    {
                        this.worldObj.spawnEntityInWorld(new EntityAetherCoin(this.worldObj, this.posX, this.posY, this.posZ, 1));
                    }

                    for (int amount = 0; amount < 3; amount++)
                    {
                        this.worldObj.spawnEntityInWorld(new EntityAetherCoin(this.worldObj, this.posX, this.posY, this.posZ, 25));
                    }

                    if (dungeon != null)
                    {
                        ArrayList members = dungeon.getQueuedMembers();

                        for (PartyMember member : members)
                        {
                            for (int rewardCount = 0; rewardCount < 5 + this.random.nextInt(3); rewardCount++)
                            {
                                this.worldObj.spawnEntityInWorld(new EntityRewardItem(this.worldObj, this.posX, this.posY, this.posZ, AetherLoot.BRONZE.getRandomItem(this.random), member.username));
                            }

                            this.worldObj.spawnEntityInWorld(new EntityRewardItem(this.worldObj, this.posX, this.posY, this.posZ, new ItemStack(AetherItems.Key, 1, 0), member.username));

                            if (spawnMusicDisk)
                            {
                                this.worldObj.spawnEntityInWorld(new EntityRewardItem(this.worldObj, this.posX, this.posY, this.posZ, new ItemStack(AetherItems.LabyrinthMusicDisk, 1, 0), member.username));
                            }
                        }
                    }
                }

                openDoor();
                finishMusic(true);
                this.worldObj.playSoundEffect(this.posX, this.posY, this.posZ, "aemisc.achieveBronzeNew", 1.0F, 1.0F);
            }
            else if (!getAwake())
            {
                Side side2 = FMLCommonHandler.instance().getEffectiveSide();

                if ((side2.isServer()) && (!this.worldObj.isRemote) && (dungeon != null))
                {
                    ArrayList members = dungeon.getQueuedMembers();
                    ArrayList teleMembers = new ArrayList();
                    EntityPlayer playerObj = (EntityPlayer)source.getEntity();

                    for (PartyMember member : members)
                    {
                        if (!member.username.equalsIgnoreCase(playerObj.username))
                        {
                            teleMembers.add(member);
                        }
                    }

                    teleportMembersFromParty(teleMembers);
                }

                this.worldObj.playSoundAtEntity(this, "aeboss.slider.awake", 2.5F, 1.0F / (this.rand.nextFloat() * 0.2F + 0.9F));
                setAwake(true);
                this.target = source.getEntity();
                int x = this.dungeonX - 1;

                for (int y = this.dungeonY; y < this.dungeonY + 8; y++)
                {
                    for (int z = this.dungeonZ + 5; z < this.dungeonZ + 11; z++)
                    {
                        this.worldObj.setBlock(x, y, z, AetherBlocks.LockedDungeonStone.blockID);
                    }
                }
            }
            else if (this.gotMovement)
            {
                this.speedy *= 1.5F;
            }

            double a = Math.abs(this.posX - source.getEntity().posX);
            double c = Math.abs(this.posZ - source.getEntity().posZ);

            if (a > c)
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

            this.harvey = (0.7F - this.health / 875.0F);
        }

        return flag;
    }

    private void unlockBlock(int i, int j, int k)
    {
        int id = this.worldObj.getBlockId(i, j, k);

        if (id == AetherBlocks.LockedDungeonStone.blockID)
        {
            this.worldObj.setBlock(i, j, k, AetherBlocks.DungeonStone.blockID, this.worldObj.getBlockMetadata(i, j, k), 4);
            unlockBlock(i + 1, j, k);
            unlockBlock(i - 1, j, k);
            unlockBlock(i, j + 1, k);
            unlockBlock(i, j - 1, k);
            unlockBlock(i, j, k + 1);
            unlockBlock(i, j, k - 1);
        }

        if (id == AetherBlocks.LockedLightDungeonStone.blockID)
        {
            this.worldObj.setBlock(i, j, k, AetherBlocks.LightDungeonStone.blockID, this.worldObj.getBlockMetadata(i, j, k), 4);
            unlockBlock(i + 1, j, k);
            unlockBlock(i - 1, j, k);
            unlockBlock(i, j + 1, k);
            unlockBlock(i, j - 1, k);
            unlockBlock(i, j, k + 1);
            unlockBlock(i, j, k - 1);
        }
    }

    public void addVelocity(double d, double d1, double d2)
    {
    }

    public void knockBack(Entity entity, int i, double d, double d1)
    {
    }

    public void blockCrush(int x, int y, int z)
    {
        if (!this.worldObj.isRemote)
        {
            int a = this.worldObj.getBlockId(x, y, z);
            int b = this.worldObj.getBlockMetadata(x, y, z);
            Collections.addAll(this.blockBans, new Integer[] { Integer.valueOf(AetherBlocks.LockedDungeonStone.blockID), Integer.valueOf(AetherBlocks.LockedLightDungeonStone.blockID), Integer.valueOf(AetherBlocks.TreasureChest.blockID), Integer.valueOf(AetherBlocks.DungeonHolystone.blockID), Integer.valueOf(AetherBlocks.DungeonEntrance.blockID), Integer.valueOf(AetherBlocks.DungeonEntranceController.blockID), Integer.valueOf(AetherBlocks.BronzeDoor.blockID), Integer.valueOf(AetherBlocks.BronzeDoorController.blockID) });

            if ((a == 0) || (this.blockBans.contains(Integer.valueOf(a))))
            {
                return;
            }

            Block.blocksList[a].onBlockDestroyedByPlayer(this.worldObj, x, y, z, 0);
            Block.blocksList[a].dropBlockAsItem(this.worldObj, x, y, z, b, 1);
            this.worldObj.setBlock(x, y, z, 0);
            this.crushed = true;

            if (this.worldObj.isRemote)
            {
                FMLClientHandler.instance().getClient().effectRenderer.a(x, y, z, a, b);
            }
        }

        if (this.worldObj.isRemote)
        {
            if (FMLClientHandler.instance().getClient().gameSettings.fancyGraphics)
            {
                addSquirrelButts(x, y, z);
            }
        }
    }

    public void addSquirrelButts(int x, int y, int z)
    {
        if (this.worldObj.isRemote)
        {
            double a = x + 0.5D + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.375D;
            double b = y + 0.5D + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.375D;
            double c = z + 0.5D + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.375D;
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
        this.dataWatcher.updateObject(26, Integer.valueOf(this.health));
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
        return getBossName() + ", the Slider";
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

