package net.aetherteam.aether.dungeons;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import net.aetherteam.aether.Aether;
import net.aetherteam.aether.AetherLoot;
import net.aetherteam.aether.CommonProxy;
import net.aetherteam.aether.dungeons.keys.DungeonKey;
import net.aetherteam.aether.dungeons.keys.EnumKeyType;
import net.aetherteam.aether.packets.AetherPacketHandler;
import net.aetherteam.aether.party.Party;
import net.aetherteam.aether.party.PartyController;
import net.aetherteam.aether.party.members.PartyMember;
import net.aetherteam.aether.tile_entities.TileEntityEntranceController;
import net.aetherteam.aether.tile_entities.TileEntitySkyrootChest;
import net.aetherteam.aether.worldgen.ComponentDungeonBronzeRoom;
import net.aetherteam.aether.worldgen.StructureBoundingBoxSerial;
import net.aetherteam.aether.worldgen.StructureBronzeDungeonStart;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Session;
import net.minecraft.world.World;
import paulscode.sound.SoundSystem;

public class Dungeon implements Serializable
{
    private DungeonType dungeonType;
    private String name;
    private int keepCap;
    private boolean hasStarted;
    public int centerX;
    public int centerZ;
    private Party currentActiveParty;
    private ArrayList<String> conquerers = new ArrayList();
    private HashMap<String, Object> leavers = new HashMap();

    public ArrayList<StructureBoundingBoxSerial> boundingBoxes = new ArrayList();
    public StructureBoundingBoxSerial boundingBox;
    private int dungeonID;
    private boolean active;
    private HashMap queuedMembers = new HashMap();

    private Integer[] controllerCoords = new Integer[3];

    private long startingTime = 0L;
    private boolean timerStarted;
    private int timerLength;
    private ArrayList<DungeonKey> keyList = new ArrayList();
    private ArrayList savedBlockCoords = new ArrayList();
    private ArrayList savedEntityCoords = new ArrayList();
    private ArrayList savedTileEntities = new ArrayList();
    private ArrayList savedMobSpawners = new ArrayList();
    String aetherVersion;

    public Dungeon(DungeonType type, int x, int z, StructureBronzeDungeonStart start)
    {
        this.dungeonType = type;
        this.name = this.dungeonType.getName();
        this.aetherVersion = Aether.getVersion();

        this.centerX = x;
        this.centerZ = z;

        updateBoundingBox(start);

        this.dungeonID = DungeonHandler.instance().getInstances().size();
    }

    public Dungeon(DungeonType type, int x, int z, StructureBoundingBoxSerial box, ArrayList boxes)
    {
        this(type, x, z, null);

        this.boundingBox = box;
        this.boundingBoxes = boxes;
    }

    public void addKey(DungeonKey key)
    {
        this.keyList.add(key);
    }

    public void removeKey(DungeonKey key)
    {
        for (DungeonKey keyToRemove : this.keyList)
        {
            if (keyToRemove.getType() == key.getType())
            {
                this.keyList.remove(keyToRemove);
                return;
            }
        }
    }

    public int getKeyAmount()
    {
        return this.keyList.size();
    }

    public int getKeyAmount(DungeonKey key)
    {
        return getKeyAmount(key.getType());
    }

    public ArrayList getKeys()
    {
        return this.keyList;
    }

    public int getKeyAmount(EnumKeyType type)
    {
        int keyAmount = 0;

        if (type != null)
        {
            for (DungeonKey keyIt : this.keyList)
            {
                if (keyIt.getType() == type)
                {
                    keyAmount++;
                }
            }
        }

        return keyAmount;
    }

    public boolean hasKeyType(EnumKeyType type)
    {
        if (type != null)
        {
            for (DungeonKey keyIt : this.keyList)
            {
                if (keyIt.getType() == type)
                {
                    return true;
                }
            }
        }

        return false;
    }

    public DungeonType getType()
    {
        return this.dungeonType;
    }

    public Dungeon setID(int id)
    {
        this.dungeonID = id;

        return this;
    }

    public int getID()
    {
        return this.dungeonID;
    }

