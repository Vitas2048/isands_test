create table if not exists district_farmer (
    farmer_id int references farmer(id),
    district_id int  references district(id)
);