package org.dows.mgc;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;

@TestConfiguration
@ComponentScan(basePackages = "org.dows.mgc")
public class TestConfig {
//    @Bean
//    public MindNodeService mindNodeService(ResourceLoader resourceLoader) {
//        MindNodeService service = new MindNodeService(resourceLoader);
//        // Set resourceLoader using reflection or setter if needed
//        try {
//            java.lang.reflect.Field field = MindNodeService.class.getDeclaredField("resourceLoader");
//            field.setAccessible(true);
//            field.set(service, resourceLoader);
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to set resourceLoader", e);
//        }
//        return service;
//    }
}