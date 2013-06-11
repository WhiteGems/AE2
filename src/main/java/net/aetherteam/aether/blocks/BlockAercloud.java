package net.aetherteam.aether.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import net.aetherteam.aether.Aether;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockAercloud extends BlockAether implements IAetherBlock
{
    private Icon backTexture;
    private Icon frontTexture;
    private Icon leftArrow;
    private Icon rightArrow;
    private Icon upArrow;
    private Icon downArrow;
    private HashMap icons = new HashMap();
    public static final String[] names = new String[]{"Cold Aercloud", "Blue Aercloud", "Golden Aercloud", "Green Aercloud", "Storm Aercloud"};
    public static final int bouncingMeta = 1;
    public static final int sinkingMeta = 2;
    public static final int randomMeta = 3;
    public static final int thunderMeta = 4;
    private Random rand = new Random();
    public static final int metaBeforePurple = 4;

    protected BlockAercloud(int var1)
    {
        super(var1, Material.ice);
        this.setHardness(0.2F);
        this.setLightOpacity(0);
        this.setStepSound(Block.soundClothFootstep);
        this.setTickRandomly(true);
    }

    public void addCreativeItems(ArrayList var1)
    {
        for (int var2 = 0; var2 < 4; ++var2)
        {
            var1.add(new ItemStack(this, 1, var2));
        }
    }

    /**
     * Determines the damage on the item the block drops. Used in cloth and wood.
     */
    public int damageDropped(int var1)
    {
        return var1 <= 4 ? var1 : 5;
    }

    /**
     * Ticks the block if it's been scheduled
     */
    public void updateTick(World var1, int var2, int var3, int var4, Random var5)
    {
        if (!var1.isRemote)
        {
            super.updateTick(var1, var2, var3, var4, var5);

            if (var1.getBlockMetadata(var2, var3, var4) == 4 && var5.nextInt(155) == 0 && var1.getWorldInfo().isRaining())
            {
                var1.addWeatherEffect(new EntityLightningBolt(var1, (double) var2, (double) (var3 + 1), (double) var4));
            }
        }
    }

    /**
     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
     * cleared to be reused)
     */
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World var1, int var2, int var3, int var4)
    {
        return var1.getBlockMetadata(var2, var3, var4) != 1 && var1.getBlockMetadata(var2, var3, var4) != 2 && var1.getBlockMetadata(var2, var3, var4) < 5 ? AxisAlignedBB.getBoundingBox((double) var2, (double) var3, (double) var4, (double) (var2 + 1), (double) var3, (double) (var4 + 1)) : AxisAlignedBB.getBoundingBox((double) var2, (double) var3, (double) var4, (double) var2, (double) var3, (double) var4);
    }

    /**
     * Returns which pass should this block be rendered on. 0 for solids and 1 for alpha
     */
    public int getRenderBlockPass()
    {
        return 1;
    }

    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    public boolean isOpaqueCube()
    {
        return false;
    }

    /**
     * Triggered whenever an entity collides with this block (enters into the block). Args: world, x, y, z, entity
     */
    public void onEntityCollidedWithBlock(World var1, int var2, int var3, int var4, Entity var5)
    {
        var5.fallDistance = 0.0F;
        int var6 = var1.getBlockMetadata(var2, var3, var4);

        if (!var5.isRiding())
        {
            EntityPlayer var7;

            if (var5 instanceof EntityPlayer)
            {
                var7 = (EntityPlayer) var5;

                if (var7.isSneaking())
                {
                    if (var5.motionY < 0.0D)
                    {
                        var5.motionY *= 0.005D;
                    }

                    return;
                }
            }

            if (var6 >= 5 && var5.posY <= (double) var3 + 1.6D && var5.posY >= (double) var3 - 0.2D)
            {
                var5.motionY = 0.1D;
            }

            if (var6 != 5 && var6 != 9)
            {
                if (var6 != 6 && var6 != 10)
                {
                    if (var6 != 7 && var6 != 11)
                    {
                        if (var6 == 8 || var6 == 12)
                        {
                            var5.motionX = -2.5D;
                        }
                    } else
                    {
                        var5.motionZ = 2.5D;
                    }
                } else
                {
                    var5.motionX = 2.5D;
                }
            } else
            {
                var5.motionZ = -2.5D;
            }

            if (var6 == 1)
            {
                var5.motionY = 2.0D;

                if (var5 instanceof EntityLiving)
                {
                    Aether.proxy.spawnRainParticles(var1, var2, var3, var4, this.rand, 15);
                }
            } else if (var6 == 2)
            {
                var5.motionY = -1.5D;
            } else if (var6 == 3)
            {
                var5.motionX *= 5.0E-10D;
                var5.motionZ *= 5.0E-10D;

                if (var5 instanceof EntityPlayer)
                {
                    var7 = (EntityPlayer) var5;

                    if (!var7.isJumping)
                    {
                        var5.setPosition((double) var2 + 0.5D, (double) ((float) var3 + var5.height), (double) var4 + 0.5D);
                    }
                } else
                {
                    var5.setPosition((double) var2 + 0.5D, (double) var3, (double) var4 + 0.5D);
                }

                Random var9 = new Random();
                int var8 = var9.nextInt(4);

                if (var8 == 0)
                {
                    var5.motionZ = -2.5D;
                } else if (var8 == 1)
                {
                    var5.motionX = 2.5D;
                } else if (var8 == 2)
                {
                    var5.motionZ = 2.5D;
                } else if (var8 == 3)
                {
                    var5.motionX = -2.5D;
                }
            } else if (var5.motionY < 0.0D)
            {
                var5.motionY *= 0.005D;
            }

            if (!(var5 instanceof EntityPlayer))
            {
                var5.fallDistance = -20.0F;
            }
        }
    }

    /**
     * Returns true if the given side of this block type should be rendered, if the adjacent block is at the given
     * coordinates.  Args: blockAccess, x, y, z, side
     */
    public boolean shouldSideBeRendered(IBlockAccess var1, int var2, int var3, int var4, int var5)
    {
        return super.shouldSideBeRendered(var1, var2, var3, var4, 1 - var5);
    }

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    public void getSubBlocks(int var1, CreativeTabs var2, List var3)
    {
        for (int var4 = 0; var4 < 6; ++var4)
        {
            var3.add(new ItemStack(var1, 1, var4));
        }
    }

    /**
     * Called whenever the block is added into the world. Args: world, x, y, z
     */
    public void onBlockAdded(World var1, int var2, int var3, int var4)
    {
        super.onBlockAdded(var1, var2, var3, var4);

        if (!var1.isRemote && var1.getBlockMetadata(var2, var3, var4) >= 9)
        {
            int var5 = var1.getBlockId(var2 - 1, var3, var4);
            int var6 = var1.getBlockId(var2 + 1, var3, var4);
            int var7 = var1.getBlockId(var2, var3, var4 + 1);
            int var8 = var1.getBlockId(var2, var3, var4 - 1);
            byte var9 = 0;

            if (Block.opaqueCubeLookup[var5] && !Block.opaqueCubeLookup[var6])
            {
                var9 = 9;
            }

            if (Block.opaqueCubeLookup[var6] && !Block.opaqueCubeLookup[var5])
            {
                var9 = 10;
            }

            if (Block.opaqueCubeLookup[var7] && !Block.opaqueCubeLookup[var8])
            {
                var9 = 11;
            }

            if (Block.opaqueCubeLookup[var8] && !Block.opaqueCubeLookup[var7])
            {
                var9 = 12;
            }

            if (var9 > 0)
            {
                var1.setBlockMetadataWithNotify(var2, var3, var4, var9, 16);
            }
        }
    }

    /**
     * Called when the block is placed in the world.
     */
    public void onBlockPlacedBy(World var1, int var2, int var3, int var4, EntityLiving var5, ItemStack var6)
    {
        int var7 = MathHelper.floor_double((double) (var5.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

        if (((EntityPlayer) var5).inventory.getCurrentItem().getItemDamage() >= 5)
        {
            switch (var7)
            {
                case 0:
                    var1.setBlockMetadataWithNotify(var2, var3, var4, 9, 16);
                    break;

                case 1:
                    var1.setBlockMetadataWithNotify(var2, var3, var4, 10, 16);
                    break;

                case 2:
                    var1.setBlockMetadataWithNotify(var2, var3, var4, 11, 16);
                    break;

                case 3:
                    var1.setBlockMetadataWithNotify(var2, var3, var4, 12, 16);
            }
        }
    }

    @SideOnly(Side.CLIENT)

    /**
     * A randomly called display update to be able to add particles or other items for display
     */
    public void randomDisplayTick(World var1, int var2, int var3, int var4, Random var5)
    {
        float var6 = (float) var2 + 0.5F;
        float var7 = (float) var3 + 0.0F + var5.nextFloat() * 10.0F / 16.0F;
        float var8 = (float) var4 + 0.5F;
        int var9 = var1.getBlockMetadata(var2, var3, var4);
        float var10 = var5.nextFloat() * 0.9F;
        float var11 = var5.nextFloat() * 0.2F;

        if (var9 != 5 && var9 != 9)
        {
            if (var9 != 6 && var9 != 10)
            {
                if (var9 != 7 && var9 != 11)
                {
                    if (var9 == 8 || var9 == 12)
                    {
                        var1.spawnParticle("reddust", (double) (var6 - var10), (double) var7, (double) (var8 + var11), 1.0D, 1.0D, 1.0D);
                        var1.spawnParticle("reddust", (double) (var6 - var10 * var5.nextFloat() / 0.4F), (double) var7, (double) (var8 + var11), 1.0D, 1.0D, 1.0D);
                    }
                } else
                {
                    var1.spawnParticle("reddust", (double) (var6 + var11), (double) var7, (double) (var8 + var10), 1.0D, 1.0D, 1.0D);
                    var1.spawnParticle("reddust", (double) (var6 + var11 * var5.nextFloat() / 0.4F), (double) var7, (double) (var8 + var10), 1.0D, 1.0D, 1.0D);
                }
            } else
            {
                var1.spawnParticle("reddust", (double) (var6 + var10), (double) var7, (double) (var8 + var11), 1.0D, 1.0D, 1.0D);
                var1.spawnParticle("reddust", (double) (var6 + var10 * var5.nextFloat() / 0.4F), (double) var7, (double) (var8 + var11), 1.0D, 1.0D, 1.0D);
            }
        } else
        {
            var1.spawnParticle("reddust", (double) (var6 + var11), (double) var7, (double) (var8 - var10), 1.0D, 1.0D, 1.0D);
            var1.spawnParticle("reddust", (double) (var6 + var11 * var5.nextFloat() / 0.4F), (double) var7, (double) (var8 - var10), 1.0D, 1.0D, 1.0D);
        }
    }

    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public Icon getIcon(int var1, int var2)
    {
        if (var2 == 5 || var2 == 9)
        {
            switch (var1)
            {
                case 0:
                    return this.upArrow;

                case 1:
                    return this.upArrow;

                case 2:
                    return this.frontTexture;

                case 3:
                    return this.backTexture;

                case 4:
                    return this.leftArrow;

                case 5:
                    return this.rightArrow;
            }
        }

        if (var2 == 6 || var2 == 10)
        {
            switch (var1)
            {
                case 0:
                    return this.rightArrow;

                case 1:
                    return this.rightArrow;

                case 2:
                    return this.leftArrow;

                case 3:
                    return this.rightArrow;

                case 4:
                    return this.backTexture;

                case 5:
                    return this.frontTexture;
            }
        }

        if (var2 == 7 || var2 == 11)
        {
            switch (var1)
            {
                case 0:
                    return this.downArrow;

                case 1:
                    return this.downArrow;

                case 2:
                    return this.backTexture;

                case 3:
                    return this.frontTexture;

                case 4:
                    return this.rightArrow;

                case 5:
                    return this.leftArrow;
            }
        }

        if (var2 == 8 || var2 == 12)
        {
            switch (var1)
            {
                case 0:
                    return this.leftArrow;

                case 1:
                    return this.leftArrow;

                case 2:
                    return this.rightArrow;

                case 3:
                    return this.leftArrow;

                case 4:
                    return this.frontTexture;

                case 5:
                    return this.backTexture;
            }
        }

        ItemStack var3 = new ItemStack(AetherBlocks.Aercloud, 1, var2);
        String var4 = var3.getItem().getItemDisplayName(var3);
        return (Icon) this.icons.get(var4);
    }

    /**
     * Called upon block activation (right click on the block.)
     */
    public boolean onBlockActivated(World var1, int var2, int var3, int var4, EntityPlayer var5, int var6, float var7, float var8, float var9)
    {
        var1.getBlockMetadata(var2, var3, var4);
        return false;
    }

    /**
     * When this method is called, your block should register all the icons it needs with the given IconRegister. This
     * is the only chance you get to register icons.
     */
    public void registerIcons(IconRegister var1)
    {
        this.frontTexture = var1.registerIcon("Aether:Purple Aercloud Front");
        this.backTexture = var1.registerIcon("Aether:Purple Aercloud Back");
        this.upArrow = var1.registerIcon("Aether:Purple Aercloud Up");
        this.downArrow = var1.registerIcon("Aether:Purple Aercloud Down");
        this.leftArrow = var1.registerIcon("Aether:Purple Aercloud Left");
        this.rightArrow = var1.registerIcon("Aether:Purple Aercloud Right");

        for (int var2 = 0; var2 < names.length; ++var2)
        {
            this.icons.put(names[var2], var1.registerIcon("Aether:" + names[var2]));
        }
    }
}
