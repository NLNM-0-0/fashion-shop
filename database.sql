/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

DROP TABLE IF EXISTS `cart`;
CREATE TABLE `cart` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `color` enum('BLACK','BLUE','BROWN','GREEN','GREY','MULTI_COLOR','ORANGE','PINK','PURPLE','RED','WHITE','YELLOW') DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `size` text,
  `item_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK9fhia6b3ekuddn6pkxlsks7rr` (`item_id`),
  KEY `FKl70asp4l4w0jmbm1tqyofho4o` (`user_id`),
  CONSTRAINT `FK9fhia6b3ekuddn6pkxlsks7rr` FOREIGN KEY (`item_id`) REFERENCES `item` (`id`),
  CONSTRAINT `FKl70asp4l4w0jmbm1tqyofho4o` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `cart_chk_1` CHECK ((`quantity` >= 0))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `category`;
CREATE TABLE `category` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` text NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `item`;
CREATE TABLE `item` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `gender` enum('MEN','WOMEN','KIDS','UNISEX') NOT NULL,
  `images` text,
  `is_deleted` bit(1) NOT NULL,
  `name` varchar(200) NOT NULL,
  `season` enum('SPRING','SUMMER','FALL','WINTER') NOT NULL,
  `sold` int NOT NULL,
  `unit_price` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Item's name` (`name`),
  CONSTRAINT `item_chk_1` CHECK ((`unit_price` >= 0))
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `item_categories`;
CREATE TABLE `item_categories` (
  `item_id` bigint NOT NULL,
  `categories_id` bigint NOT NULL,
  KEY `FKst2hlludpmv2moi198vokmowc` (`categories_id`),
  KEY `FKjfe6s3h4bfur817d80v7aori8` (`item_id`),
  CONSTRAINT `FKjfe6s3h4bfur817d80v7aori8` FOREIGN KEY (`item_id`) REFERENCES `item` (`id`),
  CONSTRAINT `FKst2hlludpmv2moi198vokmowc` FOREIGN KEY (`categories_id`) REFERENCES `category` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `item_quantity`;
CREATE TABLE `item_quantity` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `color` text,
  `quantity` int DEFAULT NULL,
  `size` text,
  `item_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKyey196sj0kngsmkqe8dxov08` (`item_id`),
  CONSTRAINT `FKyey196sj0kngsmkqe8dxov08` FOREIGN KEY (`item_id`) REFERENCES `item` (`id`),
  CONSTRAINT `item_quantity_chk_1` CHECK ((`quantity` >= 0))
) ENGINE=InnoDB AUTO_INCREMENT=193 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `like_item`;
CREATE TABLE `like_item` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `item_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Item in liked list` (`user_id`,`item_id`),
  KEY `FKt2ihlmqhe1baqx2vc91yt40lw` (`item_id`),
  CONSTRAINT `FKt28l2vt4lk4y6jpvd5o6id3h2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKt2ihlmqhe1baqx2vc91yt40lw` FOREIGN KEY (`item_id`) REFERENCES `item` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `notification`;
CREATE TABLE `notification` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `description` varchar(200) NOT NULL,
  `seen` bit(1) NOT NULL,
  `title` varchar(100) NOT NULL,
  `from_user` bigint NOT NULL,
  `to_user` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKm8pdp1mapclpr8iicppfa0olm` (`from_user`),
  KEY `FK2pgfwws64peg4hjubcwo8tvle` (`to_user`),
  CONSTRAINT `FK2pgfwws64peg4hjubcwo8tvle` FOREIGN KEY (`to_user`) REFERENCES `user` (`id`),
  CONSTRAINT `FKm8pdp1mapclpr8iicppfa0olm` FOREIGN KEY (`from_user`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `address` varchar(255) DEFAULT NULL,
  `canceled_at` datetime(6) DEFAULT NULL,
  `confirmed_at` datetime(6) DEFAULT NULL,
  `created_at` datetime(6) NOT NULL,
  `done_at` datetime(6) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `shipping_at` datetime(6) DEFAULT NULL,
  `status` enum('PENDING','CONFIRMED','SHIPPING','DONE','CANCELED') NOT NULL,
  `total_price` int DEFAULT NULL,
  `total_quantity` int DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `customer_id` bigint DEFAULT NULL,
  `staff_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK14n2jkmoyhpimhracvcdy7sst` (`customer_id`),
  KEY `FKt00kinkf6xry4b0h9k5wum1tx` (`staff_id`),
  CONSTRAINT `FK14n2jkmoyhpimhracvcdy7sst` FOREIGN KEY (`customer_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKt00kinkf6xry4b0h9k5wum1tx` FOREIGN KEY (`staff_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `orders_detail`;
CREATE TABLE `orders_detail` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `color` enum('BLACK','BLUE','BROWN','GREEN','GREY','MULTI_COLOR','ORANGE','PINK','PURPLE','RED','WHITE','YELLOW') DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `size` text,
  `unit_price` int DEFAULT NULL,
  `item_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK5fur9ob8de2qnhrv2h8u7wahg` (`item_id`),
  CONSTRAINT `FK5fur9ob8de2qnhrv2h8u7wahg` FOREIGN KEY (`item_id`) REFERENCES `item` (`id`),
  CONSTRAINT `orders_detail_chk_1` CHECK ((`quantity` >= 1)),
  CONSTRAINT `orders_detail_chk_2` CHECK ((`unit_price` >= 0))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `orders_order_details`;
