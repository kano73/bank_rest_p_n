package org.example.bank_rest_p_n.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.UuidGenerator;

/**
 * MyUser — [comment]
 *
 * @author Pavel Nenahov
 * @version 1.0
 * @since 03/09/2025
 */

@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class MyUser {
    @Id
    @UuidGenerator
    private String id;

    private String firstName;

    private String lastName;
}
