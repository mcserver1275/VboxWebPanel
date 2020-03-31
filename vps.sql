/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50562
Source Host           : localhost:3306
Source Database       : vps

Target Server Type    : MYSQL
Target Server Version : 50562
File Encoding         : 65001

Date: 2020-03-31 22:31:22
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for configure
-- ----------------------------
DROP TABLE IF EXISTS `configure`;
CREATE TABLE `configure` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '配置id',
  `vmuuid` varchar(255) DEFAULT NULL,
  `userid` int(11) DEFAULT NULL,
  `payment` varchar(255) DEFAULT NULL COMMENT '付费模式',
  `time` bigint(20) DEFAULT NULL,
  `createtime` bigint(20) DEFAULT NULL COMMENT '虚拟机创建时间',
  `examplename` varchar(255) DEFAULT NULL,
  `natport` int(11) DEFAULT '10',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for example
-- ----------------------------
DROP TABLE IF EXISTS `example`;
CREATE TABLE `example` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `cpuexecutioncap` int(11) NOT NULL DEFAULT '100' COMMENT 'CPU基准速度',
  `cpus` int(11) NOT NULL DEFAULT '1' COMMENT 'cpu数量',
  `cputype` varchar(255) DEFAULT NULL COMMENT 'CPU型号',
  `memory` int(255) DEFAULT '1024' COMMENT '虚拟机内存（MB）',
  `cpuhz` varchar(255) DEFAULT NULL,
  `price` float(10,2) DEFAULT NULL COMMENT '产品价格',
  `osid` int(11) DEFAULT '1',
  `defalutnatport` int(11) DEFAULT '10' COMMENT '默认运行虚拟机创建的端口数量',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for os
-- ----------------------------
DROP TABLE IF EXISTS `os`;
CREATE TABLE `os` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ostype` varchar(255) DEFAULT NULL COMMENT '系统类型',
  `osvdi` varchar(255) DEFAULT NULL COMMENT '系统磁盘',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for ports
-- ----------------------------
DROP TABLE IF EXISTS `ports`;
CREATE TABLE `ports` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `port` int(11) NOT NULL,
  `portname` varchar(255) DEFAULT NULL,
  `userid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `identity` varchar(255) DEFAULT 'member',
  `deposit` float DEFAULT '0' COMMENT '存款',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for vmsettings
-- ----------------------------
DROP TABLE IF EXISTS `vmsettings`;
CREATE TABLE `vmsettings` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `datapath` varchar(255) DEFAULT NULL,
  `publicip` varchar(255) DEFAULT NULL,
  `serviceregion` varchar(255) DEFAULT NULL,
  `defalutnatport` int(11) DEFAULT '10' COMMENT '给实例分配默认的创建端口',
  `portrangemin` int(11) DEFAULT '1' COMMENT '允许创建范围-最小端口',
  `portrangemax` int(11) DEFAULT '65536' COMMENT '允许创建范围-最大端口',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for websettings
-- ----------------------------
DROP TABLE IF EXISTS `websettings`;
CREATE TABLE `websettings` (
  `id` int(11) NOT NULL,
  `webtitle` varchar(255) DEFAULT NULL COMMENT '网站标题',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
