drop table if exists users cascade;
drop table if exists request cascade;
drop table if exists items cascade;
drop table if exists bookings cascade;
drop table if exists comments cascade;


CREATE TABLE IF NOT EXISTS users (
id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
email varchar(320) unique ,
name varchar(100));

CREATE TABLE IF NOT EXISTS request (
id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY key,
description varchar(500),
created_date TIMESTAMP WITHOUT TIME zone,
requestor_id BIGINT references users(id));


CREATE TABLE IF NOT EXISTS items (
id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
name varchar(250) NOT NULL,
description varchar(500) NOT NULL,
owner_id BIGINT REFERENCES  users(id),
is_available boolean,
request_id BIGINT references request(id));

create table if not exists bookings (
id bigint GENERATED ALWAYS AS IDENTITY PRIMARY key,
start_date TIMESTAMP WITHOUT TIME ZONE,
end_date TIMESTAMP WITHOUT TIME zone,
item_id bigint references items(id),
booker_id bigint references users(id),
status varchar(55));

create table if not exists comments (
id bigint GENERATED ALWAYS AS IDENTITY PRIMARY key,
text varchar (555),
item_id bigint references items(id),
author_id bigint references users(id),
created_date TIMESTAMP WITHOUT TIME ZONE);