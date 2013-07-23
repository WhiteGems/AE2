package net.aetherteam.aether.blocks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;
import net.aetherteam.aether.util.Loc;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockBlueSkyrootLeaves extends BlockAether
    implements IAetherBlock
{
    public BlockBlueSkyrootLeaves(int blockID)
    {
        super(blockID, Material.leaves);
        setTickRandomly(true);
        setHardness(0.2F);
        setStepSound(Block.soundGrassFootstep);
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    private boolean nearTrunk(World world, int px, int py, int pz)
    {
        Loc startLoc = new Loc(px, py, pz);
        LinkedList toCheck = new LinkedList();
        ArrayList checked = new ArrayList();
        toCheck.offer(new Loc(px, py, pz));
        int bLeaves = this.blockID;

        while (!toCheck.isEmpty())
        {
            Loc curLoc = (Loc)toCheck.poll();

            if (!checked.contains(curLoc))
            {
                if (curLoc.distSimple(startLoc) <= 4)
                {
                    int block = curLoc.getBlock(world);
                    int meta = curLoc.getMeta(world);

                    if (block == AetherBlocks.AetherLog.blockID)
                    {
                        return true;
                    }

                    if (block == bLeaves)
                    {
                        toCheck.addAll(Arrays.asList(curLoc.adjacent()));
                    }
                }

                checked.add(curLoc);
            }
        }

        return false;
    }

    public int quantityDropped(Random random)
    {
        return 0;
    }

    private void removeLeaves(World world, int px, int py, int pz)
    {
        world.setBlock(px, py, pz, 0);
    }

    public boolean shouldSideBeRendered(IBlockAccess iblockaccess, int i, int j, int k, int l)
    {
        int i1 = iblockaccess.getBlockId(i, j, k);
        return true;
    }

    public void updateTick(World world, int i, int j, int k, Random rand)
    {
        if (!nearTrunk(world, i, j, k))
        {
            removeLeaves(world, i, j, k);
        }
    }
}

