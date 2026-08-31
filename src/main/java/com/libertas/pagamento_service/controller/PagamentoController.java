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

        log.info("Chamando pedido-service -> PUT {} | body={}", url.replace("{id}", id), requestBody);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url, HttpMethod.PUT, new HttpEntity<>(requestBody), String.class, id);

            log.info("Resposta do pedido-service -> status={} | body={}", response.getStatusCode(), response.getBody());
        } catch (RestClientException e) {
            log.error("Falha ao chamar pedido-service: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                    .body(Map.of("message", "Falha ao atualizar status do pedido", "erro", e.getMessage()));
        }
        return ResponseEntity.ok(Map.of("message", "Pagamento processado com sucesso!"));
    }
}
