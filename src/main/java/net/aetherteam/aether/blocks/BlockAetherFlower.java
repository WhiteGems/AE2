package net.aetherteam.aether.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;

public class BlockAetherFlower extends BlockFlower
    implements IAetherBlock
{
    public BlockAetherFlower(int id)
    {
        this(id, Material.plants);
    }

    public BlockAetherFlower(int id, Material material)
    {
        super(id, material);
        setTickRandomly(true);
        float var3 = 0.2F;
        setHardness(0.0F);
        setStepSound(Block.soundGrassFootstep);
        setBlockBounds(0.5F - var3, 0.0F, 0.5F - var3, 0.5F + var3, var3 * 3.0F, 0.5F + var3);
    }

    protected boolean canThisPlantGrowOnThisBlockID(int blockID)
    {
        return (blockID == AetherBlocks.AetherGrass.blockID) || (blockID == AetherBlocks.AetherDirt.blockID);
    }

    public boolean canBlockStay(World par1World, int par2, int par3, int par4)
    {
        Block soil = Block.blocksList[par1World.getBlockId(par2, par3 - 1, par4)];
        return ((par1World.getFullBlockLightValue(par2, par3, par4) >= 8) || (par1World.canBlockSeeTheSky(par2, par3, par4))) && (soil != null) && ((soil.blockID == AetherBlocks.AetherDirt.blockID) || (soil.blockID == AetherBlocks.AetherGrass.blockID));
    }

    public Block setIconName(String name)
    {
        return setUnlocalizedName("Aether:" + name);
    }
}

