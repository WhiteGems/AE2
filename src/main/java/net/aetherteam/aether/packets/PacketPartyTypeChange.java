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
import net.aetherteam.aether.party.members.PartyMember;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.packet.Packet250CustomPayload;

public class PacketPartyTypeChange extends AetherPacket
{
    public PacketPartyTypeChange(int packetID)
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

            String partyName = dat.readUTF();
            PartyType newType = PartyType.getTypeFromString(dat.readUTF());

            Side side = FMLCommonHandler.instance().getEffectiveSide();

            if (side.isClient())
            {
                Party party = PartyController.instance().getParty(partyName);

                if (party != null)
                {
                    PartyController.instance().changePartyType(party, newType, false);
                }
            } else
            {
                Party party = PartyController.instance().getParty(partyName);
                PartyMember potentialLeader = PartyController.instance().getMember((EntityPlayer) player);

                if ((party != null) && (potentialLeader != null))
                {
                    if (party.isLeader(potentialLeader))
                    {
                        PartyController.instance().changePartyType(party, newType, false);

                        sendPacketToAllExcept(AetherPacketHandler.sendPartyTypeChange(partyName, newType), player);
                    }
                }
            }
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}

/* Location:           D:\Dev\Mc\forge_orl\mcp\jars\bin\aether.jar
 * Qualified Name:     net.aetherteam.aether.packets.PacketPartyTypeChange
 * JD-Core Version:    0.6.2
 */