package net.aetherteam.aether.dungeons;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import net.aetherteam.aether.Aether;
import net.aetherteam.aether.AetherLoot;
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
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class Dungeon implements Serializable
{
    private DungeonType dungeonType;
    private String name;
    private int keepCap;
    private boolean hasStarted;
    public int centerX;
    public int centerZ;
    private Party currentActiveParty;
    private ArrayList<String> conquerers;
    private HashMap<String, Integer> leavers;
    public ArrayList<StructureBoundingBoxSerial> boundingBoxes;
    public StructureBoundingBoxSerial boundingBox;
    private int dungeonID;
    private boolean active;
    private HashMap<String, PartyMember> queuedMembers;
    private Integer[] controllerCoords;
    private long startingTime;
    private boolean timerStarted;
    private int timerLength;
    private ArrayList<DungeonKey> keyList;
    private ArrayList<TrackedCoord> savedBlockCoords;
    private ArrayList<TrackedEntityCoord> savedEntityCoords;
    private ArrayList<TrackedTileEntityCoord> savedTileEntities;
    private ArrayList<TrackedMobSpawnerCoord> savedMobSpawners;
    String aetherVersion;

    public Dungeon(DungeonType type, int x, int z, StructureBronzeDungeonStart start)
    {
        this.conquerers = new ArrayList();
        this.leavers = new HashMap();
        this.boundingBoxes = new ArrayList();
        this.queuedMembers = new HashMap();
        this.controllerCoords = new Integer[3];
        this.startingTime = 0L;
        this.keyList = new ArrayList();
        this.savedBlockCoords = new ArrayList();
        this.savedEntityCoords = new ArrayList();
        this.savedTileEntities = new ArrayList();
        this.savedMobSpawners = new ArrayList();
        this.dungeonType = type;
        this.name = this.dungeonType.getName();
        this.aetherVersion = Aether.getVersion();
        this.centerX = x;
        this.centerZ = z;
        this.updateBoundingBox(start);
        this.dungeonID = DungeonHandler.instance().getInstances().size();
    }

    public Dungeon(DungeonType type, int x, int z, StructureBoundingBoxSerial box, ArrayList<StructureBoundingBoxSerial> boxes)
    {
        this(type, x, z, (StructureBronzeDungeonStart)null);
        this.boundingBox = box;
        this.boundingBoxes = boxes;
    }

    public void addKey(DungeonKey key)
    {
        this.keyList.add(key);
    }

    public void removeKey(DungeonKey key)
    {
        Iterator i$ = this.keyList.iterator();
        DungeonKey keyToRemove;

        do
        {
            if (!i$.hasNext())
            {
                return;
            }

            keyToRemove = (DungeonKey)i$.next();
        }
        while (keyToRemove.getType() != key.getType());

        this.keyList.remove(keyToRemove);
    }

    public int getKeyAmount()
    {
        return this.keyList.size();
    }

    public int getKeyAmount(DungeonKey key)
    {
        return this.getKeyAmount(key.getType());
    }

    public ArrayList<DungeonKey> getKeys()
    {
        return this.keyList;
    }

    public int getKeyAmount(EnumKeyType type)
    {
        int keyAmount = 0;

        if (type != null)
        {
            Iterator i$ = this.keyList.iterator();

            while (i$.hasNext())
            {
                DungeonKey keyIt = (DungeonKey)i$.next();

                if (keyIt.getType() == type)
                {
                    ++keyAmount;
                }
            }
        }

        return keyAmount;
    }

    public boolean hasKeyType(EnumKeyType type)
    {
        if (type != null)
        {
            Iterator i$ = this.keyList.iterator();

            while (i$.hasNext())
            {
                DungeonKey keyIt = (DungeonKey)i$.next();

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
                ComponentDungeonBronzeRoom component = (ComponentDungeonBronzeRoom)componentsIt.next();
                StructureBoundingBoxSerial serialBox = new StructureBoundingBoxSerial(component.getBoundingBox());
                this.boundingBoxes.add(serialBox);
                this.boundingBox.expandTo(serialBox);
            }
        }
    }

    public void finishDungeon(Party party)
    {
        if (this.currentActiveParty != null)
        {
            Side side = FMLCommonHandler.instance().getEffectiveSide();

            if (this.currentActiveParty.getName().equalsIgnoreCase(party.getName()) && this.active)
            {
                if (side.isServer())
                {
                    MinecraftServer i$ = FMLCommonHandler.instance().getMinecraftServerInstance();
                    ServerConfigurationManager member = i$.getConfigurationManager();
                    EntityPlayer player = null;
                    Iterator controller = member.playerEntityList.iterator();

                    while (controller.hasNext())
                    {
                        Object obj = controller.next();

                        if (obj instanceof EntityPlayer && party.hasMember(((EntityPlayer)obj).username.toLowerCase()) && ((EntityPlayer)obj).username.equalsIgnoreCase(party.getLeader().username.toLowerCase()))
                        {
                            player = (EntityPlayer)obj;
                        }
                    }

                    if (player != null)
                    {
                        TileEntityEntranceController controller1 = (TileEntityEntranceController)player.worldObj.getBlockTileEntity(this.controllerCoords[0].intValue(), this.controllerCoords[1].intValue(), this.controllerCoords[2].intValue());
                        controller1.teleportMembersFromParty(this.getQueuedMembers(), true);
                        this.cleanUpDungeon(controller1.worldObj);
                    }
                }

                Iterator i$1 = this.getQueuedMembers().iterator();

                while (i$1.hasNext())
                {
                    PartyMember member1 = (PartyMember)i$1.next();
                    this.conquerers.add(member1.username.toLowerCase());
                }

                this.currentActiveParty = null;
                this.setActive(false);
                this.hasStarted = false;
                this.timerStarted = false;
                this.keyList.clear();
                this.controllerCoords = new Integer[3];
                this.queuedMembers.clear();
            }

            DungeonHandler.instance().saveDungeons();
        }
    }

    public boolean hasConqueredDungeon(String username)
    {
        Iterator i$ = this.conquerers.iterator();
        String leaverName;

        do
        {
            if (!i$.hasNext())
            {
                i$ = this.leavers.keySet().iterator();

                do
                {
                    if (!i$.hasNext())
                    {
                        return false;
                    }

                    leaverName = (String)i$.next();

                    if (this.leavers.get(username.toLowerCase()) != null)
                    {
                        ;
                    }
                }
                while (!username.equalsIgnoreCase(leaverName) || this.leavers.get(username.toLowerCase()) == null || ((Integer)this.leavers.get(username.toLowerCase())).intValue() < 3);

                return true;
            }

            leaverName = (String)i$.next();
        }
        while (!username.equalsIgnoreCase(leaverName));

        return true;
    }

    public boolean hasConqueredDungeon(EntityPlayer player)
    {
        return this.hasConqueredDungeon(player.username);
    }

    public boolean hasConqueredDungeon(PartyMember player)
    {
        return this.hasConqueredDungeon(player.username);
    }

    public boolean hasAnyConqueredDungeon(ArrayList players)
    {
        for (int count = 0; count < players.size(); ++count)
        {
            if (players.get(count) instanceof PartyMember)
            {
                if (this.hasConqueredDungeon((PartyMember)players.get(count)))
                {
                    return true;
                }
            }
            else if (players.get(count) instanceof EntityPlayer)
            {
                if (this.hasConqueredDungeon((EntityPlayer)players.get(count)))
                {
                    return true;
                }
            }
            else if (players.get(count) instanceof String && this.hasConqueredDungeon((String)players.get(count)))
            {
                return true;
            }
        }

        return false;
    }

    public boolean hasListConqueredDungeon(ArrayList players)
    {
        return !this.hasAnyConqueredDungeon(players);
    }

    public void queueParty(Party party, int controlX, int controlY, int controlZ)
    {
        if (this.currentActiveParty == null && party != null)
        {
            this.currentActiveParty = party;
            this.controllerCoords[0] = Integer.valueOf(MathHelper.floor_double((double)controlX));
            this.controllerCoords[1] = Integer.valueOf(MathHelper.floor_double((double)controlY));
            this.controllerCoords[2] = Integer.valueOf(MathHelper.floor_double((double)controlZ));
        }

        DungeonHandler.instance().saveDungeons();
    }

    public boolean isSoundOn()
    {
        return this.isClient() && Aether.proxy.getClient().sndManager != null && Aether.proxy.getClient().sndManager.sndSystem != null;
    }

    public boolean isClient()
    {
        return Aether.proxy.getClient() != null;
    }

    public boolean isMusicPlaying()
    {
        return Aether.proxy.getClient().sndManager.sndSystem != null && Aether.proxy.getClient().sndManager.sndSystem.playing("streaming");
    }

    public void turnMusicOff()
    {
        if (this.isSoundOn())
        {
            Aether.proxy.getClient().sndManager.sndSystem.stop("streaming");
        }
    }

    public void playMusicFile(String fileName, int x, int y, int z)
    {
        if (this.isSoundOn())
        {
            Aether.proxy.getClient().sndManager.playStreaming(fileName, (float)x, (float)y, (float)z);
        }
    }

    public void checkForQueue()
    {
        Side side = FMLCommonHandler.instance().getEffectiveSide();

        if (this.currentActiveParty != null && this.queuedMembers.size() >= this.currentActiveParty.getMembers().size() && !this.hasStarted && this.currentActiveParty.getMembers() != null)
        {
            this.hasStarted = true;

            if (side.isServer())
            {
                MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
                ServerConfigurationManager configManager = server.getConfigurationManager();
                EntityPlayer player = null;
                Iterator controller = configManager.playerEntityList.iterator();

                while (controller.hasNext())
                {
                    Object obj = controller.next();

                    if (obj instanceof EntityPlayer && this.currentActiveParty.hasMember(((EntityPlayer)obj).username.toLowerCase()))
                    {
                        if (((EntityPlayer)obj).username.equalsIgnoreCase(this.currentActiveParty.getLeader().username.toLowerCase()))
                        {
                            player = (EntityPlayer)obj;
                        }

                        EntityPlayer player2 = (EntityPlayer)obj;

                        if (player2.dimension != 3)
                        {
                            TileEntityEntranceController controller1 = (TileEntityEntranceController)player.worldObj.getBlockTileEntity(this.controllerCoords[0].intValue(), this.controllerCoords[1].intValue(), this.controllerCoords[2].intValue());

                            if (controller1 != null)
                            {
                                int x = MathHelper.floor_double((double)controller1.xCoord);
                                int y = MathHelper.floor_double((double)controller1.yCoord);
                                int z = MathHelper.floor_double((double)controller1.zCoord);
                                DungeonHandler.instance().disbandQueue(this, this.currentActiveParty, x, y, z, PartyController.instance().getMember((EntityPlayer)obj), false);
                                PacketDispatcher.sendPacketToAllPlayers(AetherPacketHandler.sendDungeonQueueChange(false, this, x, y, z, this.currentActiveParty));
                            }

                            return;
                        }
                    }
                }

                if (player != null)
                {
                    TileEntityEntranceController controller2 = (TileEntityEntranceController)player.worldObj.getBlockTileEntity(this.controllerCoords[0].intValue(), this.controllerCoords[1].intValue(), this.controllerCoords[2].intValue());
                    controller2.teleportMembersFromParty(this.currentActiveParty.getMembers(), false);
                    this.regenBosses(player.worldObj);
                }
            }
            else if (this.hasMember(PartyController.instance().getMember(Minecraft.getMinecraft().func_110432_I().func_111285_a())))
            {
                this.playMusicFile("Dungeon Background", this.controllerCoords[0].intValue(), this.controllerCoords[1].intValue(), this.controllerCoords[2].intValue());
            }
        }
    }

    public void queueMember(Party party, PartyMember member)
    {
        Side side = FMLCommonHandler.instance().getEffectiveSide();

        if (this.isQueuedParty(party) && party.hasMember(member))
        {
            this.setActive(true);
            this.queuedMembers.put(member.username.toLowerCase(), member);
            this.checkForQueue();
        }

        DungeonHandler.instance().saveDungeons();
    }

    public void startTimer(int seconds)
    {
        this.timerLength = seconds;
        this.startingTime = System.currentTimeMillis() / 1000L;
        this.timerStarted = true;
    }

    public boolean timerFinished()
    {
        return this.getTimerSeconds() >= this.timerLength;
    }

    public boolean timerStarted()
    {
        return this.timerStarted;
    }

    public int getTimerSeconds()
    {
        long currentTime = System.currentTimeMillis() / 1000L;
        return (int)(currentTime - this.startingTime);
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
        return party != null && this.currentActiveParty != null && this.currentActiveParty.getName().equalsIgnoreCase(party.getName());
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
            if (this.currentActiveParty.getName().equalsIgnoreCase(party.getName()) && this.active && this.getQueuedMembers() != null && side.isServer())
            {
                MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
                ServerConfigurationManager configManager = server.getConfigurationManager();
                EntityPlayer player = null;
                Iterator controller = configManager.playerEntityList.iterator();

                while (controller.hasNext())
                {
                    Object obj = controller.next();

                    if (obj instanceof EntityPlayer && party.hasMember(((EntityPlayer)obj).username.toLowerCase()) && ((EntityPlayer)obj).username.equalsIgnoreCase(party.getLeader().username.toLowerCase()))
                    {
                        player = (EntityPlayer)obj;
                    }
                }

                if (player != null)
                {
                    TileEntityEntranceController controller1 = (TileEntityEntranceController)player.worldObj.getBlockTileEntity(this.controllerCoords[0].intValue(), this.controllerCoords[1].intValue(), this.controllerCoords[2].intValue());
                    controller1.teleportMembersFromParty(this.getQueuedMembers(), true);
                    this.cleanUpDungeon(controller1.worldObj);
                }
            }

            this.setActive(false);
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
        Object removingMember = null;

        if (this.currentActiveParty != null && this.hasMember(member))
        {
            if (side.isServer())
            {
                MinecraftServer count = FMLCommonHandler.instance().getMinecraftServerInstance();
                ServerConfigurationManager configManager = count.getConfigurationManager();
                EntityPlayer player = null;
                Iterator controller = configManager.playerEntityList.iterator();

                while (controller.hasNext())
                {
                    Object obj = controller.next();

                    if (obj instanceof EntityPlayer && member.username.equalsIgnoreCase(((EntityPlayer)obj).username.toLowerCase()))
                    {
                        player = (EntityPlayer)obj;
                    }
                }

                if (player != null)
                {
                    TileEntityEntranceController var10000 = (TileEntityEntranceController)player.worldObj.getBlockTileEntity(this.controllerCoords[0].intValue(), this.controllerCoords[1].intValue(), this.controllerCoords[2].intValue());
                    player.setPositionAndUpdate((double)this.controllerCoords[0].intValue() + 0.5D, (double)this.controllerCoords[1].intValue() + 1.5D, (double)this.controllerCoords[2].intValue() + 3.5D);
                }
            }

            this.queuedMembers.remove(member.username.toLowerCase());
            int count1 = this.leavers.containsKey(member.username.toLowerCase()) && this.leavers.get(member.username.toLowerCase()) != null ? ((Integer)this.leavers.get(member.username.toLowerCase())).intValue() : 0;
            this.leavers.put(member.username.toLowerCase(), Integer.valueOf(count1 + 1));

            if (this.queuedMembers.size() <= 0)
            {
                this.disbandQueue(this.currentActiveParty);
                this.setActive(false);
                this.hasStarted = false;
            }
        }

        DungeonHandler.instance().saveDungeons();
    }

    public boolean hasMember(PartyMember member)
    {
        return member != null && this.queuedMembers.get(member.username.toLowerCase()) != null;
    }

    public int getMemberLeaves(PartyMember member)
    {
        return this.leavers.get(member.username.toLowerCase()) != null ? ((Integer)this.leavers.get(member.username.toLowerCase())).intValue() : 0;
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
        ArrayList members = new ArrayList(this.queuedMembers.values());
        return members;
    }

    public void cleanUpDungeon(World world)
    {
        Side side = FMLCommonHandler.instance().getEffectiveSide();
        this.setActive(false);

        if (side.isServer() && !world.isRemote)
        {
            Object savedEntityLiving = null;
            int i;

            for (i = 0; i < this.savedBlockCoords.size(); ++i)
            {
                world.setBlockToAir(((TrackedCoord)this.savedBlockCoords.get(i)).getX(), ((TrackedCoord)this.savedBlockCoords.get(i)).getY(), ((TrackedCoord)this.savedBlockCoords.get(i)).getZ());
            }

            int y;

            for (i = 0; i < this.savedTileEntities.size(); ++i)
            {
                TileEntitySkyrootChest x;

                if (world.getBlockTileEntity(((TrackedTileEntityCoord)this.savedTileEntities.get(i)).getX(), ((TrackedTileEntityCoord)this.savedTileEntities.get(i)).getY(), ((TrackedTileEntityCoord)this.savedTileEntities.get(i)).getZ()) != null && world.getBlockTileEntity(((TrackedTileEntityCoord)this.savedTileEntities.get(i)).getX(), ((TrackedTileEntityCoord)this.savedTileEntities.get(i)).getY(), ((TrackedTileEntityCoord)this.savedTileEntities.get(i)).getZ()) instanceof TileEntitySkyrootChest)
                {
                    x = (TileEntitySkyrootChest)world.getBlockTileEntity(((TrackedTileEntityCoord)this.savedTileEntities.get(i)).getX(), ((TrackedTileEntityCoord)this.savedTileEntities.get(i)).getY(), ((TrackedTileEntityCoord)this.savedTileEntities.get(i)).getZ());
                    x.getChestContents();

                    for (y = 0; y < x.getSizeInventory(); ++y)
                    {
                        x.setInventorySlotContents(y, (ItemStack)null);
                    }
                }

                world.setBlockToAir(((TrackedTileEntityCoord)this.savedTileEntities.get(i)).getX(), ((TrackedTileEntityCoord)this.savedTileEntities.get(i)).getY(), ((TrackedTileEntityCoord)this.savedTileEntities.get(i)).getZ());
                world.setBlock(((TrackedTileEntityCoord)this.savedTileEntities.get(i)).getX(), ((TrackedTileEntityCoord)this.savedTileEntities.get(i)).getY(), ((TrackedTileEntityCoord)this.savedTileEntities.get(i)).getZ(), ((TrackedTileEntityCoord)this.savedTileEntities.get(i)).getBlock(), ((TrackedTileEntityCoord)this.savedTileEntities.get(i)).getBlockMeta(), 2);

                if (world.getBlockTileEntity(((TrackedTileEntityCoord)this.savedTileEntities.get(i)).getX(), ((TrackedTileEntityCoord)this.savedTileEntities.get(i)).getY(), ((TrackedTileEntityCoord)this.savedTileEntities.get(i)).getZ()) instanceof TileEntitySkyrootChest)
                {
                    x = (TileEntitySkyrootChest)world.getBlockTileEntity(((TrackedTileEntityCoord)this.savedTileEntities.get(i)).getX(), ((TrackedTileEntityCoord)this.savedTileEntities.get(i)).getY(), ((TrackedTileEntityCoord)this.savedTileEntities.get(i)).getZ());

                    for (y = 0; y < 3 + world.rand.nextInt(3); ++y)
                    {
                        ItemStack z = AetherLoot.NORMAL.getRandomItem(world.rand);

                        if (z.stackSize <= 0)
                        {
                            z.stackSize = 1;
                        }

                        System.out.println(world.isRemote);
                        x.setInventorySlotContents(world.rand.nextInt(x.getSizeInventory()), z);
                    }
                }
            }

            for (i = 0; i < this.savedMobSpawners.size(); ++i)
            {
                int var10 = ((TrackedMobSpawnerCoord)this.savedMobSpawners.get(i)).getX();
                y = ((TrackedMobSpawnerCoord)this.savedMobSpawners.get(i)).getY();
                int var11 = ((TrackedMobSpawnerCoord)this.savedMobSpawners.get(i)).getZ();
                String mobID = ((TrackedMobSpawnerCoord)this.savedMobSpawners.get(i)).getMobID();
                world.setBlockToAir(var10, y, var11);
                world.setBlock(var10, y, var11, Block.mobSpawner.blockID);
                TileEntityMobSpawner spawner = (TileEntityMobSpawner)world.getBlockTileEntity(var10, y, var11);

                if (spawner != null)
                {
                    spawner.getSpawnerLogic().setMobID(mobID);
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

            for (int i = 0; i < this.savedEntityCoords.size(); ++i)
            {
                savedEntityLiving = ((TrackedEntityCoord)this.savedEntityCoords.get(i)).getTrackedEntity(world);
                savedEntityLiving.setPosition((double)((TrackedEntityCoord)this.savedEntityCoords.get(i)).getX(), (double)((TrackedEntityCoord)this.savedEntityCoords.get(i)).getY(), (double)((TrackedEntityCoord)this.savedEntityCoords.get(i)).getZ());
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
