package net.aetherteam.aether;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.IPlayerTracker;
import cpw.mods.fml.common.network.PacketDispatcher;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;
import net.aetherteam.aether.data.PlayerClientInfo;
import net.aetherteam.aether.donator.Donator;
import net.aetherteam.aether.donator.DonatorChoice;
import net.aetherteam.aether.donator.EnumChoiceType;
import net.aetherteam.aether.dungeons.Dungeon;
import net.aetherteam.aether.dungeons.DungeonHandler;
import net.aetherteam.aether.packets.AetherPacketHandler;
import net.aetherteam.aether.party.Party;
import net.aetherteam.aether.party.PartyController;
import net.aetherteam.aether.party.members.PartyMember;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.util.MathHelper;

public class AetherPlayerTracker implements IPlayerTracker
{
    private EntityPlayerMP entityPlayer;
    private NBTTagList taglist;
    private Set<String> username;
    private String cooldownName;
    private int maxHealth;
    private int cooldown;
    private int cooldownMax;
    private int coinAmount;
    private boolean isParachuting;
    private int parachuteType;

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

        PacketDispatcher.sendPacketToAllPlayers(AetherPacketHandler.sendAccessoryChange(Aether.getServerPlayer((EntityPlayerMP)player).inv.writeToNBT(new NBTTagList()), false, true, Collections.singleton(player.username), (byte)1));
        PacketDispatcher.sendPacketToAllPlayers(AetherPacketHandler.sendHeartChange(false, true, (int)player.func_110138_aP(), Collections.singleton(player.username)));
        Aether var10000;

        for (int RSA = 0; RSA < configManager.playerEntityList.size(); ++RSA)
        {
            this.entityPlayer = (EntityPlayerMP)configManager.playerEntityList.get(RSA);
            this.taglist = Aether.getServerPlayer(this.entityPlayer).inv.writeToNBT(new NBTTagList());
            this.maxHealth = (int)this.entityPlayer.func_110138_aP();
            this.username = Collections.singleton(this.entityPlayer.username);
            this.cooldown = Aether.getServerPlayer(this.entityPlayer).generalcooldown;
            this.cooldownMax = Aether.getServerPlayer(this.entityPlayer).generalcooldownmax;
            this.cooldownName = Aether.getServerPlayer(this.entityPlayer).cooldownName;
            this.coinAmount = Aether.getServerPlayer(this.entityPlayer).getCoins();
            this.isParachuting = Aether.getServerPlayer(this.entityPlayer).getParachuting();
            this.parachuteType = Aether.getServerPlayer(this.entityPlayer).getParachuteType();
            ((EntityPlayerMP)player).playerNetServerHandler.sendPacketToPlayer(AetherPacketHandler.sendAccessoryChange(this.taglist, false, true, this.username, (byte)1));
            ((EntityPlayerMP)player).playerNetServerHandler.sendPacketToPlayer(AetherPacketHandler.sendHeartChange(false, true, this.maxHealth, this.username));
            String it = Aether.getInstance().getKey(this.entityPlayer.username);

            if (it != null)
            {
                ((EntityPlayerMP)player).playerNetServerHandler.sendPacketToPlayer(AetherPacketHandler.sendDonatorChange(this.entityPlayer.username, new Donator(this.entityPlayer.username, it)));
            }

            var10000 = Aether.instance;

            if (Aether.syncDonatorList.getDonator(this.entityPlayer.username) != null)
            {
                var10000 = Aether.instance;
                HashMap i$ = Aether.syncDonatorList.getDonator(this.entityPlayer.username).choices;

                if (i$ != null)
                {
                    if (i$.get(EnumChoiceType.CAPE) != null)
                    {
                        ((EntityPlayerMP)player).playerNetServerHandler.sendPacketToPlayer(AetherPacketHandler.sendDonatorChoice(this.entityPlayer.username, (DonatorChoice)i$.get(EnumChoiceType.CAPE), true, (byte)1));
                    }

                    if (i$.get(EnumChoiceType.MOA) != null)
                    {
                        ((EntityPlayerMP)player).playerNetServerHandler.sendPacketToPlayer(AetherPacketHandler.sendDonatorChoice(this.entityPlayer.username, (DonatorChoice)i$.get(EnumChoiceType.MOA), true, (byte)1));
                    }
                }
            }
        }

        PacketDispatcher.sendPacketToAllPlayers(AetherPacketHandler.sendParachuteCheck(false, this.isParachuting, this.isParachuting, this.parachuteType, Collections.singleton(player.username)));
        ((EntityPlayerMP)player).playerNetServerHandler.sendPacketToPlayer(AetherPacketHandler.sendCooldown(false, true, this.cooldown, this.cooldownMax, this.cooldownName, Collections.singleton(player.username)));
        ((EntityPlayerMP)player).playerNetServerHandler.sendPacketToPlayer(AetherPacketHandler.sendCoinChange(false, true, this.coinAmount, Collections.singleton(player.username)));
        ((EntityPlayerMP)player).playerNetServerHandler.sendPacketToPlayer(AetherPacketHandler.sendAllParties(PartyController.instance().getParties()));
        String var9 = Aether.getInstance().getKey(player.username);

