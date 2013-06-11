package net.aetherteam.aether.worldgen;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.aetherteam.aether.blocks.AetherBlocks;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.util.Direction;
import net.minecraft.util.LongHashMap;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class TeleporterAether extends Teleporter
{
    private final WorldServer worldServerInstance;
    private final Random random;

    /**
     * Stores successful portal placement locations for rapid lookup.
     */
    private final LongHashMap destinationCoordinateCache = new LongHashMap();

    /**
     * A list of valid keys for the destinationCoordainteCache. These are based on the X & Z of the players initial
     * location.
     */
    private final List destinationCoordinateKeys = new ArrayList();

    public TeleporterAether(WorldServer var1)
    {
        super(var1);
        this.worldServerInstance = var1;
        this.worldServerInstance.customTeleporters.add(this);
        this.random = new Random(var1.getSeed());
    }

    /**
     * Place an entity in a nearby portal, creating one if necessary.
     */
    public void placeInPortal(Entity var1, double var2, double var4, double var6, float var8)
    {
        if (!this.placeInExistingPortal(var1, var2, var4, var6, var8))
        {
            this.makePortal(var1);
            this.placeInExistingPortal(var1, var2, var4, var6, var8);
        }
    }

    /**
     * Place an entity in a nearby portal which already exists.
     */
    public boolean placeInExistingPortal(Entity var1, double var2, double var4, double var6, float var8)
    {
        short var9 = 128;
        double var10 = -1.0D;
        int var12 = 0;
        int var13 = 0;
        int var14 = 0;
        int var15 = MathHelper.floor_double(var1.posX);
        int var16 = MathHelper.floor_double(var1.posZ);
        long var17 = ChunkCoordIntPair.chunkXZ2Int(var15, var16);
        boolean var19 = true;
        int var28;
        double var20;
        int var22;
        double var48;

        if (this.destinationCoordinateCache.containsItem(var17))
        {
            AetherPortalPosition var23 = (AetherPortalPosition) this.destinationCoordinateCache.getValueByKey(var17);
            var10 = 0.0D;
            var12 = var23.posX;
            var13 = var23.posY;
            var14 = var23.posZ;
            var23.lastUpdateTime = this.worldServerInstance.getTotalWorldTime();
            var19 = false;
        } else
        {
            for (var22 = var15 - var9; var22 <= var15 + var9; ++var22)
            {
                var48 = (double) var22 + 0.5D - var1.posX;

                for (int var25 = var16 - var9; var25 <= var16 + var9; ++var25)
                {
                    double var26 = (double) var25 + 0.5D - var1.posZ;

                    for (var28 = this.worldServerInstance.getActualHeight() - 1; var28 >= 0; --var28)
                    {
                        if (this.worldServerInstance.getBlockId(var22, var28, var25) == AetherBlocks.AetherPortal.blockID)
                        {
                            while (this.worldServerInstance.getBlockId(var22, var28 - 1, var25) == AetherBlocks.AetherPortal.blockID)
                            {
                                --var28;
                            }

                            var20 = (double) var28 + 0.5D - var1.posY;
                            double var29 = var48 * var48 + var20 * var20 + var26 * var26;

                            if (var10 < 0.0D || var29 < var10)
                            {
                                var10 = var29;
                                var12 = var22;
                                var13 = var28;
                                var14 = var25;
                            }
                        }
                    }
                }
            }
        }

        if (var10 >= 0.0D)
        {
            if (var19)
            {
                this.destinationCoordinateCache.add(var17, new AetherPortalPosition(this, var12, var13, var14, this.worldServerInstance.getTotalWorldTime()));
                this.destinationCoordinateKeys.add(Long.valueOf(var17));
            }

            var48 = (double) var12 + 0.5D;
            double var47 = (double) var13 + 0.5D;
            var20 = (double) var14 + 0.5D;
            int var27 = -1;

            if (this.worldServerInstance.getBlockId(var12 - 1, var13, var14) == AetherBlocks.AetherPortal.blockID)
            {
                var27 = 2;
            }

            if (this.worldServerInstance.getBlockId(var12 + 1, var13, var14) == AetherBlocks.AetherPortal.blockID)
            {
                var27 = 0;
            }

            if (this.worldServerInstance.getBlockId(var12, var13, var14 - 1) == AetherBlocks.AetherPortal.blockID)
            {
                var27 = 3;
            }

            if (this.worldServerInstance.getBlockId(var12, var13, var14 + 1) == AetherBlocks.AetherPortal.blockID)
            {
                var27 = 1;
            }

            var28 = var1.getTeleportDirection();

            if (var27 > -1)
            {
                int var49 = Direction.rotateLeft[var27];
                int var30 = Direction.offsetX[var27];
                int var31 = Direction.offsetZ[var27];
                int var32 = Direction.offsetX[var49];
                int var33 = Direction.offsetZ[var49];
                boolean var34 = !this.worldServerInstance.isAirBlock(var12 + var30 + var32, var13, var14 + var31 + var33) || !this.worldServerInstance.isAirBlock(var12 + var30 + var32, var13 + 1, var14 + var31 + var33);
                boolean var35 = !this.worldServerInstance.isAirBlock(var12 + var30, var13, var14 + var31) || !this.worldServerInstance.isAirBlock(var12 + var30, var13 + 1, var14 + var31);

                if (var34 && var35)
                {
                    var27 = Direction.rotateOpposite[var27];
                    var49 = Direction.rotateOpposite[var49];
                    var30 = Direction.offsetX[var27];
                    var31 = Direction.offsetZ[var27];
                    var32 = Direction.offsetX[var49];
                    var33 = Direction.offsetZ[var49];
                    var22 = var12 - var32;
                    var48 -= (double) var32;
                    int var36 = var14 - var33;
                    var20 -= (double) var33;
                    var34 = !this.worldServerInstance.isAirBlock(var22 + var30 + var32, var13, var36 + var31 + var33) || !this.worldServerInstance.isAirBlock(var22 + var30 + var32, var13 + 1, var36 + var31 + var33);
                    var35 = !this.worldServerInstance.isAirBlock(var22 + var30, var13, var36 + var31) || !this.worldServerInstance.isAirBlock(var22 + var30, var13 + 1, var36 + var31);
                }

                float var46 = 0.5F;
                float var37 = 0.5F;

                if (!var34 && var35)
                {
                    var46 = 1.0F;
                } else if (var34 && !var35)
                {
                    var46 = 0.0F;
                } else if (var34 && var35)
                {
                    var37 = 0.0F;
                }

                var48 += (double) ((float) var32 * var46 + var37 * (float) var30);
                var20 += (double) ((float) var33 * var46 + var37 * (float) var31);
                float var38 = 0.0F;
                float var39 = 0.0F;
                float var40 = 0.0F;
                float var41 = 0.0F;

                if (var27 == var28)
                {
                    var38 = 1.0F;
                    var39 = 1.0F;
                } else if (var27 == Direction.rotateOpposite[var28])
                {
                    var38 = -1.0F;
                    var39 = -1.0F;
                } else if (var27 == Direction.rotateRight[var28])
                {
                    var40 = 1.0F;
                    var41 = -1.0F;
                } else
                {
                    var40 = -1.0F;
                    var41 = 1.0F;
                }

                double var42 = var1.motionX;
                double var44 = var1.motionZ;
                var1.motionX = var42 * (double) var38 + var44 * (double) var41;
                var1.motionZ = var42 * (double) var40 + var44 * (double) var39;
                var1.rotationYaw = var8 - (float) (var28 * 90) + (float) (var27 * 90);
            } else
            {
                var1.motionX = var1.motionY = var1.motionZ = 0.0D;
            }

            var1.setLocationAndAngles(var48, var47, var20, var1.rotationYaw, var1.rotationPitch);
            return true;
        } else
        {
            return false;
        }
    }

    public boolean makePortal(Entity var1)
    {
        byte var2 = 16;
        double var3 = -1.0D;
        int var5 = MathHelper.floor_double(var1.posX);
        int var6 = MathHelper.floor_double(var1.posY);
        int var7 = MathHelper.floor_double(var1.posZ);
        int var8 = var5;
        int var9 = var6;
        int var10 = var7;
        int var11 = 0;
        int var12 = this.random.nextInt(4);
        double var32;
        double var33;
        int var23;
        int var22;
        int var21;
        int var20;
        int var19;
        double var17;
        int var16;
        int var27;
        int var26;
        int var25;
        int var24;
        double var14;
        int var13;

        for (var13 = var5 - var2; var13 <= var5 + var2; ++var13)
        {
            var14 = (double) var13 + 0.5D - var1.posX;

            for (var16 = var7 - var2; var16 <= var7 + var2; ++var16)
            {
                var17 = (double) var16 + 0.5D - var1.posZ;
                label266:

                for (var19 = 127; var19 >= 0; --var19)
                {
                    if (this.worldServerInstance.isAirBlock(var13, var19, var16))
                    {
                        while (var19 > 0 && this.worldServerInstance.isAirBlock(var13, var19 - 1, var16))
                        {
                            --var19;
                        }

                        for (var20 = var12; var20 < var12 + 4; ++var20)
                        {
                            var21 = var20 % 2;
                            var22 = 1 - var21;

                            if (var20 % 4 >= 2)
                            {
                                var21 = -var21;
                                var22 = -var22;
                            }

                            for (var23 = 0; var23 < 3; ++var23)
                            {
                                for (var24 = 0; var24 < 4; ++var24)
                                {
                                    for (var25 = -1; var25 < 4; ++var25)
                                    {
                                        var26 = var13 + (var24 - 1) * var21 + var23 * var22;
                                        var27 = var19 + var25;
                                        int var28 = var16 + (var24 - 1) * var22 - var23 * var21;

                                        if (!this.blockIsGood(this.worldServerInstance.getBlockId(var26, var27, var28), this.worldServerInstance.getBlockMetadata(var26, var27, var28)) || !this.worldServerInstance.isAirBlock(var26, var27, var28))
                                        {
                                            continue label266;
                                        }
                                    }
                                }
                            }

                            var32 = (double) var19 + 0.5D - var1.posY;
                            var33 = var14 * var14 + var32 * var32 + var17 * var17;

                            if (var3 < 0.0D || var33 < var3)
                            {
                                var3 = var33;
                                var8 = var13;
                                var9 = var19;
                                var10 = var16;
                                var11 = var20 % 4;
                            }
                        }
                    }
                }
            }
        }

        if (var3 < 0.0D)
        {
            for (var13 = var5 - var2; var13 <= var5 + var2; ++var13)
            {
                var14 = (double) var13 + 0.5D - var1.posX;

                for (var16 = var7 - var2; var16 <= var7 + var2; ++var16)
                {
                    var17 = (double) var16 + 0.5D - var1.posZ;
                    label216:

                    for (var19 = 127; var19 >= 0; --var19)
                    {
                        if (this.worldServerInstance.isAirBlock(var13, var19, var16))
                        {
                            while (this.worldServerInstance.isAirBlock(var13, var19 - 1, var16) && var19 > 0)
                            {
                                --var19;
                            }

                            for (var20 = var12; var20 < var12 + 2; ++var20)
                            {
                                var21 = var20 % 2;
                                var22 = 1 - var21;

                                for (var23 = 0; var23 < 4; ++var23)
                                {
                                    for (var24 = -1; var24 < 4; ++var24)
                                    {
                                        var25 = var13 + (var23 - 1) * var21;
                                        var26 = var19 + var24;
                                        var27 = var16 + (var23 - 1) * var22;

                                        if (!this.blockIsGood(this.worldServerInstance.getBlockId(var25, var26, var27), this.worldServerInstance.getBlockMetadata(var25, var26, var27)) || !this.worldServerInstance.isAirBlock(var25, var26, var27))
                                        {
                                            continue label216;
                                        }
                                    }
                                }

                                var32 = (double) var19 + 0.5D - var1.posY;
                                var33 = var14 * var14 + var32 * var32 + var17 * var17;

                                if (var3 < 0.0D || var33 < var3)
                                {
                                    var3 = var33;
                                    var8 = var13;
                                    var9 = var19;
                                    var10 = var16;
                                    var11 = var20 % 2;
                                }
                            }
                        }
                    }
                }
            }
        }

        int var30 = var8;
        int var15 = var9;
        var16 = var10;
        int var29 = var11 % 2;
        int var18 = 1 - var29;

        if (var11 % 4 >= 2)
        {
            var29 = -var29;
            var18 = -var18;
        }

        boolean var31;

        if (var3 < 0.0D)
        {
            if (var9 < 70)
            {
                var9 = 70;
            }

            if (var9 > 118)
            {
                var9 = 118;
            }

            var15 = var9;

            for (var19 = -1; var19 <= 1; ++var19)
            {
                for (var20 = 1; var20 < 3; ++var20)
                {
                    for (var21 = -1; var21 < 3; ++var21)
                    {
                        var22 = var30 + (var20 - 1) * var29 + var19 * var18;
                        var23 = var15 + var21;
                        var24 = var16 + (var20 - 1) * var18 - var19 * var29;
                        var31 = var21 < 0;
                        this.worldServerInstance.setBlock(var22, var23, var24, var31 ? Block.glowStone.blockID : 0);
                    }
                }
            }
        }

        for (var19 = 0; var19 < 4; ++var19)
        {
            for (var20 = 0; var20 < 4; ++var20)
            {
                for (var21 = -1; var21 < 4; ++var21)
                {
                    var22 = var30 + (var20 - 1) * var29;
                    var23 = var15 + var21;
                    var24 = var16 + (var20 - 1) * var18;
                    var31 = var20 == 0 || var20 == 3 || var21 == -1 || var21 == 3;
                    this.worldServerInstance.setBlock(var22, var23, var24, var31 ? Block.glowStone.blockID : AetherBlocks.AetherPortal.blockID, 0, 2);
                }
            }

            for (var20 = 0; var20 < 4; ++var20)
            {
                for (var21 = -1; var21 < 4; ++var21)
                {
                    var22 = var30 + (var20 - 1) * var29;
                    var23 = var15 + var21;
                    var24 = var16 + (var20 - 1) * var18;
                    this.worldServerInstance.notifyBlocksOfNeighborChange(var22, var23, var24, this.worldServerInstance.getBlockId(var22, var23, var24));
                }
            }
        }

        return true;
    }

    public boolean blockIsGood(int var1, int var2)
    {
        return var1 == 0 ? false : (!Block.blocksList[var1].blockMaterial.isSolid() ? false : var1 != AetherBlocks.Aercloud.blockID);
    }

    /**
     * called periodically to remove out-of-date portal locations from the cache list. Argument par1 is a
     * WorldServer.getTotalWorldTime() value.
     */
    public void removeStalePortalLocations(long var1)
    {
        if (var1 % 100L == 0L)
        {
            Iterator var3 = this.destinationCoordinateKeys.iterator();
            long var4 = var1 - 600L;

            while (var3.hasNext())
            {
                Long var6 = (Long) var3.next();
                AetherPortalPosition var7 = (AetherPortalPosition) this.destinationCoordinateCache.getValueByKey(var6.longValue());

                if (var7 == null || var7.lastUpdateTime < var4)
                {
                    var3.remove();
                    this.destinationCoordinateCache.remove(var6.longValue());
                }
            }
        }
    }
}
