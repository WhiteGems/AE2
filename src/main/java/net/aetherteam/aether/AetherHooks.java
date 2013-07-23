package net.aetherteam.aether;

import java.util.Iterator;
import java.util.List;
import net.aetherteam.aether.blocks.AetherBlocks;
import net.aetherteam.aether.dungeons.Dungeon;
import net.aetherteam.aether.dungeons.DungeonHandler;
import net.aetherteam.aether.items.AetherItems;
import net.aetherteam.aether.party.Party;
import net.aetherteam.aether.party.PartyController;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;

public class AetherHooks
{
    private static final int[] banItemIDs = new int[0];

    @ForgeSubscribe
    public void onPlayerInteract(PlayerInteractEvent var1)
    {
        this.handlePortalActivation(var1);
        this.handleAerogelPlacement(var1);
        this.handleAetherBlockBans(var1);
        this.handleAetherMountInteraction(var1);
        this.addBlockToDungeonInstance(var1);
    }

    private void handleAetherBlockBans(PlayerInteractEvent var1)
    {
        if (var1.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK)
        {
            if (var1.entityPlayer.dimension == 3)
            {
                ItemStack var2 = var1.entityPlayer.getCurrentEquippedItem();

                if (var2 != null)
                {
                    int[] var3 = banItemIDs;
                    int var4 = var3.length;

                    for (int var5 = 0; var5 < var4; ++var5)
                    {
                        int var6 = var3[var5];

                        if (var2.itemID == var6)
                        {
                            var1.setCanceled(true);
                            return;
                        }
                    }
                }
            }
        }
    }

    private void handleAetherMountInteraction(PlayerInteractEvent var1)
    {
        if (var1.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK)
        {
            Vec3 var2 = var1.entityPlayer.getLookVec();
            double var3 = 6.0D;
            AxisAlignedBB var5 = var1.entityPlayer.boundingBox.expand(var3, var3, var3);
            List var6 = var1.entityPlayer.worldObj.getEntitiesWithinAABB(Entity.class, var5);
            Object var7 = null;
            double var8 = 0.0D;
            Iterator var10 = var6.iterator();

            while (var10.hasNext())
            {
                Object var11 = var10.next();

                if (var11 != var1.entityPlayer)
                {
                    Entity var12 = (Entity)var11;
                    Vec3 var13 = Vec3.createVectorHelper(var12.posX - var1.entityPlayer.posX, var12.boundingBox.minY + (double)(var12.height / 2.0F) - var1.entityPlayer.posY - (double)var1.entityPlayer.getEyeHeight(), var12.posZ - var1.entityPlayer.posZ);
                    double var14 = var13.lengthVector();

                    if (var14 <= var3)
                    {
                        var13 = var13.normalize();
                        double var16 = var2.dotProduct(var13);

                        if (var16 >= 1.0D - 0.125D / var14 && var1.entityPlayer.canEntityBeSeen(var12) && (var8 == 0.0D || var14 < var8))
                        {
                            var8 = var14;
                        }
                    }
                }
            }

            if (var1.entityPlayer.worldObj.isRemote && Aether.getPlayerBase(var1.entityPlayer).riddenBy != null)
            {
                Aether.getPlayerBase(var1.entityPlayer).riddenBy.onUnMount();
            }

            if (!var1.entityPlayer.worldObj.isRemote && Aether.getPlayerBase((EntityPlayerMP)var1.entityPlayer).riddenBy != null)
            {
                Aether.getPlayerBase((EntityPlayerMP)var1.entityPlayer).riddenBy.onUnMount();
            }
        }
    }

