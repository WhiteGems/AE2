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
    public PacketSyncAllParties(int var1)
    {
        super(var1);
    }

    public void onPacketReceived(Packet250CustomPayload var1, Player var2)
    {
        DataInputStream var3 = new DataInputStream(new ByteArrayInputStream(var1.data));
        new BufferedReader(new InputStreamReader(var3));

        try
        {
            Side var5 = FMLCommonHandler.instance().getEffectiveSide();

            if (var5.isClient())
            {
                PartyController.instance().getParties().clear();
                byte var6 = var3.readByte();
                int var7 = var3.readInt();

                for (int var8 = 0; var8 < var7; ++var8)
                {
                    String var9 = var3.readUTF();
                    String var10 = var3.readUTF();
                    String var11 = "";
                    PartyType var12 = PartyType.getTypeFromString(var3.readUTF());
                    int var13 = var3.readInt();
                    Party var14 = (new Party(var9, new PartyMember(var10, var11))).setType(var12);
                    PartyController.instance().addParty(var14, false);
                    int var15 = var3.readInt();

                    for (int var16 = 0; var16 < var15; ++var16)
                    {
                        String var17 = var3.readUTF();
                        String var18 = "";
                        MemberType var19 = MemberType.getTypeFromString(var3.readUTF());
                        PartyMember var20 = (new PartyMember(var17, var18)).promoteTo(var19);
                        PartyController.instance().joinParty(var14, var20, false);
                    }
                }
            }
        }
        catch (Exception var21)
        {
            var21.printStackTrace();
        }
    }
}
