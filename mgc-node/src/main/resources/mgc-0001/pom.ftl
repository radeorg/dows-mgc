<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <#if parent??>
<parent>
        <artifactId>${parent.artifactId!""}</artifactId>
        <groupId>${parent.groupId!""}</groupId>
        <version>${parent.version!""}</version>
    </parent>
    </#if>
    <artifactId>${artifactId!""}</artifactId>
    <#if modules ?? && (modules?size >0)>
    <groupId>${groupId!""}</groupId>
    <version>${version!""}</version>
    <packaging>pom</packaging>

    <modules>
        <#list modules as module>
        <module>${module.artifactId!""}</module>
        </#list>
    </modules>

    <dependencyManagement>
        <dependencies>
            <#list modules as module>
            <dependency>
                <artifactId>${module.artifactId!""}</artifactId>
                <groupId>${module.groupId!""}</groupId>
                <version><#noparse>${project.version}</#noparse></version>
            </dependency>

            </#list>
            <#if dependencies??>
            <#list dependencies as dependency>
            <dependency>
                <artifactId>${dependency.artifactId!""}</artifactId>
                <groupId>${dependency.groupId!""}</groupId>
                <version>${dependency.version!""}</version>
            </dependency>
            </#list>
            </#if>
        </dependencies>
    </dependencyManagement>
    </#if>

    <dependencies>
    <#if dependencies??>
        <#list dependencies as dependency>
        <dependency>
            <artifactId>${dependency.artifactId}</artifactId>
            <groupId>${dependency.groupId}</groupId>
            <#if dependency.version??>
            <version>${dependency.version!""}</version>
            </#if>
        </dependency>
        </#list>

        <#if artifactId?contains("api")>
        <dependency>
            <groupId>org.dows.framework</groupId>
            <artifactId>framework-api</artifactId>
        </dependency>
        </#if>
        <#if artifactId?contains("pojo")>
        <dependency>
            <groupId>org.dows.framework</groupId>
            <artifactId>framework-api</artifactId>
        </dependency>
        </#if>
        <#if artifactId?contains("repository")>
        <dependency>
            <groupId>org.dows.framework</groupId>
            <artifactId>crud-mybatis</artifactId>
        </dependency>
        </#if>
        <#if artifactId?contains("app")>
        <dependency>
            <groupId>com.zaxxer</groupId>
            <artifactId>HikariCP</artifactId>
        </dependency>
        <dependency>
            <groupId>org.mariadb.jdbc</groupId>
            <artifactId>mariadb-java-client</artifactId>
        </dependency>
        <!--jackson-->
        <dependency>
            <groupId>com.fasterxml.jackson.datatype</groupId>
            <artifactId>jackson-datatype-jsr310</artifactId>
        </dependency>

        <!--异步日志-->
        <dependency>
            <groupId>com.lmax</groupId>
            <artifactId>disruptor</artifactId>
        </dependency>

        <!--api 接口文档-->
        <dependency>
            <groupId>org.springdoc</groupId>
            <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
        </dependency>

        <!--web -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-undertow</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        </#if>
    </#if>
    </dependencies>

    <#if artifactId?contains("app")>
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
    </#if>
</project>
<#--<?xml version="1.0" encoding="UTF-8"?>-->
<#--<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"-->
<#--         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">-->
<#--    <#if parent??>-->
<#--<parent>-->
<#--        <artifactId>${parent.artifactId!""}</artifactId>-->
<#--        <groupId>${parent.groupId!""}</groupId>-->
<#--        <version>${parent.version!""}</version>-->
<#--    </parent>-->
<#--    <#else >-->
<#--<parent>-->
<#--        <artifactId>dows-parent</artifactId>-->
<#--        <groupId>org.dows.framework</groupId>-->
<#--        <version>20220701-SNAPSHOT</version>-->
<#--    </parent>-->
<#--    </#if>-->

<#--    <modelVersion>4.0.0</modelVersion>-->
<#--    <artifactId>${artifactId!""}</artifactId>-->
<#--    <#if !parent??>-->
<#--    <groupId>${groupId!""}</groupId>-->
<#--    <version>${version!""}</version>-->
<#--    </#if>-->
<#--    <#if modules ?? && (modules?size >0)>-->
<#--    <packaging>pom</packaging>-->
<#--    </#if>-->


<#--    <#if modules ?? && (modules?size >0)>-->
<#--    <modules>-->
<#--        <#list modules as module>-->
<#--        <module>${module!""}</module>-->
<#--        </#list>-->
<#--    </modules>-->
<#--    </#if>-->

