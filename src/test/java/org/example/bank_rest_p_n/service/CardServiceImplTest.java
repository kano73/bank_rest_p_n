package org.example.bank_rest_p_n.service;

import org.example.bank_rest_p_n.exception.IllegalOperation;
import org.example.bank_rest_p_n.exception.NoDataFoundException;
import org.example.bank_rest_p_n.model.dto.*;
import org.example.bank_rest_p_n.model.entity.MyCard;
import org.example.bank_rest_p_n.model.entity.MyUser;
import org.example.bank_rest_p_n.model.enumClass.CardStatus;
import org.example.bank_rest_p_n.repository.CardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CardServiceImpl Tests")
class CardServiceImplTest {

    @Mock
    private CardRepository cardRepository;

    @Mock
    private CardGeneratorImpl cardGeneratorImpl;

    @InjectMocks
    private CardServiceImpl cardService;

    private MyUser testUser;
    private MyCard testCard1;
    private MyCard testCard2;
    private final String USER_ID = "user123";
    private final String CARD_ID_1 = "card123";
    private final String CARD_ID_2 = "card456";

    @BeforeEach
    void setUp() {
        // Устанавливаем значение PAGE_SIZE через рефлексию
        ReflectionTestUtils.setField(cardService, "PAGE_SIZE", 10);

        testUser = new MyUser();
        testUser.setId(USER_ID);

        testCard1 = new MyCard();
        testCard1.setId(CARD_ID_1);
        testCard1.setNumber("1234567890123456");
        testCard1.setOwner(testUser);
        testCard1.setBalance(new BigDecimal("1000.00"));
        testCard1.setStatus(CardStatus.ACTIVE);

        testCard2 = new MyCard();
        testCard2.setId(CARD_ID_2);
        testCard2.setNumber("9876543210987654");
        testCard2.setOwner(testUser);
        testCard2.setBalance(new BigDecimal("500.00"));
        testCard2.setStatus(CardStatus.ACTIVE);
    }

    @Test
    @DisplayName("Создание карты - успешно")
    void createCard_Success() {
        // Given
        when(cardGeneratorImpl.generateCard(USER_ID)).thenReturn(testCard1);
        when(cardRepository.save(testCard1)).thenReturn(testCard1);

        // When
        CardResponseDTO result = cardService.createCard(USER_ID);

        // Then
        assertNotNull(result);
        verify(cardGeneratorImpl).generateCard(USER_ID);
        verify(cardRepository).save(testCard1);
    }

    @Test
    @DisplayName("Поиск карт пользователя по ID - успешно")
    void findUsersCardsById_Success() {
        // Given
        List<MyCard> cards = Arrays.asList(testCard1, testCard2);
        Page<MyCard> cardPage = new PageImpl<>(cards);
        when(cardRepository.findAllByOwner_Id(eq(USER_ID), any(PageRequest.class)))
                .thenReturn(cardPage);

        // When
        List<CardResponseDTO> result = cardService.findUsersCardsById(USER_ID, 0);

        // Then
        assertEquals(2, result.size());
        verify(cardRepository).findAllByOwner_Id(USER_ID, PageRequest.of(0, 10));
    }

