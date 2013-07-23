package net.aetherteam.mainmenu_api;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class MenuBaseSorter
{
    public static HashMap menuBaseClasses = new HashMap();

    public static void addMenuToSorter(String var0, Class var1)
    {
        menuBaseClasses.put(var0, var1);
    }

    public static boolean isMenuRegistered(String var0)
    {
        for (int var1 = 0; var1 < menuBaseClasses.size(); ++var1)
        {
            if (menuBaseClasses.get(var0) != null)
            {
                return true;
            }
        }

        return false;
    }

    public static MenuBase createMenuBaseObject(String var0)
    {
        if (!isMenuRegistered(var0))
        {
            System.out.println("The Menu Base \'" + var0 + "\' has not been registered!");
            return null;
        }
        else
        {
            Class var1 = (Class)menuBaseClasses.get(var0);

            if (var1 == null)
            {
                throw new NullPointerException("The Menu Base \'" + var0 + "\' has a null MenuBase class!");
            }
            else
            {
                MenuBase var2 = null;

                try
                {
                    Constructor var3 = null;

                    try
                    {
                        var3 = var1.getConstructor(new Class[0]);
                    }
                    catch (NoSuchMethodException var7)
                    {
                        var7.printStackTrace();
                    }
                    catch (SecurityException var8)
                    {
                        var8.printStackTrace();
                    }

                    try
                    {
                        var2 = (MenuBase)var3.newInstance(new Object[0]);
                    }
                    catch (IllegalArgumentException var5)
                    {
                        var5.printStackTrace();
                    }
                    catch (InvocationTargetException var6)
                    {
                        var6.printStackTrace();
                    }
                }
                catch (InstantiationException var9)
                {
                    var9.printStackTrace();
                }
                catch (IllegalAccessException var10)
                {
                    var10.printStackTrace();
                }

                if (var2 == null)
                {
                    throw new NullPointerException("Menu API failed to create a Menu Base object of \'" + var0 + "\'!");
                }
                else
                {
                    return var2;
                }
            }
        }
    }

    public static Class getMenuBaseClass(String var0)
    {
        return (Class)menuBaseClasses.get(var0);
    }

    public static int getSize()
    {
        return menuBaseClasses.size();
    }

    public static HashMap getMenuHashMap()
    {
        return menuBaseClasses;
    }
}
