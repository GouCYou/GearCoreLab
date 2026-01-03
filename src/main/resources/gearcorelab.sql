/*
 Navicat Premium Dump SQL

 Source Server         : localdb
 Source Server Type    : MySQL
 Source Server Version : 80041 (8.0.41)
 Source Host           : localhost:3306
 Source Schema         : gearcorelab

 Target Server Type    : MySQL
 Target Server Version : 80041 (8.0.41)
 File Encoding         : 65001

 Date: 18/06/2025 15:04:18
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for comment_images
-- ----------------------------
DROP TABLE IF EXISTS `comment_images`;
CREATE TABLE `comment_images`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `comment_id` bigint NOT NULL,
  `image_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_comment_images_comment`(`comment_id` ASC) USING BTREE,
  CONSTRAINT `comment_images_ibfk_1` FOREIGN KEY (`comment_id`) REFERENCES `comments` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of comment_images
-- ----------------------------

-- ----------------------------
-- Table structure for comment_likes
-- ----------------------------
DROP TABLE IF EXISTS `comment_likes`;
CREATE TABLE `comment_likes`  (
  `comment_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`comment_id`, `user_id`) USING BTREE,
  INDEX `fk_cl_user`(`user_id` ASC) USING BTREE,
  CONSTRAINT `fk_cl_comment` FOREIGN KEY (`comment_id`) REFERENCES `comments` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `fk_cl_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of comment_likes
-- ----------------------------
INSERT INTO `comment_likes` VALUES (51, 6, '2025-06-18 10:15:00');

-- ----------------------------
-- Table structure for comments
-- ----------------------------
DROP TABLE IF EXISTS `comments`;
CREATE TABLE `comments`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `post_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `image_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `created_at` datetime NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id` ASC) USING BTREE,
  INDEX `comments_ibfk_1`(`post_id` ASC) USING BTREE,
  CONSTRAINT `comments_ibfk_1` FOREIGN KEY (`post_id`) REFERENCES `posts` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `comments_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 53 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of comments
-- ----------------------------
INSERT INTO `comments` VALUES (50, 39, 12, '厉害', NULL, '2025-06-18 10:14:36');
INSERT INTO `comments` VALUES (51, 39, 6, '我很喜欢', NULL, '2025-06-18 10:14:47');
INSERT INTO `comments` VALUES (52, 39, 13, '我很帅', '/uploads/7bc85cc5820548a2b5e7b6a1942d9402.png', '2025-06-18 11:11:17');

-- ----------------------------
-- Table structure for hardware_components
-- ----------------------------
DROP TABLE IF EXISTS `hardware_components`;
CREATE TABLE `hardware_components`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '产品名称',
  `brand` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '品牌',
  `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '组件类型',
  `category` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '细分类别',
  `specifications` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '规格描述(JSON格式)',
  `price` decimal(10, 2) NULL DEFAULT NULL COMMENT '价格',
  `image_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '产品图片URL',
  `is_active` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否启用',
  `sort_order` int NOT NULL DEFAULT 0 COMMENT '排序顺序',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_hardware_type`(`type` ASC) USING BTREE,
  INDEX `idx_hardware_brand`(`brand` ASC) USING BTREE,
  INDEX `idx_hardware_active`(`is_active` ASC) USING BTREE,
  INDEX `idx_hardware_sort`(`sort_order` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 134 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '硬件组件表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of hardware_components
-- ----------------------------
INSERT INTO `hardware_components` VALUES (1, 'AMD Ryzen 7 9950X3D', 'AMD', 'cpu', 'cpu', '8核16线程 4.5GHz', 2299.00, NULL, 1, 1, '2025-06-16 22:57:50', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (2, 'Intel Core i7-13700K', 'Intel', 'cpu', '13th Gen', '{\"generation\":\"Raptor Lake\",\"socket\":\"LGA1700\",\"cores\":16,\"threads\":24,\"baseClock\":\"3.4GHz\",\"boostClock\":\"5.4GHz\"}', 2599.00, NULL, 0, 2, '2025-06-16 22:57:50', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (3, 'MSI MAG B650 TOMAHAWK WIFI', 'MSI', 'motherboard', 'B650', '{\"chipset\":\"B650\",\"socket\":\"AM5\",\"memoryType\":\"DDR5\",\"maxMemory\":\"128GB\",\"memorySlots\":4}', 1299.00, NULL, 1, 3, '2025-06-16 22:57:50', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (4, 'ASUS ROG STRIX Z790-E GAMING WIFI', 'ASUS', 'motherboard', 'Z790', '{\"chipset\":\"Z790\",\"socket\":\"LGA1700\",\"memoryType\":\"DDR5\",\"maxMemory\":\"128GB\",\"memorySlots\":4}', 2899.00, NULL, 1, 4, '2025-06-16 22:57:50', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (5, 'G.SKILL Trident Z5 RGB 32GB', 'G.SKILL', 'ram', 'DDR5', '{\"type\":\"DDR5\",\"speed\":6000,\"capacity\":32,\"modules\":2,\"timing\":\"CL36\"}', 1199.00, NULL, 1, 5, '2025-06-16 22:57:50', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (6, '海盗船 Vengeance LPX 16GB', '海盗船', 'ram', 'DDR4', '{\"type\":\"DDR4\",\"speed\":3200,\"capacity\":16,\"modules\":2,\"timing\":\"CL16\"}', 399.00, NULL, 1, 6, '2025-06-16 22:57:50', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (7, 'NVIDIA GeForce RTX 4070 Ti', 'NVIDIA', 'gpu', 'RTX 40系', '{\"series\":\"RTX 4070 Ti\",\"memory\":12,\"memoryType\":\"GDDR6X\",\"powerConsumption\":285}', 4999.00, NULL, 1, 7, '2025-06-16 22:57:50', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (8, 'AMD Radeon RX 7800 XT', 'AMD', 'gpu', 'RX 7000系', '{\"series\":\"RX 7800 XT\",\"memory\":16,\"memoryType\":\"GDDR6\",\"powerConsumption\":263}', 3999.00, NULL, 1, 8, '2025-06-16 22:57:50', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (9, '三星 980 PRO 1TB', '三星', 'storage', 'NVMe SSD', '{\"type\":\"NVMe SSD\",\"capacity\":1024,\"interface\":\"M.2\",\"readSpeed\":7000,\"writeSpeed\":5000}', 699.00, NULL, 1, 9, '2025-06-16 22:57:50', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (10, '西数 WD Blue 2TB', '西数', 'storage', 'HDD', '{\"type\":\"HDD\",\"capacity\":2048,\"interface\":\"SATA\",\"rpm\":7200}', 399.00, NULL, 1, 10, '2025-06-16 22:57:50', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (11, '海盗船 RM850x', '海盗船', 'psu', '全模组', '{\"wattage\":850,\"efficiency\":\"80+ Gold\",\"modular\":true,\"warranty\":10}', 899.00, NULL, 1, 11, '2025-06-16 22:57:50', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (12, '安钛克 NE650G', '安钛克', 'psu', '半模组', '{\"wattage\":650,\"efficiency\":\"80+ Gold\",\"modular\":false,\"warranty\":7}', 459.00, NULL, 1, 12, '2025-06-16 22:57:50', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (13, '追风者 P400A', '追风者', 'case', '中塔', '{\"formFactor\":\"Mid-Tower\",\"maxGPULength\":420,\"maxCPUCoolerHeight\":160,\"fans\":3}', 599.00, NULL, 1, 13, '2025-06-16 22:57:50', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (14, '酷冷至尊 MasterBox TD500', '酷冷至尊', 'case', '中塔', '{\"formFactor\":\"Mid-Tower\",\"maxGPULength\":410,\"maxCPUCoolerHeight\":165,\"fans\":3}', 699.00, NULL, 1, 14, '2025-06-16 22:57:50', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (15, 'Intel Core i9-13900K', 'Intel', 'cpu', '第13代酷睿', '{\"cores\": 24, \"threads\": 32, \"base_clock\": \"3.0GHz\", \"boost_clock\": \"5.8GHz\", \"socket\": \"LGA1700\"}', 4299.00, '/img/hardware/cpu-i9-13900k.jpg', 1, 1, '2025-06-17 00:46:24', '2025-06-17 00:46:24');
INSERT INTO `hardware_components` VALUES (16, 'AMD Ryzen 9 7950X', 'AMD', 'cpu', 'Ryzen 7000系列', '{\"cores\": 16, \"threads\": 32, \"base_clock\": \"4.5GHz\", \"boost_clock\": \"5.7GHz\", \"socket\": \"AM5\"}', 4599.00, '/img/hardware/cpu-ryzen-7950x.jpg', 1, 2, '2025-06-17 00:46:24', '2025-06-17 00:46:24');
INSERT INTO `hardware_components` VALUES (17, 'NVIDIA GeForce RTX 4090', 'NVIDIA', 'gpu', 'RTX 40系列', '{\"memory\": \"24GB GDDR6X\", \"cuda_cores\": 16384, \"base_clock\": \"2230MHz\", \"boost_clock\": \"2520MHz\"}', 12999.00, '/img/hardware/gpu-rtx-4090.jpg', 1, 1, '2025-06-17 00:46:24', '2025-06-17 00:46:24');
INSERT INTO `hardware_components` VALUES (18, 'AMD Radeon RX 7900 XTX', 'AMD', 'gpu', 'RX 7000系列', '{\"memory\": \"24GB GDDR6\", \"stream_processors\": 6144, \"game_clock\": \"2300MHz\", \"boost_clock\": \"2500MHz\"}', 7999.00, '/img/hardware/gpu-rx-7900xtx.jpg', 1, 2, '2025-06-17 00:46:24', '2025-06-17 00:46:24');
INSERT INTO `hardware_components` VALUES (19, 'ASUS ROG STRIX Z790-E', 'ASUS', 'motherboard', 'Z790芯片组', '{\"socket\": \"LGA1700\", \"memory_slots\": 4, \"max_memory\": \"128GB\", \"pcie_slots\": \"4x PCIe 5.0\"}', 2899.00, '/img/hardware/mb-z790-e.jpg', 1, 1, '2025-06-17 00:46:24', '2025-06-17 00:46:24');
INSERT INTO `hardware_components` VALUES (20, 'MSI MAG B650 TOMAHAWK', 'MSI', 'motherboard', 'B650芯片组', '{\"socket\": \"AM5\", \"memory_slots\": 4, \"max_memory\": \"128GB\", \"pcie_slots\": \"2x PCIe 5.0\"}', 1299.00, '/img/hardware/mb-b650-tomahawk.jpg', 1, 2, '2025-06-17 00:46:24', '2025-06-17 00:46:24');
INSERT INTO `hardware_components` VALUES (21, 'Corsair Vengeance DDR5-5600 32GB', 'Corsair', 'ram', 'DDR5内存', '{\"capacity\": \"32GB\", \"speed\": \"DDR5-5600\", \"timings\": \"CL36\", \"voltage\": \"1.25V\"}', 1299.00, '/img/hardware/ram-corsair-ddr5.jpg', 1, 1, '2025-06-17 00:46:24', '2025-06-17 00:46:24');
INSERT INTO `hardware_components` VALUES (22, 'G.SKILL Trident Z5 DDR5-6000 32GB', 'G.SKILL', 'ram', 'DDR5内存', '{\"capacity\": \"32GB\", \"speed\": \"DDR5-6000\", \"timings\": \"CL30\", \"voltage\": \"1.35V\"}', 1599.00, '/img/hardware/ram-gskill-ddr5.jpg', 1, 2, '2025-06-17 00:46:24', '2025-06-17 00:46:24');
INSERT INTO `hardware_components` VALUES (23, 'Samsung 980 PRO 2TB', 'Samsung', 'storage', 'NVMe SSD', '{\"capacity\": \"2TB\", \"interface\": \"PCIe 4.0 x4\", \"read_speed\": \"7000MB/s\", \"write_speed\": \"5100MB/s\"}', 1299.00, '/img/hardware/ssd-980pro.jpg', 1, 1, '2025-06-17 00:46:24', '2025-06-17 00:46:24');
INSERT INTO `hardware_components` VALUES (24, 'WD Black SN850X 2TB', 'Western Digital', 'storage', 'NVMe SSD', '{\"capacity\": \"2TB\", \"interface\": \"PCIe 4.0 x4\", \"read_speed\": \"7300MB/s\", \"write_speed\": \"6600MB/s\"}', 1399.00, '/img/hardware/ssd-sn850x.jpg', 1, 2, '2025-06-17 00:46:24', '2025-06-17 00:46:24');
INSERT INTO `hardware_components` VALUES (25, 'Corsair RM1000x', 'Corsair', 'psu', '1000W电源', '{\"wattage\": \"1000W\", \"efficiency\": \"80+ Gold\", \"modular\": \"全模组\", \"warranty\": \"10年\"}', 1299.00, '/img/hardware/psu-rm1000x.jpg', 1, 1, '2025-06-17 00:46:24', '2025-06-17 00:46:24');
INSERT INTO `hardware_components` VALUES (26, 'Seasonic Focus GX-850', 'Seasonic', 'psu', '850W电源', '{\"wattage\": \"850W\", \"efficiency\": \"80+ Gold\", \"modular\": \"全模组\", \"warranty\": \"10年\"}', 999.00, '/img/hardware/psu-focus-gx850.jpg', 1, 2, '2025-06-17 00:46:24', '2025-06-17 00:46:24');
INSERT INTO `hardware_components` VALUES (27, 'Fractal Design Define 7', 'Fractal Design', 'case', '中塔机箱', '{\"form_factor\": \"ATX\", \"max_gpu_length\": \"440mm\", \"max_cpu_cooler\": \"185mm\", \"drive_bays\": \"6x 3.5\", 4x 2.5\"\"}', 899.00, '/img/hardware/case-define7.jpg', 1, 1, '2025-06-17 00:46:24', '2025-06-17 00:46:24');
INSERT INTO `hardware_components` VALUES (28, 'Lian Li PC-O11 Dynamic', 'Lian Li', 'case', '中塔机箱', '{\"form_factor\": \"ATX\", \"max_gpu_length\": \"420mm\", \"max_cpu_cooler\": \"155mm\", \"drive_bays\": \"2x 3.5\", 3x 2.5\"\"}', 1099.00, '/img/hardware/case-o11-dynamic.jpg', 1, 2, '2025-06-17 00:46:24', '2025-06-17 00:46:24');
INSERT INTO `hardware_components` VALUES (29, 'Intel Core i9-14900K', 'Intel', 'cpu', '第14代酷睿', '{\"cores\": 24, \"threads\": 32, \"base_clock\": \"3.2GHz\", \"boost_clock\": \"6.0GHz\", \"socket\": \"LGA1700\", \"tdp\": \"125W\"}', 4599.00, '/img/hardware/cpu-i9-14900k.jpg', 1, 1, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (30, 'Intel Core i7-14700K', 'Intel', 'cpu', '第14代酷睿', '{\"cores\": 20, \"threads\": 28, \"base_clock\": \"3.4GHz\", \"boost_clock\": \"5.6GHz\", \"socket\": \"LGA1700\", \"tdp\": \"125W\"}', 3299.00, '/img/hardware/cpu-i7-14700k.jpg', 1, 2, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (31, 'Intel Core i5-14600K', 'Intel', 'cpu', '第14代酷睿', '{\"cores\": 14, \"threads\": 20, \"base_clock\": \"3.5GHz\", \"boost_clock\": \"5.3GHz\", \"socket\": \"LGA1700\", \"tdp\": \"125W\"}', 2299.00, '/img/hardware/cpu-i5-14600k.jpg', 1, 3, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (32, 'Intel Core i9-13900K', 'Intel', 'cpu', '第13代酷睿', '{\"cores\": 24, \"threads\": 32, \"base_clock\": \"3.0GHz\", \"boost_clock\": \"5.8GHz\", \"socket\": \"LGA1700\", \"tdp\": \"125W\"}', 4299.00, '/img/hardware/cpu-i9-13900k.jpg', 0, 4, '2025-06-17 13:25:40', '2025-06-18 11:16:55');
INSERT INTO `hardware_components` VALUES (33, 'Intel Core i7-13700K', 'Intel', 'cpu', '第13代酷睿', '{\"cores\": 16, \"threads\": 24, \"base_clock\": \"3.4GHz\", \"boost_clock\": \"5.4GHz\", \"socket\": \"LGA1700\", \"tdp\": \"125W\"}', 3099.00, '/img/hardware/cpu-i7-13700k.jpg', 1, 5, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (34, 'Intel Core i5-13600K', 'Intel', 'cpu', '第13代酷睿', '{\"cores\": 14, \"threads\": 20, \"base_clock\": \"3.5GHz\", \"boost_clock\": \"5.1GHz\", \"socket\": \"LGA1700\", \"tdp\": \"125W\"}', 2099.00, '/img/hardware/cpu-i5-13600k.jpg', 1, 6, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (35, 'Intel Core i9-12900K', 'Intel', 'cpu', '第12代酷睿', '{\"cores\": 16, \"threads\": 24, \"base_clock\": \"3.2GHz\", \"boost_clock\": \"5.2GHz\", \"socket\": \"LGA1700\", \"tdp\": \"125W\"}', 3899.00, '/img/hardware/cpu-i9-12900k.jpg', 1, 7, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (36, 'Intel Core i7-12700K', 'Intel', 'cpu', '第12代酷睿', '{\"cores\": 12, \"threads\": 20, \"base_clock\": \"3.6GHz\", \"boost_clock\": \"5.0GHz\", \"socket\": \"LGA1700\", \"tdp\": \"125W\"}', 2799.00, '/img/hardware/cpu-i7-12700k.jpg', 1, 8, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (37, 'Intel Core i5-12600K', 'Intel', 'cpu', '第12代酷睿', '{\"cores\": 10, \"threads\": 16, \"base_clock\": \"3.7GHz\", \"boost_clock\": \"4.9GHz\", \"socket\": \"LGA1700\", \"tdp\": \"125W\"}', 1899.00, '/img/hardware/cpu-i5-12600k.jpg', 1, 9, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (38, 'AMD Ryzen 9 7950X3D', 'AMD', 'cpu', 'Ryzen 7000系列', '{\"cores\": 16, \"threads\": 32, \"base_clock\": \"4.2GHz\", \"boost_clock\": \"5.7GHz\", \"socket\": \"AM5\", \"tdp\": \"120W\"}', 5299.00, '/img/hardware/cpu-ryzen-7950x3d.jpg', 1, 10, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (39, 'AMD Ryzen 9 7950X', 'AMD', 'cpu', 'Ryzen 7000系列', '{\"cores\": 16, \"threads\": 32, \"base_clock\": \"4.5GHz\", \"boost_clock\": \"5.7GHz\", \"socket\": \"AM5\", \"tdp\": \"170W\"}', 4599.00, '/img/hardware/cpu-ryzen-7950x.jpg', 1, 11, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (40, 'AMD Ryzen 7 7800X3D', 'AMD', 'cpu', 'Ryzen 7000系列', '{\"cores\": 8, \"threads\": 16, \"base_clock\": \"4.2GHz\", \"boost_clock\": \"5.0GHz\", \"socket\": \"AM5\", \"tdp\": \"120W\"}', 3299.00, '/img/hardware/cpu-ryzen-7800x3d.jpg', 1, 12, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (41, 'AMD Ryzen 7 7700X', 'AMD', 'cpu', 'Ryzen 7000系列', '{\"cores\": 8, \"threads\": 16, \"base_clock\": \"4.5GHz\", \"boost_clock\": \"5.4GHz\", \"socket\": \"AM5\", \"tdp\": \"105W\"}', 2799.00, '/img/hardware/cpu-ryzen-7700x.jpg', 1, 13, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (42, 'AMD Ryzen 5 7600X', 'AMD', 'cpu', 'Ryzen 7000系列', '{\"cores\": 6, \"threads\": 12, \"base_clock\": \"4.7GHz\", \"boost_clock\": \"5.3GHz\", \"socket\": \"AM5\", \"tdp\": \"105W\"}', 1999.00, '/img/hardware/cpu-ryzen-7600x.jpg', 1, 14, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (43, 'AMD Ryzen 9 5950X', 'AMD', 'cpu', 'Ryzen 5000系列', '{\"cores\": 16, \"threads\": 32, \"base_clock\": \"3.4GHz\", \"boost_clock\": \"4.9GHz\", \"socket\": \"AM4\", \"tdp\": \"105W\"}', 4199.00, '/img/hardware/cpu-ryzen-5950x.jpg', 1, 15, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (44, 'AMD Ryzen 9 5900X', 'AMD', 'cpu', 'Ryzen 5000系列', '{\"cores\": 12, \"threads\": 24, \"base_clock\": \"3.7GHz\", \"boost_clock\": \"4.8GHz\", \"socket\": \"AM4\", \"tdp\": \"105W\"}', 3299.00, '/img/hardware/cpu-ryzen-5900x.jpg', 1, 16, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (45, 'AMD Ryzen 7 5800X3D', 'AMD', 'cpu', 'Ryzen 5000系列', '{\"cores\": 8, \"threads\": 16, \"base_clock\": \"3.4GHz\", \"boost_clock\": \"4.5GHz\", \"socket\": \"AM4\", \"tdp\": \"105W\"}', 2899.00, '/img/hardware/cpu-ryzen-5800x3d.jpg', 1, 17, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (46, 'AMD Ryzen 7 5800X', 'AMD', 'cpu', 'Ryzen 5000系列', '{\"cores\": 8, \"threads\": 16, \"base_clock\": \"3.8GHz\", \"boost_clock\": \"4.7GHz\", \"socket\": \"AM4\", \"tdp\": \"105W\"}', 2499.00, '/img/hardware/cpu-ryzen-5800x.jpg', 1, 18, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (47, 'AMD Ryzen 5 5600X', 'AMD', 'cpu', 'Ryzen 5000系列', '{\"cores\": 6, \"threads\": 12, \"base_clock\": \"3.7GHz\", \"boost_clock\": \"4.6GHz\", \"socket\": \"AM4\", \"tdp\": \"65W\"}', 1699.00, '/img/hardware/cpu-ryzen-5600x.jpg', 1, 19, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (48, 'NVIDIA GeForce RTX 4090', 'NVIDIA', 'gpu', 'RTX 40系列', '{\"memory\": \"24GB GDDR6X\", \"cuda_cores\": 16384, \"base_clock\": \"2230MHz\", \"boost_clock\": \"2520MHz\", \"memory_bandwidth\": \"1008GB/s\", \"tdp\": \"450W\"}', 12999.00, '/img/hardware/gpu-rtx-4090.jpg', 1, 1, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (49, 'NVIDIA GeForce RTX 4080', 'NVIDIA', 'gpu', 'RTX 40系列', '{\"memory\": \"16GB GDDR6X\", \"cuda_cores\": 9728, \"base_clock\": \"2205MHz\", \"boost_clock\": \"2505MHz\", \"memory_bandwidth\": \"716.8GB/s\", \"tdp\": \"320W\"}', 8999.00, '/img/hardware/gpu-rtx-4080.jpg', 1, 2, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (50, 'NVIDIA GeForce RTX 4070 Ti', 'NVIDIA', 'gpu', 'RTX 40系列', '{\"memory\": \"12GB GDDR6X\", \"cuda_cores\": 7680, \"base_clock\": \"2310MHz\", \"boost_clock\": \"2610MHz\", \"memory_bandwidth\": \"504.2GB/s\", \"tdp\": \"285W\"}', 6299.00, '/img/hardware/gpu-rtx-4070ti.jpg', 1, 3, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (51, 'NVIDIA GeForce RTX 4070', 'NVIDIA', 'gpu', 'RTX 40系列', '{\"memory\": \"12GB GDDR6X\", \"cuda_cores\": 5888, \"base_clock\": \"1920MHz\", \"boost_clock\": \"2475MHz\", \"memory_bandwidth\": \"504.2GB/s\", \"tdp\": \"200W\"}', 4799.00, '/img/hardware/gpu-rtx-4070.jpg', 1, 4, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (52, 'NVIDIA GeForce RTX 4060 Ti', 'NVIDIA', 'gpu', 'RTX 40系列', '{\"memory\": \"16GB GDDR6\", \"cuda_cores\": 4352, \"base_clock\": \"2310MHz\", \"boost_clock\": \"2535MHz\", \"memory_bandwidth\": \"288GB/s\", \"tdp\": \"165W\"}', 3599.00, '/img/hardware/gpu-rtx-4060ti.jpg', 1, 5, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (53, 'NVIDIA GeForce RTX 4060', 'NVIDIA', 'gpu', 'RTX 40系列', '{\"memory\": \"8GB GDDR6\", \"cuda_cores\": 3072, \"base_clock\": \"1830MHz\", \"boost_clock\": \"2460MHz\", \"memory_bandwidth\": \"272GB/s\", \"tdp\": \"115W\"}', 2399.00, '/img/hardware/gpu-rtx-4060.jpg', 1, 6, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (54, 'NVIDIA GeForce RTX 3090 Ti', 'NVIDIA', 'gpu', 'RTX 30系列', '{\"memory\": \"24GB GDDR6X\", \"cuda_cores\": 10752, \"base_clock\": \"1560MHz\", \"boost_clock\": \"1860MHz\", \"memory_bandwidth\": \"1008GB/s\", \"tdp\": \"450W\"}', 10999.00, '/img/hardware/gpu-rtx-3090ti.jpg', 1, 7, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (55, 'NVIDIA GeForce RTX 3090', 'NVIDIA', 'gpu', 'RTX 30系列', '{\"memory\": \"24GB GDDR6X\", \"cuda_cores\": 10496, \"base_clock\": \"1395MHz\", \"boost_clock\": \"1695MHz\", \"memory_bandwidth\": \"936.2GB/s\", \"tdp\": \"350W\"}', 9999.00, '/img/hardware/gpu-rtx-3090.jpg', 1, 8, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (56, 'NVIDIA GeForce RTX 3080 Ti', 'NVIDIA', 'gpu', 'RTX 30系列', '{\"memory\": \"12GB GDDR6X\", \"cuda_cores\": 10240, \"base_clock\": \"1365MHz\", \"boost_clock\": \"1665MHz\", \"memory_bandwidth\": \"912.4GB/s\", \"tdp\": \"350W\"}', 7999.00, '/img/hardware/gpu-rtx-3080ti.jpg', 1, 9, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (57, 'NVIDIA GeForce RTX 3080', 'NVIDIA', 'gpu', 'RTX 30系列', '{\"memory\": \"10GB GDDR6X\", \"cuda_cores\": 8704, \"base_clock\": \"1440MHz\", \"boost_clock\": \"1710MHz\", \"memory_bandwidth\": \"760.3GB/s\", \"tdp\": \"320W\"}', 5999.00, '/img/hardware/gpu-rtx-3080.jpg', 1, 10, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (58, 'NVIDIA GeForce RTX 3070 Ti', 'NVIDIA', 'gpu', 'RTX 30系列', '{\"memory\": \"8GB GDDR6X\", \"cuda_cores\": 6144, \"base_clock\": \"1580MHz\", \"boost_clock\": \"1770MHz\", \"memory_bandwidth\": \"608.3GB/s\", \"tdp\": \"290W\"}', 4599.00, '/img/hardware/gpu-rtx-3070ti.jpg', 1, 11, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (59, 'NVIDIA GeForce RTX 3070', 'NVIDIA', 'gpu', 'RTX 30系列', '{\"memory\": \"8GB GDDR6\", \"cuda_cores\": 5888, \"base_clock\": \"1500MHz\", \"boost_clock\": \"1725MHz\", \"memory_bandwidth\": \"448GB/s\", \"tdp\": \"220W\"}', 3999.00, '/img/hardware/gpu-rtx-3070.jpg', 1, 12, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (60, 'NVIDIA GeForce RTX 3060 Ti', 'NVIDIA', 'gpu', 'RTX 30系列', '{\"memory\": \"8GB GDDR6\", \"cuda_cores\": 4864, \"base_clock\": \"1410MHz\", \"boost_clock\": \"1665MHz\", \"memory_bandwidth\": \"448GB/s\", \"tdp\": \"200W\"}', 2999.00, '/img/hardware/gpu-rtx-3060ti.jpg', 1, 13, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (61, 'NVIDIA GeForce RTX 3060', 'NVIDIA', 'gpu', 'RTX 30系列', '{\"memory\": \"12GB GDDR6\", \"cuda_cores\": 3584, \"base_clock\": \"1320MHz\", \"boost_clock\": \"1777MHz\", \"memory_bandwidth\": \"360GB/s\", \"tdp\": \"170W\"}', 2299.00, '/img/hardware/gpu-rtx-3060.jpg', 1, 14, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (62, 'AMD Radeon RX 7900 XTX', 'AMD', 'gpu', 'RX 7000系列', '{\"memory\": \"24GB GDDR6\", \"stream_processors\": 6144, \"game_clock\": \"2300MHz\", \"boost_clock\": \"2500MHz\", \"memory_bandwidth\": \"960GB/s\", \"tdp\": \"355W\"}', 7999.00, '/img/hardware/gpu-rx-7900xtx.jpg', 1, 15, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (63, 'AMD Radeon RX 7900 XT', 'AMD', 'gpu', 'RX 7000系列', '{\"memory\": \"20GB GDDR6\", \"stream_processors\": 5376, \"game_clock\": \"2000MHz\", \"boost_clock\": \"2400MHz\", \"memory_bandwidth\": \"800GB/s\", \"tdp\": \"300W\"}', 6999.00, '/img/hardware/gpu-rx-7900xt.jpg', 1, 16, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (64, 'AMD Radeon RX 7800 XT', 'AMD', 'gpu', 'RX 7000系列', '{\"memory\": \"16GB GDDR6\", \"stream_processors\": 3840, \"game_clock\": \"2124MHz\", \"boost_clock\": \"2430MHz\", \"memory_bandwidth\": \"624GB/s\", \"tdp\": \"263W\"}', 4999.00, '/img/hardware/gpu-rx-7800xt.jpg', 1, 17, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (65, 'AMD Radeon RX 7700 XT', 'AMD', 'gpu', 'RX 7000系列', '{\"memory\": \"12GB GDDR6\", \"stream_processors\": 3456, \"game_clock\": \"2171MHz\", \"boost_clock\": \"2544MHz\", \"memory_bandwidth\": \"432GB/s\", \"tdp\": \"245W\"}', 3999.00, '/img/hardware/gpu-rx-7700xt.jpg', 1, 18, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (66, 'AMD Radeon RX 7600', 'AMD', 'gpu', 'RX 7000系列', '{\"memory\": \"8GB GDDR6\", \"stream_processors\": 2048, \"game_clock\": \"2250MHz\", \"boost_clock\": \"2655MHz\", \"memory_bandwidth\": \"288GB/s\", \"tdp\": \"165W\"}', 2199.00, '/img/hardware/gpu-rx-7600.jpg', 1, 19, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (67, 'AMD Radeon RX 6950 XT', 'AMD', 'gpu', 'RX 6000系列', '{\"memory\": \"16GB GDDR6\", \"stream_processors\": 5120, \"game_clock\": \"2100MHz\", \"boost_clock\": \"2310MHz\", \"memory_bandwidth\": \"576GB/s\", \"tdp\": \"335W\"}', 5999.00, '/img/hardware/gpu-rx-6950xt.jpg', 1, 20, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (68, 'AMD Radeon RX 6900 XT', 'AMD', 'gpu', 'RX 6000系列', '{\"memory\": \"16GB GDDR6\", \"stream_processors\": 5120, \"game_clock\": \"2015MHz\", \"boost_clock\": \"2250MHz\", \"memory_bandwidth\": \"512GB/s\", \"tdp\": \"300W\"}', 5499.00, '/img/hardware/gpu-rx-6900xt.jpg', 1, 21, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (69, 'AMD Radeon RX 6800 XT', 'AMD', 'gpu', 'RX 6000系列', '{\"memory\": \"16GB GDDR6\", \"stream_processors\": 4608, \"game_clock\": \"2015MHz\", \"boost_clock\": \"2250MHz\", \"memory_bandwidth\": \"512GB/s\", \"tdp\": \"300W\"}', 4799.00, '/img/hardware/gpu-rx-6800xt.jpg', 1, 22, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (70, 'AMD Radeon RX 6800', 'AMD', 'gpu', 'RX 6000系列', '{\"memory\": \"16GB GDDR6\", \"stream_processors\": 3840, \"game_clock\": \"1815MHz\", \"boost_clock\": \"2105MHz\", \"memory_bandwidth\": \"512GB/s\", \"tdp\": \"250W\"}', 4199.00, '/img/hardware/gpu-rx-6800.jpg', 1, 23, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (71, 'AMD Radeon RX 6700 XT', 'AMD', 'gpu', 'RX 6000系列', '{\"memory\": \"12GB GDDR6\", \"stream_processors\": 2560, \"game_clock\": \"2424MHz\", \"boost_clock\": \"2581MHz\", \"memory_bandwidth\": \"384GB/s\", \"tdp\": \"230W\"}', 3299.00, '/img/hardware/gpu-rx-6700xt.jpg', 1, 24, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (72, 'AMD Radeon RX 6600 XT', 'AMD', 'gpu', 'RX 6000系列', '{\"memory\": \"8GB GDDR6\", \"stream_processors\": 2048, \"game_clock\": \"2359MHz\", \"boost_clock\": \"2589MHz\", \"memory_bandwidth\": \"256GB/s\", \"tdp\": \"160W\"}', 2599.00, '/img/hardware/gpu-rx-6600xt.jpg', 1, 25, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (73, 'ASUS ROG MAXIMUS Z790 HERO', 'ASUS', 'motherboard', 'Z790芯片组', '{\"socket\": \"LGA1700\", \"memory_slots\": 4, \"max_memory\": \"128GB\", \"memory_type\": \"DDR5\", \"pcie_slots\": \"4x PCIe 5.0\", \"wifi\": \"WiFi 6E\", \"ethernet\": \"2.5Gb\"}', 4299.00, '/img/hardware/mb-z790-hero.jpg', 1, 1, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (74, 'ASUS ROG STRIX Z790-E GAMING', 'ASUS', 'motherboard', 'Z790芯片组', '{\"socket\": \"LGA1700\", \"memory_slots\": 4, \"max_memory\": \"128GB\", \"memory_type\": \"DDR5\", \"pcie_slots\": \"3x PCIe 5.0\", \"wifi\": \"WiFi 6E\", \"ethernet\": \"2.5Gb\"}', 2899.00, '/img/hardware/mb-z790-e.jpg', 1, 2, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (75, 'MSI MEG Z790 ACE', 'MSI', 'motherboard', 'Z790芯片组', '{\"socket\": \"LGA1700\", \"memory_slots\": 4, \"max_memory\": \"128GB\", \"memory_type\": \"DDR5\", \"pcie_slots\": \"4x PCIe 5.0\", \"wifi\": \"WiFi 6E\", \"ethernet\": \"10Gb\"}', 5299.00, '/img/hardware/mb-z790-ace.jpg', 1, 3, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (76, 'MSI MAG Z790 TOMAHAWK', 'MSI', 'motherboard', 'Z790芯片组', '{\"socket\": \"LGA1700\", \"memory_slots\": 4, \"max_memory\": \"128GB\", \"memory_type\": \"DDR5\", \"pcie_slots\": \"3x PCIe 5.0\", \"wifi\": \"WiFi 6\", \"ethernet\": \"2.5Gb\"}', 1999.00, '/img/hardware/mb-z790-tomahawk.jpg', 1, 4, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (77, 'Gigabyte Z790 AORUS MASTER', 'Gigabyte', 'motherboard', 'Z790芯片组', '{\"socket\": \"LGA1700\", \"memory_slots\": 4, \"max_memory\": \"128GB\", \"memory_type\": \"DDR5\", \"pcie_slots\": \"4x PCIe 5.0\", \"wifi\": \"WiFi 6E\", \"ethernet\": \"10Gb\"}', 4799.00, '/img/hardware/mb-z790-master.jpg', 1, 5, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (78, 'ASRock Z790 Taichi', 'ASRock', 'motherboard', 'Z790芯片组', '{\"socket\": \"LGA1700\", \"memory_slots\": 4, \"max_memory\": \"128GB\", \"memory_type\": \"DDR5\", \"pcie_slots\": \"3x PCIe 5.0\", \"wifi\": \"WiFi 6E\", \"ethernet\": \"2.5Gb\"}', 3299.00, '/img/hardware/mb-z790-taichi.jpg', 1, 6, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (79, 'ASUS ROG CROSSHAIR X670E HERO', 'ASUS', 'motherboard', 'X670E芯片组', '{\"socket\": \"AM5\", \"memory_slots\": 4, \"max_memory\": \"128GB\", \"memory_type\": \"DDR5\", \"pcie_slots\": \"4x PCIe 5.0\", \"wifi\": \"WiFi 6E\", \"ethernet\": \"2.5Gb\"}', 4599.00, '/img/hardware/mb-x670e-hero.jpg', 1, 7, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (80, 'MSI MEG X670E ACE', 'MSI', 'motherboard', 'X670E芯片组', '{\"socket\": \"AM5\", \"memory_slots\": 4, \"max_memory\": \"128GB\", \"memory_type\": \"DDR5\", \"pcie_slots\": \"4x PCIe 5.0\", \"wifi\": \"WiFi 6E\", \"ethernet\": \"10Gb\"}', 5599.00, '/img/hardware/mb-x670e-ace.jpg', 1, 8, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (81, 'Gigabyte X670E AORUS MASTER', 'Gigabyte', 'motherboard', 'X670E芯片组', '{\"socket\": \"AM5\", \"memory_slots\": 4, \"max_memory\": \"128GB\", \"memory_type\": \"DDR5\", \"pcie_slots\": \"4x PCIe 5.0\", \"wifi\": \"WiFi 6E\", \"ethernet\": \"10Gb\"}', 4999.00, '/img/hardware/mb-x670e-master.jpg', 1, 9, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (82, 'ASUS TUF GAMING B650-PLUS', 'ASUS', 'motherboard', 'B650芯片组', '{\"socket\": \"AM5\", \"memory_slots\": 4, \"max_memory\": \"128GB\", \"memory_type\": \"DDR5\", \"pcie_slots\": \"2x PCIe 5.0\", \"wifi\": \"无\", \"ethernet\": \"2.5Gb\"}', 1199.00, '/img/hardware/mb-b650-plus.jpg', 1, 10, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (83, 'MSI MAG B650 TOMAHAWK WIFI', 'MSI', 'motherboard', 'B650芯片组', '{\"socket\": \"AM5\", \"memory_slots\": 4, \"max_memory\": \"128GB\", \"memory_type\": \"DDR5\", \"pcie_slots\": \"2x PCIe 5.0\", \"wifi\": \"WiFi 6\", \"ethernet\": \"2.5Gb\"}', 1299.00, '/img/hardware/mb-b650-tomahawk.jpg', 1, 11, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (84, 'ASRock B650 Pro RS', 'ASRock', 'motherboard', 'B650芯片组', '{\"socket\": \"AM5\", \"memory_slots\": 4, \"max_memory\": \"128GB\", \"memory_type\": \"DDR5\", \"pcie_slots\": \"2x PCIe 4.0\", \"wifi\": \"无\", \"ethernet\": \"1Gb\"}', 899.00, '/img/hardware/mb-b650-pro.jpg', 1, 12, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (85, 'Corsair Dominator Platinum RGB DDR5-6000 32GB', 'Corsair', 'ram', 'DDR5内存', '{\"capacity\": \"32GB\", \"speed\": \"DDR5-6000\", \"timings\": \"CL30\", \"voltage\": \"1.35V\", \"kit\": \"2x16GB\", \"rgb\": \"是\"}', 2299.00, '/img/hardware/ram-corsair-dom-ddr5.jpg', 1, 1, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (86, 'G.SKILL Trident Z5 RGB DDR5-6400 32GB', 'G.SKILL', 'ram', 'DDR5内存', '{\"capacity\": \"32GB\", \"speed\": \"DDR5-6400\", \"timings\": \"CL32\", \"voltage\": \"1.4V\", \"kit\": \"2x16GB\", \"rgb\": \"是\"}', 2599.00, '/img/hardware/ram-gskill-z5-ddr5.jpg', 1, 2, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (87, 'Kingston Fury Beast DDR5-5600 32GB', 'Kingston', 'ram', 'DDR5内存', '{\"capacity\": \"32GB\", \"speed\": \"DDR5-5600\", \"timings\": \"CL36\", \"voltage\": \"1.25V\", \"kit\": \"2x16GB\", \"rgb\": \"否\"}', 1399.00, '/img/hardware/ram-kingston-beast-ddr5.jpg', 1, 3, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (88, 'Corsair Vengeance DDR5-5600 32GB', 'Corsair', 'ram', 'DDR5内存', '{\"capacity\": \"32GB\", \"speed\": \"DDR5-5600\", \"timings\": \"CL36\", \"voltage\": \"1.25V\", \"kit\": \"2x16GB\", \"rgb\": \"否\"}', 1299.00, '/img/hardware/ram-corsair-vengeance-ddr5.jpg', 1, 4, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (89, 'G.SKILL Ripjaws S5 DDR5-5200 32GB', 'G.SKILL', 'ram', 'DDR5内存', '{\"capacity\": \"32GB\", \"speed\": \"DDR5-5200\", \"timings\": \"CL40\", \"voltage\": \"1.1V\", \"kit\": \"2x16GB\", \"rgb\": \"否\"}', 1099.00, '/img/hardware/ram-gskill-s5-ddr5.jpg', 1, 5, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (90, 'Crucial DDR5-4800 32GB', 'Crucial', 'ram', 'DDR5内存', '{\"capacity\": \"32GB\", \"speed\": \"DDR5-4800\", \"timings\": \"CL40\", \"voltage\": \"1.1V\", \"kit\": \"2x16GB\", \"rgb\": \"否\"}', 999.00, '/img/hardware/ram-crucial-ddr5.jpg', 1, 6, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (91, 'Corsair Dominator Platinum RGB DDR4-3600 32GB', 'Corsair', 'ram', 'DDR4内存', '{\"capacity\": \"32GB\", \"speed\": \"DDR4-3600\", \"timings\": \"CL18\", \"voltage\": \"1.35V\", \"kit\": \"2x16GB\", \"rgb\": \"是\"}', 1599.00, '/img/hardware/ram-corsair-dom-ddr4.jpg', 1, 7, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (92, 'G.SKILL Trident Z Neo DDR4-3600 32GB', 'G.SKILL', 'ram', 'DDR4内存', '{\"capacity\": \"32GB\", \"speed\": \"DDR4-3600\", \"timings\": \"CL16\", \"voltage\": \"1.35V\", \"kit\": \"2x16GB\", \"rgb\": \"是\"}', 1299.00, '/img/hardware/ram-gskill-neo-ddr4.jpg', 1, 8, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (93, 'Kingston Fury Beast DDR4-3200 32GB', 'Kingston', 'ram', 'DDR4内存', '{\"capacity\": \"32GB\", \"speed\": \"DDR4-3200\", \"timings\": \"CL16\", \"voltage\": \"1.35V\", \"kit\": \"2x16GB\", \"rgb\": \"否\"}', 899.00, '/img/hardware/ram-kingston-beast-ddr4.jpg', 1, 9, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (94, 'Corsair Vengeance LPX DDR4-3200 32GB', 'Corsair', 'ram', 'DDR4内存', '{\"capacity\": \"32GB\", \"speed\": \"DDR4-3200\", \"timings\": \"CL16\", \"voltage\": \"1.35V\", \"kit\": \"2x16GB\", \"rgb\": \"否\"}', 799.00, '/img/hardware/ram-corsair-lpx-ddr4.jpg', 1, 10, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (95, 'Samsung 990 PRO 4TB', 'Samsung', 'storage', 'NVMe SSD', '{\"capacity\": \"4TB\", \"interface\": \"PCIe 4.0 x4\", \"read_speed\": \"7450MB/s\", \"write_speed\": \"6900MB/s\", \"form_factor\": \"M.2 2280\", \"warranty\": \"5年\"}', 3299.00, '/img/hardware/ssd-990pro-4tb.jpg', 1, 1, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (96, 'Samsung 990 PRO 2TB', 'Samsung', 'storage', 'NVMe SSD', '{\"capacity\": \"2TB\", \"interface\": \"PCIe 4.0 x4\", \"read_speed\": \"7450MB/s\", \"write_speed\": \"6900MB/s\", \"form_factor\": \"M.2 2280\", \"warranty\": \"5年\"}', 1699.00, '/img/hardware/ssd-990pro-2tb.jpg', 1, 2, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (97, 'Samsung 990 PRO 1TB', 'Samsung', 'storage', 'NVMe SSD', '{\"capacity\": \"1TB\", \"interface\": \"PCIe 4.0 x4\", \"read_speed\": \"7450MB/s\", \"write_speed\": \"6900MB/s\", \"form_factor\": \"M.2 2280\", \"warranty\": \"5年\"}', 899.00, '/img/hardware/ssd-990pro-1tb.jpg', 1, 3, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (98, 'WD Black SN850X 4TB', 'Western Digital', 'storage', 'NVMe SSD', '{\"capacity\": \"4TB\", \"interface\": \"PCIe 4.0 x4\", \"read_speed\": \"7300MB/s\", \"write_speed\": \"6600MB/s\", \"form_factor\": \"M.2 2280\", \"warranty\": \"5年\"}', 3199.00, '/img/hardware/ssd-sn850x-4tb.jpg', 1, 4, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (99, 'WD Black SN850X 2TB', 'Western Digital', 'storage', 'NVMe SSD', '{\"capacity\": \"2TB\", \"interface\": \"PCIe 4.0 x4\", \"read_speed\": \"7300MB/s\", \"write_speed\": \"6600MB/s\", \"form_factor\": \"M.2 2280\", \"warranty\": \"5年\"}', 1599.00, '/img/hardware/ssd-sn850x-2tb.jpg', 1, 5, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (100, 'WD Black SN850X 1TB', 'Western Digital', 'storage', 'NVMe SSD', '{\"capacity\": \"1TB\", \"interface\": \"PCIe 4.0 x4\", \"read_speed\": \"7300MB/s\", \"write_speed\": \"6300MB/s\", \"form_factor\": \"M.2 2280\", \"warranty\": \"5年\"}', 799.00, '/img/hardware/ssd-sn850x-1tb.jpg', 1, 6, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (101, 'Crucial T700 2TB', 'Crucial', 'storage', 'NVMe SSD', '{\"capacity\": \"2TB\", \"interface\": \"PCIe 5.0 x4\", \"read_speed\": \"12400MB/s\", \"write_speed\": \"11800MB/s\", \"form_factor\": \"M.2 2280\", \"warranty\": \"5年\"}', 2299.00, '/img/hardware/ssd-t700-2tb.jpg', 1, 7, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (102, 'Crucial T700 1TB', 'Crucial', 'storage', 'NVMe SSD', '{\"capacity\": \"1TB\", \"interface\": \"PCIe 5.0 x4\", \"read_speed\": \"12400MB/s\", \"write_speed\": \"11800MB/s\", \"form_factor\": \"M.2 2280\", \"warranty\": \"5年\"}', 1299.00, '/img/hardware/ssd-t700-1tb.jpg', 1, 8, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (103, 'Kingston KC3000 2TB', 'Kingston', 'storage', 'NVMe SSD', '{\"capacity\": \"2TB\", \"interface\": \"PCIe 4.0 x4\", \"read_speed\": \"7000MB/s\", \"write_speed\": \"7000MB/s\", \"form_factor\": \"M.2 2280\", \"warranty\": \"5年\"}', 1399.00, '/img/hardware/ssd-kc3000-2tb.jpg', 1, 9, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (104, 'Kingston KC3000 1TB', 'Kingston', 'storage', 'NVMe SSD', '{\"capacity\": \"1TB\", \"interface\": \"PCIe 4.0 x4\", \"read_speed\": \"7000MB/s\", \"write_speed\": \"6000MB/s\", \"form_factor\": \"M.2 2280\", \"warranty\": \"5年\"}', 699.00, '/img/hardware/ssd-kc3000-1tb.jpg', 1, 10, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (105, 'Samsung 870 EVO 4TB', 'Samsung', 'storage', 'SATA SSD', '{\"capacity\": \"4TB\", \"interface\": \"SATA III\", \"read_speed\": \"560MB/s\", \"write_speed\": \"530MB/s\", \"form_factor\": \"2.5英寸\", \"warranty\": \"5年\"}', 2199.00, '/img/hardware/ssd-870evo-4tb.jpg', 1, 11, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (106, 'Samsung 870 EVO 2TB', 'Samsung', 'storage', 'SATA SSD', '{\"capacity\": \"2TB\", \"interface\": \"SATA III\", \"read_speed\": \"560MB/s\", \"write_speed\": \"530MB/s\", \"form_factor\": \"2.5英寸\", \"warranty\": \"5年\"}', 1199.00, '/img/hardware/ssd-870evo-2tb.jpg', 1, 12, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (107, 'Samsung 870 EVO 1TB', 'Samsung', 'storage', 'SATA SSD', '{\"capacity\": \"1TB\", \"interface\": \"SATA III\", \"read_speed\": \"560MB/s\", \"write_speed\": \"530MB/s\", \"form_factor\": \"2.5英寸\", \"warranty\": \"5年\"}', 699.00, '/img/hardware/ssd-870evo-1tb.jpg', 1, 13, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (108, 'Crucial MX4 2TB', 'Crucial', 'storage', 'SATA SSD', '{\"capacity\": \"2TB\", \"interface\": \"SATA III\", \"read_speed\": \"560MB/s\", \"write_speed\": \"510MB/s\", \"form_factor\": \"2.5英寸\", \"warranty\": \"5年\"}', 999.00, '/img/hardware/ssd-mx4-2tb.jpg', 1, 14, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (109, 'Crucial MX4 1TB', 'Crucial', 'storage', 'SATA SSD', '{\"capacity\": \"1TB\", \"interface\": \"SATA III\", \"read_speed\": \"560MB/s\", \"write_speed\": \"510MB/s\", \"form_factor\": \"2.5英寸\", \"warranty\": \"5年\"}', 599.00, '/img/hardware/ssd-mx4-1tb.jpg', 1, 15, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (110, 'Seagate IronWolf Pro 18TB', 'Seagate', 'storage', '机械硬盘', '{\"capacity\": \"18TB\", \"interface\": \"SATA III\", \"rpm\": \"7200RPM\", \"cache\": \"256MB\", \"form_factor\": \"3.5英寸\", \"warranty\": \"5年\"}', 3599.00, '/img/hardware/hdd-ironwolf-18tb.jpg', 1, 16, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (111, 'WD Black 8TB', 'Western Digital', 'storage', '机械硬盘', '{\"capacity\": \"8TB\", \"interface\": \"SATA III\", \"rpm\": \"7200RPM\", \"cache\": \"256MB\", \"form_factor\": \"3.5英寸\", \"warranty\": \"5年\"}', 1599.00, '/img/hardware/hdd-black-8tb.jpg', 1, 17, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (112, 'Seagate Barracuda 4TB', 'Seagate', 'storage', '机械硬盘', '{\"capacity\": \"4TB\", \"interface\": \"SATA III\", \"rpm\": \"5400RPM\", \"cache\": \"256MB\", \"form_factor\": \"3.5英寸\", \"warranty\": \"2年\"}', 699.00, '/img/hardware/hdd-barracuda-4tb.jpg', 1, 18, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (113, 'WD Blue 2TB', 'Western Digital', 'storage', '机械硬盘', '{\"capacity\": \"2TB\", \"interface\": \"SATA III\", \"rpm\": \"5400RPM\", \"cache\": \"256MB\", \"form_factor\": \"3.5英寸\", \"warranty\": \"2年\"}', 399.00, '/img/hardware/hdd-blue-2tb.jpg', 1, 19, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (114, 'Corsair AX1600i', 'Corsair', 'psu', '1600W电源', '{\"wattage\": \"1600W\", \"efficiency\": \"80+ Titanium\", \"modular\": \"全模组\", \"warranty\": \"10年\", \"fan_size\": \"140mm\", \"cables\": \"扁平线\"}', 3299.00, '/img/hardware/psu-ax1600i.jpg', 1, 1, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (115, 'Seasonic Prime TX-1300', 'Seasonic', 'psu', '1300W电源', '{\"wattage\": \"1300W\", \"efficiency\": \"80+ Titanium\", \"modular\": \"全模组\", \"warranty\": \"12年\", \"fan_size\": \"135mm\", \"cables\": \"扁平线\"}', 2899.00, '/img/hardware/psu-prime-tx1300.jpg', 1, 2, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (116, 'EVGA SuperNOVA 1200 P2', 'EVGA', 'psu', '1200W电源', '{\"wattage\": \"1200W\", \"efficiency\": \"80+ Platinum\", \"modular\": \"全模组\", \"warranty\": \"10年\", \"fan_size\": \"135mm\", \"cables\": \"扁平线\"}', 2199.00, '/img/hardware/psu-supernova-1200.jpg', 1, 3, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (117, 'Corsair RM1000x', 'Corsair', 'psu', '1000W电源', '{\"wattage\": \"1000W\", \"efficiency\": \"80+ Gold\", \"modular\": \"全模组\", \"warranty\": \"10年\", \"fan_size\": \"135mm\", \"cables\": \"扁平线\"}', 1299.00, '/img/hardware/psu-rm1000x.jpg', 1, 4, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (118, 'Seasonic Focus GX-850', 'Seasonic', 'psu', '850W电源', '{\"wattage\": \"850W\", \"efficiency\": \"80+ Gold\", \"modular\": \"全模组\", \"warranty\": \"10年\", \"fan_size\": \"120mm\", \"cables\": \"扁平线\"}', 999.00, '/img/hardware/psu-focus-gx850.jpg', 1, 5, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (119, 'Corsair RM750x', 'Corsair', 'psu', '750W电源', '{\"wattage\": \"750W\", \"efficiency\": \"80+ Gold\", \"modular\": \"全模组\", \"warranty\": \"10年\", \"fan_size\": \"120mm\", \"cables\": \"扁平线\"}', 899.00, '/img/hardware/psu-rm750x.jpg', 1, 6, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (120, 'Seasonic Focus GX-650', 'Seasonic', 'psu', '650W电源', '{\"wattage\": \"650W\", \"efficiency\": \"80+ Gold\", \"modular\": \"全模组\", \"warranty\": \"10年\", \"fan_size\": \"120mm\", \"cables\": \"扁平线\"}', 699.00, '/img/hardware/psu-focus-gx650.jpg', 1, 7, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (121, 'Corsair CV650', 'Corsair', 'psu', '650W电源', '{\"wattage\": \"650W\", \"efficiency\": \"80+ Bronze\", \"modular\": \"非模组\", \"warranty\": \"3年\", \"fan_size\": \"120mm\", \"cables\": \"固定线\"}', 399.00, '/img/hardware/psu-cv650.jpg', 1, 8, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (122, 'EVGA BR 600W', 'EVGA', 'psu', '600W电源', '{\"wattage\": \"600W\", \"efficiency\": \"80+ Bronze\", \"modular\": \"非模组\", \"warranty\": \"3年\", \"fan_size\": \"120mm\", \"cables\": \"固定线\"}', 349.00, '/img/hardware/psu-br600.jpg', 1, 9, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (123, 'Cooler Master MWE 550W', 'Cooler Master', 'psu', '550W电源', '{\"wattage\": \"550W\", \"efficiency\": \"80+ Bronze\", \"modular\": \"非模组\", \"warranty\": \"5年\", \"fan_size\": \"120mm\", \"cables\": \"固定线\"}', 299.00, '/img/hardware/psu-mwe550.jpg', 1, 10, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (124, 'Lian Li PC-O11 Dynamic EVO', 'Lian Li', 'case', '中塔机箱', '{\"form_factor\": \"ATX\", \"max_gpu_length\": \"420mm\", \"max_cpu_cooler\": \"155mm\", \"drive_bays\": \"2x 3.5\", 4x 2.5\"\", \"fans_included\": \"无\", \"side_panel\": \"钢化玻璃\"}', 1299.00, '/img/hardware/case-o11-evo.jpg', 1, 1, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (125, 'Fractal Design Define 7', 'Fractal Design', 'case', '中塔机箱', '{\"form_factor\": \"ATX\", \"max_gpu_length\": \"440mm\", \"max_cpu_cooler\": \"185mm\", \"drive_bays\": \"6x 3.5\", 4x 2.5\"\", \"fans_included\": \"2x 140mm\", \"side_panel\": \"钢化玻璃\"}', 899.00, '/img/hardware/case-define7.jpg', 1, 2, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (126, 'NZXT H7 Flow', 'NZXT', 'case', '中塔机箱', '{\"form_factor\": \"ATX\", \"max_gpu_length\": \"400mm\", \"max_cpu_cooler\": \"165mm\", \"drive_bays\": \"2x 3.5\", 4x 2.5\"\", \"fans_included\": \"3x 120mm\", \"side_panel\": \"钢化玻璃\"}', 1099.00, '/img/hardware/case-h7-flow.jpg', 1, 3, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (127, 'Corsair 4000D Airflow', 'Corsair', 'case', '中塔机箱', '{\"form_factor\": \"ATX\", \"max_gpu_length\": \"360mm\", \"max_cpu_cooler\": \"170mm\", \"drive_bays\": \"2x 3.5\", 2x 2.5\"\", \"fans_included\": \"2x 120mm\", \"side_panel\": \"钢化玻璃\"}', 799.00, '/img/hardware/case-4000d.jpg', 1, 4, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (128, 'be quiet! Pure Base 500DX', 'be quiet!', 'case', '中塔机箱', '{\"form_factor\": \"ATX\", \"max_gpu_length\": \"369mm\", \"max_cpu_cooler\": \"190mm\", \"drive_bays\": \"2x 3.5\", 3x 2.5\"\", \"fans_included\": \"3x 140mm\", \"side_panel\": \"钢化玻璃\"}', 999.00, '/img/hardware/case-pure500dx.jpg', 1, 5, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (129, 'Phanteks Eclipse P400A', 'Phanteks', 'case', '中塔机箱', '{\"form_factor\": \"ATX\", \"max_gpu_length\": \"420mm\", \"max_cpu_cooler\": \"160mm\", \"drive_bays\": \"2x 3.5\", 2x 2.5\"\", \"fans_included\": \"2x 120mm\", \"side_panel\": \"钢化玻璃\"}', 699.00, '/img/hardware/case-p400a.jpg', 1, 6, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (130, 'Cooler Master MasterBox TD500 Mesh', 'Cooler Master', 'case', '中塔机箱', '{\"form_factor\": \"ATX\", \"max_gpu_length\": \"410mm\", \"max_cpu_cooler\": \"165mm\", \"drive_bays\": \"2x 3.5\", 2x 2.5\"\", \"fans_included\": \"3x 120mm RGB\", \"side_panel\": \"钢化玻璃\"}', 899.00, '/img/hardware/case-td500.jpg', 1, 7, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (131, 'Thermaltake Core P3', 'Thermaltake', 'case', '开放式机箱', '{\"form_factor\": \"ATX\", \"max_gpu_length\": \"300mm\", \"max_cpu_cooler\": \"无限制\", \"drive_bays\": \"2x 3.5\", 2x 2.5\"\", \"fans_included\": \"无\", \"side_panel\": \"开放式\"}', 1199.00, '/img/hardware/case-core-p3.jpg', 1, 8, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (132, 'Silverstone SG13', 'Silverstone', 'case', 'ITX机箱', '{\"form_factor\": \"Mini-ITX\", \"max_gpu_length\": \"267mm\", \"max_cpu_cooler\": \"61mm\", \"drive_bays\": \"1x 3.5\", 1x 2.5\"\", \"fans_included\": \"无\", \"side_panel\": \"金属\"}', 599.00, '/img/hardware/case-sg13.jpg', 1, 9, '2025-06-17 13:25:40', '2025-06-17 13:25:40');
INSERT INTO `hardware_components` VALUES (133, 'Fractal Design Node 202', 'Fractal Design', 'case', 'ITX机箱', '{\"form_factor\": \"Mini-ITX\", \"max_gpu_length\": \"310mm\", \"max_cpu_cooler\": \"56mm\", \"drive_bays\": \"1x 3.5\", 2x 2.5\"\", \"fans_included\": \"无\", \"side_panel\": \"金属\"}', 799.00, '/img/hardware/case-node202.jpg', 1, 10, '2025-06-17 13:25:40', '2025-06-17 13:25:40');

-- ----------------------------
-- Table structure for hardware_config
-- ----------------------------
DROP TABLE IF EXISTS `hardware_config`;
CREATE TABLE `hardware_config`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `config_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '配置键',
  `config_value` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '配置值(JSON格式)',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '配置描述',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_config_key`(`config_key` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '硬件配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of hardware_config
-- ----------------------------
INSERT INTO `hardware_config` VALUES (1, 'cpu_config', '{\r\n    \"title\": \"处理器 (CPU)\",\r\n    \"fields\": [\r\n        {\"name\": \"name\", \"label\": \"产品名称\", \"type\": \"text\", \"required\": true},\r\n        {\"name\": \"brand\", \"label\": \"品牌\", \"type\": \"select\", \"required\": true, \"options\": [\"Intel\", \"AMD\"]},\r\n        {\"name\": \"generation\", \"label\": \"代数\", \"type\": \"text\", \"required\": true},\r\n        {\"name\": \"socket\", \"label\": \"接口类型\", \"type\": \"select\", \"required\": true, \"options\": [\"LGA1700\", \"LGA1200\", \"AM4\", \"AM5\"]},\r\n        {\"name\": \"cores\", \"label\": \"核心数\", \"type\": \"number\", \"required\": true},\r\n        {\"name\": \"threads\", \"label\": \"线程数\", \"type\": \"number\", \"required\": true},\r\n        {\"name\": \"baseFreq\", \"label\": \"基础频率(GHz)\", \"type\": \"number\", \"step\": \"0.1\"},\r\n        {\"name\": \"boostFreq\", \"label\": \"加速频率(GHz)\", \"type\": \"number\", \"step\": \"0.1\"},\r\n        {\"name\": \"price\", \"label\": \"价格(元)\", \"type\": \"number\", \"required\": true},\r\n        {\"name\": \"imageUrl\", \"label\": \"图片链接\", \"type\": \"url\"}\r\n    ],\r\n    \"columns\": [\"名称\", \"品牌\", \"代数\", \"接口\", \"核心/线程\", \"频率\", \"价格\", \"操作\"]\r\n}', 'CPU硬件配置', '2025-06-16 23:25:53', '2025-06-16 23:25:53');
INSERT INTO `hardware_config` VALUES (2, 'motherboard_config', '{\r\n    \"title\": \"主板\",\r\n    \"fields\": [\r\n        {\"name\": \"name\", \"label\": \"产品名称\", \"type\": \"text\", \"required\": true},\r\n        {\"name\": \"brand\", \"label\": \"品牌\", \"type\": \"select\", \"required\": true, \"options\": [\"华硕\", \"微星\", \"技嘉\", \"华擎\"]},\r\n        {\"name\": \"chipset\", \"label\": \"芯片组\", \"type\": \"select\", \"required\": true, \"options\": [\"Z790\", \"B760\", \"H610\", \"X670E\", \"B650\"]},\r\n        {\"name\": \"socket\", \"label\": \"接口类型\", \"type\": \"select\", \"required\": true, \"options\": [\"LGA1700\", \"LGA1200\", \"AM4\", \"AM5\"]},\r\n        {\"name\": \"formFactor\", \"label\": \"板型\", \"type\": \"select\", \"required\": true, \"options\": [\"ATX\", \"Micro-ATX\", \"Mini-ITX\"]},\r\n        {\"name\": \"memoryType\", \"label\": \"内存类型\", \"type\": \"select\", \"required\": true, \"options\": [\"DDR4\", \"DDR5\"]},\r\n        {\"name\": \"memorySlots\", \"label\": \"内存插槽数\", \"type\": \"number\", \"required\": true},\r\n        {\"name\": \"maxMemory\", \"label\": \"最大内存(GB)\", \"type\": \"number\"},\r\n        {\"name\": \"price\", \"label\": \"价格(元)\", \"type\": \"number\", \"required\": true},\r\n        {\"name\": \"imageUrl\", \"label\": \"图片链接\", \"type\": \"url\"}\r\n    ],\r\n    \"columns\": [\"名称\", \"品牌\", \"芯片组\", \"接口\", \"板型\", \"内存类型\", \"价格\", \"操作\"]\r\n}', '主板硬件配置', '2025-06-16 23:25:53', '2025-06-16 23:25:53');
INSERT INTO `hardware_config` VALUES (3, 'ram_config', '{\r\n    \"title\": \"内存 (RAM)\",\r\n    \"fields\": [\r\n        {\"name\": \"name\", \"label\": \"产品名称\", \"type\": \"text\", \"required\": true},\r\n        {\"name\": \"brand\", \"label\": \"品牌\", \"type\": \"select\", \"required\": true, \"options\": [\"金士顿\", \"海盗船\", \"芝奇\", \"十铨\", \"威刚\"]},\r\n        {\"name\": \"type\", \"label\": \"内存类型\", \"type\": \"select\", \"required\": true, \"options\": [\"DDR4\", \"DDR5\"]},\r\n        {\"name\": \"capacity\", \"label\": \"容量(GB)\", \"type\": \"number\", \"required\": true},\r\n        {\"name\": \"speed\", \"label\": \"频率(MHz)\", \"type\": \"number\", \"required\": true},\r\n        {\"name\": \"timing\", \"label\": \"时序\", \"type\": \"text\"},\r\n        {\"name\": \"voltage\", \"label\": \"电压(V)\", \"type\": \"number\", \"step\": \"0.1\"},\r\n        {\"name\": \"price\", \"label\": \"价格(元)\", \"type\": \"number\", \"required\": true},\r\n        {\"name\": \"imageUrl\", \"label\": \"图片链接\", \"type\": \"url\"}\r\n    ],\r\n    \"columns\": [\"名称\", \"品牌\", \"类型\", \"容量\", \"频率\", \"时序\", \"价格\", \"操作\"]\r\n}', '内存硬件配置', '2025-06-16 23:25:53', '2025-06-16 23:25:53');
INSERT INTO `hardware_config` VALUES (4, 'gpu_config', '{\r\n    \"title\": \"显卡 (GPU)\",\r\n    \"fields\": [\r\n        {\"name\": \"name\", \"label\": \"产品名称\", \"type\": \"text\", \"required\": true},\r\n        {\"name\": \"brand\", \"label\": \"品牌\", \"type\": \"select\", \"required\": true, \"options\": [\"NVIDIA\", \"AMD\"]},\r\n        {\"name\": \"series\", \"label\": \"系列\", \"type\": \"text\", \"required\": true},\r\n        {\"name\": \"memory\", \"label\": \"显存(GB)\", \"type\": \"number\", \"required\": true},\r\n        {\"name\": \"memoryType\", \"label\": \"显存类型\", \"type\": \"select\", \"options\": [\"GDDR6\", \"GDDR6X\", \"GDDR5\"]},\r\n        {\"name\": \"coreClock\", \"label\": \"核心频率(MHz)\", \"type\": \"number\"},\r\n        {\"name\": \"boostClock\", \"label\": \"加速频率(MHz)\", \"type\": \"number\"},\r\n        {\"name\": \"powerConsumption\", \"label\": \"功耗(W)\", \"type\": \"number\"},\r\n        {\"name\": \"price\", \"label\": \"价格(元)\", \"type\": \"number\", \"required\": true},\r\n        {\"name\": \"imageUrl\", \"label\": \"图片链接\", \"type\": \"url\"}\r\n    ],\r\n    \"columns\": [\"名称\", \"品牌\", \"系列\", \"显存\", \"频率\", \"功耗\", \"价格\", \"操作\"]\r\n}', '显卡硬件配置', '2025-06-16 23:25:53', '2025-06-16 23:25:53');
INSERT INTO `hardware_config` VALUES (5, 'storage_config', '{\r\n    \"title\": \"存储设备\",\r\n    \"fields\": [\r\n        {\"name\": \"name\", \"label\": \"产品名称\", \"type\": \"text\", \"required\": true},\r\n        {\"name\": \"brand\", \"label\": \"品牌\", \"type\": \"select\", \"required\": true, \"options\": [\"三星\", \"西数\", \"希捷\", \"金士顿\", \"英特尔\"]},\r\n        {\"name\": \"type\", \"label\": \"存储类型\", \"type\": \"select\", \"required\": true, \"options\": [\"SSD\", \"HDD\", \"M.2 NVMe\"]},\r\n        {\"name\": \"capacity\", \"label\": \"容量(GB)\", \"type\": \"number\", \"required\": true},\r\n        {\"name\": \"interface\", \"label\": \"接口类型\", \"type\": \"select\", \"required\": true, \"options\": [\"SATA III\", \"M.2\", \"PCIe 3.0\", \"PCIe 4.0\"]},\r\n        {\"name\": \"readSpeed\", \"label\": \"读取速度(MB/s)\", \"type\": \"number\"},\r\n        {\"name\": \"writeSpeed\", \"label\": \"写入速度(MB/s)\", \"type\": \"number\"},\r\n        {\"name\": \"price\", \"label\": \"价格(元)\", \"type\": \"number\", \"required\": true},\r\n        {\"name\": \"imageUrl\", \"label\": \"图片链接\", \"type\": \"url\"}\r\n    ],\r\n    \"columns\": [\"名称\", \"品牌\", \"类型\", \"容量\", \"接口\", \"读写速度\", \"价格\", \"操作\"]\r\n}', '存储设备硬件配置', '2025-06-16 23:25:53', '2025-06-16 23:25:53');
INSERT INTO `hardware_config` VALUES (6, 'psu_config', '{\r\n    \"title\": \"电源 (PSU)\",\r\n    \"fields\": [\r\n        {\"name\": \"name\", \"label\": \"产品名称\", \"type\": \"text\", \"required\": true},\r\n        {\"name\": \"brand\", \"label\": \"品牌\", \"type\": \"select\", \"required\": true, \"options\": [\"海盗船\", \"酷冷至尊\", \"安钛克\", \"振华\", \"全汉\"]},\r\n        {\"name\": \"wattage\", \"label\": \"额定功率(W)\", \"type\": \"number\", \"required\": true},\r\n        {\"name\": \"efficiency\", \"label\": \"80Plus认证\", \"type\": \"select\", \"options\": [\"80Plus\", \"80Plus Bronze\", \"80Plus Silver\", \"80Plus Gold\", \"80Plus Platinum\", \"80Plus Titanium\"]},\r\n        {\"name\": \"modular\", \"label\": \"模组化\", \"type\": \"select\", \"required\": true, \"options\": [\"全模组\", \"半模组\", \"非模组\"]},\r\n        {\"name\": \"formFactor\", \"label\": \"规格\", \"type\": \"select\", \"required\": true, \"options\": [\"ATX\", \"SFX\", \"SFX-L\"]},\r\n        {\"name\": \"price\", \"label\": \"价格(元)\", \"type\": \"number\", \"required\": true},\r\n        {\"name\": \"imageUrl\", \"label\": \"图片链接\", \"type\": \"url\"}\r\n    ],\r\n    \"columns\": [\"名称\", \"品牌\", \"功率\", \"认证\", \"模组化\", \"规格\", \"价格\", \"操作\"]\r\n}', '电源硬件配置', '2025-06-16 23:25:53', '2025-06-16 23:25:53');
INSERT INTO `hardware_config` VALUES (7, 'case_config', '{\r\n    \"title\": \"机箱\",\r\n    \"fields\": [\r\n        {\"name\": \"name\", \"label\": \"产品名称\", \"type\": \"text\", \"required\": true},\r\n        {\"name\": \"brand\", \"label\": \"品牌\", \"type\": \"select\", \"required\": true, \"options\": [\"酷冷至尊\", \"追风者\", \"海盗船\", \"联力\", \"银欣\"]},\r\n        {\"name\": \"formFactor\", \"label\": \"板型支持\", \"type\": \"select\", \"required\": true, \"options\": [\"ATX\", \"Micro-ATX\", \"Mini-ITX\", \"E-ATX\"]},\r\n        {\"name\": \"maxGpuLength\", \"label\": \"最大显卡长度(mm)\", \"type\": \"number\"},\r\n        {\"name\": \"maxCpuCoolerHeight\", \"label\": \"最大CPU散热器高度(mm)\", \"type\": \"number\"},\r\n        {\"name\": \"fanSlots\", \"label\": \"风扇位数量\", \"type\": \"number\"},\r\n        {\"name\": \"sidePanel\", \"label\": \"侧板类型\", \"type\": \"select\", \"options\": [\"钢化玻璃\", \"亚克力\", \"金属\"]},\r\n        {\"name\": \"price\", \"label\": \"价格(元)\", \"type\": \"number\", \"required\": true},\r\n        {\"name\": \"imageUrl\", \"label\": \"图片链接\", \"type\": \"url\"}\r\n    ],\r\n    \"columns\": [\"名称\", \"品牌\", \"板型支持\", \"显卡长度\", \"散热器高度\", \"侧板\", \"价格\", \"操作\"]\r\n}', '机箱硬件配置', '2025-06-16 23:25:53', '2025-06-16 23:25:53');

-- ----------------------------
-- Table structure for message_conversations
-- ----------------------------
DROP TABLE IF EXISTS `message_conversations`;
CREATE TABLE `message_conversations`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user1_id` bigint NOT NULL COMMENT '用户1 ID（较小的用户ID）',
  `user2_id` bigint NOT NULL COMMENT '用户2 ID（较大的用户ID）',
  `last_message_id` bigint NULL DEFAULT NULL COMMENT '最后一条消息ID',
  `last_message_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '最后一条消息内容',
  `last_message_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后消息时间',
  `user1_unread_count` int NULL DEFAULT 0 COMMENT '用户1未读消息数',
  `user2_unread_count` int NULL DEFAULT 0 COMMENT '用户2未读消息数',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_users`(`user1_id` ASC, `user2_id` ASC) USING BTREE,
  INDEX `last_message_id`(`last_message_id` ASC) USING BTREE,
  INDEX `idx_user1_updated`(`user1_id` ASC, `updated_at` ASC) USING BTREE,
  INDEX `idx_user2_updated`(`user2_id` ASC, `updated_at` ASC) USING BTREE,
  INDEX `idx_last_message_time`(`last_message_time` ASC) USING BTREE,
  CONSTRAINT `message_conversations_ibfk_1` FOREIGN KEY (`user1_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `message_conversations_ibfk_2` FOREIGN KEY (`user2_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `message_conversations_ibfk_3` FOREIGN KEY (`last_message_id`) REFERENCES `private_messages` (`id`) ON DELETE SET NULL ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '消息会话表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of message_conversations
-- ----------------------------

-- ----------------------------
-- Table structure for news
-- ----------------------------
DROP TABLE IF EXISTS `news`;
CREATE TABLE `news`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `image_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `publish_time` datetime NULL DEFAULT NULL,
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `summary` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `category` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `author` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `view_count` int NULL DEFAULT 0,
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'published' COMMENT '新闻状态(published/draft)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of news
-- ----------------------------
INSERT INTO `news` VALUES (12, '16GB超大显存甜品卡 主流玩家畅玩3A首选', '如果你正在寻找一款能在2K分辨率下流畅运行主流游戏，同时兼顾创意设计和未来升级潜力的显卡，蓝宝石氮动 RX 9060 XT OC 16G D6无疑是近期甜品级市场的亮眼选择。这款显卡不仅延续了蓝宝石旗舰系列的工艺基因，更以超越公版的性能调校、扎实的散热架构和实用的16GB大显存，为中高端玩家提供了高性价比的解决方案。\r\n蓝宝石氮动 RX 9060 XT OC 16G D6的核心规格一点也不弱，显卡基于AMD新一代RDNA4架构，Navi44核心，搭载2048个流处理器，显存配置为128bit位宽的16GBGDDR6，带宽达322.3GB/s。其最大亮点在于出厂超频——加速频率提升至3324MHz（公版为3130MHz），性能更加强劲。\r\n蓝宝石氮动 RX 9060 XT OC 16G D6支持双HDMI2.1b + DP2.1a接口，可同时输出三屏8K/144Hz画面，满足多屏游戏、电竞等任务场景。尽管性能强劲，显卡仅需单8-pin供电接口，整卡功耗约180W，450W电源即可满足需求，升级成本大幅降低。\r\n如果你追求高画质+高帧率平衡，并且需处理视频剪辑、3D渲染等显存密集型任务，还希望价格方面有优势的话，蓝宝石氮动 RX 9060 XT OC 16G D6可以说是目前一个非常不错的选择。\r\n目前京东蓝宝石自营旗舰店售价3299元，正值新品促销期。若预算有限且侧重1080p游戏，8GB极地版同样值得关注；但若瞄准未来3～5年游戏趋势，16G氮动版的显存冗余与散热规格更值得入手。', '/uploads/news/6109983c-d00f-431e-9f80-a7ec1747ec66.jpg', '2025-06-17 23:27:00', '2025-06-17 23:29:33', '2025-06-18 11:12:40', '', '', '管理员', 27, 'published');
INSERT INTO `news` VALUES (13, '暗夜降临！影驰RTX5070 Ti 星曜NOX OC正式发售！', '圣洁之后，魔力无穷！继纯白旗舰星曜LUNA发售之后，代表暗夜的纯黑星曜NOX也正式登场！\r\n与星曜LUNA截然不同，星曜NOX显卡采用全黑外观。以哑光黑作为主色调，使用了本代特有的圆弧外观，整体质感内敛细腻，展现了不同以往的星曜魅力。\r\n显卡正面依旧是特有的IMLARGB穹顶灯，IML工艺为星曜显卡的正面灯组表面带来了一层硬化镀膜，与星轨灯效搭配起来相得益彰。\r\n影驰50系星曜显卡全系采用了一体式磁吸设计，方便玩家快拆显卡，轻松完成DIY定制以及显卡除尘。\r\n同样的，星曜NOX显卡也配备了S-Mode（标准）与P-Mode（性能）两种预设频率，玩家们可轻松一键切换，调节BIOS模式。\r\nRTX5070 Ti星曜NOX显卡会原生附带一款镜面背板，同时赠送一块原色背板。通过原生镜面背板，机箱的玩灯效果大大增强，轻松玩出不一样的RGB特效。\r\n\r\n除原色背板，星曜娘专属贴纸也会作为赠品置于箱内，玩家可根据自己的需求进行更改或涂装，打造独属于你的星曜显卡。\r\n目前影驰RTX5070 Ti 星曜NOX显卡已经在影驰各大电商平台上线！感兴趣的朋友可以点击上方卡片进行购买！', '/uploads/news/1e41bb8c-4ae0-4090-b6b7-68050190f716.jpg', '2025-06-18 08:40:00', '2025-06-18 08:41:35', '2025-06-18 11:18:08', '', '产品动态', '管理员', 15, 'published');
INSERT INTO `news` VALUES (14, '技嘉RTX5070雪鹰显卡限时直降千元', '技嘉 GeForce RTX 5070 AERO OC 12G 雪鹰显卡采用全新DLSS4技术，支持AI智能学习带来更流畅的游戏体验和更高效的图形处理能力，专为高性能游戏与专业创作打造。\r\n\r\n目前在京东活动售价5899元，每日零点可下单领取西安消费券满5000减500元，并参与满1元打9折及PLUS会员立减29.49元的多重优惠叠加，最终实付仅需5169.51元即可入手，对比原价6099元节省近千元，是入手高端显卡的理想时机。', '/uploads/news/0d21ea8a-be79-448e-8cca-407ae136715c.jpg', '2025-06-18 09:35:00', '2025-06-18 09:36:22', '2025-06-18 11:18:03', '', '产品动态', '管理员', 12, 'published');
INSERT INTO `news` VALUES (15, '比电影有意思多了！RTX5080 超级冰龙开启视效拉满的《沙丘：觉醒》', '由Funcom工作室开发的开放世界多人在线游戏《沙丘：觉醒》已正式登陆PC平台。\r\n游戏制作灵感源于弗兰克·赫伯特经典科幻小说《沙丘》，并深度结合丹尼斯·维伦纽瓦电影版世界观。通过生存玩法和宏大的开放世界设定备受科幻迷们的瞩目。\r\n\r\n与电影「沙丘」有什么关联？', '/uploads/news/25a1269b-d444-4118-8ea5-3c16d715d06c.jpg', '2025-06-18 11:15:00', '2025-06-18 11:16:13', '2025-06-18 11:18:16', '', '行业动态', '管理员', 4, 'published');

-- ----------------------------
-- Table structure for notifications
-- ----------------------------
DROP TABLE IF EXISTS `notifications`;
CREATE TABLE `notifications`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `is_read` tinyint(1) NOT NULL DEFAULT 0,
  `created_at` datetime NOT NULL,
  `read_at` datetime NULL DEFAULT NULL,
  `sender_username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `sender_avatar_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `related_post_id` bigint NULL DEFAULT NULL,
  `related_comment_id` bigint NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_notifications_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_notifications_created_at`(`created_at` ASC) USING BTREE,
  INDEX `idx_notifications_is_read`(`is_read` ASC) USING BTREE,
  INDEX `related_post_id`(`related_post_id` ASC) USING BTREE,
  INDEX `related_comment_id`(`related_comment_id` ASC) USING BTREE,
  CONSTRAINT `notifications_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `notifications_ibfk_2` FOREIGN KEY (`related_post_id`) REFERENCES `posts` (`id`) ON DELETE SET NULL ON UPDATE RESTRICT,
  CONSTRAINT `notifications_ibfk_3` FOREIGN KEY (`related_comment_id`) REFERENCES `comments` (`id`) ON DELETE SET NULL ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of notifications
-- ----------------------------
INSERT INTO `notifications` VALUES (4, 6, '12', '12', 'SYSTEM', 1, '2025-06-17 22:23:53', '2025-06-18 11:19:44', NULL, NULL, NULL, NULL);
INSERT INTO `notifications` VALUES (5, 12, '系统通知示例', '系统通知示例', 'SYSTEM', 1, '2025-06-18 09:56:53', '2025-06-18 10:17:41', NULL, NULL, NULL, NULL);
INSERT INTO `notifications` VALUES (6, 6, '系统通知示例', '系统通知示例', 'SYSTEM', 1, '2025-06-18 09:56:53', '2025-06-18 11:19:43', NULL, NULL, NULL, NULL);
INSERT INTO `notifications` VALUES (7, 13, '系统通知测试样例', '系统通知测试样例', 'SYSTEM', 0, '2025-06-18 11:17:22', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `notifications` VALUES (8, 12, '系统通知测试样例', '系统通知测试样例', 'SYSTEM', 0, '2025-06-18 11:17:22', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `notifications` VALUES (9, 6, '系统通知测试样例', '系统通知测试样例', 'SYSTEM', 1, '2025-06-18 11:17:22', '2025-06-18 11:19:43', NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for post_images
-- ----------------------------
DROP TABLE IF EXISTS `post_images`;
CREATE TABLE `post_images`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `post_id` bigint NOT NULL,
  `image_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `sort_order` tinyint NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_post_images_post`(`post_id` ASC) USING BTREE,
  CONSTRAINT `post_images_ibfk_1` FOREIGN KEY (`post_id`) REFERENCES `posts` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of post_images
-- ----------------------------
INSERT INTO `post_images` VALUES (16, 39, '/uploads/59303a727c6942e783432d5cbf93be87.jpg', 0);

-- ----------------------------
-- Table structure for post_likes
-- ----------------------------
DROP TABLE IF EXISTS `post_likes`;
CREATE TABLE `post_likes`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `post_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  `created_at` datetime NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_post_user`(`post_id` ASC, `user_id` ASC) USING BTREE,
  INDEX `user_id`(`user_id` ASC) USING BTREE,
  CONSTRAINT `post_likes_ibfk_1` FOREIGN KEY (`post_id`) REFERENCES `posts` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `post_likes_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 97 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of post_likes
-- ----------------------------
INSERT INTO `post_likes` VALUES (94, 39, 6, '2025-06-18 10:14:54');

-- ----------------------------
-- Table structure for posts
-- ----------------------------
DROP TABLE IF EXISTS `posts`;
CREATE TABLE `posts`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `image_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NULL DEFAULT NULL,
  `last_activity` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `config_id` bigint NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_posts_created_at`(`created_at` ASC) USING BTREE,
  INDEX `user_id`(`user_id` ASC) USING BTREE,
  CONSTRAINT `posts_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 41 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of posts
-- ----------------------------
INSERT INTO `posts` VALUES (39, 12, '我是我的世界大神籽岷', '我是我的世界大神籽岷', NULL, '2025-06-18 10:14:19', NULL, '2025-06-18 10:14:19', NULL);
INSERT INTO `posts` VALUES (40, 13, '我这套配置怎么样', '我这套配置怎么样', NULL, '2025-06-18 11:12:02', NULL, '2025-06-18 11:12:01', 4);

-- ----------------------------
-- Table structure for private_messages
-- ----------------------------
DROP TABLE IF EXISTS `private_messages`;
CREATE TABLE `private_messages`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `sender_id` bigint NOT NULL COMMENT '发送者ID',
  `receiver_id` bigint NOT NULL COMMENT '接收者ID',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '消息内容',
  `is_read` tinyint(1) NULL DEFAULT 0 COMMENT '是否已读',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_sender_receiver`(`sender_id` ASC, `receiver_id` ASC) USING BTREE,
  INDEX `idx_receiver_unread`(`receiver_id` ASC, `is_read` ASC) USING BTREE,
  INDEX `idx_created_at`(`created_at` ASC) USING BTREE,
  CONSTRAINT `private_messages_ibfk_1` FOREIGN KEY (`sender_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `private_messages_ibfk_2` FOREIGN KEY (`receiver_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '私信表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of private_messages
-- ----------------------------

-- ----------------------------
-- Table structure for user_configs
-- ----------------------------
DROP TABLE IF EXISTS `user_configs`;
CREATE TABLE `user_configs`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `config_data` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_configs
-- ----------------------------
INSERT INTO `user_configs` VALUES (2, 6, '1', '1', '{\"components\":{\"cpu\":{\"id\":1,\"name\":\"AMD Ryzen 7 9950X3D\",\"brand\":\"AMD\",\"price\":2299,\"specs\":\"8核16线程 4.5GHz\",\"generation\":\"\",\"socket\":\"\",\"cores\":0,\"threads\":0},\"motherboard\":null,\"ram\":null,\"gpu\":null,\"storage\":[],\"psu\":null,\"case\":null}}', '2025-06-17 12:28:00', '2025-06-17 12:28:00');
INSERT INTO `user_configs` VALUES (3, 6, '11', '1', '{\"components\":{\"cpu\":{\"id\":29,\"name\":\"Intel Core i9-14900K\",\"brand\":\"Intel\",\"price\":4599,\"specs\":\"{\\\"cores\\\": 24, \\\"threads\\\": 32, \\\"base_clock\\\": \\\"3.2GHz\\\", \\\"boost_clock\\\": \\\"6.0GHz\\\", \\\"socket\\\": \\\"LGA1700\\\", \\\"tdp\\\": \\\"125W\\\"}\",\"generation\":\"\",\"socket\":\"LGA1700\",\"cores\":24,\"threads\":32},\"motherboard\":{\"id\":73,\"name\":\"ASUS ROG MAXIMUS Z790 HERO\",\"brand\":\"ASUS\",\"price\":4299,\"specs\":\"{\\\"socket\\\": \\\"LGA1700\\\", \\\"memory_slots\\\": 4, \\\"max_memory\\\": \\\"128GB\\\", \\\"memory_type\\\": \\\"DDR5\\\", \\\"pcie_slots\\\": \\\"4x PCIe 5.0\\\", \\\"wifi\\\": \\\"WiFi 6E\\\", \\\"ethernet\\\": \\\"2.5Gb\\\"}\",\"chipset\":\"\",\"socket\":\"LGA1700\",\"memoryType\":\"\"},\"ram\":{\"id\":85,\"name\":\"Corsair Dominator Platinum RGB DDR5-6000 32GB\",\"brand\":\"Corsair\",\"price\":2299,\"specs\":\"{\\\"capacity\\\": \\\"32GB\\\", \\\"speed\\\": \\\"DDR5-6000\\\", \\\"timings\\\": \\\"CL30\\\", \\\"voltage\\\": \\\"1.35V\\\", \\\"kit\\\": \\\"2x16GB\\\", \\\"rgb\\\": \\\"是\\\"}\",\"type\":\"\",\"speed\":\"DDR5-6000\",\"capacity\":\"32GB\"},\"gpu\":{\"id\":48,\"name\":\"NVIDIA GeForce RTX 4090\",\"brand\":\"NVIDIA\",\"price\":12999,\"specs\":\"{\\\"memory\\\": \\\"24GB GDDR6X\\\", \\\"cuda_cores\\\": 16384, \\\"base_clock\\\": \\\"2230MHz\\\", \\\"boost_clock\\\": \\\"2520MHz\\\", \\\"memory_bandwidth\\\": \\\"1008GB/s\\\", \\\"tdp\\\": \\\"450W\\\"}\",\"series\":\"\",\"memory\":\"24GB GDDR6X\",\"memoryType\":\"\"},\"storage\":[{\"id\":95,\"name\":\"Samsung 990 PRO 4TB\",\"brand\":\"Samsung\",\"price\":3299,\"specs\":\"{\\\"capacity\\\": \\\"4TB\\\", \\\"interface\\\": \\\"PCIe 4.0 x4\\\", \\\"read_speed\\\": \\\"7450MB/s\\\", \\\"write_speed\\\": \\\"6900MB/s\\\", \\\"form_factor\\\": \\\"M.2 2280\\\", \\\"warranty\\\": \\\"5年\\\"}\",\"type\":\"\",\"capacity\":\"4TB\",\"interface\":\"PCIe 4.0 x4\"}],\"psu\":{\"id\":114,\"name\":\"Corsair AX1600i\",\"brand\":\"Corsair\",\"price\":3299,\"specs\":\"{\\\"wattage\\\": \\\"1600W\\\", \\\"efficiency\\\": \\\"80+ Titanium\\\", \\\"modular\\\": \\\"全模组\\\", \\\"warranty\\\": \\\"10年\\\", \\\"fan_size\\\": \\\"140mm\\\", \\\"cables\\\": \\\"扁平线\\\"}\",\"wattage\":\"1600W\",\"efficiency\":\"80+ Titanium\",\"modular\":\"全模组\"},\"case\":{\"id\":124,\"name\":\"Lian Li PC-O11 Dynamic EVO\",\"brand\":\"Lian Li\",\"price\":1299,\"specs\":\"{\\\"form_factor\\\": \\\"ATX\\\", \\\"max_gpu_length\\\": \\\"420mm\\\", \\\"max_cpu_cooler\\\": \\\"155mm\\\", \\\"drive_bays\\\": \\\"2x 3.5\\\", 4x 2.5\\\"\\\", \\\"fans_included\\\": \\\"无\\\", \\\"side_panel\\\": \\\"钢化玻璃\\\"}\",\"formFactor\":\"\"}}}', '2025-06-17 14:42:05', '2025-06-17 14:42:05');
INSERT INTO `user_configs` VALUES (4, 13, '很好', '很好', '{\"components\":{\"cpu\":{\"id\":29,\"name\":\"Intel Core i9-14900K\",\"brand\":\"Intel\",\"price\":4599,\"specs\":\"{\\\"cores\\\": 24, \\\"threads\\\": 32, \\\"base_clock\\\": \\\"3.2GHz\\\", \\\"boost_clock\\\": \\\"6.0GHz\\\", \\\"socket\\\": \\\"LGA1700\\\", \\\"tdp\\\": \\\"125W\\\"}\",\"generation\":\"\",\"socket\":\"LGA1700\",\"cores\":24,\"threads\":32},\"motherboard\":{\"id\":73,\"name\":\"ASUS ROG MAXIMUS Z790 HERO\",\"brand\":\"ASUS\",\"price\":4299,\"specs\":\"{\\\"socket\\\": \\\"LGA1700\\\", \\\"memory_slots\\\": 4, \\\"max_memory\\\": \\\"128GB\\\", \\\"memory_type\\\": \\\"DDR5\\\", \\\"pcie_slots\\\": \\\"4x PCIe 5.0\\\", \\\"wifi\\\": \\\"WiFi 6E\\\", \\\"ethernet\\\": \\\"2.5Gb\\\"}\",\"chipset\":\"\",\"socket\":\"LGA1700\",\"memoryType\":\"\"},\"ram\":{\"id\":85,\"name\":\"Corsair Dominator Platinum RGB DDR5-6000 32GB\",\"brand\":\"Corsair\",\"price\":2299,\"specs\":\"{\\\"capacity\\\": \\\"32GB\\\", \\\"speed\\\": \\\"DDR5-6000\\\", \\\"timings\\\": \\\"CL30\\\", \\\"voltage\\\": \\\"1.35V\\\", \\\"kit\\\": \\\"2x16GB\\\", \\\"rgb\\\": \\\"是\\\"}\",\"type\":\"\",\"speed\":\"DDR5-6000\",\"capacity\":\"32GB\"},\"gpu\":{\"id\":48,\"name\":\"NVIDIA GeForce RTX 4090\",\"brand\":\"NVIDIA\",\"price\":12999,\"specs\":\"{\\\"memory\\\": \\\"24GB GDDR6X\\\", \\\"cuda_cores\\\": 16384, \\\"base_clock\\\": \\\"2230MHz\\\", \\\"boost_clock\\\": \\\"2520MHz\\\", \\\"memory_bandwidth\\\": \\\"1008GB/s\\\", \\\"tdp\\\": \\\"450W\\\"}\",\"series\":\"\",\"memory\":\"24GB GDDR6X\",\"memoryType\":\"\"},\"storage\":[{\"id\":95,\"name\":\"Samsung 990 PRO 4TB\",\"brand\":\"Samsung\",\"price\":3299,\"specs\":\"{\\\"capacity\\\": \\\"4TB\\\", \\\"interface\\\": \\\"PCIe 4.0 x4\\\", \\\"read_speed\\\": \\\"7450MB/s\\\", \\\"write_speed\\\": \\\"6900MB/s\\\", \\\"form_factor\\\": \\\"M.2 2280\\\", \\\"warranty\\\": \\\"5年\\\"}\",\"type\":\"\",\"capacity\":\"4TB\",\"interface\":\"PCIe 4.0 x4\"}],\"psu\":{\"id\":114,\"name\":\"Corsair AX1600i\",\"brand\":\"Corsair\",\"price\":3299,\"specs\":\"{\\\"wattage\\\": \\\"1600W\\\", \\\"efficiency\\\": \\\"80+ Titanium\\\", \\\"modular\\\": \\\"全模组\\\", \\\"warranty\\\": \\\"10年\\\", \\\"fan_size\\\": \\\"140mm\\\", \\\"cables\\\": \\\"扁平线\\\"}\",\"wattage\":\"1600W\",\"efficiency\":\"80+ Titanium\",\"modular\":\"全模组\"},\"case\":{\"id\":124,\"name\":\"Lian Li PC-O11 Dynamic EVO\",\"brand\":\"Lian Li\",\"price\":1299,\"specs\":\"{\\\"form_factor\\\": \\\"ATX\\\", \\\"max_gpu_length\\\": \\\"420mm\\\", \\\"max_cpu_cooler\\\": \\\"155mm\\\", \\\"drive_bays\\\": \\\"2x 3.5\\\", 4x 2.5\\\"\\\", \\\"fans_included\\\": \\\"无\\\", \\\"side_panel\\\": \\\"钢化玻璃\\\"}\",\"formFactor\":\"\"}}}', '2025-06-18 11:11:50', '2025-06-18 11:11:50');

-- ----------------------------
-- Table structure for user_follows
-- ----------------------------
DROP TABLE IF EXISTS `user_follows`;
CREATE TABLE `user_follows`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `follower_id` bigint NOT NULL,
  `following_id` bigint NOT NULL,
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_follows`(`follower_id` ASC, `following_id` ASC) USING BTREE,
  INDEX `fk_following`(`following_id` ASC) USING BTREE,
  CONSTRAINT `fk_follower` FOREIGN KEY (`follower_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `fk_following` FOREIGN KEY (`following_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_follows
-- ----------------------------
INSERT INTO `user_follows` VALUES (4, 12, 6, '2025-06-18 10:05:48');
INSERT INTO `user_follows` VALUES (5, 6, 12, '2025-06-18 10:28:47');
INSERT INTO `user_follows` VALUES (7, 13, 6, '2025-06-18 11:13:19');

-- ----------------------------
-- Table structure for user_roles
-- ----------------------------
DROP TABLE IF EXISTS `user_roles`;
CREATE TABLE `user_roles`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `role_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_user`(`user_id` ASC) USING BTREE,
  CONSTRAINT `fk_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_roles
-- ----------------------------
INSERT INTO `user_roles` VALUES (1, 6, 'ADMIN');
INSERT INTO `user_roles` VALUES (2, 6, 'USER');
INSERT INTO `user_roles` VALUES (7, 12, 'USER');
INSERT INTO `user_roles` VALUES (8, 13, 'USER');

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `enabled` tinyint(1) NOT NULL DEFAULT 0,
  `email_verify_code` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `created_at` datetime NOT NULL,
  `avatar_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `signature` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `background_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `ban_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '封禁类型',
  `ban_reason` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '封禁原因',
  `ban_until` datetime NULL DEFAULT NULL COMMENT '封禁截止时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username` ASC) USING BTREE,
  UNIQUE INDEX `email`(`email` ASC) USING BTREE,
  INDEX `idx_users_email`(`email` ASC) USING BTREE,
  INDEX `idx_users_username`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES (6, 'wiwjs22b', '$2a$10$L8OTh9gxjktRDvx6m33yu.mUqHoLLkIp2PrS6xEQlh7mmxT7Zn0g6', 'wiwjs22b@qq.com', 1, NULL, '2025-06-10 11:28:08', '/uploads/avatar_a67d367f-fd40-43cd-a40b-4c9afa99b905.bmp', '天下为公', '/uploads/background_cdb29ded-d438-4ebb-acfc-a9fb1eb8cf6f.jpg', NULL, NULL, NULL);
INSERT INTO `users` VALUES (12, 'GouC', '$2a$10$W5o7imTmgEcquA1P6J4qPeVVYv1osttSOw0/OBAa7WxauPJ9GUuPG', '3053461866@qq.com', 1, '6126', '2025-06-16 16:53:53', '/uploads/avatar_a9aef32c-5ec9-45c8-a7fd-76f8e3730f34.jpg', '人民万岁', NULL, 'TEMPORARY', '1', '2025-06-18 11:15:14');
INSERT INTO `users` VALUES (13, 'GouCYou', '$2a$10$SXVx7UbXU2Ks9k2tlDgH5emDzqvHCHEihqTD7M3sEpzDEvgr.oWwS', 'goucyou@foxmail.com', 1, '7851', '2025-06-18 11:10:29', NULL, '我很牛', '/uploads/background_db350084-1317-4df4-976d-06489338768b.jpg', 'TEMPORARY', '封号', '2025-06-18 11:14:45');

SET FOREIGN_KEY_CHECKS = 1;
