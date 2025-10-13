package org.dows.mgc.rest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChatController {

    @GetMapping(value = "/chat")
    public String demo1(Model model) {
        return "chat";
    }
}
