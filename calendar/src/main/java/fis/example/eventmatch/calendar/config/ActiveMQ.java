package fis.example.eventmatch.calendar.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class ActiveMQ {

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
    public org.apache.activemq.camel.component.ActiveMQConfiguration jmsConfig() {
        org.apache.activemq.camel.component.ActiveMQConfiguration jmsConfig = new org.apache.activemq.camel.component.ActiveMQConfiguration();
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

}