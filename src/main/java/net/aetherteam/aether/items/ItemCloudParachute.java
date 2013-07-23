package net.aetherteam.aether.items;

import net.aetherteam.aether.Aether;
import net.aetherteam.aether.entities.EntityCloudParachute;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemCloudParachute extends ItemAether
{
    public ItemCloudParachute(int var1, int var2)
    {
        super(var1);
        this.maxStackSize = 1;
        this.setMaxDamage(var2);
        this.setCreativeTab(Aether.tools);
    }
    
    public Item setIconName(String var1)
    {
        return this.setUnlocalizedName("Aether:" + var1);
    }
    
    
    /**
     * Called each tick as long the item is on a player inventory. Uses by maps to check if is on a player hand and
     * update it's contents.
     */
    public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5) 
    {       
    	if (par3Entity instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer)par3Entity;
            if(player.fallDistance > 20){
            	player.fallDistance = 0;
            	if (!par2World.isRemote)
                {
                	EntityCloudParachute entityCloudParachute = new EntityCloudParachute(par2World, player, this.itemID == AetherItems.GoldenCloudParachute.itemID);
                	par2World.spawnEntityInWorld(entityCloudParachute);
                }

            	par1ItemStack.damageItem(1, player);         	
            } 
        }
    }
    
    

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack var1, World world, EntityPlayer player)
    {
        if (Aether.getServerPlayer(player) == null)
        {
            return var1;
        }
        else
        {
            Aether.getServerPlayer(player).setParachuting(true, this.getParachuteType());
            --var1.stackSize;
            return var1;
        }
    }

    private int getParachuteType()
    {
        return this.itemID == AetherItems.CloudParachute.itemID ? 0 : (this.itemID == AetherItems.GoldenCloudParachute.itemID ? 1 : (this.itemID == AetherItems.PurpleCloudParachute.itemID ? 2 : (this.itemID == AetherItems.GreenCloudParachute.itemID ? 3 : (this.itemID == AetherItems.BlueCloudParachute.itemID ? 4 : 0))));
    }
}
