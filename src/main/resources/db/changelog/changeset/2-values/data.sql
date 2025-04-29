
insert into users (id, name, date_of_birth, password)
values (1, 'User One', '1990-01-01', '$2a$10$9FzVpwcDBweG9WbJ7V4bC.hPAIwMgzH0wZTz.K1oXHYbb5eiSmnRS'),

       (2, 'User Two', '1992-05-15', '$2a$10$rRsQ.C7OBGiWMv2tCBNHDORSkQYsI1SDuJ6mGD9h1QBdXgCa2cXrC');

insert into account (id, user_id, balance)
values (1, 1, 2000.00),
       (2, 2, 2934.00);

insert into email_data (id, user_id, email)
values (1, 1, 'user1@example.com'),
       (2, 2, 'user2@example.com');

insert into phone_data (id, user_id, phone)
values (1, 1, '12345678'),
       (2, 2, '23456789');