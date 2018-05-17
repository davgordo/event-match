package fis.example.eventmatch.calendar.route;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import fis.example.eventmatch.calendar.model.Calendar;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

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

        rest("/").description("Calendar REST service")

            .get("/calendars/{userId}")
                .produces("application/json").type(Calendar.class)
                .route().routeId("get-calendar")
                .process(exchange -> {
                    String userId = exchange.getIn().getHeader("userId").toString();
                    DBObject query = BasicDBObjectBuilder.start("userId", userId).get();
                    exchange.getIn().setBody(query);
                })
                .to("mongodb:calendarDb?database={{mongodb.database}}&collection={{mongodb.collection}}&operation=findOneByQuery&dynamicity=true")
                .process(exchange -> {
                    BasicDBObject result = exchange.getIn().getBody(BasicDBObject.class);
                    result.remove("_id");
                    exchange.getIn().setBody(result);
                })
                .log("Looks like this: ${body}")
            .endRest()

            .post("/calendars/{userId}")
                .consumes("application/json").type(Calendar.class)
                .route().routeId("update-calendar")
                .removeHeaders("CamelHttp*")
                .to("mongodb:calendarDb?database={{mongodb.database}}&collection={{mongodb.collection}}&operation=insert&writeConcern=SAFE")
                .setBody(simple("Calendar Event ${headers.userId}"))
                .setExchangePattern(ExchangePattern.InOnly)
                .to("amq:topic:calendar.updated")
                .setBody(constant(null))
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(HttpStatus.CREATED.value()))
            .endRest();

    }

}