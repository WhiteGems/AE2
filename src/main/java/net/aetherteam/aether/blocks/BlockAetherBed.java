package net.aetherteam.aether.blocks;

import java.util.Iterator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumStatus;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

public class BlockAetherBed extends BlockBed implements IAetherBlock
{
    public static final int[][] footBlockToHeadBlockMap = new int[][] {{0, 1}, { -1, 0}, {0, -1}, {1, 0}};

    public BlockAetherBed(int var1)
    {
        super(var1);
    }

    public Block setIconName(String var1)
    {
        return this.setUnlocalizedName("Aether:" + var1);
    }

    /**
     * Called upon block activation (right click on the block.)
     */
    public boolean onBlockActivated(World var1, int var2, int var3, int var4, EntityPlayer var5, int var6, float var7, float var8, float var9)
    {
        if (var1.isRemote)
        {
            return true;
        }
        else
        {
            int var10 = var1.getBlockMetadata(var2, var3, var4);

            if (!isBlockHeadOfBed(var10))
            {
                int var11 = getDirection(var10);
                var2 += footBlockToHeadBlockMap[var11][0];
                var4 += footBlockToHeadBlockMap[var11][1];

                if (var1.getBlockId(var2, var3, var4) != this.blockID)
                {
                    return true;
                }

                var10 = var1.getBlockMetadata(var2, var3, var4);
            }

            if (!var1.provider.canRespawnHere())
            {
                var5.addChatMessage("在这个世界睡不着");
                return true;
            }
            else
            {
                if (isBedOccupied(var10))
                {
                    EntityPlayer var15 = null;
                    Iterator var12 = var1.playerEntities.iterator();

                    while (var12.hasNext())
                    {
                        EntityPlayer var13 = (EntityPlayer)var12.next();

                        if (var13.isPlayerSleeping())
                        {
                            ChunkCoordinates var14 = var13.playerLocation;

                            if (var14.posX == var2 && var14.posY == var3 && var14.posZ == var4)
                            {
                                var15 = var13;
                            }
                        }
                    }

                    if (var15 != null)
                    {
                        var5.addChatMessage("tile.bed.occupied");
                        return true;
                    }

                    setBedOccupied(var1, var2, var3, var4, false);
                }

                EnumStatus var16 = var5.sleepInBedAt(var2, var3, var4);

                if (var16 == EnumStatus.OK)
                {
                    setBedOccupied(var1, var2, var3, var4, true);
                    return true;
                }
                else
                {
                    if (var16 == EnumStatus.NOT_POSSIBLE_NOW)
                    {
                        var5.addChatMessage("tile.bed.noSleep");
                    }
                    else if (var16 == EnumStatus.NOT_SAFE)
                    {
                        var5.addChatMessage("tile.bed.notSafe");
                    }

                    return true;
                }
            }
        }
    }
}
