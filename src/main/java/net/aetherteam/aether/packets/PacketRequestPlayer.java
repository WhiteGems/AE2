package net.aetherteam.aether.packets;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.InputStreamReader;
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
        new BufferedReader(new InputStreamReader(dat));

        try
        {
            byte ex = dat.readByte();
            boolean adding = dat.readBoolean();
            String partyName = dat.readUTF();
            String leaderName = dat.readUTF();
            String requestedPlayer = dat.readUTF();
            Side side = FMLCommonHandler.instance().getEffectiveSide();
            Party party;

            if (side.isClient())
            {
                party = PartyController.instance().getParty(partyName);

                if (party != null)
                {
                    PartyController.instance().requestPlayer(party, PartyController.instance().getMember(leaderName), requestedPlayer, false);
                    System.out.println("Requested Player \'" + requestedPlayer + "\' to the Party: " + partyName + "!");
                }
            }
            else
            {
                party = PartyController.instance().getParty(partyName);
                PartyMember potentialLeader = PartyController.instance().getMember((EntityPlayer)player);

                if (party != null)
                {
                    if (party.isLeader(potentialLeader))
                    {
                        PartyController.instance().requestPlayer(party, potentialLeader, requestedPlayer, false);
                        this.sendPacketToAllExcept(AetherPacketHandler.sendRequestPlayer(adding, partyName, leaderName, requestedPlayer), player);
                    }
                    else if (potentialLeader != null)
                    {
                        System.out.println("A player (" + potentialLeader.username + ") tried to request a member (" + requestedPlayer + ") but didn\'t have permission.");
                    }
                }
                else
                {
                    System.out.println("Something went wrong! The player " + requestedPlayer + " got to requested into a null party!");
                }
            }
        }
        catch (Exception var13)
        {
            var13.printStackTrace();
        }
    }
}
