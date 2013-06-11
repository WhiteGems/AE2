package net.aetherteam.aether.entities.camera;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class CameraManager
{
    private static Minecraft mc = Minecraft.getMinecraft();
    private static EntityPlayer player = mc.thePlayer;
    private static Camera activeCamera;
    private static Entity viewingEntity = mc.renderViewEntity;

    public static void useCamera(World var0, Camera var1)
    {
        if (!var0.isRemote)
        {
            activeCamera = var1;
            var0.spawnEntityInWorld(activeCamera);
            mc.renderViewEntity = var1;
        }
    }

    public static void turnOffCamera(World var0)
    {
        if (!var0.isRemote)
        {
            mc.renderViewEntity = player;
            activeCamera.setDead();
            activeCamera = null;
        }
    }
}
