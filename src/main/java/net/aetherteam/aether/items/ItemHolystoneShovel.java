package net.aetherteam.aether.items;

import java.util.Random;
import net.aetherteam.aether.blocks.AetherBlocks;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemHolystoneShovel extends ItemSpade
{
    private static Random random = new Random();
    public static final Block[] blocksEffectiveAgainst = new Block[] {Block.grass, Block.dirt, Block.sand, Block.gravel, Block.snow, Block.blockSnow, Block.blockClay, Block.tilledField, Block.slowSand, Block.mycelium, AetherBlocks.AetherDirt, AetherBlocks.AetherGrass};

    public ItemHolystoneShovel(int var1, EnumToolMaterial var2)
    {
        super(var1, var2);
    }

    public Item setIconName(String var1)
    {
        return this.setUnlocalizedName("Aether:" + var1);
    }

    public boolean onBlockDestroyed(ItemStack var1, World var2, int var3, int var4, int var5, int var6, EntityLiving var7)
    {
        if (random.nextInt(20) == 0 && !var2.isRemote)
        {
            var7.dropItemWithOffset(AetherItems.AmbrosiumShard.itemID, 1, 0.0F);
        }

        return super.onBlockDestroyed(var1, var2, var3, var4, var5, var6, var7);
    }

    /**
     * Returns the strength of the stack against a given block. 1.0F base, (Quality+1)*2 if correct blocktype, 1.5F if
     * sword
     */
    public float getStrVsBlock(ItemStack var1, Block var2)
    {
        for (int var3 = 0; var3 < blocksEffectiveAgainst.length; ++var3)
        {
            if (blocksEffectiveAgainst[var3] == var2)
            {
                return this.efficiencyOnProperMaterial;
            }
        }

        return 1.0F;
    }
}
