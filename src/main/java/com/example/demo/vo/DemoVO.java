package com.example.demo.vo;

import lombok.Data;

/**
 * @author hiems
 * @date 2019/12/29 23:37
 * @description
 */
@Data
public class DemoVO {
    private Integer code;
    private String msg;
    private Object data;

    public static DemoVO success(Object data) {
        DemoVO demoVO = new DemoVO();
        demoVO.setCode(200);
        demoVO.setMsg("success");
        demoVO.setData(data);
        return demoVO;
    }

}
