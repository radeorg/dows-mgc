package ${pkg!""};

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
<#--import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;-->
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.*;
import java.math.BigDecimal;

/**
 * @description ${comment!""}
 * @author ${author.name!""}
 * @email  ${author.email!""}
 * @since  ${.now}
 */
<#if extendClass??>
@EqualsAndHashCode(callSuper = true)
</#if>
@Data
@NoArgsConstructor
<#--@Schema(name = "${parameter.type} 对象", title = "${(parameter.descr!"")}")-->
<#--@ApiModel(value = "${parameter.type} 对象", description = "${(parameter.descr!"")}")-->
public class ${clazz!""}{
<#if fields?? && (fields?size >0)>
    <#list fields as field >
        <#if field.notnull??>
    @NotNull(message = "${field.descr!""}[${field.name!""}]<#if field.notnull.descr?? && field.notnull.descr != "">${field.notnull.descr!""}<#else >不能为空</#if>")
        </#if>
        <#if field.size??>
    @Size(<#if field.size.min??>min = ${field.size.min!""}</#if>, <#if field.size.max??>max = ${field.size.max!""}, </#if>message = "${field.descr!""}[${field.name!""}]<#if field.size.descr?? && field.size.descr != "">${field.size.descr!""}<#else >长度有误,允许的大小值为:${field.size.value!""}</#if>")
        </#if>
        <#if field.fieldType?has_content>
<#--    @ApiModelProperty("${field.descr!""}")-->
    @Schema(title = "${field.descr!""}")
    private ${(field.fieldType!"")?cap_first} ${field.name!""};
        </#if>
    </#list>

</#if>
}
