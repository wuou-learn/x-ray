package io.github.x.ray.agent.util;

import com.alibaba.deps.org.objectweb.asm.Opcodes;

/**
 * ClassUtils
 *
 * @author 吴欧(欧弟)
 */
public class ClassUtils {
    private ClassUtils() {
    }

    public static boolean isAbstract(int access) {
        return (access & Opcodes.ACC_ABSTRACT) > 0;
    }
}
