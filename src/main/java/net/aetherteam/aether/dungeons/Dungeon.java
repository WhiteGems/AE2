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
import net.minecraft.client.audio.SoundManager;
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
    private ArrayList conquerers;
    private HashMap leavers;
    public ArrayList boundingBoxes;
    public StructureBoundingBoxSerial boundingBox;
    private int dungeonID;
    private boolean active;
    private HashMap queuedMembers;
    private Integer[] controllerCoords;
    private long startingTime;
    private boolean timerStarted;
    private int timerLength;
    private ArrayList keyList;
    private ArrayList savedBlockCoords;
    private ArrayList savedEntityCoords;
    private ArrayList savedTileEntities;
    private ArrayList savedMobSpawners;
    String aetherVersion;

    public Dungeon(DungeonType var1, int var2, int var3, StructureBronzeDungeonStart var4)
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
        this.dungeonType = var1;
        this.name = this.dungeonType.getName();
        this.aetherVersion = Aether.getVersion();
        this.centerX = var2;
        this.centerZ = var3;
        this.updateBoundingBox(var4);
        this.dungeonID = DungeonHandler.instance().getInstances().size();
    }

    public Dungeon(DungeonType var1, int var2, int var3, StructureBoundingBoxSerial var4, ArrayList var5)
    {
        this(var1, var2, var3, (StructureBronzeDungeonStart)null);
        this.boundingBox = var4;
        this.boundingBoxes = var5;
    }

    public void addKey(DungeonKey var1)
    {
        this.keyList.add(var1);
    }

    public void removeKey(DungeonKey var1)
    {
        Iterator var2 = this.keyList.iterator();
        DungeonKey var3;

        do
        {
            if (!var2.hasNext())
            {
                return;
            }

            var3 = (DungeonKey)var2.next();
        }
        while (var3.getType() != var1.getType());

        this.keyList.remove(var3);
    }

    public int getKeyAmount()
    {
        return this.keyList.size();
    }

    public int getKeyAmount(DungeonKey var1)
    {
        return this.getKeyAmount(var1.getType());
    }

    public ArrayList getKeys()
    {
        return this.keyList;
    }

    public int getKeyAmount(EnumKeyType var1)
    {
        int var2 = 0;

        if (var1 != null)
        {
            Iterator var3 = this.keyList.iterator();

            while (var3.hasNext())
            {
                DungeonKey var4 = (DungeonKey)var3.next();

                if (var4.getType() == var1)
                {
                    ++var2;
                }
            }
        }

        return var2;
    }

    public boolean hasKeyType(EnumKeyType var1)
    {
        if (var1 != null)
        {
            Iterator var2 = this.keyList.iterator();

            while (var2.hasNext())
            {
                DungeonKey var3 = (DungeonKey)var2.next();

                if (var3.getType() == var1)
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

    public Dungeon setID(int var1)
    {
        this.dungeonID = var1;
        return this;
    }

    public int getID()
    {
        return this.dungeonID;
    }

    public void updateBoundingBox(StructureBronzeDungeonStart var1)
    {
        if (var1 != null)
        {
            this.boundingBox = StructureBoundingBoxSerial.getNewBoundingBox();
            Iterator var2 = var1.components.iterator();

            while (var2.hasNext())
            {
                ComponentDungeonBronzeRoom var3 = (ComponentDungeonBronzeRoom)var2.next();
                StructureBoundingBoxSerial var4 = new StructureBoundingBoxSerial(var3.getBoundingBox());
                this.boundingBoxes.add(var4);
                this.boundingBox.expandTo(var4);
            }
        }
    }

    public void finishDungeon(Party var1)
    {
        if (this.currentActiveParty != null)
        {
            Side var2 = FMLCommonHandler.instance().getEffectiveSide();

            if (this.currentActiveParty.getName().equalsIgnoreCase(var1.getName()) && this.active)
            {
                if (var2.isServer())
                {
                    MinecraftServer var3 = FMLCommonHandler.instance().getMinecraftServerInstance();
                    ServerConfigurationManager var4 = var3.getConfigurationManager();
                    EntityPlayer var5 = null;
                    Iterator var6 = var4.playerEntityList.iterator();

                    while (var6.hasNext())
                    {
                        Object var7 = var6.next();

                        if (var7 instanceof EntityPlayer && var1.hasMember(((EntityPlayer)var7).username.toLowerCase()) && ((EntityPlayer)var7).username.equalsIgnoreCase(var1.getLeader().username.toLowerCase()))
                        {
                            var5 = (EntityPlayer)var7;
                        }
                    }

                    if (var5 != null)
                    {
                        TileEntityEntranceController var10 = (TileEntityEntranceController)var5.worldObj.getBlockTileEntity(this.controllerCoords[0].intValue(), this.controllerCoords[1].intValue(), this.controllerCoords[2].intValue());
                        var10.teleportMembersFromParty(this.getQueuedMembers(), true);
                        this.cleanUpDungeon(var10.worldObj);
                    }
                }

                Iterator var8 = this.getQueuedMembers().iterator();

                while (var8.hasNext())
                {
                    PartyMember var9 = (PartyMember)var8.next();
                    this.conquerers.add(var9.username.toLowerCase());
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

    public boolean hasConqueredDungeon(String var1)
    {
        Iterator var2 = this.conquerers.iterator();
        String var3;

        do
        {
            if (!var2.hasNext())
            {
                var2 = this.leavers.keySet().iterator();

                do
                {
                    if (!var2.hasNext())
                    {
                        return false;
                    }

                    var3 = (String)var2.next();

                    if (this.leavers.get(var1.toLowerCase()) != null)
                    {
                        ;
                    }
                }
                while (!var1.equalsIgnoreCase(var3) || this.leavers.get(var1.toLowerCase()) == null || ((Integer)this.leavers.get(var1.toLowerCase())).intValue() < 3);

                return true;
            }

            var3 = (String)var2.next();
        }
        while (!var1.equalsIgnoreCase(var3));

        return true;
    }

    public boolean hasConqueredDungeon(EntityPlayer var1)
    {
        return this.hasConqueredDungeon(var1.username);
    }

    public boolean hasConqueredDungeon(PartyMember var1)
    {
        return this.hasConqueredDungeon(var1.username);
    }

    public boolean hasAnyConqueredDungeon(ArrayList var1)
    {
        for (int var2 = 0; var2 < var1.size(); ++var2)
        {
            if (var1.get(var2) instanceof PartyMember)
            {
                if (this.hasConqueredDungeon((PartyMember)var1.get(var2)))
                {
                    return true;
                }
            }
            else if (var1.get(var2) instanceof EntityPlayer)
            {
                if (this.hasConqueredDungeon((EntityPlayer)var1.get(var2)))
                {
                    return true;
                }
            }
            else if (var1.get(var2) instanceof String && this.hasConqueredDungeon((String)var1.get(var2)))
            {
                return true;
            }
        }

        return false;
    }

    public boolean hasListConqueredDungeon(ArrayList var1)
    {
        return !this.hasAnyConqueredDungeon(var1);
    }

    public void queueParty(Party var1, int var2, int var3, int var4)
    {
        if (this.currentActiveParty == null && var1 != null)
        {
            this.currentActiveParty = var1;
            this.controllerCoords[0] = Integer.valueOf(MathHelper.floor_double((double)var2));
            this.controllerCoords[1] = Integer.valueOf(MathHelper.floor_double((double)var3));
            this.controllerCoords[2] = Integer.valueOf(MathHelper.floor_double((double)var4));
        }

        DungeonHandler.instance().saveDungeons();
    }

    public boolean isSoundOn()
    {
        boolean var1;

        if (this.isClient() && Aether.proxy.getClient().sndManager != null)
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
        SoundManager var10000 = Aether.proxy.getClient().sndManager;
        boolean var1;

        if (SoundManager.sndSystem != null)
        {
            var10000 = Aether.proxy.getClient().sndManager;

            if (SoundManager.sndSystem.playing("streaming"))
            {
                var1 = true;
                return var1;
            }
        }

        var1 = false;
        return var1;
    }

    public void turnMusicOff()
    {
        if (this.isSoundOn())
        {
            SoundManager var10000 = Aether.proxy.getClient().sndManager;
            SoundManager.sndSystem.stop("streaming");
        }
    }

    public void playMusicFile(String var1, int var2, int var3, int var4)
    {
        if (this.isSoundOn())
        {
            Aether.proxy.getClient().sndManager.playStreaming(var1, (float)var2, (float)var3, (float)var4);
        }
    }

    public void checkForQueue()
    {
        Side var1 = FMLCommonHandler.instance().getEffectiveSide();

        if (this.currentActiveParty != null && this.queuedMembers.size() >= this.currentActiveParty.getMembers().size() && !this.hasStarted && this.currentActiveParty.getMembers() != null)
        {
            this.hasStarted = true;

            if (var1.isServer())
            {
                MinecraftServer var2 = FMLCommonHandler.instance().getMinecraftServerInstance();
                ServerConfigurationManager var3 = var2.getConfigurationManager();
                EntityPlayer var4 = null;
                Iterator var5 = var3.playerEntityList.iterator();

                while (var5.hasNext())
                {
                    Object var6 = var5.next();

                    if (var6 instanceof EntityPlayer && this.currentActiveParty.hasMember(((EntityPlayer)var6).username.toLowerCase()))
                    {
                        if (((EntityPlayer)var6).username.equalsIgnoreCase(this.currentActiveParty.getLeader().username.toLowerCase()))
                        {
                            var4 = (EntityPlayer)var6;
                        }

                        EntityPlayer var7 = (EntityPlayer)var6;

                        if (var7.dimension != 3)
                        {
                            TileEntityEntranceController var8 = (TileEntityEntranceController)var4.worldObj.getBlockTileEntity(this.controllerCoords[0].intValue(), this.controllerCoords[1].intValue(), this.controllerCoords[2].intValue());

                            if (var8 != null)
                            {
                                int var9 = MathHelper.floor_double((double)var8.xCoord);
                                int var10 = MathHelper.floor_double((double)var8.yCoord);
                                int var11 = MathHelper.floor_double((double)var8.zCoord);
                                DungeonHandler.instance().disbandQueue(this, this.currentActiveParty, var9, var10, var11, PartyController.instance().getMember((EntityPlayer)var6), false);
                                PacketDispatcher.sendPacketToAllPlayers(AetherPacketHandler.sendDungeonQueueChange(false, this, var9, var10, var11, this.currentActiveParty));
                            }

                            return;
                        }
                    }
                }

                if (var4 != null)
                {
                    TileEntityEntranceController var12 = (TileEntityEntranceController)var4.worldObj.getBlockTileEntity(this.controllerCoords[0].intValue(), this.controllerCoords[1].intValue(), this.controllerCoords[2].intValue());
                    var12.teleportMembersFromParty(this.currentActiveParty.getMembers(), false);
                    this.regenBosses(var4.worldObj);
                }
            }
            else if (this.hasMember(PartyController.instance().getMember(Minecraft.getMinecraft().session.username)))
            {
                this.playMusicFile("Dungeon Background", this.controllerCoords[0].intValue(), this.controllerCoords[1].intValue(), this.controllerCoords[2].intValue());
            }
        }
    }

    public void queueMember(Party var1, PartyMember var2)
    {
        Side var3 = FMLCommonHandler.instance().getEffectiveSide();

        if (this.isQueuedParty(var1) && var1.hasMember(var2))
        {
            this.setActive(true);
            this.queuedMembers.put(var2.username.toLowerCase(), var2);
            this.checkForQueue();
        }

        DungeonHandler.instance().saveDungeons();
    }

    public void startTimer(int var1)
    {
        this.timerLength = var1;
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
        long var1 = System.currentTimeMillis() / 1000L;
        return (int)(var1 - this.startingTime);
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

    public boolean isQueuedParty(Party var1)
    {
        return var1 != null && this.currentActiveParty != null && this.currentActiveParty.getName().equalsIgnoreCase(var1.getName());
    }

    public boolean isActive()
    {
        return this.active;
    }

    public void setActive(boolean var1)
    {
        this.active = var1;
    }

    public void disbandQueue(Party var1)
    {
        Side var2 = FMLCommonHandler.instance().getEffectiveSide();

        if (this.currentActiveParty != null)
        {
            if (this.currentActiveParty.getName().equalsIgnoreCase(var1.getName()) && this.active && this.getQueuedMembers() != null && var2.isServer())
            {
                MinecraftServer var3 = FMLCommonHandler.instance().getMinecraftServerInstance();
                ServerConfigurationManager var4 = var3.getConfigurationManager();
                EntityPlayer var5 = null;
                Iterator var6 = var4.playerEntityList.iterator();

                while (var6.hasNext())
                {
                    Object var7 = var6.next();

                    if (var7 instanceof EntityPlayer && var1.hasMember(((EntityPlayer)var7).username.toLowerCase()) && ((EntityPlayer)var7).username.equalsIgnoreCase(var1.getLeader().username.toLowerCase()))
                    {
                        var5 = (EntityPlayer)var7;
                    }
                }

                if (var5 != null)
                {
                    TileEntityEntranceController var8 = (TileEntityEntranceController)var5.worldObj.getBlockTileEntity(this.controllerCoords[0].intValue(), this.controllerCoords[1].intValue(), this.controllerCoords[2].intValue());
                    var8.teleportMembersFromParty(this.getQueuedMembers(), true);
                    this.cleanUpDungeon(var8.worldObj);
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

    public void disbandMember(PartyMember var1)
    {
        Side var2 = FMLCommonHandler.instance().getEffectiveSide();
        Object var3 = null;

        if (this.currentActiveParty != null && this.hasMember(var1))
        {
            if (var2.isServer())
            {
                MinecraftServer var4 = FMLCommonHandler.instance().getMinecraftServerInstance();
                ServerConfigurationManager var5 = var4.getConfigurationManager();
                EntityPlayer var6 = null;
                Iterator var7 = var5.playerEntityList.iterator();

                while (var7.hasNext())
                {
                    Object var8 = var7.next();

                    if (var8 instanceof EntityPlayer && var1.username.equalsIgnoreCase(((EntityPlayer)var8).username.toLowerCase()))
                    {
                        var6 = (EntityPlayer)var8;
                    }
                }

                if (var6 != null)
                {
                    TileEntityEntranceController var10000 = (TileEntityEntranceController)var6.worldObj.getBlockTileEntity(this.controllerCoords[0].intValue(), this.controllerCoords[1].intValue(), this.controllerCoords[2].intValue());
                    var6.setPositionAndUpdate((double)this.controllerCoords[0].intValue() + 0.5D, (double)this.controllerCoords[1].intValue() + 1.5D, (double)this.controllerCoords[2].intValue() + 3.5D);
                }
            }

            this.queuedMembers.remove(var1.username.toLowerCase());
            int var9 = this.leavers.containsKey(var1.username.toLowerCase()) && this.leavers.get(var1.username.toLowerCase()) != null ? ((Integer)this.leavers.get(var1.username.toLowerCase())).intValue() : 0;
            this.leavers.put(var1.username.toLowerCase(), Integer.valueOf(var9 + 1));

            if (this.queuedMembers.size() <= 0)
            {
                this.disbandQueue(this.currentActiveParty);
                this.setActive(false);
                this.hasStarted = false;
            }
        }

        DungeonHandler.instance().saveDungeons();
    }

    public boolean hasMember(PartyMember var1)
    {
        return var1 != null && this.queuedMembers.get(var1.username.toLowerCase()) != null;
    }

    public int getMemberLeaves(PartyMember var1)
    {
        return this.leavers.get(var1.username.toLowerCase()) != null ? ((Integer)this.leavers.get(var1.username.toLowerCase())).intValue() : 0;
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

    public ArrayList getQueuedMembers()
    {
        ArrayList var1 = new ArrayList(this.queuedMembers.values());
        return var1;
    }

    public void cleanUpDungeon(World var1)
    {
        Side var2 = FMLCommonHandler.instance().getEffectiveSide();
        this.setActive(false);

        if (var2.isServer() && !var1.isRemote)
        {
            Object var3 = null;
            int var4;

            for (var4 = 0; var4 < this.savedBlockCoords.size(); ++var4)
            {
                var1.setBlockToAir(((TrackedCoord)this.savedBlockCoords.get(var4)).getX(), ((TrackedCoord)this.savedBlockCoords.get(var4)).getY(), ((TrackedCoord)this.savedBlockCoords.get(var4)).getZ());
            }

            int var6;

            for (var4 = 0; var4 < this.savedTileEntities.size(); ++var4)
            {
                TileEntitySkyrootChest var5;

                if (var1.getBlockTileEntity(((TrackedTileEntityCoord)this.savedTileEntities.get(var4)).getX(), ((TrackedTileEntityCoord)this.savedTileEntities.get(var4)).getY(), ((TrackedTileEntityCoord)this.savedTileEntities.get(var4)).getZ()) != null && var1.getBlockTileEntity(((TrackedTileEntityCoord)this.savedTileEntities.get(var4)).getX(), ((TrackedTileEntityCoord)this.savedTileEntities.get(var4)).getY(), ((TrackedTileEntityCoord)this.savedTileEntities.get(var4)).getZ()) instanceof TileEntitySkyrootChest)
                {
                    var5 = (TileEntitySkyrootChest)var1.getBlockTileEntity(((TrackedTileEntityCoord)this.savedTileEntities.get(var4)).getX(), ((TrackedTileEntityCoord)this.savedTileEntities.get(var4)).getY(), ((TrackedTileEntityCoord)this.savedTileEntities.get(var4)).getZ());
                    var5.getChestContents();

                    for (var6 = 0; var6 < var5.getSizeInventory(); ++var6)
                    {
                        var5.setInventorySlotContents(var6, (ItemStack)null);
                    }
                }

                var1.setBlockToAir(((TrackedTileEntityCoord)this.savedTileEntities.get(var4)).getX(), ((TrackedTileEntityCoord)this.savedTileEntities.get(var4)).getY(), ((TrackedTileEntityCoord)this.savedTileEntities.get(var4)).getZ());
                var1.setBlock(((TrackedTileEntityCoord)this.savedTileEntities.get(var4)).getX(), ((TrackedTileEntityCoord)this.savedTileEntities.get(var4)).getY(), ((TrackedTileEntityCoord)this.savedTileEntities.get(var4)).getZ(), ((TrackedTileEntityCoord)this.savedTileEntities.get(var4)).getBlock(), ((TrackedTileEntityCoord)this.savedTileEntities.get(var4)).getBlockMeta(), 2);

                if (var1.getBlockTileEntity(((TrackedTileEntityCoord)this.savedTileEntities.get(var4)).getX(), ((TrackedTileEntityCoord)this.savedTileEntities.get(var4)).getY(), ((TrackedTileEntityCoord)this.savedTileEntities.get(var4)).getZ()) instanceof TileEntitySkyrootChest)
                {
                    var5 = (TileEntitySkyrootChest)var1.getBlockTileEntity(((TrackedTileEntityCoord)this.savedTileEntities.get(var4)).getX(), ((TrackedTileEntityCoord)this.savedTileEntities.get(var4)).getY(), ((TrackedTileEntityCoord)this.savedTileEntities.get(var4)).getZ());

                    for (var6 = 0; var6 < 3 + var1.rand.nextInt(3); ++var6)
                    {
                        ItemStack var7 = AetherLoot.NORMAL.getRandomItem(var1.rand);

                        if (var7.stackSize <= 0)
                        {
                            var7.stackSize = 1;
                        }

                        System.out.println(var1.isRemote);
                        var5.setInventorySlotContents(var1.rand.nextInt(var5.getSizeInventory()), var7);
                    }
                }
            }

            for (var4 = 0; var4 < this.savedMobSpawners.size(); ++var4)
            {
                int var10 = ((TrackedMobSpawnerCoord)this.savedMobSpawners.get(var4)).getX();
                var6 = ((TrackedMobSpawnerCoord)this.savedMobSpawners.get(var4)).getY();
                int var11 = ((TrackedMobSpawnerCoord)this.savedMobSpawners.get(var4)).getZ();
                String var8 = ((TrackedMobSpawnerCoord)this.savedMobSpawners.get(var4)).getMobID();
                var1.setBlockToAir(var10, var6, var11);
                var1.setBlock(var10, var6, var11, Block.mobSpawner.blockID);
                TileEntityMobSpawner var9 = (TileEntityMobSpawner)var1.getBlockTileEntity(var10, var6, var11);

                if (var9 != null)
                {
                    var9.func_98049_a().setMobID(var8);
                }
            }
        }

        DungeonHandler.instance().saveDungeons();
    }

    public void regenBosses(World var1)
    {
        Side var2 = FMLCommonHandler.instance().getEffectiveSide();

        if (var2.isServer())
        {
            EntityLiving var3 = null;

            for (int var4 = 0; var4 < this.savedEntityCoords.size(); ++var4)
            {
                var3 = ((TrackedEntityCoord)this.savedEntityCoords.get(var4)).getTrackedEntity(var1);
                var3.setPosition((double)((TrackedEntityCoord)this.savedEntityCoords.get(var4)).getX(), (double)((TrackedEntityCoord)this.savedEntityCoords.get(var4)).getY(), (double)((TrackedEntityCoord)this.savedEntityCoords.get(var4)).getZ());
                var1.spawnEntityInWorld(var3);
            }
        }

        DungeonHandler.instance().saveDungeons();
    }

    public boolean hasStarted()
    {
        return this.hasStarted;
    }

    public void registerBlockPlacement(int var1, int var2, int var3)
    {
        Side var4 = FMLCommonHandler.instance().getEffectiveSide();

        if (var4.isServer())
        {
            this.savedBlockCoords.add(new TrackedCoord(var1, var2, var3));
        }

        DungeonHandler.instance().saveDungeons();
    }

    public void registerEntity(float var1, float var2, float var3, EntityLiving var4)
    {
        Side var5 = FMLCommonHandler.instance().getEffectiveSide();

        if (var5.isServer())
        {
            this.savedEntityCoords.add(new TrackedEntityCoord(var1, var2, var3, EntityList.getEntityString(var4)));
        }

        DungeonHandler.instance().saveDungeons();
    }

    public void registerSafeBlock(int var1, int var2, int var3, int var4, int var5)
    {
        Side var6 = FMLCommonHandler.instance().getEffectiveSide();

        if (var6.isServer())
        {
            this.savedTileEntities.add(new TrackedTileEntityCoord(var1, var2, var3, var4, var5));
        }

        DungeonHandler.instance().saveDungeons();
    }

    public void registerMobSpawner(int var1, int var2, int var3, String var4)
    {
        Side var5 = FMLCommonHandler.instance().getEffectiveSide();

        if (var5.isServer())
        {
            this.savedMobSpawners.add(new TrackedMobSpawnerCoord(var1, var2, var3, var4));
        }

        DungeonHandler.instance().saveDungeons();
    }
}
