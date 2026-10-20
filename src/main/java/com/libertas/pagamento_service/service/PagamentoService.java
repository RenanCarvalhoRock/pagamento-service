package com.libertas.pagamento_service.service;

import java.time.Duration;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class PagamentoService {

    private static final Logger log = LoggerFactory.getLogger(PagamentoService.class);

    private final RestTemplate restTemplate = buildRestTemplate();

    @Value("${pedido.service.url}")
    private String pedidoServiceUrl;

    private static RestTemplate buildRestTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofSeconds(3));
        factory.setReadTimeout(Duration.ofSeconds(5));
        return new RestTemplate(factory);
    }

    /**
     * Simula o processamento do pagamento e atualiza o status do pedido para PAGO no pedido-service.
     *
     * @throws RestClientException se a chamada ao pedido-service falhar
     */
    public void processar(String pedidoId) {
        // Lógica para processar o pagamento
        String url = pedidoServiceUrl + "/pedidos/{id}/atualizar-pedido";
        Map<String, String> requestBody = Map.of("status", "PAGO");

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(requestBody), String.class, pedidoId);
        log.info("Resposta do pedido-service -> status={} | body={}", response.getStatusCode(), response.getBody());
    }
}
