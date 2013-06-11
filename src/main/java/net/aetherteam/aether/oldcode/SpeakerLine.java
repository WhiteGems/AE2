package net.aetherteam.aether.oldcode;

public class SpeakerLine
{
    public static final String AUTHOR_COLOR = "\u00a77";
    public static final String RECIPIENT_COLOR = "\u00a77";
    public static final String MESSAGE_COLOR = "\u00a7r";
    public static final String DEFAULT_COLOR = "\u00a7r";
    public boolean hasAuthor;
    public boolean hasRecipient;
    public boolean hasContent;
    public String author;
    public String recipient;
    public String content;
    public long duration;

    public SpeakerLine(String var1, String var2, String var3, long var4)
    {
        this.author = String.format("[%s%s%s] ", new Object[]{"\u00a77", var1, "\u00a7r"});
        this.hasAuthor = true;
        this.recipient = String.format("%s%s%s: ", new Object[]{"\u00a77", var2, "\u00a7r"});
        this.hasRecipient = true;
        this.content = String.format("%s%s%s", new Object[]{"\u00a7r", var3, "\u00a7r"});
        this.hasContent = true;
        this.duration = var4;
    }

    public SpeakerLine(String var1, String var2, String var3)
    {
        this.author = String.format("[%s%s%s] ", new Object[]{"\u00a77", var1, "\u00a7r"});
        this.hasAuthor = true;
        this.recipient = String.format("%s%s%s: ", new Object[]{"\u00a77", var2, "\u00a7r"});
        this.hasRecipient = true;
        this.content = String.format("%s%s%s", new Object[]{"\u00a7r", var3, "\u00a7r"});
        this.hasContent = true;
    }

    public SpeakerLine(String var1, String var2)
    {
        this.author = String.format("[%s%s%s] ", new Object[]{"\u00a77", var1, "\u00a7r"});
        this.hasAuthor = true;
        this.content = String.format("%s%s%s", new Object[]{"\u00a7r", var2, "\u00a7r"});
        this.hasContent = true;
    }

    public SpeakerLine(String var1)
    {
        this.content = String.format("%s%s%s", new Object[]{"\u00a7r", var1, "\u00a7r"});
        this.hasContent = true;
    }

    public String getLine()
    {
        StringBuilder var1 = new StringBuilder();

        if (this.hasAuthor)
        {
            var1.append(this.author);
        }

        if (this.hasRecipient)
        {
            var1.append(this.recipient);
        }

        if (this.hasContent)
        {
            var1.append(this.content);
        }

        return var1.length() > 0 ? var1.toString() : "无法读取该行.";
    }
}
