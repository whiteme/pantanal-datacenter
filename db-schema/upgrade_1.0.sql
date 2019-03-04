use xuwu;
/*2019-02-27 */
alter table quote add column `city` varchar(50) DEFAULT NULL COMMENT '城市' after name;
alter table quote add column `distirct` varchar(50) DEFAULT NULL COMMENT '区县' after city;

update sysconfig set version=1.0,releasedate=now();