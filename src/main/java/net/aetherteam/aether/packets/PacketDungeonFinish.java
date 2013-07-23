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
import net.aetherteam.aether.tile_entities.TileEntityEntranceController;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.packet.Packet250CustomPayload;

public class PacketDungeonFinish extends AetherPacket
{
    public PacketDungeonFinish(int var1)
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
            int var8 = var3.readInt();
            int var9 = var3.readInt();
            int var10 = var3.readInt();
            Side var11 = FMLCommonHandler.instance().getEffectiveSide();

            if (var11.isClient())
            {
                Party var12 = PartyController.instance().getParty(var7);
                Dungeon var13 = DungeonHandler.instance().getDungeon(var6);
                PartyMember var14 = PartyController.instance().getMember((EntityPlayer)var2);
                EntityPlayer var15 = (EntityPlayer)var2;
                TileEntityEntranceController var16 = (TileEntityEntranceController)var15.worldObj.getBlockTileEntity(var8, var9, var10);

                if (var12 != null && var13 != null && var16 != null)
                {
                    DungeonHandler.instance().finishDungeon(var13, var12, var16, false);
                }
            }
        }
        catch (Exception var17)
        {
            var17.printStackTrace();
        }
    }
}
