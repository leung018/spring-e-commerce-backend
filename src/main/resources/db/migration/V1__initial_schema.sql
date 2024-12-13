CREATE
    TABLE
        orders(
            buyer_user_id VARCHAR(36),
            id VARCHAR(36) NOT NULL,
            request_id VARCHAR(36),
            PRIMARY KEY(id),
            UNIQUE(
                request_id,
                buyer_user_id
            )
        );

CREATE
    TABLE
        products(
            price NUMERIC(
                19,
                10
            ),
            quantity INTEGER NOT NULL,
            id VARCHAR(36) NOT NULL,
            name VARCHAR(255) NOT NULL,
            user_id VARCHAR(36),
            PRIMARY KEY(id)
        );

CREATE
    TABLE
        purchase_items(
            quantity INTEGER,
            order_id VARCHAR(36) NOT NULL,
            product_id VARCHAR(36) NOT NULL,
            PRIMARY KEY(
                order_id,
                product_id
            )
        );

CREATE
    TABLE
        users(
            balance NUMERIC(
                19,
                10
            ),
            id VARCHAR(36) NOT NULL,
            password VARCHAR(60) NOT NULL,
            username VARCHAR(20) NOT NULL UNIQUE,
            PRIMARY KEY(id)
        );

ALTER TABLE
    IF EXISTS purchase_items ADD CONSTRAINT FKdcec4w10xea9gqcsp3wqns9tc FOREIGN KEY(order_id) REFERENCES orders;
