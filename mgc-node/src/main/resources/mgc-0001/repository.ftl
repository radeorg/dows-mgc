package ${pkg!""};

import org.dows.framework.crud.mybatis.MybatisRepositoryImpl;
import ${basePkg!""}.${pkgs.entity!""}.${name?cap_first!""}${suffix.entity!""};
import ${basePkg!""}.${pkgs.mapper!""}.${name?cap_first!""}${suffix.mapper!""};
import org.springframework.stereotype.Service;
<#if imports??>
    <#list imports as i>
        import ${i!""};
    </#list>
</#if>

/**
 * @description ${comment!""}
 * @author ${author.name!""}
 * @email ${author.email!""}
 * @since ${.now}
 */
@Service
public class ${name?cap_first!""}${suffix.repository!""} extends MybatisRepositoryImpl<${name?cap_first!""}${suffix.mapper!""},${name?cap_first!""}${suffix.entity!""}> {

}