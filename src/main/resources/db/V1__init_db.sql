-- -- Tables
-- create sequence user_seq start with 1 increment by 1;
-- create table users (
--                        id bigint not null,
--                        bucket_id bigint unique,
--                        name varchar(255),
--                        number varchar(255),
--                        password varchar(255),
--                        role varchar(255) check (role in ('USER','ADMIN')),
--                        primary key (id)
-- );
--
-- create sequence bucket_seq start with 1 increment by 1;
-- create table buckets (
--                          id bigint not null,
--                          user_id bigint unique,
--                          primary key (id)
-- );
-- alter table if exists buckets
--     add constraint buckets_fk_user
--         foreign key (user_id) references users on delete cascade;
--
-- create sequence category_seq start with 1 increment by 1;
-- create table categories (
--                             id bigint not null,
--                             name varchar(255),
--                             primary key (id)
-- );
--
-- create sequence product_seq start with 1 increment by 1;
-- create table products (
--                           id bigint not null,
--                           price float not null,
--                           description varchar(255),
--                           name varchar(255),
--                           primary key (id)
-- );
--
-- create table products_categories (
--                                      category_id bigint not null,
--                                      product_id bigint not null,
--                                      primary key (category_id, product_id)
--                                      --foreign key (category_id) references categories(id),
--                                      --foreign key (product_id) references products(id)
-- );
-- alter table if exists products_categories
--     add constraint products_categories_fk_category
--         foreign key (category_id) references categories on delete cascade;
--
-- alter table if exists products_categories
--     add constraint products_categories_fk_product
--         foreign key (product_id) references products on delete cascade;
--
-- create table buckets_products (
--                                   product_id bigint not null,
--                                   bucket_id bigint not null,
--                                   primary key (bucket_id, product_id)
--                                   --foreign key (bucket_id) references buckets(id),
--                                   --foreign key (product_id) references products(id)
-- );
-- alter table if exists buckets_products
--     add constraint bucket_products_fk_product
--         foreign key (product_id) references products on delete cascade;
--
-- alter table if exists buckets_products
--     add constraint bucket_products_fk_bucket
--         foreign key (bucket_id) references buckets on delete cascade;
--
-- create sequence order_seq start with 1 increment by 1;
-- create table orders (
--                         id bigint not null,
--                         sum float not null,
--                         created timestamp,
--                         updated timestamp,
--                         user_id bigint,
--                         status varchar(255) check (status in ('NEW','APPROVED','CANCELED','PAID','CLOSED')),
--                         primary key (id)
--                         --foreign key (user_id) references users(id)
-- );
-- alter table if exists orders
--     add constraint orders_fk_user
--         foreign key (user_id) references users on delete cascade;
--
-- create sequence order_details_seq start with 1 increment by 1;
-- create table orders_details (
--                                 id bigint not null,
--                                 amount float not null,
--                                 price float not null,
--                                 order_id bigint,
--                                 product_id bigint,
--                                 primary key (id)
--                                 --foreign key (order_id) references orders(id),
--                                 --foreign key (product_id) references products(id)
-- );
-- alter table if exists orders_details
--     add constraint orders_details_fk_order
--         foreign key (order_id) references orders on delete cascade;
--
-- alter table if exists orders_details
--     add constraint orders_details_fk_product
--         foreign key (product_id) references products on delete cascade;
-- ------------