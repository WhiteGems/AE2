package net.aetherteam.aether.worldgen;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;

public class StructureBronzeDungeonPieces
{
    static boolean PreviousRoomHadThisAttached(List roomsLinkedToTheRoom, Class room)
    {
        Iterator var2 = roomsLinkedToTheRoom.iterator();

        while (var2.hasNext())
        {
            StructureComponent var3 = (StructureComponent)var2.next();

            if (room.isInstance(var3))
            {
                return true;
            }
        }

        return false;
    }

    private static StructureComponent getRandomComponent(StructureBronzeDungeonStart structureAsAWhole, ComponentDungeonBronzeRoom previousStructor, List components, Random random, int i, int j, int k, int direction, int componentNumber)
    {
        int var7 = random.nextInt(100);
        StructureBoundingBox var8;

        if (!(previousStructor instanceof ComponentDungeonEntranceTop) && !PreviousRoomHadThisAttached(previousStructor.roomsLinkedToTheRoom, ComponentDungeonEntranceTop.class) && random.nextInt(1000) < componentNumber * componentNumber)
        {
            var8 = ComponentDungeonEntranceTop.findValidPlacement(components, random, i, j, k, direction);

            if (var8 != null)
            {
                ComponentDungeonEntranceTop room = new ComponentDungeonEntranceTop(componentNumber, previousStructor, structureAsAWhole, random, var8, direction);

                if (doRoomsHaveIntersectingEntrances(previousStructor, room))
                {
                    return room;
                }
            }
        }

        if (random.nextInt(100) < componentNumber * componentNumber)
        {
            var8 = ComponentDungeonBronzeCog.findValidPlacement(components, random, i, j, k, direction);

            if (var8 != null && numberOfCertainRoom(components, ComponentDungeonBronzeCog.class) < 1)
            {
                ComponentDungeonBronzeCog room1 = new ComponentDungeonBronzeCog(componentNumber, previousStructor, structureAsAWhole, random, var8, direction);

                if (doRoomsHaveIntersectingEntrances(previousStructor, room1))
                {
                    return room1;
                }
            }
        }

        if (random.nextInt(100) < componentNumber * componentNumber)
        {
            var8 = ComponentDungeonBronzeSentryGuard.findValidPlacement(components, random, i, j, k, direction);

            if (var8 != null && numberOfCertainRoom(components, ComponentDungeonBronzeSentryGuard.class) < 2)
            {
                ComponentDungeonBronzeSentryGuard room4 = new ComponentDungeonBronzeSentryGuard(componentNumber, previousStructor, structureAsAWhole, random, var8, direction);

                if (doRoomsHaveIntersectingEntrances(previousStructor, room4))
                {
                    return room4;
                }
            }
        }

        if (random.nextInt(100) < componentNumber * componentNumber)
        {
            var8 = ComponentDungeonBronzeHost.findValidPlacement(components, random, i, j, k, direction);

            if (var8 != null && numberOfCertainRoom(components, ComponentDungeonBronzeHost.class) < 2)
            {
                ComponentDungeonBronzeHost room5 = new ComponentDungeonBronzeHost(componentNumber, previousStructor, structureAsAWhole, random, var8, direction);

                if (doRoomsHaveIntersectingEntrances(previousStructor, room5))
                {
                    return room5;
                }
            }
        }
        else if (var7 < 30)
        {
            var8 = ComponentDungeonCorridor.findValidPlacement(components, random, i, j, k, direction);

            if (var8 != null)
            {
                ComponentDungeonCorridor room2 = new ComponentDungeonCorridor(componentNumber, previousStructor, structureAsAWhole, random, var8, direction);

                if (doRoomsHaveIntersectingEntrances(previousStructor, room2))
                {
                    return room2;
                }
            }
        }
        else if (var7 < 50)
        {
            var8 = ComponentDungeonBronzeChest.findValidPlacement(components, random, i, j, k, direction);

            if (var8 != null)
            {
                ComponentDungeonBronzeChest room3 = new ComponentDungeonBronzeChest(componentNumber, previousStructor, structureAsAWhole, random, var8, direction);

                if (doRoomsHaveIntersectingEntrances(previousStructor, room3))
                {
                    return room3;
                }
            }
        }
        else if (var7 < 70)
        {
            if (!(previousStructor instanceof ComponentDungeonStair))
            {
                var8 = ComponentDungeonStair.findValidPlacement(components, random, i, j, k, direction);

                if (var8 != null)
                {
                    ComponentDungeonStair room6 = new ComponentDungeonStair(componentNumber, previousStructor, structureAsAWhole, random, var8, direction);

                    if (doRoomsHaveIntersectingEntrances(previousStructor, room6))
                    {
                        return room6;
                    }
                }
            }
        }
        else if (var7 >= 70)
        {
            var8 = ComponentDungeonBronzeLight.findValidPlacement(components, random, i, j, k, direction);

            if (var8 != null)
            {
                ComponentDungeonBronzeLight room7 = new ComponentDungeonBronzeLight(componentNumber, previousStructor, structureAsAWhole, random, var8, direction);

                if (doRoomsHaveIntersectingEntrances(previousStructor, room7))
                {
                    return room7;
                }
            }
        }

        return null;
    }

