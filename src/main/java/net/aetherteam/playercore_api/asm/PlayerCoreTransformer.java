package net.aetherteam.playercore_api.asm;

import cpw.mods.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import cpw.mods.fml.relauncher.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.*;

import java.util.Iterator;

public class PlayerCoreTransformer implements IClassTransformer
{
    private static final String MCP_RENDERMANAGER = "net.minecraft.client.renderer.entity.RenderManager";
    private static final String OBF_RENDERMANAGER = FMLDeobfuscatingRemapper.INSTANCE.unmap(MCP_RENDERMANAGER.replace(".","/")); //"bgy";
    private static final String MCP_PLAYERCONTROLLERMP = "net.minecraft.client.multiplayer.PlayerControllerMP";
    private static final String OBF_PLAYERCONTROLLERMP = FMLDeobfuscatingRemapper.INSTANCE.unmap(MCP_PLAYERCONTROLLERMP);//"bdr";
    private static final String MCP_SCM = "net.minecraft.server.management.ServerConfigurationManager";
    private static final String OBF_SCM = FMLDeobfuscatingRemapper.INSTANCE.unmap(MCP_SCM);//"gu";
    private static final String PLAYERCORE_CLIENT = "net.aetherteam.playercore_api.cores.PlayerCoreClient";
    private static final String PLAYERCORE_SERVER = "net.aetherteam.playercore_api.cores.PlayerCoreServer";
    private static final String PLAYERCORE_RENDER = "net.aetherteam.playercore_api.cores.PlayerCoreRender";
    private static final boolean DEBUG_MODE = false;

    public byte[] transform(String name, String transformedName, byte[] bytes)
    {
        if (name.equals(OBF_RENDERMANAGER) || name.equals(MCP_RENDERMANAGER))
        {
            return transformRender(name,transformedName,bytes);
        }

        if ( name.equals(OBF_SCM) || name.equals(MCP_SCM))
        {
            return transformServer(name, transformedName, bytes);
        }

        if (name.equals(OBF_PLAYERCONTROLLERMP) || name.equals(MCP_PLAYERCONTROLLERMP))
        {
            return transformClient(name, transformedName, bytes);
        }

        return bytes;
    }

    public byte[] transformRender(String name, String transformedName, byte[] bytes)
    {
        ClassReader classReader = new ClassReader(bytes); ClassNode classNode = new ClassNode();
        classReader.accept(classNode, 0);

        MethodNode methodNode = classNode.methods.get(0);

        TypeInsnNode tn = (TypeInsnNode)methodNode.instructions.get(253);
        tn.desc = PLAYERCORE_RENDER.replace(".", "/");

        MethodInsnNode mn = (MethodInsnNode)methodNode.instructions.get(255);
        mn.owner = PLAYERCORE_RENDER.replace(".", "/");

        ClassWriter cw = new ClassWriter(3); classNode.accept(cw);

        return cw.toByteArray();
    }
    public byte[] transformClient(String name, String transformedName, byte[] bytes)
    {
        ClassReader classReader = new ClassReader(bytes); ClassNode classNode = new ClassNode();
        classReader.accept(classNode, 0);

        MethodNode methodNode = classNode.methods.get(17);

        TypeInsnNode tn = (TypeInsnNode)methodNode.instructions.get(2);
        tn.desc = PLAYERCORE_CLIENT.replace(".", "/");

        MethodInsnNode mn = (MethodInsnNode)methodNode.instructions.get(12);
        mn.owner = PLAYERCORE_CLIENT.replace(".", "/");

        ClassWriter cw = new ClassWriter(3); classNode.accept(cw);

        return cw.toByteArray();
    }
    public byte[] transformServer(String name, String transformedName, byte[] bytes)
    {
        ClassReader classReader = new ClassReader(bytes); ClassNode classNode = new ClassNode();
        classReader.accept(classNode, 0);

        MethodNode methodNodeCreatePlayer = classNode.methods.get(12);
        for (MethodNode m : classNode.methods)
        {
            if (m.name.equals("a") && m.desc.equals("(Ljava/lang/String;)Ljc;"))
            {
                methodNodeCreatePlayer = m;
                System.out.println("analysing methodNodeCreatePlayer, name: " + methodNodeCreatePlayer.name + ", desc: " + methodNodeCreatePlayer.desc);
                analyse(methodNodeCreatePlayer);
                break;
            }
        }
        int i, c = methodNodeCreatePlayer.instructions.size();
        for(i = c - 1;i >= 0;i--)
        {
            AbstractInsnNode node = methodNodeCreatePlayer.instructions.get(i);
            if (node instanceof TypeInsnNode && ((TypeInsnNode)node).desc.equals("jc"))
            {
                System.out.println("node: " + ((TypeInsnNode)node).desc);
                TypeInsnNode tnCreatePlayer = (TypeInsnNode)node;
                tnCreatePlayer.desc = PLAYERCORE_SERVER.replace(".", "/");

                MethodInsnNode mnCreatePlayer = (MethodInsnNode)methodNodeCreatePlayer.instructions.get(i+11);
                mnCreatePlayer.owner = PLAYERCORE_SERVER.replace(".", "/");
                break;
            }
        }

        MethodNode methodNodeRespawnPlayer = classNode.methods.get(13);
        boolean isMCPC = true;
        try
        {
            Class.forName("org.bukkit.Bukkit");
        } catch (ClassNotFoundException e)
        {
            isMCPC = false;
            return bytes;
        }
        for (MethodNode m : classNode.methods)
        {
            if (m.name.equals("a") && m.desc.equals("(Ljc;IZ)Ljc;"))
            {
                methodNodeRespawnPlayer = m;
                System.out.println("analysing methodNodeRespawnPlayer, name: " + methodNodeRespawnPlayer.name + ", desc: " + methodNodeRespawnPlayer.desc);
                analyse(methodNodeRespawnPlayer);
            }
        }
        c = methodNodeRespawnPlayer.instructions.size();
        for(i = c - 1;i >= 0;i--)
        {
            AbstractInsnNode node = methodNodeRespawnPlayer.instructions.get(i);
            if (node instanceof TypeInsnNode && ((TypeInsnNode)node).desc.equals("jc"))
            {
                TypeInsnNode tnRespawnPlayer = (TypeInsnNode)node;
                tnRespawnPlayer.desc = PLAYERCORE_SERVER.replace(".", "/");

                MethodInsnNode mnRespawnPlayer = (MethodInsnNode)methodNodeRespawnPlayer.instructions.get(i + 13);
                mnRespawnPlayer.owner = PLAYERCORE_SERVER.replace(".", "/");
                break;
            }
        }

        ClassWriter cw = new ClassWriter(0); classNode.accept(cw);

        return cw.toByteArray();
    }
    public void analyse(MethodNode methodNode)
    {
        Iterator iter = methodNode.instructions.iterator(); int i = 0;

        while (iter.hasNext())
        {
            AbstractInsnNode node = (AbstractInsnNode)iter.next();

            if (node instanceof TypeInsnNode)
            {
                TypeInsnNode tn = (TypeInsnNode)node;
                System.out.println(i + ": TypeInsnNode, desc: " + tn.desc);
                continue;
            }

            if (node instanceof MethodInsnNode)
            {
                MethodInsnNode mn = (MethodInsnNode)node;
                System.out.println(i + ": MethodInsnNode, owner: " + mn.owner);
                continue;
            }

            // System.out.println(i + ": Node : " + node.toString());

            i++;
        }
    }
}
