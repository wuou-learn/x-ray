package io.github.x.ray.agent.transformer.handle.app;

import com.alibaba.arthas.deps.org.slf4j.Logger;
import com.alibaba.arthas.deps.org.slf4j.LoggerFactory;
import com.alibaba.bytekit.asm.MethodProcessor;
import com.alibaba.bytekit.asm.interceptor.InterceptorProcessor;
import com.alibaba.bytekit.asm.interceptor.parser.DefaultInterceptorClassParser;
import com.alibaba.bytekit.asm.meta.ClassMeta;
import com.alibaba.bytekit.utils.AsmUtils;
import com.alibaba.deps.org.objectweb.asm.tree.ClassNode;
import com.alibaba.deps.org.objectweb.asm.tree.MethodNode;
import io.github.x.ray.agent.context.XrayContext;
import io.github.x.ray.agent.dict.SystemConfigDict;
import io.github.x.ray.agent.transformer.handle.XrayHandle;
import io.github.x.ray.agent.transformer.instrument.AppInstrument;
import io.github.x.ray.agent.util.ClassUtils;

import java.util.List;
import java.util.Properties;

/**
 * AppHandle
 *
 * @author wuou
 */
public class AppHandle implements XrayHandle {
    private static Logger log = LoggerFactory.getLogger(AppHandle.class);

    private List<InterceptorProcessor> processors;

    public AppHandle() {
        DefaultInterceptorClassParser interceptorClassParser = new DefaultInterceptorClassParser();
        processors = interceptorClassParser.parse(AppInstrument.class);
    }

    @Override
    public boolean filterClassName(String className) {
        if (className == null) {
            return Boolean.TRUE;
        }
        Properties initConfig = XrayContext.getInitConfig();
        String include = initConfig.getProperty(SystemConfigDict.X_RAY_INCLUDES);

        String[] includes = include.split(",");
        boolean filter = Boolean.TRUE;
        for (String packageName : includes) {
            if (className.contains(packageName)) {
                filter = Boolean.FALSE;
            }
        }

        String exclude = initConfig.getProperty(SystemConfigDict.X_RAY_EXCLUDES);
        String[] excludes = exclude.split(",");
        for (String packageName : excludes) {
            if (className.contains(packageName)) {
                filter = Boolean.TRUE;
            }
        }
        return filter;
    }

    @Override
    public byte[] process(String className, byte[] classfileBuffer, ClassLoader loader) {
        try {
            ClassNode classNode = AsmUtils.toClassNode(classfileBuffer);
            ClassMeta classMeta = new ClassMeta(classfileBuffer, loader);
            if (classMeta.isInterface()) {
                return new byte[0];
            }
            for (MethodNode methodNode : classNode.methods) {
                if ("<init>".equalsIgnoreCase(methodNode.name) || "<clinit>".equalsIgnoreCase(methodNode.name) || ClassUtils.isAbstract(methodNode.access)) {
                    continue;
                }
                MethodProcessor methodProcessor = new MethodProcessor(classNode, methodNode);
                for (InterceptorProcessor interceptor : processors) {
                    interceptor.process(methodProcessor);
                }
            }
            return AsmUtils.toBytes(classNode);
        } catch (Exception e) {
            log.debug("x-ray app handle failed.class:{}, msg:{} stack:{}",className, e.getMessage(), e.getStackTrace());
        }
        return classfileBuffer;
    }

    @Override
    public int priority() {
        return 1;
    }

}
