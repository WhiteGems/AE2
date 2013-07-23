package net.aetherteam.aether.items;

import java.util.Random;
import net.aetherteam.aether.blocks.AetherBlocks;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemHolystonePickaxe extends ItemPickaxe
{
    private static Random random = new Random();
    public static final Block[] blocksEffectiveAgainst = { Block.cobblestone, Block.stoneDoubleSlab, Block.stoneSingleSlab, Block.stone, Block.sandStone, Block.cobblestoneMossy, Block.oreIron, Block.blockIron, Block.oreCoal, Block.blockGold, Block.oreGold, Block.oreDiamond, Block.blockDiamond, Block.ice, Block.netherrack, Block.oreLapis, Block.blockLapis, Block.oreRedstone, Block.oreRedstoneGlowing, Block.rail, Block.railDetector, Block.railPowered, Block.railActivator, AetherBlocks.Holystone, AetherBlocks.HolystoneBrick, AetherBlocks.HolystoneStairs, AetherBlocks.HolystoneWall };

    protected ItemHolystonePickaxe(int i, EnumToolMaterial enumtoolmaterial)
    {
        super(i, enumtoolmaterial);
    }

    public Item setIconName(String name)
    {
        return setUnlocalizedName("Aether:" + name);
    }

    public boolean onBlockDestroyed(ItemStack itemstack, World world, int i, int j, int k, int l, EntityLiving entityliving)
    {
        if (random.nextInt(20) == 0)
        {
            if (!world.isRemote)
            {
                entityliving.dropItemWithOffset(AetherItems.AmbrosiumShard.itemID, 1, 0.0F);
            }
        }

        return super.onBlockDestroyed(itemstack, world, i, j, k, l, entityliving);
    }
}

