package org.example.bank_rest_p_n.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * AdminUpdateUserDTO — [comment]
 *
 * @author Pavel Nenahov
 * @version 1.0
 * @since 03/09/2025
 */

@Getter
@Setter
public class AdminUpdateUserDTO {
    @NotNull
    private String userId;
    @NotNull
    private Boolean isBlocked;
}
