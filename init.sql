/*
 Navicat Premium Data Transfer

 Source Server         : web
 Source Server Type    : MySQL
 Source Server Version : 80031
 Source Host           : localhost:3306
 Source Schema         : storage

 Target Server Type    : MySQL
 Target Server Version : 80031
 File Encoding         : 65001

 Date: 10/06/2024 16:08:38
*/

USE storage

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for check
-- ----------------------------
DROP TABLE IF EXISTS `check`;
CREATE TABLE `check`  (
  `check_id` int(0) NOT NULL AUTO_INCREMENT COMMENT '一次盘点单的ID',
  `operator_id` int(0) NULL DEFAULT NULL COMMENT '盘点操作员的ID',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '这次盘点的创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '这次盘点的更新时间',
  PRIMARY KEY (`check_id`) USING BTREE,
  INDEX `operator_id`(`operator_id`) USING BTREE,
  CONSTRAINT `operator_id` FOREIGN KEY (`operator_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of check
-- ----------------------------

-- ----------------------------
-- Table structure for check_details
-- ----------------------------
DROP TABLE IF EXISTS `check_details`;
CREATE TABLE `check_details`  (
  `check_id` int(0) NOT NULL COMMENT '盘点单的ID',
  `product_id` int(0) NOT NULL COMMENT '盘点的具体商品的ID',
  `location_id` int(0) NOT NULL COMMENT '盘点的具体商品的库位ID',
  `check_number_theory` int(0) NOT NULL COMMENT '此商品的理论存在数量',
  `check_number` int(0) NOT NULL COMMENT '此商品的实际存在数量',
  PRIMARY KEY (`check_id`, `product_id`, `location_id`) USING BTREE,
  INDEX `product_id`(`product_id`) USING BTREE,
  INDEX `location_id1`(`location_id`) USING BTREE,
  CONSTRAINT `check_id` FOREIGN KEY (`check_id`) REFERENCES `check` (`check_id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `location_id1` FOREIGN KEY (`location_id`) REFERENCES `location` (`location_id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `product_id` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of check_details
-- ----------------------------

-- ----------------------------
-- Table structure for inbound
-- ----------------------------
DROP TABLE IF EXISTS `inbound`;
CREATE TABLE `inbound`  (
  `inbound_order_id` int(0) NOT NULL AUTO_INCREMENT COMMENT '入库订单的ID',
  `inbound_order_description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '对于该入库订单的描述',
  `manager_id` int(0) NOT NULL COMMENT '发出此入库订单的经理ID',
  `operator_id` int(0) NOT NULL DEFAULT 0 COMMENT '处理此入库订单的操作员ID',
  `create_time` datetime(0) NOT NULL COMMENT '该入库订单的创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '该入库订单的完成时间',
  `isfinished` int(0) NOT NULL DEFAULT 0 COMMENT '0代表未完成，1代表已经完成',
  PRIMARY KEY (`inbound_order_id`) USING BTREE,
  INDEX `manager_id`(`manager_id`) USING BTREE,
  INDEX `operator_id2`(`operator_id`) USING BTREE,
  CONSTRAINT `manager_id` FOREIGN KEY (`manager_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `operator_id2` FOREIGN KEY (`operator_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of inbound
-- ----------------------------

-- ----------------------------
-- Table structure for inbound_details
-- ----------------------------
DROP TABLE IF EXISTS `inbound_details`;
CREATE TABLE `inbound_details`  (
  `inbound_order_id` int(0) NOT NULL COMMENT '入库订单的ID',
  `product_id` int(0) NOT NULL COMMENT '此订单入库的商品ID',
  `location_id` int(0) NOT NULL COMMENT '此订单入库的商品，摆放的库位ID',
  `production_date` date NOT NULL COMMENT '此订单商品的过期日期',
  `inbound_number_theoretical` int(0) NOT NULL COMMENT '此订单商品的理论入库数量',
  `inbound_number_ture` int(0) NULL DEFAULT NULL COMMENT '此订单商品的实际入库数量',
  PRIMARY KEY (`inbound_order_id`, `product_id`, `location_id`, `production_date`) USING BTREE,
  INDEX `location_id`(`location_id`) USING BTREE,
  INDEX `product_id2`(`product_id`) USING BTREE,
  CONSTRAINT `inbound_oder_id` FOREIGN KEY (`inbound_order_id`) REFERENCES `inbound` (`inbound_order_id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `location_id` FOREIGN KEY (`location_id`) REFERENCES `location` (`location_id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `product_id2` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of inbound_details
-- ----------------------------

-- ----------------------------
-- Table structure for location
-- ----------------------------
DROP TABLE IF EXISTS `location`;
CREATE TABLE `location`  (
  `location_id` int(0) NOT NULL AUTO_INCREMENT COMMENT '库位ID',
  `capacity` float NOT NULL COMMENT '总容量',
  `capacity_use` float NOT NULL COMMENT '已经使用的容量',
  PRIMARY KEY (`location_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of location
-- ----------------------------
INSERT INTO `location` VALUES (1, 100, 0);

-- ----------------------------
-- Table structure for outbound
-- ----------------------------
DROP TABLE IF EXISTS `outbound`;
CREATE TABLE `outbound`  (
  `outbound_order_id` int(0) NOT NULL AUTO_INCREMENT COMMENT '出库订单的ID',
  `outbound_order_description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '对于该出库订单的描述',
  `manager_id` int(0) NOT NULL COMMENT '发出此出库订单的经理ID',
  `operator_id` int(0) NOT NULL COMMENT '处理此出库订单的操作员ID',
  `create_time` datetime(0) NOT NULL COMMENT '该出库订单的创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '该出库订单的更新时间',
  `isfinished` int(0) NOT NULL COMMENT '0代表未完成，1代表已经完成',
  PRIMARY KEY (`outbound_order_id`) USING BTREE,
  INDEX `manager_id1`(`manager_id`) USING BTREE,
  INDEX `operator_id1`(`operator_id`) USING BTREE,
  CONSTRAINT `manager_id1` FOREIGN KEY (`manager_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `operator_id1` FOREIGN KEY (`operator_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of outbound
-- ----------------------------

-- ----------------------------
-- Table structure for outbound_details
-- ----------------------------
DROP TABLE IF EXISTS `outbound_details`;
CREATE TABLE `outbound_details`  (
  `outbound_order_id` int(0) NOT NULL COMMENT '出库订单的ID',
  `product_id` int(0) NOT NULL COMMENT '此订单出库的商品ID',
  `outbound_number` int(0) NOT NULL COMMENT '此订单商品的出库数量',
  PRIMARY KEY (`outbound_order_id`, `product_id`) USING BTREE,
  INDEX `product_id1`(`product_id`) USING BTREE,
  CONSTRAINT `outbound_order_id` FOREIGN KEY (`outbound_order_id`) REFERENCES `outbound` (`outbound_order_id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `product_id1` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of outbound_details
-- ----------------------------

-- ----------------------------
-- Table structure for product
-- ----------------------------
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product`  (
  `product_id` int(0) NOT NULL AUTO_INCREMENT COMMENT '商品ID',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商品的描述',
  `provider` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '供应商',
  `cost` float(10, 2) NOT NULL COMMENT '成本',
  `price` float(10, 2) NOT NULL COMMENT '售价',
  PRIMARY KEY (`product_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of product
-- ----------------------------
INSERT INTO `product` VALUES (1, '可口可乐', '可口可乐公司', 2.00, 3.00);
INSERT INTO `product` VALUES (2, '旺仔牛奶', '旺仔', 5.00, 10.00);

-- ----------------------------
-- Table structure for stock
-- ----------------------------
DROP TABLE IF EXISTS `stock`;
CREATE TABLE `stock`  (
  `product_id` int(0) NOT NULL COMMENT '商品ID',
  `production_date` date NOT NULL COMMENT '商品的过期日期',
  `location_id` int(0) NOT NULL COMMENT '商品所在的库位ID',
  `number` int(0) NOT NULL COMMENT '商品在仓库中目前的数量（同一生产日期，同一库位是一条记录）',
  `status` int(0) NULL DEFAULT NULL COMMENT '是否过期，0代表未过期，1代表已经过期',
  PRIMARY KEY (`product_id`, `production_date`, `location_id`) USING BTREE,
  INDEX `location_id3`(`location_id`) USING BTREE,
  CONSTRAINT `location_id3` FOREIGN KEY (`location_id`) REFERENCES `location` (`location_id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `product_id3` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of stock
-- ----------------------------
INSERT INTO `stock` VALUES (1, '2024-06-05', 1, 5, 1);
INSERT INTO `stock` VALUES (1, '2024-10-08', 1, 10, 1);
INSERT INTO `stock` VALUES (2, '2024-04-03', 1, 50, 1);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `user_id` int(0) NOT NULL AUTO_INCREMENT COMMENT '用户的ID',
  `user_nickname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '账号（工号）',
  `user_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户的名字',
  `user_password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户的密码',
  `user_type` int(0) NOT NULL COMMENT '用户的类别，0代表经理，1代表操作员，2代表系统管理员',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------

-- ----------------------------
-- Event structure for check_expired_products
-- ----------------------------
DROP EVENT IF EXISTS `check_expired_products`;
delimiter ;;
CREATE EVENT `check_expired_products`
ON SCHEDULE
EVERY '1' SECOND STARTS '2024-06-06 16:23:02'
DO UPDATE stock
  SET status = 1
  WHERE production_date < NOW()
;;
delimiter ;

SET FOREIGN_KEY_CHECKS = 1;
