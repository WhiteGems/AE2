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
    public PacketPartyTypeChange(int var1)
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
            PartyType var7 = PartyType.getTypeFromString(var3.readUTF());
            Side var8 = FMLCommonHandler.instance().getEffectiveSide();
            Party var9;

            if (var8.isClient())
            {
                var9 = PartyController.instance().getParty(var6);

                if (var9 != null)
                {
                    PartyController.instance().changePartyType(var9, var7, false);
                }
            }
            else
            {
                var9 = PartyController.instance().getParty(var6);
                PartyMember var10 = PartyController.instance().getMember((EntityPlayer)var2);

                if (var9 != null && var10 != null && var9.isLeader(var10))
                {
                    PartyController.instance().changePartyType(var9, var7, false);
                    this.sendPacketToAllExcept(AetherPacketHandler.sendPartyTypeChange(var6, var7), var2);
                }
            }
        }
        catch (Exception var11)
        {
            var11.printStackTrace();
        }
    }
}
