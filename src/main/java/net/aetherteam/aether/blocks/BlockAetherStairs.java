package net.aetherteam.aether.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.util.Icon;

public class BlockAetherStairs extends BlockStairs
    implements IAetherBlock
{
    private Block modelBlock;
    private int modelMeta;

    protected BlockAetherStairs(int par1, Block par2Block, int metadata)
    {
        super(par1, par2Block, metadata);
        this.modelBlock = par2Block;
        this.modelMeta = metadata;
        setHardness(par2Block.blockHardness);
        setResistance(par2Block.blockResistance / 3.0F);
        setStepSound(par2Block.stepSound);
        setLightOpacity(0);
    }

    public Block setIconName(String name)
    {
        return setUnlocalizedName("Aether:" + name);
    }

    public Icon getIcon(int par1, int par2)
    {
        return this.modelBlock.getIcon(par1, this.modelMeta);
    }
}

