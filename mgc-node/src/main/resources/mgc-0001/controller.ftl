package ${pkg!""};

<#if imports??>
    <#list imports as i>
import ${i!""};
    </#list>
</#if>
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.dows.framework.crud.mybatis.MybatisRest;
import ${basePkg!""}.${pkgs.entity!"entity"}.${name?cap_first!""}${suffix.entity?cap_first!""};
import ${basePkg!""}.${pkgs.repository!"repository"}.${name?cap_first!""}${suffix.repository?cap_first!""};
import org.springframework.web.bind.annotation.RestController;

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
public class ${name?cap_first!""}${suffix.controller!""}  implements MybatisRest<${name?cap_first!""}${suffix.entity?cap_first!""},${name?cap_first!""}${suffix.repository?cap_first!""}>{

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
