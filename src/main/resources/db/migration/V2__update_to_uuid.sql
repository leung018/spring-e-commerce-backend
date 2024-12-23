ALTER TABLE
    users ALTER COLUMN id TYPE UUID
        USING id::uuid;

ALTER TABLE
    orders ALTER COLUMN buyer_user_id TYPE UUID
        USING buyer_user_id::uuid;

ALTER TABLE
    products ALTER COLUMN user_id TYPE UUID
        USING user_id::uuid;