 DROP TABLE IF EXISTS `stock_order`;
 23 /*!40101 SET @saved_cs_client     = @@character_set_client */;
 24 /*!40101 SET character_set_client = utf8 */;
 25 CREATE TABLE `stock_order` (
 26   `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
 27   `sid` int(11) NOT NULL COMMENT '库存ID',
 28   `name` varchar(30) NOT NULL DEFAULT '' COMMENT '商品名称',
 29   `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
 30   PRIMARY KEY (`id`)
 31 ) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
 32 
 40 INSERT INTO `stock_order` VALUES (1,2,'手机','2021-04-10 04:14:38'),(2,2,'手机','2021-04-10 04:15:47'),(3,2,'手机','2021-04-10 09:56:43'),(4,2,'手机','2021-04-10 09:57:49'),(5,2,'手机',    '2021-04-10 10:04:56'),(6,2,'手机','2021-04-10 10:05:41');