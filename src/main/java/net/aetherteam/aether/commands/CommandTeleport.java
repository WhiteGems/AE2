package net.aetherteam.aether.commands;

import java.util.List;
import net.aetherteam.aether.dungeons.Dungeon;
import net.aetherteam.aether.dungeons.DungeonHandler;
import net.aetherteam.aether.party.Party;
import net.aetherteam.aether.party.PartyController;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.MathHelper;

public class CommandTeleport extends CommandBase
{
    public String getCommandName()
    {
        return "tp";
    }

    /**
     * Return the required permission level for this command.
     */
    public int getRequiredPermissionLevel()
    {
        return 2;
    }

    public String getCommandUsage(ICommandSender par1ICommandSender)
    {
        return "commands.tp.usage";
    }

    public void processCommand(ICommandSender par1ICommandSender, String[] par2ArrayOfStr)
    {
        if (par2ArrayOfStr.length < 1)
        {
            throw new WrongUsageException("commands.tp.usage", new Object[0]);
        }
        else
        {
            EntityPlayerMP entityplayermp;

            if (par2ArrayOfStr.length != 2 && par2ArrayOfStr.length != 4)
            {
                entityplayermp = getCommandSenderAsPlayer(par1ICommandSender);
            }
            else
            {
                entityplayermp = func_82359_c(par1ICommandSender, par2ArrayOfStr[0]);

                if (entityplayermp == null)
                {
                    throw new PlayerNotFoundException();
                }
            }

            if (par2ArrayOfStr.length != 3 && par2ArrayOfStr.length != 4)
            {
                if (par2ArrayOfStr.length == 1 || par2ArrayOfStr.length == 2)
                {
                    EntityPlayerMP var11 = func_82359_c(par1ICommandSender, par2ArrayOfStr[par2ArrayOfStr.length - 1]);

                    if (var11 == null)
                    {
                        throw new PlayerNotFoundException();
                    }

                    if (var11.worldObj != entityplayermp.worldObj)
                    {
                        notifyAdmins(par1ICommandSender, "commands.tp.notSameDimension", new Object[0]);
                        return;
                    }

                    if (!this.dungeonCheck(entityplayermp, par1ICommandSender, var11.posX, var11.posY, var11.posZ))
                    {
                        return;
                    }

                    entityplayermp.mountEntity((Entity)null);
                    entityplayermp.playerNetServerHandler.setPlayerLocation(var11.posX, var11.posY, var11.posZ, var11.rotationYaw, var11.rotationPitch);
                    notifyAdmins(par1ICommandSender, "commands.tp.success", new Object[] {entityplayermp.getEntityName(), var11.getEntityName()});
                }
            }
            else if (entityplayermp.worldObj != null)
            {
                int i = par2ArrayOfStr.length - 3;
                double d0 = this.func_82368_a(par1ICommandSender, entityplayermp.posX, par2ArrayOfStr[i++]);
                double d1 = this.func_82367_a(par1ICommandSender, entityplayermp.posY, par2ArrayOfStr[i++], 0, 0);
                double d2 = this.func_82368_a(par1ICommandSender, entityplayermp.posZ, par2ArrayOfStr[i++]);

                if (!this.dungeonCheck(entityplayermp, par1ICommandSender, d0, d1, d2))
                {
                    return;
                }

                entityplayermp.mountEntity((Entity)null);
                entityplayermp.setPositionAndUpdate(d0, d1, d2);
                notifyAdmins(par1ICommandSender, "commands.tp.success.coordinates", new Object[] {entityplayermp.getEntityName(), Double.valueOf(d0), Double.valueOf(d1), Double.valueOf(d2)});
            }
        }
    }

    public boolean dungeonCheck(EntityPlayerMP playerbeingTeleported, ICommandSender par1ICommandSender, double x, double y, double z)
    {
        Party party = PartyController.instance().getParty((EntityPlayer)playerbeingTeleported);
        Dungeon dungeon = DungeonHandler.instance().getDungeon(party);
        Dungeon dungeonAtPos = DungeonHandler.instance().getInstanceAt(MathHelper.floor_double(x), MathHelper.floor_double(y), MathHelper.floor_double(z));

        if (playerbeingTeleported.dimension == 3)
        {
            if (party != null && dungeon != null && dungeon.hasMember(PartyController.instance().getMember((EntityPlayer)playerbeingTeleported)) && dungeonAtPos == null)
            {
                notifyAdmins(par1ICommandSender, "You cannot teleport " + playerbeingTeleported.username + " out side of a dungeon!", new Object[0]);
                return false;
            }

            if (dungeonAtPos != null && dungeonAtPos.getQueuedParty() != null && dungeonAtPos.getQueuedParty() != party)
            {
                notifyAdmins(par1ICommandSender, "You cannot teleport " + playerbeingTeleported.username + " into another dungeon!", new Object[0]);
                return false;
            }

            if (dungeonAtPos != null)
            {
                notifyAdmins(par1ICommandSender, "You cannot teleport " + playerbeingTeleported.username + " into a dungeon!", new Object[0]);
                return false;
            }
        }

        return true;
    }

    private double func_82368_a(ICommandSender par1ICommandSender, double par2, String par4Str)
    {
        return this.func_82367_a(par1ICommandSender, par2, par4Str, -30000000, 30000000);
    }

    private double func_82367_a(ICommandSender par1ICommandSender, double par2, String par4Str, int par5, int par6)
    {
        boolean flag = par4Str.startsWith("~");
        double d1 = flag ? par2 : 0.0D;

        if (!flag || par4Str.length() > 1)
        {
            boolean flag1 = par4Str.contains(".");

            if (flag)
            {
                par4Str = par4Str.substring(1);
            }

            d1 += parseDouble(par1ICommandSender, par4Str);

            if (!flag1 && !flag)
            {
                d1 += 0.5D;
            }
        }

        if (par5 != 0 || par6 != 0)
        {
            if (d1 < (double)par5)
            {
                throw new NumberInvalidException("commands.generic.double.tooSmall", new Object[] {Double.valueOf(d1), Integer.valueOf(par5)});
            }

            if (d1 > (double)par6)
            {
                throw new NumberInvalidException("commands.generic.double.tooBig", new Object[] {Double.valueOf(d1), Integer.valueOf(par6)});
            }
        }

        return d1;
    }

    /**
     * Adds the strings available in this command to the given list of tab completion options.
     */
    public List addTabCompletionOptions(ICommandSender par1ICommandSender, String[] par2ArrayOfStr)
    {
        return par2ArrayOfStr.length != 1 && par2ArrayOfStr.length != 2 ? null : getListOfStringsMatchingLastWord(par2ArrayOfStr, MinecraftServer.getServer().getAllUsernames());
    }

    /**
     * Return whether the specified command parameter index is a username parameter.
     */
    public boolean isUsernameIndex(String[] par1ArrayOfStr, int par2)
    {
        return par2 == 0;
    }
}
