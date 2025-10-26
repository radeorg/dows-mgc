package org.dows.mgc.generator;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.service.TokenStream;
import dev.langchain4j.service.tool.ToolExecution;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.dows.mgc.GenerationSessionRegistry;
import org.dows.mgc.ai.AiCodeGeneratorService;
import org.dows.mgc.ai.AiCodeGeneratorServiceFactory;
import org.dows.mgc.ai.model.HtmlCodeResult;
import org.dows.mgc.ai.model.MultiFileCodeResult;
import org.dows.mgc.ai.model.message.AiResponseMessage;
import org.dows.mgc.ai.model.message.ToolExecutedMessage;
import org.dows.mgc.ai.model.message.ToolRequestMessage;
import org.dows.mgc.ai.tools.BaseTool;
import org.dows.mgc.ai.tools.ToolManager;
import org.dows.mgc.builder.VueProjectBuilder;
import org.dows.mgc.constant.AppConstant;
import org.dows.mgc.context.UserContextHolder;
import org.dows.mgc.exception.BusinessException;
import org.dows.mgc.exception.ErrorCode;
import org.dows.mgc.model.enums.ChatHistoryMessageTypeEnum;
import org.dows.mgc.model.enums.CodeGenTypeEnum;
import org.dows.mgc.monitor.MonitorContext;
import org.dows.mgc.monitor.MonitorContextHolder;
import org.dows.mgc.parser.CodeParserExecutor;
import org.dows.mgc.saver.CodeFileSaverExecutor;
import org.dows.mgc.service.ChatHistoryService;
import org.dows.mgc.service.GenerationStatusService;
import org.dows.mgc.uim.User;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
public class AiCodeGenerator implements CodeGeneratorAdaptor {

    @Resource
    private AiCodeGeneratorServiceFactory aiCodeGeneratorServiceFactory;

    @Resource
    private VueProjectBuilder vueProjectBuilder;

    @Resource
    private GenerationStatusService generationStatusService;

    @Resource
    private ChatHistoryService chatHistoryService;

    @Resource
    private ToolManager toolManager;

    /**
     * 统一入口：根据类型生成并保存代码
     *
     * @param userMessage     用户提示词
     * @param codeGenTypeEnum 生成类型
     * @param appId           应用ID
     * @return 保存的目录
     */
    public File generateCodeAndSave(String userMessage, CodeGenTypeEnum codeGenTypeEnum, Long appId) {
        if (codeGenTypeEnum == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "生成类型为空");
        }

        // 确保监控上下文存在
        ensureMonitorContext(appId);

