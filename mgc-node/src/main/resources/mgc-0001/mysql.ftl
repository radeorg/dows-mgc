<#if mysql??>
spring:
    datasource:
        driverClassName: org.mariadb.jdbc.Driver
        type: org.mariadb.jdbc.MariaDbDataSource
        #    schema: classpath:form-schema.sql
        #    data: classpath:form-data.sql
        hikari:
            connection-timeout: 60000
            minimum-idle: 5
            maximum-pool-size: 10
            idle-timeout: 300000
            max-lifetime: 1200000
            auto-commit: true
            connection-test-query: SELECT 1
            validation-timeout: 3000
            read-only: false
            login-timeout: 5

    <#list mysql as auth>
---
spring:
    config:
        activate:
            on-profile: ${auth.env!"dev"}
    datasource:
        url: jdbc:mariadb://${auth.ip!"127.0.0.1"}:${auth.port!"3306"}/wes_all?serverTimezone=GMT%2B8&autoReconnect=true&allowMultiQueries=true&useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false
        username: ${auth.username!"root"}
        password: ${auth.pwd!"123456"}
    </#list>
</#if>