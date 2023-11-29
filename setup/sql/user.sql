USE Users;
CREATE TABLE `users` (
    `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT,
    `name` varchar(128) DEFAULT NULL COMMENT '昵称',
    `pwd` varchar(124) DEFAULT NULL COMMENT '密码',
    `head_img` varchar(524) DEFAULT NULL COMMENT '头像',
    `slogan` varchar(524) DEFAULT NULL COMMENT '用户签名',
    `sex` tinyint(2) DEFAULT '1' COMMENT '0表示女，1表示男',
    `points` int(10) DEFAULT '0' COMMENT '积分',
    `create_time` datetime DEFAULT NULL,
    `mail` varchar(64) DEFAULT NULL COMMENT '邮箱',
    `secret` varchar(12) DEFAULT NULL COMMENT '盐，用于个人敏感信息处理',
    PRIMARY KEY (`id`),
    UNIQUE KEY `mail_idx` (`mail`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE `address` (
    `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
    `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
    `default_status` int(1) DEFAULT NULL COMMENT '是否默认收货地址：0->否；1->是',
    `receive_name` varchar(64) DEFAULT NULL COMMENT '收发货人姓名',
    `phone` varchar(64) DEFAULT NULL COMMENT '收货人电话',
    `province` varchar(64) DEFAULT NULL COMMENT '省/直辖市',
    `city` varchar(64) DEFAULT NULL COMMENT '市',
    `region` varchar(64) DEFAULT NULL COMMENT '区',
    `detail_address` varchar(200) DEFAULT NULL COMMENT '详细地址',
    `create_time` datetime DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8mb4 COMMENT='电商-公司收发货地址表';

CREATE TABLE `undo_log` (
                            `id` bigint(20) NOT NULL AUTO_INCREMENT,
                            `branch_id` bigint(20) NOT NULL,
                            `xid` varchar(100) NOT NULL,
                            `context` varchar(128) NOT NULL,
                            `rollback_info` longblob NOT NULL,
                            `log_status` int(11) NOT NULL,
                            `log_created` datetime NOT NULL,
                            `log_modified` datetime NOT NULL,
                            PRIMARY KEY (`id`),
                            UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;