package com.libertas.pagamento_service.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;

import com.libertas.pagamento_service.service.PagamentoService;

@RestController
@RequestMapping("/pagamento")
public class PagamentoController {

    private static final Logger log = LoggerFactory.getLogger(PagamentoController.class);

    private final PagamentoService pagamentoService;

    public PagamentoController(PagamentoService pagamentoService) {
        this.pagamentoService = pagamentoService;
    }

    @PostMapping("/{id}/processar")
    public ResponseEntity<Map<String, Object>> processarPagamento(@PathVariable String id) {
        try {
            pagamentoService.processar(id);
            return ResponseEntity.ok(Map.of("message", "Pagamento processado com sucesso!"));
        } catch (RestClientException e) {
            log.error("Erro ao atualizar o status do pedido: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Erro ao atualizar o status do pedido."));
        }
    }

}
