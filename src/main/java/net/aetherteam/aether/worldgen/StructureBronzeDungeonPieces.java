package net.aetherteam.aether.worldgen;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;

public class StructureBronzeDungeonPieces
{
    static boolean PreviousRoomHadThisAttached(List var0, Class var1)
    {
        Iterator var2 = var0.iterator();

        while (var2.hasNext())
        {
            StructureComponent var3 = (StructureComponent)var2.next();

            if (var1.isInstance(var3))
            {
                return true;
            }
        }

        return false;
    }

    private static StructureComponent getRandomComponent(StructureBronzeDungeonStart var0, ComponentDungeonBronzeRoom var1, List var2, Random var3, int var4, int var5, int var6, int var7, int var8)
    {
        int var9 = var3.nextInt(100);
        StructureBoundingBox var10;

        if (!(var1 instanceof ComponentDungeonEntranceTop) && !PreviousRoomHadThisAttached(var1.roomsLinkedToTheRoom, ComponentDungeonEntranceTop.class) && var3.nextInt(1000) < var8 * var8)
        {
            var10 = ComponentDungeonEntranceTop.findValidPlacement(var2, var3, var4, var5, var6, var7);

            if (var10 != null)
            {
                ComponentDungeonEntranceTop var11 = new ComponentDungeonEntranceTop(var8, var1, var0, var3, var10, var7);

                if (doRoomsHaveIntersectingEntrances(var1, var11))
                {
                    return var11;
                }
            }
        }

        if (var3.nextInt(100) < var8 * var8)
        {
            var10 = ComponentDungeonBronzeCog.findValidPlacement(var2, var3, var4, var5, var6, var7);

            if (var10 != null && numberOfCertainRoom(var2, ComponentDungeonBronzeCog.class) < 1)
            {
                ComponentDungeonBronzeCog var12 = new ComponentDungeonBronzeCog(var8, var1, var0, var3, var10, var7);

                if (doRoomsHaveIntersectingEntrances(var1, var12))
                {
                    return var12;
                }
            }
        }

        if (var3.nextInt(100) < var8 * var8)
        {
            var10 = ComponentDungeonBronzeSentryGuard.findValidPlacement(var2, var3, var4, var5, var6, var7);

            if (var10 != null && numberOfCertainRoom(var2, ComponentDungeonBronzeSentryGuard.class) < 2)
            {
                ComponentDungeonBronzeSentryGuard var15 = new ComponentDungeonBronzeSentryGuard(var8, var1, var0, var3, var10, var7);

                if (doRoomsHaveIntersectingEntrances(var1, var15))
                {
                    return var15;
                }
            }
        }

        if (var3.nextInt(100) < var8 * var8)
        {
            var10 = ComponentDungeonBronzeHost.findValidPlacement(var2, var3, var4, var5, var6, var7);

            if (var10 != null && numberOfCertainRoom(var2, ComponentDungeonBronzeHost.class) < 2)
            {
                ComponentDungeonBronzeHost var16 = new ComponentDungeonBronzeHost(var8, var1, var0, var3, var10, var7);

                if (doRoomsHaveIntersectingEntrances(var1, var16))
                {
                    return var16;
                }
            }
        }
        else if (var9 < 30)
        {
            var10 = ComponentDungeonCorridor.findValidPlacement(var2, var3, var4, var5, var6, var7);

            if (var10 != null)
            {
                ComponentDungeonCorridor var13 = new ComponentDungeonCorridor(var8, var1, var0, var3, var10, var7);

                if (doRoomsHaveIntersectingEntrances(var1, var13))
                {
                    return var13;
                }
            }
        }
        else if (var9 < 50)
        {
            var10 = ComponentDungeonBronzeChest.findValidPlacement(var2, var3, var4, var5, var6, var7);

            if (var10 != null)
            {
                ComponentDungeonBronzeChest var14 = new ComponentDungeonBronzeChest(var8, var1, var0, var3, var10, var7);

                if (doRoomsHaveIntersectingEntrances(var1, var14))
                {
                    return var14;
                }
            }
        }
        else if (var9 < 70)
        {
            if (!(var1 instanceof ComponentDungeonStair))
            {
                var10 = ComponentDungeonStair.findValidPlacement(var2, var3, var4, var5, var6, var7);

                if (var10 != null)
                {
                    ComponentDungeonStair var17 = new ComponentDungeonStair(var8, var1, var0, var3, var10, var7);

                    if (doRoomsHaveIntersectingEntrances(var1, var17))
                    {
                        return var17;
                    }
                }
            }
        }
        else if (var9 >= 70)
        {
            var10 = ComponentDungeonBronzeLight.findValidPlacement(var2, var3, var4, var5, var6, var7);

            if (var10 != null)
            {
                ComponentDungeonBronzeLight var18 = new ComponentDungeonBronzeLight(var8, var1, var0, var3, var10, var7);

                if (doRoomsHaveIntersectingEntrances(var1, var18))
                {
                    return var18;
                }
            }
        }

        return null;
    }

    public static int numberOfCertainRoom(List var0, Class var1)
    {
        Iterator var2 = var0.iterator();
        int var4 = 0;

        while (var2.hasNext())
        {
            StructureComponent var3 = (StructureComponent)var2.next();

            if (var1.isInstance(var3))
            {
                ++var4;
            }
        }

        return var4;
    }

    public static boolean doRoomsHaveIntersectingEntrances(ComponentDungeonBronzeRoom var0, ComponentDungeonBronzeRoom var1)
    {
        Iterator var2 = var0.entrances.iterator();

        while (var2.hasNext())
        {
            StructureBoundingBox var3 = (StructureBoundingBox)var2.next();
            Iterator var4 = var1.entrances.iterator();

            while (var4.hasNext())
            {
                StructureBoundingBox var5 = findIntercetingCube(var3, (StructureBoundingBox)var4.next());

                if (var5 != null)
                {
                    return true;
                }
            }
        }

        return false;
    }

    public static StructureBoundingBox findIntercetingCube(StructureBoundingBox var0, StructureBoundingBox var1)
    {
        int var2 = Math.max(var0.minX, var1.minX);
        int var3 = Math.max(var0.minY, var1.minY);
        int var4 = Math.max(var0.minZ, var1.minZ);
        int var5 = Math.min(var0.maxX, var1.maxX);
        int var6 = Math.min(var0.maxY, var1.maxY);
        int var7 = Math.min(var0.maxZ, var1.maxZ);
        return var2 < var5 && var3 < var6 && var4 < var7 ? new StructureBoundingBox(var2, var3, var4, var5, var6, var7) : null;
    }

    private static StructureComponent getNextMineShaftComponent(StructureBronzeDungeonStart var0, StructureComponent var1, List var2, Random var3, int var4, int var5, int var6, int var7, int var8)
    {
        if (var8 > 8)
        {
            return null;
        }
        else if (Math.abs(var4 - var0.X) <= 80 && Math.abs(var6 - var0.Z) <= 80)
        {
            StructureComponent var9 = getRandomComponent(var0, (ComponentDungeonBronzeRoom)var1, var2, var3, var4, var5, var6, var7, var8 + 1);

            if (var9 != null)
            {
                var2.add(var9);
            }

            return var9;
        }
        else
        {
            return null;
        }
    }

    static StructureComponent getNextComponent(StructureBronzeDungeonStart var0, StructureComponent var1, List var2, Random var3, int var4, int var5, int var6, int var7, int var8)
    {
        return getNextMineShaftComponent(var0, var1, var2, var3, var4, var5, var6, var7, var8);
    }
}
