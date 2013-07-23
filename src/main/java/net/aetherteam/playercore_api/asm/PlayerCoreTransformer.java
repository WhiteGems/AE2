package net.aetherteam.playercore_api.asm;

import cpw.mods.fml.relauncher.IClassTransformer;
import java.util.ListIterator;
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
    private static final String OBF_RENDERMANAGER = "bgy";
    private static final String MCP_PLAYERCONTROLLERMP = "net.minecraft.client.multiplayer.PlayerControllerMP";
    private static final String OBF_PLAYERCONTROLLERMP = "bdr";
    private static final String MCP_SCM = "net.minecraft.server.management.ServerConfigurationManager";
    private static final String OBF_SCM = "gu";
    private static final String PLAYERCORE_CLIENT = "net.aetherteam.playercore_api.cores.PlayerCoreClient";
    private static final String PLAYERCORE_SERVER = "net.aetherteam.playercore_api.cores.PlayerCoreServer";
    private static final String PLAYERCORE_RENDER = "net.aetherteam.playercore_api.cores.PlayerCoreRender";
    private static final boolean DEBUG_MODE = false;

    public byte[] transform(String var1, String var2, byte[] var3)
    {
        ClassReader var4;
        ClassNode var5;
        MethodNode var6;
        TypeInsnNode var7;
        MethodInsnNode var8;
        ClassWriter var9;

        if (!var1.equals("net.minecraft.client.renderer.entity.RenderManager") && !var1.equals("bgy"))
        {
            if (!var1.equals("net.minecraft.server.management.ServerConfigurationManager") && !var1.equals("gu"))
            {
                if (!var1.equals("net.minecraft.client.multiplayer.PlayerControllerMP") && !var1.equals("bdr"))
                {
                    return var3;
                }
                else
                {
                    var4 = new ClassReader(var3);
                    var5 = new ClassNode();
                    var4.accept(var5, 0);
                    var6 = (MethodNode)var5.methods.get(17);
                    var7 = (TypeInsnNode)var6.instructions.get(2);
                    var7.desc = "net.aetherteam.playercore_api.cores.PlayerCoreClient".replace(".", "/");
                    var8 = (MethodInsnNode)var6.instructions.get(12);
                    var8.owner = "net.aetherteam.playercore_api.cores.PlayerCoreClient".replace(".", "/");
                    var9 = new ClassWriter(3);
                    var5.accept(var9);
                    return var9.toByteArray();
                }
            }
            else
            {
                var4 = new ClassReader(var3);
                var5 = new ClassNode();
                var4.accept(var5, 0);
                var6 = (MethodNode)var5.methods.get(12);
                System.out.println("analysing methodNodeCreatePlayer, name: " + var6.name + ", desc: " + var6.desc);
                this.analyse(var6);
                var7 = (TypeInsnNode)var6.instructions.get(101);
                var7.desc = "net.aetherteam.playercore_api.cores.PlayerCoreServer".replace(".", "/");
                var8 = (MethodInsnNode)var6.instructions.get(112);
                var8.owner = "net.aetherteam.playercore_api.cores.PlayerCoreServer".replace(".", "/");
                MethodNode var13 = (MethodNode)var5.methods.get(13);
                System.out.println("analysing methodNodeRespawnPlayer, name: " + var13.name + ", desc: " + var13.desc);
                this.analyse(var13);
                TypeInsnNode var10 = (TypeInsnNode)var13.instructions.get(117);
                var10.desc = "net.aetherteam.playercore_api.cores.PlayerCoreServer".replace(".", "/");
                MethodInsnNode var11 = (MethodInsnNode)var13.instructions.get(130);
                var11.owner = "net.aetherteam.playercore_api.cores.PlayerCoreServer".replace(".", "/");
                ClassWriter var12 = new ClassWriter(0);
                var5.accept(var12);
                return var12.toByteArray();
            }
        }
        else
        {
            var4 = new ClassReader(var3);
            var5 = new ClassNode();
            var4.accept(var5, 0);
            var6 = (MethodNode)var5.methods.get(0);
            var7 = (TypeInsnNode)var6.instructions.get(253);
            var7.desc = "net.aetherteam.playercore_api.cores.PlayerCoreRender".replace(".", "/");
            var8 = (MethodInsnNode)var6.instructions.get(255);
            var8.owner = "net.aetherteam.playercore_api.cores.PlayerCoreRender".replace(".", "/");
            var9 = new ClassWriter(3);
            var5.accept(var9);
            return var9.toByteArray();
        }
    }

    public void analyse(MethodNode var1)
    {
        ListIterator var2 = var1.instructions.iterator();

        for (int var3 = 0; var2.hasNext(); ++var3)
        {
            AbstractInsnNode var4 = (AbstractInsnNode)var2.next();

            if (var4 instanceof TypeInsnNode)
            {
                TypeInsnNode var5 = (TypeInsnNode)var4;
                System.out.println(var3 + ": TypeInsnNode, desc: " + var5.desc);
            }

            if (var4 instanceof MethodInsnNode)
            {
                MethodInsnNode var6 = (MethodInsnNode)var4;
                System.out.println(var3 + ": MethodInsnNode, owner: " + var6.owner);
            }
        }
    }
}
