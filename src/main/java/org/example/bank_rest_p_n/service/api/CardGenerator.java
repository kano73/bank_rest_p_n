package org.example.bank_rest_p_n.service.api;

import org.example.bank_rest_p_n.model.entity.MyCard;

public interface CardGenerator {
    MyCard generateCard(String ownerId);
}
