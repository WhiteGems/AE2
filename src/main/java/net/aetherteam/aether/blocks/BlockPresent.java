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

public class BlockPresent extends BlockAether
    implements IAetherBlock
{
    public static Icon sprTop;
    public static Icon sprSide;
    int rarity;
    int randStart = 6;
    int randEnd = 9;
    long range = this.randEnd - this.randStart + 1L;
    long fraction;
    int randomNumber;
    int crateType;

    public BlockPresent(int var1)
    {
        super(var1, Material.wood);
        setHardness(0.1F);
        setStepSound(Block.soundGrassFootstep);
    }

    public Icon getIcon(int var1, int meta)
    {
        return var1 == 1 ? sprTop : var1 == 0 ? sprTop : sprSide;
    }

    public void harvestBlock(World world, EntityPlayer entityplayer, int i, int j, int k, int l)
    {
        Random random = new Random();
        int randStart = 6;
        int randEnd = 9;
        long range = randEnd - randStart + 1L;
        long fraction = (long)(range * random.nextDouble());
        int randomNumber = (int)(fraction + randStart);
        int crateType = random.nextInt(4);

        if (crateType == 0)
        {
            for (int index = 1; index <= randomNumber; index++)
            {
                world.spawnEntityInWorld(new EntityXPOrb(world, i, j, k, 1));
            }
        }
        else if (crateType == 1)
        {
            if (random.nextInt(9) == 0)
            {
                dropBlockAsItem_do(world, i, j, k, new ItemStack(AetherItems.CandyCaneSword, 1));
            }
            else
            {
                for (int index = 1; index <= randomNumber; index++)
                {
                    dropBlockAsItem_do(world, i, j, k, new ItemStack(AetherItems.GingerBreadMan, 1));
                }
            }
        }
        else if (!world.isRemote)
        {
            EntityTNTPresent entitytntprimed = new EntityTNTPresent(world, i + 0.5F, j + 0.5F, k + 0.5F);
            entitytntprimed.fuse = (world.rand.nextInt(entitytntprimed.fuse / 4) + entitytntprimed.fuse / 8);
            world.spawnEntityInWorld(entitytntprimed);
            world.playSoundAtEntity(entitytntprimed, "random.fuse", 1.0F, 1.0F);
        }
    }

    public int idDropped(int i, Random random, int k)
    {
        return 0;
    }

    public void onBlockPlaced(World world, int i, int j, int k, int l)
    {
        Random random = new Random();
        this.fraction = ((long)(this.range * random.nextDouble()));
        this.randomNumber = ((int)(this.fraction + this.randStart));
        this.crateType = random.nextInt(4);
        this.rarity = random.nextInt(9);
    }

    public int quantityDropped(Random random)
    {
        return 0;
    }

    public int quantityDroppedWithBonus(int i, Random random)
    {
        return quantityDropped(random);
    }

    public void registerIcons(IconRegister par1IconRegister)
    {
        sprTop = par1IconRegister.registerIcon("Aether:Present Top");
        sprSide = par1IconRegister.registerIcon("Aether:Present Side");
        super.registerIcons(par1IconRegister);
    }
}

