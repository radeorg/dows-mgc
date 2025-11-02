package org.dows.mgc.git;

import org.springframework.expression.ParserContext;

/**
 * @description: </br>
 * @author: lait.zhang@gmail.com
 * @date: 5/8/2024 1:51 PM
 * @history: </br>
 * <author>      <time>      <version>    <desc>
 * 修改人姓名      修改时间        版本号       描述
 */
public class GitParserContext implements ParserContext {
    @Override
    public boolean isTemplate() {
        return true;
    }

    @Override
    public String getExpressionPrefix() {
        return "${";
    }

    @Override
    public String getExpressionSuffix() {
        return "}";
    }
}

