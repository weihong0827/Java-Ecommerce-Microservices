Use Product;
CREATE TABLE `banner` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `img` varchar(524) DEFAULT NULL COMMENT '图片',
  `url` varchar(524) DEFAULT NULL COMMENT '跳转地址',
  `weight` int(11) DEFAULT NULL COMMENT '权重',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4;

INSERT INTO `banner` (`id`, `img`, `url`, `weight`)
VALUES
    (1, 'https://file.xdclass.net/video/2020/alibabacloud/zx-lbt.jpeg', 'https://m.xdclass.net/#/member', 1),
    (2, 'https://file.xdclass.net/video/%E5%AE%98%E7%BD%91%E8%BD%AE%E6%92%AD%E5%9B%BE/20%E5%B9%B4%E5%8F%8C11%E9%98%BF%E9%87%8C%E4%BA%91/fc-lbt.jpeg', 'https://www.aliyun.com/1111/pintuan-share?ptCode=MTcwMTY3MzEyMjc5MDU2MHx8MTE0fDE%3D&userCode=r5saexap', 3),
    (3, 'https://file.xdclass.net/video/%E5%AE%98%E7%BD%91%E8%BD%AE%E6%92%AD%E5%9B%BE/20%E5%B9%B4%E5%8F%8C11%E9%98%BF%E9%87%8C%E4%BA%91/FAN-lbu-vip.jpeg', 'https://file.xdclass.net/video/%E5%AE%98%E7%BD%91%E8%BD%AE%E6%92%AD%E5%9B%BE/Nginx.jpeg', 2);



CREATE TABLE `product` (
   `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT,
   `title` varchar(128) DEFAULT NULL COMMENT '标题',
   `cover_img` varchar(128) DEFAULT NULL COMMENT '封面图',
   `detail` varchar(256) DEFAULT '' COMMENT '详情',
   `old_price` decimal(16,2) DEFAULT NULL COMMENT '老价格',
   `price` decimal(16,2) DEFAULT NULL COMMENT '新价格',
   `stock` int(11) DEFAULT NULL COMMENT '库存',
   `create_time` datetime DEFAULT NULL COMMENT '创建时间',
   `lock_stock` int(11) DEFAULT '0' COMMENT '锁定库存',
   PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4;


INSERT INTO `product` (`id`, `title`, `cover_img`, `detail`, `old_price`, `price`, `stock`, `create_time`, `lock_stock`)
VALUES
    (1, '小滴课堂抱枕', 'https://file.xdclass.net/video/2020/alibabacloud/zt-alibabacloud.png', 'https://file.xdclass.net/video/2021/60-MLS/summary.jpeg', 32.00, 213.00, 100, '2021-09-12 00:00:00', 31),
    (2, '技术人的杯子Linux', 'https://file.xdclass.net/video/2020/alibabacloud/zt-alibabacloud.png', 'https://file.xdclass.net/video/2021/59-Postman/summary.jpeg', 432.00, 42.00, 20, '2021-03-12 00:00:00', 2),
    (3, '技术人的杯子docker', 'https://file.xdclass.net/video/2020/alibabacloud/zt-alibabacloud.png', 'https://file.xdclass.net/video/2021/60-MLS/summary.jpeg', 35.00, 12.00, 20, '2022-09-22 00:00:00', 13),
    (4, '技术人的杯子git', 'https://file.xdclass.net/video/2020/alibabacloud/zt-alibabacloud.png', 'https://file.xdclass.net/video/2021/60-MLS/summary.jpeg', 12.00, 14.00, 20, '2022-11-12 00:00:00', 2);
