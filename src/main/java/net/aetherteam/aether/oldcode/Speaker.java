package net.aetherteam.aether.oldcode;

import java.io.PrintStream;
import java.util.ArrayList;

public class Speaker
    implements Runnable
{
    public ArrayList voiceQueue;
    public SpeakerLine voiceLine;
    public Thread voiceThread;

    public Speaker()
    {
        this.voiceQueue = new ArrayList();
        this.voiceThread = new Thread(this);
        this.voiceThread.start();
    }

    public void say(SpeakerLine sl)
    {
        this.voiceQueue.add(sl);
    }

    public void run()
    {
        while (true)
            try
            {
                update();
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
    }

    public void update() throws InterruptedException
    {
        this.voiceLine = ((SpeakerLine)this.voiceQueue.get(0));
        this.voiceThread.wait(this.voiceLine.duration);
        this.voiceQueue.remove(this.voiceLine);
    }

    public void render()
    {
        System.out.println(this.voiceLine.getLine());
    }
}

