package io.github.x.ray.xrayspringbootstarter.model;

public class XRayTestModel {
    private static final long serialVersionUID = 340602390492241982L;

    private String requestId;
    private String poCode;

    private String poName;

    private Long poType;

    public String getPoCode() {
        return poCode;
    }

    public void setPoCode(String poCode) {
        this.poCode = poCode;
    }

    public String getPoName() {
        return poName;
    }

    public void setPoName(String poName) {
        this.poName = poName;
    }

    public Long getPoType() {
        return poType;
    }

    public void setPoType(Long poType) {
        this.poType = poType;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
