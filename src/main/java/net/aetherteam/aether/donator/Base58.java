package net.aetherteam.aether.donator;

import java.math.BigInteger;

public class Base58
{
    private static final String ALPHABET = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz";
    private static final BigInteger BASE = BigInteger.valueOf(58L);

    public static String encode(byte[] input)
    {
        BigInteger bi = new BigInteger(1, input);
        StringBuffer s = new StringBuffer();
        while (bi.compareTo(BASE) >= 0)
        {
            BigInteger mod = bi.mod(BASE);
            s.insert(0, "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz".charAt(mod.intValue()));
            bi = bi.subtract(mod).divide(BASE);
        }
        s.insert(0, "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz".charAt(bi.intValue()));

        for (byte anInput : input)
        {
            if (anInput != 0) break;
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
        BigInteger decoded = decodeToBigInteger(input);
        if (decoded == null)
        {
            return null;
        }
        byte[] bytes = decoded.toByteArray();

        boolean stripSignByte = (bytes.length > 1) && (bytes[0] == 0) && (bytes[1] < 0);

        int leadingZeros = 0;
        for (int i = 0; (i < input.length()) && (input.charAt(i) == "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz".charAt(0)); i++)
        {
            leadingZeros++;
        }

        byte[] tmp = new byte[bytes.length - (stripSignByte ? 1 : 0) + leadingZeros];

        System.arraycopy(bytes, stripSignByte ? 1 : 0, tmp, leadingZeros, tmp.length - leadingZeros);

        return tmp;
    }

    private static BigInteger decodeToBigInteger(String input)
    {
        BigInteger bi = BigInteger.valueOf(0L);

        for (int i = input.length() - 1; i >= 0; i--)
        {
            int alphaIndex = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz".indexOf(input.charAt(i));
            if (alphaIndex == -1)
            {
                return null;
            }
            bi = bi.add(BigInteger.valueOf(alphaIndex).multiply(BASE.pow(input.length() - 1 - i)));
        }

        return bi;
    }
}

/* Location:           D:\Dev\Mc\forge_orl\mcp\jars\bin\aether.jar
 * Qualified Name:     net.aetherteam.aether.donator.Base58
 * JD-Core Version:    0.6.2
 */