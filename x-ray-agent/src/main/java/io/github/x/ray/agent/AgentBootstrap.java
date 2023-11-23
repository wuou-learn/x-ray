package io.github.x.ray.agent;

import io.github.x.ray.agent.classloader.XrayClassloader;
import io.github.x.ray.agent.logger.LogUtil;
import io.github.x.ray.agent.util.ReflectUtils;

import java.lang.instrument.Instrumentation;
import java.lang.reflect.Method;

/**
 * agent bootstrap
 *
 * @author wuou
 */
public class AgentBootstrap {

    public static void premain(String args, Instrumentation inst) {
        main(args, inst, false);
    }

    public static void agentmain(String args, Instrumentation inst) {
        main(args, inst, true);
    }

    public static void main(String args, Instrumentation inst, boolean premain) {
        XrayClassloader.init(AgentBootstrap.class.getClassLoader());
        XrayClassloader cl = XrayClassloader.getClassloader();

        try {
            Class<?> agent = cl.loadClass("io.github.x.ray.agent.core.init.AgentImpl");
            Method init = ReflectUtils.getMethodByName(agent, "init");
            init.invoke(agent.newInstance(), args, inst, premain);
        } catch (Exception e) {
        }

        LogUtil.info("agent start end.");
    }

}
