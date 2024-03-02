package com.example.waggle.web.controller;

import com.example.waggle.domain.chat.entity.Room;
import com.example.waggle.domain.chat.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RequiredArgsConstructor
@Controller
@Slf4j
public class IndexController implements ErrorController {

    private final RoomService roomService;

    @GetMapping({"/", "/error"})
    public String index() {
        return "index.html";
    }

    @GetMapping("/chat/room/enter/{roomId}")
    public String chat(Model model, @PathVariable String roomId) {
        model.addAttribute("roomId", roomId);
        return "chat/roomdetail";
    }

    @GetMapping("/chat/room/{roomId}")
    public String enterChatRoom(@PathVariable String roomId, Model model) {
        Room chatRoom = roomService.getRoom(roomId);
        model.addAttribute("room", chatRoom);
        return "chat/roomdetail";
    }

    @GetMapping("/room")
    public String room() {
        return "chat/room";
    }

}
