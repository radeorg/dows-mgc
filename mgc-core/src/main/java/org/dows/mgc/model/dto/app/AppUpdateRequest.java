package org.dows.mgc.model.dto.app;

import lombok.Data;

import java.io.Serializable;

@Data
public class AppUpdateRequest implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    private Long id;
    /**
     * 应用名称
     */
    private String appName;
    /**
     * 优先级
     */
    private Integer priority;
    /**
     * 是否为VIP专属应用（null-不限制，true-是，false-否）
     */
    private Boolean isVipOnly;
}
