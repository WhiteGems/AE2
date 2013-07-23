package net.aetherteam.aether.notifications.client;

import java.util.ArrayList;

public class StringBox
{
    String text = new String();
    int boxCharWidth = 10;

    public StringBox(String text, int boxCharWidth)
    {
        this.text = text;
        this.boxCharWidth = boxCharWidth;
    }

    public ArrayList getStringList()
    {
        ArrayList strings = new ArrayList();

        while (this.text.length() > this.boxCharWidth)
        {
            String subDescription = this.text.substring(0, this.boxCharWidth);
            this.text = this.text.replace(this.text.substring(0, this.boxCharWidth), "");
            strings.add(subDescription);
        }

        if ((this.text.length() > 0) && (this.text.length() <= this.boxCharWidth))
        {
            strings.add(this.text);
        }

        return strings;
    }
}

