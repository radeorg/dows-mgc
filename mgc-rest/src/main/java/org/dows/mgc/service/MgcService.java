package org.dows.mgc.service;

import reactor.core.publisher.Flux;

import java.util.Map;

public interface MgcService {

    Flux<String> buildRequirement(String text);

    Flux<String> buildClass(String text);

    Map<String, Object> buildWeb(String text);
}
