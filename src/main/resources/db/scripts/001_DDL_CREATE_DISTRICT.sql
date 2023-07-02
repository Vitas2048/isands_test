create table if not exists district (
    id serial primary key not null,
    name varchar(255) unique not null,
    code varchar(255) unique,
    archive_status boolean
);