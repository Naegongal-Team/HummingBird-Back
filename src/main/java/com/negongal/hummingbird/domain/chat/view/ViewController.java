package com.negongal.hummingbird.domain.chat.view;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
@RequestMapping("/view")
public class ViewController {

    private final ViewChatService viewChatService;

    @GetMapping("/chat/room")
    public String rooms(Model model) {
        return "/chat/room";
    }

    @Operation(hidden = true)
    @GetMapping("/chat/room/list")
    @ResponseBody
    public List<ViewChatRoom> getRooms() {
        return viewChatService.findAllRoom();
    }

    @GetMapping("/chat/room/enter/{roomId}")
    public String roomDetail(Model model, @PathVariable String roomId) {
        model.addAttribute("roomId", roomId);
        return "/chat/roomdetail";
    }

    @Operation(hidden = true)
    @GetMapping("/chat/room/{roomId}")
    @ResponseBody
    public ViewChatRoom getRoom(@PathVariable String roomId) {
        return viewChatService.findByRoomId(roomId);
    }
}
