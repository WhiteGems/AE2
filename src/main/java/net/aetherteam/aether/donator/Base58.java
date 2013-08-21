package net.aetherteam.aether.donator;

import java.math.BigInteger;

public class Base58
{
    private static final String ALPHABET = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz";
    private static final BigInteger BASE = BigInteger.valueOf(58L);

    public static String encode(byte[] input)
    {
        BigInteger bi = new BigInteger(1, input);
        StringBuffer s;
        BigInteger arr$;

        for (s = new StringBuffer(); bi.compareTo(BASE) >= 0; bi = bi.subtract(arr$).divide(BASE))
        {
            arr$ = bi.mod(BASE);
            s.insert(0, "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz".charAt(arr$.intValue()));
        }

        s.insert(0, "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz".charAt(bi.intValue()));
        byte[] var7 = input;
        int len$ = input.length;

        for (int i$ = 0; i$ < len$; ++i$)
        {
            byte anInput = var7[i$];

            if (anInput != 0)
            {
                break;
            }

            s.insert(0, "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz".charAt(0));
        }

        return s.toString();
    }

    public static byte[] decode(String input)
    {
        if (input.length() == 0)
        {
            return null;
        }
        else
        {
            BigInteger decoded = decodeToBigInteger(input);

            if (decoded == null)
            {
                return null;
            }
            else
            {
                byte[] bytes = decoded.toByteArray();
                boolean stripSignByte = bytes.length > 1 && bytes[0] == 0 && bytes[1] < 0;
                int leadingZeros = 0;

                for (int tmp = 0; tmp < input.length() && input.charAt(tmp) == "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz".charAt(0); ++tmp)
                {
                    ++leadingZeros;
                }

                byte[] var6 = new byte[bytes.length - (stripSignByte ? 1 : 0) + leadingZeros];
                System.arraycopy(bytes, stripSignByte ? 1 : 0, var6, leadingZeros, var6.length - leadingZeros);
                return var6;
            }
        }
    }

    private static BigInteger decodeToBigInteger(String input)
    {
        BigInteger bi = BigInteger.valueOf(0L);

        for (int i = input.length() - 1; i >= 0; --i)
        {
            int alphaIndex = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz".indexOf(input.charAt(i));

            if (alphaIndex == -1)
            {
                return null;
            }

            bi = bi.add(BigInteger.valueOf((long)alphaIndex).multiply(BASE.pow(input.length() - 1 - i)));
        }

        return bi;
    }
}
