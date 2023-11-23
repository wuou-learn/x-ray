package io.github.x.ray.agent.context;

import java.util.Properties;

/**
 * XrayContext
 *
 * @author wuou
 */
public class XrayContext {

    /**
     * agent init config
     */
    public static Properties INIT_CONFIG;

    public static ThreadLocal<String> X_RAY_KEY = new ThreadLocal<String>();

    public static Properties getInitConfig() {
        return INIT_CONFIG;
    }

    public static void setInitConfig(Properties initConfig) {
        INIT_CONFIG = initConfig;
    }
}
