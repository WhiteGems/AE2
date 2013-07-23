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
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.LongHashMap;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.PortalPosition;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class TeleporterAether extends Teleporter
{
    private final WorldServer worldServerInstance;
    private final Random random;
    private final LongHashMap field_85191_c;
    private final List field_85190_d;
    private boolean createPortal;

    public TeleporterAether(WorldServer par1WorldServer)
    {
        super(par1WorldServer);
        this.destinationCoordinateCache = new LongHashMap();
        this.destinationCoordinateKeys = new ArrayList();
        this.worldServerInstance = par1WorldServer;
        this.worldServerInstance.customTeleporters.add(this);
        this.random = new Random(par1WorldServer.getTotalWorldTime());
        this.createPortal = true;
    }

    public TeleporterAether(WorldServer par1WorldServer, boolean createPortal)
    {
        super(par1WorldServer);
        this.destinationCoordinateCache = new LongHashMap();
        this.destinationCoordinateKeys = new ArrayList();
        this.worldServerInstance = par1WorldServer;
        this.worldServerInstance.customTeleporters.add(this);
        this.random = new Random(par1WorldServer.getTotalWorldTime());
        this.createPortal = false;
    }

    public void placeInPortal(Entity par1Entity, double par2, double par4, double par6, float par8)
    {
        if (!placeInExistingPortal(par1Entity, par2, par4, par6, par8))
        {
            if ((this.createPortal = 1) != 0)
            {
                makePortal(par1Entity);
            }

            placeInExistingPortal(par1Entity, par2, par4, par6, par8);
        }
    }

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

        if (this.destinationCoordinateCache.containsItem(j1))
        {
            AetherPortalPosition AetherPortalPosition = (AetherPortalPosition)this.destinationCoordinateCache.getValueByKey(j1);
            d3 = 0.0D;
            i = AetherPortalPosition.posX;
            j = AetherPortalPosition.posY;
            k = AetherPortalPosition.posZ;
            AetherPortalPosition.lastUpdateTime = this.worldServerInstance.getWorldTime();
            flag = false;
        }
        else
        {
            for (int k1 = l - short1; k1 <= l + short1; k1++)
            {
                double d5 = k1 + 0.5D - par1Entity.posX;

                for (int l1 = i1 - short1; l1 <= i1 + short1; l1++)
                {
                    double d6 = l1 + 0.5D - par1Entity.posZ;

                    for (int i2 = this.worldServerInstance.R() - 1; i2 >= 0; i2--)
                    {
                        if (this.worldServerInstance.getBlockId(k1, i2, l1) == AetherBlocks.AetherPortal.blockID)
                        {
                            while (this.worldServerInstance.getBlockId(k1, i2 - 1, l1) == AetherBlocks.AetherPortal.blockID)
                            {
                                i2--;
                            }

                            double d4 = i2 + 0.5D - par1Entity.posY;
                            double d7 = d5 * d5 + d4 * d4 + d6 * d6;

                            if ((d3 < 0.0D) || (d7 < d3))
                            {
                                d3 = d7;
                                i = k1;
                                j = i2;
                                k = l1;
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
                this.destinationCoordinateCache.add(j1, new AetherPortalPosition(this, i, j, k, this.worldServerInstance.getWorldTime()));
                this.destinationCoordinateKeys.add(Long.valueOf(j1));
            }

            double d8 = i + 0.5D;
            double d9 = j + 0.5D;
            double d4 = k + 0.5D;
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

            int k2 = par1Entity.getTeleportDirection();

            if (j2 > -1)
            {
                int l2 = net.minecraft.util.Direction.rotateLeft[j2];
                int i3 = net.minecraft.util.Direction.offsetX[j2];
                int j3 = net.minecraft.util.Direction.offsetZ[j2];
                int k3 = net.minecraft.util.Direction.offsetX[l2];
                int l3 = net.minecraft.util.Direction.offsetZ[l2];
                boolean flag1 = (!invalidSpawn(i + i3 + k3, j, k + j3 + l3)) || (!invalidSpawn(i + i3 + k3, j + 1, k + j3 + l3));
                boolean flag2 = (!invalidSpawn(i + i3, j, k + j3)) || (!invalidSpawn(i + i3, j + 1, k + j3));

                if ((flag1) && (flag2))
                {
                    j2 = net.minecraft.util.Direction.rotateOpposite[j2];
                    l2 = net.minecraft.util.Direction.rotateOpposite[l2];
                    i3 = net.minecraft.util.Direction.offsetX[j2];
                    j3 = net.minecraft.util.Direction.offsetZ[j2];
                    k3 = net.minecraft.util.Direction.offsetX[l2];
                    l3 = net.minecraft.util.Direction.offsetZ[l2];
                    int k1 = i - k3;
                    d8 -= k3;
                    int i4 = k - l3;
                    d4 -= l3;
                    flag1 = (!invalidSpawn(k1 + i3 + k3, j, i4 + j3 + l3)) || (!invalidSpawn(k1 + i3 + k3, j + 1, i4 + j3 + l3));
                    flag2 = (!invalidSpawn(k1 + i3, j, i4 + j3)) || (!invalidSpawn(k1 + i3, j + 1, i4 + j3));
                }

                float f1 = 0.5F;
                float f2 = 0.5F;

                if ((!flag1) && (flag2))
                {
                    f1 = 1.0F;
                }
                else if ((flag1) && (!flag2))
                {
                    f1 = 0.0F;
                }
                else if ((flag1) && (flag2))
                {
                    f2 = 0.0F;
                }

                d8 += k3 * f1 + f2 * i3;
                d4 += l3 * f1 + f2 * j3;
                float f3 = 0.0F;
                float f4 = 0.0F;
                float f5 = 0.0F;
                float f6 = 0.0F;

                if (j2 == k2)
                {
                    f3 = 1.0F;
                    f4 = 1.0F;
                }
                else if (j2 == net.minecraft.util.Direction.rotateOpposite[k2])
                {
                    f3 = -1.0F;
                    f4 = -1.0F;
                }
                else if (j2 == net.minecraft.util.Direction.rotateRight[k2])
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
                par1Entity.motionX = (d10 * f3 + d11 * f6);
                par1Entity.motionZ = (d10 * f5 + d11 * f4);
                par1Entity.rotationYaw = (par8 - k2 * 90 + j2 * 90);
            }
            else
            {
                par1Entity.motionX = (par1Entity.motionY = par1Entity.motionZ = 0.0D);
            }

            par1Entity.setLocationAndAngles(d8, d9, d4, par1Entity.rotationYaw, par1Entity.rotationPitch);
            return true;
        }

        return false;
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

        for (int i2 = i - b0; i2 <= i + b0; i2++)
        {
            double d1 = i2 + 0.5D - par1Entity.posX;

            for (int j2 = k - b0; j2 <= k + b0; j2++)
            {
                double d2 = j2 + 0.5D - par1Entity.posZ;

                label421: for (int k2 = this.worldServerInstance.R() - 1; k2 >= 0; k2--)
                {
                    if (invalidSpawn(i2, k2, j2))
                    {
                        while ((k2 > 0) && (invalidSpawn(i2, k2 - 1, j2)))
                        {
                            k2--;
                        }

                        for (int i3 = l1; i3 < l1 + 4; i3++)
                        {
                            int l2 = i3 % 2;
                            int k3 = 1 - l2;

                            if (i3 % 4 >= 2)
                            {
                                l2 = -l2;
                                k3 = -k3;
                            }

                            for (int j3 = 0; j3 < 3; j3++)
                            {
                                for (int i4 = 0; i4 < 4; i4++)
                                {
                                    for (int l3 = -1; l3 < 4; l3++)
                                    {
                                        int k4 = i2 + (i4 - 1) * l2 + j3 * k3;
                                        int j4 = k2 + l3;
                                        int l4 = j2 + (i4 - 1) * k3 - j3 * l2;

                                        if (((l3 < 0) && (!this.worldServerInstance.getBlockMaterial(k4, j4, l4).isSolid())) || ((l3 >= 0) && (!invalidSpawn(k4, j4, l4))))
                                        {
                                            break label421;
                                        }
                                    }
                                }
                            }

                            double d4 = k2 + 0.5D - par1Entity.posY;
                            double d3 = d1 * d1 + d4 * d4 + d2 * d2;

                            if ((d0 < 0.0D) || (d3 < d0))
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
            for (i2 = i - b0; i2 <= i + b0; i2++)
            {
                double d1 = i2 + 0.5D - par1Entity.posX;

                for (int j2 = k - b0; j2 <= k + b0; j2++)
                {
                    double d2 = j2 + 0.5D - par1Entity.posZ;

                    label762: for (int k2 = this.worldServerInstance.R() - 1; k2 >= 0; k2--)
                    {
                        if (invalidSpawn(i2, k2, j2))
                        {
                            while ((k2 > 0) && (invalidSpawn(i2, k2 - 1, j2)))
                            {
                                k2--;
                            }

                            for (int i3 = l1; i3 < l1 + 2; i3++)
                            {
                                int l2 = i3 % 2;
                                int k3 = 1 - l2;

                                for (int j3 = 0; j3 < 4; j3++)
                                {
                                    for (int i4 = -1; i4 < 4; i4++)
                                    {
                                        int l3 = i2 + (j3 - 1) * l2;
                                        int k4 = k2 + i4;
                                        int j4 = j2 + (j3 - 1) * k3;

                                        if (((i4 < 0) && (!this.worldServerInstance.getBlockMaterial(l3, k4, j4).isSolid())) || ((i4 >= 0) && (!invalidSpawn(l3, k4, j4))))
                                        {
                                            break label762;
                                        }
                                    }
                                }

                                double d4 = k2 + 0.5D - par1Entity.posY;
                                double d3 = d1 * d1 + d4 * d4 + d2 * d2;

                                if ((d0 < 0.0D) || (d3 < d0))
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

        int i5 = l;
        int j5 = i1;
        int j2 = j1;
        int k5 = k1 % 2;
        int l5 = 1 - k5;

        if (k1 % 4 >= 2)
        {
            k5 = -k5;
            l5 = -l5;
        }

        if (d0 < 0.0D)
        {
            if (i1 < 70)
            {
                i1 = 70;
            }

            if (i1 > this.worldServerInstance.R() - 10)
            {
                i1 = this.worldServerInstance.R() - 10;
            }

            j5 = i1;

            for (int k2 = -1; k2 <= 1; k2++)
            {
                for (int i3 = 1; i3 < 3; i3++)
                {
                    for (int l2 = -1; l2 < 3; l2++)
                    {
                        int k3 = i5 + (i3 - 1) * k5 + k2 * l5;
                        int j3 = j5 + l2;
                        int i4 = j2 + (i3 - 1) * l5 - k2 * k5;
                        boolean flag = l2 < 0;
                        this.worldServerInstance.setBlock(k3, j3, i4, flag ? Block.glowStone.blockID : 0);
                    }
                }
            }
        }

        for (int k2 = 0; k2 < 4; k2++)
        {
            for (int i3 = 0; i3 < 4; i3++)
            {
                for (int l2 = -1; l2 < 4; l2++)
                {
                    int k3 = i5 + (i3 - 1) * k5;
                    int j3 = j5 + l2;
                    int i4 = j2 + (i3 - 1) * l5;
                    boolean flag = (i3 == 0) || (i3 == 3) || (l2 == -1) || (l2 == 3);
                    this.worldServerInstance.setBlock(k3, j3, i4, flag ? Block.glowStone.blockID : AetherBlocks.AetherPortal.blockID, 0, 2);
                }
            }

            for (i3 = 0; i3 < 4; i3++)
            {
                for (int l2 = -1; l2 < 4; l2++)
                {
                    int k3 = i5 + (i3 - 1) * k5;
                    int j3 = j5 + l2;
                    int i4 = j2 + (i3 - 1) * l5;
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
        return (this.worldServerInstance.isAirBlock(x, y, z)) || ((Block.blocksList[id] != null) && ((Block.blocksList[id] instanceof BlockAether)) && (((BlockAether)Block.blocksList[id]).isDungeonBlock())) || (dungeon != null) || ((Block.blocksList[id] != null) && (!Block.blocksList[id].isOpaqueCube()));
    }

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

                if ((AetherPortalPosition == null) || (AetherPortalPosition.lastUpdateTime < j))
                {
                    iterator.remove();
                    this.destinationCoordinateCache.remove(olong.longValue());
                }
            }
        }
    }
}

