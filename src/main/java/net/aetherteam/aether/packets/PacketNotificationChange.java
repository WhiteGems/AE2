package net.aetherteam.aether.packets;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import net.aetherteam.aether.notifications.Notification;
import net.aetherteam.aether.notifications.NotificationHandler;
import net.aetherteam.aether.notifications.NotificationType;
import net.aetherteam.aether.party.Party;
import net.aetherteam.aether.party.PartyController;
import net.aetherteam.aether.party.members.PartyMember;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;

public class PacketNotificationChange extends AetherPacket
{
    public PacketNotificationChange(int var1)
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
            String var10 = var3.readUTF();
            NotificationType var11 = NotificationType.getTypeFromString(var7);
            EntityPlayer var12 = (EntityPlayer)var2;
            Side var13 = FMLCommonHandler.instance().getEffectiveSide();

            if (var13.isClient())
            {
                Notification var14 = new Notification(var11, "Notification Received!", var9, var10);

                if (!NotificationHandler.instance().hasReceivedFromBefore(var9, var11) && var6)
                {
                    NotificationHandler.instance().receiveNotification(var14);
                }
                else if (!var6)
                {
                    NotificationHandler.instance().removeSentNotification(var14, false);
                }
            }
            else
            {
                MinecraftServer var22 = FMLCommonHandler.instance().getMinecraftServerInstance();
                ServerConfigurationManager var15 = var22.getConfigurationManager();
                ArrayList var16 = new ArrayList();
                Iterator var17 = var15.playerEntityList.iterator();

                while (var17.hasNext())
                {
                    Object var18 = var17.next();

                    if (var18 instanceof EntityPlayer)
                    {
                        var16.add((EntityPlayer)var18);
                    }
                }

                EntityPlayer var23 = null;
                Iterator var26 = var16.iterator();

                while (var26.hasNext())
                {
                    EntityPlayer var19 = (EntityPlayer)var26.next();

                    if (var19.username.toLowerCase().equalsIgnoreCase(var6 ? var10 : var9))
                    {
                        var23 = var19;
                    }
                }

                PartyMember var25 = PartyController.instance().getMember(var12);
                Party var24 = PartyController.instance().getParty(var25);
                System.out.println("Trying!");

                if (var24 != null && var25 != null)
                {
                    System.out.println("Party: " + var24);
                    System.out.println("Recruiter: " + var25.username);
                }

                Notification var20;

                if (!NotificationHandler.instance().hasSentToBefore(var10, var11, var9) && var12.username.toLowerCase().equalsIgnoreCase(var9) && var23 != null)
                {
                    System.out.println("Validated!");

                    if (var24 != null && !var24.isLeader(var25))
                    {
                        System.out.println("Oops! :(");
                        return;
                    }

                    var20 = new Notification(var11, "Notification Received!", var9, var10);
                    NotificationHandler.instance().receiveNotification(var20);
                    PacketDispatcher.sendPacketToPlayer(AetherPacketHandler.sendNotificationChange(var20, var6), (Player)var23);
                }
                else if (!var6)
                {
                    var20 = new Notification(var11, "Notification Received!", var9, var10);
                    NotificationHandler.instance().removeSentNotification(var20, false);
                    PacketDispatcher.sendPacketToPlayer(AetherPacketHandler.sendNotificationChange(var20, var6), (Player)var23);
                }
            }
        }
        catch (Exception var21)
        {
            var21.printStackTrace();
        }
    }
}
