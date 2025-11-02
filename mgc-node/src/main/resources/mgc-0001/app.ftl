<#if application?? >
server:
    port: ${application.port!"8080"}
    undertow:
        buffer-size: 1024
        direct-buffers: true
        threads:
            io: 4
            worker: 32
    <#if application.contextPath??>
    servlet:
        context-path: ${application.contextPath!"/"}
    </#if>

spring:
    application:
        name: ${application.name!""}
    profiles:
        <#if includes??>
        include: <#list includes as include>${include!""}<#if include_has_next>,</#if></#list>
        </#if>
        active: ${application.env!"dev"}
</#if>



<#if configInfo?? && configInfo.fileName == "redis">
spring:
    redis:
        #数据库索引
        database: <#noparse>${REDIS_DB:</#noparse>0}
        #host: <#noparse>${REDIS_HOST:</#noparse>10.251.76.39}
        #port: <#noparse>${REDIS_PORT:7001}</#noparse>
        #password: <#noparse>${REDIS_PWD:</#noparse>}
        #password: <#noparse>${REDIS_PWD:</#noparse>mypassword}
        #连接超时时间
        timeout: 30000
        client-type: jedis
        #    lettuce:
        #      pool:
        #        max-idle: 8
        #        min-idle: 0
        #        max-active: 8
        #        max-wait: -1ms

    <#if configInfo.env == "prod">
---
#生产环境
spring:
    config:
        activate:
        on-profile: pro
    redis:
        #数据库索引
        host: <#noparse>${REDIS_HOST:</#noparse>101.35.194.46}
        port: <#noparse>${REDIS_PORT:</#noparse>}
        password: <#noparse>${REDIS_PWD:</#noparse>}

    </#if>
    <#if configInfo.env == "test">
---
#测试环境
spring:
    config:
        activate:
            on-profile: test
    redis:
        #    测试环境集群：host: redis-sit.mid.io，# 测试环境redis 有问题
        host: <#noparse>${REDIS_HOST:</#noparse>101.35.194.46}
        port: 6379
        jedis:
            pool:
                max-active: 10000
                max-wait: -1
                max-idle: 100
                min-idle: 20
    </#if>
    <#if configInfo.env == "dev">
---
#开发环境
spring:
    config:
        activate:
            on-profile: dev
    redis:
        #数据库索引
        host: <#noparse>${REDIS_HOST:</#noparse>101.35.194.46}
        password: <#noparse>${REDIS_PWD:</#noparse>}
        #    cluster:
        #      max-redirects: 3
        #      nodes:
        #        - 10.251.76.39:7001
        #        - 10.251.76.39:7004
        #        - 10.251.76.21:7002
        #        - 10.251.76.21:7005
        #        - 10.251.76.22:7003
        #        - 10.251.76.22:7006
        jedis:
            pool:
                max-active: 500
                max-idle: 10
    </#if>
</#if>
<#if configInfo?? && configInfo.fileName == "rocketmq">
    rabbitmq:
    host:
</#if>
<#if configInfo?? && configInfo.fileName == "mysql">
spring:
    datasource:
        driverClassName: com.mysql.cj.jdbc.Driver
        type: com.zaxxer.hikari.HikariDataSource
        #    schema: classpath:form-schema.sql
        #    data: classpath:form-data.sql
        hikari:
            maximum-pool-size: 100
            minimum-idle: 50
            connectionTimeout: 6000

    <#if configInfo.env == "prod">
#虽然两种都可以运行成功，但下面这种写法才是2.4版本的最好的写法，原因是2.4版本中官方将配置文件的加载进行了更新，
#所以如果使用的是2.4版本推荐用下面的这种写法，至于原因就是为了提升对 Kubernetes 的原生支持而作的修改
#spring:
#  profiles: pro
---
#生产环境
spring:
    config:
        activate:
            on-profile: prod
    datasource:
        url: jdbc:mysql://101.35.194.46:3306/nezhadb?serverTimezone=GMT%2B8&useSSL=false&characterEncoding=utf8&connectTimeout=3000&rewriteBatchedStatements=true&allowPublicKeyRetrieval=true
        username: nezha
        password: 7fFsECXmpjX5y3Xw
    </#if>
    <#if configInfo.env == "dev">
---
#开发环境
spring:
    #  profiles: dev
    config:
        activate:
            on-profile: dev
    datasource:
        url: jdbc:mysql://101.35.194.46:3306/nezhadb?serverTimezone=GMT%2B8&autoReconnect=true&allowMultiQueries=true&useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false
        username: nezha
        password: 7fFsECXmpjX5y3Xw
    </#if>
    <#if configInfo.env == "local">
---
#本地环境
spring:
    config:
        activate:
            on-profile: local
    datasource:
        url: jdbc:mysql://101.35.194.46:3306/nezhadb?serverTimezone=GMT%2B8&autoReconnect=true&allowMultiQueries=true&useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false
        username: admin
        password: 7fFsECXmpjX5y3X
    </#if>
</#if>


