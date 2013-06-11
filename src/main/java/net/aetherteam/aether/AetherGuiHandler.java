package net.aetherteam.aether;

import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

import net.aetherteam.aether.client.gui.GuiFreezer;
import net.aetherteam.aether.client.gui.GuiIncubator;
import net.aetherteam.aether.client.gui.GuiLore;
import net.aetherteam.aether.client.gui.GuiSkyrootCrafting;
import net.aetherteam.aether.client.gui.GuiTreasureChest;
import net.aetherteam.aether.client.gui.dungeons.GuiDungeonEntrance;
import net.aetherteam.aether.client.gui.social.GuiDungeonScreen;
import net.aetherteam.aether.containers.ContainerFreezer;
import net.aetherteam.aether.containers.ContainerIncubator;
import net.aetherteam.aether.containers.ContainerLore;
import net.aetherteam.aether.containers.ContainerSkyrootWorkbench;
import net.aetherteam.aether.tile_entities.TileEntityEntranceController;
import net.aetherteam.aether.tile_entities.TileEntityFreezer;
import net.aetherteam.aether.tile_entities.TileEntityIncubator;
import net.aetherteam.aether.tile_entities.TileEntityTreasureChest;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class AetherGuiHandler implements IGuiHandler
{
    private static List guiContainers = new ArrayList();
    private static List containers = new ArrayList();
    private static List tile_entities = new ArrayList();
    public static final int treasureChestID = guiContainers.size() + 1;
    public static final int incubatorID = guiContainers.size() + 2;
    public static final int freezerID = guiContainers.size() + 3;
    public static final int realLoreID = guiContainers.size() + 4;
    public static final int loreID = guiContainers.size() + 5;
    public static final int craftingID = guiContainers.size() + 6;
    public static final int entranceID = guiContainers.size() + 7;
    public static final int loadingScreenID = guiContainers.size() + 8;

    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
    {
        TileEntity tile = world.getBlockTileEntity(x, y, z);

        if (id == treasureChestID)
        {
            return new ContainerChest(player.inventory, (TileEntityTreasureChest) tile);
        }
        if (id == incubatorID)
        {
            return new ContainerIncubator(player.inventory, (TileEntityIncubator) tile);
        }
        if (id == freezerID)
        {
            return new ContainerFreezer(player.inventory, (TileEntityFreezer) tile);
        }
        if (id == loreID)
        {
            return new ContainerLore(player.inventory, true, player);
        }
        if (id == craftingID)
        {
            return new ContainerSkyrootWorkbench(player.inventory, player.worldObj, x, y, z);
        }

        return null;
    }

    @SideOnly(Side.CLIENT)
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
    {
        TileEntity tile = world.getBlockTileEntity(x, y, z);

        if (!player.worldObj.isRemote)
        {
            return null;
        }

        if (id == treasureChestID)
        {
            return new GuiTreasureChest(player.inventory, (TileEntityTreasureChest) tile, ((TileEntityTreasureChest) tile).getKind());
        }
        if (id == incubatorID)
        {
            return new GuiIncubator(player.inventory, (TileEntityIncubator) tile);
        }
        if (id == freezerID)
        {
            return new GuiFreezer(player.inventory, (TileEntityFreezer) tile);
        }
        if (id == loreID)
        {
            return new GuiLore(player.inventory, player, 0);
        }
        if (id == craftingID)
        {
            return new GuiSkyrootCrafting(player.inventory, player.worldObj, x, y, z);
        }
        if (id == entranceID)
        {
            return new GuiDungeonEntrance(player, null, (TileEntityEntranceController) tile);
        }
        if (id == loadingScreenID)
        {
            return new GuiDungeonScreen(Minecraft.getMinecraft());
        }

        return null;
    }

    public static void registerGui(Class guiContainer, Class container)
    {
        guiContainers.add(guiContainer);
        containers.add(container);
        tile_entities.add(TileEntity.class);
    }

    public static int getID(Class guiContainer, Class tile)
    {
        if (guiContainers.contains(guiContainer))
        {
            tile_entities.add(guiContainers.indexOf(guiContainer), tile);
            return guiContainers.indexOf(guiContainer);
        }

        return 0;
    }
}

/* Location:           D:\Dev\Mc\forge_orl\mcp\jars\bin\aether.jar
 * Qualified Name:     net.aetherteam.aether.AetherGuiHandler
 * JD-Core Version:    0.6.2
 */