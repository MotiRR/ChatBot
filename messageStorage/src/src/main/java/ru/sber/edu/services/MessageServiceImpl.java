package ru.sber.edu.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sber.edu.modules.*;
import ru.sber.edu.repository.MessageDAOImpl;
import java.util.Date;
import java.util.List;

@Service("messageService")
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageDAOImpl messageDAO;

    @Override
    public void save(Message message) {
         messageDAO.save(message);
    }

    @Override
    public List<Message> findByFromUserWithDate(int id, Long date){
        return messageDAO.findByFromUserWithDate(id, date);
    }

    @Override
    public List<Message> findByFromUserToUserWithDate(int idFromUser, int idToUser, Long date) {
        return messageDAO.findByFromUserToUserWithDate(idFromUser, idToUser, date);
    }


}
