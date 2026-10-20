package com.libertas.pagamento_service.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

import com.libertas.pagamento_service.config.RabbitMQConfig;
import com.libertas.pagamento_service.event.PedidoCriadoEvent;
import com.libertas.pagamento_service.service.PagamentoService;

@Component
public class PedidoCriadoListener {

    private static final Logger log = LoggerFactory.getLogger(PedidoCriadoListener.class);

    private final PagamentoService pagamentoService;

    public PedidoCriadoListener(PagamentoService pagamentoService) {
        this.pagamentoService = pagamentoService;
    }

    @RabbitListener(queues = RabbitMQConfig.PEDIDO_CRIADO_QUEUE)
    public void onPedidoCriado(PedidoCriadoEvent evento) {
        log.info("Evento pedido.criado recebido -> {}", evento);
        try {
            pagamentoService.processar(String.valueOf(evento.id()));
            log.info("Pagamento do pedido {} processado com sucesso", evento.id());
        } catch (RestClientException e) {
            log.error("Erro ao processar pagamento do pedido {}: {}", evento.id(), e.getMessage());
            // Evita reentrega infinita da mesma mensagem
            throw new AmqpRejectAndDontRequeueException("Falha ao processar pagamento do pedido " + evento.id(), e);
        }
    }
}
