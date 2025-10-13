package org.dows.mgc.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.dows.mgc.dto.ChatRequest;
import org.dows.mgc.service.MgcService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.Map;

@RequestMapping("/mgc")
@RestController
@RequiredArgsConstructor
public class MgcController {

    private final MgcService mgcService;


    @GetMapping(value = "/buildRequirement", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> buildRequirement(@RequestParam String text) {
        return mgcService.buildRequirement(text).concatWith(Flux.just("<COMPLETE>"))
                .map(s -> "[" + s + "]")
                .doOnNext(System.out::println);
    }

    @GetMapping(value = "/buildClass", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> buildClass(@RequestParam String text) {
        return mgcService.buildClass(text).concatWith(Flux.just("<COMPLETE>"))
                .map(s -> "[" + s + "]")
                .doOnNext(System.out::println);
    }

    @PostMapping("/buildWeb")
    public Map<String, Object> buildWeb(@RequestBody @Valid ChatRequest chatRequest) {
        return mgcService.buildWeb(chatRequest.getText());
    }
}
