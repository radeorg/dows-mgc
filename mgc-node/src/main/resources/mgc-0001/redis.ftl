<#if redis??>
spring:


    <#list redis as auth>
---
spring:
    config:
        activate:
            on-profile: ${auth.env!"dev"}

    </#list>
</#if>