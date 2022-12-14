

create table if not exists users
(
    id    bigint GENERATED BY DEFAULT AS IDENTITY not null
        constraint users_pk
            primary key,
    name  varchar not null,
    email varchar unique not null
);

create table if not exists categories
(
    id   bigint GENERATED BY DEFAULT AS IDENTITY not null
        constraint categories_pk
            primary key,
    name varchar unique not null
);

create table if not exists locations
(
    id  bigint GENERATED BY DEFAULT AS IDENTITY not null
        constraint locations_pk
            primary key,
    lat double precision not null,
    lon double precision not null
);

create table if not exists compilations
(
    id     bigint GENERATED BY DEFAULT AS IDENTITY not null
        constraint compilations_pk
            primary key,
    pinned boolean,
    title  varchar
);

create table if not exists events
(
    id      bigint GENERATED BY DEFAULT AS IDENTITY not null
        constraint events_pk
            primary key,
    annotation         varchar   not null,
    category_id        bigint    not null
        constraint events_categories_id_fk
            references categories,
    event_date         timestamp not null,
    initiator_id       bigint    not null
        constraint events_users_id_fk
            references users,
    paid               boolean   not null,
    title              varchar   not null,
    created_on         timestamp,
    description        varchar,
    location_id        bigint    not null
        constraint events_locations_id_fk
            references locations,
    participant_limit  integer,
    published_on       timestamp,
    request_moderation boolean,
    state              varchar
);

create table if not exists compilation_events
(
    event_id       bigint,
    compilation_id bigint,
    primary key (event_id, compilation_id),
    CONSTRAINT fk_compiled_events_compilations FOREIGN KEY (compilation_id)
    REFERENCES compilations (id) ON DELETE CASCADE,
    CONSTRAINT fk_compiled_events_events FOREIGN KEY (event_id) REFERENCES events (id)
);

create table if not exists requests
(
    id           bigint GENERATED BY DEFAULT AS IDENTITY not null
        constraint requests_pk
            primary key,
    created      timestamp,
    event_id     bigint
        constraint requests_events_id_fk
            references events,
    requester_id bigint
        constraint requests_users_id_fk
            references users,
    status        varchar
);

create table if not exists comments
(
    id        bigint GENERATED BY DEFAULT AS IDENTITY not null
        constraint comments_pk
            primary key,
    text      varchar not null,
    event_id  bigint  not null,
    author_id bigint  not null,
    created   timestamp,
    CONSTRAINT fk_events FOREIGN KEY (event_id)
    REFERENCES events (id) ON DELETE CASCADE,
    CONSTRAINT fk_users FOREIGN KEY (author_id) REFERENCES users (id)
);
