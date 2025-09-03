package org.example.bank_rest_p_n.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import lombok.*;
import org.example.bank_rest_p_n.model.enumClass.CardStatus;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * MyCard — [comment]
 *
 * @author Pavel Nenahov
 * @version 1.0
 * @since 03/09/2025
 */

@Entity
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedEntityGraph(
        name = "Card.withOwner",
        attributeNodes = @NamedAttributeNode("owner")
)
public class MyCard {

    @Id
    @UuidGenerator
    private String id;

    @Column(length = 19, nullable = false, unique = true)
    private String number;

    @ManyToOne
    private MyUser owner;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Enumerated(EnumType.STRING)
    private CardStatus status;

    @DecimalMin("0.00")
    private BigDecimal balance;

    @Version
    private int version;
}
