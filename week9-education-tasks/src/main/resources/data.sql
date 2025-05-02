INSERT INTO category (name)
VALUES ('Electronics'),
       ('Clothing'),
       ('Books');

INSERT INTO product (name, price, category_id)
VALUES ('Laptop Acer', 999.99, 1),
       ('Laptop HP', 888.99, 1),
       ('Laptop HP Pavilion', 888.99, 1),
       ('Laptop Lenovo', 888.99, 1),
       ('Laptop Lenovo11', 777.99, 1),
       ('T-Shirt', 19.99, 2),
       ('Spring Guide', 29.99, 3),
       ('Java Head First', 99.99, 3),
       ('Spring Guide', 29.99, 3);

insert into users (username, password, enabled)
values ('admin', '{bcrypt}$2a$12$RDQrb1dIhBDIT9QNUXEZb.3b12WKWAzrgarIMqMl.ydPyeN1uWBzC', 1),
       ('user', '{bcrypt}$2a$12$RDQrb1dIhBDIT9QNUXEZb.3b12WKWAzrgarIMqMl.ydPyeN1uWBzC', 1),
       ('user2', '{bcrypt}$2a$12$RDQrb1dIhBDIT9QNUXEZb.3b12WKWAzrgarIMqMl.ydPyeN1uWBzC', 1);

insert into authorities
values ('admin', 'admin'),
       ('user', 'user'),
       ('user2', 'user');