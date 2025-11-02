springdoc:
    api-docs:
        enabled: false
        path: /api-docs
    swagger-ui:
        #自定义swagger前端请求路径，输入http：127.0.0.1:8080/doc会自动重定向到swagger-ui.html页面
        path: /doc
        packages-to-scan: ${application.basePkg!""}.${pkgs.api!""}
    group-configs:
        - group: ${application.name!""}-admin
          packagesToScan: ${application.basePkg!""}.${pkgs.rest!""}
        - group: ${application.name!""}-tenant
          packagesToScan: ${application.basePkg!""}.${pkgs.rest!""}.tenant
        - group: ${application.name!""}-user
          packagesToScan: ${application.basePkg!""}.${pkgs.rest!""}.user

---
#开发环境
spring:
    config:
        activate:
            on-profile: dev
springdoc:
    api-docs:
        enabled: true

---
#测试环境
spring:
    config:
        activate:
            on-profile: sit
springdoc:
    api-docs:
        enabled: true

---
#验收环境
spring:
    config:
        activate:
            on-profile: uat
springdoc:
    api-docs:
        enabled: true

---
#生产环境
spring:
    config:
        activate:
            on-profile: pro
springdoc:
    api-docs:
        enabled: false