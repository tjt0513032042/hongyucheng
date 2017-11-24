drop table if exists hongyuecheng.check_record;

/*==============================================================*/
/* Table: check_record                                          */
/*==============================================================*/
create table hongyuecheng.check_record
(
   id                   int(11) not null auto_increment,
   shop_id              int(11) comment '商家ID',
   check_id             int(11),
   check_value          varchar(64) default NULL,
   check_date           timestamp null default NULL,
   primary key (id)
)DEFAULT CHARSET=utf8;


insert into hongyuecheng.check_record(shop_id,check_id,check_value,check_date)
 values
(1,1,1,'2017-11-25'),
(1,2,0,'2017-11-25'),
(1,3,0,'2017-11-25'),
(1,4,1,'2017-11-25'),
(1,1,1,'2017-11-26'),
(1,2,0,'2017-11-26'),
(1,3,0,'2017-11-26'),
(1,4,1,'2017-11-26'),
(2,1,1,'2017-11-25'),
(2,2,0,'2017-11-25'),
(2,3,0,'2017-11-25'),
(2,4,1,'2017-11-25'),
(2,1,1,'2017-11-27'),
(2,2,0,'2017-11-27'),
(2,3,0,'2017-11-27'),
(2,4,1,'2017-11-27');