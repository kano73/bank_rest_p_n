# Monolit backend Spring boot app for Bank Simulation

**Features**

* User authentication and registration
* Profile management (updating personal data)
* Working with cards

  * Creating cards
  * Top-up
  * Transfer between cards
  * Viewing balance and list of cards

**Admin Panel**

* User management (block, delete, filter)
* Card management (create, block/unblock, delete, search)

**Controller Structure**

* `AuthController` → login / logout
* `UserController` → registration, updating data
* `CardController` → card and transaction operations
* `AdminUserPanelController` → admin functions for users
* `AdminCardPanelController` → admin functions for cards

**API Endpoints**

*Authentication*

* `POST /login` → login
* `POST /logout` → logout

*User*

* `POST /register` → register
* `PUT /update_my_info` → update profile

*Cards (user)*

* `POST /new_card` → create card
* `GET /my_cards?page={page}` → list of cards
* `GET /balance` → card balance
* `PUT /top_up` → top up card
* `PUT /transaction` → transfer funds

*Admin (users)*

* `PUT /block_user` → block user
* `DELETE /delete_user` → delete user
* `GET /get_users?pageNumber={page}` → list with filtering

*Admin (cards)*

* `POST /admin/new_card_for_user` → create a card for a user
* `GET /admin/users_cards?pageNumber={page}` → list of cards with filtering
* `PUT /admin/change_card_status` → change card status
* `DELETE /admin/delete_card` → delete card

**Project Setup**

**Requirements**

* Java 17+
* Maven 3+
* PostgreSQL (or another SQL backend)

The application will run on:
[http://localhost:8080](http://localhost:8080)
