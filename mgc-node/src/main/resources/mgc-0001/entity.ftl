package ${pkg!""};

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.*;
import java.math.BigDecimal;

/**
 * ${comment!""}
 * @author ${author.name!""}
 * @email ${author.email!""}
 * @since ${.now}
 */
@SuppressWarnings("serial")
@Data
@ToString
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "${name?cap_first!""}${suffix.entity!""}", title = "${comment!""}")
@TableName("${name?replace("([a-z])([A-Z]+)","$1_$2","r")?lower_case}")
public class ${name?cap_first!""}${suffix.entity!""} {
<#if fields?? && (fields?size >0)>
    <#list fields as field >
        <#if field.nullable??>
    @NotNull(message = "${field.comment!""}[${field.name!""}]")
        </#if>
        <#if field.length??>
    <#--@Size(<#if field.size.min??>min = ${field.size.min!""}</#if>, <#if field.size.max??>max = ${field.size.max!""}, </#if>message = "${field.descr!""}[${field.name!""}]<#if field.size.descr?? && field.size.descr != "">${field.size.descr!""}<#else >长度有误,允许的大小值为:${field.size.value!""}</#if>")-->
        </#if>
        <#if field.type?has_content>
    @Schema(title = "${field.comment!""}")
    private ${(field.type!"")?cap_first} ${field.name!""};
        </#if>
    </#list>
</#if>
}