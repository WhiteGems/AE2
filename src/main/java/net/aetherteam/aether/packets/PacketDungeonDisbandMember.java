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
import net.aetherteam.aether.party.PartyController;
import net.aetherteam.aether.party.members.PartyMember;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.packet.Packet250CustomPayload;

public class PacketDungeonDisbandMember extends AetherPacket
{
    public PacketDungeonDisbandMember(int var1)
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
                Dungeon var9 = DungeonHandler.instance().getDungeon(var6);
                PartyMember var10 = PartyController.instance().getMember(var7);

                if (var9 != null && var10 != null)
                {
                    DungeonHandler.instance().disbandMember(var9, var10, false);
                }
            }
            else
            {
                PartyMember var14 = PartyController.instance().getMember(var7);
                EntityPlayer var13 = (EntityPlayer)var2;
                Dungeon var11 = DungeonHandler.instance().getDungeon(var6);

                if (var11 != null && var11.isActive() && var14 != null && var14.username.equalsIgnoreCase(var13.username))
                {
                    DungeonHandler.instance().disbandMember(var11, var14, false);
                    this.sendPacketToAllExcept(AetherPacketHandler.sendDungeonDisbandMember(var11, var14), var2);
                }
            }
        }
        catch (Exception var12)
        {
            var12.printStackTrace();
        }
    }
}
