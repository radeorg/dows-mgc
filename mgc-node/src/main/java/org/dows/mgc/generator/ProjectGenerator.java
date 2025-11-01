package org.dows.mgc.generator;

import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dows.mgc.entity.MindNode;
import org.dows.mgc.entity.ProjectAttribute;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
@RequiredArgsConstructor
@Component
public class ProjectGenerator implements ObjectGenerator {
    @Override
    public void generate(String appId, MindNode mindNode) {
        log.info("project node: {}", JSONUtil.toJsonStr(mindNode));
        ProjectAttribute projectAttribute = mindNode.getNodeAttribute().toAttribute(ProjectAttribute.class);

        String folderPath = projectAttribute.getFolderPath();
        try {
            Files.createDirectories(Path.of(folderPath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String repository = projectAttribute.getRepository();

        // todo 创建git仓库git init
        try {
            Process process = Runtime.getRuntime().exec("git init");
            process.waitFor();


        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
