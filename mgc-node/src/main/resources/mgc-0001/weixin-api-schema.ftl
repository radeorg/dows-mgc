dows:
  sdk:
    weixin:
      api-beans:
        <#list beanSchemas as bean>
        - module: ${bean.module.name!""}
          pkg: ${bean.pkg!""}
          name: ${bean.name!""}
          code: ${bean.code!""}
          descr: ${bean.descr!""}
          methods:
            <#list bean.methods as method>
            - name: ${method.name!""}
              code: ${method.code!""}
              overview: ${method.overview!""}
              descr: ${method.descr!""}
              docUrl: ${method.docUrl!""}
              url: ${method.url!""}
              httpMethod: ${method.httpMethod!""}
            </#list>
        </#list>
