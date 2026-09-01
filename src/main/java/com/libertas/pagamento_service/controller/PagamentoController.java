package com.libertas.pagamento_service.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/pagamento")
public class PagamentoController {

    private static final Logger log = LoggerFactory.getLogger(PagamentoController.class);

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${pedido.service.url}")
    private String pedidoServiceUrl;

    @PostMapping("/{id}/processar")
    public ResponseEntity<Map<String, Object>> processarPagamento(@PathVariable String id) {
        // Lógica para processar o pagamento
        String url = pedidoServiceUrl + "/pedidos/{id}/atualizar-pedido";
        Map<String, String> requestBody = Map.of("status", "PAGO");
        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(requestBody), String.class, id);
            log.info("Resposta do pedido-service -> status={} | body={}", response.getStatusCode(), response.getBody());
            return ResponseEntity.ok(Map.of("message", "Pagamento processado com sucesso!"));
        } catch (RestClientException e) {
            log.error("Erro ao atualizar o status do pedido: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Erro ao atualizar o status do pedido."));
        }
    }

}
