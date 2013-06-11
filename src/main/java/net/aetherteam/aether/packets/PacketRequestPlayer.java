package net.aetherteam.aether.packets;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

import net.aetherteam.aether.party.Party;
import net.aetherteam.aether.party.PartyController;
import net.aetherteam.aether.party.members.PartyMember;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.packet.Packet250CustomPayload;

public class PacketRequestPlayer extends AetherPacket
{
    public PacketRequestPlayer(int packetID)
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
            String partyName = dat.readUTF();

            String leaderName = dat.readUTF();
            String leaderSkinUrl = dat.readUTF();
            String requestedPlayer = dat.readUTF();

            Side side = FMLCommonHandler.instance().getEffectiveSide();

            if (side.isClient())
            {
                Party party = PartyController.instance().getParty(partyName);

                if (party != null)
                {
                    PartyController.instance().requestPlayer(party, PartyController.instance().getMember(leaderName), requestedPlayer, false);

                    System.out.println("Requested Player '" + requestedPlayer + "' to the Party: " + partyName + "!");
                }
            } else
            {
                Party party = PartyController.instance().getParty(partyName);

                PartyMember potentialLeader = PartyController.instance().getMember((EntityPlayer) player);

                if (party != null)
                {
                    if (party.isLeader(potentialLeader))
                    {
                        PartyController.instance().requestPlayer(party, potentialLeader, requestedPlayer, false);

                        sendPacketToAllExcept(AetherPacketHandler.sendRequestPlayer(adding, partyName, leaderName, leaderSkinUrl, requestedPlayer), player);
                    } else if (potentialLeader != null)
                    {
                        System.out.println("A player (" + potentialLeader.username + ") tried to request a member (" + requestedPlayer + ") but didn't have permission.");
                    }
                } else
                    System.out.println("Something went wrong! The player " + requestedPlayer + " got to requested into a null party!");
            }
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}

/* Location:           D:\Dev\Mc\forge_orl\mcp\jars\bin\aether.jar
 * Qualified Name:     net.aetherteam.aether.packets.PacketRequestPlayer
 * JD-Core Version:    0.6.2
 */