    public void updateBoundingBox(StructureBronzeDungeonStart structure)
    {
        if (structure != null)
        {
            this.boundingBox = StructureBoundingBoxSerial.getNewBoundingBox();
            Iterator componentsIt = structure.components.iterator();

            while (componentsIt.hasNext())
            {
                ComponentDungeonBronzeRoom component = (ComponentDungeonBronzeRoom) componentsIt.next();

                StructureBoundingBoxSerial serialBox = new StructureBoundingBoxSerial(component.getBoundingBox());

                this.boundingBoxes.add(serialBox);

                this.boundingBox.expandTo(serialBox);
            }
        }
    }

    public void finishDungeon(Party party)
    {
        if (this.currentActiveParty == null)
        {
            return;
        }

        Side side = FMLCommonHandler.instance().getEffectiveSide();

        if ((this.currentActiveParty.getName().equalsIgnoreCase(party.getName())) && (this.active))
        {
            if (side.isServer())
            {
                MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
                ServerConfigurationManager configManager = server.getConfigurationManager();

                EntityPlayer player = null;

                for (Iterator i$ = configManager.playerEntityList.iterator(); i$.hasNext(); )
                {
                    Object obj = i$.next();

                    if (((obj instanceof EntityPlayer)) && (party.hasMember(((EntityPlayer) obj).username.toLowerCase())))
                    {
                        if (((EntityPlayer) obj).username.equalsIgnoreCase(party.getLeader().username.toLowerCase()))
                        {
                            player = (EntityPlayer) obj;
                        }
                    }
                }

                if (player != null)
                {
                    TileEntityEntranceController controller = (TileEntityEntranceController) player.worldObj.getBlockTileEntity(this.controllerCoords[0].intValue(), this.controllerCoords[1].intValue(), this.controllerCoords[2].intValue());

                    controller.teleportMembersFromParty(getQueuedMembers(), true);
                    cleanUpDungeon(controller.worldObj);
                }
            }

            for (PartyMember member : getQueuedMembers())
            {
                this.conquerers.add(member.username.toLowerCase());
            }

            this.currentActiveParty = null;
            setActive(false);
            this.hasStarted = false;
            this.timerStarted = false;
            this.keyList.clear();
            this.controllerCoords = new Integer[3];
            this.queuedMembers.clear();
        }

        DungeonHandler.instance().saveDungeons();
    }

    public boolean hasConqueredDungeon(String username)
    {
        for (String conquererName : this.conquerers)
        {
            if (username.equalsIgnoreCase(conquererName))
            {
                return true;
            }
        }

        for (String leaverName : this.leavers.keySet())
        {
            if ((this.leavers.get(username.toLowerCase()) == null) || ((username.equalsIgnoreCase(leaverName)) && (this.leavers.get(username.toLowerCase()) != null) && (((Integer) this.leavers.get(username.toLowerCase())).intValue() >= 3)))
            {
                return true;
            }
        }

        return false;
    }

    public boolean hasConqueredDungeon(EntityPlayer player)
    {
        return hasConqueredDungeon(player.username);
    }

    public boolean hasConqueredDungeon(PartyMember player)
    {
        return hasConqueredDungeon(player.username);
    }

    public boolean hasAnyConqueredDungeon(ArrayList players)
    {
        for (int count = 0; count < players.size(); count++)
        {
            if ((players.get(count) instanceof PartyMember))
            {
                if (hasConqueredDungeon((PartyMember) players.get(count)))
                {
                    return true;
                }
            } else if ((players.get(count) instanceof EntityPlayer))
            {
                if (hasConqueredDungeon((EntityPlayer) players.get(count)))
                {
                    return true;
                }
            } else if (((players.get(count) instanceof String)) && (hasConqueredDungeon((String) players.get(count))))
            {
                return true;
            }

        }

        return false;
    }

    public boolean hasListConqueredDungeon(ArrayList players)
    {
        if (hasAnyConqueredDungeon(players))
        {
            return false;
        }

        return true;
    }

