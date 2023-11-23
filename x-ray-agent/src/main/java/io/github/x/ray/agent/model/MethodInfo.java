package io.github.x.ray.agent.model;

import com.alibaba.fastjson2.JSONObject;

/**
 * 方法详情
 *
 * @author wuou
 */
public class MethodInfo {

    /**
     * 行号
     */
    private Integer lineNumber;
    /**
     * 局部变量
     */
    private JSONObject localVars;
    /**
     * 方法入参数
     */
    private JSONObject methodVars;
    /**
     * 方法返回值
     */
    private String returnVar;
    /**
     * 异常方法栈
     */
    private StackTraceElement[] exceptionStack;
    /**
     * 异常信息
     */
    private String exceptionMsg;

    public Integer getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(Integer lineNumber) {
        this.lineNumber = lineNumber;
    }

    public JSONObject getLocalVars() {
        return localVars;
    }

    public void setLocalVars(JSONObject localVars) {
        this.localVars = localVars;
    }

    public JSONObject getMethodVars() {
        return methodVars;
    }

    public void setMethodVars(JSONObject methodVars) {
        this.methodVars = methodVars;
    }

    public String getReturnVar() {
        return returnVar;
    }

    public void setReturnVar(String returnVar) {
        this.returnVar = returnVar;
    }

    public StackTraceElement[] getExceptionStack() {
        return exceptionStack;
    }

    public void setExceptionStack(StackTraceElement[] exceptionStack) {
        this.exceptionStack = exceptionStack;
    }

    public String getExceptionMsg() {
        return exceptionMsg;
    }

    public void setExceptionMsg(String exceptionMsg) {
        this.exceptionMsg = exceptionMsg;
    }

}
