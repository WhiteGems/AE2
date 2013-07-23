package net.aetherteam.aether.party.members;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import java.io.Serializable;
import net.aetherteam.aether.Aether;
import net.minecraft.entity.player.EntityPlayer;

public class PartyMember implements Serializable
{
    public String username;
    public int skinIndex;
    public String skinUrl;
    private MemberType type;
    Side side;

    public PartyMember(EntityPlayer var1)
    {
        this.type = MemberType.MEMBER;
        this.side = FMLCommonHandler.instance().getEffectiveSide();
        this.username = var1.username;

        if (this.side.isClient())
        {
            this.skinIndex = Aether.proxy.getClient().renderEngine.getTextureForDownloadableImage(var1.skinUrl, "/mob/char.png");
            this.skinUrl = var1.skinUrl;
        }
    }

    public PartyMember(String var1, String var2)
    {
        this.type = MemberType.MEMBER;
        this.side = FMLCommonHandler.instance().getEffectiveSide();
        this.username = var1;

        if (this.side.isClient())
        {
            this.skinIndex = Aether.proxy.getClient().renderEngine.getTextureForDownloadableImage(var2, "/mob/char.png");
            this.skinUrl = var2;
        }
    }

    public PartyMember promoteTo(MemberType var1)
    {
        this.type = var1;
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
