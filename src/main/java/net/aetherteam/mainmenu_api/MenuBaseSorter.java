package net.aetherteam.mainmenu_api;

import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class MenuBaseSorter
{
    public static HashMap menuBaseClasses = new HashMap();

    public static void addMenuToSorter(String menuName, Class menu)
    {
        menuBaseClasses.put(menuName, menu);
    }

    public static boolean isMenuRegistered(String menuName)
    {
        for (int count = 0; count < menuBaseClasses.size(); count++)
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
            System.out.println("The Menu Base '" + menuName + "' has not been registered!");
            return null;
        }

        Class menuBase = (Class) menuBaseClasses.get(menuName);

        if (menuBase == null)
        {
            throw new NullPointerException("The Menu Base '" + menuName + "' has a null MenuBase class!");
        }

        MenuBase menu = null;
        try
        {
            Constructor c = null;
            try
            {
                c = menuBase.getConstructor(new Class[0]);
            } catch (NoSuchMethodException e)
            {
                e.printStackTrace();
            } catch (SecurityException e)
            {
                e.printStackTrace();
            }
            try
            {
                menu = (MenuBase) c.newInstance(new Object[0]);
            } catch (IllegalArgumentException e)
            {
                e.printStackTrace();
            } catch (InvocationTargetException e)
            {
                e.printStackTrace();
            }
        } catch (InstantiationException e)
        {
            e.printStackTrace();
        } catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }

        if (menu == null)
        {
            throw new NullPointerException("Menu API failed to create a Menu Base object of '" + menuName + "'!");
        }

        return menu;
    }

    public static Class getMenuBaseClass(String menuName)
    {
        return (Class) menuBaseClasses.get(menuName);
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

/* Location:           D:\Dev\Mc\forge_orl\mcp\jars\bin\aether.jar
 * Qualified Name:     net.aetherteam.mainmenu_api.MenuBaseSorter
 * JD-Core Version:    0.6.2
 */