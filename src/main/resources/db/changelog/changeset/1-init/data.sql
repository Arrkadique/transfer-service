create table users
(
    id            bigint primary key,
    name          varchar(500),
    date_of_birth date,
    password      varchar(500)
);

create table account
(
    id      bigint primary key,
    user_id bigint references users (id),
    balance decimal
);

create table email_data
(
    id      bigint primary key,
    user_id bigint references users (id),
    email   varchar(200)
);

create table phone_data
(
    id      bigint primary key,
    user_id bigint references users (id),
    phone   varchar(13)
);