    public void queueParty(Party party, int controlX, int controlY, int controlZ)
    {
        if ((this.currentActiveParty == null) && (party != null))
        {
            this.currentActiveParty = party;

            this.controllerCoords[0] = Integer.valueOf(MathHelper.floor_double(controlX));
            this.controllerCoords[1] = Integer.valueOf(MathHelper.floor_double(controlY));
            this.controllerCoords[2] = Integer.valueOf(MathHelper.floor_double(controlZ));
        }

        DungeonHandler.instance().saveDungeons();
    }

    public boolean isSoundOn()
    {
        return (isClient()) && (Aether.proxy.getClient().sndManager != null) && (SoundManager.sndSystem != null);
    }

    public boolean isClient()
    {
        return Aether.proxy.getClient() != null;
    }

    public boolean isMusicPlaying()
    {
        return (SoundManager.sndSystem != null) && (SoundManager.sndSystem.playing("streaming"));
    }

    public void turnMusicOff()
    {
        if (isSoundOn())
        {
            SoundManager.sndSystem.stop("streaming");
        }
    }

    public void playMusicFile(String fileName, int x, int y, int z)
    {
        if (isSoundOn())
        {
            Aether.proxy.getClient().sndManager.playStreaming(fileName, x, y, z);
        }
    }

    public void checkForQueue()
    {
        Side side = FMLCommonHandler.instance().getEffectiveSide();

        if ((this.currentActiveParty != null) && (this.queuedMembers.size() >= this.currentActiveParty.getMembers().size()) && (!this.hasStarted) && (this.currentActiveParty.getMembers() != null))
        {
            this.hasStarted = true;
            if (side.isServer())
            {
                MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
                ServerConfigurationManager configManager = server.getConfigurationManager();

                EntityPlayer player = null;

                for (Iterator i$ = configManager.playerEntityList.iterator(); i$.hasNext(); )
                {
                    Object obj = i$.next();

                    if (((obj instanceof EntityPlayer)) && (this.currentActiveParty.hasMember(((EntityPlayer) obj).username.toLowerCase())))
                    {
                        if (((EntityPlayer) obj).username.equalsIgnoreCase(this.currentActiveParty.getLeader().username.toLowerCase()))
                        {
                            player = (EntityPlayer) obj;
                        }

                        EntityPlayer player2 = (EntityPlayer) obj;

                        if (player2.dimension != 3)
                        {
                            TileEntityEntranceController controller = (TileEntityEntranceController) player.worldObj.getBlockTileEntity(this.controllerCoords[0].intValue(), this.controllerCoords[1].intValue(), this.controllerCoords[2].intValue());

                            if (controller != null)
                            {
                                int x = MathHelper.floor_double(controller.xCoord);
                                int y = MathHelper.floor_double(controller.yCoord);
                                int z = MathHelper.floor_double(controller.zCoord);

                                DungeonHandler.instance().disbandQueue(this, this.currentActiveParty, x, y, z, PartyController.instance().getMember((EntityPlayer) obj), false);

                                PacketDispatcher.sendPacketToAllPlayers(AetherPacketHandler.sendDungeonQueueChange(false, this, x, y, z, this.currentActiveParty));
                            }

                            return;
                        }
                    }
                }

                if (player != null)
                {
                    TileEntityEntranceController controller = (TileEntityEntranceController) player.worldObj.getBlockTileEntity(this.controllerCoords[0].intValue(), this.controllerCoords[1].intValue(), this.controllerCoords[2].intValue());

                    controller.teleportMembersFromParty(this.currentActiveParty.getMembers(), false);

                    regenBosses(player.worldObj);
                }

            } else if (hasMember(PartyController.instance().getMember(Minecraft.getMinecraft().session.username)))
            {
                playMusicFile("Dungeon Background", this.controllerCoords[0].intValue(), this.controllerCoords[1].intValue(), this.controllerCoords[2].intValue());
            }
        }
    }

    public void queueMember(Party party, PartyMember member)
    {
        Side side = FMLCommonHandler.instance().getEffectiveSide();

        if ((isQueuedParty(party)) && (party.hasMember(member)))
        {
            setActive(true);
            this.queuedMembers.put(member.username.toLowerCase(), member);

            checkForQueue();
        }

        DungeonHandler.instance().saveDungeons();
    }

    public void startTimer(int seconds)
    {
        this.timerLength = seconds;
        this.startingTime = (System.currentTimeMillis() / 1000L);
        this.timerStarted = true;
    }