<#--    <#if !parent??>-->
<#--    <dependencyManagement>-->
<#--        <dependencies>-->
<#--        <#list modules as module>-->
<#--            <dependency>-->
<#--                <artifactId>${module!""}</artifactId>-->
<#--                <groupId>${groupId!""}</groupId>-->
<#--                <version>${version!""}</version>-->
<#--            </dependency>-->

<#--        </#list>-->
<#--        </dependencies>-->
<#--    </dependencyManagement>-->
<#--    </#if>-->


<#--    <dependencies>-->
<#--        <#if !parent??>-->
<#--        <dependency>-->
<#--            <groupId>org.projectlombok</groupId>-->
<#--            <artifactId>lombok</artifactId>-->
<#--        </dependency>-->
<#--        </#if>-->
<#--        <#if artifactId?contains("api")>-->
<#--        <dependency>-->
<#--            <groupId>io.swagger</groupId>-->
<#--            <artifactId>swagger-annotations</artifactId>-->
<#--        </dependency>-->
<#--        <dependency>-->
<#--            <groupId>jakarta.validation</groupId>-->
<#--            <artifactId>jakarta.validation-api</artifactId>-->
<#--        </dependency>-->
<#--        <dependency>-->
<#--            <groupId>org.springframework</groupId>-->
<#--            <artifactId>spring-web</artifactId>-->
<#--        </dependency>-->
<#--        <dependency>-->
<#--            <groupId>org.dows.framework</groupId>-->
<#--            <artifactId>framework-api</artifactId>-->
<#--        </dependency>-->
<#--        <dependency>-->
<#--            <groupId>io.swagger.core.v3</groupId>-->
<#--            <artifactId>swagger-annotations-jakarta</artifactId>-->
<#--            <version>2.2.8</version>-->
<#--        </dependency>-->
<#--        </#if>-->
<#--        <#if artifactId?contains("biz")>-->
<#--        <dependency>-->
<#--            <groupId>${groupId!""}</groupId>-->
<#--            <artifactId>${artifactId?substring(0,artifactId?index_of("-"))}-crud</artifactId>-->
<#--        </dependency>-->
<#--        </#if>-->
<#--        <#if artifactId?contains("rest")>-->
<#--        <dependency>-->
<#--            <groupId>${groupId!""}</groupId>-->
<#--            <artifactId>${artifactId?substring(0,artifactId?index_of("-"))}-biz</artifactId>-->
<#--        </dependency>-->

<#--        <dependency>-->
<#--            <groupId>org.springframework</groupId>-->
<#--            <artifactId>spring-webmvc</artifactId>-->
<#--        </dependency>-->
<#--        </#if>-->
<#--        <#if artifactId?contains("crud")>-->
<#--        <dependency>-->
<#--            <groupId>${groupId!""}</groupId>-->
<#--            <artifactId>${artifactId?substring(0,artifactId?index_of("-"))}-api</artifactId>-->
<#--        </dependency>-->
<#--        <dependency>-->
<#--            <groupId>${groupId!""}</groupId>-->
<#--            <artifactId>${artifactId?substring(0,artifactId?index_of("-"))}-pojo</artifactId>-->
<#--        </dependency>-->
<#--        </#if>-->
<#--        <#if artifactId?contains("app")>-->
<#--        <dependency>-->
<#--            <groupId>${groupId!""}</groupId>-->
<#--            <artifactId>${artifactId?substring(0,artifactId?index_of("-"))}-rest</artifactId>-->
<#--        </dependency>-->
<#--        <dependency>-->
<#--            <groupId>${groupId!""}</groupId>-->
<#--            <artifactId>${artifactId?substring(0,artifactId?index_of("-"))}-config</artifactId>-->
<#--        </dependency>-->
<#--        </#if>-->
<#--        <#if artifactId?contains("boot")>-->
<#--        <dependency>-->
<#--            <groupId>${groupId!""}</groupId>-->
<#--            <artifactId>${artifactId?substring(0,artifactId?index_of("-"))}-biz</artifactId>-->
<#--        </dependency>-->
<#--        <dependency>-->
<#--            <groupId>${groupId!""}</groupId>-->
<#--            <artifactId>${artifactId?substring(0,artifactId?index_of("-"))}-config</artifactId>-->
<#--        </dependency>-->
<#--        </#if>-->
<#--        <#if artifactId?contains("config")>-->

<#--        <dependency>-->
<#--            <groupId>${groupId!""}</groupId>-->
<#--            <artifactId>${artifactId?substring(0,artifactId?index_of("-"))}-api</artifactId>-->
<#--        </dependency>-->

<#--        <dependency>-->
<#--            <groupId>org.dows.framework</groupId>-->
<#--            <artifactId>crud-mybatis</artifactId>-->
<#--        </dependency>-->

