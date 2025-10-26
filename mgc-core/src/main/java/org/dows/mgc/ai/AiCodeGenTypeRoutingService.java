package org.dows.mgc.ai;


import dev.langchain4j.service.SystemMessage;
import org.dows.mgc.model.enums.CodeGenTypeEnum;


public interface AiCodeGenTypeRoutingService {


    @SystemMessage(fromResource = "prompt/codegen-routing-system-prompt.txt")
    CodeGenTypeEnum getCodeGenType(String userMessage);
}
