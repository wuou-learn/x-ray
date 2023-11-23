package io.github.x.ray.xrayspringbootstarter.controller;

import io.github.x.ray.xrayspringbootstarter.model.XRayTestModel;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CountDownLatch;

/**
 * 健康检查
 *
 * @author wuou
 */
@RestController
@RequestMapping("/x-ray/health")
public class CheckController {

    public static final String STR = "DRFTGYUHJI_ERT";

    private static XRayTestModel STATIC_DTO;

    private static ThreadLocal<XRayTestModel> THREAD_LOCAL = new ThreadLocal<>();

    private static CountDownLatch COUNT_DOWN_LATCH = new CountDownLatch(10);


    {
        COUNT_DOWN_LATCH.countDown();
        STATIC_DTO = new XRayTestModel();
        STATIC_DTO.setPoCode("初始化");
        STATIC_DTO.setPoName("初始化NAME");
    }

    @RequestMapping("/check")
    public String check() {
        return "health check.";
    }

    @RequestMapping("/test")
    public XRayTestModel method1(@RequestBody XRayTestModel req) {
        THREAD_LOCAL.set(req);
        int i = 1;
        int j = 2;
        int sum = i+j;
        method2_1(sum);
        method2_2(sum);
        if (req.getPoCode().equals("123")) {
            throw new RuntimeException("TEST EXCEPTION...");
        }
        req.setPoName("测试res");
        req.setRequestId("idgsahasgd");

        // req i j
        return req;
    }

    public String method2_1(int sum){
        method3_1(sum+1);
        return "sum="+sum+1;
    }

    public String method2_2(int sum){
        return "sum="+sum+2;
    }

    public String method3_1(int sum){
        return "sum="+sum+3;
    }
}
