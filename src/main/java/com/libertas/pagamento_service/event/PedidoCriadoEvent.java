package com.libertas.pagamento_service.event;

import java.math.BigDecimal;

public record PedidoCriadoEvent(
        Long id,
        String cliente,
        String itens,
        BigDecimal valor,
        String status) {
}
