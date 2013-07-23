package net.aetherteam.aether.entities.mounts;

import cpw.mods.fml.common.network.PacketDispatcher;
import java.util.ArrayList;
import net.aetherteam.aether.packets.AetherPacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityRainFX;
import net.minecraft.util.MovementInputFromOptions;

public class MountSystem
{
    private static Minecraft mc = Minecraft.getMinecraft();
    private static MovementInputFromOptions player;
    public static ArrayList mountInput = new ArrayList();
    private static boolean isJumping;
    private static boolean movingForward;
    private static boolean movingBackward;
    private static boolean movingLeft;
    private static boolean movingRight;

    public static void processDirections()
    {
        player = mc.thePlayer;

        if (player != null)
        {
            float forward = player.b.b;
            float strafe = player.b.a;
            movingForward = forward > 0.1F;
            movingBackward = forward < -0.1F;
            movingLeft = strafe > 0.1F;
            movingRight = strafe < -0.1F;
            isJumping = player.bG;

            if (player.o != null)
            {
                applyInput(MountInput.FORWARD, movingForward);
                applyInput(MountInput.BACKWARD, movingBackward);
                applyInput(MountInput.LEFT, movingLeft);
                applyInput(MountInput.RIGHT, movingRight);
                applyInput(MountInput.JUMP, isJumping);
            }
        }
    }

    public static void applyInput(MountInput direction, boolean add)
    {
        if (add)
        {
            if (!mountInput.contains(direction))
            {
                mountInput.add(direction);
                sendInputPacket();
            }
        }
        else if (mountInput.contains(direction))
        {
            mountInput.remove(direction);
            sendInputPacket();
        }
    }

    private static void sendInputPacket()
    {
        PacketDispatcher.sendPacketToServer(AetherPacketHandler.sendPlayerInput(player.bS, mountInput, player.bG));
    }
}