    public boolean timerFinished()
    {
        return getTimerSeconds() >= this.timerLength;
    }

    public boolean timerStarted()
    {
        return this.timerStarted;
    }

    public int getTimerSeconds()
    {
        long currentTime = System.currentTimeMillis() / 1000L;

        return (int) (currentTime - this.startingTime);
    }

    public int getTimerLength()
    {
        return this.timerLength;
    }

    public boolean hasQueuedParty()
    {
        return this.currentActiveParty != null;
    }

    public Party getQueuedParty()
    {
        return this.currentActiveParty;
    }

    public boolean isQueuedParty(Party party)
    {
        return (party != null) && (this.currentActiveParty != null) && (this.currentActiveParty.getName().equalsIgnoreCase(party.getName()));
    }

    public boolean isActive()
    {
        return this.active;
    }

    public void setActive(boolean flag)
    {
        this.active = flag;
    }

    public void disbandQueue(Party party)
    {
        Side side = FMLCommonHandler.instance().getEffectiveSide();

        if (this.currentActiveParty != null)
        {
            if ((this.currentActiveParty.getName().equalsIgnoreCase(party.getName())) && (this.active) && (getQueuedMembers() != null))
            {
                if (side.isServer())
                {
                    MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
                    ServerConfigurationManager configManager = server.getConfigurationManager();

                    EntityPlayer player = null;

                    for (Iterator i$ = configManager.playerEntityList.iterator(); i$.hasNext(); )
                    {
                        Object obj = i$.next();

                        if (((obj instanceof EntityPlayer)) && (party.hasMember(((EntityPlayer) obj).username.toLowerCase())))
                        {
                            if (((EntityPlayer) obj).username.equalsIgnoreCase(party.getLeader().username.toLowerCase()))
                            {
                                player = (EntityPlayer) obj;
                            }
                        }
                    }

                    if (player != null)
                    {
                        TileEntityEntranceController controller = (TileEntityEntranceController) player.worldObj.getBlockTileEntity(this.controllerCoords[0].intValue(), this.controllerCoords[1].intValue(), this.controllerCoords[2].intValue());

                        controller.teleportMembersFromParty(getQueuedMembers(), true);
                        cleanUpDungeon(controller.worldObj);
                    }
                }
            }

            setActive(false);
            this.hasStarted = false;
            this.queuedMembers.clear();
            this.currentActiveParty = null;
            this.controllerCoords = new Integer[3];
            this.keyList.clear();
        }

        DungeonHandler.instance().saveDungeons();
    }

    public void disbandMember(PartyMember member)
    {
        Side side = FMLCommonHandler.instance().getEffectiveSide();
        PartyMember removingMember = null;

        if ((this.currentActiveParty != null) && (hasMember(member)))
        {
            if (side.isServer())
            {
                MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
                ServerConfigurationManager configManager = server.getConfigurationManager();

                EntityPlayer player = null;

                for (Iterator i$ = configManager.playerEntityList.iterator(); i$.hasNext(); )
                {
                    Object obj = i$.next();

                    if (((obj instanceof EntityPlayer)) && (member.username.equalsIgnoreCase(((EntityPlayer) obj).username.toLowerCase())))
                    {
                        player = (EntityPlayer) obj;
                    }
                }

                if (player != null)
                {
                    TileEntityEntranceController controller = (TileEntityEntranceController) player.worldObj.getBlockTileEntity(this.controllerCoords[0].intValue(), this.controllerCoords[1].intValue(), this.controllerCoords[2].intValue());

                    player.setPositionAndUpdate(this.controllerCoords[0].intValue() + 0.5D, this.controllerCoords[1].intValue() + 1.5D, this.controllerCoords[2].intValue() + 3.5D);
                }

            }

            this.queuedMembers.remove(member.username.toLowerCase());

            int count = (this.leavers.containsKey(member.username.toLowerCase())) && (this.leavers.get(member.username.toLowerCase()) != null) ? ((Integer) this.leavers.get(member.username.toLowerCase())).intValue() : 0;

            this.leavers.put(member.username.toLowerCase(), Integer.valueOf(count + 1));

            if (this.queuedMembers.size() <= 0)
            {
                disbandQueue(this.currentActiveParty);
                setActive(false);
                this.hasStarted = false;
            }
        }

        DungeonHandler.instance().saveDungeons();
    }