    private void addBlockToDungeonInstance(PlayerInteractEvent var1)
    {
        Party var2 = PartyController.instance().getParty(var1.entityPlayer);
        Dungeon var3 = DungeonHandler.instance().getDungeon(var2);
        int[][][] var4 = (int[][][])null;

        if (PartyController.instance().getParty(var1.entityPlayer) != null && DungeonHandler.instance().isInDungeon(var2) && var3.hasStarted() && var3 != null && var3.hasMember(PartyController.instance().getMember(var1.entityPlayer)))
        {
            if (var1.action != PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK)
            {
                return;
            }

            ItemStack var5 = var1.entityPlayer.getCurrentEquippedItem();

            if (var5 != null && var5.getItem() instanceof ItemBlock)
            {
                switch (var1.face)
                {
                    case 0:
                        if (var1.entityPlayer.worldObj.getBlockId(var1.x, var1.y - 1, var1.z) == 0)
                        {
                            var3.registerBlockPlacement(var1.x, var1.y - 1, var1.z);
                        }

                        break;

                    case 1:
                        if (var1.entityPlayer.worldObj.getBlockId(var1.x, var1.y + 1, var1.z) == 0)
                        {
                            var3.registerBlockPlacement(var1.x, var1.y + 1, var1.z);
                        }

                        break;

                    case 2:
                        if (var1.entityPlayer.worldObj.getBlockId(var1.x, var1.y, var1.z - 1) == 0)
                        {
                            var3.registerBlockPlacement(var1.x, var1.y, var1.z - 1);
                        }

                        break;

                    case 3:
                        if (var1.entityPlayer.worldObj.getBlockId(var1.x, var1.y, var1.z + 1) == 0)
                        {
                            var3.registerBlockPlacement(var1.x, var1.y, var1.z + 1);
                        }

                        break;

                    case 4:
                        if (var1.entityPlayer.worldObj.getBlockId(var1.x - 1, var1.y, var1.z) == 0)
                        {
                            var3.registerBlockPlacement(var1.x - 1, var1.y, var1.z);
                        }

                        break;

                    case 5:
                        if (var1.entityPlayer.worldObj.getBlockId(var1.x + 1, var1.y, var1.z) == 0)
                        {
                            var3.registerBlockPlacement(var1.x + 1, var1.y, var1.z);
                        }
                }
            }
        }
    }

