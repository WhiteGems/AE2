package net.aetherteam.aether.data;

public class AetherOptions
{
    private static boolean renderMemberHead = true;
    private static boolean minimalPartyHUD = false;
    private static boolean showPartyHUD = true;
    private static boolean showPartyName = true;
    private static boolean showNotifications = true;
    private static boolean slideCoinbar = true;

    public static void setMinimalPartyHUD(boolean var0)
    {
        minimalPartyHUD = var0;
    }

    public static void setRenderHead(boolean var0)
    {
        renderMemberHead = var0;
    }

    public static void setShowPartyHUD(boolean var0)
    {
        showPartyHUD = var0;
    }

    public static void setShowPartyName(boolean var0)
    {
        showPartyName = var0;
    }

    public static void setShowNotifications(boolean var0)
    {
        showNotifications = var0;
    }

    public static void setSlideCoinbar(boolean var0)
    {
        slideCoinbar = var0;
    }

    public static boolean getRenderHead()
    {
        return renderMemberHead;
    }

    public static boolean getMinimalPartyHUD()
    {
        return minimalPartyHUD;
    }

    public static boolean getShowPartyHUD()
    {
        return showPartyHUD;
    }

    public static boolean getShowPartyName()
    {
        return showPartyName;
    }

    public static boolean getShowNotifications()
    {
        return showNotifications;
    }

    public static boolean getSlideCoinbar()
    {
        return slideCoinbar;
    }
}
