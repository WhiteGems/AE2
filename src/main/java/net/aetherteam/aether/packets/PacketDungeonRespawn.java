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
    public PacketDungeonRespawn(int packetID)
    {
        super(packetID);
    }

    public void onPacketReceived(Packet250CustomPayload packet, Player player)
    {
        DataInputStream dat = new DataInputStream(new ByteArrayInputStream(packet.data));
        new BufferedReader(new InputStreamReader(dat));

        try
        {
            byte ex = dat.readByte();
            int dungeonID = dat.readInt();
            String partyName = dat.readUTF();
            Side side = FMLCommonHandler.instance().getEffectiveSide();

            if (side.isClient())
            {
                Party party = PartyController.instance().getParty(partyName);
                Dungeon dungeon = DungeonHandler.instance().getDungeon(dungeonID);

                if (party != null && dungeon != null)
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
