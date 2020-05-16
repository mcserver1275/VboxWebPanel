/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50562
Source Host           : localhost:3306
Source Database       : vps

Target Server Type    : MYSQL
Target Server Version : 50562
File Encoding         : 65001

Date: 2020-05-16 19:19:42
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
  `usestate` int(11) NOT NULL DEFAULT '1' COMMENT '使用状态',
  `osid` int(11) DEFAULT '1',
  `exampleid` int(11) NOT NULL DEFAULT '1',
  `intranetip` varchar(255) DEFAULT NULL,
  `vmname` varchar(255) DEFAULT NULL,
  `vmstate` varchar(255) DEFAULT NULL,
  `cpu` int(11) DEFAULT NULL,
  `memory` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=65 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of configure
-- ----------------------------
INSERT INTO `configure` VALUES ('42', 'a599394a-f45b-4c12-b28a-9ecf88226d8a', '3', '年付费', '1589300931', '1584891159', '通用型 g2', '9', '2', '1', '1', '', '1Cn6F2giOe', 'poweroff', '2', '2048');
INSERT INTO `configure` VALUES ('46', 'd6697d25-c9b4-425e-89e3-5205d9964eb7', '3', '月付费', '1590138668', '1587546668', '通用型 g1', '10', '1', '2', '1', '169.254.157.182', 'qN1P6aN4uZ', 'poweroff', '1', '1474');
INSERT INTO `configure` VALUES ('48', 'c2aa660f-a25c-49ea-9613-43000f33b300', '3', '月付费', '1590221549', '1587629549', '通用型 g1', '8', '1', '3', '1', '10.0.2.15', 'DRtmh7MO4z', 'aborted', '1', '1600');
INSERT INTO `configure` VALUES ('50', '8afea8f2-a253-4682-a929-0b32f21cb458', '3', '月付费', '1590759835', '1588167835', '通用型 g1', '10', '1', '1', '1', '169.254.86.62', 'efDDJMepIi', 'running', '2', '2048');
INSERT INTO `configure` VALUES ('62', '5073a0cc-67bb-405e-95ba-9129058bb106', '3', '月付费', '1594203407', '1588933007', '通用型 g1', '10', '1', '3', '1', '10.0.2.15', '3GqI26EK0k', 'aborted', '2', '2048');
INSERT INTO `configure` VALUES ('63', '46e83ec9-13c8-43db-aa4f-f598538a2852', '3', '月付费', '1591611847', '1588933447', '通用型 g2', '10', '1', '1', '2', '169.254.25.147', 'OA0p3kCTMT', 'poweroff', '3', '1066');
INSERT INTO `configure` VALUES ('64', '4a085089-5a8d-4d82-be41-612232c233c3', '1', '月付费', '1592301891', '1589623491', '通用型 g1', '10', '1', '1', '1', '169.254.101.108', 'a2TyNxW2ml', 'running', '2', '2048');

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
-- Records of example
-- ----------------------------
INSERT INTO `example` VALUES ('1', '通用型 g1', '100', '2', 'Intel Xeon(Cascade Lake) Platinum 8269CY', '2048', '2.5 GHz/3.2 GHz', '12.99', '3', '10');
INSERT INTO `example` VALUES ('2', '通用型 g2', '90', '3', 'Intel Xeon(Cascade Lake) Platinum 8269CY', '1066', '3.0GHz/5GHz', '5.00', '1', '10');

