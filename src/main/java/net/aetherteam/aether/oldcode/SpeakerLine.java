package net.aetherteam.aether.oldcode;

public class SpeakerLine
{
    public static final String AUTHOR_COLOR = "§7";
    public static final String RECIPIENT_COLOR = "§7";
    public static final String MESSAGE_COLOR = "§r";
    public static final String DEFAULT_COLOR = "§r";
    public boolean hasAuthor;
    public boolean hasRecipient;
    public boolean hasContent;
    public String author;
    public String recipient;
    public String content;
    public long duration;

    public SpeakerLine(String author, String recipient, String content, long duration)
    {
        this.author = String.format("[%s%s%s] ", new Object[] { "§7", author, "§r" });
        this.hasAuthor = true;
        this.recipient = String.format("%s%s%s: ", new Object[] { "§7", recipient, "§r" });
        this.hasRecipient = true;
        this.content = String.format("%s%s%s", new Object[] { "§r", content, "§r" });
        this.hasContent = true;
        this.duration = duration;
    }

    public SpeakerLine(String author, String recipient, String content)
    {
        this.author = String.format("[%s%s%s] ", new Object[] { "§7", author, "§r" });
        this.hasAuthor = true;
        this.recipient = String.format("%s%s%s: ", new Object[] { "§7", recipient, "§r" });
        this.hasRecipient = true;
        this.content = String.format("%s%s%s", new Object[] { "§r", content, "§r" });
        this.hasContent = true;
    }

    public SpeakerLine(String author, String content)
    {
        this.author = String.format("[%s%s%s] ", new Object[] { "§7", author, "§r" });
        this.hasAuthor = true;
        this.content = String.format("%s%s%s", new Object[] { "§r", content, "§r" });
        this.hasContent = true;
    }

    public SpeakerLine(String content)
    {
        this.content = String.format("%s%s%s", new Object[] { "§r", content, "§r" });
        this.hasContent = true;
    }

    public String getLine()
    {
        StringBuilder sb = new StringBuilder();

        if (this.hasAuthor)
        {
            sb.append(this.author);
        }

        if (this.hasRecipient)
        {
            sb.append(this.recipient);
        }

        if (this.hasContent)
        {
            sb.append(this.content);
        }

        return sb.length() > 0 ? sb.toString() : "Unable to fetch line.";
    }
}

