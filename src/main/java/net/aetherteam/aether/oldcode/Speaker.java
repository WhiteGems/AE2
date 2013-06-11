package net.aetherteam.aether.oldcode;

import java.util.ArrayList;

public class Speaker implements Runnable
{
    public ArrayList voiceQueue = new ArrayList();
    public SpeakerLine voiceLine;
    public Thread voiceThread = new Thread(this);

    public Speaker()
    {
        this.voiceThread.start();
    }

    public void say(SpeakerLine var1)
    {
        this.voiceQueue.add(var1);
    }

    public void run()
    {
        while (true)
        {
            try
            {
                while (true)
                {
                    this.update();
                }
            } catch (InterruptedException var2)
            {
                var2.printStackTrace();
            }
        }
    }

    public void update() throws InterruptedException
    {
        this.voiceLine = (SpeakerLine) this.voiceQueue.get(0);
        this.voiceThread.wait(this.voiceLine.duration);
        this.voiceQueue.remove(this.voiceLine);
    }

    public void render()
    {
        System.out.println(this.voiceLine.getLine());
    }
}
