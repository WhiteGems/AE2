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
import net.aetherteam.aether.oldcode.NameGenerator;
import net.aetherteam.aether.packets.AetherPacketHandler;
import net.aetherteam.aether.party.Party;
import net.aetherteam.aether.party.PartyController;
import net.aetherteam.aether.party.members.PartyMember;
import net.minecraft.block.Block;
import net.minecraft.client.audio.SoundManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
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
    public String dir;
    private boolean[] stageDone;
    public Random random;
    public ArrayList queuedMembers;
    public ArrayList deadMembers;
    ArrayList activeMembers;
    public AxisAlignedBB originalAABB;
    public ArrayList blockBans;
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

    public EntitySlider(World var1)
    {
        super(var1);
        this.dir = "/net/aetherteam/aether/client/sprites";
        this.stageDone = new boolean[5];
        this.random = new Random();
        this.queuedMembers = new ArrayList();
        this.deadMembers = new ArrayList();
        this.activeMembers = new ArrayList();
        this.blockBans = new ArrayList();
        this.rotationYaw = 0.0F;
        this.rotationPitch = 0.0F;
        this.setSize(2.0F, 2.0F);
        this.health = this.getMaxHealth();
        this.dennis = 1;
        this.jumpMovementFactor = 0.0F;
        this.chatTime = 60;
        this.stageDone[0] = false;
        this.stageDone[1] = false;
        this.stageDone[2] = false;
        this.stageDone[3] = false;
        this.stageDone[4] = false;
        this.texture = this.dir + "/bosses/slider/sliderSleep.png";
        this.setBossName(NameGenerator.next(6, 12));
        this.originalAABB = AxisAlignedBB.getBoundingBox(-7.0D, -1.0D, -7.5D, 7.0D, 9.0D, 7.5D).addCoord(this.posX, this.posY, this.posZ);
    }

    public EntitySlider(World var1, double var2, double var4, double var6)
    {
        this(var1);
        this.setPosition(var2, var4, var6);
    }

    public int getMaxHealth()
    {
        return 500;
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
        this.dataWatcher.addObject(16, Byte.valueOf((byte) 0));
        this.dataWatcher.addObject(17, Byte.valueOf((byte) 0));
        this.dataWatcher.addObject(18, String.valueOf(AetherNameGen.gen()));
        this.dataWatcher.addObject(19, Byte.valueOf((byte) 0));
        this.dataWatcher.addObject(20, Byte.valueOf((byte) 0));
        this.dataWatcher.addObject(21, Byte.valueOf((byte) 0));
    }

    public boolean getAwake()
    {
        return (this.dataWatcher.getWatchableObjectByte(16) & 1) != 0;
    }

    public void setAwake(boolean var1)
    {
        if (!this.worldObj.isRemote)
        {
            if (var1)
            {
                this.dataWatcher.updateObject(16, Byte.valueOf((byte) 1));
            } else
            {
                this.dataWatcher.updateObject(16, Byte.valueOf((byte) 0));
            }
        }
    }

    /**
     * Called when the mob's health reaches 0.
     */
    public void onDeath(DamageSource var1)
    {
        this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, 0.3F, false);

        if (var1.getEntity() instanceof EntityPlayer)
        {
            EntityPlayer var2 = (EntityPlayer) var1.getEntity();
            Party var3 = PartyController.instance().getParty(PartyController.instance().getMember(var2));
            Dungeon var4 = DungeonHandler.instance().getInstanceAt(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ));

            if (var4 != null && var3 != null)
            {
                Side var5 = FMLCommonHandler.instance().getEffectiveSide();

                if (var5.isServer())
                {
                    DungeonHandler.instance().startTimer(var4, var3, 60);
                    PacketDispatcher.sendPacketToAllPlayers(AetherPacketHandler.sendDungeonTimerStart(var4, var3, 60));
                }
            }
        }

        super.onDeath(var1);
    }

    public boolean getCritical()
    {
        return (this.dataWatcher.getWatchableObjectByte(17) & 1) != 0;
    }

    public void setCritical(boolean var1)
    {
        if (!this.worldObj.isRemote)
        {
            if (var1)
            {
                this.dataWatcher.updateObject(17, Byte.valueOf((byte) 1));
            } else
            {
                this.dataWatcher.updateObject(17, Byte.valueOf((byte) 0));
            }
        }
    }

    public String getBossName()
    {
        return this.dataWatcher.getWatchableObjectString(18);
    }

    public void setBossName(String var1)
    {
        if (!this.worldObj.isRemote)
        {
            this.dataWatcher.updateObject(18, String.valueOf(var1));
        }
    }

    public boolean hasStartedMusic()
    {
        return (this.dataWatcher.getWatchableObjectByte(19) & 1) != 0;
    }

    public void startMusic(boolean var1)
    {
        if (var1)
        {
            this.dataWatcher.updateObject(19, Byte.valueOf((byte) 1));
        } else
        {
            this.dataWatcher.updateObject(19, Byte.valueOf((byte) 0));
        }
    }

    public boolean hasPlayedMusic()
    {
        return (this.dataWatcher.getWatchableObjectByte(20) & 1) != 0;
    }

    public void playMusic(boolean var1)
    {
        if (var1)
        {
            this.dataWatcher.updateObject(20, Byte.valueOf((byte) 1));
        } else
        {
            this.dataWatcher.updateObject(20, Byte.valueOf((byte) 0));
        }
    }

    public boolean hasFinishedSoundtrack()
    {
        return (this.dataWatcher.getWatchableObjectByte(21) & 1) != 0;
    }

    public void finishMusic(boolean var1)
    {
        if (var1)
        {
            this.dataWatcher.updateObject(21, Byte.valueOf((byte) 1));
            this.playMusicFile("Slider Finish");
        } else
        {
            this.dataWatcher.updateObject(21, Byte.valueOf((byte) 0));
        }
    }

    /**
     * Takes in the distance the entity has fallen this tick and whether its on the ground to update the fall distance
     * and deal fall damage if landing on the ground.  Args: distanceFallenThisTick, onGround
     */
    protected void updateFallState(double var1, boolean var3)
    {}

    /**
     * Called when the mob is falling. Calculates and applies fall damage.
     */
    protected void fall(float var1)
    {}

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
        return "aeboss.slider.die";
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound var1)
    {
        super.writeEntityToNBT(var1);
        var1.setFloat("Speedy", this.speedy);
        var1.setShort("MoveTimer", (short) this.moveTimer);
        var1.setShort("Direction", (short) this.direction);
        var1.setBoolean("GotMovement", this.gotMovement);
        var1.setBoolean("Awake", this.getAwake());
        var1.setBoolean("Critical", this.getCritical());
        var1.setInteger("DungeonX", this.dungeonX);
        var1.setInteger("DungeonY", this.dungeonY);
        var1.setInteger("DungeonZ", this.dungeonZ);
        var1.setString("BossName", this.getBossName());
        var1.setBoolean("PlayedMusic", this.hasPlayedMusic());
        var1.setBoolean("StartedMusic", this.hasStartedMusic());
        var1.setBoolean("FinishedMusic", this.hasFinishedSoundtrack());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound var1)
    {
        super.readEntityFromNBT(var1);
        this.speedy = var1.getFloat("Speedy");
        this.moveTimer = var1.getShort("MoveTimer");
        this.direction = var1.getShort("Direction");
        this.gotMovement = var1.getBoolean("GotMovement");
        this.setAwake(var1.getBoolean("Awake"));
        this.setCritical(var1.getBoolean("Critical"));
        this.dungeonX = var1.getInteger("DungeonX");
        this.dungeonY = var1.getInteger("DungeonY");
        this.dungeonZ = var1.getInteger("DungeonZ");
        this.setBossName(var1.getString("BossName"));
        this.playMusic(var1.getBoolean("PlayedMusic"));
        this.startMusic(var1.getBoolean("StartedMusic"));
        this.finishMusic(var1.getBoolean("FinishedMusic"));
    }

    public boolean isSoundOn()
    {
        boolean var1;

        if (this.worldObj.isRemote && this.isClient() && Aether.proxy.getClient().sndManager != null)
        {
            SoundManager var10000 = Aether.proxy.getClient().sndManager;

            if (SoundManager.sndSystem != null)
            {
                var1 = true;
                return var1;
            }
        }

        var1 = false;
        return var1;
    }

    public boolean isClient()
    {
        return Aether.proxy.getClient() != null;
    }

    public boolean isMusicPlaying()
    {
        boolean var1;

        if (Aether.proxy.getClient() != null)
        {
            SoundManager var10000 = Aether.proxy.getClient().sndManager;

            if (SoundManager.sndSystem != null)
            {
                var10000 = Aether.proxy.getClient().sndManager;

                if (SoundManager.sndSystem.playing("streaming"))
                {
                    var1 = true;
                    return var1;
                }
            }
        }

        var1 = false;
        return var1;
    }

    public void turnMusicOff()
    {
        if (this.isSoundOn())
        {
            ;
        }
    }

    public void playMusicFile(String var1)
    {
        if (this.isSoundOn())
        {
            float var10002 = (float) this.posX;
            float var10003 = (float) this.posY;
            Aether.proxy.getClient().sndManager.playStreaming(var1, var10002, var10003, (float) this.posZ);
        }
    }

    public boolean isDead()
    {
        return this.health <= 0 || this.isDead;
    }

    public void resetSlider()
    {
        this.setAwake(false);
        this.stop();
        this.openDoor();
        this.moveTimer = 0;
        this.health = 500;

        if (!this.worldObj.isRemote)
        {
            this.setPosition((double) ((float) this.dungeonX + 8.0F), (double) ((float) this.dungeonY + 1.0F), (double) ((float) this.dungeonZ + 8.0F));
        }

        List var1 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(35.0D, 15.0D, 35.0D));

        for (int var2 = 0; var2 < var1.size(); ++var2)
        {
            Entity var3 = (Entity) var1.get(var2);

            if (var3 instanceof EntitySentry)
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

        if (!this.worldObj.isRemote && this.getBossHP() != this.health)
        {
            this.setBossHP();
        }

        DungeonHandler var1 = DungeonHandler.instance();
        Dungeon var2 = var1.getInstanceAt(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ));

        if (var2 != null && this.dungeonX == 0 && this.dungeonY == 0 && this.dungeonZ == 0)
        {
            this.setDungeon((int) this.posX - 8, (int) this.posY - 2, (int) this.posZ - 8);
        }

        if (var2 != null)
        {
            if (!var2.isActive())
            {
                this.setDead();
            }

            if (this.getAwake())
            {
                this.queuedMembers = var2.getQueuedMembers();
            }
        }

        this.jumpMovementFactor = 0.0F;
        this.renderYawOffset = this.rotationPitch = this.rotationYaw = 0.0F;

        if ((!this.hasPlayedMusic() || !this.isMusicPlaying() && this.hasPlayedMusic()) && this.hasStartedMusic() && !this.hasFinishedSoundtrack() && !this.isDead() && this.getAwake())
        {
            if (!this.hasFinishedSoundtrack())
            {
                this.playMusicFile("Slider Battle");
            }

            this.playMusic(true);
        }

        if (this.hasFinishedSoundtrack() || this.isDead() || this.health <= 0)
        {
            this.playMusicFile("Slider Finish");
        }

        if (this.getCritical())
        {
            this.texture = this.dir + "/bosses/slider/sliderAwake_red.png";
        } else if (this.getAwake())
        {
            this.texture = this.dir + "/bosses/slider/sliderAwake.png";
        } else
        {
            this.texture = this.dir + "/bosses/slider/sliderSleep.png";
        }

        if (this.getAwake())
        {
            this.activeMembers.clear();
            Iterator var3 = this.queuedMembers.iterator();
            PartyMember var4;

            while (var3.hasNext())
            {
                var4 = (PartyMember) var3.next();
                EntityPlayer var5 = this.worldObj.getPlayerEntityByName(var4.username);

                if (var5 != null && (int) var5.posX >= this.dungeonX && (int) var5.posX < this.dungeonX + 16 && (int) var5.posZ >= this.dungeonZ + 1 && (int) var5.posZ < this.dungeonZ + 16 && !this.activeMembers.contains(var5.username.toLowerCase()))
                {
                    this.activeMembers.add(var5.username.toLowerCase());
                }
            }

            var3 = this.queuedMembers.iterator();

            while (var3.hasNext())
            {
                var4 = (PartyMember) var3.next();

                if (!this.activeMembers.contains(var4.username.toLowerCase()) && !this.deadMembers.contains(var4.username.toLowerCase()))
                {
                    this.deadMembers.add(var4.username.toLowerCase());
                }
            }

            boolean var17 = this.deadMembers.size() >= this.queuedMembers.size();
            this.target = this.worldObj.getClosestPlayerToEntity(this, -1.0D);

            if (this.target != null && this.target instanceof EntityPlayer)
            {
                EntityPlayer var16 = (EntityPlayer) this.target;

                if (var17)
                {
                    this.resetSlider();
                    return;
                }
            } else if (this.target == null)
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
            } else
            {
                this.setCritical(false);
            }

            this.fallDistance = 0.0F;
            double var6;
            double var15;
            double var8;

            if (this.gotMovement)
            {
                if (this.isCollided)
                {
                    var15 = this.posX - 0.5D;
                    var6 = this.boundingBox.minY + 0.75D;
                    var8 = this.posZ - 0.5D;
                    this.crushed = false;

                    if (var6 < 124.0D && var6 > 4.0D)
                    {
                        double var13;
                        int var10;
                        double var11;

                        if (this.direction == 0)
                        {
                            for (var10 = 0; var10 < 25; ++var10)
                            {
                                var11 = (double) (var10 / 5 - 2) * 0.75D;
                                var13 = (double) (var10 % 5 - 2) * 0.75D;
                                this.blockCrush((int) (var15 + var11), (int) (var6 + 1.5D), (int) (var8 + var13));
                            }
                        } else if (this.direction == 1)
                        {
                            for (var10 = 0; var10 < 25; ++var10)
                            {
                                var11 = (double) (var10 / 5 - 2) * 0.75D;
                                var13 = (double) (var10 % 5 - 2) * 0.75D;
                                this.blockCrush((int) (var15 + var11), (int) (var6 - 1.5D), (int) (var8 + var13));
                            }
                        } else if (this.direction == 2)
                        {
                            for (var10 = 0; var10 < 25; ++var10)
                            {
                                var11 = (double) (var10 / 5 - 2) * 0.75D;
                                var13 = (double) (var10 % 5 - 2) * 0.75D;
                                this.blockCrush((int) (var15 + 1.5D), (int) (var6 + var11), (int) (var8 + var13));
                            }
                        } else if (this.direction == 3)
                        {
                            for (var10 = 0; var10 < 25; ++var10)
                            {
                                var11 = (double) (var10 / 5 - 2) * 0.75D;
                                var13 = (double) (var10 % 5 - 2) * 0.75D;
                                this.blockCrush((int) (var15 - 1.5D), (int) (var6 + var11), (int) (var8 + var13));
                            }
                        } else if (this.direction == 4)
                        {
                            for (var10 = 0; var10 < 25; ++var10)
                            {
                                var11 = (double) (var10 / 5 - 2) * 0.75D;
                                var13 = (double) (var10 % 5 - 2) * 0.75D;
                                this.blockCrush((int) (var15 + var11), (int) (var6 + var13), (int) (var8 + 1.5D));
                            }
                        } else if (this.direction == 5)
                        {
                            for (var10 = 0; var10 < 25; ++var10)
                            {
                                var11 = (double) (var10 / 5 - 2) * 0.75D;
                                var13 = (double) (var10 % 5 - 2) * 0.75D;
                                this.blockCrush((int) (var15 + var11), (int) (var6 + var13), (int) (var8 - 1.5D));
                            }
                        }
                    }

                    if (this.crushed)
                    {
                        this.worldObj.playSoundEffect(this.posX, this.posY, this.posZ, "random.explode", 3.0F, (0.625F + (this.worldObj.rand.nextFloat() - this.worldObj.rand.nextFloat()) * 0.2F) * 0.7F);
                        this.worldObj.playSoundAtEntity(this, "aeboss.slider.collide", 2.5F, 1.0F / (this.rand.nextFloat() * 0.2F + 0.9F));
                    }

                    this.stop();
                } else
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
                        this.motionY = (double) this.speedy;

                        if (this.boundingBox.minY > this.target.boundingBox.minY + 0.35D)
                        {
                            this.stop();
                            this.moveTimer = this.getCritical() ? 4 : 8;
                        }
                    } else if (this.direction == 1)
                    {
                        this.motionY = (double) (-this.speedy);

                        if (this.boundingBox.minY < this.target.boundingBox.minY - 0.25D)
                        {
                            this.stop();
                            this.moveTimer = this.getCritical() ? 4 : 8;
                        }
                    } else if (this.direction == 2)
                    {
                        this.motionX = (double) this.speedy;

                        if (this.posX > this.target.posX + 0.125D)
                        {
                            this.stop();
                            this.moveTimer = this.getCritical() ? 4 : 8;
                        }
                    } else if (this.direction == 3)
                    {
                        this.motionX = (double) (-this.speedy);

                        if (this.posX < this.target.posX - 0.125D)
                        {
                            this.stop();
                            this.moveTimer = this.getCritical() ? 4 : 8;
                        }
                    } else if (this.direction == 4)
                    {
                        this.motionZ = (double) this.speedy;

                        if (this.posZ > this.target.posZ + 0.125D)
                        {
                            this.stop();
                            this.moveTimer = this.getCritical() ? 4 : 8;
                        }
                    } else if (this.direction == 5)
                    {
                        this.motionZ = (double) (-this.speedy);

                        if (this.posZ < this.target.posZ - 0.125D)
                        {
                            this.stop();
                            this.moveTimer = this.getCritical() ? 4 : 8;
                        }
                    }
                }
            } else
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
                } else
                {
                    var15 = Math.abs(this.posX - this.target.posX);
                    var6 = Math.abs(this.boundingBox.minY - this.target.boundingBox.minY);
                    var8 = Math.abs(this.posZ - this.target.posZ);

                    if (var15 > var8)
                    {
                        this.direction = 2;

                        if (this.posX > this.target.posX)
                        {
                            this.direction = 3;
                        }
                    } else
                    {
                        this.direction = 4;

                        if (this.posZ > this.target.posZ)
                        {
                            this.direction = 5;
                        }
                    }

                    if (var6 > var15 && var6 > var8 || var6 > 0.25D && this.rand.nextInt(5) == 0)
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
            --this.chatTime;
        }
    }

    private boolean isBossStage(int var1)
    {
        switch (var1)
        {
            case 0:
                return this.getHealth() <= 500 && this.getHealth() >= 400;

            case 1:
                return this.getHealth() < 400 && this.getHealth() >= 300;

            case 2:
                return this.getHealth() < 300 && this.getHealth() >= 200;

            case 3:
                return this.getHealth() < 200 && this.getHealth() >= 125;

            case 4:
                return this.getHealth() < 125;

            default:
                return false;
        }
    }

    private void spawnSentries(int var1, int var2)
    {
        if (!this.stageDone[var2])
        {
            if (var1 < 0)
            {
                var1 = 0;
            }

            int var3 = 0;

            for (int var5 = 0; var5 < var1; ++var5)
            {
                if (var3 > 3)
                {
                    var3 = 0;
                }

                int[] var4 = this.getSpotCoords(var3);
                EntitySentry var6 = new EntitySentry(this.worldObj);
                var6.setPosition((double) var4[0] + 0.5D, (double) var4[1] + 1.5D, (double) var4[2] + 0.5D);
                this.worldObj.spawnEntityInWorld(var6);
                ++var3;
            }

            this.stageDone[var2] = true;
        }
    }

    private int[] getSpotCoords(int var1)
    {
        int[] var2 = new int[3];
        int var3 = this.dungeonX;
        int var4 = this.dungeonY;
        int var5 = this.dungeonZ;

        switch (var1)
        {
            case 0:
                var2[0] = var3 + 13;
                var2[2] = var5 + 13;
                break;

            case 1:
                var2[0] = var3 + 13;
                var2[2] = var5 + 1;
                break;

            case 2:
                var2[0] = var3 + 1;
                var2[2] = var5 + 13;
                break;

            case 3:
                var2[0] = var3 + 1;
                var2[2] = var5 + 1;
        }

        var2[1] = var4;
        return var2;
    }

    private void openDoor()
    {
        int var1 = this.dungeonX - 1;

        for (int var2 = this.dungeonY; var2 < this.dungeonY + 4; ++var2)
        {
            for (int var3 = this.dungeonZ + 6; var3 < this.dungeonZ + 10; ++var3)
            {
                this.worldObj.setBlock(var1, var2, var3, 0);
            }
        }
    }

    private void finishDoor()
    {
        for (int var1 = this.dungeonY; var1 < this.dungeonY + 4; ++var1)
        {
            for (int var2 = this.dungeonZ + 5; var2 < this.dungeonZ + 10; ++var2)
            {
                this.worldObj.setBlock(this.dungeonX - 1, var1, var2, AetherBlocks.BronzeDoor.blockID);
            }
        }

        this.worldObj.setBlock(this.dungeonX - 1, this.dungeonY + 1, this.dungeonZ + 7, AetherBlocks.BronzeDoor.blockID, 1, 2);
        this.worldObj.setBlock(this.dungeonX, this.dungeonY - 1, this.dungeonZ + 7, AetherBlocks.BronzeDoorController.blockID);
    }

    /**
     * Applies a velocity to each of the entities pushing them away from each other. Args: entity
     */
    public void applyEntityCollision(Entity var1)
    {
        if (this.getAwake() && this.gotMovement)
        {
            if (var1 instanceof EntitySentry)
            {
                return;
            }

            if (var1 instanceof EntityLiving)
            {
                this.worldObj.playSoundAtEntity(this, "aeboss.slider.collide", 2.5F, 1.0F / (this.rand.nextFloat() * 0.2F + 0.9F));

                if (var1 instanceof EntityCreature || var1 instanceof EntityPlayer)
                {
                    EntityLiving var2 = (EntityLiving) var1;
                    var2.motionY += 0.35D;
                    var2.motionX *= 2.0D;
                    var2.motionZ *= 2.0D;
                }

                this.stop();
            }

            var1.attackEntityFrom(DamageSource.causeMobDamage(this), 6);
        }
    }

    /**
     * Drop 0-2 items of this living's type. @param par1 - Whether this entity has recently been hit by a player. @param
     * par2 - Level of Looting used to kill this mob.
     */
    protected void dropFewItems(boolean var1, int var2)
    {
        for (int var3 = 0; var3 < 20 + this.rand.nextInt(10); ++var3)
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

    private void chatItUp(EntityPlayer var1, String var2)
    {
        Side var3 = FMLCommonHandler.instance().getEffectiveSide();

        if (this.chatTime <= 0 && Aether.proxy.getClient() != null && var3.isClient())
        {
            Aether.proxy.displayMessage(var1, var2);
            this.chatTime = 60;
        }
    }

    public void teleportMembersFromParty(ArrayList var1)
    {
        Side var2 = FMLCommonHandler.instance().getEffectiveSide();

        if (var2.isServer())
        {
            MinecraftServer var3 = FMLCommonHandler.instance().getMinecraftServerInstance();
            ServerConfigurationManager var4 = var3.getConfigurationManager();

            for (int var5 = 0; var5 < var4.playerEntityList.size(); ++var5)
            {
                Object var6 = var4.playerEntityList.get(var5);
                Iterator var7 = var1.iterator();

                while (var7.hasNext())
                {
                    PartyMember var8 = (PartyMember) var7.next();

                    if (var6 instanceof EntityPlayerMP && ((EntityPlayerMP) var6).username.equalsIgnoreCase(var8.username))
                    {
                        ((EntityPlayerMP) var6).setPositionAndUpdate((double) ((float) ((double) this.dungeonX + 0.5D)), (double) ((float) this.dungeonY), (double) ((float) ((double) this.dungeonZ + 8.0D)));
                    }
                }
            }
        }
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource var1, int var2)
    {
        if (var1.getEntity() instanceof EntitySentry)
        {
            return false;
        } else if (var1.getEntity() != null && var1.getEntity() instanceof EntityPlayer)
        {
            EntityPlayer var3 = (EntityPlayer) var1.getEntity();
            ItemStack var4 = var3.getCurrentEquippedItem();

            if (var4 != null && var4.getItem() != null)
            {
                if (!(var4.getItem() instanceof ItemPickaxe) && !(var4.getItem() instanceof ItemTool))
                {
                    this.chatItUp(var3, "嗯~，一个石头一样的怪家伙。用" + var4.getItem().getItemDisplayName(var4) + "估计对付不了这玩意儿！");
                    return false;
                } else
                {
                    AetherCommonPlayerHandler var5 = Aether.getPlayerBase(var3);
                    Side var6 = FMLCommonHandler.instance().getEffectiveSide();
                    boolean var7;

                    if (var5 != null)
                    {
                        var7 = true;

                        if (!var3.isDead && var7)
                        {
                            var5.setCurrentBoss(this);
                        }
                    }

                    if (this.health > 0 && !this.isDead)
                    {
                        this.startMusic(true);
                    } else
                    {
                        this.finishMusic(true);
                        this.playMusicFile("Slider Finish");
                    }

                    Dungeon var8 = DungeonHandler.instance().getInstanceAt(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ));
                    int var10;
                    int var12;

                    if (var8 != null && var8.hasQueuedParty())
                    {
                        Party var9 = var8.getQueuedParty();
                        var10 = var8.getQueuedMembers().size();
                        float var11 = (float) (var10 - 1) * 0.045F;
                        var12 = MathHelper.clamp_int((int) ((float) var2 - (float) var2 * var11), 1, var2);
                        var7 = super.attackEntityFrom(var1, var12);
                    } else
                    {
                        var7 = super.attackEntityFrom(var1, Math.max(0, var2));
                    }

                    if (var7)
                    {
                        if (var8 != null)
                        {
                            this.queuedMembers = var8.getQueuedMembers();
                        }

                        for (int var29 = 0; var29 < (this.health <= 0 ? 16 : 48); ++var29)
                        {
                            double var28 = this.posX + (double) (this.rand.nextFloat() - this.rand.nextFloat()) * 1.5D;
                            double var19 = this.boundingBox.minY + 1.75D + (double) (this.rand.nextFloat() - this.rand.nextFloat()) * 1.5D;
                            double var14 = this.posZ + (double) (this.rand.nextFloat() - this.rand.nextFloat()) * 1.5D;

                            if (this.health <= 0)
                            {
                                this.worldObj.spawnParticle("explode", var28, var19, var14, 0.0D, 0.0D, 0.0D);
                            }
                        }

                        ArrayList var24;

                        if (this.health <= 0)
                        {
                            this.isDead = true;

                            if (!this.worldObj.isRemote)
                            {
                                boolean var30 = false;

                                if (this.random.nextInt(10) == 0)
                                {
                                    var30 = true;
                                }

                                for (var10 = 0; var10 < 25 + this.random.nextInt(25); ++var10)
                                {
                                    this.worldObj.spawnEntityInWorld(new EntityAetherCoin(this.worldObj, this.posX, this.posY, this.posZ, 1));
                                }

                                for (var10 = 0; var10 < 3; ++var10)
                                {
                                    this.worldObj.spawnEntityInWorld(new EntityAetherCoin(this.worldObj, this.posX, this.posY, this.posZ, 25));
                                }

                                if (var8 != null)
                                {
                                    var24 = var8.getQueuedMembers();
                                    Iterator var27 = var24.iterator();

                                    while (var27.hasNext())
                                    {
                                        PartyMember var21 = (PartyMember) var27.next();

                                        for (int var13 = 0; var13 < 5 + this.random.nextInt(3); ++var13)
                                        {
                                            this.worldObj.spawnEntityInWorld(new EntityRewardItem(this.worldObj, this.posX, this.posY, this.posZ, AetherLoot.BRONZE.getRandomItem(this.random), var21.username));
                                        }

                                        this.worldObj.spawnEntityInWorld(new EntityRewardItem(this.worldObj, this.posX, this.posY, this.posZ, new ItemStack(AetherItems.Key, 1, 0), var21.username));

                                        if (var30)
                                        {
                                            this.worldObj.spawnEntityInWorld(new EntityRewardItem(this.worldObj, this.posX, this.posY, this.posZ, new ItemStack(AetherItems.LabyrinthMusicDisk, 1, 0), var21.username));
                                        }
                                    }
                                }
                            }

                            this.openDoor();
                            this.finishMusic(true);
                            this.worldObj.playSoundEffect(this.posX, this.posY, this.posZ, "aemisc.achieveBronzeNew", 1.0F, 1.0F);
                        } else if (!this.getAwake())
                        {
                            Side var25 = FMLCommonHandler.instance().getEffectiveSide();

                            if (var25.isServer() && !this.worldObj.isRemote && var8 != null)
                            {
                                var24 = var8.getQueuedMembers();
                                ArrayList var22 = new ArrayList();
                                EntityPlayer var18 = (EntityPlayer) var1.getEntity();
                                Iterator var17 = var24.iterator();

                                while (var17.hasNext())
                                {
                                    PartyMember var16 = (PartyMember) var17.next();

                                    if (!var16.username.equalsIgnoreCase(var18.username))
                                    {
                                        var22.add(var16);
                                    }
                                }

                                this.teleportMembersFromParty(var22);
                            }

                            this.worldObj.playSoundAtEntity(this, "aeboss.slider.awake", 2.5F, 1.0F / (this.rand.nextFloat() * 0.2F + 0.9F));
                            this.setAwake(true);
                            this.target = var1.getEntity();
                            var10 = this.dungeonX - 1;

                            for (int var23 = this.dungeonY; var23 < this.dungeonY + 8; ++var23)
                            {
                                for (var12 = this.dungeonZ + 5; var12 < this.dungeonZ + 11; ++var12)
                                {
                                    this.worldObj.setBlock(var10, var23, var12, AetherBlocks.LockedDungeonStone.blockID);
                                }
                            }
                        } else if (this.gotMovement)
                        {
                            this.speedy *= 1.5F;
                        }

                        double var26 = Math.abs(this.posX - var1.getEntity().posX);
                        double var20 = Math.abs(this.posZ - var1.getEntity().posZ);

                        if (var26 > var20)
                        {
                            this.dennis = 1;
                            this.rennis = 0;

                            if (this.posX > var1.getEntity().posX)
                            {
                                this.dennis = -1;
                            }
                        } else
                        {
                            this.rennis = 1;
                            this.dennis = 0;

                            if (this.posZ > var1.getEntity().posZ)
                            {
                                this.rennis = -1;
                            }
                        }

                        this.harvey = 0.7F - (float) this.health / 875.0F;
                    }

                    return var7;
                }
            } else
            {
                this.chatItUp(var3, "Hmm. It\'s a rock-solid block. My fist wouldn\'t work on this.");
                return false;
            }
        } else
        {
            return false;
        }
    }

    private void unlockBlock(int var1, int var2, int var3)
    {
        int var4 = this.worldObj.getBlockId(var1, var2, var3);

        if (var4 == AetherBlocks.LockedDungeonStone.blockID)
        {
            this.worldObj.setBlock(var1, var2, var3, AetherBlocks.DungeonStone.blockID, this.worldObj.getBlockMetadata(var1, var2, var3), 4);
            this.unlockBlock(var1 + 1, var2, var3);
            this.unlockBlock(var1 - 1, var2, var3);
            this.unlockBlock(var1, var2 + 1, var3);
            this.unlockBlock(var1, var2 - 1, var3);
            this.unlockBlock(var1, var2, var3 + 1);
            this.unlockBlock(var1, var2, var3 - 1);
        }

        if (var4 == AetherBlocks.LockedLightDungeonStone.blockID)
        {
            this.worldObj.setBlock(var1, var2, var3, AetherBlocks.LightDungeonStone.blockID, this.worldObj.getBlockMetadata(var1, var2, var3), 4);
            this.unlockBlock(var1 + 1, var2, var3);
            this.unlockBlock(var1 - 1, var2, var3);
            this.unlockBlock(var1, var2 + 1, var3);
            this.unlockBlock(var1, var2 - 1, var3);
            this.unlockBlock(var1, var2, var3 + 1);
            this.unlockBlock(var1, var2, var3 - 1);
        }
    }

    /**
     * Adds to the current velocity of the entity. Args: x, y, z
     */
    public void addVelocity(double var1, double var3, double var5)
    {}

    /**
     * knocks back this entity
     */
    public void knockBack(Entity var1, int var2, double var3, double var5)
    {}

    public void blockCrush(int var1, int var2, int var3)
    {
        if (!this.worldObj.isRemote)
        {
            int var4 = this.worldObj.getBlockId(var1, var2, var3);
            int var5 = this.worldObj.getBlockMetadata(var1, var2, var3);
            Collections.addAll(this.blockBans, new Integer[]{Integer.valueOf(AetherBlocks.LockedDungeonStone.blockID), Integer.valueOf(AetherBlocks.LockedLightDungeonStone.blockID), Integer.valueOf(AetherBlocks.TreasureChest.blockID), Integer.valueOf(AetherBlocks.DungeonHolystone.blockID), Integer.valueOf(AetherBlocks.DungeonEntrance.blockID), Integer.valueOf(AetherBlocks.DungeonEntranceController.blockID), Integer.valueOf(AetherBlocks.BronzeDoor.blockID), Integer.valueOf(AetherBlocks.BronzeDoorController.blockID)});

            if (var4 == 0 || this.blockBans.contains(Integer.valueOf(var4)))
            {
                return;
            }

            Block.blocksList[var4].onBlockDestroyedByPlayer(this.worldObj, var1, var2, var3, 0);
            Block.blocksList[var4].dropBlockAsItem(this.worldObj, var1, var2, var3, var5, 1);
            this.worldObj.setBlock(var1, var2, var3, 0);
            this.crushed = true;

            if (this.worldObj.isRemote)
            {
                FMLClientHandler.instance().getClient().effectRenderer.addBlockDestroyEffects(var1, var2, var3, var4, var5);
            }
        }

        if (this.worldObj.isRemote && FMLClientHandler.instance().getClient().gameSettings.fancyGraphics)
        {
            this.addSquirrelButts(var1, var2, var3);
        }
    }

    public void addSquirrelButts(int var1, int var2, int var3)
    {
        if (this.worldObj.isRemote)
        {
            double var4 = (double) var1 + 0.5D + (double) (this.rand.nextFloat() - this.rand.nextFloat()) * 0.375D;
            double var6 = (double) var2 + 0.5D + (double) (this.rand.nextFloat() - this.rand.nextFloat()) * 0.375D;
            double var8 = (double) var3 + 0.5D + (double) (this.rand.nextFloat() - this.rand.nextFloat()) * 0.375D;
            this.worldObj.spawnParticle("explode", var4, var6, var8, 0.0D, 0.0D, 0.0D);
        }
    }

    public void setDungeon(int var1, int var2, int var3)
    {
        this.dungeonX = var1;
        this.dungeonY = var2;
        this.dungeonZ = var3;
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
        return this.getMaxHealth();
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
