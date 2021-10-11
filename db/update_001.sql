create table role
(
    id        serial primary key,
    authority varchar(200) not null unique
);

create table room
(
    id   serial primary key,
    name varchar(2000)
);

create table person
(
    id       serial primary key,
    name     varchar(200) not null,
    login    varchar(200) not null unique,
    password varchar(200) not null,
    role_id  int          not null references role (id)
);

create table message
(
    id        serial primary key,
    text      text                        not null,
    created   timestamp without time zone not null default now(),
    person_id int                         not null references person (id),
    room_id   int                         not null references room (id)
);

insert into role (authority)
values ('ROLE_USER');
insert into role (authority)
values ('ROLE_ADMIN');

insert into room (name)
values ('Room 1');
insert into room (name)
values ('Room 2');
insert into room (name)
values ('Room 2');

insert into person (name, login, password, role_id)
values ('admin', 'admin', 'admin', (select id from role where authority = 'ROLE_ADMIN'));

insert into person (name, login, password, role_id)
values ('user', 'user', 'user', (select id from role where authority = 'ROLE_USER'));

insert into message (text, person_id, room_id)
values ('Добро пожаловать в комнату 1', 1, 1);

insert into message (text, person_id, room_id)
values ('Добро пожаловать в комнату 2', 2, 2);