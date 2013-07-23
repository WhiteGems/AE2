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

    public Object getServerGuiElement(int var1, EntityPlayer var2, World var3, int var4, int var5, int var6)
    {
        TileEntity var7 = var3.getBlockTileEntity(var4, var5, var6);
        return var1 == treasureChestID ? new ContainerChest(var2.inventory, (TileEntityTreasureChest)var7) : (var1 == incubatorID ? new ContainerIncubator(var2.inventory, (TileEntityIncubator)var7) : (var1 == freezerID ? new ContainerFreezer(var2.inventory, (TileEntityFreezer)var7) : (var1 == loreID ? new ContainerLore(var2.inventory, true, var2) : (var1 == craftingID ? new ContainerSkyrootWorkbench(var2.inventory, var2.worldObj, var4, var5, var6) : null))));
    }

    @SideOnly(Side.CLIENT)
    public Object getClientGuiElement(int var1, EntityPlayer var2, World var3, int var4, int var5, int var6)
    {
        TileEntity var7 = var3.getBlockTileEntity(var4, var5, var6);
        return !var2.worldObj.isRemote ? null : (var1 == treasureChestID ? new GuiTreasureChest(var2.inventory, (TileEntityTreasureChest)var7, ((TileEntityTreasureChest)var7).getKind()) : (var1 == incubatorID ? new GuiIncubator(var2.inventory, (TileEntityIncubator)var7) : (var1 == freezerID ? new GuiFreezer(var2.inventory, (TileEntityFreezer)var7) : (var1 == loreID ? new GuiLore(var2.inventory, var2, 0) : (var1 == craftingID ? new GuiSkyrootCrafting(var2.inventory, var2.worldObj, var4, var5, var6) : (var1 == entranceID ? new GuiDungeonEntrance(var2, (GuiScreen)null, (TileEntityEntranceController)var7) : (var1 == loadingScreenID ? new GuiDungeonScreen(Minecraft.getMinecraft()) : null)))))));
    }

    public static void registerGui(Class var0, Class var1)
    {
        guiContainers.add(var0);
        containers.add(var1);
        tile_entities.add(TileEntity.class);
    }

    public static int getID(Class var0, Class var1)
    {
        if (guiContainers.contains(var0))
        {
            tile_entities.add(guiContainers.indexOf(var0), var1);
            return guiContainers.indexOf(var0);
        }
        else
        {
            return 0;
        }
    }
}
