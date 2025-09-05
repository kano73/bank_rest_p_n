Возможности
    Аутентификация и регистрация пользователей
    
    Управление профилем (обновление личных данных)
    
    Работа с картами
    
    создание карт
    
    пополнение
    
    перевод между картами
    
    просмотр баланса и списка карт

Админ-панель
    
    управление пользователями (блокировка, удаление, фильтрация)
    
    управление картами (создание, блокировка/разблокировка, удаление, поиск)
    
    Структура контроллеров
    
    AuthController → авторизация / выход
    
    UserController → регистрация, обновление данных
    
    CardController → работа с картами и транзакциями
    
    AdminUserPanelController → админ-функции для пользователей
    
    AdminCardPanelController → админ-функции для карт

API Эндпоинты
    Аутентификация
        
        POST /login → вход
        
        POST /logout → выход
    
    Пользователь
    
        POST /register → регистрация
        
        PUT /update_my_info → обновление профиля
        
    Карты (пользователь)
    
        POST /new_card → создать карту
        
        GET /my_cards?page={page} → список карт
        
        GET /balance → баланс карты
        
        PUT /top_up → пополнить карту
        
        PUT /transaction → перевод средств
    
    Админ (пользователи)
    
        PUT /block_user → блокировка
        
        DELETE /delete_user → удаление
        
        GET /get_users?pageNumber={page} → список с фильтрацией
    
    Админ (карты)
    
        POST /admin/new_card_for_user → создать карту пользователю
        
        GET /admin/users_cards?pageNumber={page} → список карт с фильтрацией
        
        PUT /admin/change_card_status → изменить статус карты
        
        DELETE /admin/delete_card → удалить карту

 Запуск проекта
Требования

Java 17+

Maven 3+

PostgreSQL (или другой SQL-бэкенд)

Приложение поднимется на:

http://localhost:8080