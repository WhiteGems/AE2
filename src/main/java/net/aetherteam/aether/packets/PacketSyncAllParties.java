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
import net.aetherteam.aether.party.PartyType;
import net.aetherteam.aether.party.members.MemberType;
import net.aetherteam.aether.party.members.PartyMember;
import net.minecraft.network.packet.Packet250CustomPayload;

public class PacketSyncAllParties extends AetherPacket
{
    public PacketSyncAllParties(int packetID)
    {
        super(packetID);
    }

    public void onPacketReceived(Packet250CustomPayload packet, Player player)
    {
        DataInputStream dat = new DataInputStream(new ByteArrayInputStream(packet.data));
        new BufferedReader(new InputStreamReader(dat));

        try
        {
            Side ex = FMLCommonHandler.instance().getEffectiveSide();

            if (ex.isClient())
            {
                PartyController.instance().getParties().clear();
                byte packetType = dat.readByte();
                int partyAmount = dat.readInt();

                for (int i = 0; i < partyAmount; ++i)
                {
                    String partyName = dat.readUTF();
                    String leaderUsername = dat.readUTF();
                    PartyType partyType = PartyType.getTypeFromString(dat.readUTF());
                    int memberSizeLimit = dat.readInt();
                    Party party = (new Party(partyName, new PartyMember(leaderUsername))).setType(partyType);
                    PartyController.instance().addParty(party, false);
                    int memberAmount = dat.readInt();

                    for (int j = 0; j < memberAmount; ++j)
                    {
                        String memberUsername = dat.readUTF();
                        MemberType memberType = MemberType.getTypeFromString(dat.readUTF());
                        PartyMember member = (new PartyMember(memberUsername)).promoteTo(memberType);
                        PartyController.instance().joinParty(party, member, false);
                    }
                }
            }
        }
        catch (Exception var19)
        {
            var19.printStackTrace();
        }
    }
}
