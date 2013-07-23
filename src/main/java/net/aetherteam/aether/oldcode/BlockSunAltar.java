package net.aetherteam.aether.oldcode;

import net.aetherteam.aether.blocks.BlockAether;
import net.aetherteam.aether.blocks.IAetherBlock;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class BlockSunAltar extends BlockAether
    implements IAetherBlock
{
    Minecraft mc = Minecraft.getMinecraft();

    protected BlockSunAltar(int par1)
    {
        super(par1, Material.wood);
        setHardness(2.0F);
    }

    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
    {
        if (par5EntityPlayer.dimension != 3)
        {
            par5EntityPlayer.addChatMessage("block.SunAltar.notPossibleHere");
            return true;
        }

        return true;
    }
}

