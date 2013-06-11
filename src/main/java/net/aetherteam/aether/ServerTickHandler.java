package net.aetherteam.aether;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.network.PacketDispatcher;

import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;

import net.aetherteam.aether.dungeons.Dungeon;
import net.aetherteam.aether.dungeons.DungeonHandler;
import net.aetherteam.aether.packets.AetherPacketHandler;
import net.aetherteam.aether.party.Party;
import net.aetherteam.aether.party.PartyController;
import net.aetherteam.aether.party.members.PartyMember;
import net.aetherteam.aether.tile_entities.TileEntityEntranceController;
import net.minecraft.entity.Entity;
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

    public void tickEnd(EnumSet type, Object[] tickData)
    {
        if (type.equals(EnumSet.of(TickType.SERVER)))
        {
            for (Dungeon dungeon : DungeonHandler.instance().getInstances())
            {
                if ((dungeon.timerStarted()) && (dungeon.timerFinished()))
                {
                    MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
                    ServerConfigurationManager configManager = server.getConfigurationManager();

                    Party party = dungeon.getQueuedParty();

                    EntityPlayer partyLeader = null;

                    if (party != null)
                    {
                        for (Iterator i$ = configManager.playerEntityList.iterator(); i$.hasNext(); )
                        {
                            Object obj = i$.next();

                            if (((obj instanceof EntityPlayer)) && (party.hasMember(PartyController.instance().getMember((EntityPlayer) obj))))
                            {
                                if (((EntityPlayer) obj).username.equalsIgnoreCase(party.getLeader().username))
                                {
                                    partyLeader = (EntityPlayer) obj;
                                }
                            }
                        }

                        if (partyLeader != null)
                        {
                            TileEntityEntranceController controller = (TileEntityEntranceController) partyLeader.worldObj.getBlockTileEntity(dungeon.getControllerX(), dungeon.getControllerY(), dungeon.getControllerZ());

                            DungeonHandler.instance().finishDungeon(dungeon, party, controller, false);

                            PacketDispatcher.sendPacketToAllPlayers(AetherPacketHandler.sendDungeonFinish(dungeon, controller, party));
                        }

                        return;
                    }
                }
            }
        }
    }

    public EnumSet ticks()
    {
        return EnumSet.of(TickType.SERVER);
    }

    public void tickStart(EnumSet type, Object[] tickData)
    {
    }
}

/* Location:           D:\Dev\Mc\forge_orl\mcp\jars\bin\aether.jar
 * Qualified Name:     net.aetherteam.aether.ServerTickHandler
 * JD-Core Version:    0.6.2
 */