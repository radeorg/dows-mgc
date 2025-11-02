package ${pkg!""};

<#if imports??>
    <#list imports as i>
import ${i!""};
    </#list>
</#if>
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.dows.framework.api.Response;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.math.BigDecimal;

/**
 * @description ${comment!""}
 * @author ${author.name!""}
 * @email ${author.email!""}
 * @since ${.now}
 */
@RequiredArgsConstructor
@RestController
@Tag(name = "${comment!""}", description = "${comment!""}")
public class ${name?cap_first!""}${suffix.rest!""}<#if api??> implements ${name?cap_first!""}${suffix.api?cap_first!""}</#if>{

    private final ${(name?cap_first)!""}${(suffix.biz?cap_first)!""} ${(name?uncap_first)!""}${(suffix.biz?cap_first)!""};
<#if methods?? && (methods?size >0)>
    <#list methods as method >
    <#if method.expose>

        <#if method.method == "get">
    public <#if method.output??>${method.output.code?cap_first!""}<#else>void</#if> ${method.name?uncap_first!""}(<#if method.inputs??><#list method.inputs as input>${input.code?cap_first!""} ${input.name?uncap_first!""}<#if input_has_next>, </#if></#list></#if>){
        <#if method.output??>return </#if>${(name?uncap_first)!""}${(suffix.biz?cap_first)!""}.${(method.name!"")?uncap_first}(<#if method.inputs??><#list method.inputs as input>${input.name?uncap_first!""}<#if input_has_next>, </#if></#list></#if>);
    }
        </#if>
        <#if method.method == "post">
    public <#if method.output??>${method.output.code?cap_first!""}<#else>void</#if> ${method.name?uncap_first!""}(<#if method.inputs??><#list method.inputs as input><#if input.customType>@Validated @RequestBody </#if>${input.code?cap_first!""} ${input.name?uncap_first!""}<#if input_has_next>, </#if></#list></#if>){
        <#if method.output??>return </#if>${(name?uncap_first)!""}${(suffix.biz?cap_first)!""}.${(method.name!"")?uncap_first}(<#if method.inputs??><#list method.inputs as input>${input.name?uncap_first!""}<#if input_has_next>, </#if></#list></#if>);
    }
        </#if>
        <#if method.method == "put">
    public <#if method.output??>${method.output.code?cap_first!""}<#else>void</#if> ${method.name?uncap_first!""}(<#if method.inputs??><#list method.inputs as input><#if input.customType>@Validated @RequestBody</#if>${input.code?cap_first!""} ${input.name?uncap_first!""}<#if input_has_next>, </#if></#list></#if>){
        <#if method.output??>return </#if>${(name?uncap_first)!""}${(suffix.biz?cap_first)!""}.${(method.name!"")?uncap_first}(<#if method.inputs??><#list method.inputs as input>${input.name?uncap_first!""}<#if input_has_next>, </#if></#list></#if>);
    }
        </#if>
        <#if method.method == "delete">
    public <#if method.output??>${method.output.code?cap_first!""}<#else>void</#if> ${method.name?uncap_first!""}(<#if method.inputs??><#list method.inputs as input><#if input.customType>@Validated @RequestBody</#if>${input.code?cap_first!""} ${input.name?uncap_first!""}<#if input_has_next>, </#if></#list></#if>){
        <#if method.output??>return </#if>${(name?uncap_first)!""}${(suffix.biz?cap_first)!""}.${(method.name!"")?uncap_first}(<#if method.inputs??><#list method.inputs as input>${input.name?uncap_first!""}<#if input_has_next>, </#if></#list></#if>);
    }
        </#if>
    <#else>

    @Operation(summary = "${method.comment!""}")
        <#if method.method == "get">
    @GetMapping("${method.url!""}")
    public <#if method.output??>${method.output.code?cap_first!""}<#else>void</#if> ${method.name?uncap_first!""}(<#if method.inputs??><#list method.inputs as input>${input.code?cap_first!""} ${input.name?uncap_first!""}<#if input_has_next>, </#if></#list></#if>){
        <#if method.output??>return </#if>${(name?uncap_first)!""}${(suffix.biz?cap_first)!""}.${(method.name!"")?uncap_first}(<#if method.inputs??><#list method.inputs as input>${input.name?uncap_first!""}<#if input_has_next>, </#if></#list></#if>);
    }
        </#if>
        <#if method.method == "post">
    @PostMapping("${method.url!""}")
    public <#if method.output??>${method.output.code?cap_first!""}<#else>void</#if> ${method.name?uncap_first!""}(<#if method.inputs??><#list method.inputs as input><#if input.customType>@Validated @RequestBody </#if>${input.code?cap_first!""} ${input.name?uncap_first!""}<#if input_has_next>, </#if></#list></#if>){
        <#if method.output??>return </#if>${(name?uncap_first)!""}${(suffix.biz?cap_first)!""}.${(method.name!"")?uncap_first}(<#if method.inputs??><#list method.inputs as input>${input.name?uncap_first!""}<#if input_has_next>, </#if></#list></#if>);
    }
        </#if>
        <#if method.method == "put">
    @PutMapping("${method.url!""}")
    public <#if method.output??>${method.output.code?cap_first!""}<#else>void</#if> ${method.name?uncap_first!""}(<#if method.inputs??><#list method.inputs as input><#if input.customType>@Validated @RequestBody</#if>${input.code?cap_first!""} ${input.name?uncap_first!""}<#if input_has_next>, </#if></#list></#if>){
        <#if method.output??>return </#if>${(name?uncap_first)!""}${(suffix.biz?cap_first)!""}.${(method.name!"")?uncap_first}(<#if method.inputs??><#list method.inputs as input>${input.name?uncap_first!""}<#if input_has_next>, </#if></#list></#if>);
    }
        </#if>
        <#if method.method == "delete">
    @DeleteMapping("${method.url!""}")
    public <#if method.output??>${method.output.code?cap_first!""}<#else>void</#if> ${method.name?uncap_first!""}(<#if method.inputs??><#list method.inputs as input><#if input.customType>@Validated @RequestBody</#if>${input.code?cap_first!""} ${input.name?uncap_first!""}<#if input_has_next>, </#if></#list></#if>){
        <#if method.output??>return </#if>${(name?uncap_first)!""}${(suffix.biz?cap_first)!""}.${(method.name!"")?uncap_first}(<#if method.inputs??><#list method.inputs as input>${input.name?uncap_first!""}<#if input_has_next>, </#if></#list></#if>);
    }
        </#if>
    </#if>
    </#list>
