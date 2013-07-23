package net.aetherteam.aether;

import net.aetherteam.aether.blocks.AetherBlocks;
import net.aetherteam.aether.containers.InventoryAether;
import net.aetherteam.aether.entities.EntityAetherCoin;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.event.Event.Result;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.BonemealEvent;

public class AetherEvents
{
    @ForgeSubscribe
    public void bonemealEvent(BonemealEvent var1)
    {
        World var2 = var1.world;
        int var3 = var2.getBlockId(var1.X, var1.Y, var1.Z);

        if (var3 == AetherBlocks.AetherGrass.blockID)
        {
            if (!var1.world.isRemote)
            {
                int var4 = 0;
                label41:

                for (int var5 = 0; var5 < 64; ++var5)
                {
                    int var6 = var1.X;
                    int var7 = var1.Y + 1;
                    int var8 = var1.Z;

                    for (int var9 = 0; var9 < var5 / 16; ++var9)
                    {
                        var6 += var2.rand.nextInt(3) - 1;
                        var7 += (var2.rand.nextInt(3) - 1) * var2.rand.nextInt(3) / 2;
                        var8 += var2.rand.nextInt(3) - 1;

                        if (var2.getBlockId(var6, var7 - 1, var8) != var3 || var2.isBlockNormalCube(var6, var7, var8))
                        {
                            continue label41;
                        }
                    }

                    if (var2.getBlockId(var6, var7, var8) == 0)
                    {
                        if (var2.rand.nextInt(20 + 10 * var4) == 0)
                        {
                            var2.setBlock(var6, var7, var8, AetherBlocks.WhiteFlower.blockID);
                            ++var4;
                        }
                        else if (var2.rand.nextInt(10 + 2 * var4) <= 2)
                        {
                            var2.setBlock(var6, var7, var8, AetherBlocks.PurpleFlower.blockID);
                            ++var4;
                        }
                        else if (var2.rand.nextInt(10 + 2 * var4) <= 8)
                        {
                            var2.setBlock(var6, var7, var8, AetherBlocks.TallAetherGrass.blockID);
                            ++var4;
                        }
                    }
                }
            }

            var1.setResult(Event.Result.ALLOW);
        }
    }

    @ForgeSubscribe
    public void playerDeathEvent(LivingDeathEvent var1)
    {
        if (var1.entityLiving instanceof EntityPlayer)
        {
            EntityPlayer var2 = (EntityPlayer)var1.entityLiving;
            PlayerBaseAetherServer var3 = Aether.getServerPlayer(var2);

            if (!var2.worldObj.getGameRules().getGameRuleBooleanValue("keepInventory"))
            {
                var3.inv.dropAllItems();

                if (!var2.worldObj.isRemote)
                {
                    double var4 = var1.entityLiving.posX;
                    double var6 = var1.entityLiving.posY;
                    double var8 = var1.entityLiving.posZ;
                    World var10 = var2.worldObj;
                    var2.worldObj.spawnEntityInWorld(new EntityAetherCoin(var10, var4, var6, var8, var3.getCoins()));
                    var3.inv = new InventoryAether(var2);
                    var3.setCoinAmount(0);
                    var1.setResult(Event.Result.ALLOW);
                }
            }
        }
    }
}
