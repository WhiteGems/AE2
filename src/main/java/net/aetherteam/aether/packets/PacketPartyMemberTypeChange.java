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
        new BufferedReader(new InputStreamReader(dat));

        try
        {
            byte ex = dat.readByte();
            String username = dat.readUTF();
            MemberType newType = MemberType.getTypeFromString(dat.readUTF());
            Side side = FMLCommonHandler.instance().getEffectiveSide();
            PartyMember affectedMember;

            if (side.isClient())
            {
                PartyController.instance();
                affectedMember = PartyController.instance().getMember(username);
                PartyController.instance();
                Party potentialLeader = PartyController.instance().getParty(affectedMember);

                if (potentialLeader != null)
                {
                    PartyController.instance();
                    PartyController.instance().promoteMember(affectedMember, newType, false);
                }
            }
            else
            {
                PartyController.instance();
                affectedMember = PartyController.instance().getMember(username);
                PartyController.instance();
                PartyMember potentialLeader1 = PartyController.instance().getMember((EntityPlayer)player);
                PartyController.instance();
                Party party = PartyController.instance().getParty(potentialLeader1);

                if (party != null && potentialLeader1 != null)
                {
                    if (party.isLeader(potentialLeader1))
                    {
                        PartyController.instance();
                        PartyController.instance().promoteMember(affectedMember, newType, false);
                        this.sendPacketToAllExcept(AetherPacketHandler.sendMemberTypeChange(username, newType), player);
                    }
                    else
                    {
                        System.out.println(potentialLeader1.username + " was not the leader of the " + party.getName() + " party! Cannot promote member.");
                    }
                }
                else
                {
                    System.out.println("Something went wrong! Party was null while trying to promote a member.");
                }
            }
        }
        catch (Exception var12)
        {
            var12.printStackTrace();
        }
    }
}