    public static int numberOfCertainRoom(List components, Class room)
    {
        Iterator var2 = components.iterator();
        int max = 0;

        while (var2.hasNext())
        {
            StructureComponent var3 = (StructureComponent)var2.next();

            if (room.isInstance(var3))
            {
                ++max;
            }
        }

        return max;
    }

    public static boolean doRoomsHaveIntersectingEntrances(ComponentDungeonBronzeRoom room1, ComponentDungeonBronzeRoom room2)
    {
        Iterator iterMyEntrance = room1.entrances.iterator();

        while (iterMyEntrance.hasNext())
        {
            StructureBoundingBox myCube = (StructureBoundingBox)iterMyEntrance.next();
            Iterator iterRoomEntrance = room2.entrances.iterator();

            while (iterRoomEntrance.hasNext())
            {
                StructureBoundingBox cube = findIntercetingCube(myCube, (StructureBoundingBox)iterRoomEntrance.next());

                if (cube != null)
                {
                    return true;
                }
            }
        }

        return false;
    }

    public static StructureBoundingBox findIntercetingCube(StructureBoundingBox b1, StructureBoundingBox b2)
    {
        int minX = Math.max(b1.minX, b2.minX);
        int minY = Math.max(b1.minY, b2.minY);
        int minZ = Math.max(b1.minZ, b2.minZ);
        int maxX = Math.min(b1.maxX, b2.maxX);
        int maxY = Math.min(b1.maxY, b2.maxY);
        int maxZ = Math.min(b1.maxZ, b2.maxZ);
        return minX < maxX && minY < maxY && minZ < maxZ ? new StructureBoundingBox(minX, minY, minZ, maxX, maxY, maxZ) : null;
    }

    private static StructureComponent getNextMineShaftComponent(StructureBronzeDungeonStart structureAsAWhole, StructureComponent previousStructor, List components, Random random, int i, int j, int k, int direction, int componentNumber)
    {
        if (componentNumber > 8)
        {
            return null;
        }
        else if (Math.abs(i - structureAsAWhole.X) <= 80 && Math.abs(k - structureAsAWhole.Z) <= 80)
        {
            StructureComponent var8 = getRandomComponent(structureAsAWhole, (ComponentDungeonBronzeRoom)previousStructor, components, random, i, j, k, direction, componentNumber + 1);

            if (var8 != null)
            {
                components.add(var8);
            }

            return var8;
        }
        else
        {
            return null;
        }
    }

    static StructureComponent getNextComponent(StructureBronzeDungeonStart structureAsAWhole, StructureComponent par0StructureComponent, List par1List, Random par2Random, int par3, int par4, int par5, int par6, int par7)
    {
        return getNextMineShaftComponent(structureAsAWhole, par0StructureComponent, par1List, par2Random, par3, par4, par5, par6, par7);
    }
}
