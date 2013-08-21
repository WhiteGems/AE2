package net.aetherteam.mainmenu_api;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class MenuBaseSorter
{
    public static HashMap < String, Class <? extends MenuBase >> menuBaseClasses = new HashMap();

    public static void addMenuToSorter(String menuName, Class <? extends MenuBase > menu)
    {
        menuBaseClasses.put(menuName, menu);
    }

    public static boolean isMenuRegistered(String menuName)
    {
        for (int count = 0; count < menuBaseClasses.size(); ++count)
        {
            if (menuBaseClasses.get(menuName) != null)
            {
                return true;
            }
        }

        return false;
    }

    public static MenuBase createMenuBaseObject(String menuName)
    {
        if (!isMenuRegistered(menuName))
        {
            System.out.println("The Menu Base \'" + menuName + "\' has not been registered!");
            return null;
        }
        else
        {
            Class menuBase = (Class)menuBaseClasses.get(menuName);

            if (menuBase == null)
            {
                throw new NullPointerException("The Menu Base \'" + menuName + "\' has a null MenuBase class!");
            }
            else
            {
                MenuBase menu = null;

                try
                {
                    Constructor e = null;

                    try
                    {
                        e = menuBase.getConstructor(new Class[0]);
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
                        menu = (MenuBase)e.newInstance(new Object[0]);
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

                if (menu == null)
                {
                    throw new NullPointerException("Menu API failed to create a Menu Base object of \'" + menuName + "\'!");
                }
                else
                {
                    return menu;
                }
            }
        }
    }

    public static Class getMenuBaseClass(String menuName)
    {
        return (Class)menuBaseClasses.get(menuName);
    }

    public static int getSize()
    {
        return menuBaseClasses.size();
    }

    public static HashMap < String, Class <? extends MenuBase >> getMenuHashMap()
    {
        return menuBaseClasses;
    }
}
