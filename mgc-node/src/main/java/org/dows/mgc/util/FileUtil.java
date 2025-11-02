package org.dows.mgc.util;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Slf4j
public class FileUtil {

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

}