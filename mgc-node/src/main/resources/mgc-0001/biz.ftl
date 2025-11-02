package ${pkg!""};

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
<#if imports??>
    <#list imports as i>
import ${i!""};
    </#list>
</#if>
<#--import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;-->
<#--import io.swagger.v3.oas.annotations.media.Schema;-->
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;
import java.math.BigDecimal;

/**
 * @description ${comment!""}
 * @author ${author.name!""}
 * @email ${author.email!""}
 * @since ${.now}
 */
<#if extendClass??>
@EqualsAndHashCode(callSuper = true)
</#if>
@RequiredArgsConstructor
@Slf4j
@Component
public class ${name?cap_first!""}${suffix.biz!""}{
<#if methods?? && (methods?size >0)>
    <#list methods as method >
    /**
     * @param
     * @return
     * @说明: ${method.comment!""}
     * @关联表: ${method.tables!""}
     * @工时: ${method.workTime!"0"}${method.timeUnit!"H"}
     * @开发者: ${method.developer!""}
     * @开始时间: ${method.startTime!""}
     * @创建时间: ${.now}
     */
    public <#if method.output??>${method.output.code?cap_first!""}<#else>void</#if> ${method.name?uncap_first!""}(<#if method.inputs??><#list method.inputs as input>${input.code?cap_first!""} ${input.name?uncap_first!""}<#if input_has_next>, </#if></#list></#if>){
        <#if method.output??>
            <#if  method.output.type=='boolean'>
        return Boolean.FALSE;
            <#else >
        return new ${method.output.code!""}();
            </#if>
        </#if>
    }
    </#list>

</#if>
}
