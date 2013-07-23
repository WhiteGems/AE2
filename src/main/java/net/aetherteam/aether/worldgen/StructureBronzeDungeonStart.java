package net.aetherteam.aether.worldgen;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import net.aetherteam.aether.blocks.AetherBlocks;
import net.aetherteam.aether.dungeons.Dungeon;
import net.aetherteam.aether.dungeons.DungeonHandler;
import net.aetherteam.aether.dungeons.DungeonType;
import net.minecraft.block.Block;
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

    public StructureBronzeDungeonStart(World world, Random random, int par3, int par4)
    {
        this.X = ((par3 << 4) + 2);
        this.Z = ((par4 << 4) + 2);
        ComponentDungeonBronzeBoss var5 = new ComponentDungeonBronzeBoss(0, null, this, random, (par3 << 4) + 2, (par4 << 4) + 2);
        this.components.add(var5);
        var5.buildComponent(this.components, random);

        for (int numb = 1; numb < 8; numb++)
        {
            List list = (List)this.components.clone();
            Iterator var2 = list.iterator();

            do
            {
                StructureComponent var3 = (StructureComponent)var2.next();

                if (var3.getComponentType() == numb)
                {
                    var3.buildComponent(var3, this.components, random);
                }
            }
            while (var2.hasNext());
        }

        updateBoundingBox();

        if (!world.isRemote)
        {
            this.dungeonInstance = new Dungeon(DungeonType.BRONZE, this.X, this.Z, this);
            DungeonHandler.instance().addInstance(this.dungeonInstance);
        }
    }

    public void generateStructure(World par1World, Random par2Random, StructureBoundingBox structureBoundingBox, double[] dirtDepth)
    {
        GenDirtForChunk(par1World, structureBoundingBox.minX, structureBoundingBox.minZ, dirtDepth, par2Random);
        Iterator var4 = this.components.iterator();

        while (var4.hasNext())
        {
            StructureComponent var5 = (StructureComponent)var4.next();

            if ((var5.getBoundingBox().intersectsWith(structureBoundingBox)) && (!var5.addComponentParts(par1World, par2Random, structureBoundingBox)))
            {
                var4.remove();
            }
        }
    }

    float getInterPolated(float h00, float h01, float h10, int h11, int x, int y)
    {
        x = x < 0 ? x + 16 : x;
        y = y < 0 ? y + 16 : y;
        float i = x / 16.0F;
        float j = y / 16.0F;
        return h00 * (1.0F - i) * (1.0F - j) + h10 * i * (1.0F - j) + h01 * j * (1.0F - i) + h11 * i * j;
    }

    public void GenDirtForChunk(World worldObj, int x, int z, double[] dirtDepth, Random random)
    {
        int h00 = findhighest(this.components, x - 16, z - 16, x + 16, z + 16);
        int h10 = findhighest(this.components, x, z - 16, x + 32, z + 16);
        int h01 = findhighest(this.components, x - 16, z, x + 16, z + 32);
        int h11 = findhighest(this.components, x, z, x + 32, z + 32);
        int l00 = findlowest(this.components, x - 16, z - 16, x + 16, z + 16);
        int l10 = findlowest(this.components, x, z - 16, x + 32, z + 16);
        int l01 = findlowest(this.components, x - 16, z, x + 16, z + 32);
        int l11 = findlowest(this.components, x, z, x + 32, z + 32);

        for (int i = x; i < x + 16; i++)
        {
            for (int k = z; k < z + 16; k++)
            {
                int low = (int)getInterPolated(h00, h01, h10, h11, i - x, k - z);
                int other = (int)getInterPolated(l00, l01, l10, l11, i - x, k - z);

                if (low > other)
                {
                    worldObj.setBlock(i, low + 1, k, AetherBlocks.AetherGrass.blockID, 0, 2);
                    int counter = 0;
                    int dirtdeaptha = (int)(dirtDepth[(i - x + (k - z) * 16)] / 3.0D + 3.0D + random.nextDouble() * 0.25D);

                    for (int j = low; j > other; j--)
                    {
                        counter++;

                        if (counter < dirtdeaptha)
                        {
                            worldObj.setBlock(i, j, k, AetherBlocks.AetherDirt.blockID, 0, 2);
                        }
                        else
                        {
                            worldObj.setBlock(i, j, k, AetherBlocks.Holystone.blockID, 0, 2);
                        }
                    }
                }
            }
        }
    }

    private int findlowest(LinkedList components, int i, int j, int k, int l)
    {
        StructureBoundingBox par1StructureBoundingBox = new StructureBoundingBox(i, 0, j, k, 128, l);
        Iterator var2 = components.iterator();
        int max = 40;

        while (true)
        {
            if (!var2.hasNext())
            {
                return max;
            }

            StructureComponent var3 = (StructureComponent)var2.next();

            if ((var3.getBoundingBox() != null) && (var3.getBoundingBox().intersectsWith(par1StructureBoundingBox)) && (!(var3 instanceof ComponentDungeonBronzeEntrance)))
            {
                max = var3.getBoundingBox().minY > max ? max : var3.getBoundingBox().minY;
            }
        }
    }

    public static int findhighest(List par0List, int x, int y, int x1, int y1)
    {
        StructureBoundingBox par1StructureBoundingBox = new StructureBoundingBox(x, 0, y, x1, 128, y1);
        Iterator var2 = par0List.iterator();
        int max = 30;

        while (true)
        {
            if (!var2.hasNext())
            {
                return max;
            }

            StructureComponent var3 = (StructureComponent)var2.next();

            if ((var3.getBoundingBox() != null) && (var3.getBoundingBox().intersectsWith(par1StructureBoundingBox)) && (!(var3 instanceof ComponentDungeonBronzeEntrance)))
            {
                max = var3.getBoundingBox().maxY < max ? max : var3.getBoundingBox().maxY;
            }
        }
    }
}

