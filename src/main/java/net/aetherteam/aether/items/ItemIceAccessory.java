package net.aetherteam.aether.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.aetherteam.aether.PlayerBaseAetherServer;
import net.aetherteam.aether.client.PlayerBaseAetherClient;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class ItemIceAccessory extends ItemAccessory
{
    public ItemIceAccessory(int i, int j, int k, int l)
    {
        super(i, j, k, l, 16777215);
    }

    public ItemIceAccessory(int i, int j, String path, int l)
    {
        super(i, j, 0, l);
        this.texture = path;
    }

    public ItemIceAccessory(int i, int j, String path, int l, int m)
    {
        super(i, j, 0, l, m);
        this.texture = path;
    }

    public ItemIceAccessory(int i, int j, String path, int l, int m, boolean flag)
    {
        super(i, j, path, l, m);
        this.colouriseRender = flag;
    }

    public void activateServerPassive(EntityPlayer player, PlayerBaseAetherServer playerBase)
    {
        int i = MathHelper.floor_double(player.posX);
        int j = MathHelper.floor_double(player.boundingBox.minY);
        int k = MathHelper.floor_double(player.posZ);
        double yoff = player.posY - j;
        Material mat0 = player.worldObj.getBlockMaterial(i, j, k);
        Material mat1 = player.worldObj.getBlockMaterial(i, j - 1, k);

        for (int l = i - 1; l <= i + 1; l++)
        {
            for (int i1 = j - 1; i1 <= j + 1; i1++)
            {
                for (int j1 = k - 1; j1 <= k + 1; j1++)
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
                                playerBase.setSlotStack(playerBase.getSlotIndex(this.itemID), null);
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
                                playerBase.setSlotStack(playerBase.getSlotIndex(this.itemID), null);
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
                                playerBase.setSlotStack(playerBase.getSlotIndex(this.itemID), null);
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
                                playerBase.setSlotStack(playerBase.getSlotIndex(this.itemID), null);
                            }
                        }
                    }
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public void activateClientPassive(EntityPlayer player, PlayerBaseAetherClient playerBase)
    {
        int i = MathHelper.floor_double(player.posX);
        int j = MathHelper.floor_double(player.boundingBox.minY);
        int k = MathHelper.floor_double(player.posZ);
        double yoff = player.posY - j;
        Material mat0 = player.worldObj.getBlockMaterial(i, j, k);
        Material mat1 = player.worldObj.getBlockMaterial(i, j - 1, k);

        for (int l = i - 1; l <= i + 1; l++)
        {
            for (int i1 = j - 1; i1 <= j + 1; i1++)
            {
                for (int j1 = k - 1; j1 <= k + 1; j1++)
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
                                playerBase.setSlotStack(playerBase.getSlotIndex(this.itemID), null);
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
                                playerBase.setSlotStack(playerBase.getSlotIndex(this.itemID), null);
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
                                playerBase.setSlotStack(playerBase.getSlotIndex(this.itemID), null);
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
                                playerBase.setSlotStack(playerBase.getSlotIndex(this.itemID), null);
                            }
                        }
                    }
                }
            }
        }
    }
}

