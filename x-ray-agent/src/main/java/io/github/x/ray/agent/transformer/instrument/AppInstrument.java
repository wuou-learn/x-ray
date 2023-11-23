package io.github.x.ray.agent.transformer.instrument;

import com.alibaba.bytekit.asm.binding.Binding;
import com.alibaba.bytekit.asm.interceptor.annotation.AtEnter;
import com.alibaba.bytekit.asm.interceptor.annotation.AtExceptionExit;
import com.alibaba.bytekit.asm.interceptor.annotation.AtExit;
import io.github.x.ray.agent.transformer.instrument.impl.AppInstrumentImpl;

/**
 * AppInstrument
 *
 * @author wuou
 */
public class AppInstrument {

    @AtEnter(inline = true, suppress = Exception.class, suppressHandler = ExceptionInstrument.class)
    public static void atEnter(@Binding.This Object clazz,
                               @Binding.MethodName String methodName,
                               @Binding.MethodDesc String methodDesc,
                               @Binding.Args Object[] args,
                               @Binding.ArgNames String[] argsName) {
        AppInstrumentImpl.appMethodAtEnter(clazz, methodName, methodDesc, args, argsName);
    }

    @AtExit(inline = true)
    public static void atExit(@Binding.This Object clazz,
                              @Binding.MethodName String methodName,
                              @Binding.MethodDesc String methodDesc,
                              @Binding.LocalVars Object[] localVars,
                              @Binding.LocalVarNames String[] localVarNames,
                              @Binding.Return Object returnObject,
                              @Binding.Line Integer line) {
        AppInstrumentImpl.appMethodAtExit(clazz, methodName, methodDesc, localVars, localVarNames, returnObject, line);
    }

    @AtExceptionExit(inline = true, onException = Exception.class)
    public static void atExceptionExit(@Binding.Throwable Exception e,
                                       @Binding.Args Object[] args,
                                       @Binding.ArgNames String[] argsName,
                                       @Binding.LocalVars Object[] localVars,
                                       @Binding.LocalVarNames String[] localVarNames) {
        AppInstrumentImpl.appMethodAtException(e, args, argsName, localVars, localVarNames);
    }
}
