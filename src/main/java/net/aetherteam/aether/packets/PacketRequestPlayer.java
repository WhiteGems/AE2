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

public class PacketRequestPlayer extends AetherPacket
{
    public PacketRequestPlayer(int var1)
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
            boolean var6 = var3.readBoolean();
            String var7 = var3.readUTF();
            String var8 = var3.readUTF();
            String var9 = var3.readUTF();
            String var10 = var3.readUTF();
            Side var11 = FMLCommonHandler.instance().getEffectiveSide();
            Party var12;

            if (var11.isClient())
            {
                var12 = PartyController.instance().getParty(var7);

                if (var12 != null)
                {
                    PartyController.instance().requestPlayer(var12, PartyController.instance().getMember(var8), var10, false);
                    System.out.println("Requested Player \'" + var10 + "\' to the Party: " + var7 + "!");
                }
            }
            else
            {
                var12 = PartyController.instance().getParty(var7);
                PartyMember var13 = PartyController.instance().getMember((EntityPlayer)var2);

                if (var12 != null)
                {
                    if (var12.isLeader(var13))
                    {
                        PartyController.instance().requestPlayer(var12, var13, var10, false);
                        this.sendPacketToAllExcept(AetherPacketHandler.sendRequestPlayer(var6, var7, var8, var9, var10), var2);
                    }
                    else if (var13 != null)
                    {
                        System.out.println("A player (" + var13.username + ") tried to request a member (" + var10 + ") but didn\'t have permission.");
                    }
                }
                else
                {
                    System.out.println("Something went wrong! The player " + var10 + " got to requested into a null party!");
                }
            }
        }
        catch (Exception var14)
        {
            var14.printStackTrace();
        }
    }
}
