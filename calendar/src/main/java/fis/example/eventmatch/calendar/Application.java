package fis.example.eventmatch.calendar;

import fis.example.eventmatch.calendar.model.Calendar;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.activemq.camel.component.ActiveMQConfiguration;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.servlet.CamelHttpTransportServlet;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class Application extends SpringBootServletInitializer {

    @Value("${activemq.broker.url}")
    String amqBrokerURL;

    @Value("${activemq.broker.username:#null}")
    String amqUser;

    @Value("${activemq.broker.password:#null}")
    String amqPassword;

    @Value("${activemq.pool.max.connections}")
    Integer amqMaxConnections;

    @Value("${activemq.concurrent.consumers}")
    Integer amqConcurrentConsumers;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public ServletRegistrationBean servletRegistrationBean() {
        ServletRegistrationBean servlet = new ServletRegistrationBean(
            new CamelHttpTransportServlet(), "/*");
        servlet.setName("CamelServlet");
        return servlet;
    }

    @Bean
    public ActiveMQConnectionFactory jmsConnectionFactory() {
        ActiveMQConnectionFactory jmsConnectionFactory = new ActiveMQConnectionFactory();
        jmsConnectionFactory.setBrokerURL(amqBrokerURL);
        jmsConnectionFactory.setUserName(amqUser);
        jmsConnectionFactory.setPassword(amqPassword);
        return jmsConnectionFactory;
    }

    @Primary
    @Bean(initMethod = "start", destroyMethod = "stop")
    public PooledConnectionFactory pooledConnectionFactory() {
        PooledConnectionFactory pooledConnectionFactory = new PooledConnectionFactory();
        pooledConnectionFactory.setMaxConnections(amqMaxConnections);
        pooledConnectionFactory.setConnectionFactory(jmsConnectionFactory());
        return pooledConnectionFactory;
    }

    @Bean
    public ActiveMQConfiguration jmsConfig() {
        ActiveMQConfiguration jmsConfig = new ActiveMQConfiguration();
        jmsConfig.setConnectionFactory(pooledConnectionFactory());
        jmsConfig.setConcurrentConsumers(amqConcurrentConsumers);
        return jmsConfig;
    }

    @Bean
    public ActiveMQComponent amq() {
        ActiveMQComponent component = new ActiveMQComponent();
        component.setConfiguration(jmsConfig());
        return component;
    }

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
                    .setBody(constant(new Calendar()))
                .endRest()

                .post("/calendars/{userId}")
                    .consumes("application/json").type(Calendar.class)
                    .route().routeId("update-calendar")
                    .removeHeaders("CamelHttp*")
                    .setBody(simple("Calendar Event ${headers.userId}"))
                    .setExchangePattern(ExchangePattern.InOnly)
                    .to("amq:topic:calendar.updated")
                    .setBody(constant(null))
                    .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(HttpStatus.CREATED.value()))
                .endRest();

        }

    }

}