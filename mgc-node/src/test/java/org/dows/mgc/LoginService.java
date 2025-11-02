package org.dows.mgc;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.dows.mgc.entity.GitMindNode;
import org.dows.mgc.entity.MindNode;
import org.dows.mgc.mind.GitMind;
import org.dows.mgc.mind.MindXpath;
import org.dows.mgc.reader.GitmindReader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Map;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest
//@SpringBootApplication
@ContextConfiguration(classes = { TestConfig.class}) // SpringBootApplication 二选一
public class LoginService {
    @Autowired
    private GitmindReader gitmindReader;

    @Test
    public void test() {

        GitMind gitMind = GitMind.builder().build()
                .addMindXpath(MindXpath.builder()
                        .apiXpath(List.of("/app/dd/admin"))
                        .databaseXpath(List.of("/app/dd/dows_app"))
                        .mindFileName("project/dows-eaglee/鹰眼")
                        .build());
                /*.addMindXpath(MindXpath.builder()
                        .apiXpath(List.of("/app1/cc/admin1"))
                        .databaseXpath(List.of("/app1/cc/dows_app1"))
                        .mindFileName("project/app1/应用")
                        .build());*/

        Map<String, List<MindNode>> gitMindNodes = gitmindReader.getGitMindNode(gitMind);
        //List<MindNode> gitMindNodes = gitmindReader.getGitMindNode(gitMind);


        log.info("{}", JSONUtil.toJsonStr(gitMindNodes));
    }


}