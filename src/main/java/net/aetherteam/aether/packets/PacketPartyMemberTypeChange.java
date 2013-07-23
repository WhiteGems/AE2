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
    public PacketPartyMemberTypeChange(int var1)
    {
        super(var1);
    }

    public void onPacketReceived(Packet250CustomPayload var1, Player var2)
    {
        DataInputStream var3 = new DataInputStream(new ByteArrayInputStream(var1.data));
        new BufferedReader(new InputStreamReader(var3));

        try
        {
            byte var5 = var3.readByte();
            String var6 = var3.readUTF();
            MemberType var7 = MemberType.getTypeFromString(var3.readUTF());
            Side var8 = FMLCommonHandler.instance().getEffectiveSide();
            PartyMember var9;

            if (var8.isClient())
            {
                PartyController.instance();
                var9 = PartyController.instance().getMember(var6);
                PartyController.instance();
                Party var10 = PartyController.instance().getParty(var9);

                if (var10 != null)
                {
                    PartyController.instance();
                    PartyController.instance().promoteMember(var9, var7, false);
                }
            }
            else
            {
                PartyController.instance();
                var9 = PartyController.instance().getMember(var6);
                PartyController.instance();
                PartyMember var13 = PartyController.instance().getMember((EntityPlayer)var2);
                PartyController.instance();
                Party var11 = PartyController.instance().getParty(var13);

                if (var11 != null && var13 != null)
                {
                    if (var11.isLeader(var13))
                    {
                        PartyController.instance();
                        PartyController.instance().promoteMember(var9, var7, false);
                        this.sendPacketToAllExcept(AetherPacketHandler.sendMemberTypeChange(var6, var7), var2);
                    }
                    else
                    {
                        System.out.println(var13.username + " was not the leader of the " + var11.getName() + " party! Cannot promote member.");
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
