ALTER TABLE
    products ALTER COLUMN id TYPE UUID
        USING id::uuid;

ALTER TABLE
    purchase_items ALTER COLUMN product_id TYPE UUID
        USING product_id::uuid;