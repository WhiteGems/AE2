package net.aetherteam.aether.items;

import net.aetherteam.aether.PlayerAetherServer;
import net.aetherteam.aether.client.PlayerAetherClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class ItemIceAccessory extends ItemAccessory
{
    public ItemIceAccessory(int i, int j, int k, int l)
    {
        super(i, j, k, l, 16777215);
    }

    public ItemIceAccessory(int i, int j, ResourceLocation path, int l)
    {
        super(i, j, 0, l);
        this.texture = path;
    }

    public ItemIceAccessory(int i, int j, ResourceLocation path, int l, int m)
    {
        super(i, j, 0, l, m);
        this.texture = path;
    }

    public ItemIceAccessory(int i, int j, ResourceLocation path, int l, int m, boolean flag)
    {
        super(i, j, path, l, m);
        this.colouriseRender = flag;
    }

    public void activateServerPassive(EntityPlayer player, PlayerAetherServer playerBase)
    {
        int i = MathHelper.floor_double(player.posX);
        int j = MathHelper.floor_double(player.boundingBox.minY);
        int k = MathHelper.floor_double(player.posZ);
        double var10000 = player.posY - (double)j;
        player.worldObj.getBlockMaterial(i, j, k);
        player.worldObj.getBlockMaterial(i, j - 1, k);

        for (int l = i - 1; l <= i + 1; ++l)
        {
            for (int i1 = j - 1; i1 <= j + 1; ++i1)
            {
                for (int j1 = k - 1; j1 <= k + 1; ++j1)
                {
                    if (player.worldObj.getBlockId(l, i1, j1) == 8)
                    {
                        if (player.worldObj.getBlockMetadata(l, i1, j1) == 0)
                        {
                            player.worldObj.setBlock(l, i1, j1, 79);
                        }
                        else
                        {
                            player.worldObj.setBlock(l, i1, j1, 0);
                        }

                        if (playerBase.getSlotStack(this.itemID) != null)
                        {
                            playerBase.getSlotStack(this.itemID).damageItem(1, player);

                            if (playerBase.getSlotStack(this.itemID).stackSize < 1)
                            {
                                playerBase.setSlotStack(playerBase.getSlotIndex(this.itemID), (ItemStack)null);
                            }
                        }
                    }
                    else if (player.worldObj.getBlockId(l, i1, j1) == 9)
                    {
                        if (player.worldObj.getBlockMetadata(l, i1, j1) == 0)
                        {
                            player.worldObj.setBlock(l, i1, j1, 79);
                        }
                        else
                        {
                            player.worldObj.setBlock(l, i1, j1, 0);
                        }

                        if (playerBase.getSlotStack(this.itemID) != null)
                        {
                            playerBase.getSlotStack(this.itemID).damageItem(1, player);

                            if (playerBase.getSlotStack(this.itemID).stackSize < 1)
                            {
                                playerBase.setSlotStack(playerBase.getSlotIndex(this.itemID), (ItemStack)null);
                            }
                        }
                    }
                    else if (player.worldObj.getBlockId(l, i1, j1) == 10)
                    {
                        if (player.worldObj.getBlockMetadata(l, i1, j1) == 0)
                        {
                            player.worldObj.setBlock(l, i1, j1, 49);
                        }
                        else
                        {
                            player.worldObj.setBlock(l, i1, j1, 0);
                        }

                        if (playerBase.getSlotStack(this.itemID) != null)
                        {
                            playerBase.getSlotStack(this.itemID).damageItem(1, player);

                            if (playerBase.getSlotStack(this.itemID).stackSize < 1)
                            {
                                playerBase.setSlotStack(playerBase.getSlotIndex(this.itemID), (ItemStack)null);
                            }
                        }
                    }
                    else if (player.worldObj.getBlockId(l, i1, j1) == 11)
                    {
                        if (player.worldObj.getBlockMetadata(l, i1, j1) == 0)
                        {
                            player.worldObj.setBlock(l, i1, j1, 49);
                        }
                        else
                        {
                            player.worldObj.setBlock(l, i1, j1, 0);
                        }

                        if (playerBase.getSlotStack(this.itemID) != null)
                        {
                            playerBase.getSlotStack(this.itemID).damageItem(1, player);

                            if (playerBase.getSlotStack(this.itemID).stackSize < 1)
                            {
                                playerBase.setSlotStack(playerBase.getSlotIndex(this.itemID), (ItemStack)null);
                            }
                        }
                    }
                }
            }
        }
    }

    public void activateClientPassive(EntityPlayer player, PlayerAetherClient playerBase)
    {
        int i = MathHelper.floor_double(player.posX);
        int j = MathHelper.floor_double(player.boundingBox.minY);
        int k = MathHelper.floor_double(player.posZ);
        double var10000 = player.posY - (double)j;
        player.worldObj.getBlockMaterial(i, j, k);
        player.worldObj.getBlockMaterial(i, j - 1, k);

        for (int l = i - 1; l <= i + 1; ++l)
        {
            for (int i1 = j - 1; i1 <= j + 1; ++i1)
            {
                for (int j1 = k - 1; j1 <= k + 1; ++j1)
                {
                    if (player.worldObj.getBlockId(l, i1, j1) == 8)
                    {
                        if (player.worldObj.getBlockMetadata(l, i1, j1) == 0)
                        {
                            player.worldObj.setBlock(l, i1, j1, 79);
                        }
                        else
                        {
                            player.worldObj.setBlock(l, i1, j1, 0);
                        }

                        if (playerBase.getSlotStack(this.itemID) != null)
                        {
                            playerBase.getSlotStack(this.itemID).damageItem(1, player);

                            if (playerBase.getSlotStack(this.itemID).stackSize < 1)
                            {
                                playerBase.setSlotStack(playerBase.getSlotIndex(this.itemID), (ItemStack)null);
                            }
                        }
                    }
                    else if (player.worldObj.getBlockId(l, i1, j1) == 9)
                    {
                        if (player.worldObj.getBlockMetadata(l, i1, j1) == 0)
                        {
                            player.worldObj.setBlock(l, i1, j1, 79);
                        }
                        else
                        {
                            player.worldObj.setBlock(l, i1, j1, 0);
                        }

                        if (playerBase.getSlotStack(this.itemID) != null)
                        {
                            playerBase.getSlotStack(this.itemID).damageItem(1, player);

                            if (playerBase.getSlotStack(this.itemID).stackSize < 1)
                            {
                                playerBase.setSlotStack(playerBase.getSlotIndex(this.itemID), (ItemStack)null);
                            }
                        }
                    }
                    else if (player.worldObj.getBlockId(l, i1, j1) == 10)
                    {
                        if (player.worldObj.getBlockMetadata(l, i1, j1) == 0)
                        {
                            player.worldObj.setBlock(l, i1, j1, 49);
                        }
                        else
                        {
                            player.worldObj.setBlock(l, i1, j1, 0);
                        }

                        if (playerBase.getSlotStack(this.itemID) != null)
                        {
                            playerBase.getSlotStack(this.itemID).damageItem(1, player);

                            if (playerBase.getSlotStack(this.itemID).stackSize < 1)
                            {
                                playerBase.setSlotStack(playerBase.getSlotIndex(this.itemID), (ItemStack)null);
                            }
                        }
                    }
                    else if (player.worldObj.getBlockId(l, i1, j1) == 11)
                    {
                        if (player.worldObj.getBlockMetadata(l, i1, j1) == 0)
                        {
                            player.worldObj.setBlock(l, i1, j1, 49);
                        }
                        else
                        {
                            player.worldObj.setBlock(l, i1, j1, 0);
                        }

                        if (playerBase.getSlotStack(this.itemID) != null)
                        {
                            playerBase.getSlotStack(this.itemID).damageItem(1, player);

                            if (playerBase.getSlotStack(this.itemID).stackSize < 1)
                            {
                                playerBase.setSlotStack(playerBase.getSlotIndex(this.itemID), (ItemStack)null);
                            }
                        }
                    }
                }
            }
        }
    }
}
