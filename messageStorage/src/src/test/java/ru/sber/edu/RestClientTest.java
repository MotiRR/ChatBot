package ru.sber.edu;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.util.UriComponentsBuilder;
import ru.sber.edu.modules.Message;
import ru.sber.edu.modules.MessageTypes;
import java.util.Arrays;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:test.properties")
public class RestClientTest {

    @LocalServerPort
    private int port;

    private static final Logger logger = LoggerFactory.getLogger(RestClientTest.class);
    private static final String URL_POST_MESSAGE = "/messenger";
    private static final String URL_GET_MESSAGE_HISTORY_FULL = "/messenger/history/full";
    private static final String URL_GET_MESSAGE_HISTORY = "/messenger/history";

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    public void testCreate() {
        logger.info("--> Testing create message");
        Message messageNew = new Message();
        messageNew.setFromUser(1);
        messageNew.setToUser(2);
        messageNew.setMessage("This is message Rest for");
        messageNew.setType(MessageTypes.TEXT);
        messageNew.setTime(100000L);
        messageNew = restTemplate.postForObject(createURLWithPort(URL_POST_MESSAGE), messageNew, Message.class);
        logger.info("Message created successfully: " + messageNew);
    }

    @Test
    public void testFindByIdFromUser() {
        logger.info("--> Testing message by id from user: 1 and time = 100L");

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(createURLWithPort(URL_GET_MESSAGE_HISTORY_FULL))
          .queryParam("fromUser", 1)
          .queryParam("fromDate", 100L);

        Message[] message = restTemplate.getForObject(builder.toUriString(), Message[].class);
        int len = message.length;
        assertTrue(len == 1);
        listMessages(message);
    }

    @Test
    public void testFindByIdFromUserToUser() {
        logger.info("--> Testing message by id from user: 1, to user: 2 and time = 100L");
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(createURLWithPort(URL_GET_MESSAGE_HISTORY))
          .queryParam("fromUser", 1)
          .queryParam("toUser", 2)
          .queryParam("fromDate", 100L);

        Message[] message = restTemplate.getForObject(builder.toUriString(), Message[].class);
        int len = message.length;
        assertTrue(len == 1);
        listMessages(message);
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }

    private void listMessages(Message[] messages) {
        Arrays.stream(messages).forEach(s -> logger.info(s.toString()));
    }

}


