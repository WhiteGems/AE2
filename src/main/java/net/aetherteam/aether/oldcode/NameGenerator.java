package net.aetherteam.aether.oldcode;

import java.util.Random;

public class NameGenerator
{
    private static final Random rand = new Random();
    private static final Object[][] vowels = new Object[][] {a("a", 7), a("e", 7), a("i", 7), a("o", 7), a("u", 7), a("a", 7), a("e", 7), a("i", 7), a("o", 7), a("u", 7), a("a", 7), a("e", 7), a("i", 7), a("o", 7), a("u", 7), a("a", 7), a("e", 7), a("i", 7), a("o", 7), a("u", 7), a("a", 7), a("e", 7), a("i", 7), a("o", 7), a("u", 7), a("a", 7), a("e", 7), a("i", 7), a("o", 7), a("u", 7), a("a", 7), a("e", 7), a("i", 7), a("o", 7), a("u", 7), a("a", 7), a("e", 7), a("i", 7), a("o", 7), a("u", 7), a("a", 7), a("e", 7), a("i", 7), a("o", 7), a("u", 7), a("a", 7), a("e", 7), a("i", 7), a("o", 7), a("u", 7), a("a", 7), a("e", 7), a("i", 7), a("o", 7), a("u", 7), a("a", 7), a("e", 7), a("i", 7), a("o", 7), a("u", 7), a("ae", 7), a("ai", 7), a("ao", 7), a("au", 7), a("aa", 7), a("ea", 7), a("eo", 7), a("eu", 7), a("ee", 7), a("ia", 7), a("io", 7), a("iu", 7), a("ii", 7), a("oa", 7), a("oe", 7), a("oi", 7), a("ou", 7), a("oo", 7), a("eau", 7), a("\'", 4), a("y", 7)};
    private static final Object[][] consonants = new Object[][] {a("b", 7), a("c", 7), a("d", 7), a("f", 7), a("g", 7), a("h", 7), a("j", 7), a("k", 7), a("l", 7), a("m", 7), a("n", 7), a("p", 7), a("qu", 6), a("r", 7), a("s", 7), a("t", 7), a("v", 7), a("w", 7), a("x", 7), a("y", 7), a("z", 7), a("sc", 7), a("ch", 7), a("gh", 7), a("ph", 7), a("sh", 7), a("th", 7), a("wh", 6), a("ck", 5), a("nk", 5), a("rk", 5), a("sk", 7), a("wk", 0), a("cl", 3), a("fl", 3), a("gl", 3), a("kl", 3), a("ll", 3), a("pl", 3), a("sl", 3), a("br", 3), a("cr", 3), a("dr", 3), a("fr", 3), a("gr", 3), a("kr", 3), a("pr", 3), a("sr", 3), a("tr", 3), a("ss", 5), a("st", 7), a("str", 3), a("b", 7), a("c", 7), a("d", 7), a("f", 7), a("g", 7), a("h", 7), a("j", 7), a("k", 7), a("l", 7), a("m", 7), a("n", 7), a("p", 7), a("r", 7), a("s", 7), a("t", 7), a("v", 7), a("w", 7), a("b", 7), a("c", 7), a("d", 7), a("f", 7), a("g", 7), a("h", 7), a("j", 7), a("k", 7), a("l", 7), a("m", 7), a("n", 7), a("p", 7), a("r", 7), a("s", 7), a("t", 7), a("v", 7), a("w", 7), a("br", 3), a("dr", 3), a("fr", 3), a("gr", 3), a("kr", 3)};

    private static int rand(int var0, int var1)
    {
        return var0 + (int)(Math.random() * (double)(var1 - var0 + 1));
    }

    private static Object[] a(String var0, int var1)
    {
        return new Object[] {var0, Integer.valueOf(var1)};
    }

    public static String next(int var0, int var1)
    {
        Object[] var2 = null;
        String var3 = "";
        int var4 = rand(var0, var1);
        boolean var5 = rand.nextBoolean();
        boolean var6 = false;

        for (int var7 = 1; var7 <= var4; ++var7)
        {
            while (!var6)
            {
                if (var5)
                {
                    var2 = vowels[rand(0, vowels.length - 1)];
                }
                else
                {
                    var2 = consonants[rand(0, consonants.length - 1)];
                }

                int var8 = ((Integer)var2[1]).intValue();

                if (var7 == 1)
                {
                    if ((var8 & 2) > 0)
                    {
                        var6 = true;
                    }
                }
                else if (var7 == var4)
                {
                    if ((var8 & 1) > 0)
                    {
                        var6 = true;
                    }
                }
                else if ((var8 & 4) > 0)
                {
                    var6 = true;
                }
            }

            var3 = var3 + (String)var2[0];
            var5 = !var5;
            var6 = false;
        }

        var3 = var3.substring(0, 1).toUpperCase() + var3.substring(1);
        return var3;
    }
}
