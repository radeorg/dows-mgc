package org.dows.mgc.git;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;


public interface RepositoryService {
    RepositoryResponse createRepository(RepositorySetting repositorySetting) throws IOException;

    // 删除仓库
    RepositoryResponse deleteRepository(String repoName) throws IOException;

    // 获取仓库信息列表
    List<RepositoryResponse> getRepositories(RepositorySetting repositorySetting) throws IOException;

    List<RepositoryResponse> getPageRepositories(Long currentPage, Long pageSize) throws IOException;

    // 获取仓库信息
    RepositoryResponse getRepository(RepositorySetting repositorySetting) throws IOException;


    default RepositoryService gitAction(GitAction gitAction, GitArgs gitArgs) {
        gitAction.exec(gitArgs);
        return this;
    }

    default void initDeploy(String projectFolder, RepositoryResponse repository, DeploySetting deploySetting) {
        // todo copy gitlab-ci.yml and replace placeholder
        String folder = deploySetting.getWorkDir();
        ClassPathResource classPathResource = new ClassPathResource("cicd/.gitlab-ci.yml");
        ClassPathResource classPathResource2 = new ClassPathResource("cicd/sonar-project.properties");
        ClassPathResource classPathResource3 = new ClassPathResource("cicd");
        try {
            Path path = Path.of(folder + File.separator + projectFolder + File.separator + ".gitlab-ci.yml");
            Path path2 = Path.of(folder + File.separator + projectFolder + File.separator + "sonar-project.properties");
            Path path3 = Path.of(folder + File.separator + projectFolder + File.separator + "cicd/dev/.env");
            Files.copy(classPathResource.getStream(), path, StandardCopyOption.REPLACE_EXISTING);
            Files.copy(classPathResource2.getStream(), path2, StandardCopyOption.REPLACE_EXISTING);
            FileUtil.copyFolderRecursively(Path.of(classPathResource3.getAbsolutePath()),
                    Path.of(folder + File.separator + projectFolder + File.separator + "cicd"));

            CicdScriptParam cicdScriptParam = deploySetting.getCicdScriptParam();
            if(cicdScriptParam != null) {
                Map<String, Object> stringObjectMap = BeanUtil.beanToMap(cicdScriptParam);
                // 读取文件gitlab-ci内容
                String content = Files.readString(path);
                String newContent = replaceVar(stringObjectMap, content);

                assert newContent != null;
                Files.writeString(path, newContent);

                // 读取文件sonar-project.properties内容
                String content2 = Files.readString(path2);
                String newContent2 = replaceVar(stringObjectMap, content2);

                assert newContent2 != null;
                Files.writeString(path2, newContent2);

                // 读取文件.env内容
                String content3 = Files.readString(path3);
                String newContent3 = replaceVar(stringObjectMap, content3);

                assert newContent3 != null;
                Files.writeString(path3, newContent3);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    default void gitPush(String projectFolder, RepositoryResponse repository, RepositorySetting repositorySetting) {
        String folder = repositorySetting.getWorkDir();
        GitArgs gitArgs = new GitArgs();
        gitArgs.setRepository(repository.getGitUrl());
        gitArgs.setBranch(repositorySetting.getBranch());
        gitArgs.setFolder(folder);
        gitArgs.setCommit("init");
        gitArgs.setProject(folder + File.separator + projectFolder);
        gitAction(GitAction.init, gitArgs)
                .gitAction(GitAction.branch, gitArgs);
        if (repositorySetting.isAutoInit()) {
            //todo copy gitignore
            ClassPathResource classPathResource = new ClassPathResource("ignore/java.gitignore");
            try {
                // target ignore
                Path path = Path.of(folder + File.separator + projectFolder + File.separator + ".gitignore");
                Files.copy(classPathResource.getStream(), path, StandardCopyOption.REPLACE_EXISTING);
            } catch (Exception e) {
                throw new RuntimeException("file copy error");
            }

            //todo gen license files
        }
        this.gitAction(GitAction.push, gitArgs);
    }

}
