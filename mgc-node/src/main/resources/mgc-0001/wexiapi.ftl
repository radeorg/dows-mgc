dows:
  sdk:
    weixin:
<#list beanSchemas as bs>
    <#list bs.methods as method>
      "${bs.pkg!""}.${bs.code?cap_first!""}.${method.code?uncap_first!""}": ${method.url!""}
    </#list>
</#list>