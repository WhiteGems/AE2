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
import net.aetherteam.aether.dungeons.keys.DungeonKey;
import net.aetherteam.aether.dungeons.keys.EnumKeyType;
import net.aetherteam.aether.party.Party;
import net.aetherteam.aether.party.PartyController;
import net.aetherteam.aether.tile_entities.TileEntityBronzeDoorController;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.packet.Packet250CustomPayload;

public class PacketDungeonKey extends AetherPacket
{
    public PacketDungeonKey(int var1)
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
            int var7 = var3.readInt();
            String var8 = var3.readUTF();
            DungeonKey var9 = new DungeonKey(EnumKeyType.getTypeFromString(var3.readUTF()));
            Side var10 = FMLCommonHandler.instance().getEffectiveSide();
            Party var11;
            Dungeon var12;

            if (var10.isClient())
            {
                var11 = PartyController.instance().getParty(var8);
                var12 = DungeonHandler.instance().getDungeon(var7);

                if (var11 != null && var12 != null && var9 != null)
                {
                    if (var6)
                    {
                        DungeonHandler.instance().addKey(var12, var11, var9);
                    }
                    else
                    {
                        DungeonHandler.instance().removeKey(var12, var11, var9);
                    }
                }
            }
            else if (!var6)
            {
                var11 = PartyController.instance().getParty(var8);
                var12 = DungeonHandler.instance().getDungeon(var7);
                EntityPlayer var13 = (EntityPlayer)var2;
                int var14 = var3.readInt();
                int var15 = var3.readInt();
                int var16 = var3.readInt();
                TileEntityBronzeDoorController var17 = (TileEntityBronzeDoorController)var13.worldObj.getBlockTileEntity(var14, var15, var16);

                if (var11 != null && var12 != null && var9 != null && var12.getQueuedParty().hasMember(PartyController.instance().getMember(var13)))
                {
                    DungeonHandler.instance().removeKey(var12, var11, var9);
                    PacketDispatcher.sendPacketToAllPlayers(AetherPacketHandler.removeDungeonKey(var12, var11, var9.getType(), var17));
                }
            }
        }
        catch (Exception var18)
        {
            var18.printStackTrace();
        }
    }
}
