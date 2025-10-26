package org.dows.mgc.rest;

import org.dows.mgc.common.BaseResponse;
import org.dows.mgc.common.ResultUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class HealthController {

    @GetMapping("/")
    public BaseResponse<String> healthCheck() {

        return ResultUtils.success("OK");
    }
}
