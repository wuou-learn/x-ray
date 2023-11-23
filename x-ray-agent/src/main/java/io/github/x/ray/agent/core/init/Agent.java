package io.github.x.ray.agent.core.init;

import java.lang.instrument.Instrumentation;

/**
 * Agent
 *
 * @author wuou
 */
public interface Agent {

    /**
     * init agent
     *
     * @param args
     * @param inst
     */
    void init(final String args, final Instrumentation inst, final boolean premain);
}
