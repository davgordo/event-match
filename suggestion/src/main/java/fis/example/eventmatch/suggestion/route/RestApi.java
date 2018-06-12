package fis.example.eventmatch.suggestion.route;


import fis.example.eventmatch.suggestion.model.Calendar;
import fis.example.eventmatch.suggestion.model.Suggestion;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.spi.DataFormat;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

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
                .bindingMode(RestBindingMode.json);

        restConfiguration()
                .producerComponent("http4");

        rest("/").description("Suggestions REST service")

            .get("/suggestions?user={userId}")
                .produces("application/json").type(Suggestion[].class)
                .route().routeId("get-suggestions")
                .removeHeaders("*","userId")
                .to("rest:get:calendars/{userId}?host=calendar.svc")
                .unmarshal().json(JsonLibrary.Jackson, Calendar.class)
                .process(exchange -> {
                    Calendar response = exchange.getIn().getBody(Calendar.class);
                    ArrayList<Suggestion> suggestionList = new ArrayList<Suggestion>();
                    Suggestion suggestion = new Suggestion();
                    suggestion.setEvent("Perfect fit for " + response.getUserId());
                    suggestionList.add(suggestion);
                    exchange.getIn().setBody(suggestionList);
                });

    }

}