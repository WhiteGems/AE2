package net.aetherteam.aether;

import java.util.Random;
import net.aetherteam.aether.blocks.AetherBlocks;
import net.aetherteam.aether.containers.InventoryAether;
import net.aetherteam.aether.entities.EntityAetherCoin;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.event.Event.Result;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.BonemealEvent;

public class AetherEvents
{
    @ForgeSubscribe
    public void bonemealEvent(BonemealEvent event)
    {
        World world = event.world;
        int id = world.getBlockId(event.X, event.Y, event.Z);

        if (id == AetherBlocks.AetherGrass.blockID)
        {
            if (!event.world.isRemote)
            {
                int iSpawned = 0;

                label316: for (int var9 = 0; var9 < 64; var9++)
                {
                    int x = event.X;
                    int y = event.Y + 1;
                    int z = event.Z;

                    for (int var13 = 0; var13 < var9 / 16; var13++)
                    {
                        x += world.rand.nextInt(3) - 1;
                        y += (world.rand.nextInt(3) - 1) * world.rand.nextInt(3) / 2;
                        z += world.rand.nextInt(3) - 1;

                        if ((world.getBlockId(x, y - 1, z) != id) || (world.isBlockNormalCube(x, y, z)))
                        {
                            break label316;
                        }
                    }

                    if (world.getBlockId(x, y, z) == 0)
                    {
                        if (world.rand.nextInt(20 + 10 * iSpawned) == 0)
                        {
                            world.setBlock(x, y, z, AetherBlocks.WhiteFlower.blockID);
                            iSpawned++;
                        }
                        else if (world.rand.nextInt(10 + 2 * iSpawned) <= 2)
                        {
                            world.setBlock(x, y, z, AetherBlocks.PurpleFlower.blockID);
                            iSpawned++;
                        }
                        else if (world.rand.nextInt(10 + 2 * iSpawned) <= 8)
                        {
                            world.setBlock(x, y, z, AetherBlocks.TallAetherGrass.blockID);
                            iSpawned++;
                        }
                    }
                }
            }

            event.setResult(Event.Result.ALLOW);
        }
    }

    @ForgeSubscribe
    public void playerDeathEvent(LivingDeathEvent event)
    {
        if ((event.entityLiving instanceof EntityPlayer))
        {
            EntityPlayer player = (EntityPlayer)event.entityLiving;
            PlayerBaseAetherServer base = Aether.getServerPlayer(player);

            if (!player.worldObj.N().getGameRuleBooleanValue("keepInventory"))
            {
                base.inv.dropAllItems();

                if (!player.worldObj.isRemote)
                {
                    double x = event.entityLiving.posX;
                    double y = event.entityLiving.posY;
                    double z = event.entityLiving.posZ;
                    World world = player.worldObj;
                    player.worldObj.spawnEntityInWorld(new EntityAetherCoin(world, x, y, z, base.getCoins()));
                    base.inv = new InventoryAether(player);
                    base.setCoinAmount(0);
                    event.setResult(Event.Result.ALLOW);
                }
            }
        }
    }
}

