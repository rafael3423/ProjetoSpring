package com.example.demo2.config;


import com.example.demo2.domain.tasks.TestTask;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpConnectException;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.AmqpTimeoutException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.transaction.RabbitTransactionManager;
import org.springframework.amqp.support.converter.ContentTypeDelegatingMessageConverter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MarshallingMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.context.WebApplicationContext;

/**
 * Configures the AMQP client.
 */
@Configuration
@Scope(WebApplicationContext.SCOPE_APPLICATION)
public class AmqpConfig implements MessagePostProcessor {

    public static final String CONFIG_HOST = "integracao.amqp.host";
    public static final String CONFIG_PORT = "integracao.amqp.port";
    public static final String CONFIG_USERNAME = "integracao.amqp.username";
    public static final String CONFIG_PASSWORD = "integracao.amqp.password";
    public static final String CONFIG_CONSUMERS = "integracao.amqp.consumers";
    public static final String CONFIG_CHANNEL_CACHE = "integracao.amqp.channel_cache";

    public static final String DEFAULT_USERNAME = "guest";
    public static final String DEFAULT_PASSWORD = "guest";
    public static final String DEFAULT_EXCHANGE = "";
    public static final String EXCHANGE = "integracao";
    public static final String DEAD_LETTER_EXCHANGE = "integracao.dead";
    public static final String DEAD_LETTER_QUEUE = "integracao.dead";

    private static final int DEFAULT_PORT = 5672;
    private static final int DEFAULT_MAX_CONSUMERS = 8;
    private static final int MIN_CONSUMERS = 2;
    private static final int PREFETCH = 2;
    private static final int MAX_RETRIES = 8;

    private static final Logger LOG
            = LoggerFactory.getLogger(AmqpConfig.class);

    @Bean
    public ConnectionFactory connectionFactory() throws Exception {
        LOG.info("Configuring AMQP connection factory");

        String host = "localhost";

        int port = DEFAULT_PORT;
        try {
            port = 5672;
        } catch (NoSuchElementException e) {
            LOG.info("Using default AMQP service port");
        }

        CachingConnectionFactory f = new CachingConnectionFactory(host, port);
        f.setPublisherConfirms(false);
        f.setPublisherReturns(false);

        try {
            f.setUsername("rabbitmq");
            f.setPassword("3j3b4ig4");
        } catch (NoSuchElementException e) {
            LOG.info("Using default AMQP credentials");
            f.setUsername(DEFAULT_USERNAME);
            f.setPassword(DEFAULT_PASSWORD);
        }

        f.afterPropertiesSet();
        return f;
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory f) {
        RabbitTemplate t = new RabbitTemplate(f);
        RetryTemplate rt = new RetryTemplate();

        Map<Class<? extends Throwable>, Boolean> m = new HashMap<>();
        m.put(Exception.class, false);
        m.put(AmqpConnectException.class, true);
        m.put(AmqpTimeoutException.class, true);

        // default values are fine
        rt.setBackOffPolicy(new ExponentialBackOffPolicy());
        rt.setRetryPolicy(new SimpleRetryPolicy(MAX_RETRIES, m));
        t.setRetryTemplate(rt);
        return t;
    }

   @Bean
    public RabbitListenerContainerFactory<SimpleMessageListenerContainer>
            rabbitListenerContainerFactory(ConnectionFactory cf,
                    PlatformTransactionManager ptm, MessageConverter mc) {
        LOG.info("Configuring AMQP consumer container factory");

        SimpleRabbitListenerContainerFactory f
                = new SimpleRabbitListenerContainerFactory();
        f.setConnectionFactory(cf);
        f.setConcurrentConsumers(MIN_CONSUMERS);
        f.setPrefetchCount(PREFETCH);
        f.setTransactionManager(ptm);
        f.setChannelTransacted(true);
        f.setMessageConverter(mc);
        f.setDefaultRequeueRejected(true);
        f.setMaxConcurrentConsumers(DEFAULT_MAX_CONSUMERS);
        
        return f;
    }

    @Bean
    public MessageConverter messageConverter(Marshaller marshaller) {
        ContentTypeDelegatingMessageConverter c
                = new ContentTypeDelegatingMessageConverter();
        c.addDelegate(MediaType.APPLICATION_XML_VALUE,
                new MarshallingMessageConverter(marshaller));
        c.addDelegate(MediaType.APPLICATION_JSON_VALUE,
                new Jackson2JsonMessageConverter());
        return c;
    }

    @Bean
    public Exchange integracaoExchange() {
        return new DirectExchange(EXCHANGE, true, false);
    }
    
//   @Bean
//   public CustomExchange delayExchange() {
//       Map<String, Object> args = new HashMap<String, Object>();
//       args.put("x-delayed-type", "direct");
//       return new CustomExchange(EXCHANGE, "x-delayed-message", true, false, args);
//   }

    @Bean
    public Exchange deadLetterExchange() {
        return new FanoutExchange(DEAD_LETTER_EXCHANGE, true, false);
    }

    @Bean
    public Queue deadLetterQueue() {
        return new Queue(DEAD_LETTER_QUEUE, true, false, false);
    }

    @Bean
    public Binding deadLetterBinding() {
        return new Binding(DEAD_LETTER_QUEUE, Binding.DestinationType.QUEUE,
                DEAD_LETTER_EXCHANGE, DEAD_LETTER_QUEUE, null);
    }

    @Bean
    @Qualifier("amqp.queue.default")
    public Arguments defaultQueueArguments() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE);
        args.put("x-dead-letter-routing-key", DEAD_LETTER_QUEUE);
        return new Arguments(args);
    }

    @Bean
    @ConditionalOnMissingBean
    public org.springframework.oxm.Marshaller marshaller() {
        Jaxb2Marshaller m = new Jaxb2Marshaller();
                m.setClassesToBeBound(TestTask.class);

        return m;
    }

    @Bean
    @ConditionalOnMissingBean
    public PlatformTransactionManager transactionManager(ConnectionFactory f) {
        return new RabbitTransactionManager(f);
    }

    @Override
    public Message postProcessMessage(Message m) throws AmqpException {
        MessageProperties p = m.getMessageProperties();
        p.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
        return m;
    }

    /**
     * Wraps argument map for easier injection.
     */
    public static class Arguments {

        private final Map<String, Object> arguments;

        public Arguments(Map<String, Object> arguments) {
            this.arguments = arguments;
        }

        public Map<String, Object> getArguments() {
            return arguments;
        }

    }


}
