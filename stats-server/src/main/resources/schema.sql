create table if not exists stats
(
    id        bigint GENERATED BY DEFAULT AS IDENTITY not null
    constraint stats_pk
    primary key,
    app       varchar,
    uri       varchar,
    ip        varchar,
    timestamp timestamp
);