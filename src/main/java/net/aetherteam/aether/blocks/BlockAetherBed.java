package net.aetherteam.aether.blocks;

import java.util.Iterator;
import net.minecraft.block.BlockBed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumStatus;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

public class BlockAetherBed extends BlockBed
{
    /** Maps the foot-of-bed block to the head-of-bed block. */
    public static final int[][] footBlockToHeadBlockMap = new int[][] {{0, 1}, { -1, 0}, {0, -1}, {1, 0}};

    public BlockAetherBed(int id)
    {
        super(id);
    }

    /**
     * Called upon block activation (right click on the block.)
     */
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
    {
        if (par1World.isRemote)
        {
            return true;
        }
        else
        {
            int var10 = par1World.getBlockMetadata(par2, par3, par4);

            if (!isBlockHeadOfBed(var10))
            {
                int var20 = getDirection(var10);
                par2 += footBlockToHeadBlockMap[var20][0];
                par4 += footBlockToHeadBlockMap[var20][1];

                if (par1World.getBlockId(par2, par3, par4) != this.blockID)
                {
                    return true;
                }

                var10 = par1World.getBlockMetadata(par2, par3, par4);
            }

            if (!par1World.provider.canRespawnHere())
            {
                par5EntityPlayer.addChatMessage("Cannot sleep in this dimension.");
                return true;
            }
            else
            {
                if (isBedOccupied(var10))
                {
                    EntityPlayer var201 = null;
                    Iterator var12 = par1World.playerEntities.iterator();

                    while (var12.hasNext())
                    {
                        EntityPlayer var13 = (EntityPlayer)var12.next();

                        if (var13.isPlayerSleeping())
                        {
                            ChunkCoordinates var14 = var13.playerLocation;

                            if (var14.posX == par2 && var14.posY == par3 && var14.posZ == par4)
                            {
                                var201 = var13;
                            }
                        }
                    }

                    if (var201 != null)
                    {
                        par5EntityPlayer.addChatMessage("tile.bed.occupied");
                        return true;
                    }

                    setBedOccupied(par1World, par2, par3, par4, false);
                }

                EnumStatus var202 = par5EntityPlayer.sleepInBedAt(par2, par3, par4);

                if (var202 == EnumStatus.OK)
                {
                    setBedOccupied(par1World, par2, par3, par4, true);
                    return true;
                }
                else
                {
                    if (var202 == EnumStatus.NOT_POSSIBLE_NOW)
                    {
                        par5EntityPlayer.addChatMessage("tile.bed.noSleep");
                    }
                    else if (var202 == EnumStatus.NOT_SAFE)
                    {
                        par5EntityPlayer.addChatMessage("tile.bed.notSafe");
                    }

                    return true;
                }
            }
        }
    }
}
