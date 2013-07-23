package net.aetherteam.aether.packets;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
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
        BufferedReader buf = new BufferedReader(new InputStreamReader(dat));

        try
        {
            Side side = FMLCommonHandler.instance().getEffectiveSide();

            if (side.isClient())
            {
                PartyController.instance().getParties().clear();
                byte packetType = dat.readByte();
                int partyAmount = dat.readInt();

                for (int i = 0; i < partyAmount; i++)
                {
                    String partyName = dat.readUTF();
                    String leaderUsername = dat.readUTF();
                    String leaderSkinUrl = "";
                    PartyType partyType = PartyType.getTypeFromString(dat.readUTF());
                    int memberSizeLimit = dat.readInt();
                    Party party = new Party(partyName, new PartyMember(leaderUsername, leaderSkinUrl)).setType(partyType);
                    PartyController.instance().addParty(party, false);
                    int memberAmount = dat.readInt();

                    for (int j = 0; j < memberAmount; j++)
                    {
                        String memberUsername = dat.readUTF();
                        String skinUrl = "";
                        MemberType memberType = MemberType.getTypeFromString(dat.readUTF());
                        PartyMember member = new PartyMember(memberUsername, skinUrl).promoteTo(memberType);
                        PartyController.instance().joinParty(party, member, false);
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

