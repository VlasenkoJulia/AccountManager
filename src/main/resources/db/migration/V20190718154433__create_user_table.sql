
CREATE TABLE `user`
(
    `user_name` varchar(255) NOT NULL,
    `email` varchar(255) NOT NULL,
    `password`  varchar(255) NOT NULL,
    `enabled` boolean default 1 NOT NULL,
    `reset_token` CHAR(36),
    PRIMARY KEY (`user_name`),
    UNIQUE (`email`)
) ENGINE = InnoDB