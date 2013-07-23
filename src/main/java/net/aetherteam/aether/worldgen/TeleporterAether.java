package net.aetherteam.aether.worldgen;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.aetherteam.aether.blocks.AetherBlocks;
import net.aetherteam.aether.blocks.BlockAether;
import net.aetherteam.aether.dungeons.Dungeon;
import net.aetherteam.aether.dungeons.DungeonHandler;
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

    /** Stores successful portal placement locations for rapid lookup. */
    private final LongHashMap destinationCoordinateCache = new LongHashMap();

    /**
     * A list of valid keys for the destinationCoordainteCache. These are based on the X & Z of the players initial
     * location.
     */
    private final List destinationCoordinateKeys = new ArrayList();
    private boolean createPortal;

    public TeleporterAether(WorldServer var1)
    {
        super(var1);
        this.worldServerInstance = var1;
        this.worldServerInstance.customTeleporters.add(this);
        this.random = new Random(var1.getSeed());
        this.createPortal = true;
    }

    public TeleporterAether(WorldServer var1, boolean var2)
    {
        super(var1);
        this.worldServerInstance = var1;
        this.worldServerInstance.customTeleporters.add(this);
        this.random = new Random(var1.getSeed());
        this.createPortal = false;
    }

    /**
     * Place an entity in a nearby portal, creating one if necessary.
     */
    public void placeInPortal(Entity var1, double var2, double var4, double var6, float var8)
    {
        if (!this.placeInExistingPortal(var1, var2, var4, var6, var8))
        {
            if (this.createPortal = true)
            {
                this.makePortal(var1);
            }

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
        double var20;
        int var22;
        int var28;
        double var46;

        if (this.destinationCoordinateCache.containsItem(var17))
        {
            AetherPortalPosition var23 = (AetherPortalPosition)this.destinationCoordinateCache.getValueByKey(var17);
            var10 = 0.0D;
            var12 = var23.posX;
            var13 = var23.posY;
            var14 = var23.posZ;
            var23.lastUpdateTime = this.worldServerInstance.getTotalWorldTime();
            var19 = false;
        }
        else
        {
            for (var22 = var15 - var9; var22 <= var15 + var9; ++var22)
            {
                var46 = (double)var22 + 0.5D - var1.posX;

                for (int var25 = var16 - var9; var25 <= var16 + var9; ++var25)
                {
                    double var26 = (double)var25 + 0.5D - var1.posZ;

                    for (var28 = this.worldServerInstance.getActualHeight() - 1; var28 >= 0; --var28)
                    {
                        if (this.worldServerInstance.getBlockId(var22, var28, var25) == AetherBlocks.AetherPortal.blockID)
                        {
                            while (this.worldServerInstance.getBlockId(var22, var28 - 1, var25) == AetherBlocks.AetherPortal.blockID)
                            {
                                --var28;
                            }

                            var20 = (double)var28 + 0.5D - var1.posY;
                            double var29 = var46 * var46 + var20 * var20 + var26 * var26;

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

            var46 = (double)var12 + 0.5D;
            double var47 = (double)var13 + 0.5D;
            var20 = (double)var14 + 0.5D;
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
                int var48 = Direction.rotateLeft[var27];
                int var30 = Direction.offsetX[var27];
                int var31 = Direction.offsetZ[var27];
                int var32 = Direction.offsetX[var48];
                int var33 = Direction.offsetZ[var48];
                boolean var34 = !this.invalidSpawn(var12 + var30 + var32, var13, var14 + var31 + var33) || !this.invalidSpawn(var12 + var30 + var32, var13 + 1, var14 + var31 + var33);
                boolean var35 = !this.invalidSpawn(var12 + var30, var13, var14 + var31) || !this.invalidSpawn(var12 + var30, var13 + 1, var14 + var31);

                if (var34 && var35)
                {
                    var27 = Direction.rotateOpposite[var27];
                    var48 = Direction.rotateOpposite[var48];
                    var30 = Direction.offsetX[var27];
                    var31 = Direction.offsetZ[var27];
                    var32 = Direction.offsetX[var48];
                    var33 = Direction.offsetZ[var48];
                    var22 = var12 - var32;
                    var46 -= (double)var32;
                    int var36 = var14 - var33;
                    var20 -= (double)var33;
                    var34 = !this.invalidSpawn(var22 + var30 + var32, var13, var36 + var31 + var33) || !this.invalidSpawn(var22 + var30 + var32, var13 + 1, var36 + var31 + var33);
                    var35 = !this.invalidSpawn(var22 + var30, var13, var36 + var31) || !this.invalidSpawn(var22 + var30, var13 + 1, var36 + var31);
                }

                float var49 = 0.5F;
                float var37 = 0.5F;

                if (!var34 && var35)
                {
                    var49 = 1.0F;
                }
                else if (var34 && !var35)
                {
                    var49 = 0.0F;
                }
                else if (var34 && var35)
                {
                    var37 = 0.0F;
                }

                var46 += (double)((float)var32 * var49 + var37 * (float)var30);
                var20 += (double)((float)var33 * var49 + var37 * (float)var31);
                float var38 = 0.0F;
                float var39 = 0.0F;
                float var40 = 0.0F;
                float var41 = 0.0F;

                if (var27 == var28)
                {
                    var38 = 1.0F;
                    var39 = 1.0F;
                }
                else if (var27 == Direction.rotateOpposite[var28])
                {
                    var38 = -1.0F;
                    var39 = -1.0F;
                }
                else if (var27 == Direction.rotateRight[var28])
                {
                    var40 = 1.0F;
                    var41 = -1.0F;
                }
                else
                {
                    var40 = -1.0F;
                    var41 = 1.0F;
                }

                double var42 = var1.motionX;
                double var44 = var1.motionZ;
                var1.motionX = var42 * (double)var38 + var44 * (double)var41;
                var1.motionZ = var42 * (double)var40 + var44 * (double)var39;
                var1.rotationYaw = var8 - (float)(var28 * 90) + (float)(var27 * 90);
            }
            else
            {
                var1.motionX = var1.motionY = var1.motionZ = 0.0D;
            }

            var1.setLocationAndAngles(var46, var47, var20, var1.rotationYaw, var1.rotationPitch);
            return true;
        }
        else
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
        int var13;
        double var14;
        double var16;
        int var19;
        int var18;
        int var21;
        int var20;
        int var23;
        int var22;
        int var25;
        int var24;
        int var27;
        int var26;
        double var28;
        double var30;
        int var32;

        for (var13 = var5 - var2; var13 <= var5 + var2; ++var13)
        {
            var14 = (double)var13 + 0.5D - var1.posX;

            for (var18 = var7 - var2; var18 <= var7 + var2; ++var18)
            {
                var16 = (double)var18 + 0.5D - var1.posZ;
                label272:

                for (var19 = this.worldServerInstance.getActualHeight() - 1; var19 >= 0; --var19)
                {
                    if (this.invalidSpawn(var13, var19, var18))
                    {
                        while (var19 > 0 && this.invalidSpawn(var13, var19 - 1, var18))
                        {
                            --var19;
                        }

                        for (var21 = var12; var21 < var12 + 4; ++var21)
                        {
                            var20 = var21 % 2;
                            var23 = 1 - var20;

                            if (var21 % 4 >= 2)
                            {
                                var20 = -var20;
                                var23 = -var23;
                            }

                            for (var22 = 0; var22 < 3; ++var22)
                            {
                                for (var25 = 0; var25 < 4; ++var25)
                                {
                                    for (var24 = -1; var24 < 4; ++var24)
                                    {
                                        var27 = var13 + (var25 - 1) * var20 + var22 * var23;
                                        var26 = var19 + var24;
                                        var32 = var18 + (var25 - 1) * var23 - var22 * var20;

                                        if (var24 < 0 && !this.worldServerInstance.getBlockMaterial(var27, var26, var32).isSolid() || var24 >= 0 && !this.invalidSpawn(var27, var26, var32))
                                        {
                                            continue label272;
                                        }
                                    }
                                }
                            }

                            var30 = (double)var19 + 0.5D - var1.posY;
                            var28 = var14 * var14 + var30 * var30 + var16 * var16;

                            if (var3 < 0.0D || var28 < var3)
                            {
                                var3 = var28;
                                var8 = var13;
                                var9 = var19;
                                var10 = var18;
                                var11 = var21 % 4;
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
                var14 = (double)var13 + 0.5D - var1.posX;

                for (var18 = var7 - var2; var18 <= var7 + var2; ++var18)
                {
                    var16 = (double)var18 + 0.5D - var1.posZ;
                    label220:

                    for (var19 = this.worldServerInstance.getActualHeight() - 1; var19 >= 0; --var19)
                    {
                        if (this.invalidSpawn(var13, var19, var18))
                        {
                            while (var19 > 0 && this.invalidSpawn(var13, var19 - 1, var18))
                            {
                                --var19;
                            }

                            for (var21 = var12; var21 < var12 + 2; ++var21)
                            {
                                var20 = var21 % 2;
                                var23 = 1 - var20;

                                for (var22 = 0; var22 < 4; ++var22)
                                {
                                    for (var25 = -1; var25 < 4; ++var25)
                                    {
                                        var24 = var13 + (var22 - 1) * var20;
                                        var27 = var19 + var25;
                                        var26 = var18 + (var22 - 1) * var23;

                                        if (var25 < 0 && !this.worldServerInstance.getBlockMaterial(var24, var27, var26).isSolid() || var25 >= 0 && !this.invalidSpawn(var24, var27, var26))
                                        {
                                            continue label220;
                                        }
                                    }
                                }

                                var30 = (double)var19 + 0.5D - var1.posY;
                                var28 = var14 * var14 + var30 * var30 + var16 * var16;

                                if (var3 < 0.0D || var28 < var3)
                                {
                                    var3 = var28;
                                    var8 = var13;
                                    var9 = var19;
                                    var10 = var18;
                                    var11 = var21 % 2;
                                }
                            }
                        }
                    }
                }
            }
        }

        var32 = var8;
        int var33 = var9;
        var18 = var10;
        int var34 = var11 % 2;
        int var35 = 1 - var34;

        if (var11 % 4 >= 2)
        {
            var34 = -var34;
            var35 = -var35;
        }

        boolean var36;

        if (var3 < 0.0D)
        {
            if (var9 < 70)
            {
                var9 = 70;
            }

            if (var9 > this.worldServerInstance.getActualHeight() - 10)
            {
                var9 = this.worldServerInstance.getActualHeight() - 10;
            }

            var33 = var9;

            for (var19 = -1; var19 <= 1; ++var19)
            {
                for (var21 = 1; var21 < 3; ++var21)
                {
                    for (var20 = -1; var20 < 3; ++var20)
                    {
                        var23 = var32 + (var21 - 1) * var34 + var19 * var35;
                        var22 = var33 + var20;
                        var25 = var18 + (var21 - 1) * var35 - var19 * var34;
                        var36 = var20 < 0;
                        this.worldServerInstance.setBlock(var23, var22, var25, var36 ? Block.glowStone.blockID : 0);
                    }
                }
            }
        }

        for (var19 = 0; var19 < 4; ++var19)
        {
            for (var21 = 0; var21 < 4; ++var21)
            {
                for (var20 = -1; var20 < 4; ++var20)
                {
                    var23 = var32 + (var21 - 1) * var34;
                    var22 = var33 + var20;
                    var25 = var18 + (var21 - 1) * var35;
                    var36 = var21 == 0 || var21 == 3 || var20 == -1 || var20 == 3;
                    this.worldServerInstance.setBlock(var23, var22, var25, var36 ? Block.glowStone.blockID : AetherBlocks.AetherPortal.blockID, 0, 2);
                }
            }

            for (var21 = 0; var21 < 4; ++var21)
            {
                for (var20 = -1; var20 < 4; ++var20)
                {
                    var23 = var32 + (var21 - 1) * var34;
                    var22 = var33 + var20;
                    var25 = var18 + (var21 - 1) * var35;
                    this.worldServerInstance.notifyBlocksOfNeighborChange(var23, var22, var25, this.worldServerInstance.getBlockId(var23, var22, var25));
                }
            }
        }

        return true;
    }

    private boolean invalidSpawn(int var1, int var2, int var3)
    {
        int var4 = this.worldServerInstance.getBlockId(var1, var2, var3);
        Dungeon var5 = DungeonHandler.instance().getInstanceAt(var1, var2, var3);
        return this.worldServerInstance.isAirBlock(var1, var2, var3) || Block.blocksList[var4] != null && Block.blocksList[var4] instanceof BlockAether && ((BlockAether)Block.blocksList[var4]).isDungeonBlock() || var5 != null || Block.blocksList[var4] != null && !Block.blocksList[var4].isOpaqueCube();
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
                Long var6 = (Long)var3.next();
                AetherPortalPosition var7 = (AetherPortalPosition)this.destinationCoordinateCache.getValueByKey(var6.longValue());

                if (var7 == null || var7.lastUpdateTime < var4)
                {
                    var3.remove();
                    this.destinationCoordinateCache.remove(var6.longValue());
                }
            }
        }
    }
}
