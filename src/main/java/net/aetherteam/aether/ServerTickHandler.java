package net.aetherteam.aether;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.network.PacketDispatcher;
import java.util.EnumSet;
import java.util.Iterator;
import net.aetherteam.aether.dungeons.Dungeon;
import net.aetherteam.aether.dungeons.DungeonHandler;
import net.aetherteam.aether.packets.AetherPacketHandler;
import net.aetherteam.aether.party.Party;
import net.aetherteam.aether.party.PartyController;
import net.aetherteam.aether.tile_entities.TileEntityEntranceController;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.world.World;

public class ServerTickHandler implements ITickHandler
{
    public World lastWorld = null;
    public boolean needPreGen = true;

    public String getLabel()
    {
        return null;
    }

    public void tickEnd(EnumSet<TickType> type, Object ... tickData)
    {
        if (type.equals(EnumSet.of(TickType.SERVER)))
        {
            Iterator i$ = DungeonHandler.instance().getInstances().iterator();

            while (i$.hasNext())
            {
                Dungeon dungeon = (Dungeon)i$.next();

                if (dungeon.timerStarted() && dungeon.timerFinished())
                {
                    MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
                    ServerConfigurationManager configManager = server.getConfigurationManager();
                    Party party = dungeon.getQueuedParty();
                    EntityPlayer partyLeader = null;

                    if (party != null)
                    {
                        Iterator controller = configManager.playerEntityList.iterator();

                        while (controller.hasNext())
                        {
                            Object obj = controller.next();

                            if (obj instanceof EntityPlayer && party.hasMember(PartyController.instance().getMember((EntityPlayer)obj)) && ((EntityPlayer)obj).username.equalsIgnoreCase(party.getLeader().username))
                            {
                                partyLeader = (EntityPlayer)obj;
                            }
                        }

                        if (partyLeader != null)
                        {
                            TileEntityEntranceController controller1 = (TileEntityEntranceController)partyLeader.worldObj.getBlockTileEntity(dungeon.getControllerX(), dungeon.getControllerY(), dungeon.getControllerZ());
                            DungeonHandler.instance().finishDungeon(dungeon, party, controller1, false);
                            PacketDispatcher.sendPacketToAllPlayers(AetherPacketHandler.sendDungeonFinish(dungeon, controller1, party));
                        }

                        return;
                    }
                }
            }
        }
    }

    public EnumSet<TickType> ticks()
    {
        return EnumSet.of(TickType.SERVER);
    }

    public void tickStart(EnumSet<TickType> type, Object ... tickData) {}
}
