package org.dows.mgc.handler;

import jakarta.annotation.Resource;
import org.dows.mgc.exception.BusinessException;
import org.dows.mgc.exception.ErrorCode;
import org.dows.mgc.entity.UserEntity;
import org.dows.mgc.model.enums.CodeGenTypeEnum;
import org.dows.mgc.service.ChatHistoryService;
import org.dows.mgc.uim.User;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;


@Component
public class StreamHandlerExecutor {

    @Resource
    private SimpleTextStreamHandler simpleTextStreamHandler;
    @Resource
    private JsonMessageStreamHandler jsonMessageStreamHandler;


    /**
     * 执行代码流处理
     *
     * @param originFlux      代码内容流
     * @param codeGenTypeEnum 代码生成类型
     * @return 解析结果（HtmlCodeResult 或 MultiFileCodeResult）
     */

    public Flux<String> doExecutor(Flux<String> originFlux, CodeGenTypeEnum codeGenTypeEnum,
                                   ChatHistoryService chatHistoryService,
                                   long appId, User loginUser) {
        return switch (codeGenTypeEnum) {
            case HTML, MULTI_FILE -> simpleTextStreamHandler.handle(originFlux, chatHistoryService, appId, loginUser);
            case VUE_PROJECT -> jsonMessageStreamHandler.handle(originFlux, chatHistoryService, appId, loginUser);
            default -> throw new BusinessException(ErrorCode.SYSTEM_ERROR, "不支持的代码生成类型");
        };
    }
}
