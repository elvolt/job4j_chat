create table if not exists role
(
    id        serial primary key,
    authority varchar(200) not null unique
);

create table if not exists room
(
    id   serial primary key,
    name varchar(2000)
);

create table if not exists person
(
    id       serial primary key,
    name     varchar(200) not null,
    login    varchar(200) not null unique,
    password varchar(200) not null,
    role_id  int          not null references role (id)
);

create table if not exists message
(
    id        serial primary key,
    text      text                        not null,
    created   timestamp without time zone not null default now(),
    person_id int                         not null references person (id),
    room_id   int                         not null references room (id)
);