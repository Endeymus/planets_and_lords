create table lord
(
    id   int auto_increment
        primary key,
    name varchar(50) null,
    age  int         null
);

create table planet
(
    id      int auto_increment
        primary key,
    name    varchar(100) null,
    id_lord int          null,
    constraint planet_name_uindex
        unique (name),
    constraint planet_lord_id_fk
        foreign key (id_lord) references lord (id)
            on update cascade on delete cascade
);