CREATE TABLE `orders_order_details` (
  `order_id` bigint NOT NULL,
  `order_details_id` bigint NOT NULL,
  UNIQUE KEY `UK_22wlf6q0mc7mkuqp7bvm3dm2d` (`order_details_id`),
  KEY `FK95iwi9c75iprmwbnr63vq4863` (`order_id`),
  CONSTRAINT `FK95iwi9c75iprmwbnr63vq4863` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`),
  CONSTRAINT `FKqbo3e15evqa8b8tid4pfrdiq6` FOREIGN KEY (`order_details_id`) REFERENCES `orders_detail` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `otp`;
CREATE TABLE `otp` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `expiry_date` datetime(6) DEFAULT NULL,
  `otp` varchar(255) DEFAULT NULL,
  `retry` int NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_4mkxc1wpojj1vymcvurokktwm` (`user_id`),
  CONSTRAINT `FKdrrkob03otk15fxe9b0bkkp35` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `otp_chk_1` CHECK (((`retry` <= 5) and (`retry` >= 0)))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `password_reset_token`;
CREATE TABLE `password_reset_token` (
  `id` int NOT NULL AUTO_INCREMENT,
  `expiry_date` datetime(6) DEFAULT NULL,
  `token` varchar(255) DEFAULT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKos4dl59qmc1h9591pyqcpsakq` (`user_id`),
  CONSTRAINT `FKos4dl59qmc1h9591pyqcpsakq` FOREIGN KEY (`user_id`) REFERENCES `user_auth` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `stock_change_history`;
CREATE TABLE `stock_change_history` (
  `id` int NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `quantity` int DEFAULT NULL,
  `quantity_left` int DEFAULT NULL,
  `type` enum('SELL','INCREASE','DECREASE','PAYBACK') NOT NULL,
  `item_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKb4adnu0v1rm7ps6rtymbcgldt` (`item_id`),
  CONSTRAINT `FKb4adnu0v1rm7ps6rtymbcgldt` FOREIGN KEY (`item_id`) REFERENCES `item` (`id`),
  CONSTRAINT `stock_change_history_chk_1` CHECK ((`quantity_left` >= 0))
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `stock_report`;
CREATE TABLE `stock_report` (
  `id` int NOT NULL AUTO_INCREMENT,
  `decrease` int DEFAULT NULL,
  `final` int DEFAULT NULL,
  `increase` int DEFAULT NULL,
  `initial` int DEFAULT NULL,
  `payback` int DEFAULT NULL,
  `sell` int DEFAULT NULL,
  `time_from` datetime(6) DEFAULT NULL,
  `time_to` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `stock_report_detail`;
CREATE TABLE `stock_report_detail` (
  `id` int NOT NULL AUTO_INCREMENT,
  `decrease` int DEFAULT NULL,
  `final` int DEFAULT NULL,
  `increase` int DEFAULT NULL,
  `initial` int DEFAULT NULL,
  `payback` int DEFAULT NULL,
  `sell` int DEFAULT NULL,
  `item_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKa3roxcyo4wwg2rbmoja1dkcit` (`item_id`),
  CONSTRAINT `FKa3roxcyo4wwg2rbmoja1dkcit` FOREIGN KEY (`item_id`) REFERENCES `item` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `stock_report_details`;
CREATE TABLE `stock_report_details` (
  `stock_report_id` int NOT NULL,
  `details_id` int NOT NULL,
  UNIQUE KEY `UK_46hm2ny9cl1b4q3r9vvf7fqj8` (`details_id`),
  KEY `FKe2ekf1cho58oumlhw44uj0ygy` (`stock_report_id`),
  CONSTRAINT `FK29hcppx5m36kdut16pahwip99` FOREIGN KEY (`details_id`) REFERENCES `stock_report_detail` (`id`),
  CONSTRAINT `FKe2ekf1cho58oumlhw44uj0ygy` FOREIGN KEY (`stock_report_id`) REFERENCES `stock_report` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `address` varchar(50) NOT NULL,
  `dob` varchar(10) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `image` text NOT NULL,
  `male` bit(1) NOT NULL,
  `name` varchar(200) NOT NULL,
  `phone` varchar(11) DEFAULT NULL,
  `user_auth_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_b6973dkj4eo2p8wa04r90l1ld` (`user_auth_id`),
  UNIQUE KEY `Phone` (`phone`),
  UNIQUE KEY `Email` (`email`),
  CONSTRAINT `FKceqgvkipb0do1j0xr4neax6ek` FOREIGN KEY (`user_auth_id`) REFERENCES `user_auth` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `user_auth`;
CREATE TABLE `user_auth` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `is_admin` bit(1) NOT NULL,
  `is_deleted` bit(1) NOT NULL,
  `is_verified` bit(1) NOT NULL,
  `password` varchar(255) NOT NULL,
  `phone` varchar(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



INSERT INTO `category` (`id`, `name`) VALUES
(1, 'Hoodies & Sweatshirts');
INSERT INTO `category` (`id`, `name`) VALUES
(2, 'T-Shirts');
INSERT INTO `category` (`id`, `name`) VALUES
(3, 'Casual Tops');
INSERT INTO `category` (`id`, `name`) VALUES
(4, 'Pants'),
(5, 'Shorts'),
(6, 'Sneakers'),
(7, 'Shirts & Polos'),
(8, 'Hats'),
(10, 'Jackets'),
(11, 'Boots');

INSERT INTO `item` (`id`, `created_at`, `gender`, `images`, `is_deleted`, `name`, `season`, `sold`, `unit_price`) VALUES
(1, '2025-01-04 02:55:01.386000', 'WOMEN', '[\"https://firebasestorage.googleapis.com/v0/b/fashion-shop-94e75.appspot.com/o/tnf40_a2b5b26a-cbf2-4d00-9ef3-086f27ed0448.png?alt=media\",\"https://firebasestorage.googleapis.com/v0/b/fashion-shop-94e75.appspot.com/o/tnf41_8211be45-f1e3-44f9-9a9f-08d3718fe239.png?alt=media\"]', 0, 'Women’s Short-Sleeve Climb Tee', 'SPRING', 0, 525000);
INSERT INTO `item` (`id`, `created_at`, `gender`, `images`, `is_deleted`, `name`, `season`, `sold`, `unit_price`) VALUES
(2, '2025-01-04 02:59:16.552000', 'WOMEN', '[\"https://firebasestorage.googleapis.com/v0/b/fashion-shop-94e75.appspot.com/o/tnf15_362d6ae2-ff0d-4a53-a803-4c9782e65a5e.png?alt=media\",\"https://firebasestorage.googleapis.com/v0/b/fashion-shop-94e75.appspot.com/o/tnf11_b8140205-b289-4a81-b2ef-9ced8ef1d1b4.png?alt=media\",\"https://firebasestorage.googleapis.com/v0/b/fashion-shop-94e75.appspot.com/o/tnf12_acea5a08-9750-46c0-8ffe-76aecb7df17f.png?alt=media\",\"https://firebasestorage.googleapis.com/v0/b/fashion-shop-94e75.appspot.com/o/tnf13_15d6a241-edc2-4b23-839f-49acc010af83.png?alt=media\",\"https://firebasestorage.googleapis.com/v0/b/fashion-shop-94e75.appspot.com/o/tnf14_aa1e0cd2-ae7a-4035-b549-a3f433f0708d.png?alt=media\"]', 0, 'Women’s 1996 Retro Nuptse Jacket', 'WINTER', 0, 3270000);
INSERT INTO `item` (`id`, `created_at`, `gender`, `images`, `is_deleted`, `name`, `season`, `sold`, `unit_price`) VALUES
(3, '2025-01-04 03:01:31.763000', 'WOMEN', '[\"https://firebasestorage.googleapis.com/v0/b/fashion-shop-94e75.appspot.com/o/tnf16_8ce020c6-c36b-48ff-9c6e-ad2cc4c61f50.png?alt=media\",\"https://firebasestorage.googleapis.com/v0/b/fashion-shop-94e75.appspot.com/o/tnf17_bdd5a79f-9844-4811-b4df-5ea91f0e5288.png?alt=media\",\"https://firebasestorage.googleapis.com/v0/b/fashion-shop-94e75.appspot.com/o/tnf18_71161974-e584-4a39-bc92-3e1dc66743b8.png?alt=media\"]', 0, 'Women’s Shelbe Raschel Hoodie', 'WINTER', 0, 2890000);
INSERT INTO `item` (`id`, `created_at`, `gender`, `images`, `is_deleted`, `name`, `season`, `sold`, `unit_price`) VALUES
(4, '2025-01-04 03:03:33.721000', 'WOMEN', '[\"https://firebasestorage.googleapis.com/v0/b/fashion-shop-94e75.appspot.com/o/tnf1_d627ec23-c1ae-4282-8915-089cc36ddf21.png?alt=media\",\"https://firebasestorage.googleapis.com/v0/b/fashion-shop-94e75.appspot.com/o/tnf2png_ac94e414-86eb-42ef-bbdf-01dcabc1490b.png?alt=media\",\"https://firebasestorage.googleapis.com/v0/b/fashion-shop-94e75.appspot.com/o/tnf3_b57ed0d4-28c9-4d81-8e67-9697600d5e65.png?alt=media\",\"https://firebasestorage.googleapis.com/v0/b/fashion-shop-94e75.appspot.com/o/tnf4_41512468-8843-4138-942c-71019b50b98f.png?alt=media\"]', 0, 'Women’s Aconcagua 3 Jacket', 'WINTER', 0, 5360000),
(5, '2025-01-04 03:05:23.560000', 'WOMEN', '[\"https://firebasestorage.googleapis.com/v0/b/fashion-shop-94e75.appspot.com/o/tnf6_e8ffc00a-2cc5-4776-a6bb-50dd916045fa.png?alt=media\",\"https://firebasestorage.googleapis.com/v0/b/fashion-shop-94e75.appspot.com/o/tnf7_dfb3e873-ae87-40b3-b982-9762e5d8a6f6.png?alt=media\"]', 0, 'Women’s Terra Peak Jacket', 'WINTER', 0, 2790000),
(6, '2025-01-04 03:07:33.228000', 'WOMEN', '[\"https://firebasestorage.googleapis.com/v0/b/fashion-shop-94e75.appspot.com/o/tnf42_c986395a-ece4-47da-b25a-0b06df390544.png?alt=media\",\"https://firebasestorage.googleapis.com/v0/b/fashion-shop-94e75.appspot.com/o/tnf43_3e8ddeae-5bc0-41a6-8a98-df7126fc29e3.png?alt=media\",\"https://firebasestorage.googleapis.com/v0/b/fashion-shop-94e75.appspot.com/o/tnf44_58a7f034-0542-4cb0-b804-070b9f00b38b.png?alt=media\"]', 0, 'Women\'s Kikash Wind Jacket', 'WINTER', 0, 1954000),
(7, '2025-01-04 03:10:51.243000', 'WOMEN', '[\"https://firebasestorage.googleapis.com/v0/b/fashion-shop-94e75.appspot.com/o/tnf47_883f8d9a-9aaa-4742-bf84-840111a3b527.png?alt=media\",\"https://firebasestorage.googleapis.com/v0/b/fashion-shop-94e75.appspot.com/o/tnf48_c804534e-bad5-470b-be5d-b87175cfa0dd.png?alt=media\",\"https://firebasestorage.googleapis.com/v0/b/fashion-shop-94e75.appspot.com/o/tnf49_0afe1fc3-b18c-4a1c-a231-1bffb2dba2f6.png?alt=media\"]', 0, 'Women’s ThermoBall Lace Up Waterproof Boots', 'WINTER', 0, 3490000),
(8, '2025-01-04 03:12:45.239000', 'WOMEN', '[\"https://firebasestorage.googleapis.com/v0/b/fashion-shop-94e75.appspot.com/o/tnf45_ba7f8e97-7de7-47a4-97ae-7775421ba80e.png?alt=media\",\"https://firebasestorage.googleapis.com/v0/b/fashion-shop-94e75.appspot.com/o/tnf46_3ff03e9e-dfc4-40f3-886c-2d793b525a49.png?alt=media\"]', 0, 'Women’s Altamesa 300 Shoes', 'SUMMER', 0, 2980000),
(9, '2025-01-04 03:14:25.651000', 'MEN', '[\"https://firebasestorage.googleapis.com/v0/b/fashion-shop-94e75.appspot.com/o/tnf37_d6ca05c0-3e8b-4eb8-a903-0e9d50d02063.png?alt=media\",\"https://firebasestorage.googleapis.com/v0/b/fashion-shop-94e75.appspot.com/o/tnf38_d0459354-2f9e-445a-98c8-51fb01492baa.png?alt=media\",\"https://firebasestorage.googleapis.com/v0/b/fashion-shop-94e75.appspot.com/o/tnf39_43f34f59-c0ff-4db5-bece-422417ee80ad.png?alt=media\"]', 0, 'Men’s Altamesa 300 Shoes', 'SUMMER', 0, 3121000),
(10, '2025-01-04 03:17:03.305000', 'MEN', '[\"https://firebasestorage.googleapis.com/v0/b/fashion-shop-94e75.appspot.com/o/tnf23_7ec0283b-dcba-44ff-8b53-3d0bb6663313.png?alt=media\",\"https://firebasestorage.googleapis.com/v0/b/fashion-shop-94e75.appspot.com/o/tnf24_966d10c0-7cac-4216-a2d6-99cc7253dbc8.png?alt=media\"]', 0, 'Men’s Chilkat V Lace Waterproof Boots', 'WINTER', 0, 3676000),
(11, '2025-01-04 03:18:54.248000', 'MEN', '[\"https://firebasestorage.googleapis.com/v0/b/fashion-shop-94e75.appspot.com/o/tnf19_558368ea-256e-456c-b07b-b0f9d90123ae.png?alt=media\",\"https://firebasestorage.googleapis.com/v0/b/fashion-shop-94e75.appspot.com/o/tnf20_629a7d62-a573-48a8-8ed3-06ce15b6e9e8.png?alt=media\",\"https://firebasestorage.googleapis.com/v0/b/fashion-shop-94e75.appspot.com/o/tnf21_86dd94b0-d180-49ab-9046-9e86ae459a7e.png?alt=media\",\"https://firebasestorage.googleapis.com/v0/b/fashion-shop-94e75.appspot.com/o/tnf22_f1ffb2d9-fe8a-4c7d-bd54-f9c4e5545b8b.png?alt=media\"]', 0, 'Men’s Back-To-Berkeley IV Leather Waterproof Boots', 'WINTER', 0, 4125000),
(12, '2025-01-04 03:21:24.889000', 'WOMEN', '[\"https://firebasestorage.googleapis.com/v0/b/fashion-shop-94e75.appspot.com/o/tnf25_66f97290-2179-42ad-91c7-bc8285c97be1.png?alt=media\",\"https://firebasestorage.googleapis.com/v0/b/fashion-shop-94e75.appspot.com/o/tnf26_692c33b1-b25f-456f-a518-ada8c3606352.png?alt=media\",\"https://firebasestorage.googleapis.com/v0/b/fashion-shop-94e75.appspot.com/o/tnf27_4ed443ef-689a-4fe0-a275-c4b7cdc0f756.png?alt=media\"]', 0, 'Women’s Nuptse Short Jacket', 'WINTER', 0, 2695000),
(13, '2025-01-04 03:24:50.955000', 'WOMEN', '[\"https://firebasestorage.googleapis.com/v0/b/fashion-shop-94e75.appspot.com/o/ss1_f4c60b03-ec85-4d63-843c-72596fe55b4b.png?alt=media\",\"https://firebasestorage.googleapis.com/v0/b/fashion-shop-94e75.appspot.com/o/ss2_a011f04f-4436-44ee-b0f6-0de448edcac3.png?alt=media\",\"https://firebasestorage.googleapis.com/v0/b/fashion-shop-94e75.appspot.com/o/ss3_b739f119-b9cc-4f2f-bfd7-161b0eb15fc1.png?alt=media\"]', 0, 'Women’s Dune Sky Long-Sleeve', 'WINTER', 0, 625000),
(14, '2025-01-04 03:27:03.354000', 'KIDS', '[\"https://firebasestorage.googleapis.com/v0/b/fashion-shop-94e75.appspot.com/o/ss8_8608dbcd-b92d-4661-bbd7-4684d7b067f8.png?alt=media\",\"https://firebasestorage.googleapis.com/v0/b/fashion-shop-94e75.appspot.com/o/ss9_e543c1a7-100c-4e0e-80f6-25ecd95d817a.png?alt=media\"]', 0, 'Girls’ Long-Sleeve Graphic Tee', 'SPRING', 0, 325000),
(15, '2025-01-04 03:29:07.779000', 'KIDS', '[\"https://firebasestorage.googleapis.com/v0/b/fashion-shop-94e75.appspot.com/o/ss10_411a5610-1611-46aa-83dc-8be5482a2b72.png?alt=media\",\"https://firebasestorage.googleapis.com/v0/b/fashion-shop-94e75.appspot.com/o/ss11_62c503f2-4225-4e91-a9a0-136144d2c8cf.png?alt=media\"]', 0, 'Boys’ & Girls’ Denali Jacket', 'SPRING', 0, 975000),
(16, '2025-01-04 03:31:02.894000', 'MEN', '[\"https://firebasestorage.googleapis.com/v0/b/fashion-shop-94e75.appspot.com/o/ss4_c46ba54f-db32-4e92-ab86-3d9fc2f9d0b1.png?alt=media\",\"https://firebasestorage.googleapis.com/v0/b/fashion-shop-94e75.appspot.com/o/ss5_4784b940-3939-47bc-a87a-36ad0ee09910.png?alt=media\",\"https://firebasestorage.googleapis.com/v0/b/fashion-shop-94e75.appspot.com/o/ss7_751737e2-0540-4238-b748-3eeca33ca357.png?alt=media\"]', 0, 'Men’s Yumiori Reversible Jacket', 'WINTER', 0, 3950000),
(17, '2025-01-04 03:32:45.208000', 'MEN', '[\"https://firebasestorage.googleapis.com/v0/b/fashion-shop-94e75.appspot.com/o/rb6_fe318ce8-5128-4801-a2f5-9db859fa71eb.png?alt=media\",\"https://firebasestorage.googleapis.com/v0/b/fashion-shop-94e75.appspot.com/o/rb7_8a008623-107a-4bf3-be8a-b54dd950248f.png?alt=media\",\"https://firebasestorage.googleapis.com/v0/b/fashion-shop-94e75.appspot.com/o/rb8_c62d2050-3916-493f-bdbf-0549c44c3ed0.png?alt=media\"]', 0, 'Men’s Big Apex Bionic 3 Jacket', 'SPRING', 0, 2765000),
(18, '2025-01-04 03:35:12.278000', 'KIDS', '[\"https://firebasestorage.googleapis.com/v0/b/fashion-shop-94e75.appspot.com/o/rb4_7201e731-d28a-49f3-9c37-19a3221c32ee.png?alt=media\",\"https://firebasestorage.googleapis.com/v0/b/fashion-shop-94e75.appspot.com/o/rb5_6231c2ce-996e-475d-9b68-0012167595a9.png?alt=media\"]', 0, 'Youth Fastpack Hiker Waterproof Shoes', 'SPRING', 0, 2765000),
(19, '2025-01-04 03:37:33.387000', 'KIDS', '[\"https://firebasestorage.googleapis.com/v0/b/fashion-shop-94e75.appspot.com/o/tnf31_ac3da005-1a5c-4cfd-89cb-4298676fdc1f.png?alt=media\",\"https://firebasestorage.googleapis.com/v0/b/fashion-shop-94e75.appspot.com/o/tnf32_329abbf4-865c-4923-82c3-94c44ef4ff16.png?alt=media\",\"https://firebasestorage.googleapis.com/v0/b/fashion-shop-94e75.appspot.com/o/tnf33_c74ba762-d63f-40c1-96de-415ccf64e390.png?alt=media\",\"https://firebasestorage.googleapis.com/v0/b/fashion-shop-94e75.appspot.com/o/tnf34_a63d1c24-3a53-4e86-9b0f-226bd31c3d5c.png?alt=media\"]', 0, 'Toddler Alpenglow II Boots', 'WINTER', 0, 2545000),
(20, '2025-01-04 03:39:39.909000', 'KIDS', '[\"https://firebasestorage.googleapis.com/v0/b/fashion-shop-94e75.appspot.com/o/tnf28_59b36b12-f855-4b32-9616-40abcb0f73cc.png?alt=media\",\"https://firebasestorage.googleapis.com/v0/b/fashion-shop-94e75.appspot.com/o/tnf29_25ef8005-5482-48c9-9da4-6f132d031aa3.png?alt=media\",\"https://firebasestorage.googleapis.com/v0/b/fashion-shop-94e75.appspot.com/o/tnf30_626117d7-3553-44d5-a38b-163628bbb6c6.png?alt=media\"]', 0, 'Boys’ & Girls’ North Down Fleece-Lined Short Parka', 'WINTER', 0, 1775000),
(21, '2025-01-04 03:41:50.308000', 'KIDS', '[\"https://firebasestorage.googleapis.com/v0/b/fashion-shop-94e75.appspot.com/o/rb1_2663c37e-266f-47bd-a227-455b923a0025.png?alt=media\",\"https://firebasestorage.googleapis.com/v0/b/fashion-shop-94e75.appspot.com/o/rb2_8bfe0416-c11e-4cb8-9311-b48675cff4a7.png?alt=media\",\"https://firebasestorage.googleapis.com/v0/b/fashion-shop-94e75.appspot.com/o/rb3_92add646-a904-4a59-be56-48dd1d3c7dd7.png?alt=media\"]', 0, 'Youth Altamesa Shoes', 'SPRING', 0, 2435000);

INSERT INTO `item_categories` (`item_id`, `categories_id`) VALUES
(1, 2);
INSERT INTO `item_categories` (`item_id`, `categories_id`) VALUES
(1, 3);
INSERT INTO `item_categories` (`item_id`, `categories_id`) VALUES
(2, 10);
INSERT INTO `item_categories` (`item_id`, `categories_id`) VALUES
(3, 10),
(4, 10),
(5, 10),
(6, 10),
(7, 6),
(8, 6),
(9, 6),
(10, 6),
(10, 11),
(11, 6),
(11, 11),
(12, 10),
(13, 1),
(13, 3),
(14, 1),
(14, 3),
(15, 10),
(16, 10),
(17, 10),
(18, 6),
(19, 6),
(19, 11),
(20, 10),
(21, 6);

INSERT INTO `item_quantity` (`id`, `color`, `quantity`, `size`, `item_id`) VALUES
(5, 'WHITE', 20, 'S', 1);
INSERT INTO `item_quantity` (`id`, `color`, `quantity`, `size`, `item_id`) VALUES
(6, 'WHITE', 20, 'XL', 1);
INSERT INTO `item_quantity` (`id`, `color`, `quantity`, `size`, `item_id`) VALUES
(7, 'WHITE', 20, 'L', 1);
INSERT INTO `item_quantity` (`id`, `color`, `quantity`, `size`, `item_id`) VALUES
(8, 'WHITE', 20, 'M', 1),
(9, 'BLUE', 20, 'S', 2),
(10, 'BROWN', 20, 'S', 2),
(11, 'GREEN', 20, 'S', 2),
(12, 'BLUE', 30, 'M', 2),
(13, 'BROWN', 30, 'M', 2),
(14, 'GREEN', 30, 'M', 2),
(15, 'BLUE', 10, 'L', 2),
(16, 'BROWN', 10, 'L', 2),
(17, 'GREEN', 10, 'L', 2),
(18, 'BLUE', 10, 'XL', 2),
(19, 'BROWN', 10, 'XL', 2),
(20, 'GREEN', 10, 'XL', 2),
(21, 'BLUE', 10, 'XXL', 2),
(22, 'BROWN', 10, 'XXL', 2),
(23, 'GREEN', 10, 'XXL', 2),
(24, 'PINK', 20, 'S', 3),
(25, 'PINK', 40, 'M', 3),
(26, 'PINK', 40, 'L', 3),
(27, 'PINK', 20, 'XL', 3),
(28, 'GREEN', 20, 'S', 4),
(29, 'BROWN', 20, 'S', 4),
(30, 'GREEN', 30, 'M', 4),
(31, 'BROWN', 30, 'M', 4),
(32, 'GREEN', 30, 'L', 4),
(33, 'BROWN', 30, 'L', 4),
(34, 'GREEN', 20, 'XL', 4),
(35, 'BROWN', 20, 'XL', 4),
(36, 'BLUE', 10, 'S', 5),
(37, 'BLUE', 10, 'M', 5),
(38, 'BLUE', 10, 'L', 5),
(39, 'BLUE', 10, 'XL', 5),
(40, 'BLUE', 10, 'XXL', 5),
(41, 'GREEN', 10, 'S', 6),
(42, 'BLACK', 10, 'S', 6),
(43, 'MULTI_COLOR', 10, 'S', 6),
(44, 'GREEN', 10, 'M', 6),
(45, 'BLACK', 10, 'M', 6),
(46, 'MULTI_COLOR', 10, 'M', 6),
(47, 'GREEN', 10, 'L', 6),
(48, 'BLACK', 10, 'L', 6),
(49, 'MULTI_COLOR', 10, 'L', 6),
(50, 'GREEN', 10, 'XL', 6),
(51, 'BLACK', 10, 'XL', 6),
(52, 'MULTI_COLOR', 10, 'XL', 6),
(53, 'GREEN', 10, '5', 7),
(54, 'WHITE', 10, '5', 7),
(55, 'BLACK', 10, '5', 7),
(56, 'GREEN', 10, '6', 7),
(57, 'WHITE', 10, '6', 7),
(58, 'BLACK', 10, '6', 7),
(59, 'GREEN', 10, '7', 7),
(60, 'WHITE', 10, '7', 7),
(61, 'BLACK', 10, '7', 7),
(62, 'GREEN', 10, '8', 7),
(63, 'WHITE', 10, '8', 7),
(64, 'BLACK', 10, '8', 7),
(65, 'GREEN', 10, '9', 7),
(66, 'WHITE', 10, '9', 7),
(67, 'BLACK', 10, '9', 7),
(68, 'WHITE', 10, '5.5', 8),
(69, 'BLUE', 10, '5.5', 8),
(70, 'WHITE', 10, '6.5', 8),
(71, 'BLUE', 20, '6.5', 8),
(72, 'WHITE', 10, '7.5', 8),
(73, 'BLUE', 10, '7.5', 8),
(74, 'BLACK', 10, '7', 9),
(75, 'BLACK', 10, '7.5', 9),
(76, 'BLACK', 10, '8', 9),
(77, 'BLACK', 10, '8.5', 9),
(78, 'BROWN', 10, '7', 10),
(79, 'BLACK', 10, '7', 10),
(80, 'BROWN', 20, '7.5', 10),
(81, 'BLACK', 20, '7.5', 10),
(82, 'BROWN', 20, '8', 10),
(83, 'BLACK', 20, '8', 10),
(84, 'BROWN', 5, '9', 10),
(85, 'BLACK', 5, '9', 10),
(86, 'BLACK', 10, '7', 11),
(87, 'BROWN', 10, '7', 11),
(88, 'BLACK', 10, '7.5', 11),
(89, 'BROWN', 10, '7.5', 11),
(90, 'BLACK', 10, '8', 11),
(91, 'BROWN', 5, '8', 11),
(92, 'BLUE', 20, 'S', 12),
(93, 'PINK', 20, 'S', 12),
(94, 'BLUE', 5, 'M', 12),
(95, 'PINK', 5, 'M', 12),
(96, 'BLUE', 10, 'L', 12),
(97, 'PINK', 10, 'L', 12),
(98, 'BLUE', 15, 'XL', 12),
(99, 'PINK', 15, 'XL', 12),
(100, 'GREY', 5, 'S', 13),
(101, 'BLACK', 5, 'S', 13),
(102, 'PURPLE', 5, 'S', 13),
(103, 'GREY', 5, 'M', 13),
(104, 'BLACK', 5, 'M', 13),
(105, 'PURPLE', 5, 'M', 13),
(106, 'GREY', 5, 'L', 13),
(107, 'BLACK', 5, 'L', 13),
(108, 'PURPLE', 5, 'L', 13),
(109, 'GREY', 5, 'XL', 13),
(110, 'BLACK', 5, 'XL', 13),
(111, 'PURPLE', 5, 'XL', 13),
(112, 'BLUE', 20, 'M(10)', 14),
(113, 'PINK', 20, 'M(10)', 14),
(114, 'BLUE', 20, 'L(12)', 14),
(115, 'PINK', 20, 'L(12)', 14),
(116, 'BROWN', 10, 'M (10)', 15),
(117, 'BLUE', 10, 'M (10)', 15),
(118, 'BROWN', 10, 'L (12)', 15),
(119, 'BLUE', 9, 'L (12)', 15),
(120, 'BROWN', 9, 'XL (14/16)', 15),
(121, 'BLUE', 9, 'XL (14/16)', 15),
(122, 'BLUE', 10, 'S', 16),
(123, 'GREEN', 8, 'S', 16),
(124, 'BLUE', 8, 'M', 16),
(125, 'GREEN', 8, 'M', 16),
(126, 'BLUE', 8, 'L', 16),
(127, 'GREEN', 8, 'L', 16),
(128, 'BLUE', 8, 'XL', 16),
(129, 'GREEN', 0, 'XL', 16),
(130, 'BLACK', 0, 'S', 17),
(131, 'GREEN', 0, 'S', 17),
(132, 'BLUE', 0, 'S', 17),
(133, 'BLACK', 5, 'M', 17),
(134, 'GREEN', 5, 'M', 17),
(135, 'BLUE', 5, 'M', 17),
(136, 'BLACK', 5, 'L', 17),
(137, 'GREEN', 5, 'L', 17),
(138, 'BLUE', 5, 'L', 17),
(139, 'BLACK', 5, 'XL', 17),
(140, 'GREEN', 5, 'XL', 17),
(141, 'BLUE', 5, 'XL', 17),
(142, 'BLUE', 0, '4', 18),
(143, 'RED', 0, '4', 18),
(144, 'PINK', 0, '4', 18),
(145, 'BLUE', 0, '5', 18),
(146, 'RED', 0, '5', 18),
(147, 'PINK', 0, '5', 18),
(148, 'BLUE', 10, '6', 18),
(149, 'RED', 10, '6', 18),
(150, 'PINK', 10, '6', 18),
(151, 'BLUE', 5, '7', 18),
(152, 'RED', 5, '7', 18),
(153, 'PINK', 5, '7', 18),
(154, 'GREY', 0, '4', 19),
(155, 'PINK', 0, '4', 19),
(156, 'BLUE', 0, '4', 19),
(157, 'GREEN', 0, '4', 19),
(158, 'GREY', 5, '5', 19),
(159, 'PINK', 5, '5', 19),
(160, 'BLUE', 5, '5', 19),
(161, 'GREEN', 5, '5', 19),
(162, 'GREY', 5, '6', 19),
(163, 'PINK', 5, '6', 19),
(164, 'BLUE', 5, '6', 19),
(165, 'GREEN', 5, '6', 19),
(166, 'GREY', 5, '7', 19),
(167, 'PINK', 5, '7', 19),
(168, 'BLUE', 5, '7', 19),
(169, 'GREEN', 5, '7', 19),
(170, 'GREY', 0, '8', 19),
(171, 'PINK', 0, '8', 19),
(172, 'BLUE', 0, '8', 19),
(173, 'GREEN', 0, '8', 19),
(174, 'MULTI_COLOR', 10, 'S', 20),
(175, 'PURPLE', 10, 'S', 20),
(176, 'PINK', 10, 'S', 20),
(177, 'MULTI_COLOR', 10, 'XS (6)', 20),
(178, 'PURPLE', 5, 'XS (6)', 20),
(179, 'PINK', 5, 'XS (6)', 20),
(180, 'MULTI_COLOR', 5, 'S (7/8)', 20),
(181, 'PURPLE', 5, 'S (7/8)', 20),
(182, 'PINK', 0, 'S (7/8)', 20),
(183, 'MULTI_COLOR', 5, 'M (10)', 20),
(184, 'PURPLE', 5, 'M (10)', 20),
(185, 'PINK', 5, 'M (10)', 20),
(186, 'MULTI_COLOR', 5, 'L (12)', 20),
(187, 'PURPLE', 0, 'L (12)', 20),
(188, 'PINK', 0, 'L (12)', 20),
(189, 'BLUE', 10, '10', 21),
(190, 'BLUE', 10, '11', 21),
(191, 'BLUE', 10, '12', 21),
(192, 'BLUE', 0, '13', 21);















INSERT INTO `stock_change_history` (`id`, `created_at`, `quantity`, `quantity_left`, `type`, `item_id`) VALUES
(1, '2025-01-04 02:55:01.602000', 20, 20, 'INCREASE', 1);
INSERT INTO `stock_change_history` (`id`, `created_at`, `quantity`, `quantity_left`, `type`, `item_id`) VALUES
(2, '2025-01-04 02:55:11.937000', 60, 80, 'INCREASE', 1);
INSERT INTO `stock_change_history` (`id`, `created_at`, `quantity`, `quantity_left`, `type`, `item_id`) VALUES
(3, '2025-01-04 02:59:16.588000', 240, 240, 'INCREASE', 2);
INSERT INTO `stock_change_history` (`id`, `created_at`, `quantity`, `quantity_left`, `type`, `item_id`) VALUES
(4, '2025-01-04 03:01:31.780000', 120, 120, 'INCREASE', 3),
(5, '2025-01-04 03:03:33.742000', 200, 200, 'INCREASE', 4),
(6, '2025-01-04 03:05:23.576000', 50, 50, 'INCREASE', 5),
(7, '2025-01-04 03:07:33.258000', 120, 120, 'INCREASE', 6),
(8, '2025-01-04 03:10:51.278000', 150, 150, 'INCREASE', 7),
(9, '2025-01-04 03:12:45.260000', 70, 70, 'INCREASE', 8),
(10, '2025-01-04 03:14:25.665000', 40, 40, 'INCREASE', 9),
(11, '2025-01-04 03:17:03.324000', 110, 110, 'INCREASE', 10),
(12, '2025-01-04 03:18:54.264000', 55, 55, 'INCREASE', 11),
(13, '2025-01-04 03:21:24.918000', 100, 100, 'INCREASE', 12),
(14, '2025-01-04 03:24:50.996000', 60, 60, 'INCREASE', 13),
(15, '2025-01-04 03:27:03.368000', 80, 80, 'INCREASE', 14),
(16, '2025-01-04 03:29:07.797000', 57, 57, 'INCREASE', 15),
(17, '2025-01-04 03:31:02.916000', 58, 58, 'INCREASE', 16),
(18, '2025-01-04 03:32:45.230000', 45, 45, 'INCREASE', 17),
(19, '2025-01-04 03:35:12.305000', 45, 45, 'INCREASE', 18),
(20, '2025-01-04 03:37:33.423000', 60, 60, 'INCREASE', 19),
(21, '2025-01-04 03:39:39.943000', 80, 80, 'INCREASE', 20),
(22, '2025-01-04 03:41:50.322000', 30, 30, 'INCREASE', 21);







INSERT INTO `user` (`id`, `address`, `dob`, `email`, `image`, `male`, `name`, `phone`, `user_auth_id`) VALUES
(1, 'TPHC M', '21/12/2003', NULL, 'https://firebasestorage.googleapis.com/v0/b/fashion-shop-94e75.appspot.com/o/Default%2Fdefault-avatar.png?alt=media1', 1, 'Admin name', '0123456789', 1);
INSERT INTO `user` (`id`, `address`, `dob`, `email`, `image`, `male`, `name`, `phone`, `user_auth_id`) VALUES
(2, 'TPHCM', '12/12/2000', NULL, 'https://firebasestorage.googleapis.com/v0/b/fashion-shop-94e75.appspot.com/o/Default%2Fdefault-avatar.png?alt=media', 1, 'user', NULL, 2);
INSERT INTO `user` (`id`, `address`, `dob`, `email`, `image`, `male`, `name`, `phone`, `user_auth_id`) VALUES
(3, 'TPHCM', '12/12/2000', NULL, 'https://firebasestorage.googleapis.com/v0/b/fashion-shop-94e75.appspot.com/o/Default%2Fdefault-avatar.png?alt=media', 1, 'user', NULL, 3);
INSERT INTO `user` (`id`, `address`, `dob`, `email`, `image`, `male`, `name`, `phone`, `user_auth_id`) VALUES
(4, 'TPHCM 1', '12/12/2001', NULL, 'https://firebasestorage.googleapis.com/v0/b/fashion-shop-94e75.appspot.com/o/Default%2Fdefault-avatar.png?alt=media 1', 1, 'user 1', NULL, 5),
(5, 'TPHCM 1', '12/12/2001', 'user1@gmail.com', 'https://firebasestorage.googleapis.com/v0/b/fashion-shop-94e75.appspot.com/o/Default%2Fdefault-avatar.png?alt=media1', 0, 'user 1', NULL, 6);

INSERT INTO `user_auth` (`id`, `email`, `is_admin`, `is_deleted`, `is_verified`, `password`, `phone`) VALUES
(1, 'admin@gmail.com', 1, 0, 1, '$2a$10$VRhFqN5Wx8Dhgne.eg1GqOAEF067IKGYVtGqMDXXu8fZPH7fN4w.W', NULL);
INSERT INTO `user_auth` (`id`, `email`, `is_admin`, `is_deleted`, `is_verified`, `password`, `phone`) VALUES
(2, 'user@gmail.com', 0, 0, 1, '$2a$10$.b9TLym3qJQJS/BpaE/AMOkx6y4X4At7aVEhaXtlrDRWygYlR809q', NULL);
INSERT INTO `user_auth` (`id`, `email`, `is_admin`, `is_deleted`, `is_verified`, `password`, `phone`) VALUES
(3, 'userAdmin@gmail.com', 0, 1, 1, '$2a$10$QIjMK9i24GXsrrQChm2VV.OxVixfitnXdqJzjtT2CKehaG6VtktLe', NULL);
INSERT INTO `user_auth` (`id`, `email`, `is_admin`, `is_deleted`, `is_verified`, `password`, `phone`) VALUES
(4, 'a@gmail.com', 0, 1, 0, '$2a$10$QIjMK9i24GXsrrQChm2VV.OxVixfitnXdqJzjtT2CKehaG6VtktLe', NULL),
(5, 'userAdmin2@gmail.com', 0, 1, 1, '$2a$10$U84dtcFRW1lLgXnVPX0VGeQ/OzqZOB/773wYPxagjOJ68/uDScOtO', NULL),
(6, NULL, 0, 0, 1, '$2a$10$x/5F8NfRioeCmEZ8PUh/RurupCEauuKIx7/UHBIUeBpfayIMD7BtO', '0123456789');


/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;