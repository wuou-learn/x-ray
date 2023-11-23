package io.github.x.ray.agent.transformer.instrument;

import com.alibaba.bytekit.asm.binding.Binding;
import com.alibaba.bytekit.asm.interceptor.annotation.AtExit;
import io.github.x.ray.agent.transformer.instrument.impl.HttpInstrumentImpl;

/**
 * HttpInstrument
 *
 * @author 吴欧(欧弟)
 */
public class HttpInstrument {

    @AtExit(inline = true, suppress = Exception.class, suppressHandler = ExceptionInstrument.class)
    public static void atExit(@Binding.Return Object returnObject) {
        HttpInstrumentImpl.httpMethodAtExist(returnObject);
    }
}
