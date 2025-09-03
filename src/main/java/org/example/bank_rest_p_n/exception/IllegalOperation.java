package org.example.bank_rest_p_n.exception;

/**
 * IllegalOperation — [comment]
 *
 *
 * @author Pavel Nenahov
 * @version 1.0
 * @since 03/09/2025
 */

public class IllegalOperation extends RuntimeException {
  public IllegalOperation(String message) {
    super(message);
  }
}
