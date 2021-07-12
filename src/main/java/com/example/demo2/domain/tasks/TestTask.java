package com.example.demo2.domain.tasks;

import org.slf4j.Logger;
import javax.annotation.PostConstruct;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import com.example.demo2.config.AmqpConfig;
import static com.example.demo2.config.AmqpConfig.EXCHANGE;
import com.example.demo2.domain.model.Cliente;
import com.google.gson.Gson;

@Component
public class TestTask {

    public static final String QUEUE = "integracao.tasks.test_task";

    @Autowired
    private AmqpTemplate amqpTemplate;

    private static final Logger LOGGER = LoggerFactory.getLogger(TestTask.class);
    
    Gson gson = new Gson();

    @PostConstruct
    public void init() {
    }

    @Bean
    public Queue testTaskRotinaQueue(@Qualifier("amqp.queue.default") AmqpConfig.Arguments args) {
        return new Queue(QUEUE, true, false, false, args.getArguments());
    }

    @Bean
    public Binding testTaskBinding() {
        return new Binding(QUEUE, Binding.DestinationType.QUEUE, EXCHANGE, QUEUE, null);
    }
    
    @RabbitListener(queues = QUEUE)
    public void testarTaskListener(String request) {
       
        Cliente cliente = gson.fromJson(request, Cliente.class);

        LOGGER.warn("Entrou no listener da fila TestTask - {}", cliente.getId().toString());
        
        try {
        
                System.out.println(cliente);

        } catch (Exception e) {
            LOGGER.warn("Erro na TestTask - {}", cliente.getId().toString());
            e.printStackTrace();
        }

    }

    public void testarTaskSend(Cliente cliente) {

        LOGGER.warn("Entrou na fila TestTask - {}", cliente.getId().toString());
        
        byte[] request = gson.toJson(cliente).getBytes();
        
       
//       MessageProperties properties = new MessageProperties();
//       properties.setHeader("x-delay", 6000);
       
       amqpTemplate.convertAndSend(EXCHANGE, QUEUE,
               MessageBuilder.withBody(request)
               		.setContentType(MediaType.TEXT_PLAIN_VALUE)
               		.setContentEncoding("UTF-8")
                       .setDeliveryMode(MessageDeliveryMode.PERSISTENT).build());
    }
    
}
