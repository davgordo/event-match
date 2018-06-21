package fis.example.eventmatch.suggestion.route;

import fis.example.eventmatch.suggestion.model.Calendar;
import fis.example.eventmatch.suggestion.model.Event;
import fis.example.eventmatch.suggestion.model.Suggestion;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;

@Component
class RestApi extends RouteBuilder {

    @Override
    public void configure() {

        restConfiguration()
                .contextPath("/").apiContextPath("/api-doc")
                .apiProperty("api.title", "Camel REST API")
                .apiProperty("api.version", "1.0")
                .apiProperty("cors", "true")
                .apiContextRouteId("doc-api")
                .component("servlet")
                .bindingMode(RestBindingMode.json)
                .producerComponent("http4");

        rest("/").description("Suggestions REST service")

            .get("/suggestions?user={userId}")
                .produces("application/json").type(Suggestion[].class)
                .route().routeId("get-suggestions")
                .removeHeaders("*","userId")
                .to("rest:get:calendars/{userId}?host=calendar:8080")
                .unmarshal().json(JsonLibrary.Jackson, Calendar.class)
                .setProperty("calendar", body())
                .to("rest:get:events?host=event:8080")
                .unmarshal().json(JsonLibrary.Jackson, Event[].class)
                .setProperty("events", body())
                .process(exchange -> {
                    Calendar calendar = exchange.getProperty("calendar", Calendar.class);
                    ArrayList<Event> events = new ArrayList<Event>(Arrays.asList(exchange.getProperty("events", Event[].class)));
                    ArrayList<Suggestion> suggestions = new ArrayList<Suggestion>();

                    if(calendar.getEntryList().size() > 0 && events.size() > 0) {
                        Suggestion suggestion = new Suggestion();
                        suggestion.setEvent("Perfect fit for " + calendar.getUserId());
                        suggestions.add(suggestion);
                    }

                    exchange.getIn().setBody(suggestions);
                });

    }

}