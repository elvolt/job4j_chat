insert into role (authority)
values ('ROLE_USER'),
       ('ROLE_ADMIN');

insert into room (name)
values ('Room 1'),
       ('Room 2');

insert into person (name, login, password, role_id)
values ('admin', 'admin', '$2a$10$Uh3DB.J3/fkb4OFd4VMqt./oLXEaRGqmRohf6yJ0IyKtz2BOKetye',
        (select id from role where authority = 'ROLE_ADMIN'));

insert into message (text, person_id, room_id)
values ('Добро пожаловать в комнату 1',
        (select id from person where login = 'admin'),
        (select id from room where name = 'Room 1'));