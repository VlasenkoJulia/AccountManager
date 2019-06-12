CREATE TABLE `account_cards`
(
    `account_id` int(11),
    `card_id`    int(11),
    KEY `account_id_fk` (`account_id`),
    KEY `card_id_fk` (`card_id`),
    CONSTRAINT `account_id_fk` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`),
    CONSTRAINT `card_id_fk` FOREIGN KEY (`card_id`) REFERENCES `card` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8