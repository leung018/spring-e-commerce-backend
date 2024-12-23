ALTER TABLE
    orders ALTER COLUMN request_id TYPE UUID
        USING request_id::uuid;