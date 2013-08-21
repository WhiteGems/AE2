package net.aetherteam.aether.data;

public class AetherOptions
{
    private static boolean renderMemberHead = true;
    private static boolean minimalPartyHUD = false;
    private static boolean showPartyHUD = true;
    private static boolean showPartyName = true;
    private static boolean showNotifications = true;
    private static boolean slideCoinbar = true;

    public static void setMinimalPartyHUD(boolean option)
    {
        minimalPartyHUD = option;
    }

    public static void setRenderHead(boolean option)
    {
        renderMemberHead = option;
    }

    public static void setShowPartyHUD(boolean option)
    {
        showPartyHUD = option;
    }

    public static void setShowPartyName(boolean option)
    {
        showPartyName = option;
    }

    public static void setShowNotifications(boolean option)
    {
        showNotifications = option;
    }

    public static void setSlideCoinbar(boolean option)
    {
        slideCoinbar = option;
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
