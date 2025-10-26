package org.dows.mgc.generator;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.dows.mgc.parser.CodeFileSaver;
import org.dows.mgc.parser.CodeParserOld;
import org.dows.mgc.ai.AiCodeGeneratorService;
import org.dows.mgc.ai.model.HtmlCodeResult;
import org.dows.mgc.ai.model.MultiFileCodeResult;
import org.dows.mgc.exception.BusinessException;
import org.dows.mgc.exception.ErrorCode;
import org.dows.mgc.model.enums.CodeGenTypeEnum;
import reactor.core.publisher.Flux;

import java.io.File;

/*
 * 门面模式
 * AiCodeGeneratorFacade - 用于提供AI代码生成相关的服务接口
 * 通过此Facade可以调用具体的AI代码生成服务
 *
 * @deprecated 此类已被弃用，请使用新的代码生成服务
 */
@Slf4j
@Deprecated
public class AiCodeGeneratorFacadeOld {

    @Resource
    private AiCodeGeneratorService aiCodeGeneratorService;


    @Resource
    private CodeParserOld codeParser;

    public File generateCodeAndSave(String userMessage, CodeGenTypeEnum codeGenType) {
        if (codeGenType == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "代码生成类型不能为空");
        }
        return switch (codeGenType) {
            case CodeGenTypeEnum.HTML -> generateCodeAndSaveHtml(userMessage);
            case CodeGenTypeEnum.MULTI_FILE -> generateCodeAndSaveMultiFile(userMessage);
            default -> {
                String errorMessage = String.format("不支持的代码生成类型: %s", codeGenType.getText());
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, errorMessage);
            }

        };
    }

    private File generateCodeAndSaveHtml(String userMessage) {
        HtmlCodeResult htmlCodeResult = aiCodeGeneratorService.generateHtmlCode(userMessage);
        return CodeFileSaver.saveHtmlFile(htmlCodeResult);
    }

    private File generateCodeAndSaveMultiFile(String userMessage) {
        MultiFileCodeResult multiFileCodeResult = aiCodeGeneratorService.generateMultiFileCode(userMessage);
        return CodeFileSaver.saveMultiFile(multiFileCodeResult);
    }

    public Flux<String> generateCodeAndSaveStream(String userMessage, CodeGenTypeEnum codeGenType) {
        if (codeGenType == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "代码生成类型不能为空");
        }
        return switch (codeGenType) {
            case CodeGenTypeEnum.HTML -> generateCodeAndSaveHtmlStream(userMessage);
            case CodeGenTypeEnum.MULTI_FILE -> generateCodeAndSaveMultiFileStream(userMessage);
            default -> {
                String errorMessage = String.format("不支持的代码生成类型: %s", codeGenType.getText());
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, errorMessage);
            }

        };
    }

    /**
     * 生成 HTML 模式的代码并保存（流式）
     *
     * @param userMessage 用户提示词
     * @return 保存的目录
     */
    private Flux<String> generateCodeAndSaveHtmlStream(String userMessage) {
        Flux<String> result = aiCodeGeneratorService.generateHtmlCodeStream(userMessage);
        // 当流式返回生成代码完成后，再保存代码
        StringBuilder codeBuilder = new StringBuilder();
        return result
                .doOnNext(chunk -> {
                    // 实时收集代码片段
                    codeBuilder.append(chunk);
                })
                .doOnComplete(() -> {
                    // 流式返回完成后保存代码
                    try {
                        String completeHtmlCode = codeBuilder.toString();
                        HtmlCodeResult htmlCodeResult = codeParser.parseHtmlCode(completeHtmlCode);
                        // 保存代码到文件
                        File savedDir = CodeFileSaver.saveHtmlFile(htmlCodeResult);
                        log.info("保存成功，路径为：" + savedDir.getAbsolutePath());
                    } catch (Exception e) {
                        log.error("保存失败2: {}", e.getMessage());
                    }
                });
    }

    /**
     * 生成多文件模式的代码并保存（流式）
     *
     * @param userMessage 用户提示词
     * @return 保存的目录
     */
    private Flux<String> generateCodeAndSaveMultiFileStream(String userMessage) {
        Flux<String> result = aiCodeGeneratorService.generateMultiFileCodeStream(userMessage);
        // 当流式返回生成代码完成后，再保存代码
        StringBuilder codeBuilder = new StringBuilder();
        return result
                .doOnNext(chunk -> {
                    // 实时收集代码片段
                    codeBuilder.append(chunk);
                })
                .doOnComplete(() -> {
                    // 流式返回完成后保存代码
                    try {
                        String completeMultiFileCode = codeBuilder.toString();
                        MultiFileCodeResult multiFileResult = codeParser.parseMultiFileCode(completeMultiFileCode);
                        // 保存代码到文件
                        File savedDir = CodeFileSaver.saveMultiFile(multiFileResult);
                        log.info("保存成功，路径为：" + savedDir.getAbsolutePath());
                    } catch (Exception e) {
                        log.error("保存失败1: {}", e.getMessage());
                    }
                });
    }

}
