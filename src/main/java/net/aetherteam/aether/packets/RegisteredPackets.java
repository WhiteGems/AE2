package net.aetherteam.aether.packets;

import java.util.ArrayList;

public class RegisteredPackets
{
    private static ArrayList packets = new ArrayList();
    public static final AetherPacket riding = new PacketRiding(0);
    public static final AetherPacket accessoryChange = new PacketAccessoryChange(1);
    public static final AetherPacket heartChange = new PacketHeartChange(2);
    public static final AetherPacket cooldown = new PacketCooldown(3);
    public static final AetherPacket coinChange = new PacketCoinChange(4);
    public static final AetherPacket partyChange = new PacketPartyChange(5);
    public static final AetherPacket partyNameChange = new PacketPartyNameChange(6);
    public static final AetherPacket memberChange = new PacketPartyMemberChange(7);
    public static final AetherPacket partyTypeChange = new PacketPartyTypeChange(8);
    public static final AetherPacket notification = new PacketNotificationChange(9);
    public static final AetherPacket donatorChange = new PacketDonatorChange(10);
    public static final AetherPacket donatorChoice = new PacketDonatorChoice(11);
    public static final AetherPacket donatorTypeRemoval = new PacketDonatorTypeRemoval(12);
    public static final AetherPacket playerInput = new PacketPlayerInput(13);
    public static final AetherPacket allParties = new PacketSyncAllParties(14);
    public static final AetherPacket requestPlayer = new PacketRequestPlayer(15);
    public static final AetherPacket playerClientInfo = new PacketPlayerClientInfo(16);
    public static final AetherPacket memberTypeChange = new PacketPartyMemberTypeChange(17);
    public static final AetherPacket dungeonQueueChange = new PacketDungeonQueueChange(18);
    public static final AetherPacket dungeonChange = new PacketDungeonChange(19);
    public static final AetherPacket dungeonMemberQueue = new PacketDungeonMemberQueue(20);
    public static final AetherPacket dungeonFinish = new PacketDungeonFinish(21);
    public static final AetherPacket dungeonTimerStart = new PacketDungeonTimerStart(22);
    public static final AetherPacket dungeonKey = new PacketDungeonKey(23);
    public static final AetherPacket dungeonRespawn = new PacketDungeonRespawn(24);
    public static final AetherPacket dungeonDisbandMember = new PacketDungeonDisbandMember(25);
    public static final AetherPacket dungeonQueueCheck = new PacketDungeonQueueCheck(26);
    public static final AetherPacket parachute = new PacketParachute(27);

    public static void registerPacket(AetherPacket var0)
    {
        packets.add(var0);
    }

    public static ArrayList getPackets()
    {
        return packets;
    }
}
