create table if not exists farmer (
    id serial primary key,
    organization_name varchar(255),
    opf_form int references opf(id),
    inn varchar(255),
    kpp varchar(255),
    ogrn varchar(255),
    reg_district int references district(id),
    reg_date timestamp,
    archive_status boolean
);