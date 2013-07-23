package net.aetherteam.aether.blocks;

import java.util.Random;
import net.aetherteam.aether.items.AetherItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockAmbrosiumOre extends BlockAether
    implements IAetherBlock
{
    public BlockAmbrosiumOre(int blockID)
    {
        super(blockID, Material.rock);
        setHardness(3.0F);
        setResistance(5.0F);
        setStepSound(Block.soundStoneFootstep);
    }

    public void harvestBlock(World world, EntityPlayer entityplayer, int x, int y, int z, int meta)
    {
        entityplayer.addStat(net.minecraft.stats.StatList.mineBlockStatArray[this.blockID], 1);
        entityplayer.addExhaustion(0.025F);
        int i1 = EnchantmentHelper.getFortuneModifier(entityplayer) != 0 ? EnchantmentHelper.getFortuneModifier(entityplayer) : 1;

        if (!world.isRemote)
        {
            if ((canSilkHarvest(world, entityplayer, x, y, z, meta)) && (EnchantmentHelper.getSilkTouchModifier(entityplayer)))
            {
                ItemStack itemstack = createStackedBlock(meta);

                if (itemstack != null)
                {
                    dropBlockAsItem_do(world, x, y, z, itemstack);
                }
            }
            else if ((meta == 0) && (entityplayer.cd() != null) && (entityplayer.cd().itemID == AetherItems.SkyrootPickaxe.itemID))
            {
                ItemStack stack = new ItemStack(AetherItems.AmbrosiumShard.itemID, MathHelper.clamp_int(new Random().nextInt(i1 * 5), 1, i1 * 5 + 1), 0);
                entityplayer.addStat(net.minecraft.stats.StatList.mineBlockStatArray[this.blockID], 1);
                dropBlockAsItem_do(world, x, y, z, stack);
            }
            else
            {
                ItemStack stack = new ItemStack(AetherItems.AmbrosiumShard.itemID, MathHelper.clamp_int(new Random().nextInt(i1), 1, i1 + 1), 0);
                dropBlockAsItem_do(world, x, y, z, stack);
            }

            dropXpOnBlockBreak(world, x, y, z, 2);
        }
    }

    public int idDropped(int i, Random random, int k)
    {
        return AetherItems.AmbrosiumShard.itemID;
    }
}

