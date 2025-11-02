package org.dows.mgc.git;

import cn.hutool.core.bean.BeanUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @description: </br>
 * @author: lait.zhang@gmail.com
 * @date: 5/6/2024 7:00 PM
 * @history: </br>
 * <author>      <time>      <version>    <desc>
 * 修改人姓名      修改时间        版本号       描述
 */
@Slf4j
public enum GitAction {
    init(List.of("git init --initial-branch=main", "git remote add origin ${repository}")),
    pull(List.of("git pull")),
    clone(List.of("git clone ${repository} ${project}")),
    branch(List.of("git checkout -b ${branch}")),
    merge(List.of("git checkout main", "git merge ${branch}", "git checkout ${branch}")),
    checkout(List.of("git checkout ${branch}")),
    push(List.of("git add .", "git commit -m '${commit}'", "git push -uf origin ${branch}"));


    @Getter
    private List<String> commands;

    GitAction(List<String> commands) {
        this.commands = commands;
    }

    public void exec(GitArgs gitArgs) {
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(gitArgs);
        // 先替换
        List<String> commands = new ArrayList<>();
        for (String command : this.commands) {
            // 替换参数
            String commandAction = replaceVar(stringObjectMap, command);
            commands.add(commandAction);
        }

        try {
            Process process = null;
            File file;
            for (String command : commands) {
                if (command.contains("git clone")) {
                    file = new File(gitArgs.getFolder());
                } else {
                    file = new File(gitArgs.getProject());
                }
                process = Runtime.getRuntime().exec(command, null, file);
                /*try {
                    process.waitFor();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }*/
                String line;
                while ((line = process.inputReader().readLine()) != null) {
                    log.info("当前文件夹:{},执行git命令:{},指令返回{}", file.getAbsolutePath(), command, line);
                }
            }

        } catch (IOException e) {
            throw new RuntimeException("git命令执行失败", e);
        }
    }

    public static String replaceVar(Map<String, Object> vars, String template) {
        if (!StringUtils.hasLength(template)) {
            log.info(String.format("调用%s方法失败,模板字符串替换失败,模板字符串不能为空",
                    Thread.currentThread().getStackTrace()[1].getMethodName()));
            return null;
        }
        if (CollectionUtils.isEmpty(vars)) {
            log.info(String.format("调用%s方法失败,模板字符串替换失败,map不能为空",
                    Thread.currentThread().getStackTrace()[1].getMethodName()));
            return null;
        }
        StringSubstitutor stringSubstitutor = new StringSubstitutor(vars);
        return stringSubstitutor.replace(template);
    }

}

