-- MySQL dump 10.13  Distrib 5.7.24, for Linux (x86_64)
--
-- Host: 127.0.0.1    Database: xuwu
-- ------------------------------------------------------
-- Server version	5.7.24

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `community`
--

DROP TABLE IF EXISTS `community`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `community` (
  `id` bigint(10) NOT NULL AUTO_INCREMENT,
  `uid` varchar(200) DEFAULT NULL COMMENT 'md5(city-name)',
  `city` varchar(50) DEFAULT NULL COMMENT '城市',
  `name` varchar(50) DEFAULT NULL,
  `distirct` varchar(50) DEFAULT NULL COMMENT '区县',
  `landmark` varchar(50) DEFAULT NULL COMMENT '商圈',
  `buildingtype` varchar(50) DEFAULT NULL COMMENT '建筑类型',
  `buildingyear` int(4) DEFAULT NULL COMMENT '建成年份',
  `buildings` int(4) DEFAULT NULL COMMENT '总栋数',
  `houses` int(4) DEFAULT NULL COMMENT '总户数',
  `floors` int(4) DEFAULT NULL COMMENT '最高楼层',
  `elevator` tinyint(1) DEFAULT NULL COMMENT '是否有电梯',
  `volume` double(10,2) DEFAULT NULL COMMENT '容积率',
  `subway` varchar(50) DEFAULT NULL COMMENT '地铁线路:9号线,14号线(西段)',
  `subwaystation` varchar(50) DEFAULT NULL COMMENT '地铁站名',
  `latitude` double(14,5) DEFAULT NULL COMMENT '纬度',
  `longtitude` double(14,5) DEFAULT NULL COMMENT '经度',
  `source` varchar(50) DEFAULT NULL COMMENT '来源',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_community_uid` (`uid`),
  UNIQUE KEY `uk_community_city_name` (`city`,`name`)
) ENGINE=InnoDB AUTO_INCREMENT=16297 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPRESSED;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `communityrent`
--

DROP TABLE IF EXISTS `communityrent`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `communityrent` (
  `id` bigint(10) NOT NULL AUTO_INCREMENT,
  `communityuid` varchar(200) DEFAULT NULL COMMENT '小区id，对应community表的uid',
  `city` varchar(50) DEFAULT NULL COMMENT '小区城市',
  `name` varchar(50) DEFAULT NULL COMMENT '小区名字',
  `low` int(4) DEFAULT NULL COMMENT '最低价',
  `high` int(4) DEFAULT NULL COMMENT '最高价',
  `houses` int(4) DEFAULT NULL COMMENT '房子总数',
  `createdate` date DEFAULT NULL COMMENT '行情时间',
  PRIMARY KEY (`id`),
  KEY `idx_communityrent_communityuid` (`communityuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPRESSED;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `house`
--

DROP TABLE IF EXISTS `house`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `house` (
  `id` bigint(10) NOT NULL AUTO_INCREMENT,
  `city` varchar(50) DEFAULT NULL COMMENT '城市',
  `name` varchar(50) DEFAULT NULL COMMENT '名字',
  `community` varchar(255) DEFAULT NULL COMMENT '小区uid',
  `communityuid` varchar(255) DEFAULT NULL,
  `distirct` varchar(50) DEFAULT NULL COMMENT '区县',
  `landmark` varchar(50) DEFAULT NULL COMMENT '商圈',
  `room` int(4) DEFAULT '0' COMMENT '几室',
  `hall` int(4) DEFAULT '0' COMMENT '几厅',
  `toilet` int(4) DEFAULT '0' COMMENT '卫',
  `price` int(4) DEFAULT NULL COMMENT '价格元',
  `renttype` int(4) DEFAULT NULL COMMENT '0:UNKNOWN;1:JOIN;2:ALL',
  `size` int(4) DEFAULT NULL,
  `orien` varchar(50) DEFAULT NULL,
  `square` int(4) DEFAULT NULL COMMENT '面积，平方',
  `orientation` varchar(50) DEFAULT NULL COMMENT '格式:-朝向,如:-东南-南-北',
  `subway` varchar(50) DEFAULT NULL COMMENT '地铁线路:9号线,14号线(西段)',
  `subwaystation` varchar(50) DEFAULT NULL COMMENT '地铁站名',
  `latitude` double(14,5) DEFAULT NULL COMMENT '纬度',
  `longtitude` double(14,5) DEFAULT NULL COMMENT '经度',
  `exposuredate` date DEFAULT NULL COMMENT '上架时间',
  `maxfloor` int(4) DEFAULT NULL COMMENT '最高楼层',
  `floorlevel` int(4) DEFAULT NULL COMMENT '0:UNKNOWN;-1:LOW;-2:MIDDLE;-3:HIGH',
  `elevator` tinyint(1) DEFAULT NULL COMMENT '是否有电梯',
  `visitcnt` int(4) DEFAULT NULL COMMENT '看房次数',
  `createdate` date DEFAULT NULL COMMENT '创建时间',
  `source` varchar(50) DEFAULT NULL COMMENT '来源',
  PRIMARY KEY (`id`),
  KEY `idx_house_communityuid` (`community`),
  KEY `idx_house_city` (`city`)
) ENGINE=InnoDB AUTO_INCREMENT=4528465 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPRESSED;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `house_raw`
--

DROP TABLE IF EXISTS `house_raw`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `house_raw` (
  `id` varchar(255) NOT NULL,
  `city` varchar(50) DEFAULT NULL COMMENT '城市',
  `name` varchar(50) DEFAULT NULL COMMENT '名字',
  `community` varchar(255) DEFAULT NULL COMMENT '小区uid',
  `distirct` varchar(50) DEFAULT NULL COMMENT '区县',
  `landmark` varchar(50) DEFAULT NULL COMMENT '商圈',
  `room` int(4) DEFAULT '0' COMMENT '几室',
  `hall` int(4) DEFAULT '0' COMMENT '几厅',
  `toilet` int(4) DEFAULT '0' COMMENT '卫',
  `price` int(4) DEFAULT NULL COMMENT '价格元',
  `renttype` int(4) DEFAULT NULL COMMENT '0:UNKNOWN;1:JOIN;2:ALL',
  `square` int(4) DEFAULT NULL COMMENT '面积，平方',
  `orientation` varchar(50) DEFAULT NULL COMMENT '格式:-朝向,如:-东南-南-北',
  `subway` varchar(50) DEFAULT NULL COMMENT '地铁线路:9号线,14号线(西段)',
  `subwaystation` varchar(50) DEFAULT NULL COMMENT '地铁站名',
  `latitude` double(14,5) DEFAULT NULL COMMENT '纬度',
  `longtitude` double(14,5) DEFAULT NULL COMMENT '经度',
  `exposuredate` varchar(255) DEFAULT NULL COMMENT '上架时间',
  `maxfloor` int(4) DEFAULT NULL COMMENT '最高楼层',
  `floorlevel` int(4) DEFAULT NULL COMMENT '0:UNKNOWN;-1:LOW;-2:MIDDLE;-3:HIGH',
  `elevator` tinyint(1) DEFAULT NULL COMMENT '是否有电梯',
  `visitcnt` int(4) DEFAULT NULL COMMENT '看房次数',
  `createdate` date DEFAULT NULL COMMENT '创建时间',
  `source` varchar(50) DEFAULT NULL COMMENT '来源',
  KEY `idx_house_communityuid` (`community`),
  KEY `city` (`city`) USING BTREE,
  KEY `source` (`source`) USING BTREE,
  KEY `createdate` (`createdate`) USING BTREE
) ENGINE=MyISAM DEFAULT CHARSET=utf8 ROW_FORMAT=COMPRESSED;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `importlog`
--

DROP TABLE IF EXISTS `importlog`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `importlog` (
  `id` bigint(10) NOT NULL AUTO_INCREMENT,
  `source` varchar(50) DEFAULT NULL COMMENT '来源',
  `city` varchar(50) DEFAULT NULL COMMENT '城市',
  `day` int(4) DEFAULT NULL COMMENT '数据时间,20181220',
  `filename` varchar(50) DEFAULT NULL COMMENT '文件名',
  `filetime` datetime DEFAULT NULL COMMENT '文件时间',
  `total` int(4) DEFAULT NULL COMMENT '房源数',
  `createdate` datetime DEFAULT NULL COMMENT '导入时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_importlog_filename` (`filename`)
) ENGINE=InnoDB AUTO_INCREMENT=73 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPRESSED;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `properties`
--

DROP TABLE IF EXISTS `properties`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `properties` (
  `id` bigint(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(200) DEFAULT NULL,
  `value` varchar(2000) DEFAULT NULL COMMENT '配置值',
  `description` varchar(2000) DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_properties_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPRESSED;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `quote`
--

DROP TABLE IF EXISTS `quote`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `quote` (
  `code` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `high` decimal(10,0) DEFAULT NULL,
  `low` decimal(10,0) DEFAULT NULL,
  `high_sm` decimal(10,0) DEFAULT NULL,
  `avg_sm` decimal(10,0) DEFAULT NULL,
  `low_sm` decimal(10,0) DEFAULT NULL,
  `volume` int(11) DEFAULT NULL,
  `date` varchar(125) COLLATE utf8_unicode_ci DEFAULT NULL,
  `period` int(11) DEFAULT '1',
  `source` varchar(125) COLLATE utf8_unicode_ci DEFAULT '',
  `type` varchar(125) COLLATE utf8_unicode_ci DEFAULT '',
  KEY `date` (`date`),
  KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sysconfig`
--

DROP TABLE IF EXISTS `sysconfig`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sysconfig` (
  `id` bigint(10) NOT NULL AUTO_INCREMENT,
  `version` double(14,10) DEFAULT NULL COMMENT '版本号',
  `releasedate` datetime DEFAULT NULL COMMENT '发布日期',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPRESSED;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `id` bigint(10) NOT NULL AUTO_INCREMENT,
  `username` varchar(200) DEFAULT NULL,
  `password` varchar(200) DEFAULT NULL,
  `description` varchar(2000) DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPRESSED;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-02-27 11:20:56
