package ${pkg!""};

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.Operation;
<#if imports??>
    <#list imports as i>
import ${i!""};
    </#list>
</#if>
import org.springframework.web.bind.annotation.*;

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
<#--@Data
@NoArgsConstructor-->
<#--@Schema(name = "${parameter.type} 对象", title = "${(parameter.descr!"")}")-->
<#--@ApiModel(value = "${parameter.type} 对象", description = "${(parameter.descr!"")}")-->
public interface ${name?cap_first!""}${suffix.api!""}{
<#if methods?? && (methods?size >0)>
    <#list methods as method >
    /**
     * ${method.comment!""}
     * <#--@param ${method?uncap_first!""}Request-->
     */
    @Operation(summary = "${method.comment!""}")
    <#--<#if method.output??>${method.output.code?cap_first!""}<#else>void</#if> ${method.name?uncap_first!""}(<#if method.inputs??><#list method.inputs as input>${input.code?cap_first!""} ${input.type?uncap_first!""}<#if input_has_next>, </#if></#list></#if>);-->
    <#if method.method == "get">
    @GetMapping("${method.url!""}")
    <#if method.output??>${method.output.code?cap_first!""}<#else>void</#if> ${method.name?uncap_first!""}(<#if method.inputs??><#list method.inputs as input>${input.code?cap_first!""} ${input.name?uncap_first!""}<#if input_has_next>, </#if></#list></#if>);
    </#if>
    <#if method.method == "post">
    @PostMapping("${method.url!""}")
    <#if method.output??>${method.output.code?cap_first!""}<#else>void</#if> ${method.name?uncap_first!""}(<#if method.inputs??><#list method.inputs as input>${input.code?cap_first!""} ${input.name?uncap_first!""}<#if input_has_next>, </#if></#list></#if>);
    </#if>
    <#if method.method == "put">
    @PutMapping("${method.url!""}")
    <#if method.output??>${method.output.code?cap_first!""}<#else>void</#if> ${method.name?uncap_first!""}(<#if method.inputs??><#list method.inputs as input>${input.code?cap_first!""} ${input.name?uncap_first!""}<#if input_has_next>, </#if></#list></#if>);
    </#if>
    <#if method.method == "delete">
    @DeleteMapping("${method.url!""}")
    <#if method.output??>${method.output.code?cap_first!""}<#else>void</#if> ${method.name?uncap_first!""}(<#if method.inputs??><#list method.inputs as input>${input.code?cap_first!""} ${input.name?uncap_first!""}<#if input_has_next>, </#if></#list></#if>);
    </#if>
    </#list>

</#if>
}
