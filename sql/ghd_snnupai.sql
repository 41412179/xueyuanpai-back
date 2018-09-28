/*
Navicat MySQL Data Transfer

Source Server         : snnupai
Source Server Version : 50641
Source Host           : 193.112.27.81:3306
Source Database       : ghd_snnupai

Target Server Type    : MYSQL
Target Server Version : 50641
File Encoding         : 65001

Date: 2018-09-28 14:16:25
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for article
-- ----------------------------
DROP TABLE IF EXISTS `article`;
CREATE TABLE `article` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `content` varchar(255) DEFAULT '' COMMENT '文章内容',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `delete_status` varchar(1) DEFAULT '1' COMMENT '是否有效  1.有效  2无效',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8 COMMENT='发布号作者表';

-- ----------------------------
-- Records of article
-- ----------------------------
INSERT INTO `article` VALUES ('5', '莎士比亚', '2017-10-25 09:08:45', '2017-10-30 17:59:41', '1');
INSERT INTO `article` VALUES ('6', '亚里士多德', '2017-10-26 10:49:28', '2017-11-18 09:54:15', '1');
INSERT INTO `article` VALUES ('10', '亚历山大', '2017-10-26 14:57:45', '2017-11-08 13:28:52', '1');
INSERT INTO `article` VALUES ('11', '李白', '2017-10-26 15:23:42', '2017-10-26 15:23:42', '1');
INSERT INTO `article` VALUES ('19', '文章test2', '2017-11-18 13:37:07', '2017-11-18 13:37:11', '1');

-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `content` text NOT NULL COMMENT '评论内容',
  `entity_id` varchar(50) NOT NULL COMMENT '帖子id或者comment的id',
  `anonymous` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '是否匿名',
  `remarks` varchar(128) DEFAULT NULL COMMENT '备注',
  `ban_kuai_type` int(11) NOT NULL COMMENT '板块类型',
  `entity_type` int(10) NOT NULL COMMENT '评论的对象类型',
  `head_url` varchar(160) NOT NULL COMMENT '显示评论时的头像url',
  `nick_name` varchar(64) NOT NULL COMMENT '显示评论时的昵称',
  `created_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `created_by` bigint(20) NOT NULL COMMENT '创建人id',
  `update_by` bigint(20) NOT NULL COMMENT '更新人id',
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '删除状态，0表示正常，1表示删除',
  `post_id` varchar(128) NOT NULL COMMENT '该条评论属于哪个帖子',
  PRIMARY KEY (`id`),
  KEY `entity_index` (`entity_id`,`entity_type`)
) ENGINE=InnoDB AUTO_INCREMENT=61 DEFAULT CHARSET=utf8 COMMENT='评论表';

-- ----------------------------
-- Records of comment
-- ----------------------------
INSERT INTO `comment` VALUES ('58', '我来自评一个', '35', '0', null, '2', '5', 'http://images.nowcoder.com/head/526t.png', 'PLifer', '2018-05-14 16:06:53', '2018-05-14 16:06:53', '1229', '1229', '0', '35');
INSERT INTO `comment` VALUES ('59', '我再来自评一个', '35', '0', null, '2', '5', 'http://images.nowcoder.com/head/526t.png', 'PLifer', '2018-05-14 16:10:22', '2018-05-14 16:10:22', '1229', '1229', '0', '35');
INSERT INTO `comment` VALUES ('60', 'sdgfsdf', 'd34465c1502c48fa9d651c352a71e3b4', '1', null, '1', '1', 'http://images.nowcoder.com/head/547t.png', '杨过', '2018-06-28 12:39:46', '2018-06-28 12:39:46', '1229', '1229', '0', 'd34465c1502c48fa9d651c352a71e3b4');

-- ----------------------------
-- Table structure for feed
-- ----------------------------
DROP TABLE IF EXISTS `feed`;
CREATE TABLE `feed` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `event_type` int(11) NOT NULL,
  `event_id` bigint(20) NOT NULL,
  `data` tinytext,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=101 DEFAULT CHARSET=utf8 COMMENT='动态';

-- ----------------------------
-- Records of feed
-- ----------------------------

-- ----------------------------
-- Table structure for gong_gao
-- ----------------------------
DROP TABLE IF EXISTS `gong_gao`;
CREATE TABLE `gong_gao` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `content` text NOT NULL,
  `created_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='公告表';

-- ----------------------------
-- Records of gong_gao
-- ----------------------------
INSERT INTO `gong_gao` VALUES ('1', '这是测试公告', '2018-04-11 22:37:10');
INSERT INTO `gong_gao` VALUES ('2', '这是新公告', '2018-04-11 22:40:15');
INSERT INTO `gong_gao` VALUES ('3', '欢迎大家加入qq群一起交流，反馈bug和问题建议，群号713204055', '2018-04-12 13:29:33');

-- ----------------------------
-- Table structure for image
-- ----------------------------
DROP TABLE IF EXISTS `image`;
CREATE TABLE `image` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `entity_type` int(11) NOT NULL,
  `entity_id` varchar(64) NOT NULL,
  `url` varchar(256) NOT NULL,
  `del_status` int(11) NOT NULL COMMENT '0表示没有删除，1表示删除',
  `created_date` datetime NOT NULL COMMENT '创建日期',
  `created_by` bigint(20) NOT NULL COMMENT '创建人id',
  `updated_date` datetime NOT NULL,
  `updated_by` bigint(20) NOT NULL COMMENT '更新人的id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8 COMMENT='图片表';

-- ----------------------------
-- Records of image
-- ----------------------------
INSERT INTO `image` VALUES ('7', '1', 'c9caa35dddda40e8bb1c8815a91b3067', 'http://p7bv5h9bw.bkt.clouddn.com/FoEeAkmXCLQ00yt8sdmTD3JK9yBu', '0', '2018-05-04 16:57:45', '1224', '2018-05-04 16:57:45', '1224');
INSERT INTO `image` VALUES ('8', '1', '47bc21a4e9804eb88b8685c40b14e32a', 'http://p7bv5h9bw.bkt.clouddn.com/FqA8fj-1Kg_Y5LkVg3igPOCSqFC3', '0', '2018-05-06 10:27:40', '1224', '2018-05-06 10:27:40', '1224');
INSERT INTO `image` VALUES ('9', '1', '81c423f91bf74a2ab993c4435052c3ef', 'http://p7bv5h9bw.bkt.clouddn.com/FqufojoTLXyoXf2w3YXpanQCW8MC', '0', '2018-05-06 10:52:49', '1225', '2018-05-06 10:52:49', '1225');
INSERT INTO `image` VALUES ('10', '1', '81c423f91bf74a2ab993c4435052c3ef', 'http://p7bv5h9bw.bkt.clouddn.com/Fn8kpIZwMFdy1q7dCUjX_3IbT0zt', '0', '2018-05-06 10:52:53', '1225', '2018-05-06 10:52:53', '1225');
INSERT INTO `image` VALUES ('11', '1', '81c423f91bf74a2ab993c4435052c3ef', 'http://p7bv5h9bw.bkt.clouddn.com/FhmNKPaSN3OSyvPtsgwYLhCayece', '0', '2018-05-06 10:52:53', '1225', '2018-05-06 10:52:53', '1225');
INSERT INTO `image` VALUES ('12', '1', 'e517371557f44cba9b8bdbea5214a0b5', 'http://p7bv5h9bw.bkt.clouddn.com/FhmNKPaSN3OSyvPtsgwYLhCayece', '0', '2018-05-06 10:57:31', '1225', '2018-05-06 10:57:31', '1225');
INSERT INTO `image` VALUES ('13', '1', 'e517371557f44cba9b8bdbea5214a0b5', 'http://p7bv5h9bw.bkt.clouddn.com/FkMMdUUOxtMLWu6hf56hz4GYirTH', '0', '2018-05-06 10:57:31', '1225', '2018-05-06 10:57:31', '1225');
INSERT INTO `image` VALUES ('14', '1', '21a98a66b83140c582890cec75a5cd3e', 'http://p7bv5h9bw.bkt.clouddn.com/Fh3THA0a9EkqkUzHWJBo_n5cum5j', '0', '2018-05-06 22:30:01', '1225', '2018-05-06 22:30:01', '1225');
INSERT INTO `image` VALUES ('15', '1', '07c6087b6cee4615a37a7fd66f150b7e', 'http://p7bv5h9bw.bkt.clouddn.com/Fh3THA0a9EkqkUzHWJBo_n5cum5j', '0', '2018-05-06 22:30:04', '1225', '2018-05-06 22:30:04', '1225');
INSERT INTO `image` VALUES ('16', '1', '21a98a66b83140c582890cec75a5cd3e', 'http://p7bv5h9bw.bkt.clouddn.com/Fr3YiRmvERKq8OMh40F0Qf8W5HCv', '0', '2018-05-06 22:30:06', '1225', '2018-05-06 22:30:06', '1225');
INSERT INTO `image` VALUES ('17', '1', '07c6087b6cee4615a37a7fd66f150b7e', 'http://p7bv5h9bw.bkt.clouddn.com/Fr3YiRmvERKq8OMh40F0Qf8W5HCv', '0', '2018-05-06 22:30:08', '1225', '2018-05-06 22:30:08', '1225');
INSERT INTO `image` VALUES ('18', '1', 'ee2ed5fc65b84f07bf265d5f42ab1b08', 'http://p7bv5h9bw.bkt.clouddn.com/Fp_GtbtU97tpD7JpFFZ3dWS2mNJl', '0', '2018-05-10 09:01:48', '1225', '2018-05-10 09:01:48', '1225');
INSERT INTO `image` VALUES ('19', '1', 'ee2ed5fc65b84f07bf265d5f42ab1b08', 'http://p7bv5h9bw.bkt.clouddn.com/FokfXMGQoZzVgx7-Fop00coN_vve', '0', '2018-05-10 09:01:48', '1225', '2018-05-10 09:01:48', '1225');
INSERT INTO `image` VALUES ('20', '1', 'a781077800cc40d9838aa80764b8492f', 'http://p7bv5h9bw.bkt.clouddn.com/Fp_GtbtU97tpD7JpFFZ3dWS2mNJl', '0', '2018-05-10 09:02:27', '1225', '2018-05-10 09:02:27', '1225');
INSERT INTO `image` VALUES ('21', '1', 'a781077800cc40d9838aa80764b8492f', 'http://p7bv5h9bw.bkt.clouddn.com/FokfXMGQoZzVgx7-Fop00coN_vve', '0', '2018-05-10 09:02:27', '1225', '2018-05-10 09:02:27', '1225');
INSERT INTO `image` VALUES ('22', '1', 'd34465c1502c48fa9d651c352a71e3b4', 'http://p7bv5h9bw.bkt.clouddn.com/FluQEs7-490gmwDgeREhGvv2VBVC', '0', '2018-05-28 19:37:22', '1228', '2018-05-28 19:37:22', '1228');
INSERT INTO `image` VALUES ('23', '1', '9132d86feb61444aaebdeef869910c43', 'http://p7bv5h9bw.bkt.clouddn.com/FvX4rSaBmkcTGNJGMfpQVQNnEqh-', '0', '2018-06-27 12:37:29', '1229', '2018-06-27 12:37:29', '1229');

-- ----------------------------
-- Table structure for login_ticket
-- ----------------------------
DROP TABLE IF EXISTS `login_ticket`;
CREATE TABLE `login_ticket` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `ticket` varchar(45) NOT NULL,
  `expired` datetime NOT NULL,
  `status` int(11) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `ticket_unique` (`ticket`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='ticket表';

-- ----------------------------
-- Records of login_ticket
-- ----------------------------

-- ----------------------------
-- Table structure for love
-- ----------------------------
DROP TABLE IF EXISTS `love`;
CREATE TABLE `love` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `content` text NOT NULL,
  `anonymous` int(11) NOT NULL DEFAULT '0' COMMENT '0表示匿名,1表示不匿名',
  `created_date` datetime NOT NULL,
  `updated_date` datetime NOT NULL,
  `status` int(11) NOT NULL DEFAULT '0',
  `anonymous_nick_name` varchar(16) DEFAULT NULL COMMENT '如果是匿名发布，则保留匿名名称',
  `anonymous_head_url` varchar(320) DEFAULT NULL COMMENT '如果是匿名发布，保留匿名发布时的头像url',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=67 DEFAULT CHARSET=utf8 COMMENT='love表';

-- ----------------------------
-- Records of love
-- ----------------------------
INSERT INTO `love` VALUES ('66', '2', 'pmm，我爱你...嘿嘿，匿名说吧，实名有点不好意思。', '1', '2018-09-24 18:03:15', '2018-09-24 18:03:15', '0', '韩林儿', 'http://images.nowcoder.com/head/490t.png');

-- ----------------------------
-- Table structure for message
-- ----------------------------
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `from_id` bigint(20) NOT NULL,
  `to_id` bigint(20) NOT NULL,
  `created_date` datetime NOT NULL,
  `has_read` int(11) NOT NULL DEFAULT '0',
  `conversation_id` varchar(128) NOT NULL,
  `content` text,
  PRIMARY KEY (`id`),
  KEY `conversation_index` (`conversation_id`),
  KEY `created_date` (`created_date`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8 COMMENT='私信表';

-- ----------------------------
-- Records of message
-- ----------------------------
INSERT INTO `message` VALUES ('1', '1000', '1001', '2018-09-15 03:10:29', '0', '1000_1001', 'message');
INSERT INTO `message` VALUES ('2', '1000', '1001', '2018-09-15 03:10:30', '0', '1000_1001', 'message');
INSERT INTO `message` VALUES ('3', '1000', '1001', '2018-09-15 03:10:30', '0', '1000_1001', 'message');
INSERT INTO `message` VALUES ('4', '1000', '1001', '2018-09-15 03:10:30', '0', '1000_1001', 'message');
INSERT INTO `message` VALUES ('5', '1001', '1002', '2018-09-15 03:10:30', '0', '1001_1002', 'message');
INSERT INTO `message` VALUES ('6', '1001', '1002', '2018-09-15 03:10:30', '0', '1001_1002', 'message');
INSERT INTO `message` VALUES ('7', '1001', '1002', '2018-09-15 03:10:30', '0', '1001_1002', 'message');
INSERT INTO `message` VALUES ('8', '1001', '1002', '2018-09-15 03:10:30', '0', '1001_1002', 'message');
INSERT INTO `message` VALUES ('9', '1002', '1003', '2018-09-15 03:10:30', '0', '1002_1003', 'message');
INSERT INTO `message` VALUES ('10', '1002', '1003', '2018-09-15 03:10:31', '0', '1002_1003', 'message');
INSERT INTO `message` VALUES ('11', '1002', '1003', '2018-09-15 03:10:31', '0', '1002_1003', 'message');
INSERT INTO `message` VALUES ('12', '1002', '1003', '2018-09-15 03:10:31', '0', '1002_1003', 'message');

-- ----------------------------
-- Table structure for person_signature
-- ----------------------------
DROP TABLE IF EXISTS `person_signature`;
CREATE TABLE `person_signature` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `content` text NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='个人签名';

-- ----------------------------
-- Records of person_signature
-- ----------------------------

-- ----------------------------
-- Table structure for post
-- ----------------------------
DROP TABLE IF EXISTS `post`;
CREATE TABLE `post` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `title` varchar(128) NOT NULL,
  `content` text NOT NULL,
  `created_date` datetime NOT NULL,
  `updated_date` datetime NOT NULL,
  `status` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='post表';

-- ----------------------------
-- Records of post
-- ----------------------------

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `rid` int(11) NOT NULL AUTO_INCREMENT,
  `rname` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`rid`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COMMENT='已经废弃';

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES ('3', '管理员');
INSERT INTO `role` VALUES ('4', '版主');
INSERT INTO `role` VALUES ('5', '普通用户');

-- ----------------------------
-- Table structure for sys_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission` (
  `id` int(11) NOT NULL DEFAULT '0' COMMENT '自定id,主要供前端展示权限列表分类排序使用.',
  `menu_code` varchar(255) DEFAULT '' COMMENT '归属菜单,前端判断并展示菜单使用,',
  `menu_name` varchar(255) DEFAULT '' COMMENT '菜单的中文释义',
  `permission_code` varchar(255) DEFAULT '' COMMENT '权限的代码/通配符,对应代码中@RequiresPermissions 的value',
  `permission_name` varchar(255) DEFAULT '' COMMENT '本权限的中文释义',
  `required_permission` tinyint(1) DEFAULT '2' COMMENT '是否本菜单必选权限, 1.必选 2非必选 通常是"列表"权限是必选',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='后台权限表';

-- ----------------------------
-- Records of sys_permission
-- ----------------------------
INSERT INTO `sys_permission` VALUES ('101', 'article', '文章管理', 'article:list', '列表', '1');
INSERT INTO `sys_permission` VALUES ('102', 'article', '文章管理', 'article:add', '新增', '2');
INSERT INTO `sys_permission` VALUES ('103', 'article', '文章管理', 'article:update', '修改', '2');
INSERT INTO `sys_permission` VALUES ('601', 'user', '用户', 'user:list', '列表', '1');
INSERT INTO `sys_permission` VALUES ('602', 'user', '用户', 'user:add', '新增', '2');
INSERT INTO `sys_permission` VALUES ('603', 'user', '用户', 'user:update', '修改', '2');
INSERT INTO `sys_permission` VALUES ('701', 'role', '角色权限', 'role:list', '列表', '1');
INSERT INTO `sys_permission` VALUES ('702', 'role', '角色权限', 'role:add', '新增', '2');
INSERT INTO `sys_permission` VALUES ('703', 'role', '角色权限', 'role:update', '修改', '2');
INSERT INTO `sys_permission` VALUES ('704', 'role', '角色权限', 'role:delete', '删除', '2');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(20) DEFAULT NULL COMMENT '角色名',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `delete_status` varchar(1) DEFAULT '1' COMMENT '是否有效  1有效  2无效',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='角色表';

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES ('1', '管理员', '2017-11-22 16:24:34', '2017-11-22 16:24:52', '1');
INSERT INTO `sys_role` VALUES ('2', '用户', '2017-11-22 16:24:34', '2018-09-15 23:12:15', '1');
INSERT INTO `sys_role` VALUES ('3', '版主', '2017-11-22 16:28:47', '2018-09-15 23:12:33', '1');

-- ----------------------------
-- Table structure for sys_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_permission`;
CREATE TABLE `sys_role_permission` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) DEFAULT NULL COMMENT '角色id',
  `permission_id` int(11) DEFAULT NULL COMMENT '权限id',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `delete_status` varchar(1) DEFAULT '1' COMMENT '是否有效 1有效     2无效',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8 COMMENT='角色-权限关联表';

-- ----------------------------
-- Records of sys_role_permission
-- ----------------------------
INSERT INTO `sys_role_permission` VALUES ('1', '2', '101', '2017-11-22 16:26:21', '2017-11-22 16:26:32', '1');
INSERT INTO `sys_role_permission` VALUES ('2', '2', '102', '2017-11-22 16:26:21', '2017-11-22 16:26:32', '1');
INSERT INTO `sys_role_permission` VALUES ('5', '2', '602', '2017-11-22 16:28:28', '2017-11-22 16:28:28', '1');
INSERT INTO `sys_role_permission` VALUES ('6', '2', '601', '2017-11-22 16:28:28', '2017-11-22 16:28:28', '1');
INSERT INTO `sys_role_permission` VALUES ('7', '2', '603', '2017-11-22 16:28:28', '2017-11-22 16:28:28', '1');
INSERT INTO `sys_role_permission` VALUES ('8', '2', '703', '2017-11-22 16:28:28', '2017-11-22 16:28:28', '1');
INSERT INTO `sys_role_permission` VALUES ('9', '2', '701', '2017-11-22 16:28:28', '2017-11-22 16:28:28', '1');
INSERT INTO `sys_role_permission` VALUES ('10', '2', '702', '2017-11-22 16:28:28', '2017-11-22 16:28:28', '1');
INSERT INTO `sys_role_permission` VALUES ('11', '2', '704', '2017-11-22 16:28:31', '2017-11-22 16:28:31', '1');
INSERT INTO `sys_role_permission` VALUES ('12', '2', '103', '2017-11-22 16:28:31', '2017-11-22 16:28:31', '1');
INSERT INTO `sys_role_permission` VALUES ('13', '3', '601', '2017-11-22 16:28:47', '2017-11-22 16:28:47', '1');
INSERT INTO `sys_role_permission` VALUES ('14', '3', '701', '2017-11-22 16:28:47', '2017-11-22 16:28:47', '1');
INSERT INTO `sys_role_permission` VALUES ('15', '3', '702', '2017-11-22 16:35:01', '2017-11-22 16:35:01', '1');
INSERT INTO `sys_role_permission` VALUES ('16', '3', '704', '2017-11-22 16:35:01', '2017-11-22 16:35:01', '1');
INSERT INTO `sys_role_permission` VALUES ('17', '3', '102', '2017-11-22 16:35:01', '2017-11-22 16:35:01', '1');
INSERT INTO `sys_role_permission` VALUES ('18', '3', '101', '2017-11-22 16:35:01', '2017-11-22 16:35:01', '1');
INSERT INTO `sys_role_permission` VALUES ('19', '3', '603', '2017-11-22 16:35:01', '2017-11-22 16:35:01', '1');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) DEFAULT NULL COMMENT '用户名',
  `password` varchar(255) DEFAULT NULL COMMENT '密码',
  `nickname` varchar(255) DEFAULT NULL COMMENT '昵称',
  `role_id` int(11) DEFAULT '0' COMMENT '角色ID',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `delete_status` varchar(1) DEFAULT '1' COMMENT '是否有效  1有效  2无效',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10008 DEFAULT CHARSET=utf8 COMMENT='废弃，运营后台用户表，所有信息在user表中可以找到';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('10003', 'admin', '123456', '超级用户23', '1', '2017-10-30 11:52:38', '2017-11-17 23:51:40', '1');
INSERT INTO `sys_user` VALUES ('10004', 'user', '123456', '莎士比亚', '2', '2017-10-30 16:13:02', '2017-11-18 02:48:24', '1');
INSERT INTO `sys_user` VALUES ('10005', 'aaa', '123456', 'abba', '1', '2017-11-15 14:02:56', '2017-11-17 23:51:42', '1');
INSERT INTO `sys_user` VALUES ('10007', 'test', '123456', '就看看列表', '3', '2017-11-22 16:29:41', '2017-11-22 16:29:41', '1');

-- ----------------------------
-- Table structure for trade
-- ----------------------------
DROP TABLE IF EXISTS `trade`;
CREATE TABLE `trade` (
  `id` varchar(50) NOT NULL COMMENT 'trade_id',
  `del_flag` int(11) NOT NULL COMMENT '是否被发布者删除',
  `status` int(11) NOT NULL COMMENT '出售状态，0表示未出售，1表示已出售，2表示已过期',
  `title` varchar(160) NOT NULL,
  `owner_id` bigint(20) NOT NULL COMMENT '发布信息人的id',
  `xiaoQu` int(11) NOT NULL COMMENT '0表示长安校区，1表示雁塔校区',
  `anonymous` int(11) NOT NULL DEFAULT '0' COMMENT '0表示匿名,1表示不匿名',
  `content` text NOT NULL COMMENT '交易内容',
  `qq` varchar(16) DEFAULT NULL COMMENT 'qq',
  `weixin` varchar(64) DEFAULT NULL COMMENT 'weixin',
  `contacter` varchar(32) DEFAULT NULL COMMENT '联系人',
  `created_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `price` int(11) NOT NULL,
  `updated_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `anonymous_nick_name` varchar(50) DEFAULT NULL COMMENT '匿名时发布者的昵称',
  `anonymous_head_url` varchar(64) DEFAULT NULL COMMENT '匿名时的头像url',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='跳蚤市场交易表';

-- ----------------------------
-- Records of trade
-- ----------------------------

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `real_name` varchar(32) NOT NULL DEFAULT '''''' COMMENT '真实姓名',
  `nick_name` varchar(16) NOT NULL DEFAULT '''昵称''' COMMENT '昵称',
  `password` varchar(64) DEFAULT '''''' COMMENT '密码',
  `salt` varchar(64) DEFAULT NULL,
  `phone` varchar(64) NOT NULL DEFAULT '''''' COMMENT '手机号',
  `description` varchar(64) NOT NULL DEFAULT '''''' COMMENT '个人描述',
  `major` varchar(64) NOT NULL DEFAULT '''''' COMMENT '专业',
  `email` varchar(64) DEFAULT NULL COMMENT 'email',
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '是否被封号',
  `sex` tinyint(4) NOT NULL DEFAULT '0' COMMENT '默认是女，1表示男',
  `register_status` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '注册状态',
  `acc_points` int(11) NOT NULL DEFAULT '0' COMMENT '个人积分',
  `vip` int(11) NOT NULL DEFAULT '0' COMMENT '是否是vip会员',
  `birth_year` int(11) NOT NULL DEFAULT '1998' COMMENT '出生年份',
  `head_url` varchar(200) NOT NULL DEFAULT '''https://ss0.baidu.com/-Po3dSag_xI4khGko9WTAnF6hhy/image/h%3D300/sign=dcb5bee7b88f8c54fcd3c32f0a282dee/c9fcc3cec3fdfc03e1a19bb1d83f8794a5c226d9.jpg''' COMMENT '头像url',
  `entrance_year` int(11) NOT NULL DEFAULT '2016' COMMENT '入学年份',
  `created_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `sys_role_id` int(1) unsigned NOT NULL DEFAULT '2' COMMENT '2代表默认角色，用户',
  PRIMARY KEY (`id`),
  UNIQUE KEY `nick_name_index` (`nick_name`),
  UNIQUE KEY `phone_index` (`phone`),
  UNIQUE KEY `email_index` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='用户表';

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', '', '17600172104', 'W+uBrx/ngk7CIP9MbIZUcQ==', 'b472d', '17600172104', '', '', '17600172104@qq.com', '0', '1', '1', '0', '0', '1997', 'http://images.nowcoder.com/head/667t.png', '2014', '2018-09-18 12:26:17', '2018-09-18 12:26:17', '2');
INSERT INTO `user` VALUES ('2', '', 'test1', 'l6caL+76j1z5hYgYTt828w==', '857a7', '17600172123', '', '', 'liming1234@qq.com', '0', '1', '1', '0', '0', '1997', 'http://images.nowcoder.com/head/908t.png', '2014', '2018-09-24 18:00:06', '2018-09-24 18:00:06', '2');
