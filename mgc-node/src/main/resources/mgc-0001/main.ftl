package ${pkg!""};

<#if imports??>
    <#list imports as i>
import ${i!""};
    </#list>
</#if>
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.Map;
import java.util.Date;
import java.math.BigDecimal;

/**
 * @description ${comment!""}
 * @author ${author.name!""}
 * @email ${author.email!""}
 * @since ${.now}
 */
@SpringBootApplication
public class ${name?cap_first!""}${suffix.app!""}{
    public static void main(String[] args) {
        SpringApplication.run(${name?cap_first!""}${suffix.app!""}.class, args);
    }
}
