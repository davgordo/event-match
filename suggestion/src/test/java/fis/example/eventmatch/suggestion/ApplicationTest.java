package fis.example.eventmatch.suggestion;

import org.apache.camel.test.spring.CamelSpringBootRunner;
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
public class ApplicationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testGetSuggestionsNoAvailability() throws Exception {

        String suggestionList = "[]";
        ResponseEntity<String> response = restTemplate.getForEntity("/suggestions?user=davgordo", String.class);
        Assert.assertEquals(HttpStatus.OK,response.getStatusCode());
        Assert.assertEquals(suggestionList, response.getBody());

    }
}
