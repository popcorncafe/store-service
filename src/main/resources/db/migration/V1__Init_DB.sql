CREATE TABLE address
(
    id          uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    city_name   varchar(20) NOT NULL,
    street_name varchar(20) NOT NULL,
    home_number integer     NOT NULL,
    home_letter varchar(3)  NOT NULL
);

CREATE TABLE storage
(
    id uuid PRIMARY KEY DEFAULT gen_random_uuid()
);

CREATE TABLE store
(
    id         uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    storage_id uuid  NOT NULL REFERENCES storage (id) ON DELETE SET NULL,
    address_id uuid  NOT NULL REFERENCES address (id) ON DELETE SET NULL,
    location   point NOT NULL   DEFAULT point(0.0, 0.0)
);

CREATE TABLE ingredient
(
    id         uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    name       varchar(20) NOT NULL,
    unit_price float8      NOT NULL,
    measure    smallint    NOT NULL
);

CREATE TABLE product
(
    id          uuid PRIMARY KEY      DEFAULT gen_random_uuid(),
    name        varchar(20)  NOT NULL,
    description varchar(200) NULL,
    size        smallint     NOT NULL DEFAULT 0
);

CREATE TABLE storage_ingredient
(
    storage_id    uuid   NOT NULL REFERENCES storage (id) ON DELETE CASCADE,
    ingredient_id uuid   NOT NULL REFERENCES ingredient (id) ON DELETE CASCADE,
    amount        float4 NULL
);

CREATE TABLE product_ingredient
(
    product_id    uuid   NOT NULL REFERENCES product (id) ON DELETE CASCADE,
    ingredient_id uuid   NOT NULL REFERENCES ingredient (id) ON DELETE CASCADE,
    amount        float4 NULL
);

CREATE TABLE cart
(
    id          uuid PRIMARY KEY     DEFAULT gen_random_uuid(),
    client_id   bigint      NOT NULL DEFAULT 0,
    store_id    uuid        NOT NULL REFERENCES store (id),
    order_date  timestamptz NOT NULL DEFAULT current_timestamp,
    order_price float8      NOT NULL,
    status      smallint    NOT NULL DEFAULT 0,
    is_paid     bool        NOT NULL,
    products    uuid[]      NOT NULL
);
--
-- CREATE TABLE cart_products
-- (
--     cart_id    uuid NOT NULL REFERENCES cart (id) ON DELETE CASCADE,
--     product_id uuid NOT NULL REFERENCES product (id) ON DELETE CASCADE
-- );