<#--        <dependency>-->
<#--            <groupId>com.zaxxer</groupId>-->
<#--            <artifactId>HikariCP</artifactId>-->
<#--        </dependency>-->
<#--        <dependency>-->
<#--            <groupId>mysql</groupId>-->
<#--            <artifactId>mysql-connector-java</artifactId>-->
<#--        </dependency>-->
<#--        <dependency>-->
<#--            <groupId>org.mariadb.jdbc</groupId>-->
<#--            <artifactId>mariadb-java-client</artifactId>-->
<#--        </dependency>-->

<#--        <dependency>-->
<#--            <groupId>com.fasterxml.jackson.datatype</groupId>-->
<#--            <artifactId>jackson-datatype-jsr310</artifactId>-->
<#--        </dependency>-->

<#--        <!--异步日志&ndash;&gt;-->
<#--        <dependency>-->
<#--            <groupId>com.lmax</groupId>-->
<#--            <artifactId>disruptor</artifactId>-->
<#--        </dependency>-->

<#--        <!--api 接口文档&ndash;&gt;-->
<#--        <dependency>-->
<#--            <groupId>io.springfox</groupId>-->
<#--            <artifactId>springfox-boot-starter</artifactId>-->
<#--        </dependency>-->
<#--        <dependency>-->
<#--            <groupId>com.github.xiaoymin</groupId>-->
<#--            <artifactId>knife4j-spring-boot-starter</artifactId>-->
<#--        </dependency>-->

<#--        <!--web &ndash;&gt;-->
<#--        <dependency>-->
<#--            <groupId>org.springframework.boot</groupId>-->
<#--            <artifactId>spring-boot-starter-web</artifactId>-->
<#--        </dependency>-->
<#--        <dependency>-->
<#--            <groupId>org.springframework.boot</groupId>-->
<#--            <artifactId>spring-boot-starter-undertow</artifactId>-->
<#--        </dependency>-->
<#--        <dependency>-->
<#--            <groupId>org.springframework.boot</groupId>-->
<#--            <artifactId>spring-boot-starter-actuator</artifactId>-->
<#--        </dependency>-->
<#--        </#if>-->
<#--        &lt;#&ndash;<#if dependencies ?? && (dependencies?size >0)>-->
<#--            <#list dependencies as dependencie>-->
<#--                <dependency>-->
<#--                    <artifactId>${(dependencie.artifactId)!""}</artifactId>-->
<#--                    <groupId>${(dependencie.groupId)!""}</groupId>-->
<#--                    <#if dependencie.version??>-->
<#--                        <version>${(dependencie.version)!""}</version>-->
<#--                    </#if>-->
<#--                </dependency>-->
<#--            </#list>-->
<#--        </#if>&ndash;&gt;-->
<#--    </dependencies>-->

<#--    <build>-->
<#--        <#if artifactId?contains("-app")>-->
<#--        <resources>-->
<#--            <resource>-->
<#--                <directory>src/main/java</directory>-->
<#--                <includes>-->
<#--                    <include>**/*.yml</include>-->
<#--                    <include>**/*.properties</include>-->
<#--                    <include>**/*.xml</include>-->
<#--                    <include>**/*.sql</include>-->
<#--                </includes>-->
<#--                <filtering>true</filtering>-->
<#--            </resource>-->
<#--            <resource>-->
<#--                <directory>src/main/resources</directory>-->
<#--                <includes>-->
<#--                    <include>**/*.yml</include>-->
<#--                    <include>**/*.properties</include>-->
<#--                    <include>**/*.xml</include>-->
<#--                    <include>**/*.sql</include>-->
<#--                </includes>-->
<#--                <filtering>true</filtering>-->
<#--            </resource>-->
<#--        </resources>-->
<#--        </#if>-->
<#--        <plugins>-->
<#--            <#if artifactId?contains("-app")>-->
<#--            <plugin>-->
<#--                <groupId>org.springframework.boot</groupId>-->
<#--                <artifactId>spring-boot-maven-plugin</artifactId>-->
<#--            </plugin>-->

<#--            <plugin>-->
<#--                <groupId>org.apache.maven.plugins</groupId>-->
<#--                <artifactId>maven-resources-plugin</artifactId>-->
<#--                <configuration>-->
<#--                    <delimiters>@</delimiters>-->
<#--                    <useDefaultDelimiters>false</useDefaultDelimiters>-->
<#--                </configuration>-->
<#--            </plugin>-->
<#--            </#if>-->
<#--        </plugins>-->
<#--    </build>-->

<#--</project>-->
