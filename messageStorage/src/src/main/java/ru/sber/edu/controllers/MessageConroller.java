package ru.sber.edu.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.sber.edu.modules.Message;
import ru.sber.edu.services.MessageService;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/messenger")
public class MessageConroller {

    MessageService messageService;

    public MessageConroller(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    void createMessage(@RequestBody Message newMessage) {
        messageService.save(newMessage);
    }

    @GetMapping("/history/full")
    List<Message> readHistoryFull(@RequestParam("fromUser") Integer fromUser,
                              @RequestParam("fromDate") Long fromDate) {
        return messageService.findByFromUserWithDate(fromUser, fromDate);
    }

    @GetMapping("history")
    List<Message> readHistory(@RequestParam("fromUser") Integer fromUser,
                              @RequestParam("toUser") Integer toUser,
                              @RequestParam("fromDate") Long fromDate) {
        System.out.println("getHistory fromUser = " + fromUser + " toUser " + toUser + " fromDate " + fromDate);
        return messageService.findByFromUserToUserWithDate(fromUser, toUser, fromDate);
    }
}
