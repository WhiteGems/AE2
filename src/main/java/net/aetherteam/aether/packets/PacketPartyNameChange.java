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
    public PacketPartyNameChange(int var1)
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
            String var7 = var3.readUTF();
            Side var8 = FMLCommonHandler.instance().getEffectiveSide();
            Party var9;

            if (var8.isClient())
            {
                var9 = PartyController.instance().getParty(var6);

                if (var9 != null && !var7.isEmpty() && PartyController.instance().getParty(var7) == null)
                {
                    PartyController.instance().changePartyName(var9, var7, false);
                    System.out.println("\'" + var6 + "\'s name changed to \'" + var7 + "\'");
                }
                else
                {
                    System.out.println("Either the party was null or the name was empty. Party name change unsuccessful!");
                }
            }
            else
            {
                var9 = PartyController.instance().getParty(var6);
                PartyMember var10 = PartyController.instance().getMember((EntityPlayer)var2);

                if (var9 != null && !var7.isEmpty() && PartyController.instance().getParty(var7) == null)
                {
                    if (var9.isLeader(var10))
                    {
                        PartyController.instance().changePartyName(var9, var7, false);
                        this.sendPacketToAllExcept(AetherPacketHandler.sendPartyNameChange(var6, var7), var2);
                    }
                    else
                    {
                        System.out.println("A player (" + var10.username + ") tried to change the name of a party (" + var9.getName() + ") but didn\'t have permission.");
                    }
                }
                else
                {
                    System.out.println("Something went wrong! The player " + var10.username + " tried to change the name of a null party!");
                }
            }
        }
        catch (Exception var11)
        {
            var11.printStackTrace();
        }
    }
}
