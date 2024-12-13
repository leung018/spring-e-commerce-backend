create table orders (buyer_user_id varchar(255), id varchar(255) not null, request_id varchar(255), primary key (id), unique (request_id, buyer_user_id));
create table products (price numeric(19,10) check (price>=0), quantity integer not null check (quantity>=0), id varchar(255) not null, name varchar(255) not null, user_id varchar(255), primary key (id));
create table purchase_items (quantity integer, order_id varchar(255) not null, product_id varchar(255) not null, primary key (order_id, product_id));
create table users (balance numeric(19,10) check (balance>=0), id varchar(255) not null, password varchar(255) not null, username varchar(255) not null unique, primary key (id));
alter table if exists purchase_items add constraint FKdcec4w10xea9gqcsp3wqns9tc foreign key (order_id) references orders;
