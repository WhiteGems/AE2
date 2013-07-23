package net.aetherteam.aether.party.members;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import java.io.Serializable;
import net.aetherteam.aether.Aether;
import net.aetherteam.aether.CommonProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class PartyMember
    implements Serializable
{
    public String username;
    public int skinIndex;
    public String skinUrl;
    private MemberType type = MemberType.MEMBER;

    Side side = FMLCommonHandler.instance().getEffectiveSide();

    public PartyMember(EntityPlayer player)
    {
        this.username = player.username;

        if (this.side.isClient())
        {
            this.skinIndex = Aether.proxy.getClient().renderEngine.a(player.skinUrl, "/mob/char.png");
            this.skinUrl = player.skinUrl;
        }
    }

    public PartyMember(String username, String skinUrl)
    {
        this.username = username;

        if (this.side.isClient())
        {
            this.skinIndex = Aether.proxy.getClient().renderEngine.a(skinUrl, "/mob/char.png");
            this.skinUrl = skinUrl;
        }
    }

    public PartyMember promoteTo(MemberType type)
    {
        this.type = type;
        return this;
    }

    public boolean isLeader()
    {
        return this.type == MemberType.LEADER;
    }

    public boolean isModerator()
    {
        return this.type == MemberType.MODERATOR;
    }

    public boolean canKick()
    {
        return this.type.canKick();
    }

    public boolean canBan()
    {
        return this.type.canBan();
    }

    public boolean canRecruit()
    {
        return this.type.canRecruit();
    }

    public boolean canPromote()
    {
        return this.type.canPromote();
    }

    public MemberType getType()
    {
        return this.type;
    }
}

