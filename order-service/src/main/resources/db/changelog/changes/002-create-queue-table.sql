--liquibase formatted sql

--changeset hieuhatrung:003
CREATE TABLE queues (
    id BIGSERIAL PRIMARY KEY,
    shop_id BIGINT NOT NULL,
    name VARCHAR(50) NOT NULL,
    max_size INT NOT NULL DEFAULT 50,
    current_size INT NOT NULL DEFAULT 0,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

--changeset hieuhatrung:004
CREATE TABLE queue_orders (
    id BIGSERIAL PRIMARY KEY,
    queue_id BIGINT NOT NULL,
    order_id BIGINT NOT NULL,
    position INT NOT NULL,
    entry_time TIMESTAMP NOT NULL,
    FOREIGN KEY (queue_id) REFERENCES queues(id),
    FOREIGN KEY (order_id) REFERENCES orders(id)
);

--changeset hieuhatrung:005
CREATE INDEX idx_queue_orders_queue_id ON queue_orders(queue_id);
CREATE INDEX idx_queue_orders_order_id ON queue_orders(order_id); 