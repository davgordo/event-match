package fis.example.eventmatch.calendar;

import org.apache.camel.EndpointInject;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.apache.camel.test.spring.MockEndpointsAndSkip;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RunWith(CamelSpringBootRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@MockEndpointsAndSkip("amq:*")
public class ApplicationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @EndpointInject(uri = "mock:amq:topic:calendar.updated")
    MockEndpoint mockCalendarUpdatedTopic;

    @Test
    public void testGetCalendar() throws Exception {

        String calendarUpdate = "{\"userId\":\"davgordo\",\"entryList\":[{\"available\":true,\"start\":1535819400000,\"end\":1535828400000}]}";
        ResponseEntity<String> response1 = restTemplate.postForEntity("/calendars/davgordo", calendarUpdate, String.class);
        Assert.assertEquals(HttpStatus.CREATED,response1.getStatusCode());

        ResponseEntity<String> response2 = restTemplate.getForEntity("/calendars/davgordo", String.class);
        Assert.assertEquals(HttpStatus.OK, response2.getStatusCode());
        Assert.assertEquals(calendarUpdate, response2.getBody());
    }
}
