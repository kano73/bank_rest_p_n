package org.example.bank_rest_p_n.model.dto;

import lombok.Getter;
import lombok.Setter;
import org.example.bank_rest_p_n.model.enumClass.MyRole;

/**
 * FilterUserDTO — [comment]
 *
 * @author Pavel Nenahov
 * @version 1.0
 * @since 03/09/2025
 */

@Getter
@Setter
public class FilterUserDTO {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private MyRole role;
    private Boolean isBlocked;

    private int pageNumber;
}
