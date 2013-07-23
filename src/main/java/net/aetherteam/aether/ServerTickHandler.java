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

    public void tickEnd(EnumSet var1, Object ... var2)
    {
        if (var1.equals(EnumSet.of(TickType.SERVER)))
        {
            Iterator var3 = DungeonHandler.instance().getInstances().iterator();

            while (var3.hasNext())
            {
                Dungeon var4 = (Dungeon)var3.next();

                if (var4.timerStarted() && var4.timerFinished())
                {
                    MinecraftServer var5 = FMLCommonHandler.instance().getMinecraftServerInstance();
                    ServerConfigurationManager var6 = var5.getConfigurationManager();
                    Party var7 = var4.getQueuedParty();
                    EntityPlayer var8 = null;

                    if (var7 != null)
                    {
                        Iterator var9 = var6.playerEntityList.iterator();

                        while (var9.hasNext())
                        {
                            Object var10 = var9.next();

                            if (var10 instanceof EntityPlayer && var7.hasMember(PartyController.instance().getMember((EntityPlayer)var10)) && ((EntityPlayer)var10).username.equalsIgnoreCase(var7.getLeader().username))
                            {
                                var8 = (EntityPlayer)var10;
                            }
                        }

                        if (var8 != null)
                        {
                            TileEntityEntranceController var11 = (TileEntityEntranceController)var8.worldObj.getBlockTileEntity(var4.getControllerX(), var4.getControllerY(), var4.getControllerZ());
                            DungeonHandler.instance().finishDungeon(var4, var7, var11, false);
                            PacketDispatcher.sendPacketToAllPlayers(AetherPacketHandler.sendDungeonFinish(var4, var11, var7));
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

    public void tickStart(EnumSet var1, Object ... var2) {}
}
