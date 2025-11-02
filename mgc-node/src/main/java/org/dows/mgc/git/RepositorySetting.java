package org.dows.mgc.git;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(title = "仓库请求")
@Data
public class RepositorySetting {
    @Schema(title = "平台[gitlab|github|...]")
    private String channel;
    @Schema(title = "组织ID")
    private Long organizationId;
    private Long groupId;
    @Schema(title = "仓库ID")
    private Long repositoryId;
    @Schema(title = "仓库名称")
    private String repositoryName;
    @Schema(title = "仓库所有者")
    private String owner;

    @Schema(title = "描述")
    private String description;
    @Schema(title = "是否公开")
    private String visibility;
    @Schema(title = "是否自动初始化")
    private boolean autoInit;
    @Schema(title = "是否组织仓库")
    private boolean organization;

    // 工作目录
    @JsonIgnore
    private String workDir;
    @JsonIgnore
    //分支 需用bulidRequest 设置进来
    private String branch;
}
