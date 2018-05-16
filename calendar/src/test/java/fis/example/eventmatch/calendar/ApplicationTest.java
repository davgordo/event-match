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
        mockCalendarUpdatedTopic.expectedMessageCount(1);
        ResponseEntity<String> response = restTemplate.getForEntity("/calendars/1", String.class);

        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertTrue(response.getBody().contains("entryList"));
    }

    @Test
    public void testUpdateCalendar() throws Exception {
        String calendarUpdate = "{\"entryList\": [{\"available\": \"true\", \"start\": \"2018-09-01T16:30:00\", \"end\":\"2018-09-01T19:00:00\"}]}";
        mockCalendarUpdatedTopic.expectedMessageCount(1);
        ResponseEntity<String> response = restTemplate.postForEntity("/calendars/1", calendarUpdate, String.class);

        Assert.assertEquals(HttpStatus.CREATED,response.getStatusCode());
        Assert.assertFalse(response.hasBody());
        mockCalendarUpdatedTopic.assertIsSatisfied();
    }

}
