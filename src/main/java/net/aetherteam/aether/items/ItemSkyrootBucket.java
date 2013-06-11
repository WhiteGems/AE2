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
    public static final String[] names = new String[]{"Skyroot Bucket", "Skyroot Milk Bucket", "Skyroot Poison Bucket", "Skyroot Remedy Bucket", "Skyroot Water Bucket"};
    @SideOnly(Side.CLIENT)
    private Icon[] icons;
    private int waterMoving = 4;

    public ItemSkyrootBucket(int var1)
    {
        super(var1);
        this.setHasSubtypes(true);
        this.maxStackSize = 16;
    }

    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    public void getSubItems(int var1, CreativeTabs var2, List var3)
    {
        var3.add(new ItemStack(this, 1, 0));
        var3.add(new ItemStack(this, 1, 1));
        var3.add(new ItemStack(this, 1, 2));
        var3.add(new ItemStack(this, 1, 3));
        var3.add(new ItemStack(this, 1, this.waterMoving));
    }

    public Item setIconName(String var1)
    {
        return this.setUnlocalizedName("Aether:" + var1);
    }

    /**
     * Gets an icon index based on an item's damage value
     */
    public Icon getIconFromDamage(int var1)
    {
        return this.icons[var1];
    }

    /**
     * Returns the unlocalized name of this item. This version accepts an ItemStack so different stacks can have
     * different names based on their damage or NBT.
     */
    public String getUnlocalizedName(ItemStack var1)
    {
        int var2 = MathHelper.clamp_int(var1.getItemDamage(), 0, names.length - 1);
        return super.getUnlocalizedName() + "." + names[var2];
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister var1)
    {
        this.icons = new Icon[names.length];

        for (int var2 = 0; var2 < names.length; ++var2)
        {
            this.icons[var2] = var1.registerIcon("Aether:" + names[var2]);
        }
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack var1, World var2, EntityPlayer var3)
    {
        float var4 = 1.0F;
        float var5 = var3.prevRotationPitch + (var3.rotationPitch - var3.prevRotationPitch) * var4;
        float var6 = var3.prevRotationYaw + (var3.rotationYaw - var3.prevRotationYaw) * var4;
        double var7 = var3.prevPosX + (var3.posX - var3.prevPosX) * (double) var4;
        double var9 = var3.prevPosY + (var3.posY - var3.prevPosY) * (double) var4 + 1.62D - (double) var3.yOffset;
        double var11 = var3.prevPosZ + (var3.posZ - var3.prevPosZ) * (double) var4;
        Vec3 var13 = Vec3.createVectorHelper(var7, var9, var11);
        float var14 = MathHelper.cos(-var6 * 0.01745329F - (float) Math.PI);
        float var15 = MathHelper.sin(-var6 * 0.01745329F - (float) Math.PI);
        float var16 = -MathHelper.cos(-var5 * 0.01745329F);
        float var17 = MathHelper.sin(-var5 * 0.01745329F);
        float var18 = var15 * var16;
        float var20 = var14 * var16;
        double var21 = 5.0D;
        Vec3 var23 = var13.addVector((double) var18 * var21, (double) var17 * var21, (double) var20 * var21);
        MovingObjectPosition var24 = var2.rayTraceBlocks_do(var13, var23, var1.getItemDamage() == 0);

        if (var1.getItemDamage() == 2 && (var24 == null || var24.entityHit == null || !(var24.entityHit instanceof EntityAechorPlant)))
        {
            if (!var2.isRemote)
            {
                var3.addPotionEffect(new PotionEffect(Potion.poison.id, 200, 0));
                var3.addPotionEffect(new PotionEffect(Potion.confusion.id, 200, 3));
            }

            var1.setItemDamage(0);
            return var1;
        } else if (var1.getItemDamage() == 3)
        {
            if (!var2.isRemote)
            {
                var3.curePotionEffects(new ItemStack(Item.bucketMilk));
            }

            return new ItemStack(AetherItems.SkyrootBucket);
        } else
        {
            if (var24 != null && var24.typeOfHit == EnumMovingObjectType.TILE && (var1.getItemDamage() == 0 || var1.getItemDamage() == this.waterMoving))
            {
                int var25 = var24.blockX;
                int var26 = var24.blockY;
                int var27 = var24.blockZ;

                if (!var2.canMineBlock(var3, var25, var26, var27))
                {
                    return var1;
                }

                if (var1.getItemDamage() == 0)
                {
                    if (var2.getBlockMaterial(var25, var26, var27) == Material.water && var2.getBlockMetadata(var25, var26, var27) == 0)
                    {
                        var2.setBlock(var25, var26, var27, 0);
                        var1.setItemDamage(this.waterMoving);
                        return var1;
                    }
                } else
                {
                    if (var1.getItemDamage() <= 3 && var1.getItemDamage() != 0)
                    {
                        return new ItemStack(AetherItems.SkyrootBucket);
                    }

                    if (var24.sideHit == 0)
                    {
                        --var26;
                    }

                    if (var24.sideHit == 1)
                    {
                        ++var26;
                    }

                    if (var24.sideHit == 2)
                    {
                        --var27;
                    }

                    if (var24.sideHit == 3)
                    {
                        ++var27;
                    }

                    if (var24.sideHit == 4)
                    {
                        --var25;
                    }

                    if (var24.sideHit == 5)
                    {
                        ++var25;
                    }

                    if (var2.isAirBlock(var25, var26, var27) || !var2.getBlockMaterial(var25, var26, var27).isSolid())
                    {
                        if (var2.provider.isHellWorld && var1.getItemDamage() == this.waterMoving)
                        {
                            var2.playSoundEffect(var7 + 0.5D, var9 + 0.5D, var11 + 0.5D, "random.fizz", 0.5F, 2.6F + (var2.rand.nextFloat() - var2.rand.nextFloat()) * 0.8F);

                            for (int var28 = 0; var28 < 8; ++var28)
                            {
                                var2.spawnParticle("largesmoke", (double) var25 + Math.random(), (double) var26 + Math.random(), (double) var27 + Math.random(), 0.0D, 0.0D, 0.0D);
                            }
                        } else
                        {
                            var2.setBlock(var25, var26, var27, Block.waterMoving.blockID, 0, 4);
                        }

                        return new ItemStack(AetherItems.SkyrootBucket);
                    }
                }
            } else if (var1.getItemDamage() == 0 && var24 != null && var24.entityHit != null && var24.entityHit instanceof EntityCow)
            {
                var1.setItemDamage(1);
                return var1;
            }

            return var1;
        }
    }
}
