package net.aetherteam.aether.party.members;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.io.Serializable;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public class PartyMember implements Serializable
{
    public String username;
    private transient EntityPlayer player;
    private MemberType type;
    Side side;

    public PartyMember(EntityPlayer player)
    {
        this.type = MemberType.MEMBER;
        this.side = FMLCommonHandler.instance().getEffectiveSide();
        this.username = player.username;
        this.player = player;
    }

    @SideOnly(Side.CLIENT)
    public PartyMember(String username)
    {
        this.type = MemberType.MEMBER;
        this.side = FMLCommonHandler.instance().getEffectiveSide();
        this.username = username;
        this.player = Minecraft.getMinecraft().theWorld.getPlayerEntityByName(username);
    }

    @SideOnly(Side.CLIENT)
    public EntityPlayer getPlayer()
    {
        if (this.player == null)
        {
            this.player = Minecraft.getMinecraft().theWorld.getPlayerEntityByName(this.username);
        }

        return this.player;
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
