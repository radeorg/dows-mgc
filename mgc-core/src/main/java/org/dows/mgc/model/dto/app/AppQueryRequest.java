package org.dows.mgc.model.dto.app;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dows.mgc.common.PageRequest;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class AppQueryRequest extends PageRequest implements Serializable {

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
     * 应用封面
     */
    private String cover;
    /**
     * 应用初始化的 prompt
     */
    private String initPrompt;
    /**
     * 代码生成类型（枚举）
     */
    private String codeGenType;
    /**
     * 部署标识
     */
    private String deployKey;
    /**
     * 优先级
     */
    private Integer priority;
    /**
     * 创建用户id
     */
    private Long userId;
    /**
     * 是否为VIP专属应用（null-不限制，true-是，false-否）
     */
    private Boolean isVipOnly;
}
