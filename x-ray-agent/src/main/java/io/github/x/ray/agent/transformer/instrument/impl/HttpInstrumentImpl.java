package io.github.x.ray.agent.transformer.instrument.impl;

import com.alibaba.arthas.deps.org.slf4j.MDC;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import io.github.x.ray.agent.context.XrayContext;
import io.github.x.ray.agent.logger.LogUtil;
import io.github.x.ray.agent.model.CampareInfo;
import io.github.x.ray.agent.util.CollectionUtils;
import io.github.x.ray.agent.util.TreeUtil;

import java.util.Deque;
import java.util.List;
import java.util.Objects;

/**
 * app instrument
 *
 * @author wuou
 */
public class HttpInstrumentImpl {
    private HttpInstrumentImpl() {
    }

    /**
     * 方法调用出口
     *
     * @param returnObj  出参详请
     */
    public static void httpMethodAtExist(Object returnObj) {
        if (null == returnObj) {
            return;
        }
        if (returnObj.getClass().getName().contains("EmptyBodyCheckingHttpInputMessage")) {
            handleRequest(returnObj);
        } else {
            handleResponse(returnObj);
        }
    }

    public static void handleRequest(Object returnObj) {
        JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(returnObj));


        JSONObject headers =(JSONObject) jsonObject.get("headers");
        if (Objects.isNull(headers)) {
            return;
        }
        JSONArray key =(JSONArray) headers.get("x-ray-key");
        if (Objects.isNull(key) || key.isEmpty()) {
            return;
        }
        XrayContext.X_RAY_KEY.set((String) key.get(0));
    }

    public static void handleResponse(Object returnObj) {
        JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(returnObj));
        Object o = jsonObject.get("requestId");

        Deque<CampareInfo> methodStack = AppInstrumentImpl.methodEnterThreadLocal.get();
        List<CampareInfo> campareInfoList = AppInstrumentImpl.methodExistThreadLocal.get();
        if (methodStack != null && methodStack.isEmpty() && CollectionUtils.isNotEmpty(campareInfoList)) {
            MDC.put("requestId", Objects.nonNull(o) ? o.toString() : null);
            MDC.put("xrayKey", XrayContext.X_RAY_KEY.get());
            LogUtil.info("Method info:{}", JSON.toJSONString(TreeUtil.buildTree(campareInfoList)));
            AppInstrumentImpl.remove();
        }
    }
}
