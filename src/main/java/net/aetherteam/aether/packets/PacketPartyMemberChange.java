package net.aetherteam.aether.packets;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.InputStreamReader;

import net.aetherteam.aether.dungeons.Dungeon;
import net.aetherteam.aether.dungeons.DungeonHandler;
import net.aetherteam.aether.party.Party;
import net.aetherteam.aether.party.PartyController;
import net.aetherteam.aether.party.PartyType;
import net.aetherteam.aether.party.members.PartyMember;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.packet.Packet250CustomPayload;

public class PacketPartyMemberChange extends AetherPacket
{
    public PacketPartyMemberChange(int var1)
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
            PartyMember var12;

            if (var10.isClient())
            {
                var11 = PartyController.instance().getParty(var7);

                if (var11 != null)
                {
                    if (var6)
                    {
                        var12 = new PartyMember(var8, var9);
                        PartyController.instance().joinParty(var11, var12, false);
                        System.out.println("Added Player \'" + var8 + "\' to the Party: " + var7 + "!");
                    }
                    else
                    {
                        var12 = PartyController.instance().getMember(var8);
                        PartyController.instance().leaveParty(var11, var12, false);
                        System.out.println("Removed Player \'" + var8 + "\' from the Party: " + var7 + "!");
                    }
                }
            }
            else
            {
                var11 = PartyController.instance().getParty(var7);
                var12 = PartyController.instance().getMember((EntityPlayer)var2);
                PartyMember var13 = PartyController.instance().getMember(var8);

                if (var11 != null)
                {
                    if (!var11.isLeader(var12) && var11.getType() != PartyType.OPEN && (var12 == null || !var12.username.toLowerCase().equalsIgnoreCase(var13.username) || var6) && !var11.isRequestedPlayer(var8))
                    {
                        System.out.println("A player (" + var12.username + ") tried to add/kick a member (" + var13.username + ") but didn\'t have permission or the party was not \'open\'.");
                    }
                    else
                    {
                        if (var6 && var13 == null)
                        {
                            PartyController.instance().joinParty(var11, new PartyMember(var8, ""), false);
                        }
                        else
                        {
                            Dungeon var14 = DungeonHandler.instance().getDungeon(var11);
                            PartyController.instance().leaveParty(var11, var13, false);

                            if (var14 != null && !var14.hasStarted())
                            {
                                DungeonHandler.instance().checkForQueue(var14);
                                PacketDispatcher.sendPacketToAllPlayers(AetherPacketHandler.sendDungeonQueueCheck(var14));
                            }
                        }

                        this.sendPacketToAllExcept(AetherPacketHandler.sendPartyMemberChange(var6, var7, var8, var9), var2);
                    }
                }
                else if (var13 != null)
                {
                    System.out.println("Something went wrong! The player " + var13.username + " tried to join/leave a null party!");
                }
            }
        }
        catch (Exception var15)
        {
            var15.printStackTrace();
        }
    }
}
