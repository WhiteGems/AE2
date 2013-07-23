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

    public static void useCamera(World world, Camera camera)
    {
        if (!world.isRemote)
        {
            activeCamera = camera;
            world.spawnEntityInWorld(activeCamera);
            mc.renderViewEntity = camera;
        }
    }

    public static void turnOffCamera(World world)
    {
        if (!world.isRemote)
        {
            mc.renderViewEntity = player;
            activeCamera.setDead();
            activeCamera = null;
        }
    }
}