    private void handleAerogelPlacement(PlayerInteractEvent var1)
    {
        if (var1.action == PlayerInteractEvent.Action.RIGHT_CLICK_AIR)
        {
            if (var1.entityPlayer.dimension == 3)
            {
                ItemStack var2 = var1.entityPlayer.getCurrentEquippedItem();

                if (var2 != null && var2.itemID == Item.bucketLava.itemID)
                {
                    if (var1.entityPlayer.worldObj.isRemote)
                    {
                        ;
                    }

                    float var3 = 1.0F;
                    double var10000 = var1.entityPlayer.prevPosX + (var1.entityPlayer.posX - var1.entityPlayer.prevPosX) * (double)var3;
                    double var6 = var1.entityPlayer.prevPosY + (var1.entityPlayer.posY - var1.entityPlayer.prevPosY) * (double)var3 + 1.62D - (double)var1.entityPlayer.yOffset;
                    var10000 = var1.entityPlayer.prevPosZ + (var1.entityPlayer.posZ - var1.entityPlayer.prevPosZ) * (double)var3;
                    boolean var10 = false;
                    MovingObjectPosition var11 = this.getMovingObjectPositionFromPlayer(var1.entityPlayer.worldObj, var1.entityPlayer, var10);

                    if (var11 != null && var11.typeOfHit == EnumMovingObjectType.TILE)
                    {
                        int var12 = var11.blockX;
                        int var13 = var11.blockY;
                        int var14 = var11.blockZ;

                        if (var1.entityPlayer.worldObj.canMineBlock(var1.entityPlayer, var12, var13, var14))
                        {
                            if (var11.sideHit == 0)
                            {
                                --var13;
                            }
                            else if (var11.sideHit == 1)
                            {
                                ++var13;
                            }
                            else if (var11.sideHit == 2)
                            {
                                --var14;
                            }
                            else if (var11.sideHit == 3)
                            {
                                ++var14;
                            }
                            else if (var11.sideHit == 4)
                            {
                                --var12;
                            }
                            else if (var11.sideHit == 5)
                            {
                                ++var12;
                            }

                            if (var1.entityPlayer.canPlayerEdit(var12, var13, var14, var11.sideHit, var2))
                            {
                                if (var1.entityPlayer.worldObj.isAirBlock(var12, var13, var14) || !var1.entityPlayer.worldObj.getBlockMaterial(var12, var13, var14).isSolid())
                                {
                                    if (!var1.entityPlayer.capabilities.isCreativeMode)
                                    {
                                        ;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void handlePortalActivation(PlayerInteractEvent var1)
    {
        if (var1.action == PlayerInteractEvent.Action.RIGHT_CLICK_AIR)
        {
            Party var2 = PartyController.instance().getParty(var1.entityPlayer);

            if (var2 == null || DungeonHandler.instance().getDungeon(var2) == null)
            {
                ItemStack var3 = var1.entityPlayer.getCurrentEquippedItem();

                if (var3 != null && (var3.itemID == Item.bucketWater.itemID || var3.itemID == AetherItems.SkyrootBucket.itemID || var3.getItemDamage() == 8))
                {
                    float var4 = 1.0F;
                    double var10000 = var1.entityPlayer.prevPosX + (var1.entityPlayer.posX - var1.entityPlayer.prevPosX) * (double)var4;
                    double var7 = var1.entityPlayer.prevPosY + (var1.entityPlayer.posY - var1.entityPlayer.prevPosY) * (double)var4 + 1.62D - (double)var1.entityPlayer.yOffset;
                    var10000 = var1.entityPlayer.prevPosZ + (var1.entityPlayer.posZ - var1.entityPlayer.prevPosZ) * (double)var4;
                    boolean var11 = false;
                    MovingObjectPosition var12 = this.getMovingObjectPositionFromPlayer(var1.entityPlayer.worldObj, var1.entityPlayer, var11);

                    if (var12 != null && var12.typeOfHit == EnumMovingObjectType.TILE)
                    {
                        int var13 = var12.blockX;
                        int var14 = var12.blockY;
                        int var15 = var12.blockZ;

                        if (var1.entityPlayer.worldObj.getBlockId(var13, var14, var15) == Block.glowStone.blockID)
                        {
                            if (var1.entityPlayer.worldObj.canMineBlock(var1.entityPlayer, var13, var14, var15))
                            {
                                if (var12.sideHit == 0)
                                {
                                    --var14;
                                }
                                else if (var12.sideHit == 1)
                                {
                                    ++var14;
                                }
                                else if (var12.sideHit == 2)
                                {
                                    --var15;
                                }
                                else if (var12.sideHit == 3)
                                {
                                    ++var15;
                                }
                                else if (var12.sideHit == 4)
                                {
                                    --var13;
                                }
                                else if (var12.sideHit == 5)
                                {
                                    ++var13;
                                }

                                if (var1.entityPlayer.canPlayerEdit(var13, var14, var15, var12.sideHit, var3))
                                {
                                    if (var1.entityPlayer.worldObj.isAirBlock(var13, var14, var15) || !var1.entityPlayer.worldObj.getBlockMaterial(var13, var14, var15).isSolid())
                                    {
                                        if (!var1.entityPlayer.worldObj.isRemote && AetherBlocks.AetherPortal.tryToCreatePortal(var1.entityPlayer.worldObj, var13, var14, var15))
                                        {
                                            var1.setCanceled(true);

                                            if (var3 == new ItemStack(AetherItems.SkyrootBucket.itemID, 1, Block.waterMoving.blockID))
                                            {
                                                new ItemStack(AetherItems.SkyrootBucket.itemID, 1, 0);
                                                return;
                                            }

                                            if (!var1.entityPlayer.capabilities.isCreativeMode)
                                            {
                                                var3.itemID = Item.bucketEmpty.itemID;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private MovingObjectPosition getMovingObjectPositionFromPlayer(World var1, EntityPlayer var2, boolean var3)
    {
        float var4 = 1.0F;
        float var5 = var2.prevRotationPitch + (var2.rotationPitch - var2.prevRotationPitch) * var4;
        float var6 = var2.prevRotationYaw + (var2.rotationYaw - var2.prevRotationYaw) * var4;
        double var7 = var2.prevPosX + (var2.posX - var2.prevPosX) * (double)var4;
        double var9 = var2.prevPosY + (var2.posY - var2.prevPosY) * (double)var4 + 1.62D - (double)var2.yOffset;
        double var11 = var2.prevPosZ + (var2.posZ - var2.prevPosZ) * (double)var4;
        Vec3 var13 = var1.getWorldVec3Pool().getVecFromPool(var7, var9, var11);
        float var14 = MathHelper.cos(-var6 * 0.017453292F - (float)Math.PI);
        float var15 = MathHelper.sin(-var6 * 0.017453292F - (float)Math.PI);
        float var16 = -MathHelper.cos(-var5 * 0.017453292F);
        float var17 = MathHelper.sin(-var5 * 0.017453292F);
        float var18 = var15 * var16;
        float var19 = var14 * var16;
        double var20 = 5.0D;

        if (var2 instanceof EntityPlayerMP)
        {
            var20 = ((EntityPlayerMP)var2).theItemInWorldManager.getBlockReachDistance();
        }

        Vec3 var22 = var13.addVector((double)var18 * var20, (double)var17 * var20, (double)var19 * var20);
        return var1.rayTraceBlocks_do_do(var13, var22, var3, !var3);
    }
}
