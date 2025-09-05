package org.example.bank_rest_p_n.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.example.bank_rest_p_n.model.enumClass.MyRole;
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
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class MyUser {
    @Id
    @UuidGenerator
    @EqualsAndHashCode.Include
    private String id;

    private String firstName;

    private String lastName;

    @NotNull
    @Column(unique = true)
    private String email;

    @NotNull
    private String password;

    @NotNull
    @Enumerated(EnumType.STRING)
    private MyRole role;

    @NotNull
    private Boolean isBlocked = Boolean.FALSE;
}
