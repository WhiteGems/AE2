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

    /** A private Random() function in Teleporter */
    private final Random random;

    /** Stores successful portal placement locations for rapid lookup. */
    private final LongHashMap destinationCoordinateCache = new LongHashMap();

    /**
     * A list of valid keys for the destinationCoordainteCache. These are based on the X & Z of the players initial
     * location.
     */
    private final List destinationCoordinateKeys = new ArrayList();
    private boolean createPortal;

    public TeleporterAether(WorldServer par1WorldServer)
    {
        super(par1WorldServer);
        this.worldServerInstance = par1WorldServer;
        this.worldServerInstance.customTeleporters.add(this);
        this.random = new Random(par1WorldServer.getSeed());
        this.createPortal = true;
    }

    public TeleporterAether(WorldServer par1WorldServer, boolean createPortal)
    {
        super(par1WorldServer);
        this.worldServerInstance = par1WorldServer;
        this.worldServerInstance.customTeleporters.add(this);
        this.random = new Random(par1WorldServer.getSeed());
        this.createPortal = false;
    }

    /**
     * Place an entity in a nearby portal, creating one if necessary.
     */
    public void placeInPortal(Entity par1Entity, double par2, double par4, double par6, float par8)
    {
        if (!this.placeInExistingPortal(par1Entity, par2, par4, par6, par8))
        {
            if (this.createPortal = true)
            {
                this.makePortal(par1Entity);
            }

            this.placeInExistingPortal(par1Entity, par2, par4, par6, par8);
        }
    }

    /**
     * Place an entity in a nearby portal which already exists.
     */
    public boolean placeInExistingPortal(Entity par1Entity, double par2, double par4, double par6, float par8)
    {
        short short1 = 128;
        double d3 = -1.0D;
        int i = 0;
        int j = 0;
        int k = 0;
        int l = MathHelper.floor_double(par1Entity.posX);
        int i1 = MathHelper.floor_double(par1Entity.posZ);
        long j1 = ChunkCoordIntPair.chunkXZ2Int(l, i1);
        boolean flag = true;
        double d4;
        int k1;
        int k2;
        double var46;

        if (this.destinationCoordinateCache.containsItem(j1))
        {
            AetherPortalPosition d8 = (AetherPortalPosition)this.destinationCoordinateCache.getValueByKey(j1);
            d3 = 0.0D;
            i = d8.posX;
            j = d8.posY;
            k = d8.posZ;
            d8.lastUpdateTime = this.worldServerInstance.getTotalWorldTime();
            flag = false;
        }
        else
        {
            for (k1 = l - short1; k1 <= l + short1; ++k1)
            {
                var46 = (double)k1 + 0.5D - par1Entity.posX;

                for (int d9 = i1 - short1; d9 <= i1 + short1; ++d9)
                {
                    double d6 = (double)d9 + 0.5D - par1Entity.posZ;

                    for (k2 = this.worldServerInstance.getActualHeight() - 1; k2 >= 0; --k2)
                    {
                        if (this.worldServerInstance.getBlockId(k1, k2, d9) == AetherBlocks.AetherPortal.blockID)
                        {
                            while (this.worldServerInstance.getBlockId(k1, k2 - 1, d9) == AetherBlocks.AetherPortal.blockID)
                            {
                                --k2;
                            }

                            d4 = (double)k2 + 0.5D - par1Entity.posY;
                            double l2 = var46 * var46 + d4 * d4 + d6 * d6;

                            if (d3 < 0.0D || l2 < d3)
                            {
                                d3 = l2;
                                i = k1;
                                j = k2;
                                k = d9;
                            }
                        }
                    }
                }
            }
        }

        if (d3 >= 0.0D)
        {
            if (flag)
            {
                this.destinationCoordinateCache.add(j1, new AetherPortalPosition(this, i, j, k, this.worldServerInstance.getTotalWorldTime()));
                this.destinationCoordinateKeys.add(Long.valueOf(j1));
            }

            var46 = (double)i + 0.5D;
            double var47 = (double)j + 0.5D;
            d4 = (double)k + 0.5D;
            int j2 = -1;

            if (this.worldServerInstance.getBlockId(i - 1, j, k) == AetherBlocks.AetherPortal.blockID)
            {
                j2 = 2;
            }

            if (this.worldServerInstance.getBlockId(i + 1, j, k) == AetherBlocks.AetherPortal.blockID)
            {
                j2 = 0;
            }

            if (this.worldServerInstance.getBlockId(i, j, k - 1) == AetherBlocks.AetherPortal.blockID)
            {
                j2 = 3;
            }

            if (this.worldServerInstance.getBlockId(i, j, k + 1) == AetherBlocks.AetherPortal.blockID)
            {
                j2 = 1;
            }

            k2 = par1Entity.getTeleportDirection();

            if (j2 > -1)
            {
                int var48 = Direction.rotateLeft[j2];
                int i3 = Direction.offsetX[j2];
                int j3 = Direction.offsetZ[j2];
                int k3 = Direction.offsetX[var48];
                int l3 = Direction.offsetZ[var48];
                boolean flag1 = !this.invalidSpawn(i + i3 + k3, j, k + j3 + l3) || !this.invalidSpawn(i + i3 + k3, j + 1, k + j3 + l3);
                boolean flag2 = !this.invalidSpawn(i + i3, j, k + j3) || !this.invalidSpawn(i + i3, j + 1, k + j3);

                if (flag1 && flag2)
                {
                    j2 = Direction.rotateOpposite[j2];
                    var48 = Direction.rotateOpposite[var48];
                    i3 = Direction.offsetX[j2];
                    j3 = Direction.offsetZ[j2];
                    k3 = Direction.offsetX[var48];
                    l3 = Direction.offsetZ[var48];
                    k1 = i - k3;
                    var46 -= (double)k3;
                    int f1 = k - l3;
                    d4 -= (double)l3;
                    flag1 = !this.invalidSpawn(k1 + i3 + k3, j, f1 + j3 + l3) || !this.invalidSpawn(k1 + i3 + k3, j + 1, f1 + j3 + l3);
                    flag2 = !this.invalidSpawn(k1 + i3, j, f1 + j3) || !this.invalidSpawn(k1 + i3, j + 1, f1 + j3);
                }

                float var49 = 0.5F;
                float f2 = 0.5F;

                if (!flag1 && flag2)
                {
                    var49 = 1.0F;
                }
                else if (flag1 && !flag2)
                {
                    var49 = 0.0F;
                }
                else if (flag1 && flag2)
                {
                    f2 = 0.0F;
                }

                var46 += (double)((float)k3 * var49 + f2 * (float)i3);
                d4 += (double)((float)l3 * var49 + f2 * (float)j3);
                float f3 = 0.0F;
                float f4 = 0.0F;
                float f5 = 0.0F;
                float f6 = 0.0F;

                if (j2 == k2)
                {
                    f3 = 1.0F;
                    f4 = 1.0F;
                }
                else if (j2 == Direction.rotateOpposite[k2])
                {
                    f3 = -1.0F;
                    f4 = -1.0F;
                }
                else if (j2 == Direction.rotateRight[k2])
                {
                    f5 = 1.0F;
                    f6 = -1.0F;
                }
                else
                {
                    f5 = -1.0F;
                    f6 = 1.0F;
                }

                double d10 = par1Entity.motionX;
                double d11 = par1Entity.motionZ;
                par1Entity.motionX = d10 * (double)f3 + d11 * (double)f6;
                par1Entity.motionZ = d10 * (double)f5 + d11 * (double)f4;
                par1Entity.rotationYaw = par8 - (float)(k2 * 90) + (float)(j2 * 90);
            }
            else
            {
                par1Entity.motionX = par1Entity.motionY = par1Entity.motionZ = 0.0D;
            }

            par1Entity.setLocationAndAngles(var46, var47, d4, par1Entity.rotationYaw, par1Entity.rotationPitch);
            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean makePortal(Entity par1Entity)
    {
        byte b0 = 16;
        double d0 = -1.0D;
        int i = MathHelper.floor_double(par1Entity.posX);
        int j = MathHelper.floor_double(par1Entity.posY);
        int k = MathHelper.floor_double(par1Entity.posZ);
        int l = i;
        int i1 = j;
        int j1 = k;
        int k1 = 0;
        int l1 = this.random.nextInt(4);
        int i2;
        double d1;
        double d2;
        int k2;
        int j2;
        int i3;
        int l2;
        int k3;
        int j3;
        int i4;
        int l3;
        int k4;
        int j4;
        double d3;
        double d4;
        int i5;

        for (i2 = i - b0; i2 <= i + b0; ++i2)
        {
            d1 = (double)i2 + 0.5D - par1Entity.posX;

            for (j2 = k - b0; j2 <= k + b0; ++j2)
            {
                d2 = (double)j2 + 0.5D - par1Entity.posZ;
                label272:

                for (k2 = this.worldServerInstance.getActualHeight() - 1; k2 >= 0; --k2)
                {
                    if (this.invalidSpawn(i2, k2, j2))
                    {
                        while (k2 > 0 && this.invalidSpawn(i2, k2 - 1, j2))
                        {
                            --k2;
                        }

                        for (i3 = l1; i3 < l1 + 4; ++i3)
                        {
                            l2 = i3 % 2;
                            k3 = 1 - l2;

                            if (i3 % 4 >= 2)
                            {
                                l2 = -l2;
                                k3 = -k3;
                            }

                            for (j3 = 0; j3 < 3; ++j3)
                            {
                                for (i4 = 0; i4 < 4; ++i4)
                                {
                                    for (l3 = -1; l3 < 4; ++l3)
                                    {
                                        k4 = i2 + (i4 - 1) * l2 + j3 * k3;
                                        j4 = k2 + l3;
                                        i5 = j2 + (i4 - 1) * k3 - j3 * l2;

                                        if (l3 < 0 && !this.worldServerInstance.getBlockMaterial(k4, j4, i5).isSolid() || l3 >= 0 && !this.invalidSpawn(k4, j4, i5))
                                        {
                                            continue label272;
                                        }
                                    }
                                }
                            }

                            d4 = (double)k2 + 0.5D - par1Entity.posY;
                            d3 = d1 * d1 + d4 * d4 + d2 * d2;

                            if (d0 < 0.0D || d3 < d0)
                            {
                                d0 = d3;
                                l = i2;
                                i1 = k2;
                                j1 = j2;
                                k1 = i3 % 4;
                            }
                        }
                    }
                }
            }
        }

        if (d0 < 0.0D)
        {
            for (i2 = i - b0; i2 <= i + b0; ++i2)
            {
                d1 = (double)i2 + 0.5D - par1Entity.posX;

                for (j2 = k - b0; j2 <= k + b0; ++j2)
                {
                    d2 = (double)j2 + 0.5D - par1Entity.posZ;
                    label220:

                    for (k2 = this.worldServerInstance.getActualHeight() - 1; k2 >= 0; --k2)
                    {
                        if (this.invalidSpawn(i2, k2, j2))
                        {
                            while (k2 > 0 && this.invalidSpawn(i2, k2 - 1, j2))
                            {
                                --k2;
                            }

                            for (i3 = l1; i3 < l1 + 2; ++i3)
                            {
                                l2 = i3 % 2;
                                k3 = 1 - l2;

                                for (j3 = 0; j3 < 4; ++j3)
                                {
                                    for (i4 = -1; i4 < 4; ++i4)
                                    {
                                        l3 = i2 + (j3 - 1) * l2;
                                        k4 = k2 + i4;
                                        j4 = j2 + (j3 - 1) * k3;

                                        if (i4 < 0 && !this.worldServerInstance.getBlockMaterial(l3, k4, j4).isSolid() || i4 >= 0 && !this.invalidSpawn(l3, k4, j4))
                                        {
                                            continue label220;
                                        }
                                    }
                                }

                                d4 = (double)k2 + 0.5D - par1Entity.posY;
                                d3 = d1 * d1 + d4 * d4 + d2 * d2;

                                if (d0 < 0.0D || d3 < d0)
                                {
                                    d0 = d3;
                                    l = i2;
                                    i1 = k2;
                                    j1 = j2;
                                    k1 = i3 % 2;
                                }
                            }
                        }
                    }
                }
            }
        }

        i5 = l;
        int j5 = i1;
        j2 = j1;
        int k5 = k1 % 2;
        int l5 = 1 - k5;

        if (k1 % 4 >= 2)
        {
            k5 = -k5;
            l5 = -l5;
        }

        boolean flag;

        if (d0 < 0.0D)
        {
            if (i1 < 70)
            {
                i1 = 70;
            }

            if (i1 > this.worldServerInstance.getActualHeight() - 10)
            {
                i1 = this.worldServerInstance.getActualHeight() - 10;
            }

            j5 = i1;

            for (k2 = -1; k2 <= 1; ++k2)
            {
                for (i3 = 1; i3 < 3; ++i3)
                {
                    for (l2 = -1; l2 < 3; ++l2)
                    {
                        k3 = i5 + (i3 - 1) * k5 + k2 * l5;
                        j3 = j5 + l2;
                        i4 = j2 + (i3 - 1) * l5 - k2 * k5;
                        flag = l2 < 0;
                        this.worldServerInstance.setBlock(k3, j3, i4, flag ? Block.glowStone.blockID : 0);
                    }
                }
            }
        }

        for (k2 = 0; k2 < 4; ++k2)
        {
            for (i3 = 0; i3 < 4; ++i3)
            {
                for (l2 = -1; l2 < 4; ++l2)
                {
                    k3 = i5 + (i3 - 1) * k5;
                    j3 = j5 + l2;
                    i4 = j2 + (i3 - 1) * l5;
                    flag = i3 == 0 || i3 == 3 || l2 == -1 || l2 == 3;
                    this.worldServerInstance.setBlock(k3, j3, i4, flag ? Block.glowStone.blockID : AetherBlocks.AetherPortal.blockID, 0, 2);
                }
            }

            for (i3 = 0; i3 < 4; ++i3)
            {
                for (l2 = -1; l2 < 4; ++l2)
                {
                    k3 = i5 + (i3 - 1) * k5;
                    j3 = j5 + l2;
                    i4 = j2 + (i3 - 1) * l5;
                    this.worldServerInstance.notifyBlocksOfNeighborChange(k3, j3, i4, this.worldServerInstance.getBlockId(k3, j3, i4));
                }
            }
        }

        return true;
    }

    private boolean invalidSpawn(int x, int y, int z)
    {
        int id = this.worldServerInstance.getBlockId(x, y, z);
        Dungeon dungeon = DungeonHandler.instance().getInstanceAt(x, y, z);
        return this.worldServerInstance.isAirBlock(x, y, z) || Block.blocksList[id] != null && Block.blocksList[id] instanceof BlockAether && ((BlockAether)Block.blocksList[id]).isDungeonBlock() || dungeon != null || Block.blocksList[id] != null && !Block.blocksList[id].isOpaqueCube();
    }

    /**
     * called periodically to remove out-of-date portal locations from the cache list. Argument par1 is a
     * WorldServer.getTotalWorldTime() value.
     */
    public void removeStalePortalLocations(long par1)
    {
        if (par1 % 100L == 0L)
        {
            Iterator iterator = this.destinationCoordinateKeys.iterator();
            long j = par1 - 600L;

            while (iterator.hasNext())
            {
                Long olong = (Long)iterator.next();
                AetherPortalPosition AetherPortalPosition = (AetherPortalPosition)this.destinationCoordinateCache.getValueByKey(olong.longValue());

                if (AetherPortalPosition == null || AetherPortalPosition.lastUpdateTime < j)
                {
                    iterator.remove();
                    this.destinationCoordinateCache.remove(olong.longValue());
                }
            }
        }
    }
}
