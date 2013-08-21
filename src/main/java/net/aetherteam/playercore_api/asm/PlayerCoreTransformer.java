package net.aetherteam.playercore_api.asm;

import java.io.IOException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.logging.Logger;
import net.aetherteam.playercore_api.PlayerCoreLoadingPlugin;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;

public class PlayerCoreTransformer implements IClassTransformer
{
    private static final String MCP_RENDERMANAGER = "net.minecraft.client.renderer.entity.RenderManager";
    private static final String OBF_RENDERMANAGER = "bgi";
    private static final String MCP_PLAYERCONTROLLERMP = "net.minecraft.client.multiplayer.PlayerControllerMP";
    private static final String OBF_PLAYERCONTROLLERMP = "bcz";
    private static final String MCP_SCM = "net.minecraft.server.management.ServerConfigurationManager";
    private static final String OBF_SCM = "hm";
    private static final String MCP_ENTITYPLAYERSP = "net.minecraft.client.entity.EntityClientPlayerMP";
    private static final String OBF_ENTITYPLAYERSP = "bdf";
    private static final String MCP_ENTITYPLAYERMP = "net.minecraft.entity.player.EntityPlayerMP";
    private static final String OBF_ENTITYPLAYERMP = "ju";
    private static final String MCP_RENDERPLAYER = "net.minecraft.client.renderer.entity.RenderPlayer";
    private static final String OBF_RENDERPLAYER = "bhg";
    private static final String PLAYERCORE_CLIENT = "net.aetherteam.playercore_api.cores.PlayerCoreClient";
    private static final String PLAYERCORE_SERVER = "net.aetherteam.playercore_api.cores.PlayerCoreServer";
    private static final String PLAYERCORE_RENDER = "net.aetherteam.playercore_api.cores.PlayerCoreRender";
    private static boolean checkedObfuscation;
    private static boolean isObfuscated;

    public byte[] transform(String name, String transformedName, byte[] bytes)
    {
        if (!checkedObfuscation)
        {
            LaunchClassLoader classReader = (LaunchClassLoader)PlayerCoreLoadingPlugin.class.getClassLoader();

            try
            {
                isObfuscated = classReader.getClassBytes("net.minecraft.world.World") == null;
            }
            catch (IOException var7)
            {
                ;
            }

            checkedObfuscation = true;
        }

        ClassNode classNode;
        ClassWriter cw;
        ClassReader classReader1;

        if (!name.equals("net.minecraft.client.renderer.entity.RenderManager") && !name.equals("bgi"))
        {
            if (!name.equals("net.minecraft.server.management.ServerConfigurationManager") && !name.equals("hm"))
            {
                if (!name.equals("net.minecraft.client.multiplayer.PlayerControllerMP") && !name.equals("bcz"))
                {
                    return bytes;
                }
                else
                {
                    classReader1 = new ClassReader(bytes);
                    classNode = new ClassNode();
                    classReader1.accept(classNode, 0);
                    this.replaceInstance(classNode, isObfuscated ? "bdf" : "net.minecraft.client.entity.EntityClientPlayerMP", "net.aetherteam.playercore_api.cores.PlayerCoreClient");
                    cw = new ClassWriter(3);
                    classNode.accept(cw);
                    return cw.toByteArray();
                }
            }
            else
            {
                classReader1 = new ClassReader(bytes);
                classNode = new ClassNode();
                classReader1.accept(classNode, 0);
                this.replaceInstance(classNode, isObfuscated ? "ju" : "net.minecraft.entity.player.EntityPlayerMP", "net.aetherteam.playercore_api.cores.PlayerCoreServer");
                cw = new ClassWriter(0);
                classNode.accept(cw);
                return cw.toByteArray();
            }
        }
        else
        {
            classReader1 = new ClassReader(bytes);
            classNode = new ClassNode();
            classReader1.accept(classNode, 0);
            this.replaceInstance(classNode, isObfuscated ? "bhg" : "net.minecraft.client.renderer.entity.RenderPlayer", "net.aetherteam.playercore_api.cores.PlayerCoreRender");
            cw = new ClassWriter(3);
            classNode.accept(cw);
            return cw.toByteArray();
        }
    }

    public void replaceInstance(ClassNode classNode, String oldInstance, String newInstance)
    {
        Logger logger = Logger.getLogger("PlayerCoreTransformer");
        System.out.println("class: " + classNode.name);
        System.out.println("replacing " + oldInstance + " with " + newInstance);
        oldInstance = oldInstance.replace(".", "/");
        newInstance = newInstance.replace(".", "/");
        Iterator i$ = classNode.methods.iterator();

        while (i$.hasNext())
        {
            MethodNode methodNode = (MethodNode)i$.next();
            ListIterator iter = methodNode.instructions.iterator();
            TypeInsnNode previousTypeInsnNode = null;

            while (iter.hasNext())
            {
                AbstractInsnNode node = (AbstractInsnNode)iter.next();

                if (node instanceof TypeInsnNode)
                {
                    TypeInsnNode mn = (TypeInsnNode)node;

                    if (mn.desc.equals(oldInstance))
                    {
                        previousTypeInsnNode = mn;
                    }
                }

                if (node instanceof MethodInsnNode)
                {
                    MethodInsnNode mn1 = (MethodInsnNode)node;

                    if (mn1.owner.equals(oldInstance) && mn1.name.equals("<init>"))
                    {
                        mn1.owner = newInstance;
                        previousTypeInsnNode.desc = newInstance;
                        System.out.println("found injection point: method: " + methodNode.name + " typeinsn: " + methodNode.instructions.indexOf(previousTypeInsnNode) + " methodinsn: " + methodNode.instructions.indexOf(mn1));
                    }
                }
            }
        }
    }
}
