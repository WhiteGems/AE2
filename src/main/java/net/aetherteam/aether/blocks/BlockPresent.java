package net.aetherteam.aether.blocks;

import java.util.Random;
import net.aetherteam.aether.entities.EntityTNTPresent;
import net.aetherteam.aether.items.AetherItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

public class BlockPresent extends BlockAether implements IAetherBlock
{
    public static Icon sprTop;
    public static Icon sprSide;
    int rarity;
    int randStart = 6;
    int randEnd = 9;
    long range;
    long fraction;
    int randomNumber;
    int crateType;

    public BlockPresent(int var1)
    {
        super(var1, Material.wood);
        this.range = (long)this.randEnd - (long)this.randStart + 1L;
        this.setHardness(0.1F);
        this.setStepSound(Block.soundGrassFootstep);
    }

    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public Icon getIcon(int var1, int meta)
    {
        return var1 == 0 ? sprTop : (var1 == 1 ? sprTop : sprSide);
    }

    /**
     * Called when the player destroys a block with an item that can harvest it. (i, j, k) are the coordinates of the
     * block and l is the block's subtype/damage.
     */
    public void harvestBlock(World world, EntityPlayer entityplayer, int i, int j, int k, int l)
    {
        Random random = new Random();
        byte randStart = 6;
        byte randEnd = 9;
        long range = (long)randEnd - (long)randStart + 1L;
        long fraction = (long)((double)range * random.nextDouble());
        int randomNumber = (int)(fraction + (long)randStart);
        int crateType = random.nextInt(4);
        int entitytntprimed;

        if (crateType == 0)
        {
            for (entitytntprimed = 1; entitytntprimed <= randomNumber; ++entitytntprimed)
            {
                world.spawnEntityInWorld(new EntityXPOrb(world, (double)i, (double)j, (double)k, 1));
            }
        }
        else if (crateType == 1)
        {
            if (random.nextInt(9) == 0)
            {
                this.dropBlockAsItem_do(world, i, j, k, new ItemStack(AetherItems.CandyCaneSword, 1));
            }
            else
            {
                for (entitytntprimed = 1; entitytntprimed <= randomNumber; ++entitytntprimed)
                {
                    this.dropBlockAsItem_do(world, i, j, k, new ItemStack(AetherItems.GingerBreadMan, 1));
                }
            }
        }
        else if (!world.isRemote)
        {
            EntityTNTPresent var17 = new EntityTNTPresent(world, (double)((float)i + 0.5F), (double)((float)j + 0.5F), (double)((float)k + 0.5F));
            var17.fuse = world.rand.nextInt(var17.fuse / 4) + var17.fuse / 8;
            world.spawnEntityInWorld(var17);
            world.playSoundAtEntity(var17, "aether:random.fuse", 1.0F, 1.0F);
        }
    }

    /**
     * Returns the ID of the items to drop on destruction.
     */
    public int idDropped(int i, Random random, int k)
    {
        return 0;
    }

    public void onBlockPlaced(World world, int i, int j, int k, int l)
    {
        Random random = new Random();
        this.fraction = (long)((double)this.range * random.nextDouble());
        this.randomNumber = (int)(this.fraction + (long)this.randStart);
        this.crateType = random.nextInt(4);
        this.rarity = random.nextInt(9);
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    public int quantityDropped(Random random)
    {
        return 0;
    }

    /**
     * Returns the usual quantity dropped by the block plus a bonus of 1 to 'i' (inclusive).
     */
    public int quantityDroppedWithBonus(int i, Random random)
    {
        return this.quantityDropped(random);
    }

    /**
     * When this method is called, your block should register all the icons it needs with the given IconRegister. This
     * is the only chance you get to register icons.
     */
    public void registerIcons(IconRegister par1IconRegister)
    {
        sprTop = par1IconRegister.registerIcon("aether:Present Top");
        sprSide = par1IconRegister.registerIcon("aether:Present Side");
        super.registerIcons(par1IconRegister);
    }
}
