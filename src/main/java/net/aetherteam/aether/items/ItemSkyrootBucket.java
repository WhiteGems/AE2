package net.aetherteam.aether.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.aetherteam.aether.entities.EntityAechorPlant;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class ItemSkyrootBucket extends ItemAether
{
    public static final String[] names = new String[] {"Skyroot Bucket", "Skyroot Milk Bucket", "Skyroot Poison Bucket", "Skyroot Remedy Bucket", "Skyroot Water Bucket"};
    @SideOnly(Side.CLIENT)
    private Icon[] icons;
    private int waterMoving = 4;

    public ItemSkyrootBucket(int i)
    {
        super(i);
        this.setHasSubtypes(true);
        this.maxStackSize = 16;
    }

    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List itemList)
    {
        itemList.add(new ItemStack(this, 1, 0));
        itemList.add(new ItemStack(this, 1, 1));
        itemList.add(new ItemStack(this, 1, 2));
        itemList.add(new ItemStack(this, 1, 3));
        itemList.add(new ItemStack(this, 1, this.waterMoving));
    }

    /**
     * Gets an icon index based on an item's damage value
     */
    public Icon getIconFromDamage(int damage)
    {
        return this.icons[damage];
    }

    /**
     * Returns the unlocalized name of this item. This version accepts an ItemStack so different stacks can have
     * different names based on their damage or NBT.
     */
    public String getUnlocalizedName(ItemStack par1ItemStack)
    {
        int var2 = MathHelper.clamp_int(par1ItemStack.getItemDamage(), 0, names.length - 1);
        return super.getUnlocalizedName() + "." + names[var2];
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.icons = new Icon[names.length];

        for (int i = 0; i < names.length; ++i)
        {
            this.icons[i] = par1IconRegister.registerIcon("aether:" + names[i]);
        }
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        float f = 1.0F;
        float f1 = entityplayer.prevRotationPitch + (entityplayer.rotationPitch - entityplayer.prevRotationPitch) * f;
        float f2 = entityplayer.prevRotationYaw + (entityplayer.rotationYaw - entityplayer.prevRotationYaw) * f;
        double d = entityplayer.prevPosX + (entityplayer.posX - entityplayer.prevPosX) * (double)f;
        double d1 = entityplayer.prevPosY + (entityplayer.posY - entityplayer.prevPosY) * (double)f + 1.62D - (double)entityplayer.yOffset;
        double d2 = entityplayer.prevPosZ + (entityplayer.posZ - entityplayer.prevPosZ) * (double)f;
        Vec3 vec3d = Vec3.createVectorHelper(d, d1, d2);
        float f3 = MathHelper.cos(-f2 * 0.01745329F - (float)Math.PI);
        float f4 = MathHelper.sin(-f2 * 0.01745329F - (float)Math.PI);
        float f5 = -MathHelper.cos(-f1 * 0.01745329F);
        float f6 = MathHelper.sin(-f1 * 0.01745329F);
        float f7 = f4 * f5;
        float f9 = f3 * f5;
        double d3 = 5.0D;
        Vec3 vec3d1 = vec3d.addVector((double)f7 * d3, (double)f6 * d3, (double)f9 * d3);
        MovingObjectPosition movingobjectposition = world.clip(vec3d, vec3d1, itemstack.getItemDamage() == 0);

        if (itemstack.getItemDamage() == 2 && (movingobjectposition == null || movingobjectposition.entityHit == null || !(movingobjectposition.entityHit instanceof EntityAechorPlant)))
        {
            if (!world.isRemote)
            {
                entityplayer.addPotionEffect(new PotionEffect(Potion.poison.id, 200, 0));
                entityplayer.addPotionEffect(new PotionEffect(Potion.confusion.id, 200, 3));
            }

            itemstack.setItemDamage(0);
            return itemstack;
        }
        else if (itemstack.getItemDamage() == 3)
        {
            if (!world.isRemote)
            {
                entityplayer.curePotionEffects(new ItemStack(Item.bucketMilk));
            }

            return new ItemStack(AetherItems.SkyrootBucket);
        }
        else
        {
            if (movingobjectposition != null && movingobjectposition.typeOfHit == EnumMovingObjectType.TILE && (itemstack.getItemDamage() == 0 || itemstack.getItemDamage() == this.waterMoving))
            {
                int i = movingobjectposition.blockX;
                int j = movingobjectposition.blockY;
                int k = movingobjectposition.blockZ;

                if (!world.canMineBlock(entityplayer, i, j, k))
                {
                    return itemstack;
                }

                if (itemstack.getItemDamage() == 0)
                {
                    if (world.getBlockMaterial(i, j, k) == Material.water && world.getBlockMetadata(i, j, k) == 0)
                    {
                        world.setBlock(i, j, k, 0);
                        itemstack.setItemDamage(this.waterMoving);
                        return itemstack;
                    }
                }
                else
                {
                    if (itemstack.getItemDamage() <= 3 && itemstack.getItemDamage() != 0)
                    {
                        return new ItemStack(AetherItems.SkyrootBucket);
                    }

                    if (movingobjectposition.sideHit == 0)
                    {
                        --j;
                    }

                    if (movingobjectposition.sideHit == 1)
                    {
                        ++j;
                    }

                    if (movingobjectposition.sideHit == 2)
                    {
                        --k;
                    }

                    if (movingobjectposition.sideHit == 3)
                    {
                        ++k;
                    }

                    if (movingobjectposition.sideHit == 4)
                    {
                        --i;
                    }

                    if (movingobjectposition.sideHit == 5)
                    {
                        ++i;
                    }

                    if (world.isAirBlock(i, j, k) || !world.getBlockMaterial(i, j, k).isSolid())
                    {
                        if (world.provider.isHellWorld && itemstack.getItemDamage() == this.waterMoving)
                        {
                            world.playSoundEffect(d + 0.5D, d1 + 0.5D, d2 + 0.5D, "random.fizz", 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);

                            for (int l = 0; l < 8; ++l)
                            {
                                world.spawnParticle("largesmoke", (double)i + Math.random(), (double)j + Math.random(), (double)k + Math.random(), 0.0D, 0.0D, 0.0D);
                            }
                        }
                        else
                        {
                            world.setBlock(i, j, k, Block.waterMoving.blockID, 0, 4);
                        }

                        return new ItemStack(AetherItems.SkyrootBucket);
                    }
                }
            }
            else if (itemstack.getItemDamage() == 0 && movingobjectposition != null && movingobjectposition.entityHit != null && movingobjectposition.entityHit instanceof EntityCow)
            {
                itemstack.setItemDamage(1);
                return itemstack;
            }

            return itemstack;
        }
    }
}
