package org.dows.mgc;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

//@SpringBootApplication
@ExtendWith(SpringExtension.class)
@SpringBootTest
@ContextConfiguration(classes = {TestConfig.class}) // SpringBootApplication 二选一
public class MindServiceTest {


    @Autowired
    private MindService mindService;

    @Test
    public void test() {
        String file = "dows-eaglee.gmind";
        String appId = mindService.init(file);
        mindService.generate(appId);
    }


}
