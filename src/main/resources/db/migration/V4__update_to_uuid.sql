ALTER TABLE
    purchase_items DROP
        CONSTRAINT fk_purchase_items_order_id;

ALTER TABLE
    orders ALTER COLUMN id TYPE UUID
        USING id::uuid;

ALTER TABLE
    purchase_items ALTER COLUMN order_id TYPE UUID
        USING order_id::uuid;

ALTER TABLE
    purchase_items ADD CONSTRAINT fk_purchase_items_order_id FOREIGN KEY(order_id) REFERENCES orders(id);