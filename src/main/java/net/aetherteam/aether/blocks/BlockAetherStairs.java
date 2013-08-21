package net.aetherteam.aether.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.util.Icon;

public class BlockAetherStairs extends BlockStairs implements IAetherBlock
{
    /** The block that is used as model for the stair. */
    private Block modelBlock;
    private int modelMeta;

    protected BlockAetherStairs(int par1, Block par2Block, int metadata)
    {
        super(par1, par2Block, metadata);
        this.modelBlock = par2Block;
        this.modelMeta = metadata;
        this.setHardness(par2Block.blockHardness);
        this.setResistance(par2Block.blockResistance / 3.0F);
        this.setStepSound(par2Block.stepSound);
        this.setLightOpacity(0);
    }

    public Block setIconName(String name)
    {
        this.field_111026_f = "aether:" + name;
        return this.setUnlocalizedName("aether:" + name);
    }

    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public Icon getIcon(int par1, int par2)
    {
        return this.modelBlock.getIcon(par1, this.modelMeta);
    }
}
