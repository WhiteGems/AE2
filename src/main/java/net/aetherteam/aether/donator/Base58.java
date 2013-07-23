package net.aetherteam.aether.donator;

import java.math.BigInteger;

public class Base58
{
    private static final String ALPHABET = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz";
    private static final BigInteger BASE = BigInteger.valueOf(58L);

    public static String encode(byte[] var0)
    {
        BigInteger var1 = new BigInteger(1, var0);
        StringBuffer var2;
        BigInteger var3;

        for (var2 = new StringBuffer(); var1.compareTo(BASE) >= 0; var1 = var1.subtract(var3).divide(BASE))
        {
            var3 = var1.mod(BASE);
            var2.insert(0, "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz".charAt(var3.intValue()));
        }

        var2.insert(0, "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz".charAt(var1.intValue()));
        byte[] var7 = var0;
        int var4 = var0.length;

        for (int var5 = 0; var5 < var4; ++var5)
        {
            byte var6 = var7[var5];

            if (var6 != 0)
            {
                break;
            }

            var2.insert(0, "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz".charAt(0));
        }

        return var2.toString();
    }

    public static byte[] decode(String var0)
    {
        if (var0.length() == 0)
        {
            return null;
        }
        else
        {
            BigInteger var1 = decodeToBigInteger(var0);

            if (var1 == null)
            {
                return null;
            }
            else
            {
                byte[] var2 = var1.toByteArray();
                boolean var3 = var2.length > 1 && var2[0] == 0 && var2[1] < 0;
                int var4 = 0;

                for (int var5 = 0; var5 < var0.length() && var0.charAt(var5) == "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz".charAt(0); ++var5)
                {
                    ++var4;
                }

                byte[] var6 = new byte[var2.length - (var3 ? 1 : 0) + var4];
                System.arraycopy(var2, var3 ? 1 : 0, var6, var4, var6.length - var4);
                return var6;
            }
        }
    }

    private static BigInteger decodeToBigInteger(String var0)
    {
        BigInteger var1 = BigInteger.valueOf(0L);

        for (int var2 = var0.length() - 1; var2 >= 0; --var2)
        {
            int var3 = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz".indexOf(var0.charAt(var2));

            if (var3 == -1)
            {
                return null;
            }

            var1 = var1.add(BigInteger.valueOf((long)var3).multiply(BASE.pow(var0.length() - 1 - var2)));
        }

        return var1;
    }
}
