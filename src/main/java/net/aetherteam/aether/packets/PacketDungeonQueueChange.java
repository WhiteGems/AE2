package net.aetherteam.aether.packets;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import net.aetherteam.aether.dungeons.Dungeon;
import net.aetherteam.aether.dungeons.DungeonHandler;
import net.aetherteam.aether.party.Party;
import net.aetherteam.aether.party.PartyController;
import net.aetherteam.aether.party.members.PartyMember;
import net.aetherteam.aether.tile_entities.TileEntityEntranceController;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.util.MathHelper;

public class PacketDungeonQueueChange extends AetherPacket
{
    public PacketDungeonQueueChange(int packetID)
    {
        super(packetID);
    }

    public void onPacketReceived(Packet250CustomPayload packet, Player player)
    {
        DataInputStream dat = new DataInputStream(new ByteArrayInputStream(packet.data));
        new BufferedReader(new InputStreamReader(dat));

        try
        {
            byte ex = dat.readByte();
            boolean adding = dat.readBoolean();
            int dungeonID = dat.readInt();
            String partyName = dat.readUTF();
            int tileX = dat.readInt();
            int tileY = dat.readInt();
            int tileZ = dat.readInt();
            Side side = FMLCommonHandler.instance().getEffectiveSide();
            Party party;

            if (side.isClient())
            {
                party = PartyController.instance().getParty(partyName);
                Dungeon potentialLeader = DungeonHandler.instance().getDungeon(dungeonID);
                PartyMember entityPlayer = PartyController.instance().getMember((EntityPlayer)player);
                EntityPlayer dungeon = (EntityPlayer)player;

                if (party != null && potentialLeader != null)
                {
                    if (adding)
                    {
                        DungeonHandler.instance().queueParty(potentialLeader, party, tileX, tileY, tileZ, false);
                    }
                    else
                    {
                        DungeonHandler.instance().disbandQueue(potentialLeader, party, tileX, tileY, tileZ, entityPlayer, false);
                    }
                }
            }
            else
            {
                party = PartyController.instance().getParty(partyName);
                PartyMember potentialLeader1 = PartyController.instance().getMember((EntityPlayer)player);
                EntityPlayerMP entityPlayer1 = (EntityPlayerMP)player;
                Dungeon dungeon1 = DungeonHandler.instance().getDungeon(dungeonID);
                TileEntityEntranceController controller = (TileEntityEntranceController)entityPlayer1.worldObj.getBlockTileEntity(MathHelper.floor_double((double)tileX), MathHelper.floor_double((double)tileY), MathHelper.floor_double((double)tileZ));
                MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
                ServerConfigurationManager configManager = server.getConfigurationManager();
                Iterator x = configManager.playerEntityList.iterator();

                while (x.hasNext())
                {
                    Object y = x.next();

                    if (y instanceof EntityPlayer)
                    {
                        EntityPlayer z = (EntityPlayer)y;

                        if (!z.equals(entityPlayer1) && PartyController.instance().getParty((EntityPlayer)entityPlayer1).hasMember(PartyController.instance().getMember(z)) && z.worldObj.provider.dimensionId != 3)
                        {
                            PacketDispatcher.sendPacketToPlayer(AetherPacketHandler.sendDungeonQueueChange(false, dungeon1, tileX, tileY, tileZ, party), player);
                            entityPlayer1.addChatMessage("\u00a7o All of your party members aren\'t in the Aether!");
                            return;
                        }
                    }
                }

                if (party != null && dungeon1 != null && potentialLeader1 != null)
                {
                    int y1;
                    int z1;
                    int x1;

                    if (party.isLeader(potentialLeader1) && adding && controller != null && !dungeon1.hasAnyConqueredDungeon(party.getMembers()))
                    {
                        x1 = MathHelper.floor_double((double)controller.xCoord);
                        y1 = MathHelper.floor_double((double)controller.yCoord);
                        z1 = MathHelper.floor_double((double)controller.zCoord);
                        System.out.println("Leader was validated, adding the party " + party.getName() + " to the Dungeon\'s queue.");
                        DungeonHandler.instance().queueParty(dungeon1, party, x1, y1, z1, false);
                        this.sendPacketToAllExcept(AetherPacketHandler.sendDungeonQueueChange(adding, dungeon1, x1, y1, z1, party), player);
                    }
                    else if (!adding)
                    {
                        x1 = MathHelper.floor_double((double)controller.xCoord);
                        y1 = MathHelper.floor_double((double)controller.yCoord);
                        z1 = MathHelper.floor_double((double)controller.zCoord);
                        System.out.println("No validation needed, removing party " + partyName + " from the Dungeon queue.");
                        DungeonHandler.instance().disbandQueue(dungeon1, party, x1, y1, z1, potentialLeader1, false);
                        this.sendPacketToAllExcept(AetherPacketHandler.sendDungeonQueueChange(adding, dungeon1, x1, y1, z1, party), player);
                    }
                    else
                    {
                        System.out.println("Something went wrong, the validation of the leader was incorrect. Party not added/removed from Dungeon Instance.");
                    }
                }
            }
        }
        catch (Exception var23)
        {
            var23.printStackTrace();
        }
    }
}
