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
    private Set username;
    private String cooldownName;
    private int maxHealth;
    private int cooldown;
    private int cooldownMax;
    private int coinAmount;
    private boolean isParachuting;
    private int parachuteType;

    public void onPlayerLogin(EntityPlayer var1)
    {
        MinecraftServer var2 = FMLCommonHandler.instance().getMinecraftServerInstance();
        ServerConfigurationManager var3 = var2.getConfigurationManager();

        if (var3.playerEntityList.size() == 1)
        {
            Aether.proxy.getClientInventories().clear();
            Aether.proxy.getClientExtraHearts().clear();
            Aether.proxy.getClientCooldown().clear();
            Aether.proxy.getClientMaxCooldown().clear();
            Aether.proxy.getPlayerClientInfo().clear();
        }

        PacketDispatcher.sendPacketToAllPlayers(AetherPacketHandler.sendAccessoryChange(Aether.getServerPlayer((EntityPlayerMP)var1).inv.writeToNBT(new NBTTagList()), false, true, Collections.singleton(var1.username), (byte)1));
        PacketDispatcher.sendPacketToAllPlayers(AetherPacketHandler.sendHeartChange(false, true, Aether.getServerPlayer(var1).maxHealth, Collections.singleton(var1.username)));
        Aether var10000;

        for (int var4 = 0; var4 < var3.playerEntityList.size(); ++var4)
        {
            this.entityPlayer = (EntityPlayerMP)var3.playerEntityList.get(var4);
            this.taglist = Aether.getServerPlayer(this.entityPlayer).inv.writeToNBT(new NBTTagList());
            this.maxHealth = Aether.getServerPlayer(this.entityPlayer).maxHealth;
            this.username = Collections.singleton(this.entityPlayer.username);
            this.cooldown = Aether.getServerPlayer(this.entityPlayer).generalcooldown;
            this.cooldownMax = Aether.getServerPlayer(this.entityPlayer).generalcooldownmax;
            this.cooldownName = Aether.getServerPlayer(this.entityPlayer).cooldownName;
            this.coinAmount = Aether.getServerPlayer(this.entityPlayer).getCoins();
            this.isParachuting = Aether.getServerPlayer(this.entityPlayer).getParachuting();
            this.parachuteType = Aether.getServerPlayer(this.entityPlayer).getParachuteType();
            ((EntityPlayerMP)var1).playerNetServerHandler.sendPacketToPlayer(AetherPacketHandler.sendAccessoryChange(this.taglist, false, true, this.username, (byte)1));
            ((EntityPlayerMP)var1).playerNetServerHandler.sendPacketToPlayer(AetherPacketHandler.sendHeartChange(false, true, this.maxHealth, this.username));
            String var5 = Aether.getInstance().getKey(this.entityPlayer.username);

            if (var5 != null)
            {
                ((EntityPlayerMP)var1).playerNetServerHandler.sendPacketToPlayer(AetherPacketHandler.sendDonatorChange(this.entityPlayer.username, new Donator(this.entityPlayer.username, var5)));
            }

            var10000 = Aether.instance;

            if (Aether.syncDonatorList.getDonator(this.entityPlayer.username) != null)
            {
                var10000 = Aether.instance;
                HashMap var6 = Aether.syncDonatorList.getDonator(this.entityPlayer.username).choices;

                if (var6 != null)
                {
                    if (var6.get(EnumChoiceType.CAPE) != null)
                    {
                        ((EntityPlayerMP)var1).playerNetServerHandler.sendPacketToPlayer(AetherPacketHandler.sendDonatorChoice(this.entityPlayer.username, (DonatorChoice)var6.get(EnumChoiceType.CAPE), true, (byte)1));
                    }

                    if (var6.get(EnumChoiceType.MOA) != null)
                    {
                        ((EntityPlayerMP)var1).playerNetServerHandler.sendPacketToPlayer(AetherPacketHandler.sendDonatorChoice(this.entityPlayer.username, (DonatorChoice)var6.get(EnumChoiceType.MOA), true, (byte)1));
                    }
                }
            }
        }

        PacketDispatcher.sendPacketToAllPlayers(AetherPacketHandler.sendParachuteCheck(false, this.isParachuting, this.isParachuting, this.parachuteType, Collections.singleton(var1.username)));
        ((EntityPlayerMP)var1).playerNetServerHandler.sendPacketToPlayer(AetherPacketHandler.sendCooldown(false, true, this.cooldown, this.cooldownMax, this.cooldownName, Collections.singleton(var1.username)));
        ((EntityPlayerMP)var1).playerNetServerHandler.sendPacketToPlayer(AetherPacketHandler.sendCoinChange(false, true, this.coinAmount, Collections.singleton(var1.username)));
        ((EntityPlayerMP)var1).playerNetServerHandler.sendPacketToPlayer(AetherPacketHandler.sendAllParties(PartyController.instance().getParties()));
        String var9 = Aether.getInstance().getKey(var1.username);

        if (var9 != null)
        {
            Aether.getInstance();
            Aether.syncDonatorList.sendDonatorToAll(var1.username, new Donator(var1.username, var9));
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
            String var7 = (String)var13.getKey();
            PlayerClientInfo var8 = (PlayerClientInfo)var13.getValue();
            ((EntityPlayerMP)var1).playerNetServerHandler.sendPacketToPlayer(AetherPacketHandler.sendPlayerClientInfo(false, true, var7, var8));
        }

        this.updatePlayerClientInfo((EntityPlayerMP)var1, true);
        Iterator var12 = DungeonHandler.instance().getInstances().iterator();

        while (var12.hasNext())
        {
            Dungeon var14 = (Dungeon)var12.next();
            ((EntityPlayerMP)var1).playerNetServerHandler.sendPacketToPlayer(AetherPacketHandler.sendDungeonChange(true, var14));
        }
    }

    public void updatePlayerClientInfo(EntityPlayerMP var1, boolean var2)
    {
        if (!var1.worldObj.isRemote)
        {
            MinecraftServer var3 = FMLCommonHandler.instance().getMinecraftServerInstance();
            ServerConfigurationManager var4 = var3.getConfigurationManager();
            PlayerClientInfo var5 = new PlayerClientInfo(var1.getHealth(), var1.getMaxHealth(), var1.getFoodStats().getFoodLevel(), var1.getTotalArmorValue(), Aether.getServerPlayer(var1).getCoins());
            Aether.proxy.getPlayerClientInfo().put(var1.username, var5);
            PartyMember var6 = PartyController.instance().getMember(var1.username);
            Party var7 = PartyController.instance().getParty(var6);

            for (int var8 = 0; var8 < var4.playerEntityList.size(); ++var8)
            {
                EntityPlayerMP var9 = (EntityPlayerMP)var4.playerEntityList.get(var8);
                var9.playerNetServerHandler.sendPacketToPlayer(AetherPacketHandler.sendPlayerClientInfo(false, var2, var1.username, var5));
            }

            var1.playerNetServerHandler.sendPacketToPlayer(AetherPacketHandler.sendPlayerClientInfo(false, var2, var1.username, var5));
        }
    }

    public void onPlayerLogout(EntityPlayer var1)
    {
        PartyMember var2 = PartyController.instance().getMember(var1);
        int var3 = MathHelper.floor_double(var1.posX);
        int var4 = MathHelper.floor_double(var1.posY);
        int var5 = MathHelper.floor_double(var1.posZ);
        MinecraftServer var6 = FMLCommonHandler.instance().getMinecraftServerInstance();
        ServerConfigurationManager var7 = var6.getConfigurationManager();
        Party var8 = PartyController.instance().getParty(var2);

        if (var2 != null && var7.playerEntityList.size() > 1 && var8 != null)
        {
            Dungeon var9 = DungeonHandler.instance().getDungeon(var8);
            PartyController.instance().leaveParty(var8, var2, false);
            PacketDispatcher.sendPacketToAllPlayers(AetherPacketHandler.sendPartyMemberChange(false, var8.getName(), var2.username, ""));

            if (var9 != null && !var9.hasStarted())
            {
                DungeonHandler.instance().checkForQueue(var9);
                PacketDispatcher.sendPacketToAllPlayers(AetherPacketHandler.sendDungeonQueueCheck(var9));
            }
        }

        PacketDispatcher.sendPacketToAllPlayers(AetherPacketHandler.sendAccessoryChange(Aether.getServerPlayer((EntityPlayerMP)var1).inv.writeToNBT(new NBTTagList()), false, false, Collections.singleton(var1.username), (byte)1));
        PacketDispatcher.sendPacketToAllPlayers(AetherPacketHandler.sendHeartChange(false, false, Aether.getServerPlayer(var1).maxHealth, Collections.singleton(var1.username)));
        PacketDispatcher.sendPacketToAllPlayers(AetherPacketHandler.sendCooldown(false, false, Aether.getServerPlayer(var1).generalcooldown, Aether.getServerPlayer(var1).generalcooldownmax, Aether.getServerPlayer(var1).cooldownName, Collections.singleton(var1.username)));
        PacketDispatcher.sendPacketToAllPlayers(AetherPacketHandler.sendCoinChange(false, false, Aether.getServerPlayer(var1).getCoins(), Collections.singleton(var1.username)));
        PacketDispatcher.sendPacketToAllPlayers(AetherPacketHandler.sendParachuteCheck(false, false, Aether.getServerPlayer(var1).getParachuting(), Aether.getServerPlayer(var1).getParachuteType(), Collections.singleton(var1.username)));
        this.updatePlayerClientInfo((EntityPlayerMP)var1, false);
    }

    public void onPlayerChangedDimension(EntityPlayer var1) {}

    public void onPlayerRespawn(EntityPlayer var1) {}
}
