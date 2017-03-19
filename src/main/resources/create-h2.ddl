
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

create table if not exists grouping
(
    id          bigint not null generated always as identity,
    name        varchar(200) not null unique
);

create table if not exists person_grouping
(
    person_id   bigint not null,
    grouping_id bigint not null,
    primary key (person_id, grouping_id),
    foreign key (person_id) references person (id) on delete cascade,
    foreign key (grouping_id) references grouping (id) on delete cascade
);
