package org.dows.mgc.builder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dows.mgc.context.AppMindCache;
import org.dows.mgc.context.ProjectContext;
import org.dows.mgc.entity.MindNode;
import org.dows.mgc.entity.NodeType;
import org.dows.mgc.entity.RepositoryAttribute;
import org.dows.mgc.entity.RepositoryType;
import org.dows.mgc.reader.MindReader;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class RepositoryBuilder implements AttributeBuilder {

    private final AppMindCache appMindCache;
    @Override
    public Integer getOrder() {
        return 0;
    }

    @Override
    public void buildAttribute(String appId, MindNode node) {
        log.info("RepositoryBuilder buildAttribute appId: {}, node: {}", appId, node);
        RepositoryAttribute repositoryAttribute = new RepositoryAttribute();
        repositoryAttribute.setDescription(node.getDescription());
        String repository = node.getValue();
        NodeType nodeType = node.getNodeType();

        if (nodeType == NodeType.github) {
            String url = "github.com/" + repository.replaceAll("\\.", "/") + ".git";
            repositoryAttribute.setRepository(url);
            repositoryAttribute.setRepositoryType(RepositoryType.github);
        } else if (nodeType == NodeType.gitee) {
            String url = "gitee.com/" + repository.replaceAll("\\.", "/") + ".git";
            repositoryAttribute.setRepository(url);
            repositoryAttribute.setRepositoryType(RepositoryType.gitee);
        } else if (nodeType == NodeType.gitlab) {
            String url = "gitlab.com/" + repository.replaceAll("\\.", "/") + ".git";
            repositoryAttribute.setRepository(url);
            repositoryAttribute.setRepositoryType(RepositoryType.gitlab);
        }


        List<String> children = node.getChildren();
        node.setNodeAttribute(repositoryAttribute);

        ProjectContext projectContext = ProjectContext.getProjectContext(appId);
        projectContext.setRepositoryAttribute(repositoryAttribute);
    }


    /**
     * 解析格式为"d:fff.f.dows-eaglee"的字符串，生成对应的文件系统路径
     *
     * @param pathStr 输入的路径字符串
     * @return 解析后的文件系统路径
     */
    private String parsePath(String pathStr) {
        if (pathStr == null || pathStr.isEmpty()) {
            return "";
        }

        String parsedPath;
        boolean isWindows = System.getProperty("os.name").toLowerCase().contains("win");
        String separator = isWindows ? "\\" : "/";

        // 判断是否包含盘符（Windows特有）
        if (isWindows && pathStr.matches("^[a-zA-Z]:.*")) {
            // 提取盘符
            String driveLetter = pathStr.substring(0, 2); // 例如 "d:"
            // 提取剩余部分并将点替换为路径分隔符
            String pathPart = pathStr.substring(2).replace('.', separator.charAt(0));
            parsedPath = driveLetter + pathPart;
        } else {
            // Linux系统或Windows无盘符情况
            if (isWindows) {
                // Windows无盘符情况
                parsedPath = pathStr.replace('.', separator.charAt(0));
            } else {
                // Linux系统情况
                if (pathStr.matches("^[a-zA-Z]:.*")) {
                    // 如果Linux系统输入包含Windows风格的盘符，忽略盘符并添加根路径
                    String pathPart = pathStr.substring(2).replace('.', separator.charAt(0));
                    parsedPath = separator + pathPart;
                } else {
                    // 普通Linux路径，直接替换点并添加根路径
                    parsedPath = separator + pathStr.replace('.', separator.charAt(0));
                }
            }
        }

        return parsedPath;
    }


    /**
     * 解析特殊格式的路径字符串，生成对应的文件系统路径
     * 格式: [盘符:]路径部分.路径部分:...:文件夹名
     * 例如: d:fff.f:dows-eaglee -> d:\fff\f\dows-eaglee (Windows) 或 /fff/f/dows-eaglee (Linux)
     *
     * @param pathStr 输入的路径字符串
     * @return 解析后的文件系统路径
     */
    private String parsePath1(String pathStr) {
        if (pathStr == null || pathStr.isEmpty()) {
            return "";
        }

        // 分割路径字符串为路径部分和文件夹部分
        int lastColonIndex = pathStr.lastIndexOf(':');
        if (lastColonIndex == -1) {
            return pathStr; // 如果没有冒号，直接返回原字符串
        }

        String pathPart = pathStr.substring(0, lastColonIndex);
        String folderName = pathStr.substring(lastColonIndex + 1);
        //判断当前系统是否为Linux系统
        String osName = System.getProperty("os.name").toLowerCase();
        boolean isWindows = osName.contains("nux") || osName.contains("nix");
        // 解析路径部分
        String parsedPath;
        if (isWindows) {
            // Windows系统解析逻辑
            if (pathPart.matches("^[a-zA-Z]:.*")) {
                // 有盘符的情况，如 d:fff.f
                parsedPath = pathPart.replace('.', '\\');
            } else if (pathPart.startsWith(":")) {
                // 无盘符但以冒号开头的情况，如 :fff.f
                parsedPath = pathPart.substring(1).replace('.', '\\');
            } else {
                // 其他情况，直接替换
                parsedPath = pathPart.replace('.', '\\');
            }
        } else {
            // Linux系统解析逻辑
            if (pathPart.startsWith(":")) {
                // 以冒号开头的情况，如 :fff.f
                parsedPath = "/" + pathPart.substring(1).replace('.', '/');
            } else if (pathPart.matches("^[a-zA-Z]:.*")) {
                // 有盘符的情况，去除盘符并添加斜杠，如 d:fff.f -> /fff/f
                parsedPath = "/" + pathPart.substring(2).replace('.', '/');
            } else {
                // 其他情况，直接替换并添加斜杠
                parsedPath = "/" + pathPart.replace('.', '/');
            }
        }

        // 组合路径和文件夹名
        String separator = isWindows ? "\\" : "/";
        // 确保路径部分不以分隔符结尾
        if (parsedPath.endsWith(separator)) {
            parsedPath = parsedPath.substring(0, parsedPath.length() - 1);
        }
        // 确保文件夹名不以分隔符开头
        if (folderName.startsWith(separator)) {
            folderName = folderName.substring(1);
        }

        return parsedPath + separator + folderName;
    }


//    private String parsePath(String pathStr, boolean isWindows) {
//        if (pathStr == null || pathStr.isEmpty()) {
//            return "";
//        }
//
//        // 分割路径字符串为路径部分和文件夹部分
//        int lastColonIndex = pathStr.lastIndexOf(':');
//        if (lastColonIndex == -1) {
//            return pathStr; // 如果没有冒号，直接返回原字符串
//        }
//
//        String pathPart = pathStr.substring(0, lastColonIndex);
//        String folderName = pathStr.substring(lastColonIndex + 1);
//
//        // 解析路径部分
//        String parsedPath;
//        if (isWindows) {
//            // Windows系统解析逻辑
//            if (pathPart.matches("^[a-zA-Z]:.*")) {
//                // 有盘符的情况，如 d:fff.f
//                parsedPath = pathPart.replace('.', '\\');
//            } else if (pathPart.startsWith(":")) {
//                // 无盘符但以冒号开头的情况，如 :fff.f
//                parsedPath = pathPart.substring(1).replace('.', '\\');
//            } else {
//                // 其他情况，直接替换
//                parsedPath = pathPart.replace('.', '\\');
//            }
//        } else {
//            // Linux系统解析逻辑
//            if (pathPart.startsWith(":")) {
//                // 以冒号开头的情况，如 :fff.f
//                parsedPath = "/" + pathPart.substring(1).replace('.', '/');
//            } else if (pathPart.matches("^[a-zA-Z]:.*")) {
//                // 有盘符的情况，去除盘符并添加斜杠，如 d:fff.f -> /fff/f
//                parsedPath = "/" + pathPart.substring(2).replace('.', '/');
//            } else {
//                // 其他情况，直接替换并添加斜杠
//                parsedPath = "/" + pathPart.replace('.', '/');
//            }
//        }
//
//        // 组合路径和文件夹名
//        String separator = isWindows ? "\\" : "/";
//        // 确保路径部分不以分隔符结尾
//        if (parsedPath.endsWith(separator)) {
//            parsedPath = parsedPath.substring(0, parsedPath.length() - 1);
//        }
//        // 确保文件夹名不以分隔符开头
//        if (folderName.startsWith(separator)) {
//            folderName = folderName.substring(1);
//        }
//
//        return parsedPath + separator + folderName;
//    }
}