-- ----------------------------
-- Table structure for os
-- ----------------------------
DROP TABLE IF EXISTS `os`;
CREATE TABLE `os` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ostype` varchar(255) DEFAULT NULL COMMENT '系统类型',
  `osvdi` varchar(255) DEFAULT NULL COMMENT '系统磁盘',
  `defaultusername` varchar(255) DEFAULT NULL COMMENT '默认显示的系统用户名',
  `defalutpassword` varchar(255) DEFAULT NULL COMMENT '默认显示的系统密码',
  `guestusername` varchar(255) DEFAULT NULL COMMENT '来宾用户名',
  `guestpassword` varchar(255) DEFAULT NULL COMMENT '来宾密码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of os
-- ----------------------------
INSERT INTO `os` VALUES ('1', 'Windows 2008 (64)', 'D:\\虚拟机面板测试\\VirtualBox VMs\\1Cn6F2giOe\\1Cn6F2giOe.vdi', 'Administrator', 'QwQ233.', 'admin$', 'LN1275886165ln');
INSERT INTO `os` VALUES ('2', 'Windows 2016 (64)', 'D:\\VirtualBox VMs\\s2\\s2.vdi', 'Administrator', 'aaaa', 'admin$', 'LN1275886165ln');
INSERT INTO `os` VALUES ('3', 'CentOS 7 (64)', 'D:\\虚拟机面板测试\\VirtualBox VMs\\CentOS_2\\CentOS_2_1.vdi', 'Administrator', 'test123', 'root$', 'root');

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
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ports
-- ----------------------------
INSERT INTO `ports` VALUES ('6', '1236', 'xMZo_远程', '3');
INSERT INTO `ports` VALUES ('13', '4589', 'Pff0_test', '3');
INSERT INTO `ports` VALUES ('14', '4848', 'psrX_远程', '3');
INSERT INTO `ports` VALUES ('15', '4545', 'nmxf_ssh', '3');
INSERT INTO `ports` VALUES ('16', '4562', '5i8S_yuanc ', '3');
INSERT INTO `ports` VALUES ('17', '12365', 'hPkC_远程', '3');
INSERT INTO `ports` VALUES ('18', '14623', 'r8z3_mstsc', '3');
INSERT INTO `ports` VALUES ('19', '12364', 'h4av_ssh', '3');
INSERT INTO `ports` VALUES ('21', '33610', 's1g9_远程', '3');
INSERT INTO `ports` VALUES ('22', '58741', 'rYW4_远程', '3');
INSERT INTO `ports` VALUES ('23', '55765', 'siQj_远程', '3');
INSERT INTO `ports` VALUES ('24', '29595', '4qFn_远程', '1');

-- ----------------------------
-- Table structure for rechargecode
-- ----------------------------
DROP TABLE IF EXISTS `rechargecode`;
CREATE TABLE `rechargecode` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) NOT NULL DEFAULT '' COMMENT '充值码',
  `deposit` float NOT NULL COMMENT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of rechargecode
-- ----------------------------
INSERT INTO `rechargecode` VALUES ('1', 'j6bWz5m7ixlT9mVw4XVW', '1.99');
INSERT INTO `rechargecode` VALUES ('2', 'YMCOpyQE0BQnkXgwofIq', '1.99');
INSERT INTO `rechargecode` VALUES ('3', 'sQe0GDQIVEltYAN5wnOz', '1.99');
INSERT INTO `rechargecode` VALUES ('4', 'I8pYcjfxOl6zIvTjLj9e', '1.99');
INSERT INTO `rechargecode` VALUES ('6', 'tg5huXHmy5rkJSb5EXac', '1.99');
INSERT INTO `rechargecode` VALUES ('7', 'v3pgoHDC6bdzafEsIUDK', '1.99');
INSERT INTO `rechargecode` VALUES ('8', 'YpycLDye7i8pPHhrPv3j', '1.99');
INSERT INTO `rechargecode` VALUES ('9', '2ww77cpPxuHi5OKqbtXP', '1.99');
INSERT INTO `rechargecode` VALUES ('10', 'dLvaYBWTuJcM9Jhf0053', '1.99');

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
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', '123', '121', 'member', '94.03');
INSERT INTO `user` VALUES ('3', 'qwq', '121', 'admin', '71.07');
INSERT INTO `user` VALUES ('7', 'twt', '123', 'member', '0');
INSERT INTO `user` VALUES ('8', 'ning', '123', 'member', '10.68');
INSERT INTO `user` VALUES ('9', 'rty', '2333', 'member', '0');
INSERT INTO `user` VALUES ('10', 'asd', '121', 'member', '0');

-- ----------------------------
-- Table structure for vmsettings
-- ----------------------------
DROP TABLE IF EXISTS `vmsettings`;
CREATE TABLE `vmsettings` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `datapath` varchar(255) DEFAULT NULL,
  `publicip` varchar(255) DEFAULT NULL,
  `serviceregion` varchar(255) DEFAULT NULL,
  `portrangemin` int(11) DEFAULT '1' COMMENT '允许创建范围-最小端口',
  `portrangemax` int(11) DEFAULT '65536' COMMENT '允许创建范围-最大端口',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of vmsettings
-- ----------------------------
INSERT INTO `vmsettings` VALUES ('1', 'D:\\虚拟机面板测试\\', 'domain.com', '广州', '1', '65536');

-- ----------------------------
-- Table structure for websettings
-- ----------------------------
DROP TABLE IF EXISTS `websettings`;
CREATE TABLE `websettings` (
  `id` int(11) NOT NULL,
  `webtitle` varchar(255) DEFAULT NULL COMMENT '网站标题',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of websettings
-- ----------------------------
INSERT INTO `websettings` VALUES ('1', '测试云');
