package dev.canessaalvamiguel.servicecompany.entities;

import java.math.BigDecimal;

public record Product(Long id, String description, Long amount, BigDecimal price) {
}
