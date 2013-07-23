package net.aetherteam.aether.notifications.client;

import java.util.ArrayList;

public class StringBox
{
    String text = new String();
    int boxCharWidth = 10;

    public StringBox(String var1, int var2)
    {
        this.text = var1;
        this.boxCharWidth = var2;
    }

    public ArrayList getStringList()
    {
        ArrayList var1 = new ArrayList();

        while (this.text.length() > this.boxCharWidth)
        {
            String var2 = this.text.substring(0, this.boxCharWidth);
            this.text = this.text.replace(this.text.substring(0, this.boxCharWidth), "");
            var1.add(var2);
        }

        if (this.text.length() > 0 && this.text.length() <= this.boxCharWidth)
        {
            var1.add(this.text);
        }

        return var1;
    }
}
