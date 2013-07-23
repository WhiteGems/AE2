package net.aetherteam.aether.party;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import java.util.ArrayList;
import java.util.Iterator;
import net.aetherteam.aether.dungeons.Dungeon;
import net.aetherteam.aether.dungeons.DungeonHandler;
import net.aetherteam.aether.notifications.client.ClientNotificationHandler;
import net.aetherteam.aether.packets.AetherPacketHandler;
import net.aetherteam.aether.party.exceptions.PartyFunctionException;
import net.aetherteam.aether.party.members.MemberType;
import net.aetherteam.aether.party.members.PartyMember;
import net.minecraft.entity.player.EntityPlayer;

public class PartyController
{
    private ArrayList parties = new ArrayList();
    private static PartyController clientController = new PartyController();
    private static PartyController serverController = new PartyController();

    public static PartyController instance()
    {
        Side var0 = FMLCommonHandler.instance().getEffectiveSide();
        return var0.isClient() ? clientController : serverController;
    }

    public boolean addParty(Party var1, boolean var2)
    {
        Side var3 = FMLCommonHandler.instance().getEffectiveSide();

        try
        {
            if (var1 == null)
            {
                throw new PartyFunctionException("The party getting added was null! Aborting!");
            }

            if (this.getParty(var1.getName()) == null)
            {
                this.parties.add(var1);

                if (var2 && var3.isClient())
                {
                    PacketDispatcher.sendPacketToServer(AetherPacketHandler.sendPartyChange(true, var1.getName(), var1.getLeader().username, var1.getLeader().skinUrl));
                }

                return true;
            }
        }
        catch (PartyFunctionException var5)
        {
            var5.printStackTrace();
        }

        return false;
    }

    public boolean removeParty(Party var1, boolean var2)
    {
        Side var3 = FMLCommonHandler.instance().getEffectiveSide();

        if (var1 != null)
        {
            Dungeon var4 = DungeonHandler.instance().getDungeon(var1);

            if (var4 != null)
            {
                var4.disbandQueue(var1);
            }

            if (var3.isClient() && var1.hasMember(this.getMember(ClientNotificationHandler.clientUsername())))
            {
                ClientNotificationHandler.createGeneric("Party Disbanded!", "", "");
            }

            this.parties.remove(var1);

            if (var2 && var3 == Side.CLIENT)
            {
                PacketDispatcher.sendPacketToServer(AetherPacketHandler.sendPartyChange(false, var1.getName(), var1.getLeader().username, var1.getLeader().skinUrl));
            }

            return true;
        }
        else
        {
            System.out.println("A party was trying to remove itself, but unfortunately doesn\'t exist :(");
            return false;
        }
    }

    public boolean promoteMember(PartyMember var1, MemberType var2, boolean var3)
    {
        Side var4 = FMLCommonHandler.instance().getEffectiveSide();

        if (var1 != null && var2 != null)
        {
            Party var5 = this.getParty(var1);

            if (var5 != null)
            {
                var5.promoteMember(var1, var2);

                if (var3 && var4.isClient())
                {
                    PacketDispatcher.sendPacketToServer(AetherPacketHandler.sendMemberTypeChange(var1.username, var2));
                }

                return true;
            }
        }
        else
        {
            System.out.println("A party member was trying to promote itself, but unfortunately doesn\'t exist :(");
        }

        return false;
    }

    public void requestPlayer(Party var1, PartyMember var2, String var3, boolean var4)
    {
        Side var5 = FMLCommonHandler.instance().getEffectiveSide();

        if (this.parties.contains(var1) && var1 != null && !var3.isEmpty() && var3 != null)
        {
            var1.queueRequestedPlayer(var3);

            if (var4 && var5.isClient())
            {
                PacketDispatcher.sendPacketToServer(AetherPacketHandler.sendRequestPlayer(true, var1.getName(), var2.username, var2.skinUrl, var3));
            }
        }
    }

    public void removePlayerRequest(Party var1, PartyMember var2, String var3, boolean var4)
    {
        Side var5 = FMLCommonHandler.instance().getEffectiveSide();

        if (this.parties.contains(var1) && var1 != null && !var3.isEmpty() && var3 != null)
        {
            var1.removeRequestedPlayer(var3);

            if (var4 && var5.isClient())
            {
                PacketDispatcher.sendPacketToServer(AetherPacketHandler.sendRequestPlayer(false, var1.getName(), var2.username, var2.skinUrl, var3));
            }
        }
    }

    public void joinParty(Party var1, PartyMember var2, boolean var3)
    {
        Side var4 = FMLCommonHandler.instance().getEffectiveSide();

        if (this.parties.contains(var1) && var1 != null && var2 != null)
        {
            if (var4.isClient() && var1.hasMember(this.getMember(ClientNotificationHandler.clientUsername())))
            {
                ClientNotificationHandler.createGeneric("Member joined!", var2.username, "");
            }

            var1.join(var2);

            if (var3 && var4.isClient())
            {
                PacketDispatcher.sendPacketToServer(AetherPacketHandler.sendPartyMemberChange(true, var1.getName(), var2.username, var2.skinUrl));
            }
        }
    }

