CREATE
    TABLE
        orders(
            buyer_user_id VARCHAR(255),
            id VARCHAR(255) NOT NULL,
            request_id VARCHAR(255),
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
            ) CHECK(
                price >= 0
            ),
            quantity INTEGER NOT NULL CHECK(
                quantity >= 0
            ),
            id VARCHAR(255) NOT NULL,
            name VARCHAR(255) NOT NULL,
            user_id VARCHAR(255),
            PRIMARY KEY(id)
        );

CREATE
    TABLE
        purchase_items(
            quantity INTEGER,
            order_id VARCHAR(255) NOT NULL,
            product_id VARCHAR(255) NOT NULL,
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
            ) CHECK(
                balance >= 0
            ),
            id VARCHAR(255) NOT NULL,
            password VARCHAR(255) NOT NULL,
            username VARCHAR(255) NOT NULL UNIQUE,
            PRIMARY KEY(id)
        );

ALTER TABLE
    IF EXISTS purchase_items ADD CONSTRAINT FKdcec4w10xea9gqcsp3wqns9tc FOREIGN KEY(order_id) REFERENCES orders;
