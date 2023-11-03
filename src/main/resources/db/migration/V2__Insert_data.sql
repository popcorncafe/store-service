WITH new_address AS (
    INSERT INTO address (city_name, street_name, home_number, home_letter)
        VALUES ('Test-city', 'Test-street', 32, 'G')
        RETURNING id),
     new_storage AS (
         INSERT INTO storage (id)
             VALUES (default)
             RETURNING id)

INSERT INTO store (id, storage_id, address_id, location)
VALUES ('2cde0e67-f524-4949-af26-642d2add6faf'::uuid, (SELECT id FROM new_storage),
        (SELECT id FROM new_address), point(40.1824, 44.5137));

WITH new_ingredients AS (
    INSERT INTO ingredient (name, unit_price, measure)
        VALUES ('Water', 2.98, 3),
               ('Coffee beans', 5.29, 1),
               ('Sugar', 0.75, 1),
               ('Caramel syrup', 5.34, 2),
               ('Milk', 1.17, 3)
        RETURNING id),
     new_products AS (
         INSERT INTO product (name, description, size)
             VALUES ('Coffee', 'Simple coffee', 0),
                    ('Coffee', 'Simple coffee', 1),
                    ('Coffee', 'Simple coffee', 2),
                    ('Latte', 'Latte', 0),
                    ('Latte', 'Latte', 1),
                    ('Latte', 'Latte', 2)
             RETURNING id)
INSERT
INTO product_ingredient (product_id, ingredient_id, amount)
VALUES ((SELECT (array_agg(id))[1] FROM new_products), (SELECT (array_agg(id))[1] FROM new_ingredients), 150),
       ((SELECT (array_agg(id))[1] FROM new_products), (SELECT (array_agg(id))[2] FROM new_ingredients), 5.25),
       ((SELECT (array_agg(id))[2] FROM new_products), (SELECT (array_agg(id))[1] FROM new_ingredients), 200),
       ((SELECT (array_agg(id))[2] FROM new_products), (SELECT (array_agg(id))[2] FROM new_ingredients), 7),
       ((SELECT (array_agg(id))[3] FROM new_products), (SELECT (array_agg(id))[1] FROM new_ingredients), 250),
       ((SELECT (array_agg(id))[3] FROM new_products), (SELECT (array_agg(id))[2] FROM new_ingredients), 8.75),
       ((SELECT (array_agg(id))[4] FROM new_products), (SELECT (array_agg(id))[1] FROM new_ingredients), 50),
       ((SELECT (array_agg(id))[4] FROM new_products), (SELECT (array_agg(id))[5] FROM new_ingredients), 100),
       ((SELECT (array_agg(id))[4] FROM new_products), (SELECT (array_agg(id))[2] FROM new_ingredients), 1.75),
       ((SELECT (array_agg(id))[5] FROM new_products), (SELECT (array_agg(id))[1] FROM new_ingredients), 67),
       ((SELECT (array_agg(id))[5] FROM new_products), (SELECT (array_agg(id))[5] FROM new_ingredients), 133),
       ((SELECT (array_agg(id))[5] FROM new_products), (SELECT (array_agg(id))[2] FROM new_ingredients), 2.35),
       ((SELECT (array_agg(id))[6] FROM new_products), (SELECT (array_agg(id))[1] FROM new_ingredients), 84),
       ((SELECT (array_agg(id))[6] FROM new_products), (SELECT (array_agg(id))[5] FROM new_ingredients), 166),
       ((SELECT (array_agg(id))[6] FROM new_products), (SELECT (array_agg(id))[2] FROM new_ingredients), 2.94);

WITH ingredients AS (
    SELECT id FROM ingredient
), storage_id AS (
    SELECT id FROM storage LIMIT 1
)

INSERT INTO storage_ingredient (storage_id, ingredient_id, amount)
VALUES ((SELECT id FROM storage_id), (SELECT (array_agg(id))[1] FROM ingredients), 2040.48),
       ((SELECT id FROM storage_id), (SELECT (array_agg(id))[2] FROM ingredients), 4325.32),
       ((SELECT id FROM storage_id), (SELECT (array_agg(id))[3] FROM ingredients), 3873.43),
       ((SELECT id FROM storage_id), (SELECT (array_agg(id))[4] FROM ingredients), 3243.32),
       ((SELECT id FROM storage_id), (SELECT (array_agg(id))[5] FROM ingredients), 1784.32);



-- INSERT INTO storage_ingredient(storage_id, ingredient_id, amount)
-- VALUES ('3e43edde-2bb8-400d-b7e0-5d3c517e443d'::uuid, '8e9272e1-188e-45c4-b23f-7fdb0a796f47'::uuid, 100.5),
--        ('3e43edde-2bb8-400d-b7e0-5d3c517e443d'::uuid, '01add104-f13e-4783-a293-9a91e2ce75c1'::uuid, 27.4),
--        ('3e43edde-2bb8-400d-b7e0-5d3c517e443d'::uuid, '055d67c3-2af0-433f-b2cc-5ed3b7f7ac0d'::uuid, 53.4),
--        ('3e43edde-2bb8-400d-b7e0-5d3c517e443d'::uuid, '61517f2f-1380-4b6a-84c5-0f2e5609a867'::uuid, 6.7),
--        ('3e43edde-2bb8-400d-b7e0-5d3c517e443d'::uuid, '47acd4fe-b93c-48bc-92c2-1e56c818fb18'::uuid, 24.3);

--
-- INSERT INTO cart(id, client_id, store_id, order_date, order_price, is_paid)
-- VALUES ('10606a89-6d90-4af0-b3f7-5d081c94734a'::uuid, 111, '54d40e78-7fbb-45d0-a113-bfb903089daf'::uuid,
--         '2019-03-27 17:24:56.754000 +00:00', 349.32, true);
--
