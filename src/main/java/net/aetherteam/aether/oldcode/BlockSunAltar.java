package net.aetherteam.aether.oldcode;

import net.aetherteam.aether.blocks.BlockAether;
import net.aetherteam.aether.blocks.IAetherBlock;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class BlockSunAltar extends BlockAether implements IAetherBlock
{
    Minecraft mc = Minecraft.getMinecraft();

    protected BlockSunAltar(int var1)
    {
        super(var1, Material.wood);
        this.setHardness(2.0F);
    }

    /**
     * Called upon block activation (right click on the block.)
     */
    public boolean onBlockActivated(World var1, int var2, int var3, int var4, EntityPlayer var5, int var6, float var7, float var8, float var9)
    {
        if (var5.dimension != 3)
        {
            var5.addChatMessage("block.SunAltar.notPossibleHere");
            return true;
        } else
        {
            return true;
        }
    }
}
