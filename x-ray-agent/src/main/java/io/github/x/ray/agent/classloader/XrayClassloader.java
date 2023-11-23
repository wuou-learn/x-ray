package io.github.x.ray.agent.classloader;

/**
 * XrayClassloader
 *
 * @author 吴欧(欧弟)
 */
public class XrayClassloader extends ClassLoader{

    private static XrayClassloader cl;

    public static void init(ClassLoader parent) {
        cl = new XrayClassloader(parent);
    }

    private XrayClassloader(ClassLoader parent) {
        super(parent);
    }

    public static XrayClassloader getClassloader() {
        return cl;
    }
}
