package org.dows.mgc.generator;

import org.dows.mgc.model.enums.CodeGenTypeEnum;
import reactor.core.publisher.Flux;

import java.io.File;

public interface CodeGeneratorAdaptor {

    /**
     * 统一入口：根据类型生成并保存代码
     *
     * @param userMessage     用户提示词
     * @param codeGenTypeEnum 生成类型
     * @param appId           应用ID
     * @return 保存的目录
     */
    public File generateCodeAndSave(String userMessage, CodeGenTypeEnum codeGenTypeEnum, Long appId);

    /**
     * 统一入口：根据类型生成并保存代码（流式）
     *
     * @param userMessage     用户提示词
     * @param codeGenTypeEnum 生成类型
     * @param appId           应用ID
     */
    public Flux<String> generateCodeAndSaveStream(String userMessage, CodeGenTypeEnum codeGenTypeEnum, Long appId);
}
