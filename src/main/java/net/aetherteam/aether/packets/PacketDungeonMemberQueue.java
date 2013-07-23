package net.aetherteam.aether.packets;

import cpw.mods.fml.common.FMLCommonHandler;
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
import net.aetherteam.aether.party.members.PartyMember;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.packet.Packet250CustomPayload;

public class PacketDungeonMemberQueue extends AetherPacket
{
    public PacketDungeonMemberQueue(int var1)
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
            int var6 = var3.readInt();
            String var7 = var3.readUTF();
            Side var8 = FMLCommonHandler.instance().getEffectiveSide();
            PartyMember var9;
            Party var10;

            if (var8.isClient())
            {
                var9 = PartyController.instance().getMember(var7);
                var10 = PartyController.instance().getParty(var9);
                Dungeon var11 = DungeonHandler.instance().getDungeon(var6);

                if (var10 != null && var11 != null)
                {
                    DungeonHandler.instance().queueMember(var11, var9, false);
                }
            }
            else
            {
                var9 = PartyController.instance().getMember(var7);
                var10 = PartyController.instance().getParty(var9);
                EntityPlayerMP var14 = (EntityPlayerMP)var2;
                Dungeon var12 = DungeonHandler.instance().getDungeon(var6);

                if (var10 != null && var12 != null)
                {
                    if (var14.username.equalsIgnoreCase(var7))
                    {
                        System.out.println("No validation needed, adding party member \'" + var7 + "\' to the Dungeon.");
                        DungeonHandler.instance().queueMember(var12, var9, false);
                        this.sendPacketToAllExcept(AetherPacketHandler.sendDungeonMemberQueue(var12, var9), var2);
                    }
                    else
                    {
                        System.out.println("Something went wrong, the validation of the leader was incorrect. Party not added/removed from Dungeon Instance.");
                    }
                }
            }
        }
        catch (Exception var13)
        {
            var13.printStackTrace();
        }
    }
}
