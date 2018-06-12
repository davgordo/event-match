package fis.example.eventmatch.suggestion;

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

import static org.apache.camel.builder.Builder.constant;

@RunWith(CamelSpringBootRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@MockEndpointsAndSkip("rest:*")
public class ApplicationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @EndpointInject(uri = "mock:rest:get:calendars/{userId}")
    MockEndpoint mockCalendarService;

    @Test
    public void testGetUserCalendar() throws Exception {

        String mockCalendarResponse = "{\"userId\":\"davgordo\",\"entryList\":[{\"available\":true,\"start\":1535819400000,\"end\":1535828400000}]}";
        String expectedSuggestionList = "[{\"score\":0,\"event\":\"Perfect fit for davgordo\"}]";
        mockCalendarService.returnReplyBody(constant(mockCalendarResponse));
        mockCalendarService.expectedMessageCount(1);

        ResponseEntity<String> response = restTemplate.getForEntity("/suggestions?userId=davgordo", String.class);

        mockCalendarService.assertIsSatisfied();
        Assert.assertEquals(HttpStatus.OK,response.getStatusCode());
        Assert.assertEquals(expectedSuggestionList, response.getBody());

    }
}
