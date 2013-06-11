package net.aetherteam.aether.donator;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import net.aetherteam.aether.Aether;
import net.aetherteam.aether.packets.AetherPacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

public class SyncDonatorList
{
    String meName;
    Donator meDonator;
    HashMap DonatorList = new HashMap();
    boolean server = false;
    String MyRSA;

    public void initialVerification(Minecraft mc)
    {
        String Username = mc.session.username;

        this.MyRSA = Aether.getInstance().getMyKey(Username.toLowerCase());

        if (this.MyRSA != null)
        {
            Aether.getInstance();
            Aether.syncDonatorList.addMe(Username.toLowerCase(), this.MyRSA);
        }
    }

    public void addMe(String username, String myRSA)
    {
        if (this.server)
        {
            return;
        }

        this.meName = username.toLowerCase();

        if (myRSA != null)
        {
            this.meDonator = new Donator(username.toLowerCase(), myRSA);
            addDonator(this.meName.toLowerCase(), this.meDonator);
        }
    }

    public boolean isDonator(String name)
    {
        return this.DonatorList.containsKey(name.toLowerCase());
    }

    public Donator getDonator(String name)
    {
        return (Donator) this.DonatorList.get(name.toLowerCase());
    }

    public void addDonator(String name, Donator donator)
    {
        if (Aether.instance.isLegit(name.toLowerCase(), donator.getRSA()))
        {
            this.DonatorList.put(name.toLowerCase(), donator);
        }
        if (this.server) sendDonatorToAll(name.toLowerCase(), donator);
    }

    public void sendDonatorToAll(String name, Donator donator)
    {
        PacketDispatcher.sendPacketToAllPlayers(AetherPacketHandler.sendDonatorChange(name.toLowerCase(), donator));
    }

    public void sendChoiceToAll(String donator, DonatorChoice choice, boolean adding)
    {
        PacketDispatcher.sendPacketToServer(AetherPacketHandler.sendDonatorChoice(donator.toLowerCase(), choice, adding, (byte) 0));
    }

    public void sendTypeRemoveToAll(String donator, EnumChoiceType type)
    {
        PacketDispatcher.sendPacketToServer(AetherPacketHandler.sendDonatorTypeRemoval(donator.toLowerCase(), type, (byte) 0));
    }

    void sendAllToOne(Player player)
    {
        Iterator i = this.DonatorList.values().iterator();

        while (i.hasNext())
        {
            String name = (String) i.next();
            PacketDispatcher.sendPacketToPlayer(AetherPacketHandler.sendDonatorChange(name, (Donator) this.DonatorList.get(name.toLowerCase())), player);
        }
    }
}

/* Location:           D:\Dev\Mc\forge_orl\mcp\jars\bin\aether.jar
 * Qualified Name:     net.aetherteam.aether.donator.SyncDonatorList
 * JD-Core Version:    0.6.2
 */