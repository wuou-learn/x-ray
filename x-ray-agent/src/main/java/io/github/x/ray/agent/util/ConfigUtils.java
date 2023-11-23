package io.github.x.ray.agent.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;
import java.util.Properties;

/**
 *
 * @author ouwu
 */
public class ConfigUtils {

    public static Properties parseConfig(String args) {
        return parseConfigFromArgs(args);
    }

    private static Properties parseConfigFromArgs(String args) {
        args = decodeArg(args);
        Map<String, String> map = FeatureCodec.DEFAULT_COMMANDLINE_CODEC.toMap(args);
        Properties properties = new Properties();
        properties.putAll(map);
        return properties;
    }

    private static String decodeArg(String arg) {
        if (arg == null) {
            return null;
        }
        try {
            return URLDecoder.decode(arg, "utf-8");
        } catch (UnsupportedEncodingException e) {
            return arg;
        }
    }


}
