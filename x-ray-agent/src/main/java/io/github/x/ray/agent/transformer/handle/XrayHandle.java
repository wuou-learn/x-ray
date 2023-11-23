package io.github.x.ray.agent.transformer.handle;

/**
 * XrayHandle
 *
 * @author wuou
 */
public interface XrayHandle {

    /**
     *
     * @param className
     * @return true-filter false-nonFilter
     */
    boolean filterClassName(String className);

    byte[] process(String className, byte[] classfileBuffer, ClassLoader loader);

    int priority();

}
