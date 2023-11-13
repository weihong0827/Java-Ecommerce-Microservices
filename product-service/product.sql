CREATE TABLE banner (
                        id serial PRIMARY KEY,
                        img varchar(524) DEFAULT NULL, -- Image
                        url varchar(524) DEFAULT NULL, -- Jump Address
                        weight int DEFAULT NULL -- Weight
);

COMMENT ON COLUMN banner.img IS 'Image';
COMMENT ON COLUMN banner.url IS 'Jump Address';
COMMENT ON COLUMN banner.weight IS 'Weight';

INSERT INTO banner (id, img, url, weight)
VALUES
    (1, 'https://file.xdclass.net/video/2020/alibabacloud/zx-lbt.jpeg', 'https://m.xdclass.net/#/member', 1),
    (2, 'https://file.xdclass.net/video/%E5%AE%98%E7%BD%91%E8%BD%AE%E6%92%AD%E5%9B%BE/20%E5%B9%B4%E5%8F%8C11%E9%98%BF%E9%87%8C%E4%BA%91/fc-lbt.jpeg', 'https://www.aliyun.com/1111/pintuan-share?ptCode=MTcwMTY3MzEyMjc5MDU2MHx8MTE0fDE%3D&userCode=r5saexap', 3),
    (3, 'https://file.xdclass.net/video/%E5%AE%98%E7%BD%91%E8%BD%AE%E6%92%AD%E5%9B%BE/20%E5%B9%B4%E5%8F%8C11%E9%98%BF%E9%87%8C%E4%BA%91/FAN-lbu-vip.jpeg', 'https://file.xdclass.net/video/%E5%AE%98%E7%BD%91%E8%BD%AE%E6%92%AD%E5%9B%BE/Nginx.jpeg', 2);
CREATE TABLE product (
                         id bigserial PRIMARY KEY,
                         title varchar(128) DEFAULT NULL, -- Title
                         cover_img varchar(128) DEFAULT NULL, -- Cover Image
                         detail varchar(256) DEFAULT '' , -- Details
                         old_price decimal(16,2) DEFAULT NULL, -- Old Price
                         price decimal(16,2) DEFAULT NULL, -- New Price
                         stock int DEFAULT NULL, -- Stock
                         create_time timestamp DEFAULT NULL, -- Creation Time
                         lock_stock int DEFAULT '0' -- Locked Stock
);

COMMENT ON COLUMN product.title IS 'Title';
COMMENT ON COLUMN product.cover_img IS 'Cover Image';
COMMENT ON COLUMN product.detail IS 'Details';
COMMENT ON COLUMN product.old_price IS 'Old Price';
COMMENT ON COLUMN product.price IS 'New Price';
COMMENT ON COLUMN product.stock IS 'Stock';
COMMENT ON COLUMN product.create_time IS 'Creation Time';
COMMENT ON COLUMN product.lock_stock IS 'Locked Stock';

INSERT INTO product (id, title, cover_img, detail, old_price, price, stock, create_time, lock_stock)
VALUES
    (1, 'Xiaodi Classroom Pillow', 'https://file.xdclass.net/video/2020/alibabacloud/zt-alibabacloud.png', 'https://file.xdclass.net/video/2021/60-MLS/summary.jpeg', 32.00, 213.00, 100, '2021-09-12 00:00:00', 31),
    (2, 'Tech Person Cup Linux', 'https://file.xdclass.net/video/2020/alibabacloud/zt-alibabacloud.png', 'https://file.xdclass.net/video/2021/59-Postman/summary.jpeg', 432.00, 42.00, 20, '2021-03-12 00:00:00', 2),
    (3, 'Tech Person Cup Docker', 'https://file.xdclass.net/video/2020/alibabacloud/zt-alibabacloud.png', 'https://file.xdclass.net/video/2021/60-MLS/summary.jpeg', 35.00, 12.00, 20, '2022-09-22 00:00:00', 13),
    (4, 'Tech Person Cup Git', 'https://file.xdclass.net/video/2020/alibabacloud/zt-alibabacloud.png', 'https://file.xdclass.net/video/2021/60-MLS/summary.jpeg', 12.00, 14.00, 20, '2022-11-12 00:00:00', 2);
