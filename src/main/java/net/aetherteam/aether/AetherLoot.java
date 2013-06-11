package net.aetherteam.aether;

import java.util.Random;

import net.aetherteam.aether.blocks.AetherBlocks;
import net.aetherteam.aether.items.AetherItems;
import net.minecraft.item.ItemStack;

public enum AetherLoot
{
    NORMAL(new Choice[]{new Choice(new ItemStack(AetherItems.ZanitePickaxe), 10.0F), new Choice(new ItemStack(AetherItems.GummieSwet, getRandomStacksize(7) + 1, getRandomStacksize(2)), 10.0F), new Choice(new ItemStack(AetherItems.SkyrootBucket, 1, 2), 8.0F), new Choice(new ItemStack(AetherItems.DartShooter), 10.0F), new Choice(new ItemStack(AetherItems.MoaEgg, 1, 0), 7.0F), new Choice(new ItemStack(AetherItems.AmbrosiumShard, getRandomStacksize(10) + 1), 12.0F), new Choice(new ItemStack(AetherItems.Dart, getRandomStacksize(5) + 1, 0), 10.0F), new Choice(new ItemStack(AetherItems.Dart, getRandomStacksize(3) + 1, 1), 10.0F), new Choice(new ItemStack(AetherItems.Dart, getRandomStacksize(3) + 1, 2), 10.0F), new Choice(new ItemStack(AetherItems.AetherMusicDisk), 5.0F), new Choice(new ItemStack(AetherItems.SkyrootBucket), 12.0F), new Choice(new ItemStack(AetherItems.SkyrootBucket), 12.0F), new Choice(new ItemStack(net.minecraft.item.Item.itemsList[(net.minecraft.item.Item.record13.itemID + getRandomStacksize(2))]), 5.0F), new Choice(new ItemStack(AetherItems.ZaniteBoots), 8.0F), new Choice(new ItemStack(AetherItems.ZaniteHelmet), 8.0F), new Choice(new ItemStack(AetherItems.ZaniteLeggings), 8.0F), new Choice(new ItemStack(AetherItems.ZaniteChestplate), 8.0F), new Choice(new ItemStack(AetherItems.IronPendant), 10.0F), new Choice(new ItemStack(AetherItems.GoldenPendant), 9.0F), new Choice(new ItemStack(AetherItems.ZaniteRing), 8.0F), new Choice(new ItemStack(AetherBlocks.AmbrosiumTorch, getRandomStacksize(4) + 1), 10.0F), new Choice(new ItemStack(AetherItems.SwetCape), 6.0F)}),

    BRONZE(new Choice[]{new Choice(new ItemStack(AetherItems.HammerOfNotch), 6.0F), new Choice(new ItemStack(AetherItems.LightningKnife, getRandomStacksize(15) + 1), 8.0F), new Choice(new ItemStack(AetherItems.LightningSword), 8.0F), new Choice(new ItemStack(AetherItems.AgilityCape), 6.0F), new Choice(new ItemStack(AetherItems.DexterityCape), 6.0F), new Choice(new ItemStack(AetherItems.SentryBoots), 6.0F), new Choice(new ItemStack(AetherItems.RegenerationStone), 8.0F), new Choice(new ItemStack(AetherItems.NeptuneGloves), 8.0F), new Choice(new ItemStack(AetherItems.NeptuneHelmet), 8.0F), new Choice(new ItemStack(AetherItems.NeptuneChestplate), 6.0F), new Choice(new ItemStack(AetherItems.NeptuneLeggings), 8.0F), new Choice(new ItemStack(AetherItems.NeptuneBoots), 8.0F)}),

    SILVER(new Choice[]{new Choice(new ItemStack(AetherItems.ValkyrieAxe), 8.0F), new Choice(new ItemStack(AetherItems.ValkyrieShovel), 8.0F), new Choice(new ItemStack(AetherItems.ValkyriePickaxe), 8.0F), new Choice(new ItemStack(AetherItems.ValkyrieLance), 6.0F), new Choice(new ItemStack(AetherItems.HolySword), 10.0F), new Choice(new ItemStack(AetherItems.ValkyrieGloves), 8.0F), new Choice(new ItemStack(AetherItems.ValkyrieHelmet), 8.0F), new Choice(new ItemStack(AetherItems.ValkyrieChestplate), 6.0F), new Choice(new ItemStack(AetherItems.ValkyrieLeggings), 8.0F), new Choice(new ItemStack(AetherItems.ValkyrieBoots), 8.0F), new Choice(new ItemStack(AetherItems.ValkyrieMusicDisk), 6.0F), new Choice(new ItemStack(AetherItems.InvisibilityCloak), 4.0F)}),

    GOLD(new Choice[]{new Choice(new ItemStack(AetherItems.VampireBlade), 8.0F), new Choice(new ItemStack(AetherItems.ShardOfLife), 8.0F), new Choice(new ItemStack(AetherItems.FlamingSword), 8.0F), new Choice(new ItemStack(AetherItems.PigSlayer), 6.0F), new Choice(new ItemStack(AetherItems.IronBubble), 6.0F), new Choice(new ItemStack(AetherItems.DartShooter, 1, 3), 10.0F), new Choice(new ItemStack(AetherItems.PhoenixGloves), 8.0F), new Choice(new ItemStack(AetherItems.PhoenixHelmet), 8.0F), new Choice(new ItemStack(AetherItems.PhoenixChestplate), 6.0F), new Choice(new ItemStack(AetherItems.PhoenixLeggings), 8.0F), new Choice(new ItemStack(AetherItems.PhoenixBoots), 8.0F)});

    private final Choice[] choices;
    private final float total;

    private AetherLoot(Choice[] choices)
    {
        this.choices = choices;
        this.total = getTotalWeight();
    }

    private float getTotalWeight()
    {
        float weight = 0.0F;
        for (Choice c : this.choices)
        {
            weight += c.getWeight();
        }
        return weight;
    }

    private static int getRandomStacksize(int size)
    {
        Random random = new Random();
        int randomStacksize = random.nextInt(size);
        return randomStacksize;
    }

    public ItemStack getRandomItem(Random rand)
    {
        float randNum = rand.nextFloat() * this.total;

        for (Choice c : this.choices)
        {
            randNum -= c.getWeight();

            if (randNum < 0.0F)
            {
                return c.getStack();
            }
        }

        return null;
    }

    public static class Choice
    {
        private ItemStack stack;
        private float weight;

        public Choice(ItemStack stack, float weight)
        {
            this.stack = stack;
            this.weight = weight;
        }

        public ItemStack getStack()
        {
            return this.stack;
        }

        public float getWeight()
        {
            return this.weight;
        }
    }
}

/* Location:           D:\Dev\Mc\forge_orl\mcp\jars\bin\aether.jar
 * Qualified Name:     net.aetherteam.aether.AetherLoot
 * JD-Core Version:    0.6.2
 */