package net.aetherteam.aether.donator;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import java.util.HashMap;
import java.util.Iterator;
import net.aetherteam.aether.Aether;
import net.aetherteam.aether.packets.AetherPacketHandler;
import net.minecraft.client.Minecraft;

public class SyncDonatorList
{
    String meName;
    Donator meDonator;
    HashMap DonatorList = new HashMap();
    boolean server = false;
    String MyRSA;

    public void initialVerification(Minecraft var1)
    {
        String var2 = var1.session.username;
        this.MyRSA = Aether.getInstance().getMyKey(var2.toLowerCase());

        if (this.MyRSA != null)
        {
            Aether.getInstance();
            Aether.syncDonatorList.addMe(var2.toLowerCase(), this.MyRSA);
        }
    }

    public void addMe(String var1, String var2)
    {
        if (!this.server)
        {
            this.meName = var1.toLowerCase();

            if (var2 != null)
            {
                this.meDonator = new Donator(var1.toLowerCase(), var2);
                this.addDonator(this.meName.toLowerCase(), this.meDonator);
            }
        }
    }

    public boolean isDonator(String var1)
    {
        return this.DonatorList.containsKey(var1.toLowerCase());
    }

    public Donator getDonator(String var1)
    {
        return (Donator)this.DonatorList.get(var1.toLowerCase());
    }

    public void addDonator(String var1, Donator var2)
    {
        if (Aether.instance.isLegit(var1.toLowerCase(), var2.getRSA()))
        {
            this.DonatorList.put(var1.toLowerCase(), var2);
        }

        if (this.server)
        {
            this.sendDonatorToAll(var1.toLowerCase(), var2);
        }
    }

    public void sendDonatorToAll(String var1, Donator var2)
    {
        PacketDispatcher.sendPacketToAllPlayers(AetherPacketHandler.sendDonatorChange(var1.toLowerCase(), var2));
    }

    public void sendChoiceToAll(String var1, DonatorChoice var2, boolean var3)
    {
        PacketDispatcher.sendPacketToServer(AetherPacketHandler.sendDonatorChoice(var1.toLowerCase(), var2, var3, (byte)0));
    }

    public void sendTypeRemoveToAll(String var1, EnumChoiceType var2)
    {
        PacketDispatcher.sendPacketToServer(AetherPacketHandler.sendDonatorTypeRemoval(var1.toLowerCase(), var2, (byte)0));
    }

    void sendAllToOne(Player var1)
    {
        Iterator var2 = this.DonatorList.values().iterator();

        while (var2.hasNext())
        {
            String var3 = (String)var2.next();
            PacketDispatcher.sendPacketToPlayer(AetherPacketHandler.sendDonatorChange(var3, (Donator)this.DonatorList.get(var3.toLowerCase())), var1);
        }
    }
}
