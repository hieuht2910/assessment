--liquibase formatted sql

--changeset hieuhatrung:001
CREATE TABLE IF NOT EXISTS orders (
    id BIGSERIAL PRIMARY KEY,
    customer_name VARCHAR(255) NOT NULL,
    order_details TEXT,
    order_time TIMESTAMP NOT NULL,
    status VARCHAR(50) NOT NULL
);

--changeset hieuhatrung:002
CREATE INDEX IF NOT EXISTS idx_orders_status ON orders(status);

--rollback DROP TABLE orders;
--rollback DROP INDEX idx_orders_status; 