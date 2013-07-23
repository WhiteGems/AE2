package net.aetherteam.aether.packets;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.InputStreamReader;
import net.aetherteam.aether.dungeons.Dungeon;
import net.aetherteam.aether.dungeons.DungeonHandler;
import net.aetherteam.aether.party.Party;
import net.aetherteam.aether.party.PartyController;
import net.aetherteam.aether.party.members.PartyMember;
import net.aetherteam.aether.tile_entities.TileEntityEntranceController;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.world.World;

public class PacketDungeonFinish extends AetherPacket
{
    public PacketDungeonFinish(int packetID)
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
                TileEntityEntranceController controller = (TileEntityEntranceController)entityPlayer.worldObj.getBlockTileEntity(tileX, tileY, tileZ);

                if ((party != null) && (dungeon != null))
                {
                    if (controller != null)
                    {
                        DungeonHandler.instance().finishDungeon(dungeon, party, controller, false);
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

