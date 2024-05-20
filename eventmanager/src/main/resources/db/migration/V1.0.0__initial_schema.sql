create table if not exists addresses (
        id bigint generated by default as identity,
        address varchar(255),
        city varchar(255),
        country varchar(255),
        primary key (id)
    );

create table if not exists events (
        id bigint generated by default as identity,
        name varchar(255),
        description varchar(255) not null,
        owner_id bigint not null,
        place_id bigint not null,
        start_date_time timestamp(6) with time zone not null,
        end_date_time timestamp(6) with time zone not null,
        max_participants_count integer not null,
        primary key (id)
    );

create table if not exists participants (
        id bigint generated by default as identity,
        name varchar(255) not null,
        surname varchar(255) not null,
        email varchar(255) not null,
        phone_number varchar(255) not null,
        primary key (id)
    );

create table if not exists events_participants (
        events_id bigint not null,
        participants_id bigint not null
    );

create table if not exists owners (
        id bigint generated by default as identity,
        name varchar(255) not null,
        surname varchar(255),
        email varchar(255) not null,
        phone_number varchar(255) not null,
        primary key (id)
    );

create table if not exists places (
        id bigint generated by default as identity,
        name varchar(255) not null,
        address varchar(255) not null,
        primary key (id)
    );

alter table if exists events
       add constraint event_owner
       foreign key (owner_id)
       references owners
       on delete cascade;

alter table if exists events
       add constraint event_place
       foreign key (place_id)
       references places;

alter table if exists events_participants
       add constraint event_participant
       foreign key (participants_id)
       references participants;

alter table if exists events_participants
       add constraint participant_event
       foreign key (events_id)
       references events;