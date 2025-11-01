package org.dows.mgc;

import lombok.extern.slf4j.Slf4j;
import org.dows.mgc.git.GitTools;
import org.eclipse.jgit.lib.Repository;
import org.junit.jupiter.api.Test;

import java.io.IOException;

@Slf4j
public class GitTest {


    @Test
    public void testGit() throws IOException {
        String username = "lait";
        String password = "lait";
        String authorName = "";
        String authorEmail = "lait.zhang@gmail.com";
        String dirPath = "c:/repoTest/";
        String clonePath = "c:/JGitTest0";
        String httpsUrl = "https://github.com/readeorg/test.git";
        httpsUrl = "https://gitee.com/radeorg/test.git";
        String sshUrl = "git@github.com:radeorg/test.git";
        //新建仓库
        /*Repository newRepo = createGitRepository(dirPath);
        log.error("新建或打开已有的仓库，分支名为：" + newRepo.getBranch());*/
        //删除仓库
        /*deleteGitRepository(dirPath);*/
        //仓库状态
        /*Status repoStatus = getGitStatus(dirPath);
        log.error("添加的文件：" + repoStatus.getAdded());
        log.error("修改的文件：" + repoStatus.getModified());
        log.error("改变的文件：" + repoStatus.getChanged());
        log.error("忽略的文件：" + repoStatus.getIgnoredNotInIndex());
        log.error("删除的文件：" + repoStatus.getRemoved());
        log.error("未提交文件：" + repoStatus.getUncommittedChanges());*/
        //克隆
        /*if (cloneGit(httpsUrl, clonePath, username, password)) {
            log.error("clone完成\n目录：" + clonePath);
        } else {
            log.error("clone失败");
        }*/
        //检出
        /*if (checkoutGit(clonePath)) {
            log.error("检出成功\n目录：" + clonePath);
        } else {
            log.error("检出失败");
        }*/
        //创建分支
        if (GitTools.createBranch("dev", clonePath)) {
            log.error("创建新分支成功\n分支名：dev");
        } else {
            log.error("创建新分支失败");
        }
        //打开仓库
        Repository repo = GitTools.openGitRepository(dirPath);
        log.error("新打开仓库分支：" + repo.getBranch());

        //提交
        /*if (commitAndPush(new File(clonePath), username, password, "jgit提交测试")) {
            log.error("提交成功");
        } else {
            log.error("提交失败");
        }*/

        String gitPath = "https://gitee.com/helixin/aicode_template.git";
        System.out.println(gitPath.substring(gitPath.lastIndexOf("/") + 1).replace(".git", ""));
    }
}
