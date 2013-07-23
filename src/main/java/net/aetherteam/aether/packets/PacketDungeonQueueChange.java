package net.aetherteam.aether.packets;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import net.aetherteam.aether.dungeons.Dungeon;
import net.aetherteam.aether.dungeons.DungeonHandler;
import net.aetherteam.aether.party.Party;
import net.aetherteam.aether.party.PartyController;
import net.aetherteam.aether.party.members.PartyMember;
import net.aetherteam.aether.tile_entities.TileEntityEntranceController;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.util.MathHelper;

public class PacketDungeonQueueChange extends AetherPacket
{
    public PacketDungeonQueueChange(int var1)
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
            int var9 = var3.readInt();
            int var10 = var3.readInt();
            int var11 = var3.readInt();
            Side var12 = FMLCommonHandler.instance().getEffectiveSide();
            Party var13;

            if (var12.isClient())
            {
                var13 = PartyController.instance().getParty(var8);
                Dungeon var14 = DungeonHandler.instance().getDungeon(var7);
                PartyMember var15 = PartyController.instance().getMember((EntityPlayer)var2);
                EntityPlayer var16 = (EntityPlayer)var2;

                if (var13 != null && var14 != null)
                {
                    if (var6)
                    {
                        DungeonHandler.instance().queueParty(var14, var13, var9, var10, var11, false);
                    }
                    else
                    {
                        DungeonHandler.instance().disbandQueue(var14, var13, var9, var10, var11, var15, false);
                    }
                }
            }
            else
            {
                var13 = PartyController.instance().getParty(var8);
                PartyMember var27 = PartyController.instance().getMember((EntityPlayer)var2);
                EntityPlayerMP var26 = (EntityPlayerMP)var2;
                Dungeon var28 = DungeonHandler.instance().getDungeon(var7);
                TileEntityEntranceController var17 = (TileEntityEntranceController)var26.worldObj.getBlockTileEntity(MathHelper.floor_double((double)var9), MathHelper.floor_double((double)var10), MathHelper.floor_double((double)var11));
                MinecraftServer var18 = FMLCommonHandler.instance().getMinecraftServerInstance();
                ServerConfigurationManager var19 = var18.getConfigurationManager();
                Iterator var20 = var19.playerEntityList.iterator();

                while (var20.hasNext())
                {
                    Object var21 = var20.next();

                    if (var21 instanceof EntityPlayer)
                    {
                        EntityPlayer var22 = (EntityPlayer)var21;

                        if (!var22.equals(var26) && PartyController.instance().getParty((EntityPlayer)var26).hasMember(PartyController.instance().getMember(var22)) && var22.worldObj.provider.dimensionId != 3)
                        {
                            PacketDispatcher.sendPacketToPlayer(AetherPacketHandler.sendDungeonQueueChange(false, var28, var9, var10, var11, var13), var2);
                            var26.addChatMessage("\u00a7o All of your party members aren\'t in the Aether!");
                            return;
                        }
                    }
                }

                if (var13 != null && var28 != null && var27 != null)
                {
                    int var25;
                    int var24;
                    int var29;

                    if (var13.isLeader(var27) && var6 && var17 != null && !var28.hasAnyConqueredDungeon(var13.getMembers()))
                    {
                        var29 = MathHelper.floor_double((double)var17.xCoord);
                        var25 = MathHelper.floor_double((double)var17.yCoord);
                        var24 = MathHelper.floor_double((double)var17.zCoord);
                        System.out.println("Leader was validated, adding the party " + var13.getName() + " to the Dungeon\'s queue.");
                        DungeonHandler.instance().queueParty(var28, var13, var29, var25, var24, false);
                        this.sendPacketToAllExcept(AetherPacketHandler.sendDungeonQueueChange(var6, var28, var29, var25, var24, var13), var2);
                    }
                    else if (!var6)
                    {
                        var29 = MathHelper.floor_double((double)var17.xCoord);
                        var25 = MathHelper.floor_double((double)var17.yCoord);
                        var24 = MathHelper.floor_double((double)var17.zCoord);
                        System.out.println("No validation needed, removing party " + var8 + " from the Dungeon queue.");
                        DungeonHandler.instance().disbandQueue(var28, var13, var29, var25, var24, var27, false);
                        this.sendPacketToAllExcept(AetherPacketHandler.sendDungeonQueueChange(var6, var28, var29, var25, var24, var13), var2);
                    }
                    else
                    {
                        System.out.println("Something went wrong, the validation of the leader was incorrect. Party not added/removed from Dungeon Instance.");
                    }
                }
            }
        }
        catch (Exception var23)
        {
            var23.printStackTrace();
        }
    }
}
