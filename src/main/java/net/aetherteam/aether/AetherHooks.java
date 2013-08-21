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
    public void onPlayerInteract(PlayerInteractEvent e)
    {
        this.handlePortalActivation(e);
        this.handleAerogelPlacement(e);
        this.handleAetherBlockBans(e);
        this.handleAetherMountInteraction(e);
        this.addBlockToDungeonInstance(e);
    }

    private void handleAetherBlockBans(PlayerInteractEvent e)
    {
        if (e.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK)
        {
            if (e.entityPlayer.dimension == 3)
            {
                ItemStack currentStack = e.entityPlayer.getCurrentEquippedItem();

                if (currentStack != null)
                {
                    int[] arr$ = banItemIDs;
                    int len$ = arr$.length;

                    for (int i$ = 0; i$ < len$; ++i$)
                    {
                        int banItemID = arr$[i$];

                        if (currentStack.itemID == banItemID)
                        {
                            e.setCanceled(true);
                            return;
                        }
                    }
                }
            }
        }
    }

    private void handleAetherMountInteraction(PlayerInteractEvent e)
    {
        if (e.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK)
        {
            Vec3 look = e.entityPlayer.getLookVec();
            double dist = 6.0D;
            AxisAlignedBB aabb = e.entityPlayer.boundingBox.expand(dist, dist, dist);
            List list = e.entityPlayer.worldObj.getEntitiesWithinAABB(Entity.class, aabb);
            Object found = null;
            double foundLen = 0.0D;
            Iterator i$ = list.iterator();

            while (i$.hasNext())
            {
                Object o = i$.next();

                if (o != e.entityPlayer)
                {
                    Entity ent = (Entity)o;
                    Vec3 vec = Vec3.createVectorHelper(ent.posX - e.entityPlayer.posX, ent.boundingBox.minY + (double)(ent.height / 2.0F) - e.entityPlayer.posY - (double)e.entityPlayer.getEyeHeight(), ent.posZ - e.entityPlayer.posZ);
                    double len = vec.lengthVector();

                    if (len <= dist)
                    {
                        vec = vec.normalize();
                        double dot = look.dotProduct(vec);

                        if (dot >= 1.0D - 0.125D / len && e.entityPlayer.canEntityBeSeen(ent) && (foundLen == 0.0D || len < foundLen))
                        {
                            foundLen = len;
                        }
                    }
                }
            }

            if (e.entityPlayer.worldObj.isRemote && Aether.getPlayerBase(e.entityPlayer).riddenBy != null)
            {
                Aether.getPlayerBase(e.entityPlayer).riddenBy.onUnMount();
            }

            if (!e.entityPlayer.worldObj.isRemote && Aether.getPlayerBase((EntityPlayerMP)e.entityPlayer).riddenBy != null)
            {
                Aether.getPlayerBase((EntityPlayerMP)e.entityPlayer).riddenBy.onUnMount();
            }
        }
    }

    private void addBlockToDungeonInstance(PlayerInteractEvent e)
    {
        Party party = PartyController.instance().getParty(e.entityPlayer);
        Dungeon dungeon = DungeonHandler.instance().getDungeon(party);
        int[][][] arrayOfInt = (int[][][])null;

        if (PartyController.instance().getParty(e.entityPlayer) != null && DungeonHandler.instance().isInDungeon(party) && dungeon.hasStarted() && dungeon != null && dungeon.hasMember(PartyController.instance().getMember(e.entityPlayer)))
        {
            if (e.action != PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK)
            {
                return;
            }

            ItemStack currentStack = e.entityPlayer.getCurrentEquippedItem();

            if (currentStack != null && currentStack.getItem() instanceof ItemBlock)
            {
                switch (e.face)
                {
                    case 0:
                        if (e.entityPlayer.worldObj.getBlockId(e.x, e.y - 1, e.z) == 0)
                        {
                            dungeon.registerBlockPlacement(e.x, e.y - 1, e.z);
                        }

                        break;

                    case 1:
                        if (e.entityPlayer.worldObj.getBlockId(e.x, e.y + 1, e.z) == 0)
                        {
                            dungeon.registerBlockPlacement(e.x, e.y + 1, e.z);
                        }

                        break;

                    case 2:
                        if (e.entityPlayer.worldObj.getBlockId(e.x, e.y, e.z - 1) == 0)
                        {
                            dungeon.registerBlockPlacement(e.x, e.y, e.z - 1);
                        }

                        break;

                    case 3:
                        if (e.entityPlayer.worldObj.getBlockId(e.x, e.y, e.z + 1) == 0)
                        {
                            dungeon.registerBlockPlacement(e.x, e.y, e.z + 1);
                        }

                        break;

                    case 4:
                        if (e.entityPlayer.worldObj.getBlockId(e.x - 1, e.y, e.z) == 0)
                        {
                            dungeon.registerBlockPlacement(e.x - 1, e.y, e.z);
                        }

                        break;

                    case 5:
                        if (e.entityPlayer.worldObj.getBlockId(e.x + 1, e.y, e.z) == 0)
                        {
                            dungeon.registerBlockPlacement(e.x + 1, e.y, e.z);
                        }
                }
            }
        }
    }

    private void handleAerogelPlacement(PlayerInteractEvent e)
    {
        if (e.action == PlayerInteractEvent.Action.RIGHT_CLICK_AIR)
        {
            if (e.entityPlayer.dimension == 3)
            {
                ItemStack currentStack = e.entityPlayer.getCurrentEquippedItem();

                if (currentStack != null && currentStack.itemID == Item.bucketLava.itemID)
                {
                    if (e.entityPlayer.worldObj.isRemote)
                    {
                        ;
                    }

                    float var4 = 1.0F;
                    double var10000 = e.entityPlayer.prevPosX + (e.entityPlayer.posX - e.entityPlayer.prevPosX) * (double)var4;
                    double var7 = e.entityPlayer.prevPosY + (e.entityPlayer.posY - e.entityPlayer.prevPosY) * (double)var4 + 1.62D - (double)e.entityPlayer.yOffset;
                    var10000 = e.entityPlayer.prevPosZ + (e.entityPlayer.posZ - e.entityPlayer.prevPosZ) * (double)var4;
                    boolean var11 = false;
                    MovingObjectPosition var12 = this.getMovingObjectPositionFromPlayer(e.entityPlayer.worldObj, e.entityPlayer, var11);

                    if (var12 != null && var12.typeOfHit == EnumMovingObjectType.TILE)
                    {
                        int var13 = var12.blockX;
                        int var14 = var12.blockY;
                        int var15 = var12.blockZ;

                        if (e.entityPlayer.worldObj.canMineBlock(e.entityPlayer, var13, var14, var15))
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

                            if (e.entityPlayer.canPlayerEdit(var13, var14, var15, var12.sideHit, currentStack))
                            {
                                if (e.entityPlayer.worldObj.isAirBlock(var13, var14, var15) || !e.entityPlayer.worldObj.getBlockMaterial(var13, var14, var15).isSolid())
                                {
                                    if (!e.entityPlayer.capabilities.isCreativeMode)
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

    private void handlePortalActivation(PlayerInteractEvent e)
    {
        if (e.action == PlayerInteractEvent.Action.RIGHT_CLICK_AIR)
        {
            Party party = PartyController.instance().getParty(e.entityPlayer);

            if (party == null || DungeonHandler.instance().getDungeon(party) == null)
            {
                ItemStack currentStack = e.entityPlayer.getCurrentEquippedItem();

                if (currentStack != null && (currentStack.itemID == Item.bucketWater.itemID || currentStack.itemID == AetherItems.SkyrootBucket.itemID || currentStack.getItemDamage() == 8))
                {
                    float var4 = 1.0F;
                    double var10000 = e.entityPlayer.prevPosX + (e.entityPlayer.posX - e.entityPlayer.prevPosX) * (double)var4;
                    double var7 = e.entityPlayer.prevPosY + (e.entityPlayer.posY - e.entityPlayer.prevPosY) * (double)var4 + 1.62D - (double)e.entityPlayer.yOffset;
                    var10000 = e.entityPlayer.prevPosZ + (e.entityPlayer.posZ - e.entityPlayer.prevPosZ) * (double)var4;
                    boolean var11 = false;
                    MovingObjectPosition var12 = this.getMovingObjectPositionFromPlayer(e.entityPlayer.worldObj, e.entityPlayer, var11);

                    if (var12 != null && var12.typeOfHit == EnumMovingObjectType.TILE)
                    {
                        int var13 = var12.blockX;
                        int var14 = var12.blockY;
                        int var15 = var12.blockZ;

                        if (e.entityPlayer.worldObj.getBlockId(var13, var14, var15) == Block.glowStone.blockID)
                        {
                            if (e.entityPlayer.worldObj.canMineBlock(e.entityPlayer, var13, var14, var15))
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

                                if (e.entityPlayer.canPlayerEdit(var13, var14, var15, var12.sideHit, currentStack))
                                {
                                    if (e.entityPlayer.worldObj.isAirBlock(var13, var14, var15) || !e.entityPlayer.worldObj.getBlockMaterial(var13, var14, var15).isSolid())
                                    {
                                        if (!e.entityPlayer.worldObj.isRemote && AetherBlocks.AetherPortal.tryToCreatePortal(e.entityPlayer.worldObj, var13, var14, var15))
                                        {
                                            e.setCanceled(true);

                                            if (currentStack == new ItemStack(AetherItems.SkyrootBucket.itemID, 1, Block.waterMoving.blockID))
                                            {
                                                new ItemStack(AetherItems.SkyrootBucket.itemID, 1, 0);
                                                return;
                                            }

                                            if (!e.entityPlayer.capabilities.isCreativeMode)
                                            {
                                                currentStack.itemID = Item.bucketEmpty.itemID;
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

    private MovingObjectPosition getMovingObjectPositionFromPlayer(World par1World, EntityPlayer par2EntityPlayer, boolean par3)
    {
        float var4 = 1.0F;
        float var5 = par2EntityPlayer.prevRotationPitch + (par2EntityPlayer.rotationPitch - par2EntityPlayer.prevRotationPitch) * var4;
        float var6 = par2EntityPlayer.prevRotationYaw + (par2EntityPlayer.rotationYaw - par2EntityPlayer.prevRotationYaw) * var4;
        double var7 = par2EntityPlayer.prevPosX + (par2EntityPlayer.posX - par2EntityPlayer.prevPosX) * (double)var4;
        double var9 = par2EntityPlayer.prevPosY + (par2EntityPlayer.posY - par2EntityPlayer.prevPosY) * (double)var4 + 1.62D - (double)par2EntityPlayer.yOffset;
        double var11 = par2EntityPlayer.prevPosZ + (par2EntityPlayer.posZ - par2EntityPlayer.prevPosZ) * (double)var4;
        Vec3 var13 = par1World.getWorldVec3Pool().getVecFromPool(var7, var9, var11);
        float var14 = MathHelper.cos(-var6 * 0.017453292F - (float)Math.PI);
        float var15 = MathHelper.sin(-var6 * 0.017453292F - (float)Math.PI);
        float var16 = -MathHelper.cos(-var5 * 0.017453292F);
        float var17 = MathHelper.sin(-var5 * 0.017453292F);
        float var18 = var15 * var16;
        float var20 = var14 * var16;
        double var21 = 5.0D;

        if (par2EntityPlayer instanceof EntityPlayerMP)
        {
            var21 = ((EntityPlayerMP)par2EntityPlayer).theItemInWorldManager.getBlockReachDistance();
        }

        Vec3 var23 = var13.addVector((double)var18 * var21, (double)var17 * var21, (double)var20 * var21);
        return par1World.rayTraceBlocks_do_do(var13, var23, par3, !par3);
    }
}
