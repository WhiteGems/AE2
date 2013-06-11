package net.aetherteam.aether;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.IPlayerTracker;
import cpw.mods.fml.common.network.PacketDispatcher;

import java.util.*;
import java.util.Map.Entry;

import net.aetherteam.aether.containers.InventoryAether;
import net.aetherteam.aether.data.PlayerClientInfo;
import net.aetherteam.aether.donator.Donator;
import net.aetherteam.aether.donator.DonatorChoice;
import net.aetherteam.aether.donator.EnumChoiceType;
import net.aetherteam.aether.donator.SyncDonatorList;
import net.aetherteam.aether.dungeons.Dungeon;
import net.aetherteam.aether.dungeons.DungeonHandler;
import net.aetherteam.aether.packets.AetherPacketHandler;
import net.aetherteam.aether.party.Party;
import net.aetherteam.aether.party.PartyController;
import net.aetherteam.aether.party.members.PartyMember;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetServerHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.util.FoodStats;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class AetherPlayerTracker implements IPlayerTracker
{
    private EntityPlayerMP entityPlayer;
    private NBTTagList taglist;
    private Set username;
    private String cooldownName;
    private int maxHealth;
    private int cooldown;
    private int cooldownMax;
    private int coinAmount;

    public void onPlayerLogin(EntityPlayer player)
    {
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        ServerConfigurationManager configManager = server.getConfigurationManager();

        if (configManager.playerEntityList.size() == 1)
        {
            Aether.proxy.getClientInventories().clear();
            Aether.proxy.getClientExtraHearts().clear();
            Aether.proxy.getClientCooldown().clear();
            Aether.proxy.getClientMaxCooldown().clear();
            Aether.proxy.getPlayerClientInfo().clear();
        }

        PacketDispatcher.sendPacketToAllPlayers(AetherPacketHandler.sendAccessoryChange(Aether.getServerPlayer((EntityPlayerMP) player).inv.writeToNBT(new NBTTagList()), false, true, Collections.singleton(player.username), (byte) 1));
        PacketDispatcher.sendPacketToAllPlayers(AetherPacketHandler.sendHeartChange(false, true, Aether.getServerPlayer(player).maxHealth, Collections.singleton(player.username)));

        for (int playerAmount = 0; playerAmount < configManager.playerEntityList.size(); playerAmount++)
        {
            this.entityPlayer = ((EntityPlayerMP) configManager.playerEntityList.get(playerAmount));

            this.taglist = Aether.getServerPlayer(this.entityPlayer).inv.writeToNBT(new NBTTagList());
            this.maxHealth = Aether.getServerPlayer(this.entityPlayer).maxHealth;
            this.username = Collections.singleton(this.entityPlayer.username);
            this.cooldown = Aether.getServerPlayer(this.entityPlayer).generalcooldown;
            this.cooldownMax = Aether.getServerPlayer(this.entityPlayer).generalcooldownmax;
            this.cooldownName = Aether.getServerPlayer(this.entityPlayer).cooldownName;
            this.coinAmount = Aether.getServerPlayer(this.entityPlayer).getCoins();

            ((EntityPlayerMP) player).playerNetServerHandler.sendPacketToPlayer(AetherPacketHandler.sendAccessoryChange(this.taglist, false, true, this.username, (byte) 1));
            ((EntityPlayerMP) player).playerNetServerHandler.sendPacketToPlayer(AetherPacketHandler.sendHeartChange(false, true, this.maxHealth, this.username));

            String RSA = Aether.getInstance().getKey(this.entityPlayer.username);

            if (RSA != null)
            {
                ((EntityPlayerMP) player).playerNetServerHandler.sendPacketToPlayer(AetherPacketHandler.sendDonatorChange(this.entityPlayer.username, new Donator(this.entityPlayer.username, RSA)));
            }

            if (Aether.syncDonatorList.getDonator(this.entityPlayer.username) != null)
            {
                HashMap choiceList = Aether.syncDonatorList.getDonator(this.entityPlayer.username).choices;

                if (choiceList != null)
                {
                    if (choiceList.get(EnumChoiceType.CAPE) != null)
                    {
                        ((EntityPlayerMP) player).playerNetServerHandler.sendPacketToPlayer(AetherPacketHandler.sendDonatorChoice(this.entityPlayer.username, (DonatorChoice) choiceList.get(EnumChoiceType.CAPE), true, (byte) 1));
                    }
                    if (choiceList.get(EnumChoiceType.MOA) != null)
                    {
                        ((EntityPlayerMP) player).playerNetServerHandler.sendPacketToPlayer(AetherPacketHandler.sendDonatorChoice(this.entityPlayer.username, (DonatorChoice) choiceList.get(EnumChoiceType.MOA), true, (byte) 1));
                    }
                }
            }
        }
        ((EntityPlayerMP) player).playerNetServerHandler.sendPacketToPlayer(AetherPacketHandler.sendCooldown(false, true, this.cooldown, this.cooldownMax, this.cooldownName, Collections.singleton(player.username)));
        ((EntityPlayerMP) player).playerNetServerHandler.sendPacketToPlayer(AetherPacketHandler.sendCoinChange(false, true, this.coinAmount, Collections.singleton(player.username)));

        ((EntityPlayerMP) player).playerNetServerHandler.sendPacketToPlayer(AetherPacketHandler.sendAllParties(PartyController.instance().getParties()));

        String RSA = Aether.getInstance().getKey(player.username);

        if (RSA != null)
        {
            Aether.getInstance();
            Aether.syncDonatorList.sendDonatorToAll(player.username, new Donator(player.username, RSA));
        }

        if (Aether.syncDonatorList.getDonator(this.entityPlayer.username) != null)
        {
            HashMap choiceList = Aether.syncDonatorList.getDonator(this.entityPlayer.username).choices;

            if (choiceList != null)
            {
                if (choiceList.get(EnumChoiceType.CAPE) != null)
                {
                    Aether.getInstance();
                    Aether.syncDonatorList.sendChoiceToAll(this.entityPlayer.username, (DonatorChoice) choiceList.get(EnumChoiceType.CAPE), true);
                }
                if (choiceList.get(EnumChoiceType.MOA) != null)
                {
                    Aether.getInstance();
                    Aether.syncDonatorList.sendChoiceToAll(this.entityPlayer.username, (DonatorChoice) choiceList.get(EnumChoiceType.MOA), true);
                }
            }
        }
        Iterator it = Aether.proxy.getPlayerClientInfo().entrySet().iterator();

        while (it.hasNext())
        {
            Map.Entry pairs = (Map.Entry) it.next();

            String username = (String) pairs.getKey();
            PlayerClientInfo playerClientInfo = (PlayerClientInfo) pairs.getValue();

            ((EntityPlayerMP) player).playerNetServerHandler.sendPacketToPlayer(AetherPacketHandler.sendPlayerClientInfo(false, true, username, playerClientInfo));
        }

        updatePlayerClientInfo((EntityPlayerMP) player, true);

        for (Dungeon dungeon : DungeonHandler.instance().getInstances())
        {
            ((EntityPlayerMP) player).playerNetServerHandler.sendPacketToPlayer(AetherPacketHandler.sendDungeonChange(true, dungeon));
        }
    }

    public void updatePlayerClientInfo(EntityPlayerMP player, boolean adding)
    {
        if (!player.worldObj.isRemote)
        {
            MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
            ServerConfigurationManager configManager = server.getConfigurationManager();

            PlayerClientInfo playerClientInfo = new PlayerClientInfo(player.getHealth(), player.getMaxHealth(), player.getFoodStats().getFoodLevel(), player.getTotalArmorValue(), Aether.getServerPlayer(player).getCoins());

            Aether.proxy.getPlayerClientInfo().put(player.username, playerClientInfo);

            PartyMember member = PartyController.instance().getMember(player.username);
            Party party = PartyController.instance().getParty(member);

            for (int playerAmount = 0; playerAmount < configManager.playerEntityList.size(); playerAmount++)
            {
                EntityPlayerMP entityPlayer = (EntityPlayerMP) configManager.playerEntityList.get(playerAmount);

                entityPlayer.playerNetServerHandler.sendPacketToPlayer(AetherPacketHandler.sendPlayerClientInfo(false, adding, player.username, playerClientInfo));
            }

            player.playerNetServerHandler.sendPacketToPlayer(AetherPacketHandler.sendPlayerClientInfo(false, adding, player.username, playerClientInfo));
        }
    }

    public void onPlayerLogout(EntityPlayer player)
    {
        PartyMember member = PartyController.instance().getMember(player);

        int x = MathHelper.floor_double(player.posX);
        int y = MathHelper.floor_double(player.posY);
        int z = MathHelper.floor_double(player.posZ);

        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        ServerConfigurationManager configManager = server.getConfigurationManager();

        Party party = PartyController.instance().getParty(member);

        if ((member != null) && (configManager.playerEntityList.size() > 1))
        {
            if (party != null)
            {
                Dungeon dungeon = DungeonHandler.instance().getDungeon(party);

                PartyController.instance().leaveParty(party, member, false);

                PacketDispatcher.sendPacketToAllPlayers(AetherPacketHandler.sendPartyMemberChange(false, party.getName(), member.username, ""));

                if ((dungeon != null) && (!dungeon.hasStarted()))
                {
                    DungeonHandler.instance().checkForQueue(dungeon);

                    PacketDispatcher.sendPacketToAllPlayers(AetherPacketHandler.sendDungeonQueueCheck(dungeon));
                }
            }
        }

        PacketDispatcher.sendPacketToAllPlayers(AetherPacketHandler.sendAccessoryChange(Aether.getServerPlayer((EntityPlayerMP) player).inv.writeToNBT(new NBTTagList()), false, false, Collections.singleton(player.username), (byte) 1));
        PacketDispatcher.sendPacketToAllPlayers(AetherPacketHandler.sendHeartChange(false, false, Aether.getServerPlayer(player).maxHealth, Collections.singleton(player.username)));
        PacketDispatcher.sendPacketToAllPlayers(AetherPacketHandler.sendCooldown(false, false, Aether.getServerPlayer(player).generalcooldown, Aether.getServerPlayer(player).generalcooldownmax, Aether.getServerPlayer(player).cooldownName, Collections.singleton(player.username)));
        PacketDispatcher.sendPacketToAllPlayers(AetherPacketHandler.sendCoinChange(false, false, Aether.getServerPlayer(player).getCoins(), Collections.singleton(player.username)));

        updatePlayerClientInfo((EntityPlayerMP) player, false);
    }

    public void onPlayerChangedDimension(EntityPlayer player)
    {
    }

    public void onPlayerRespawn(EntityPlayer player)
    {
    }
}

/* Location:           D:\Dev\Mc\forge_orl\mcp\jars\bin\aether.jar
 * Qualified Name:     net.aetherteam.aether.AetherPlayerTracker
 * JD-Core Version:    0.6.2
 */