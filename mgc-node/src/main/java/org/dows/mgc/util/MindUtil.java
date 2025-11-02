package org.dows.mgc.util;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Slf4j
public class MindUtil {

    /**
     * 递归复制文件夹及其子文件夹和文件到目标地址。
     *
     * @param sourcePath 源文件夹路径
     * @param targetPath 目标文件夹路径
     * @throws IOException 如果复制过程中发生I/O错误
     */
    public static void copyFolderRecursively(Path sourcePath, Path targetPath) throws IOException {
        // 确保源路径存在且是目录
        if (!Files.exists(sourcePath) || !Files.isDirectory(sourcePath)) {
            throw new IOException("Source path does not exist or is not a directory: " + sourcePath);
        }

        // 创建目标目录，如果它不存在
        if (!Files.exists(targetPath)) {
            Files.createDirectories(targetPath);
        }

        Files.walk(sourcePath)
                .filter(Files::isRegularFile)
                .forEach(sourceFile -> {
                    try {
                        Path relativePath = sourcePath.relativize(sourceFile);
                        Path targetFile = targetPath.resolve(relativePath);
                        Files.createDirectories(targetFile.getParent());
                        Files.copy(sourceFile, targetFile, StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }

    public static void replaceDeployWithTrue(String filePath) {
        Path path = Paths.get(filePath);
        try {
            // 读取文件内容
            String content = new String(Files.readAllBytes(path), java.nio.charset.StandardCharsets.UTF_8);

            // 替换所有`${deploy}`为`true`
            String newContent = content.replace("${deploy}", "true");

            // 将新内容写回到文件
            Files.write(path, newContent.getBytes(java.nio.charset.StandardCharsets.UTF_8));

            System.out.println("Replacement completed successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 从格式为/.../的字符串中提取最后一个冒号后最后一个点的内容
     * 如果最后一个冒号后没有点，则直接取该部分内容
     *
     * @param input 输入字符串
     * @return 提取的字符串
     */
    public static  String extractProjectName(String input) {
        if (input == null || input.isEmpty()) {
            return null;
        }

        // 提取/.../中间的内容
        int firstSlashIndex = input.indexOf('/');
        int lastSlashIndex = input.lastIndexOf('/');

        if (firstSlashIndex == -1 || lastSlashIndex == -1 || firstSlashIndex == lastSlashIndex) {
            return null; // 不符合/.../格式
        }

        String content = input.substring(firstSlashIndex + 1, lastSlashIndex);

        // 处理最后一个冒号后的内容
        int lastColonIndex = content.lastIndexOf(':');
        if (lastColonIndex != -1) {
            String afterLastColon = content.substring(lastColonIndex + 1);

            // 检查最后一个点
            int lastDotIndex = afterLastColon.lastIndexOf('.');
            if (lastDotIndex != -1 && lastDotIndex < afterLastColon.length() - 1) {
                return afterLastColon.substring(lastDotIndex + 1);
            } else {
                return afterLastColon; // 如果没有点或点在最后，直接返回冒号后的内容
            }
        } else {
            // 如果没有冒号，返回整个中间内容
            return content;
        }
    }

}