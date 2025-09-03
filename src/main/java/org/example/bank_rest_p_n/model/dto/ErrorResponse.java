package org.example.bank_rest_p_n.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * ErrorResponse — [comment]
 *
 * @author Pavel Nenahov
 * @version 1.0
 * @since 03/09/2025
 */

@AllArgsConstructor
@Getter
@Setter
public class ErrorResponse {
    private String message;
}