    @Test
    @DisplayName("Транзакция между картами - успешно")
    void transaction_Success() {
        // Given
        TransactionCardRequestDTO requestDTO = new TransactionCardRequestDTO();
        requestDTO.setFromCardId(CARD_ID_1);
        requestDTO.setToCardId(CARD_ID_2);
        requestDTO.setAmount(new BigDecimal("200.00"));

        when(cardRepository.findById(CARD_ID_1)).thenReturn(Optional.of(testCard1));
        when(cardRepository.findById(CARD_ID_2)).thenReturn(Optional.of(testCard2));
        when(cardRepository.save(any(MyCard.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Boolean result = cardService.transaction(testUser, requestDTO);

        // Then
        assertTrue(result);
        assertEquals(new BigDecimal("800.00"), testCard1.getBalance());
        assertEquals(new BigDecimal("700.00"), testCard2.getBalance());
        verify(cardRepository, times(2)).save(any(MyCard.class));
    }

    @Test
    @DisplayName("Транзакция - карта отправителя не найдена")
    void transaction_FromCardNotFound() {
        // Given
        TransactionCardRequestDTO requestDTO = new TransactionCardRequestDTO();
        requestDTO.setFromCardId(CARD_ID_1);
        requestDTO.setToCardId(CARD_ID_2);
        requestDTO.setAmount(new BigDecimal("200.00"));

        when(cardRepository.findById(CARD_ID_1)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NoDataFoundException.class,
                () -> cardService.transaction(testUser, requestDTO));
        verify(cardRepository, never()).save(any(MyCard.class));
    }

    @Test
    @DisplayName("Транзакция - карта получателя не найдена")
    void transaction_ToCardNotFound() {
        // Given
        TransactionCardRequestDTO requestDTO = new TransactionCardRequestDTO();
        requestDTO.setFromCardId(CARD_ID_1);
        requestDTO.setToCardId(CARD_ID_2);
        requestDTO.setAmount(new BigDecimal("200.00"));

        when(cardRepository.findById(CARD_ID_1)).thenReturn(Optional.of(testCard1));
        when(cardRepository.findById(CARD_ID_2)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NoDataFoundException.class,
                () -> cardService.transaction(testUser, requestDTO));
        verify(cardRepository, never()).save(any(MyCard.class));
    }

    @Test
    @DisplayName("Транзакция - карта отправителя заблокирована")
    void transaction_FromCardBlocked() {
        // Given
        testCard1.setStatus(CardStatus.BLOCKED);
        TransactionCardRequestDTO requestDTO = new TransactionCardRequestDTO();
        requestDTO.setFromCardId(CARD_ID_1);
        requestDTO.setToCardId(CARD_ID_2);
        requestDTO.setAmount(new BigDecimal("200.00"));

        when(cardRepository.findById(CARD_ID_1)).thenReturn(Optional.of(testCard1));
        when(cardRepository.findById(CARD_ID_2)).thenReturn(Optional.of(testCard2));

        // When & Then
        assertThrows(IllegalOperation.class,
                () -> cardService.transaction(testUser, requestDTO));
        verify(cardRepository, never()).save(any(MyCard.class));
    }

    @Test
    @DisplayName("Обновление статуса карты - успешно")
    void updateCardStatus_Success() {
        // Given
        UpdateStateDTO requestDTO = new UpdateStateDTO();
        requestDTO.setCardId(CARD_ID_1);
        requestDTO.setStatus(CardStatus.BLOCKED);

        when(cardRepository.findById(CARD_ID_1)).thenReturn(Optional.of(testCard1));
        when(cardRepository.save(testCard1)).thenReturn(testCard1);

        // When
        Boolean result = cardService.updateCardStatus(requestDTO);

        // Then
        assertTrue(result);
        assertEquals(CardStatus.BLOCKED, testCard1.getStatus());
        verify(cardRepository).save(testCard1);
    }

    @Test
    @DisplayName("Обновление статуса карты - карта не найдена")
    void updateCardStatus_CardNotFound() {
        // Given
        UpdateStateDTO requestDTO = new UpdateStateDTO();
        requestDTO.setCardId(CARD_ID_1);
        requestDTO.setStatus(CardStatus.BLOCKED);

        when(cardRepository.findById(CARD_ID_1)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NoDataFoundException.class,
                () -> cardService.updateCardStatus(requestDTO));
        verify(cardRepository, never()).save(any(MyCard.class));
    }

    @Test
    @DisplayName("Поиск карт по фильтру - успешно")
    void findCardsByFilter_Success() {
        // Given
        FilterCardDTO filterDTO = new FilterCardDTO();
        List<MyCard> cards = Arrays.asList(testCard1, testCard2);
        Page<MyCard> cardPage = new PageImpl<>(cards);

        when(cardRepository.findAll(any(Specification.class), any(PageRequest.class)))
                .thenReturn(cardPage);

        // When
        List<MyCard> result = cardService.findCardsByFilter(filterDTO, 0);

        // Then
        assertEquals(2, result.size());
        verify(cardRepository).findAll(any(Specification.class), eq(PageRequest.of(0, 10)));
    }

    @Test
    @DisplayName("Удаление карты - успешно")
    void deleteCard_Success() {
        // Given
        DeleteCardDTO requestDTO = new DeleteCardDTO();
        requestDTO.setCardId(CARD_ID_1);

        when(cardRepository.deleteByNumber(CARD_ID_1)).thenReturn(true);

        // When
        Boolean result = cardService.deleteCard(requestDTO);

        // Then
        assertTrue(result);
        verify(cardRepository).deleteByNumber(CARD_ID_1);
    }

    @Test
    @DisplayName("Получение баланса карты - успешно")
    void getBalanceOfCard_Success() {
        // Given
        when(cardRepository.findById(CARD_ID_1)).thenReturn(Optional.of(testCard1));

        // When
        BigDecimal balance = cardService.getBalanceOfCard(USER_ID, CARD_ID_1);

        // Then
        assertEquals(new BigDecimal("1000.00"), balance);
    }

    @Test
    @DisplayName("Получение баланса карты - карта не найдена")
    void getBalanceOfCard_CardNotFound() {
        // Given
        when(cardRepository.findById(CARD_ID_1)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NoDataFoundException.class,
                () -> cardService.getBalanceOfCard(USER_ID, CARD_ID_1));
    }

    @Test
    @DisplayName("Получение баланса карты - пользователь не владелец")
    void getBalanceOfCard_NotOwner() {
        // Given
        when(cardRepository.findById(CARD_ID_1)).thenReturn(Optional.of(testCard1));

        // When & Then
        assertThrows(IllegalOperation.class,
                () -> cardService.getBalanceOfCard("anotherUser", CARD_ID_1));
    }

    @Test
    @DisplayName("Пополнение карты - успешно")
    void topUp_Success() {
        // Given
        TopUpDTO requestDTO = new TopUpDTO();
        requestDTO.setCardId(CARD_ID_1);
        requestDTO.setAmount(new BigDecimal("500.00"));

        when(cardRepository.findById(CARD_ID_1)).thenReturn(Optional.of(testCard1));
        when(cardRepository.save(testCard1)).thenReturn(testCard1);

        // When
        Boolean result = cardService.topUp(requestDTO);

        // Then
        assertTrue(result);
        assertEquals(new BigDecimal("1500.00"), testCard1.getBalance());
        verify(cardRepository).save(testCard1);
    }

    @Test
    @DisplayName("Пополнение карты - карта не найдена")
    void topUp_CardNotFound() {
        // Given
        TopUpDTO requestDTO = new TopUpDTO();
        requestDTO.setCardId(CARD_ID_1);
        requestDTO.setAmount(new BigDecimal("500.00"));

        when(cardRepository.findById(CARD_ID_1)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NoDataFoundException.class,
                () -> cardService.topUp(requestDTO));
        verify(cardRepository, never()).save(any(MyCard.class));
    }

    @Test
    @DisplayName("Пополнение карты - карта заблокирована")
    void topUp_CardBlocked() {
        // Given
        testCard1.setStatus(CardStatus.BLOCKED);
        TopUpDTO requestDTO = new TopUpDTO();
        requestDTO.setCardId(CARD_ID_1);
        requestDTO.setAmount(new BigDecimal("500.00"));

        when(cardRepository.findById(CARD_ID_1)).thenReturn(Optional.of(testCard1));

        // When & Then
        assertThrows(IllegalOperation.class,
                () -> cardService.topUp(requestDTO));
        verify(cardRepository, never()).save(any(MyCard.class));
    }

    @Test
    @DisplayName("Транзакция - карта получателя заблокирована")
    void transaction_ToCardBlocked() {
        // Given
        testCard2.setStatus(CardStatus.BLOCKED);
        TransactionCardRequestDTO requestDTO = new TransactionCardRequestDTO();
        requestDTO.setFromCardId(CARD_ID_1);
        requestDTO.setToCardId(CARD_ID_2);
        requestDTO.setAmount(new BigDecimal("200.00"));

        when(cardRepository.findById(CARD_ID_1)).thenReturn(Optional.of(testCard1));
        when(cardRepository.findById(CARD_ID_2)).thenReturn(Optional.of(testCard2));

        // When & Then
        assertThrows(IllegalOperation.class,
                () -> cardService.transaction(testUser, requestDTO));
        verify(cardRepository, never()).save(any(MyCard.class));
    }
}