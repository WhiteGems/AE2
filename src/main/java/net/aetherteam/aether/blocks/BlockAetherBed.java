package net.aetherteam.aether.blocks;

import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumStatus;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;

public class BlockAetherBed extends BlockBed
    implements IAetherBlock
{
    public static final int[][] footBlockToHeadBlockMap = { { 0, 1 }, { -1, 0 }, { 0, -1 }, { 1, 0 } };

    public BlockAetherBed(int id)
    {
        super(id);
    }

    public Block setIconName(String name)
    {
        return setUnlocalizedName("Aether:" + name);
    }

    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
    {
        if (par1World.isRemote)
        {
            return true;
        }

        int var10 = par1World.getBlockMetadata(par2, par3, par4);

        if (!isBlockHeadOfBed(var10))
        {
            int var11 = getDirection(var10);
            par2 += footBlockToHeadBlockMap[var11][0];
            par4 += footBlockToHeadBlockMap[var11][1];

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

        if (isBedOccupied(var10))
        {
            EntityPlayer var18 = null;
            Iterator var12 = par1World.playerEntities.iterator();

            while (var12.hasNext())
            {
                EntityPlayer var13 = (EntityPlayer)var12.next();

                if (var13.isPlayerSleeping())
                {
                    ChunkCoordinates var14 = var13.playerLocation;

                    if ((var14.posX == par2) && (var14.posY == par3) && (var14.posZ == par4))
                    {
                        var18 = var13;
                    }
                }
            }

            if (var18 != null)
            {
                par5EntityPlayer.addChatMessage("tile.bed.occupied");
                return true;
            }

            setBedOccupied(par1World, par2, par3, par4, false);
        }

        EnumStatus var20 = par5EntityPlayer.sleepInBedAt(par2, par3, par4);

        if (var20 == EnumStatus.OK)
        {
            setBedOccupied(par1World, par2, par3, par4, true);
            return true;
        }

        if (var20 == EnumStatus.NOT_POSSIBLE_NOW)
        {
            par5EntityPlayer.addChatMessage("tile.bed.noSleep");
        }
        else if (var20 == EnumStatus.NOT_SAFE)
        {
            par5EntityPlayer.addChatMessage("tile.bed.notSafe");
        }

        return true;
    }
}