    public void leaveParty(Party var1, PartyMember var2, boolean var3)
    {
        Side var4 = FMLCommonHandler.instance().getEffectiveSide();

        if (this.parties.contains(var1) && var1.hasMember(var2) && var1 != null && var2 != null)
        {
            Dungeon var5 = DungeonHandler.instance().getDungeon(var1);

            if (var5 != null)
            {
                var5.disbandMember(var2);
            }

            if (var4.isClient() && var1.hasMember(this.getMember(ClientNotificationHandler.clientUsername())) && !var2.username.equalsIgnoreCase(ClientNotificationHandler.clientUsername()))
            {
                ClientNotificationHandler.createGeneric("Member left!", var2.username, "");
            }

            if (var3 && var4.isClient())
            {
                PacketDispatcher.sendPacketToServer(AetherPacketHandler.sendPartyMemberChange(false, var1.getName(), var2.username, var2.skinUrl));
            }

            var1.leave(var2);
        }
    }

    public boolean changePartyName(Party var1, String var2, boolean var3)
    {
        Side var4 = FMLCommonHandler.instance().getEffectiveSide();

        if (this.parties.contains(var1) && var1 != null && !var2.isEmpty() && this.getParty(var2) == null)
        {
            if (var4.isClient() && var1.hasMember(this.getMember(ClientNotificationHandler.clientUsername())))
            {
                ClientNotificationHandler.createGeneric("Party Name Changed!", "To: " + var2, "");
            }

            if (var3 && var4.isClient())
            {
                PacketDispatcher.sendPacketToServer(AetherPacketHandler.sendPartyNameChange(var1.getName(), var2));
            }

            var1.setName(var2);
            return true;
        }
        else
        {
            return false;
        }
    }

    public void changePartyType(Party var1, PartyType var2, boolean var3)
    {
        Side var4 = FMLCommonHandler.instance().getEffectiveSide();

        if (this.parties.contains(var1) && var1 != null && var2 != null)
        {
            if (var4.isClient() && var1.hasMember(this.getMember(ClientNotificationHandler.clientUsername())))
            {
                ClientNotificationHandler.createGeneric("Party Changed!", "Now: " + var2.name(), "");
            }

            var1.setType(var2);

            if (var3 && var4 == Side.CLIENT)
            {
                PacketDispatcher.sendPacketToServer(AetherPacketHandler.sendPartyTypeChange(var1.getName(), var2));
            }
        }
    }

    public Party getParty(String var1)
    {
        Iterator var2 = this.parties.iterator();
        Party var3;

        do
        {
            if (!var2.hasNext())
            {
                return null;
            }

            var3 = (Party)var2.next();
        }
        while (!var3.getName().equalsIgnoreCase(var1));

        return var3;
    }

    public Party getParty(EntityPlayer var1)
    {
        PartyMember var2 = this.getMember(var1);
        Iterator var3 = this.parties.iterator();
        Party var4;

        do
        {
            if (!var3.hasNext())
            {
                return null;
            }

            var4 = (Party)var3.next();
        }
        while (!var4.getMembers().contains(var2));

        return var4;
    }

    public Party getParty(PartyMember var1)
    {
        Iterator var2 = this.parties.iterator();
        Party var3;

        do
        {
            if (!var2.hasNext())
            {
                return null;
            }

            var3 = (Party)var2.next();
        }
        while (!var3.getMembers().contains(var1));

        return var3;
    }

    public PartyMember getMember(String var1)
    {
        Iterator var2 = this.parties.iterator();

        while (var2.hasNext())
        {
            Party var3 = (Party)var2.next();
            Iterator var4 = var3.getMembers().iterator();

            while (var4.hasNext())
            {
                PartyMember var5 = (PartyMember)var4.next();

                if (var5.username.equalsIgnoreCase(var1))
                {
                    return var5;
                }
            }
        }

        return null;
    }

    public PartyMember getMember(EntityPlayer var1)
    {
        return this.getMember(var1.username);
    }

    public boolean inParty(Party var1, String var2)
    {
        return this.getMember(var2) != null && var1.hasMember(this.getMember(var2));
    }

    public boolean inParty(Party var1, EntityPlayer var2)
    {
        return this.inParty(var1, var2.username);
    }

    public boolean inParty(Party var1, PartyMember var2)
    {
        return this.inParty(var1, var2.username);
    }

    public boolean inParty(String var1)
    {
        return this.getMember(var1) != null;
    }

    public boolean inParty(EntityPlayer var1)
    {
        return this.inParty(var1.username);
    }

    public boolean inParty(PartyMember var1)
    {
        return this.inParty(var1.username);
    }

    public boolean isLeader(String var1)
    {
        return this.getMember(var1) != null && this.getMember(var1).isLeader();
    }

    public boolean isLeader(EntityPlayer var1)
    {
        return this.isLeader(var1.username);
    }

    public boolean isLeader(PartyMember var1)
    {
        return this.isLeader(var1.username);
    }

    public void setParties(ArrayList var1)
    {
        Side var2 = FMLCommonHandler.instance().getEffectiveSide();
        this.parties = var1;
    }

    public ArrayList getParties()
    {
        return this.parties;
    }
}