        if (var9 != null)
        {
            Aether.getInstance();
            Aether.syncDonatorList.sendDonatorToAll(player.username, new Donator(player.username, var9));
        }

        var10000 = Aether.instance;

        if (Aether.syncDonatorList.getDonator(this.entityPlayer.username) != null)
        {
            var10000 = Aether.instance;
            HashMap var10 = Aether.syncDonatorList.getDonator(this.entityPlayer.username).choices;

            if (var10 != null)
            {
                if (var10.get(EnumChoiceType.CAPE) != null)
                {
                    Aether.getInstance();
                    Aether.syncDonatorList.sendChoiceToAll(this.entityPlayer.username, (DonatorChoice)var10.get(EnumChoiceType.CAPE), true);
                }

                if (var10.get(EnumChoiceType.MOA) != null)
                {
                    Aether.getInstance();
                    Aether.syncDonatorList.sendChoiceToAll(this.entityPlayer.username, (DonatorChoice)var10.get(EnumChoiceType.MOA), true);
                }
            }
        }

        Iterator var11 = Aether.proxy.getPlayerClientInfo().entrySet().iterator();

        while (var11.hasNext())
        {
            Entry var13 = (Entry)var11.next();
            String dungeon = (String)var13.getKey();
            PlayerClientInfo playerClientInfo = (PlayerClientInfo)var13.getValue();
            ((EntityPlayerMP)player).playerNetServerHandler.sendPacketToPlayer(AetherPacketHandler.sendPlayerClientInfo(false, true, dungeon, playerClientInfo));
        }

        this.updatePlayerClientInfo((EntityPlayerMP)player, true);
        Iterator var12 = DungeonHandler.instance().getInstances().iterator();

        while (var12.hasNext())
        {
            Dungeon var14 = (Dungeon)var12.next();
            ((EntityPlayerMP)player).playerNetServerHandler.sendPacketToPlayer(AetherPacketHandler.sendDungeonChange(true, var14));
        }
    }

    public void updatePlayerClientInfo(EntityPlayerMP player, boolean adding)
    {
        if (!player.worldObj.isRemote)
        {
            MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
            ServerConfigurationManager configManager = server.getConfigurationManager();
            PlayerClientInfo playerClientInfo = new PlayerClientInfo((int)player.func_110143_aJ(), (int)player.func_110138_aP(), player.getFoodStats().getFoodLevel(), player.getTotalArmorValue(), Aether.getServerPlayer(player).getCoins());
            Aether.proxy.getPlayerClientInfo().put(player.username, playerClientInfo);
            PartyMember member = PartyController.instance().getMember(player.username);
            Party party = PartyController.instance().getParty(member);

            for (int playerAmount = 0; playerAmount < configManager.playerEntityList.size(); ++playerAmount)
            {
                EntityPlayerMP entityPlayer = (EntityPlayerMP)configManager.playerEntityList.get(playerAmount);
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

        if (member != null && configManager.playerEntityList.size() > 1 && party != null)
        {
            Dungeon dungeon = DungeonHandler.instance().getDungeon(party);
            PartyController.instance().leaveParty(party, member, false);
            PacketDispatcher.sendPacketToAllPlayers(AetherPacketHandler.sendPartyMemberChange(false, party.getName(), member.username));

            if (dungeon != null && !dungeon.hasStarted())
            {
                DungeonHandler.instance().checkForQueue(dungeon);
                PacketDispatcher.sendPacketToAllPlayers(AetherPacketHandler.sendDungeonQueueCheck(dungeon));
            }
        }

        PacketDispatcher.sendPacketToAllPlayers(AetherPacketHandler.sendAccessoryChange(Aether.getServerPlayer((EntityPlayerMP)player).inv.writeToNBT(new NBTTagList()), false, false, Collections.singleton(player.username), (byte)1));
        PacketDispatcher.sendPacketToAllPlayers(AetherPacketHandler.sendHeartChange(false, false, (int)this.entityPlayer.func_110138_aP(), Collections.singleton(player.username)));
        PacketDispatcher.sendPacketToAllPlayers(AetherPacketHandler.sendCooldown(false, false, Aether.getServerPlayer(player).generalcooldown, Aether.getServerPlayer(player).generalcooldownmax, Aether.getServerPlayer(player).cooldownName, Collections.singleton(player.username)));
        PacketDispatcher.sendPacketToAllPlayers(AetherPacketHandler.sendCoinChange(false, false, Aether.getServerPlayer(player).getCoins(), Collections.singleton(player.username)));
        PacketDispatcher.sendPacketToAllPlayers(AetherPacketHandler.sendParachuteCheck(false, false, Aether.getServerPlayer(player).getParachuting(), Aether.getServerPlayer(player).getParachuteType(), Collections.singleton(player.username)));
        this.updatePlayerClientInfo((EntityPlayerMP)player, false);
    }

    public void onPlayerChangedDimension(EntityPlayer player) {}

    public void onPlayerRespawn(EntityPlayer player) {}
}