    public boolean hasMember(PartyMember member)
    {
        if (member != null)
        {
            if (this.queuedMembers.get(member.username.toLowerCase()) != null)
            {
                return true;
            }
        }

        return false;
    }

    public int getMemberLeaves(PartyMember member)
    {
        if (this.leavers.get(member.username.toLowerCase()) != null)
        {
            return ((Integer) this.leavers.get(member.username.toLowerCase())).intValue();
        }

        return 0;
    }

    public int getAmountQueued()
    {
        return this.queuedMembers.size();
    }

    public int getControllerX()
    {
        return this.controllerCoords[0].intValue();
    }

    public int getControllerY()
    {
        return this.controllerCoords[1].intValue();
    }

    public int getControllerZ()
    {
        return this.controllerCoords[2].intValue();
    }

    public ArrayList<PartyMember> getQueuedMembers()
    {
        ArrayList<PartyMember> members = new ArrayList(this.queuedMembers.values());

        return members;
    }

    public void cleanUpDungeon(World world)
    {
        Side side = FMLCommonHandler.instance().getEffectiveSide();

        setActive(false);

        if ((side.isServer()) && (!world.isRemote))
        {
            EntityLiving savedEntityLiving = null;

            for (int i = 0; i < this.savedBlockCoords.size(); i++)
            {
                world.setBlockToAir(((TrackedCoord) this.savedBlockCoords.get(i)).getX(), ((TrackedCoord) this.savedBlockCoords.get(i)).getY(), ((TrackedCoord) this.savedBlockCoords.get(i)).getZ());
            }

            for (int i = 0; i < this.savedTileEntities.size(); i++)
            {
                if ((world.getBlockTileEntity(((TrackedTileEntityCoord) this.savedTileEntities.get(i)).getX(), ((TrackedTileEntityCoord) this.savedTileEntities.get(i)).getY(), ((TrackedTileEntityCoord) this.savedTileEntities.get(i)).getZ()) != null) && ((world.getBlockTileEntity(((TrackedTileEntityCoord) this.savedTileEntities.get(i)).getX(), ((TrackedTileEntityCoord) this.savedTileEntities.get(i)).getY(), ((TrackedTileEntityCoord) this.savedTileEntities.get(i)).getZ()) instanceof TileEntitySkyrootChest)))
                {
                    TileEntitySkyrootChest chestEntity1 = (TileEntitySkyrootChest) world.getBlockTileEntity(((TrackedTileEntityCoord) this.savedTileEntities.get(i)).getX(), ((TrackedTileEntityCoord) this.savedTileEntities.get(i)).getY(), ((TrackedTileEntityCoord) this.savedTileEntities.get(i)).getZ());

                    chestEntity1.getChestContents();

                    for (int count = 0; count < chestEntity1.getSizeInventory(); count++)
                    {
                        chestEntity1.setInventorySlotContents(count, null);
                    }
                }

                world.setBlockToAir(((TrackedTileEntityCoord) this.savedTileEntities.get(i)).getX(), ((TrackedTileEntityCoord) this.savedTileEntities.get(i)).getY(), ((TrackedTileEntityCoord) this.savedTileEntities.get(i)).getZ());
                world.setBlock(((TrackedTileEntityCoord) this.savedTileEntities.get(i)).getX(), ((TrackedTileEntityCoord) this.savedTileEntities.get(i)).getY(), ((TrackedTileEntityCoord) this.savedTileEntities.get(i)).getZ(), ((TrackedTileEntityCoord) this.savedTileEntities.get(i)).getBlock(), ((TrackedTileEntityCoord) this.savedTileEntities.get(i)).getBlockMeta(), 2);

                if ((world.getBlockTileEntity(((TrackedTileEntityCoord) this.savedTileEntities.get(i)).getX(), ((TrackedTileEntityCoord) this.savedTileEntities.get(i)).getY(), ((TrackedTileEntityCoord) this.savedTileEntities.get(i)).getZ()) instanceof TileEntitySkyrootChest))
                {
                    TileEntitySkyrootChest chestEntity = (TileEntitySkyrootChest) world.getBlockTileEntity(((TrackedTileEntityCoord) this.savedTileEntities.get(i)).getX(), ((TrackedTileEntityCoord) this.savedTileEntities.get(i)).getY(), ((TrackedTileEntityCoord) this.savedTileEntities.get(i)).getZ());

                    for (int count = 0; count < 3 + world.rand.nextInt(3); count++)
                    {
                        chestEntity.setInventorySlotContents(world.rand.nextInt(chestEntity.getSizeInventory()), AetherLoot.NORMAL.getRandomItem(world.rand));
                    }
                }
            }

            for (int i = 0; i < this.savedMobSpawners.size(); i++)
            {
                int x = ((TrackedMobSpawnerCoord) this.savedMobSpawners.get(i)).getX();
                int y = ((TrackedMobSpawnerCoord) this.savedMobSpawners.get(i)).getY();
                int z = ((TrackedMobSpawnerCoord) this.savedMobSpawners.get(i)).getZ();

                String mobID = ((TrackedMobSpawnerCoord) this.savedMobSpawners.get(i)).getMobID();

                world.setBlockToAir(x, y, z);
                world.setBlock(x, y, z, Block.mobSpawner.blockID);

                TileEntityMobSpawner spawner = (TileEntityMobSpawner) world.getBlockTileEntity(x, y, z);

                if (spawner != null)
                {
                    spawner.func_98049_a().setMobID(mobID);
                }
            }
        }

        DungeonHandler.instance().saveDungeons();
    }

