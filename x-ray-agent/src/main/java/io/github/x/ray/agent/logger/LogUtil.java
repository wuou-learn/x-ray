package io.github.x.ray.agent.logger;

import com.alibaba.arthas.deps.ch.qos.logback.classic.LoggerContext;
import com.alibaba.arthas.deps.ch.qos.logback.classic.joran.JoranConfigurator;
import com.alibaba.arthas.deps.ch.qos.logback.classic.spi.ILoggingEvent;
import com.alibaba.arthas.deps.ch.qos.logback.core.Appender;
import com.alibaba.arthas.deps.ch.qos.logback.core.rolling.RollingFileAppender;
import com.alibaba.arthas.deps.org.slf4j.LoggerFactory;
import io.github.x.ray.agent.context.XrayContext;
import io.github.x.ray.agent.dict.SystemConfigDict;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 
 */
public class LogUtil {

    private static LoggerContext context;
    private static String logFile = "";

    public static LoggerContext init() {
        try {
            JarFile jarFile = new JarFile(XrayContext.getInitConfig().getProperty(SystemConfigDict.X_RAY_HOME));
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (entry.getName().equalsIgnoreCase("xray-logback.xml")) {
                    File tempFile = File.createTempFile("temp", ".xml");
                    tempFile.deleteOnExit();

                    try (InputStream inputStream = jarFile.getInputStream(entry);
                         FileOutputStream outputStream = new FileOutputStream(tempFile)) {
                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, bytesRead);
                        }
                    }

                    LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
                    loggerContext.reset();
                    JoranConfigurator configurator = new JoranConfigurator();
                    configurator.setContext(loggerContext);
                    configurator.doConfigure(tempFile.toURI().toURL());
                    // 查找 arthas.log appender
                    Iterator<Appender<ILoggingEvent>> appenders = loggerContext.getLogger("root").iteratorForAppenders();

                    while (appenders.hasNext()) {
                        Appender<ILoggingEvent> appender = appenders.next();
                        if (appender instanceof RollingFileAppender) {
                            RollingFileAppender fileAppender = (RollingFileAppender) appender;
                            if ("X_RAY".equalsIgnoreCase(fileAppender.getName())) {
                                logFile = new File(fileAppender.getFile()).getCanonicalPath();
                            }
                        }
                    }
                    context = loggerContext;
                    return loggerContext;
                }
            }
        } catch (Exception e) {
        }

        return null;
    }

    public static void info(String msg, Object... objects) {
        context.getLogger("xray").info(msg, objects);
    }

    public static void debug(String msg, Object... objects) {
        context.getLogger("xray").debug(msg, objects);
    }
}
