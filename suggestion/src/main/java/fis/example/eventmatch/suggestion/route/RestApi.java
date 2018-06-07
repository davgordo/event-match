package fis.example.eventmatch.suggestion.route;


import fis.example.eventmatch.suggestion.model.Suggestion;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
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

        rest("/").description("Suggestions REST service")

            .get("/suggestions?user={userId}")
                .produces("application/json").type(Suggestion[].class)
                .route().routeId("get-suggestions")
                .process(exchange -> {
                    ArrayList<Suggestion> suggestionList = new ArrayList<Suggestion>();
                    exchange.getIn().setBody(suggestionList);
                });

    }

}