    public void regenBosses(World world)
    {
        Side side = FMLCommonHandler.instance().getEffectiveSide();

        if (side.isServer())
        {
            EntityLiving savedEntityLiving = null;

            for (int i = 0; i < this.savedEntityCoords.size(); i++)
            {
                savedEntityLiving = ((TrackedEntityCoord) this.savedEntityCoords.get(i)).getTrackedEntity(world);
                savedEntityLiving.setPosition(((TrackedEntityCoord) this.savedEntityCoords.get(i)).getX(), ((TrackedEntityCoord) this.savedEntityCoords.get(i)).getY(), ((TrackedEntityCoord) this.savedEntityCoords.get(i)).getZ());
                world.spawnEntityInWorld(savedEntityLiving);
            }
        }

        DungeonHandler.instance().saveDungeons();
    }

    public boolean hasStarted()
    {
        return this.hasStarted;
    }

    public void registerBlockPlacement(int x, int y, int z)
    {
        Side side = FMLCommonHandler.instance().getEffectiveSide();

        if (side.isServer())
        {
            this.savedBlockCoords.add(new TrackedCoord(x, y, z));
        }

        DungeonHandler.instance().saveDungeons();
    }

    public void registerEntity(float f, float g, float h, EntityLiving entityLiving)
    {
        Side side = FMLCommonHandler.instance().getEffectiveSide();

        if (side.isServer())
        {
            this.savedEntityCoords.add(new TrackedEntityCoord(f, g, h, EntityList.getEntityString(entityLiving)));
        }

        DungeonHandler.instance().saveDungeons();
    }

    public void registerSafeBlock(int x, int y, int z, int blockID, int meta)
    {
        Side side = FMLCommonHandler.instance().getEffectiveSide();

        if (side.isServer())
        {
            this.savedTileEntities.add(new TrackedTileEntityCoord(x, y, z, blockID, meta));
        }

        DungeonHandler.instance().saveDungeons();
    }

    public void registerMobSpawner(int x, int y, int z, String mobID)
    {
        Side side = FMLCommonHandler.instance().getEffectiveSide();

        if (side.isServer())
        {
            this.savedMobSpawners.add(new TrackedMobSpawnerCoord(x, y, z, mobID));
        }

        DungeonHandler.instance().saveDungeons();
    }
}

/* Location:           D:\Dev\Mc\forge_orl\mcp\jars\bin\aether.jar
 * Qualified Name:     net.aetherteam.aether.dungeons.Dungeon
 * JD-Core Version:    0.6.2
 */