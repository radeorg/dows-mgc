package ${pkg!""};

import org.apache.ibatis.annotations.Mapper;
import org.dows.framework.crud.mybatis.MybatisMapper;
import ${basePkg!""}.${pkgs.entity!""}.${name?cap_first!""}${suffix.entity!""};
<#if imports??>
    <#list imports as i>
        import ${i!""};
    </#list>
</#if>


/**
 * ${comment!""}
 *
 * @author ${author.name!""}
 * @email ${author.email!""}
 * @since ${.now}
 */
@Mapper
public interface ${name?cap_first!""}${suffix.mapper!""} extends MybatisMapper<${name?cap_first!""}${suffix.entity!""}> {

}