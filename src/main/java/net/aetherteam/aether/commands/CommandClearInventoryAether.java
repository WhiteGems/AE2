package net.aetherteam.aether.commands;

import java.util.List;

import net.aetherteam.aether.Aether;
import net.aetherteam.aether.containers.InventoryAether;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

public class CommandClearInventoryAether extends CommandBase
{
    public String getCommandName()
    {
        return "clear";
    }

    public String getCommandUsage(ICommandSender var1)
    {
        return var1.translateString("commands.clear.usage", new Object[0]);
    }

    /**
     * Return the required permission level for this command.
     */
    public int getRequiredPermissionLevel()
    {
        return 2;
    }

    public void processCommand(ICommandSender var1, String[] var2)
    {
        EntityPlayerMP var3 = var2.length == 0 ? getCommandSenderAsPlayer(var1) : func_82359_c(var1, var2[0]);
        int var4 = var2.length >= 2 ? parseIntWithMin(var1, var2[1], 1) : -1;
        int var5 = var2.length >= 3 ? parseIntWithMin(var1, var2[2], 0) : -1;
        int var6 = var3.inventory.clearInventory(var4, var5);
        Aether.getServerPlayer(var3).inv.isEmpty();
        Aether.getServerPlayer(var3).inv = new InventoryAether(var3);
        Aether.getServerPlayer(var3).inv.onInventoryChanged();
        var3.inventoryContainer.detectAndSendChanges();

        if (var6 == 0 && Aether.getServerPlayer(var3).inv.isEmpty())
        {
            throw new CommandException("commands.clear.failure", new Object[]{var3.getEntityName()});
        } else
        {
            notifyAdmins(var1, "commands.clear.success", new Object[]{var3.getEntityName(), Integer.valueOf(var6)});
        }
    }

    /**
     * Adds the strings available in this command to the given list of tab completion options.
     */
    public List addTabCompletionOptions(ICommandSender var1, String[] var2)
    {
        return var2.length == 1 ? getListOfStringsMatchingLastWord(var2, this.getAllOnlineUsernames()) : null;
    }

    protected String[] getAllOnlineUsernames()
    {
        return MinecraftServer.getServer().getAllUsernames();
    }

    /**
     * Return whether the specified command parameter index is a username parameter.
     */
    public boolean isUsernameIndex(String[] var1, int var2)
    {
        return var2 == 0;
    }
}
