package net.aetherteam.aether.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.aetherteam.aether.PlayerBaseAetherServer;
import net.aetherteam.aether.client.PlayerBaseAetherClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;

public class ItemIceAccessory extends ItemAccessory
{
    public ItemIceAccessory(int var1, int var2, int var3, int var4)
    {
        super(var1, var2, var3, var4, 16777215);
    }

    public ItemIceAccessory(int var1, int var2, String var3, int var4)
    {
        super(var1, var2, 0, var4);
        this.texture = var3;
    }

    public ItemIceAccessory(int var1, int var2, String var3, int var4, int var5)
    {
        super(var1, var2, 0, var4, var5);
        this.texture = var3;
    }

    public ItemIceAccessory(int var1, int var2, String var3, int var4, int var5, boolean var6)
    {
        super(var1, var2, var3, var4, var5);
        this.colouriseRender = var6;
    }

    public void activateServerPassive(EntityPlayer var1, PlayerBaseAetherServer var2)
    {
        int var3 = MathHelper.floor_double(var1.posX);
        int var4 = MathHelper.floor_double(var1.boundingBox.minY);
        int var5 = MathHelper.floor_double(var1.posZ);
        double var10000 = var1.posY - (double)var4;
        var1.worldObj.getBlockMaterial(var3, var4, var5);
        var1.worldObj.getBlockMaterial(var3, var4 - 1, var5);

        for (int var10 = var3 - 1; var10 <= var3 + 1; ++var10)
        {
            for (int var11 = var4 - 1; var11 <= var4 + 1; ++var11)
            {
                for (int var12 = var5 - 1; var12 <= var5 + 1; ++var12)
                {
                    if (var1.worldObj.getBlockId(var10, var11, var12) == 8)
                    {
                        if (var1.worldObj.getBlockMetadata(var10, var11, var12) == 0)
                        {
                            var1.worldObj.setBlock(var10, var11, var12, 79);
                        }
                        else
                        {
                            var1.worldObj.setBlock(var10, var11, var12, 0);
                        }

                        if (var2.getSlotStack(this.itemID) != null)
                        {
                            var2.getSlotStack(this.itemID).damageItem(1, var1);

                            if (var2.getSlotStack(this.itemID).stackSize < 1)
                            {
                                var2.setSlotStack(var2.getSlotIndex(this.itemID), (ItemStack)null);
                            }
                        }
                    }
                    else if (var1.worldObj.getBlockId(var10, var11, var12) == 9)
                    {
                        if (var1.worldObj.getBlockMetadata(var10, var11, var12) == 0)
                        {
                            var1.worldObj.setBlock(var10, var11, var12, 79);
                        }
                        else
                        {
                            var1.worldObj.setBlock(var10, var11, var12, 0);
                        }

                        if (var2.getSlotStack(this.itemID) != null)
                        {
                            var2.getSlotStack(this.itemID).damageItem(1, var1);

                            if (var2.getSlotStack(this.itemID).stackSize < 1)
                            {
                                var2.setSlotStack(var2.getSlotIndex(this.itemID), (ItemStack)null);
                            }
                        }
                    }
                    else if (var1.worldObj.getBlockId(var10, var11, var12) == 10)
                    {
                        if (var1.worldObj.getBlockMetadata(var10, var11, var12) == 0)
                        {
                            var1.worldObj.setBlock(var10, var11, var12, 49);
                        }
                        else
                        {
                            var1.worldObj.setBlock(var10, var11, var12, 0);
                        }

                        if (var2.getSlotStack(this.itemID) != null)
                        {
                            var2.getSlotStack(this.itemID).damageItem(1, var1);

                            if (var2.getSlotStack(this.itemID).stackSize < 1)
                            {
                                var2.setSlotStack(var2.getSlotIndex(this.itemID), (ItemStack)null);
                            }
                        }
                    }
                    else if (var1.worldObj.getBlockId(var10, var11, var12) == 11)
                    {
                        if (var1.worldObj.getBlockMetadata(var10, var11, var12) == 0)
                        {
                            var1.worldObj.setBlock(var10, var11, var12, 49);
                        }
                        else
                        {
                            var1.worldObj.setBlock(var10, var11, var12, 0);
                        }

                        if (var2.getSlotStack(this.itemID) != null)
                        {
                            var2.getSlotStack(this.itemID).damageItem(1, var1);

                            if (var2.getSlotStack(this.itemID).stackSize < 1)
                            {
                                var2.setSlotStack(var2.getSlotIndex(this.itemID), (ItemStack)null);
                            }
                        }
                    }
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public void activateClientPassive(EntityPlayer var1, PlayerBaseAetherClient var2)
    {
        int var3 = MathHelper.floor_double(var1.posX);
        int var4 = MathHelper.floor_double(var1.boundingBox.minY);
        int var5 = MathHelper.floor_double(var1.posZ);
        double var10000 = var1.posY - (double)var4;
        var1.worldObj.getBlockMaterial(var3, var4, var5);
        var1.worldObj.getBlockMaterial(var3, var4 - 1, var5);

        for (int var10 = var3 - 1; var10 <= var3 + 1; ++var10)
        {
            for (int var11 = var4 - 1; var11 <= var4 + 1; ++var11)
            {
                for (int var12 = var5 - 1; var12 <= var5 + 1; ++var12)
                {
                    if (var1.worldObj.getBlockId(var10, var11, var12) == 8)
                    {
                        if (var1.worldObj.getBlockMetadata(var10, var11, var12) == 0)
                        {
                            var1.worldObj.setBlock(var10, var11, var12, 79);
                        }
                        else
                        {
                            var1.worldObj.setBlock(var10, var11, var12, 0);
                        }

                        if (var2.getSlotStack(this.itemID) != null)
                        {
                            var2.getSlotStack(this.itemID).damageItem(1, var1);

                            if (var2.getSlotStack(this.itemID).stackSize < 1)
                            {
                                var2.setSlotStack(var2.getSlotIndex(this.itemID), (ItemStack)null);
                            }
                        }
                    }
                    else if (var1.worldObj.getBlockId(var10, var11, var12) == 9)
                    {
                        if (var1.worldObj.getBlockMetadata(var10, var11, var12) == 0)
                        {
                            var1.worldObj.setBlock(var10, var11, var12, 79);
                        }
                        else
                        {
                            var1.worldObj.setBlock(var10, var11, var12, 0);
                        }

                        if (var2.getSlotStack(this.itemID) != null)
                        {
                            var2.getSlotStack(this.itemID).damageItem(1, var1);

                            if (var2.getSlotStack(this.itemID).stackSize < 1)
                            {
                                var2.setSlotStack(var2.getSlotIndex(this.itemID), (ItemStack)null);
                            }
                        }
                    }
                    else if (var1.worldObj.getBlockId(var10, var11, var12) == 10)
                    {
                        if (var1.worldObj.getBlockMetadata(var10, var11, var12) == 0)
                        {
                            var1.worldObj.setBlock(var10, var11, var12, 49);
                        }
                        else
                        {
                            var1.worldObj.setBlock(var10, var11, var12, 0);
                        }

                        if (var2.getSlotStack(this.itemID) != null)
                        {
                            var2.getSlotStack(this.itemID).damageItem(1, var1);

                            if (var2.getSlotStack(this.itemID).stackSize < 1)
                            {
                                var2.setSlotStack(var2.getSlotIndex(this.itemID), (ItemStack)null);
                            }
                        }
                    }
                    else if (var1.worldObj.getBlockId(var10, var11, var12) == 11)
                    {
                        if (var1.worldObj.getBlockMetadata(var10, var11, var12) == 0)
                        {
                            var1.worldObj.setBlock(var10, var11, var12, 49);
                        }
                        else
                        {
                            var1.worldObj.setBlock(var10, var11, var12, 0);
                        }

                        if (var2.getSlotStack(this.itemID) != null)
                        {
                            var2.getSlotStack(this.itemID).damageItem(1, var1);

                            if (var2.getSlotStack(this.itemID).stackSize < 1)
                            {
                                var2.setSlotStack(var2.getSlotIndex(this.itemID), (ItemStack)null);
                            }
                        }
                    }
                }
            }
        }
    }
}
