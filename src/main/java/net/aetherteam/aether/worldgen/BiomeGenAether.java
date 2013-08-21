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
    public WorldGenerator getRandomWorldGenForTrees(Random random)
    {
        int ratio = random.nextInt(100);
        return (WorldGenerator)(ratio <= 9 ? new AetherGenNormalTree(AetherBlocks.GreenSkyrootLeaves.blockID, AetherBlocks.AetherLog.blockID, 0) : (ratio > 9 && ratio <= 18 ? new AetherGenLargeTree(AetherBlocks.GreenSkyrootLeaves.blockID, AetherBlocks.AetherLog.blockID, 0) : (ratio > 18 && ratio <= 35 ? new AetherGenNormalTree(AetherBlocks.BlueSkyrootLeaves.blockID, AetherBlocks.AetherLog.blockID, 0) : (ratio > 35 && ratio <= 63 ? new AetherGenMassiveTree(AetherBlocks.GreenSkyrootLeaves.blockID, 8, false) : (ratio > 63 && ratio <= 80 ? new AetherGenMassiveTree(AetherBlocks.BlueSkyrootLeaves.blockID, 8, false) : (ratio > 80 && ratio <= 85 ? new AetherGenLargeTree(AetherBlocks.GoldenOakLeaves.blockID, AetherBlocks.AetherLog.blockID, 2) : (ratio > 85 && ratio <= 90 ? new AetherGenMassiveTree(AetherBlocks.GreenSkyrootLeaves.blockID, 20, true) : (ratio > 90 && ratio <= 95 ? new AetherGenFruitTree(AetherBlocks.PurpleCrystalLeaves.blockID, 1, 50, 5, true) : new AetherGenMassiveTree(AetherBlocks.DarkBlueSkyrootLeaves.blockID, 35, true)))))))));
    }

    /**
     * Gets a WorldGen appropriate for this biome.
     */
    public WorldGenerator getRandomWorldGenForGrass(Random par1Random)
    {
        return new WorldGenTallGrass(AetherBlocks.TallAetherGrass.blockID, 1);
    }

    /**
     * takes temperature, returns color
     */
    public int getSkyColorByTemp(float f)
    {
        return 12632319;
    }
}
