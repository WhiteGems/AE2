package net.aetherteam.aether.oldcode;

import java.util.Random;

public class NameGenerator
{
    private static final Random rand = new Random();
    private static final Object[][] vowels = { a("a", 7), a("e", 7), a("i", 7), a("o", 7), a("u", 7), a("a", 7), a("e", 7), a("i", 7), a("o", 7), a("u", 7), a("a", 7), a("e", 7), a("i", 7), a("o", 7), a("u", 7), a("a", 7), a("e", 7), a("i", 7), a("o", 7), a("u", 7), a("a", 7), a("e", 7), a("i", 7), a("o", 7), a("u", 7), a("a", 7), a("e", 7), a("i", 7), a("o", 7), a("u", 7), a("a", 7), a("e", 7), a("i", 7), a("o", 7), a("u", 7), a("a", 7), a("e", 7), a("i", 7), a("o", 7), a("u", 7), a("a", 7), a("e", 7), a("i", 7), a("o", 7), a("u", 7), a("a", 7), a("e", 7), a("i", 7), a("o", 7), a("u", 7), a("a", 7), a("e", 7), a("i", 7), a("o", 7), a("u", 7), a("a", 7), a("e", 7), a("i", 7), a("o", 7), a("u", 7), a("ae", 7), a("ai", 7), a("ao", 7), a("au", 7), a("aa", 7), a("ea", 7), a("eo", 7), a("eu", 7), a("ee", 7), a("ia", 7), a("io", 7), a("iu", 7), a("ii", 7), a("oa", 7), a("oe", 7), a("oi", 7), a("ou", 7), a("oo", 7), a("eau", 7), a("'", 4), a("y", 7) };

    private static final Object[][] consonants = { a("b", 7), a("c", 7), a("d", 7), a("f", 7), a("g", 7), a("h", 7), a("j", 7), a("k", 7), a("l", 7), a("m", 7), a("n", 7), a("p", 7), a("qu", 6), a("r", 7), a("s", 7), a("t", 7), a("v", 7), a("w", 7), a("x", 7), a("y", 7), a("z", 7), a("sc", 7), a("ch", 7), a("gh", 7), a("ph", 7), a("sh", 7), a("th", 7), a("wh", 6), a("ck", 5), a("nk", 5), a("rk", 5), a("sk", 7), a("wk", 0), a("cl", 3), a("fl", 3), a("gl", 3), a("kl", 3), a("ll", 3), a("pl", 3), a("sl", 3), a("br", 3), a("cr", 3), a("dr", 3), a("fr", 3), a("gr", 3), a("kr", 3), a("pr", 3), a("sr", 3), a("tr", 3), a("ss", 5), a("st", 7), a("str", 3), a("b", 7), a("c", 7), a("d", 7), a("f", 7), a("g", 7), a("h", 7), a("j", 7), a("k", 7), a("l", 7), a("m", 7), a("n", 7), a("p", 7), a("r", 7), a("s", 7), a("t", 7), a("v", 7), a("w", 7), a("b", 7), a("c", 7), a("d", 7), a("f", 7), a("g", 7), a("h", 7), a("j", 7), a("k", 7), a("l", 7), a("m", 7), a("n", 7), a("p", 7), a("r", 7), a("s", 7), a("t", 7), a("v", 7), a("w", 7), a("br", 3), a("dr", 3), a("fr", 3), a("gr", 3), a("kr", 3) };

    private static int rand(int min, int max)
    {
        return min + (int)(Math.random() * (max - min + 1));
    }

    private static Object[] a(String s, int i)
    {
        return new Object[] { s, Integer.valueOf(i) };
    }

    public static String next(int min, int max)
    {
        Object[] data = null;
        String name = "";
        int len = rand(min, max);
        boolean isVow = rand.nextBoolean();
        boolean found = false;

        for (int i = 1; i <= len; i++)
        {
            while (!found)
            {
                if (isVow)
                {
                    data = vowels[rand(0, vowels.length - 1)];
                }
                else
                {
                    data = consonants[rand(0, consonants.length - 1)];
                }

                int flag = ((Integer)data[1]).intValue();

                if (i == 1)
                {
                    if ((flag & 0x2) > 0)
                    {
                        found = true;
                    }
                }
                else if (i == len)
                {
                    if ((flag & 0x1) > 0)
                    {
                        found = true;
                    }
                }
                else if ((flag & 0x4) > 0)
                {
                    found = true;
                }
            }

            name = name + (String)data[0];
            isVow = !isVow;
            found = false;
        }

        name = name.substring(0, 1).toUpperCase() + name.substring(1);
        return name;
    }
}

