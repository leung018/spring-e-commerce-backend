CREATE
    TABLE
        orders(
            buyer_user_id VARCHAR(36),
            id VARCHAR(36) NOT NULL,
            request_id VARCHAR(36) NOT NULL,
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
            user_id VARCHAR(36) NOT NULL,
            PRIMARY KEY(id)
        );

CREATE
    TABLE
        purchase_items(
            quantity INTEGER NOT NULL,
            order_id VARCHAR(36) NOT NULL,
            product_id VARCHAR(36) NOT NULL,
            PRIMARY KEY(
                order_id,
                product_id
            ),
            CONSTRAINT FK_purchase_items_order_id FOREIGN KEY(order_id) REFERENCES orders(id)
        );

CREATE
    TABLE
        users(
            balance NUMERIC(
                19,
                10
            ) NOT NULL,
            id VARCHAR(36) NOT NULL,
            password VARCHAR(60) NOT NULL,
            username VARCHAR(50) NOT NULL UNIQUE,
            PRIMARY KEY(id)
        );
