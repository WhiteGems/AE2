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
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class AetherGuiHandler implements IGuiHandler
{
    private static List < Class <? extends GuiContainer >> guiContainers = new ArrayList();
    private static List < Class <? extends Container >> containers = new ArrayList();
    private static List < Class <? extends TileEntity >> tile_entities = new ArrayList();
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
        return id == treasureChestID ? new ContainerChest(player.inventory, (TileEntityTreasureChest)tile) : (id == incubatorID ? new ContainerIncubator(player.inventory, (TileEntityIncubator)tile) : (id == freezerID ? new ContainerFreezer(player.inventory, (TileEntityFreezer)tile) : (id == loreID ? new ContainerLore(player.inventory, true, player) : (id == craftingID ? new ContainerSkyrootWorkbench(player.inventory, player.worldObj, x, y, z) : null))));
    }

    @SideOnly(Side.CLIENT)
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
    {
        TileEntity tile = world.getBlockTileEntity(x, y, z);
        return !player.worldObj.isRemote ? null : (id == treasureChestID ? new GuiTreasureChest(player.inventory, (TileEntityTreasureChest)tile, ((TileEntityTreasureChest)tile).getKind()) : (id == incubatorID ? new GuiIncubator(player.inventory, (TileEntityIncubator)tile) : (id == freezerID ? new GuiFreezer(player.inventory, (TileEntityFreezer)tile) : (id == loreID ? new GuiLore(player.inventory, player, 0) : (id == craftingID ? new GuiSkyrootCrafting(player.inventory, player.worldObj, x, y, z) : (id == entranceID ? new GuiDungeonEntrance(player, (GuiScreen)null, (TileEntityEntranceController)tile) : (id == loadingScreenID ? new GuiDungeonScreen(Minecraft.getMinecraft()) : null)))))));
    }

    public static void registerGui(Class <? extends GuiContainer > guiContainer, Class <? extends Container > container)
    {
        guiContainers.add(guiContainer);
        containers.add(container);
        tile_entities.add(TileEntity.class);
    }

    public static int getID(Class <? extends GuiContainer > guiContainer, Class <? extends TileEntity > tile)
    {
        if (guiContainers.contains(guiContainer))
        {
            tile_entities.add(guiContainers.indexOf(guiContainer), tile);
            return guiContainers.indexOf(guiContainer);
        }
        else
        {
            return 0;
        }
    }
}
