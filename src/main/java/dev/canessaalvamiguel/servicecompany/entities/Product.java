package dev.canessaalvamiguel.servicecompany.entities;

import java.math.BigDecimal;

public record Product(
    Long companyId,
    Long id,
    String name,
    Long amount,
    BigDecimal price
) {
}
