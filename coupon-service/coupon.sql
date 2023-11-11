-- Create coupon table
CREATE TABLE coupon (
                        id BIGSERIAL PRIMARY KEY,
                        category VARCHAR(11),
                        publish VARCHAR(11),
                        coupon_img VARCHAR(524),
                        coupon_title VARCHAR(256),
                        price DECIMAL(16,2),
                        user_limit INT,
                        start_time TIMESTAMP,
                        end_time TIMESTAMP,
                        publish_count INT,
                        stock INT DEFAULT 0,
                        create_time TIMESTAMP,
                        condition_price DECIMAL(16,2)
);

-- Comments for coupon table
COMMENT ON COLUMN coupon.id IS 'id';
COMMENT ON COLUMN coupon.category IS 'Coupon type [NEW_USER registration gift coupon, TASK task coupon, PROMOTION promotional coupon]';
COMMENT ON COLUMN coupon.publish IS 'Publish status, PUBLISH published, DRAFT draft, OFFLINE offline';
COMMENT ON COLUMN coupon.coupon_img IS 'Coupon image';
COMMENT ON COLUMN coupon.coupon_title IS 'Coupon title';
COMMENT ON COLUMN coupon.price IS 'Discount price';
COMMENT ON COLUMN coupon.user_limit IS 'Limit per person';
COMMENT ON COLUMN coupon.start_time IS 'Coupon start time';
COMMENT ON COLUMN coupon.end_time IS 'Coupon expiration time';
COMMENT ON COLUMN coupon.publish_count IS 'Total number of coupons';
COMMENT ON COLUMN coupon.stock IS 'Stock';
COMMENT ON COLUMN coupon.create_time IS 'Creation time of the coupon';
COMMENT ON COLUMN coupon.condition_price IS 'Minimum amount required to use';

-- Create coupon_record table
CREATE TABLE coupon_record (
                               id BIGSERIAL PRIMARY KEY,
                               coupon_id BIGINT,
                               create_time TIMESTAMP,
                               use_state VARCHAR(32),
                               user_id BIGINT,
                               user_name VARCHAR(128),
                               coupon_title VARCHAR(256),
                               start_time TIMESTAMP,
                               end_time TIMESTAMP,
                               order_id BIGINT,
                               price DECIMAL(16,2),
                               condition_price DECIMAL(16,2)
);

-- Comments for coupon_record table
COMMENT ON COLUMN coupon_record.id IS 'id';
COMMENT ON COLUMN coupon_record.coupon_id IS 'Coupon id';
COMMENT ON COLUMN coupon_record.create_time IS 'Time of creation or acquisition';
COMMENT ON COLUMN coupon_record.use_state IS 'Usage status: available NEW, used USED, expired EXPIRED';
COMMENT ON COLUMN coupon_record.user_id IS 'User id';
COMMENT ON COLUMN coupon_record.user_name IS 'User nickname';
COMMENT ON COLUMN coupon_record.coupon_title IS 'Coupon title';
COMMENT ON COLUMN coupon_record.start_time IS 'Start time';
COMMENT ON COLUMN coupon_record.end_time IS 'End time';
COMMENT ON COLUMN coupon_record.order_id IS 'Order id';
COMMENT ON COLUMN coupon_record.price IS 'Discount price';
COMMENT ON COLUMN coupon_record.condition_price IS 'Minimum amount required to use';


INSERT INTO coupon (id, category, publish, coupon_img, coupon_title, price, user_limit, start_time, end_time, publish_count, stock, create_time, condition_price)
VALUES
    (18, 'NEW_USER', 'PUBLISH', 'https://file.xdclass.net/video/2020/alibabacloud/zt-alibabacloud.png', 'Permanently Valid - New User Registration - 0 Yuan Minimum Spending- 5 Yuan Discount Coupon - Limited to 2 per Person - Non-stackable Use', 5.00, 2, '2000-01-01 00:00:00', '2099-01-29 00:00:00', 100000000, 99999991, '2020-12-26 16:33:02', 0.00),
    (19, 'PROMOTION', 'PUBLISH', 'https://file.xdclass.net/video/2020/alibabacloud/zt-alibabacloud.png', 'Valid - From January 2021 to January 2025 - 20 Yuan Minimum Spending - 5 Yuan Discount Coupon - Limited to 2 per Person - Non-stackable Use', 5.00, 2, '2000-01-29 00:00:00', '2025-01-29 00:00:00', 10, 3, '2020-12-26 16:33:03', 20.00),
    (22, 'PROMOTION', 'PUBLISH', 'https://file.xdclass.net/video/2020/alibabacloud/zt-alibabacloud.png', 'Expired - From August 2020 to September 2020 - Product ID 3 - 6 Yuan Discount Coupon - Limited to 1 per Person - Stackable Use', 6.00, 1, '2020-08-01 00:00:00', '2020-09-29 00:00:00', 100, 100, '2020-12-26 16:33:03', 0.00),
    (20, 'PROMOTION', 'PUBLISH', 'https://file.xdclass.net/video/2020/alibabacloud/zt-alibabacloud.png', 'Valid - From August 2020 to September 2021 - Product ID 1 - 8.8 Yuan Discount Coupon - Limited to 2 per Person - Non-stackable Use', 8.80, 2, '2020-08-01 00:00:00', '2021-09-29 00:00:00', 100, 96, '2020-12-26 16:33:03',0.00),
    (21, 'PROMOTION', 'PUBLISH', 'https://file.xdclass.net/video/2020/alibabacloud/zt-alibabacloud.png', 'Valid - From August 2020 to September 2021 - Product ID 2 - 9.9 Yuan Discount Coupon - Limited to 2 per Person - Stackable Use', 8.80, 2, '2020-08-01 00:00:00', '2021-09-29 00:00:00', 100, 96, '2020-12-26 16:33:03', 0.00);
