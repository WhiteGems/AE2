package net.aetherteam.aether.worldgen;

import cpw.mods.fml.common.registry.GameRegistry;
import java.util.List;
import java.util.Random;
import net.aetherteam.aether.blocks.AetherBlocks;
import net.minecraft.block.Block;
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
        setBiomeName("Aether");
        setDisableRain();
        GameRegistry.removeBiome(this);
    }

    public WorldGenerator getRandomWorldGenForTrees(Random random)
    {
        int ratio = random.nextInt(100);

        if (ratio <= 9)
        {
            return new AetherGenNormalTree(AetherBlocks.GreenSkyrootLeaves.blockID, AetherBlocks.AetherLog.blockID, 0);
        }

        if ((ratio > 9) && (ratio <= 18))
        {
            return new AetherGenLargeTree(AetherBlocks.GreenSkyrootLeaves.blockID, AetherBlocks.AetherLog.blockID, 0);
        }

        if ((ratio > 18) && (ratio <= 35))
        {
            return new AetherGenNormalTree(AetherBlocks.BlueSkyrootLeaves.blockID, AetherBlocks.AetherLog.blockID, 0);
        }

        if ((ratio > 35) && (ratio <= 63))
        {
            return new AetherGenMassiveTree(AetherBlocks.GreenSkyrootLeaves.blockID, 8, false);
        }

        if ((ratio > 63) && (ratio <= 80))
        {
            return new AetherGenMassiveTree(AetherBlocks.BlueSkyrootLeaves.blockID, 8, false);
        }

        if ((ratio > 80) && (ratio <= 85))
        {
            return new AetherGenLargeTree(AetherBlocks.GoldenOakLeaves.blockID, AetherBlocks.AetherLog.blockID, 2);
        }

        if ((ratio > 85) && (ratio <= 90))
        {
            return new AetherGenMassiveTree(AetherBlocks.GreenSkyrootLeaves.blockID, 20, true);
        }

        if ((ratio > 90) && (ratio <= 95))
        {
            return new AetherGenFruitTree(AetherBlocks.PurpleCrystalLeaves.blockID, 1, 50, 5, true);
        }

        return new AetherGenMassiveTree(AetherBlocks.DarkBlueSkyrootLeaves.blockID, 35, true);
    }

    public WorldGenerator getRandomWorldGenForGrass(Random par1Random)
    {
        return new WorldGenTallGrass(AetherBlocks.TallAetherGrass.blockID, 1);
    }

    public int getSkyColorByTemp(float f)
    {
        return 12632319;
    }
}

