package net.aetherteam.aether.packets;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.List;
import net.aetherteam.aether.dungeons.Dungeon;
import net.aetherteam.aether.dungeons.DungeonHandler;
import net.aetherteam.aether.party.Party;
import net.aetherteam.aether.party.PartyController;
import net.aetherteam.aether.party.members.PartyMember;
import net.aetherteam.aether.tile_entities.TileEntityEntranceController;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;

public class PacketDungeonQueueChange extends AetherPacket
{
    public PacketDungeonQueueChange(int packetID)
    {
        super(packetID);
    }

    public void onPacketReceived(Packet250CustomPayload packet, Player player)
    {
        DataInputStream dat = new DataInputStream(new ByteArrayInputStream(packet.data));
        BufferedReader buf = new BufferedReader(new InputStreamReader(dat));

        try
        {
            byte packetType = dat.readByte();
            boolean adding = dat.readBoolean();
            int dungeonID = dat.readInt();
            String partyName = dat.readUTF();
            int tileX = dat.readInt();
            int tileY = dat.readInt();
            int tileZ = dat.readInt();
            Side side = FMLCommonHandler.instance().getEffectiveSide();

            if (side.isClient())
            {
                Party party = PartyController.instance().getParty(partyName);
                Dungeon dungeon = DungeonHandler.instance().getDungeon(dungeonID);
                PartyMember potentialLeader = PartyController.instance().getMember((EntityPlayer)player);
                EntityPlayer entityPlayer = (EntityPlayer)player;

                if ((party != null) && (dungeon != null))
                {
                    if (adding)
                    {
                        DungeonHandler.instance().queueParty(dungeon, party, tileX, tileY, tileZ, false);
                    }
                    else
                    {
                        DungeonHandler.instance().disbandQueue(dungeon, party, tileX, tileY, tileZ, potentialLeader, false);
                    }
                }
            }
            else
            {
                Party party = PartyController.instance().getParty(partyName);
                PartyMember potentialLeader = PartyController.instance().getMember((EntityPlayer)player);
                EntityPlayerMP entityPlayer = (EntityPlayerMP)player;
                Dungeon dungeon = DungeonHandler.instance().getDungeon(dungeonID);
                TileEntityEntranceController controller = (TileEntityEntranceController)entityPlayer.worldObj.getBlockTileEntity(MathHelper.floor_double(tileX), MathHelper.floor_double(tileY), MathHelper.floor_double(tileZ));
                MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
                ServerConfigurationManager configManager = server.getConfigurationManager();

                for (Iterator i$ = configManager.playerEntityList.iterator(); i$.hasNext();)
                {
                    Object obj = i$.next();

                    if ((obj instanceof EntityPlayer))
                    {
                        EntityPlayer entityPlayer1 = (EntityPlayer)obj;

                        if ((!entityPlayer1.equals(entityPlayer)) && (PartyController.instance().getParty(entityPlayer).hasMember(PartyController.instance().getMember(entityPlayer1))) &&
                                (entityPlayer1.worldObj.provider.dimensionId != 3))
                        {
                            PacketDispatcher.sendPacketToPlayer(AetherPacketHandler.sendDungeonQueueChange(false, dungeon, tileX, tileY, tileZ, party), player);
                            entityPlayer.addChatMessage("§o All of your party members aren't in the Aether!");
                            return;
                        }
                    }
                }

                if ((party != null) && (dungeon != null) && (potentialLeader != null))
                {
                    if ((party.isLeader(potentialLeader)) && (adding) && (controller != null) && (!dungeon.hasAnyConqueredDungeon(party.getMembers())))
                    {
                        int x = MathHelper.floor_double(controller.xCoord);
                        int y = MathHelper.floor_double(controller.yCoord);
                        int z = MathHelper.floor_double(controller.zCoord);
                        System.out.println("Leader was validated, adding the party " + party.getName() + " to the Dungeon's queue.");
                        DungeonHandler.instance().queueParty(dungeon, party, x, y, z, false);
                        sendPacketToAllExcept(AetherPacketHandler.sendDungeonQueueChange(adding, dungeon, x, y, z, party), player);
                    }
                    else if (!adding)
                    {
                        int x = MathHelper.floor_double(controller.xCoord);
                        int y = MathHelper.floor_double(controller.yCoord);
                        int z = MathHelper.floor_double(controller.zCoord);
                        System.out.println("No validation needed, removing party " + partyName + " from the Dungeon queue.");
                        DungeonHandler.instance().disbandQueue(dungeon, party, x, y, z, potentialLeader, false);
                        sendPacketToAllExcept(AetherPacketHandler.sendDungeonQueueChange(adding, dungeon, x, y, z, party), player);
                    }
                    else
                    {
                        System.out.println("Something went wrong, the validation of the leader was incorrect. Party not added/removed from Dungeon Instance.");
                    }
                }
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}

