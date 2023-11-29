CREATE TABLE product_order (
                               id BIGSERIAL PRIMARY KEY,
                               out_trade_no VARCHAR(64),
                               state VARCHAR(11),
                               create_time TIMESTAMP WITHOUT TIME ZONE,
                               total_amount DECIMAL(16,2),
                               pay_amount DECIMAL(16,2),
                               pay_type VARCHAR(64),
                               nickname VARCHAR(64),
                               head_img VARCHAR(524),
                               user_id INTEGER,
                               del INTEGER DEFAULT '0',
                               update_time TIMESTAMP WITHOUT TIME ZONE,
                               order_type VARCHAR(32),
                               receiver_address VARCHAR(1024)
);

CREATE TABLE product_order_item (
                                    id BIGSERIAL PRIMARY KEY,
                                    product_order_id BIGINT,
                                    out_trade_no VARCHAR(32),
                                    product_id BIGINT,
                                    product_name VARCHAR(128),
                                    product_img VARCHAR(524),
                                    buy_num INTEGER,
                                    create_time TIMESTAMP WITHOUT TIME ZONE,
                                    total_amount DECIMAL(16,2),
                                    amount DECIMAL(16,0)
);
COMMENT ON COLUMN product_order.out_trade_no IS 'Order unique identifier';
COMMENT ON COLUMN product_order.state IS 'NEW for unpaid order, PAY for paid order, CANCEL for order canceled due to timeout';
COMMENT ON COLUMN product_order.create_time IS 'Order creation time';
COMMENT ON COLUMN product_order.total_amount IS 'Total order amount';
COMMENT ON COLUMN product_order.pay_amount IS 'Actual order payment price';
COMMENT ON COLUMN product_order.pay_type IS 'Payment type, such as WeChat, Bank, Alipay';
COMMENT ON COLUMN product_order.nickname IS 'Nickname';
COMMENT ON COLUMN product_order.head_img IS 'Avatar';
COMMENT ON COLUMN product_order.user_id IS 'User id';
COMMENT ON COLUMN product_order.del IS '0 for not deleted, 1 for deleted';
COMMENT ON COLUMN product_order.update_time IS 'Update time';
COMMENT ON COLUMN product_order.order_type IS 'Order type DAILY for regular, PROMOTION for promotional';
COMMENT ON COLUMN product_order.receiver_address IS 'Delivery address stored in JSON';

COMMENT ON COLUMN product_order_item.product_order_id IS 'Order number';
COMMENT ON COLUMN product_order_item.product_id IS 'Product id';
COMMENT ON COLUMN product_order_item.product_name IS 'Product name';
COMMENT ON COLUMN product_order_item.product_img IS 'Product image';
COMMENT ON COLUMN product_order_item.buy_num IS 'Purchase quantity';
COMMENT ON COLUMN product_order_item.total_amount IS 'Total price of shopping item';
COMMENT ON COLUMN product_order_item.amount IS 'Unit price of shopping item';
