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
import net.aetherteam.aether.notifications.client.ClientNotificationHandler;
import net.aetherteam.aether.party.Party;
import net.aetherteam.aether.party.PartyController;
import net.minecraft.network.packet.Packet250CustomPayload;

public class PacketDungeonRespawn extends AetherPacket
{
    public PacketDungeonRespawn(int var1)
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

            if (var8.isClient())
            {
                Party var9 = PartyController.instance().getParty(var7);
                Dungeon var10 = DungeonHandler.instance().getDungeon(var6);

                if (var9 != null && var10 != null)
                {
                    ClientNotificationHandler.openDialogueBox("你已重生!");
                }
            }
        }
        catch (Exception var11)
        {
            var11.printStackTrace();
        }
    }
}