        // 通过工厂来获得aiservice
        AiCodeGeneratorService aiCodeGeneratorService = aiCodeGeneratorServiceFactory.getAiCodeGeneratorService(appId,
                codeGenTypeEnum);
        return switch (codeGenTypeEnum) {
            case CodeGenTypeEnum.HTML -> {
                HtmlCodeResult result = aiCodeGeneratorService.generateHtmlCode(userMessage);
                yield CodeFileSaverExecutor.executeSaver(result, CodeGenTypeEnum.HTML, appId);
            }
            case CodeGenTypeEnum.MULTI_FILE -> {
                MultiFileCodeResult result = aiCodeGeneratorService.generateMultiFileCode(userMessage);
                yield CodeFileSaverExecutor.executeSaver(result, CodeGenTypeEnum.MULTI_FILE, appId);
            }
            default -> {
                String errorMessage = "不支持的生成类型：" + codeGenTypeEnum.getValue();
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, errorMessage);
            }
        };
    }

    /**
     * 统一入口：根据类型生成并保存代码（流式）
     *
     * @param userMessage     用户提示词
     * @param codeGenTypeEnum 生成类型
     * @param appId           应用ID
     */
    public Flux<String> generateCodeAndSaveStream(String userMessage, CodeGenTypeEnum codeGenTypeEnum, Long appId) {
        if (codeGenTypeEnum == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "生成类型为空");
        }

        // 确保监控上下文存在
        ensureMonitorContext(appId);

        // 通过工厂来获得aiservice
        AiCodeGeneratorService aiCodeGeneratorService = aiCodeGeneratorServiceFactory.getAiCodeGeneratorService(appId,
                codeGenTypeEnum);

        return switch (codeGenTypeEnum) {
            case CodeGenTypeEnum.HTML -> {
                Flux<String> codeStream = aiCodeGeneratorService.generateHtmlCodeStream(userMessage);
                yield processCodeStream(codeStream, CodeGenTypeEnum.HTML, appId);
            }
            case CodeGenTypeEnum.MULTI_FILE -> {
                Flux<String> codeStream = aiCodeGeneratorService.generateMultiFileCodeStream(userMessage);
                yield processCodeStream(codeStream, CodeGenTypeEnum.MULTI_FILE, appId);
            }
            case CodeGenTypeEnum.VUE_PROJECT -> {
                TokenStream codeStream = aiCodeGeneratorService.generateVueCodeStream(appId, userMessage);
                yield processTokenStream(codeStream, appId);
            }
        };
    }

    /**
     * 确保监控上下文存在，如果不存在则创建一个默认的
     *
     * @param appId 应用ID
     */
    private void ensureMonitorContext(Long appId) {
        MonitorContext existingContext = MonitorContextHolder.getContext();
        if (existingContext == null) {
            log.info("MonitorContext not found, creating default context for appId: {} on thread: {}",
                    appId, Thread.currentThread().getName());
            MonitorContext defaultContext = MonitorContext.builder()
                    .userId("system") // 如果没有用户上下文，使用系统标识
                    .appId(appId.toString())
                    .build();
            MonitorContextHolder.setContext(defaultContext);

            // 验证设置是否成功
            MonitorContext verifyContext = MonitorContextHolder.getContext();
            if (verifyContext != null) {
                log.info("MonitorContext successfully set: userId={}, appId={}",
                        verifyContext.getUserId(), verifyContext.getAppId());
            } else {
                log.error("Failed to set MonitorContext, still null after setting");
            }
        } else {
            log.debug("MonitorContext already exists: userId={}, appId={} on thread: {}",
                    existingContext.getUserId(), existingContext.getAppId(), Thread.currentThread().getName());
        }
    }

    /**
     * 将 TokenStream 转换为 Flux<String>，并传递工具调用信息 适配器模式
     *
     * @param tokenStream TokenStream 对象
     * @param appId       应用ID
     * @return Flux<String> 流式响应
     */
    private Flux<String> processTokenStream(TokenStream tokenStream, Long appId) {
        // 在TokenStream启动前，获取当前的监控上下文
        MonitorContext currentContext = MonitorContextHolder.getContext();
        if (currentContext == null) {
            log.warn("processTokenStream: MonitorContext is null, creating default context for appId: {}", appId);
            currentContext = MonitorContext.builder()
                    .userId("system")
                    .appId(appId.toString())
                    .build();
            MonitorContextHolder.setContext(currentContext);
        }

        // 保存上下文引用，用于在TokenStream回调中恢复
        final MonitorContext contextForCallbacks = currentContext;

        return Flux.create(sink -> {
            final AtomicInteger toolCallCount = new AtomicInteger(0);
            final AtomicBoolean cancelNotified = new AtomicBoolean(false);
            // 预先捕获当前用户上下文（可能为 null）
            User contextUser = UserContextHolder.get();
            // 累积局部工具参数（流式分片 JSON 拼接用） key=index
            Map<String, StringBuilder> partialToolArgs = new ConcurrentHashMap<>();
            // 累积完整的 AI 回复内容，用于保存到数据库（包括工具调用过程）
            final StringBuilder completeAiResponse = new StringBuilder();

            // 封装一个统一的取消检测
            Runnable cancelCheck = () -> {
                if (GenerationSessionRegistry.isCancelled(appId) && cancelNotified.compareAndSet(false, true)) {
                    // 恢复上下文
                    MonitorContextHolder.setContext(contextForCallbacks);
                    if (contextUser != null) {
                        org.dows.mgc.context.UserContextHolder.set(contextUser);
                    }
                    log.info("用户取消生成，appId={}", appId);
                    try {
                        generationStatusService.markStopped(appId, contextForCallbacks.getAppId(), "user_stopped");
                    } catch (Exception ignore) {
                    }
                    AiResponseMessage cancelMsg = new AiResponseMessage("\n\n⏹️ 用户已取消，本次生成已停止。\n");
                    sink.next(JSONUtil.toJsonStr(cancelMsg));
                    sink.complete();
                }
            };

            tokenStream
                    .onPartialResponse((String partialResponse) -> {
                        cancelCheck.run();
                        if (cancelNotified.get())
                            return; // 取消后不再发送
                        MonitorContextHolder.setContext(contextForCallbacks);
                        if (contextUser != null) {
                            org.dows.mgc.context.UserContextHolder.set(contextUser);
                        }
                        // 累积 AI 响应内容到完整回复中
                        completeAiResponse.append(partialResponse);
                        AiResponseMessage aiResponseMessage = new AiResponseMessage(partialResponse);
                        sink.next(JSONUtil.toJsonStr(aiResponseMessage));
                        log.debug("AI 部分响应: {}",
                                partialResponse.length() > 100 ? partialResponse.substring(0, 100) + "..." : partialResponse);
                    })
                    .onPartialToolExecutionRequest((index, toolExecutionRequest) -> {
                        cancelCheck.run();
                        if (cancelNotified.get())
                            return;
                        MonitorContextHolder.setContext(contextForCallbacks);
                        if (contextUser != null) {
                            org.dows.mgc.context.UserContextHolder.set(contextUser);
                        }
                        String fragment = toolExecutionRequest.arguments();
                        if (fragment != null) {
                            String key = index + "|" + toolExecutionRequest.name();
                            partialToolArgs.computeIfAbsent(key, k -> new StringBuilder()).append(fragment);
                            // 仅做轻量日志，避免误判流式分片非 JSON
                            if (fragment.length() > 0) {
                                log.debug("工具[{}] 分片参数追加 index={} fragmentLen={} head='{}'", toolExecutionRequest.name(), index,
                                        fragment.length(), fragment.substring(0, Math.min(30, fragment.length())).replaceAll("\n", "\\n"));
                            }
                        }
                        ToolRequestMessage toolRequestMessage = new ToolRequestMessage(toolExecutionRequest);
                        sink.next(JSONUtil.toJsonStr(toolRequestMessage));
                    })
                    .onToolExecuted((ToolExecution toolExecution) -> {
                        cancelCheck.run();
                        if (cancelNotified.get())
                            return;
                        MonitorContextHolder.setContext(contextForCallbacks);
                        if (contextUser != null) {
                            org.dows.mgc.context.UserContextHolder.set(contextUser);
                        }
                        int currentCount = toolCallCount.incrementAndGet();
                        // 尝试获取累积的完整参数（如有）
                        String accumulatedArgs = null;
                        try {
                            String key = toolExecution.request().name();
                            StringBuilder sb = partialToolArgs.get(key);
                            if (sb != null)
                                accumulatedArgs = sb.toString();
                        } catch (Exception ignore) {
                        }
                        if (accumulatedArgs != null) {
                            log.info("工具执行完成 #{}: {} (accArgsLen={}) -> {}", currentCount, toolExecution.request().name(),
                                    accumulatedArgs.length(),
                                    toolExecution.result().length() > 100 ? toolExecution.result().substring(0, 100) + "..."
                                            : toolExecution.result());
                        } else {
                            log.info("工具执行完成 #{}: {} -> {}", currentCount, toolExecution.request().name(),
                                    toolExecution.result().length() > 100 ? toolExecution.result().substring(0, 100) + "..."
                                            : toolExecution.result());
                        }
                        // 使用与JsonMessageStreamHandler相同的工具信息格式化逻辑
                        String toolName = toolExecution.request().name();
                        String toolArgs = toolExecution.request().arguments();

                        try {
                            JSONObject jsonObject = JSONUtil.parseObj(toolArgs);
                            BaseTool tool = toolManager.getTool(toolName);
                            if (tool != null) {
                                String result = tool.generateToolExecutedResult(jsonObject);
                                String output = String.format("\n\n%s\n\n", result);
                                completeAiResponse.append(output);
                            }
                        } catch (Exception e) {
                            log.debug("工具执行信息格式化失败: {}", e.getMessage());
                            // 降级处理：使用简单格式
                            completeAiResponse.append("\n\n[工具调用] ").append(toolName).append("\n\n");
                        }

                        ToolExecutedMessage toolExecutedMessage = new ToolExecutedMessage(toolExecution);
                        sink.next(JSONUtil.toJsonStr(toolExecutedMessage));
                    })
                    .onCompleteResponse((ChatResponse response) -> {
                        // 如果已经取消，直接忽略完成回调（取消逻辑里已 complete）
                        if (cancelNotified.get() || GenerationSessionRegistry.isCancelled(appId)) {
                            if (cancelNotified.compareAndSet(false, true)) {
                                // 还未来得及触发取消消息（极端时序），补发一次
                                MonitorContextHolder.setContext(contextForCallbacks);
                                if (contextUser != null) {
                                    org.dows.mgc.context.UserContextHolder.set(contextUser);
                                }
                                try {
                                    generationStatusService.markStopped(appId, contextForCallbacks.getAppId(), "user_stopped");
                                } catch (Exception ignore) {
                                }
                                AiResponseMessage cancelMsg = new AiResponseMessage("\n\n⏹️ 用户已取消，本次生成已停止。\n");
                                sink.next(JSONUtil.toJsonStr(cancelMsg));
                                sink.complete();
                            }
                            return;
                        }
                        MonitorContextHolder.setContext(contextForCallbacks);
                        if (contextUser != null) {
                            org.dows.mgc.context.UserContextHolder.set(contextUser);
                        }
                        log.info("AI 响应完成，总工具调用次数: {}", toolCallCount.get());

                        // 直接保存完整的AI回复到数据库，不依赖前端流完成状态
                        try {
                            if (completeAiResponse.length() > 0) {
                                String completeContent = completeAiResponse.toString();
                                Long userId = contextUser != null ? contextUser.getId() : null;
                                log.info("保存完整AI回复到数据库，长度: {}, userId: {}", completeContent.length(), userId);
                                chatHistoryService.addChatMessage(appId, completeContent, ChatHistoryMessageTypeEnum.AI.getValue(), userId);
                            }
                        } catch (Exception e) {
                            log.error("保存AI回复到数据库失败: {}", e.getMessage(), e);
                        }

                        String pathName = AppConstant.CODE_OUTPUT_ROOT_DIR + File.separator + "vue_project_" + appId;
                        boolean buildSuccess = vueProjectBuilder.buildVueProject(pathName);
                        String buildMsg;
                        if (buildSuccess) {
                            buildMsg = "\n\n✅ Vue 项目构建成功，可以预览。\n";
                            try {
                                generationStatusService.markBuilt(appId, contextForCallbacks.getAppId(), "success");
                            } catch (Exception ignore) {
                            }
                        } else {
                            buildMsg = "\n\n❌ Vue 项目构建失败，请检查 package.json / 依赖或构建日志。\n";
                            log.warn("Vue 项目构建失败（已通知前端），路径: {}", pathName);
                            try {
                                generationStatusService.markFailed(appId, contextForCallbacks.getAppId(), "build failed");
                            } catch (Exception ignore) {
                            }
                        }
                        AiResponseMessage buildResultMessage = new AiResponseMessage(buildMsg);
                        sink.next(JSONUtil.toJsonStr(buildResultMessage));
                        sink.complete();
                    })
                    .onError((Throwable error) -> {
                        if (cancelNotified.get() || GenerationSessionRegistry.isCancelled(appId)) {
                            // 取消后若底层仍抛错，忽略（已向前端发过取消消息）
                            return;
                        }
                        MonitorContextHolder.setContext(contextForCallbacks);
                        if (contextUser != null) {
                            org.dows.mgc.context.UserContextHolder.set(contextUser);
                        }
                        String errMsg = error.getMessage();
                        if (errMsg != null && errMsg.contains("function.arguments") && errMsg.contains("invalid_parameter_error")) {
                            log.error("TokenStream 处理错误 (函数参数格式问题)，工具调用次数: {} detail={}", toolCallCount.get(), errMsg);
                        } else {
                            log.error("TokenStream 处理错误，工具调用次数: {}", toolCallCount.get(), error);
                        }
                        try {
                            generationStatusService.markFailed(appId, contextForCallbacks.getAppId(), error.getMessage());
                        } catch (Exception ignore) {
                        }
                        sink.error(error);
                    })
                    .start();
        });
    }

    /**
     * 通用流式代码处理方法
     *
     * @param codeStream  代码流
     * @param codeGenType 代码生成类型
     * @param appId       应用ID
     * @return 流式响应
     */
    private Flux<String> processCodeStream(Flux<String> codeStream, CodeGenTypeEnum codeGenType, Long appId) {
        StringBuilder codeBuilder = new StringBuilder();
        return codeStream.doOnNext(chunk -> {
            // 实时收集代码片段
            codeBuilder.append(chunk);
        }).doOnComplete(() -> {
            // 流式返回完成后保存代码
            try {
                String completeCode = codeBuilder.toString();
                // 使用执行器解析代码
                Object parsedResult = CodeParserExecutor.executeParser(completeCode, codeGenType);
                // 使用执行器保存代码
                File savedDir = CodeFileSaverExecutor.executeSaver(parsedResult, codeGenType, appId);
                log.info("保存成功，路径为：" + savedDir.getAbsolutePath());
            } catch (Exception e) {
                log.error("保存失败3: {}", e.getMessage());
            }
        });
    }
}
