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
import net.minecraft.item.ItemFood;
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
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer player)
    {
        int isFull = this.getItemDamageFromStack(itemstack);
    	float trans = 1.0F;
        float rotationspeed = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * trans;
        float rotationspeedY = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * trans;
        double speedX = player.prevPosX + (player.posX - player.prevPosX) * (double) trans;
        double speedY = player.prevPosY + (player.posY - player.prevPosY) * (double) trans + 1.62D - (double) player.yOffset;
        double speedZ = player.prevPosZ + (player.posZ - player.prevPosZ) * (double) trans;
        Vec3 vec3 = Vec3.createVectorHelper(speedX, speedY, speedZ);
        float cosrotationspeedY = MathHelper.cos(-rotationspeedY * 0.01745329F - (float) Math.PI);
        float sinrotationspeedY = MathHelper.sin(-rotationspeedY * 0.01745329F - (float) Math.PI);
        float cosrotationspeed = -MathHelper.cos(-rotationspeed * 0.01745329F);
        float sinrotationspeed = MathHelper.sin(-rotationspeed * 0.01745329F);
        float var18 = sinrotationspeedY * cosrotationspeed;
        float var20 = cosrotationspeedY * cosrotationspeed;
        double trans5 = 5.0D;
        Vec3 var23 = vec3.addVector((double) var18 * trans5, (double) sinrotationspeed * trans5, (double) var20 * trans5);
        MovingObjectPosition position = world.rayTraceBlocks_do(vec3, var23, (itemstack.getItemDamage() == 0 ||itemstack.getItemDamage() == 4));
        
        if(position == null && (itemstack.getItemDamage() == 0 ||itemstack.getItemDamage() == 4))
        {
        	return itemstack;
        }
        
        int positionx = 0;
        int positiony = 0;
        int positionz = 0;   
        
        if(itemstack.getItemDamage() == 0 ||itemstack.getItemDamage() == 4)
        {
        	positionx = position.blockX;
            positiony = position.blockY;
            positionz = position.blockZ;   
        }

    	int meta = 0;
    	meta = itemstack.getItemDamage();
    	int num = 0;
    	num = itemstack.stackSize;
    	ItemStack empty = new ItemStack(AetherItems.SkyrootBucket,1,0);
    	ItemStack nmilk = new ItemStack(Item.bucketMilk);
    		 
    	switch(meta)
    	{
    	case 0:
    		if (world.getBlockMaterial(positionx, positiony, positionz) == Material.water && world.getBlockMetadata(positionx, positiony, positionz) == 0)
            {
            	world.setBlock(positionx, positiony, positionz, 0);
            	itemstack.stackSize--;
            	ItemStack water = new ItemStack(AetherItems.SkyrootBucket,1,this.waterMoving);
            	if(!player.inventory.addItemStackToInventory(water))
        		{
        			player.dropPlayerItem(water);
        			return itemstack;
        		}
                return itemstack;
            } 
    		if (world.getBlockMaterial(positionx, positiony, positionz) == Material.lava && world.getBlockMetadata(positionx, positiony, positionz) == 0)
            {
            	world.setBlock(positionx, positiony, positionz, 0);
            	itemstack.stackSize--;
            	world.spawnParticle("largesmoke", (double) positionx + Math.random(), (double) positiony + Math.random(), (double) positionz + Math.random(), 0.0D, 0.0D, 0.0D);
                player.setFire(5);
            	return itemstack;
            } 
    		if(position.entityHit instanceof EntityCow)
    		{
    			itemstack.stackSize--;
    			ItemStack milk = new ItemStack(AetherItems.SkyrootBucket,1,1);
            	if(!player.inventory.addItemStackToInventory(milk))
        		{
        			player.dropPlayerItem(milk);
        		}
    			return itemstack;
    			
    		}
    		return itemstack;
    	case 1:
    		player.curePotionEffects(nmilk);
    		itemstack.stackSize--;
    		if(!player.inventory.addItemStackToInventory(empty))
    		{
    			player.dropPlayerItem(empty);
    		}
    		return itemstack;
    	case 2:
    		player.addPotionEffect(new PotionEffect(Potion.poison.id, 200, 0));
        	player.addPotionEffect(new PotionEffect(Potion.confusion.id, 200, 3));
        	world.playSoundAtEntity(player, "random.burp", 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
    		itemstack.stackSize--;
    		if(!player.inventory.addItemStackToInventory(empty))
    		{
    			player.dropPlayerItem(empty);
    		}
    		return itemstack;
    	case 3:
    		player.addPotionEffect(new PotionEffect(Potion.regeneration.id, 200, 0));
    		world.playSoundAtEntity(player, "random.burp", 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
    		itemstack.stackSize--;
    		if(!player.inventory.addItemStackToInventory(empty))
    		{
    			player.dropPlayerItem(empty);
    		}
    		return itemstack;
    	case 4:
    		if (position.sideHit == 0)
            {
                --positiony;
            }

            if (position.sideHit == 1)
            {
                ++positiony;
            }

            if (position.sideHit == 2)
            {
                --positionz;
            }

            if (position.sideHit == 3)
            {
                ++positionz;
            }

            if (position.sideHit == 4)
            {
                --positionx;
            }

            if (position.sideHit == 5)
            {
                ++positionx;
            }
    		if(this.tryPlaceContainedLiquid(world, speedX, speedY, speedZ, positionx, positiony, positionz, itemstack))
    		{
    			itemstack.stackSize--;
    			if(!player.inventory.addItemStackToInventory(empty))
        		{
        			player.dropPlayerItem(empty);
        		}
            	return itemstack;
    		}
    		return itemstack;
    	}
    	
        return itemstack;
    }

/**
 * Attempts to place the liquid contained inside the bucket.
 */
public boolean tryPlaceContainedLiquid(World par1World, double par2, double par4, double par6, int par8, int par9, int par10, ItemStack itemstack)
{
	int isFull = itemstack.getItemDamage();
    if (isFull != 4)
    {
        return false;
    }
    else
    {

        if (par1World.provider.isHellWorld)
        {
            par1World.playSoundEffect(par2 + 0.5D, par4 + 0.5D, par6 + 0.5D, "random.fizz", 0.5F, 2.6F + (par1World.rand.nextFloat() - par1World.rand.nextFloat()) * 0.8F);

            for (int l = 0; l < 8;)
            {
            	l++;
                par1World.spawnParticle("largesmoke", (double)par8 + Math.random(), (double)par9 + Math.random(), (double)par10 + Math.random(), 0.0D, 0.0D, 0.0D);
                return true;
            }
        }
        else
        {
        	if(par1World.getBlockMaterial(par8, par9, par10) == Material.air)
        	{
        		par1World.setBlock(par8, par9, par10, 9, 0, 1);
        	}
        	else
        	{
        		return false;
        	}
        }
 	
        return true;
    }
}
}

