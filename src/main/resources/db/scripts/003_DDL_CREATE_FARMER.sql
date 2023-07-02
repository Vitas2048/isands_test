create table if not exists farmer (
    id serial primary key,
    organization_name varchar(255) not null,
    opf_id int references opf(id),
    inn varchar(255) not null,
    kpp varchar(255),
    ogrn varchar(255),
    reg_district_id int references district(id),
    reg_date timestamp,
    archive_status boolean
);