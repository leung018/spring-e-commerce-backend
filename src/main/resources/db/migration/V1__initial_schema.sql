CREATE
    TABLE
        orders(
            buyer_user_id UUID NOT NULL,
            id UUID NOT NULL,
            request_id UUID NOT NULL,
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
            ) NOT NULL,
            quantity INTEGER NOT NULL,
            id UUID NOT NULL,
            name VARCHAR(255) NOT NULL,
            user_id UUID NOT NULL,
            PRIMARY KEY(id)
        );

CREATE
    TABLE
        purchase_items(
            quantity INTEGER NOT NULL,
            order_id UUID NOT NULL,
            product_id UUID NOT NULL,
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
            id UUID NOT NULL,
            password VARCHAR(60) NOT NULL,
            username VARCHAR(20) NOT NULL UNIQUE,
            PRIMARY KEY(id)
        );
