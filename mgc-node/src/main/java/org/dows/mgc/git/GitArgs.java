package org.dows.mgc.git;

import lombok.Data;

/**
 * @description: </br>
 * @author: lait.zhang@gmail.com
 * @date: 5/8/2024 10:22 AM
 * @history: </br>
 * <author>      <time>      <version>    <desc>
 * 修改人姓名      修改时间        版本号       描述
 */
@Data
public class GitArgs {
    private String project;
    // 文件夹
    private String folder;
    // 仓库
    private String repository;
    // 分支
    private String branch;
    // 提交信息
    private String commit;

}

