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

public class PacketPartyChange extends AetherPacket
{
    public PacketPartyChange(int packetID)
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
            String potentialLeaderName = dat.readUTF();
            String skinUrl = dat.readUTF();
            Side side = FMLCommonHandler.instance().getEffectiveSide();

            if (side.isClient())
            {
                Party party = PartyController.instance().getParty(partyName);

                if ((adding) && (party == null))
                {
                    PartyController.instance().addParty(new Party(partyName, new PartyMember(potentialLeaderName, skinUrl)), false);
                    System.out.println(partyName + " created!");
                }
                else
                {
                    PartyController.instance().removeParty(party, false);
                    System.out.println(partyName + " removed!");
                }
            }
            else
            {
                Party party = PartyController.instance().getParty(partyName);
                PartyMember potentialLeader = PartyController.instance().getMember((EntityPlayer)player);
                EntityPlayer entityPlayer = (EntityPlayer)player;

                if ((adding) && (party == null) && (potentialLeader == null))
                {
                    System.out.println("No validation needed, creating and adding party " + partyName);
                    PartyController.instance().addParty(new Party(partyName, new PartyMember((EntityPlayer)player)), false);
                    sendPacketToAllExcept(AetherPacketHandler.sendPartyChange(adding, partyName, potentialLeaderName, skinUrl), player);
                }
                else if ((potentialLeader != null) && (party != null) && (party.isLeader(potentialLeader)) && (entityPlayer.username.equalsIgnoreCase(potentialLeaderName)) && (!adding))
                {
                    System.out.println("Leader was validated, removing the party " + party.getName());
                    PartyController.instance().removeParty(party, false);
                    sendPacketToAllExcept(AetherPacketHandler.sendPartyChange(adding, partyName, potentialLeaderName, skinUrl), player);
                }
                else
                {
                    System.out.println("Something went wrong, the validation of the leader was incorrect. Party not added/removed.");
                }
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}

