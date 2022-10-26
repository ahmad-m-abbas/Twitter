CREATE TABLE `friends`
(
    firstUser varchar(36) NULL,
    secondUser varchar(36) NOT NULL,
    PRIMARY KEY (firstUser, secondUser),
    FOREIGN KEY (firstUser) REFERENCES user(id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (secondUser) REFERENCES `user`(id) ON DELETE CASCADE ON UPDATE CASCADE
);