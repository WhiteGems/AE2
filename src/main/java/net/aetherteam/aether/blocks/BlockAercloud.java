package net.aetherteam.aether.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import net.aetherteam.aether.Aether;
import net.aetherteam.playercore_api.cores.IPlayerCoreCommon;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
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
    private HashMap<String, Icon> icons = new HashMap();
    public static final String[] names = new String[] {"Cold Aercloud", "Blue Aercloud", "Golden Aercloud", "Green Aercloud", "Storm Aercloud"};
    public static final int bouncingMeta = 1;
    public static final int sinkingMeta = 2;
    public static final int randomMeta = 3;
    public static final int thunderMeta = 4;
    private Random rand = new Random();
    public static final int metaBeforePurple = 4;

    protected BlockAercloud(int blockID)
    {
        super(blockID, Material.ice);
        this.setHardness(0.2F);
        this.setLightOpacity(0);
        this.setStepSound(Block.soundClothFootstep);
        this.setTickRandomly(true);
    }

    public void addCreativeItems(ArrayList itemList)
    {
        for (int meta = 0; meta < 4; ++meta)
        {
            itemList.add(new ItemStack(this, 1, meta));
        }
    }

    /**
     * Determines the damage on the item the block drops. Used in cloth and wood.
     */
    public int damageDropped(int meta)
    {
        return meta <= 4 ? meta : 5;
    }

    /**
     * Ticks the block if it's been scheduled
     */
    public void updateTick(World world, int i, int j, int k, Random random)
    {
        if (!world.isRemote)
        {
            super.updateTick(world, i, j, k, random);

            if (world.getBlockMetadata(i, j, k) == 4 && random.nextInt(155) == 0 && world.getWorldInfo().isRaining())
            {
                world.addWeatherEffect(new EntityLightningBolt(world, (double)i, (double)(j + 1), (double)k));
            }
        }
    }

    /**
     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
     * cleared to be reused)
     */
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k)
    {
        return world.getBlockMetadata(i, j, k) != 1 && world.getBlockMetadata(i, j, k) != 2 && world.getBlockMetadata(i, j, k) < 5 ? AxisAlignedBB.getBoundingBox((double)i, (double)j, (double)k, (double)(i + 1), (double)j, (double)(k + 1)) : AxisAlignedBB.getBoundingBox((double)i, (double)j, (double)k, (double)i, (double)j, (double)k);
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
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity)
    {
        entity.fallDistance = 0.0F;
        int meta = world.getBlockMetadata(x, y, z);

        if (!entity.isRiding())
        {
            EntityPlayer rand;

            if (entity instanceof EntityPlayer)
            {
                rand = (EntityPlayer)entity;

                if (rand.isSneaking())
                {
                    if (entity.motionY < 0.0D)
                    {
                        entity.motionY *= 0.005D;
                    }

                    return;
                }
            }

            if (meta >= 5 && entity.posY <= (double)y + 1.6D && entity.posY >= (double)y - 0.2D)
            {
                entity.motionY = 0.1D;
            }

            if (meta != 5 && meta != 9)
            {
                if (meta != 6 && meta != 10)
                {
                    if (meta != 7 && meta != 11)
                    {
                        if (meta == 8 || meta == 12)
                        {
                            entity.motionX = -2.5D;
                        }
                    }
                    else
                    {
                        entity.motionZ = 2.5D;
                    }
                }
                else
                {
                    entity.motionX = 2.5D;
                }
            }
            else
            {
                entity.motionZ = -2.5D;
            }

            if (meta == 1)
            {
                entity.motionY = 2.0D;

                if (entity instanceof EntityLiving)
                {
                    Aether.proxy.spawnRainParticles(world, x, y, z, this.rand, 15);
                }
            }
            else if (meta == 2)
            {
                entity.motionY = -1.5D;
            }
            else if (meta == 3)
            {
                entity.motionX *= 5.0E-10D;
                entity.motionZ *= 5.0E-10D;

                if (entity instanceof EntityPlayer)
                {
                    rand = (EntityPlayer)entity;

                    if (!((IPlayerCoreCommon)rand).isJumping())
                    {
                        entity.setPosition((double)x + 0.5D, (double)((float)y + entity.height), (double)z + 0.5D);
                    }
                }
                else
                {
                    entity.setPosition((double)x + 0.5D, (double)y, (double)z + 0.5D);
                }

                Random rand1 = new Random();
                int chance = rand1.nextInt(4);

                if (chance == 0)
                {
                    entity.motionZ = -2.5D;
                }
                else if (chance == 1)
                {
                    entity.motionX = 2.5D;
                }
                else if (chance == 2)
                {
                    entity.motionZ = 2.5D;
                }
                else if (chance == 3)
                {
                    entity.motionX = -2.5D;
                }
            }
            else if (entity.motionY < 0.0D)
            {
                entity.motionY *= 0.005D;
            }

            if (!(entity instanceof EntityPlayer))
            {
                entity.fallDistance = -20.0F;
            }
        }
    }

    /**
     * Returns true if the given side of this block type should be rendered, if the adjacent block is at the given
     * coordinates.  Args: blockAccess, x, y, z, side
     */
    public boolean shouldSideBeRendered(IBlockAccess iblockaccess, int i, int j, int k, int l)
    {
        return super.shouldSideBeRendered(iblockaccess, i, j, k, 1 - l);
    }

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (int var4 = 0; var4 < 6; ++var4)
        {
            par3List.add(new ItemStack(par1, 1, var4));
        }
    }

    /**
     * Called whenever the block is added into the world. Args: world, x, y, z
     */
    public void onBlockAdded(World world, int par2, int par3, int par4)
    {
        super.onBlockAdded(world, par2, par3, par4);

        if (!world.isRemote && world.getBlockMetadata(par2, par3, par4) >= 9)
        {
            int left = world.getBlockId(par2 - 1, par3, par4);
            int right = world.getBlockId(par2 + 1, par3, par4);
            int front = world.getBlockId(par2, par3, par4 + 1);
            int behind = world.getBlockId(par2, par3, par4 - 1);
            byte meta = 0;

            if (Block.opaqueCubeLookup[left] && !Block.opaqueCubeLookup[right])
            {
                meta = 9;
            }

            if (Block.opaqueCubeLookup[right] && !Block.opaqueCubeLookup[left])
            {
                meta = 10;
            }

            if (Block.opaqueCubeLookup[front] && !Block.opaqueCubeLookup[behind])
            {
                meta = 11;
            }

            if (Block.opaqueCubeLookup[behind] && !Block.opaqueCubeLookup[front])
            {
                meta = 12;
            }

            if (meta > 0)
            {
                world.setBlockMetadataWithNotify(par2, par3, par4, meta, 16);
            }
        }
    }

    /**
     * Called when the block is placed in the world.
     */
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack itemStack)
    {
        int facing = MathHelper.floor_double((double)(entityLiving.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

        if (((EntityPlayer)entityLiving).inventory.getCurrentItem().getItemDamage() >= 5)
        {
            switch (facing)
            {
                case 0:
                    world.setBlockMetadataWithNotify(x, y, z, 9, 16);
                    break;

                case 1:
                    world.setBlockMetadataWithNotify(x, y, z, 10, 16);
                    break;

                case 2:
                    world.setBlockMetadataWithNotify(x, y, z, 11, 16);
                    break;

                case 3:
                    world.setBlockMetadataWithNotify(x, y, z, 12, 16);
            }
        }
    }

    @SideOnly(Side.CLIENT)

    /**
     * A randomly called display update to be able to add particles or other items for display
     */
    public void randomDisplayTick(World world, int x, int y, int z, Random random)
    {
        float x1 = (float)x + 0.5F;
        float y1 = (float)y + 0.0F + random.nextFloat() * 10.0F / 16.0F;
        float z1 = (float)z + 0.5F;
        int meta = world.getBlockMetadata(x, y, z);
        float i = random.nextFloat() * 0.9F;
        float j = random.nextFloat() * 0.2F;

        if (meta != 5 && meta != 9)
        {
            if (meta != 6 && meta != 10)
            {
                if (meta != 7 && meta != 11)
                {
                    if (meta == 8 || meta == 12)
                    {
                        world.spawnParticle("reddust", (double)(x1 - i), (double)y1, (double)(z1 + j), 1.0D, 1.0D, 1.0D);
                        world.spawnParticle("reddust", (double)(x1 - i * random.nextFloat() / 0.4F), (double)y1, (double)(z1 + j), 1.0D, 1.0D, 1.0D);
                    }
                }
                else
                {
                    world.spawnParticle("reddust", (double)(x1 + j), (double)y1, (double)(z1 + i), 1.0D, 1.0D, 1.0D);
                    world.spawnParticle("reddust", (double)(x1 + j * random.nextFloat() / 0.4F), (double)y1, (double)(z1 + i), 1.0D, 1.0D, 1.0D);
                }
            }
            else
            {
                world.spawnParticle("reddust", (double)(x1 + i), (double)y1, (double)(z1 + j), 1.0D, 1.0D, 1.0D);
                world.spawnParticle("reddust", (double)(x1 + i * random.nextFloat() / 0.4F), (double)y1, (double)(z1 + j), 1.0D, 1.0D, 1.0D);
            }
        }
        else
        {
            world.spawnParticle("reddust", (double)(x1 + j), (double)y1, (double)(z1 - i), 1.0D, 1.0D, 1.0D);
            world.spawnParticle("reddust", (double)(x1 + j * random.nextFloat() / 0.4F), (double)y1, (double)(z1 - i), 1.0D, 1.0D, 1.0D);
        }
    }

    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public Icon getIcon(int side, int meta)
    {
        if (meta == 5 || meta == 9)
        {
            switch (side)
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

        if (meta == 6 || meta == 10)
        {
            switch (side)
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

        if (meta == 7 || meta == 11)
        {
            switch (side)
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

        if (meta == 8 || meta == 12)
        {
            switch (side)
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

        ItemStack stack = new ItemStack(AetherBlocks.Aercloud, 1, meta);
        String name = stack.getItem().getItemDisplayName(stack);
        return (Icon)this.icons.get(name);
    }

    /**
     * Called upon block activation (right click on the block.)
     */
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int par6, float par7, float par8, float par9)
    {
        world.getBlockMetadata(x, y, z);
        return false;
    }

    /**
     * When this method is called, your block should register all the icons it needs with the given IconRegister. This
     * is the only chance you get to register icons.
     */
    public void registerIcons(IconRegister iconRegister)
    {
        this.frontTexture = iconRegister.registerIcon("aether:Purple Aercloud Front");
        this.backTexture = iconRegister.registerIcon("aether:Purple Aercloud Back");
        this.upArrow = iconRegister.registerIcon("aether:Purple Aercloud Up");
        this.downArrow = iconRegister.registerIcon("aether:Purple Aercloud Down");
        this.leftArrow = iconRegister.registerIcon("aether:Purple Aercloud Left");
        this.rightArrow = iconRegister.registerIcon("aether:Purple Aercloud Right");

        for (String name : names)
        {
            this.icons.put(name, iconRegister.registerIcon("aether:" + name));
        }
    }
}
