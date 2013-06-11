package net.aetherteam.aether.entities.mounts;

import cpw.mods.fml.common.network.PacketDispatcher;

import java.util.ArrayList;

import net.aetherteam.aether.packets.AetherPacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;

public class MountSystem
{
    private static Minecraft mc = Minecraft.getMinecraft();
    private static EntityPlayerSP player;
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
            float var0 = player.movementInput.moveForward;
            float var1 = player.movementInput.moveStrafe;
            movingForward = var0 > 0.1F;
            movingBackward = var0 < -0.1F;
            movingLeft = var1 > 0.1F;
            movingRight = var1 < -0.1F;
            isJumping = player.isJumping;

            if (player.ridingEntity != null)
            {
                applyInput(MountInput.FORWARD, movingForward);
                applyInput(MountInput.BACKWARD, movingBackward);
                applyInput(MountInput.LEFT, movingLeft);
                applyInput(MountInput.RIGHT, movingRight);
                applyInput(MountInput.JUMP, isJumping);
            }
        }
    }

    public static void applyInput(MountInput var0, boolean var1)
    {
        if (var1)
        {
            if (!mountInput.contains(var0))
            {
                mountInput.add(var0);
                sendInputPacket();
            }
        } else if (mountInput.contains(var0))
        {
            mountInput.remove(var0);
            sendInputPacket();
        }
    }

    private static void sendInputPacket()
    {
        PacketDispatcher.sendPacketToServer(AetherPacketHandler.sendPlayerInput(player.username, mountInput, player.isJumping));
    }
}
