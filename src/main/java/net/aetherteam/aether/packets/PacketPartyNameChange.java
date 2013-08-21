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

public class PacketPartyNameChange extends AetherPacket
{
    public PacketPartyNameChange(int packetID)
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
            String partyName = dat.readUTF();
            String newPartyName = dat.readUTF();
            Side side = FMLCommonHandler.instance().getEffectiveSide();
            Party party;

            if (side.isClient())
            {
                party = PartyController.instance().getParty(partyName);

                if (party != null && !newPartyName.isEmpty() && PartyController.instance().getParty(newPartyName) == null)
                {
                    PartyController.instance().changePartyName(party, newPartyName, false);
                    System.out.println("\'" + partyName + "\'s name changed to \'" + newPartyName + "\'");
                }
                else
                {
                    System.out.println("Either the party was null or the name was empty. Party name change unsuccessful!");
                }
            }
            else
            {
                party = PartyController.instance().getParty(partyName);
                PartyMember potentialLeader = PartyController.instance().getMember((EntityPlayer)player);

                if (party != null && !newPartyName.isEmpty() && PartyController.instance().getParty(newPartyName) == null)
                {
                    if (party.isLeader(potentialLeader))
                    {
                        PartyController.instance().changePartyName(party, newPartyName, false);
                        this.sendPacketToAllExcept(AetherPacketHandler.sendPartyNameChange(partyName, newPartyName), player);
                    }
                    else
                    {
                        System.out.println("A player (" + potentialLeader.username + ") tried to change the name of a party (" + party.getName() + ") but didn\'t have permission.");
                    }
                }
                else
                {
                    System.out.println("Something went wrong! The player " + potentialLeader.username + " tried to change the name of a null party!");
                }
            }
        }
        catch (Exception var11)
        {
            var11.printStackTrace();
        }
    }
}
