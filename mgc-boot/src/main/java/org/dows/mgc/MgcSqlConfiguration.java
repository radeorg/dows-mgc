package org.dows.mgc;

import org.dows.mgc.sql.DataSourceHelper;
import org.dows.mgc.sql.web.ModelWebService;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

import javax.sql.DataSource;
import java.util.Map;

@AutoConfiguration
public class MgcSqlConfiguration {

    @Bean
    public DataSourceHelper dataSourceHelper(DataSource dataSource) {
        return new DataSourceHelper(dataSource);
    }

    @Bean
    public ModelWebService entityWebService(DataSourceHelper dataSourceHelper) {
        return new ModelWebService(dataSourceHelper);
    }

    @Bean("mgcSqlWebRouter")
    public RouterFunction<ServerResponse> mgcSqlWebRouter(ModelWebService modelWebService) {
        return RouterFunctions.route().GET("/entity/view/{id}", request -> {
                    String id = request.pathVariable("id");
                    String contextPath = request.requestPath().contextPath().value();
                    return ServerResponse.ok().contentType(MediaType.TEXT_HTML).body(modelWebService.view(id, contextPath));
                }).POST("/entity/select/{id}", request -> {
                    String id = request.pathVariable("id");
                    Map<String, Object> params = request.body(new ParameterizedTypeReference<>() {
                    });
                    return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(Result.success(modelWebService.select(id, params)));
                }).POST("/entity/save/{id}", request -> {
                    String id = request.pathVariable("id");
                    Map<String, Object> params = request.body(new ParameterizedTypeReference<>() {
                    });
                    return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(Result.success(modelWebService.save(id, params)));
                })
                .POST("/entity/getById/{id}", request -> {
                    String id = request.pathVariable("id");
                    Map<String, Object> params = request.body(new ParameterizedTypeReference<>() {
                    });
                    return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(Result.success(modelWebService.getById(id, params)));
                })
                .POST("/entity/deleteByIds/{id}", request -> {
                    String id = request.pathVariable("id");
                    Map<String, Object> params = request.body(new ParameterizedTypeReference<>() {
                    });
                    return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(Result.success(modelWebService.deleteByIds(id, params)));
                })
                .build();
    }
}
