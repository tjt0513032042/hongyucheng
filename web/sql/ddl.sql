CREATE DATABASE /*!32312 IF NOT EXISTS*/`hongyuecheng` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `hongyuecheng`;

/*Table structure for table `check_option_info` */

DROP TABLE IF EXISTS `check_option_info`;

CREATE TABLE `check_option_info` (
  `option_id` int(11) NOT NULL AUTO_INCREMENT,
  `option_type` int(1) NOT NULL DEFAULT '0' COMMENT '0:常规, 1:专项',
  `option_name` varchar(50) NOT NULL,
  `option_code` char(50) NOT NULL,
  `option_sort` int(11) NOT NULL,
  `check_type` int(1) NOT NULL DEFAULT '0' COMMENT '0:开店, 1:闭店',
  PRIMARY KEY (`option_id`),
  UNIQUE KEY `u_code` (`option_code`)
) ENGINE=InnoDB AUTO_INCREMENT=73 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `check_plan`;

CREATE TABLE `check_plan` (
  `plan_id` int(11) NOT NULL AUTO_INCREMENT,
  `check_date` date NOT NULL,
  `shop_ids` char(100) NOT NULL,
  PRIMARY KEY (`plan_id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `check_record_detail`;

CREATE TABLE `check_record_detail` (
  `record_id` int(11) NOT NULL,
  `option_code` char(50) NOT NULL,
  `option_result` int(1) NOT NULL COMMENT '0:是，1:否'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `check_records`;

CREATE TABLE `check_records` (
  `record_id` int(11) NOT NULL AUTO_INCREMENT,
  `shop_id` int(11) NOT NULL,
  `check_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `user_id` int(11) NOT NULL,
  `record_type` int(1) NOT NULL COMMENT '0:开店, 1:闭店',
  PRIMARY KEY (`record_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `check_result`;

CREATE TABLE `check_result` (
  `plan_id` int(11) NOT NULL,
  `shop_id` int(11) NOT NULL,
  `status` int(1) NOT NULL DEFAULT '0' COMMENT '0:待抽查,1:已抽查',
  `description` varchar(200) DEFAULT NULL COMMENT '抽查结果描述',
  `image_names` varchar(500) DEFAULT NULL COMMENT '上传图片在服务器的文件名称'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `device_info`;

CREATE TABLE `device_info` (
  `device_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '设备id',
  `device_name` varchar(20) NOT NULL COMMENT '设备名称',
  `device_type` int(1) NOT NULL COMMENT '设备类型:0 办公类;1 安全防范类;2 照明;3 保鲜类;4 生鲜水池类;5 其他',
  PRIMARY KEY (`device_id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `shop_info`;

CREATE TABLE `shop_info` (
  `shop_id` int(11) NOT NULL AUTO_INCREMENT,
  `shop_name` varchar(100) DEFAULT NULL,
  `shop_type` int(1) DEFAULT NULL,
  `first_person_name` varchar(20) DEFAULT NULL COMMENT '第一责任人',
  `first_person_post` varchar(30) DEFAULT NULL COMMENT '职务',
  `second_person_name` varchar(20) DEFAULT NULL COMMENT '第二责任人',
  `second_person_post` varchar(30) DEFAULT NULL COMMENT '职务',
  `close_shop_box` varchar(50) DEFAULT NULL COMMENT '闭店表箱号',
  `has_spare_key` tinyint(1) DEFAULT '0' COMMENT '是否有备用钥匙',
  `spare_key_box` varchar(50) DEFAULT NULL COMMENT '备用钥匙箱号',
  `running_devices` char(200) DEFAULT NULL COMMENT '闭店后仍需运行的设备',
  `first_person_phone` varchar(11) DEFAULT NULL COMMENT '第一责任人号码',
  `second_person_phone` varchar(11) DEFAULT NULL COMMENT '第二责任人号码',
  `floor` char(3) NOT NULL,
  `s_no` char(20) NOT NULL,
  PRIMARY KEY (`shop_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `user_info`;

CREATE TABLE `user_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) NOT NULL,
  `role` int(2) NOT NULL,
  `phone` char(11) NOT NULL,
  `password` char(8) NOT NULL DEFAULT '123456',
  `shop_id` int(11) DEFAULT NULL,
  `active` char(1) NOT NULL DEFAULT 'Y',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;