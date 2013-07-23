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
    public Icon getIcon(int var1, int var2)
    {
        return var1 == 0 ? sprTop : (var1 == 1 ? sprTop : sprSide);
    }

    /**
     * Called when the player destroys a block with an item that can harvest it. (i, j, k) are the coordinates of the
     * block and l is the block's subtype/damage.
     */
    public void harvestBlock(World var1, EntityPlayer var2, int var3, int var4, int var5, int var6)
    {
        Random var7 = new Random();
        byte var8 = 6;
        byte var9 = 9;
        long var10 = (long)var9 - (long)var8 + 1L;
        long var12 = (long)((double)var10 * var7.nextDouble());
        int var14 = (int)(var12 + (long)var8);
        int var15 = var7.nextInt(4);
        int var16;

        if (var15 == 0)
        {
            for (var16 = 1; var16 <= var14; ++var16)
            {
                var1.spawnEntityInWorld(new EntityXPOrb(var1, (double)var3, (double)var4, (double)var5, 1));
            }
        }
        else if (var15 == 1)
        {
            if (var7.nextInt(9) == 0)
            {
                this.dropBlockAsItem_do(var1, var3, var4, var5, new ItemStack(AetherItems.CandyCaneSword, 1));
            }
            else
            {
                for (var16 = 1; var16 <= var14; ++var16)
                {
                    this.dropBlockAsItem_do(var1, var3, var4, var5, new ItemStack(AetherItems.GingerBreadMan, 1));
                }
            }
        }
        else if (!var1.isRemote)
        {
            EntityTNTPresent var17 = new EntityTNTPresent(var1, (double)((float)var3 + 0.5F), (double)((float)var4 + 0.5F), (double)((float)var5 + 0.5F));
            var17.fuse = var1.rand.nextInt(var17.fuse / 4) + var17.fuse / 8;
            var1.spawnEntityInWorld(var17);
            var1.playSoundAtEntity(var17, "random.fuse", 1.0F, 1.0F);
        }
    }

    /**
     * Returns the ID of the items to drop on destruction.
     */
    public int idDropped(int var1, Random var2, int var3)
    {
        return 0;
    }

    public void onBlockPlaced(World var1, int var2, int var3, int var4, int var5)
    {
        Random var6 = new Random();
        this.fraction = (long)((double)this.range * var6.nextDouble());
        this.randomNumber = (int)(this.fraction + (long)this.randStart);
        this.crateType = var6.nextInt(4);
        this.rarity = var6.nextInt(9);
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    public int quantityDropped(Random var1)
    {
        return 0;
    }

    /**
     * Returns the usual quantity dropped by the block plus a bonus of 1 to 'i' (inclusive).
     */
    public int quantityDroppedWithBonus(int var1, Random var2)
    {
        return this.quantityDropped(var2);
    }

    /**
     * When this method is called, your block should register all the icons it needs with the given IconRegister. This
     * is the only chance you get to register icons.
     */
    public void registerIcons(IconRegister var1)
    {
        sprTop = var1.registerIcon("Aether:Present Top");
        sprSide = var1.registerIcon("Aether:Present Side");
        super.registerIcons(var1);
    }
}
