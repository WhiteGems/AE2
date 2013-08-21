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
    public PacketNotificationChange(int packetID)
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
                Notification server = new Notification(type, "收到消息!", sendingPlayer, receivingPlayer);

                if (!NotificationHandler.instance().hasReceivedFromBefore(sendingPlayer, type) && adding)
                {
                    NotificationHandler.instance().receiveNotification(server);
                }
                else if (!adding)
                {
                    NotificationHandler.instance().removeSentNotification(server, false);
                }
            }
            else
            {
                MinecraftServer server1 = FMLCommonHandler.instance().getMinecraftServerInstance();
                ServerConfigurationManager configManager = server1.getConfigurationManager();
                ArrayList playerList = new ArrayList();
                Iterator realReceivingPlayer = configManager.playerEntityList.iterator();

                while (realReceivingPlayer.hasNext())
                {
                    Object recruiter = realReceivingPlayer.next();

                    if (recruiter instanceof EntityPlayer)
                    {
                        playerList.add((EntityPlayer)recruiter);
                    }
                }

                EntityPlayer realReceivingPlayer1 = null;
                Iterator recruiter2 = playerList.iterator();

                while (recruiter2.hasNext())
                {
                    EntityPlayer party = (EntityPlayer)recruiter2.next();

                    if (party.username.toLowerCase().equalsIgnoreCase(adding ? receivingPlayer : sendingPlayer))
                    {
                        realReceivingPlayer1 = party;
                    }
                }

                PartyMember recruiter1 = PartyController.instance().getMember(realSendingPlayer);
                Party party1 = PartyController.instance().getParty(recruiter1);
                System.out.println("Trying!");

                if (party1 != null && recruiter1 != null)
                {
                    System.out.println("Party: " + party1);
                    System.out.println("Recruiter: " + recruiter1.username);
                }

                Notification notification;

                if (!NotificationHandler.instance().hasSentToBefore(receivingPlayer, type, sendingPlayer) && realSendingPlayer.username.toLowerCase().equalsIgnoreCase(sendingPlayer) && realReceivingPlayer1 != null)
                {
                    System.out.println("Validated!");

                    if (party1 != null && !party1.isLeader(recruiter1))
                    {
                        System.out.println("Oops! :(");
                        return;
                    }

                    notification = new Notification(type, "收到消息!", sendingPlayer, receivingPlayer);
                    NotificationHandler.instance().receiveNotification(notification);
                    PacketDispatcher.sendPacketToPlayer(AetherPacketHandler.sendNotificationChange(notification, adding), (Player)realReceivingPlayer1);
                }
                else if (!adding)
                {
                    notification = new Notification(type, "收到消息!", sendingPlayer, receivingPlayer);
                    NotificationHandler.instance().removeSentNotification(notification, false);
                    PacketDispatcher.sendPacketToPlayer(AetherPacketHandler.sendNotificationChange(notification, adding), (Player)realReceivingPlayer1);
                }
            }
        }
        catch (Exception var21)
        {
            var21.printStackTrace();
        }
    }
}
