 DROP TABLE IF EXISTS `stock`;
 23 /*!40101 SET @saved_cs_client     = @@character_set_client */;
 24 /*!40101 SET character_set_client = utf8 */;
 25 CREATE TABLE `stock` (
 26   `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
 27   `name` varchar(50) NOT NULL DEFAULT '' COMMENT '名称',
 28   `count` int(11) NOT NULL COMMENT '库存',
 29   `sale` int(11) NOT NULL COMMENT '已售',
 30   `version` int(11) NOT NULL COMMENT '乐观锁，版本号',
 31   PRIMARY KEY (`id`)
 32 ) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
 33 
 41 INSERT INTO `stock` VALUES (2,'huawei',44,6,6),(3,'xiaomi',50,0,0);