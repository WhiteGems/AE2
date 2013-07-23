package net.aetherteam.aether.worldgen;

import cpw.mods.fml.common.registry.GameRegistry;
import java.util.Random;
import net.aetherteam.aether.blocks.AetherBlocks;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.feature.WorldGenTallGrass;
import net.minecraft.world.gen.feature.WorldGenerator;

public class BiomeGenAether extends BiomeGenBase
{
    public BiomeGenAether()
    {
        super(237);
        this.rainfall = 0.0F;
        this.spawnableMonsterList.clear();
        this.spawnableCreatureList.clear();
        this.spawnableWaterCreatureList.clear();
        this.spawnableCaveCreatureList.clear();
        this.setBiomeName("Aether");
        this.setDisableRain();
        GameRegistry.removeBiome(this);
    }

    /**
     * Gets a WorldGen appropriate for this biome.
     */
    public WorldGenerator getRandomWorldGenForTrees(Random var1)
    {
        int var2 = var1.nextInt(100);
        return (WorldGenerator)(var2 <= 9 ? new AetherGenNormalTree(AetherBlocks.GreenSkyrootLeaves.blockID, AetherBlocks.AetherLog.blockID, 0) : (var2 > 9 && var2 <= 18 ? new AetherGenLargeTree(AetherBlocks.GreenSkyrootLeaves.blockID, AetherBlocks.AetherLog.blockID, 0) : (var2 > 18 && var2 <= 35 ? new AetherGenNormalTree(AetherBlocks.BlueSkyrootLeaves.blockID, AetherBlocks.AetherLog.blockID, 0) : (var2 > 35 && var2 <= 63 ? new AetherGenMassiveTree(AetherBlocks.GreenSkyrootLeaves.blockID, 8, false) : (var2 > 63 && var2 <= 80 ? new AetherGenMassiveTree(AetherBlocks.BlueSkyrootLeaves.blockID, 8, false) : (var2 > 80 && var2 <= 85 ? new AetherGenLargeTree(AetherBlocks.GoldenOakLeaves.blockID, AetherBlocks.AetherLog.blockID, 2) : (var2 > 85 && var2 <= 90 ? new AetherGenMassiveTree(AetherBlocks.GreenSkyrootLeaves.blockID, 20, true) : (var2 > 90 && var2 <= 95 ? new AetherGenFruitTree(AetherBlocks.PurpleCrystalLeaves.blockID, 1, 50, 5, true) : new AetherGenMassiveTree(AetherBlocks.DarkBlueSkyrootLeaves.blockID, 35, true)))))))));
    }

    /**
     * Gets a WorldGen appropriate for this biome.
     */
    public WorldGenerator getRandomWorldGenForGrass(Random var1)
    {
        return new WorldGenTallGrass(AetherBlocks.TallAetherGrass.blockID, 1);
    }

    /**
     * takes temperature, returns color
     */
    public int getSkyColorByTemp(float var1)
    {
        return 12632319;
    }
}
