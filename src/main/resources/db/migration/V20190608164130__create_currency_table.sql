CREATE TABLE `currency`
(
    `code` varchar(3) NOT NULL,
    `rate` decimal(6, 2),
    `name` varchar(20),
    `ISO`  varchar(3),
    PRIMARY KEY (`code`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8