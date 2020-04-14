CREATE TABLE `account`
(
    `id`            int(11) NOT NULL AUTO_INCREMENT,
    `number`        varchar(30),
    `currency_code` varchar(3),
    `type`          varchar(20),
    `balance`       decimal(10, 2) DEFAULT '0.00',
    `open_date`     timestamp ,
    `client_id`     int(11),
    PRIMARY KEY (`id`),
    KEY `client_id_fk` (`client_id`),
    KEY `currency_code_fk` (`currency_code`),
    CONSTRAINT `client_id_fk` FOREIGN KEY (`client_id`) REFERENCES `client` (`id`),
    CONSTRAINT `currency_code_fk` FOREIGN KEY (`currency_code`) REFERENCES `currency` (`code`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 31
  DEFAULT CHARSET = utf8
