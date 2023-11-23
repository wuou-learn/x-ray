package io.github.x.ray.agent.model;

import java.util.List;

/**
 * CampareInfo
 *
 * @author wuou
 */
public class CampareInfo {
    /**
     * id
     */
    private Long id;
    /**
     * 类名
     */
    private String clazz;
    /**
     * 方法名称
     */
    private String methodName;
    /**
     * 方法入参详情
     */
    private String methodDesc;
    /**
     * enter
     */
    private MethodInfo enter;
    /**
     * exist
     */
    private MethodInfo exit;
    /**
     * childrenList
     */
    private List<CampareInfo> childrenList;
    /**
     * parentId
     */
    private Long parentId;

    public MethodInfo getEnter() {
        return enter;
    }

    public void setEnter(MethodInfo enter) {
        this.enter = enter;
    }

    public MethodInfo getExit() {
        return exit;
    }

    public void setExit(MethodInfo exit) {
        this.exit = exit;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getMethodDesc() {
        return methodDesc;
    }

    public void setMethodDesc(String methodDesc) {
        this.methodDesc = methodDesc;
    }

    public List<CampareInfo> getChildrenList() {
        return childrenList;
    }

    public void setChildrenList(List<CampareInfo> childrenList) {
        this.childrenList = childrenList;
    }

}
