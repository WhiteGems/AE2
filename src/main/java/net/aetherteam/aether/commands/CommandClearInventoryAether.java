package net.aetherteam.aether.commands;

import java.util.List;
import net.aetherteam.aether.Aether;
import net.aetherteam.aether.PlayerBaseAetherServer;
import net.aetherteam.aether.containers.InventoryAether;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.server.MinecraftServer;

public class CommandClearInventoryAether extends CommandBase
{
    public String getCommandName()
    {
        return "clear";
    }

    public String getCommandUsage(ICommandSender par1ICommandSender)
    {
        return par1ICommandSender.translateString("commands.clear.usage", new Object[0]);
    }

    public int getRequiredPermissionLevel()
    {
        return 2;
    }

    public void processCommand(ICommandSender par1ICommandSender, String[] par2ArrayOfStr)
    {
        EntityPlayerMP entityplayermp = par2ArrayOfStr.length == 0 ? getCommandSenderAsPlayer(par1ICommandSender) : func_82359_c(par1ICommandSender, par2ArrayOfStr[0]);
        int i = par2ArrayOfStr.length >= 2 ? parseIntWithMin(par1ICommandSender, par2ArrayOfStr[1], 1) : -1;
        int j = par2ArrayOfStr.length >= 3 ? parseIntWithMin(par1ICommandSender, par2ArrayOfStr[2], 0) : -1;
        int k = entityplayermp.inventory.clearInventory(i, j);
        Aether.getServerPlayer(entityplayermp).inv.isEmpty();
        Aether.getServerPlayer(entityplayermp).inv = new InventoryAether(entityplayermp);
        Aether.getServerPlayer(entityplayermp).inv.onInventoryChanged();
        entityplayermp.inventoryContainer.detectAndSendChanges();

        if ((k == 0) && (Aether.getServerPlayer(entityplayermp).inv.isEmpty()))
        {
            throw new CommandException("commands.clear.failure", new Object[] { entityplayermp.getEntityName() });
        }

        notifyAdmins(par1ICommandSender, "commands.clear.success", new Object[] { entityplayermp.getEntityName(), Integer.valueOf(k) });
    }

    public List addTabCompletionOptions(ICommandSender par1ICommandSender, String[] par2ArrayOfStr)
    {
        return par2ArrayOfStr.length == 1 ? getListOfStringsMatchingLastWord(par2ArrayOfStr, getAllOnlineUsernames()) : null;
    }

    protected String[] getAllOnlineUsernames()
    {
        return MinecraftServer.getServer().getAllUsernames();
    }

    public boolean isUsernameIndex(String[] par1ArrayOfStr, int par2)
    {
        return par2 == 0;
    }
}

