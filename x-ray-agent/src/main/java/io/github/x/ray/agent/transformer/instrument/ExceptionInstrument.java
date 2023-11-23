package io.github.x.ray.agent.transformer.instrument;

import com.alibaba.arthas.deps.org.slf4j.Logger;
import com.alibaba.arthas.deps.org.slf4j.LoggerFactory;
import com.alibaba.bytekit.asm.binding.Binding;
import com.alibaba.bytekit.asm.interceptor.annotation.ExceptionHandler;

/**
 * ExceptionInstrument
 *
 * @author wuou
 */
public class ExceptionInstrument {

    private static final Logger log = LoggerFactory.getLogger(ExceptionInstrument.class);

    @ExceptionHandler(inline = true)
    public static void onSuppress(@Binding.Throwable Throwable e) {
        log.error("x-ray instrument exception handle:{}",e.getMessage());
    }
}
