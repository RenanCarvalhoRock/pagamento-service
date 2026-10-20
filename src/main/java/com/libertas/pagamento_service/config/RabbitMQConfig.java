package com.libertas.pagamento_service.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String PEDIDOS_EXCHANGE = "pedidos";
    public static final String ROUTING_KEY_PEDIDO_CRIADO = "pedido.criado";
    public static final String PEDIDO_CRIADO_QUEUE = "pagamento.pedido.criado";

    // Mesma definição da exchange declarada pelo pedido-service (declaração é idempotente)
    @Bean
    public TopicExchange pedidosExchange() {
        return new TopicExchange(PEDIDOS_EXCHANGE, true, false);
    }

    @Bean
    public Queue pedidoCriadoQueue() {
        return new Queue(PEDIDO_CRIADO_QUEUE, true);
    }

    @Bean
    public Binding pedidoCriadoBinding(Queue pedidoCriadoQueue, TopicExchange pedidosExchange) {
        return BindingBuilder.bind(pedidoCriadoQueue).to(pedidosExchange).with(ROUTING_KEY_PEDIDO_CRIADO);
    }

    // Converte o JSON da mensagem para o tipo do parâmetro do @RabbitListener,
    // sem depender da classe do publisher (header __TypeId__).
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }
}
