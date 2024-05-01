package com.example.waggle.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/chat")
@Controller
public class ChatController {

    @GetMapping("/rooms")
    public String rooms() {
        return "chat/room";
    }

    @GetMapping("/rooms/{roomId}")
    public String roomDetail(Model model, @PathVariable String roomId) {
        model.addAttribute("roomId", roomId);
        return "chat/roomdetail";
    }

}
