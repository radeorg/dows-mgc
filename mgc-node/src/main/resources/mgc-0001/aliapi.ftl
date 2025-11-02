dows:
  sdk:
    alipay:
<#list beanSchemas as bs>
    <#list methods as method>
      "${bs.pkg!""}${bs.name?cap_first!""}.${method.name?uncap_first!""}": ${method.url!""}
    </#list>
</#list>