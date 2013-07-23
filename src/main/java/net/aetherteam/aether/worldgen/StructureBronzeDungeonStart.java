package net.aetherteam.aether.worldgen;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import net.aetherteam.aether.blocks.AetherBlocks;
import net.aetherteam.aether.dungeons.Dungeon;
import net.aetherteam.aether.dungeons.DungeonHandler;
import net.aetherteam.aether.dungeons.DungeonType;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;

public class StructureBronzeDungeonStart
{
    public int X;
    public int Z;
    Dungeon dungeonInstance;
    public LinkedList components = new LinkedList();
    protected StructureBoundingBox boundingBox;

    public StructureBoundingBox getBoundingBox()
    {
        return this.boundingBox;
    }

    public LinkedList getComponents()
    {
        return this.components;
    }

    protected void updateBoundingBox()
    {
        this.boundingBox = StructureBoundingBox.getNewBoundingBox();
        Iterator var1 = this.components.iterator();

        while (var1.hasNext())
        {
            StructureComponent var2 = (StructureComponent)var1.next();
            this.boundingBox.expandTo(var2.getBoundingBox());
        }
    }

    public StructureBronzeDungeonStart(World var1, Random var2, int var3, int var4)
    {
        this.X = (var3 << 4) + 2;
        this.Z = (var4 << 4) + 2;
        ComponentDungeonBronzeBoss var5 = new ComponentDungeonBronzeBoss(0, (StructureComponent)null, this, var2, (var3 << 4) + 2, (var4 << 4) + 2);
        this.components.add(var5);
        var5.buildComponent(this.components, var2);

        for (int var7 = 1; var7 < 8; ++var7)
        {
            List var8 = (List)this.components.clone();
            Iterator var9 = var8.iterator();

            do
            {
                StructureComponent var6 = (StructureComponent)var9.next();

                if (var6.getComponentType() == var7)
                {
                    var6.buildComponent(var6, this.components, var2);
                }
            }
            while (var9.hasNext());
        }

        this.updateBoundingBox();

        if (!var1.isRemote)
        {
            this.dungeonInstance = new Dungeon(DungeonType.BRONZE, this.X, this.Z, this);
            DungeonHandler.instance().addInstance(this.dungeonInstance);
        }
    }

    public void generateStructure(World var1, Random var2, StructureBoundingBox var3, double[] var4)
    {
        this.GenDirtForChunk(var1, var3.minX, var3.minZ, var4, var2);
        Iterator var5 = this.components.iterator();

        while (var5.hasNext())
        {
            StructureComponent var6 = (StructureComponent)var5.next();

            if (var6.getBoundingBox().intersectsWith(var3) && !var6.addComponentParts(var1, var2, var3))
            {
                var5.remove();
            }
        }
    }

    float getInterPolated(float var1, float var2, float var3, int var4, int var5, int var6)
    {
        var5 = var5 < 0 ? var5 + 16 : var5;
        var6 = var6 < 0 ? var6 + 16 : var6;
        float var7 = (float)var5 / 16.0F;
        float var8 = (float)var6 / 16.0F;
        return var1 * (1.0F - var7) * (1.0F - var8) + var3 * var7 * (1.0F - var8) + var2 * var8 * (1.0F - var7) + (float)var4 * var7 * var8;
    }

    public void GenDirtForChunk(World var1, int var2, int var3, double[] var4, Random var5)
    {
        int var6 = findhighest(this.components, var2 - 16, var3 - 16, var2 + 16, var3 + 16);
        int var7 = findhighest(this.components, var2, var3 - 16, var2 + 32, var3 + 16);
        int var8 = findhighest(this.components, var2 - 16, var3, var2 + 16, var3 + 32);
        int var9 = findhighest(this.components, var2, var3, var2 + 32, var3 + 32);
        int var10 = this.findlowest(this.components, var2 - 16, var3 - 16, var2 + 16, var3 + 16);
        int var11 = this.findlowest(this.components, var2, var3 - 16, var2 + 32, var3 + 16);
        int var12 = this.findlowest(this.components, var2 - 16, var3, var2 + 16, var3 + 32);
        int var13 = this.findlowest(this.components, var2, var3, var2 + 32, var3 + 32);

        for (int var14 = var2; var14 < var2 + 16; ++var14)
        {
            for (int var15 = var3; var15 < var3 + 16; ++var15)
            {
                int var16 = (int)this.getInterPolated((float)var6, (float)var8, (float)var7, var9, var14 - var2, var15 - var3);
                int var17 = (int)this.getInterPolated((float)var10, (float)var12, (float)var11, var13, var14 - var2, var15 - var3);

                if (var16 > var17)
                {
                    var1.setBlock(var14, var16 + 1, var15, AetherBlocks.AetherGrass.blockID, 0, 2);
                    int var18 = 0;
                    int var19 = (int)(var4[var14 - var2 + (var15 - var3) * 16] / 3.0D + 3.0D + var5.nextDouble() * 0.25D);

                    for (int var20 = var16; var20 > var17; --var20)
                    {
                        ++var18;

                        if (var18 < var19)
                        {
                            var1.setBlock(var14, var20, var15, AetherBlocks.AetherDirt.blockID, 0, 2);
                        }
                        else
                        {
                            var1.setBlock(var14, var20, var15, AetherBlocks.Holystone.blockID, 0, 2);
                        }
                    }
                }
            }
        }
    }

    private int findlowest(LinkedList var1, int var2, int var3, int var4, int var5)
    {
        StructureBoundingBox var6 = new StructureBoundingBox(var2, 0, var3, var4, 128, var5);
        Iterator var7 = var1.iterator();
        int var9 = 40;

        while (var7.hasNext())
        {
            StructureComponent var8 = (StructureComponent)var7.next();

            if (var8.getBoundingBox() != null && var8.getBoundingBox().intersectsWith(var6) && !(var8 instanceof ComponentDungeonBronzeEntrance))
            {
                var9 = var8.getBoundingBox().minY > var9 ? var9 : var8.getBoundingBox().minY;
            }
        }

        return var9;
    }

    public static int findhighest(List var0, int var1, int var2, int var3, int var4)
    {
        StructureBoundingBox var5 = new StructureBoundingBox(var1, 0, var2, var3, 128, var4);
        Iterator var6 = var0.iterator();
        int var8 = 30;

        while (var6.hasNext())
        {
            StructureComponent var7 = (StructureComponent)var6.next();

            if (var7.getBoundingBox() != null && var7.getBoundingBox().intersectsWith(var5) && !(var7 instanceof ComponentDungeonBronzeEntrance))
            {
                var8 = var7.getBoundingBox().maxY < var8 ? var8 : var7.getBoundingBox().maxY;
            }
        }

        return var8;
    }
}
