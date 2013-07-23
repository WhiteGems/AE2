package net.aetherteam.aether.packets;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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
    public PacketNotificationChange(int packetID)
    {
        super(packetID);
    }

    public void onPacketReceived(Packet250CustomPayload packet, Player player)
    {
        DataInputStream dat = new DataInputStream(new ByteArrayInputStream(packet.data));
        BufferedReader buf = new BufferedReader(new InputStreamReader(dat));

        try
        {
            byte packetType = dat.readByte();
            boolean adding = dat.readBoolean();
            String notificationType = dat.readUTF();
            String headerText = dat.readUTF();
            String sendingPlayer = dat.readUTF();
            String receivingPlayer = dat.readUTF();
            NotificationType type = NotificationType.getTypeFromString(notificationType);
            EntityPlayer realSendingPlayer = (EntityPlayer)player;
            Side side = FMLCommonHandler.instance().getEffectiveSide();

            if (side.isClient())
            {
                Notification notification = new Notification(type, "Notification Received!", sendingPlayer, receivingPlayer);

                if ((!NotificationHandler.instance().hasReceivedFromBefore(sendingPlayer, type)) && (adding))
                {
                    NotificationHandler.instance().receiveNotification(notification);
                }
                else if (!adding)
                {
                    NotificationHandler.instance().removeSentNotification(notification, false);
                }
            }
            else
            {
                MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
                ServerConfigurationManager configManager = server.getConfigurationManager();
                ArrayList playerList = new ArrayList();

                for (Iterator i$ = configManager.playerEntityList.iterator(); i$.hasNext();)
                {
                    Object obj = i$.next();

                    if ((obj instanceof EntityPlayer))
                    {
                        playerList.add((EntityPlayer)obj);
                    }
                }

                EntityPlayer realReceivingPlayer = null;

                for (EntityPlayer iteratedPlayer : playerList)
                {
                    if (iteratedPlayer.username.toLowerCase().equalsIgnoreCase(adding ? receivingPlayer : sendingPlayer))
                    {
                        realReceivingPlayer = iteratedPlayer;
                    }
                }

                PartyMember recruiter = PartyController.instance().getMember(realSendingPlayer);
                Party party = PartyController.instance().getParty(recruiter);
                System.out.println("Trying!");

                if ((party != null) && (recruiter != null))
                {
                    System.out.println("Party: " + party);
                    System.out.println("Recruiter: " + recruiter.username);
                }

                if ((!NotificationHandler.instance().hasSentToBefore(receivingPlayer, type, sendingPlayer)) && (realSendingPlayer.username.toLowerCase().equalsIgnoreCase(sendingPlayer)) && (realReceivingPlayer != null))
                {
                    System.out.println("Validated!");

                    if ((party != null) && (!party.isLeader(recruiter)))
                    {
                        System.out.println("Oops! :(");
                        return;
                    }

                    Notification notification = new Notification(type, "Notification Received!", sendingPlayer, receivingPlayer);
                    NotificationHandler.instance().receiveNotification(notification);
                    PacketDispatcher.sendPacketToPlayer(AetherPacketHandler.sendNotificationChange(notification, adding), (Player)realReceivingPlayer);
                }
                else if (!adding)
                {
                    Notification notification = new Notification(type, "Notification Received!", sendingPlayer, receivingPlayer);
                    NotificationHandler.instance().removeSentNotification(notification, false);
                    PacketDispatcher.sendPacketToPlayer(AetherPacketHandler.sendNotificationChange(notification, adding), (Player)realReceivingPlayer);
                }
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}

