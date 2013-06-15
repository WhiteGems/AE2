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

    public String getCommandUsage(ICommandSender var1)
    {
        return var1.translateString("commands.tp.usage", new Object[0]);
    }

    public void processCommand(ICommandSender var1, String[] var2)
    {
        if (var2.length < 1)
        {
            throw new WrongUsageException("commands.tp.usage", new Object[0]);
        } else
        {
            EntityPlayerMP var3;

            if (var2.length != 2 && var2.length != 4)
            {
                var3 = getCommandSenderAsPlayer(var1);
            } else
            {
                var3 = func_82359_c(var1, var2[0]);

                if (var3 == null)
                {
                    throw new PlayerNotFoundException();
                }
            }

            if (var2.length != 3 && var2.length != 4)
            {
                if (var2.length == 1 || var2.length == 2)
                {
                    EntityPlayerMP var11 = func_82359_c(var1, var2[var2.length - 1]);

                    if (var11 == null)
                    {
                        throw new PlayerNotFoundException();
                    }

                    if (var11.worldObj != var3.worldObj)
                    {
                        notifyAdmins(var1, "commands.tp.notSameDimension", new Object[0]);
                        return;
                    }

                    if (!this.dungeonCheck(var3, var1, var11.posX, var11.posY, var11.posZ))
                    {
                        return;
                    }

                    var3.mountEntity((Entity) null);
                    var3.playerNetServerHandler.setPlayerLocation(var11.posX, var11.posY, var11.posZ, var11.rotationYaw, var11.rotationPitch);
                    notifyAdmins(var1, "commands.tp.success", new Object[]{var3.getEntityName(), var11.getEntityName()});
                }
            } else if (var3.worldObj != null)
            {
                int var4 = var2.length - 3;
                double var5 = this.func_82368_a(var1, var3.posX, var2[var4++]);
                double var7 = this.func_82367_a(var1, var3.posY, var2[var4++], 0, 0);
                double var9 = this.func_82368_a(var1, var3.posZ, var2[var4++]);

                if (!this.dungeonCheck(var3, var1, var5, var7, var9))
                {
                    return;
                }

                var3.mountEntity((Entity) null);
                var3.setPositionAndUpdate(var5, var7, var9);
                notifyAdmins(var1, "commands.tp.success.coordinates", new Object[]{var3.getEntityName(), Double.valueOf(var5), Double.valueOf(var7), Double.valueOf(var9)});
            }
        }
    }

    public boolean dungeonCheck(EntityPlayerMP var1, ICommandSender var2, double var3, double var5, double var7)
    {
        Party var9 = PartyController.instance().getParty((EntityPlayer) var1);
        Dungeon var10 = DungeonHandler.instance().getDungeon(var9);
        Dungeon var11 = DungeonHandler.instance().getInstanceAt(MathHelper.floor_double(var3), MathHelper.floor_double(var5), MathHelper.floor_double(var7));

        if (var1.dimension == 3)
        {
            if (var9 != null && var10 != null && var10.hasMember(PartyController.instance().getMember((EntityPlayer) var1)) && var11 == null)
            {
                notifyAdmins(var2, "你不能传送 " + var1.username + " 到地牢外面!", new Object[0]);
                return false;
            }

            if (var11 != null && var11.getQueuedParty() != null && var11.getQueuedParty() != var9)
            {
                notifyAdmins(var2, "你不能传送 " + var1.username + " 进入其他地牢!", new Object[0]);
                return false;
            }

            if (var11 != null)
            {
                notifyAdmins(var2, "你不能传送 " + var1.username + " 进入地牢!", new Object[0]);
                return false;
            }
        }

        return true;
    }

    private double func_82368_a(ICommandSender var1, double var2, String var4)
    {
        return this.func_82367_a(var1, var2, var4, -30000000, 30000000);
    }

    private double func_82367_a(ICommandSender var1, double var2, String var4, int var5, int var6)
    {
        boolean var7 = var4.startsWith("~");
        double var8 = var7 ? var2 : 0.0D;

        if (!var7 || var4.length() > 1)
        {
            boolean var10 = var4.contains(".");

            if (var7)
            {
                var4 = var4.substring(1);
            }

            var8 += func_82363_b(var1, var4);

            if (!var10 && !var7)
            {
                var8 += 0.5D;
            }
        }

        if (var5 != 0 || var6 != 0)
        {
            if (var8 < (double) var5)
            {
                throw new NumberInvalidException("commands.generic.double.tooSmall", new Object[]{Double.valueOf(var8), Integer.valueOf(var5)});
            }

            if (var8 > (double) var6)
            {
                throw new NumberInvalidException("commands.generic.double.tooBig", new Object[]{Double.valueOf(var8), Integer.valueOf(var6)});
            }
        }

        return var8;
    }

    /**
     * Adds the strings available in this command to the given list of tab completion options.
     */
    public List addTabCompletionOptions(ICommandSender var1, String[] var2)
    {
        return var2.length != 1 && var2.length != 2 ? null : getListOfStringsMatchingLastWord(var2, MinecraftServer.getServer().getAllUsernames());
    }

    /**
     * Return whether the specified command parameter index is a username parameter.
     */
    public boolean isUsernameIndex(String[] var1, int var2)
    {
        return var2 == 0;
    }
}
