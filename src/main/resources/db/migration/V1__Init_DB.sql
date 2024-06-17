CREATE TABLE address
(
    address_id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    city_name   varchar(40) NOT NULL DEFAULT ' ',
    street_name varchar(40) NOT NULL DEFAULT ' ',
    home_number integer     NOT NULL DEFAULT 0,
    home_letter varchar(3)  NOT NULL DEFAULT ' '
);

CREATE TABLE store
(
    store_id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    address_id uuid  NOT NULL REFERENCES address (address_id) ON DELETE SET NULL,
    location   point NOT NULL   DEFAULT point(0.0, 0.0)
);

CREATE TABLE ingredient
(
    ingredient_id uuid PRIMARY KEY     DEFAULT gen_random_uuid(),
    name          varchar(30) NOT NULL,
    unit_price    float8      NOT NULL DEFAULT 0.0,
    measure       varchar(15) NOT NULL DEFAULT 'GRAMS'
);

CREATE TABLE store_ingredient
(
    store_id      uuid   NOT NULL REFERENCES store (store_id) ON DELETE CASCADE,
    ingredient_id uuid   NOT NULL REFERENCES ingredient (ingredient_id) ON DELETE CASCADE,
    amount        float4 NOT NULL DEFAULT 0
);

CREATE TABLE product
(
    product_id uuid PRIMARY KEY     DEFAULT gen_random_uuid(),
    name       varchar(20) NOT NULL DEFAULT ' ',
    description varchar(200) NOT NULL DEFAULT ' ',
    size       varchar(6)  NOT NULL DEFAULT 'SMALL'
);

CREATE TABLE product_ingredient
(
    product_id    uuid   NOT NULL REFERENCES product (product_id) ON DELETE CASCADE,
    ingredient_id uuid   NOT NULL REFERENCES ingredient (ingredient_id) ON DELETE CASCADE,
    amount float4 NULL,
    CONSTRAINT unique_product_ingredient UNIQUE (product_id, ingredient_id)
);

CREATE TABLE cart
(
    cart_id uuid PRIMARY KEY     DEFAULT gen_random_uuid(),
    client_id   bigint      NOT NULL DEFAULT 0,
    store_id    uuid        NOT NULL REFERENCES store (store_id),
    order_date  timestamptz NOT NULL DEFAULT current_timestamp,
    order_price float8      NOT NULL,
    status  varchar(20) NOT NULL DEFAULT 'CREATED'
);


CREATE INDEX idx_cart_client_id ON cart (client_id);

CREATE TABLE cart_products
(
    cart_id    uuid NOT NULL REFERENCES cart (cart_id) ON DELETE CASCADE,
    product_id uuid NOT NULL REFERENCES product (product_id) ON DELETE CASCADE
);