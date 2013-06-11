package net.aetherteam.aether.tile_entities;

import cpw.mods.fml.common.registry.GameRegistry;

public class AetherTileEntities
{
    public static void init()
    {
        GameRegistry.registerTileEntity(TileEntityFreezer.class, "Freezer");
        GameRegistry.registerTileEntity(TileEntityIncubator.class, "Incubator");
        GameRegistry.registerTileEntity(TileEntityAltar.class, "Altar");
        GameRegistry.registerTileEntity(TileEntityTreasureChest.class, "Treasure Chest");
        GameRegistry.registerTileEntity(TileEntitySkyrootChest.class, "Skyroot Chest");
        GameRegistry.registerTileEntity(TileEntityBronzeDoorController.class, "Bronze Door");
        GameRegistry.registerTileEntity(TileEntityEntranceController.class, "Dungeon Entrance");
        GameRegistry.registerTileEntity(TileEntityBronzeSpawner.class, "Bronze Spawner");
    }
}
