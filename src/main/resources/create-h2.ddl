
create table if not exists person
(
    id          bigint not null generated always as identity,
    firstname   varchar(200) not null,
    lastname    varchar(200) not null,
    birthdate   date
);

create table if not exists phone
(
    id          bigint not null generated always as identity,
    value       varchar(200) not null,
    person_id   bigint not null,
    foreign key (person_id) references person (id) on delete cascade
);

create table if not exists email
(
    id          bigint not null generated always as identity,
    value       varchar(200) not null,
    person_id   bigint not null,
    foreign key (person_id) references person (id) on delete cascade
);

create table if not exists group
(
    id          bigint not null generated always as identity,
    name        varchar(200) not null unique
);

create table if not exists person_group
(
    person_id   bigint not null,
    group_id    bignint not null,
    primary key (person_id, group_id),
    foreign key (person_id) references person (id) on delete cascade,
    foreign key (group_id) references group (id) on delete cascade
);
