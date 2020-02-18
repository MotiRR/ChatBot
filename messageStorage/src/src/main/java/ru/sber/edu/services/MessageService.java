package ru.sber.edu.services;

import ru.sber.edu.modules.Message;

import java.util.Date;
import java.util.List;

public interface MessageService {

    void save(Message message);
    List<Message> findByFromUserWithDate(int id, Long date);
    List<Message> findByFromUserToUserWithDate(int idFromUser, int idToUser, Long date);
}
