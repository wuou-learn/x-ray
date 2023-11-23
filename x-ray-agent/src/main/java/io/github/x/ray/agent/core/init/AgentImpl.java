package io.github.x.ray.agent.core.init;

import io.github.x.ray.agent.context.XrayContext;
import io.github.x.ray.agent.dict.SystemConfigDict;
import io.github.x.ray.agent.logger.LogUtil;
import io.github.x.ray.agent.transformer.AsmTransformer;
import io.github.x.ray.agent.transformer.handle.app.AppHandle;
import io.github.x.ray.agent.transformer.handle.http.HttpHandle;
import io.github.x.ray.agent.util.ConfigUtils;
import io.github.x.ray.agent.util.ListUtils;

import java.io.File;
import java.lang.instrument.Instrumentation;
import java.security.CodeSource;
import java.util.Properties;

/**
 * AgentImpl
 *
 * @author wuou
 */
public class AgentImpl implements Agent{
    /**
     * init agent
     *
     * @param args
     * @param inst
     */
    @Override
    public void init(String args, Instrumentation inst, boolean premain) {
        Properties properties = ConfigUtils.parseConfig(args);
        findXrayAgentHomePath(properties);
        XrayContext.setInitConfig(properties);
        AsmTransformer asmTransformer = new AsmTransformer(ListUtils.newArrayList(new AppHandle(), new HttpHandle()));
        inst.addTransformer(asmTransformer, true);
        LogUtil.init();
    }

    private String findXrayAgentHomePath(Properties config) {
        String xRayHome = config.getProperty(SystemConfigDict.X_RAY_HOME);

        if (xRayHome == null) {
            CodeSource codeSource = this.getClass().getProtectionDomain().getCodeSource();
            String agentJarFile = codeSource.getLocation().getFile();

            File agentJarDir = new File(agentJarFile);
            if (agentJarDir.exists()) {
                config.put(SystemConfigDict.X_RAY_HOME, agentJarFile);
            }
        }

        return (String) config.get(SystemConfigDict.X_RAY_HOME);
    }
}
