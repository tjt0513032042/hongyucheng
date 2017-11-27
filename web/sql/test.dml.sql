insert  into `check_plan`(`plan_id`,`check_date`,`shop_ids`) values

(3,'2017-11-22','2,3,1'),

(6,'2017-11-25','1'),

(7,'2017-11-23','3,1'),

(8,'2017-11-24','3,1,2');


insert  into `check_record_detail`(`record_id`,`option_code`,`option_result`) values

(1,'c001',0),

(1,'c002',1),

(1,'c003',0);

insert  into `check_records`(`record_id`,`shop_id`,`check_date`,`user_id`,`record_type`) values

(1,3,'2017-11-25 11:20:16',5,0);

insert  into `check_result`(`plan_id`,`shop_id`,`status`,`description`,`image_names`) values

(3,2,1,'检查完成，没有问题','1d2cbf.md.jpg;1d2cbf.md.jpg');

insert  into `shop_info`(`shop_id`,`shop_name`,`shop_type`,`first_person_name`,`first_person_post`,`second_person_name`,`second_person_post`,`close_shop_box`,`has_spare_key`,`spare_key_box`,`running_devices`,`first_person_phone`,`second_person_phone`,`floor`,`s_no`) values

(2,'商家2',0,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,'',''),

(3,'商家3',1,'1','a1','2','b1','321',1,'123','10,11,12','12','22','',''),

(4,'t1',0,'1','12321','','','1',1,'','','32131','','1','t1'),

(5,'t1',0,'','','','','',0,'','','','','B2','t2');


insert  into `user_info`(`id`,`name`,`role`,`phone`,`password`,`shop_id`,`active`) values

(4,'name',1,'156','123456',NULL,'Y'),

(5,'测试1',0,'12321','123456',1,'Y'),

(6,'测试2',0,'1111111','123456',NULL,'Y'),

(7,'test',1,'21','123456',NULL,'Y');