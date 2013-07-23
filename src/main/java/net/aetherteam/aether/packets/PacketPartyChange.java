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

public class PacketPartyChange extends AetherPacket
{
    public PacketPartyChange(int var1)
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
            Side var10 = FMLCommonHandler.instance().getEffectiveSide();
            Party var11;

            if (var10.isClient())
            {
                var11 = PartyController.instance().getParty(var7);

                if (var6 && var11 == null)
                {
                    PartyController.instance().addParty(new Party(var7, new PartyMember(var8, var9)), false);
                    System.out.println(var7 + " created!");
                }
                else
                {
                    PartyController.instance().removeParty(var11, false);
                    System.out.println(var7 + " removed!");
                }
            }
            else
            {
                var11 = PartyController.instance().getParty(var7);
                PartyMember var12 = PartyController.instance().getMember((EntityPlayer)var2);
                EntityPlayer var13 = (EntityPlayer)var2;

                if (var6 && var11 == null && var12 == null)
                {
                    System.out.println("No validation needed, creating and adding party " + var7);
                    PartyController.instance().addParty(new Party(var7, new PartyMember((EntityPlayer)var2)), false);
                    this.sendPacketToAllExcept(AetherPacketHandler.sendPartyChange(var6, var7, var8, var9), var2);
                }
                else if (var12 != null && var11 != null && var11.isLeader(var12) && var13.username.equalsIgnoreCase(var8) && !var6)
                {
                    System.out.println("Leader was validated, removing the party " + var11.getName());
                    PartyController.instance().removeParty(var11, false);
                    this.sendPacketToAllExcept(AetherPacketHandler.sendPartyChange(var6, var7, var8, var9), var2);
                }
                else
                {
                    System.out.println("Something went wrong, the validation of the leader was incorrect. Party not added/removed.");
                }
            }
        }
        catch (Exception var14)
        {
            var14.printStackTrace();
        }
    }
}
