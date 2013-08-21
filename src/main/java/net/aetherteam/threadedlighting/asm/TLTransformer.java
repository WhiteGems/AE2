package net.aetherteam.threadedlighting.asm;

import java.io.IOException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.logging.Logger;
import net.aetherteam.threadedlighting.TLLoadingPlugin;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;

public class TLTransformer implements IClassTransformer
{
    private static final String MCP_NETCLIENTHANDLER = "net.minecraft.client.multiplayer.NetClientHandler";
    private static final String OBF_NETCLIENTHANDLER = "bct";
    private static final String MCP_CHUNKPROVIDERCLIENT = "net.minecraft.client.multiplayer.ChunkProviderClient";
    private static final String OBF_CHUNKPROVIDERCLIENT = "bcy";
    private static final String MCP_CHUNK = "net.minecraft.world.chunk.Chunk";
    private static final String OBF_CHUNK = "adq";
    private static final String MCP_WORLDCLIENT = "net.minecraft.client.multiplayer.WorldClient";
    private static final String OBF_WORLDCLIENT = "bda";
    private static final String MCP_WORLD = "net.minecraft.world.World";
    private static final String OBF_WORLD = "abv";
    private static final String THREADED_WORLD = "net.aetherteam.threadedlighting.world.ThreadedWorld";
    private static final String THREADED_CHUNK = "net.aetherteam.threadedlighting.world.ThreadedChunk";
    private static boolean checkedObfuscation;
    private static boolean isObfuscated;

    public byte[] transform(String name, String transformedName, byte[] bytes)
    {
        if (!checkedObfuscation)
        {
            LaunchClassLoader classReader = (LaunchClassLoader)TLLoadingPlugin.class.getClassLoader();

            try
            {
                isObfuscated = classReader.getClassBytes("net.minecraft.world.World") == null;
            }
            catch (IOException var11)
            {
                ;
            }

            checkedObfuscation = true;
        }

        ClassNode classNode;
        ClassWriter mn;
        ClassReader classReader1;

        if (!name.equals("net.minecraft.client.multiplayer.NetClientHandler") && !name.equals("bct"))
        {
            if (!name.equals("net.minecraft.client.multiplayer.ChunkProviderClient") && !name.equals("bcy"))
            {
                if (!name.equals("net.minecraft.world.chunk.Chunk") && !name.equals("adq"))
                {
                    if (name.equals("net.aetherteam.threadedlighting.world.ThreadedChunk"))
                    {
                        classReader1 = new ClassReader(bytes);
                        classNode = new ClassNode();
                        classReader1.accept(classNode, 0);
                        MethodInsnNode mn2 = (MethodInsnNode)((MethodNode)classNode.methods.get(2)).instructions.get(6);
                        mn2.owner = classNode.superName.replace(".", "/");
                        mn2.setOpcode(183);
                        ClassWriter cw2 = new ClassWriter(3);
                        classNode.accept(cw2);
                        return cw2.toByteArray();
                    }
                    else
                    {
                        return bytes;
                    }
                }
                else
                {
                    classReader1 = new ClassReader(bytes);
                    classNode = new ClassNode();
                    classReader1.accept(classNode, 0);
                    MethodNode mn1 = this.findMethod(classNode, isObfuscated ? "h" : "relightBlock", "(III)V");
                    mn1.access = 1;
                    MethodNode cw = this.findMethod(classNode, isObfuscated ? "a" : "setBlockIDWithMetadata", "(IIIII)Z");
                    cw.access = 1;
                    ListIterator iter = cw.instructions.iterator();

                    while (iter.hasNext())
                    {
                        AbstractInsnNode cw1 = (AbstractInsnNode)iter.next();

                        if (cw1 instanceof MethodInsnNode)
                        {
                            MethodInsnNode methodInsnNode = (MethodInsnNode)cw1;

                            if (methodInsnNode.name.equals(isObfuscated ? "h" : "relightBlock") && methodInsnNode.desc.equals("(III)V"))
                            {
                                methodInsnNode.setOpcode(182);
                            }
                        }
                    }

                    ClassWriter cw3 = new ClassWriter(3);
                    classNode.accept(cw3);
                    return cw3.toByteArray();
                }
            }
            else
            {
                classReader1 = new ClassReader(bytes);
                classNode = new ClassNode();
                classReader1.accept(classNode, 0);
                this.replaceInstance(classNode, isObfuscated ? "adq" : "net.minecraft.world.chunk.Chunk", "net.aetherteam.threadedlighting.world.ThreadedChunk");
                mn = new ClassWriter(0);
                classNode.accept(mn);
                return mn.toByteArray();
            }
        }
        else
        {
            classReader1 = new ClassReader(bytes);
            classNode = new ClassNode();
            classReader1.accept(classNode, 0);
            this.replaceInstance(classNode, isObfuscated ? "bda" : "net.minecraft.client.multiplayer.WorldClient", "net.aetherteam.threadedlighting.world.ThreadedWorld");
            mn = new ClassWriter(0);
            classNode.accept(mn);
            return mn.toByteArray();
        }
    }

    public MethodNode findMethod(ClassNode classNode, String name, String desc)
    {
        Iterator i$ = classNode.methods.iterator();
        MethodNode methodNode;

        do
        {
            if (!i$.hasNext())
            {
                return null;
            }

            methodNode = (MethodNode)i$.next();
        }
        while (!methodNode.name.equals(name) || !methodNode.desc.equals(desc));

        return methodNode;
    }

    public void replaceInstance(ClassNode classNode, String oldInstance, String newInstance)
    {
        Logger logger = Logger.getLogger("TLTransformer");
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

    public void transformSuperclass(ClassNode classNode, String oldSuperclass, String superclass)
    {
        oldSuperclass = oldSuperclass.replace(".", "/");
        superclass = superclass.replace(".", "/");
        classNode.superName = superclass;
        Iterator i$ = classNode.methods.iterator();

        while (i$.hasNext())
        {
            MethodNode method = (MethodNode)i$.next();
            ListIterator iter = method.instructions.iterator();

            while (iter.hasNext())
            {
                AbstractInsnNode insnNode = (AbstractInsnNode)iter.next();

                if (insnNode instanceof MethodInsnNode)
                {
                    MethodInsnNode methodInsnNode = (MethodInsnNode)insnNode;

                    if (methodInsnNode.owner.equals(oldSuperclass))
                    {
                        methodInsnNode.owner = superclass;
                    }
                }
            }
        }
    }
}
