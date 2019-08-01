
CREATE TABLE `user`
(
    `user_name` varchar(255) NOT NULL,
    `password`  varchar(255) NOT NULL,
    `enabled` boolean default 1 NOT NULL,
    PRIMARY KEY (`user_name`)
) ENGINE = InnoDB