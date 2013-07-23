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
import net.aetherteam.aether.party.members.MemberType;
import net.aetherteam.aether.party.members.PartyMember;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.packet.Packet250CustomPayload;

public class PacketPartyMemberTypeChange extends AetherPacket
{
    public PacketPartyMemberTypeChange(int packetID)
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
            String username = dat.readUTF();
            MemberType newType = MemberType.getTypeFromString(dat.readUTF());
            Side side = FMLCommonHandler.instance().getEffectiveSide();

            if (side.isClient())
            {
                PartyController.instance();
                PartyMember affectedMember = PartyController.instance().getMember(username);
                PartyController.instance();
                Party party = PartyController.instance().getParty(affectedMember);

                if (party != null)
                {
                    PartyController.instance();
                    PartyController.instance().promoteMember(affectedMember, newType, false);
                }
            }
            else
            {
                PartyController.instance();
                PartyMember affectedMember = PartyController.instance().getMember(username);
                PartyController.instance();
                PartyMember potentialLeader = PartyController.instance().getMember((EntityPlayer)player);
                PartyController.instance();
                Party party = PartyController.instance().getParty(potentialLeader);

                if ((party != null) && (potentialLeader != null))
                {
                    if (party.isLeader(potentialLeader))
                    {
                        PartyController.instance();
                        PartyController.instance().promoteMember(affectedMember, newType, false);
                        sendPacketToAllExcept(AetherPacketHandler.sendMemberTypeChange(username, newType), player);
                    }
                    else
                    {
                        System.out.println(potentialLeader.username + " was not the leader of the " + party.getName() + " party! Cannot promote member.");
                    }
                }
                else
                {
                    System.out.println("Something went wrong! Party was null while trying to promote a member.");
                }
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}