</#if>
}
<#--
/**
* @description ${project.descr!""}:${api.descr!""}:${interface.descr!""}
*
* @author ${project.author!""}
* @date ${.now}
*/
@RequiredArgsConstructor
@RestController
<#if !interfaceInfos[interface.name]??>
&lt;#&ndash;@Api(tags = "${interface.descr!""}")&ndash;&gt;
@Tag(name = "${interface.descr!""}", description = "${interface.descr!""}")
</#if>
public class ${(interface.name?cap_first)!""}${(suffix.rest?cap_first)!""} <#if interfaceInfos[interface.name]??>implements ${(interface.name?cap_first)!""}${(suffix.api?cap_first)!""}</#if>{
    private final ${(interface.name?cap_first)!""}${(suffix.biz?cap_first)!""} ${(interface.name?uncap_first)!""}${(suffix.biz?cap_first)!""};

<#if methods?? && (methods?size >0)>
    <#list methods as m>
    /**
    * ${m.descr!""}
    * @param
    * @return
    */
        <#if m.methodType== "api">
    @Override
            <#if m.httpMethod == "get">
    public <#if (m.output.type)??><#if m.output.paramType??><#if m.output.paramType=='Void'>void<#else>${m.output.returnType!""}</#if></#if></#if> ${(m.name!"")?uncap_first}(<#if m.inputs?? && (m.inputs?size >0)><#list m.inputs as ip><#if ip.paramType??>@Validated ${ip.paramType!""}</#if> ${ip.name!""}<#if ip_has_next>, </#if></#list></#if>) {
        <#if (m.output.type)??><#if m.output.paramType??><#if m.output.paramType!='Void'>return </#if>${(interface.name?uncap_first)!""}${(suffix.biz?cap_first)!""}.${(m.name!"")?uncap_first}(<#if m.inputs?? && (m.inputs?size >0)><#list m.inputs as ip>${ip.name!""}<#if ip_has_next>,</#if> </#list></#if>);</#if></#if>
    }
            </#if>
            <#if m.httpMethod == "post">
    public <#if (m.output.type)??><#if m.output.paramType??><#if m.output.paramType=='Void'>void<#else>${m.output.returnType!""}</#if></#if></#if> ${(m.name!"")?uncap_first}(<#if m.inputs?? && (m.inputs?size >0)><#list m.inputs as ip><#if ip.paramType??>@RequestBody @Validated ${ip.paramType!""}</#if> ${ip.name!""}<#if ip_has_next>, </#if></#list></#if>) {
        <#if (m.output.type)??><#if m.output.paramType??><#if m.output.paramType!='Void'>return </#if>${(interface.name?uncap_first)!""}${(suffix.biz?cap_first)!""}.${(m.name!"")?uncap_first}(<#if m.inputs?? && (m.inputs?size >0)><#list m.inputs as ip>${ip.name!""}<#if ip_has_next>,</#if></#list></#if>);</#if></#if>
    }
            </#if>
            <#if m.httpMethod == "put">
    public <#if (m.output.type)??><#if m.output.paramType??><#if m.output.paramType=='Void'>void<#else>${m.output.returnType!""}</#if></#if></#if> ${(m.name!"")?uncap_first}(<#if m.inputs?? && (m.inputs?size >0)><#list m.inputs as ip><#if ip.paramType??>@Validated ${ip.paramType!""}</#if> ${ip.name!""}<#if ip_has_next>, </#if></#list></#if>) {
        <#if (m.output.type)??><#if m.output.paramType??><#if m.output.paramType!='Void'>return </#if>${(interface.name?uncap_first)!""}${(suffix.biz?cap_first)!""}.${(m.name!"")?uncap_first}(<#if m.inputs?? && (m.inputs?size >0)><#list m.inputs as ip>${ip.name!""}<#if ip_has_next>,</#if></#list></#if>);</#if></#if>
    }
            </#if>
            <#if m.httpMethod == "delete">
    public <#if (m.output.type)??><#if m.output.paramType??><#if m.output.paramType=='Void'>void<#else>${m.output.returnType!""}</#if></#if></#if> ${(m.name!"")?uncap_first}(<#if m.inputs?? && (m.inputs?size >0)><#list m.inputs as ip><#if ip.paramType??>@Validated ${ip.paramType!""}</#if> ${ip.name!""}<#if ip_has_next>, </#if></#list></#if>) {
        <#if (m.output.type)??><#if m.output.paramType??><#if m.output.paramType!='Void'>return </#if>${(interface.name?uncap_first)!""}${(suffix.biz?cap_first)!""}.${(m.name!"")?uncap_first}(<#if m.inputs?? && (m.inputs?size >0)><#list m.inputs as ip>${ip.name!""}<#if ip_has_next>,</#if></#list></#if>);</#if></#if>
    }
            </#if>
        <#else>
    @Operation(summary = "${m.descr!""}")
   &lt;#&ndash; @ApiOperation("${m.descr!""}")&ndash;&gt;
            <#if m.httpMethod == "get">
    @GetMapping("v1/${(api.name!"")?uncap_first}/${(interface.name!"")?uncap_first}/${(m.name!"")?uncap_first}")
    public <#if (m.output.type)??><#if m.output.paramType??><#if m.output.paramType=='Void'>void<#else>${m.output.returnType!""}</#if></#if></#if> ${(m.name!"")?uncap_first}(<#if m.inputs?? && (m.inputs?size >0)><#list m.inputs as ip><#if ip.paramType??>@Validated ${ip.paramType!""}</#if> ${ip.name!""}<#if ip_has_next>, </#if></#list></#if>) {
        <#if (m.output.type)??><#if m.output.paramType??><#if m.output.paramType!='Void'>return </#if>${(interface.name?uncap_first)!""}${(suffix.biz?cap_first)!""}.${(m.name!"")?uncap_first}(<#if m.inputs?? && (m.inputs?size >0)><#list m.inputs as ip>${ip.name!""}<#if ip_has_next>,</#if></#list></#if>);</#if></#if>
    }
            </#if>
            <#if m.httpMethod == "post">
    @PostMapping("v1/${(api.mapping!"")?uncap_first}/${(interface.name!"")?uncap_first}/${(m.name!"")?uncap_first}")
    public <#if (m.output.type)??><#if m.output.paramType??><#if m.output.paramType=='Void'>void<#else>${m.output.returnType!""}</#if></#if></#if> ${(m.name!"")?uncap_first}(<#if m.inputs?? && (m.inputs?size >0)><#list m.inputs as ip><#if ip.paramType??>@RequestBody @Validated ${ip.paramType!""}</#if> ${ip.name!""}<#if ip_has_next>,</#if> </#list></#if>) {
        <#if (m.output.type)??><#if m.output.paramType??><#if m.output.paramType!='Void'>return </#if>${(interface.name?uncap_first)!""}${(suffix.biz?cap_first)!""}.${(m.name!"")?uncap_first}(<#if m.inputs?? && (m.inputs?size >0)><#list m.inputs as ip>${ip.name!""}<#if ip_has_next>,</#if></#list></#if>);</#if></#if>
    }
            </#if>
            <#if m.httpMethod == "put">
    @PutMapping("v1/${(api.name!"")?uncap_first}/${(interface.name!"")?uncap_first}/${(m.name!"")?uncap_first}")
    public <#if (m.output.type)??><#if m.output.paramType??><#if m.output.paramType=='Void'>void<#else>${m.output.returnType!""}</#if></#if></#if> ${(m.name!"")?uncap_first}(<#if m.inputs?? && (m.inputs?size >0)><#list m.inputs as ip><#if ip.paramType??>@Validated ${ip.paramType!""}</#if> ${ip.name!""}<#if ip_has_next>,</#if> </#list></#if>) {
        <#if (m.output.type)??><#if m.output.paramType??><#if m.output.paramType!='Void'>return </#if>${(interface.name?uncap_first)!""}${(suffix.biz?cap_first)!""}.${(m.name!"")?uncap_first}(<#if m.inputs?? && (m.inputs?size >0)><#list m.inputs as ip>${ip.name!""}<#if ip_has_next>,</#if></#list></#if>);</#if></#if>
    }
            </#if>
            <#if m.httpMethod == "delete">
    @DeleteMapping("v1/${(api.name!"")?uncap_first}/${(interface.name!"")?uncap_first}/${(m.name!"")?uncap_first}")
    public <#if (m.output.type)??><#if m.output.paramType??><#if m.output.paramType=='Void'>void<#else>${m.output.returnType!""}</#if></#if></#if> ${(m.name!"")?uncap_first}(<#if m.inputs?? && (m.inputs?size >0)><#list m.inputs as ip><#if ip.paramType??>@Validated ${ip.paramType!""}</#if> ${ip.name!""}<#if ip_has_next>,</#if> </#list></#if>) {
        <#if (m.output.type)??><#if m.output.paramType??><#if m.output.paramType!='Void'>return </#if>${(interface.name?uncap_first)!""}${(suffix.biz?cap_first)!""}.${(m.name!"")?uncap_first}(<#if m.inputs?? && (m.inputs?size >0)><#list m.inputs as ip>${ip.name!""}<#if ip_has_next>,</#if></#list></#if>);</#if></#if>
    }
            </#if>
        </#if>

    </#list>
</#if>

}-->
