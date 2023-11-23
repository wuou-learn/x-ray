package io.github.x.ray.agent.transformer.handle.http;


import com.alibaba.arthas.deps.org.slf4j.Logger;
import com.alibaba.arthas.deps.org.slf4j.LoggerFactory;
import com.alibaba.bytekit.asm.MethodProcessor;
import com.alibaba.bytekit.asm.interceptor.InterceptorProcessor;
import com.alibaba.bytekit.asm.interceptor.parser.DefaultInterceptorClassParser;
import com.alibaba.bytekit.asm.meta.ClassMeta;
import com.alibaba.bytekit.utils.AsmUtils;
import com.alibaba.deps.org.objectweb.asm.tree.ClassNode;
import com.alibaba.deps.org.objectweb.asm.tree.MethodNode;
import io.github.x.ray.agent.transformer.handle.XrayHandle;
import io.github.x.ray.agent.transformer.instrument.HttpInstrument;

import java.util.List;


/**
 * HttpHandle
 *
 * @author wuou
 */
public class HttpHandle implements XrayHandle {
    private static Logger log = LoggerFactory.getLogger(HttpHandle.class);

    private List<InterceptorProcessor> processors;
    public HttpHandle() {
        DefaultInterceptorClassParser interceptorClassParser = new DefaultInterceptorClassParser();
        processors = interceptorClassParser.parse(HttpInstrument.class);
    }

    @Override
    public boolean filterClassName(String className) {
        if (className.equals("org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyAdviceChain")) {
            return false;
        }
        return true;
    }

    @Override
    public byte[] process(String className, byte[] classfileBuffer, ClassLoader loader) {
        try {
            ClassNode classNode = AsmUtils.toClassNode(classfileBuffer);
            ClassMeta classMeta = new ClassMeta(classfileBuffer, loader);
            if (classMeta.isInterface()) {
                return AsmUtils.toBytes(classNode);
            }
            for (MethodNode methodNode : classNode.methods) {
                if (!("beforeBodyRead".equalsIgnoreCase(methodNode.name) || "beforeBodyWrite".equalsIgnoreCase(methodNode.name))) {
                    continue;
                }
                MethodProcessor methodProcessor = new MethodProcessor(classNode, methodNode);
                for (InterceptorProcessor interceptor : processors) {
                    interceptor.process(methodProcessor);
                }
            }
            return AsmUtils.toBytes(classNode);
        } catch (Exception e) {
            log.debug("x-ray http handle failed.class:{}, msg:{} stack:{}",className, e.getMessage(), e.getStackTrace());
        }
        return classfileBuffer;
    }

    @Override
    public int priority() {
        return 0;
    }
}
