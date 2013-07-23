package net.aetherteam.aether.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import net.aetherteam.aether.Aether;
import net.aetherteam.aether.CommonProxy;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldInfo;

public class BlockAercloud extends BlockAether
    implements IAetherBlock
{
    private Icon backTexture;
    private Icon frontTexture;
    private Icon leftArrow;
    private Icon rightArrow;
    private Icon upArrow;
    private Icon downArrow;
    private HashMap icons = new HashMap();
    public static final String[] names = { "Cold Aercloud", "Blue Aercloud", "Golden Aercloud", "Green Aercloud", "Storm Aercloud" };
    public static final int bouncingMeta = 1;
    public static final int sinkingMeta = 2;
    public static final int randomMeta = 3;
    public static final int thunderMeta = 4;
    private Random rand = new Random();
    public static final int metaBeforePurple = 4;

    protected BlockAercloud(int blockID)
    {
        super(blockID, Material.ice);
        setHardness(0.2F);
        setLightOpacity(0);
        setStepSound(Block.soundClothFootstep);
        setTickRandomly(true);
    }

    public void addCreativeItems(ArrayList itemList)
    {
        for (int meta = 0; meta < 4; meta++)
        {
            itemList.add(new ItemStack(this, 1, meta));
        }
    }

    public int damageDropped(int meta)
    {
        if (meta <= 4)
        {
            return meta;
        }

        return 5;
    }

    public void updateTick(World world, int i, int j, int k, Random random)
    {
        if (world.isRemote)
        {
            return;
        }

        super.updateTick(world, i, j, k, random);

        if ((world.getBlockMetadata(i, j, k) == 4) && (random.nextInt(155) == 0) && (world.M().isRaining()))
        {
            world.addWeatherEffect(new EntityLightningBolt(world, i, j + 1, k));
        }
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k)
    {
        if ((world.getBlockMetadata(i, j, k) == 1) || (world.getBlockMetadata(i, j, k) == 2) || (world.getBlockMetadata(i, j, k) >= 5))
        {
            return AxisAlignedBB.getBoundingBox(i, j, k, i, j, k);
        }

        return AxisAlignedBB.getBoundingBox(i, j, k, i + 1, j, k + 1);
    }

    public int getRenderBlockPass()
    {
        return 1;
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity)
    {
        entity.fallDistance = 0.0F;
        int meta = world.getBlockMetadata(x, y, z);

        if (!entity.isRiding())
        {
            if ((entity instanceof EntityPlayer))
            {
                EntityPlayer player = (EntityPlayer)entity;

                if (player.isSneaking())
                {
                    if (entity.motionY < 0.0D)
                    {
                        entity.motionY *= 0.005D;
                    }

                    return;
                }
            }

            if ((meta >= 5) && (entity.posY <= y + 1.6D) && (entity.posY >= y - 0.2D))
            {
                entity.motionY = 0.1D;
            }

            if ((meta == 5) || (meta == 9))
            {
                entity.motionZ = -2.5D;
            }
            else if ((meta == 6) || (meta == 10))
            {
                entity.motionX = 2.5D;
            }
            else if ((meta == 7) || (meta == 11))
            {
                entity.motionZ = 2.5D;
            }
            else if ((meta == 8) || (meta == 12))
            {
                entity.motionX = -2.5D;
            }

            if (meta == 1)
            {
                entity.motionY = 2.0D;

                if ((entity instanceof EntityLiving))
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
                entity.motionX *= 5.E-010D;
                entity.motionZ *= 5.E-010D;

                if ((entity instanceof EntityPlayer))
                {
                    EntityPlayer player = (EntityPlayer)entity;

                    if (!player.isJumping)
                    {
                        entity.setPosition(x + 0.5D, y + entity.height, z + 0.5D);
                    }
                }
                else
                {
                    entity.setPosition(x + 0.5D, y, z + 0.5D);
                }

                Random rand = new Random();
                int chance = rand.nextInt(4);

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

    public boolean shouldSideBeRendered(IBlockAccess iblockaccess, int i, int j, int k, int l)
    {
        return super.shouldSideBeRendered(iblockaccess, i, j, k, 1 - l);
    }

    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (int var4 = 0; var4 < 6; var4++)
        {
            par3List.add(new ItemStack(par1, 1, var4));
        }
    }

    public void onBlockAdded(World world, int par2, int par3, int par4)
    {
        super.onBlockAdded(world, par2, par3, par4);

        if ((!world.isRemote) && (world.getBlockMetadata(par2, par3, par4) >= 9))
        {
            int left = world.getBlockId(par2 - 1, par3, par4);
            int right = world.getBlockId(par2 + 1, par3, par4);
            int front = world.getBlockId(par2, par3, par4 + 1);
            int behind = world.getBlockId(par2, par3, par4 - 1);
            byte meta = 0;

            if ((Block.opaqueCubeLookup[left] != 0) && (Block.opaqueCubeLookup[right] == 0))
            {
                meta = 9;
            }

            if ((Block.opaqueCubeLookup[right] != 0) && (Block.opaqueCubeLookup[left] == 0))
            {
                meta = 10;
            }

            if ((Block.opaqueCubeLookup[front] != 0) && (Block.opaqueCubeLookup[behind] == 0))
            {
                meta = 11;
            }

            if ((Block.opaqueCubeLookup[behind] != 0) && (Block.opaqueCubeLookup[front] == 0))
            {
                meta = 12;
            }

            if (meta > 0)
            {
                world.setBlockMetadataWithNotify(par2, par3, par4, meta, 16);
            }
        }
    }

    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving entityLiving, ItemStack itemStack)
    {
        int facing = MathHelper.floor_double(entityLiving.rotationYaw * 4.0F / 360.0F + 0.5D) & 0x3;

        if (((EntityPlayer)entityLiving).inventory.getCurrentItem().getItemDamage() >= 5)
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

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random random)
    {
        float x1 = x + 0.5F;
        float y1 = y + 0.0F + random.nextFloat() * 10.0F / 16.0F;
        float z1 = z + 0.5F;
        int meta = world.getBlockMetadata(x, y, z);
        float i = random.nextFloat() * 0.9F;
        float j = random.nextFloat() * 0.2F;

        if ((meta == 5) || (meta == 9))
        {
            world.spawnParticle("reddust", x1 + j, y1, z1 - i, 1.0D, 1.0D, 1.0D);
            world.spawnParticle("reddust", x1 + j * random.nextFloat() / 0.4F, y1, z1 - i, 1.0D, 1.0D, 1.0D);
        }
        else if ((meta == 6) || (meta == 10))
        {
            world.spawnParticle("reddust", x1 + i, y1, z1 + j, 1.0D, 1.0D, 1.0D);
            world.spawnParticle("reddust", x1 + i * random.nextFloat() / 0.4F, y1, z1 + j, 1.0D, 1.0D, 1.0D);
        }
        else if ((meta == 7) || (meta == 11))
        {
            world.spawnParticle("reddust", x1 + j, y1, z1 + i, 1.0D, 1.0D, 1.0D);
            world.spawnParticle("reddust", x1 + j * random.nextFloat() / 0.4F, y1, z1 + i, 1.0D, 1.0D, 1.0D);
        }
        else if ((meta == 8) || (meta == 12))
        {
            world.spawnParticle("reddust", x1 - i, y1, z1 + j, 1.0D, 1.0D, 1.0D);
            world.spawnParticle("reddust", x1 - i * random.nextFloat() / 0.4F, y1, z1 + j, 1.0D, 1.0D, 1.0D);
        }
    }

    public Icon getIcon(int side, int meta)
    {
        if ((meta == 5) || (meta == 9))
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

        if ((meta == 6) || (meta == 10))
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

        if ((meta == 7) || (meta == 11))
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

        if ((meta == 8) || (meta == 12))
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

    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int par6, float par7, float par8, float par9)
    {
        int meta = world.getBlockMetadata(x, y, z);
        return false;
    }

    public void registerIcons(IconRegister iconRegister)
    {
        this.frontTexture = iconRegister.registerIcon("Aether:Purple Aercloud Front");
        this.backTexture = iconRegister.registerIcon("Aether:Purple Aercloud Back");
        this.upArrow = iconRegister.registerIcon("Aether:Purple Aercloud Up");
        this.downArrow = iconRegister.registerIcon("Aether:Purple Aercloud Down");
        this.leftArrow = iconRegister.registerIcon("Aether:Purple Aercloud Left");
        this.rightArrow = iconRegister.registerIcon("Aether:Purple Aercloud Right");

        for (int i = 0; i < names.length; i++)
        {
            this.icons.put(names[i], iconRegister.registerIcon("Aether:" + names[i]));
        }
    }
}

