package io.github.x.ray.agent.transformer.instrument.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import io.github.x.ray.agent.context.XrayContext;
import io.github.x.ray.agent.logger.LogUtil;
import io.github.x.ray.agent.model.CampareInfo;
import io.github.x.ray.agent.model.MethodInfo;
import io.github.x.ray.agent.util.CollectionUtils;
import io.github.x.ray.agent.util.ListUtils;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * app instrument
 *
 * @author wuou
 */
public class AppInstrumentImpl {

    private AppInstrumentImpl() {
    }

    public static ThreadLocal<AtomicInteger> methodIdThreadLocal = new ThreadLocal<>();
    public static ThreadLocal<Deque<CampareInfo>> methodEnterThreadLocal = new ThreadLocal<>();
    public static ThreadLocal<List<CampareInfo>> methodExistThreadLocal = new ThreadLocal<>();

    /**
     * 方法调用入口
     *
     * @param clazz         类信息
     * @param args          方法入参详情
     * @param argsName      方法入参名称
     */
    public static void appMethodAtEnter(Object clazz,
                                        String methodName,
                                        String methodDesc,
                                        Object[] args,
                                        String[] argsName) {
        if (null == clazz || null == XrayContext.X_RAY_KEY || null == XrayContext.X_RAY_KEY.get()) {
            return;
        }
        AtomicInteger methodId = getMethodId();
        Deque<CampareInfo> methodStack = getMethodEnter();

        MethodInfo methodEnter = buildMethodInfo(args, argsName, null, null, null);
        CampareInfo campareInfo = new CampareInfo();
        campareInfo.setEnter(methodEnter);
        campareInfo.setId(methodId.longValue());
        campareInfo.setClazz(clazz.getClass().getName());
        campareInfo.setMethodName(methodName);
        campareInfo.setMethodDesc(methodDesc);

        methodStack.push(campareInfo);
        methodId.getAndIncrement();
    }

    private static AtomicInteger getMethodId() {
        AtomicInteger methodId = methodIdThreadLocal.get();
        if (null == methodId) {
            methodId = new AtomicInteger(1);
            methodIdThreadLocal.set(methodId);
        }
        return methodId;
    }

    private static Deque<CampareInfo> getMethodEnter() {
        Deque<CampareInfo> methodStack = methodEnterThreadLocal.get();
        if (null == methodStack) {
            methodStack = new ArrayDeque<>();
            methodEnterThreadLocal.set(methodStack);
        }
        return methodStack;
    }

    /**
     * 方法调用出口
     *
     * @param clazz         类信息
     * @param methodName    方法名
     * @param methodDesc    方法入参信息
     * @param localVars     局部变量详情
     * @param localVarNames 局部变量名称
     * @param returnObject  方法出参obj
     * @param line          方法出参行数
     */
    public static void appMethodAtExit(Object clazz,
                                       String methodName,
                                       String methodDesc,
                                       Object[] localVars,
                                       String[] localVarNames,
                                       Object returnObject,
                                       Integer line) {
        if (null == clazz || null == XrayContext.X_RAY_KEY || null == XrayContext.X_RAY_KEY.get()) {
            return;
        }
        MethodInfo methodExist = buildMethodInfo(null, null, localVars, localVarNames, line);
        String returnVal;
        try {
            returnVal = null == returnObject ? null : JSON.toJSONString(returnObject);
        } catch (Exception e) {
            returnVal = returnObject.toString();
        }
        methodExist.setReturnVar(returnVal);

        List<CampareInfo> campareInfoList = getMethodExist();
        Deque<CampareInfo> methodStack = getMethodEnter();
        if (!methodStack.isEmpty()) {
            CampareInfo pop = methodStack.pop();
            pop.setExit(methodExist);
            pop.setParentId(methodStack.isEmpty() ? 0 : methodStack.getFirst().getId());
            pop.setClazz(clazz.getClass().getName());
            pop.setMethodName(methodName);
            pop.setMethodDesc(methodDesc);
            campareInfoList.add(pop);
        }

    }

    private static List<CampareInfo> getMethodExist() {
        List<CampareInfo> campareInfoList = methodExistThreadLocal.get();
        if (CollectionUtils.isEmpty(campareInfoList)) {
            campareInfoList = ListUtils.newArrayList();
            methodExistThreadLocal.set(campareInfoList);
        }
        return campareInfoList;
    }

    private static MethodInfo buildMethodInfo(Object[] args,
                                              String[] argsName,
                                              Object[] localVars,
                                              String[] localVarNames,
                                              Integer lineNumber) {
        MethodInfo methodInfo = new MethodInfo();
        if (null != argsName) {
            JSONObject jsonObject = new JSONObject();
            for (int i = 0; i < argsName.length; i++) {
                jsonObject.put(argsName[i], buildMethodObj(args[i]));
            }
            methodInfo.setMethodVars(jsonObject);
        }
        if (null != localVarNames) {
            JSONObject jsonObject = new JSONObject();
            for (int i = 0; i < localVarNames.length; i++) {
                if ("this".equals(localVarNames[i])) {
                    continue;
                }
                jsonObject.put(localVarNames[i], buildMethodObj(localVars[i]));
            }
            methodInfo.setLocalVars(jsonObject);
        }
        methodInfo.setLineNumber(lineNumber);
        return methodInfo;
    }

    public static Object buildMethodObj(Object arg) {
        try {
            JSON.toJSONString(arg);
        } catch (Exception e) {
            LogUtil.debug("Method args set error.{} class:{}", e.getMessage(), arg.getClass().getName());
            return arg.toString();
        }
        return arg;
    }

    public static void appMethodAtException(Exception e,
                                            Object[] args,
                                            String[] argsName,
                                            Object[] localVars,
                                            String[] localVarNames) {
        if (null == XrayContext.X_RAY_KEY || null == XrayContext.X_RAY_KEY.get()) {
            return;
        }
        MethodInfo methodException = buildMethodInfo(args, argsName, localVars, localVarNames, null);
        methodException.setExceptionMsg(e.getMessage());
        methodException.setExceptionStack(e.getStackTrace());

        List<CampareInfo> campareInfoList = getMethodExist();
        Deque<CampareInfo> methodStack = getMethodEnter();
        if (!methodStack.isEmpty()) {
            CampareInfo pop = methodStack.pop();
            pop.setExit(methodException);
            pop.setParentId(methodStack.isEmpty() ? 0 : methodStack.getFirst().getId());
            campareInfoList.add(pop);
        }
        while (!methodStack.isEmpty()) {
            CampareInfo campareInfo = methodStack.pop();
            campareInfo.setParentId(methodStack.isEmpty() ? 0 : methodStack.getFirst().getId());
            campareInfoList.add(campareInfo);
        }
    }

    public static void remove() {
        methodIdThreadLocal.remove();
        methodEnterThreadLocal.remove();
        methodExistThreadLocal.remove();
    }
}
